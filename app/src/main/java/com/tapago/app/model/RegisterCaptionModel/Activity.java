package com.tapago.app.model.RegisterCaptionModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activity {
    @SerializedName("register")
    @Expose
    private String register;
    @SerializedName("phone_umber")
    @Expose
    private String phoneUmber;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("confirm_password")
    @Expose
    private String confirmPassword;
    @SerializedName("number_exist")
    @Expose
    private String numberExist;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("enter_first_name")
    @Expose
    private String enterFirstName;
    @SerializedName("enter_last_name")
    @Expose
    private String enterLastName;
    @SerializedName("enter_email")
    @Expose
    private String enterEmail;
    @SerializedName("enter_invalid_email")
    @Expose
    private String enterInvalidEmail;
    @SerializedName("enter_password")
    @Expose
    private String enterPassword;
    @SerializedName("enter_password_mini_length")
    @Expose
    private String enterPasswordMiniLength;
    @SerializedName("enter_confirm_password")
    @Expose
    private String enterConfirmPassword;
    @SerializedName("enter_confirm_not_metch")
    @Expose
    private String enterConfirmNotMetch;
    @SerializedName("enter_mobile")
    @Expose
    private String enterMobile;
    @SerializedName("enter_invalid_mobile")
    @Expose
    private String enterInvalidMobile;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("Last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("valid_password")
    @Expose
    private String validPassword;

    public String getValidPassword() {
        return validPassword;
    }

    public void setValidPassword(String validPassword) {
        this.validPassword = validPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getPhoneUmber() {
        return phoneUmber;
    }

    public void setPhoneUmber(String phoneUmber) {
        this.phoneUmber = phoneUmber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNumberExist() {
        return numberExist;
    }

    public void setNumberExist(String numberExist) {
        this.numberExist = numberExist;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEnterFirstName() {
        return enterFirstName;
    }

    public void setEnterFirstName(String enterFirstName) {
        this.enterFirstName = enterFirstName;
    }

    public String getEnterLastName() {
        return enterLastName;
    }

    public void setEnterLastName(String enterLastName) {
        this.enterLastName = enterLastName;
    }

    public String getEnterEmail() {
        return enterEmail;
    }

    public void setEnterEmail(String enterEmail) {
        this.enterEmail = enterEmail;
    }

    public String getEnterInvalidEmail() {
        return enterInvalidEmail;
    }

    public void setEnterInvalidEmail(String enterInvalidEmail) {
        this.enterInvalidEmail = enterInvalidEmail;
    }

    public String getEnterPassword() {
        return enterPassword;
    }

    public void setEnterPassword(String enterPassword) {
        this.enterPassword = enterPassword;
    }

    public String getEnterPasswordMiniLength() {
        return enterPasswordMiniLength;
    }

    public void setEnterPasswordMiniLength(String enterPasswordMiniLength) {
        this.enterPasswordMiniLength = enterPasswordMiniLength;
    }

    public String getEnterConfirmPassword() {
        return enterConfirmPassword;
    }

    public void setEnterConfirmPassword(String enterConfirmPassword) {
        this.enterConfirmPassword = enterConfirmPassword;
    }

    public String getEnterConfirmNotMetch() {
        return enterConfirmNotMetch;
    }

    public void setEnterConfirmNotMetch(String enterConfirmNotMetch) {
        this.enterConfirmNotMetch = enterConfirmNotMetch;
    }

    public String getEnterMobile() {
        return enterMobile;
    }

    public void setEnterMobile(String enterMobile) {
        this.enterMobile = enterMobile;
    }

    public String getEnterInvalidMobile() {
        return enterInvalidMobile;
    }

    public void setEnterInvalidMobile(String enterInvalidMobile) {
        this.enterInvalidMobile = enterInvalidMobile;
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

}