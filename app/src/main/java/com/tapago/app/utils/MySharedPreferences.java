package com.tapago.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tapago.app.TaPagoApp;
import com.tapago.app.model.BasketModel;
import com.tapago.app.rest.RestConstant;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MySharedPreferences {

    public static MySharedPreferences mySharedPreferences;
    private String SP_NAME = "Tapago_Prefs";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public Gson gson;

    public static final String PREFS_GUEST_BASKET_LISTING = "PREFS_GUEST_BASKET_LISTING";
    private MySharedPreferences(Context context, Gson gson) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        this.gson = gson;
    }

    public static MySharedPreferences getMySharedPreferences() {
        if (mySharedPreferences == null) {
            mySharedPreferences = new MySharedPreferences(TaPagoApp.getTaPagoApp(), new Gson());
        }
        return mySharedPreferences;
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean("isLogin", false);
    }

    public void setLogin(boolean isLogin) {
        editor.putBoolean("isLogin", isLogin).apply();
    }

    public String getCategoryId() {
        return sharedPreferences.getString(RestConstant.CATEGORY_ID, "");
    }

    public void setCategoryId(String categoryId) {
        editor.putString(RestConstant.CATEGORY_ID, categoryId).apply();
    }

    public String getCity() {
        return sharedPreferences.getString(RestConstant.CITY, "");
    }

    public void setCity(String city) {
        editor.putString(RestConstant.CITY, city).apply();
    }

    public String getUserRoleId() {
        return sharedPreferences.getString(RestConstant.ROLE, "");
    }

    public void setUserRoleId(String userRoleId) {
        editor.putString(RestConstant.ROLE, userRoleId).apply();
    }

    public String getDeviceId() {
        return sharedPreferences.getString(RestConstant.DEVICE_Id, "");
    }

    public void setDeviceId(String deviceid) {
        editor.putString(RestConstant.DEVICE_Id, deviceid).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(RestConstant.LANGUAGE_STORE, "en");
    }

    public void setLanguage(String language) {
        editor.putString(RestConstant.LANGUAGE_STORE, language).apply();
    }

    public String getFirebaseId() {
        return sharedPreferences.getString(RestConstant.FIREBASE_ID, "");
    }

    public void setFirebaseId(String firebaseId) {
        editor.putString(RestConstant.FIREBASE_ID, firebaseId).apply();
    }

    public void setProfile(String profile) {
        editor.putString(RestConstant.PROFILE, profile).apply();
    }

    public String getProfile() {
        return sharedPreferences.getString(RestConstant.PROFILE, "");
    }

    public String getUserId() {
        return sharedPreferences.getString(RestConstant.USER_ID, "");
    }

    public void setUserId(String userId) {
        editor.putString(RestConstant.USER_ID, userId).apply();
    }

    public String getFirstName() {
        return sharedPreferences.getString(RestConstant.FIRST_NAME, "");
    }

    public void setFirstName(String firstName) {
        editor.putString(RestConstant.FIRST_NAME, firstName).apply();
    }

    public String getLastName() {
        return sharedPreferences.getString(RestConstant.LAST_NAME, "");
    }

    public void setLastName(String lastName) {
        editor.putString(RestConstant.LAST_NAME, lastName).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(RestConstant.EMAIL, "");
    }

    public void setEmail(String email) {
        editor.putString(RestConstant.EMAIL, email).apply();
    }

    public String getCountryCode() {
        return sharedPreferences.getString(RestConstant.COUNTRY_CODE, "");
    }

    public void setCountryCode(String countryCode) {
        editor.putString(RestConstant.COUNTRY_CODE, countryCode).apply();
    }

    public String getCountryName() {
        return sharedPreferences.getString(RestConstant.COUNTRY_NAME, "");
    }

    public void setCountryName(String countryName) {
        editor.putString(RestConstant.COUNTRY_NAME, countryName).apply();
    }

    public String getContactNumber() {
        return sharedPreferences.getString(RestConstant.CONTACT_NUMBER, "");
    }

    public void setContactNumber(String number) {
        editor.putString(RestConstant.CONTACT_NUMBER, number).apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(RestConstant.ACCESS_TOKEN, "");
    }

    public void setAccessToken(String deviceToken) {
        editor.putString(RestConstant.ACCESS_TOKEN, deviceToken).apply();
    }

    public String getRecentlyView() {
        return sharedPreferences.getString(RestConstant.RECENTLY_VIEW, "");
    }

    public void setRecentlyView(String recentlyView) {
        editor.putString(RestConstant.RECENTLY_VIEW, recentlyView).apply();
    }

    public String getPaymentHistory() {
        return sharedPreferences.getString(RestConstant.PAYMENT_HISTORY, "");
    }

    public void setPaymentHistory(String paymentHistory) {
        editor.putString(RestConstant.PAYMENT_HISTORY, paymentHistory).apply();
    }

    public String getMyVoucher() {
        return sharedPreferences.getString(RestConstant.MY_VOUCHER, "");
    }

    public void setMyVoucher(String myVoucher) {
        editor.putString(RestConstant.MY_VOUCHER, myVoucher).apply();
    }

    public String getAboutUs() {
        return sharedPreferences.getString(RestConstant.ABOUTUS, "");
    }

    public void setAboutUs(String aboutUS) {
        editor.putString(RestConstant.ABOUTUS, aboutUS).apply();
    }

    public String getPrivacyPolicy() {
        return sharedPreferences.getString(RestConstant.PRIVACY_POLICY, "");
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        editor.putString(RestConstant.PRIVACY_POLICY, privacyPolicy).apply();
    }

    public String getTermsCondition() {
        return sharedPreferences.getString(RestConstant.TERMS_CONDITION, "");
    }

    public void setTermsCondition(String termsCondition) {
        editor.putString(RestConstant.TERMS_CONDITION, termsCondition).apply();
    }

    public String getNeedHelp() {
        return sharedPreferences.getString(RestConstant.NEED_HELP, "");
    }

    public void setNeedHelp(String needHelp) {
        editor.putString(RestConstant.NEED_HELP, needHelp).apply();
    }

    public String getPopularEvent() {
        return sharedPreferences.getString(RestConstant.POPULAR_EVENT, "");
    }

    public void setPopularEvent(String popularEvent) {
        editor.putString(RestConstant.POPULAR_EVENT, popularEvent).apply();
    }

    public String getSlug() {
        return sharedPreferences.getString(RestConstant.SLUG, "");
    }

    public void setSlug(String slug) {
        editor.putString(RestConstant.SLUG, slug).apply();
    }

    public String getVersion() {
        return sharedPreferences.getString(RestConstant.VIRSION, "");
    }

    public void setVersion(String version) {
        editor.putString(RestConstant.VIRSION, version).apply();
    }

    public String getHereHelp() {
        return sharedPreferences.getString(RestConstant.HERE_TO_HELP, "");
    }

    public void setHereHelp(String hereHelp) {
        editor.putString(RestConstant.HERE_TO_HELP, hereHelp).apply();
    }

    public String getAskQuestion() {
        return sharedPreferences.getString(RestConstant.ASK_QUESTION, "");
    }

    public void setAskQuestion(String askQuestion) {
        editor.putString(RestConstant.ASK_QUESTION, askQuestion).apply();
    }

    public String getSearch() {
        return sharedPreferences.getString(RestConstant.SEARCH, "");
    }

    public void setSearch(String search) {
        editor.putString(RestConstant.SEARCH, search).apply();
    }

    public String getNoEventFound() {
        return sharedPreferences.getString(RestConstant.NOEVENTFOUND, "");
    }

    public void setNoEventFound(String noEventFound) {
        editor.putString(RestConstant.NOEVENTFOUND, noEventFound).apply();
    }

    public String getPopularTopic() {
        return sharedPreferences.getString(RestConstant.POPULARTOPIC, "");
    }

    public void setPopularTopic(String popularTopic) {
        editor.putString(RestConstant.POPULARTOPIC, popularTopic).apply();
    }

    public String getEnterQuestion() {
        return sharedPreferences.getString(RestConstant.ENTER_QUESTION, "");
    }

    public void setEnterQuestion(String enterQuestion) {
        editor.putString(RestConstant.ENTER_QUESTION, enterQuestion).apply();
    }


    public String getFirst_Name_Caption() {
        return sharedPreferences.getString(RestConstant.FIRST_NAME_CAPTION, "");
    }

    public void setFirst_Name_Caption(String first_name_caption) {
        editor.putString(RestConstant.FIRST_NAME_CAPTION, first_name_caption).apply();
    }

    public String getLast_Name_Caption() {
        return sharedPreferences.getString(RestConstant.LAST_NAME_CAPTION, "");
    }

    public void setLast_Name_Caption(String last_name_caption) {
        editor.putString(RestConstant.LAST_NAME_CAPTION, last_name_caption).apply();
    }

    public String getEmail_Caption() {
        return sharedPreferences.getString(RestConstant.EMAIL_CAPTION, "");
    }

    public void setEmail_Caption(String email_caption) {
        editor.putString(RestConstant.EMAIL_CAPTION, email_caption).apply();
    }

    public String getPhone_Caption() {
        return sharedPreferences.getString(RestConstant.PHONE_CAPTION, "");
    }

    public void setPhone_Caption(String phone_caption) {
        editor.putString(RestConstant.PHONE_CAPTION, phone_caption).apply();
    }

    public String getUpdateCity() {
        return sharedPreferences.getString(RestConstant.UPDATE_CITY, "");
    }

    public void setUpdateCity(String updateCity) {
        editor.putString(RestConstant.UPDATE_CITY, updateCity).apply();
    }

    public String getChangePassword() {
        return sharedPreferences.getString(RestConstant.CHANGEPASSWORD, "");
    }

    public void setChangePassword(String changePassword) {
        editor.putString(RestConstant.CHANGEPASSWORD, changePassword).apply();
    }

    public String getSubmit() {
        return sharedPreferences.getString(RestConstant.SUBMIT, "");
    }

    public void setSubmit(String submit) {
        editor.putString(RestConstant.SUBMIT, submit).apply();
    }

    public String getPleaseEnterFirstName() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_FIRST_NAME, "");
    }

    public void setPleaseEnterFirstName(String pleaseEnterFirstName) {
        editor.putString(RestConstant.PLEASE_ENTER_FIRST_NAME, pleaseEnterFirstName).apply();
    }

    public String getPleaseEnterLastName() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_LAST_NAME, "");
    }

    public void setPleaseEnterLastName(String pleaseEnterLastName) {
        editor.putString(RestConstant.PLEASE_ENTER_LAST_NAME, pleaseEnterLastName).apply();
    }

    public String getPleaseEnterYourEmail() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_YOUR_EMAIL, "");
    }

    public void setPleaseEnterYourEmail(String pleaseEnterYourEmail) {
        editor.putString(RestConstant.PLEASE_ENTER_YOUR_EMAIL, pleaseEnterYourEmail).apply();
    }

    public String getPleaseEnterValidEmail() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_VALID_EMAIL, "");
    }

    public void setPleaseEnterValidEmail(String pleaseEnterValidEmail) {
        editor.putString(RestConstant.PLEASE_ENTER_VALID_EMAIL, pleaseEnterValidEmail).apply();
    }

    public String getPleaseEnterMobileNumber() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_MOBILE_NUMBER, "");
    }

    public void setPleaseEnterMobileNumber(String pleaseEnterMobileNumber) {
        editor.putString(RestConstant.PLEASE_ENTER_MOBILE_NUMBER, pleaseEnterMobileNumber).apply();
    }

    public String getPleaseEnterValidMobileNumber() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_VALID_MOBILE_NUMBER, "");
    }

    public void setPleaseEnterValidMobileNumber(String pleaseEnterValidMobileNumber) {
        editor.putString(RestConstant.PLEASE_ENTER_VALID_MOBILE_NUMBER, pleaseEnterValidMobileNumber).apply();
    }

    public String getOldPassword() {
        return sharedPreferences.getString(RestConstant.OLD_PASSWORD, "");
    }

    public void setOldPassword(String oldPassword) {
        editor.putString(RestConstant.OLD_PASSWORD, oldPassword).apply();
    }

    public String getNewPassword() {
        return sharedPreferences.getString(RestConstant.NEW_PASSWORD, "");
    }

    public void setNewPassword(String newPassword) {
        editor.putString(RestConstant.NEW_PASSWORD, newPassword).apply();
    }

    public String getConfirmPassword() {
        return sharedPreferences.getString(RestConstant.CONFIRM_PASSWORD, "");
    }

    public void setConfirmPassword(String confirmPassword) {
        editor.putString(RestConstant.CONFIRM_PASSWORD, confirmPassword).apply();
    }

    public String getPleaseEnterOldPassword() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_OLD_PASSWORD, "");
    }

    public void setPleaseEnterOldPassword(String pleaseEnterOldPassword) {
        editor.putString(RestConstant.PLEASE_ENTER_OLD_PASSWORD, pleaseEnterOldPassword).apply();
    }

    public String getPleaseEnterNewPassword() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_NEW_PASSWORD, "");
    }

    public void setPleaseEnterNewPassword(String pleaseEnterNewPassword) {
        editor.putString(RestConstant.PLEASE_ENTER_NEW_PASSWORD, pleaseEnterNewPassword).apply();
    }

    public String getPassword_should_be_minimum_6_characters() {
        return sharedPreferences.getString(RestConstant.PASSWORD_Should_Be_Minimum_6_Characters, "");
    }

    public void setPassword_should_be_minimum_6_characters(String password_should_be_minimum_6_characters) {
        editor.putString(RestConstant.PASSWORD_Should_Be_Minimum_6_Characters, password_should_be_minimum_6_characters).apply();
    }

    public String getPlease_Enter_Confirm_Password() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_CONFIRM_PASSWORD, "");
    }

    public void setPlease_Enter_Confirm_Password(String please_enter_confirm_password) {
        editor.putString(RestConstant.PLEASE_ENTER_CONFIRM_PASSWORD, please_enter_confirm_password).apply();
    }

    public String getPassword_And_Confirm_Password_Does_Not_Match() {
        return sharedPreferences.getString(RestConstant.PASSWORD_And_Confirm_Password_Does_Not_Match, "");
    }

    public void setPassword_And_Confirm_Password_Does_Not_Match(String password_and_confirm_password_does_not_match) {
        editor.putString(RestConstant.PASSWORD_And_Confirm_Password_Does_Not_Match, password_and_confirm_password_does_not_match).apply();
    }


    public String getEvent_Category() {
        return sharedPreferences.getString(RestConstant.EVENT_CATEGORY, "");
    }

    public void setEvent_Category(String event_category) {
        editor.putString(RestConstant.EVENT_CATEGORY, event_category).apply();
    }

    public String getEvent_Name() {
        return sharedPreferences.getString(RestConstant.EVENT_NAME, "");
    }

    public void setEvent_Name(String event_name) {
        editor.putString(RestConstant.EVENT_NAME, event_name).apply();
    }

    public String getEnter_Your_Event_Name() {
        return sharedPreferences.getString(RestConstant.ENTER_YOUR_EVENT_NAME, "");
    }

    public void setEnter_Your_Event_Name(String enter_your_event_name) {
        editor.putString(RestConstant.ENTER_YOUR_EVENT_NAME, enter_your_event_name).apply();
    }

    public String getStart_Event_Time() {
        return sharedPreferences.getString(RestConstant.START_EVENT_TIME, "");
    }

    public void setStart_Event_Time(String start_event_time) {
        editor.putString(RestConstant.START_EVENT_TIME, start_event_time).apply();
    }

    public String getYour_Event_Time() {
        return sharedPreferences.getString(RestConstant.YOUR_EVENT_TIME, "");
    }

    public void setYour_Event_Time(String your_event_time) {
        editor.putString(RestConstant.YOUR_EVENT_TIME, your_event_time).apply();
    }

    public String getStart_Event_Date() {
        return sharedPreferences.getString(RestConstant.START_EVENT_DATE, "");
    }

    public void setStart_Event_Date(String start_event_date) {
        editor.putString(RestConstant.START_EVENT_DATE, start_event_date).apply();
    }

    public String getYour_Event_Date() {
        return sharedPreferences.getString(RestConstant.YOUR_EVENT_DATE, "");
    }

    public void setYour_Event_Date(String your_event_date) {
        editor.putString(RestConstant.YOUR_EVENT_DATE, your_event_date).apply();
    }

    public String getEnd_Event_Time() {
        return sharedPreferences.getString(RestConstant.END_EVENT_TIME, "");
    }

    public void setEnd_Event_Time(String end_event_time) {
        editor.putString(RestConstant.END_EVENT_TIME, end_event_time).apply();
    }

    public String getEnd_Event_Date() {
        return sharedPreferences.getString(RestConstant.END_EVENT_DATE, "");
    }

    public void setEnd_Event_Date(String end_event_date) {
        editor.putString(RestConstant.END_EVENT_DATE, end_event_date).apply();
    }

    public String getEvent_Description() {
        return sharedPreferences.getString(RestConstant.EVENT_DESCRIPTION, "");
    }

    public void setEvent_Description(String event_description) {
        editor.putString(RestConstant.EVENT_DESCRIPTION, event_description).apply();
    }

    public String getWrite_Something_About_Your_Event() {
        return sharedPreferences.getString(RestConstant.WRITE_SOMETHING_ABOUT_YOUR_EVENT, "");
    }

    public void setWrite_Something_About_Your_Event(String write_something_about_your_event) {
        editor.putString(RestConstant.WRITE_SOMETHING_ABOUT_YOUR_EVENT, write_something_about_your_event).apply();
    }

    public String getEvent_Address() {
        return sharedPreferences.getString(RestConstant.EVENT_ADDRESS, "");
    }

    public void setEvent_Address(String event_address) {
        editor.putString(RestConstant.EVENT_ADDRESS, event_address).apply();
    }

    public String getEnter_Your_Event_Address() {
        return sharedPreferences.getString(RestConstant.ENTER_YOUR_EVENT_ADDRESS, "");
    }

    public void setEnter_Your_Event_Address(String enter_your_event_address) {
        editor.putString(RestConstant.ENTER_YOUR_EVENT_ADDRESS, enter_your_event_address).apply();
    }

    public String getEvent_Vanue() {
        return sharedPreferences.getString(RestConstant.EVENT_VANUE, "");
    }

    public void setEvent_Vanue(String event_vanue) {
        editor.putString(RestConstant.EVENT_VANUE, event_vanue).apply();
    }

    public String getEnter_Your_Event_Vanue() {
        return sharedPreferences.getString(RestConstant.ENTER_YOUR_EVENT_VENUE, "");
    }

    public void setEnter_Your_Event_Vanue(String enter_your_event_vanue) {
        editor.putString(RestConstant.ENTER_YOUR_EVENT_VENUE, enter_your_event_vanue).apply();
    }

    public String getCityCaption() {
        return sharedPreferences.getString(RestConstant.EVENT_CITY, "");
    }

    public void setCityCaption(String cityCaption) {
        editor.putString(RestConstant.EVENT_CITY, cityCaption).apply();
    }

    public String getEnterCity() {
        return sharedPreferences.getString(RestConstant.ENTER_YOUR_CITY, "");
    }

    public void setEnterCity(String enterCity) {
        editor.putString(RestConstant.ENTER_YOUR_CITY, enterCity).apply();
    }

    public String getAdd_Event_Banner() {
        return sharedPreferences.getString(RestConstant.ADD_EVENT_BANNER, "");
    }

    public void setAdd_Event_Banner(String add_event_banner) {
        editor.putString(RestConstant.ADD_EVENT_BANNER, add_event_banner).apply();
    }

    public String getAdd_Event_Banner_Thumb() {
        return sharedPreferences.getString(RestConstant.ADD_EVENT_BANNER_THUMB, "");
    }

    public void setAdd_Event_Banner_Thumb(String add_event_banner) {
        editor.putString(RestConstant.ADD_EVENT_BANNER_THUMB, add_event_banner).apply();
    }

    public String getBudget() {
        return sharedPreferences.getString(RestConstant.BUDGET, "");
    }

    public void setBudget(String budget) {
        editor.putString(RestConstant.BUDGET, budget).apply();
    }

    public String getCreate_Event() {
        return sharedPreferences.getString(RestConstant.CREATE_EVENT, "");
    }

    public void setCreate_Event(String create_event) {
        editor.putString(RestConstant.CREATE_EVENT, create_event).apply();
    }

    public String getPlease_Enter_Event_Name() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_EVENT_NAME, "");
    }

    public void setPlease_Enter_Event_Name(String please_enter_event_name) {
        editor.putString(RestConstant.PLEASE_ENTER_EVENT_NAME, please_enter_event_name).apply();
    }

    public String getPlease_Select_Start_Date() {
        return sharedPreferences.getString(RestConstant.PLEASE_SELECT_START_DATE, "");
    }

    public void setPlease_Select_Start_Date(String please_select_start_date) {
        editor.putString(RestConstant.PLEASE_SELECT_START_DATE, please_select_start_date).apply();
    }

    public String getPlease_Select_End_Date() {
        return sharedPreferences.getString(RestConstant.PLEASE_SELECT_END_DATE, "");
    }

    public void setPlease_Select_End_Date(String please_select_end_date) {
        editor.putString(RestConstant.PLEASE_SELECT_END_DATE, please_select_end_date).apply();
    }

    public String getPlease_Select_Start_Time() {
        return sharedPreferences.getString(RestConstant.PLEASE_SELECT_START_TIME, "");
    }

    public void setPlease_Select_Start_Time(String please_select_start_time) {
        editor.putString(RestConstant.PLEASE_SELECT_START_TIME, please_select_start_time).apply();
    }

    public String getPlease_Select_End_Time() {
        return sharedPreferences.getString(RestConstant.PLEASE_SELECT_END_TIME, "");
    }

    public void setPlease_Select_End_Time(String please_select_end_time) {
        editor.putString(RestConstant.PLEASE_SELECT_END_TIME, please_select_end_time).apply();
    }

    public String getPlease_Enter_Event_Description() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_EVENT_DESCRIPTION, "");
    }

    public void setPlease_Enter_Event_Description(String please_enter_event_description) {
        editor.putString(RestConstant.PLEASE_ENTER_EVENT_DESCRIPTION, please_enter_event_description).apply();
    }

    public String getPlease_Enter_Event_Address() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_EVENT_ADDRESS, "");
    }

    public void setPlease_Enter_Event_Address(String please_enter_event_address) {
        editor.putString(RestConstant.PLEASE_ENTER_EVENT_ADDRESS, please_enter_event_address).apply();
    }

    public String getPlease_Enter_Event_Venue() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_EVENT_VENUE, "");
    }

    public void setPlease_Enter_Event_Venue(String please_enter_event_venue) {
        editor.putString(RestConstant.PLEASE_ENTER_EVENT_VENUE, please_enter_event_venue).apply();
    }

    public String getPlease_Enter_Event_Budget() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_EVENT_BUDGET, "");
    }

    public void setPlease_Enter_Event_Budget(String please_enter_event_budget) {
        editor.putString(RestConstant.PLEASE_ENTER_EVENT_BUDGET, please_enter_event_budget).apply();
    }

    public String getPlease_Select_Banner_Image() {
        return sharedPreferences.getString(RestConstant.PLEASE_SELECT_BANNER_IMAGE, "");
    }

    public void setPlease_Select_Banner_Image(String please_select_banner_image) {
        editor.putString(RestConstant.PLEASE_SELECT_BANNER_IMAGE, please_select_banner_image).apply();
    }

    public String getPlease_Select_Thumb_Image() {
        return sharedPreferences.getString(RestConstant.PLEASE_SELECT_THUMB_IMAGE, "");
    }

    public void setPlease_Select_Thumb_Image(String please_select_thumb_image) {
        editor.putString(RestConstant.PLEASE_SELECT_THUMB_IMAGE, please_select_thumb_image).apply();
    }

    public String getPlease_Select_Category() {
        return sharedPreferences.getString(RestConstant.PLEASE_SELECT_CATEGORY, "");
    }

    public void setPlease_Select_Category(String please_select_category) {
        editor.putString(RestConstant.PLEASE_SELECT_CATEGORY, please_select_category).apply();
    }

    public String getPlease_Enter_Lowest_Amount() {
        return sharedPreferences.getString(RestConstant.PLEASE_ENTER_LOWEST_AMOUNT, "");
    }

    public void setPlease_Enter_Lowest_Amount(String please_enter_lowest_amount) {
        editor.putString(RestConstant.PLEASE_ENTER_LOWEST_AMOUNT, please_enter_lowest_amount).apply();
    }


    public String getAbout() {
        return sharedPreferences.getString(RestConstant.ABOUT, "");
    }

    public void setAbout(String about) {
        editor.putString(RestConstant.ABOUT, about).apply();
    }

    public String getVenue() {
        return sharedPreferences.getString(RestConstant.VENUE, "");
    }

    public void setVenue(String venue) {
        editor.putString(RestConstant.VENUE, venue).apply();
    }

    public String getDirection() {
        return sharedPreferences.getString(RestConstant.GET_DIRECTION, "");
    }

    public void setDirection(String direction) {
        editor.putString(RestConstant.GET_DIRECTION, direction).apply();
    }

    public String getOrganiser() {
        return sharedPreferences.getString(RestConstant.ORGANISER, "");
    }

    public void setOrganiser(String organiser) {
        editor.putString(RestConstant.ORGANISER, organiser).apply();
    }

    public String getMoreLike() {
        return sharedPreferences.getString(RestConstant.MORE_LIKE, "");
    }

    public void setMoreLike(String moreLike) {
        editor.putString(RestConstant.MORE_LIKE, moreLike).apply();
    }

    public String getRequestVoucher() {
        return sharedPreferences.getString(RestConstant.REQUESTVOUCHER, "");
    }

    public void setRequestVoucher(String requestVoucher) {
        editor.putString(RestConstant.REQUESTVOUCHER, requestVoucher).apply();
    }

    public String getRedeemVoucher() {
        return sharedPreferences.getString(RestConstant.REDEEMVOUCHER, "");
    }

    public void setRedeemVoucher(String redeemVoucher) {
        editor.putString(RestConstant.REDEEMVOUCHER, redeemVoucher).apply();
    }

    public String getEnterCityName() {
        return sharedPreferences.getString(RestConstant.ENTERCITYNAME, "");
    }

    public void setEnterCityName(String enterCityName) {
        editor.putString(RestConstant.ENTERCITYNAME, enterCityName).apply();
    }

    public String getAddMultiple() {
        return sharedPreferences.getString(RestConstant.MULTIPLE_CITY, "");
    }

    public void setAddMultiple(String addMultiple) {
        editor.putString(RestConstant.MULTIPLE_CITY, addMultiple).apply();
    }

    public String getUpdateEvent() {
        return sharedPreferences.getString(RestConstant.UPDATEC, "");
    }

    public void setUpdateEvent(String updateEvent) {
        editor.putString(RestConstant.UPDATEC, updateEvent).apply();
    }

    public void setVoucherType(String voucherType) {
        editor.putString(RestConstant.VOUCHER_TYPE, voucherType).apply();
    }

    public String getVoucherType() {
        return sharedPreferences.getString(RestConstant.VOUCHER_TYPE, "");
    }

    public String getAfterEndDate() {
        return sharedPreferences.getString(RestConstant.AFTEREVENT, "");
    }

    public void setAfterEndDate(String afterEndDate) {
        editor.putString(RestConstant.AFTEREVENT, afterEndDate).apply();
    }

    public String getTicket() {
        return sharedPreferences.getString(RestConstant.TICKET, "");
    }

    public void setTicket(String ticket) {
        editor.putString(RestConstant.TICKET, ticket).apply();
    }

    public String getQuantity() {
        return sharedPreferences.getString(RestConstant.QUANTITY, "");
    }

    public void setQuantity(String quantity) {
        editor.putString(RestConstant.QUANTITY, quantity).apply();
    }

    public String getAmount() {
        return sharedPreferences.getString(RestConstant.AMOUNT, "");
    }

    public void setAmount(String amount) {
        editor.putString(RestConstant.AMOUNT, amount).apply();
    }

    public String getTotal() {
        return sharedPreferences.getString(RestConstant.TOTAL, "");
    }

    public void setTotal(String total) {
        editor.putString(RestConstant.TOTAL, total).apply();
    }

    public String getFilledAllDetail() {
        return sharedPreferences.getString(RestConstant.FILLEDDETAILS, "");
    }

    public void setFilledAllDetail(String filledAllDetail) {
        editor.putString(RestConstant.FILLEDDETAILS, filledAllDetail).apply();
    }

    public String getVoucher() {
        return sharedPreferences.getString(RestConstant.VOUCHER, "");
    }

    public void setVoucher(String voucher) {
        editor.putString(RestConstant.VOUCHER, voucher).apply();
    }

    public String getBookTicket() {
        return sharedPreferences.getString(RestConstant.BOOKTICKET, "");
    }

    public void setBookTicket(String bookTicket) {
        editor.putString(RestConstant.BOOKTICKET, bookTicket).apply();
    }

    public String getSelectVoucher() {
        return sharedPreferences.getString(RestConstant.SELECTVOUCHERTYPE, "");
    }

    public void setSelectVoucher(String selectVoucher) {
        editor.putString(RestConstant.SELECTVOUCHERTYPE, selectVoucher).apply();
    }

    public String getPerVoucher() {
        return sharedPreferences.getString(RestConstant.PERVOUCHER, "");
    }

    public void setPerVoucher(String perVoucher) {
        editor.putString(RestConstant.PERVOUCHER, perVoucher).apply();
    }

    public String getNumberOfVoucher() {
        return sharedPreferences.getString(RestConstant.NUMBEROFVOUCHER, "");
    }

    public void setNumberOfVoucher(String numberOfVoucher) {
        editor.putString(RestConstant.NUMBEROFVOUCHER, numberOfVoucher).apply();
    }

    public String getAmountToBePaid() {
        return sharedPreferences.getString(RestConstant.AMOUNTTOBE, "");
    }

    public void setAmountToBePaid(String amountToBePaid) {
        editor.putString(RestConstant.AMOUNTTOBE, amountToBePaid).apply();
    }

    public String getProceed() {
        return sharedPreferences.getString(RestConstant.PROCEED, "");
    }

    public void setProceed(String proceed) {
        editor.putString(RestConstant.PROCEED, proceed).apply();
    }

    public String getPleaseSelectVoucherType() {
        return sharedPreferences.getString(RestConstant.PLEASESELECTVOUCHERTYPE, "");
    }

    public void setPleaseSelectVoucherType(String pleaseSelectVoucherType) {
        editor.putString(RestConstant.PLEASESELECTVOUCHERTYPE, pleaseSelectVoucherType).apply();
    }


    public String getPleaseSelectNumVoucherType() {
        return sharedPreferences.getString(RestConstant.PLEASESELECTNUMBERVOUCHER, "");
    }

    public void setPleaseSelectNumVoucherType(String pleaseSelectNumVoucherType) {
        editor.putString(RestConstant.PLEASESELECTNUMBERVOUCHER, pleaseSelectNumVoucherType).apply();
    }

    public String getVoucherList() {
        return sharedPreferences.getString(RestConstant.VOUCHERLIST, "");
    }

    public void setVoucherList(String voucherList) {
        editor.putString(RestConstant.VOUCHERLIST, voucherList).apply();
    }

    public String getTicketList() {
        return sharedPreferences.getString(RestConstant.TICKETLIST, "");
    }

    public void setTicketList(String ticketList) {
        editor.putString(RestConstant.TICKETLIST, ticketList).apply();
    }

    public String getPrice() {
        return sharedPreferences.getString(RestConstant.PRICE, "");
    }

    public void setPrice(String price) {
        editor.putString(RestConstant.PRICE, price).apply();
    }

    public String getApproved() {
        return sharedPreferences.getString(RestConstant.APPROVED, "");
    }

    public void setApproved(String approved) {
        editor.putString(RestConstant.APPROVED, approved).apply();
    }

    public String getDisApproved() {
        return sharedPreferences.getString(RestConstant.DISAPPROVED, "");
    }

    public void setDisApproved(String disApproved) {
        editor.putString(RestConstant.DISAPPROVED, disApproved).apply();
    }

    public String getStatus() {
        return sharedPreferences.getString(RestConstant.STATUS, "");
    }

    public void setStatus(String status) {
        editor.putString(RestConstant.STATUS, status).apply();
    }

    public String getSelectTicket() {
        return sharedPreferences.getString(RestConstant.SELECTTICKET, "");
    }

    public void setSelectTicket(String selectTicket) {
        editor.putString(RestConstant.SELECTTICKET, selectTicket).apply();
    }

    public String getTicketType() {
        return sharedPreferences.getString(RestConstant.TICKETTYPEC, "");
    }

    public void setTicketType(String ticketType) {
        editor.putString(RestConstant.TICKETTYPEC, ticketType).apply();
    }

    public String getLogout() {
        return sharedPreferences.getString(RestConstant.LOGOUT, "");
    }

    public void setLogout(String logout) {
        editor.putString(RestConstant.LOGOUT, logout).apply();
    }

    public String getPaymentDate() {
        return sharedPreferences.getString(RestConstant.PAYMENT_DATE, "");
    }

    public void setPaymentDate(String paymentDate) {
        editor.putString(RestConstant.PAYMENT_DATE, paymentDate).apply();
    }

    public String getPaymentType() {
        return sharedPreferences.getString(RestConstant.PAYMENT_TYPE, "");
    }

    public void setPaymentType(String paymentType) {
        editor.putString(RestConstant.PAYMENT_TYPE, paymentType).apply();
    }

    public String getQrCode() {
        return sharedPreferences.getString(RestConstant.QRCODE, "");
    }

    public void setQrCode(String qrCode) {
        editor.putString(RestConstant.QRCODE, qrCode).apply();
    }

    public String getSelectCategories() {
        return sharedPreferences.getString(RestConstant.SELECTCATEGORIES, "");
    }

    public void setSelectCategories(String selectCategories) {
        editor.putString(RestConstant.SELECTCATEGORIES, selectCategories).apply();
    }

    public String getNext() {
        return sharedPreferences.getString(RestConstant.NEXT, "");
    }

    public void setNext(String next) {
        editor.putString(RestConstant.NEXT, next).apply();
    }

    public String getStrSelectCategories() {
        return sharedPreferences.getString(RestConstant.STRCATEGORIES, "");
    }

    public void setStrSelectCategories(String strSelectCategories) {
        editor.putString(RestConstant.STRCATEGORIES, strSelectCategories).apply();
    }

    public String getRemainingBudget() {
        return sharedPreferences.getString(RestConstant.REMAININGBUDGET, "");
    }

    public void setRemainingBudget(String remainingBudget) {
        editor.putString(RestConstant.REMAININGBUDGET, remainingBudget).apply();
    }

    public String getPaymentStatus() {
        return sharedPreferences.getString(RestConstant.PAYMENTSTATUS, "");
    }

    public void setPaymentStatus(String paymentStatus) {
        editor.putString(RestConstant.PAYMENTSTATUS, paymentStatus).apply();
    }

    public String getRedeemDateTime() {
        return sharedPreferences.getString(RestConstant.REDEEMDATETIME, "");
    }

    public void setRedeemDateTime(String redeemDateTime) {
        editor.putString(RestConstant.REDEEMDATETIME, redeemDateTime).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(RestConstant.USERNAME, "");
    }

    public void setUserName(String userName) {
        editor.putString(RestConstant.USERNAME, userName).apply();
    }

    public String getChangeLanguage() {
        return sharedPreferences.getString(RestConstant.CHANGELANGUAGE, "");
    }

    public void setChangeLanguage(String changeLanguage) {
        editor.putString(RestConstant.CHANGELANGUAGE, changeLanguage).apply();
    }

    public String getRedeemQrCode() {
        return sharedPreferences.getString(RestConstant.REDEEMQRCODE, "");
    }

    public void setRedeemQrCode(String redeemQrCode) {
        editor.putString(RestConstant.REDEEMQRCODE, redeemQrCode).apply();
    }

    public String getTransationId() {
        return sharedPreferences.getString(RestConstant.TRANSATIONID, "");
    }

    public void setTransationId(String transationId) {
        editor.putString(RestConstant.TRANSATIONID, transationId).apply();
    }

    public String getNotification() {
        return sharedPreferences.getString(RestConstant.NOTIFICATIONS, "");
    }

    public void setNotification(String notification) {
        editor.putString(RestConstant.NOTIFICATIONS, notification).apply();
    }

    public String getHelp() {
        return sharedPreferences.getString(RestConstant.HELP, "");
    }

    public void setHelp(String help) {
        editor.putString(RestConstant.HELP, help).apply();
    }

    public String getCreateVendor() {
        return sharedPreferences.getString(RestConstant.CREATEVENDOR, "");
    }

    public void setCreateVendor(String createVendor) {
        editor.putString(RestConstant.CREATEVENDOR, createVendor).apply();
    }

    public String getAddVendor() {
        return sharedPreferences.getString(RestConstant.ADDVENDOR, "");
    }

    public void setAddVendor(String addVendor) {
        editor.putString(RestConstant.ADDVENDOR, addVendor).apply();
    }

    public String getType() {
        return sharedPreferences.getString(RestConstant.TYPE, "");
    }

    public void setType(String type) {
        editor.putString(RestConstant.TYPE, type).apply();
    }

    public String getDiscount() {
        return sharedPreferences.getString(RestConstant.DISCOUNT, "");
    }

    public void setDiscount(String discount) {
        editor.putString(RestConstant.DISCOUNT, discount).apply();
    }

    public String getSelect_vendor() {
        return sharedPreferences.getString(RestConstant.SELECT_VENDER, "");
    }

    public void setSelect_vendor(String select_vendor) {
        editor.putString(RestConstant.SELECT_VENDER, select_vendor).apply();
    }

    public String getRemove() {
        return sharedPreferences.getString(RestConstant.REMOVE, "");
    }

    public void setRemove(String remove) {
        editor.putString(RestConstant.REMOVE, remove).apply();
    }

    public String getEventId() {
        return sharedPreferences.getString(RestConstant.EVENTID, "");
    }

    public void setEventId(String remove) {
        editor.putString(RestConstant.EVENTID, remove).apply();
    }


    public String getAddress() {
        return sharedPreferences.getString(RestConstant.ADDRESS, "");
    }

    public void setAddress(String address) {
        editor.putString(RestConstant.ADDRESS, address).apply();
    }

    public String getVoucherQrCode() {
        return sharedPreferences.getString(RestConstant.VOUCHERQRCODE, "");
    }

    public void setVoucherQrCode(String voucherQrCode) {
        editor.putString(RestConstant.VOUCHERQRCODE, voucherQrCode).apply();
    }

    public String getSelectPayment() {
        return sharedPreferences.getString(RestConstant.SELECTPAYMENT, "");
    }

    public void setSelectPayment(String selectPayment) {
        editor.putString(RestConstant.SELECTPAYMENT, selectPayment).apply();
    }

    public String getEnterAmount() {
        return sharedPreferences.getString(RestConstant.ENTER_AMOUNT, "");
    }

    public void setEnterAmount(String enterAmount) {
        editor.putString(RestConstant.ENTER_AMOUNT, enterAmount).apply();
    }

    public String getEnterVoucherCode() {
        return sharedPreferences.getString(RestConstant.ENTER_VOUCHER_CODE, "");
    }

    public void setEnterVoucherCode(String enterVoucherCode) {
        editor.putString(RestConstant.ENTER_VOUCHER_CODE, enterVoucherCode).apply();
    }

    public String getOr() {
        return sharedPreferences.getString(RestConstant.OR, "");
    }

    public void setOr(String or) {
        editor.putString(RestConstant.OR, or).apply();
    }

    public String getScanYourCode() {
        return sharedPreferences.getString(RestConstant.SCAN_YOUR_CODE, "");
    }

    public void setScanYourCode(String scanYourCode) {
        editor.putString(RestConstant.SCAN_YOUR_CODE, scanYourCode).apply();
    }

    public String getUpdateCategories() {
        return sharedPreferences.getString(RestConstant.UPDATECATEGORIES, "");
    }

    public void setUpdateCategories(String updateCategories) {
        editor.putString(RestConstant.UPDATECATEGORIES, updateCategories).apply();
    }

    public String getEventStartDate() {
        return sharedPreferences.getString(RestConstant.EVENTSTARTDATE, "");
    }

    public void setEventStartDate(String eventStartDate) {
        editor.putString(RestConstant.EVENTSTARTDATE, eventStartDate).apply();
    }

    public String getEventEndDate() {
        return sharedPreferences.getString(RestConstant.EVENTENDDATE, "");
    }

    public void setEventEndDate(String eventEndDate) {
        editor.putString(RestConstant.EVENTENDDATE, eventEndDate).apply();
    }

    public String getEnterTicketCode() {
        return sharedPreferences.getString(RestConstant.ENTERTICKETCODE, "");
    }

    public void setEnterTicketCode(String enterTicketCode) {
        editor.putString(RestConstant.ENTERTICKETCODE, enterTicketCode).apply();
    }

    public String getEventFees() {
        return sharedPreferences.getString(RestConstant.EVENTFEES, "");
    }

    public void setEventFees(String eventFees) {
        editor.putString(RestConstant.EVENTFEES, eventFees).apply();
    }

    public String getTotalAmount() {
        return sharedPreferences.getString(RestConstant.EVENTTOTAL_AMOUNT, "");
    }

    public void setTotalAmount(String eventTotalAmount) {
        editor.putString(RestConstant.EVENTTOTAL_AMOUNT, eventTotalAmount).apply();
    }

    public String getDiscountAmount() {
        return sharedPreferences.getString(RestConstant.DISCOUNTAMOUT, "");
    }

    public void setDiscountAmount(String discountAmount) {
        editor.putString(RestConstant.DISCOUNTAMOUT, discountAmount).apply();
    }

    public String getRemainingAmount() {
        return sharedPreferences.getString(RestConstant.REMAININGAMOUNT, "");
    }

    public void setRemainingAmount(String remainingAmount) {
        editor.putString(RestConstant.REMAININGAMOUNT, remainingAmount).apply();
    }

    public String getDiscountPercenTage() {
        return sharedPreferences.getString(RestConstant.DISCOUNTPERCENTAGE, "");
    }

    public void setDiscountPercenTage(String discountPercenTage) {
        editor.putString(RestConstant.DISCOUNTPERCENTAGE, discountPercenTage).apply();
    }

    public String getCurrentTime(){
        return sharedPreferences.getString(RestConstant.CURRENTTIME, "");
    }

    public void setCurrentTime(String currentTime) {
        editor.putString(RestConstant.CURRENTTIME, currentTime).apply();
    }

    public void clearPreferences() {
        editor.clear().apply();
    }

    public <T> void putObject(String key, T value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, gson.toJson(value));
        editor.apply();
    }

    public <T> T getObject(String key, Class<T> clazz) {
        return gson.fromJson(getString(key, null), clazz);
    }

    public <T> void putList(String key, List<T> list) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, gson.toJson(list));
        editor.apply();
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(getString(key, null), typeOfT);
    }

    public <T> void putArray(String key, T[] arrays) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, gson.toJson(arrays));
        editor.apply();
    }

    public <T> T[] getArray(String key, Class<T[]> clazz) {
        return gson.fromJson(getString(key, null), clazz);
    }

    public void removeKey(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor != null) {
            editor.remove(key);
            editor.apply();
        }
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public static ArrayList<BasketModel> getGuestBasketListing() {
        Type type = new TypeToken<ArrayList<BasketModel>>() {
        }.getType();
        return new Gson().fromJson(MySharedPreferences.readStringPrefsVal(PREFS_GUEST_BASKET_LISTING), type);
    }

    public void setGuestBasketListing(ArrayList<BasketModel> basketListing) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_GUEST_BASKET_LISTING, new Gson().toJson(basketListing));
        editor.apply();
    }

    private static String readStringPrefsVal(String key) {
        return getMySharedPreferences().getString(key, null);
    }


}
