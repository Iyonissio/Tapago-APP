package com.tapago.app.model.ProductModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductImage implements Serializable {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("product_id")
@Expose
private Integer productId;
@SerializedName("image")
@Expose
private String image;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Integer getProductId() {
return productId;
}

public void setProductId(Integer productId) {
this.productId = productId;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

}