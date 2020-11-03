package com.tapago.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.tapago.app.R;
import com.tapago.app.utils.MySharedPreferences;

import java.util.Objects;


public class VendorSelection extends Dialog implements View.OnClickListener {
    private Context mContext;
    public AppCompatTextView txtOk, txtCancel, txtBody;
    public RecyclerView rcList;

    public VendorSelection(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_vendorlist);
        View v = Objects.requireNonNull(getWindow()).getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        txtCancel = findViewById(R.id.txtCancel);
        txtOk = findViewById(R.id.txtOk);
        rcList = findViewById(R.id.rcSelect);
        txtBody = findViewById(R.id.txtBody);
        txtBody.setText(MySharedPreferences.getMySharedPreferences().getSelect_vendor());
        txtCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == txtCancel) {
            dismiss();
        }
    }

}
