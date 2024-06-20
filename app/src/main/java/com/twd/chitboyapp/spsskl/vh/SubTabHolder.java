package com.twd.chitboyapp.spsskl.vh;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;

public class SubTabHolder extends RecyclerView.ViewHolder {
    public ImageView imgmenuimg;
    public TextView txtmnuname;
    public CardView submenucard;

    public SubTabHolder(@NonNull View itemView) {
        super(itemView);
        imgmenuimg = itemView.findViewById(R.id.imgmenuimg);
        txtmnuname = itemView.findViewById(R.id.imgmnuname);
        submenucard = itemView.findViewById(R.id.submenucard);

    }
}
