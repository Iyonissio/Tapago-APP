package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.model.MoreEvent.EventList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreEventAdapter extends RecyclerView.Adapter<MoreEventAdapter.MyViewHolder> {

    private Context context;
    private List<EventList> eventLists;
    private OnItemClickLister onItemClickLister = null;


    public MoreEventAdapter(Context activity, List<EventList> eventLists) {
        this.context = activity;
        this.eventLists = eventLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_event, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        EventList datum = eventLists.get(i);
        myViewHolder.txtEventName.setText(datum.getEventTitle());
        myViewHolder.txtEventName.setSelected(true);
        myViewHolder.txtDate.setText(datum.getEventStartDate() + " " + datum.getEventStartDateTime());
        myViewHolder.txtEventDes.setText(datum.getEventDescription());
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder);
        Glide.with(context).load(datum.getEventThumbImg()).apply(options).into(myViewHolder.imgEventBanner);
    }

    public interface OnItemClickLister {
        void itemClicked(View view, int position);
    }

    @Override
    public int getItemCount() {
        return eventLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imgEventBanner)
        AppCompatImageView imgEventBanner;
        @BindView(R.id.txtEventName)
        AppCompatTextView txtEventName;
        @BindView(R.id.txtEventDes)
        AppCompatTextView txtEventDes;
        @BindView(R.id.txtDate)
        AppCompatTextView txtDate;

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
