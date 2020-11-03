package com.tapago.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.tapago.app.R;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.ForgotPassModel.ForgotOTPModel;
import com.tapago.app.model.ForgotPassowordCaption.ForgotPassowordCaption;
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

public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.edEmail)
    AppCompatEditText edEmail;
    @BindView(R.id.btnSubmit)
    AppCompatButton btnSubmit;
    @BindView(R.id.txtForgot)
    AppCompatTextView txtForgot;
    @BindView(R.id.txtLogin)
    TextView txtLogin;
    @BindView(R.id.txtBack)
    AppCompatTextView txtBack;
    String enterEmail = "", validEmail = "",loginType;
    @BindView(R.id.inputEmail)
    TextInputLayout inputEmail;
    @BindView(R.id.radio_Email)
    AppCompatRadioButton radioEmail;
    @BindView(R.id.radio_Mobile)
    AppCompatRadioButton radioMobile;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.edt_number)
    AppCompatEditText edtNumber;
    @BindView(R.id.til_mobile)
    TextInputLayout tilMobile;
    @BindView(R.id.linearNumber)
    LinearLayout linearNumber;
    @BindView(R.id.linearEmail)
    LinearLayout linearEmail;
    @BindView(R.id.viewEmail)
    View viewEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        forgotPasswordCaptionApi();

        loginmobileOrEmail();
    }

    @OnClick({R.id.txtLogin, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtLogin:
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.btnSubmit:
                if (loginType.equals("email")) {
                    if (loginValidation()) {
                        resetPassword();
                    }
                }else {
                    if (phoneValidation()) {
                        sendOtpApi();
                    }
                }
                AppUtils.hideSoftKeyboard(getActivity());
                break;
        }
    }
    private boolean phoneValidation() {
        if (TextUtils.isEmpty(AppUtils.getText(edtNumber))) {
            showSnackBar(getActivity(), "Please enter number");
            return false;
        } else if (!AppUtils.isValidMobile(AppUtils.getText(edtNumber))) {
            showSnackBar(getActivity(), "Please enter valid number");
            return false;
        } else {
            return true;
        }
    }

    /**
     * validation forgot password
     *
     * @return
     */
    private boolean loginValidation() {
        if (TextUtils.isEmpty(AppUtils.getText(edEmail))) {
            showSnackBar(getActivity(), enterEmail);
            return false;
        } else if (!AppUtils.isEmailValid(AppUtils.getText(edEmail))) {
            showSnackBar(getActivity(), validEmail);
            return false;
        } else {
            return true;
        }
    }

    /**
     * forgotPasswordCaptionApi
     */
    private void forgotPasswordCaptionApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("activity_name", "ForgotPasswordActivity");
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<ForgotPassowordCaption> call;
            call = RetrofitRestClient.getInstance().forgotpasswordcaptionApi(params);

            if (call == null) return;

            call.enqueue(new Callback<ForgotPassowordCaption>() {
                @Override
                public void onResponse(@NonNull Call<ForgotPassowordCaption> call, @NonNull Response<ForgotPassowordCaption> response) {
                    hideProgressDialog();
                    final ForgotPassowordCaption forgotPassowordCaption;
                    if (response.isSuccessful()) {
                        forgotPassowordCaption = response.body();
                        if (Objects.requireNonNull(forgotPassowordCaption).getCode() == 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        txtForgot.setText(forgotPassowordCaption.getActivity().getForgotPassword());
                                        inputEmail.setHint(forgotPassowordCaption.getActivity().getEmail());
                                        btnSubmit.setText(forgotPassowordCaption.getActivity().getSubmit());
                                        txtBack.setText(forgotPassowordCaption.getActivity().getBackTo());
                                        txtLogin.setText(forgotPassowordCaption.getActivity().getLogin());
                                        enterEmail = forgotPassowordCaption.getActivity().getEnterEmail();
                                        validEmail = forgotPassowordCaption.getActivity().getEnterValidEmail();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (Objects.requireNonNull(forgotPassowordCaption).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), forgotPassowordCaption.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ForgotPassowordCaption> call, @NonNull Throwable t) {
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
     * Forgot Password Api
     */
    private void resetPassword() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("email", AppUtils.getText(edEmail));
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().forgotpassword(params);

            if (call == null) return;
            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    BasicModel forgotPasswordModel;
                    if (response.isSuccessful()) {
                        forgotPasswordModel = response.body();
                        if (Objects.requireNonNull(forgotPasswordModel).getCode() == 200) {
                            showSnackBars(getActivity(), forgotPasswordModel.getMessage());
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                    AppUtils.finishFromLeftToRight(getActivity());
                                }
                            }, 800);
                        } else if (Objects.requireNonNull(forgotPasswordModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), forgotPasswordModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BasicModel> call, @NonNull Throwable t) {
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

    private void loginmobileOrEmail() {
        if (radioEmail.isChecked()) {
            linearEmail.setVisibility(View.VISIBLE);
            linearNumber.setVisibility(View.GONE);
            viewEmail.setVisibility(View.VISIBLE);
            inputEmail.requestFocus();
            edtNumber.setText("");
            loginType = "email";
        } else {
            linearEmail.setVisibility(View.GONE);
            linearNumber.setVisibility(View.VISIBLE);
            tilMobile.requestFocus();
            edEmail.setText("");
            viewEmail.setVisibility(View.GONE);
            loginType = "phone";
        }


        radioEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radioEmail.isChecked()) {
                    linearEmail.setVisibility(View.VISIBLE);
                    linearNumber.setVisibility(View.GONE);
                    viewEmail.setVisibility(View.VISIBLE);
                    inputEmail.requestFocus();
                    edtNumber.setText("");
                    loginType = "email";
                } else {
                    linearEmail.setVisibility(View.GONE);
                    linearNumber.setVisibility(View.VISIBLE);
                    tilMobile.requestFocus();
                    edEmail.setText("");
                    viewEmail.setVisibility(View.GONE);
                    loginType = "phone";
                }
            }
        });

        radioMobile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radioMobile.isChecked()) {
                    linearEmail.setVisibility(View.GONE);
                    linearNumber.setVisibility(View.VISIBLE);
                    tilMobile.requestFocus();
                    edEmail.setText("");
                    viewEmail.setVisibility(View.GONE);
                    loginType = "phone";
                } else {
                    linearEmail.setVisibility(View.VISIBLE);
                    linearNumber.setVisibility(View.GONE);
                    viewEmail.setVisibility(View.VISIBLE);
                    inputEmail.requestFocus();
                    edtNumber.setText("");
                    loginType = "email";
                }
            }
        });
    }

    private void sendOtpApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> map = new HashMap<>();
            map.put("phone_no", ccp.getSelectedCountryCodeWithPlus() + AppUtils.getText(edtNumber));
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
                            Intent intent = new Intent(getActivity(), ForgotPassOTPActivity.class);
                            intent.putExtra("country_code", ccp.getSelectedCountryCodeWithPlus());
                            intent.putExtra("contact_country_code_name", ccp.getSelectedCountryNameCode());
                            intent.putExtra("number",AppUtils.getText(edtNumber));
                            intent.putExtra("userID",String.valueOf(sendOtpModel.getUserId()));
                            startActivity(intent);
                            AppUtils.startFromRightToLeft(getActivity());
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
}
