package com.tapago.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;

import com.tapago.app.R;

import java.util.Objects;

public class RequestVoucherDialog extends Dialog implements View.OnClickListener {

    private AppCompatImageView imgClose;
    public AppCompatTextView txtPrice, txtTotalPrice, remainingVoucher, txtSelectVoucher, txtPerVoucher, txtNumVoucher, txtAmount, txtDiscount,tvDiscount;
    public Spinner vTypeSpinner, vNumberSpinner;
    public Button btnProceed;

    public RequestVoucherDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_request_voucher);
        View v = Objects.requireNonNull(getWindow()).getDecorView();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setBackgroundResource(android.R.color.transparent);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        txtPrice = v.findViewById(R.id.voucherPrice);
        txtTotalPrice = v.findViewById(R.id.voucherTotalPrice);
        vTypeSpinner = v.findViewById(R.id.vTypeSpinner);
        vNumberSpinner = v.findViewById(R.id.vNumberSpinner);
        txtDiscount = v.findViewById(R.id.txtDiscount);
        btnProceed = v.findViewById(R.id.btnProceed);
        remainingVoucher = v.findViewById(R.id.remainingVoucher);
        imgClose = v.findViewById(R.id.imgClose);

        txtSelectVoucher = v.findViewById(R.id.txtSelectVoucher);
        txtPerVoucher = v.findViewById(R.id.txtPerVoucher);
        txtNumVoucher = v.findViewById(R.id.txtNumVoucher);
        txtAmount = v.findViewById(R.id.txtAmount);
        tvDiscount = v.findViewById(R.id.tvDiscount);
        imgClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == imgClose) {
            dismiss();
        }
    }
}
