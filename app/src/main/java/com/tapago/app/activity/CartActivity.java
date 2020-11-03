package com.tapago.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tapago.app.R;
import com.tapago.app.adapter.ItemViewCartAdapter;
import com.tapago.app.model.BasketModel;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.BasketUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.recycleViewCart)
    RecyclerView recycleViewCart;
    @BindView(R.id.main_progress)
    ProgressBar mainProgress;
    @BindView(R.id.txtNoDataFound)
    AppCompatTextView txtNoDataFound;
    @BindView(R.id.txtTryAgain)
    AppCompatTextView txtTryAgain;
    @BindView(R.id.txtQty)
    AppCompatTextView txtQty;
    @BindView(R.id.views)
    View views;
    @BindView(R.id.txtPrice)
    AppCompatTextView txtPrice;
    @BindView(R.id.tv_checkout)
    AppCompatTextView tvCheckout;
    @BindView(R.id.rlCart)
    RelativeLayout rlCart;

    ItemViewCartAdapter itemCartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        txtNoDataFound.setText(R.string.item_not_found);

        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ChooseAddressActivity.class);
                startActivity(intent);
                AppUtils.startFromRightToLeft(getActivity());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setBottomLayoutValue();
    }

    @SuppressLint("SetTextI18n")
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
                    rlCart.setVisibility(View.VISIBLE);
                    tvCheckout.setText(getString(R.string.checkout));
                    txtPrice.setText(String.valueOf(mainPrice)+"MT");
                    txtQty.setText(String.valueOf(sumqty) + " " + getString(R.string.item));

                    recycleViewCart.setLayoutManager(new LinearLayoutManager(getActivity()));
                    itemCartAdapter = new ItemViewCartAdapter(CartActivity.this, arrOrderList, CartActivity.this);
                    recycleViewCart.setAdapter(itemCartAdapter);
                } else {
                    rlCart.setVisibility(View.GONE);
                    recycleViewCart.setVisibility(View.GONE);
                    txtNoDataFound.setVisibility(View.VISIBLE);
                }
            } else {
                recycleViewCart.setVisibility(View.GONE);
                rlCart.setVisibility(View.GONE);
                txtNoDataFound.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAddBasket(final BasketModel productDetailItem, final int position) {
        BasketUtils.addCart(BasketUtils.getCartObj(productDetailItem));
        itemCartAdapter.notifyItemChanged(position);
    }

    @OnClick(R.id.ivBackArrow)
    public void onViewClicked() {
        finish();
        AppUtils.finishFromLeftToRight(getActivity());
    }
}
