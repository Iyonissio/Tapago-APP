package com.tapago.app.model.ProductModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Datum implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("about_product")
    @Expose
    private String aboutProduct;
    @SerializedName("product_brand")
    @Expose
    private String productBrand;
    @SerializedName("main_image")
    @Expose
    private String mainImage;
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("defaultImage")
    @Expose
    private String defaultImage;
    @SerializedName("product_image")
    @Expose
    private List<ProductImage> productImage = null;
    @SerializedName("product_size")
    @Expose
    private List<ProductSize> productSize = null;

    //For Most Popular
    @SerializedName("category_ids")
    @Expose
    private String categoryIds;

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    private boolean repeatItem = false;
    private boolean minus = false;
    private boolean isSameScale = false;
    private boolean isMostpopular = false;
    private String categoryId;
    private int addedQuantity;
    private int mainQuantity;
    private String price;
    private String productScale;

    public boolean isMostpopular() {
        return isMostpopular;
    }

    public void setMostpopular(boolean mostpopular) {
        isMostpopular = mostpopular;
    }

    public boolean isSameScale() {
        return isSameScale;
    }

    public void setSameScale(boolean sameScale) {
        isSameScale = sameScale;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductScale() {
        return productScale;
    }

    public void setProductScale(String productScale) {
        this.productScale = productScale;
    }


    public int getAddedQuantity() {
        return addedQuantity;
    }

    public void setAddedQuantity(int addedQuantity) {
        this.addedQuantity = addedQuantity;
    }

    public int getMainQuantity() {
        return mainQuantity;
    }

    public void setMainQuantity(int mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isRepeatItem() {
        return repeatItem;
    }

    public void setRepeatItem(boolean repeatItem) {
        this.repeatItem = repeatItem;
    }

    public boolean isMinus() {
        return minus;
    }

    public void setMinus(boolean minus) {
        this.minus = minus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public List<ProductImage> getProductImage() {
        return productImage;
    }

    public void setProductImage(List<ProductImage> productImage) {
        this.productImage = productImage;
    }

    public List<ProductSize> getProductSize() {
        return productSize;
    }

    public void setProductSize(List<ProductSize> productSize) {
        this.productSize = productSize;
    }

}