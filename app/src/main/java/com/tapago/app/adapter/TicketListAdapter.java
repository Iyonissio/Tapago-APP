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

import com.tapago.app.R;
import com.tapago.app.activity.BookTicketActivity;
import com.tapago.app.model.TicketListModel.Datum;
import com.tapago.app.utils.MySharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.MyViewHolder> {

    private Context context;
    private List<Datum> datumList;
    private OnItemClickLister onItemClickLister = null;
    BookTicketActivity bookTicketActivity;

    public TicketListAdapter(Context activity, List<Datum> datumList, BookTicketActivity bookTicketActivity) {
        this.context = activity;
        this.datumList = datumList;
        this.bookTicketActivity = bookTicketActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book_ticket, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Datum datum = datumList.get(i);
        myViewHolder.txtTypeGold.setText(datum.getTicketCategoryName());
        myViewHolder.remainingVoucher.setText(context.getString(R.string.only) + " " + String.valueOf(datum.getRemainingQty()) + " " + context.getString(R.string.ticket_left));
        myViewHolder.txtDiscount.setText(MySharedPreferences.getMySharedPreferences().getDiscount() + " " + datum.getTicketDiscount() + "%");
        myViewHolder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentNos = Integer.parseInt(myViewHolder.txtCount.getText().toString());
                if (Integer.parseInt(datum.getRemainingQty()) > currentNos) {
                    myViewHolder.txtCount.setText(String.valueOf(++currentNos));
                    float sum = Float.valueOf(datum.getTicketPrice()) * (float) currentNos;
                    String firstNumberAsString = String.format("%.0f", sum);
                    myViewHolder.txtTotalGold.setText("MT" + String.valueOf(firstNumberAsString));
                    bookTicketActivity.total();
                }
            }
        });
        myViewHolder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentNos = Integer.parseInt(myViewHolder.txtCount.getText().toString());
                if (currentNos > 0) {
                    myViewHolder.txtCount.setText(String.valueOf(--currentNos));
                    float sum = Float.valueOf(datum.getTicketPrice()) * (float) currentNos;
                    String firstNumberAsString = String.format("%.0f", sum);
                    myViewHolder.txtTotalGold.setText("MT" + String.valueOf(firstNumberAsString));
                    bookTicketActivity.total();
                }
            }
        });
    }

    public interface OnItemClickLister {
        void itemClicked(View view, int position);
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtTypeGold)
        AppCompatTextView txtTypeGold;
        @BindView(R.id.txtCount)
        AppCompatTextView txtCount;
        @BindView(R.id.txtTotalGold)
        AppCompatTextView txtTotalGold;
        @BindView(R.id.imgMinus)
        AppCompatImageView imgMinus;
        @BindView(R.id.imgPlus)
        AppCompatImageView imgPlus;
        @BindView(R.id.remainingVoucher)
        AppCompatTextView remainingVoucher;
        @BindView(R.id.txtDiscount)
        AppCompatTextView txtDiscount;

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
