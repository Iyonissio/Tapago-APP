package com.tapago.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapago.app.R;
import com.tapago.app.model.PaymentHistoryModel.Datum;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;
import com.tapago.app.utils.MySharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentHistoryAdapter extends LoadMoreRecyclerAdapter<PaymentHistoryAdapter.MyViewHolder, Datum> {

    private Context context;


    public PaymentHistoryAdapter(Context activity, List<Datum> datumList) {
        super(datumList);
        this.context = activity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_payment_history, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Datum datum = getList().get(i);
        myViewHolder.txtEventName.setText(datum.getEventTitle());
        myViewHolder.txtAmount.setText("MT" + String.valueOf(datum.getPaymentAmount()));
        myViewHolder.txtCategoryName.setText(datum.getCategory());
        myViewHolder.txtPaymentDate.setText(datum.getCreatedDate());
        myViewHolder.txtPaymentType.setText(datum.getPaymentFor());
        myViewHolder.txtQty.setText(String.valueOf(datum.getQty()));
        myViewHolder.txtDiscount.setText(String.valueOf(datum.getDiscount()));
        myViewHolder.txPaymentType.setText(datum.getPaymentType());
        myViewHolder.txtPaymentStatus.setText(datum.getPaymentStatus());

        myViewHolder.tvPaymentDate.setText(MySharedPreferences.getMySharedPreferences().getPaymentDate());
       // myViewHolder.tvPaymentType.setText(MySharedPreferences.getMySharedPreferences().getPayme());
        myViewHolder.tvQty.setText(MySharedPreferences.getMySharedPreferences().getQuantity());
        myViewHolder.tvDiscount.setText(MySharedPreferences.getMySharedPreferences().getDiscount());
    }


    public class MyViewHolder extends BaseRecyclerAdapter<PaymentHistoryAdapter.MyViewHolder, Datum>.ViewHolder {
        @BindView(R.id.txtEventName)
        AppCompatTextView txtEventName;
        @BindView(R.id.tvPaymentType)
        AppCompatTextView tvPaymentType;
        @BindView(R.id.txtPaymentType)
        AppCompatTextView txtPaymentType;
        @BindView(R.id.tvPaymentDate)
        AppCompatTextView tvPaymentDate;
        @BindView(R.id.txtPaymentDate)
        AppCompatTextView txtPaymentDate;
        @BindView(R.id.txtCategoryName)
        AppCompatTextView txtCategoryName;
        @BindView(R.id.txtAmount)
        AppCompatTextView txtAmount;
        @BindView(R.id.tvQty)
        AppCompatTextView tvQty;
        @BindView(R.id.txtQty)
        AppCompatTextView txtQty;
        @BindView(R.id.tvDiscount)
        AppCompatTextView tvDiscount;
        @BindView(R.id.txtDiscount)
        AppCompatTextView txtDiscount;
        @BindView(R.id.txPaymentType)
        AppCompatTextView txPaymentType;
        @BindView(R.id.txtPaymentStatus)
        AppCompatTextView txtPaymentStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
