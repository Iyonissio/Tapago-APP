package com.tapago.app.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tapago.app.R;
import com.tapago.app.adapter.AutoCompleteAdapter;
import com.tapago.app.adapter.CategorySpinnerAdapter;
import com.tapago.app.adapter.MultiSelectorEditAdapter;
import com.tapago.app.adapter.TicketCategoryEditAdapter;
import com.tapago.app.adapter.VoucherEditAdapter;
import com.tapago.app.dialog.VendorSelection;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.BrainTreeTokenModel;
import com.tapago.app.model.CategoryList.CategoryList;
import com.tapago.app.model.CategoryList.Datum;
import com.tapago.app.model.CreateEventModel;
import com.tapago.app.model.EditEventModel.EditEventModel;
import com.tapago.app.model.EditEventModel.Ticket;
import com.tapago.app.model.EditEventModel.VendorList;
import com.tapago.app.model.EditEventModel.Voucher;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.CameraPhoto;
import com.tapago.app.utils.ImageCompression;
import com.tapago.app.utils.MySharedPreferences;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.Orientation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEventActivity extends BaseActivity implements PaymentMethodNonceCreatedListener, BraintreeCancelListener, BraintreeErrorListener, DropInResult.DropInResultListener {

    @BindView(R.id.ivBackArrow)
    ImageView backImg;
    @BindView(R.id.edt_event_name)
    AppCompatEditText edtEventName;
    @BindView(R.id.edt_event_organiser)
    AppCompatEditText edtEventOrganiser;
    @BindView(R.id.edt_start_time)
    AppCompatEditText edtStartTime;
    @BindView(R.id.edt_start_date)
    AppCompatEditText edtStartDate;
    @BindView(R.id.edt_end_time)
    AppCompatEditText edtEndTime;
    @BindView(R.id.edt_end_date)
    AppCompatEditText edtEndDate;
    @BindView(R.id.edt_event_description)
    AppCompatEditText edtEventDescription;
    @BindView(R.id.edt_event_address)
    AppCompatEditText edtEventAddress;
    @BindView(R.id.edt_event_venue)
    AppCompatEditText edtEventVenue;
    @BindView(R.id.add_event_banner)
    AppCompatImageView addEventBanner;
    @BindView(R.id.add_event_thumb)
    AppCompatImageView addEventThumb;
    @BindView(R.id.edt_budget)
    AppCompatEditText edtBudget;
    @BindView(R.id.btnSubmit)
    AppCompatButton btnSubmit;
    @BindView(R.id.categorySpinner)
    Spinner categorySpinner;
    @BindView(R.id.rcCategory)
    RecyclerView rcCategory;
    @BindView(R.id.txtEventCategory)
    AppCompatTextView txtEventCategory;
    @BindView(R.id.txtEventName)
    AppCompatTextView txtEventName;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtStartEventTime)
    AppCompatTextView txtStartEventTime;
    @BindView(R.id.txtEventStartdate)
    AppCompatTextView txtEventStartdate;
    @BindView(R.id.txtEndTime)
    AppCompatTextView txtEndTime;
    @BindView(R.id.txtEndDate)
    AppCompatTextView txtEndDate;
    @BindView(R.id.txtEventDes)
    AppCompatTextView txtEventDes;
    @BindView(R.id.txtEventAddress)
    AppCompatTextView txtEventAddress;
    @BindView(R.id.txtEventVenue)
    AppCompatTextView txtEventVenue;
    @BindView(R.id.txtAddBanner)
    AppCompatTextView txtAddBanner;
    @BindView(R.id.txtAddEventThumb)
    AppCompatTextView txtAddEventThumb;
    @BindView(R.id.txtBudget)
    AppCompatTextView txtBudget;
    @BindView(R.id.edt_event_city)
    AppCompatAutoCompleteTextView edtEventCity;
    @BindView(R.id.txtEventCity)
    AppCompatTextView txtEventCity;
    @BindView(R.id.txtVoucherType)
    AppCompatTextView txtVoucherType;
    @BindView(R.id.txtTicket)
    AppCompatTextView txtTicket;
    @BindView(R.id.rcTicket)
    RecyclerView rcTicket;
    @BindView(R.id.txtSelectVendor)
    AppCompatTextView txtSelectVendor;
    @BindView(R.id.edtSelectVendor)
    AppCompatEditText edtSelectVendor;
    @BindView(R.id.txtEventFee)
    AppCompatTextView txtEventFee;
    @BindView(R.id.edt_event_fee)
    AppCompatEditText edtEventFee;
    @BindView(R.id.txtTotalAmount)
    AppCompatTextView txtTotalAmount;
    @BindView(R.id.edt_event_amount)
    AppCompatEditText edtEventAmount;
    String strEventId = "", startDate = "", endDate = "", startTime = "", endTime = "", checkImage = "", userChoosenTask, bannerPath = "", thumbPath = "", locality = "", categoryId = "", strSelectBanner = "", strSelectThumb = "", strPaymentStatus = "", eventMessage = "", eventFees = "";
    private ArrayList<Datum> categoryArrayList = new ArrayList<>();
    private ArrayList<Voucher> vCategoryArrayList = new ArrayList<>();
    private ArrayList<Ticket> ticketCategoryArrayList = new ArrayList<>();
    VoucherEditAdapter voucherEditAdapter;
    TicketCategoryEditAdapter ticketCategoryAdapter;
    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_WRITE_STORAGE_PERM = 124;
    private static final int RC_READ_STORAGE_PERM = 125;
    private CameraPhoto cameraPhoto;
    private int PICK_CAMERA_REQUEST = 2;
    private int PICK_IMAGE_REQUEST = 1;
    private ArrayList<String> photoPaths = new ArrayList<>();
    List<com.tapago.app.model.Voucher> list = new ArrayList<>();
    List<com.tapago.app.model.Ticket> ticketlist = new ArrayList<>();
    Geocoder geocoder;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelnew();
        }
    };
    private String token = "", paymentMethodNonce1 = "", Payment_Type = "";
    private int REQUEST_CODE = 145;
    private PaymentMethodNonce mNonce;
    private PaymentMethodType mPaymentMethodType;
    private VendorSelection vendorSelection;
    private StringBuilder sb, sb1, listCheck;
    ArrayList<VendorList> vendorListUser = new ArrayList<>();
    MultiSelectorEditAdapter multiSelectorAdapter;
    PlacesClient placesClient;
    AutoCompleteAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        vendorSelection = new VendorSelection(getActivity());
        sb = new StringBuilder();
        sb1 = new StringBuilder();
        listCheck = new StringBuilder();
        Intent intent = getIntent();
        if (intent != null) {
            strEventId = intent.getStringExtra("eventId");
            strPaymentStatus = intent.getStringExtra("paymentStatus");
        }

        try {
            txtEventCategory.setText(MySharedPreferences.getMySharedPreferences().getEvent_Category());
            txtEventName.setText(MySharedPreferences.getMySharedPreferences().getEvent_Name());
            edtEventName.setHint(MySharedPreferences.getMySharedPreferences().getEnter_Your_Event_Name());
            txtStartEventTime.setText(MySharedPreferences.getMySharedPreferences().getStart_Event_Time());
            edtStartTime.setHint(MySharedPreferences.getMySharedPreferences().getYour_Event_Time());
            txtEventStartdate.setText(MySharedPreferences.getMySharedPreferences().getStart_Event_Date());
            edtStartDate.setHint(MySharedPreferences.getMySharedPreferences().getYour_Event_Date());
            txtEndDate.setText(MySharedPreferences.getMySharedPreferences().getEnd_Event_Date());
            edtEndDate.setHint(MySharedPreferences.getMySharedPreferences().getYour_Event_Date());
            txtEndTime.setText(MySharedPreferences.getMySharedPreferences().getEnd_Event_Time());
            edtEndTime.setHint(MySharedPreferences.getMySharedPreferences().getYour_Event_Time());
            txtEventDes.setText(MySharedPreferences.getMySharedPreferences().getEvent_Description());
            edtEventDescription.setHint(MySharedPreferences.getMySharedPreferences().getWrite_Something_About_Your_Event());
            txtEventAddress.setText(MySharedPreferences.getMySharedPreferences().getEvent_Address());
            edtEventAddress.setHint(MySharedPreferences.getMySharedPreferences().getEnter_Your_Event_Address());
            txtEventVenue.setText(MySharedPreferences.getMySharedPreferences().getEvent_Vanue());
            edtEventVenue.setHint(MySharedPreferences.getMySharedPreferences().getEnter_Your_Event_Vanue());
            txtAddBanner.setText(MySharedPreferences.getMySharedPreferences().getAdd_Event_Banner());
            txtAddEventThumb.setText(MySharedPreferences.getMySharedPreferences().getAdd_Event_Banner_Thumb());
            txtBudget.setText(MySharedPreferences.getMySharedPreferences().getBudget());
            btnSubmit.setText(MySharedPreferences.getMySharedPreferences().getUpdateEvent());
            txtName.setText(MySharedPreferences.getMySharedPreferences().getUpdateEvent());
            txtEventCity.setText(MySharedPreferences.getMySharedPreferences().getCityCaption());
            edtEventCity.setHint(MySharedPreferences.getMySharedPreferences().getEnterCity());
            txtVoucherType.setText(MySharedPreferences.getMySharedPreferences().getVoucherType());
            txtTicket.setText(MySharedPreferences.getMySharedPreferences().getTicketType());
            txtSelectVendor.setText(MySharedPreferences.getMySharedPreferences().getSelect_vendor());
            edtSelectVendor.setHint(MySharedPreferences.getMySharedPreferences().getSelect_vendor());
            txtEventFee.setText(MySharedPreferences.getMySharedPreferences().getEventFees());
            txtTotalAmount.setText(MySharedPreferences.getMySharedPreferences().getTotalAmount());
            rcTicket.setNestedScrollingEnabled(false);
            rcCategory.setNestedScrollingEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Datum language = categoryArrayList.get(position - 1);
                    categoryId = String.valueOf(language.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (strPaymentStatus.equalsIgnoreCase("Pending")) {
            edtBudget.setEnabled(true);
        } else {
            edtBudget.setEnabled(false);
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.apiKey));
        }

        placesClient = Places.createClient(this);
        edtEventCity.setThreshold(1);
        edtEventCity.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        edtEventCity.setAdapter(adapter);

        categoryListApi();

    }

    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
                            locality = task.getPlace().getAddress();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();

                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    @OnClick({R.id.ivBackArrow, R.id.add_event_banner, R.id.add_event_thumb, R.id.btnSubmit, R.id.edt_start_date, R.id.edt_end_date, R.id.edt_start_time, R.id.edt_end_time, R.id.edtSelectVendor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.add_event_banner:
                checkImage = "banner";
                selectImage();
                break;
            case R.id.add_event_thumb:
                checkImage = "thumb";
                selectImage();
                break;
            case R.id.btnSubmit:
                createEventValidation();
                break;
            case R.id.edt_start_date:
                AppUtils.hideSoftKeyboard(getActivity());
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok), datePickerDialog);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), datePickerDialog);
                datePickerDialog.show();
                break;
            case R.id.edt_end_date:
                AppUtils.hideSoftKeyboard(getActivity());
                if (TextUtils.isEmpty(startDate)) {
                    showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Select_Start_Date());
                    return;
                }
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(Objects.requireNonNull(getActivity()), date2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog2.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog2.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok), datePickerDialog2);
                datePickerDialog2.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), datePickerDialog2);
                datePickerDialog2.show();
                break;
            case R.id.edt_start_time:
                AppUtils.hideSoftKeyboard(getActivity());
                selectTime();
                break;
            case R.id.edt_end_time:
                AppUtils.hideSoftKeyboard(getActivity());
                if (TextUtils.isEmpty(startTime)) {
                    showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Select_Start_Time());
                    return;
                }

                selectTime2();
                break;
            case R.id.edtSelectVendor:
                try {
                    if (sb1.length() > 0) {
                        multiSelectorAdapter = new MultiSelectorEditAdapter(getActivity(), vendorListUser, sb1.toString());
                        vendorSelection.rcList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        vendorSelection.rcList.setAdapter(multiSelectorAdapter);
                    } else {
                        multiSelectorAdapter = new MultiSelectorEditAdapter(getActivity(), vendorListUser, "");
                        vendorSelection.rcList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        vendorSelection.rcList.setAdapter(multiSelectorAdapter);
                    }
                    vendorSelection.txtOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sb = new StringBuilder();
                            sb1 = new StringBuilder();
                            listCheck = new StringBuilder();
                            for (int i = 0; i < vendorListUser.size(); i++) {
                                if (vendorListUser.get(i).getCheckFlag().equals("checked")) {
                                    sb.append(vendorListUser.get(i).getId()).append(",");
                                    sb1.append(vendorListUser.get(i).getId()).append(",");
                                    listCheck.append(vendorListUser.get(i).getFirstName()).append(",");
                                }
                            }
                            if (sb.length() > 0) {
                                String str = sb.toString().substring(0, sb.toString().length() - 1);
                                String str1 = sb1.toString().substring(0, sb1.toString().length() - 1);
                                String strList = listCheck.toString().substring(0, listCheck.toString().length() - 1);
                                sb = new StringBuilder();
                                sb1 = new StringBuilder();
                                edtSelectVendor.setText(strList);
                                sb.append(str);
                                sb1.append("[").append(str1).append("]");
                            } else {
                                edtSelectVendor.setText("");
                            }
                            vendorSelection.dismiss();
                        }
                    });
                    vendorSelection.txtCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            vendorSelection.dismiss();
                        }
                    });
                    vendorSelection.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void createEventValidation() {
        String d1 = startDate + " " + startTime;
        String d2 = endDate + " " + endTime;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        boolean b = false;
        try {
            if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                b = true;//If start date is before end date
            } else if (dfDate.parse(d1).equals(dfDate.parse(d2))) {
                b = true;//If two dates are equal
            } else if (dfDate.parse(d2).before(dfDate.parse(d1))) {
                b = false; //If start date is after the end date
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (categorySpinner.getSelectedItemPosition() < 1) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Select_Category());
        } else if (TextUtils.isEmpty(AppUtils.getText(edtSelectVendor))) {
            showSnackBar(getActivity(), getString(R.string.please_select_vendor));
        } else if (TextUtils.isEmpty(AppUtils.getText(edtEventName))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Enter_Event_Name());
        } else if (TextUtils.isEmpty(startDate)) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Select_Start_Date());
        } else if (TextUtils.isEmpty(endDate)) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Select_End_Date());
        } else if (TextUtils.isEmpty(startTime)) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Select_Start_Time());
        } else if (TextUtils.isEmpty(endTime)) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Select_End_Time());
        } else if (!b) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getAfterEndDate());
        } else if (TextUtils.isEmpty(AppUtils.getText(edtEventDescription))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Enter_Event_Description());
        } else if (TextUtils.isEmpty(AppUtils.getText(edtEventAddress))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Enter_Event_Address());
        } else if (TextUtils.isEmpty(AppUtils.getText(edtEventCity))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getEnterCity());
        } else if (TextUtils.isEmpty(AppUtils.getText(edtEventVenue))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Enter_Event_Venue());
        } else if (TextUtils.isEmpty(AppUtils.getText(edtBudget))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Enter_Event_Budget());
        } else if (TextUtils.isEmpty(bannerPath)) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Select_Banner_Image());
        } else if (TextUtils.isEmpty(thumbPath)) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Select_Thumb_Image());
        } else {
            try {
                list = new ArrayList<>();
                float totalAmount = 0;
                for (int i = 0; i < vCategoryArrayList.size(); i++) {
                    View view = rcCategory.getChildAt(i);
                    EditText edQty = view.findViewById(R.id.edQty);
                    EditText edAmount = view.findViewById(R.id.edAmount);
                    EditText edDiscount = view.findViewById(R.id.edDisCount);
                    TextView txtAmount = view.findViewById(R.id.txtTotal);
                    String qty = edQty.getText().toString();
                    String amount = edAmount.getText().toString();
                    String discount = edDiscount.getText().toString();
                    String id = String.valueOf(vCategoryArrayList.get(i).getId());
                    String totalAmounts = txtAmount.getText().toString().replace("MT", "");

                    if (qty.length() > 0 && amount.length() > 0 && totalAmounts.length() > 0) {
                        totalAmount = Float.valueOf(totalAmounts) + totalAmount;
                        com.tapago.app.model.Voucher vc = new com.tapago.app.model.Voucher();
                        vc.setId(id);
                        vc.setQty(qty);
                        vc.setAmount(amount);
                        if (discount.equals("")) {
                            vc.setVoucher_discount("0");
                        } else {
                            vc.setVoucher_discount(discount);
                        }
                        list.add(vc);
                    }
                }
                ticketlist = new ArrayList<>();
                float totalAmountTicket = 0;
                for (int i = 0; i < ticketCategoryArrayList.size(); i++) {
                    View view = rcTicket.getChildAt(i);
                    EditText edQty = view.findViewById(R.id.edQty);
                    EditText edAmount = view.findViewById(R.id.edAmount);
                    EditText edDiscount = view.findViewById(R.id.edDisCount);
                    TextView txtAmount = view.findViewById(R.id.txtTotal);
                    String qty = edQty.getText().toString();
                    String amount = edAmount.getText().toString();
                    String discount = edDiscount.getText().toString();
                    String id = String.valueOf(ticketCategoryArrayList.get(i).getId());
                    String totalAmounts = txtAmount.getText().toString().replace("MT", "");
                    if (qty.length() > 0 && amount.length() > 0 && totalAmounts.length() > 0) {
                        totalAmountTicket = Float.valueOf(totalAmounts) + totalAmountTicket;
                        com.tapago.app.model.Ticket vc = new com.tapago.app.model.Ticket();
                        vc.setId(id);
                        vc.setQty(qty);
                        vc.setAmount(amount);
                        if (discount.equals("")) {
                            vc.setTicket_discount("0");
                        } else {
                            vc.setTicket_discount(discount);
                        }
                        ticketlist.add(vc);
                    }
                }

                float mainAmount;
                mainAmount = totalAmount + totalAmountTicket;

                if (totalAmountTicket == 0.0 && ticketlist.size() == 0) {
                    showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getFilledAllDetail());
                } else if (Float.valueOf(AppUtils.getText(edtBudget)) < mainAmount) {
                    showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPlease_Enter_Lowest_Amount());
                } else {
                    if (strPaymentStatus.equalsIgnoreCase("Pending")) {
                        new android.support.v7.app.AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                                .setMessage(eventMessage + " " + "MT" + eventFees)
                                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        updateEventApi();
                                    }
                                })
                                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    } else {
                        updateEventApi();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void brainTreeTokenApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            map.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            map.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());

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
        checkImage = "";
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_library), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.take_photo))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        userChoosenTask = "Take Photo";
                        callWrite();
                    } else {
                        CaptureImage();
                    }
                } else if (items[item].equals(getString(R.string.choose_library))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        userChoosenTask = "Choose from Library";
                        callWrite();
                    } else {
                        PickGalleryImage();
                    }
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @AfterPermissionGranted(RC_WRITE_STORAGE_PERM)
    private void callWrite() {
        if (EasyPermissions.hasPermissions(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Have permission, do the thing!
            callRead();
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, RC_WRITE_STORAGE_PERM, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .setRationale(R.string.rationale_call)
                            .setPositiveButtonText(getString(R.string.ok))
                            .setNegativeButtonText(getString(R.string.cancel))
                            .build());
        }
    }

    @AfterPermissionGranted(RC_READ_STORAGE_PERM)
    private void callRead() {
        if (EasyPermissions.hasPermissions(Objects.requireNonNull(getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Have permission, do the thing!
            switch (userChoosenTask) {
                case "Take Photo":
                    callCamera();
                    break;
                case "Choose from Library":
                    PickGalleryImage();
                    break;
            }
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, RC_READ_STORAGE_PERM, Manifest.permission.READ_EXTERNAL_STORAGE)
                            .setRationale(R.string.rationale_call)
                            .setPositiveButtonText(getString(R.string.ok))
                            .setNegativeButtonText(getString(R.string.cancel))
                            .build());
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    private void callCamera() {
        if (EasyPermissions.hasPermissions(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
            switch (userChoosenTask) {
                case "Take Photo":
                    CaptureImage();
                    break;
            }
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

    /**
     * capture image from camera
     */
    private void CaptureImage() {
        cameraPhoto = new CameraPhoto(getActivity());
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(), PICK_CAMERA_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * pick image from gallery
     */
    private void PickGalleryImage() {
        FilePickerBuilder.getInstance()
                .setMaxCount(1)
                .setSelectedFiles(photoPaths)
                .setActivityTheme(R.style.FilePickerTheme)
                .setActivityTitle("Please select media")
                .enableCameraSupport(false)
                .enableVideoPicker(false)
                .showGifs(false)
                .showFolderView(true)
                .enableSelectAll(false)
                .enableImagePicker(true)
                .withOrientation(Orientation.PORTRAIT_ONLY)
                .pickPhoto(this, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (checkImage.equals("banner")) {
            strSelectBanner = "banner";
            if (requestCode == PICK_CAMERA_REQUEST && resultCode == RESULT_OK) {
                cameraPhoto.addToGallery();
                bannerPath = cameraPhoto.getPhotoPath();
                bannerPath = ImageCompression.compressImage(bannerPath);
                Glide.with(Objects.requireNonNull(getActivity())).load(bannerPath).into(addEventBanner);
            } else if (requestCode == PICK_IMAGE_REQUEST) {
                if (resultCode == RESULT_OK && data != null) {
                    photoPaths = new ArrayList<>();
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    for (int i = 0; i < photoPaths.size(); i++) {
                        bannerPath = photoPaths.get(i);
                    }
                    Glide.with(Objects.requireNonNull(getActivity())).load(bannerPath).into(addEventBanner);
                }
            }
        } else if (checkImage.equals("thumb")) {
            strSelectThumb = "thumb";
            if (requestCode == PICK_CAMERA_REQUEST && resultCode == RESULT_OK) {
                cameraPhoto.addToGallery();
                thumbPath = cameraPhoto.getPhotoPath();
                thumbPath = ImageCompression.compressImage(thumbPath);
                Glide.with(Objects.requireNonNull(getActivity())).load(thumbPath).into(addEventThumb);
            } else if (requestCode == PICK_IMAGE_REQUEST) {
                if (resultCode == RESULT_OK && data != null) {
                    photoPaths = new ArrayList<>();
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    for (int i = 0; i < photoPaths.size(); i++) {
                        thumbPath = photoPaths.get(i);
                    }
                    Glide.with(Objects.requireNonNull(getActivity())).load(thumbPath).into(addEventThumb);
                }

            }
        } else {
            if (requestCode == REQUEST_CODE && data != null) {
                if (resultCode == Activity.RESULT_OK) {
                    DropInResult result = Objects.requireNonNull(data).getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                    displayResult(Objects.requireNonNull(result.getPaymentMethodNonce()), result.getDeviceData());
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // canceled
                    Toast.makeText(getActivity(), "User Cancel", Toast.LENGTH_SHORT).show();
                } else {
                    // an error occurred, checked the returned exception
                    Exception exception = (Exception) Objects.requireNonNull(data).getSerializableExtra(DropInActivity.EXTRA_ERROR);
                    Log.e("exception", exception.toString());
                }
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
            updateEventApi();
        }

    }

    private String formatAddress(PostalAddress address) {
        return address.getRecipientName() + " " + address.getStreetAddress() + " " +
                address.getExtendedAddress() + " " + address.getLocality() + " " + address.getRegion() +
                " " + address.getPostalCode() + " " + address.getCountryCodeAlpha2();
    }

    private void selectTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Time time = new Time(selectedHour, selectedMinute, 0);
                //little h uses 12 hour format and big H uses 24 hour format
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
                //format takes in a Date, and Time is a sublcass of Date
                startTime = simpleDateFormat.format(time);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm:ss a");
                //format takes in a Date, and Time is a sublcass of Date
                String s = simpleDateFormat1.format(time);
                edtStartTime.setText(s);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok), mTimePicker);
        mTimePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), mTimePicker);
        mTimePicker.show();

    }

    private void selectTime2() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Time time = new Time(selectedHour, selectedMinute, 0);
                //little h uses 12 hour format and big H uses 24 hour format
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
                //format takes in a Date, and Time is a sublcass of Date
                endTime = simpleDateFormat.format(time);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm:ss a");
                //format takes in a Date, and Time is a sublcass of Date
                String s = simpleDateFormat1.format(time);
                edtEndTime.setText(s);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok), mTimePicker);
        mTimePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), mTimePicker);
        mTimePicker.show();
    }

    private void getEventApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            // showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("event_id", strEventId);

            Call<EditEventModel> call;
            call = RetrofitRestClient.getInstance().editApi(params);

            if (call == null) return;

            call.enqueue(new Callback<EditEventModel>() {
                @Override
                public void onResponse(@NonNull final Call<EditEventModel> call, @NonNull Response<EditEventModel> response) {
                    hideProgressDialog();
                    final EditEventModel editEventModel;
                    if (response.isSuccessful()) {
                        editEventModel = response.body();
                        if (Objects.requireNonNull(editEventModel).getCode() == 200) {
                            try {
                                eventFees = editEventModel.getData().getEventFee();
                                eventMessage = editEventModel.getData().getEventFeeMessage();
                                edtEventFee.setText(eventFees);
                                if (eventFees.length() > 0 && editEventModel.getData().getEventBudget().length() > 0) {
                                    float sum = Float.valueOf(editEventModel.getData().getEventBudget()) + Float.valueOf(edtEventFee.getText().toString());
                                    String firstNumberAsString = String.format("%.0f", sum);
                                    edtEventAmount.setText(firstNumberAsString);
                                }
                                edtEventName.setText(editEventModel.getData().getEventTitle());
                                String[] splited = editEventModel.getData().getEventStartDateTime().split("\\s+");
                                edtStartDate.setText(splited[0]);
                                startDate = splited[0];
                                startTime = splited[1] + " " + splited[2];
                                edtStartTime.setText(splited[1] + " " + splited[2]);
                                String[] spliteid = editEventModel.getData().getEventEnddateTime().split("\\s+");
                                edtEndDate.setText(spliteid[0]);
                                endDate = spliteid[0];
                                endTime = spliteid[1] + " " + spliteid[2];
                                edtEndTime.setText(spliteid[1] + " " + spliteid[2]);
                                edtEventDescription.setText(editEventModel.getData().getEventDescription());
                                edtEventCity.setText(editEventModel.getData().getCity());
                                edtEventAddress.setText(editEventModel.getData().getEventAddress());
                                bannerPath = editEventModel.getData().getEventBannerImg();
                                thumbPath = editEventModel.getData().getEventThumbImg();
                                Glide.with(getActivity()).load(editEventModel.getData().getEventBannerImg()).into(addEventBanner);
                                Glide.with(getActivity()).load(editEventModel.getData().getEventThumbImg()).into(addEventThumb);
                                edtBudget.setText(String.valueOf(editEventModel.getData().getEventBudget()));
                                edtEventVenue.setText(editEventModel.getData().getEventVenue());

                                rcCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
                                vCategoryArrayList.addAll(editEventModel.getData().getVoucher());
                                voucherEditAdapter = new VoucherEditAdapter(getActivity(), editEventModel.getData().getVoucher());
                                rcCategory.setAdapter(voucherEditAdapter);

                                rcTicket.setLayoutManager(new LinearLayoutManager(getActivity()));
                                ticketCategoryArrayList.addAll(editEventModel.getData().getTicket());
                                ticketCategoryAdapter = new TicketCategoryEditAdapter(getActivity(), editEventModel.getData().getTicket());
                                rcTicket.setAdapter(ticketCategoryAdapter);

                                vendorListUser.addAll(editEventModel.getData().getVendorList());

                                for (int i = 0; i < vendorListUser.size(); i++) {
                                    if (vendorListUser.get(i).getCheckFlag().equals("checked")) {
                                        sb.append(vendorListUser.get(i).getId()).append(",");
                                        sb1.append(vendorListUser.get(i).getId()).append(",");
                                        listCheck.append(vendorListUser.get(i).getFirstName()).append(",");
                                    }
                                }
                                if (sb.length() > 0) {
                                    String str = sb.toString().substring(0, sb.toString().length() - 1);
                                    String str1 = sb1.toString().substring(0, sb1.toString().length() - 1);
                                    String strList = listCheck.toString().substring(0, listCheck.toString().length() - 1);
                                    sb = new StringBuilder();
                                    sb1 = new StringBuilder();
                                    edtSelectVendor.setText(strList);
                                    sb.append(str);
                                    sb1.append("[").append(str1).append("]");
                                } else {
                                    edtSelectVendor.setText("");
                                }

                                if (vCategoryArrayList.size() == 0) {
                                    txtVoucherType.setVisibility(View.GONE);
                                }

                                if (ticketCategoryArrayList.size() == 0) {
                                    txtTicket.setVisibility(View.GONE);
                                }

                                if (categoryArrayList != null) {
                                    selectCategory(categoryArrayList, editEventModel.getData().getCategoryId());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(editEventModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), editEventModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EditEventModel> call, @NonNull Throwable t) {
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

    private void categoryListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("deviceid", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<CategoryList> call;
            call = RetrofitRestClient.getInstance().categoryList(params);

            if (call == null) return;

            call.enqueue(new Callback<CategoryList>() {
                @Override
                public void onResponse(@NonNull final Call<CategoryList> call, @NonNull Response<CategoryList> response) {
                    //hideProgressDialog();
                    final CategoryList categoryList;
                    if (response.isSuccessful()) {
                        categoryList = response.body();
                        if (Objects.requireNonNull(categoryList).getCode() == 200) {
                            try {
                                categoryArrayList.clear();
                                categoryArrayList.addAll(categoryList.getData());
                                categorySpinner.setAdapter(new CategorySpinnerAdapter(getActivity(), categoryList.getData()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(categoryList).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), categoryList.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }

                    getEventApi();
                }

                @Override
                public void onFailure(@NonNull Call<CategoryList> call, @NonNull Throwable t) {
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

    private void selectCategory(ArrayList<Datum> categoryArrayList, Object value) {
        for (int i = 0; i < categoryArrayList.size(); i++) {
            if (categoryArrayList.get(i).getId().equals(value)) {
                categorySpinner.setSelection(i + 1);
                break;
            }
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startDate = sdf.format(myCalendar.getTime());
        edtStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelnew() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        endDate = sdf.format(myCalendar.getTime());
        edtEndDate.setText(sdf.format(myCalendar.getTime()));
    }


    /**
     * update event Api
     */
    private void updateEventApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, RequestBody> params = new HashMap<>();
            params.put("user_id", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getUserId()));
            params.put("device_id", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getDeviceId()));
            params.put("access_token", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getAccessToken()));
            params.put("category_id", AppUtils.getRequestBody(categoryId));
            params.put("event_id", AppUtils.getRequestBody(strEventId));
            params.put("event_title", AppUtils.getRequestBody(AppUtils.getText(edtEventName)));
            params.put("event_description", AppUtils.getRequestBody(AppUtils.getText(edtEventDescription)));
            params.put("event_start_date_time", AppUtils.getRequestBody(startDate + " " + startTime));
            params.put("event_enddate_time", AppUtils.getRequestBody(endDate + " " + endTime));
            params.put("event_budget", AppUtils.getRequestBody(AppUtils.getText(edtBudget)));
            params.put("event_venue", AppUtils.getRequestBody(AppUtils.getText(edtEventVenue)));
            params.put("event_address", AppUtils.getRequestBody(AppUtils.getText(edtEventAddress)));
            if (locality.length() > 0) {
                params.put("city", AppUtils.getRequestBody(locality));
            } else {
                params.put("city", AppUtils.getRequestBody(AppUtils.getText(edtEventCity)));
            }
            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();
            params.put("detail", AppUtils.getRequestBody(String.valueOf(myCustomArray)));
            Gson gson1 = new GsonBuilder().create();
            JsonArray myTicket = gson1.toJsonTree(ticketlist).getAsJsonArray();
            params.put("ticket_detail", AppUtils.getRequestBody(String.valueOf(myTicket)));
            params.put("vendor_details", AppUtils.getRequestBody(sb.toString()));
            params.put("event_fee", AppUtils.getRequestBody(AppUtils.getText(edtEventFee)));
            params.put("lang", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getLanguage()));

            MultipartBody.Part partbody1 = null;
            if (strSelectBanner.equals("banner")) {
                if (bannerPath.length() > 0) {
                    File file = new File(bannerPath);
                    RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), file);
                    partbody1 = MultipartBody.Part.createFormData("banner_image", file.getName(), reqFile1);
                }
            }
            MultipartBody.Part partbody2 = null;
            if (strSelectThumb.equals("thumb")) {
                if (thumbPath.length() > 0) {
                    File file2 = new File(thumbPath);
                    RequestBody reqFile2 = RequestBody.create(MediaType.parse("image/*"), file2);
                    partbody2 = MultipartBody.Part.createFormData("thumb_image", file2.getName(), reqFile2);
                }
            }

            Call<CreateEventModel> call;
            call = RetrofitRestClient.getInstance().updateApi(params, partbody1, partbody2);

            if (call == null) return;

            call.enqueue(new Callback<CreateEventModel>() {
                @Override
                public void onResponse(@NonNull Call<CreateEventModel> call, @NonNull Response<CreateEventModel> response) {
                    hideProgressDialog();
                    final CreateEventModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            Toast.makeText(EditEventActivity.this, basicModel.getMessage(), Toast.LENGTH_SHORT).show();
                            if (strPaymentStatus.equalsIgnoreCase("Pending")) {
                                createPaymentApi(basicModel.getEventId());
                            } else {
                                finish();
                                AppUtils.finishFromLeftToRight(getActivity());
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
                public void onFailure(@NonNull Call<CreateEventModel> call, @NonNull Throwable t) {
                    hideProgressDialog();
                    if (t instanceof SocketTimeoutException) {
                        showSnackBar(getActivity(), getString(R.string.connection_timeout));
                    } else {
                        t.printStackTrace();
                        showSnackBar(getActivity(), getString(R.string.something_went_wrong));
                    }
                }
            });
        }
    }


    public void createPaymentApi(String eventId) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            map.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            map.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            map.put("amount", AppUtils.getText(edtBudget));
            map.put("event_fee", AppUtils.getText(edtEventFee));
            map.put("payment_method_nonce", "");
            map.put("event_id", eventId);
            map.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            map.put("payment_type", "COD");

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().createEventApi(map);

            if (call == null) return;
            call.enqueue(new Callback<BasicModel>() {
                @Override

                public void onResponse(@NonNull Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            try {
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                                AppUtils.finishFromLeftToRight(getActivity());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(basicModel).getCode() == 999) {
                            logout();
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
