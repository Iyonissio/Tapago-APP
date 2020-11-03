package com.tapago.app.model.UserTicketQr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("requested_date")
    @Expose
    private String requestedDate;
    @SerializedName("requested_qty")
    @Expose
    private Integer requestedQty;
    @SerializedName("total_amount")
    @Expose
    private Integer totalAmount;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("event_title")
    @Expose
    private String eventTitle;
    @SerializedName("event_start_date")
    @Expose
    private String eventStartDate;
    @SerializedName("event_start_time")
    @Expose
    private String eventStartTime;
    @SerializedName("event_end_date")
    @Expose
    private String eventEndDate;
    @SerializedName("event_end_time")
    @Expose
    private String eventEndTime;
    @SerializedName("ticket_category_name")
    @Expose
    private String ticketCategoryName;
    @SerializedName("request_ticket_status")
    @Expose
    private String requestTicketStatus;
    @SerializedName("ticket_code")
    @Expose
    private Integer ticketCode;
    @SerializedName("qrcode")
    @Expose
    private String qrcode;
    @SerializedName("request_status")
    @Expose
    private String requestStatus;
    @SerializedName("total_redeem_amount")
    @Expose
    private Integer totalRedeemAmount;
    @SerializedName("remaining_amount")
    @Expose
    private Integer remainingAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Integer getRequestedQty() {
        return requestedQty;
    }

    public void setRequestedQty(Integer requestedQty) {
        this.requestedQty = requestedQty;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getTicketCategoryName() {
        return ticketCategoryName;
    }

    public void setTicketCategoryName(String ticketCategoryName) {
        this.ticketCategoryName = ticketCategoryName;
    }

    public String getRequestTicketStatus() {
        return requestTicketStatus;
    }

    public void setRequestTicketStatus(String requestTicketStatus) {
        this.requestTicketStatus = requestTicketStatus;
    }

    public Integer getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(Integer ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Integer getTotalRedeemAmount() {
        return totalRedeemAmount;
    }

    public void setTotalRedeemAmount(Integer totalRedeemAmount) {
        this.totalRedeemAmount = totalRedeemAmount;
    }

    public Integer getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Integer remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

}