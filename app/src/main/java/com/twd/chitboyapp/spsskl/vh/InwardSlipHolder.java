package com.twd.chitboyapp.spsskl.vh;

import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class InwardSlipHolder extends RecyclerView.ViewHolder {
    public AppCompatTextView txtslipno;
    public WebView vwslipdata;
    public AppCompatButton btnprint;
    public AppCompatButton btnverify;

    public InwardSlipHolder(@NonNull View itemView) {
        super(itemView);
        txtslipno = itemView.findViewById(R.id.txtslipno);
        vwslipdata = itemView.findViewById(R.id.vwslipdata);
        btnprint = itemView.findViewById(R.id.btnprint);
        btnverify = itemView.findViewById(R.id.btnverify);
    }
}
