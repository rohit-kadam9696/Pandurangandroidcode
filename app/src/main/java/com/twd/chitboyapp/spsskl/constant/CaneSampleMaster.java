package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;

import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.pojo.CaneSamplePlantationData;

public class CaneSampleMaster {
    public void setMasterData(Activity activity, CaneSamplePlantationData caneSamplePlantationData) {

        AppCompatTextView txtplotnotxt = activity.findViewById(R.id.txtplotnotxt);
        AppCompatTextView txtfarmernametxt = activity.findViewById(R.id.txtfarmernametxt);
        AppCompatTextView txtvillagetxt = activity.findViewById(R.id.txtvillagetxt);
        AppCompatTextView txthangamtxt = activity.findViewById(R.id.txthangamtxt);
        AppCompatTextView txtvarietytxt = activity.findViewById(R.id.txtvarietytxt);
        AppCompatTextView txtareatxt = activity.findViewById(R.id.txtareatxt);

        txtplotnotxt.setText(caneSamplePlantationData.getNplotNo());
        txtfarmernametxt.setText(caneSamplePlantationData.getFarmerName());
        txtvillagetxt.setText(caneSamplePlantationData.getVilleageName());
        txthangamtxt.setText(caneSamplePlantationData.getHungamName());
        txtvarietytxt.setText(caneSamplePlantationData.getVarityName());
        txtareatxt.setText(caneSamplePlantationData.getNarea());

    }
}
