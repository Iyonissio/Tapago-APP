package com.tapago.app.activity;

import android.annotation.SuppressLint;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.tapago.app.R;
import com.tapago.app.adapter.AutoCompleteAdapter;
import com.tapago.app.adapter.MyCityAdapter;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.CityModel.CityModel;
import com.tapago.app.model.CityModel.Datum;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCityActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.cityCount)
    AppCompatTextView cityCount;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.recycleCity)
    RecyclerView recycleCity;
    @BindView(R.id.edt_search)
    AppCompatAutoCompleteTextView edtSearch;
    Geocoder geocoder;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.txtMultiple)
    AppCompatTextView txtMultiple;
    private ArrayList<Datum> myCityList = new ArrayList<>();
    MyCityAdapter myCityAdapter;
    private String locality = "";
    private StringBuilder sb;
    PlacesClient placesClient;
    AutoCompleteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_city);
        ButterKnife.bind(this);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        sb = new StringBuilder();

        txtName.setText(MySharedPreferences.getMySharedPreferences().getUpdateCity());
        edtSearch.setHint(MySharedPreferences.getMySharedPreferences().getEnterCityName());
        txtMultiple.setText(MySharedPreferences.getMySharedPreferences().getAddMultiple());

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.apiKey));
        }

        placesClient = Places.createClient(this);
        edtSearch.setThreshold(1);
        edtSearch.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        edtSearch.setAdapter(adapter);

        getCityApi();
    }


    @OnClick({R.id.ivBackArrow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
        }
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
                            addCityApi();
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

    /**
     * getCityApi
     */
    private void getCityApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            myCityList.clear();
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<CityModel> call;
            call = RetrofitRestClient.getInstance().getCity(params);

            if (call == null) return;

            call.enqueue(new Callback<CityModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull final Call<CityModel> call, @NonNull Response<CityModel> response) {
                    hideProgressDialog();
                    final CityModel cityModel;
                    if (response.isSuccessful()) {
                        cityModel = response.body();
                        if (Objects.requireNonNull(cityModel).getCode() == 200) {
                            try {
                                myCityList.addAll(cityModel.getData());
                                recycleCity.setLayoutManager(new LinearLayoutManager(getActivity()));
                                myCityAdapter = new MyCityAdapter(UpdateCityActivity.this, cityModel.getData(), UpdateCityActivity.this);
                                recycleCity.setAdapter(myCityAdapter);

                                if (myCityList.size() == 1) {
                                    cityCount.setText(getString(R.string.current) + " " + myCityList.size() + " " + getString(R.string.city));
                                } else if (myCityList.size() > 0) {
                                    cityCount.setText(getString(R.string.current) + " " + myCityList.size() + " " + getString(R.string.citys));
                                }
                                edtSearch.setText("");


                                if (myCityList.size() > 0) {
                                    sb = new StringBuilder();
                                    for (int i = 0; i < myCityList.size(); i++) {
                                        sb.append(myCityList.get(i).getCityName()).append(",");
                                    }

                                    if (sb.length() > 0) {
                                        String str = sb.toString().substring(0, sb.toString().length() - 1);
                                        MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
                                        mySharedPreferences.setCity(str);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(cityModel).getCode() == 201) {
                            try {
                                MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
                                mySharedPreferences.setCity("");
                                myCityAdapter = new MyCityAdapter(UpdateCityActivity.this, myCityList, UpdateCityActivity.this);
                                recycleCity.setAdapter(myCityAdapter);
                                cityCount.setText(getString(R.string.current) + " " + "0" + " " + getString(R.string.citys));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(cityModel).getCode() == 999) {
                            logout();
                        } else {
                            showSnackBar(getActivity(), cityModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CityModel> call, @NonNull Throwable t) {
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
     * addCityApi
     */
    private void addCityApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("city_name", locality);
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().addCity(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull final Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            showSnackBars(getActivity(), basicModel.getMessage());
                            getCityApi();
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
        } else {
            showSnackBar(getActivity(), getString(R.string.no_internet));
        }
    }


    /**
     * removeCityApi
     */
    public void removeCityApi(String cityId) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("id", cityId);
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().removeCity(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull final Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            showSnackBars(getActivity(), basicModel.getMessage());
                            getCityApi();
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
        } else {
            showSnackBar(getActivity(), getString(R.string.no_internet));
        }
    }
}
