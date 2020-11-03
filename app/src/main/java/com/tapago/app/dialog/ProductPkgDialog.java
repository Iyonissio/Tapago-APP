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


public class ProductPkgDialog extends Dialog {
    private Context mContext;
 //   public AppCompatTextView txtOk, txtCancel, txtBody;
    public RecyclerView rcList;
    public AppCompatTextView txtTitle;

    public ProductPkgDialog(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_product_pkg);
        View v = Objects.requireNonNull(getWindow()).getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        rcList = findViewById(R.id.rcProductPkg);
        txtTitle = findViewById(R.id.txtTitle);
    }


}
