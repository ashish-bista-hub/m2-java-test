package com.m2.msisdn.test.utils;

import com.google.i18n.phonenumbers.CountryCodeToRegionCodeMap;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.m2.msisdn.test.model.Gender;
import com.m2.msisdn.test.model.Msisdn;
import com.m2.msisdn.test.model.MsisdnCsvDto;
import com.m2.msisdn.test.model.Rejected;
import com.m2.msisdn.test.model.SimType;
import com.m2.msisdn.test.model.Validate;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static Validate validateMsisdn(List<MsisdnCsvDto> msisdnCsvDtos) {
        final List<Msisdn> msisdns = new ArrayList<>();
        final List<Rejected> rejecteds = new ArrayList<>();
        for (final MsisdnCsvDto d : msisdnCsvDtos) {
            if (StringUtils.isEmpty(d.getMsisdn()) || StringUtils.isEmpty(d.getAddress()) || StringUtils.isEmpty(d.getDob()) ||
                    StringUtils.isEmpty(d.getGender()) || StringUtils.isEmpty(d.getIdNo()) || StringUtils.isEmpty(d.getName()) ||
                    StringUtils.isEmpty(d.getSimType())) {
                d.setRejectedReason("Empty or Null property");
                rejecteds.add(getRejected(d));
                continue;
            }
            final String formattedMsisdn = getFormattedMsisdn(d.getMsisdn());
            if (StringUtils.isEmpty(formattedMsisdn)) {
                d.setRejectedReason("MSISDN format error");
                continue;
            }
            final Date dob = getDob(d);
            if (!isValidName(d) || !isValidDob(d, dob) || !isValidGender(d) || !isValidAddress(d) || !isValidIdNo(d) || !isValidSimType(d)) {
                rejecteds.add(getRejected(d));
                continue;
            }
            if (!containsMsisdn(msisdns, formattedMsisdn)) {
                final Msisdn msisdn = new Msisdn();
                msisdn.setMsisdn(formattedMsisdn);
                msisdn.setName(d.getName());
                msisdn.setDob(dob);
                msisdn.setGender(d.getGender().length() > 1 ? Gender.findByFull(d.getGender()) : Gender.findByAbbr(d.getGender()));
                msisdn.setAddress(d.getAddress());
                msisdn.setIdNo(d.getIdNo());
                msisdn.setSimType(SimType.find(d.getSimType()));
                msisdns.add(msisdn);
            } else {
                d.setRejectedReason("Duplicate MSISDN entry");
                rejecteds.add(getRejected(d));
            }
        }
        final Validate validate = new Validate();
        validate.setInvalids(rejecteds);
        validate.setValids(msisdns);
        return validate;
    }

    private static Rejected getRejected(MsisdnCsvDto d) {
        return new Rejected(d.getMsisdn(), d.getSimType(), d.getName(), d.getDob(), d.getGender(), d.getAddress(),
                d.getIdNo(), d.getRejectedReason());
    }

    private static boolean containsMsisdn(final List<Msisdn> list, final String msisdn) {
        return list.stream().map(Msisdn::getMsisdn).anyMatch(msisdn::equalsIgnoreCase);
    }

    private static String getFormattedMsisdn(String msisdn) {
        try {
            final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            final Phonenumber.PhoneNumber numberProto = phoneUtil.parse(msisdn, msisdn.startsWith("0091") || msisdn.startsWith("+91") ? "IN" : "NP");
            return phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            return null;
        }
    }

    private static boolean isValidIdNo(MsisdnCsvDto dto) {
        for (int i = 0; i < dto.getIdNo().length(); i++) {
            if (!Character.isLetterOrDigit(dto.getIdNo().charAt(i))) {
                dto.setRejectedReason("Invalid Id No");
                return false;
            }
        }
        return true;
    }

    private static boolean isValidAddress(MsisdnCsvDto dto) {
        final boolean valid = dto.getAddress().length() >= 20;
        if (!valid) {
            dto.setRejectedReason("Invalid Address");
        }
        return valid;
    }

    private static boolean isValidGender(MsisdnCsvDto dto) {
        final boolean valid = Stream.of("M", "F", "Male", "Female").anyMatch(g -> g.equalsIgnoreCase(dto.getGender()));
        if (!valid) {
            dto.setRejectedReason("Invalid Gender");
        }
        return valid;
    }

    private static boolean isValidSimType(MsisdnCsvDto dto) {
        final boolean valid = Stream.of("Postpaid", "Prepaid").anyMatch(st -> st.equalsIgnoreCase(dto.getSimType()));
        if (!valid) {
            dto.setRejectedReason("Invalid Sim Type");
        }
        return valid;
    }

    private static boolean isValidDob(MsisdnCsvDto dto, Date dob) {
        final Date today = new Date();
        final boolean valid = null != dob && !dob.after(today) && !dob.equals(today);
        if (!valid) {
            dto.setRejectedReason("Invalid Date of Birth");
        }
        return valid;
    }

    private static Date getDob(MsisdnCsvDto dto) {
        try {
            final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            return formatter.parse(dto.getDob());
        } catch (ParseException e) {
            dto.setRejectedReason("Invalid DOB format");
            return null;
        }
    }

    private static boolean isValidName(MsisdnCsvDto dto) {
        final String specialCharacters = " !#$%&'()*+,-./:;<=>?@[]^_`{|}~0123456789";
        final String str2[] = dto.getName().split(" ");
        for (int i = 0; i < str2.length; i++) {
            if (specialCharacters.contains(str2[i])) {
                dto.setRejectedReason("Invalid Name");
                return false;
            }
        }
        return true;
    }
}
