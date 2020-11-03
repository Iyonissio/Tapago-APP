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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tapago.app.R;
import com.tapago.app.adapter.MonthSpinnerAdapter;
import com.tapago.app.adapter.YearSpinnerAdapter;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.CreateEventModel;
import com.tapago.app.model.MonthModel;
import com.tapago.app.model.Ticket;
import com.tapago.app.model.Voucher;
import com.tapago.app.model.YearModel;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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

public class PaymentEventActivity extends BaseActivity {

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
    List<Voucher> list = new ArrayList<>();
    List<Ticket> ticketlist = new ArrayList<>();
    String categoryId = "", eventName = "", eventBudget = "", startDate = "", startTime = "", endDate = "", endTime = "",
            eventVenue = "", eventAddress = "", locality = "", city = "", eventDescription = "", vendorDetails = "", eventFee = "",
            bannerPath = "", thumbPath = "", month = "", years = "", eventId = "", activity = "", editEventID = "", selectBanner = "", selectThumb = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_payment);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("categoryId");
            eventName = getIntent().getStringExtra("eventName");
            eventBudget = getIntent().getStringExtra("eventBudget");
            startDate = getIntent().getStringExtra("startDate");
            startTime = getIntent().getStringExtra("startTime");
            endDate = getIntent().getStringExtra("endDate");
            endTime = getIntent().getStringExtra("endTime");
            eventVenue = getIntent().getStringExtra("eventVenue");
            eventAddress = getIntent().getStringExtra("eventAddress");
            locality = getIntent().getStringExtra("locality");
            city = getIntent().getStringExtra("city");
            eventDescription = getIntent().getStringExtra("eventDescription");
            vendorDetails = getIntent().getStringExtra("vendorDetails");
            eventFee = getIntent().getStringExtra("eventFee");
            bannerPath = getIntent().getStringExtra("bannerImage");
            thumbPath = getIntent().getStringExtra("thumbPath");
            list = (List<Voucher>) getIntent().getSerializableExtra("details");
            ticketlist = (List<Ticket>) getIntent().getSerializableExtra("ticketDetails");
            activity = getIntent().getStringExtra("activity");
            editEventID = getIntent().getStringExtra("eventId");
            selectBanner = getIntent().getStringExtra("selectBanner");
            selectThumb = getIntent().getStringExtra("selectThumb");
        }

        radGroup.clearCheck();

        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = radioGroup.findViewById(i);
                if (null != rb && i > -1) {
                    if (rb.getText().equals("COD")) {
                        lrCard.setVisibility(View.GONE);
                    } else {
                        lrCard.setVisibility(View.VISIBLE);
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
                if (activity != null) {
                    if (activity.equals("Edit")) {
                        if (eventId.equals("")) {
                            updateEventApi();
                        } else {
                            createPaymentApi(eventId);
                        }
                    }
                } else {
                    if (eventId.equals("")) {
                        createEventApi();
                    } else {
                        createPaymentApi(eventId);
                    }
                }
                break;
        }

    }


    private void createEventApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, RequestBody> params = new HashMap<>();
            params.put("user_id", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getUserId()));
            params.put("device_id", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getDeviceId()));
            params.put("access_token", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getAccessToken()));
            params.put("category_id", AppUtils.getRequestBody(categoryId));
            params.put("event_title", AppUtils.getRequestBody(eventName));
            params.put("event_budget", AppUtils.getRequestBody(eventBudget));
            params.put("start_date", AppUtils.getRequestBody(startDate + " " + startTime));
            params.put("end_date", AppUtils.getRequestBody(endDate + " " + endTime));
            params.put("event_venue", AppUtils.getRequestBody(eventVenue));
            params.put("event_address", AppUtils.getRequestBody(eventAddress));
            if (locality.length() > 0) {
                params.put("city", AppUtils.getRequestBody(locality));
            } else {
                params.put("city", AppUtils.getRequestBody(city));
            }
            params.put("event_description", AppUtils.getRequestBody(eventDescription));
            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();
            params.put("detail", AppUtils.getRequestBody(String.valueOf(myCustomArray)));
            Gson gson1 = new GsonBuilder().create();
            JsonArray myTicket = gson1.toJsonTree(ticketlist).getAsJsonArray();
            params.put("ticket_detail", AppUtils.getRequestBody(String.valueOf(myTicket)));
            params.put("vendor_details", AppUtils.getRequestBody(vendorDetails));
            params.put("event_fee", AppUtils.getRequestBody(eventFee));
            params.put("lang", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getLanguage()));

            MultipartBody.Part partbody1 = null;
            if (bannerPath.length() > 0) {
                File file = new File(bannerPath);
                RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), file);
                partbody1 = MultipartBody.Part.createFormData("banner_image", file.getName(), reqFile1);
            }
            MultipartBody.Part partbody2 = null;
            if (thumbPath.length() > 0) {
                File file2 = new File(thumbPath);
                RequestBody reqFile2 = RequestBody.create(MediaType.parse("image/*"), file2);
                partbody2 = MultipartBody.Part.createFormData("thumb_image", file2.getName(), reqFile2);
            }

            Call<CreateEventModel> call;
            call = RetrofitRestClient.getInstance().createEvent(params, partbody1, partbody2);

            if (call == null) return;

            call.enqueue(new Callback<CreateEventModel>() {
                @Override
                public void onResponse(@NonNull Call<CreateEventModel> call, @NonNull Response<CreateEventModel> response) {
                    hideProgressDialog();
                    final CreateEventModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            Toast.makeText(PaymentEventActivity.this, basicModel.getMessage(), Toast.LENGTH_SHORT).show();
                            eventId = basicModel.getEventId();
                            createPaymentApi(basicModel.getEventId());
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
                public void onFailure(@NonNull Call<CreateEventModel> call, @NonNull Throwable t) {
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


    public void createPaymentApi(String eventId) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            RadioButton rb = radGroup.findViewById(radGroup.getCheckedRadioButtonId());
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            map.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            map.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            map.put("amount", eventBudget);
            map.put("event_fee", eventFee);
            // map.put("payment_method_nonce", paymentMethodNonce1);
            map.put("payment_method_nonce", "");
            map.put("event_id", eventId);
            map.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            if (rb.getText().equals("COD")) {
                map.put("payment_type", "COD");
            } else {
                map.put("payment_type", "Online");
                map.put("account_number", AppUtils.getText(edtCardNumber));
                if (month.length() == 1) {
                    map.put("expiry_month", "0" + month);
                } else {
                    map.put("expiry_month", month);
                }
                map.put("expiry_year", years);
                map.put("card_holder_name", AppUtils.getText(edtEdtNameOnCard));
                map.put("cvc", AppUtils.getText(edtCvv));
            }

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().createEventApi(map);

            if (call == null) return;
            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            try {
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                                AppUtils.finishFromLeftToRight(getActivity());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(basicModel).getCode() == 999) {
                            logout();
                        }else {
                            AppUtils.showToast(getActivity(), basicModel.getMessage());
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


    /**
     * update event Api
     */
    private void updateEventApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, RequestBody> params = new HashMap<>();
            params.put("user_id", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getUserId()));
            params.put("device_id", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getDeviceId()));
            params.put("access_token", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getAccessToken()));
            params.put("category_id", AppUtils.getRequestBody(categoryId));
            params.put("event_id", AppUtils.getRequestBody(editEventID));
            params.put("event_title", AppUtils.getRequestBody(eventName));
            params.put("event_description", AppUtils.getRequestBody(eventDescription));
            params.put("event_start_date_time", AppUtils.getRequestBody(startDate + " " + startTime));
            params.put("event_enddate_time", AppUtils.getRequestBody(endDate + " " + endTime));
            params.put("event_budget", AppUtils.getRequestBody(eventBudget));
            params.put("event_venue", AppUtils.getRequestBody(eventVenue));
            params.put("event_address", AppUtils.getRequestBody(eventAddress));
            if (locality.length() > 0) {
                params.put("city", AppUtils.getRequestBody(locality));
            } else {
                params.put("city", AppUtils.getRequestBody(city));
            }
            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();
            params.put("detail", AppUtils.getRequestBody(String.valueOf(myCustomArray)));
            Gson gson1 = new GsonBuilder().create();
            JsonArray myTicket = gson1.toJsonTree(ticketlist).getAsJsonArray();
            params.put("ticket_detail", AppUtils.getRequestBody(String.valueOf(myTicket)));
            params.put("vendor_details", AppUtils.getRequestBody(vendorDetails));
            params.put("event_fee", AppUtils.getRequestBody(eventFee));
            params.put("lang", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getLanguage()));

            MultipartBody.Part partbody1 = null;
            if (selectBanner.equals("banner")) {
                if (bannerPath.length() > 0) {
                    File file = new File(bannerPath);
                    RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), file);
                    partbody1 = MultipartBody.Part.createFormData("banner_image", file.getName(), reqFile1);
                }
            }
            MultipartBody.Part partbody2 = null;
            if (selectThumb.equals("thumb")) {
                if (thumbPath.length() > 0) {
                    File file2 = new File(thumbPath);
                    RequestBody reqFile2 = RequestBody.create(MediaType.parse("image/*"), file2);
                    partbody2 = MultipartBody.Part.createFormData("thumb_image", file2.getName(), reqFile2);
                }
            }

            Call<CreateEventModel> call;
            call = RetrofitRestClient.getInstance().updateApi(params, partbody1, partbody2);

            if (call == null) return;

            call.enqueue(new Callback<CreateEventModel>() {
                @Override
                public void onResponse(@NonNull Call<CreateEventModel> call, @NonNull Response<CreateEventModel> response) {
                    hideProgressDialog();
                    final CreateEventModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            Toast.makeText(PaymentEventActivity.this, basicModel.getMessage(), Toast.LENGTH_SHORT).show();
                            eventId = basicModel.getEventId();
                            createPaymentApi(basicModel.getEventId());
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
                public void onFailure(@NonNull Call<CreateEventModel> call, @NonNull Throwable t) {
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