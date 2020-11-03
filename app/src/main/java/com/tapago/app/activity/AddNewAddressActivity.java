package com.tapago.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.tapago.app.R;
import com.tapago.app.model.BasicModel;
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

public class AddNewAddressActivity extends BaseActivity {


    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_firstName)
    AppCompatEditText edtFirstName;
    @BindView(R.id.til_firstname)
    TextInputLayout tilFirstname;
    @BindView(R.id.edt_lastName)
    AppCompatEditText edtLastName;
    @BindView(R.id.til_lastName)
    TextInputLayout tilLastName;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.edt_number)
    AppCompatEditText edtNumber;
    @BindView(R.id.til_mobile)
    TextInputLayout tilMobile;
    @BindView(R.id.edt_street_Name)
    AppCompatEditText edtStreetName;
    @BindView(R.id.til_street_name)
    TextInputLayout tilStreetName;
    @BindView(R.id.edt_house_number)
    AppCompatEditText edtHouseNumber;
    @BindView(R.id.til_house_number)
    TextInputLayout tilHouseNumber;
    @BindView(R.id.edt_District)
    AppCompatEditText edtDistrict;
    @BindView(R.id.til_District)
    TextInputLayout tilDistrict;
    @BindView(R.id.edt_city)
    AppCompatEditText edtCity;
    @BindView(R.id.til_city)
    TextInputLayout tilCity;
    @BindView(R.id.txtAddressType)
    AppCompatTextView txtAddressType;
    @BindView(R.id.radioHome)
    RadioButton radioHome;
    @BindView(R.id.radioOffice)
    RadioButton radioOffice;
    @BindView(R.id.radioOther)
    RadioButton radioOther;
    @BindView(R.id.radGroup)
    RadioGroup radGroup;
    @BindView(R.id.btn_add_address)
    AppCompatButton btnAddAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ivBackArrow, R.id.btn_add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.btn_add_address:
                if (validation()) {
                    insertAddresslApi();
                }
                break;
        }
    }

    private boolean validation() {
        if (TextUtils.isEmpty(AppUtils.getText(edtFirstName))) {
            showSnackBar(getActivity(), getString(R.string.pleaseEnterFirstName));
            return false;
        } else if (!AppUtils.isNameValid(AppUtils.getText(edtFirstName))) {
            showSnackBar(getActivity(), getString(R.string.first_no_number_or_symbols));
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edtLastName))) {
            showSnackBar(getActivity(), getString(R.string.pleaseEnterLastName));
            return false;
        } else if (!AppUtils.isNameValid(AppUtils.getText(edtLastName))) {
            showSnackBar(getActivity(), getString(R.string.last_no_number_or_symbols));
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edtNumber))) {
            showSnackBar(getActivity(), getString(R.string.pleaseEnterMobile));
            return false;
        } else if (!AppUtils.isValidMobile(AppUtils.getText(edtNumber))) {
            showSnackBar(getActivity(), getString(R.string.pleaseEnterValidMobile));
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edtStreetName))) {
            showSnackBar(getActivity(), getString(R.string.pleaseenterStreetName));
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edtHouseNumber))) {
            showSnackBar(getActivity(), getString(R.string.pleaseEnterhouseNo));
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edtDistrict))) {
            showSnackBar(getActivity(), getString(R.string.pleaseEnterdistrict));
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edtCity))) {
            showSnackBar(getActivity(), getString(R.string.pleaseEnterCity));
            return false;
        } else if (radGroup.getCheckedRadioButtonId() == -1) {
            showSnackBar(getActivity(), getString(R.string.please_select_address_type));
            return false;
        } else {
            return true;
        }
    }

    public void insertAddresslApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("first_name", AppUtils.getText(edtFirstName));
            params.put("last_name", AppUtils.getText(edtLastName));
            params.put("mobile", ccp.getSelectedCountryCodeWithPlus() + AppUtils.getText(edtNumber));
            params.put("house_no", AppUtils.getText(edtHouseNumber));
            params.put("landmark", AppUtils.getText(edtStreetName));
            params.put("city", AppUtils.getText(edtCity));
            params.put("district", AppUtils.getText(edtDistrict));
            RadioButton rb = radGroup.findViewById(radGroup.getCheckedRadioButtonId());
            if (rb.getText().equals(getResources().getString(R.string.home))) {
                params.put("address_type", "Home");
            } else if (rb.getText().equals(getResources().getString(R.string.office))) {
                params.put("address_type", "Work");
            } else {
                params.put("address_type", "Other");
            }

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().insertAddressAPI(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            finish();
                            AppUtils.finishFromLeftToRight(getActivity());
                            Toast.makeText(getActivity(), basicModel.getMessage(), Toast.LENGTH_LONG).show();
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
