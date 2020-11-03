package com.tapago.app.model.PaymentHistoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("event_title")
    @Expose
    private String eventTitle;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("voucher_id")
    @Expose
    private Integer voucherId;
    @SerializedName("payment_amount")
    @Expose
    private Double paymentAmount;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("payment_for")
    @Expose
    private String paymentFor;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentFor() {
        return paymentFor;
    }

    public void setPaymentFor(String paymentFor) {
        this.paymentFor = paymentFor;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}