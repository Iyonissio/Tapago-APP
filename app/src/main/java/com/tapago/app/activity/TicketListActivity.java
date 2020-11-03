package com.tapago.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tapago.app.R;
import com.tapago.app.adapter.BookUserListAdapter;
import com.tapago.app.dialog.QrCodeDialog;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.BookTicketUserModel.BookTicketUserModel;
import com.tapago.app.model.BookTicketUserModel.Datum;
import com.tapago.app.model.BrainTreeTokenModel;
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

public class TicketListActivity extends BaseActivity implements PaymentMethodNonceCreatedListener, BraintreeCancelListener, BraintreeErrorListener, DropInResult.DropInResultListener {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.recycleVouchersList)
    RecyclerView recycleVouchersList;

    BookUserListAdapter bookUserListAdapter;
    ArrayList<Datum> bookTicketUser = new ArrayList<>();
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    private String token = "", paymentMethodNonce1 = "", Payment_Type = "", strRequestId = "", strAmount = "", strDiscount = "";
    private int REQUEST_CODE = 145;
    private PaymentMethodNonce mNonce;
    private PaymentMethodType mPaymentMethodType;
    private QrCodeDialog qrCodeDialog;
    LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private int TOTAL_PAGES;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers_list);
        ButterKnife.bind(this);

        qrCodeDialog = new QrCodeDialog(this);

        setRecyclerView();
        txtName.setText(MySharedPreferences.getMySharedPreferences().getTicketList());

    }

    @Override
    public void onResume() {
        super.onResume();
        bookTicketUser.clear();
        ticketListUserApi(currentPage);
    }

    //Setting recycler view
    private void setRecyclerView() {
        recycleVouchersList.setHasFixedSize(true);
        bookUserListAdapter = new BookUserListAdapter(getActivity(), bookTicketUser, TicketListActivity.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycleVouchersList.setLayoutManager(linearLayoutManager);//Linear Items
        recycleVouchersList.setItemAnimator(new DefaultItemAnimator());
        recycleVouchersList.setAdapter(bookUserListAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                ticketListUserApi(current_page);
            }
        };
        recycleVouchersList.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                bookTicketUser.clear();
                ticketListUserApi(currentPage);
                break;
        }
    }

    private void ticketListUserApi(final int page) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            if (page == 1) {
                try {
                    mainProgress.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                bookUserListAdapter.showLoadMore(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("page_number", String.valueOf(page));

            Call<BookTicketUserModel> call;
            call = RetrofitRestClient.getInstance().requestUserTicketListApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BookTicketUserModel>() {
                @Override
                public void onResponse(@NonNull final Call<BookTicketUserModel> call, @NonNull Response<BookTicketUserModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final BookTicketUserModel bookTicketUserModel;
                    if (response.isSuccessful()) {
                        bookTicketUserModel = response.body();
                        if (Objects.requireNonNull(bookTicketUserModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                TOTAL_PAGES = bookTicketUserModel.getPageCount();
                                bookTicketUser.addAll(bookTicketUserModel.getData());
                                bookUserListAdapter = new BookUserListAdapter(getActivity(), bookTicketUser, TicketListActivity.this);
                                recycleVouchersList.setAdapter(bookUserListAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(bookTicketUserModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(bookTicketUserModel).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(bookTicketUserModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                                bookUserListAdapter.showLoadMore(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), bookTicketUserModel.getMessage());
                            endlessRecyclerOnScrollListener.previousState();
                        }
                        bookUserListAdapter.showLoadMore(false);
                    } else {
                        showSnackBar(getActivity(), response.message());
                        endlessRecyclerOnScrollListener.previousState();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BookTicketUserModel> call, @NonNull Throwable t) {
                    try {
                        bookUserListAdapter.showLoadMore(false);
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
        Payment_Type = paymentMethodNonce.getTypeLabel();

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
            //createPaymentApi(strRequestId, strAmount, strDiscount);
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

    public void openQrDialog(String qrCode) {
        qrCodeDialog.progressBar.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(qrCode)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        qrCodeDialog.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(qrCodeDialog.imgQrCode);
        qrCodeDialog.show();
    }
}
