package com.tapago.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;


import com.tapago.app.R;

import java.util.Objects;


public class CustomDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    public AppCompatTextView txtOk, txtTitle, txtMessage;


    public CustomDialog(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom);
        View v = Objects.requireNonNull(getWindow()).getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        txtOk = findViewById(R.id.txtOk);
        txtTitle = findViewById(R.id.txtTitle);
        txtMessage = findViewById(R.id.txtMessage);
        txtOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == txtOk) {
            dismiss();
        }
    }

}
