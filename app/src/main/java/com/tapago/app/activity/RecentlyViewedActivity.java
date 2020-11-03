package com.tapago.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tapago.app.R;
import com.tapago.app.adapter.RecentlyViewedAdapter;
import com.tapago.app.model.RecentViewModel.Datum;
import com.tapago.app.model.RecentViewModel.RecentViewModel;
import com.tapago.app.rest.RestConstant;
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

public class RecentlyViewedActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.recycleViewRecentlyViewed)
    RecyclerView recycleViewRecentlyViewed;
    RecentlyViewedAdapter recentlyViewedAdapter;
    List<Datum> recentViewList = new ArrayList<>();
    @BindView(R.id.txtTryAgain)
    TextView txtTryAgain;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_viewed);
        ButterKnife.bind(this);

        txtNoDataFound.setText(MySharedPreferences.getMySharedPreferences().getNoEventFound());
        recycleViewRecentlyViewed.setLayoutManager(new LinearLayoutManager(getActivity()));
        txtName.setText(MySharedPreferences.getMySharedPreferences().getRecentlyView());

        recentViewListApi();
    }

    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                recentViewListApi();
                break;
        }
    }

    private void recentViewListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            recentViewList.clear();
            try {
                mainProgress.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("limit", "true");
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<RecentViewModel> call;
            call = RetrofitRestClient.getInstance().recentViewList(params);

            if (call == null) return;

            call.enqueue(new Callback<RecentViewModel>() {
                @Override
                public void onResponse(@NonNull final Call<RecentViewModel> call, @NonNull Response<RecentViewModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final RecentViewModel recentViewModel;
                    if (response.isSuccessful()) {
                        recentViewModel = response.body();
                        if (Objects.requireNonNull(recentViewModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                recentViewList.addAll(recentViewModel.getData());
                                recentlyViewedAdapter = new RecentlyViewedAdapter(getActivity(), recentViewList);
                                recycleViewRecentlyViewed.setAdapter(recentlyViewedAdapter);
                                recentlyViewedAdapter.setOnItemClickLister(new RecentlyViewedAdapter.OnItemClickLister() {
                                    @Override
                                    public void itemClicked(View view, int position) {
                                        RestConstant.CLICK_MY_EVENT = "";
                                        Intent i = new Intent(getActivity(), EventDescription.class);
                                        i.putExtra("eventId", String.valueOf(recentViewList.get(position).getEventId()));
                                        i.putExtra("cityName", recentViewList.get(position).getCityName());
                                        i.putExtra("categoryId", String.valueOf(recentViewList.get(position).getCategoryId()));
                                        i.putExtra("image", recentViewList.get(position).getEventBannerImg());
                                        i.putExtra("eventName", recentViewList.get(position).getEventTitle());
                                        i.putExtra("eventDes", recentViewList.get(position).getEventDescription());
                                        i.putExtra("eventVenue", recentViewList.get(position).getEventVenue());
                                        i.putExtra("eventAddress", recentViewList.get(position).getEventAddress());
                                        i.putExtra("eventStartDate", recentViewList.get(position).getEventDate());
                                        i.putExtra("eventStartTime", recentViewList.get(position).getEventStartDateTime());
                                        i.putExtra("eventEndDate", recentViewList.get(position).getEventEnddateTimeSort());
                                        i.putExtra("eventEndTime", recentViewList.get(position).getEventEnddateTime());
                                        i.putExtra("eventCreateDate", recentViewList.get(position).getCreateDate());
                                        i.putExtra("eventCreateTime", recentViewList.get(position).getCreateTime());
                                        i.putExtra("eventOrganiserName", recentViewList.get(position).getOrganiser_name());
                                        i.putExtra("eventOrganiserImage", recentViewList.get(position).getOrganiset_image());
                                        i.putExtra("merchantId", recentViewList.get(position).getMerchantId());
                                        i.putExtra("categoryName", recentViewList.get(position).getCategoryName());
                                        startActivity(i);
                                        AppUtils.startFromRightToLeft(getActivity());
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(recentViewModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(recentViewModel).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(recentViewModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), recentViewModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RecentViewModel> call, @NonNull Throwable t) {
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
