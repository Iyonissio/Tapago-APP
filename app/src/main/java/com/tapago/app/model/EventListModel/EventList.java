package com.tapago.app.model.EventListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventList {

    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("organiser_name")
    @Expose
    private String organiser_name;
    @SerializedName("organiser_image")
    @Expose
    private String organiset_image;
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
    private String eventDate;
    @SerializedName("event_start_date_time_sort")
    @Expose
    private String eventStartDateTimeShort;
    @SerializedName("event_start_date_time")
    @Expose
    private String eventStartDateTime;
    @SerializedName("event_enddate_time")
    @Expose
    private String eventEnddateTime;
    @SerializedName("created_date")
    @Expose
    private String createDate;
    @SerializedName("created_time")
    @Expose
    private String createTime;
    @SerializedName("event_budget")
    @Expose
    private String  eventBudget;
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
    @SerializedName("city")
    @Expose
    private String cityName;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("event_enddate_time_sort")
    @Expose
    private String eventEndDate;

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getOrganiser_name() {
        return organiser_name;
    }

    public void setOrganiser_name(String organiser_name) {
        this.organiser_name = organiser_name;
    }

    public String getOrganiset_image() {
        return organiset_image;
    }

    public void setOrganiset_image(String organiset_image) {
        this.organiset_image = organiset_image;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEventStartDateTimeShort() {
        return eventStartDateTimeShort;
    }

    public void setEventStartDateTimeShort(String eventStartDateTimeShort) {
        this.eventStartDateTimeShort = eventStartDateTimeShort;
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

}