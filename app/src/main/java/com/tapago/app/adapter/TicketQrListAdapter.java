package com.tapago.app.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapago.app.R;
import com.tapago.app.activity.TicketQrCodeActivity;

import com.tapago.app.model.UserTicketQr.Datum;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;
import com.tapago.app.utils.MySharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketQrListAdapter extends LoadMoreRecyclerAdapter<TicketQrListAdapter.MyViewHolder, Datum> {

    private Context context;
    TicketQrCodeActivity ticketQrCodeActivity;

    public TicketQrListAdapter(Context context, List<Datum> voucherListUser, TicketQrCodeActivity ticketQrCodeActivity) {
        super(voucherListUser);
        context = context;
        this.ticketQrCodeActivity = ticketQrCodeActivity;
    }

    @Override
    public TicketQrListAdapter.MyViewHolder onCreateHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_voucher_qr_code, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindHolder(TicketQrListAdapter.MyViewHolder myViewHolder, int i) {
        final Datum data = getList().get(i);
        myViewHolder.txtName.setText(data.getEventTitle());
        myViewHolder.txtVoucherType.setText(data.getTicketCategoryName());
        myViewHolder.txtStartDate.setText(data.getEventStartDate());
        myViewHolder.txtStartTime.setText(data.getEventStartTime());
        myViewHolder.txtEndDate.setText(data.getEventEndDate());
        myViewHolder.txtEndTime.setText(data.getEventEndTime());
        myViewHolder.txtQuentity.setText(String.valueOf(data.getRequestedQty()));
        myViewHolder.txtStatus.setText(data.getRequestTicketStatus());
        myViewHolder.txtAmount.setText("MT" + String.valueOf(data.getTotalAmount()));
        myViewHolder.btnProcess.setText(MySharedPreferences.getMySharedPreferences().getQrCode());
        myViewHolder.txtQrCode.setText(String.valueOf(data.getTicketCode()));
        myViewHolder.tvTemp.setText("MT" + String.valueOf(data.getRemainingAmount()));
        myViewHolder.tvTemp.setVisibility(View.INVISIBLE);

        myViewHolder.tvVoucherType.setText(MySharedPreferences.getMySharedPreferences().getTicketType());
        myViewHolder.tvPrice.setText(MySharedPreferences.getMySharedPreferences().getPrice());
        myViewHolder.tvStartDate.setText(MySharedPreferences.getMySharedPreferences().getStart_Event_Date());
        myViewHolder.tvStartTime.setText(MySharedPreferences.getMySharedPreferences().getStart_Event_Time());
        myViewHolder.tvEndDate.setText(MySharedPreferences.getMySharedPreferences().getEnd_Event_Date());
        myViewHolder.tvEndTime.setText(MySharedPreferences.getMySharedPreferences().getEnd_Event_Time());
        myViewHolder.tvQuantity.setText(MySharedPreferences.getMySharedPreferences().getQuantity());
        myViewHolder.tvStatus.setText(MySharedPreferences.getMySharedPreferences().getStatus());
        myViewHolder.tvQrCode.setText(MySharedPreferences.getMySharedPreferences().getQrCode());
        myViewHolder.txtTemp.setText(MySharedPreferences.getMySharedPreferences().getRemainingAmount());
        myViewHolder.txtTemp.setVisibility(View.INVISIBLE);

        myViewHolder.btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketQrCodeActivity.openQrDialog(data.getQrcode());
            }
        });
    }

    public class MyViewHolder extends BaseRecyclerAdapter<MyViewHolder, Datum>.ViewHolder {
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
        @BindView(R.id.txtTemp)
        AppCompatTextView txtTemp;
        @BindView(R.id.tvTemp)
        AppCompatTextView tvTemp;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}