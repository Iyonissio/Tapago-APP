package com.tapago.app.model.EditEventModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Voucher {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("voucher_category_name")
    @Expose
    private String voucherCategoryName;
    @SerializedName("no_of_voucher")
    @Expose
    private String noOfVoucher;
    @SerializedName("voucher_price")
    @Expose
    private Integer voucherPrice;
    @SerializedName("checked_flag")
    @Expose
    private String checkedFlag;
    @SerializedName("voucher_discount")
    @Expose
    private Integer voucherDiscount;
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

    public String getVoucherCategoryName() {
        return voucherCategoryName;
    }

    public void setVoucherCategoryName(String voucherCategoryName) {
        this.voucherCategoryName = voucherCategoryName;
    }

    public String getNoOfVoucher() {
        return noOfVoucher;
    }

    public void setNoOfVoucher(String noOfVoucher) {
        this.noOfVoucher = noOfVoucher;
    }

    public Integer getVoucherPrice() {
        return voucherPrice;
    }

    public void setVoucherPrice(Integer voucherPrice) {
        this.voucherPrice = voucherPrice;
    }

    public String getCheckedFlag() {
        return checkedFlag;
    }

    public void setCheckedFlag(String checkedFlag) {
        this.checkedFlag = checkedFlag;
    }

    public Integer getVoucherDiscount() {
        return voucherDiscount;
    }

    public void setVoucherDiscount(Integer voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }

}