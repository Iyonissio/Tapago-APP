package com.tapago.app.model.AddressListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datum implements Serializable {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("first_name")
@Expose
private String firstName;
@SerializedName("last_name")
@Expose
private String lastName;
@SerializedName("mobile")
@Expose
private String mobile;
@SerializedName("address_type")
@Expose
private String addressType;
@SerializedName("house_no")
@Expose
private String houseNo;
@SerializedName("landmark")
@Expose
private String landmark;
@SerializedName("district")
@Expose
private String district;
@SerializedName("city")
@Expose
private String city;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getFirstName() {
return firstName;
}

public void setFirstName(String firstName) {
this.firstName = firstName;
}

public String getLastName() {
return lastName;
}

public void setLastName(String lastName) {
this.lastName = lastName;
}

public String getMobile() {
return mobile;
}

public void setMobile(String mobile) {
this.mobile = mobile;
}

public String getAddressType() {
return addressType;
}

public void setAddressType(String addressType) {
this.addressType = addressType;
}

public String getHouseNo() {
return houseNo;
}

public void setHouseNo(String houseNo) {
this.houseNo = houseNo;
}

public String getLandmark() {
return landmark;
}

public void setLandmark(String landmark) {
this.landmark = landmark;
}

public String getDistrict() {
return district;
}

public void setDistrict(String district) {
this.district = district;
}

public String getCity() {
return city;
}

public void setCity(String city) {
this.city = city;
}

}