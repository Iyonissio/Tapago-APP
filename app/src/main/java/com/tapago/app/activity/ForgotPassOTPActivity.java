package com.tapago.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.chaos.view.PinView;
import com.tapago.app.R;
import com.tapago.app.model.ForgotPassModel.ForgotOTPModel;
import com.tapago.app.model.ForgotPassModel.ForgotVerifyOTPModel;
import com.tapago.app.model.OTPCaption.OTPModel;
import com.tapago.app.rest.RestConstant;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassOTPActivity extends BaseActivity {

    @BindView(R.id.firstPinView)
    PinView firstPinView;
    @BindView(R.id.btn_verify)
    AppCompatButton btnVerify;
    @BindView(R.id.txtdid)
    AppCompatTextView txtdid;
    @BindView(R.id.txtResendCode)
    AppCompatTextView txtResendCode;
    String enterOTP = "";
    @BindView(R.id.txtVerification)
    AppCompatTextView txtVerification;
    @BindView(R.id.txtVDes)
    AppCompatTextView txtVDes;

    String countryCode = "", countrycodename = "", number = "", userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_otp);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            countryCode = getIntent().getStringExtra("country_code");
            number = getIntent().getStringExtra("number");
            userID = getIntent().getStringExtra("userID");
            countrycodename = getIntent().getStringExtra("contact_country_code_name");
        }

        OTPCaptionApi();
    }

    @OnClick({R.id.btn_verify, R.id.txtResendCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_verify:
                if (AppUtils.isConnectedToInternet(getActivity())) {
                    if (TextUtils.isEmpty(AppUtils.getText(firstPinView))) {
                        showSnackBar(getActivity(), enterOTP);
                    } else {
                        VerifyOtpApi();
                    }
                }
                break;
            case R.id.txtResendCode:
                firstPinView.setText("");
                sendOtpApi();
                break;
        }
    }

    /**
     * Call Verify OTP
     */
    private void VerifyOtpApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> map = new HashMap<>();
            map.put("phone_no", countryCode + number);
            map.put("otp", AppUtils.getText(firstPinView));
            map.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
//            map.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
//            map.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());

            Call<ForgotVerifyOTPModel> call;
            call = RetrofitRestClient.getInstance().forgotOTPVeryfyApi(map);

            if (call == null) return;

            call.enqueue(new Callback<ForgotVerifyOTPModel>() {
                @Override
                public void onResponse(@NonNull Call<ForgotVerifyOTPModel> call, @NonNull Response<ForgotVerifyOTPModel> response) {
                    hideProgressDialog();
                    final ForgotVerifyOTPModel verifyOtpModel;
                    if (response.isSuccessful()) {
                        verifyOtpModel = response.body();
                        if (Objects.requireNonNull(verifyOtpModel).getCode() == 200) {
                            AppUtils.showToast(getActivity(), verifyOtpModel.getMessage());
                            RestConstant.FORGOT_PASSWORD = "FORGOT_PASSWORD";
                            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                            intent.putExtra("userID", String.valueOf(verifyOtpModel.getUserId()));
                            intent.putExtra("accessToken", verifyOtpModel.getToken());
                            intent.putExtra("title", "Reset Password");
                            startActivity(intent);
                        } else if (Objects.requireNonNull(verifyOtpModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), verifyOtpModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ForgotVerifyOTPModel> call, @NonNull Throwable t) {
                    hideProgressDialog();
                    if (t instanceof SocketTimeoutException) {
                        showSnackBar(getActivity(), getString(R.string.connection_timeout));
                    } else {
                        t.printStackTrace();
                        showSnackBar(getActivity(), getString(R.string.something_went_wrong));
                    }
                }
            });
        } else {
            showSnackBar(getActivity(), getString(R.string.no_internet));
        }
    }

    private void sendOtpApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> map = new HashMap<>();
            map.put("phone_no", countryCode + number);
            map.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<ForgotOTPModel> call;
            call = RetrofitRestClient.getInstance().forgotPassOTPApi(map);

            if (call == null) return;

            call.enqueue(new Callback<ForgotOTPModel>() {
                @Override
                public void onResponse(@NonNull Call<ForgotOTPModel> call, @NonNull Response<ForgotOTPModel> response) {
                    hideProgressDialog();
                    final ForgotOTPModel sendOtpModel;
                    if (response.isSuccessful()) {
                        sendOtpModel = response.body();
                        if (Objects.requireNonNull(sendOtpModel).getCode() == 200) {
                            AppUtils.showToast(getActivity(), sendOtpModel.getMessage());
                        } else if (Objects.requireNonNull(sendOtpModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), sendOtpModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ForgotOTPModel> call, @NonNull Throwable t) {
                    hideProgressDialog();
                    if (t instanceof SocketTimeoutException) {
                        showSnackBar(getActivity(), getString(R.string.connection_timeout));
                    } else {
                        t.printStackTrace();
                        showSnackBar(getActivity(), getString(R.string.something_went_wrong));
                    }
                }
            });
        } else {
            showSnackBar(getActivity(), getString(R.string.no_internet));
        }
    }

    /**
     * OTPCaptionApi
     */
    private void OTPCaptionApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("activity_name", "OtpActivity");
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<OTPModel> call;
            call = RetrofitRestClient.getInstance().otpcaptionApi(params);

            if (call == null) return;

            call.enqueue(new Callback<OTPModel>() {
                @Override
                public void onResponse(@NonNull Call<OTPModel> call, @NonNull Response<OTPModel> response) {
                    hideProgressDialog();
                    final OTPModel otpModel;
                    if (response.isSuccessful()) {
                        otpModel = response.body();
                        if (Objects.requireNonNull(otpModel).getCode() == 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        txtVerification.setText(otpModel.getActivity().getVerify());
                                        txtVDes.setText(otpModel.getActivity().getPleaseEnterVerificationPath());
                                        btnVerify.setText(otpModel.getActivity().getVerify());
                                        txtdid.setText(otpModel.getActivity().getDidntReceiveOTP());
                                        txtResendCode.setText(otpModel.getActivity().getResendCode());
                                        enterOTP = otpModel.getActivity().getEnterOtp();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (Objects.requireNonNull(otpModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), otpModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<OTPModel> call, @NonNull Throwable t) {
                    hideProgressDialog();
                    if (t instanceof SocketTimeoutException) {
                        showSnackBar(getActivity(), getString(R.string.connection_timeout));
                    } else {
                        t.printStackTrace();
                        showSnackBar(getActivity(), getString(R.string.something_went_wrong));
                    }
                }
            });
        } else {
            showSnackBar(getActivity(), getString(R.string.no_internet));
        }
    }
}
