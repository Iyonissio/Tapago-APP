package com.tapago.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tapago.app.R;
import com.tapago.app.utils.MySharedPreferences;

import java.util.Objects;

public class PaymentOptionDialog extends Dialog {

    public RadioButton cash, paypal;
    public RadioGroup group;
    public AppCompatTextView ok, cancel, select_option;

    public PaymentOptionDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_payment_option);
        View v = Objects.requireNonNull(getWindow()).getDecorView();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setBackgroundResource(android.R.color.transparent);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        cash = v.findViewById(R.id.radioCash);
        paypal = v.findViewById(R.id.radioPaypal);
        group = v.findViewById(R.id.rg);
        ok = v.findViewById(R.id.tv_ok);
        cancel = v.findViewById(R.id.tv_cancel);
        select_option = v.findViewById(R.id.tv_select_payment);
        select_option.setText(MySharedPreferences.getMySharedPreferences().getSelectPayment());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}