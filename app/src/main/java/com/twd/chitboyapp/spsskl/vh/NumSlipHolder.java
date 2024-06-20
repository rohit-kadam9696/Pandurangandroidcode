package com.twd.chitboyapp.spsskl.vh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class NumSlipHolder extends RecyclerView.ViewHolder {
    public AppCompatTextView txtseasonval;
    public AppCompatTextView txtslipnoval;
    public AppCompatTextView txtfarmerval;
    public AppCompatTextView txttransporterval;
    public AppCompatTextView txtvehiclenoval;
    public AppCompatTextView txtvehicletypeval;
    public AppCompatTextView txttranscodeval;
    public AppCompatTextView txtvillageval;
    public AppCompatTextView txtvillagenameval;
    public AppCompatTextView txtvehiclegroupidval;
    public AppCompatTextView txtfronttailerval;
    public AppCompatTextView txtbacktailerval;
    public AppCompatTextView txtbacktailerlbl;
    public AppCompatTextView txtnccollon6;

    public NumSlipHolder(@NonNull View itemView) {
        super(itemView);
        txtseasonval = itemView.findViewById(R.id.txtseasonval);
        txtslipnoval = itemView.findViewById(R.id.txtslipnoval);
        txtfarmerval = itemView.findViewById(R.id.txtfarmerval);
        txttransporterval = itemView.findViewById(R.id.txttransporterval);
        txtvehiclenoval = itemView.findViewById(R.id.txtvehiclenoval);
        txtvehicletypeval = itemView.findViewById(R.id.txtvehicletypeval);
        txttranscodeval = itemView.findViewById(R.id.txttranscodeval);
        txtvillageval = itemView.findViewById(R.id.txtvillageval);
        txtvillagenameval = itemView.findViewById(R.id.txtvillagenameval);
        txtvehiclegroupidval = itemView.findViewById(R.id.txtvehiclegroupidval);
        txtfronttailerval = itemView.findViewById(R.id.txtfronttailerval);
        txtbacktailerlbl = itemView.findViewById(R.id.txtbacktailerlbl);
        txtnccollon6 = itemView.findViewById(R.id.txtnccollon6);
        txtbacktailerval = itemView.findViewById(R.id.txtbacktailerval);
    }
}
