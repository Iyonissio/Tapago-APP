package com.tapago.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.tapago.app.R;
import com.tapago.app.adapter.OrderHistoryAdapter;
import com.tapago.app.adapter.ShippingPaymentHistoryAdapter;
import com.tapago.app.model.OrderHistoryModel.OrderHistoryResponseModel;
import com.tapago.app.model.ShippingPaymentHistoryModel.Datum;
import com.tapago.app.model.ShippingPaymentHistoryModel.ShippingPaymentHistoryModel;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingPaymentHistoryActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.recycleViewShippingHistory)
    RecyclerView recycleViewShippingHistory;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;

    ShippingPaymentHistoryAdapter shippingPaymentHistoryAdapter;
    List<Datum>paymenthistoryList =new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_payment_history);
        ButterKnife.bind(this);

        setRecyclerView();
        paymentHistoryListApi();
    }

    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                paymentHistoryListApi();
                break;
        }
    }

    //Setting recycler view
    private void setRecyclerView() {
        recycleViewShippingHistory.setHasFixedSize(true);
        shippingPaymentHistoryAdapter = new ShippingPaymentHistoryAdapter(getActivity(), paymenthistoryList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleViewShippingHistory.setLayoutManager(linearLayoutManager);//Linear Items
        recycleViewShippingHistory.setItemAnimator(new DefaultItemAnimator());
        recycleViewShippingHistory.setAdapter(shippingPaymentHistoryAdapter);
    }

    private void paymentHistoryListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<ShippingPaymentHistoryModel> call;
            call = RetrofitRestClient.getInstance().shippingPaymentHistoryAPI(params);

            if (call == null) return;

            call.enqueue(new Callback<ShippingPaymentHistoryModel>() {
                @Override
                public void onResponse(@NonNull final Call<ShippingPaymentHistoryModel> call, @NonNull Response<ShippingPaymentHistoryModel> response) {
                    hideProgressDialog();
                    final ShippingPaymentHistoryModel paymentHistoryResponseModel;
                    if (response.isSuccessful()) {
                        paymentHistoryResponseModel = response.body();
                        if (Objects.requireNonNull(paymentHistoryResponseModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                paymenthistoryList.clear();
                                paymenthistoryList.addAll(paymentHistoryResponseModel.getData());
                                shippingPaymentHistoryAdapter = new ShippingPaymentHistoryAdapter(getActivity(), paymenthistoryList);
                                recycleViewShippingHistory.setAdapter(shippingPaymentHistoryAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(paymentHistoryResponseModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(paymentHistoryResponseModel).getCode() == 201) {
                            try {
                                txtNoDataFound.setText(paymentHistoryResponseModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), paymentHistoryResponseModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ShippingPaymentHistoryModel> call, @NonNull Throwable t) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                        if (t instanceof SocketTimeoutException) {
                            txtNoDataFound.setText(getString(R.string.connection_timeout));
                            txtTryAgain.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                        } else {
                            t.printStackTrace();
                            txtNoDataFound.setText(getString(R.string.something_went_wrong));
                            txtTryAgain.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            txtNoDataFound.setText(getString(R.string.no_internet));
            txtTryAgain.setVisibility(View.VISIBLE);
            txtNoDataFound.setVisibility(View.VISIBLE);
        }
    }

}
