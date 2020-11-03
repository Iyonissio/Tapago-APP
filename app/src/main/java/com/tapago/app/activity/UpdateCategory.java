package com.tapago.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.tapago.app.R;
import com.tapago.app.adapter.UpdateCategoryAdapter;
import com.tapago.app.model.BasicModel;
import com.tapago.app.model.CategoryCaption.Category;
import com.tapago.app.model.CategoryList.CategoryList;
import com.tapago.app.model.CategoryList.Datum;
import com.tapago.app.pagination.EndlessRecyclerOnScrollListener;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCategory extends BaseActivity {

    @BindView(R.id.txtText)
    AppCompatTextView txtText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btnNext)
    AppCompatButton btnNext;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    private ArrayList<Datum> categoryArrayList = new ArrayList<>();
    UpdateCategoryAdapter categoryAdapter;
    private StringBuilder sb,sb1;
    String strCategories = "";
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private int TOTAL_PAGES;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        sb = new StringBuilder();
        sb1 = new StringBuilder();

        setRecyclerView();
        categoryCaptionApi();
    }


    //Setting recycler view
    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        categoryAdapter = new UpdateCategoryAdapter(getActivity(), categoryArrayList, UpdateCategory.this);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);//Linear Items
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoryAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                categoryListApi(current_page);
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        AppUtils.finishFromLeftToRight(getActivity());
    }


    @OnClick({R.id.btnNext, R.id.txtTryAgain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                sb = new StringBuilder();
                sb1 = new StringBuilder();
                if (categoryArrayList.size() > 0) {
                    for (int i = 0; i < categoryArrayList.size(); i++) {
                        if (categoryArrayList.get(i).getChackFlag().equalsIgnoreCase("true")) {
                            sb.append(categoryArrayList.get(i).getId()).append(",");
                        }
                    }
                }

                if (sb.length() > 0) {
                    String str = sb.toString().substring(0, sb.toString().length() - 1);
                    sb = new StringBuilder();
                    sb.append("[").append(str).append("]");
                    sb1.append(str);
                    MySharedPreferences mySharedPreferences = MySharedPreferences.getMySharedPreferences();
                    mySharedPreferences.setCategoryId(sb.toString());

                    addCategory();
                } else {
                    showSnackBar(getActivity(), strCategories);
                }
                break;
            case R.id.txtTryAgain:
                categoryArrayList.clear();
                categoryCaptionApi();
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
                                        txtTryAgain.setVisibility(View.GONE);
                                        txtNoDataFound.setVisibility(View.GONE);
                                        txtText.setText(category.getActivity().getSelectCategories());
                                        btnNext.setText(category.getActivity().getNext());
                                        strCategories = category.getActivity().getCategorySelect();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            categoryListApi(currentPage);
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

    /**
     * CategoryListApi
     */
    private void categoryListApi(final int page) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            try {
                mainProgress.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("deviceid", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("page_number", String.valueOf(page));
            params.put("pagination_flag", "true");
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<CategoryList> call;
            call = RetrofitRestClient.getInstance().categoryList(params);

            if (call == null) return;

            call.enqueue(new Callback<CategoryList>() {
                @Override
                public void onResponse(@NonNull final Call<CategoryList> call, @NonNull Response<CategoryList> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final CategoryList categoryList;
                    if (response.isSuccessful()) {
                        categoryList = response.body();
                        if (Objects.requireNonNull(categoryList).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                TOTAL_PAGES = categoryList.getPageCount();
                                categoryArrayList.addAll(categoryList.getData());
                                categoryAdapter = new UpdateCategoryAdapter(getActivity(), categoryArrayList, UpdateCategory.this);
                                recyclerView.setAdapter(categoryAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(categoryList).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(categoryList).getCode() == 301) {
                            try {
                                txtNoDataFound.setText(categoryList.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                }
                                endlessRecyclerOnScrollListener.previousState();
                                categoryAdapter.showLoadMore(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), categoryList.getMessage());
                            endlessRecyclerOnScrollListener.previousState();
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                        endlessRecyclerOnScrollListener.previousState();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CategoryList> call, @NonNull Throwable t) {
                    try {
                        categoryAdapter.showLoadMore(false);
                        endlessRecyclerOnScrollListener.previousState();
                        mainProgress.setVisibility(View.GONE);
                        if (t instanceof SocketTimeoutException) {
                            txtNoDataFound.setText(getString(R.string.connection_timeout));
                            txtTryAgain.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                            if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                            } else {
                                showSnackBar(getActivity(), getString(R.string.connection_timeout));
                            }
                        } else {
                            t.printStackTrace();
                            txtNoDataFound.setText(getString(R.string.something_went_wrong));
                            txtTryAgain.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                            if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                            } else {
                                showSnackBar(getActivity(), getString(R.string.something_went_wrong));
                            }
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
            if (page > TOTAL_PAGES && TOTAL_PAGES != 0) {
                txtNoDataFound.setVisibility(View.GONE);
                txtTryAgain.setVisibility(View.GONE);
            } else {
                showSnackBar(getActivity(), getString(R.string.no_internet));
            }
        }
    }

    /**
     * addCategoryApi
     */
    private void addCategory() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            showProgressDialog(getActivity());
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", MySharedPreferences.getMySharedPreferences().getUserId());
            params.put("access_token", MySharedPreferences.getMySharedPreferences().getAccessToken());
            params.put("device_id", MySharedPreferences.getMySharedPreferences().getDeviceId());
            params.put("category_id", sb1.toString());
            params.put("lang", MySharedPreferences.getMySharedPreferences().getLanguage());

            Call<BasicModel> call;
            call = RetrofitRestClient.getInstance().addCategoryApi(params);

            if (call == null) return;

            call.enqueue(new Callback<BasicModel>() {
                @Override
                public void onResponse(@NonNull final Call<BasicModel> call, @NonNull Response<BasicModel> response) {
                    hideProgressDialog();
                    final BasicModel basicModel;
                    if (response.isSuccessful()) {
                        basicModel = response.body();
                        if (Objects.requireNonNull(basicModel).getCode() == 200) {
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            AppUtils.finishFromLeftToRight(getActivity());
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
