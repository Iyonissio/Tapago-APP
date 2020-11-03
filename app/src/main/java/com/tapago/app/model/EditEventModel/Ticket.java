package com.tapago.app.model.EditEventModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ticket {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ticket_category_name")
    @Expose
    private String ticketCategoryName;
    @SerializedName("no_of_ticket")
    @Expose
    private String noOfTicket;
    @SerializedName("ticket_price")
    @Expose
    private Integer ticketPrice;
    @SerializedName("ticket_discount")
    @Expose
    private Integer ticketDiscount;
    @SerializedName("checked_flag")
    @Expose
    private String checkedFlag;
    @SerializedName("editable_flag")
    @Expose
    private String editFlag;
    private boolean isCheckBoxChecked = true;

    public String getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }

    public String getCheckedFlag() {
        return checkedFlag;
    }

    public void setCheckedFlag(String checkedFlag) {
        this.checkedFlag = checkedFlag;
    }

    public boolean isCheckBoxChecked() {
        return isCheckBoxChecked;
    }

    public void setCheckBoxChecked(boolean checkBoxChecked) {
        isCheckBoxChecked = checkBoxChecked;
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

    public String getNoOfTicket() {
        return noOfTicket;
    }

    public void setNoOfTicket(String noOfTicket) {
        this.noOfTicket = noOfTicket;
    }

    public Integer getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Integer ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Integer getTicketDiscount() {
        return ticketDiscount;
    }

    public void setTicketDiscount(Integer ticketDiscount) {
        this.ticketDiscount = ticketDiscount;
    }

}