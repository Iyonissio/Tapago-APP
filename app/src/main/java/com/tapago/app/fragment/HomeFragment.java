package com.tapago.app.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.activity.CategoryMainActivity;
import com.tapago.app.activity.EventActivity;
import com.tapago.app.activity.EventDescription;
import com.tapago.app.activity.LoginActivity;
import com.tapago.app.activity.MainActivity;
import com.tapago.app.adapter.EventListAdapter;
import com.tapago.app.model.EventListModel.EventList;
import com.tapago.app.model.EventListModel.EventModel;
import com.tapago.app.pagination.EndlessRecyclerOnScrollListener;
import com.tapago.app.rest.RestConstant;
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
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.txtDate)
    AppCompatTextView txtDate;
    @BindView(R.id.txtEventName)
    AppCompatTextView txtEventName;
    @BindView(R.id.txtEventDes)
    AppCompatTextView txtEventDes;
    @BindView(R.id.imgBanner)
    AppCompatImageView imgBanner;
    @BindView(R.id.rcEventList)
    RecyclerView rcEventList;
    @BindView(R.id.txtPopular)
    AppCompatTextView txtPopular;
    @BindView(R.id.txtSlug)
    AppCompatTextView txtSlug;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.lrPopular)
    LinearLayout lrPopular;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.lrdescription)
    LinearLayout lrdescription;
    @BindView(R.id.lrMain)
    LinearLayout lrMain;
    private ArrayList<EventList> eventArrayList = new ArrayList<>();
    EventListAdapter eventListAdapter;
    String strEventVenue = "", strEventAddress = "", strEventStartDate = "", strEventStartTime = "", strEventCreateDate = "", strEventCreateTime = "", strOrganiserName = "", strEventId = "",
            strOrganiserImage = "", strImageBanner = "", strCategoryId = "", strCityName = "", strMerchentId = "", strCategoryName = "", strEventEndDate = "", strEventEndTime = "";
    LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private int TOTAL_PAGES;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    MainActivity mainActivity;

    @SuppressLint("ValidFragment")
    public HomeFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("3")) {
            lrMain.setVisibility(View.VISIBLE);
            coordinatorLayout.setVisibility(View.GONE);
        } else {
            lrMain.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
            txtPopular.setText(MySharedPreferences.getMySharedPreferences().getPopularEvent());
            txtSlug.setText(MySharedPreferences.getMySharedPreferences().getSlug());
            txtNoDataFound.setText(MySharedPreferences.getMySharedPreferences().getNoEventFound());
            txtEventName.setSelected(true);
            setRecyclerView();
            eventListApi(currentPage);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.notificationListUserApi();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rlEvent, R.id.txtTryAgain, R.id.btnSupermarket, R.id.btnEvent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlEvent:
                RestConstant.CLICK_MY_EVENT = "HomeFragment";
                Intent i1 = new Intent(getActivity(), EventDescription.class);
                i1.putExtra("eventId", strEventId);
                i1.putExtra("cityName", strCityName);
                i1.putExtra("categoryId", strCategoryId);
                i1.putExtra("image", strImageBanner);
                i1.putExtra("eventName", AppUtils.getText(txtEventName));
                i1.putExtra("eventDes", AppUtils.getText(txtEventDes));
                i1.putExtra("eventVenue", strEventVenue);
                i1.putExtra("eventAddress", strEventAddress);
                i1.putExtra("eventStartDate", strEventStartDate);
                i1.putExtra("eventStartTime", strEventStartTime);
                i1.putExtra("eventEndDate", strEventEndDate);
                i1.putExtra("eventEndTime", strEventEndTime);
                i1.putExtra("eventCreateDate", strEventCreateDate);
                i1.putExtra("eventCreateTime", strEventCreateTime);
                i1.putExtra("eventOrganiserName", strOrganiserName);
                i1.putExtra("eventOrganiserImage", strOrganiserImage);
                i1.putExtra("merchantId", strMerchentId);
                i1.putExtra("categoryName", strCategoryName);
                startActivity(i1);
                break;
            case R.id.txtTryAgain:
                eventArrayList.clear();
                eventListApi(currentPage);
                break;
            case R.id.btnSupermarket:
                Intent intent = new Intent(getActivity(), CategoryMainActivity.class);
                startActivity(intent);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.btnEvent:
                Intent intent1 = new Intent(getActivity(), EventActivity.class);
                startActivity(intent1);
                AppUtils.startFromRightToLeft(getActivity());
                break;
        }

    }

    //Setting recycler view
    private void setRecyclerView() {
        rcEventList.setHasFixedSize(true);
        eventListAdapter = new EventListAdapter(getActivity(), eventArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcEventList.setLayoutManager(linearLayoutManager);//Linear Items
        rcEventList.setItemAnimator(new DefaultItemAnimator());
        rcEventList.setAdapter(eventListAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                eventListApi(current_page);
            }
        };
        rcEventList.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    /**
     * eventList Api
     */
    private void eventListApi(final int page) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            if (page == 1) {
                try {
                    mainProgress.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                eventListAdapter.showLoadMore(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("category_id", MySharedPreferences.getMySharedPreferences().getCategoryId());
            params.put("city", MySharedPreferences.getMySharedPreferences().getCity());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("page_number", String.valueOf(page));

            Call<EventModel> call;
            call = RetrofitRestClient.getInstance().eventList(params);

            if (call == null) return;

            call.enqueue(new Callback<EventModel>() {
                @Override
                public void onResponse(@NonNull Call<EventModel> call, @NonNull Response<EventModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final EventModel eventModel;
                    if (response.isSuccessful()) {
                        eventModel = response.body();
                        if (Objects.requireNonNull(eventModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                lrPopular.setVisibility(View.VISIBLE);
                                coordinatorLayout.setVisibility(View.VISIBLE);
                                TOTAL_PAGES = eventModel.getPageCount();
                                RequestOptions options = new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder);
                                Glide.with(Objects.requireNonNull(getActivity())).load(eventModel.getPopularEvent().getEventBannerImg()).apply(options).into(imgBanner);
                                txtEventName.setText(eventModel.getPopularEvent().getEventTitle());
                                txtEventDes.setText(eventModel.getPopularEvent().getEventDescription());
                                txtDate.setText(eventModel.getPopularEvent().getEventStartDateTimeShort());
                                strCategoryId = String.valueOf(eventModel.getPopularEvent().getCategoryId());
                                strEventId = String.valueOf(eventModel.getPopularEvent().getEventId());
                                strImageBanner = eventModel.getPopularEvent().getEventBannerImg();
                                strEventVenue = eventModel.getPopularEvent().getEventVenue();
                                strEventAddress = eventModel.getPopularEvent().getEventAddress();
                                strCityName = eventModel.getPopularEvent().getCityName();
                                strEventCreateDate = eventModel.getPopularEvent().getCreateDate();
                                strEventCreateTime = eventModel.getPopularEvent().getCreateTime();
                                strEventStartDate = eventModel.getPopularEvent().getEventDate();
                                strEventStartTime = eventModel.getPopularEvent().getEventStartDateTime();
                                strOrganiserName = eventModel.getPopularEvent().getOrganiser_name();
                                strOrganiserImage = eventModel.getPopularEvent().getOrganiser_name();
                                strMerchentId = eventModel.getPopularEvent().getMerchantId();
                                strCategoryName = eventModel.getPopularEvent().getCategoryName();
                                strEventEndDate = eventModel.getPopularEvent().getEventEndDate();
                                strEventEndTime = eventModel.getPopularEvent().getEventEnddateTime();
                                eventArrayList.addAll(eventModel.getEventList());
                                eventListAdapter = new EventListAdapter(getActivity(), eventArrayList);
                                rcEventList.setAdapter(eventListAdapter);
                                eventListAdapter.setOnItemClickLister(new EventListAdapter.OnItemClickLister() {
                                    @Override
                                    public void itemClicked(View view, int position) {
                                        RestConstant.CLICK_MY_EVENT = "HomeFragment";
                                        Intent i = new Intent(getActivity(), EventDescription.class);
                                        i.putExtra("eventId", String.valueOf(eventArrayList.get(position).getEventId()));
                                        i.putExtra("cityName", eventArrayList.get(position).getCityName());
                                        i.putExtra("categoryId", String.valueOf(eventArrayList.get(position).getCategoryId()));
                                        i.putExtra("image", eventArrayList.get(position).getEventBannerImg());
                                        i.putExtra("eventName", eventArrayList.get(position).getEventTitle());
                                        i.putExtra("eventDes", eventArrayList.get(position).getEventDescription());
                                        i.putExtra("eventVenue", eventArrayList.get(position).getEventVenue());
                                        i.putExtra("eventAddress", eventArrayList.get(position).getEventAddress());
                                        i.putExtra("eventStartDate", eventArrayList.get(position).getEventDate());
                                        i.putExtra("eventStartTime", eventArrayList.get(position).getEventStartDateTime());
                                        i.putExtra("eventEndDate", eventArrayList.get(position).getEventEndDate());
                                        i.putExtra("eventEndTime", eventArrayList.get(position).getEventEnddateTime());
                                        i.putExtra("eventCreateDate", eventArrayList.get(position).getCreateDate());
                                        i.putExtra("eventCreateTime", eventArrayList.get(position).getCreateTime());
                                        i.putExtra("eventOrganiserName", eventArrayList.get(position).getOrganiser_name());
                                        i.putExtra("eventOrganiserImage", eventArrayList.get(position).getOrganiset_image());
                                        i.putExtra("merchantId", eventArrayList.get(position).getMerchantId());
                                        i.putExtra("categoryName", eventArrayList.get(position).getCategoryName());
                                        startActivity(i);
                                        AppUtils.startFromRightToLeft(getActivity());
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(eventModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(eventModel).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(eventModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                lrPopular.setVisibility(View.GONE);
                                coordinatorLayout.setVisibility(View.GONE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                    lrPopular.setVisibility(View.VISIBLE);
                                    coordinatorLayout.setVisibility(View.VISIBLE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                                eventListAdapter.showLoadMore(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            lrPopular.setVisibility(View.GONE);
                            coordinatorLayout.setVisibility(View.GONE);
                            showSnackBar(getActivity(), eventModel.getMessage());
                            endlessRecyclerOnScrollListener.previousState();
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                        endlessRecyclerOnScrollListener.previousState();
                    }
                    eventListAdapter.showLoadMore(false);
                }

                @Override
                public void onFailure(@NonNull Call<EventModel> call, @NonNull Throwable t) {
                    try {
                        eventListAdapter.showLoadMore(false);
                        endlessRecyclerOnScrollListener.previousState();
                        mainProgress.setVisibility(View.GONE);
                        if (t instanceof SocketTimeoutException) {
                            txtNoDataFound.setText(getString(R.string.connection_timeout));
                            txtTryAgain.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                            lrPopular.setVisibility(View.GONE);
                            coordinatorLayout.setVisibility(View.GONE);
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
                            lrPopular.setVisibility(View.GONE);
                            coordinatorLayout.setVisibility(View.GONE);
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
            lrPopular.setVisibility(View.GONE);
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
}
