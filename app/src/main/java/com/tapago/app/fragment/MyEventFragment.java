package com.tapago.app.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tapago.app.R;
import com.tapago.app.activity.EditEventActivity;
import com.tapago.app.activity.EventDescription;
import com.tapago.app.activity.LoginActivity;
import com.tapago.app.activity.MainActivity;
import com.tapago.app.adapter.MyEventAdapter;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.MyEventModel.Datum;
import com.tapago.app.model.MyEventModel.MyEventResponse;
import com.tapago.app.pagination.EndlessRecyclerOnScrollListener;
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
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("ValidFragment")
public class MyEventFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.recycleViewMyEvents)
    RecyclerView recycleViewMyEvents;
    MyEventAdapter myEventAdapter;
    List<Datum> myEventList = new ArrayList<>();
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private int TOTAL_PAGES;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;

    private ProgressDialog progressDialog;
    MainActivity mainActivity;

    @SuppressLint("ValidFragment")
    public MyEventFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_my_event, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtNoDataFound.setText(MySharedPreferences.getMySharedPreferences().getNoEventFound());
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.notificationListUserApi();
        setRecyclerView();
        myEventList.clear();
        myEventListApi(currentPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.txtTryAgain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtTryAgain:
                myEventList.clear();
                myEventListApi(currentPage);
                break;
        }
    }

    //Setting recycler view
    private void setRecyclerView() {
        recycleViewMyEvents.setHasFixedSize(true);
        myEventAdapter = new MyEventAdapter(getActivity(), myEventList, MyEventFragment.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleViewMyEvents.setLayoutManager(linearLayoutManager);//Linear Items
        recycleViewMyEvents.setItemAnimator(new DefaultItemAnimator());
        recycleViewMyEvents.setAdapter(myEventAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                myEventListApi(current_page);
            }
        };
        recycleViewMyEvents.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    /**
     * myEvent List Api
     */
    private void myEventListApi(final int page) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            if (page == 1) {
                try {
                    mainProgress.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                myEventAdapter.showLoadMore(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("page_number", String.valueOf(page));

            Call<MyEventResponse> call;
            call = RetrofitRestClient.getInstance().myEventList(params);

            if (call == null) return;

            call.enqueue(new Callback<MyEventResponse>() {
                @Override
                public void onResponse(@NonNull final Call<MyEventResponse> call, @NonNull Response<MyEventResponse> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final MyEventResponse myEventResponse;
                    if (response.isSuccessful()) {
                        myEventResponse = response.body();
                        if (Objects.requireNonNull(myEventResponse).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                TOTAL_PAGES = myEventResponse.getPageCount();
                                myEventList.addAll(myEventResponse.getData());
                                myEventAdapter = new MyEventAdapter(getActivity(), myEventList, MyEventFragment.this);
                                recycleViewMyEvents.setAdapter(myEventAdapter);
                                myEventAdapter.setOnItemClickLister(new MyEventAdapter.OnItemClickLister() {
                                    @Override
                                    public void itemClicked(View view, int position) {
                                        RestConstant.CLICK_MY_EVENT = "MyEvent";
                                        Intent i = new Intent(getActivity(), EventDescription.class);
                                        i.putExtra("eventId", String.valueOf(myEventList.get(position).getEventId()));
                                        i.putExtra("cityName", myEventList.get(position).getCityName());
                                        i.putExtra("categoryId", String.valueOf(myEventList.get(position).getCategoryId()));
                                        i.putExtra("image", myEventList.get(position).getEventBannerImg());
                                        i.putExtra("eventName", myEventList.get(position).getEventTitle());
                                        i.putExtra("eventDes", myEventList.get(position).getEventDescription());
                                        i.putExtra("eventVenue", myEventList.get(position).getEventVenue());
                                        i.putExtra("eventAddress", myEventList.get(position).getEventAddress());
                                        i.putExtra("eventStartDate", myEventList.get(position).getEventDate());
                                        i.putExtra("eventStartTime", myEventList.get(position).getEventStartDateTime());
                                        i.putExtra("eventEndDate", myEventList.get(position).getEventEnddateTimeSort());
                                        i.putExtra("eventEndTime", myEventList.get(position).getEventEnddateTime());
                                        i.putExtra("eventCreateDate", myEventList.get(position).getCreateDate());
                                        i.putExtra("eventCreateTime", myEventList.get(position).getCreateTime());
                                        i.putExtra("eventOrganiserName", myEventList.get(position).getOrganiser_name());
                                        i.putExtra("eventOrganiserImage", myEventList.get(position).getOrganiset_image());
                                        i.putExtra("merchantId", myEventList.get(position).getMerchantId());
                                        i.putExtra("categoryName", myEventList.get(position).getCategoryName());
                                        startActivity(i);
                                        AppUtils.startFromRightToLeft(getActivity());
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(myEventResponse).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(myEventResponse).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(myEventResponse.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                                myEventAdapter.showLoadMore(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), myEventResponse.getMessage());
                            endlessRecyclerOnScrollListener.previousState();
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                        endlessRecyclerOnScrollListener.previousState();
                    }
                    myEventAdapter.showLoadMore(false);
                }

                @Override
                public void onFailure(@NonNull Call<MyEventResponse> call, @NonNull Throwable t) {
                    try {
                        myEventAdapter.showLoadMore(false);
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

    public void showSnackBar(Context context, String message) {
        try {
            ViewGroup view = (ViewGroup) ((ViewGroup) Objects.requireNonNull(getActivity()).findViewById(android.R.id.content)).getChildAt(0);
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE);
            ViewGroup viewGroup = (ViewGroup) snackbar.getView();
            viewGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            View viewTv = snackbar.getView();
            TextView tv = viewTv.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(ContextCompat.getColor(context, R.color.red));
            tv.setMaxLines(5);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextActivity(String eventId, String paymentStatus) {
        Intent i = new Intent(getActivity(), EditEventActivity.class);
        i.putExtra("eventId", eventId);
        i.putExtra("paymentStatus", paymentStatus);
        startActivity(i);
        AppUtils.startFromRightToLeft(getActivity());
    }


    public void logout() {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
        mySharedPreferences.setUserId("");
        mySharedPreferences.setLogin(false);
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        Objects.requireNonNull(getActivity()).finish();
        AppUtils.startFromRightToLeft(getActivity());
    }

    public void eventDeleteDialog(final String eventId) {
        new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setMessage(getString(R.string.are_delete))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        eventDeleteApi(eventId);
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }


    private void eventDeleteApi(String eventId) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("event_id", eventId);

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().eventDeleteApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull final Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            showSnackBar(getActivity(), basicModel.getMessage());
                            myEventList.clear();
                            myEventListApi(currentPage);
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

    public void showProgressDialog(Context ctx) {
        progressDialog = ProgressDialog.show(ctx, "", getString(R.string.str_please_wait), true, false);
    }


    public void hideProgressDialog() {
        try {
            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
