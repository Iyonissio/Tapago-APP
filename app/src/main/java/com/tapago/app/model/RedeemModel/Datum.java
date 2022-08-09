package com.tapago.app.model.RedeemModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("typ")
    @Expose
    private String typ;
    @SerializedName("redeem_by")
    @Expose
    private Integer redeemBy;
    @SerializedName("requested_qty")
    @Expose
    private Integer requestedQty;
    @SerializedName("redeem_date")
    @Expose
    private String redeemDate;
    @SerializedName("event_title")
    @Expose
    private String eventTitle;
    @SerializedName("ticket_category_name")
    @Expose
    private String ticketCategoryName;
    @SerializedName("created_by")
    @Expose
    private String userName;
    @SerializedName("ticket_code")
    @Expose
    private String voucherCode;
    @SerializedName("amount")
    @Expose
    private Integer redeemAmount;

    public Integer getRedeemAmount() {
        return redeemAmount;
    }

    public void setRedeemAmount(Integer redeemAmount) {
        this.redeemAmount = redeemAmount;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public Integer getRedeemBy() {
        return redeemBy;
    }

    public void setRedeemBy(Integer redeemBy) {
        this.redeemBy = redeemBy;
    }

    public Integer getRequestedQty() {
        return requestedQty;
    }

    public void setRequestedQty(Integer requestedQty) {
        this.requestedQty = requestedQty;
    }

    public String getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(String redeemDate) {
        this.redeemDate = redeemDate;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getTicketCategoryName() {
        return ticketCategoryName;
    }

    public void setTicketCategoryName(String ticketCategoryName) {
        this.ticketCategoryName = ticketCategoryName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}