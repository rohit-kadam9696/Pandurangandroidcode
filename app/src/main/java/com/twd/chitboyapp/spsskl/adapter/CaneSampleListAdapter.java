package com.twd.chitboyapp.spsskl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.pojo.CaneSample;
import com.twd.chitboyapp.spsskl.vh.CaneSampleListHolder;

import java.util.List;

public class CaneSampleListAdapter extends RecyclerView.Adapter<CaneSampleListHolder> {


    private final List<CaneSample> caneSampleList;

    public CaneSampleListAdapter(List<CaneSample> caneSampleList) {
        this.caneSampleList = caneSampleList;
    }

    @NonNull
    @Override
    public CaneSampleListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cane_sample, parent, false);
        return new CaneSampleListHolder(v);
    }

    @Override
    public void onBindViewHolder(CaneSampleListHolder holder, final int position) {
        CaneSample caneSample = caneSampleList.get(holder.getBindingAdapterPosition());

        holder.txtdatetxt.setText(caneSample.getSampleDate());
        holder.txtbrixtxt.setText(caneSample.getNbrix());
        holder.txtpoletxt.setText(caneSample.getNpole());
        holder.txtpuritytxt.setText(caneSample.getNpurity());
        holder.txtrecoverytxt.setText(caneSample.getNrecovery());

    }


    @Override
    public int getItemCount() {
        return caneSampleList.size();
    }

}