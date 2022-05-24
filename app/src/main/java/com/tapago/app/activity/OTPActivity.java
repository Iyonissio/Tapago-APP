package com.tapago.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.tapago.app.R;
import com.tapago.app.model.LoginModel.Data;
import com.tapago.app.model.LoginModel.Login;
import com.tapago.app.model.OTPCaption.OTPModel;
import com.tapago.app.model.SendOtpModel;
import com.tapago.app.model.UpdateProfileModel.UpdateProfileResponse;
import com.tapago.app.model.VerifyOtpModel;
import com.tapago.app.rest.RestConstant;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends BaseActivity {

    String email, pass, cpass, number, firstname, lastname, countryCode, countrycodename, picturePath;
    @BindView(R.id.firstPinView)
    PinView firstPinView;
    @BindView(R.id.txtVerification)
    AppCompatTextView txtVerification;
    @BindView(R.id.txtVDes)
    AppCompatTextView txtVDes;
    @BindView(R.id.btn_verify)
    AppCompatButton btnVerify;
    @BindView(R.id.txtdid)
    AppCompatTextView txtdid;
    @BindView(R.id.txtResendCode)
    AppCompatTextView txtResendCode;
    String enterOTP = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (RestConstant.PROFILE_UPDATE.equalsIgnoreCase("update")) {
                firstname = intent.getStringExtra("firstname");
                lastname = intent.getStringExtra("lastname");
                email = intent.getStringExtra("email");
                number = intent.getStringExtra("contact_number");
                countryCode = intent.getStringExtra("country_code");
                countrycodename = intent.getStringExtra("contact_country_code_name");
                picturePath = intent.getStringExtra("picturePath");
            } else {
                firstname = intent.getStringExtra("firstname");
                lastname = intent.getStringExtra("lastname");
                email = intent.getStringExtra("email");
                pass = intent.getStringExtra("password");
                cpass = intent.getStringExtra("confirm_password");
                number = intent.getStringExtra("contact_number");
                countryCode = intent.getStringExtra("country_code");
                countrycodename = intent.getStringExtra("contact_country_code_name");
            }
        }

        OTPCaptionApi();
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

    @OnClick({R.id.txtResendCode, R.id.btn_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtResendCode:
                firstPinView.setText("");
                System.out.println("Antes do sendOTPAPI metodo------------------------");
                sendOtpApi();
                break;
            case R.id.btn_verify:
                if (AppUtils.isConnectedToInternet(getActivity())) {
                    if (TextUtils.isEmpty(AppUtils.getText(firstPinView))) {
                        showSnackBar(getActivity(), enterOTP);
                    } else {
                        VerifyOtpApi();
                    }
                }
                break;
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
            map.put("mobile_no", countryCode + number);
            map.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            map.put("email", email);
            System.out.println("Dentro do sendOTP");
            if (RestConstant.PROFILE_UPDATE.equalsIgnoreCase("update")) {
                map.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
                System.out.println("Dentro do sendOTP---if SIM");
            } else {
                map.put("user_id", "");
                System.out.println("Dentro do sendOTP---if NAO");
            }
            map.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            map.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());

            Call<SendOtpModel> call;
            call = RetrofitRestClient.getInstance().sendOtp(map);

            if (call == null) return;

            call.enqueue(new Callback<SendOtpModel>() {
                @Override
                public void onResponse(@NonNull Call<SendOtpModel> call, @NonNull Response<SendOtpModel> response) {
                    hideProgressDialog();
                    final SendOtpModel sendOtpModel;
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
                public void onFailure(@NonNull Call<SendOtpModel> call, @NonNull Throwable t) {
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
     * Call Verify OTP
     */
    private void VerifyOtpApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> map = new HashMap<>();
            map.put("mobile_no", countryCode + number);
            map.put("otp", AppUtils.getText(firstPinView));
            map.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            map.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            map.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());

            Call<VerifyOtpModel> call;
            call = RetrofitRestClient.getInstance().verifyOtp(map);

            if (call == null) return;

            call.enqueue(new Callback<VerifyOtpModel>() {
                @Override
                public void onResponse(@NonNull Call<VerifyOtpModel> call, @NonNull Response<VerifyOtpModel> response) {
                    hideProgressDialog();
                    final VerifyOtpModel verifyOtpModel;
                    if (response.isSuccessful()) {
                        verifyOtpModel = response.body();
                        if (Objects.requireNonNull(verifyOtpModel).getCode() == 200) {
                            AppUtils.showToast(getActivity(), verifyOtpModel.getMessage());
                            if (RestConstant.PROFILE_UPDATE.equalsIgnoreCase("update")) {
                                updateApi();
                            } else {
                                signUpAPi();
                            }
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
                public void onFailure(@NonNull Call<VerifyOtpModel> call, @NonNull Throwable t) {
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
     * update profile api
     */
    private void updateApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, RequestBody> params = new HashMap<>();
            params.put("first_name", AppUtils.getRequestBody(firstname));
            params.put("last_name", AppUtils.getRequestBody(lastname));
            params.put("email", AppUtils.getRequestBody(email));
            params.put("country_code", AppUtils.getRequestBody(countryCode));
            params.put("contact_number", AppUtils.getRequestBody(number));
            params.put("country_code_name", AppUtils.getRequestBody(countrycodename));
            params.put("user_id", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getUserId()));
            params.put("user_device_id", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getDeviceId()));
            params.put("access_token", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getAccessToken()));
            params.put("lang", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getLanguage()));

            MultipartBody.Part partbody1 = null;
            if (picturePath.length() > 0) {
                File file = new File(picturePath);
                RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), file);
                partbody1 = MultipartBody.Part.createFormData("image", file.getName(), reqFile1);
            }

            Call<UpdateProfileResponse> call;
            call = RetrofitRestClient.getInstance().updateProfileApi(params, partbody1);

            if (call == null) return;

            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(@NonNull Call<UpdateProfileResponse> call, @NonNull Response<UpdateProfileResponse> response) {
                    hideProgressDialog();
                    UpdateProfileResponse updateProfile;
                    if (response.isSuccessful()) {
                        updateProfile = response.body();
                        if (Objects.requireNonNull(updateProfile).getCode() == 200) {
                            try {
                                Toast.makeText(getActivity(), updateProfile.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(updateProfile).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), updateProfile.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UpdateProfileResponse> call, @NonNull Throwable t) {
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
     * call sigUp api
     */
    private void signUpAPi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("first_name", firstname);
            params.put("last_name", lastname);
            params.put("email", email);
            params.put("password", pass);
            params.put("confirm_password", cpass);
            params.put("contact_number", number);
            params.put("country_code", countryCode);
            params.put("country_code_name", countrycodename);
            params.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("fcm_token", MySharedPreferences.getMySharedPreferences().getFirebaseId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<Login> call;
            call = RetrofitRestClient.getInstance().signUp(params);

            if (call == null) return;
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                    hideProgressDialog();
                    final Login signUpResponse;
                    if (response.isSuccessful()) {
                        signUpResponse = response.body();
                        if (Objects.requireNonNull(signUpResponse).getCode() == 200) {
                            AppUtils.showToast(getActivity(), signUpResponse.getMessage());
                            setUserData(signUpResponse.getData());
                            Intent i = new Intent(getActivity(), CategoryActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            getActivity().finish();
                            AppUtils.startFromRightToLeft(getActivity());
                        } else {
                            showSnackBar(getActivity(), signUpResponse.getMessage());
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
    }
}
