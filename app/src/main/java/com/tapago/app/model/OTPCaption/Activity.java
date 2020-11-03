package com.tapago.app.model.OTPCaption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activity {

    @SerializedName("verify")
    @Expose
    private String verify;
    @SerializedName("please_enter_verification_path")
    @Expose
    private String pleaseEnterVerificationPath;
    @SerializedName("didnt_receive_OTP")
    @Expose
    private String didntReceiveOTP;
    @SerializedName("enter_otp")
    @Expose
    private String enterOtp;
    @SerializedName("resend_code")
    @Expose
    private String resendCode;

    public String getResendCode() {
        return resendCode;
    }

    public void setResendCode(String resendCode) {
        this.resendCode = resendCode;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getPleaseEnterVerificationPath() {
        return pleaseEnterVerificationPath;
    }

    public void setPleaseEnterVerificationPath(String pleaseEnterVerificationPath) {
        this.pleaseEnterVerificationPath = pleaseEnterVerificationPath;
    }

    public String getDidntReceiveOTP() {
        return didntReceiveOTP;
    }

    public void setDidntReceiveOTP(String didntReceiveOTP) {
        this.didntReceiveOTP = didntReceiveOTP;
    }

    public String getEnterOtp() {
        return enterOtp;
    }

    public void setEnterOtp(String enterOtp) {
        this.enterOtp = enterOtp;
    }

}