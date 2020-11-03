package com.tapago.app.model.EventListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("event_list")
    @Expose
    private List<EventList> eventList = null;
    @SerializedName("popular_event")
    @Expose
    private PopularEvent popularEvent;
    @SerializedName("page_count")
    @Expose
    private Integer pageCount;

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<EventList> getEventList() {
        return eventList;
    }

    public void setEventList(List<EventList> eventList) {
        this.eventList = eventList;
    }

    public PopularEvent getPopularEvent() {
        return popularEvent;
    }

    public void setPopularEvent(PopularEvent popularEvent) {
        this.popularEvent = popularEvent;
    }

}