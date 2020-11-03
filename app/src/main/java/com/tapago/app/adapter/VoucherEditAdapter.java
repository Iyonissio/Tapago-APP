package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tapago.app.R;
import com.tapago.app.model.EditEventModel.Voucher;
import com.tapago.app.utils.AppUtils;
import com.tapago.app.utils.MySharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VoucherEditAdapter extends RecyclerView.Adapter<VoucherEditAdapter.MyViewHolder> {

    private Context context;
    private List<Voucher> datumList;
    private OnItemClickLister onItemClickLister = null;

    public VoucherEditAdapter(Context context, List<Voucher> datumList) {
        this.context = context;
        this.datumList = datumList;
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_voucher_category, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        final Voucher eventList = datumList.get(i);
        myViewHolder.edQty.setHint(MySharedPreferences.getMySharedPreferences().getQuantity());
        myViewHolder.edAmount.setHint(MySharedPreferences.getMySharedPreferences().getAmount());
        myViewHolder.txtCTotal.setText(MySharedPreferences.getMySharedPreferences().getTotal());
        myViewHolder.edDiscount.setHint(MySharedPreferences.getMySharedPreferences().getDiscount());
        myViewHolder.radioGold.setText(eventList.getVoucherCategoryName());
        myViewHolder.radioGold.setChecked(eventList.isCheckBoxChecked());

        if (eventList.getCheckedFlag().equalsIgnoreCase("checked")) {
            myViewHolder.radioGold.setChecked(true);
            myViewHolder.edQty.setEnabled(true);
            myViewHolder.edAmount.setEnabled(true);
            myViewHolder.edDiscount.setEnabled(true);
        } else {
            myViewHolder.radioGold.setChecked(false);
            myViewHolder.edQty.setEnabled(false);
            myViewHolder.edAmount.setEnabled(false);
            myViewHolder.edDiscount.setEnabled(false);
            myViewHolder.edQty.setText("");
            myViewHolder.edAmount.setText("");
            myViewHolder.edDiscount.setText("");
            myViewHolder.txtTotal.setText("MT0.00");
        }

        myViewHolder.radioGold.setTag(i);
        myViewHolder.radioGold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    eventList.setCheckBoxChecked(true);
                    myViewHolder.edQty.setEnabled(true);
                    myViewHolder.edAmount.setEnabled(true);
                    myViewHolder.edDiscount.setEnabled(true);
                } else {
                    eventList.setCheckBoxChecked(false);
                    myViewHolder.edQty.setEnabled(false);
                    myViewHolder.edAmount.setEnabled(false);
                    myViewHolder.edDiscount.setEnabled(false);
                    myViewHolder.edQty.setText("");
                    myViewHolder.edAmount.setText("");
                    myViewHolder.edDiscount.setText("");
                    myViewHolder.txtTotal.setText("MT0.00");
                }
            }
        });
        if (eventList.getVoucherPrice() != 0) {
            myViewHolder.edAmount.setText(String.valueOf(eventList.getVoucherPrice()));
        }
        if (eventList.getVoucherDiscount() != 0) {
            myViewHolder.edDiscount.setText(String.valueOf(eventList.getVoucherDiscount()));
        }
        if (!eventList.getNoOfVoucher().equals("0")) {
            myViewHolder.edQty.setText(eventList.getNoOfVoucher());
        }
        if (myViewHolder.edQty.getText().toString().length() > 0 && myViewHolder.edAmount.getText().toString().length() > 0) {
            float sum = Float.valueOf(myViewHolder.edAmount.getText().toString()) * Float.valueOf(myViewHolder.edQty.getText().toString());
            myViewHolder.txtTotal.setText("MT" + String.valueOf(sum));
        } else {
            myViewHolder.txtTotal.setText("MT0.00");
        }

        myViewHolder.edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (myViewHolder.edAmount.getText().toString().length() > 0) {
                    if (eventList.getVoucherPrice() > Integer.parseInt(AppUtils.getText(myViewHolder.edAmount))) {
                        AppUtils.showToast(context, context.getString(R.string.amount) + eventList.getVoucherPrice()  + context.getString(R.string.orEqual));
                    }
                }

                if (myViewHolder.edQty.getText().toString().length() > 0 && myViewHolder.edAmount.getText().toString().length() > 0) {
                    float sum = Float.valueOf(myViewHolder.edAmount.getText().toString()) * Float.valueOf(myViewHolder.edQty.getText().toString());
                    String firstNumberAsString = String.format("%.0f", sum);
                    myViewHolder.txtTotal.setText("MT" + firstNumberAsString);
                } else {
                    myViewHolder.txtTotal.setText("MT0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        myViewHolder.edQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (myViewHolder.edQty.getText().toString().length() > 0) {
                    if (Integer.parseInt(eventList.getNoOfVoucher()) > Integer.parseInt(AppUtils.getText(myViewHolder.edQty))) {
                        AppUtils.showToast(context, context.getString(R.string.quantity) + eventList.getNoOfVoucher() + context.getString(R.string.orEqual));
                    }
                }

                if (myViewHolder.edQty.getText().toString().length() > 0 && myViewHolder.edAmount.getText().toString().length() > 0) {
                    float sum = Float.valueOf(myViewHolder.edAmount.getText().toString()) * Float.valueOf(myViewHolder.edQty.getText().toString());
                    myViewHolder.txtTotal.setText("MT" + String.valueOf(sum));
                } else {
                    myViewHolder.txtTotal.setText("MT0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public interface OnItemClickLister {
        void itemClicked(View view, int position);
    }


    @Override
    public int getItemCount() {
        return datumList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.radio_gold)
        AppCompatCheckBox radioGold;
        @BindView(R.id.edQty)
        AppCompatEditText edQty;
        @BindView(R.id.edAmount)
        AppCompatEditText edAmount;
        @BindView((R.id.edDisCount))
        AppCompatEditText edDiscount;
        @BindView(R.id.txtTotal)
        AppCompatTextView txtTotal;
        @BindView(R.id.txtCTotal)
        AppCompatTextView txtCTotal;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
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
