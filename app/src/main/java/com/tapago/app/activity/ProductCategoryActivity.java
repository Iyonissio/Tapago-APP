package com.tapago.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tapago.app.R;
import com.tapago.app.adapter.ProductCategoryListAdapter;
import com.tapago.app.adapter.ProductPkgAdapter;
import com.tapago.app.dialog.ProductPkgDialog;
import com.tapago.app.model.BasketModel;
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

public class ProductCategoryActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycleViewcategory)
    RecyclerView recycleViewcategory;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;

    ProductCategoryListAdapter productCategoryListAdapter;
    ProductPkgDialog productPkgDialog;
    ProductPkgAdapter productPkgAdapter;
    List<Datum> productList = new ArrayList<>();

    String categoryId = "", categoryName = "";
    @BindView(R.id.txtQty)
    AppCompatTextView txtbottomQty;
    @BindView(R.id.views)
    View views;
    @BindView(R.id.txtPrice)
    AppCompatTextView txtbottomPrice;
    @BindView(R.id.tv_view_cart)
    AppCompatTextView tvbottmViewCart;
    @BindView(R.id.rlBottomView)
    RelativeLayout rlBottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        ButterKnife.bind(this);
        Intent i = getIntent();

        if (i != null) {
            categoryId = i.getStringExtra("categoryId");
            categoryName = i.getStringExtra("categotyName");
        }

        productPkgDialog = new ProductPkgDialog(ProductCategoryActivity.this);
        txtNoDataFound.setText(R.string.product_not_found);
        txtName.setText(categoryName);
        rlBottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ProductCategoryActivity.this, CartActivity.class);
                    startActivity(intent);
                    AppUtils.startFromRightToLeft(ProductCategoryActivity.this);
            }
        });

        productListApi();
    }

    @OnClick({R.id.ivBackArrow, R.id.txtTryAgain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.txtTryAgain:
                productListApi();
                break;
        }
    }

    /*public void productPkgDialog(){
        productPkgDialog.rcList.setLayoutManager(new LinearLayoutManager(getActivity()));
        productPkgAdapter = new ProductPkgAdapter(ProductCategoryActivity.this,ProductCategoryActivity.this);
        productPkgDialog.rcList.setAdapter(productPkgAdapter);
        productPkgDialog.show();
    }*/

    private void productListApi() {
        if (AppUtils.isConnectedToInternet(getActivity())) {
            productList.clear();
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
            params.put("category_id", categoryId);

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
                                txtNoDataFound.setVisibility(View.GONE);
                                txtTryAgain.setVisibility(View.GONE);
                                productList.addAll(categoryResponseModel.getData());
                                recycleViewcategory.setLayoutManager(new LinearLayoutManager(getActivity()));
                                productCategoryListAdapter = new ProductCategoryListAdapter(getActivity(), productList, ProductCategoryActivity.this,categoryId);
                                recycleViewcategory.setAdapter(productCategoryListAdapter);
                                productCategoryListAdapter.setOnItemClickLister(new ProductCategoryListAdapter.OnItemClickLister() {
                                    @Override
                                    public void itemClicked(View view, int position) {
                                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                                        intent.putExtra("productDetail", productList.get(position));
                                        intent.putExtra("categoryName", categoryName);
                                        intent.putExtra("categoryID", categoryId);
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


    public void loadAddBasket(final Datum productDetailItem, final int position) {
        BasketUtils.addInBasket(BasketUtils.getBasketObj(productDetailItem));
        productCategoryListAdapter.notifyItemChanged(position);
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
                        String price =splitPrice[0];
                        String scale =splitPrice[0];
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
                    tvbottmViewCart.setText(getString(R.string.view_cart));
                    txtbottomPrice.setText(String.valueOf(mainPrice)+"MT");
                    txtbottomQty.setText(String.valueOf(sumqty) + " " + getString(R.string.item));
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
        setBottomLayoutValue();
    }
}
