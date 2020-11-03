package com.tapago.app.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.tapago.app.R;
import com.tapago.app.dialog.CustomDialog;
import com.tapago.app.model.QrCodeModel;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeRedeemHistoryVendor extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.edtCode)
    AppCompatEditText edtCode;
    @BindView(R.id.btnSubmit)
    AppCompatButton btnSubmit;
    @BindView(R.id.txtVoucher)
    AppCompatTextView txtVoucher;
    @BindView(R.id.txtTicket)
    AppCompatTextView txtTicket;
    @BindView(R.id.edtAmount)
    AppCompatEditText edtAmount;
    @BindView(R.id.txtScanCode)
    AppCompatTextView txtScanCode;
    @BindView(R.id.txtOR)
    AppCompatTextView txtOR;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    private String strType = "voucher", paymentType = "", voucherCode = "", eventId = "";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 150;
    public static final int REQUEST_PERMISSION_SETTING_CAMERA = 3;
    public static final int REQUEST_PERMISSION_LOCATION_SETTING = 101;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_redeem_history_vendor);
        ButterKnife.bind(this);

        btnSubmit.setText(MySharedPreferences.getMySharedPreferences().getSubmit());
        txtVoucher.setText(MySharedPreferences.getMySharedPreferences().getVoucher());
        txtTicket.setText(MySharedPreferences.getMySharedPreferences().getTicket());
        txtScanCode.setText(MySharedPreferences.getMySharedPreferences().getScanYourCode());
        txtOR.setText(MySharedPreferences.getMySharedPreferences().getOr());
        edtCode.setHint(MySharedPreferences.getMySharedPreferences().getEnterVoucherCode());
        edtAmount.setHint(MySharedPreferences.getMySharedPreferences().getAmount());
        txtName.setText(MySharedPreferences.getMySharedPreferences().getRedeemQrCode());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkCameraPermission();
        } else {
            scanResult();
        }
    }


    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
            return false;
        } else {
            scanResult();
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
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            scanResult();
                        }
                    }
                } else {
                    for (int i = 0, len = permissions.length; i < len; i++) {
                        String permission = permissions[i];
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            // user rejected the permission
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                boolean showRationale = shouldShowRequestPermissionRationale(permission);
                                if (!showRationale) {
                                    // Permission denied, Disable the functionality that depends on this permission.
                                    Toast.makeText(getActivity(), "Check Permission Camera", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING_CAMERA);
                                } else {
                                    // Permission denied, Disable the functionality that depends on this permission.
                                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                                    new AlertDialog.Builder(QrCodeRedeemHistoryVendor.this).setMessage("Please Approve Camera Permission to use this app")
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
            scanResult();
        }
    }

    public void scanResult() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (strType.equals("voucher")) {
                            edtCode.setVisibility(View.GONE);
                            txtOR.setVisibility(View.GONE);
                            if (AppUtils.getText(edtAmount).length() > 0) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result.getText());
                                    paymentType = jsonObject.getString("payment_type");
                                    voucherCode = jsonObject.getString("qrcode");
                                    eventId = jsonObject.getString("event_id");
                                    scanQrCodeApi(voucherCode, paymentType, eventId);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(result.getText());
                                    paymentType = jsonObject.getString("payment_type");
                                    voucherCode = jsonObject.getString("qrcode");
                                    eventId = jsonObject.getString("event_id");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            AppUtils.showToast(getActivity(), "Please enter Voucher Amount");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result.getText());
                                voucherCode = jsonObject.getString("qrcode");
                                eventId = jsonObject.getString("event_id");
                                scanQrCodeApi(voucherCode, paymentType, eventId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }


    @OnClick({R.id.ivBackArrow, R.id.btnSubmit, R.id.txtTicket, R.id.txtVoucher})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.btnSubmit:
                if (validation()) {
                    if (edtCode.getVisibility() == View.VISIBLE) {
                        scanQrCodeApi(AppUtils.getText(edtCode), paymentType, eventId);
                    } else {
                        scanQrCodeApi(voucherCode, paymentType, eventId);
                    }
                }
                break;
            case R.id.txtVoucher:
                mCodeScanner.startPreview();
                edtAmount.setVisibility(View.VISIBLE);
                edtCode.setText("");
                edtAmount.setText("");
                edtCode.setVisibility(View.VISIBLE);
                txtOR.setVisibility(View.VISIBLE);

                txtVoucher.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_primary));
                txtTicket.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_white));

                txtVoucher.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                txtTicket.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

                edtCode.setHint(MySharedPreferences.getMySharedPreferences().getEnterVoucherCode());

                strType = "voucher";

                break;
            case R.id.txtTicket:
                mCodeScanner.startPreview();
                edtAmount.setVisibility(View.GONE);
                edtCode.setText("");
                edtAmount.setText("");
                edtCode.setVisibility(View.VISIBLE);
                txtOR.setVisibility(View.VISIBLE);

                txtVoucher.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_white));
                txtTicket.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_primary_white));

                txtVoucher.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                txtTicket.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                edtCode.setHint(MySharedPreferences.getMySharedPreferences().getEnterTicketCode());

                strType = "ticket";
                break;
        }
    }

    private boolean validation() {
        if (TextUtils.isEmpty(AppUtils.getText(edtCode)) && edtCode.getVisibility() == View.VISIBLE) {
            showSnackBar(getActivity(), getString(R.string.please_enter_code));
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edtAmount)) && edtAmount.getVisibility() == View.VISIBLE) {
            showSnackBar(getActivity(), getString(R.string.please_enter_amount));
            return false;
        } else {
            return true;
        }
    }


    /**
     * scanQrCode
     */
    public void scanQrCodeApi(String voucherCode, String paymentType, String eventId) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("voucher_code", voucherCode);
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("payment_type", paymentType);
            if (strType.equals("voucher")) {
                params.put("type", strType);
            } else {
                params.put("type", strType);
            }
            if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("6")) {
                params.put("redeem_by_user", "vendor");
            } else {
                params.put("redeem_by_user", "merchant");
            }
            if (strType.equals("voucher")) {
                params.put("amount", AppUtils.getText(edtAmount));
            } else {
                params.put("amount", "");
            }
            params.put("event_id", eventId);


            Call<QrCodeModel> call;
            call = RetrofitRestClient.getInstance().redeemQrCodeApi(params);

            if (call == null) return;

            call.enqueue(new Callback<QrCodeModel>() {
                @Override
                public void onResponse(@NonNull final Call<QrCodeModel> call, @NonNull Response<QrCodeModel> response) {
                    final QrCodeModel qrCodeModel;
                    if (response.isSuccessful()) {
                        qrCodeModel = response.body();
                        if (Objects.requireNonNull(qrCodeModel).getCode() == 200) {
                            edtCode.setText("");
                            edtAmount.setText("");
                            mCodeScanner.startPreview();
                            CustomDialog customDialog = new CustomDialog(getActivity());
                            customDialog.txtTitle.setText(getString(R.string.app_name));
                            customDialog.txtMessage.setText(qrCodeModel.getMessage() + " " + "MT" + qrCodeModel.getAmount());
                            customDialog.show();
                            /*Toast.makeText(QrCodeRedeemHistoryVendor.this, qrCodeModel.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(QrCodeRedeemHistoryVendor.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            AppUtils.startFromRightToLeft(getActivity());*/
                        } else if (Objects.requireNonNull(qrCodeModel).getCode() == 999) {
                            logout();
                        } else {
                            try {
                                edtCode.setVisibility(View.VISIBLE);
                                txtOR.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mCodeScanner.startPreview();
                            showSnackBar(getActivity(), qrCodeModel.getMessage());
                        }
                    } else {
                        try {
                            edtCode.setVisibility(View.VISIBLE);
                            txtOR.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mCodeScanner.startPreview();
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<QrCodeModel> call, @NonNull Throwable t) {
                    try {
                        edtCode.setVisibility(View.VISIBLE);
                        txtOR.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mCodeScanner.startPreview();
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

    @Override
    public void onPause() {
        super.onPause();
        mCodeScanner.releaseResources();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

}
