package com.tapago.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.hbb20.CountryCodePicker;
import com.tapago.app.R;
import com.tapago.app.model.LoginCaptionModel.BasicCaption;
import com.tapago.app.model.LoginModel.Data;
import com.tapago.app.model.LoginModel.Login;
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


public class LoginActivity extends BaseActivity {

    @BindView(R.id.txtEnglish)
    AppCompatTextView txtEnglish;
    @BindView(R.id.txtPortuguese)
    AppCompatTextView txtPortuguese;
    @BindView(R.id.txtLogin)
    AppCompatTextView txtLogin;
    @BindView(R.id.edEmail)
    AppCompatEditText edEmail;
    @BindView(R.id.edPassword)
    AppCompatEditText edPassword;
    @BindView(R.id.txtForgot)
    AppCompatTextView txtForgot;
    @BindView(R.id.btnLogin)
    AppCompatButton btnLogin;
    @BindView(R.id.txtNUser)
    AppCompatTextView txtNUser;
    @BindView(R.id.txtRegister)
    AppCompatTextView txtRegister;
    @BindView(R.id.inputEmail)
    TextInputLayout inputEmail;
    @BindView(R.id.inputPassword)
    TextInputLayout inputPassword;
    @BindView(R.id.txtUnicode)
    AppCompatTextView txtUnicode;
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
    @BindView(R.id.viewLogin)
    View viewLogin;
    private String enterEmail = "", validEmail = "", enterPassword = "", passwordLength = "", loginType = "", enterNumber = "", validNumber = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //set language
        if (MySharedPreferences.getMySharedPreferences().getLanguage().equals("en")) {
            txtEnglish.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_primary));
            txtPortuguese.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_white));

            txtEnglish.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            txtPortuguese.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

            MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
            mySharedPreferences.setLanguage("en");

        } else if (MySharedPreferences.getMySharedPreferences().getLanguage().equals("pt")) {
            txtEnglish.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_white));
            txtPortuguese.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_primary_white));

            txtEnglish.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            txtPortuguese.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

            MySharedPreferences mySharedPreferences1 = MySharedPreferences.getMySharedPreferences();
            mySharedPreferences1.setLanguage("pt");
        }

        loginCaptionApi();

        loginmobileOrEmail();
    }

    @OnClick({R.id.btnLogin, R.id.txtForgot, R.id.txtRegister, R.id.txtEnglish, R.id.txtPortuguese})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if (loginType.equals("email")) {
                    if (loginValidation()) {
                        loginApi();
                    }
                } else {
                    if (phoneValidation()) {
                        loginApi();
                    }
                }
                break;
            case R.id.txtForgot:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.txtRegister:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.txtEnglish:
                txtEnglish.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_primary));
                txtPortuguese.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_white));

                txtEnglish.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                txtPortuguese.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

                MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
                mySharedPreferences.setLanguage("en");

                loginCaptionApi();

                setLanguage("en");
                break;
            case R.id.txtPortuguese:
                txtEnglish.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_white));
                txtPortuguese.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_primary_white));

                txtEnglish.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                txtPortuguese.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                MySharedPreferences mySharedPreferences1 = MySharedPreferences.getMySharedPreferences();
                mySharedPreferences1.setLanguage("pt");

                loginCaptionApi();

                setLanguage("pt");
                break;

        }
    }

    private boolean phoneValidation() {
        if (TextUtils.isEmpty(AppUtils.getText(edtNumber))) {
            showSnackBar(getActivity(), enterNumber);
            return false;
        } else if (!AppUtils.isValidMobile(AppUtils.getText(edtNumber))) {
            showSnackBar(getActivity(), validNumber);
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edPassword))) {
            showSnackBar(getActivity(), enterPassword);
            return false;
        } else {
            return true;
        }
    }

    /**
     * RegisterCaption validation
     */
    private boolean loginValidation() {
        if (TextUtils.isEmpty(AppUtils.getText(edEmail))) {
            showSnackBar(getActivity(), enterEmail);
            return false;
        } else if (!AppUtils.isEmailValid(AppUtils.getText(edEmail))) {
            showSnackBar(getActivity(), validEmail);
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edPassword))) {
            showSnackBar(getActivity(), enterPassword);
            return false;
        } else {
            return true;
        }
    }


    /**
     * loginCaptionApi
     */
    private void loginCaptionApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("activity_name", "LoginActivity");
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicCaption> call;
            call = RetrofitRestClient.getInstance().captionApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicCaption>() {
                @Override
                public void onResponse(@NonNull Call<BasicCaption> call, @NonNull Response<BasicCaption> response) {
                    hideProgressDialog();
                    final BasicCaption basicCaption;
                    if (response.isSuccessful()) {
                        basicCaption = response.body();
                        if (Objects.requireNonNull(basicCaption).getCode() == 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        radioMobile.setText(basicCaption.getActivity().getMobileNumber());
                                        radioEmail.setText(basicCaption.getActivity().getEmail());
                                        txtLogin.setText(basicCaption.getActivity().getLogin());
                                        txtForgot.setText(basicCaption.getActivity().getForgotPassword());
                                        btnLogin.setText(basicCaption.getActivity().getLogin());
                                        txtNUser.setText(basicCaption.getActivity().getNewUser());
                                        txtRegister.setText(basicCaption.getActivity().getRegister());
                                        inputEmail.setHint(basicCaption.getActivity().getEmail());
                                        inputPassword.setHint(basicCaption.getActivity().getPassword());
                                        enterEmail = basicCaption.getActivity().getEnterEmail();
                                        validEmail = basicCaption.getActivity().getEnterValidEmail();
                                        enterPassword = basicCaption.getActivity().getEnterPassword();
                                        passwordLength = basicCaption.getActivity().getPasswordLength();
                                        enterNumber = basicCaption.getActivity().getEnterNumber();
                                        validNumber = basicCaption.getActivity().getValidNumber();
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
                public void onFailure(@NonNull Call<BasicCaption> call, @NonNull Throwable t) {
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
     * Login Api
     */
    private void loginApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("country_code", ccp.getSelectedCountryCodeWithPlus());
            if (loginType.equals("email")) {
                params.put("email_or_phone", AppUtils.getText(edEmail));
            } else {
                params.put("email_or_phone", AppUtils.getText(edtNumber));
            }
            params.put("type", loginType);
            params.put("password", AppUtils.getText(edPassword));
            params.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("fcm_token", MySharedPreferences.getMySharedPreferences().getFirebaseId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<Login> call;
            call = RetrofitRestClient.getInstance().validateLogin(params);

            if (call == null) return;

            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                    hideProgressDialog();
                    final Login login;
                    if (response.isSuccessful()) {
                        login = response.body();
                        if (Objects.requireNonNull(login).getCode() == 200) {
                            AppUtils.showToast(getActivity(), login.getMessage());
                            setUserData(login.getData());
                            if (login.getData().getUserRoleId().equals("6")) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else if (login.getData().getLocation().length() > 0 && login.getData().getCategory().length() > 0) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, CategoryActivity.class));
                            }
                            finish();
                            AppUtils.startFromRightToLeft(getActivity());
                        } else if (Objects.requireNonNull(login).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), login.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
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
     * Store data into pref
     *
     * @param data
     */
    private void setUserData(Data data) {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
        mySharedPreferences.setLogin(true);
        mySharedPreferences.setUserId(String.valueOf(data.getId()));
        mySharedPreferences.setFirstName(data.getFirstName());
        mySharedPreferences.setLastName(data.getLastName());
        mySharedPreferences.setEmail(data.getEmail());
        mySharedPreferences.setAccessToken(data.getAccessToken());
        mySharedPreferences.setContactNumber(data.getPhone());
        mySharedPreferences.setCountryCode(data.getCountryCode());
        mySharedPreferences.setCountryName(data.getCountryCodeName());
        mySharedPreferences.setProfile(data.getImage());
        mySharedPreferences.setUserRoleId(String.valueOf(data.getUserRoleId()));
        mySharedPreferences.setEventId(String.valueOf(data.getEventId()));
        mySharedPreferences.setCity(data.getLocation());
        mySharedPreferences.setCategoryId("[" + data.getCategory() + "]");
    }

    private void loginmobileOrEmail() {
        if (radioEmail.isChecked()) {
            linearEmail.setVisibility(View.VISIBLE);
            linearNumber.setVisibility(View.GONE);
            viewLogin.setVisibility(View.VISIBLE);
            inputEmail.requestFocus();
            edtNumber.setText("");
            edPassword.setText("");
            loginType = "email";
        } else {
            linearEmail.setVisibility(View.GONE);
            linearNumber.setVisibility(View.VISIBLE);
            tilMobile.requestFocus();
            edEmail.setText("");
            edPassword.setText("");
            viewLogin.setVisibility(View.GONE);
            loginType = "phone";
        }


        radioEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radioEmail.isChecked()) {
                    linearEmail.setVisibility(View.VISIBLE);
                    linearNumber.setVisibility(View.GONE);
                    viewLogin.setVisibility(View.VISIBLE);
                    inputEmail.requestFocus();
                    edtNumber.setText("");
                    loginType = "email";
                } else {
                    linearEmail.setVisibility(View.GONE);
                    linearNumber.setVisibility(View.VISIBLE);
                    tilMobile.requestFocus();
                    edEmail.setText("");
                    edPassword.setText("");
                    viewLogin.setVisibility(View.GONE);
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
                    edPassword.setText("");
                    viewLogin.setVisibility(View.GONE);
                    loginType = "phone";
                } else {
                    linearEmail.setVisibility(View.VISIBLE);
                    linearNumber.setVisibility(View.GONE);
                    viewLogin.setVisibility(View.VISIBLE);
                    inputEmail.requestFocus();
                    edtNumber.setText("");
                    edPassword.setText("");
                    loginType = "email";
                }
            }
        });
    }
}
