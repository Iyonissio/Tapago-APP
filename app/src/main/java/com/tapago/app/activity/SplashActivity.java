package com.tapago.app.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.tapago.app.R;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

public class SplashActivity extends AppCompatActivity {


    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 150;
    public static final int REQUEST_PERMISSION_SETTING_CAMERA = 3;
    public static final int REQUEST_PERMISSION_LOCATION_SETTING = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_spalsh);
        MySharedPreferences.getMySharedPreferences().setDeviceId(AppUtils.getDeviceId(SplashActivity.this));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkCameraPermission();
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (MySharedPreferences.getMySharedPreferences().isLogin()) {
                        if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equalsIgnoreCase("6")) {
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            if (MySharedPreferences.getMySharedPreferences().getCategoryId().length() > 0) {
                                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(SplashActivity.this, CategoryActivity.class);
                                startActivity(i);
                            }
                        }
                    } else {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                    finish();
                    AppUtils.startFromRightToLeft(SplashActivity.this);
                }
            }, 1500);
        }
    }


    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                    Manifest.permission.CAMERA)) {
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
            return false;
        } else {
            statusCheck();
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            statusCheck();
                        }
                    }
                } else {
                    for (int i = 0, len = permissions.length; i < len; i++) {
                        String permission = permissions[i];
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            // user rejected the permission
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                boolean showRationale = shouldShowRequestPermissionRationale(permission);
                                if (!showRationale) {
                                    // Permission denied, Disable the functionality that depends on this permission.
                                    Toast.makeText(SplashActivity.this, "Check Permission Camera", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING_CAMERA);
                                } else {
                                    // Permission denied, Disable the functionality that depends on this permission.
                                    Toast.makeText(SplashActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                                    new AlertDialog.Builder(SplashActivity.this).setMessage("Please Approve All Permission to use this app")
                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    checkCameraPermission();
                                                }
                                            }).show();
                                }
                            }
                        }
                    }

                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING_CAMERA) {
            // Do something after user returned from app settings screen, like showing a Toast.
            checkCameraPermission();
        } else if (requestCode == REQUEST_PERMISSION_LOCATION_SETTING) {
            // Do something after user returned from app settings screen, like showing a Toast.
            statusCheck();
        }
    }

    public void statusCheck() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (MySharedPreferences.getMySharedPreferences().isLogin()) {
                    if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equalsIgnoreCase("6")) {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        if (MySharedPreferences.getMySharedPreferences().getCategoryId().length() > 0) {
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SplashActivity.this, CategoryActivity.class);
                            startActivity(i);
                        }
                    }
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                finish();
                AppUtils.startFromRightToLeft(SplashActivity.this);
            }
        }, 1500);
    }
}



