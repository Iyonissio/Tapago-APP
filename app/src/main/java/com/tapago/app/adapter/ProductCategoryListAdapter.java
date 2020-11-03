package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.activity.ProductCategoryActivity;
import com.tapago.app.dialog.ProductPkgDialog;
import com.tapago.app.model.BasketModel;
import com.tapago.app.model.ProductModel.Datum;
import com.tapago.app.utils.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductCategoryListAdapter extends RecyclerView.Adapter<ProductCategoryListAdapter.MyViewHolder> {

    private Context context;
    private OnItemClickLister onItemClickLister = null;
    ProductPkgDialog productPkgDialog;
    ProductPkgAdapter productPkgAdapter;
    String scale = "", weight = "", pkgPrice = "", categoryId = "";
    List<Datum> productList;
    int pos = -1;
    ProductCategoryActivity productCategoryActivity;


    public ProductCategoryListAdapter(Activity activity, List<Datum> productList, ProductCategoryActivity productCategoryActivity, String categoryId) {
        this.context = activity;
        this.productList = productList;
        this.productCategoryActivity = productCategoryActivity;
        this.categoryId = categoryId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_category, viewGroup, false);
        productPkgDialog = new ProductPkgDialog(context);
        return new MyViewHolder(itemView);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        final Datum data = productList.get(i);
        try {
            // Check is Already Item Added or Not
            final boolean isAlreadyOrder = CartRowDisplay(data.getId(), data, myViewHolder.count, myViewHolder.btnAdd, myViewHolder.linearCount,myViewHolder.itemProductPkg,myViewHolder.txtproductPrice);
            // Count and Price Row is Visible or Not
            if (isAlreadyOrder) {
                myViewHolder.btnAdd.setVisibility(View.GONE);
                myViewHolder.linearCount.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.btnAdd.setVisibility(View.VISIBLE);
                myViewHolder.linearCount.setVisibility(View.GONE);
                //Set Price and weight selected from dialog
                if (i == pos && weight != "" && scale != "" && pkgPrice != "") {
                    myViewHolder.txtproductPrice.setText(pkgPrice + "/" + weight + " " + scale);
                }
                //Set weight selected from dialog
                if (i == pos && weight != "" && scale != "") {
                    myViewHolder.itemProductPkg.setText(weight + " " + scale);
                }
                if (data.getProductSize().size() > 0) {
                    //Defualt fisrt package and price selected
                    myViewHolder.itemProductPkg.setText(data.getProductSize().get(0).getProductWeight().toString() + " " + data.getProductSize().get(0).getProductScale());
                    myViewHolder.txtproductPrice.setText(data.getProductSize().get(0).getPrice().toString() + "/" + data.getProductSize().get(0).getProductWeight().toString() + " " + data.getProductSize().get(0).getProductScale());
                }
            }
            myViewHolder.count.setClickable(false);
            myViewHolder.count.setEnabled(false);
            myViewHolder.count.setFocusable(false);
            myViewHolder.linearCount.setClickable(false);
            myViewHolder.linearCount.setEnabled(false);
            myViewHolder.linearCount.setFocusable(false);
            myViewHolder.txtProductName.setText(data.getProductName());
            myViewHolder.txtProductName.setSelected(true);

            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder);
            if (data.getMainImage() != null) {
                Glide.with(context).load(data.getMainImage()).apply(options).into(myViewHolder.imgProduct);
            } else {
                Glide.with(context).load(data.getImageUrl() + data.getDefaultImage()).apply(options).into(myViewHolder.imgProduct);
            }

            myViewHolder.lrProductPkg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getProductSize().size() > 0) {
                        productPkgDialog.rcList.setLayoutManager(new LinearLayoutManager(context));
                        productPkgAdapter = new ProductPkgAdapter(context, ProductCategoryListAdapter.this, myViewHolder.itemProductPkg.getText().toString(), data.getProductSize(), i,data,productCategoryActivity);
                        productPkgDialog.rcList.setAdapter(productPkgAdapter);
                        productPkgDialog.txtTitle.setText(data.getProductName());
                        productPkgDialog.show();
                    }
                }
            });

            myViewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<BasketModel> arrOrderList = MySharedPreferences.getGuestBasketListing();
                    if (arrOrderList != null) {
                        if (arrOrderList.size() > 0) {
                            myViewHolder.btnAdd.setVisibility(View.GONE);
                            myViewHolder.linearCount.setVisibility(View.VISIBLE);
                            data.setAddedQuantity(data.getAddedQuantity() + 1);
                            data.setMainQuantity(data.getMainQuantity() + 1);
                            data.setCategoryId(categoryId);
                            data.setProductScale(myViewHolder.itemProductPkg.getText().toString());
                            data.setPrice(myViewHolder.txtproductPrice.getText().toString());
                            //product.setMenuPrices(product.getMenuPrice());
                            data.setRepeatItem(false);
                            data.setMinus(false);
                            myViewHolder.count.setText(String.valueOf(data.getAddedQuantity()));
                            productCategoryActivity.loadAddBasket(data, i);
                            productCategoryActivity.setBottomLayoutValue();
                        } else {
                            myViewHolder.btnAdd.setVisibility(View.GONE);
                            myViewHolder.linearCount.setVisibility(View.VISIBLE);
                            data.setAddedQuantity(data.getAddedQuantity() + 1);
                            data.setMainQuantity(data.getMainQuantity() + 1);
                            data.setCategoryId(categoryId);
                            data.setProductScale(myViewHolder.itemProductPkg.getText().toString());
                            data.setPrice(myViewHolder.txtproductPrice.getText().toString());
                            //product.setMenuPrices(product.getMenuPrice());
                            data.setRepeatItem(false);
                            data.setMinus(false);
                            myViewHolder.count.setText(String.valueOf(data.getAddedQuantity()));
                            productCategoryActivity.loadAddBasket(data, i);
                            productCategoryActivity.setBottomLayoutValue();
                        }
                    } else {
                        myViewHolder.btnAdd.setVisibility(View.GONE);
                        myViewHolder.linearCount.setVisibility(View.VISIBLE);
                        data.setAddedQuantity(data.getAddedQuantity() + 1);
                        data.setMainQuantity(data.getMainQuantity() + 1);
                        data.setCategoryId(categoryId);
                        data.setProductScale(myViewHolder.itemProductPkg.getText().toString());
                        data.setPrice(myViewHolder.txtproductPrice.getText().toString());
                        //product.setMenuPrices(product.getMenuPrice());
                        data.setRepeatItem(false);
                        data.setMinus(false);
                        myViewHolder.count.setText(String.valueOf(data.getAddedQuantity()));
                        productCategoryActivity.loadAddBasket(data, i);
                        productCategoryActivity.setBottomLayoutValue();
                    }
                }
            });

            myViewHolder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentNos = Integer.parseInt(myViewHolder.count.getText().toString());
                    if (currentNos > 1) {
                        myViewHolder.count.setText(String.valueOf(--currentNos));
                        data.setRepeatItem(false);
                        data.setMinus(true);
                        data.setPrice(myViewHolder.txtproductPrice.getText().toString());
                        data.setProductScale(myViewHolder.itemProductPkg.getText().toString());
                        data.setCategoryId(categoryId);
                    } else {
                        myViewHolder.btnAdd.setVisibility(View.VISIBLE);
                        myViewHolder.linearCount.setVisibility(View.GONE);
                        data.setRepeatItem(false);
                        data.setMinus(true);
                        data.setAddedQuantity(0);
                        data.setProductScale(myViewHolder.itemProductPkg.getText().toString());
                        data.setMainQuantity(0);
                        data.setPrice("0");
                    }
                    productCategoryActivity.loadAddBasket(data, i);
                    productCategoryActivity.setBottomLayoutValue();
                }
            });


            myViewHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getAddedQuantity() < 15) {
                        data.setAddedQuantity(data.getAddedQuantity() + 1);
                        data.setMainQuantity(data.getMainQuantity() + 1);
                        myViewHolder.count.setText(String.valueOf(data.getAddedQuantity()));
                        data.setProductScale(myViewHolder.itemProductPkg.getText().toString());
                        data.setPrice(myViewHolder.txtproductPrice.getText().toString());
                        data.setRepeatItem(true);
                        data.setMinus(false);
                        data.setSameScale(false);
                        data.setCategoryId(categoryId);
                        productCategoryActivity.loadAddBasket(data, i);
                        productCategoryActivity.setBottomLayoutValue();
                    } else {
                        Toast.makeText(context, context.getString(R.string.maxqty), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnItemClickLister {
        void itemClicked(View view, int position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        @BindView(R.id.btnAdd)
        AppCompatButton btnAdd;
        @BindView(R.id.minus)
        AppCompatImageView minus;
        @BindView(R.id.plus)
        AppCompatImageView plus;
        @BindView(R.id.count)
        AppCompatTextView count;
        @BindView(R.id.linearCount)
        LinearLayout linearCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickLister != null) {
                onItemClickLister.itemClicked(v, getAdapterPosition());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setPkgQty(int pos, String scale, String weight, String price) {
        this.scale = scale;
        this.weight = weight;
        this.pos = pos;
        this.pkgPrice = price;
        productPkgDialog.dismiss();
        notifyItemChanged(pos);
    }

    /***
     * Function To Check Already Menu Item Previously Added or Not
     */
    private boolean CartRowDisplay(int productid, Datum product, TextView qtyText, Button btnAdd, LinearLayout linearCount, AppCompatTextView itemProductPkg, AppCompatTextView txtproductPrice) {
        /** Get Global arrOrderList Array */
        ArrayList<BasketModel> arrOrderList = MySharedPreferences.getGuestBasketListing();
        boolean isMatch = false;

        /** arrOrderList Array Size Check */
        if (arrOrderList != null) {
            if (arrOrderList.size() > 0) {
                int sumqty = 0;
                for (int i = 0; i < arrOrderList.size(); i++) {
                    /** Compare Product*/
                    if (arrOrderList.get(i).getProductId() == productid) {
                        isMatch = true;
                        if (arrOrderList.get(i).getAddedQuantity() != 0) {
                            btnAdd.setVisibility(View.GONE);
                            linearCount.setVisibility(View.VISIBLE);
                            sumqty += arrOrderList.get(i).getAddedQuantity();
                            qtyText.setText(String.valueOf(sumqty));
                            product.setPrice(arrOrderList.get(i).getPrice());
                            product.setAddedQuantity(arrOrderList.get(i).getAddedQuantity());
                            product.setMainQuantity(arrOrderList.get(i).getMainQuantity());
                            itemProductPkg.setText(arrOrderList.get(i).getProductScale());
                            txtproductPrice.setText(arrOrderList.get(i).getPrice());
                        }
                    }
                }
            }
        }
        return isMatch;
    }

}
