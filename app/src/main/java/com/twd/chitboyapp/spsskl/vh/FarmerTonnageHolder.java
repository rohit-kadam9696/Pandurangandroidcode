package com.twd.chitboyapp.spsskl.vh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class FarmerTonnageHolder extends RecyclerView.ViewHolder {
    public AppCompatTextView txttruckcount;
    public AppCompatTextView txttrucktonnage;
    public AppCompatTextView txttractorcount;
    public AppCompatTextView txttractortonnage;
    public AppCompatTextView txtbullockcartcount;
    public AppCompatTextView txtbullockcarttonnage;
    public AppCompatTextView txttractorgadicount;
    public AppCompatTextView txttractorgaditonnage;
    public AppCompatTextView txttotaltonnage;
    public AppCompatTextView txttonhead;


    public FarmerTonnageHolder(@NonNull View itemView) {
        super(itemView);
        txttruckcount = itemView.findViewById(R.id.txttruckcount);
        txttrucktonnage = itemView.findViewById(R.id.txttrucktonnage);
        txttractorcount = itemView.findViewById(R.id.txttractorcount);
        txttractortonnage = itemView.findViewById(R.id.txttractortonnage);
        txtbullockcartcount = itemView.findViewById(R.id.txtbullockcartcount);
        txtbullockcarttonnage = itemView.findViewById(R.id.txtbullockcarttonnage);
        txttractorgadicount = itemView.findViewById(R.id.txttractorgadicount);
        txttractorgaditonnage = itemView.findViewById(R.id.txttractorgaditonnage);
        txttotaltonnage = itemView.findViewById(R.id.txttotaltonnage);
        txttonhead = itemView.findViewById(R.id.txttonhead);

    }
}
