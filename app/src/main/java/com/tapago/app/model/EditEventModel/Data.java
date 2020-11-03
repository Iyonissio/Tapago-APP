package com.tapago.app.model.EditEventModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("event_title")
    @Expose
    private String eventTitle;
    @SerializedName("event_description")
    @Expose
    private String eventDescription;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("organiser_name")
    @Expose
    private String organiserName;
    @SerializedName("organiser_image")
    @Expose
    private String organiserImage;
    @SerializedName("event_start_date_time")
    @Expose
    private String eventStartDateTime;
    @SerializedName("event_enddate_time")
    @Expose
    private String eventEnddateTime;
    @SerializedName("event_budget")
    @Expose
    private String eventBudget;
    @SerializedName("event_venue")
    @Expose
    private String eventVenue;
    @SerializedName("event_address")
    @Expose
    private String eventAddress;
    @SerializedName("event_banner_img")
    @Expose
    private String eventBannerImg;
    @SerializedName("event_thumb_img")
    @Expose
    private String eventThumbImg;
    @SerializedName("voucher")
    @Expose
    private List<Voucher> voucher = null;
    @SerializedName("ticket")
    @Expose
    private List<Ticket> ticket = null;
    @SerializedName("vendor_list")
    @Expose
    private List<VendorList> vendorList = null;
    @SerializedName("event_fee_message")
    @Expose
    private String eventFeeMessage;
    @SerializedName("event_fee")
    @Expose
    private String eventFee;

    public String getEventFee() {
        return eventFee;
    }

    public void setEventFee(String eventFee) {
        this.eventFee = eventFee;
    }

    public String getEventFeeMessage() {
        return eventFeeMessage;
    }

    public void setEventFeeMessage(String eventFeeMessage) {
        this.eventFeeMessage = eventFeeMessage;
    }

    public List<VendorList> getVendorList() {
        return vendorList;
    }

    public void setVendorList(List<VendorList> vendorList) {
        this.vendorList = vendorList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOrganiserName() {
        return organiserName;
    }

    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }

    public String getOrganiserImage() {
        return organiserImage;
    }

    public void setOrganiserImage(String organiserImage) {
        this.organiserImage = organiserImage;
    }

    public String getEventStartDateTime() {
        return eventStartDateTime;
    }

    public void setEventStartDateTime(String eventStartDateTime) {
        this.eventStartDateTime = eventStartDateTime;
    }

    public String getEventEnddateTime() {
        return eventEnddateTime;
    }

    public void setEventEnddateTime(String eventEnddateTime) {
        this.eventEnddateTime = eventEnddateTime;
    }

    public String getEventBudget() {
        return eventBudget;
    }

    public void setEventBudget(String eventBudget) {
        this.eventBudget = eventBudget;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventBannerImg() {
        return eventBannerImg;
    }

    public void setEventBannerImg(String eventBannerImg) {
        this.eventBannerImg = eventBannerImg;
    }

    public String getEventThumbImg() {
        return eventThumbImg;
    }

    public void setEventThumbImg(String eventThumbImg) {
        this.eventThumbImg = eventThumbImg;
    }

    public List<Voucher> getVoucher() {
        return voucher;
    }

    public void setVoucher(List<Voucher> voucher) {
        this.voucher = voucher;
    }

    public List<Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(List<Ticket> ticket) {
        this.ticket = ticket;
    }
}