package com.tapago.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.adapter.ProductPkgActivityAdapter;
import com.tapago.app.dialog.ProductPkgDialog;
import com.tapago.app.model.BasketModel;
import com.tapago.app.model.ProductModel.Datum;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.BasketUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailActivity extends BaseActivity {

    @BindView(R.id.ivBackArrow)
    AppCompatImageView ivBackArrow;
    @BindView(R.id.txtName)
    AppCompatTextView txtName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imgProduct)
    AppCompatImageView imgProduct;
    @BindView(R.id.txtProductName)
    AppCompatTextView txtProductName;
    @BindView(R.id.txtproductPrice)
    AppCompatTextView txtproductPrice;
    @BindView(R.id.item_product_pkg)
    AppCompatTextView itemProductPkg;
    @BindView(R.id.img_expand)
    AppCompatImageView imgExpand;
    @BindView(R.id.lrProductPkg)
    LinearLayout lrProductPkg;
    @BindView(R.id.txtAboutProduct)
    AppCompatTextView txtAboutProduct;
    @BindView(R.id.txtBrandName)
    AppCompatTextView txtBrandName;
    @BindView(R.id.txtType)
    AppCompatTextView txtType;
    @BindView(R.id.txtPackQty)
    AppCompatTextView txtPackQty;
    @BindView(R.id.btnAdd)
    AppCompatButton btnAdd;
    @BindView(R.id.card_add)
    CardView cardAdd;

    Datum data = new Datum();
    String cateoryName = "", categoryId = "";
    @BindView(R.id.lrBrandName)
    LinearLayout lrBrandName;
    @BindView(R.id.lrType)
    LinearLayout lrType;
    @BindView(R.id.lrPkgQty)
    LinearLayout lrPkgQty;
    ProductPkgActivityAdapter productPkgAdapter;
    ProductPkgDialog productPkgDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        productPkgDialog = new ProductPkgDialog(getActivity());
        Intent i = getIntent();
        if (i != null) {
            data = (Datum) i.getSerializableExtra("productDetail");
            cateoryName = i.getStringExtra("categoryName");
            categoryId = i.getStringExtra("categoryID");

            txtName.setText(cateoryName + " Details");

            if (data.getMainImage() != null) {
                RequestOptions options = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder);
                Glide.with(getActivity()).load(data.getMainImage()).apply(options).into(imgProduct);
            } else {
                RequestOptions options = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder);
                Glide.with(getActivity()).load(data.getImageUrl() + data.getDefaultImage()).apply(options).into(imgProduct);
            }
            txtProductName.setText(data.getProductName());
            txtProductName.setSelected(true);
            txtAboutProduct.setText(data.getAboutProduct());
            if (data.getProductBrand() != null) {
                txtBrandName.setText(data.getProductBrand());
            } else {
                lrBrandName.setVisibility(View.GONE);
            }
            if (data.getProductType() != null) {
                txtType.setText(data.getProductType());
            } else {
                lrType.setVisibility(View.GONE);
            }
            if(data.getProductSize().size()>0) {
                //Defualt fisrt package selected
                itemProductPkg.setText(data.getProductSize().get(0).getProductWeight().toString() + " " + data.getProductSize().get(0).getProductScale());
                txtPackQty.setText(data.getProductSize().get(0).getProductWeight().toString() + " " + data.getProductSize().get(0).getProductScale());
                //Default First item price selected
                txtproductPrice.setText(data.getProductSize().get(0).getPrice().toString() + "/" + data.getProductSize().get(0).getProductWeight().toString() + " " + data.getProductSize().get(0).getProductScale());
            }
            lrProductPkg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.getProductSize().size()>0) {
                        productPkgDialog.rcList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        productPkgAdapter = new ProductPkgActivityAdapter(getActivity(), ProductDetailActivity.this, itemProductPkg.getText().toString(), data.getProductSize());
                        productPkgDialog.rcList.setAdapter(productPkgAdapter);
                        productPkgDialog.txtTitle.setText(data.getProductName());
                        productPkgDialog.show();
                    }
                }
            });
        }
    }

    @OnClick({R.id.ivBackArrow, R.id.btnAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBackArrow:
                finish();
                AppUtils.finishFromLeftToRight(getActivity());
                break;
            case R.id.btnAdd:
                break;
        }
    }

    public void setPkgQty(String scale, String weight, String price) {
        //Set Price and weight selected from dialog
        if (weight != "" && scale != "" && price != "") {
            txtproductPrice.setText( price + "/" + weight + " " + scale);
        }
        //Set weight selected from dialog
        if (weight != "" && scale != "") {
            itemProductPkg.setText(weight + " " + scale);
            txtPackQty.setText(weight + " " + scale);
        }
        productPkgDialog.dismiss();

    }

    @OnClick(R.id.btnAdd)
    public void onViewClicked() {

        ArrayList<BasketModel> arrOrderList = MySharedPreferences.getGuestBasketListing();

        boolean isMatch = false;
        if (arrOrderList != null) {
            for (int j = 0; j < arrOrderList.size(); j++) {
                if (arrOrderList.get(j).getProductId().equals(data.getId())) {
                    if (!arrOrderList.get(j).getProductScale().equals(itemProductPkg.getText().toString())) {
                        isMatch = true;
                        break;
                    }
                }
            }
        }

        if (!isMatch) {
            data.setAddedQuantity(data.getAddedQuantity() + 1);
            data.setMainQuantity(data.getMainQuantity() + 1);
            data.setProductScale(itemProductPkg.getText().toString());
            data.setPrice(txtproductPrice.getText().toString());
            data.setRepeatItem(false);
            data.setSameScale(isMatch);
            data.setCategoryId(categoryId);

        } else {
            data.setAddedQuantity(1);
            data.setMainQuantity(1);

            data.setProductScale(itemProductPkg.getText().toString());
            data.setPrice(txtproductPrice.getText().toString());
            data.setRepeatItem(false);
            data.setSameScale(isMatch);
            data.setCategoryId(categoryId);
        }

        BasketUtils.addInBasket(BasketUtils.getBasketObj(data));
        /*Intent intent = new Intent(ProductDetailActivity.this, ProductCategoryActivity.class);
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("categotyName", cateoryName);
        startActivity(intent);*/
        finish();
        AppUtils.finishFromLeftToRight(getActivity());
    }
}
