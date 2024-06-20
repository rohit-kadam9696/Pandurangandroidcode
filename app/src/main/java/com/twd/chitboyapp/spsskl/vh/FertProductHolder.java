package com.twd.chitboyapp.spsskl.vh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class FertProductHolder extends RecyclerView.ViewHolder {
    public AppCompatTextView txtsubstoreid;
    public AppCompatTextView txtproductid;
    public AppCompatTextView txtproductname;
    public AppCompatTextView txtstock;
    public AppCompatEditText edtquantity;
    public AppCompatTextView txtunitid;
    public AppCompatTextView txtunit;
    public AppCompatTextView txthsnid;
    public AppCompatTextView txtrateval;
    public AppCompatTextView txttotal;
    public AppCompatTextView txttaxval;
    public AppCompatTextView txttotaltax;

    public FertProductHolder(@NonNull View itemView) {
        super(itemView);
        txtsubstoreid = itemView.findViewById(R.id.txtsubstoreid);
        txtproductid = itemView.findViewById(R.id.txtproductid);
        txtproductname = itemView.findViewById(R.id.txtproductname);
        txtstock = itemView.findViewById(R.id.txtstock);
        edtquantity = itemView.findViewById(R.id.edtquantity);
        txtunitid = itemView.findViewById(R.id.txtunitid);
        txtunit = itemView.findViewById(R.id.txtunit);
        txthsnid = itemView.findViewById(R.id.txthsnid);
        txtrateval = itemView.findViewById(R.id.txtrateval);
        txttotal = itemView.findViewById(R.id.txttotal);
        txttaxval = itemView.findViewById(R.id.txttaxval);
        txttotaltax = itemView.findViewById(R.id.txttotaltax);

    }
}
