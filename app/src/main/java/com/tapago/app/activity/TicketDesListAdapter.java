package com.tapago.app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapago.app.R;

import com.tapago.app.model.TicketListModel.Datum;
import com.tapago.app.utils.MySharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketDesListAdapter extends RecyclerView.Adapter<TicketDesListAdapter.MyViewHolder> {


    private Context context;
    private List<Datum> datumList;
    private OnItemClickLister onItemClickLister = null;


    public TicketDesListAdapter(Context activity, List<Datum> datumList) {
        this.context = activity;
        this.datumList = datumList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_voucher, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Datum datum = datumList.get(i);

        myViewHolder.txtQty.setText(MySharedPreferences.getMySharedPreferences().getQuantity() + " :");
        myViewHolder.txAmount.setText(MySharedPreferences.getMySharedPreferences().getAmount() + " :");
        myViewHolder.txTotal.setText(MySharedPreferences.getMySharedPreferences().getTotal() + " :");
        myViewHolder.tvDiscount.setText(MySharedPreferences.getMySharedPreferences().getDiscount() + " :");

        myViewHolder.txtVoucher.setText(datum.getTicketCategoryName());
        myViewHolder.txtQty.setText(String.valueOf(datum.getNoOfTicket()));
        myViewHolder.txtAmount.setText("MT" + String.valueOf(datum.getTicketPrice()));
        myViewHolder.txtTotal.setText("MT" + String.valueOf(datum.getTotalPrice()));
        myViewHolder.txtDiscount.setText(String.valueOf(datum.getTicketDiscount()));
    }

    public interface OnItemClickLister {
        void itemClicked(View view, int position);
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtVoucher)
        AppCompatTextView txtVoucher;
        @BindView(R.id.txtQty)
        AppCompatTextView txtQty;
        @BindView(R.id.txtAmount)
        AppCompatTextView txtAmount;
        @BindView(R.id.txtTotal)
        AppCompatTextView txtTotal;
        @BindView(R.id.txQty)
        AppCompatTextView txQty;
        @BindView(R.id.txAmount)
        AppCompatTextView txAmount;
        @BindView(R.id.txTotal)
        AppCompatTextView txTotal;
        @BindView(R.id.tvDiscount)
        AppCompatTextView tvDiscount;
        @BindView(R.id.txtDiscount)
        AppCompatTextView txtDiscount;

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
}
