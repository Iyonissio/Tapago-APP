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
import com.tapago.app.adapter.NotificationUserListAdapter;
import com.tapago.app.model.NotificationModel.Datum;
import com.tapago.app.model.NotificationModel.NotificationModel;
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

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.recycleViewNotification)
    RecyclerView recycleViewNotification;

    NotificationAdapter notificationAdapter;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    ArrayList<Datum> notificationModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        txtName.setText(MySharedPreferences.getMySharedPreferences().getNotification());

        setRecyclerView();
        notificationListUserApi();
    }

    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                notificationListUserApi();
                break;
        }
    }

    //Setting recycler view
    private void setRecyclerView() {
        recycleViewNotification.setHasFixedSize(true);
        notificationAdapter = new NotificationAdapter(getActivity(), notificationModels);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleViewNotification.setLayoutManager(linearLayoutManager);//Linear Items
        recycleViewNotification.setItemAnimator(new DefaultItemAnimator());
        recycleViewNotification.setAdapter(notificationAdapter);
    }

    private void notificationListUserApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("is_read","Y");

            Call<NotificationModel> call;
            call = RetrofitRestClient.getInstance().notificationApi(params);

            if (call == null) return;

            call.enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(@NonNull final Call<NotificationModel> call, @NonNull Response<NotificationModel> response) {
                   hideProgressDialog();
                    final NotificationModel notificationModel;
                    if (response.isSuccessful()) {
                        notificationModel = response.body();
                        if (Objects.requireNonNull(notificationModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                notificationModels.addAll(notificationModel.getData());
                                notificationAdapter = new NotificationAdapter(getActivity(), notificationModels);
                                recycleViewNotification.setAdapter(notificationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(notificationModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(notificationModel).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(notificationModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), notificationModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NotificationModel> call, @NonNull Throwable t) {
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
