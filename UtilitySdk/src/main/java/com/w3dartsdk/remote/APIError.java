package com.w3dartsdk.remote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
    This is the common class that is used when we get error from api
 */
public class APIError {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public APIError() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}