package com.tapago.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tapago.app.R;
import com.tapago.app.TaPagoApp;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AppUtils {
    private static final String APP_TAG = "AudioRecorder";
    public static int Diff_Min = 0;
    public static int Diff_Hours = 0;

    public static int logString(String message) {
        return Log.i(APP_TAG, message);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    public static boolean isNameValid(String name) {
        final String expression = "^[a-zA-Z ]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }


    public static String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void startFromRightToLeft(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.trans_left_in, R.anim.no_animation);
    }

    public static void finishFromLeftToRight(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) TaPagoApp.getTaPagoApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
    }


    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    public static void hideSoftKeyboard(Activity activity) {
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    public static long getTimeStampFromDate(String dateTime) {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            Date date = formatter.parse(dateTime);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static long getTimeStampFromDob(String dateTime) {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(dateTime);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void showAppDetailsIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }


    public static int getMipmapResId(Context context, String drawableName) {
        return context.getResources().getIdentifier(
                drawableName.toLowerCase(Locale.ENGLISH), "mipmap", context.getPackageName());
    }


    public static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String makeGoogleAPI_Url(String origin_latitude, String origin_longitude, String dest_latitude, String dest_longitude) {
        String str_origin = "origin=" + origin_latitude + "," + origin_longitude;
        String str_dest = "destination=" + dest_latitude + "," + dest_longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        // String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return "http://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    // Check EditText or String is Empty or null etc.
    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Get Device Resolution
     *
     * @param context
     *            -Activity Context
     * @return resolution as string

    public static String getDeviceResoultion(Context context) {
    int density = context.getResources().getDisplayMetrics().densityDpi;
    if (density == DisplayMetrics.DENSITY_LOW)
    return "LDPI";
    else if (density == DisplayMetrics.DENSITY_MEDIUM)
    return "MDPI";
    else if (density == DisplayMetrics.DENSITY_HIGH)
    return "HDPI";
    else if (density == DisplayMetrics.DENSITY_XHIGH)
    return "XHDPI";
    else if (density == DisplayMetrics.DENSITY_XXHIGH)
    return "XXHDPI";
    else if (density == DisplayMetrics.DENSITY_TV)
    return "MDPI";
    else
    return "MDPI";
    }*/


    /**
     * @param phone
     * @return
     */
    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 8 || phone.length() > 16) {
                // if(phone.length() != 10) {
                check = false;
                //txtPhone.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    /***
     * Convert Date FROM DD/MM/YYYY to MM/DD/YYYY
     *
     * @param date
     * @return
     */
    public static String ConvertDateToMM(String date) {

        SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat oldformat = new SimpleDateFormat("dd/MM/yyyy");
        String reformattedStr = null;
        try {
            reformattedStr = newformat.format(oldformat.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }

    /***
     * Convert Date FROM MM/DD/YYYY to DD/MM/YYYY
     *
     * @param date
     * @return
     */
    public static String ConvertDateToDD(String date) {

        SimpleDateFormat oldformat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat newformat = new SimpleDateFormat("dd/MM/yyyy");
        String reformattedStr = null;
        try {
            reformattedStr = newformat.format(oldformat.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }

    /***
     * Convert Date FROM MM/DD/YYYY to DD/MM/YYYY
     *
     * @param date
     * @return
     */
    public static Date ConvertStringToDate(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    /***
     * Convert Short Date To Full Date From  dd/MM/yyyy to dd/mm/yyyy HH:mm:ss
     *
     * @param date
     * @return
     */
    public static Date ConvertStringFullDate(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/mm/yyyy HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    /***
     * Get Date Difference Between Two Date
     *
     * @param dateStart
     * @param dateStop
     */
    public static void DateDifference(String dateStart, String dateStop) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;


        try {
            d1 = (Date) format.parse(dateStart);
            d2 = (Date) format.parse(dateStop);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            //long diffSeconds = diff / 1000 % 60;
            long Diff_Minutes = diff / (60 * 1000) % 60;
            long Diff_Hourss = diff / (60 * 60 * 1000) % 24;
            //long diffDays = diff / (24 * 60 * 60 * 1000);


            Diff_Min = (int) Diff_Minutes;
            Diff_Hours = (int) Diff_Hourss;


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * <p>
     * Checks if a date is today.
     * </p>
     *
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    /**
     * <p>
     * Checks if two dates are on the same day ignoring time.
     * </p>
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * <p>
     * Checks if two calendars represent the same day ignoring time.
     * </p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
                .get(Calendar.DAY_OF_YEAR) == cal2
                .get(Calendar.DAY_OF_YEAR));
    }

    public static void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
