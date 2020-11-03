package com.tapago.app.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.fragment.HomeFragment;
import com.tapago.app.fragment.MyEventFragment;
import com.tapago.app.fragment.ProfileFragment;
import com.tapago.app.fragment.SettingFragment;
import com.tapago.app.fragment.VenderHomeFragment;
import com.tapago.app.model.CategoryCaption.Activity;
import com.tapago.app.model.CategoryCaption.Category;
import com.tapago.app.model.NotificationModel.NotificationModel;
import com.tapago.app.rest.RestConstant;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    Fragment fragment;
    @BindView(R.id.bnve)
    BottomNavigationView bnve;
    @BindView(R.id.bnveu)
    BottomNavigationView bnveu;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;

    @BindView(R.id.txtRecently)
    AppCompatTextView txtRecently;
    @BindView(R.id.txtPaymentHistory)
    AppCompatTextView txtPaymentHistory;
    @BindView(R.id.txtVoucherList)
    AppCompatTextView txtVoucherList;
    @BindView(R.id.txtTicketList)
    AppCompatTextView txtTicketList;
    @BindView(R.id.txtRedeem)
    AppCompatTextView txtRedeem;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.lrRCView)
    LinearLayout lrRCView;
    @BindView(R.id.lrPaymentHistory)
    LinearLayout lrPaymentHistory;
    @BindView(R.id.lrVoucherList)
    LinearLayout lrVoucherList;
    @BindView(R.id.lrTicketList)
    LinearLayout lrTicketList;
    @BindView(R.id.lrRedeem)
    LinearLayout lrRedeem;
    @BindView(R.id.tvPageTitle)
    AppCompatTextView tvPageTitle;
    @BindView(R.id.lrCreateVendor)
    LinearLayout lrCreateVendor;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.txtCreateVendor)
    AppCompatTextView txtCreateVendor;
    @BindView(R.id.viewRecent)
    View viewRecent;
    @BindView(R.id.viewPayment)
    View viewPayment;
    @BindView(R.id.viewVoucher)
    View viewVoucher;
    @BindView(R.id.viewRedeem)
    View viewRedeem;
    @BindView(R.id.txtQrVoucher)
    AppCompatTextView txtQrVoucher;
    @BindView(R.id.lrQrVoucher)
    LinearLayout lrQrVoucher;
    @BindView(R.id.viewQrVoucher)
    View viewQrVoucher;
    @BindView(R.id.txtUpdateCat)
    AppCompatTextView txtUpdateCat;
    @BindView(R.id.lrUpdateCat)
    LinearLayout lrUpdateCat;
    @BindView(R.id.viewCat)
    View viewCat;
    @BindView(R.id.imgNotification)
    AppCompatImageView imgNotification;
    @BindView(R.id.cart_badge)
    TextView cartBadge;
    @BindView(R.id.txtQrTicket)
    AppCompatTextView txtQrTicket;
    @BindView(R.id.lrQrTicket)
    LinearLayout lrQrTicket;
    @BindView(R.id.viewTicket)
    View viewTicket;
    @BindView(R.id.txtOrderHistory)
    AppCompatTextView txtOrderHistory;
    @BindView(R.id.lrOrderHistory)
    LinearLayout lrOrderHistory;
    @BindView(R.id.vieworderHistory)
    View vieworderHistory;
    @BindView(R.id.txtShippingPaymentHistory)
    AppCompatTextView txtShippingPaymentHistory;
    @BindView(R.id.lrShippingPaymentHistory)
    LinearLayout lrShippingPaymentHistory;
    @BindView(R.id.viewShippingPayment)
    View viewShippingPayment;

    private long lastBack = 0;
    boolean aIsActive = false, bIsActive = false, cIsActive = false, dIsActive = false, eIsActive = false;
    private static final int RC_CAMERA_PERM = 123;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        categoryCaptionApi();

        if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("3")) {
            floatingActionButton.setVisibility(View.GONE);
            bnve.setVisibility(View.GONE);
            bnveu.setVisibility(View.VISIBLE);
            TextView textView = bnveu.findViewById(R.id.i_home).findViewById(R.id.largeLabel);
            textView.setTextSize(11);

            textView = bnveu.findViewById(R.id.i_profile).findViewById(R.id.largeLabel);
            textView.setTextSize(11);

            textView = bnveu.findViewById(R.id.i_setting).findViewById(R.id.largeLabel);
            textView.setTextSize(11);

            bnveu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedUser);

            imgNotification.setVisibility(View.VISIBLE);
        } else if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("6")) {
            floatingActionButton.setVisibility(View.GONE);
            bnve.setVisibility(View.GONE);
            bnveu.setVisibility(View.VISIBLE);
            TextView textView = bnveu.findViewById(R.id.i_home).findViewById(R.id.largeLabel);
            textView.setTextSize(11);

            textView = bnveu.findViewById(R.id.i_profile).findViewById(R.id.largeLabel);
            textView.setTextSize(11);

            textView = bnveu.findViewById(R.id.i_setting).findViewById(R.id.largeLabel);
            textView.setTextSize(11);

            bnveu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedUser);
            imgNotification.setVisibility(View.GONE);
            cartBadge.setVisibility(View.GONE);
        } else {
            floatingActionButton.setVisibility(View.VISIBLE);
            bnve.setVisibility(View.VISIBLE);
            bnveu.setVisibility(View.GONE);
            TextView textView = bnve.findViewById(R.id.i_home).findViewById(R.id.largeLabel);
            textView.setTextSize(11);

            textView = bnve.findViewById(R.id.i_event).findViewById(R.id.largeLabel);
            textView.setTextSize(11);

            textView = bnve.findViewById(R.id.i_profile).findViewById(R.id.largeLabel);
            textView.setTextSize(11);

            textView = bnve.findViewById(R.id.i_setting).findViewById(R.id.largeLabel);
            textView.setTextSize(11);

            bnve.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            imgNotification.setVisibility(View.VISIBLE);
        }


        if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("6")) {
            loadFragment(new VenderHomeFragment(MainActivity.this));
            lrRCView.setVisibility(View.GONE);
            lrPaymentHistory.setVisibility(View.GONE);
            lrVoucherList.setVisibility(View.GONE);
            lrTicketList.setVisibility(View.GONE);
            lrCreateVendor.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            viewRecent.setVisibility(View.GONE);
            viewPayment.setVisibility(View.GONE);
            viewVoucher.setVisibility(View.GONE);
            lrQrVoucher.setVisibility(View.GONE);
            lrUpdateCat.setVisibility(View.GONE);
            viewCat.setVisibility(View.GONE);
            lrQrTicket.setVisibility(View.GONE);
            viewTicket.setVisibility(View.GONE);
            lrShippingPaymentHistory.setVisibility(View.GONE);
            viewShippingPayment.setVisibility(View.GONE);
            lrOrderHistory.setVisibility(View.GONE);
            vieworderHistory.setVisibility(View.GONE);
        } else if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("3")) {
            loadFragment(new HomeFragment(MainActivity.this));
            lrRedeem.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
            lrCreateVendor.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            lrQrTicket.setVisibility(View.VISIBLE);
            viewTicket.setVisibility(View.VISIBLE);
            lrShippingPaymentHistory.setVisibility(View.VISIBLE);
            viewShippingPayment.setVisibility(View.VISIBLE);
            lrOrderHistory.setVisibility(View.VISIBLE);
            vieworderHistory.setVisibility(View.VISIBLE);
        } else {
            loadFragment(new HomeFragment(MainActivity.this));
            lrRedeem.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            lrCreateVendor.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            lrQrVoucher.setVisibility(View.GONE);
            viewQrVoucher.setVisibility(View.GONE);
            lrQrTicket.setVisibility(View.GONE);
            viewTicket.setVisibility(View.GONE);
            lrShippingPaymentHistory.setVisibility(View.GONE);
            viewShippingPayment.setVisibility(View.GONE);
            lrOrderHistory.setVisibility(View.GONE);
            vieworderHistory.setVisibility(View.GONE);
        }

        tvPageTitle.setText(getString(R.string.home));

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        txtName.setText(MySharedPreferences.getMySharedPreferences().getFirstName() + " "
                + MySharedPreferences.getMySharedPreferences().getLastName());
        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image);
        Glide.with(Objects.requireNonNull(getActivity())).load(MySharedPreferences.getMySharedPreferences().getProfile()).apply(options).into(profileImage);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.i_home:
                    item.setChecked(true);
                    fragment = new HomeFragment(MainActivity.this);
                    if (!aIsActive) {
                        aIsActive = true;
                        bIsActive = false;
                        cIsActive = false;
                        dIsActive = false;
                        eIsActive = false;
                        RestConstant.CLICK_PROFILE = "NOT_CLICKED";
                        tvPageTitle.setText(getString(R.string.home));
                        loadFragment(fragment);
                    }
                    break;
                case R.id.i_event:
                    item.setChecked(true);
                    fragment = new MyEventFragment(MainActivity.this);
                    if (!bIsActive) {
                        aIsActive = false;
                        bIsActive = true;
                        cIsActive = false;
                        dIsActive = false;
                        eIsActive = false;
                        RestConstant.CLICK_PROFILE = "NOT_CLICKED";
                        tvPageTitle.setText(getString(R.string.myEvent));
                        loadFragment(fragment);
                    }
                    return true;
                case R.id.i_create_event:
                    item.setChecked(true);
                    if (!cIsActive) {
                        aIsActive = false;
                        bIsActive = false;
                        cIsActive = true;
                        dIsActive = false;
                        eIsActive = false;
                        RestConstant.CLICK_PROFILE = "NOT_CLICKED";
                        Intent i = new Intent(getActivity(), CreateEventActivity.class);
                        startActivity(i);
                        AppUtils.startFromRightToLeft(getActivity());
                    }
                    return true;
                case R.id.i_profile:
                    item.setChecked(true);
                    fragment = new ProfileFragment(MainActivity.this);
                    if (!dIsActive) {
                        aIsActive = false;
                        bIsActive = false;
                        cIsActive = false;
                        dIsActive = true;
                        eIsActive = false;
                        RestConstant.CLICK_PROFILE = "CLICKED";
                        tvPageTitle.setText(getString(R.string.profile));
                        loadFragment(fragment);
                    }
                    return true;
                case R.id.i_setting:
                    item.setChecked(true);
                    fragment = new SettingFragment(MainActivity.this);
                    if (!eIsActive) {
                        aIsActive = false;
                        bIsActive = false;
                        cIsActive = false;
                        dIsActive = false;
                        eIsActive = true;
                        RestConstant.CLICK_PROFILE = "NOT_CLICKED";
                        tvPageTitle.setText(getString(R.string.setting));
                        loadFragment(fragment);
                    }
                    return true;
            }
            return false;
        }
    };


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedUser
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.i_home:
                    item.setChecked(true);
                    if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("6")) {
                        fragment = new VenderHomeFragment(MainActivity.this);
                    } else {
                        fragment = new HomeFragment((MainActivity.this));
                    }
                    if (!aIsActive) {
                        aIsActive = true;
                        bIsActive = false;
                        cIsActive = false;
                        dIsActive = false;
                        eIsActive = false;
                        RestConstant.CLICK_PROFILE = "NOT_CLICKED";
                        tvPageTitle.setText(getString(R.string.home));
                        loadFragment(fragment);
                    }
                    break;
                case R.id.i_profile:
                    item.setChecked(true);
                    fragment = new ProfileFragment(MainActivity.this);
                    if (!dIsActive) {
                        aIsActive = false;
                        bIsActive = false;
                        cIsActive = false;
                        dIsActive = true;
                        eIsActive = false;
                        RestConstant.CLICK_PROFILE = "CLICKED";
                        tvPageTitle.setText(getString(R.string.profile));
                        loadFragment(fragment);
                    }
                    return true;
                case R.id.i_setting:
                    item.setChecked(true);
                    fragment = new SettingFragment(MainActivity.this);
                    if (!eIsActive) {
                        aIsActive = false;
                        bIsActive = false;
                        cIsActive = false;
                        dIsActive = false;
                        eIsActive = true;
                        RestConstant.CLICK_PROFILE = "NOT_CLICKED";
                        tvPageTitle.setText(getString(R.string.setting));
                        loadFragment(fragment);
                    }
                    return true;
            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            String backStateName = fragment.getClass().getName();
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
            if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) { //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.frameContainer, fragment, backStateName);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(backStateName);
                ft.commit();
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameContainer, fragment).commit();
            }
        }
    }

    public void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            try {
                int id = bnve.getSelectedItemId();
                if (id != 0) {
                    if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("6")) {
                        fragment = new VenderHomeFragment(MainActivity.this);
                    } else {
                        fragment = new HomeFragment((MainActivity.this));
                    }
                    switchFragment(fragment);
                }
                bnve.getMenu().getItem(0).setChecked(true);

                int id1 = bnveu.getSelectedItemId();
                if (id1 != 0) {
                    if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("6")) {
                        fragment = new VenderHomeFragment(MainActivity.this);
                    } else {
                        fragment = new HomeFragment((MainActivity.this));
                    }
                    switchFragment(fragment);
                }
                bnveu.getMenu().getItem(0).setChecked(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            long now = System.currentTimeMillis();
            if (now - lastBack >= 2000) {
                lastBack = now;
                Toast.makeText(this, getString(R.string.please_back), Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * onClick view
     *
     * @param view
     */
    @OnClick({R.id.ivOpenDrawer, R.id.lrHome, R.id.lrRCView, R.id.lrPaymentHistory, R.id.lrVoucherList, R.id.lrTicketList, R.id.lrRedeem, R.id.lrCreateVendor, R.id.imgNotification
            , R.id.lrQrVoucher, R.id.lrUpdateCat, R.id.lrQrTicket,R.id.lrShippingPaymentHistory,R.id.lrOrderHistory})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivOpenDrawer:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.lrHome:
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameContainer);
                if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("6")) {
                    if (fragment instanceof VenderHomeFragment) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return;
                    } else {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        tvPageTitle.setText(getString(R.string.home));
                        switchFragment(new VenderHomeFragment(MainActivity.this));
                    }
                } else {
                    if (fragment instanceof HomeFragment) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return;
                    } else {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        tvPageTitle.setText(getString(R.string.home));
                        switchFragment(new HomeFragment((MainActivity.this)));
                    }
                }
                break;
            case R.id.lrRCView:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(getActivity(), RecentlyViewedActivity.class);
                startActivity(intent);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.lrShippingPaymentHistory:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent iProduct = new Intent(getActivity(), ShippingPaymentHistoryActivity.class);
                startActivity(iProduct);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.lrOrderHistory:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent iorderhistory = new Intent(getActivity(), OrderHistoryActivity.class);
                startActivity(iorderhistory);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.lrPaymentHistory:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent i4 = new Intent(getActivity(), PaymentHistoryActivity.class);
                startActivity(i4);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.lrVoucherList:
                drawerLayout.closeDrawer(GravityCompat.START);
                if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("3")) {
                    Intent i5 = new Intent(getActivity(), VouchersListActivity.class);
                    startActivity(i5);
                    AppUtils.startFromRightToLeft(getActivity());
                } else {
                    Intent i5 = new Intent(getActivity(), MerchantVouchersListActivity.class);
                    startActivity(i5);
                    AppUtils.startFromRightToLeft(getActivity());
                }
                break;
            case R.id.lrTicketList:
                drawerLayout.closeDrawer(GravityCompat.START);
                if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("3")) {
                    Intent i5 = new Intent(getActivity(), TicketListActivity.class);
                    startActivity(i5);
                    AppUtils.startFromRightToLeft(getActivity());
                } else {
                    Intent i5 = new Intent(getActivity(), MerchantTicketListActivity.class);
                    startActivity(i5);
                    AppUtils.startFromRightToLeft(getActivity());
                }
                break;
            case R.id.lrRedeem:
                drawerLayout.closeDrawer(GravityCompat.START);
                if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("6")) {
                    Intent i5 = new Intent(getActivity(), QrCodeRedeemHistoryVendor.class);
                    startActivity(i5);
                    AppUtils.startFromRightToLeft(getActivity());
                } else {
                    Intent i5 = new Intent(getActivity(), QrCodeRedeemHistory.class);
                    startActivity(i5);
                    AppUtils.startFromRightToLeft(getActivity());
                }
                break;
            case R.id.lrCreateVendor:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent i6 = new Intent(getActivity(), CreateVendorActivity.class);
                startActivity(i6);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.imgNotification:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent i = new Intent(getActivity(), NotificationActivity.class);
                startActivity(i);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.lrQrVoucher:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent i5 = new Intent(getActivity(), VoucherQrCodeActivity.class);
                startActivity(i5);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.lrQrTicket:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent i7 = new Intent(getActivity(), TicketQrCodeActivity.class);
                startActivity(i7);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.lrUpdateCat:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent1 = new Intent(getActivity(), UpdateCategory.class);
                startActivity(intent1);
                AppUtils.startFromRightToLeft(getActivity());
                break;
        }
    }


    /**
     * CategoryCaptionApi
     */
    private void categoryCaptionApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            try {
                showProgressDialog(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("activity_name", "CategoryActivity");
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<Category> call;
            call = RetrofitRestClient.getInstance().categorycaptionApi(params);

            if (call == null) return;

            call.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(@NonNull final Call<Category> call, @NonNull Response<Category> response) {
                    try {
                        hideProgressDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final Category category;
                    if (response.isSuccessful()) {
                        category = response.body();
                        if (Objects.requireNonNull(category).getCode() == 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        setData(category.getActivity());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
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
                    try {
                        hideProgressDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (t instanceof SocketTimeoutException) {
                            showSnackBar(getActivity(), getString(R.string.connection_timeout));
                        } else {
                            t.printStackTrace();
                            showSnackBar(getActivity(), getString(R.string.something_went_wrong));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showSnackBar(getActivity(), getString(R.string.no_internet));
        }
    }

    private void setData(Activity activity) {
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

        txtRecently.setText(MySharedPreferences.getMySharedPreferences().getRecentlyView());
        txtPaymentHistory.setText(MySharedPreferences.getMySharedPreferences().getPaymentHistory());
        txtVoucherList.setText(MySharedPreferences.getMySharedPreferences().getVoucherList());
        txtTicketList.setText(MySharedPreferences.getMySharedPreferences().getTicketList());
        txtRedeem.setText(MySharedPreferences.getMySharedPreferences().getRedeemQrCode());
        txtCreateVendor.setText(MySharedPreferences.getMySharedPreferences().getCreateVendor());
        txtQrVoucher.setText(MySharedPreferences.getMySharedPreferences().getVoucherQrCode());
        txtUpdateCat.setText(MySharedPreferences.getMySharedPreferences().getUpdateCategories());
    }

    public void notificationListUserApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<NotificationModel> call;
            call = RetrofitRestClient.getInstance().notificationApi(params);

            if (call == null) return;

            call.enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(@NonNull final Call<NotificationModel> call, @NonNull Response<NotificationModel> response) {
                    final NotificationModel notificationModel;
                    if (response.isSuccessful()) {
                        notificationModel = response.body();
                        if (Objects.requireNonNull(notificationModel).getCode() == 200) {
                            try {
                                cartBadge.setText(String.valueOf(notificationModel.getUnreadCount()));
                                ShortcutBadger.applyCount(MainActivity.this, notificationModel.getUnreadCount());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(notificationModel).getCode() == 999) {
                            logout();
                        } else {
                            //showSnackBar(getActivity(), notificationModel.getMessage());
                        }
                    } else {
                        //  showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NotificationModel> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        //  showSnackBar(getActivity(), getString(R.string.connection_timeout));
                    } else {
                        t.printStackTrace();
                        // showSnackBar(getActivity(), getString(R.string.something_went_wrong));
                    }
                }
            });
        } else {
            // showSnackBar(getActivity(), getString(R.string.no_internet));
        }
    }

}
