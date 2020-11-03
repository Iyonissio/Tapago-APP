package com.tapago.app.model.VoucherListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("voucher_category_name")
    @Expose
    private String voucherCategoryName;
    @SerializedName("no_of_voucher")
    @Expose
    private Integer noOfVoucher;
    @SerializedName("voucher_price")
    @Expose
    private Integer voucherPrice;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("remaining_qty")
    @Expose
    private String remainingQty;
    @SerializedName("voucher_discount")
    @Expose
    private Integer voucherDiscount;

    public Integer getVoucherDiscount() {
        return voucherDiscount;
    }

    public void setVoucherDiscount(Integer voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }

    public String getRemainingQty() {
        return remainingQty;
    }

    public void setRemainingQty(String remainingQty) {
        this.remainingQty = remainingQty;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVoucherCategoryName() {
        return voucherCategoryName;
    }

    public void setVoucherCategoryName(String voucherCategoryName) {
        this.voucherCategoryName = voucherCategoryName;
    }

    public Integer getNoOfVoucher() {
        return noOfVoucher;
    }

    public void setNoOfVoucher(Integer noOfVoucher) {
        this.noOfVoucher = noOfVoucher;
    }

    public Integer getVoucherPrice() {
        return voucherPrice;
    }

    public void setVoucherPrice(Integer voucherPrice) {
        this.voucherPrice = voucherPrice;
    }

}