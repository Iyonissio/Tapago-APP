package com.tapago.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.tapago.app.R;
import com.tapago.app.model.BasicModel;
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

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.old_password)
    EditText oldPassword;
    @BindView(R.id.new_password)
    EditText newPassword;
    @BindView(R.id.c_new_password)
    EditText cNewPassword;
    @BindView(R.id.inputOldPWd)
    TextInputLayout inputOldPWd;
    @BindView(R.id.inputNewPassword)
    TextInputLayout inputNewPassword;
    @BindView(R.id.inputCPassword)
    TextInputLayout inputCPassword;
    @BindView(R.id.btnSubmit)
    AppCompatButton btnSubmit;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;

    String userID = "", accessToken = "",title="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        if (RestConstant.FORGOT_PASSWORD.equals("FORGOT_PASSWORD")) {
            inputOldPWd.setVisibility(View.GONE);
            oldPassword.setVisibility(View.GONE);

            if (getIntent() != null) {
                userID = getIntent().getStringExtra("userID");
                accessToken = getIntent().getStringExtra("accessToken");
                title=getIntent().getStringExtra("title");
                txtName.setText("Reset Password");
            }
        } else {
            inputOldPWd.setVisibility(View.VISIBLE);
            oldPassword.setVisibility(View.VISIBLE);
            txtName.setText(MySharedPreferences.getMySharedPreferences().getChangePassword().replace("?", ""));
        }

        inputOldPWd.setHint(MySharedPreferences.getMySharedPreferences().getOldPassword());
        inputNewPassword.setHint(MySharedPreferences.getMySharedPreferences().getNewPassword());
        inputCPassword.setHint(MySharedPreferences.getMySharedPreferences().getConfirmPassword());
        btnSubmit.setText(MySharedPreferences.getMySharedPreferences().getSubmit());
    }

    @OnClick({R.id.btnSubmit, R.id.ivBackArrow})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (RestConstant.FORGOT_PASSWORD.equals("FORGOT_PASSWORD")) {
                    if (forgotPassValidate()) {
                        forgotChnagePasswordApi();
                    }
                } else {
                    if (changePassValidation()) {
                        chnagePasswordApi();
                    }
                }

                break;
            case R.id.ivBackArrow:
                finish();
                AppUtils.startFromRightToLeft(getActivity());
                break;
        }
    }

    /**
     * change password validation
     *
     * @return
     */
    private boolean changePassValidation() {
        if (TextUtils.isEmpty(AppUtils.getText(oldPassword))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPleaseEnterOldPassword());
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(newPassword))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPleaseEnterNewPassword());
            return false;
        } else if (!AppUtils.isValidPassword(AppUtils.getText(newPassword))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPassword_should_be_minimum_6_characters());
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(cNewPassword))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Enter_Confirm_Password());
            return false;
        } else if (!AppUtils.getText(newPassword).matches(AppUtils.getText(cNewPassword))) {
            showSnackBar(this, MySharedPreferences.getMySharedPreferences().getPassword_And_Confirm_Password_Does_Not_Match());
            return false;
        } else {
            return true;
        }
    }

    private boolean forgotPassValidate() {
        if (TextUtils.isEmpty(AppUtils.getText(newPassword))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPleaseEnterNewPassword());
            return false;
        }
        if (!AppUtils.isValidPassword(AppUtils.getText(newPassword))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPassword_should_be_minimum_6_characters());
            return false;
        }
        if (TextUtils.isEmpty(AppUtils.getText(cNewPassword))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Enter_Confirm_Password());
            return false;
        }
        if (!AppUtils.getText(newPassword).equals(AppUtils.getText(cNewPassword))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPassword_And_Confirm_Password_Does_Not_Match());
            return false;
        }
        return true;
    }


    /**
     * change password api
     */
    private void chnagePasswordApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("old_password", AppUtils.getText(oldPassword));
            params.put("new_password", AppUtils.getText(newPassword));
            params.put("confirm_password", AppUtils.getText(cNewPassword));
            params.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().changePassword(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            showSnackBars(getActivity(), basicModel.getMessage());
                            logoutApi();
                        } else if (Objects.requireNonNull(basicModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), basicModel.getMessage());
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
        }
    }

    /**
     * validate login api
     */
    private void logoutApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("user_device_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());


            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().logoutApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    BasicModel logOutModel;
                    if (response.isSuccessful()) {
                        logOutModel = response.body();
                        if (Objects.requireNonNull(logOutModel).getCode() == 200) {
                            MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
                            mySharedPreferences.setLogin(false);
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            AppUtils.startFromRightToLeft(getActivity());
                        } else if (Objects.requireNonNull(logOutModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), logOutModel.getMessage());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        AppUtils.finishFromLeftToRight(getActivity());
    }

    private void forgotChnagePasswordApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", userID);
            params.put("new_password", AppUtils.getText(newPassword));
            params.put("confirm_password", AppUtils.getText(cNewPassword));
            params.put("token", accessToken);
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("role_type", "User");

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().forgotChangePassApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            //showSnackBars(getActivity(), basicModel.getMessage());
                            AppUtils.showToast(getActivity(), basicModel.getMessage());
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            AppUtils.startFromRightToLeft(getActivity());
                        } else if (Objects.requireNonNull(basicModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), basicModel.getMessage());
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
        }
    }
}