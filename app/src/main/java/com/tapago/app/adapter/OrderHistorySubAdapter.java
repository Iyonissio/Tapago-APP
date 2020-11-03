package com.tapago.app.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.model.OrderHistoryModel.ProductDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistorySubAdapter extends RecyclerView.Adapter<OrderHistorySubAdapter.MyViewHolder> {


    private Context context;
    List<ProductDetail> datumArrayList;

    public OrderHistorySubAdapter(Context context, List<ProductDetail> datumArrayList) {
        this.context = context;
        this.datumArrayList = datumArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subproduct_order_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        final ProductDetail data = datumArrayList.get(position);

        try {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder);
            Glide.with(context).load(data.getMainImage()).apply(options).into(myViewHolder.imgProduct);
            myViewHolder.txtProductName.setText(data.getProductName());
            myViewHolder.txtproductPrice.setText(data.getProductPrice().toString() + "/" + data.getProductWeight().toString());
            myViewHolder.txtpurchaseqty.setText(" " + data.getPurchaseQty());
            myViewHolder.txttotalpurchasePrice.setText(data.getTotalPrice().toString());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return datumArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgProduct)
        AppCompatImageView imgProduct;
        @BindView(R.id.txtProductName)
        AppCompatTextView txtProductName;
        @BindView(R.id.txtproductPrice)
        AppCompatTextView txtproductPrice;
        @BindView(R.id.txtpurchaseqty)
        AppCompatTextView txtpurchaseqty;
        @BindView(R.id.txttotalpurchasePrice)
        AppCompatTextView txttotalpurchasePrice;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}