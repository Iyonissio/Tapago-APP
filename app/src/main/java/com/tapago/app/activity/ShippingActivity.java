package com.tapago.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tapago.app.R;
import com.tapago.app.adapter.MonthSpinnerAdapter;
import com.tapago.app.adapter.ProductdetailAdapter;
import com.tapago.app.adapter.YearSpinnerAdapter;
import com.tapago.app.model.AddressListModel.Datum;
import com.tapago.app.model.BasketModel;
import com.tapago.app.model.MonthModel;
import com.tapago.app.model.OrderDetailsModel;
import com.tapago.app.model.PaymentModel;
import com.tapago.app.model.YearModel;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtPaymentType)
    AppCompatTextView txtPaymentType;
    @BindView(R.id.radioCOD)
    RadioButton radioCOD;
//    @BindView(R.id.radioOnPayment)
//    RadioButton radioOnPayment;
    @BindView(R.id.radGroup)
    RadioGroup radGroup;
    @BindView(R.id.edt_card_number)
    AppCompatEditText edtCardNumber;
    @BindView(R.id.til_card_number)
    TextInputLayout tilCardNumber;
    @BindView(R.id.spinner_months)
    AppCompatSpinner spinnerMonths;
    @BindView(R.id.spinner_year)
    AppCompatSpinner spinnerYear;
    @BindView(R.id.edt_cvv)
    AppCompatEditText edtCvv;
    @BindView(R.id.til_cvv)
    TextInputLayout tilCvv;
    @BindView(R.id.edt_edt_name_on_card)
    AppCompatEditText edtEdtNameOnCard;
    @BindView(R.id.til_name_on_card)
    TextInputLayout tilNameOnCard;
    @BindView(R.id.btncard_payment_pay)
    AppCompatButton btncardPaymentPay;

    ArrayList<YearModel> yearList = new ArrayList<>();
    ArrayList<MonthModel> monthList = new ArrayList<>();
    String month = "", years = "";
    @BindView(R.id.lrCard)
    LinearLayout lrCard;

    String addressId;
    List<OrderDetailsModel> orderDetailsModelList = new ArrayList<>();
    OrderDetailsModel orderDetailsModel;
    Datum datum = new Datum();
    @BindView(R.id.recycleViewProductDetails)
    RecyclerView recycleViewProductDetails;
    @BindView(R.id.txtFinalPrice)
    AppCompatTextView txtFinalPrice;
    ProductdetailAdapter productdetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);
        ButterKnife.bind(this);

        try {
            Intent intent = getIntent();
            if (intent != null) {
                addressId = intent.getStringExtra("addressID");
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
            ArrayList<BasketModel> arrOrderList = MySharedPreferences.getGuestBasketListing();
            if (arrOrderList != null) {
                if (arrOrderList.size() > 0) {
                    recycleViewProductDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
                    productdetailAdapter = new ProductdetailAdapter(getActivity(), arrOrderList);
                    recycleViewProductDetails.setAdapter(productdetailAdapter);

                    double sum = 0, mainPrice = 0;
                    int sumqty = 0;
                    for (int i = 0; i < arrOrderList.size(); i++) {
                        orderDetailsModel = new OrderDetailsModel();
                        String[] splitPrice = arrOrderList.get(i).getPrice().split("/");
                        String price = splitPrice[0];
                        String scale = splitPrice[0];
                        sum += arrOrderList.get(i).getAddedQuantity() * Double.valueOf(price);

                        sumqty += arrOrderList.get(i).getAddedQuantity();
                        /*DecimalFormat format = new DecimalFormat("0.00");
                        String formatted = format.format(sum);
                        String money = formatted.replace(',', '.');*/
                        double d = Double.valueOf(sum);
                        int r = (int) Math.round(d * 100);
                        mainPrice = r / 100.0;

                    }

                    txtFinalPrice.setText(String.valueOf(mainPrice) + "MT");
                    Log.e("final Amount", String.valueOf(mainPrice));
                    //txtPrice.setText(String.valueOf(mainPrice));
                    //txtQty.setText(String.valueOf(sumqty) + " " + getString(R.string.item));


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ivBackArrow, R.id.btncard_payment_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(ShippingActivity.this);
                break;
            case R.id.btncard_payment_pay:
                if (validation()) {
                    shippingPaymentAPi();
                }
                break;
        }
    }


    public boolean validation() {
        if (radGroup.getCheckedRadioButtonId() == -1) {
            showSnackBar(getActivity(), getString(R.string.please_select_payment_type));
            return false;
        } else if (lrCard.getVisibility() == View.VISIBLE) {
            if (cardValidation()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean cardValidation() {
        if (TextUtils.isEmpty(AppUtils.getText(edtCardNumber))) {
            showSnackBar(getActivity(), getString(R.string.please_enter_card_number));
            return false;
        } else if (AppUtils.getText(edtCardNumber).length() < 16) {
            showSnackBar(getActivity(), getString(R.string.please_enter_valid_card_number));
            return false;
        } else if (spinnerYear.getSelectedItemPosition() < 1) {
            showSnackBar(getActivity(), getString(R.string.please_select_expire_year));
            return false;
        } else if (spinnerMonths.getSelectedItemPosition() < 1) {
            showSnackBar(getActivity(), getString(R.string.please_select_expire_month));
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edtCvv))) {
            showSnackBar(getActivity(), getString(R.string.please_enter_cvv));
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edtEdtNameOnCard))) {
            showSnackBar(getActivity(), getString(R.string.please_enter_card_holder_name));
            return false;
        } else {
            return true;
        }
    }


    private void shippingPaymentAPi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            RadioButton rb = radGroup.findViewById(radGroup.getCheckedRadioButtonId());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("shipping_id", addressId);
            if (rb.getText().equals(getString(R.string.cod))) {
                params.put("payment_type", "COD");
            } else {
                params.put("payment_type", "Online");
            }

            try {
                ArrayList<BasketModel> arrOrderList = MySharedPreferences.getGuestBasketListing();
                if (arrOrderList != null) {
                    if (arrOrderList.size() > 0) {
                        double sum = 0, mainPrice = 0;
                        int sumqty = 0;
                        for (int i = 0; i < arrOrderList.size(); i++) {
                            orderDetailsModel = new OrderDetailsModel();
                            String[] splitPrice = arrOrderList.get(i).getPrice().split("/");
                            String price = splitPrice[0];
                            String scale = splitPrice[0];
                            sum += arrOrderList.get(i).getAddedQuantity() * Double.valueOf(price);

                            sumqty += arrOrderList.get(i).getAddedQuantity();
                        /*DecimalFormat format = new DecimalFormat("0.00");
                        String formatted = format.format(sum);
                        String money = formatted.replace(',', '.');*/
                            double d = Double.valueOf(sum);
                            int r = (int) Math.round(d * 100);
                            mainPrice = r / 100.0;
                            orderDetailsModel.setProduct_id(arrOrderList.get(i).getProductId());
                            orderDetailsModel.setCategory_id(arrOrderList.get(i).getCategoryId());
                            orderDetailsModel.setPackage_size(arrOrderList.get(i).getProductScale());
                            orderDetailsModel.setPrice(Double.parseDouble(price));
                            orderDetailsModel.setQty(String.valueOf(arrOrderList.get(i).getMainQuantity()));

                            orderDetailsModelList.add(orderDetailsModel);
                        }

                        params.put("final_amount", String.valueOf(mainPrice));
                        Log.e("final Amount", String.valueOf(mainPrice));
                        //txtPrice.setText(String.valueOf(mainPrice));
                        //txtQty.setText(String.valueOf(sumqty) + " " + getString(R.string.item));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Gson gson1 = new GsonBuilder().create();
            JsonArray orderdetails = gson1.toJsonTree(orderDetailsModelList).getAsJsonArray();
            params.put("order_details", String.valueOf(orderdetails));
            Log.e("OrderModel", String.valueOf(orderdetails));


            Call<PaymentModel> call;
            call = RetrofitRestClient.getInstance().paymentShippingAPI(params);

            if (call == null) return;

            call.enqueue(new Callback<PaymentModel>() {
                @Override
                public void onResponse(@NonNull Call<PaymentModel> call, @NonNull Response<PaymentModel> response) {
                    hideProgressDialog();
                    final PaymentModel paymentModel;
                    if (response.isSuccessful()) {
                        paymentModel = response.body();
                        if (Objects.requireNonNull(paymentModel).getCode() == 200) {
                            MySharedPreferences.getMySharedPreferences().setGuestBasketListing(null);
                            if (paymentModel.getResponseurl().length() > 0) {
                                Intent intent = new Intent(getActivity(), PaymentviewActivity.class);
                                intent.putExtra("url", paymentModel.getResponseurl());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            AppUtils.startFromRightToLeft(getActivity());
                        } else if (Objects.requireNonNull(paymentModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), paymentModel.getMessage());
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
        }
    }
}
