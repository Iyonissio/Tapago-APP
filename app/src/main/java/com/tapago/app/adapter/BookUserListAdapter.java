package com.tapago.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapago.app.R;
import com.tapago.app.activity.PaymentActivity;
import com.tapago.app.activity.TicketListActivity;
import com.tapago.app.model.BookTicketUserModel.Datum;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;
import com.tapago.app.utils.MySharedPreferences;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookUserListAdapter extends LoadMoreRecyclerAdapter<BookUserListAdapter.MyViewHolder, Datum> {

    private Context context;
    TicketListActivity ticketListActivity;

    public BookUserListAdapter(Activity activity, List<Datum> ticketListUser, TicketListActivity ticketListActivity) {
        super(ticketListUser);
        context = activity;
        this.ticketListActivity = ticketListActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ticketlist, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindHolder(@NonNull MyViewHolder myViewHolder, int i) {
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
        myViewHolder.txtQrCode.setText(data.getTicketCode());
        if (data.getDiscountAmount().equals("")) {
            myViewHolder.txtTemp.setText("MT0");
        } else {
            myViewHolder.txtTemp.setText("MT" + data.getDiscountAmount());
        }
        if (data.getRequestStatus().equalsIgnoreCase("Approved")) {
            myViewHolder.btnProcess.setVisibility(View.VISIBLE);
            myViewHolder.btnProcess.setText(MySharedPreferences.getMySharedPreferences().getProceed());
            myViewHolder.tvQrCode.setText("");
          /*  myViewHolder.txtQrCode.setVisibility(View.GONE);
            myViewHolder.tvQrCode.setVisibility(View.GONE);
            myViewHolder.tvTemp.setVisibility(View.GONE);
            myViewHolder.txtTemp.setVisibility(View.GONE);*/
        } else if (data.getRequestStatus().equalsIgnoreCase("Success")) {
            myViewHolder.btnProcess.setText(MySharedPreferences.getMySharedPreferences().getQrCode());
            myViewHolder.btnProcess.setVisibility(View.GONE);
            myViewHolder.tvQrCode.setText(MySharedPreferences.getMySharedPreferences().getQrCode());
          /*  myViewHolder.txtQrCode.setVisibility(View.VISIBLE);
            myViewHolder.tvQrCode.setVisibility(View.VISIBLE);
            myViewHolder.tvTemp.setVisibility(View.VISIBLE);
            myViewHolder.txtTemp.setVisibility(View.VISIBLE);*/
        } else {
            myViewHolder.btnProcess.setVisibility(View.GONE);
            myViewHolder.tvQrCode.setText("");
        }

        myViewHolder.tvVoucherType.setText(MySharedPreferences.getMySharedPreferences().getTicketType());
        myViewHolder.tvPrice.setText(MySharedPreferences.getMySharedPreferences().getPrice());
        myViewHolder.tvStartDate.setText(MySharedPreferences.getMySharedPreferences().getStart_Event_Date());
        myViewHolder.tvStartTime.setText(MySharedPreferences.getMySharedPreferences().getStart_Event_Time());
        myViewHolder.tvEndDate.setText(MySharedPreferences.getMySharedPreferences().getEnd_Event_Date());
        myViewHolder.tvEndTime.setText(MySharedPreferences.getMySharedPreferences().getEnd_Event_Time());
        myViewHolder.tvQuantity.setText(MySharedPreferences.getMySharedPreferences().getQuantity());
        myViewHolder.tvStatus.setText(MySharedPreferences.getMySharedPreferences().getStatus());
        myViewHolder.tvTemp.setText(MySharedPreferences.getMySharedPreferences().getDiscount());


        myViewHolder.btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getRequestStatus().equalsIgnoreCase("Success")) {
                    ticketListActivity.openQrDialog(data.getQrCode());
                } else {
                    //ticketListActivity.brainTreeTokenApi(String.valueOf(data.getId()), String.valueOf(data.getTotalAmount()),data.getDiscountAmount());
                    new AlertDialog.Builder(Objects.requireNonNull(context))
                            .setMessage(R.string.are_sure_ticket)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Intent intent = new Intent(context, PaymentActivity.class);
                                    intent.putExtra("requestId", String.valueOf(data.getId()));
                                    intent.putExtra("amount", String.valueOf(data.getTotalAmount()));
                                    intent.putExtra("discount", data.getDiscountAmount());
                                    intent.putExtra("type", "Ticket");
                                    context.startActivity(intent);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();

                }
            }
        });
    }


    public class MyViewHolder extends BaseRecyclerAdapter<BookUserListAdapter.MyViewHolder, Datum>.ViewHolder {
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
