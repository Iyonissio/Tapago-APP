package com.tapago.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tapago.app.R;
import com.tapago.app.adapter.ChooseAddressAdapter;
import com.tapago.app.adapter.MostPopularProductListAdapter;
import com.tapago.app.model.AddressListModel.AddressResponseModel;
import com.tapago.app.model.AddressListModel.Datum;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.ProductModel.ProductResponseModel;
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

public class ChooseAddressActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.recycle_address)
    RecyclerView recycleAddress;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    @BindView(R.id.linear_Addnew_Address)
    LinearLayout linearAddnewAddress;
    List<Datum> addressList = new ArrayList<>();
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    ChooseAddressAdapter chooseAddressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain, R.id.linear_Addnew_Address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                addressList.clear();
                addressListApi();
                break;
            case R.id.linear_Addnew_Address:
                    Intent intent=new Intent(this,AddNewAddressActivity.class);
                    startActivity(intent);
                    AppUtils.startFromRightToLeft(getActivity());
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        addressList.clear();
        addressListApi();
    }

    private void addressListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            addressList.clear();
            try {
                mainProgress.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());


            Call<AddressResponseModel> call;
            call = RetrofitRestClient.getInstance().addressListAPI(params);

            if (call == null) return;

            call.enqueue(new Callback<AddressResponseModel>() {
                @Override
                public void onResponse(@NonNull final Call<AddressResponseModel> call, @NonNull Response<AddressResponseModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final AddressResponseModel addressResponseModel;
                    if (response.isSuccessful()) {
                        addressResponseModel = response.body();
                        if (Objects.requireNonNull(addressResponseModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                addressList.clear();
                                addressList.addAll(addressResponseModel.getData());
                                recycleAddress.setLayoutManager(new LinearLayoutManager(getActivity()));
                                chooseAddressAdapter = new ChooseAddressAdapter(getActivity(),addressList,ChooseAddressActivity.this);
                                recycleAddress.setAdapter(chooseAddressAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(addressResponseModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(addressResponseModel).getCode() == 201) {
                            try {
                                txtNoDataFound.setText(addressResponseModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), addressResponseModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AddressResponseModel> call, @NonNull Throwable t) {
                    try {
                        mainProgress.setVisibility(View.GONE);
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



    public void deleteAddresslApi(String addID) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());
            params.put("shipping_id", addID);

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().deleteAddressAPI(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            Toast.makeText(getActivity(), basicModel.getMessage(),Toast.LENGTH_LONG).show();
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
        }
    }
}
