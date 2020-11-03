package com.tapago.app.model.TicketModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TicketModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("event_fee_message")
    @Expose
    private String eventFeeMessage;
    @SerializedName("event_fee")
    @Expose
    private String eventFee;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getEventFeeMessage() {
        return eventFeeMessage;
    }

    public void setEventFeeMessage(String eventFeeMessage) {
        this.eventFeeMessage = eventFeeMessage;
    }

    public String getEventFee() {
        return eventFee;
    }

    public void setEventFee(String eventFee) {
        this.eventFee = eventFee;
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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}