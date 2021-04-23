package com.m2.msisdn.test.service.impl;

import com.m2.msisdn.test.M2MsisdnApplication;
import com.m2.msisdn.test.model.Gender;
import com.m2.msisdn.test.model.Msisdn;
import com.m2.msisdn.test.model.MsisdnCsvDto;
import com.m2.msisdn.test.model.Rejected;
import com.m2.msisdn.test.model.Response;
import com.m2.msisdn.test.model.Validate;
import com.m2.msisdn.test.repository.MsisdnRepository;
import com.m2.msisdn.test.repository.RejectedRepository;
import com.m2.msisdn.test.service.MsisdnService;
import com.m2.msisdn.test.utils.ValidationUtil;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.List;

@Service
public class MsisdnServiceImpl implements MsisdnService {

    private static final Logger log = LoggerFactory.getLogger(M2MsisdnApplication.class);
    private static final String LINE_SEPARATOR = "line.separator";

    @Autowired
    private MsisdnRepository msisdnRepository;

    @Autowired
    private RejectedRepository rejectedRepository;

    @Override
    public List<Msisdn> getAll() {
        return msisdnRepository.findAll();
    }

    @Override
    public Msisdn getByMsisdn(String msisdn) {
        return msisdnRepository.findByMsisdn(msisdn);
    }

    @Override
    public void loadFile(Writer writer) {
        final List<Msisdn> msisdnList = msisdnRepository.findAll();
        if (CollectionUtils.isEmpty(msisdnList)) {
            throw new RuntimeException("MSISDN Records not found. Please upload the file first");
        }
        final String[] csvHeader = {"MSISDN", "SIMTYPE", "NAME", "DOB", "GENDER", "ADDRESS", "IDNO"};
        msisdnToText(writer, msisdnList, csvHeader);
    }

    @Override
    public void loadRejectedFile(Writer writer) {
        final List<Rejected> rejectedList = rejectedRepository.findAll();
        if (CollectionUtils.isEmpty(rejectedList)) {
            throw new RuntimeException("Rejected MSISDN Records not found. Please upload the file first");
        }
        final String[] csvHeader = {"MSISDN", "SIMTYPE", "NAME", "DOB", "GENDER", "ADDRESS", "IDNO", "REJECTEDREASON"};
        rejectedToText(writer, rejectedList, csvHeader);
    }

    @Override
    @Transactional
    public Response processCsvFile(InputStream file) {
        final Response response = new Response();
        final List<MsisdnCsvDto> msisdnCsvDtos = parseMsisdn(file);
        if (CollectionUtils.isEmpty(msisdnCsvDtos)) {
            log.error("Unable to parse and read CSV file.");
            response.setErrorMessage("Unable to parse and read CSV file.");
            return response;
        }
        final Validate validate = ValidationUtil.validateMsisdn(msisdnCsvDtos);
        if (CollectionUtils.isEmpty(validate.getInvalids()) && CollectionUtils.isEmpty(validate.getValids())) {
            log.error("Not anything to process at the moment.");
            response.setErrorMessage("Not anything to process at the moment.");
            return response;
        }
        if (!CollectionUtils.isEmpty(validate.getValids())) {
            msisdnRepository.deleteAllEntries();
            msisdnRepository.saveAll(validate.getValids());
            sendSms(validate.getValids());
            final List<Msisdn> saved = getAll();
            response.setData(saved);
            response.getMeta().put("Total Record Count", saved.size());
        }
        if (!CollectionUtils.isEmpty(validate.getInvalids())) {
            rejectedRepository.deleteAllEntries();
            rejectedRepository.saveAll(validate.getInvalids());
            printRejected(validate.getInvalids());
            response.getMeta().put("Total Rejected Entries", rejectedRepository.findAll().size());
        }
        return response;
    }

    private void printRejected(List<Rejected> rejectedList) {
        log.info("************ Rejected Entries *************");
        rejectedList.forEach(r -> {
            log.info(r.toString());
            log.info("\n");
        });
        log.info("************* Completed ***************");
    }

    private void sendSms(List<Msisdn> msisdnList) {
        log.info("************ Sending SMS *************");
        msisdnList.forEach(m -> {
            final StringBuilder builder = new StringBuilder("Hello ");
            if (Gender.M == m.getGender()) {
                builder.append("Mr. ");
            } else {
                builder.append("Ms. ");
            }
            builder.append(m.getName()).append(",").append(System.getProperty(LINE_SEPARATOR)).append("   Thank you for subscribing to M2 services.");
            builder.append(System.getProperty(LINE_SEPARATOR)).append("With Regards,").append(System.getProperty(LINE_SEPARATOR));
            builder.append("M2 customer support");
            log.info(builder.toString());
            log.info("\n");
        });
        log.info("************* SMS Completed ***************");
    }

    private List<MsisdnCsvDto> parseMsisdn(InputStream file) {
        return new CsvToBeanBuilder(new InputStreamReader(file)).withType(MsisdnCsvDto.class).withSkipLines(1)
                .build().parse();
    }

    private void msisdnToText(Writer writer, List<Msisdn> msisdnList, String[] csvHeader) {
        try {
            final ColumnPositionMappingStrategy<Msisdn> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(Msisdn.class);
            mappingStrategy.setColumnMapping(csvHeader);
            final StatefulBeanToCsv<Msisdn> beanToCsv = new StatefulBeanToCsvBuilder<Msisdn>(writer)
                    .withMappingStrategy(mappingStrategy)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            beanToCsv.write(msisdnList);
        } catch (Exception e) {
            log.error("Error writing txt error {}", e);
        }
    }

    private void rejectedToText(Writer writer, List<Rejected> msisdnList, String[] csvHeader) {
        try {
            final ColumnPositionMappingStrategy<Rejected> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(Rejected.class);
            mappingStrategy.setColumnMapping(csvHeader);
            final StatefulBeanToCsv<Rejected> beanToCsv = new StatefulBeanToCsvBuilder<Rejected>(writer)
                    .withMappingStrategy(mappingStrategy)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            beanToCsv.write(msisdnList);
        } catch (Exception e) {
            log.error("Error writing txt error {}", e);
        }
    }
}
