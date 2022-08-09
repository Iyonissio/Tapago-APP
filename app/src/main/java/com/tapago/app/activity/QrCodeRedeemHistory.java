package com.tapago.app.activity;


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
import com.tapago.app.adapter.RedeemQrAdapter;
import com.tapago.app.model.RedeemModel.Datum;
import com.tapago.app.model.RedeemModel.RedeemModel;
import com.tapago.app.pagination.EndlessRecyclerOnScrollListener;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeRedeemHistory extends BaseActivity {

    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    @BindView(R.id.recycleRedeemList)
    RecyclerView recycleRedeemList;
    ArrayList<Datum> redeemListUser = new ArrayList<>();
    RedeemQrAdapter redeemQrAdapter;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private int TOTAL_PAGES;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_qr_code);
        ButterKnife.bind(this);

        setRecyclerView();
        redeemListUserApi(currentPage);
    }


    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                System.out.println("Entrou no voltar-------------");
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                System.out.print("Clicou em tente novamente QrCodeRedeem----");
                redeemListUser.clear();
                redeemListUserApi(currentPage);
                break;
        }
    }

    //Setting recycler view
    private void setRecyclerView() {
        System.out.println("setRecyclerView");
        recycleRedeemList.setHasFixedSize(true);
        System.out.println("setRecyclerView 2");
        redeemQrAdapter = new RedeemQrAdapter(getActivity(), redeemListUser);
        System.out.println("setRecyclerView 3");
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleRedeemList.setLayoutManager(linearLayoutManager);//Linear Items
        recycleRedeemList.setItemAnimator(new DefaultItemAnimator());
        recycleRedeemList.setAdapter(redeemQrAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                redeemListUserApi(current_page);
            }
        };
        recycleRedeemList.addOnScrollListener(endlessRecyclerOnScrollListener);
    }


    private void redeemListUserApi(final int page) {
        System.out.println("Entrou na redeemListUserApi -----------------------");
        if (AppUtils.isConnectedToInternet(getActivity())) {
            System.out.println("Entrou APP Connectada -----------------------");
            if (page == 1) {
                try {
                    mainProgress.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                redeemQrAdapter.showLoadMore(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("page_number", String.valueOf(page));
            System.out.println("DADOS DO HAS ABAIXO---------------------");
            System.out.println(Arrays.asList(params));

            Call<RedeemModel> call;
            System.out.println("Apos o Call 1---------------------");
            call = RetrofitRestClient.getInstance().redeemQrCodeHistoryApi(params);
            System.out.println("Apos o Call 2---------------------");

            System.out.println("Print CALL");
            System.out.println(call);
            System.out.println("Print CALL FIM");
            if (call == null) return;
            System.out.println("--------------------Apos IF------");
            call.enqueue(new Callback<RedeemModel>() {
                @Override
                public void onResponse(@NonNull final Call<RedeemModel> call, @NonNull Response<RedeemModel> response) {
                    System.out.println("--------------------onResponse------");
                    System.out.println(response.body());
                    System.out.println("--------------------onResponse END------");
                    try {
                        System.out.println("--------------------onResponse 1.1------");
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        System.out.println("--------------------onResponse 1.2------");
                        e.printStackTrace();
                    }
                    final RedeemModel redeemModel;
                    System.out.println("--------------------Apos o Final------");
                    if (response.isSuccessful()) {
                        System.out.println("--------------------Apos o isSuccessful------");
                        redeemModel = response.body();
                        if (Objects.requireNonNull(redeemModel).getCode() == 200) {
                            System.out.println("--------------------Apos o isSuccessful getCode------");
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                TOTAL_PAGES = redeemModel.getPageCount();
                                redeemListUser.addAll(redeemModel.getData());

                                System.out.println(redeemModel.getData());
                                redeemQrAdapter = new RedeemQrAdapter(getActivity(), redeemListUser);
                                recycleRedeemList.setAdapter(redeemQrAdapter);
                                System.out.println("List Data "+ recycleRedeemList);
                                System.out.println("--------------------TRY------");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(redeemModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(redeemModel).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(redeemModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                                redeemQrAdapter.showLoadMore(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), redeemModel.getMessage());
                            endlessRecyclerOnScrollListener.previousState();
                        }
                    } else {
                        System.out.println("-------------------on response INSUCESSO------");
                        showSnackBar(getActivity(), response.message());
                        endlessRecyclerOnScrollListener.previousState();
                    }
                    redeemQrAdapter.showLoadMore(false);
                }

                @Override
                public void onFailure(@NonNull Call<RedeemModel> call, @NonNull Throwable t) {
                    try {
                        redeemQrAdapter.showLoadMore(false);
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
