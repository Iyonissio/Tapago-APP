package com.tapago.app.interfaces;


import com.tapago.app.model.AddressListModel.AddressResponseModel;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.BookTicketMerchentModel.BookTicketMerchentModel;
import com.tapago.app.model.BookTicketUserModel.BookTicketUserModel;
import com.tapago.app.model.BrainTreeTokenModel;
import com.tapago.app.model.CMSModel.CmsModel;
import com.tapago.app.model.CategoryCaption.Category;
import com.tapago.app.model.CategoryList.CategoryList;
import com.tapago.app.model.CategoryMain.CategoryResponseModel;
import com.tapago.app.model.CityModel.CityModel;
import com.tapago.app.model.CreateEventModel;
import com.tapago.app.model.EditEventModel.EditEventModel;
import com.tapago.app.model.EventListModel.EventModel;
import com.tapago.app.model.ForgotPassModel.ForgotOTPModel;
import com.tapago.app.model.ForgotPassModel.ForgotVerifyOTPModel;
import com.tapago.app.model.ForgotPassowordCaption.ForgotPassowordCaption;
import com.tapago.app.model.GetVendorModel.GetVendorModel;
import com.tapago.app.model.HelpModel.HelpResponseModel;
import com.tapago.app.model.LoginCaptionModel.BasicCaption;
import com.tapago.app.model.LoginModel.Login;
import com.tapago.app.model.MoreEvent.MoreEventModel;
import com.tapago.app.model.MyEventModel.MyEventResponse;
import com.tapago.app.model.NotificationModel.NotificationModel;
import com.tapago.app.model.OTPCaption.OTPModel;
import com.tapago.app.model.OrderHistoryModel.OrderHistoryResponseModel;
import com.tapago.app.model.PaymentHistoryModel.PaymentModel;
import com.tapago.app.model.PaymentMearchentModel.PaymentMearchentModel;
import com.tapago.app.model.ProductModel.ProductResponseModel;
import com.tapago.app.model.QrCodeModel;
import com.tapago.app.model.RecentViewModel.RecentViewModel;
import com.tapago.app.model.RedeemModel.RedeemModel;
import com.tapago.app.model.RegisterCaptionModel.RegisterCaption;
import com.tapago.app.model.SendOtpModel;
import com.tapago.app.model.ShippingPaymentHistoryModel.ShippingPaymentHistoryModel;
import com.tapago.app.model.TicketListModel.TicketListModel;
import com.tapago.app.model.TicketModel.TicketModel;
import com.tapago.app.model.UpdateProfileModel.UpdateProfileResponse;
import com.tapago.app.model.UserTicketQr.TicketQrModel;
import com.tapago.app.model.UserVoucherList.VoucherListResponse;
import com.tapago.app.model.UserVoucherQr.VoucherQrModel;
import com.tapago.app.model.VerifyOtpModel;
import com.tapago.app.model.VoucherListModel.VoucherListModel;
import com.tapago.app.model.VoucherListMurchantModel.VoucherListMurchantModel;
import com.tapago.app.model.VoucherModel.VoucherModel;
import com.tapago.app.rest.RestConstant;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiService {
    @FormUrlEncoded
    @POST(RestConstant.LOGIN_API)
    Call<Login> validateLogin(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.SEND_OTP_API)
    Call<SendOtpModel> sendOtp(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.VERIFY_OTP_API)
    Call<VerifyOtpModel> verifyOtp(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.SIGNUP_API)
    Call<Login> signUp(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.CATEGORYLSIT_API)
    Call<CategoryList> categoryList(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.FORGOTPASSWORD_API)
    Call<BasicModel> forgotpassword(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.CMS_API)
    Call<CmsModel> cmsContent(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.EVENT_LIST)
    Call<EventModel> eventList(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.HELP_LIST)
    Call<HelpResponseModel> helpList(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.CHANGEPASSWORD_API)
    Call<BasicModel> changePassword(@FieldMap HashMap<String, String> parameters);

    @Multipart
    @POST(RestConstant.UPDATE_PROFILE_API)
    Call<UpdateProfileResponse> updateProfileApi(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part bodyImg1);

    @FormUrlEncoded
    @POST(RestConstant.LOGOUT_API)
    Call<BasicModel> logoutApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.CLOSEACCOUNT_API)
    Call<BasicModel> closeAccountApi(@FieldMap HashMap<String, String> parameters);

    @Multipart
    @POST(RestConstant.CREATE_EVENTS)
    Call<CreateEventModel> createEvent(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part bodyImg1, @Part MultipartBody.Part bodyImg2);

    @FormUrlEncoded
    @POST(RestConstant.VOUCHER_CATEGORY)
    Call<VoucherModel> voucherCategory(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.MY_EVENTS)
    Call<MyEventResponse> myEventList(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.RECENT_VIEW)
    Call<RecentViewModel> recentViewList(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.VIEW_EVENT)
    Call<BasicModel> viewEvent(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.CITYADD)
    Call<BasicModel> addCity(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.GETCITY)
    Call<CityModel> getCity(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.REMOVECITY)
    Call<BasicModel> removeCity(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.MOREEVENT)
    Call<MoreEventModel> moreEventApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.VOUCHEREVENT)
    Call<VoucherListModel> voucherListApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.RVOUCHER)
    Call<BasicModel> requestVoucherApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.EVENTDETAILS)
    Call<EditEventModel> editApi(@FieldMap HashMap<String, String> parameters);

    @Multipart
    @POST(RestConstant.UPDATEEVENT)
    Call<CreateEventModel> updateApi(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part bodyImg1, @Part MultipartBody.Part bodyImg2);

    @FormUrlEncoded
    @POST(RestConstant.TICKETTYPE)
    Call<TicketModel> ticketApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.VOUCHER_LIST_MURCHANT)
    Call<VoucherListMurchantModel> voucherListMurchantApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.VOUCHER_LIST_USER)
    Call<VoucherListResponse> voucherListUserApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.APPROVE_DISAPPROVE)
    Call<BasicModel> approveDisApproveApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.TICKET_LIST)
    Call<TicketListModel> ticketListApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.REQUEST_TICKET)
    Call<BasicModel> requestTicketApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.REQUEST_TICKET_LIST)
    Call<BookTicketUserModel> requestUserTicketListApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.TICKET_MERCHENT_LIST)
    Call<BookTicketMerchentModel> ticketListMurchantApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.APPROVE_DISAPPROVE_TICKET)
    Call<BasicModel> approveDisApproveTicketApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.BRINTREETOKEN)
    Call<BrainTreeTokenModel> brinTreeApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.CREATE_EVENT_PAYMENT)
    Call<BasicModel> createEventApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.VOUCHERTICKET_PAYMENT)
    Call<com.tapago.app.model.PaymentModel> voucherTicketPaymentApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.PAYMENTHISTORY)
    Call<PaymentModel> paymentHistoryApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.REEDEEMQRCODE)
    Call<QrCodeModel> redeemQrCodeApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.REEDEEMQRCODEHISTORY)
    Call<RedeemModel> redeemQrCodeHistoryApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.PAYMENTHISTORYMEARCHENT)
    Call<PaymentMearchentModel> paymentMerchantApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.NOTIFICATION)
    Call<NotificationModel> notificationApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.VENDORADD)
    Call<BasicModel> vendorAddApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.GETVENDOR)
    Call<GetVendorModel> getVendorApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.USERVOUCHERLIST)
    Call<VoucherQrModel> voucherQrApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.USERTICKETLIST)
    Call<TicketQrModel> ticketQrApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.VENDORAPPROVED)
    Call<BasicModel> approvedVendorApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.EVENT_DELETE)
    Call<BasicModel> eventDeleteApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.FORGOTPASSWORD_OTP)
    Call<ForgotOTPModel> forgotPassOTPApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.FORGOTPASSWORD_OTP_VERIFY)
    Call<ForgotVerifyOTPModel> forgotOTPVeryfyApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.FORGOTPASSWORD_CHANGE)
    Call<BasicModel> forgotChangePassApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.ADDCATEGORY)
    Call<BasicModel> addCategoryApi(@FieldMap HashMap<String, String> parameters);

    // caption Api
    @FormUrlEncoded
    @POST(RestConstant.CAPTIONAPI)
    Call<BasicCaption> captionApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.CAPTIONAPI)
    Call<RegisterCaption> registercaptionApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.CAPTIONAPI)
    Call<OTPModel> otpcaptionApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.CAPTIONAPI)
    Call<Category> categorycaptionApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.CAPTIONAPI)
    Call<ForgotPassowordCaption> forgotpasswordcaptionApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.CategoryListMain)
    Call<CategoryResponseModel> categoryLisApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.ProductList)
    Call<ProductResponseModel> productListApi(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.PAYMENTFORSHIPPING)
    Call<com.tapago.app.model.PaymentModel> paymentShippingAPI(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.ORDERHISTORYLIST)
    Call<OrderHistoryResponseModel> orderHistoryAPI(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.SHIPPINGPAYMENTHSITORY)
    Call<ShippingPaymentHistoryModel> shippingPaymentHistoryAPI(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.MostPopularAPi)
    Call<ProductResponseModel> mostPopularAPI(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.AddressListAPI)
    Call<AddressResponseModel> addressListAPI(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.deleteAddressAPI)
    Call<BasicModel> deleteAddressAPI(@FieldMap HashMap<String, String> parameters);

    @FormUrlEncoded
    @POST(RestConstant.insertAddressAPI)
    Call<BasicModel> insertAddressAPI(@FieldMap HashMap<String, String> parameters);
}