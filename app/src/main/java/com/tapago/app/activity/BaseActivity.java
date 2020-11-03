package com.tapago.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapago.app.R;
import com.tapago.app.language.LocalizationActivityDelegate;
import com.tapago.app.language.OnLocaleChangedListener;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.ForceUpdateChecker;
import com.tapago.app.utils.MySharedPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class BaseActivity extends AppCompatActivity implements OnLocaleChangedListener, ForceUpdateChecker.OnUpdateNeededListener {
    private ProgressDialog progressDialog;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        if (MySharedPreferences.getMySharedPreferences().isLogin()) {
            ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase));
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    public final void setLanguage(String language) {
        localizationDelegate.setLanguage(this, language);
    }

    public final void setLanguage(Locale locale) {
        localizationDelegate.setLanguage(this, locale);
    }

    public final void setDefaultLanguage(String language) {
        localizationDelegate.setDefaultLanguage(language);
    }

    public final void setDefaultLanguage(Locale locale) {
        localizationDelegate.setDefaultLanguage(locale);
    }

    public final Locale getCurrentLanguage() {
        return localizationDelegate.getLanguage(this);
    }

    // Just override method locale change event
    @Override
    public void onBeforeLocaleChanged() {
    }

    @Override
    public void onAfterLocaleChanged() {
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        if (MySharedPreferences.getMySharedPreferences().getCurrentTime().length() > 0) {
            String currentDateTimeString = getCurrentTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                Date currentDate = df.parse(currentDateTimeString);
                Date nowDate = df.parse(MySharedPreferences.getMySharedPreferences().getCurrentTime());

                printDifference(nowDate, currentDate, updateUrl);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.update_tapago))
                    .setMessage(getString(R.string.update_text))
                    .setPositiveButton(getString(R.string.update),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    redirectStore(updateUrl);
                                }
                            }).setNegativeButton(getString(R.string.not_now),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MySharedPreferences.getMySharedPreferences().setCurrentTime(getCurrentTime());
                                    dialog.dismiss();
                                }
                            }).create();
            dialog.show();
        }
    }

    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public void printDifference(Date startDate, Date endDate, final String updateUrl) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if (elapsedHours > 22) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.update_tapago))
                    .setMessage(getString(R.string.update_text))
                    .setPositiveButton(getString(R.string.update),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    redirectStore(updateUrl);
                                }
                            }).setNegativeButton(getString(R.string.not_now),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MySharedPreferences.getMySharedPreferences().setCurrentTime(getCurrentTime());
                                    dialog.dismiss();
                                }
                            }).create();
            dialog.show();
        }
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * SIMPLE SNACKBAR
     */
    public void showSnackBar(Context context, String message) {
        ViewGroup view = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE);
        ViewGroup viewGroup = (ViewGroup) snackbar.getView();
        viewGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        View viewTv = snackbar.getView();
        TextView tv = viewTv.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.red));
        tv.setMaxLines(5);
        snackbar.show();
    }


    /**
     * SIMPLE SNACKBAR
     */
    public void showSnackBars(Context context, String message) {
        ViewGroup view = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE);
        ViewGroup viewGroup = (ViewGroup) snackbar.getView();
        viewGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        View viewTv = snackbar.getView();
        TextView tv = viewTv.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        tv.setMaxLines(5);
        snackbar.show();
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

    public Activity getActivity() {
        return this;
    }


    public void logout() {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
        mySharedPreferences.setUserId("");
        mySharedPreferences.setLogin(false);
        mySharedPreferences.setGuestBasketListing(null);
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        Objects.requireNonNull(getActivity()).finish();
        AppUtils.startFromRightToLeft(getActivity());
    }


}
