package com.tapago.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tapago.app.R;
import com.tapago.app.adapter.MonthSpinnerAdapter;
import com.tapago.app.adapter.YearSpinnerAdapter;
import com.tapago.app.model.MonthModel;
import com.tapago.app.model.PaymentModel;
import com.tapago.app.model.YearModel;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends BaseActivity {

    @BindView(R.id.radioCOD)
    RadioButton radioCOD;
    @BindView(R.id.radioOnPayment)
    RadioButton radioOnPayment;
    @BindView(R.id.radGroup)
    RadioGroup radGroup;
    @BindView(R.id.edt_card_number)
    AppCompatEditText edtCardNumber;
    @BindView(R.id.spinner_months)
    AppCompatSpinner spinnerMonths;
    @BindView(R.id.spinner_year)
    AppCompatSpinner spinnerYear;
    @BindView(R.id.edt_cvv)
    AppCompatEditText edtCvv;
    @BindView(R.id.edt_edt_name_on_card)
    AppCompatEditText edtEdtNameOnCard;
    @BindView(R.id.lrCard)
    LinearLayout lrCard;
    @BindView(R.id.btncard_payment_pay)
    AppCompatButton btncardPaymentPay;
    ArrayList<YearModel> yearList = new ArrayList<>();
    ArrayList<MonthModel> monthList = new ArrayList<>();
    String month = "", years = "", requestId = "", amount = "", discount = "", type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            requestId = getIntent().getStringExtra("requestId");
            amount = getIntent().getStringExtra("amount");
            discount = getIntent().getStringExtra("discount");
            type = getIntent().getStringExtra("type");
        }

        radGroup.clearCheck();

        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = radioGroup.findViewById(i);
                if (null != rb && i > -1) {
                    if (rb.getText().equals(getString(R.string.cod))) {
                        lrCard.setVisibility(View.GONE);
                    } else {
                        lrCard.setVisibility(View.GONE);
                    }
                }
            }
        });

        for (int i = 1; i <= 12; i++) {
            MonthModel monthModel = new MonthModel();
            monthModel.setMonth(String.valueOf(i));
            monthList.add(monthModel);
        }

        Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        int Max_YEAR = year + 100;

        for (int i = year; i <= Max_YEAR; i++) {
            YearModel yearModel = new YearModel();
            yearModel.setYear(String.valueOf(i));
            yearList.add(yearModel);
        }

        spinnerMonths.setAdapter(new MonthSpinnerAdapter(getActivity(), monthList));
        spinnerYear.setAdapter(new YearSpinnerAdapter(getActivity(), yearList));

        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    MonthModel monthModel = monthList.get(position - 1);
                    month = monthModel.getMonth();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    YearModel yearModel = yearList.get(position - 1);
                    years = yearModel.getYear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @OnClick({R.id.ivBackArrow, R.id.btncard_payment_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.btncard_payment_pay:
                if (radGroup.getCheckedRadioButtonId() == -1) {
                    showSnackBar(getActivity(), getString(R.string.please_select_payment_type));
                    return;
                }

                if (lrCard.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(AppUtils.getText(edtCardNumber))) {
                        showSnackBar(getActivity(), getString(R.string.please_enter_card_number));
                        return;
                    } else if (AppUtils.getText(edtCardNumber).length() < 16) {
                        showSnackBar(getActivity(), getString(R.string.please_enter_valid_card_number));
                        return;
                    } else if (spinnerYear.getSelectedItemPosition() < 1) {
                        showSnackBar(getActivity(), getString(R.string.please_select_expire_year));
                        return;
                    } else if (spinnerMonths.getSelectedItemPosition() < 1) {
                        showSnackBar(getActivity(), getString(R.string.please_select_expire_month));
                        return;
                    } else if (TextUtils.isEmpty(AppUtils.getText(edtCvv))) {
                        showSnackBar(getActivity(), getString(R.string.please_enter_cvv));
                        return;
                    } else if (TextUtils.isEmpty(AppUtils.getText(edtEdtNameOnCard))) {
                        showSnackBar(getActivity(), getString(R.string.please_enter_card_holder_name));
                        return;
                    }
                }

                createPaymentApi(requestId, amount, discount, type);
                break;
        }
    }

    public void createPaymentApi(String requestId, String amount, String discount, String type) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            RadioButton rb = radGroup.findViewById(radGroup.getCheckedRadioButtonId());
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            map.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            map.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            map.put("amount", amount);
            map.put("payment_method_nonce", "");
            map.put("request_id", requestId);
            map.put("payment_for", type);
            if (rb.getText().equals(getString(R.string.cod))) {
                map.put("payment_type", "COD");
            } else {
                map.put("payment_type", "Online");
            }

            map.put("discount", discount);
            map.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<PaymentModel> call;
            call = RetrofitRestClient.getInstance().voucherTicketPaymentApi(map);

            if (call == null) return;
            call.enqueue(new Callback<PaymentModel>() {
                @Override

                public void onResponse(@NonNull Call<PaymentModel> call, @NonNull Response<PaymentModel> response) {
                    hideProgressDialog();
                    final PaymentModel paymentModel;
                    if (response.isSuccessful()) {
                        paymentModel = response.body();
                        if (Objects.requireNonNull(paymentModel).getCode() == 200) {
                            try {
                                if (paymentModel.getResponseurl().length() > 0) {
                                    Intent intent = new Intent(getActivity(), PaymentviewActivity.class);
                                    intent.putExtra("url", paymentModel.getResponseurl());
                                    startActivity(intent);
                                } else {
                                    AppUtils.showToast(getActivity(), paymentModel.getMessage());
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(paymentModel).getCode() == 999) {
                            logout();
                        } else {
                            AppUtils.showToast(getActivity(), paymentModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PaymentModel> call, @NonNull Throwable t) {
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