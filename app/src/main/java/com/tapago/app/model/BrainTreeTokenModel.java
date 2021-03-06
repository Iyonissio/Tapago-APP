package com.tapago.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrainTreeTokenModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("token")
    @Expose
    private String token;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}