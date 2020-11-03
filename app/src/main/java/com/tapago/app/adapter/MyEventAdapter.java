package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.fragment.MyEventFragment;
import com.tapago.app.model.MyEventModel.Datum;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;
import com.tapago.app.utils.MySharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyEventAdapter extends LoadMoreRecyclerAdapter<MyEventAdapter.MyViewHolder, Datum> {

    private Context context;
    private OnItemClickLister onItemClickLister = null;
    private MyEventFragment myEventFragment;


    public MyEventAdapter(FragmentActivity activity, List<Datum> myEventList, MyEventFragment myEventFragment) {
        super(myEventList);
        this.context = activity;
        this.myEventFragment = myEventFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_my_event, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Datum datum = getList().get(i);
        myViewHolder.txtEventName.setText(datum.getEventTitle());
        myViewHolder.txtEventName.setSelected(true);
        myViewHolder.txtDate.setText(datum.getEventDate() + " " + datum.getEventStartDateTime());
        myViewHolder.txtEventDes.setText(datum.getEventDescription());
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_placeholder);
        Glide.with(context).load(datum.getEventThumbImg()).apply(options).into(myViewHolder.imgEventBanner);
        myViewHolder.txtRemainingBudget.setText("MT" + String.valueOf(datum.getRemainingBudget()));
        myViewHolder.txtPaymentStatus.setText(datum.getPaymentStatus());
        if (datum.getEventPurchase().equalsIgnoreCase("true")) {
            myViewHolder.imgDelete.setVisibility(View.GONE);
        } else {
            myViewHolder.imgDelete.setVisibility(View.VISIBLE);
        }
        myViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEventFragment.nextActivity(String.valueOf(datum.getEventId()), datum.getPaymentStatus());
            }
        });
        myViewHolder.tvRemainingBudget.setText(MySharedPreferences.getMySharedPreferences().getRemainingBudget());
        myViewHolder.tvPaymentStatus.setText(MySharedPreferences.getMySharedPreferences().getPaymentStatus());

        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEventFragment.eventDeleteDialog(String.valueOf(datum.getEventId()));
            }
        });
    }

    public interface OnItemClickLister {
        void itemClicked(View view, int position);
    }


    public class MyViewHolder extends BaseRecyclerAdapter<MyViewHolder, Datum>.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imgEventBanner)
        AppCompatImageView imgEventBanner;
        @BindView(R.id.txtEventName)
        AppCompatTextView txtEventName;
        @BindView(R.id.txtEventDes)
        AppCompatTextView txtEventDes;
        @BindView(R.id.txtDate)
        AppCompatTextView txtDate;
        @BindView(R.id.imgEdit)
        AppCompatImageView imgEdit;
        @BindView(R.id.txtRemainingBudget)
        AppCompatTextView txtRemainingBudget;
        @BindView(R.id.txtPaymentStatus)
        AppCompatTextView txtPaymentStatus;
        @BindView(R.id.tvRemainingBudget)
        AppCompatTextView tvRemainingBudget;
        @BindView(R.id.tvPaymentStatus)
        AppCompatTextView tvPaymentStatus;
        @BindView(R.id.imgDelete)
        AppCompatImageView imgDelete;


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
        return super.getItemViewType(position);
    }
}
