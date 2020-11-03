package com.tapago.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.tapago.app.R;

import java.util.Objects;

public class QrCodeDialog extends Dialog implements View.OnClickListener {

    private AppCompatImageView imgClose;
    public AppCompatImageView imgQrCode;
    public ProgressBar progressBar;


    public QrCodeDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_qr_code);
        View v = Objects.requireNonNull(getWindow()).getDecorView();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setBackgroundResource(android.R.color.transparent);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        imgQrCode = v.findViewById(R.id.imgQrCode);
        progressBar = v.findViewById(R.id.progress_bar);
        imgClose = v.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == imgClose) {
            dismiss();
        }
    }
}
