package com.w3dartsdk.model;

public class DeviceData {
    private String key;
    private String value;

    public DeviceData(String paramKey, String paramValue) {
        key = paramKey;
        value = paramValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
