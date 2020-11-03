package com.tapago.app.adapter;


import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tapago.app.R;
import com.tapago.app.activity.CreateVendorActivity;
import com.tapago.app.model.GetVendorModel.Datum;
import com.tapago.app.pagination.BaseRecyclerAdapter;
import com.tapago.app.pagination.LoadMoreRecyclerAdapter;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class VendorAdapter extends LoadMoreRecyclerAdapter<VendorAdapter.MyViewHolder, Datum> {

    private Context context;
    CreateVendorActivity createVendorActivity;

    public VendorAdapter(Context activity, List<Datum> data, CreateVendorActivity createVendorActivity) {
        super(data);
        this.context = activity;
        this.createVendorActivity = createVendorActivity;
    }

    @Override
    public MyViewHolder onCreateHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vendor_list, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindHolder(MyViewHolder holder, int position) {
        final Datum datum = getList().get(position);
        holder.txtName.setText(datum.getFirstName() + " " + datum.getLastName());
        holder.txtNumber.setText(datum.getCountryCode() + datum.getContactNumber());
        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image);
        Glide.with(Objects.requireNonNull(context)).load(datum.getImage()).apply(options).into(holder.profileImage);
        if (datum.getStatus().equalsIgnoreCase("Y")) {
            holder.txtStatus.setText("Active");
            holder.Switch.setChecked(true);
        } else {
            holder.txtStatus.setText("InActive");
            holder.Switch.setChecked(false);
        }

        holder.Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    createVendorActivity.inActiveVendorApi("Y", String.valueOf(datum.getVendorId()));
                } else {
                    // The toggle is disabled
                    createVendorActivity.inActiveVendorApi("N", String.valueOf(datum.getVendorId()));
                }
            }
        });
    }

    public class MyViewHolder extends BaseRecyclerAdapter<MyViewHolder, Datum>.ViewHolder {
        @BindView(R.id.profile_image)
        CircleImageView profileImage;
        @BindView(R.id.txtName)
        AppCompatTextView txtName;
        @BindView(R.id.txtNumber)
        AppCompatTextView txtNumber;
        @BindView(R.id.Switch)
        SwitchCompat Switch;
        @BindView(R.id.txtStatus)
        AppCompatTextView txtStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
