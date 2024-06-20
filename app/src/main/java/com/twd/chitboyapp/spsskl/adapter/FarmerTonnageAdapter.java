package com.twd.chitboyapp.spsskl.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.pojo.FarmerTonnage;
import com.twd.chitboyapp.spsskl.vh.FarmerTonnageHolder;

import java.util.List;

public class FarmerTonnageAdapter extends RecyclerView.Adapter<FarmerTonnageHolder> {


    private final List<FarmerTonnage> farmerTonnages;

    private Activity activity;

    public FarmerTonnageAdapter(List<FarmerTonnage> farmerTonnages, Activity activity) {
        this.farmerTonnages = farmerTonnages;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FarmerTonnageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tonnage, parent, false);
        return new FarmerTonnageHolder(v);
    }

    @Override
    public void onBindViewHolder(FarmerTonnageHolder holder, final int position) {
        FarmerTonnage farmerTonnage = farmerTonnages.get(holder.getBindingAdapterPosition());
        holder.txttruckcount.setText(farmerTonnage.getTruckCount());
        holder.txttrucktonnage.setText(farmerTonnage.getTruckTonnage());
        holder.txttractorcount.setText(farmerTonnage.getTractorCount());
        holder.txttractortonnage.setText(farmerTonnage.getTractorTonnage());
        holder.txtbullockcartcount.setText(farmerTonnage.getBullockcartCount());
        holder.txtbullockcarttonnage.setText(farmerTonnage.getBullockcartTonnage());
        holder.txttractorgadicount.setText(farmerTonnage.getTractorGadiCount());
        holder.txttractorgaditonnage.setText(farmerTonnage.getTractorGadiTonnage());
        holder.txttotaltonnage.setText(farmerTonnage.getTotalTonnage());
        holder.txttonhead.setText(activity.getResources().getString(R.string.tonnageinformation) + " - " + farmerTonnage.getNplotNo());

    }

    @Override
    public int getItemCount() {
        return farmerTonnages.size();
    }

}