package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tapago.app.R;
import com.tapago.app.model.RedeemModel.Datum;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;
import com.tapago.app.utils.MySharedPreferences;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RedeemQrAdapterVendor extends LoadMoreRecyclerAdapter<RedeemQrAdapterVendor.MyViewHolder, Datum> {

    private Context context;
    private OnItemClickLister onItemClickLister = null;

    public RedeemQrAdapterVendor(Context activity, List<Datum> datumList) {
        super(datumList);
        this.context = activity;
    }

    @NonNull
    @Override
    public RedeemQrAdapterVendor.MyViewHolder onCreateHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_redeem_qr_code_history, viewGroup, false);
        return new RedeemQrAdapterVendor.MyViewHolder(itemView);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHolder(@NonNull RedeemQrAdapterVendor.MyViewHolder myViewHolder, int i) {
        Datum datum = getList().get(i);
        myViewHolder.txtName.setText(datum.getEventTitle());
        myViewHolder.txtQuantity.setText(String.valueOf(datum.getRequestedQty()));
        myViewHolder.txtRedeemDate.setText(datum.getRedeemDate());
        myViewHolder.txtUserName.setText(datum.getUserName());
        myViewHolder.txtVoucherType.setText(datum.getTyp());
        myViewHolder.txtQuantity.setText(datum.getVoucherCode());
        myViewHolder.txtAmount.setText("MT" + String.valueOf(datum.getRedeemAmount()));

       /* if (datum.getTyp().equalsIgnoreCase("voucher")){
            myViewHolder.tvQuantity.setVisibility(View.GONE);
            myViewHolder.txtQuantity.setVisibility(View.GONE);
        }else {
            myViewHolder.tvQuantity.setVisibility(View.GONE);
            myViewHolder.txtQuantity.setVisibility(View.GONE);
        }*/

        myViewHolder.tvQuantity.setText(MySharedPreferences.getMySharedPreferences().getQuantity());
        myViewHolder.tvRedeemDate.setText(MySharedPreferences.getMySharedPreferences().getRedeemDateTime());
        myViewHolder.tvUserName.setText(MySharedPreferences.getMySharedPreferences().getUserName());
        myViewHolder.tvQuantity.setText(MySharedPreferences.getMySharedPreferences().getQrCode());
        myViewHolder.tvAmount.setText(MySharedPreferences.getMySharedPreferences().getAmount());
    }

    public interface OnItemClickLister {
        void itemClicked(View view, int position);
    }

    public class MyViewHolder extends BaseRecyclerAdapter<RedeemQrAdapterVendor.MyViewHolder, Datum>.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtName)
        AppCompatTextView txtName;
        @BindView(R.id.tvVoucherType)
        AppCompatTextView tvVoucherType;
        @BindView(R.id.txtVoucherType)
        AppCompatTextView txtVoucherType;
        @BindView(R.id.tvRedeemDate)
        AppCompatTextView tvRedeemDate;
        @BindView(R.id.txtRedeemDate)
        AppCompatTextView txtRedeemDate;
        @BindView(R.id.tvQuantity)
        AppCompatTextView tvQuantity;
        @BindView(R.id.txtQuantity)
        AppCompatTextView txtQuantity;
        @BindView(R.id.tvUserName)
        AppCompatTextView tvUserName;
        @BindView(R.id.txtUserName)
        AppCompatTextView txtUserName;
        @BindView(R.id.txtAmount)
        AppCompatTextView txtAmount;
        @BindView(R.id.tvAmount)
        AppCompatTextView tvAmount;

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
        return super.getItemViewType(position);
    }
}
