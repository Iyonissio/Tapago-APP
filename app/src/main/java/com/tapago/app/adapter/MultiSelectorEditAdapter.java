package com.tapago.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tapago.app.R;
import com.tapago.app.model.EditEventModel.VendorList;
import com.tapago.app.model.GetVendorModel.Datum;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectorEditAdapter extends RecyclerView.Adapter<MultiSelectorEditAdapter.MyViewHolder> {

    private ArrayList<VendorList> items;
    private Context context;
    private String value;

    public MultiSelectorEditAdapter(Context context, List<VendorList> items, String s) {
        this.items = new ArrayList<>();
        this.items.addAll(items);
        this.context = context;
        this.value = s;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final VendorList datum = items.get(position);
        try {
            holder.itemNameText.setText(datum.getFirstName());
            holder.checkbox.setTag(position);
            if (datum.getCheckFlag().equals("checked")){
                holder.checkbox.setChecked(true);
            }else {
                holder.checkbox.setChecked(false);
            }
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        datum.setCheckFlag("checked");
                    } else {
                        datum.setCheckFlag("");
                    }
                }
            });


            if (value.length() > 0) {
                JSONArray jsonArray = new JSONArray(value);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (datum.getId().equals(jsonArray.get(i))) {
                        holder.checkbox.setChecked(true);
                        datum.setCheckFlag("checked");
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView itemNameText;
        CheckBox checkbox;

        MyViewHolder(View convertView) {
            super(convertView);
            itemNameText = convertView.findViewById(R.id.itemname);
            checkbox = convertView.findViewById(R.id.checkBox1);


        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}