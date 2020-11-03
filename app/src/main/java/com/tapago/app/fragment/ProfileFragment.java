package com.tapago.app.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbb20.CountryCodePicker;
import com.tapago.app.R;
import com.tapago.app.activity.ChangePasswordActivity;
import com.tapago.app.activity.LoginActivity;
import com.tapago.app.activity.MainActivity;
import com.tapago.app.activity.OTPActivity;
import com.tapago.app.activity.UpdateCityActivity;
import com.tapago.app.model.SendOtpModel;
import com.tapago.app.model.UpdateProfileModel.Data;
import com.tapago.app.model.UpdateProfileModel.UpdateProfileResponse;
import com.tapago.app.rest.RestConstant;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.CameraPhoto;
import com.tapago.app.utils.ImageCompression;
import com.tapago.app.utils.MySharedPreferences;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
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

import static android.app.Activity.RESULT_OK;


@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.edFirstName)
    AppCompatEditText edFirstName;
    @BindView(R.id.edLastName)
    AppCompatEditText edLastName;
    @BindView(R.id.edEmail)
    AppCompatEditText edEmail;
    @BindView(R.id.c_code_picker)
    CountryCodePicker cCodePicker;
    @BindView(R.id.edMobile)
    AppCompatEditText edMobile;
    @BindView(R.id.inputFirstName)
    TextInputLayout inputFirstName;
    @BindView(R.id.inputLastName)
    TextInputLayout inputLastName;
    @BindView(R.id.inputEmail)
    TextInputLayout inputEmail;
    @BindView(R.id.inputMobile)
    TextInputLayout inputMobile;
    @BindView(R.id.txtUpdateCity)
    AppCompatTextView txtUpdateCity;
    @BindView(R.id.txtChange)
    AppCompatTextView txtChange;
    @BindView(R.id.btnSubmit)
    AppCompatButton btnSubmit;
    private String userChoosenTask, picturePath = "";
    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_WRITE_STORAGE_PERM = 124;
    private static final int RC_READ_STORAGE_PERM = 125;
    private CameraPhoto cameraPhoto;
    private int PICK_CAMERA_REQUEST = 2;
    private int PICK_IMAGE_REQUEST = 1;
    ProgressDialog progressDialog;
    private ArrayList<String> photoPaths = new ArrayList<>();
    MainActivity mainActivity;

    @SuppressLint("ValidFragment")
    public ProfileFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.notificationListUserApi();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            if (MySharedPreferences.getMySharedPreferences().getUserRoleId().equals("6")) {
                txtUpdateCity.setVisibility(View.GONE);
            } else {
                txtUpdateCity.setVisibility(View.VISIBLE);
            }
            edFirstName.setText(MySharedPreferences.getMySharedPreferences().getFirstName());
            edLastName.setText(MySharedPreferences.getMySharedPreferences().getLastName());
            edEmail.setText(MySharedPreferences.getMySharedPreferences().getEmail());
            edMobile.setText(MySharedPreferences.getMySharedPreferences().getContactNumber());
            cCodePicker.setCountryForNameCode(MySharedPreferences.getMySharedPreferences().getCountryName());
            inputFirstName.setHint(MySharedPreferences.getMySharedPreferences().getFirst_Name_Caption());
            inputLastName.setHint(MySharedPreferences.getMySharedPreferences().getLast_Name_Caption());
            inputEmail.setHint(MySharedPreferences.getMySharedPreferences().getEmail_Caption());
            inputMobile.setHint(MySharedPreferences.getMySharedPreferences().getPhone_Caption());
            txtUpdateCity.setText(MySharedPreferences.getMySharedPreferences().getUpdateCity());
            txtChange.setText(MySharedPreferences.getMySharedPreferences().getChangePassword());
            btnSubmit.setText(MySharedPreferences.getMySharedPreferences().getSubmit());
            RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image);
            Glide.with(Objects.requireNonNull(getActivity())).load(MySharedPreferences.getMySharedPreferences().getProfile()).apply(options).into(profileImage);

            if (MySharedPreferences.getMySharedPreferences().getLanguage().equalsIgnoreCase("en")) {
                cCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH);
            } else {
                cCodePicker.changeDefaultLanguage(CountryCodePicker.Language.PORTUGUESE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rlImage, R.id.txtChange, R.id.btnSubmit, R.id.txtUpdateCity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlImage:
                selectImage();
                break;
            case R.id.txtChange:
                RestConstant.FORGOT_PASSWORD = "";
                Intent i = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(i);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.txtUpdateCity:
                Intent i1 = new Intent(getActivity(), UpdateCityActivity.class);
                startActivity(i1);
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.btnSubmit:
                if (personalInfo()) {
                    File imgFile = new File(picturePath);
                    int fileSizeInKB = Integer.parseInt(String.valueOf(imgFile.length() / 1024));
                    if (fileSizeInKB > 8192) {
                        Toast.makeText(getActivity(), R.string.file_size, Toast.LENGTH_SHORT).show();
                    } else {
                        String strNum = MySharedPreferences.getMySharedPreferences().getCountryCode() + MySharedPreferences.getMySharedPreferences().getContactNumber();
                        String updNum = cCodePicker.getSelectedCountryCodeWithPlus() + edMobile.getText().toString().trim();
                        if (strNum.equals(updNum)) {
                            updateApi();
                        } else if (!MySharedPreferences.getMySharedPreferences().getEmail().equals(edEmail.getText().toString())) {
                            if (!strNum.equals(updNum)) {
                                sendOtpApi();
                            } else {
                                updateApi();
                            }
                        } else {
                            sendOtpApi();
                        }
                    }
                }
                break;
        }
    }


    /**
     * update profile api
     */
    private void updateApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, RequestBody> params = new HashMap<>();
            params.put("first_name", AppUtils.getRequestBody(AppUtils.getText(edFirstName)));
            params.put("last_name", AppUtils.getRequestBody(AppUtils.getText(edLastName)));
            params.put("email", AppUtils.getRequestBody(AppUtils.getText(edEmail)));
            params.put("country_code", AppUtils.getRequestBody(cCodePicker.getSelectedCountryCodeWithPlus()));
            params.put("contact_number", AppUtils.getRequestBody(AppUtils.getText(edMobile)));
            params.put("country_code_name", AppUtils.getRequestBody(cCodePicker.getSelectedCountryNameCode()));
            params.put("user_id", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getUserId()));
            params.put("user_device_id", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getDeviceId()));
            params.put("access_token", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getAccessToken()));
            params.put("lang", AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences().getLanguage()));

            MultipartBody.Part partbody1 = null;
            if (picturePath.length() > 0) {
                File file = new File(picturePath);
                RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), file);
                partbody1 = MultipartBody.Part.createFormData("image", file.getName(), reqFile1);
            }

            Call<UpdateProfileResponse> call;
            call = RetrofitRestClient.getInstance().updateProfileApi(params, partbody1);

            if (call == null) return;

            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(@NonNull Call<UpdateProfileResponse> call, @NonNull Response<UpdateProfileResponse> response) {
                    hideProgressDialog();
                    UpdateProfileResponse updateProfile;
                    if (response.isSuccessful()) {
                        updateProfile = response.body();
                        if (Objects.requireNonNull(updateProfile).getCode() == 200) {
                            try {
                                showSnackBars(getActivity(), updateProfile.getMessage());
                                setUserData(updateProfile.getData());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(updateProfile).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), updateProfile.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UpdateProfileResponse> call, @NonNull Throwable t) {
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
     * send Otp Api
     */
    private void sendOtpApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> map = new HashMap<>();
            map.put("mobile_no", cCodePicker.getSelectedCountryCodeWithPlus() + AppUtils.getText(edMobile));
            map.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            map.put("email", AppUtils.getText(edEmail));
            map.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            map.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            map.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());

            Call<SendOtpModel> call;
            call = RetrofitRestClient.getInstance().sendOtp(map);

            if (call == null) return;

            call.enqueue(new Callback<SendOtpModel>() {
                @Override
                public void onResponse(@NonNull Call<SendOtpModel> call, @NonNull Response<SendOtpModel> response) {
                    hideProgressDialog();
                    final SendOtpModel sendOtpModel;
                    if (response.isSuccessful()) {
                        sendOtpModel = response.body();
                        if (Objects.requireNonNull(sendOtpModel).getCode() == 200) {
                            AppUtils.showToast(getActivity(), sendOtpModel.getMessage());
                            RestConstant.PROFILE_UPDATE = "update";
                            Intent intent = new Intent(getActivity(), OTPActivity.class);
                            intent.putExtra("firstname", AppUtils.getText(edFirstName));
                            intent.putExtra("lastname", AppUtils.getText(edLastName));
                            intent.putExtra("email", AppUtils.getText(edEmail));
                            intent.putExtra("contact_number", AppUtils.getText(edMobile));
                            intent.putExtra("country_code", cCodePicker.getSelectedCountryCodeWithPlus());
                            intent.putExtra("contact_country_code_name", cCodePicker.getSelectedCountryNameCode());
                            intent.putExtra("picturePath", picturePath);
                            startActivity(intent);
                            AppUtils.startFromRightToLeft(getActivity());
                        } else if (Objects.requireNonNull(sendOtpModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), sendOtpModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SendOtpModel> call, @NonNull Throwable t) {
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

    public void logout() {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
        mySharedPreferences.setUserId("");
        mySharedPreferences.setLogin(false);
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        Objects.requireNonNull(getActivity()).finish();
        AppUtils.startFromRightToLeft(getActivity());
    }

    /**
     * Store data into pref
     *
     * @param data
     */
    private void setUserData(Data data) {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
        mySharedPreferences.setLogin(true);
        mySharedPreferences.setUserId(String.valueOf(data.getId()));
        mySharedPreferences.setFirstName(data.getFirstName());
        mySharedPreferences.setLastName(data.getLastName());
        mySharedPreferences.setEmail(data.getEmail());
        mySharedPreferences.setCountryCode(data.getCountryCode());
        mySharedPreferences.setCountryName(data.getCountryCodeName());
        mySharedPreferences.setProfile(data.getImage());

        try {
            edFirstName.setText(MySharedPreferences.getMySharedPreferences().getFirstName());
            edLastName.setText(MySharedPreferences.getMySharedPreferences().getLastName());
            edEmail.setText(MySharedPreferences.getMySharedPreferences().getEmail());
            edMobile.setText(MySharedPreferences.getMySharedPreferences().getContactNumber());
            cCodePicker.setCountryForNameCode(MySharedPreferences.getMySharedPreferences().getCountryName());

            RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image);
            Glide.with(Objects.requireNonNull(getActivity())).load(MySharedPreferences.getMySharedPreferences().getProfile()).apply(options).into(profileImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * SIMPLE DIALOG PROGRESS
     *
     * @param ctx
     */
    public void showProgressDialog(Context ctx) {
        progressDialog = ProgressDialog.show(ctx, "", getString(R.string.str_please_wait), true, false);
    }

    public void showProgressDialog(Context ctx, boolean canCancel) {
        progressDialog = ProgressDialog.show(ctx, "", getString(R.string.str_please_wait), true, canCancel);
    }

    public void hideProgressDialog() {
        try {
            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Validation
     */
    private boolean personalInfo() {
        if (TextUtils.isEmpty(AppUtils.getText(edFirstName))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPleaseEnterFirstName());
            return false;
        } else if (!AppUtils.isNameValid(AppUtils.getText(edFirstName))) {
            showSnackBar(getActivity(), getString(R.string.first_no_number_or_symbols));
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edLastName))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPleaseEnterLastName());
            return false;
        } else if (!AppUtils.isNameValid(AppUtils.getText(edLastName))) {
            showSnackBar(getActivity(), getString(R.string.first_no_number_or_symbols));
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edEmail))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPleaseEnterYourEmail());
            return false;
        } else if (!AppUtils.isEmailValid(AppUtils.getText(edEmail))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPleaseEnterValidEmail());
            return false;
        } else if (TextUtils.isEmpty(AppUtils.getText(edMobile))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPleaseEnterMobileNumber());
            return false;
        } else if (!AppUtils.isValidMobile(AppUtils.getText(edMobile))) {
            showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getPleaseEnterValidMobileNumber());
            return false;
        } else {
            return true;
        }
    }

    /**
     * SIMPLE SNACKBAR
     */
    public void showSnackBar(Context context, String message) {
        ViewGroup view = (ViewGroup) ((ViewGroup) Objects.requireNonNull(getActivity()).findViewById(android.R.id.content)).getChildAt(0);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE);
        ViewGroup viewGroup = (ViewGroup) snackbar.getView();
        viewGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        View viewTv = snackbar.getView();
        TextView tv = viewTv.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.red));
        tv.setMaxLines(5);
        snackbar.show();
    }


    /**
     * SIMPLE SNACKBAR
     */
    public void showSnackBars(Context context, String message) {
        ViewGroup view = (ViewGroup) ((ViewGroup) Objects.requireNonNull(getActivity()).findViewById(android.R.id.content)).getChildAt(0);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE);
        ViewGroup viewGroup = (ViewGroup) snackbar.getView();
        viewGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        View viewTv = snackbar.getView();
        TextView tv = viewTv.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        tv.setMaxLines(5);
        snackbar.show();
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_library), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        if (EasyPermissions.hasPermissions(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
        if (EasyPermissions.hasPermissions(Objects.requireNonNull(getContext()), Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
        if (EasyPermissions.hasPermissions(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA)) {
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
        if (requestCode == PICK_CAMERA_REQUEST && resultCode == RESULT_OK) {
            cameraPhoto.addToGallery();
            picturePath = cameraPhoto.getPhotoPath();
            picturePath = ImageCompression.compressImage(picturePath);
            Glide.with(Objects.requireNonNull(getActivity())).load(picturePath).into(profileImage);
        } else if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                photoPaths = new ArrayList<>();
                photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                for (int i = 0; i < photoPaths.size(); i++) {
                    picturePath = photoPaths.get(i);
                }
                Glide.with(Objects.requireNonNull(getActivity())).load(picturePath).into(profileImage);
            }

        }
    }
}
