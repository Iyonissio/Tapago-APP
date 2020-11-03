package com.tapago.app.model.TicketListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ticket_category_name")
    @Expose
    private String ticketCategoryName;
    @SerializedName("no_of_ticket")
    @Expose
    private Integer noOfTicket;
    @SerializedName("ticket_price")
    @Expose
    private Integer ticketPrice;
    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;
    @SerializedName("position")
    @Expose
    private Integer postition;
    @SerializedName("remaining_qty")
    @Expose
    private String remainingQty;
    @SerializedName("ticket_discount")
    @Expose
    private Integer ticketDiscount;


    public Integer getTicketDiscount() {
        return ticketDiscount;
    }

    public void setTicketDiscount(Integer ticketDiscount) {
        this.ticketDiscount = ticketDiscount;
    }

    public String getRemainingQty() {
        return remainingQty;
    }

    public void setRemainingQty(String remainingQty) {
        this.remainingQty = remainingQty;
    }

    public Integer getPostition() {
        return postition;
    }

    public void setPostition(Integer postition) {
        this.postition = postition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTicketCategoryName() {
        return ticketCategoryName;
    }

    public void setTicketCategoryName(String ticketCategoryName) {
        this.ticketCategoryName = ticketCategoryName;
    }

    public Integer getNoOfTicket() {
        return noOfTicket;
    }

    public void setNoOfTicket(Integer noOfTicket) {
        this.noOfTicket = noOfTicket;
    }

    public Integer getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Integer ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

}