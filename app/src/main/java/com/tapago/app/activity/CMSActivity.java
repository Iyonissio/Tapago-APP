package com.tapago.app.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.tapago.app.BuildConfig;
import com.tapago.app.R;
import com.tapago.app.model.CMSModel.CmsModel;
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

public class CMSActivity extends BaseActivity {

    String title = "", slug = "";
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.txtContent)
    AppCompatTextView txtContent;
    @BindView(R.id.imgLogo)
    AppCompatImageView imgLogo;
    @BindView(R.id.txtVersion)
    AppCompatTextView txtVersion;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cms);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            slug = intent.getStringExtra("slug");
            txtName.setText(title);

            if (slug.equals("about-us")) {
                imgLogo.setVisibility(View.VISIBLE);
                txtVersion.setVisibility(View.VISIBLE);
                String versionName = BuildConfig.VERSION_NAME;
                txtVersion.setText(MySharedPreferences.getMySharedPreferences().getVersion() + " " + versionName);
            } else {
                imgLogo.setVisibility(View.GONE);
                txtVersion.setVisibility(View.GONE);
            }
        }

        contentApi();
    }

    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.txtTryAgain:
                contentApi();
                break;
        }
    }

    /**
     * content api
     */
    private void contentApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("user_device_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("user_device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("slug", slug);

            Call<CmsModel> call;
            call = RetrofitRestClient.getInstance().cmsContent(params);

            if (call == null) return;

            call.enqueue(new Callback<CmsModel>() {
                @Override
                public void onResponse(@NonNull Call<CmsModel> call, @NonNull Response<CmsModel> response) {
                    hideProgressDialog();
                    final CmsModel contentResponse;
                    if (response.isSuccessful()) {
                        contentResponse = response.body();
                        if (Objects.requireNonNull(contentResponse).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                Spanned result;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    result = Html.fromHtml(contentResponse.getData().getContent(), Html.FROM_HTML_MODE_LEGACY);
                                } else {
                                    result = Html.fromHtml(contentResponse.getData().getContent());
                                }
                                txtContent.setText(result);
                                txtContent.setMovementMethod(LinkMovementMethod.getInstance());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(contentResponse).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(contentResponse.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (contentResponse.getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), contentResponse.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CmsModel> call, @NonNull Throwable t) {
                    hideProgressDialog();
                    try {
                        if (t instanceof SocketTimeoutException) {
                            txtNoDataFound.setText(getString(R.string.connection_timeout));
                            txtTryAgain.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                        } else {
                            t.printStackTrace();
                            txtNoDataFound.setText(getString(R.string.something_went_wrong));
                            txtTryAgain.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
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
        }
    }
}
