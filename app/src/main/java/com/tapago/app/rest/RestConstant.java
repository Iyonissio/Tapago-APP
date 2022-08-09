package com.tapago.app.rest;

public class RestConstant {

    //local
   //static final String BASE_URL = "https://voucherapp.xceltec.com/public/";

    // live
//    static final String BASE_URL = "http://162.0.237.160:4001";
      //my local network speranza
//    static final String BASE_URL = "http://192.168.18.71:8000";
      //my wifi home
//    static final String BASE_URL = "http://192.168.43.92:8000";

//      static final String BASE_URL = "http://127.0.0.1:8000";
    static final String BASE_URL = "https://api.tapago.speranza.co.mz";

    public static final String LOGIN_API = "api/login";
    public static final String SEND_OTP_API = "api/sendotp";
    public static final String VERIFY_OTP_API = "api/verify-otp";
    public static final String SIGNUP_API = "api/register";
    public static final String CATEGORYLSIT_API = "api/eventcategory";
    public static final String FORGOTPASSWORD_API = "api/forgotpassword";
    public static final String CMS_API = "api/cms";
    public static final String EVENT_LIST = "api/event-list";
    public static final String HELP_LIST = "api/help";
    public static final String CHANGEPASSWORD_API = "api/change-password";
    public static final String UPDATE_PROFILE_API = "api/update-profile";
    public static final String LOGOUT_API = "api/logout";
    public static final String CLOSEACCOUNT_API = "api/deactivate-user";
    public static final String CREATE_EVENTS = "api/event-create";
    public static final String VOUCHER_CATEGORY = "api/voucher-category";
    public static final String MY_EVENTS = "api/my-event";
    public static final String RECENT_VIEW = "api/recent-view";
    public static final String VIEW_EVENT = "api/view-event-create";
    public static final String CITYADD = "api/city-add";
    public static final String GETCITY = "api/city-list";
    public static final String REMOVECITY = "api/city-remove";
    public static final String MOREEVENT = "api/event-detail";
    public static final String VOUCHEREVENT = "api/voucher-list";
    public static final String RVOUCHER = "api/request-voucher";
    public static final String EVENTDETAILS = "api/eventwise-details";
    public static final String UPDATEEVENT = "api/event-update";
    public static final String TICKETTYPE = "api/ticket-category";
    public static final String VOUCHER_LIST_USER = "api/request-voucher-list";
    public static final String VOUCHER_LIST_MURCHANT = "api/merchant-voucher-list";
    public static final String APPROVE_DISAPPROVE = "api/approve-request-voucher";
    public static final String TICKET_LIST = "api/ticket-list";
    public static final String REQUEST_TICKET = "api/request-ticket";
    public static final String REQUEST_TICKET_LIST = "api/request-ticket-list";
    public static final String TICKET_MERCHENT_LIST = "api/merchant-ticket-list";
    public static final String APPROVE_DISAPPROVE_TICKET = "api/approve-request-ticket";
    public static final String BRINTREETOKEN = "api/create-token";
    public static final String CREATE_EVENT_PAYMENT = "api/event-payment";
    public static final String VOUCHERTICKET_PAYMENT = "api/voucher-ticket-payment";
    public static final String PAYMENTHISTORY = "api/payment-history";
    public static final String REEDEEMQRCODE = "api/redeem-qrcode";
    public static final String REEDEEMQRCODEHISTORY = "api/redeem-history-voucher";
    public static final String PAYMENTHISTORYMEARCHENT = "api/merchant-payment-history";
    public static final String NOTIFICATION = "api/notification";
    public static final String VENDORADD = "api/vendor/add";
    public static final String GETVENDOR = "api/merchant-vendor/list";
    public static final String VENDORAPPROVED = "api/vendor/approve-disapprove";
    public static final String EVENT_DELETE = "api/my-event-delete";
    public static final String USERVOUCHERLIST = "api/user-voucher-request-list";
    public static final String USERTICKETLIST = "api/user-ticket-request-list";
    public static final String FORGOTPASSWORD_OTP = "api/forgot-password-otp";
    public static final String FORGOTPASSWORD_OTP_VERIFY = "api/forgotpassword-verify-otp";
    public static final String FORGOTPASSWORD_CHANGE = "api/otp-change-password";
    public static final String ADDCATEGORY = "api/category-search";
    public static final String CategoryListMain = "api/category";
    public static final String ProductList = "api/product";
    public static final String PAYMENTFORSHIPPING = "api/payment";
    public static final String ORDERHISTORYLIST = "api/order-history";
    public static final String SHIPPINGPAYMENTHSITORY = "api/product-payment-history";
    public static final String MostPopularAPi = "api/product-mostpopular";
    public static final String AddressListAPI = "api/list-shipping-address";
    public static final String deleteAddressAPI = "api/delete-shipping-address";
    public static final String insertAddressAPI = "api/insert-shipping-address";


    //caption api name
    public static final String CAPTIONAPI = "api/getactivity";


