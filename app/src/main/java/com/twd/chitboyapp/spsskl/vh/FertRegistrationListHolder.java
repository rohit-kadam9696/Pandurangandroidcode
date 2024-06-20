package com.twd.chitboyapp.spsskl.vh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class FertRegistrationListHolder extends RecyclerView.ViewHolder {
    public CardView ncregistrationcard;
    public AppCompatTextView txtplotnotxt;
    public AppCompatTextView txtnondvillagetxt;
    public AppCompatTextView txtareatxt;
    public AppCompatTextView txtvaritytxt;
    public AppCompatCheckBox chkplotsel;

    public FertRegistrationListHolder(@NonNull View itemView) {
        super(itemView);
        ncregistrationcard = itemView.findViewById(R.id.ncregistrationcard);
        txtnondvillagetxt = itemView.findViewById(R.id.txtnondvillagetxt);
        txtplotnotxt = itemView.findViewById(R.id.txtplotnotxt);
        txtareatxt = itemView.findViewById(R.id.txtareatxt);
        txtvaritytxt = itemView.findViewById(R.id.txtvaritytxt);
        chkplotsel = itemView.findViewById(R.id.chkplotsel);

    }
}
