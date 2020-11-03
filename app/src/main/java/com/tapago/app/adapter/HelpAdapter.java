package com.tapago.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapago.app.R;
import com.tapago.app.model.HelpModel.Datum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.MyViewHolder> {

    private List<Datum> helpResponseModels;
    private Context context;

    public HelpAdapter(Activity activity, List<Datum> helpResponseModels) {
        this.helpResponseModels = helpResponseModels;
        this.context = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_help, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        final Datum datum = helpResponseModels.get(i);
        myViewHolder.itemQuestion.setText(datum.getTitle());
        myViewHolder.itemDesc.setText(datum.getDescription());

        myViewHolder.bind(datum, i);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanded;
                if (i == 0) {
                    expanded = datum.isClicked();
                    datum.setClicked(!expanded);
                } else {
                    expanded = datum.isClicked();
                    datum.setClicked(!expanded);
                }
                // Change the state

                // Notify the adapter that item has changed
                notifyItemChanged(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return helpResponseModels.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_question)
        AppCompatTextView itemQuestion;
        @BindView(R.id.img_expand)
        AppCompatImageView imgExpand;
        @BindView(R.id.item_desc)
        AppCompatTextView itemDesc;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Datum datum, int i) {
            boolean expanded;

            if (i == 0) {
                if (datum.isClicked()) {
                    expanded = false;
                } else {
                    expanded = true;
                }
            } else {
                expanded = datum.isClicked();
            }
            // Set the visibility based on state
            itemDesc.setVisibility(expanded ? View.VISIBLE : View.GONE);

            if (expanded) {
                imgExpand.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_up_arrow));
            } else {
                imgExpand.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_down_arrow));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
