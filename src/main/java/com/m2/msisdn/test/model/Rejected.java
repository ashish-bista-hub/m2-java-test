package com.m2.msisdn.test.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Rejected {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String msisdn;
    private String simType;
    private String name;
    private String dob;
    private String gender;
    private String address;
    private String idNo;
    private String rejectedReason;

    public Rejected() {
    }

    public Rejected(String msisdn, String simType, String name, String dob, String gender, String address, String idNo, String rejectedReason) {
        this.msisdn = msisdn;
        this.simType = simType;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.idNo = idNo;
        this.rejectedReason = rejectedReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSimType() {
        return simType;
    }

    public void setSimType(String simType) {
        this.simType = simType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    @Override
    public String toString() {
        return "Rejected{" +
                "id=" + id +
                ", msisdn='" + msisdn + '\'' +
                ", simType='" + simType + '\'' +
                ", name='" + name + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", idNo='" + idNo + '\'' +
                ", rejectedReason='" + rejectedReason + '\'' +
                '}';
    }
}
