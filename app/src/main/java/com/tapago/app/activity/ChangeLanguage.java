package com.tapago.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

import com.tapago.app.R;
import com.tapago.app.model.CategoryCaption.Activity;
import com.tapago.app.model.CategoryCaption.Category;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeLanguage extends BaseActivity {

    @BindView(R.id.imgEn)
    ImageView imgEn;
    @BindView(R.id.imgSp)
    ImageView imgSp;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelang);
        ButterKnife.bind(this);

        if (MySharedPreferences.getMySharedPreferences().getLanguage().equals("en")) {
            imgEn.setVisibility(View.VISIBLE);
            imgSp.setVisibility(View.GONE);
        } else {
            imgEn.setVisibility(View.GONE);
            imgSp.setVisibility(View.VISIBLE);
        }

        txtName.setText(MySharedPreferences.getMySharedPreferences().getChangeLanguage());

    }


    @OnClick({R.id.rlEnglish, R.id.rlSpanish, R.id.ivBackArrow})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.rlEnglish:
                MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
                mySharedPreferences.setLanguage("en");

                imgEn.setVisibility(View.VISIBLE);
                imgSp.setVisibility(View.GONE);

                categoryCaptionApi();
                break;
            case R.id.rlSpanish:
                MySharedPreferences mySharedPreferences1 = MySharedPreferences.getMySharedPreferences();
                mySharedPreferences1.setLanguage("pt");

                imgEn.setVisibility(View.GONE);
                imgSp.setVisibility(View.VISIBLE);

                categoryCaptionApi();
                break;
            case R.id.ivBackArrow:
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
        }
    }


    /**
     * CategoryCaptionApi
     */
    private void categoryCaptionApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("activity_name", "CategoryActivity");
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<Category> call;
            call = RetrofitRestClient.getInstance().categorycaptionApi(params);

            if (call == null) return;

            call.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(@NonNull final Call<Category> call, @NonNull Response<Category> response) {
                    hideProgressDialog();
                    final Category category;
                    if (response.isSuccessful()) {
                        category = response.body();
                        if (Objects.requireNonNull(category).getCode() == 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        setData(category.getActivity());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (Objects.requireNonNull(category).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), category.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Category> call, @NonNull Throwable t) {
                    hideProgressDialog();
                    try {
                        if (t instanceof SocketTimeoutException) {
                            showSnackBar(getActivity(), getString(R.string.connection_timeout));
                        } else {
                            t.printStackTrace();
                            showSnackBar(getActivity(), getString(R.string.something_went_wrong));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showSnackBar(getActivity(), getString(R.string.no_internet));
        }
    }

    private void setData(Activity activity) {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
        mySharedPreferences.setSelectCategories(activity.getSelectCategories());
        mySharedPreferences.setNext(activity.getNext());
        mySharedPreferences.setStrSelectCategories(activity.getCategorySelect());
        mySharedPreferences.setRecentlyView(activity.getRecentlyViewed());
        mySharedPreferences.setPaymentHistory(activity.getPaymentHistory());
        mySharedPreferences.setMyVoucher(activity.getMyVoucher());
        mySharedPreferences.setAboutUs(activity.getAboutUs());
        mySharedPreferences.setPrivacyPolicy(activity.getPrivacyPolicy());
        mySharedPreferences.setTermsCondition(activity.getTermsConditions());
        mySharedPreferences.setNeedHelp(activity.getNeedHelp());
        mySharedPreferences.setPopularEvent(activity.getPopularEvents());
        mySharedPreferences.setSlug(activity.getSlug());
        mySharedPreferences.setVersion(activity.getVirsion());
        mySharedPreferences.setHereHelp(activity.getWeAreHereToHelp());
        mySharedPreferences.setAskQuestion(activity.getAskAQuestion());
        mySharedPreferences.setSearch(activity.getSearch());
        mySharedPreferences.setNoEventFound(activity.getNoEventFound());
        mySharedPreferences.setPopularTopic(activity.getPopularTopic());
        mySharedPreferences.setEnterQuestion(activity.getPleaseEnterQuestion());
        mySharedPreferences.setFirst_Name_Caption(activity.getFirstName());
        mySharedPreferences.setLast_Name_Caption(activity.getLastName());
        mySharedPreferences.setEmail_Caption(activity.getEmail());
        mySharedPreferences.setPhone_Caption(activity.getPhone());
        mySharedPreferences.setUpdateCity(activity.getUpdateCity());
        mySharedPreferences.setChangePassword(activity.getChangePassword());
        mySharedPreferences.setSubmit(activity.getSubmit());
        mySharedPreferences.setPleaseEnterFirstName(activity.getPleaseEnterFirstName());
        mySharedPreferences.setPleaseEnterLastName(activity.getPleaseEnterLastName());
        mySharedPreferences.setPleaseEnterYourEmail(activity.getPleaseEnterYourEmail());
        mySharedPreferences.setPleaseEnterValidEmail(activity.getPleaseEnterValidEmail());
        mySharedPreferences.setPleaseEnterMobileNumber(activity.getPleaseEnterMobileNumber());
        mySharedPreferences.setPleaseEnterValidMobileNumber(activity.getPleaseEnterValidMobileNumber());
        mySharedPreferences.setOldPassword(activity.getOldPassword());
        mySharedPreferences.setNewPassword(activity.getNewPassword());
        mySharedPreferences.setConfirmPassword(activity.getConfirmPassword());
        mySharedPreferences.setPleaseEnterOldPassword(activity.getPleaseEnterOldPassword());
        mySharedPreferences.setPleaseEnterNewPassword(activity.getPleaseEnterNewPassword());
        mySharedPreferences.setPassword_should_be_minimum_6_characters(activity.getPasswordShouldBeMinimum6Characters());
        mySharedPreferences.setPlease_Enter_Confirm_Password(activity.getPleaseEnterConfirmPassword());
        mySharedPreferences.setPassword_And_Confirm_Password_Does_Not_Match(activity.getPasswordAndConfirmPasswordDoesNotMatch());
        mySharedPreferences.setEvent_Category(activity.getEventCategory());
        mySharedPreferences.setEvent_Name(activity.getEventName());
        mySharedPreferences.setEnter_Your_Event_Name(activity.getEnterYourEventName());
        mySharedPreferences.setStart_Event_Time(activity.getStartEventTime());
        mySharedPreferences.setYour_Event_Time(activity.getYourEventTime());
        mySharedPreferences.setStart_Event_Date(activity.getStartEventDate());
        mySharedPreferences.setYour_Event_Date(activity.getYourEventDate());
        mySharedPreferences.setEnd_Event_Time(activity.getEndEventTime());
        mySharedPreferences.setEnd_Event_Date(activity.getEndEventDate());
        mySharedPreferences.setEvent_Description(activity.getEventDescription());
        mySharedPreferences.setWrite_Something_About_Your_Event(activity.getWriteSomethingAboutYourEvent());
        mySharedPreferences.setEvent_Address(activity.getEventAddress());
        mySharedPreferences.setEnter_Your_Event_Address(activity.getEnterYourEventAddress());
        mySharedPreferences.setEvent_Vanue(activity.getEventVenue());
        mySharedPreferences.setEnter_Your_Event_Vanue(activity.getEnterYourEventVenue());
        mySharedPreferences.setAdd_Event_Banner(activity.getAddEventBanner());
        mySharedPreferences.setAdd_Event_Banner_Thumb(activity.getAddEventBannerThumb());
        mySharedPreferences.setBudget(activity.getBudget());
        mySharedPreferences.setCreate_Event(activity.getCreateEvent());
        mySharedPreferences.setPlease_Enter_Event_Name(activity.getPleaseEnterEventName());
        mySharedPreferences.setPlease_Select_Start_Date(activity.getPleaseSelectStartDate());
        mySharedPreferences.setPlease_Select_End_Date(activity.getPleaseSelectEndDate());
        mySharedPreferences.setPlease_Select_Start_Time(activity.getPleaseSelectStartTime());
        mySharedPreferences.setPlease_Select_End_Time(activity.getPleaseSelectEndTime());
        mySharedPreferences.setPlease_Enter_Event_Description(activity.getPleaseEnterEventDescription());
        mySharedPreferences.setPlease_Enter_Event_Address(activity.getPleaseEnterEventAddress());
        mySharedPreferences.setPlease_Enter_Event_Venue(activity.getPleaseEnterEventVenue());
        mySharedPreferences.setPlease_Enter_Event_Budget(activity.getPleaseEnterEventBudget());
        mySharedPreferences.setPlease_Select_Banner_Image(activity.getPleaseSelectBannerImage());
        mySharedPreferences.setPlease_Select_Thumb_Image(activity.getPleaseSelectThumbImage());
        mySharedPreferences.setPlease_Select_Category(activity.getPleaseSelectCategory());
        mySharedPreferences.setPlease_Enter_Lowest_Amount(activity.getPleaseEnterLowestAmount());
        mySharedPreferences.setCityCaption(activity.getCity());
        mySharedPreferences.setEnterCity(activity.getEnterEventCity());
        mySharedPreferences.setAbout(activity.getAbout());
        mySharedPreferences.setVenue(activity.getVenue());
        mySharedPreferences.setDirection(activity.getGetDirection());
        mySharedPreferences.setOrganiser(activity.getOrganiser());
        mySharedPreferences.setMoreLike(activity.getMoreLikeThis());
        mySharedPreferences.setRequestVoucher(activity.getRequestVoucher());
        mySharedPreferences.setRedeemVoucher(activity.getRedeemVoucher());
        mySharedPreferences.setEnterCityName(activity.getEnterCityName());
        mySharedPreferences.setAddMultiple(activity.getYouMayAddMultiple());
        mySharedPreferences.setUpdateEvent(activity.getUpdateEvent());
        mySharedPreferences.setVoucherType(activity.getVoucherType());
        mySharedPreferences.setAfterEndDate(activity.getAfterEndDate());
        mySharedPreferences.setTicket(activity.getTicket());
        mySharedPreferences.setQuantity(activity.getQuantity());
        mySharedPreferences.setAmount(activity.getAmount());
        mySharedPreferences.setTotal(activity.getTotal());
        mySharedPreferences.setFilledAllDetail(activity.getFilledAllDetails());
        mySharedPreferences.setVoucher(activity.getVoucher());
        mySharedPreferences.setBookTicket(activity.getBookTicket());
        mySharedPreferences.setSelectVoucher(activity.getSelectVoucherType());
        mySharedPreferences.setPerVoucher(activity.getPerVoucher());
        mySharedPreferences.setNumberOfVoucher(activity.getNumberOfVoucher());
        mySharedPreferences.setAmountToBePaid(activity.getAmountToBePaid());
        mySharedPreferences.setProceed(activity.getProceed());
        mySharedPreferences.setPleaseSelectVoucherType(activity.getPleaseSelectVoucherType());
        mySharedPreferences.setPleaseSelectNumVoucherType(activity.getPleaseSelectNumberOfVoucher());
        mySharedPreferences.setVoucherList(activity.getVoucherList());
        mySharedPreferences.setTicketList(activity.getTicketList());
        mySharedPreferences.setPrice(activity.getPrice());
        mySharedPreferences.setApproved(activity.getAccept());
        mySharedPreferences.setDisApproved(activity.getDisapprove());
        mySharedPreferences.setStatus(activity.getStatus());
        mySharedPreferences.setSelectTicket(activity.getSelectTicket());
        mySharedPreferences.setTicketType(activity.getTicketType());
        mySharedPreferences.setLogout(activity.getLogout());
        mySharedPreferences.setPaymentDate(activity.getPaymentDate());
        mySharedPreferences.setPaymentType(activity.getPaymentType());
        mySharedPreferences.setQrCode(activity.getQrCode());
        mySharedPreferences.setRemainingBudget(activity.getRemainingBudget());
        mySharedPreferences.setPaymentStatus(activity.getPaymentStatus());
        mySharedPreferences.setRedeemDateTime(activity.getRedeemDateTime());
        mySharedPreferences.setUserName(activity.getUserName());
        mySharedPreferences.setChangeLanguage(activity.getChangeLanguage());
        mySharedPreferences.setRedeemQrCode(activity.getRedeemQrCode());
        mySharedPreferences.setTransationId(activity.getTransactionId());
        mySharedPreferences.setNotification(activity.getNotification());
        mySharedPreferences.setCreateVendor(activity.getCreateVendor());
        mySharedPreferences.setAddVendor(activity.getAddVendor());
        mySharedPreferences.setType(activity.getType());
        mySharedPreferences.setDiscount(activity.getDiscount());
        mySharedPreferences.setSelect_vendor(activity.getSelect_vendor());
        mySharedPreferences.setRemove(activity.getRemove());
        mySharedPreferences.setAddress(activity.getAddress());
        mySharedPreferences.setVoucherQrCode(activity.getVoucherQrCode());
        mySharedPreferences.setSelectPayment(activity.getSelectPayment());
        mySharedPreferences.setEnterAmount(activity.getEnter_amount());
        mySharedPreferences.setEnterVoucherCode(activity.getEnter_voucher_code());
        mySharedPreferences.setOr(activity.getOr());
        mySharedPreferences.setScanYourCode(activity.getScan_your_code());
        mySharedPreferences.setUpdateCategories(activity.getUpdateCategories());
        mySharedPreferences.setEventStartDate(activity.getEventStartDate());
        mySharedPreferences.setEventEndDate(activity.getEventEndDate());
        mySharedPreferences.setEnterTicketCode(activity.getEnterTicketCode());
        mySharedPreferences.setEventFees(activity.getEventFees());
        mySharedPreferences.setTotalAmount(activity.getToatalAmont());
        mySharedPreferences.setDiscountAmount(activity.getDiscountAmount());
        mySharedPreferences.setRemainingAmount(activity.getRemainingAmount());
        mySharedPreferences.setDiscountPercenTage(activity.getDiscountPercentage());

        txtName.setText(MySharedPreferences.getMySharedPreferences().getChangeLanguage());

        if (MySharedPreferences.getMySharedPreferences().getLanguage().equalsIgnoreCase("en")){
            setLanguage("en");
        }else {
            setLanguage("pt");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        AppUtils.finishFromLeftToRight(getActivity());
    }
}
