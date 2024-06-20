package com.twd.chitboyapp.spsskl.vh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class CaneSampleListHolder extends RecyclerView.ViewHolder {
    public AppCompatTextView txtdatetxt;
    public AppCompatTextView txtbrixtxt;
    public AppCompatTextView txtpoletxt;
    public AppCompatTextView txtpuritytxt;
    public AppCompatTextView txtrecoverytxt;

    public CaneSampleListHolder(@NonNull View itemView) {
        super(itemView);
        txtdatetxt = itemView.findViewById(R.id.txtdatetxt);
        txtbrixtxt = itemView.findViewById(R.id.txtbrixtxt);
        txtpoletxt = itemView.findViewById(R.id.txtpoletxt);
        txtpuritytxt = itemView.findViewById(R.id.txtpuritytxt);
        txtrecoverytxt = itemView.findViewById(R.id.txtrecoverytxt);
    }
}
