package com.tapago.app.adapter;

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
import com.tapago.app.activity.ProductDetailActivity;
import com.tapago.app.model.ProductModel.ProductSize;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartProductPkgAdapter extends RecyclerView.Adapter<CartProductPkgAdapter.MyViewHolder> {


    private Context context;
    private OnItemClickLister onItemClickLister = null;
    ItemViewCartAdapter  itemViewCartAdapter;
    String selectedPkg="";
    List<ProductSize> productSizeist;
    ProductDetailActivity productDetailActivity;
    int position;


    public CartProductPkgAdapter(Context activity, ItemViewCartAdapter itemViewCartAdapter, String selectedPkg, List<ProductSize> productSizeist, int position) {
        this.context = activity;
        this.itemViewCartAdapter = itemViewCartAdapter;
        this.selectedPkg = selectedPkg;
        this.productSizeist=productSizeist;
        this.position=position;
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
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        Log.d("seletedPkg", selectedPkg);
        final ProductSize data = productSizeist.get(i);

        if(selectedPkg.equals(data.getProductWeight()+" "+data.getProductScale())){
            myViewHolder.lrmain.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
            myViewHolder.txtpkgqty.setTextColor(ContextCompat.getColor(context, R.color.white));
            myViewHolder.txtpkgprice.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        myViewHolder.txtpkgqty.setText(data.getProductWeight()+" "+data.getProductScale()+" - ");
        myViewHolder.txtpkgprice.setText("MT"+data.getPrice().toString());
        myViewHolder.lrmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewHolder.lrmain.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_gray));
                itemViewCartAdapter.setPkgQty(position, data.getProductScale(),data.getProductWeight().toString(),data.getPrice().toString());
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