    public static final String CATEGORY_ID = "category_id";
    public static final String CITY = "city";
    public static final String DEVICE_Id = "device_id";
    public static final String LANGUAGE_STORE = "language_store";
    public static final String USER_ID = "id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "email";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String FIREBASE_ID = "firebase_id";
    public static final String PROFILE = "profile";
    public static final String CONTACT_NUMBER = "contact_number";
    public static final String COUNTRY_CODE = "country_number";
    public static final String COUNTRY_NAME = "country_name";
    public static String PROFILE_UPDATE = "profile_update";
    public static String CLICK_PROFILE = "click_profile";
    public static String CLICK_MY_EVENT = "click_my_event";
    public static String FORGOT_PASSWORD = "forgot_password";
    public static final String EVENTID = "event_id";

    public static final String ROLE = "role";

    //caption shared preference
    public static final String RECENTLY_VIEW = "recently_viewed";
    public static final String PAYMENT_HISTORY = "payment_history";
    public static final String MY_VOUCHER = "my_voucher";
    public static final String ABOUTUS = "about_us";
    public static final String PRIVACY_POLICY = "privacy_policy";
    public static final String TERMS_CONDITION = "terms_conditions";
    public static final String NEED_HELP = "need_help";
    public static final String POPULAR_EVENT = "popular_events";
    public static final String NOEVENTFOUND = "no_event_found";
    public static final String POPULARTOPIC = "popular_topic";
    public static final String ENTER_QUESTION = "please_enter_question";
    public static final String SLUG = "slug";
    public static final String VIRSION = "virsion";
    public static final String HERE_TO_HELP = "We_are_here_to_help";
    public static final String ASK_QUESTION = "ask_a_question";
    public static final String SEARCH = "search";
    public static final String FIRST_NAME_CAPTION = "first_name_caption";
    public static final String LAST_NAME_CAPTION = "last_name_caption";
    public static final String EMAIL_CAPTION = "email_caption";
    public static final String PHONE_CAPTION = "Phone_caption";
    public static final String UPDATE_CITY = "update_city";
    public static final String CHANGEPASSWORD = "change_password";
    public static final String SUBMIT = "submit";
    public static final String PLEASE_ENTER_FIRST_NAME = "please_enter_first_name";
    public static final String PLEASE_ENTER_LAST_NAME = "please_enter_last_name";
    public static final String PLEASE_ENTER_YOUR_EMAIL = "please_enter_your_email";
    public static final String PLEASE_ENTER_VALID_EMAIL = "please_enter_valid_email";
    public static final String PLEASE_ENTER_MOBILE_NUMBER = "please_enter_mobile_number";
    public static final String PLEASE_ENTER_VALID_MOBILE_NUMBER = "please_enter_valid_mobile_number";
    public static final String OLD_PASSWORD = "old_password";
    public static final String NEW_PASSWORD = "new_password";
    public static final String CONFIRM_PASSWORD = "confirm_password";
    public static final String PLEASE_ENTER_OLD_PASSWORD = "please_enter_old_password";
    public static final String PLEASE_ENTER_NEW_PASSWORD = "please_enter_new_password";
    public static final String PASSWORD_Should_Be_Minimum_6_Characters = "Password_should_be_minimum_6_characters";
    public static final String PLEASE_ENTER_CONFIRM_PASSWORD = "please_enter_confirm_password";
    public static final String PASSWORD_And_Confirm_Password_Does_Not_Match = "password_and_confirm_password_does_not_match";

    //caption create event
    public static final String EVENT_CATEGORY = "event_category";
    public static final String EVENT_NAME = "event_name";
    public static final String ENTER_YOUR_EVENT_NAME = "enter_your_event_name";
    public static final String START_EVENT_TIME = "start_event_time";
    public static final String YOUR_EVENT_TIME = "your_event_time";
    public static final String START_EVENT_DATE = "start_event_date";
    public static final String YOUR_EVENT_DATE = "your_event_date";
    public static final String END_EVENT_TIME = "end_event_time";
    public static final String END_EVENT_DATE = "end_event_date";
    public static final String EVENT_DESCRIPTION = "event_description";
    public static final String WRITE_SOMETHING_ABOUT_YOUR_EVENT = "write_something_about_your_event";
    public static final String EVENT_ADDRESS = "event_address";
    public static final String EVENT_CITY = "event_city";
    public static final String ENTER_YOUR_CITY = "enter_your_city";
    public static final String ENTER_YOUR_EVENT_ADDRESS = "enter_your_event_address";
    public static final String EVENT_VANUE = "event_vanue";
    public static final String ENTER_YOUR_EVENT_VENUE = "enter_your_event_venue";
    public static final String ADD_EVENT_BANNER = "add_event_banner";
    public static final String ADD_EVENT_BANNER_THUMB = "add_event_banner_thumb";
    public static final String BUDGET = "budget";
    public static final String CREATE_EVENT = "create_event";
    public static final String PLEASE_ENTER_EVENT_NAME = "please_enter_event_name";
    public static final String PLEASE_SELECT_START_DATE = "please_select_start_date";
    public static final String PLEASE_SELECT_END_DATE = "please_select_end_date";
    public static final String PLEASE_SELECT_START_TIME = "please_select_start_time";
    public static final String PLEASE_SELECT_END_TIME = "please_select_end_time";
    public static final String PLEASE_ENTER_EVENT_DESCRIPTION = "please_enter_event_description";
    public static final String PLEASE_ENTER_EVENT_ADDRESS = "please_enter_event_address";
    public static final String PLEASE_ENTER_EVENT_VENUE = "please_enter_event_venue";
    public static final String PLEASE_ENTER_EVENT_BUDGET = "please_enter_event_budget";
    public static final String PLEASE_SELECT_BANNER_IMAGE = "please_select_banner_image";
    public static final String PLEASE_SELECT_THUMB_IMAGE = "please_select_thumb_image";
    public static final String PLEASE_SELECT_CATEGORY = "please_select_category";
    public static final String PLEASE_ENTER_LOWEST_AMOUNT = "please_enter_lowest_amount";

