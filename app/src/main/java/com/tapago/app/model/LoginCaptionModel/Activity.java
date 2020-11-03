package com.tapago.app.model.LoginCaptionModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activity {

    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("Forgot_Password")
    @Expose
    private String forgotPassword;
    @SerializedName("new_user")
    @Expose
    private String newUser;
    @SerializedName("register")
    @Expose
    private String register;
    @SerializedName("enter_email")
    @Expose
    private String enterEmail;
    @SerializedName("enter_valid_email")
    @Expose
    private String enterValidEmail;
    @SerializedName("enter_password")
    @Expose
    private String enterPassword;
    @SerializedName("password_length")
    @Expose
    private String passwordLength;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("validNumber")
    @Expose
    private String validNumber;
    @SerializedName("enterNumber")
    @Expose
    private String enterNumber;

    public String getValidNumber() {
        return validNumber;
    }

    public void setValidNumber(String validNumber) {
        this.validNumber = validNumber;
    }

    public String getEnterNumber() {
        return enterNumber;
    }

    public void setEnterNumber(String enterNumber) {
        this.enterNumber = enterNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForgotPassword() {
        return forgotPassword;
    }

    public void setForgotPassword(String forgotPassword) {
        this.forgotPassword = forgotPassword;
    }

    public String getNewUser() {
        return newUser;
    }

    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getEnterEmail() {
        return enterEmail;
    }

    public void setEnterEmail(String enterEmail) {
        this.enterEmail = enterEmail;
    }

    public String getEnterValidEmail() {
        return enterValidEmail;
    }

    public void setEnterValidEmail(String enterValidEmail) {
        this.enterValidEmail = enterValidEmail;
    }

    public String getEnterPassword() {
        return enterPassword;
    }

    public void setEnterPassword(String enterPassword) {
        this.enterPassword = enterPassword;
    }

    public String getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(String passwordLength) {
        this.passwordLength = passwordLength;
    }

}