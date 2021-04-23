package com.m2.msisdn.test.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Response {
    private Object data;
    private Map<String, Object> meta = new LinkedHashMap<>();
    private String message;
    private String errorMessage;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
