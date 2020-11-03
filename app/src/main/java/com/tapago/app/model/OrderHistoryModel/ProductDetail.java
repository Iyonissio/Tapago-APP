package com.tapago.app.model.OrderHistoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetail {

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("about_product")
    @Expose
    private String aboutProduct;
    @SerializedName("main_image")
    @Expose
    private String mainImage;
    @SerializedName("product_price")
    @Expose
    private Double productPrice;
    @SerializedName("purchase_qty")
    @Expose
    private String purchaseQty;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("total_price")
    @Expose
    private Double totalPrice;

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAboutProduct() {
        return aboutProduct;
    }

    public void setAboutProduct(String aboutProduct) {
        this.aboutProduct = aboutProduct;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }


    public String getPurchaseQty() {
        return purchaseQty;
    }

    public void setPurchaseQty(String purchaseQty) {
        this.purchaseQty = purchaseQty;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }


}
