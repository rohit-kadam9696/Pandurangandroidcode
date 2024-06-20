package com.twd.chitboyapp.spsskl.vh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class CompletePlotListHolder extends RecyclerView.ViewHolder {
    public CardView ncregistrationcard;
    public AppCompatTextView txtnondvillagetxt;
    public AppCompatTextView txtplotnotxt;
    public AppCompatTextView txthangamtxt;
    public AppCompatTextView txtareatxt;
    public AppCompatTextView txtvaritytxt;
    public AppCompatTextView txtplantationdatetxt;
    public AppCompatTextView txtexpectedyieldtxt;
    public AppCompatTextView txtharvestedtonnagetxt;
    public AppCompatTextView txtstatustxt;
    public AppCompatButton btncomplete;

    public CompletePlotListHolder(@NonNull View itemView) {
        super(itemView);
        ncregistrationcard = itemView.findViewById(R.id.ncregistrationcard);
        txtnondvillagetxt = itemView.findViewById(R.id.txtnondvillagetxt);
        txtplotnotxt = itemView.findViewById(R.id.txtplotnotxt);
        txthangamtxt = itemView.findViewById(R.id.txthangamtxt);
        txtareatxt = itemView.findViewById(R.id.txtareatxt);
        txtvaritytxt = itemView.findViewById(R.id.txtvaritytxt);
        txtplantationdatetxt = itemView.findViewById(R.id.txtplantationdatetxt);
        txtexpectedyieldtxt = itemView.findViewById(R.id.txtexpectedyieldtxt);
        txtharvestedtonnagetxt = itemView.findViewById(R.id.txtharvestedtonnagetxt);
        txtstatustxt = itemView.findViewById(R.id.txtstatustxt);
        btncomplete = itemView.findViewById(R.id.btncomplete);
    }
}
