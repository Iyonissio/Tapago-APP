package com.tapago.app.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.tapago.app.R;
import com.tapago.app.adapter.CategoryAdapter;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.CategoryCaption.Category;
import com.tapago.app.model.CategoryList.CategoryList;
import com.tapago.app.model.CategoryList.Datum;
import com.tapago.app.pagination.EndlessRecyclerOnScrollListener;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.txtText)
    AppCompatTextView txtText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btnNext)
    AppCompatButton btnNext;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    private ArrayList<Datum> categoryArrayList = new ArrayList<>();
    CategoryAdapter categoryAdapter;
    private StringBuilder sb, sb1;
    String strCategories = "";
    private static final int RC_LOCATION_PERM = 124;
    private static final int REQUEST_LOCATION = 199;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude = 0;
    private double currentLongitude = 0;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    PendingResult<LocationSettingsResult> result;
    String locality = "";
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private int TOTAL_PAGES;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        sb = new StringBuilder();
        sb1 = new StringBuilder();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setRecyclerView();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            callLocation();
        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                // mMap.setMyLocationEnabled(true);
            }
        }

        categoryCaptionApi();
    }


    //Setting recycler view
    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(getActivity(), categoryArrayList);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);//Linear Items
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoryAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                categoryListApi(current_page);
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        AppUtils.finishFromLeftToRight(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }


    @AfterPermissionGranted(RC_LOCATION_PERM)
    private void callLocation() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Have permission, do the thing!
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                // mMap.setMyLocationEnabled(true);
            }
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, RC_LOCATION_PERM, Manifest.permission.ACCESS_FINE_LOCATION)
                            .setRationale(R.string.rationale_call)
                            .setPositiveButtonText(getString(R.string.ok))
                            .setNegativeButtonText(getString(R.string.cancel))
                            .build());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOCATION_PERM && resultCode == RESULT_OK) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                //mMap.setMyLocationEnabled(true);
            }
        } else if (requestCode == REQUEST_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_OK: {
                    // All required changes were successfully made
                    Toast.makeText(CategoryActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
                    break;
                }
                case Activity.RESULT_CANCELED: {
                    // The user was asked to change settings, but chose not to
                    Toast.makeText(CategoryActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NotNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    CategoryActivity.this,
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                //Request Location Permission
                callLocation();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                mLastLocation = location;
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @OnClick({R.id.btnNext, R.id.txtTryAgain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                sb = new StringBuilder();
                sb1 = new StringBuilder();
                if (categoryArrayList.size() > 0) {
                    for (int i = 0; i < categoryArrayList.size(); i++) {
                        if (categoryArrayList.get(i).isClicked()) {
                            sb.append(categoryArrayList.get(i).getId()).append(",");
                        }
                    }
                }

                if (currentLongitude != 0 && currentLongitude != 0) {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
                        if (addresses.size() > 0) {
                            locality = addresses.get(0).getLocality();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (sb.length() > 0) {
                    String str = sb.toString().substring(0, sb.toString().length() - 1);
                    sb = new StringBuilder();
                    sb.append("[").append(str).append("]");
                    sb1.append(str);
                    MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
                    mySharedPreferences.setCategoryId(sb.toString());
                    mySharedPreferences.setCity(locality);
                    addCategory();
                } else {
                    showSnackBar(getActivity(), strCategories);
                }
                break;
            case R.id.txtTryAgain:
                categoryArrayList.clear();
                categoryCaptionApi();
                break;
        }
    }

    /**
     * CategoryCaptionApi
     */
    private void categoryCaptionApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("activity_name", "CategoryActivity");
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<Category> call;
            call = RetrofitRestClient.getInstance().categorycaptionApi(params);

            if (call == null) return;

            call.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(@NonNull final Call<Category> call, @NonNull Response<Category> response) {
                    hideProgressDialog();
                    final Category category;
                    if (response.isSuccessful()) {
                        category = response.body();
                        if (Objects.requireNonNull(category).getCode() == 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        txtTryAgain.setVisibility(View.GONE);
                                        txtNoDataFound.setVisibility(View.GONE);
                                        txtText.setText(category.getActivity().getSelectCategories());
                                        btnNext.setText(category.getActivity().getNext());
                                        strCategories = category.getActivity().getCategorySelect();
                                        setData(category.getActivity());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            categoryListApi(currentPage);
                        } else if (Objects.requireNonNull(category).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), category.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Category> call, @NonNull Throwable t) {
                    hideProgressDialog();
                    try {
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

    private void setData(com.tapago.app.model.CategoryCaption.Activity activity) {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
        mySharedPreferences.setSelectCategories(activity.getSelectCategories());
        mySharedPreferences.setNext(activity.getNext());
        mySharedPreferences.setStrSelectCategories(activity.getCategorySelect());
        mySharedPreferences.setRecentlyView(activity.getRecentlyViewed());
        mySharedPreferences.setPaymentHistory(activity.getPaymentHistory());
        mySharedPreferences.setMyVoucher(activity.getMyVoucher());
        mySharedPreferences.setAboutUs(activity.getAboutUs());
        mySharedPreferences.setPrivacyPolicy(activity.getPrivacyPolicy());
        mySharedPreferences.setTermsCondition(activity.getTermsConditions());
        mySharedPreferences.setNeedHelp(activity.getNeedHelp());
        mySharedPreferences.setPopularEvent(activity.getPopularEvents());
        mySharedPreferences.setSlug(activity.getSlug());
        mySharedPreferences.setVersion(activity.getVirsion());
        mySharedPreferences.setHereHelp(activity.getWeAreHereToHelp());
        mySharedPreferences.setAskQuestion(activity.getAskAQuestion());
        mySharedPreferences.setSearch(activity.getSearch());
        mySharedPreferences.setNoEventFound(activity.getNoEventFound());
        mySharedPreferences.setPopularTopic(activity.getPopularTopic());
        mySharedPreferences.setEnterQuestion(activity.getPleaseEnterQuestion());
        mySharedPreferences.setFirst_Name_Caption(activity.getFirstName());
        mySharedPreferences.setLast_Name_Caption(activity.getLastName());
        mySharedPreferences.setEmail_Caption(activity.getEmail());
        mySharedPreferences.setPhone_Caption(activity.getPhone());
        mySharedPreferences.setUpdateCity(activity.getUpdateCity());
        mySharedPreferences.setChangePassword(activity.getChangePassword());
        mySharedPreferences.setSubmit(activity.getSubmit());
        mySharedPreferences.setPleaseEnterFirstName(activity.getPleaseEnterFirstName());
        mySharedPreferences.setPleaseEnterLastName(activity.getPleaseEnterLastName());
        mySharedPreferences.setPleaseEnterYourEmail(activity.getPleaseEnterYourEmail());
        mySharedPreferences.setPleaseEnterValidEmail(activity.getPleaseEnterValidEmail());
        mySharedPreferences.setPleaseEnterMobileNumber(activity.getPleaseEnterMobileNumber());
        mySharedPreferences.setPleaseEnterValidMobileNumber(activity.getPleaseEnterValidMobileNumber());
        mySharedPreferences.setOldPassword(activity.getOldPassword());
        mySharedPreferences.setNewPassword(activity.getNewPassword());
        mySharedPreferences.setConfirmPassword(activity.getConfirmPassword());
        mySharedPreferences.setPleaseEnterOldPassword(activity.getPleaseEnterOldPassword());
        mySharedPreferences.setPleaseEnterNewPassword(activity.getPleaseEnterNewPassword());
        mySharedPreferences.setPassword_should_be_minimum_6_characters(activity.getPasswordShouldBeMinimum6Characters());
        mySharedPreferences.setPlease_Enter_Confirm_Password(activity.getPleaseEnterConfirmPassword());
        mySharedPreferences.setPassword_And_Confirm_Password_Does_Not_Match(activity.getPasswordAndConfirmPasswordDoesNotMatch());
        mySharedPreferences.setEvent_Category(activity.getEventCategory());
        mySharedPreferences.setEvent_Name(activity.getEventName());
        mySharedPreferences.setEnter_Your_Event_Name(activity.getEnterYourEventName());
        mySharedPreferences.setStart_Event_Time(activity.getStartEventTime());
        mySharedPreferences.setYour_Event_Time(activity.getYourEventTime());
        mySharedPreferences.setStart_Event_Date(activity.getStartEventDate());
        mySharedPreferences.setYour_Event_Date(activity.getYourEventDate());
        mySharedPreferences.setEnd_Event_Time(activity.getEndEventTime());
        mySharedPreferences.setEnd_Event_Date(activity.getEndEventDate());
        mySharedPreferences.setEvent_Description(activity.getEventDescription());
        mySharedPreferences.setWrite_Something_About_Your_Event(activity.getWriteSomethingAboutYourEvent());
        mySharedPreferences.setEvent_Address(activity.getEventAddress());
        mySharedPreferences.setEnter_Your_Event_Address(activity.getEnterYourEventAddress());
        mySharedPreferences.setEvent_Vanue(activity.getEventVenue());
        mySharedPreferences.setEnter_Your_Event_Vanue(activity.getEnterYourEventVenue());
        mySharedPreferences.setAdd_Event_Banner(activity.getAddEventBanner());
        mySharedPreferences.setAdd_Event_Banner_Thumb(activity.getAddEventBannerThumb());
        mySharedPreferences.setBudget(activity.getBudget());
        mySharedPreferences.setCreate_Event(activity.getCreateEvent());
        mySharedPreferences.setPlease_Enter_Event_Name(activity.getPleaseEnterEventName());
        mySharedPreferences.setPlease_Select_Start_Date(activity.getPleaseSelectStartDate());
        mySharedPreferences.setPlease_Select_End_Date(activity.getPleaseSelectEndDate());
        mySharedPreferences.setPlease_Select_Start_Time(activity.getPleaseSelectStartTime());
        mySharedPreferences.setPlease_Select_End_Time(activity.getPleaseSelectEndTime());
        mySharedPreferences.setPlease_Enter_Event_Description(activity.getPleaseEnterEventDescription());
        mySharedPreferences.setPlease_Enter_Event_Address(activity.getPleaseEnterEventAddress());
        mySharedPreferences.setPlease_Enter_Event_Venue(activity.getPleaseEnterEventVenue());
        mySharedPreferences.setPlease_Enter_Event_Budget(activity.getPleaseEnterEventBudget());
        mySharedPreferences.setPlease_Select_Banner_Image(activity.getPleaseSelectBannerImage());
        mySharedPreferences.setPlease_Select_Thumb_Image(activity.getPleaseSelectThumbImage());
        mySharedPreferences.setPlease_Select_Category(activity.getPleaseSelectCategory());
        mySharedPreferences.setPlease_Enter_Lowest_Amount(activity.getPleaseEnterLowestAmount());
        mySharedPreferences.setCityCaption(activity.getCity());
        mySharedPreferences.setEnterCity(activity.getEnterEventCity());
        mySharedPreferences.setAbout(activity.getAbout());
        mySharedPreferences.setVenue(activity.getVenue());
        mySharedPreferences.setDirection(activity.getGetDirection());
        mySharedPreferences.setOrganiser(activity.getOrganiser());
        mySharedPreferences.setMoreLike(activity.getMoreLikeThis());
        mySharedPreferences.setRequestVoucher(activity.getRequestVoucher());
        mySharedPreferences.setRedeemVoucher(activity.getRedeemVoucher());
        mySharedPreferences.setEnterCityName(activity.getEnterCityName());
        mySharedPreferences.setAddMultiple(activity.getYouMayAddMultiple());
        mySharedPreferences.setUpdateEvent(activity.getUpdateEvent());
        mySharedPreferences.setVoucherType(activity.getVoucherType());
        mySharedPreferences.setAfterEndDate(activity.getAfterEndDate());
        mySharedPreferences.setTicket(activity.getTicket());
        mySharedPreferences.setQuantity(activity.getQuantity());
        mySharedPreferences.setAmount(activity.getAmount());
        mySharedPreferences.setTotal(activity.getTotal());
        mySharedPreferences.setFilledAllDetail(activity.getFilledAllDetails());
        mySharedPreferences.setVoucher(activity.getVoucher());
        mySharedPreferences.setBookTicket(activity.getBookTicket());
        mySharedPreferences.setSelectVoucher(activity.getSelectVoucherType());
        mySharedPreferences.setPerVoucher(activity.getPerVoucher());
        mySharedPreferences.setNumberOfVoucher(activity.getNumberOfVoucher());
        mySharedPreferences.setAmountToBePaid(activity.getAmountToBePaid());
        mySharedPreferences.setProceed(activity.getProceed());
        mySharedPreferences.setPleaseSelectVoucherType(activity.getPleaseSelectVoucherType());
        mySharedPreferences.setPleaseSelectNumVoucherType(activity.getPleaseSelectNumberOfVoucher());
        mySharedPreferences.setVoucherList(activity.getVoucherList());
        mySharedPreferences.setTicketList(activity.getTicketList());
        mySharedPreferences.setPrice(activity.getPrice());
        mySharedPreferences.setApproved(activity.getAccept());
        mySharedPreferences.setDisApproved(activity.getDisapprove());
        mySharedPreferences.setStatus(activity.getStatus());
        mySharedPreferences.setSelectTicket(activity.getSelectTicket());
        mySharedPreferences.setTicketType(activity.getTicketType());
        mySharedPreferences.setLogout(activity.getLogout());
        mySharedPreferences.setPaymentDate(activity.getPaymentDate());
        mySharedPreferences.setPaymentType(activity.getPaymentType());
        mySharedPreferences.setQrCode(activity.getQrCode());
        mySharedPreferences.setRemainingBudget(activity.getRemainingBudget());
        mySharedPreferences.setPaymentStatus(activity.getPaymentStatus());
        mySharedPreferences.setRedeemDateTime(activity.getRedeemDateTime());
        mySharedPreferences.setUserName(activity.getUserName());
        mySharedPreferences.setChangeLanguage(activity.getChangeLanguage());
        mySharedPreferences.setRedeemQrCode(activity.getRedeemQrCode());
        mySharedPreferences.setTransationId(activity.getTransactionId());
        mySharedPreferences.setNotification(activity.getNotification());
        mySharedPreferences.setCreateVendor(activity.getCreateVendor());
        mySharedPreferences.setAddVendor(activity.getAddVendor());
        mySharedPreferences.setType(activity.getType());
        mySharedPreferences.setDiscount(activity.getDiscount());
        mySharedPreferences.setSelect_vendor(activity.getSelect_vendor());
        mySharedPreferences.setRemove(activity.getRemove());
        mySharedPreferences.setAddress(activity.getAddress());
        mySharedPreferences.setVoucherQrCode(activity.getVoucherQrCode());
        mySharedPreferences.setSelectPayment(activity.getSelectPayment());
        mySharedPreferences.setEnterAmount(activity.getEnter_amount());
        mySharedPreferences.setEnterVoucherCode(activity.getEnter_voucher_code());
        mySharedPreferences.setOr(activity.getOr());
        mySharedPreferences.setScanYourCode(activity.getScan_your_code());
        mySharedPreferences.setUpdateCategories(activity.getUpdateCategories());
        mySharedPreferences.setEventStartDate(activity.getEventStartDate());
        mySharedPreferences.setEventEndDate(activity.getEventEndDate());
        mySharedPreferences.setEnterTicketCode(activity.getEnterTicketCode());
        mySharedPreferences.setEventFees(activity.getEventFees());
        mySharedPreferences.setTotalAmount(activity.getToatalAmont());
        mySharedPreferences.setDiscountAmount(activity.getDiscountAmount());
        mySharedPreferences.setRemainingAmount(activity.getRemainingAmount());
        mySharedPreferences.setDiscountPercenTage(activity.getDiscountPercentage());

        txtText.setText(MySharedPreferences.getMySharedPreferences().getSelectCategories());
        btnNext.setText(MySharedPreferences.getMySharedPreferences().getNext());
        strCategories = MySharedPreferences.getMySharedPreferences().getStrSelectCategories();
    }

    /**
     * CategoryListApi
     */
    private void categoryListApi(final int page) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            try {
                mainProgress.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("deviceid", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("page_number", String.valueOf(page));
            params.put("pagination_flag", "true");
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<CategoryList> call;
            call = RetrofitRestClient.getInstance().categoryList(params);

            if (call == null) return;

            call.enqueue(new Callback<CategoryList>() {
                @Override
                public void onResponse(@NonNull final Call<CategoryList> call, @NonNull Response<CategoryList> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final CategoryList categoryList;
                    if (response.isSuccessful()) {
                        categoryList = response.body();
                        if (Objects.requireNonNull(categoryList).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                TOTAL_PAGES = categoryList.getPageCount();
                                categoryArrayList.addAll(categoryList.getData());
                                categoryAdapter = new CategoryAdapter(getActivity(), categoryArrayList);
                                recyclerView.setAdapter(categoryAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(categoryList).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(categoryList).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(categoryList.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                                categoryAdapter.showLoadMore(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), categoryList.getMessage());
                            endlessRecyclerOnScrollListener.previousState();
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                        endlessRecyclerOnScrollListener.previousState();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CategoryList> call, @NonNull Throwable t) {
                    try {
                        categoryAdapter.showLoadMore(false);
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
     * addCityApi
     */
    private void addCityApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("city_name", locality);
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().addCity(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull final Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            Intent i = new Intent(CategoryActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            AppUtils.startFromRightToLeft(getActivity());
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

    /**
     * addCategoryApi
     */
    private void addCategory() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("category_id", sb1.toString());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().addCategoryApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull final Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            addCityApi();
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
