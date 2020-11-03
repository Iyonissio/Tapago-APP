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
import com.tapago.app.activity.MerchantVouchersListActivity;
import com.tapago.app.model.VoucherListMurchantModel.Datum;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;
import com.tapago.app.utils.MySharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MerchantVouchersListAdapter extends LoadMoreRecyclerAdapter<MerchantVouchersListAdapter.MyViewHolder, Datum> {


    private Context context;
    private MerchantVouchersListActivity merchantVouchersListActivity;

    public MerchantVouchersListAdapter(Activity activity, List<Datum> voucherListMerchantModel, MerchantVouchersListActivity merchantVouchersListActivity) {
        super(voucherListMerchantModel);
        context = activity;
        this.merchantVouchersListActivity = merchantVouchersListActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_murchant_voucher_list, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Datum datum = getList().get(i);
        myViewHolder.txtName.setText(datum.getEventTitle());
        myViewHolder.txtVoucherType.setText(datum.getVoucherCategoryName());
        myViewHolder.txtAmount.setText("MT" + String.valueOf(datum.getTotalAmount()));
        myViewHolder.txtStartDate.setText(datum.getEventStartDate());
        myViewHolder.txtStartTime.setText(datum.getEventStartTime());
        myViewHolder.txtEndDate.setText(datum.getEventEndDate());
        myViewHolder.txtEndTime.setText(datum.getEventEndTime());
        myViewHolder.txtQuantity.setText(String.valueOf(datum.getRequestedQty()));
        if (datum.getDiscountAmount().equals("")) {
            myViewHolder.txtDiscount.setText("MT0");
        } else {
            myViewHolder.txtDiscount.setText("MT" + datum.getDiscountAmount());
        }
        myViewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merchantVouchersListActivity.approveDisapproveApi(String.valueOf(datum.getId()), "Y");
            }
        });

        myViewHolder.btnDisapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merchantVouchersListActivity.approveDisapproveApi(String.valueOf(datum.getId()), "N");
            }
        });
        myViewHolder.txtUserName.setText(datum.getFirstName() + " " + datum.getLastName());

        myViewHolder.tvVoucherType.setText(MySharedPreferences.getMySharedPreferences().getVoucherType());
        myViewHolder.tvPrice.setText(MySharedPreferences.getMySharedPreferences().getPrice());
        myViewHolder.tvStartDate.setText(MySharedPreferences.getMySharedPreferences().getStart_Event_Date());
        myViewHolder.tvStartTime.setText(MySharedPreferences.getMySharedPreferences().getStart_Event_Time());
        myViewHolder.tvEndDate.setText(MySharedPreferences.getMySharedPreferences().getEnd_Event_Date());
        myViewHolder.tvEndTime.setText(MySharedPreferences.getMySharedPreferences().getEnd_Event_Time());
        myViewHolder.tvQuantity.setText(MySharedPreferences.getMySharedPreferences().getQuantity());
        myViewHolder.btnAccept.setText(MySharedPreferences.getMySharedPreferences().getApproved());
        myViewHolder.btnDisapprove.setText(MySharedPreferences.getMySharedPreferences().getDisApproved());
        myViewHolder.tvDiscount.setText(MySharedPreferences.getMySharedPreferences().getDiscount());
        myViewHolder.tvUserName.setText(MySharedPreferences.getMySharedPreferences().getUserName());
    }


    public class MyViewHolder extends BaseRecyclerAdapter<MerchantVouchersListAdapter.MyViewHolder, Datum>.ViewHolder {
        @BindView(R.id.txtName)
        AppCompatTextView txtName;
        @BindView(R.id.txtVoucherType)
        AppCompatTextView txtVoucherType;
        @BindView(R.id.txtStartDate)
        AppCompatTextView txtStartDate;
        @BindView(R.id.txtStartTime)
        AppCompatTextView txtStartTime;
        @BindView(R.id.txtQuantity)
        AppCompatTextView txtQuantity;
        @BindView(R.id.txtAmount)
        AppCompatTextView txtAmount;
        @BindView(R.id.txtEndDate)
        AppCompatTextView txtEndDate;
        @BindView(R.id.txtEndTime)
        AppCompatTextView txtEndTime;
        @BindView(R.id.btnAccept)
        AppCompatButton btnAccept;
        @BindView(R.id.btnDisapprove)
        AppCompatButton btnDisapprove;
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
        @BindView(R.id.tvDiscount)
        AppCompatTextView tvDiscount;
        @BindView(R.id.txtDiscount)
        AppCompatTextView txtDiscount;
        @BindView(R.id.tvUserName)
        AppCompatTextView tvUserName;
        @BindView(R.id.txtUserName)
        AppCompatTextView txtUserName;


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