    //caption event description event
    public static final String ABOUT = "About";
    public static final String VENUE = "Venue";
    public static final String GET_DIRECTION = "get_direction";
    public static final String ORGANISER = "organiser";
    public static final String MORE_LIKE = "more_like_this";
    public static final String REQUESTVOUCHER = "request_voucher";
    public static final String REDEEMVOUCHER = "redeem_voucher";
    public static final String ENTERCITYNAME = "enter_city_name";
    public static final String MULTIPLE_CITY = "add_multiple";
    public static final String VOUCHER = "voucher";
    public static final String BOOKTICKET = "book_ticket";
    public static final String SELECTVOUCHERTYPE = "select_voucher_type";
    public static final String PERVOUCHER = "per_voucher";
    public static final String NUMBEROFVOUCHER = "number_of_voucher";
    public static final String AMOUNTTOBE = "amount_to_be";
    public static final String PROCEED = "proceed";
    public static final String PLEASESELECTVOUCHERTYPE = "please_select_voucher_type";
    public static final String PLEASESELECTNUMBERVOUCHER = "please_select_number_voucher";
    public static final String VOUCHERLIST = "voucher_list";
    public static final String TICKETLIST = "ticket_list";
    public static final String PRICE = "price";
    public static final String APPROVED = "approved";
    public static final String DISAPPROVED = "disapproved";
    public static final String STATUS = "status";
    public static final String SELECTTICKET = "select_ticket";
    public static final String TICKETTYPEC = "ticket_type";
    public static final String LOGOUT = "logout";
    public static final String PAYMENT_DATE = "payment_date";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String QRCODE = "qr_code";
    public static final String SELECTCATEGORIES = "select_categories";
    public static final String NEXT = "next";
    public static final String STRCATEGORIES = "str_categories";
    public static final String REMAININGBUDGET = "remaining_budget";
    public static final String PAYMENTSTATUS = "payment_status";
    public static final String REDEEMDATETIME = "redeem_date_time";
    public static final String USERNAME = "user_name";
    public static final String CHANGELANGUAGE = "change_language";
    public static final String REDEEMQRCODE = "redeem_qr_code";
    public static final String TRANSATIONID = "transaction_id";
    public static final String NOTIFICATIONS = "notification";
    public static final String HELP = "help";
    public static final String CREATEVENDOR = "create_vendor";
    public static final String ADDVENDOR = "add_vendor";
    public static final String TYPE = "type";
    public static final String DISCOUNT = "discount";
    public static final String SELECT_VENDER = "select_vender";
    public static final String REMOVE = "remove";
    public static final String ADDRESS = "address";
    public static final String VOUCHERQRCODE = "voucher_qr_code";
    public static final String SELECTPAYMENT = "select_payment";
    public static final String ENTER_AMOUNT = "enter_amount";
    public static final String ENTER_VOUCHER_CODE = "enter_voucher_code";
    public static final String OR = "or";
    public static final String SCAN_YOUR_CODE = "scan_your_code";
    public static final String UPDATECATEGORIES = "update_categories";
    public static final String EVENTSTARTDATE = "event_start_date";
    public static final String EVENTENDDATE = "event_end_date";
    public static final String ENTERTICKETCODE = "enter_ticket_code";
    public static final String EVENTFEES = "event_fees";
    public static final String EVENTTOTAL_AMOUNT = "total_amount";
    public static final String DISCOUNTAMOUT = "discount_amount";
    public static final String REMAININGAMOUNT = "remaining_amount";
    public static final String DISCOUNTPERCENTAGE = "discount_percentage";


    //caption create event
    public static final String UPDATEC = "update_event";
    public static final String VOUCHER_TYPE = "voucher_type";
    public static final String AFTEREVENT = "after_end_date";
    public static final String QUANTITY = "quantity";
    public static final String AMOUNT = "amount";
    public static final String TOTAL = "total";
    public static final String TICKET = "ticket";
    public static final String FILLEDDETAILS = "filled_all_details";

    public static final String CURRENTTIME = "current_time";
}
