package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.tapago.app.R;
import com.tapago.app.model.ShippingPaymentHistoryModel.Datum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShippingPaymentHistoryAdapter extends RecyclerView.Adapter<ShippingPaymentHistoryAdapter.MyViewHolder> {
    private Context context;
    List<Datum> datumArrayList;

    public ShippingPaymentHistoryAdapter(Context context, List<Datum> datumArrayList) {
        this.context = context;
        this.datumArrayList = datumArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipping_payment, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        final Datum data = datumArrayList.get(position);

        myViewHolder.txtpaymentDate.setText(" " + data.getPaymentDate());
        myViewHolder.txttotalAmount.setText(" MT" + data.getFinalAmount().toString());
        myViewHolder.txtTransactionId.setText(" " + data.getTransactionId());
        myViewHolder.txtOrderId.setText(" " + data.getOrderId());
        myViewHolder.txtPaymentType.setText(" " + data.getPaymentType());
        myViewHolder.txtPaymentStatus.setText(" " + data.getPaymentStatus());
    }

    @Override
    public int getItemCount() {
        return datumArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtTransactionId)
        AppCompatTextView txtTransactionId;
        @BindView(R.id.txtpaymentDate)
        AppCompatTextView txtpaymentDate;
        @BindView(R.id.txttotalAmount)
        AppCompatTextView txttotalAmount;
        @BindView(R.id.txtOrderId)
        AppCompatTextView txtOrderId;
        @BindView(R.id.txtPaymentType)
        AppCompatTextView txtPaymentType;
        @BindView(R.id.txtPaymentStatus)
        AppCompatTextView txtPaymentStatus;

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