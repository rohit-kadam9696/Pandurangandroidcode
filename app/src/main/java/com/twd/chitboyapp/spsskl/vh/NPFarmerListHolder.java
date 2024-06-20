package com.twd.chitboyapp.spsskl.vh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class NPFarmerListHolder extends RecyclerView.ViewHolder {
    public AppCompatTextView txtchitboynametxt;
    public AppCompatTextView txtfarmernametxt;
    public AppCompatTextView txtfarmercodetxt;
    public AppCompatTextView txtvillagetxt;
    public AppCompatTextView txtstatuslbl;
    public AppCompatTextView txtfvrcollon4;
    public AppCompatTextView txtstatustxt;
    public AppCompatTextView txtvillageid;
    public AppCompatTextView txtchitboyid;
    public AppCompatButton btnreject;
    public AppCompatButton btnverify;
    public ConstraintLayout llchitboy;


    public NPFarmerListHolder(@NonNull View itemView) {
        super(itemView);
        txtchitboynametxt = itemView.findViewById(R.id.txtchitboynametxt);
        txtfarmernametxt = itemView.findViewById(R.id.txtfarmernametxt);
        txtfarmercodetxt = itemView.findViewById(R.id.txtfarmercodetxt);
        txtvillagetxt = itemView.findViewById(R.id.txtvillagetxt);
        txtstatuslbl = itemView.findViewById(R.id.txtstatuslbl);
        txtfvrcollon4 = itemView.findViewById(R.id.txtfvrcollon4);
        txtstatustxt = itemView.findViewById(R.id.txtstatustxt);
        txtvillageid = itemView.findViewById(R.id.txtvillageid);
        txtchitboyid = itemView.findViewById(R.id.txtchitboyid);
        btnreject = itemView.findViewById(R.id.btnreject);
        btnverify = itemView.findViewById(R.id.btnverify);
        llchitboy = itemView.findViewById(R.id.llchitboy);

    }
}
