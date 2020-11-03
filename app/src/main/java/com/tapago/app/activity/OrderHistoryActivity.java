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
import com.tapago.app.adapter.NotificationAdapter;
import com.tapago.app.adapter.OrderHistoryAdapter;
import com.tapago.app.model.NotificationModel.NotificationModel;
import com.tapago.app.model.OrderHistoryModel.Datum;
import com.tapago.app.model.OrderHistoryModel.OrderHistoryResponseModel;
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

public class OrderHistoryActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.recycleViewOrderHistory)
    RecyclerView recycleViewOrderHistory;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;

    OrderHistoryAdapter orderHistoryAdapter;
    List<Datum>orderHistoryList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);
        setRecyclerView();
        orderHistoryListApi();
    }


    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                orderHistoryListApi();
            break;
        }
    }


    //Setting recycler view
    private void setRecyclerView() {
        recycleViewOrderHistory.setHasFixedSize(true);
        orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderHistoryList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleViewOrderHistory.setLayoutManager(linearLayoutManager);//Linear Items
        recycleViewOrderHistory.setItemAnimator(new DefaultItemAnimator());
        recycleViewOrderHistory.setAdapter(orderHistoryAdapter);
    }

    private void orderHistoryListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<OrderHistoryResponseModel> call;
            call = RetrofitRestClient.getInstance().orderHistoryAPI(params);

            if (call == null) return;

            call.enqueue(new Callback<OrderHistoryResponseModel>() {
                @Override
                public void onResponse(@NonNull final Call<OrderHistoryResponseModel> call, @NonNull Response<OrderHistoryResponseModel> response) {
                    hideProgressDialog();
                    final OrderHistoryResponseModel orderHistoryResponseModel;
                    if (response.isSuccessful()) {
                        orderHistoryResponseModel = response.body();
                        if (Objects.requireNonNull(orderHistoryResponseModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                orderHistoryList.clear();
                                orderHistoryList.addAll(orderHistoryResponseModel.getData());
                                orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderHistoryList);
                                recycleViewOrderHistory.setAdapter(orderHistoryAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(orderHistoryResponseModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(orderHistoryResponseModel).getCode() == 201) {
                            try {
                                txtNoDataFound.setText(orderHistoryResponseModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), orderHistoryResponseModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<OrderHistoryResponseModel> call, @NonNull Throwable t) {
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
