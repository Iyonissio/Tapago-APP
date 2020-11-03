package com.tapago.app.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.tapago.app.R;
import com.tapago.app.model.NotificationModel.Datum;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private Context context;
    ArrayList<Datum> datumArrayList;

    public NotificationAdapter(Context context, ArrayList<Datum> datumArrayList) {
        this.context = context;
        this.datumArrayList = datumArrayList;
    }


    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder, int position) {
        final Datum data = datumArrayList.get(position);
        holder.itemTitle.setText(data.getEventTitle());
        holder.itemDesc.setText(data.getMessage());
    }

    @Override
    public int getItemCount() {
        return datumArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_title)
        AppCompatTextView itemTitle;
        @BindView(R.id.item_desc)
        AppCompatTextView itemDesc;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}