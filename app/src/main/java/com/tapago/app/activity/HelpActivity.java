package com.tapago.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.tapago.app.R;
import com.tapago.app.adapter.HelpAdapter;
import com.tapago.app.model.HelpModel.Datum;
import com.tapago.app.model.HelpModel.HelpResponseModel;
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

public class HelpActivity extends BaseActivity {

    @BindView(R.id.edtHelp)
    AppCompatEditText edtHelp;
    @BindView(R.id.btnSearchHelp)
    AppCompatButton btnSearchHelp;
    @BindView(R.id.recycleViewHelp)
    RecyclerView recycleViewHelp;
    HelpAdapter helpAdapter;
    List<Datum> helpResponseModels = new ArrayList<>();
    String strKeyword = "";
    @BindView(R.id.txtHereHelp)
    AppCompatTextView txtHereHelp;
    @BindView(R.id.txtTopic)
    AppCompatTextView txtTopic;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);

        edtHelp.setHint(MySharedPreferences.getMySharedPreferences().getAskQuestion());
        btnSearchHelp.setText(MySharedPreferences.getMySharedPreferences().getSearch());
        txtHereHelp.setText(MySharedPreferences.getMySharedPreferences().getHereHelp());
        txtTopic.setText(MySharedPreferences.getMySharedPreferences().getPopularTopic());
        txtName.setText(MySharedPreferences.getMySharedPreferences().getHelp());

        helpListApi();
    }

    @OnClick({R.id.ivBackArrow, R.id.btnSearchHelp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.startFromRightToLeft(getActivity());
                break;
            case R.id.btnSearchHelp:
                if (TextUtils.isEmpty(AppUtils.getText(edtHelp))) {
                    showSnackBar(getActivity(), MySharedPreferences.getMySharedPreferences().getEnterQuestion());
                    return;
                }
                AppUtils.hideSoftKeyboard(getActivity());
                helpResponseModels.clear();
                strKeyword = edtHelp.getText().toString();
                helpListApi();
                break;
        }
    }

    private void helpListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("key_word", strKeyword);


            Call<HelpResponseModel> call;
            call = RetrofitRestClient.getInstance().helpList(params);

            if (call == null) return;

            call.enqueue(new Callback<HelpResponseModel>() {
                @Override
                public void onResponse(@NonNull final Call<HelpResponseModel> call, @NonNull Response<HelpResponseModel> response) {
                    hideProgressDialog();
                    final HelpResponseModel helpResponseModel;
                    if (response.isSuccessful()) {
                        helpResponseModel = response.body();
                        if (Objects.requireNonNull(helpResponseModel).getCode() == 200) {
                            helpResponseModels.addAll(helpResponseModel.getData());
                            recycleViewHelp.setLayoutManager(new LinearLayoutManager(getActivity()));
                            helpAdapter = new HelpAdapter(getActivity(), helpResponseModel.getData());
                            recycleViewHelp.setAdapter(helpAdapter);
                        } else if (Objects.requireNonNull(helpResponseModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), helpResponseModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<HelpResponseModel> call, @NonNull Throwable t) {
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
