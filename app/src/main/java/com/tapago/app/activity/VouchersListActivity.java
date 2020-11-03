package com.tapago.app.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.CardNonce;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.PostalAddress;
import com.tapago.app.R;
import com.tapago.app.adapter.VouchersListAdapter;
import com.tapago.app.dialog.PaymentOptionDialog;
import com.tapago.app.model.BrainTreeTokenModel;
import com.tapago.app.model.UserVoucherList.Datum;
import com.tapago.app.model.UserVoucherList.VoucherListResponse;
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

public class VouchersListActivity extends BaseActivity implements PaymentMethodNonceCreatedListener, BraintreeCancelListener, BraintreeErrorListener, DropInResult.DropInResultListener {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.recycleVouchersList)
    RecyclerView recycleVouchersList;

    VouchersListAdapter vouchersListAdapter;
    ArrayList<Datum> voucherListUser = new ArrayList<>();
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    private String token = "", paymentMethodNonce1 = "", strRequestId = "", strAmount = "", strDiscount = "";
    private int REQUEST_CODE = 145;
    private PaymentMethodNonce mNonce;
    private PaymentMethodType mPaymentMethodType;

    LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private int TOTAL_PAGES;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    PaymentOptionDialog paymentOptionDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers_list);
        ButterKnife.bind(this);


        paymentOptionDialog = new PaymentOptionDialog(this);

        setRecyclerView();
        txtName.setText(MySharedPreferences.getMySharedPreferences().getVoucherList());
    }

    @Override
    public void onResume() {
        super.onResume();
        voucherListUser.clear();
        voucherListUserApi(currentPage);
    }

    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                voucherListUser.clear();
                voucherListUserApi(currentPage);
                break;

        }
    }

    //Setting recycler view
    private void setRecyclerView() {
        recycleVouchersList.setHasFixedSize(true);
        vouchersListAdapter = new VouchersListAdapter(getActivity(), voucherListUser, VouchersListActivity.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleVouchersList.setLayoutManager(linearLayoutManager);//Linear Items
        recycleVouchersList.setItemAnimator(new DefaultItemAnimator());
        recycleVouchersList.setAdapter(vouchersListAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                voucherListUserApi(current_page);
            }
        };
        recycleVouchersList.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    private void voucherListUserApi(final int page) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            if (page == 1) {
                try {
                    mainProgress.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                vouchersListAdapter.showLoadMore(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("page_number", String.valueOf(page));


            Call<VoucherListResponse> call;
            call = RetrofitRestClient.getInstance().voucherListUserApi(params);

            if (call == null) return;

            call.enqueue(new Callback<VoucherListResponse>() {
                @Override
                public void onResponse(@NonNull final Call<VoucherListResponse> call, @NonNull Response<VoucherListResponse> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final VoucherListResponse voucherListResponse;
                    if (response.isSuccessful()) {
                        voucherListResponse = response.body();
                        if (Objects.requireNonNull(voucherListResponse).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                TOTAL_PAGES = voucherListResponse.getPageCount();
                                voucherListUser.addAll(voucherListResponse.getData());
                                vouchersListAdapter = new VouchersListAdapter(getActivity(), voucherListUser, VouchersListActivity.this);
                                recycleVouchersList.setAdapter(vouchersListAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(voucherListResponse).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(voucherListResponse).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(voucherListResponse.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                                vouchersListAdapter.showLoadMore(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), voucherListResponse.getMessage());
                            endlessRecyclerOnScrollListener.previousState();
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                        endlessRecyclerOnScrollListener.previousState();
                    }
                    vouchersListAdapter.showLoadMore(false);
                }

                @Override
                public void onFailure(@NonNull Call<VoucherListResponse> call, @NonNull Throwable t) {
                    try {
                        vouchersListAdapter.showLoadMore(false);
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

    public void paymentDialog(final String requestid, final String amount, final String discount) {
        new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setMessage(getString(R.string.are_sure_voucher))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(VouchersListActivity.this, PaymentActivity.class);
                        intent.putExtra("requestId",requestid);
                        intent.putExtra("amount",amount);
                        intent.putExtra("discount",discount);
                        intent.putExtra("type","Voucher");
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

       /* paymentOptionDialog.show();
        paymentOptionDialog.cash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    paymentOptionDialog.dismiss();
                    createPaymentApi(requestid, amount, "COD", discount);
                }
            }
        });
        paymentOptionDialog.paypal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    paymentOptionDialog.dismiss();
                    createPaymentApi(requestid, amount, "COD", discount);
                }
            }
        });

        paymentOptionDialog.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentOptionDialog.paypal.isChecked()) {
                    paymentOptionDialog.dismiss();
                    brainTreeTokenApi(requestid, amount, discount);
                } else {
                    paymentOptionDialog.dismiss();
                    createPaymentApi(requestid, amount, "COD", discount);
                }
            }
        });*/
    }

    public void brainTreeTokenApi(final String requestid, final String amount, final String discount) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            map.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            map.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            map.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BrainTreeTokenModel> call;
            call = RetrofitRestClient.getInstance().brinTreeApi(map);

            if (call == null) return;
            call.enqueue(new Callback<BrainTreeTokenModel>() {
                @Override

                public void onResponse(@NonNull Call<BrainTreeTokenModel> call, @NonNull Response<BrainTreeTokenModel> response) {
                    hideProgressDialog();
                    final BrainTreeTokenModel brainTreeTokenModel;
                    if (response.isSuccessful()) {
                        brainTreeTokenModel = response.body();
                        if (Objects.requireNonNull(brainTreeTokenModel).getCode() == 200) {
                            try {
                                token = brainTreeTokenModel.getToken();
                                strRequestId = requestid;
                                strAmount = amount;
                                strDiscount = discount;
                                submitPayments();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(brainTreeTokenModel).getCode() == 999) {
                            logout();
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BrainTreeTokenModel> call, @NonNull Throwable t) {
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

    private void submitPayments() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = Objects.requireNonNull(data).getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                displayResult(Objects.requireNonNull(result.getPaymentMethodNonce()), result.getDeviceData());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // canceled
                // Toast.makeText(getActivity(), "User Cancel", Toast.LENGTH_SHORT).show();
            } else {
                // an error occurred, checked the returned exception
                Exception exception = (Exception) Objects.requireNonNull(data).getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.e("exception", exception.toString());
            }
        }
    }

    private void displayResult(PaymentMethodNonce paymentMethodNonce, String deviceData) {
        mNonce = paymentMethodNonce;
        mPaymentMethodType = PaymentMethodType.forType(mNonce);
        paymentMethodNonce1 = paymentMethodNonce.getNonce();

        String details = "";
        if (mNonce instanceof CardNonce) {
            CardNonce cardNonce = (CardNonce) mNonce;
            details = "Card Last Two: " + cardNonce.getLastTwo() + "\n";
        } else if (mNonce instanceof PayPalAccountNonce) {
            PayPalAccountNonce paypalAccountNonce = (PayPalAccountNonce) mNonce;
            details = "First name: " + paypalAccountNonce.getFirstName() + "\n";
            details += "Last name: " + paypalAccountNonce.getLastName() + "\n";
            details += "Email: " + paypalAccountNonce.getEmail() + "\n";
            details += "Phone: " + paypalAccountNonce.getPhone() + "\n";
            details += "Payer id: " + paypalAccountNonce.getPayerId() + "\n";
            details += "Client metadata id: " + paypalAccountNonce.getClientMetadataId() + "\n";
            details += "Billing address: " + formatAddress(paypalAccountNonce.getBillingAddress()) + "\n";
            details += "Shipping address: " + formatAddress(paypalAccountNonce.getShippingAddress());
        }

        if (!paymentMethodNonce1.equals("")) {
          //  createPaymentApi(strRequestId, strAmount, "PayPal",strDiscount);
        }
    }


    private String formatAddress(PostalAddress address) {
        return address.getRecipientName() + " " + address.getStreetAddress() + " " +
                address.getExtendedAddress() + " " + address.getLocality() + " " + address.getRegion() +
                " " + address.getPostalCode() + " " + address.getCountryCodeAlpha2();
    }


    @Override
    public void onError(Exception error) {
        if (error instanceof ErrorWithResponse) {
            ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;
            BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");
            if (cardErrors != null) {
                // There is an issue with the credit card.
                BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
                if (expirationMonthError != null) {
                    // There is an issue with the expiration month.
                    showSnackBar(getActivity(), expirationMonthError.getMessage());
                }
            }
        }
    }

    @Override
    public void onResult(DropInResult result) {
        if (result.getPaymentMethodType() == null) {
        } else {
            mPaymentMethodType = result.getPaymentMethodType();
            if (result.getPaymentMethodNonce() != null) {
                displayResult(result.getPaymentMethodNonce(), result.getDeviceData());
            }
        }
    }

    @Override
    public void onCancel(int requestCode) {
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        displayResult(paymentMethodNonce, null);
    }


}
