package com.tapago.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response_url")
    @Expose
    private String responseurl;
    @SerializedName("payment_uuid")
    @Expose
    private String paymentuuid;

    public String getPaymentuuid() {
        return paymentuuid;
    }

    public void setPaymentuuid(String paymentuuid) {
        this.paymentuuid = paymentuuid;
    }

    public String getResponseurl() {
        return responseurl;
    }

    public void setResponseurl(String responseurl) {
        this.responseurl = responseurl;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}