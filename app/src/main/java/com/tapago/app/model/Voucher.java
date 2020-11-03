package com.tapago.app.model;


import java.io.Serializable;

public class Voucher implements Serializable {

    String id;
    String qty;
    String amount;
    String voucher_discount;

    public String getVoucher_discount() {
        return voucher_discount;
    }

    public void setVoucher_discount(String voucher_discount) {
        this.voucher_discount = voucher_discount;
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
