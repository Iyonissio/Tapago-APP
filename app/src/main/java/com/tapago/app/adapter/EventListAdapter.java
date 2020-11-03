package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.model.EventListModel.EventList;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EventListAdapter extends LoadMoreRecyclerAdapter<EventListAdapter.MyViewHolder, EventList> {

    private Context context;
    private OnItemClickLister onItemClickLister = null;

    public EventListAdapter(Context context, List<EventList> eventLists) {
        super(eventLists);
        this.context = context;
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_event, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHolder(@NonNull MyViewHolder myViewHolder, int i) {
        EventList eventList = getList().get(i);
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder);
        Glide.with(context).load(eventList.getEventThumbImg()).apply(options).into(myViewHolder.imgEventBanner);
        myViewHolder.txtEventName.setText(eventList.getEventTitle());
        myViewHolder.txtEventName.setSelected(true);
        myViewHolder.txtEventDes.setText(eventList.getEventDescription());
        myViewHolder.txtDate.setText(eventList.getEventDate() + "  " + eventList.getEventStartDateTime());
    }

    public interface OnItemClickLister {
        void itemClicked(View view, int position);
    }

    class MyViewHolder extends BaseRecyclerAdapter<MyViewHolder, EventList>.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imgEventBanner)
        AppCompatImageView imgEventBanner;
        @BindView(R.id.txtEventName)
        AppCompatTextView txtEventName;
        @BindView(R.id.txtEventDes)
        AppCompatTextView txtEventDes;
        @BindView(R.id.txtDate)
        AppCompatTextView txtDate;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
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
        return super.getItemViewType(position);
    }
}
