package com.tapago.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tapago.app.R;
import com.tapago.app.adapter.TicketListAdapter;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.BookTicket;
import com.tapago.app.model.TicketListModel.Datum;
import com.tapago.app.model.TicketListModel.TicketListModel;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookTicketActivity extends BaseActivity {

    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.txtSelect)
    AppCompatTextView txtSelect;
    @BindView(R.id.rlBookTicket)
    RecyclerView rlBookTicket;
    @BindView(R.id.txtTicketTotal)
    AppCompatTextView txtTicketTotal;
    @BindView(R.id.btnProceed)
    AppCompatButton btnProceed;
    @BindView(R.id.tvTotal)
    AppCompatTextView tvTotal;
    @BindView(R.id.txtTotal)
    AppCompatTextView txtTotal;
    @BindView(R.id.tvDiscount)
    AppCompatTextView tvDiscount;
    @BindView(R.id.txtDiscount)
    AppCompatTextView txtDiscount;
    @BindView(R.id.txtToPay)
    AppCompatTextView txtToPay;
    @BindView(R.id.txtGrandTotal)
    AppCompatTextView txtGrandTotal;

    private ArrayList<Datum> ticketListArray = new ArrayList<>();
    TicketListAdapter ticketListAdapter;
    String strEventID = "", strMerchantId = "";
    List<BookTicket> ticketlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            strEventID = intent.getStringExtra("eventId");
            strMerchantId = intent.getStringExtra("merchantId");
        }
        rlBookTicket.setLayoutManager(new LinearLayoutManager(getActivity()));
        txtName.setText(MySharedPreferences.getMySharedPreferences().getBookTicket());
        tvTotal.setText(MySharedPreferences.getMySharedPreferences().getTotal());
        txtSelect.setText(MySharedPreferences.getMySharedPreferences().getSelectTicket());
        btnProceed.setText(MySharedPreferences.getMySharedPreferences().getProceed());
        bookListApi();
    }

    @OnClick({R.id.ivBackArrow, R.id.btnProceed})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.btnProceed:
                if (txtTicketTotal.getText().toString().length() > 0) {
                    for (int i = 0; i < ticketListArray.size(); i++) {
                        View view1 = rlBookTicket.getChildAt(i);
                        TextView textCount = view1.findViewById(R.id.txtCount);

                        String Qty = textCount.getText().toString();
                        if (!Qty.equals("0")) {
                            BookTicket vc = new BookTicket();
                            vc.setTicket_id(String.valueOf(ticketListArray.get(i).getId()));
                            vc.setQty(Qty);
                            vc.setAmount(String.valueOf(ticketListArray.get(i).getTicketPrice()));
                            vc.setDiscount(String.valueOf(ticketListArray.get(i).getTicketDiscount()));
                            ticketlist.add(vc);
                        }
                    }

                    if (ticketlist.size() > 0) {
                        requestTicketApi();
                    } else {
                        AppUtils.showToast(getActivity(), getString(R.string.please_select_ticket));
                    }
                } else {
                    showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getFilledAllDetail());
                }
                break;
        }
    }


    /**
     * bookTicket Api
     */
    private void bookListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("event_id", strEventID);
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<TicketListModel> call;
            call = RetrofitRestClient.getInstance().ticketListApi(params);

            if (call == null) return;

            call.enqueue(new Callback<TicketListModel>() {
                @Override
                public void onResponse(@NonNull final Call<TicketListModel> call, @NonNull Response<TicketListModel> response) {
                    hideProgressDialog();
                    final TicketListModel ticketListModel;
                    if (response.isSuccessful()) {
                        ticketListModel = response.body();
                        if (Objects.requireNonNull(ticketListModel).getCode() == 200) {
                            try {
                                ticketListArray.addAll(ticketListModel.getData());
                                ticketListAdapter = new TicketListAdapter(getActivity(), ticketListModel.getData(), BookTicketActivity.this);
                                rlBookTicket.setAdapter(ticketListAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(ticketListModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), ticketListModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TicketListModel> call, @NonNull Throwable t) {
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


    @SuppressLint("SetTextI18n")
    public void total() {
        float totalAmount = 0;
        int qty = 0;
        float totalDiscount = 0;
        try {
            for (int i = 0; i < ticketListArray.size(); i++) {
                View view = rlBookTicket.getChildAt(i);
                TextView textCount = view.findViewById(R.id.txtCount);
                TextView textGrandTotal = view.findViewById(R.id.txtTotalGold);

                String Qty = textCount.getText().toString();
                String amount = textGrandTotal.getText().toString().replace("MT", "");

                //total amount and quantity
                totalAmount = Float.valueOf(amount) + totalAmount;
                qty = Integer.parseInt(Qty) + qty;
                String firstNumberAsString = String.format("%.0f", totalAmount);
                txtTotal.setText("MT" + String.valueOf(firstNumberAsString));
                txtTicketTotal.setText(MySharedPreferences.getMySharedPreferences().getTicket() + " " + qty);

                //discount amount
                double amountdiscount = Double.parseDouble(amount);
                double res = (amountdiscount / 100.0f) * ticketListArray.get(i).getTicketDiscount();
                totalDiscount = (float) (res + totalDiscount);
                txtDiscount.setText("MT" + String.valueOf(totalDiscount));

                float grandTotal = Float.valueOf(txtTotal.getText().toString().replace("MT", "")) - Float.valueOf(txtDiscount.getText().toString().replace("MT", ""));
                txtGrandTotal.setText("MT" + String.valueOf(grandTotal));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * bookTicket Api
     */
    private void requestTicketApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("event_id", strEventID);
            params.put("merchant_id", strMerchantId);
            Gson gson = new GsonBuilder().create();
            JsonArray myTicket = gson.toJsonTree(ticketlist).getAsJsonArray();
            params.put("detail", String.valueOf(myTicket));
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().requestTicketApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull final Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            try {
                                AppUtils.showToast(getActivity(), basicModel.getMessage());
                                finish();
                                AppUtils.finishFromLeftToRight(getActivity());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
