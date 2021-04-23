package com.m2.msisdn.test.service;


import com.m2.msisdn.test.model.Msisdn;
import com.m2.msisdn.test.model.Response;

import java.io.InputStream;
import java.io.Writer;
import java.util.List;

public interface MsisdnService {

    List<Msisdn> getAll();

    Msisdn getByMsisdn(String msisdn);

    Response processCsvFile(InputStream file);

    void loadFile(Writer writer);

    void loadRejectedFile(Writer writer);
}
