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
import com.tapago.app.adapter.PaymentHistoryAdapter;
import com.tapago.app.adapter.PaymentHistoryMerchantAdapter;
import com.tapago.app.model.PaymentHistoryModel.Datum;
import com.tapago.app.model.PaymentHistoryModel.PaymentModel;
import com.tapago.app.model.PaymentMearchentModel.PaymentMearchentModel;
import com.tapago.app.pagination.EndlessRecyclerOnScrollListener;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistoryActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.recycleViewPaymentHistory)
    RecyclerView recycleViewPaymentHistory;
    PaymentHistoryAdapter paymentHistoryAdapter;
    PaymentHistoryMerchantAdapter paymentHistoryMerchantAdapter;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    ArrayList<Datum> paymentHistoryList = new ArrayList<>();
    ArrayList<com.tapago.app.model.PaymentMearchentModel.Datum> paymentMerchantHistoryList = new ArrayList<>();
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private int TOTAL_PAGES;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        ButterKnife.bind(this);

        txtName.setText(MySharedPreferences.getMySharedPreferences().getPaymentHistory());
        setRecyclerView();
        if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("3")) {
            paymentListUserApi(currentPage);
        } else {
            paymentMerchantHistoryList.clear();
            paymentListMerchantApi(currentPage);
        }
    }

    //Setting recycler view
    private void setRecyclerView() {
        recycleViewPaymentHistory.setHasFixedSize(true);
        paymentHistoryAdapter = new PaymentHistoryAdapter(getActivity(), paymentHistoryList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleViewPaymentHistory.setLayoutManager(linearLayoutManager);//Linear Items
        recycleViewPaymentHistory.setItemAnimator(new DefaultItemAnimator());
        recycleViewPaymentHistory.setAdapter(paymentHistoryAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("3")) {
                    paymentListUserApi(current_page);
                } else {
                    paymentListMerchantApi(current_page);
                }

            }
        };
        recycleViewPaymentHistory.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("3")) {
                    paymentHistoryList.clear();
                    paymentListUserApi(currentPage);
                } else {
                    paymentMerchantHistoryList.clear();
                    paymentListMerchantApi(currentPage);
                }
                break;
        }
    }


    private void paymentListUserApi(final int page) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            if (page == 1) {
                try {
                    mainProgress.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                paymentHistoryAdapter.showLoadMore(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("page_number", String.valueOf(page));

            Call<PaymentModel> call ;
            call = RetrofitRestClient.getInstance().paymentHistoryApi(params);

            if (call == null) return;

            call.enqueue(new Callback<PaymentModel>() {
                @Override
                public void onResponse(@NonNull final Call<PaymentModel> call, @NonNull Response<PaymentModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final PaymentModel paymentModel;
                    if (response.isSuccessful()) {
                        paymentModel = response.body();
                        if (Objects.requireNonNull(paymentModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                TOTAL_PAGES = paymentModel.getPageCount();
                                paymentHistoryList.addAll(paymentModel.getData());
                                paymentHistoryAdapter = new PaymentHistoryAdapter(getActivity(), paymentHistoryList);
                                recycleViewPaymentHistory.setAdapter(paymentHistoryAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(paymentModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(paymentModel).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(paymentModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                                paymentHistoryAdapter.showLoadMore(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), paymentModel.getMessage());
                            endlessRecyclerOnScrollListener.previousState();
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                        endlessRecyclerOnScrollListener.previousState();
                    }
                    paymentHistoryAdapter.showLoadMore(false);
                }

                @Override
                public void onFailure(@NonNull Call<PaymentModel> call, @NonNull Throwable t) {
                    try {
                        paymentHistoryAdapter.showLoadMore(false);
                        endlessRecyclerOnScrollListener.previousState();
                        mainProgress.setVisibility(View.GONE);
                        if (t instanceof SocketTimeoutException) {
                            txtNoDataFound.setText(getString(R.string.connection_timeout));
                            txtTryAgain.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                            if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                            } else {
                                showSnackBar(getActivity(), getString(R.string.connection_timeout));
                            }
                        } else {
                            t.printStackTrace();
                            txtNoDataFound.setText(getString(R.string.something_went_wrong));
                            txtTryAgain.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                            if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                            } else {
                                showSnackBar(getActivity(), getString(R.string.something_went_wrong));
                            }
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
            if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                txtNoDataFound.setVisibility(View.GONE);
                txtTryAgain.setVisibility(View.GONE);
            } else {
                showSnackBar(getActivity(), getString(R.string.no_internet));
            }
        }
    }


    private void paymentListMerchantApi(final int page) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            if (page == 1) {
                try {
                    mainProgress.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                paymentHistoryMerchantAdapter.showLoadMore(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("page_number", String.valueOf(page));

            Call<PaymentMearchentModel> call;
            call = RetrofitRestClient.getInstance().paymentMerchantApi(params);

            if (call == null) return;

            call.enqueue(new Callback<PaymentMearchentModel>() {
                @Override
                public void onResponse(@NonNull final Call<PaymentMearchentModel> call, @NonNull Response<PaymentMearchentModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final PaymentMearchentModel paymentModel;
                    if (response.isSuccessful()) {
                        paymentModel = response.body();
                        if (Objects.requireNonNull(paymentModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                TOTAL_PAGES = paymentModel.getPageCount();
                                paymentMerchantHistoryList.addAll(paymentModel.getData());
                                paymentHistoryMerchantAdapter = new PaymentHistoryMerchantAdapter(getActivity(), paymentMerchantHistoryList);
                                recycleViewPaymentHistory.setAdapter(paymentHistoryMerchantAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(paymentModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(paymentModel).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(paymentModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                                paymentHistoryAdapter.showLoadMore(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), paymentModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                    paymentHistoryMerchantAdapter.showLoadMore(false);
                }

                @Override
                public void onFailure(@NonNull Call<PaymentMearchentModel> call, @NonNull Throwable t) {
                    try {
                        paymentHistoryMerchantAdapter.showLoadMore(false);
                        endlessRecyclerOnScrollListener.previousState();
                        mainProgress.setVisibility(View.GONE);
                        if (t instanceof SocketTimeoutException) {
                            txtNoDataFound.setText(getString(R.string.connection_timeout));
                            txtTryAgain.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                            if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                            } else {
                                showSnackBar(getActivity(), getString(R.string.connection_timeout));
                            }
                        } else {
                            t.printStackTrace();
                            txtNoDataFound.setText(getString(R.string.something_went_wrong));
                            txtTryAgain.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                            if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                            } else {
                                showSnackBar(getActivity(), getString(R.string.something_went_wrong));
                            }
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
            if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                txtNoDataFound.setVisibility(View.GONE);
                txtTryAgain.setVisibility(View.GONE);
            } else {
                showSnackBar(getActivity(), getString(R.string.no_internet));
            }
        }
    }


}
