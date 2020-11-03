package com.tapago.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapago.app.R;

import com.tapago.app.model.NotificationModel.Datum;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationUserListAdapter extends LoadMoreRecyclerAdapter<NotificationUserListAdapter.MyViewHolder, Datum> {

    private Context context;

    public NotificationUserListAdapter(Activity activity, List<Datum> ticketListUser) {
        super(ticketListUser);
        context = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Datum data = getList().get(i);
        myViewHolder.itemTitle.setText(data.getEventTitle());
        myViewHolder.itemDesc.setText(data.getMessage());
    }


    public class MyViewHolder extends BaseRecyclerAdapter<NotificationUserListAdapter.MyViewHolder, Datum>.ViewHolder {
        @BindView(R.id.item_title)
        AppCompatTextView itemTitle;
        @BindView(R.id.item_desc)
        AppCompatTextView itemDesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
