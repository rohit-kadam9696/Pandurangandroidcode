package com.twd.chitboyapp.spsskl.vh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class EPRListHolder extends RecyclerView.ViewHolder {
    public AppCompatTextView txtfarmernametxt;
    public AppCompatTextView txtplotnotxt;
    public AppCompatButton btnnext;

    public EPRListHolder(@NonNull View itemView) {
        super(itemView);
        txtfarmernametxt = itemView.findViewById(R.id.txtfarmernametxt);
        txtplotnotxt = itemView.findViewById(R.id.txtplotnotxt);
        btnnext = itemView.findViewById(R.id.btnnext);
    }
}
