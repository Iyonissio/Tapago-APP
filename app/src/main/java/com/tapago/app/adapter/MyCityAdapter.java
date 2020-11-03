package com.tapago.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapago.app.R;
import com.tapago.app.activity.UpdateCityActivity;
import com.tapago.app.model.CityModel.Datum;
import com.tapago.app.utils.MySharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCityAdapter extends RecyclerView.Adapter<MyCityAdapter.MyViewHolder> {


    private Context context;
    private List<Datum> datumList;
    private OnItemClickLister onItemClickLister = null;
    UpdateCityActivity updateCityActivity;


    public MyCityAdapter(Context activity, List<Datum> cityList, UpdateCityActivity updateCityActivity) {
        this.context = activity;
        this.datumList = cityList;
        this.updateCityActivity = updateCityActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_city, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Datum datum = datumList.get(i);
        myViewHolder.txtCityName.setText(datum.getCityName());
        myViewHolder.txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCityActivity.removeCityApi(String.valueOf(datum.getId()));
            }
        });
        myViewHolder.txtRemove.setText(MySharedPreferences.getMySharedPreferences().getRemove());
    }

    public interface OnItemClickLister {
        void itemClicked(View view, int position);
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtCityName)
        AppCompatTextView txtCityName;
        @BindView(R.id.txtRemove)
        AppCompatTextView txtRemove;

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
