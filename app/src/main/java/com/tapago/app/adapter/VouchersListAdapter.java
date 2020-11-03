package com.tapago.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapago.app.R;
import com.tapago.app.activity.VouchersListActivity;
import com.tapago.app.model.UserVoucherList.Datum;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;
import com.tapago.app.utils.MySharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VouchersListAdapter extends LoadMoreRecyclerAdapter<VouchersListAdapter.MyViewHolder, Datum> {

    private Context context;
    VouchersListActivity vouchersListActivity;

    public VouchersListAdapter(Activity activity, List<Datum> voucherListUser, VouchersListActivity vouchersListActivity) {
        super(voucherListUser);
        context = activity;
        this.vouchersListActivity = vouchersListActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_voucherslist, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Datum data = getList().get(i);
        myViewHolder.txtName.setText(data.getEventTitle());
        myViewHolder.txtVoucherType.setText(data.getVoucherCategoryName());
        myViewHolder.txtStartDate.setText(data.getEventStartDate());
        myViewHolder.txtStartTime.setText(data.getEventStartTime());
        myViewHolder.txtEndDate.setText(data.getEventEnddateTime());
        myViewHolder.txtEndTime.setText(data.getEventEndTime());
        myViewHolder.txtQuentity.setText(String.valueOf(data.getRequestedQty()));
        myViewHolder.txtStatus.setText(data.getRequestVoucherStatus());
        myViewHolder.txtAmount.setText("MT" + String.valueOf(data.getTotalAmount()));
        if (data.getDiscountAmount().equals("")) {
            myViewHolder.txtQrCode.setText("MT0");
        } else {
            myViewHolder.txtQrCode.setText("MT" + data.getDiscountAmount());
        }

        if (data.getRequestStatus().equalsIgnoreCase("Approved")) {
            myViewHolder.btnProcess.setVisibility(View.VISIBLE);
            myViewHolder.btnProcess.setText(MySharedPreferences.getMySharedPreferences().getProceed());
        } else if (data.getRequestStatus().equalsIgnoreCase("Success")) {
            myViewHolder.btnProcess.setText(MySharedPreferences.getMySharedPreferences().getProceed());
            myViewHolder.btnProcess.setVisibility(View.GONE);
        } else {
            myViewHolder.btnProcess.setVisibility(View.GONE);
        }

        myViewHolder.tvVoucherType.setText(MySharedPreferences.getMySharedPreferences().getVoucherType());
        myViewHolder.tvPrice.setText(MySharedPreferences.getMySharedPreferences().getPrice());
        myViewHolder.tvStartDate.setText(MySharedPreferences.getMySharedPreferences().getStart_Event_Date());
        myViewHolder.tvStartTime.setText(MySharedPreferences.getMySharedPreferences().getStart_Event_Time());
        myViewHolder.tvEndDate.setText(MySharedPreferences.getMySharedPreferences().getEnd_Event_Date());
        myViewHolder.tvEndTime.setText(MySharedPreferences.getMySharedPreferences().getEnd_Event_Time());
        myViewHolder.tvQuantity.setText(MySharedPreferences.getMySharedPreferences().getQuantity());
        myViewHolder.tvStatus.setText(MySharedPreferences.getMySharedPreferences().getStatus());
        myViewHolder.tvQrCode.setText(MySharedPreferences.getMySharedPreferences().getDiscount());

        myViewHolder.btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vouchersListActivity.paymentDialog(String.valueOf(data.getId()), String.valueOf(data.getTotalAmount()), data.getDiscountAmount());
            }
        });

    }


    public class MyViewHolder extends BaseRecyclerAdapter<VouchersListAdapter.MyViewHolder, Datum>.ViewHolder {
        @BindView(R.id.txtName)
        AppCompatTextView txtName;
        @BindView(R.id.txtVoucherType)
        AppCompatTextView txtVoucherType;
        @BindView(R.id.txtStartDate)
        AppCompatTextView txtStartDate;
        @BindView(R.id.txtStartTime)
        AppCompatTextView txtStartTime;
        @BindView(R.id.txtQuantity)
        AppCompatTextView txtQuentity;
        @BindView(R.id.txtAmount)
        AppCompatTextView txtAmount;
        @BindView(R.id.txtEndDate)
        AppCompatTextView txtEndDate;
        @BindView(R.id.txtEndTime)
        AppCompatTextView txtEndTime;
        @BindView(R.id.txtStatus)
        AppCompatTextView txtStatus;
        @BindView(R.id.btnProcess)
        AppCompatButton btnProcess;
        @BindView(R.id.tvVoucherType)
        AppCompatTextView tvVoucherType;
        @BindView(R.id.tvStartDate)
        AppCompatTextView tvStartDate;
        @BindView(R.id.tvStartTime)
        AppCompatTextView tvStartTime;
        @BindView(R.id.tvQuantity)
        AppCompatTextView tvQuantity;
        @BindView(R.id.tvPrice)
        AppCompatTextView tvPrice;
        @BindView(R.id.tvEndDate)
        AppCompatTextView tvEndDate;
        @BindView(R.id.tvEndTime)
        AppCompatTextView tvEndTime;
        @BindView(R.id.tvStatus)
        AppCompatTextView tvStatus;
        @BindView(R.id.tvQrCode)
        AppCompatTextView tvQrCode;
        @BindView(R.id.txtQrCode)
        AppCompatTextView txtQrCode;


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
