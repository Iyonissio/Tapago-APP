package com.tapago.app.model.OrderHistoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingDetails {

@SerializedName("first_name")
@Expose
private String firstName;
@SerializedName("last_name")
@Expose
private String lastName;
@SerializedName("mobile")
@Expose
private String mobile;
@SerializedName("house_no")
@Expose
private String houseNo;
@SerializedName("apartment_name")
@Expose
private String apartmentName;
@SerializedName("landmark")
@Expose
private String landmark;
@SerializedName("city")
@Expose
private String city;
@SerializedName("pincode")
@Expose
private String pincode;

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

public String getHouseNo() {
return houseNo;
}

public void setHouseNo(String houseNo) {
this.houseNo = houseNo;
}

public String getApartmentName() {
return apartmentName;
}

public void setApartmentName(String apartmentName) {
this.apartmentName = apartmentName;
}

public String getLandmark() {
return landmark;
}

public void setLandmark(String landmark) {
this.landmark = landmark;
}

public String getCity() {
return city;
}

public void setCity(String city) {
this.city = city;
}

public String getPincode() {
return pincode;
}

public void setPincode(String pincode) {
this.pincode = pincode;
}
}