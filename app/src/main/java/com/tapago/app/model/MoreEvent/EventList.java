package com.tapago.app.model.MoreEvent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventList {

    @SerializedName("organiser_name")
    @Expose
    private String organiserName;
    @SerializedName("organiser_image")
    @Expose
    private String organiserImage;
    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("event_title")
    @Expose
    private String eventTitle;
    @SerializedName("event_description")
    @Expose
    private String eventDescription;
    @SerializedName("event_start_date")
    @Expose
    private String eventStartDate;
    @SerializedName("event_start_date_time_sort")
    @Expose
    private String eventStartDateTimeSort;
    @SerializedName("event_start_date_time")
    @Expose
    private String eventStartDateTime;
    @SerializedName("event_enddate_time_sort")
    @Expose
    private String eventEnddateTimeSort;
    @SerializedName("event_enddate_time")
    @Expose
    private String eventEnddateTime;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("event_budget")
    @Expose
    private String eventBudget;
    @SerializedName("event_venue")
    @Expose
    private String eventVenue;
    @SerializedName("event_address")
    @Expose
    private String eventAddress;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("event_banner_img")
    @Expose
    private String eventBannerImg;
    @SerializedName("event_thumb_img")
    @Expose
    private String eventThumbImg;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;

    public String getEventBudget() {
        return eventBudget;
    }

    public void setEventBudget(String eventBudget) {
        this.eventBudget = eventBudget;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
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

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventStartDateTimeSort() {
        return eventStartDateTimeSort;
    }

    public void setEventStartDateTimeSort(String eventStartDateTimeSort) {
        this.eventStartDateTimeSort = eventStartDateTimeSort;
    }

    public String getEventStartDateTime() {
        return eventStartDateTime;
    }

    public void setEventStartDateTime(String eventStartDateTime) {
        this.eventStartDateTime = eventStartDateTime;
    }

    public String getEventEnddateTimeSort() {
        return eventEnddateTimeSort;
    }

    public void setEventEnddateTimeSort(String eventEnddateTimeSort) {
        this.eventEnddateTimeSort = eventEnddateTimeSort;
    }

    public String getEventEnddateTime() {
        return eventEnddateTime;
    }

    public void setEventEnddateTime(String eventEnddateTime) {
        this.eventEnddateTime = eventEnddateTime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

}