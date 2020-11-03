package com.tapago.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tapago.app.R;
import com.tapago.app.adapter.CategoryMainAdapter;
import com.tapago.app.adapter.MostPopularProductListAdapter;
import com.tapago.app.dialog.ProductPkgDialog;
import com.tapago.app.model.BasketModel;
import com.tapago.app.model.CategoryMain.CategoryModel;
import com.tapago.app.model.CategoryMain.CategoryResponseModel;
import com.tapago.app.model.ProductModel.Datum;
import com.tapago.app.model.ProductModel.ProductResponseModel;
import com.tapago.app.rest.RetrofitRestClient;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.BasketUtils;
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

public class CategoryMainActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rcCategory)
    RecyclerView rcCategory;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    CategoryMainAdapter productListAdapter;
    List<CategoryModel> categoryList = new ArrayList<>();
    @BindView(R.id.txtQty)
    AppCompatTextView txtQty;
    @BindView(R.id.views)
    View views;
    @BindView(R.id.txtPrice)
    AppCompatTextView txtPrice;
    @BindView(R.id.tv_view_cart)
    AppCompatTextView tvViewCart;
    @BindView(R.id.rlBottomView)
    RelativeLayout rlBottomView;

    ProductPkgDialog productPkgDialog;
    List<Datum> mostpopularLst = new ArrayList<>();
    MostPopularProductListAdapter mostPopularProductListAdapter;
    @BindView(R.id.lrCategory)
    LinearLayout lrCategory;
    @BindView(R.id.rcProduct)
    RecyclerView rcProduct;
    @BindView(R.id.edt_search)
    AppCompatEditText edtSearch;
    @BindView(R.id.lrProduct)
    LinearLayout lrProduct;
    List<Datum> productList = new ArrayList<>();
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.collapsing)
    CollapsingToolbarLayout collapsing;
    @BindView(R.id.rlproductlayout)
    RelativeLayout rlproductlayout;
    @BindView(R.id.rcProductsearch)
    RecyclerView rcProductsearch;
    @BindView(R.id.clMain)
    CoordinatorLayout clMain;
    Handler handler = new Handler();
    @BindView(R.id.rlsearch)
    RelativeLayout rlsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_main);
        ButterKnife.bind(this);


        txtNoDataFound.setText(R.string.category_not_found);
        txtName.setText(R.string.supermarket);

        LinearLayoutManager linearLayout = new LinearLayoutManager(CategoryMainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rcCategory.setLayoutManager(linearLayout);
        categoryListApi();
        //  productListAdapter = new CategoryMainAdapter(CategoryMainActivity.this, categoryList);
        //recycleViewrproduct.setAdapter(productListAdapter);

        productPkgDialog = new ProductPkgDialog(CategoryMainActivity.this);
        // txtNoDataFound.setText(R.string.product_not_found);
        rlBottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryMainActivity.this, CartActivity.class);
                startActivity(intent);
                AppUtils.startFromRightToLeft(CategoryMainActivity.this);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    // make textview disappear
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clMain.setVisibility(View.GONE);
                            rlsearch.setVisibility(View.VISIBLE);

                        }
                    }, 2000);

                    mostpopularLst.clear();
                    productSearchListApi(s.toString());

                } else {
                    // make textview disappear
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clMain.setVisibility(View.VISIBLE);
                            rlsearch.setVisibility(View.GONE);
                        }
                    }, 2000);

                    mostpopularLst.clear();
                    productListApi();
                }
            }
        });


    }


    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:

                break;
        }
    }


    private void categoryListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            categoryList.clear();
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

            Call<CategoryResponseModel> call;
            call = RetrofitRestClient.getInstance().categoryLisApi(params);

            if (call == null) return;

            call.enqueue(new Callback<CategoryResponseModel>() {
                @Override
                public void onResponse(@NonNull final Call<CategoryResponseModel> call, @NonNull Response<CategoryResponseModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final CategoryResponseModel categoryResponseModel;
                    if (response.isSuccessful()) {
                        categoryResponseModel = response.body();
                        if (Objects.requireNonNull(categoryResponseModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                categoryList.addAll(categoryResponseModel.getData());
                                productListAdapter = new CategoryMainAdapter(getActivity(), categoryList);
                                rcCategory.setAdapter(productListAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(categoryResponseModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(categoryResponseModel).getCode() == 201) {
                            try {
                                txtNoDataFound.setText(categoryResponseModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), categoryResponseModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }

                    productListApi();
                }

                @Override
                public void onFailure(@NonNull Call<CategoryResponseModel> call, @NonNull Throwable t) {
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


    private void productListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            mostpopularLst.clear();
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


            Call<ProductResponseModel> call;
            call = RetrofitRestClient.getInstance().mostPopularAPI(params);

            if (call == null) return;

            call.enqueue(new Callback<ProductResponseModel>() {
                @Override
                public void onResponse(@NonNull final Call<ProductResponseModel> call, @NonNull Response<ProductResponseModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final ProductResponseModel categoryResponseModel;
                    if (response.isSuccessful()) {
                        categoryResponseModel = response.body();
                        if (Objects.requireNonNull(categoryResponseModel).getCode() == 200) {
                            try {
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                mostpopularLst.clear();
                                mostpopularLst.addAll(categoryResponseModel.getData());
                                rcProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
                                mostPopularProductListAdapter = new MostPopularProductListAdapter(getActivity(), mostpopularLst, CategoryMainActivity.this);
                                rcProduct.setAdapter(mostPopularProductListAdapter);
                                mostPopularProductListAdapter.setOnItemClickLister(new MostPopularProductListAdapter.OnItemClickLister() {
                                    @Override
                                    public void itemClicked(View view, int position) {
                                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                                        intent.putExtra("productDetail", mostpopularLst.get(position));
                                        intent.putExtra("categoryName", mostpopularLst.get(position).getProductName());
                                        intent.putExtra("categoryID", mostpopularLst.get(position).getCategoryIds());
                                        startActivity(intent);
                                        AppUtils.startFromRightToLeft(getActivity());
                                    }
                                });

                                AppUtils.hideSoftKeyboard(CategoryMainActivity.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(categoryResponseModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(categoryResponseModel).getCode() == 201) {
                            try {
                                txtNoDataFound.setText(categoryResponseModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), categoryResponseModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProductResponseModel> call, @NonNull Throwable t) {
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


    public void loadAddBasket(final Datum datum, final int position) {
        BasketUtils.addInBasket(BasketUtils.getBasketObj(datum));
        mostPopularProductListAdapter.notifyItemChanged(position);
    }

    public void setBottomLayoutValue() {
        try {
            ArrayList<BasketModel> arrOrderList = MySharedPreferences.getGuestBasketListing();
            if (arrOrderList != null) {
                if (arrOrderList.size() > 0) {
                    double sum = 0, mainPrice = 0;
                    int sumqty = 0;
                    for (int i = 0; i < arrOrderList.size(); i++) {

                        String[] splitPrice = arrOrderList.get(i).getPrice().split("/");
                        String price = splitPrice[0];
                        String scale = splitPrice[0];
                        sum += arrOrderList.get(i).getAddedQuantity() * Double.valueOf(price);

                        sumqty += arrOrderList.get(i).getAddedQuantity();
                        /*DecimalFormat format = new DecimalFormat("0.00");
                        String formatted = format.format(sum);
                        String money = formatted.replace(',', '.');*/
                        double d = Double.valueOf(sum);
                        int r = (int) Math.round(d * 100);
                        mainPrice = r / 100.0;
                    }
                    rlBottomView.setVisibility(View.VISIBLE);
                    tvViewCart.setText(getString(R.string.view_cart));
                    txtPrice.setText(String.valueOf(mainPrice) + "MT");
                    txtQty.setText(String.valueOf(sumqty) + " " + getString(R.string.item));
                } else {
                    rlBottomView.setVisibility(View.GONE);
                }
            } else {
                rlBottomView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       // productListApi();
        rcProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        mostPopularProductListAdapter = new MostPopularProductListAdapter(getActivity(), mostpopularLst, CategoryMainActivity.this);
        rcProduct.setAdapter(mostPopularProductListAdapter);

        setBottomLayoutValue();
    }


    private void productSearchListApi(String search) {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            mostpopularLst.clear();
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
            params.put("category_id", "");
            params.put("all_product", "All product");
            params.put("product_name", search);


            Call<ProductResponseModel> call;
            call = RetrofitRestClient.getInstance().productListApi(params);

            if (call == null) return;

            call.enqueue(new Callback<ProductResponseModel>() {
                @Override
                public void onResponse(@NonNull final Call<ProductResponseModel> call, @NonNull Response<ProductResponseModel> response) {
                    try {
                        mainProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final ProductResponseModel categoryResponseModel;
                    if (response.isSuccessful()) {
                        categoryResponseModel = response.body();
                        if (Objects.requireNonNull(categoryResponseModel).getCode() == 200) {
                            try {
                                mostpopularLst.clear();
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                mostpopularLst.addAll(categoryResponseModel.getData());
                                rcProductsearch.setLayoutManager(new LinearLayoutManager(getActivity()));
                                mostPopularProductListAdapter = new MostPopularProductListAdapter(getActivity(), mostpopularLst, CategoryMainActivity.this);
                                rcProductsearch.setAdapter(mostPopularProductListAdapter);
                                mostPopularProductListAdapter.setOnItemClickLister(new MostPopularProductListAdapter.OnItemClickLister() {
                                    @Override
                                    public void itemClicked(View view, int position) {
                                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                                        intent.putExtra("productDetail", mostpopularLst.get(position));
                                        intent.putExtra("categoryName", mostpopularLst.get(position).getProductName());
                                        intent.putExtra("categoryID", mostpopularLst.get(position).getCategoryIds());
                                        startActivity(intent);
                                        AppUtils.startFromRightToLeft(getActivity());
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Objects.requireNonNull(categoryResponseModel).getCode() == 999) {
                            logout();
                        } else if (Objects.requireNonNull(categoryResponseModel).getCode() == 201) {
                            try {
                                txtNoDataFound.setText(categoryResponseModel.getMessage());
                                txtTryAgain.setVisibility(View.GONE);
                                txtNoDataFound.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showSnackBar(getActivity(), categoryResponseModel.getMessage());
                        }
                    } else {
                        showSnackBar(getActivity(), response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProductResponseModel> call, @NonNull Throwable t) {
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
}
