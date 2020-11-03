package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.activity.CartActivity;
import com.tapago.app.dialog.ProductPkgDialog;
import com.tapago.app.model.BasketModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemViewCartAdapter extends RecyclerView.Adapter<ItemViewCartAdapter.MyViewHolder> {


    private List<BasketModel> basketList;
    //  AdapterItemList adapter;
    private CartActivity cartActivity;
    private Context context;
    ProductPkgDialog productPkgDialog;
    CartProductPkgAdapter cartProductPkgAdapter;
    String scale = "", weight = "", pkgPrice = "";
    int pos = -1;

    public ItemViewCartAdapter(Context activity, List<BasketModel> basketList, CartActivity cartActivity) {
        context = activity;
        this.basketList = basketList;
        this.cartActivity = cartActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_product_cart, viewGroup, false);
        productPkgDialog = new ProductPkgDialog(context);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        final BasketModel basketModel = basketList.get(i);
        try {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder);
            if (basketModel.getMainImage() != null) {
                Glide.with(context).load(basketModel.getMainImage()).apply(options).into(myViewHolder.imgProduct);
            } else {
                Glide.with(context).load(basketModel.getImageUrl() + basketModel.getDefaultImage()).apply(options).into(myViewHolder.imgProduct);
            }
            myViewHolder.txtProductName.setText(basketModel.getProductName());
            //Defualt fisrt package and price selected
            myViewHolder.itemProductPkg.setText(basketModel.getProductScale());
            myViewHolder.txtproductPrice.setText(basketModel.getPrice());
            myViewHolder.count.setText(String.valueOf(basketModel.getAddedQuantity()));

            //Set Price and weight selected from dialog
            if (i == pos && weight != "" && scale != "" && pkgPrice != "") {
                myViewHolder.txtproductPrice.setText(pkgPrice + "/" + weight + " " + scale);
            }
            //Set weight selected from dialog
            if (i == pos && weight != "" && scale != "") {
                myViewHolder.itemProductPkg.setText(weight + " " + scale);
            }
            myViewHolder.lrProductPkg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productPkgDialog.rcList.setLayoutManager(new LinearLayoutManager(context));
                    cartProductPkgAdapter = new CartProductPkgAdapter(context, ItemViewCartAdapter.this, myViewHolder.itemProductPkg.getText().toString(), basketModel.getProductSizeList(), i);
                    productPkgDialog.rcList.setAdapter(cartProductPkgAdapter);
                    productPkgDialog.txtTitle.setText(basketModel.getProductName());
                    productPkgDialog.show();
                }
            });


            myViewHolder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentNos = Integer.parseInt(myViewHolder.count.getText().toString());
                    if (currentNos > 1) {
                        myViewHolder.count.setText(String.valueOf(--currentNos));
                        basketModel.setAddedQuantity(basketModel.getAddedQuantity() - 1);
                        basketModel.setMainQuantity(basketModel.getMainQuantity() - 1);
                       /* basketModel.setPrice(myViewHolder.txtproductPrice.getText().toString());
                        basketModel.setProductScale(myViewHolder.itemProductPkg.getText().toString());*/
                    } else {
                        basketModel.setAddedQuantity(0);
                        basketModel.setMainQuantity(0);
                        basketModel.setPrice("0");
                    }
                    cartActivity.loadAddBasket(basketModel, i);
                    cartActivity.setBottomLayoutValue();
                }
            });

            myViewHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (basketModel.getAddedQuantity() < 15) {
                        basketModel.setAddedQuantity(basketModel.getAddedQuantity() + 1);
                        basketModel.setMainQuantity(basketModel.getMainQuantity() + 1);
                        myViewHolder.count.setText(String.valueOf(basketModel.getAddedQuantity()));
                        cartActivity.loadAddBasket(basketModel, i);
                        cartActivity.setBottomLayoutValue();
                    } else {
                        Toast.makeText(context, context.getString(R.string.maxqty), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return basketList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.minus)
        AppCompatImageView minus;
        @BindView(R.id.count)
        AppCompatTextView count;
        @BindView(R.id.plus)
        AppCompatImageView plus;
        @BindView(R.id.linearCount)
        LinearLayout linearCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setPkgQty(int pos, String scale, String weight, String price) {
        this.scale = scale;
        this.weight = weight;
        this.pos = pos;
        this.pkgPrice = price;
        productPkgDialog.dismiss();
        notifyItemChanged(pos);
    }
}
