package com.tapago.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.activity.ProductCategoryActivity;
import com.tapago.app.model.CategoryMain.CategoryModel;
import com.tapago.app.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryMainAdapter extends RecyclerView.Adapter<CategoryMainAdapter.MyViewHolder> {


    private Context context;
    private OnItemClickLister onItemClickLister = null;
    private List<CategoryModel> categoryList;

    public CategoryMainAdapter(Activity activity, List<CategoryModel> categoryList) {
        this.context = activity;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try {
            final CategoryModel categoryModel = categoryList.get(i);

            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder);
            Glide.with(context).load(categoryModel.getImage()).apply(options).into(myViewHolder.imgCategory);

            myViewHolder.txtproductName.setText(categoryModel.getCategoryName());
            myViewHolder.cardMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductCategoryActivity.class);
                    intent.putExtra("categoryId", categoryModel.getId().toString());
                    intent.putExtra("categotyName", categoryModel.getCategoryName().toString());
                    context.startActivity(intent);
                    AppUtils.startFromRightToLeft(context);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface OnItemClickLister {
        void itemClicked(View view, int position);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtproductName)
        AppCompatTextView txtproductName;
        @BindView(R.id.card_main)
        LinearLayout cardMain;
        @BindView(R.id.imgCategory)
        ImageView imgCategory;

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
