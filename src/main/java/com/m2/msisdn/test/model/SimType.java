package com.m2.msisdn.test.model;

public enum SimType {
    PREPAID, POSTPAID;

    public static SimType find(String type) {
        for (SimType simType : SimType.values()) {
            if (simType.name().equalsIgnoreCase(type)) {
                return simType;
            }
        }
        return null;
    }
}
