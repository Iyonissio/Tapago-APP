package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tapago.app.R;
import com.tapago.app.activity.CategoryMainActivity;
import com.tapago.app.model.ProductModel.Datum;
import com.tapago.app.model.ProductModel.ProductSize;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MostPopularProductPkgAdapter extends RecyclerView.Adapter<MostPopularProductPkgAdapter.MyViewHolder> {


    private Context context;
    private OnItemClickLister onItemClickLister = null;
    MostPopularProductListAdapter productCategoryListAdapter;
    String selectedPkg = "";
    List<ProductSize> productSizeist;
    int position;
    Datum datum;
    CategoryMainActivity categoryMainActivity;

    public MostPopularProductPkgAdapter(Context activity, MostPopularProductListAdapter productCategoryListAdapter, String selectedPkg, List<ProductSize> productSizeist, int position, Datum data, CategoryMainActivity categoryMainActivity) {
        this.context = activity;
        this.productCategoryListAdapter = productCategoryListAdapter;
        this.selectedPkg = selectedPkg;
        this.productSizeist = productSizeist;
        this.position = position;
        this.datum = data;
        this.categoryMainActivity = categoryMainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pkg_qty, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        Log.d("seletedPkg", selectedPkg);
        final ProductSize data = productSizeist.get(i);
        if (selectedPkg.equals(data.getProductWeight() + " " + data.getProductScale())) {
            myViewHolder.lrmain.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
            myViewHolder.txtpkgqty.setTextColor(ContextCompat.getColor(context, R.color.white));
            myViewHolder.txtpkgprice.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        myViewHolder.txtpkgqty.setText(data.getProductWeight() + " " + data.getProductScale() + " - ");
        myViewHolder.txtpkgprice.setText("MT" + data.getPrice().toString());
        myViewHolder.lrmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datum.getAddedQuantity() != 0) {
                    myViewHolder.lrmain.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_gray));
                    productCategoryListAdapter.setPkgQty(position, data.getProductScale(), data.getProductWeight().toString(), data.getPrice().toString());
                    datum.setProductScale(data.getProductWeight() + " " + data.getProductScale());
                    datum.setPrice(String.valueOf(data.getPrice()) + "/" + data.getProductWeight() + " " + data.getProductScale());
                    datum.setRepeatItem(true);
                    datum.setMostpopular(true);
                    datum.setMinus(false);
                    datum.setSameScale(false);
                    categoryMainActivity.loadAddBasket(datum, position);
                    categoryMainActivity.setBottomLayoutValue();
                }
            }
        });
    }

    public interface OnItemClickLister {
        void itemClicked(View view, int position);
    }

    @Override
    public int getItemCount() {
        return productSizeist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_pkg_qty)
        AppCompatTextView txtpkgqty;
        @BindView(R.id.txt_pkg_price)
        AppCompatTextView txtpkgprice;
        @BindView(R.id.lrmain)
        LinearLayout lrmain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
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
