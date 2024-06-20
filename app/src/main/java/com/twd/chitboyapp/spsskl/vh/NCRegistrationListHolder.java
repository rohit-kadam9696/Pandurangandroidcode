package com.twd.chitboyapp.spsskl.vh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class NCRegistrationListHolder extends RecyclerView.ViewHolder {
    public CardView ncregistrationcard;
    public AppCompatTextView txtnondvillagetxt;
    public AppCompatTextView txtplotnotxt;
    public AppCompatTextView txthangamtxt;
    public AppCompatTextView txtareatxt;
    public AppCompatTextView txtvaritytxt;
    public AppCompatTextView txtplantationdatetxt;
    public AppCompatTextView txtexpectedyieldlbl;
    public AppCompatTextView txtnccollon6;
    public AppCompatTextView txtexpectedyieldtxt;
    public AppCompatTextView txtstatuslbl;
    public AppCompatTextView txtnccollon7;
    public AppCompatTextView txtstatustxt;

    public NCRegistrationListHolder(@NonNull View itemView) {
        super(itemView);
        ncregistrationcard = itemView.findViewById(R.id.ncregistrationcard);
        txtnondvillagetxt = itemView.findViewById(R.id.txtnondvillagetxt);
        txtplotnotxt = itemView.findViewById(R.id.txtplotnotxt);
        txthangamtxt = itemView.findViewById(R.id.txthangamtxt);
        txtareatxt = itemView.findViewById(R.id.txtareatxt);
        txtvaritytxt = itemView.findViewById(R.id.txtvaritytxt);
        txtplantationdatetxt = itemView.findViewById(R.id.txtplantationdatetxt);
        txtexpectedyieldlbl = itemView.findViewById(R.id.txtexpectedyieldlbl);
        txtnccollon6 = itemView.findViewById(R.id.txtnccollon6);
        txtexpectedyieldtxt = itemView.findViewById(R.id.txtexpectedyieldtxt);
        txtstatuslbl = itemView.findViewById(R.id.txtstatuslbl);
        txtnccollon7 = itemView.findViewById(R.id.txtnccollon7);
        txtstatustxt = itemView.findViewById(R.id.txtstatustxt);

    }
}
