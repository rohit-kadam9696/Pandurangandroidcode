package com.twd.chitboyapp.spsskl.vh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class RemainingSlipHolder extends RecyclerView.ViewHolder {

    public CardView cvremainingslip;

    public AppCompatTextView txthangamtxt;
    public AppCompatTextView txtslipdatetxt;
    public AppCompatTextView txtslipnotxt;
    public AppCompatTextView txtfarmercodetxt;
    public AppCompatTextView txtfarmernametxt;
    public AppCompatTextView txtvvillagenametxt;
    public AppCompatTextView txtvehicaltypetxt;

    public AppCompatTextView txtharvestorter;
    public AppCompatTextView txtfvrcollon9;
    public AppCompatTextView txtharvestortxt;
    public AppCompatTextView txttransporter;
    public AppCompatTextView txtfvrcollon2;
    public AppCompatTextView txttransportertxt;

    public AppCompatTextView txtmukadam;
    public AppCompatTextView txtfvrcollon10;
    public AppCompatTextView txtmukadamtxt;
    public AppCompatTextView txtgadiwan;
    public AppCompatTextView txtfvrcollon11;
    public AppCompatTextView txtgadiwantxt;

    public AppCompatTextView txtvehiclenotxt;
    public AppCompatTextView txtwireropelbl;
    public AppCompatTextView txtfvrcollon5;
    public AppCompatTextView txtwireropetxt;
    public AppCompatTextView txtfronttrailerlbl;
    public AppCompatTextView txtfvrcollon6;
    public AppCompatTextView txtfronttrailertxt;
    public AppCompatTextView txtbacktrailerlbl;
    public AppCompatTextView txtfvrcollon7;
    public AppCompatTextView txtbacktrailertxt;
    public AppCompatTextView txtremarktxt;
    public AppCompatTextView txtdeptheadtxt;
    public AppCompatTextView txtplotnotxt;
    public AppCompatButton btncancelslip;
    public AppCompatButton btnreprint;

    public RemainingSlipHolder(@NonNull View itemView) {
        super(itemView);
        cvremainingslip = itemView.findViewById(R.id.cvremainingslip);
        txthangamtxt = itemView.findViewById(R.id.txthangamtxt);
        txtslipdatetxt = itemView.findViewById(R.id.txtslipdatetxt);
        txtslipnotxt = itemView.findViewById(R.id.txtslipnotxt);
        txtfarmercodetxt = itemView.findViewById(R.id.txtfarmercodetxt);
        txtfarmernametxt = itemView.findViewById(R.id.txtfarmernametxt);
        txtvvillagenametxt = itemView.findViewById(R.id.txtvvillagenametxt);
        txtvehicaltypetxt = itemView.findViewById(R.id.txtvehicaltypetxt);

        txtharvestorter = itemView.findViewById(R.id.txtharvestorter);
        txtfvrcollon9 = itemView.findViewById(R.id.txtfvrcollon9);
        txtharvestortxt = itemView.findViewById(R.id.txtharvestortxt);
        txttransporter = itemView.findViewById(R.id.txttransporter);
        txtfvrcollon2 = itemView.findViewById(R.id.txtfvrcollon2);
        txttransportertxt = itemView.findViewById(R.id.txttransportertxt);

        txtmukadam = itemView.findViewById(R.id.txtmukadam);
        txtfvrcollon10 = itemView.findViewById(R.id.txtfvrcollon10);
        txtmukadamtxt = itemView.findViewById(R.id.txtmukadamtxt);
        txtgadiwan = itemView.findViewById(R.id.txtgadiwan);
        txtfvrcollon11 = itemView.findViewById(R.id.txtfvrcollon11);
        txtgadiwantxt = itemView.findViewById(R.id.txtgadiwantxt);

        txtvehiclenotxt = itemView.findViewById(R.id.txtvehiclenotxt);
        txtwireropelbl = itemView.findViewById(R.id.txtwireropelbl);
        txtfvrcollon5 = itemView.findViewById(R.id.txtfvrcollon5);
        txtwireropetxt = itemView.findViewById(R.id.txtwireropetxt);
        txtfronttrailerlbl = itemView.findViewById(R.id.txtfronttrailerlbl);
        txtfvrcollon6 = itemView.findViewById(R.id.txtfvrcollon6);
        txtfronttrailertxt = itemView.findViewById(R.id.txtfronttrailertxt);
        txtbacktrailerlbl = itemView.findViewById(R.id.txtbacktrailerlbl);
        txtfvrcollon7 = itemView.findViewById(R.id.txtfvrcollon7);
        txtbacktrailertxt = itemView.findViewById(R.id.txtbacktrailertxt);
        txtremarktxt = itemView.findViewById(R.id.txtremarktxt);
        txtdeptheadtxt = itemView.findViewById(R.id.txtdeptheadtxt);
        txtplotnotxt = itemView.findViewById(R.id.txtplotnotxt);

        btncancelslip = itemView.findViewById(R.id.btncancelslip);
        btnreprint = itemView.findViewById(R.id.btnreprint);
    }
}
