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
import com.tapago.app.model.YearModel;

import java.util.List;

public class YearSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<YearModel> stringList;

    public YearSpinnerAdapter(Activity activity, List<YearModel> data) {
        this.context = activity;
        this.stringList = data;
    }

    @Override
    public int getCount() {
        return stringList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return stringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        YearModel yearModel = null;
        if (position > 0) {
            yearModel = stringList.get(position - 1);
        }
        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_list, parent, false);
        AppCompatTextView tvCatName = view.findViewById(R.id.tvSpinnerSecItem);
        if (position == 0) {
            tvCatName.setText(R.string.select_year);
            tvCatName.setEnabled(false);
            tvCatName.setClickable(false);
            tvCatName.setFocusable(false);
            tvCatName.setTextColor(ContextCompat.getColor(context, R.color.gray));
        } else {
            if (yearModel != null)
                tvCatName.setText(yearModel.getYear());
            tvCatName.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        return view;
    }
}
