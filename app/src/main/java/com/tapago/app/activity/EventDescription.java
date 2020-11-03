package com.tapago.app.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.adapter.MoreEventAdapter;
import com.tapago.app.adapter.SpinnerAdapter;
import com.tapago.app.adapter.TicketListAdapter;
import com.tapago.app.adapter.VoucherCSpinnerAdapter;
import com.tapago.app.adapter.VoucherListAdapter;
import com.tapago.app.dialog.RequestVoucherDialog;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.MoreEvent.EventList;
import com.tapago.app.model.MoreEvent.MoreEventModel;
import com.tapago.app.model.TicketListModel.TicketListModel;
import com.tapago.app.model.VoucherListModel.Datum;
import com.tapago.app.model.VoucherListModel.VoucherListModel;
import com.tapago.app.rest.RestConstant;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDescription extends BaseActivity {

    @BindView(R.id.imgBanner)
    AppCompatImageView imgBanner;
    @BindView(R.id.txtEventName)
    AppCompatTextView txtEventName;
    @BindView(R.id.txtEventDes)
    AppCompatTextView txtEventDes;
    String strImage = "", strEventName = "", strEventDes = "", strEventAddress = "", steEventVenue = "", strEventStartDate = "", strEventStartTime = "", strEventCreateDate = "", strEventCreateTime = "", strOrganiserName = "",
            strOrganiserImage = "", strEventID = "", strCategoryId = "", strCityName = "", strMerchantId = "", strVoucherId = "", strQty = "", strCategoryName = "", strEventEndDate = "", strEventEndTime = "";
    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.txtEventCreatedDate)
    AppCompatTextView txtEventCreatedDate;
    @BindView(R.id.txtEventCreatedTime)
    AppCompatTextView txtEventCreatedTime;
    @BindView(R.id.txtEventAddress)
    AppCompatTextView txtEventAddress;
    @BindView(R.id.txtEventStartdate)
    AppCompatTextView txtEventStartdate;
    @BindView(R.id.eventStartTime)
    AppCompatTextView eventStartTime;
    @BindView(R.id.txtEventDirection)
    AppCompatTextView txtEventDirection;
    @BindView(R.id.txtEventVenue)
    AppCompatTextView txtEventVenue;
    @BindView(R.id.imgOrganiserImage)
    CircleImageView imgOrganiserImage;
    @BindView(R.id.txtEventOrganiserName)
    AppCompatTextView txtEventOrganiserName;
    @BindView(R.id.recyclerViewMore)
    RecyclerView recyclerViewMore;
    @BindView(R.id.btnRequest)
    AppCompatButton btnRequest;
    @BindView(R.id.btnRedeem)
    AppCompatButton btnRedeem;
    @BindView(R.id.lrBottom)
    LinearLayout lrBottom;
    RequestVoucherDialog requestVoucherDialog;
    @BindView(R.id.txtMore)
    AppCompatTextView txtMore;
    @BindView(R.id.txtAbout)
    AppCompatTextView txtAbout;
    @BindView(R.id.txtVenue)
    AppCompatTextView txtVenue;
    @BindView(R.id.txtOrganiser)
    AppCompatTextView txtOrganiser;
    @BindView(R.id.recyclerVoucher)
    RecyclerView recyclerVoucher;
    @BindView(R.id.txtVoucher)
    AppCompatTextView txtVoucher;
    @BindView(R.id.btnBookTicket)
    AppCompatButton btnBookTicket;
    @BindView(R.id.txtEventCategory)
    AppCompatTextView txtEventCategory;
    @BindView(R.id.txtEventCat)
    AppCompatTextView txtEventCat;
    @BindView(R.id.txtAddress)
    AppCompatTextView txtAddress;
    @BindView(R.id.tvEventCity)
    AppCompatTextView tvEventCity;
    @BindView(R.id.txtEventCity)
    AppCompatTextView txtEventCity;
    @BindView(R.id.txtEventSdate)
    AppCompatTextView txtEventSdate;
    @BindView(R.id.txtEventEdate)
    AppCompatTextView txtEventEdate;
    @BindView(R.id.txtEventEndDate)
    AppCompatTextView txtEventEndDate;
    @BindView(R.id.eventEndTime)
    AppCompatTextView eventEndTime;
    @BindView(R.id.txtTicket)
    AppCompatTextView txtTicket;
    @BindView(R.id.recyclerTicket)
    RecyclerView recyclerTicket;
    private ArrayList<EventList> eventLists = new ArrayList<>();
    private ArrayList<Datum> voucherListModels = new ArrayList<>();
    private ArrayList<com.tapago.app.model.TicketListModel.Datum> ticketarryListModel = new ArrayList<>();
    MoreEventAdapter moreEventAdapter;
    VoucherListAdapter voucherListAdapter;
    TicketDesListAdapter ticketDesListAdapter;
    ArrayList<String> arrayList;
    private static final int RC_CAMERA_PERM = 123;
    int voucherDiscount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        ButterKnife.bind(this);

        requestVoucherDialog = new RequestVoucherDialog(getActivity());
        recyclerViewMore.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerVoucher.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerTicket.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        Intent intent = getIntent();
        if (intent != null) {
            strEventID = intent.getStringExtra("eventId");
            strCityName = intent.getStringExtra("cityName");
            strCategoryId = intent.getStringExtra("categoryId");
            strImage = intent.getStringExtra("image");
            strEventName = intent.getStringExtra("eventName");
            strEventDes = intent.getStringExtra("eventDes");
            strEventAddress = intent.getStringExtra("eventAddress");
            steEventVenue = intent.getStringExtra("eventVenue");
            strEventCreateDate = intent.getStringExtra("eventCreateDate");
            strEventCreateTime = intent.getStringExtra("eventCreateTime");
            strEventStartDate = intent.getStringExtra("eventStartDate");
            strEventStartTime = intent.getStringExtra("eventStartTime");
            strOrganiserName = intent.getStringExtra("eventOrganiserName");
            strOrganiserImage = intent.getStringExtra("eventOrganiserImage");
            strMerchantId = intent.getStringExtra("merchantId");
            strCategoryName = intent.getStringExtra("categoryName");
            strEventEndDate = intent.getStringExtra("eventEndDate");
            strEventEndTime = intent.getStringExtra("eventEndTime");


            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder);
            Glide.with(getActivity()).load(strImage).apply(options).into(imgBanner);
            txtEventName.setText(strEventName);
            txtEventDes.setText(strEventDes);
            txtEventStartdate.setText(strEventStartDate);
            eventStartTime.setText(strEventStartTime);
            txtEventCreatedDate.setText(strEventCreateDate);
            txtEventCreatedTime.setText(strEventCreateTime);
            txtEventVenue.setText(steEventVenue);
            txtEventAddress.setText(strEventAddress);
            txtEventOrganiserName.setText(strOrganiserName);
            txtEventCat.setText(strCategoryName);
            txtEventCity.setText(strCityName);
            txtEventEndDate.setText(strEventEndDate);
            eventEndTime.setText(strEventEndTime);
            Glide.with(getActivity()).load(strOrganiserImage).apply(options).into(imgOrganiserImage);
            if (txtEventDes.getText().toString().length() > 200) {
                makeTextViewResizable(txtEventDes, 3, "See more", true);
            }
        }

        if (RestConstant.CLICK_MY_EVENT.equals("HomeFragment")) {
            eventView();
        } else {
            moreEventListApi();
        }

        txtName.setText(MySharedPreferences.getMySharedPreferences().getEvent_Description());
        txtAbout.setText(MySharedPreferences.getMySharedPreferences().getAbout());
        txtVenue.setText(MySharedPreferences.getMySharedPreferences().getVenue());
        txtEventDirection.setText(MySharedPreferences.getMySharedPreferences().getDirection());
        txtOrganiser.setText(MySharedPreferences.getMySharedPreferences().getOrganiser());
        txtMore.setText(MySharedPreferences.getMySharedPreferences().getMoreLike());
        btnRequest.setText(MySharedPreferences.getMySharedPreferences().getRequestVoucher());
        btnRedeem.setText(MySharedPreferences.getMySharedPreferences().getRedeemQrCode());
        txtVoucher.setText(MySharedPreferences.getMySharedPreferences().getVoucher());
        txtTicket.setText(MySharedPreferences.getMySharedPreferences().getTicket());
        btnBookTicket.setText(MySharedPreferences.getMySharedPreferences().getBookTicket());
        txtEventCategory.setText(MySharedPreferences.getMySharedPreferences().getEvent_Category());
        txtAddress.setText(MySharedPreferences.getMySharedPreferences().getAddress());
        txtEventSdate.setText(MySharedPreferences.getMySharedPreferences().getEventStartDate());
        txtEventEdate.setText(MySharedPreferences.getMySharedPreferences().getEventEndDate());

        tvEventCity.setText(getString(R.string.ci));


        if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("3")) {
            btnBookTicket.setVisibility(View.VISIBLE);
            btnRedeem.setVisibility(View.GONE);
            btnRequest.setVisibility(View.VISIBLE);
        } else {
            btnBookTicket.setVisibility(View.GONE);
            btnRedeem.setVisibility(View.VISIBLE);
            btnRequest.setVisibility(View.GONE);
        }

        if (MySharedPreferences.getMySharedPreferences().getUserId().equals(strMerchantId)) {
            btnRedeem.setVisibility(View.VISIBLE);
        } else {
            btnRedeem.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.ivBackArrow, R.id.txtEventDirection, R.id.btnRedeem, R.id.btnRequest, R.id.btnBookTicket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtEventDirection:
                if (steEventVenue.length() > 0) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + steEventVenue);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
                break;
            case R.id.btnRedeem:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    callCamera();
                } else {
                    Intent i = new Intent(EventDescription.this, ScannerActivity.class);
                    MySharedPreferences.getMySharedPreferences().setEventId(strEventID);
                    startActivity(i);
                    AppUtils.startFromRightToLeft(getActivity());
                }
                break;
            case R.id.btnRequest:
                arrayList = new ArrayList<>();
                requestVoucherDialog.vNumberSpinner.setAdapter(new SpinnerAdapter(getActivity(), arrayList));
                requestVoucherDialog.txtTotalPrice.setText("MT0.00");
                requestVoucherDialog.txtPrice.setText("MT0.00");
                requestVoucherDialog.remainingVoucher.setVisibility(View.GONE);
                requestVoucherDialog.txtSelectVoucher.setText(MySharedPreferences.getMySharedPreferences().getSelectVoucher());
                requestVoucherDialog.txtPerVoucher.setText(MySharedPreferences.getMySharedPreferences().getPerVoucher());
                requestVoucherDialog.txtNumVoucher.setText(MySharedPreferences.getMySharedPreferences().getNumberOfVoucher());
                requestVoucherDialog.txtAmount.setText(MySharedPreferences.getMySharedPreferences().getAmountToBePaid());
                requestVoucherDialog.btnProceed.setText(MySharedPreferences.getMySharedPreferences().getProceed());
                requestVoucherDialog.tvDiscount.setText(MySharedPreferences.getMySharedPreferences().getDiscountAmount());
                if (voucherListModels.size() > 0) {
                    requestVoucherDialog.vTypeSpinner.setAdapter(new VoucherCSpinnerAdapter(getActivity(), voucherListModels));
                }
                requestVoucherDialog.vTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            try {
                                requestVoucherDialog.txtTotalPrice.setText("MT0.00");
                                requestVoucherDialog.txtDiscount.setText("MT0.00");
                                Datum datum = voucherListModels.get(position - 1);
                                requestVoucherDialog.txtPrice.setText("MT" + String.valueOf(datum.getVoucherPrice()));
                                strVoucherId = String.valueOf(datum.getId());
                                voucherDiscount = datum.getVoucherDiscount();
                                arrayList = new ArrayList<>();
                                for (int i = 0; i <= Integer.parseInt(datum.getRemainingQty()); i++) {
                                    arrayList.add(String.valueOf(i));
                                }
                                requestVoucherDialog.remainingVoucher.setVisibility(View.VISIBLE);
                                requestVoucherDialog.remainingVoucher.setText(getString(R.string.only) + " " + String.valueOf(datum.getRemainingQty()) + " " + getString(R.string.left));
                                requestVoucherDialog.vNumberSpinner.setAdapter(new SpinnerAdapter(getActivity(), arrayList));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                requestVoucherDialog.vNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            try {
                                if (requestVoucherDialog.txtPrice.getText().toString().length() > 0 && arrayList.get(position).length() > 0) {
                                    float sum = Float.valueOf(requestVoucherDialog.txtPrice.getText().toString().replace("MT", "")) * Float.valueOf(arrayList.get(position));
                                    String firstNumberAsString = String.format("%.0f", sum);
                                    double amount = Double.parseDouble(firstNumberAsString);
                                    double res = (amount / 100.0f) * voucherDiscount;

                                    String discount = String.format("%.0f", res);
                                    requestVoucherDialog.txtDiscount.setText("MT" + String.valueOf(discount));

                                    float minus = (float) (sum - res);
                                    String mainminus = String.format("%.0f", minus);
                                    requestVoucherDialog.txtTotalPrice.setText("MT" + String.valueOf(mainminus));
                                }
                                strQty = String.valueOf(arrayList.get(position));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                requestVoucherDialog.btnProceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (requestVoucherDialog.vTypeSpinner.getSelectedItemPosition() < 1) {
                            AppUtils.showAlertDialog(getActivity(), getString(R.string.app_name), MySharedPreferences.getMySharedPreferences().getPleaseSelectVoucherType());
                        } else if (requestVoucherDialog.vNumberSpinner.getSelectedItemPosition() < 1) {
                            AppUtils.showAlertDialog(getActivity(), getString(R.string.app_name), MySharedPreferences.getMySharedPreferences().getPleaseSelectNumVoucherType());
                        } else {
                            requestVoucherApi();
                        }
                    }
                });
                requestVoucherDialog.show();
                break;
            case R.id.btnBookTicket:
                Intent i1 = new Intent(EventDescription.this, BookTicketActivity.class);
                i1.putExtra("eventId", strEventID);
                i1.putExtra("merchantId", strMerchantId);
                startActivity(i1);
                AppUtils.startFromRightToLeft(getActivity());
                break;
        }
    }


    @AfterPermissionGranted(RC_CAMERA_PERM)
    private void callCamera() {
        if (EasyPermissions.hasPermissions(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
            Intent i = new Intent(EventDescription.this, ScannerActivity.class);
            MySharedPreferences.getMySharedPreferences().setEventId(strEventID);
            startActivity(i);
            AppUtils.startFromRightToLeft(getActivity());
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, RC_CAMERA_PERM, Manifest.permission.CAMERA)
                            .setRationale(R.string.rationale_call)
                            .setPositiveButtonText(getString(R.string.ok))
                            .setNegativeButtonText(getString(R.string.cancel))
                            .build());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText,
                                                                            final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);
        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "See less", false);
                    } else {
                        makeTextViewResizable(tv, 3, "See more", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);
        }
        return ssb;
    }

    public static class MySpannable extends ClickableSpan {
        private boolean isUnderline = false;

        /**
         * Constructor
         */
        MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(@NotNull TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#f04b23"));
        }

        @Override
        public void onClick(@NotNull View widget) {
        }
    }


    /**
     * View Event
     */
    private void eventView() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("event_id", strEventID);

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().viewEvent(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {

                        } else if (basicModel.getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), basicModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }

                    moreEventListApi();
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
     * moreEvent List Api
     */
    private void moreEventListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("category_id", strCategoryId);
            params.put("event_id", strEventID);
            params.put("city", strCityName);
            params.put("limit", "true");
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<MoreEventModel> call;
            call = RetrofitRestClient.getInstance().moreEventApi(params);

            if (call == null) return;

            call.enqueue(new Callback<MoreEventModel>() {
                @Override
                public void onResponse(@NonNull final Call<MoreEventModel> call, @NonNull Response<MoreEventModel> response) {
                    hideProgressDialog();
                    final MoreEventModel moreEventModel;
                    if (response.isSuccessful()) {
                        moreEventModel = response.body();
                        if (Objects.requireNonNull(moreEventModel).getCode() == 200) {
                            try {
                                eventLists.addAll(moreEventModel.getEventList());
                                moreEventAdapter = new MoreEventAdapter(getActivity(), moreEventModel.getEventList());
                                recyclerViewMore.setAdapter(moreEventAdapter);
                                if (eventLists.size() > 0) {
                                    txtMore.setVisibility(View.VISIBLE);
                                } else {
                                    txtMore.setVisibility(View.GONE);
                                }
                                moreEventAdapter.setOnItemClickLister(new MoreEventAdapter.OnItemClickLister() {
                                    @Override
                                    public void itemClicked(View view, int position) {
                                        Intent i = new Intent(getActivity(), EventDescription.class);
                                        i.putExtra("eventId", String.valueOf(eventLists.get(position).getEventId()));
                                        i.putExtra("cityName", eventLists.get(position).getCity());
                                        i.putExtra("categoryId", String.valueOf(eventLists.get(position).getCategoryId()));
                                        i.putExtra("image", eventLists.get(position).getEventBannerImg());
                                        i.putExtra("eventName", eventLists.get(position).getEventTitle());
                                        i.putExtra("eventDes", eventLists.get(position).getEventDescription());
                                        i.putExtra("eventVenue", eventLists.get(position).getEventVenue());
                                        i.putExtra("eventAddress", eventLists.get(position).getEventAddress());
                                        i.putExtra("eventStartDate", eventLists.get(position).getEventStartDate());
                                        i.putExtra("eventStartTime", eventLists.get(position).getEventStartDateTime());
                                        i.putExtra("eventEndDate", eventLists.get(position).getEventEnddateTimeSort());
                                        i.putExtra("eventEndTime", eventLists.get(position).getEventEnddateTime());
                                        i.putExtra("eventCreateDate", eventLists.get(position).getCreatedDate());
                                        i.putExtra("eventCreateTime", eventLists.get(position).getCreatedTime());
                                        i.putExtra("eventOrganiserName", eventLists.get(position).getOrganiserName());
                                        i.putExtra("eventOrganiserImage", eventLists.get(position).getOrganiserImage());
                                        i.putExtra("merchantId", eventLists.get(position).getMerchantId());
                                        i.putExtra("categoryName", eventLists.get(position).getCategoryName());
                                        startActivity(i);
                                        finish();
                                        AppUtils.startFromRightToLeft(getActivity());
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(moreEventModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), moreEventModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }

                    voucherListApi();
                }

                @Override
                public void onFailure(@NonNull Call<MoreEventModel> call, @NonNull Throwable t) {
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
     * voucher List Api
     */
    private void voucherListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("event_id", strEventID);
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<VoucherListModel> call;
            call = RetrofitRestClient.getInstance().voucherListApi(params);

            if (call == null) return;

            call.enqueue(new Callback<VoucherListModel>() {
                @Override
                public void onResponse(@NonNull final Call<VoucherListModel> call, @NonNull Response<VoucherListModel> response) {
                    hideProgressDialog();
                    final VoucherListModel voucherListModel;
                    if (response.isSuccessful()) {
                        voucherListModel = response.body();
                        if (Objects.requireNonNull(voucherListModel).getCode() == 200) {
                            try {
                                voucherListModels.addAll(voucherListModel.getData());
                                voucherListAdapter = new VoucherListAdapter(getActivity(), voucherListModel.getData());
                                recyclerVoucher.setAdapter(voucherListAdapter);
                                if (voucherListModels.size() > 0) {
                                    txtVoucher.setVisibility(View.VISIBLE);
                                } else {
                                    txtVoucher.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(voucherListModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), voucherListModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }


                    bookListApi();
                }

                @Override
                public void onFailure(@NonNull Call<VoucherListModel> call, @NonNull Throwable t) {
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
                                ticketarryListModel.addAll(ticketListModel.getData());
                                ticketDesListAdapter = new TicketDesListAdapter(getActivity(), ticketListModel.getData());
                                recyclerTicket.setAdapter(ticketDesListAdapter);
                                if (ticketarryListModel.size() > 0) {
                                    txtTicket.setVisibility(View.VISIBLE);
                                } else {
                                    txtTicket.setVisibility(View.GONE);
                                }
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

    /**
     * request voucher Api
     */
    private void requestVoucherApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("event_id", strEventID);
            params.put("voucher_id", strVoucherId);
            params.put("merchant_id", strMerchantId);
            params.put("qty", strQty);
            params.put("discount", String.valueOf(voucherDiscount));
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().requestVoucherApi(params);

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
                                requestVoucherDialog.dismiss();
                                AppUtils.showToast(getActivity(), basicModel.getMessage());
                                requestVoucherDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(basicModel).getCode() == 999) {
                            logout();
                        } else {
                            AppUtils.showToast(getActivity(), basicModel.getMessage());
                        }
                    } else {
                        AppUtils.showToast(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BasicModel> call, @NonNull Throwable t) {
                    hideProgressDialog();
                    if (t instanceof SocketTimeoutException) {
                        AppUtils.showToast(getActivity(), getString(R.string.connection_timeout));
                    } else {
                        t.printStackTrace();
                        AppUtils.showToast(getActivity(), getString(R.string.something_went_wrong));
                    }
                }
            });
        } else {
            AppUtils.showToast(getActivity(), getString(R.string.no_internet));
        }
    }
}
