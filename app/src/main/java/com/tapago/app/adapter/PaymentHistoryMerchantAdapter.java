package com.tapago.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapago.app.R;
import com.tapago.app.model.PaymentMearchentModel.Datum;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;
import com.tapago.app.utils.MySharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentHistoryMerchantAdapter extends LoadMoreRecyclerAdapter<PaymentHistoryMerchantAdapter.MyViewHolder, Datum> {

    private Context context;


    public PaymentHistoryMerchantAdapter(Context activity, List<Datum> datumList) {
        super(datumList);
        this.context = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_payment_history_merchent, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Datum datum = getList().get(i);
        myViewHolder.txtEventName.setText(datum.getEventTitle());
        myViewHolder.txtAmount.setText("MT" + String.valueOf(datum.getPaymentAmount()));
        myViewHolder.txtPaymentType.setText(datum.getTransactionId());
        myViewHolder.txtPaymentDate.setText(datum.getCreateDate());

        myViewHolder.tvTransactionId.setText(MySharedPreferences.getMySharedPreferences().getTransationId());
        myViewHolder.tvPaymentDate.setText(MySharedPreferences.getMySharedPreferences().getPaymentDate());
    }


    public class MyViewHolder extends BaseRecyclerAdapter<PaymentHistoryMerchantAdapter.MyViewHolder, Datum>.ViewHolder {
        @BindView(R.id.txtEventName)
        AppCompatTextView txtEventName;
        @BindView(R.id.tvTransactionId)
        AppCompatTextView tvTransactionId;
        @BindView(R.id.txtPaymentType)
        AppCompatTextView txtPaymentType;
        @BindView(R.id.tvPaymentDate)
        AppCompatTextView tvPaymentDate;
        @BindView(R.id.txtPaymentDate)
        AppCompatTextView txtPaymentDate;
        @BindView(R.id.txtAmount)
        AppCompatTextView txtAmount;

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
