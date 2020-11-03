package com.tapago.app.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tapago.app.R;
import com.tapago.app.adapter.TicketQrListAdapter;
import com.tapago.app.adapter.VouchersQrListAdapter;
import com.tapago.app.dialog.QrCodeDialog;
import com.tapago.app.model.UserTicketQr.Datum;
import com.tapago.app.model.UserTicketQr.TicketQrModel;
import com.tapago.app.model.UserVoucherQr.VoucherQrModel;
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

public class TicketQrCodeActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.recycleVouchersList)
    RecyclerView recycleVouchersList;
    TicketQrListAdapter ticketQrListAdapter;
    ArrayList<Datum> datumArrayList = new ArrayList<>();
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private int TOTAL_PAGES;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private QrCodeDialog qrCodeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers_list);
        ButterKnife.bind(this);

        qrCodeDialog = new QrCodeDialog(this);

        setRecyclerView();
        txtName.setText(getString(R.string.ticket_qr_code));
        voucherListUserApi(currentPage);
    }

    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                datumArrayList.clear();
                voucherListUserApi(currentPage);
                break;

        }
    }

    //Setting recycler view
    private void setRecyclerView() {
        recycleVouchersList.setHasFixedSize(true);
        ticketQrListAdapter = new TicketQrListAdapter(getActivity(), datumArrayList, TicketQrCodeActivity.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleVouchersList.setLayoutManager(linearLayoutManager);//Linear Items
        recycleVouchersList.setItemAnimator(new DefaultItemAnimator());
        recycleVouchersList.setAdapter(ticketQrListAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                voucherListUserApi(current_page);
            }
        };
        recycleVouchersList.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    private void voucherListUserApi(final int page) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            if (page == 1) {
                try {
                    mainProgress.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ticketQrListAdapter.showLoadMore(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("page_number", String.valueOf(page));

            Call<TicketQrModel> call;
            call = RetrofitRestClient.getInstance().ticketQrApi(params);

            if (call == null) return;

            call.enqueue(new Callback<TicketQrModel>() {
                @Override
                public void onResponse(@NonNull final Call<TicketQrModel> call, @NonNull Response<TicketQrModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final TicketQrModel ticketQrModel;
                    if (response.isSuccessful()) {
                        ticketQrModel = response.body();
                        if (Objects.requireNonNull(ticketQrModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                TOTAL_PAGES = ticketQrModel.getPageCount();
                                datumArrayList.addAll(ticketQrModel.getData());
                                ticketQrListAdapter = new TicketQrListAdapter(getActivity(), datumArrayList, TicketQrCodeActivity.this);
                                recycleVouchersList.setAdapter(ticketQrListAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(ticketQrModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(ticketQrModel).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(ticketQrModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                                ticketQrListAdapter.showLoadMore(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), ticketQrModel.getMessage());
                            endlessRecyclerOnScrollListener.previousState();
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                        endlessRecyclerOnScrollListener.previousState();
                    }
                    ticketQrListAdapter.showLoadMore(false);
                }

                @Override
                public void onFailure(@NonNull Call<TicketQrModel> call, @NonNull Throwable t) {
                    try {
                        ticketQrListAdapter.showLoadMore(false);
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


    public void openQrDialog(String qrCode) {
        qrCodeDialog.progressBar.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(qrCode)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        qrCodeDialog.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(qrCodeDialog.imgQrCode);
        qrCodeDialog.show();
    }

}
