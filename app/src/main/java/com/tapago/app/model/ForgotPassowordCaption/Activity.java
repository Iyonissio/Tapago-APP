package com.tapago.app.model.ForgotPassowordCaption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activity {

    @SerializedName("forgot_password")
    @Expose
    private String forgotPassword;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("Submit")
    @Expose
    private String submit;
    @SerializedName("enter_email")
    @Expose
    private String enterEmail;
    @SerializedName("enter_valid_email")
    @Expose
    private String enterValidEmail;
    @SerializedName("back_to")
    @Expose
    private String backTo;
    @SerializedName("Login")
    @Expose
    private String login;

    public String getForgotPassword() {
        return forgotPassword;
    }

    public void setForgotPassword(String forgotPassword) {
        this.forgotPassword = forgotPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
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

    public String getBackTo() {
        return backTo;
    }

    public void setBackTo(String backTo) {
        this.backTo = backTo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}