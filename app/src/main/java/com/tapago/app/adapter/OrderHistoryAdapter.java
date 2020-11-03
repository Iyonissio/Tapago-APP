package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.tapago.app.R;
import com.tapago.app.model.OrderHistoryModel.Datum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private Context context;
    List<Datum> datumArrayList;

    OrderHistorySubAdapter orderHistorySubAdapter;

    public OrderHistoryAdapter(Context context, List<Datum> datumArrayList) {
        this.context = context;
        this.datumArrayList = datumArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history_main, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        final Datum data = datumArrayList.get(i);

        try {
            myViewHolder.bind(data, i);

            myViewHolder.recycleViewOrderHistory.setLayoutManager(new LinearLayoutManager(context));
            orderHistorySubAdapter = new OrderHistorySubAdapter(context, data.getProductDetails());
            myViewHolder.recycleViewOrderHistory.setAdapter(orderHistorySubAdapter);

            if (data.getPaymentDetails().getFinalAmount() != null) {
                myViewHolder.txtFinalPrice.setText(data.getPaymentDetails().getFinalAmount().toString());
            }
            if (data.getPaymentDetails().getPaymentStatus() != null) {
                if (data.getPaymentDetails().getPaymentStatus().equals("Success")) {
                    myViewHolder.txtpaymentStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
                } else {
                    myViewHolder.txtpaymentStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
            }
            if (data.getPaymentDetails().getPaymentStatus() != null) {
                myViewHolder.txtpaymentStatus.setText(data.getPaymentDetails().getPaymentStatus());
            }
            if (data.getPaymentDetails().getPaymentType() != null) {
                myViewHolder.txtpaymentType.setText(data.getPaymentDetails().getPaymentType());
            }
            String[] fullDate = data.getOrderDate().split(" ");
            String date = fullDate[0];
            String time = fullDate[1];

            myViewHolder.txtorderDate.setText(" " + date);
            myViewHolder.txtOrderId.setText(" " + data.getId().toString());

            myViewHolder.lrdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean expanded;
                    if (i == 0) {
                        expanded = data.isExpanded();
                        data.setExpanded(!expanded);
                    } else {
                        expanded = data.isExpanded();
                        data.setExpanded(!expanded);
                    }
                    // Notify the adapter that item has changed
                    notifyItemChanged(i);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return datumArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtOrderId)
        AppCompatTextView txtOrderId;
        @BindView(R.id.txtorderDate)
        AppCompatTextView txtorderDate;
        @BindView(R.id.lrdate)
        LinearLayout lrdate;
        @BindView(R.id.recycleViewOrderHistory)
        RecyclerView recycleViewOrderHistory;
        @BindView(R.id.txtpaymentType)
        AppCompatTextView txtpaymentType;
        @BindView(R.id.txtFinalPrice)
        AppCompatTextView txtFinalPrice;
        @BindView(R.id.txtpaymentStatus)
        AppCompatTextView txtpaymentStatus;
        @BindView(R.id.lrExpand)
        LinearLayout lrExpand;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(final Datum data, int i) {
            // Get the state
            boolean expanded;

            if (i == 0) {
                if (data.isExpanded()) {
                    expanded = false;
                } else {
                    expanded = true;
                }
            } else {
                expanded = data.isExpanded();
            }
            // Set the visibility based on state
            lrExpand.setVisibility(expanded ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}