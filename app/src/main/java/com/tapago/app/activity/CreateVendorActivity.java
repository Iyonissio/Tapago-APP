package com.tapago.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.tapago.app.R;
import com.tapago.app.adapter.VendorAdapter;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.GetVendorModel.Datum;
import com.tapago.app.model.GetVendorModel.GetVendorModel;
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

public class CreateVendorActivity extends BaseActivity {

    @BindView(R.id.recycleVendorList)
    RecyclerView recycleVendorList;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    VendorAdapter vendorAdapter;
    ArrayList<Datum> vendorListUser = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private int TOTAL_PAGES;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vendor);
        ButterKnife.bind(this);

        txtName.setText(MySharedPreferences.getMySharedPreferences().getCreateVendor());

        setRecyclerView();
        vendorListUserApi(currentPage);
    }

    @OnClick({R.id.ivBackArrow, R.id.floatingActionButton, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.floatingActionButton:
                Intent intent = new Intent(getActivity(), AddVendorActivity.class);
                startActivity(intent);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.txtTryAgain:
                vendorListUser.clear();
                vendorListUserApi(currentPage);
                break;
        }
    }


    //Setting recycler view
    private void setRecyclerView() {
        recycleVendorList.setHasFixedSize(true);
        vendorAdapter = new VendorAdapter(getActivity(), vendorListUser, CreateVendorActivity.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleVendorList.setLayoutManager(linearLayoutManager);//Linear Items
        recycleVendorList.setItemAnimator(new DefaultItemAnimator());
        recycleVendorList.setAdapter(vendorAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                vendorListUserApi(current_page);
            }
        };
        recycleVendorList.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    /**
     * venodor List
     *
     * @param page
     */
    private void vendorListUserApi(final int page) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            if (page == 1) {
                try {
                    mainProgress.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                vendorAdapter.showLoadMore(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("page_number", String.valueOf(page));
            params.put("pagination_flag", "true");


            Call<GetVendorModel> call;
            call = RetrofitRestClient.getInstance().getVendorApi(params);

            if (call == null) return;

            call.enqueue(new Callback<GetVendorModel>() {
                @Override
                public void onResponse(@NonNull final Call<GetVendorModel> call, @NonNull Response<GetVendorModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final GetVendorModel getVendorModel;
                    if (response.isSuccessful()) {
                        getVendorModel = response.body();
                        if (Objects.requireNonNull(getVendorModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                TOTAL_PAGES = getVendorModel.getPageCount();
                                vendorListUser.addAll(getVendorModel.getData());
                                vendorAdapter = new VendorAdapter(getActivity(), vendorListUser, CreateVendorActivity.this);
                                recycleVendorList.setAdapter(vendorAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(getVendorModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(getVendorModel).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(getVendorModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), getVendorModel.getMessage());
                            endlessRecyclerOnScrollListener.previousState();
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                        endlessRecyclerOnScrollListener.previousState();
                    }
                    vendorAdapter.showLoadMore(false);
                }

                @Override
                public void onFailure(@NonNull Call<GetVendorModel> call, @NonNull Throwable t) {
                    try {
                        vendorAdapter.showLoadMore(false);
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


    /**
     * inActiveVendorApi
     */
    public void inActiveVendorApi(String status, String vendorId) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("status", status);
            params.put("vendor_id", vendorId);
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().approvedVendorApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull final Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            vendorListUser.clear();
                            vendorListUserApi(currentPage);
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
        } else {
            showSnackBar(getActivity(), getString(R.string.no_internet));
        }
    }


}
