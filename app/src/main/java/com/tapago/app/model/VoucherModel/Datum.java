package com.tapago.app.model.VoucherModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("voucher_category_name")
    @Expose
    private String voucherCategoryName;
    @SerializedName("voucher_category_description")
    @Expose
    private String voucherCategoryDescription;
    private boolean isCheckBoxChecked = false;

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

    public String getVoucherCategoryDescription() {
        return voucherCategoryDescription;
    }

    public void setVoucherCategoryDescription(String voucherCategoryDescription) {
        this.voucherCategoryDescription = voucherCategoryDescription;
    }

}