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
import com.tapago.app.adapter.MerchantTicketListAdapter;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.BookTicketMerchentModel.BookTicketMerchentModel;
import com.tapago.app.model.BookTicketMerchentModel.Datum;
import com.tapago.app.pagination.EndlessRecyclerOnScrollListener;
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

public class MerchantTicketListActivity extends BaseActivity {


    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.recycleVouchersList)
    RecyclerView recycleVouchersList;

    MerchantTicketListAdapter merchantTicketListAdapter;
    List<Datum> ticketListMerchantModel = new ArrayList<>();
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
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
        setContentView(R.layout.activity_murchant_vouchers_list);
        ButterKnife.bind(this);


        txtName.setText(MySharedPreferences.getMySharedPreferences().getTicketList());
        setRecyclerView();
        ticketListMerchantApi(currentPage);
    }

    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                ticketListMerchantModel.clear();
                ticketListMerchantApi(currentPage);
                break;
        }
    }

    //Setting recycler view
    private void setRecyclerView() {
        recycleVouchersList.setHasFixedSize(true);
        merchantTicketListAdapter = new MerchantTicketListAdapter(getActivity(), ticketListMerchantModel, MerchantTicketListActivity.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleVouchersList.setLayoutManager(linearLayoutManager);//Linear Items
        recycleVouchersList.setItemAnimator(new DefaultItemAnimator());
        recycleVouchersList.setAdapter(merchantTicketListAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                ticketListMerchantApi(current_page);
            }
        };
        recycleVouchersList.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    private void ticketListMerchantApi(final int page) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            if (page == 1) {
                try {
                    mainProgress.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                merchantTicketListAdapter.showLoadMore(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("page_number", String.valueOf(page));

            Call<BookTicketMerchentModel> call;
            call = RetrofitRestClient.getInstance().ticketListMurchantApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BookTicketMerchentModel>() {
                @Override
                public void onResponse(@NonNull final Call<BookTicketMerchentModel> call, @NonNull Response<BookTicketMerchentModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final BookTicketMerchentModel bookTicketMerchentModel;
                    if (response.isSuccessful()) {
                        bookTicketMerchentModel = response.body();
                        if (Objects.requireNonNull(bookTicketMerchentModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                TOTAL_PAGES = bookTicketMerchentModel.getPageCount();
                                ticketListMerchantModel.addAll(bookTicketMerchentModel.getData());
                                merchantTicketListAdapter = new MerchantTicketListAdapter(getActivity(), ticketListMerchantModel, MerchantTicketListActivity.this);
                                recycleVouchersList.setAdapter(merchantTicketListAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(bookTicketMerchentModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(bookTicketMerchentModel).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(bookTicketMerchentModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                                merchantTicketListAdapter.showLoadMore(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), bookTicketMerchentModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                    merchantTicketListAdapter.showLoadMore(false);
                }

                @Override
                public void onFailure(@NonNull Call<BookTicketMerchentModel> call, @NonNull Throwable t) {
                    try {
                        merchantTicketListAdapter.showLoadMore(false);
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

    public void approveDisapproveApi(String id, String isApprove) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("request_voucher_id", id);
            params.put("is_approve", isApprove);

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().approveDisApproveTicketApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull final Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel voucherListResponse;
                    if (response.isSuccessful()) {
                        voucherListResponse = response.body();
                        if (Objects.requireNonNull(voucherListResponse).getCode() == 200) {
                            AppUtils.showToast(getActivity(), voucherListResponse.getMessage());
                            ticketListMerchantModel.clear();
                            ticketListMerchantApi(currentPage);
                        } else if (Objects.requireNonNull(voucherListResponse).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), voucherListResponse.getMessage());
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
}
