package com.tapago.app.model;

import com.tapago.app.model.ProductModel.ProductImage;
import com.tapago.app.model.ProductModel.ProductSize;

import java.io.Serializable;
import java.util.List;

public class BasketModel implements Serializable {

    private Integer productId;
    private String productName;
    private String aboutProduct;
    private String mainImage;
    private String productType;
    private String imageUrl;
    private String defaultImage;
    private String brandName;
    private Integer categoryId;
    private List<ProductImage> productImageList = null;
    private List<ProductSize> productSizeList = null;
    private boolean repeatItem = false;
    private boolean minus = false;
    private boolean isSameScale = false;
    private int addedQuantity;
    private int mainQuantity;
    private String price;
    private String productScale;
    private String productBrand;
    private Boolean isMostPopular =false;

    public Boolean getMostPopular() {
        return isMostPopular;
    }

    public void setMostPopular(Boolean mostPopular) {
        isMostPopular = mostPopular;
    }

    public boolean isSameScale() {
        return isSameScale;
    }

    public void setSameScale(boolean sameScale) {
        isSameScale = sameScale;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
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

    public int getMainQuantity() {
        return mainQuantity;
    }

    public void setMainQuantity(int mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    public int getAddedQuantity() {
        return addedQuantity;
    }

    public void setAddedQuantity(int addedQuantity) {
        this.addedQuantity = addedQuantity;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public List<ProductImage> getProductImageList() {
        return productImageList;
    }

    public void setProductImageList(List<ProductImage> productImageList) {
        this.productImageList = productImageList;
    }

    public List<ProductSize> getProductSizeList() {
        return productSizeList;
    }

    public void setProductSizeList(List<ProductSize> productSizeList) {
        this.productSizeList = productSizeList;
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
}
