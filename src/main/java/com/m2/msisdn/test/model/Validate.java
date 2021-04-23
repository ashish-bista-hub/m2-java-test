package com.m2.msisdn.test.model;

import java.util.List;

public class Validate {

    private List<Rejected> invalids;
    private List<Msisdn> valids;

    public List<Rejected> getInvalids() {
        return invalids;
    }

    public void setInvalids(List<Rejected> invalids) {
        this.invalids = invalids;
    }

    public List<Msisdn> getValids() {
        return valids;
    }

    public void setValids(List<Msisdn> valids) {
        this.valids = valids;
    }
}
