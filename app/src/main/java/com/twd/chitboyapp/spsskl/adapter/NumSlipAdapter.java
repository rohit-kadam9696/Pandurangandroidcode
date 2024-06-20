package com.twd.chitboyapp.spsskl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.pojo.NumSlip;
import com.twd.chitboyapp.spsskl.vh.NumSlipHolder;

import java.util.List;

public class NumSlipAdapter extends RecyclerView.Adapter<NumSlipHolder> {


    private final List<NumSlip> numSlips;

    public NumSlipAdapter(List<NumSlip> numSlips) {
        this.numSlips = numSlips;
    }

    @NonNull
    @Override
    public NumSlipHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_num_slip, parent, false);
        return new NumSlipHolder(v);
    }

    @Override
    public void onBindViewHolder(NumSlipHolder holder, final int position) {
        NumSlip numSlip = numSlips.get(holder.getBindingAdapterPosition());

        holder.txtseasonval.setText(numSlip.getSeason());
        holder.txtslipnoval.setText(numSlip.getSlipno());
        holder.txtfarmerval.setText(numSlip.getFarmer());
        holder.txttransporterval.setText(numSlip.getTransporter());
        holder.txtvehiclenoval.setText(numSlip.getVehicleno());
        holder.txtvehicletypeval.setText(numSlip.getVehicletype());
        holder.txtvehiclegroupidval.setText(numSlip.getVehiclegroupid());
        holder.txttranscodeval.setText(numSlip.getTranscode());
        holder.txtvillageval.setText(numSlip.getVillageid());
        holder.txtvillagenameval.setText(numSlip.getVillageName());
        holder.txtfronttailerval.setText(numSlip.getFrontTailer());
        if(numSlip.getBackTailer() == null) {
            holder.txtbacktailerlbl.setVisibility(View.GONE);
            holder.txtnccollon6.setVisibility(View.GONE);
            holder.txtbacktailerval.setVisibility(View.GONE);
        } else
            holder.txtbacktailerval.setText(numSlip.getBackTailer());

    }


    @Override
    public int getItemCount() {
        return numSlips.size();
    }

    public List<NumSlip> getItems() {
        return numSlips;
    }

}