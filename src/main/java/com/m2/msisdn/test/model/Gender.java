package com.m2.msisdn.test.model;

public enum Gender {
    F("FEMALE", "F"), M("MALE", "M");

    private final String full;
    private final String abbr;

    Gender(String full, String abbr) {
        this.full = full;
        this.abbr = abbr;
    }

    public static Gender findByAbbr(String abbr) {
        for (Gender gender : Gender.values()) {
            if (gender.abbr.equalsIgnoreCase(abbr)) {
                return gender;
            }
        }
        return null;
    }

    public static Gender findByFull(String full) {
        for (Gender gender : Gender.values()) {
            if (gender.full.equalsIgnoreCase(full)) {
                return gender;
            }
        }
        return null;
    }
}
