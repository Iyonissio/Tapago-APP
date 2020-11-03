package com.tapago.app.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapago.app.R;
import com.tapago.app.activity.CMSActivity;
import com.tapago.app.activity.ChangeLanguage;
import com.tapago.app.activity.HelpActivity;
import com.tapago.app.activity.LoginActivity;
import com.tapago.app.activity.MainActivity;
import com.tapago.app.model.BasicModel;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.io.File;
import java.net.SocketTimeoutException;
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
public class SettingFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.txtAbout)
    AppCompatTextView txtAbout;
    @BindView(R.id.txtPrivacyPolicy)
    AppCompatTextView txtPrivacyPolicy;
    @BindView(R.id.txtTerms)
    AppCompatTextView txtTerms;
    @BindView(R.id.btnHelp)
    AppCompatButton btnHelp;
    ProgressDialog progressDialog;
    @BindView(R.id.txtLogout)
    AppCompatTextView txtLogout;
    @BindView(R.id.txtChangeLang)
    AppCompatTextView txtChangeLang;
    MainActivity mainActivity;

    @SuppressLint("ValidFragment")
    public SettingFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.notificationListUserApi();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtAbout.setText(MySharedPreferences.getMySharedPreferences().getAboutUs());
        txtPrivacyPolicy.setText(MySharedPreferences.getMySharedPreferences().getPrivacyPolicy());
        txtTerms.setText(MySharedPreferences.getMySharedPreferences().getTermsCondition());
        btnHelp.setText(MySharedPreferences.getMySharedPreferences().getNeedHelp());
        txtLogout.setText(MySharedPreferences.getMySharedPreferences().getLogout());
        txtChangeLang.setText(MySharedPreferences.getMySharedPreferences().getChangeLanguage());

        mainActivity.notificationListUserApi();
    }

    @OnClick({R.id.cdAbout, R.id.cdPrivacyPolicy, R.id.cdTerms, R.id.cdLogOut, R.id.btnHelp
            , R.id.cdChangeLang,R.id.cdCloseAccount})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cdAbout:
                Intent i = new Intent(getActivity(), CMSActivity.class);
                i.putExtra("title", AppUtils.getText(txtAbout));
                i.putExtra("slug", "about-us");
                startActivity(i);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.cdPrivacyPolicy:
                Intent i1 = new Intent(getActivity(), CMSActivity.class);
                i1.putExtra("title", AppUtils.getText(txtPrivacyPolicy));
                i1.putExtra("slug", "privacy-policy");
                startActivity(i1);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.cdTerms:
                Intent i2 = new Intent(getActivity(), CMSActivity.class);
                i2.putExtra("title", AppUtils.getText(txtTerms));
                i2.putExtra("slug", "terms-and-comditions");
                startActivity(i2);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.btnHelp:
                Intent i3 = new Intent(getActivity(), HelpActivity.class);
                startActivity(i3);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.cdChangeLang:
                startActivity(new Intent(getActivity(), ChangeLanguage.class));
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.cdLogOut:
                new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                        .setMessage(getString(R.string.are_logout))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                logoutApi();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                break;
            case R.id.cdCloseAccount:
                new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                        .setMessage(getString(R.string.are_close))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                closeAccountApi();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                break;
        }
    }


    /**
     * close account api
     */
    private void closeAccountApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().closeAccountApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    BasicModel logOutModel;
                    if (response.isSuccessful()) {
                        logOutModel = response.body();
                        if (Objects.requireNonNull(logOutModel).getCode() == 200) {
                            try {
                                File folder = new File(Environment.getExternalStorageDirectory().getPath(), ".TaPago");
                                if (folder.isDirectory()) {
                                    File[] children = folder.listFiles();
                                    for (File child : children) {
                                        child.delete();
                                    }

                                    folder.delete();
                                }

                            } catch (Exception e) {
                                /* Log.d(LOG,"Some error happened?");*/
                            }

                            MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
                            mySharedPreferences.setLogin(false);
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            Objects.requireNonNull(getActivity()).finish();
                            AppUtils.startFromRightToLeft(getActivity());
                        } else if (Objects.requireNonNull(logOutModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), logOutModel.getMessage());
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
     * logout api
     */
    private void logoutApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("user_device_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());


            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().logoutApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    BasicModel logOutModel;
                    if (response.isSuccessful()) {
                        logOutModel = response.body();
                        if (Objects.requireNonNull(logOutModel).getCode() == 200) {
                            try {
                                File folder = new File(Environment.getExternalStorageDirectory().getPath(), ".TaPago");
                                if (folder.isDirectory()) {
                                    File[] children = folder.listFiles();
                                    for (File child : children) {
                                        child.delete();
                                    }

                                    folder.delete();
                                }

                            } catch (Exception e) {
                                /* Log.d(LOG,"Some error happened?");*/
                            }

                            MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
                            mySharedPreferences.setLogin(false);
                            mySharedPreferences.setGuestBasketListing(null);
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            Objects.requireNonNull(getActivity()).finish();
                            AppUtils.startFromRightToLeft(getActivity());
                        } else if (Objects.requireNonNull(logOutModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), logOutModel.getMessage());
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

    /**
     * SIMPLE DIALOG PROGRESS
     *
     * @param ctx
     */
    public void showProgressDialog(Context ctx) {
        progressDialog = ProgressDialog.show(ctx, "", getString(R.string.str_please_wait), true, false);
    }

    public void showProgressDialog(Context ctx, boolean canCancel) {
        progressDialog = ProgressDialog.show(ctx, "", getString(R.string.str_please_wait), true, canCancel);
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

    /**
     * SIMPLE SNACKBAR
     */
    public void showSnackBar(Context context, String message) {
        ViewGroup view = (ViewGroup) ((ViewGroup) Objects.requireNonNull(getActivity()).findViewById(android.R.id.content)).getChildAt(0);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE);
        ViewGroup viewGroup = (ViewGroup) snackbar.getView();
        viewGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        View viewTv = snackbar.getView();
        TextView tv = viewTv.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.red));
        tv.setMaxLines(5);
        snackbar.show();
    }
}
