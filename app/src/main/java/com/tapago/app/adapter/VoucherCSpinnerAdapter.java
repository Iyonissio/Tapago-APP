package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tapago.app.R;
import com.tapago.app.model.VoucherListModel.Datum;


import java.util.List;

public class VoucherCSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<Datum> datumList;

    public VoucherCSpinnerAdapter(Activity activity, List<Datum> data) {

        context = activity;
        this.datumList = data;
    }

    @Override
    public int getCount() {
        return datumList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return datumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Datum language1 = null;
        if (position > 0) {
            language1 = datumList.get(position - 1);
        }
        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_list, parent, false);
        AppCompatTextView tvCatName = view.findViewById(R.id.tvSpinnerSecItem);
        if (position == 0) {
            tvCatName.setText(R.string.select_voucher_category);
            tvCatName.setEnabled(false);
            tvCatName.setClickable(false);
            tvCatName.setFocusable(false);
            tvCatName.setTextColor(ContextCompat.getColor(context, R.color.gray));
        } else {
            if (language1 != null)
                tvCatName.setText(language1.getVoucherCategoryName());
            tvCatName.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        return view;
    }
}
