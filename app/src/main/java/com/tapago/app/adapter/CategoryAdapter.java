package com.tapago.app.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.model.CategoryList.Datum;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;
import com.tapago.app.utils.MySharedPreferences;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends LoadMoreRecyclerAdapter<CategoryAdapter.MyViewHolder, Datum> {

    private Context context;


    public CategoryAdapter(Context activity, ArrayList<Datum> categoryList) {
        super(categoryList);
        this.context = activity;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindHolder(@NonNull CategoryAdapter.MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        final Datum datum = getList().get(i);
        myViewHolder.bind(datum);
        myViewHolder.txtCategoryName.setText(datum.getCategoryName());
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(datum.getCategoryImage()).apply(options).into(myViewHolder.imgBackground);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean clicked = datum.isClicked();
                datum.setClicked(!clicked);
                notifyItemChanged(i);
            }
        });
    }

    class MyViewHolder extends BaseRecyclerAdapter<CategoryAdapter.MyViewHolder, Datum>.ViewHolder {
        @BindView(R.id.txtCategoryName)
        TextView txtCategoryName;
        @BindView(R.id.imgTick)
        ImageView tick;
        @BindView(R.id.imgBackground)
        AppCompatImageView imgBackground;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Datum datum) {
            boolean clicked = datum.isClicked();
            if (clicked) {
                tick.setVisibility(View.VISIBLE);
            } else {
                tick.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
