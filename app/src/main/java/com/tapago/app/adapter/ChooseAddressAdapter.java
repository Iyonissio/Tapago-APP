package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.tapago.app.R;
import com.tapago.app.activity.ChooseAddressActivity;
import com.tapago.app.activity.ShippingActivity;
import com.tapago.app.model.AddressListModel.Datum;
import com.tapago.app.utils.AppUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseAddressAdapter extends RecyclerView.Adapter<ChooseAddressAdapter.MyViewHolder> {

    private Context context;
    private List<Datum> addressLists;
    private int selectedPosition = -1;
    private ChooseAddressActivity chooseAddressActivity;

    public ChooseAddressAdapter(Activity activity, List<Datum> addressLists, ChooseAddressActivity chooseAddressActivity) {
        context = activity;
        this.addressLists = addressLists;
        this.chooseAddressActivity = chooseAddressActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_choose_address, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder dataViewHolder, @SuppressLint("RecyclerView") final int position) {
        final Datum data = addressLists.get(position);

        dataViewHolder.txtAddressName.setText(data.getAddressType());
        //if (data.getCity() != null) {
            dataViewHolder.txtAddressDetails.setText(data.getHouseNo() + "," + data.getLandmark()+","+data.getDistrict()+","+data.getCity());
        /*} else {
            dataViewHolder.txtAddressDetails.setText(data.getHouseNo());
        }*/


       /* if (selectedPosition == position)
            dataViewHolder.imgSelect.setVisibility(View.VISIBLE);
        else
            dataViewHolder.imgSelect.setVisibility(View.GONE);*/

        dataViewHolder.linearHomeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataViewHolder.imgSelect.setVisibility(View.VISIBLE);
                Intent intent= new Intent(context, ShippingActivity.class);
                intent.putExtra("addressID",String.valueOf(data.getId()));
                context.startActivity(intent);
                AppUtils.startFromRightToLeft(context);
            }
        });
        if(data.getAddressType().equalsIgnoreCase("Home")){
            dataViewHolder.imgAddressIcon.setImageResource(R.drawable.ic_home);
        }else if(data.getAddressType().equalsIgnoreCase("Work")){
            dataViewHolder.imgAddressIcon.setImageResource(R.drawable.ic_work);
        }else{
            dataViewHolder.imgAddressIcon.setImageResource(R.drawable.ic_new_other);
        }


        dataViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Objects.requireNonNull(context), R.style.AlertDialogTheme)
                        .setMessage(R.string.you_want_to_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                chooseAddressActivity.deleteAddresslApi(String.valueOf(data.getId()));
                                addressLists.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, addressLists.size());
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgAddressIcon)
        AppCompatImageView imgAddressIcon;
        @BindView(R.id.txtAddressName)
        AppCompatTextView txtAddressName;
        @BindView(R.id.imgSelect)
        AppCompatImageView imgSelect;
        @BindView(R.id.txtAddressDetails)
        AppCompatTextView txtAddressDetails;
        @BindView(R.id.linear_home_location)
        LinearLayout linearHomeLocation;
        @BindView(R.id.imgDelete)
        AppCompatImageView imgDelete;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

