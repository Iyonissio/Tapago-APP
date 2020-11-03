package com.tapago.app.model;


import java.io.Serializable;

public class Ticket implements Serializable {

    String id;
    String qty;
    String amount;
    String ticket_discount;

    public String getTicket_discount() {
        return ticket_discount;
    }

    public void setTicket_discount(String ticket_discount) {
        this.ticket_discount = ticket_discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
