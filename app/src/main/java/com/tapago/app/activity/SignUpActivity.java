package com.tapago.app.activity;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.hbb20.CountryCodePicker;
import com.tapago.app.R;
import com.tapago.app.model.RegisterCaptionModel.RegisterCaption;
import com.tapago.app.model.SendOtpModel;
import com.tapago.app.rest.RestConstant;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.edFirstName)
    AppCompatEditText edFirstName;
    @BindView(R.id.edLastName)
    AppCompatEditText edLastName;
    @BindView(R.id.edEmail)
    AppCompatEditText edEmail;
    @BindView(R.id.edMobile)
    AppCompatEditText edMobile;
    @BindView(R.id.edPassword)
    AppCompatEditText edPassword;
    @BindView(R.id.edCPassword)
    AppCompatEditText edCPassword;
    @BindView(R.id.txtRegister)
    AppCompatTextView txtRegister;
    @BindView(R.id.inputFirstName)
    TextInputLayout inputFirstName;
    @BindView(R.id.inputLastName)
    TextInputLayout inputLastName;
    @BindView(R.id.inputEmail)
    TextInputLayout inputEmail;
    @BindView(R.id.inputMobile)
    TextInputLayout inputMobile;
    @BindView(R.id.inputPassword)
    TextInputLayout inputPassword;
    @BindView(R.id.inputCPassword)
    TextInputLayout inputCPassword;
    @BindView(R.id.btnRegister)
    AppCompatButton btnRegister;
    @BindView(R.id.txtAlready)
    AppCompatTextView txtAlready;
    @BindView(R.id.txtLogin)
    AppCompatTextView txtLogin;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    private String enterFirst = "", enterLast = "", enterEmail = "", validEmail = "", enterPassword = "", passwordLength = "", enterConfirmPwd = "", confirmPwdMatch = "", enterMobile = "", validMobile = "", validPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        if (MySharedPreferences.getMySharedPreferences().getLanguage().equalsIgnoreCase("en")) {
            ccp.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH);
        } else {
            ccp.changeDefaultLanguage(CountryCodePicker.Language.PORTUGUESE);
        }

        signUpCaptionApi();
    }

    @OnClick({R.id.btnRegister, R.id.txtLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                if (signUpValidation()) {
                    System.out.println("Entrei na ValidacaoSignUP");
                    sendOtpApi();
                    System.out.println("Entrei na ValidacaoSignUP Apos o SendOtpApi");
                }
                break;
            case R.id.txtLogin:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
                AppUtils.startFromRightToLeft(getActivity());
                break;
        }
    }


    /**
     * signUpCaptionApi
     */
    private void signUpCaptionApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("activity_name", "RegisterActivity");
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<RegisterCaption> call;
            call = RetrofitRestClient.getInstance().registercaptionApi(params);

            if (call == null) return;

            call.enqueue(new Callback<RegisterCaption>() {
                @Override
                public void onResponse(@NonNull Call<RegisterCaption> call, @NonNull Response<RegisterCaption> response) {
                    hideProgressDialog();
                    final RegisterCaption basicCaption;
                    if (response.isSuccessful()) {
                        basicCaption = response.body();
                        if (Objects.requireNonNull(basicCaption).getCode() == 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        txtRegister.setText(basicCaption.getActivity().getRegister());
                                        inputFirstName.setHint(basicCaption.getActivity().getFirstName());
                                        inputLastName.setHint(basicCaption.getActivity().getLastName());
                                        inputEmail.setHint(basicCaption.getActivity().getEmail());
                                        inputMobile.setHint(basicCaption.getActivity().getPhoneUmber());
                                        inputPassword.setHint(basicCaption.getActivity().getPassword());
                                        inputCPassword.setHint(basicCaption.getActivity().getConfirmPassword());
                                        btnRegister.setText(basicCaption.getActivity().getRegister());
                                        txtAlready.setText(basicCaption.getActivity().getNumberExist());
                                        txtLogin.setText(basicCaption.getActivity().getLogin());
                                        enterFirst = basicCaption.getActivity().getEnterFirstName();
                                        enterLast = basicCaption.getActivity().getEnterLastName();
                                        enterEmail = basicCaption.getActivity().getEnterEmail();
                                        validEmail = basicCaption.getActivity().getEnterInvalidEmail();
                                        enterPassword = basicCaption.getActivity().getEnterPassword();
                                        passwordLength = basicCaption.getActivity().getEnterPasswordMiniLength();
                                        enterConfirmPwd = basicCaption.getActivity().getEnterConfirmPassword();
                                        confirmPwdMatch = basicCaption.getActivity().getEnterConfirmNotMetch();
                                        enterMobile = basicCaption.getActivity().getEnterMobile();
                                        validMobile = basicCaption.getActivity().getEnterInvalidMobile();
                                        validPassword = basicCaption.getActivity().getValidPassword();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (Objects.requireNonNull(basicCaption).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), basicCaption.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RegisterCaption> call, @NonNull Throwable t) {
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
     * Validation
     */
    private boolean signUpValidation() {
        if (TextUtils.isEmpty(AppUtils.getText(edFirstName))) {
            showSnackBar(getActivity(), enterFirst);
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edLastName))) {
            showSnackBar(getActivity(), enterLast);
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edEmail))) {
            showSnackBar(getActivity(), enterEmail);
            return false;
        } else if (!AppUtils.isEmailValid(AppUtils.getText(edEmail))) {
            showSnackBar(getActivity(), validEmail);
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edMobile))) {
            showSnackBar(getActivity(), enterMobile);
            return false;
        } else if (!AppUtils.isValidMobile(AppUtils.getText(edMobile))) {
            showSnackBar(getActivity(), validMobile);
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edPassword))) {
            showSnackBar(getActivity(), enterPassword);
            return false;
        } else if (!AppUtils.isValidPassword(AppUtils.getText(edPassword))) {
            showSnackBar(getActivity(), validPassword);
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edCPassword))) {
            showSnackBar(getActivity(), enterConfirmPwd);
            return false;
        } else if (!AppUtils.getText(edPassword).matches(AppUtils.getText(edCPassword))) {
            showSnackBar(this, confirmPwdMatch);
            return false;
        } else {
            return true;
        }
    }

    /**
     * send Otp Api
     */
    private void sendOtpApi() {
        System.out.println("sendOTP Entrei");
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> map = new HashMap<>();
            map.put("mobile_no", ccp.getSelectedCountryCodeWithPlus() + AppUtils.getText(edMobile));
            map.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            map.put("email", AppUtils.getText(edEmail));
            map.put("user_id", "");
            map.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            map.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            System.out.println("sendOTP Entrei - Dados");
            System.out.println("HASMAP---------------->"+Arrays.asList(map));
            Call<SendOtpModel> call;
            call = RetrofitRestClient.getInstance().sendOtp(map);
            System.out.println("Antes do call return------- " + call.isCanceled());
            if (call == null) return;
            System.out.println("Apos o call return---------");
            call.enqueue(new Callback<SendOtpModel>() {
                @Override
                public void onResponse(@NonNull Call<SendOtpModel> call, @NonNull Response<SendOtpModel> response) {
                    System.out.print("Dentro do enfileiramento de Dados------");
                    hideProgressDialog();
                    final SendOtpModel sendOtpModel;
                    System.out.print("Dentro do enfileiramento de Dados 2 ------");
                    if (response.isSuccessful()) {
                        System.out.print("Dentro do enfileiramento de Dados 3 ------");
                        sendOtpModel = response.body();
                        if (Objects.requireNonNull(sendOtpModel).getCode() == 200) {
                            AppUtils.showToast(getActivity(), sendOtpModel.getMessage());
                            RestConstant.PROFILE_UPDATE = "";
                            Intent intent = new Intent(getActivity(), OTPActivity.class);
                            intent.putExtra("firstname", AppUtils.getText(edFirstName));
                            intent.putExtra("lastname", AppUtils.getText(edLastName));
                            intent.putExtra("email", AppUtils.getText(edEmail));
                            intent.putExtra("password", AppUtils.getText(edPassword));
                            intent.putExtra("confirm_password", AppUtils.getText(edCPassword));
                            intent.putExtra("contact_number", AppUtils.getText(edMobile));
                            intent.putExtra("country_code", ccp.getSelectedCountryCodeWithPlus());
                            intent.putExtra("contact_country_code_name", ccp.getSelectedCountryNameCode());
                            startActivity(intent);
                            AppUtils.startFromRightToLeft(getActivity());
                        } else if (Objects.requireNonNull(sendOtpModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), sendOtpModel.getMessage());
                        }
                    } else {
                        System.out.println("Response Error " + response.headers());
                        showSnackBar(getActivity(), response.message());
                    }
                }


                @Override
                public void onFailure(@NonNull Call<SendOtpModel> call, @NonNull Throwable t) {
                    System.out.print("Correu para a falha------");
                    hideProgressDialog();
                    if (t instanceof SocketTimeoutException) {
                        System.out.print("Correu para a falha 1------");
                        showSnackBar(getActivity(), getString(R.string.connection_timeout));
                    } else {
                        System.out.print("Correu para a falha 2------");
                        t.printStackTrace();
                        showSnackBar(getActivity(), getString(R.string.something_went_wrong));
                    }
                }
            });
            System.out.println("Apos o call return---------2");
        } else {
            showSnackBar(getActivity(), getString(R.string.no_internet));
        }
    }

}
