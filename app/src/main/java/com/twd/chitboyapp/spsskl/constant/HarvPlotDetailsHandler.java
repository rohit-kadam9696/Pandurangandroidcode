package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;

import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.pojo.HarvPlotDetailsResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class HarvPlotDetailsHandler {
    public void setData(HarvPlotDetailsResponse plotDetailsResponse, Activity activity) {
        AppCompatTextView txtplotnotxt, txtfactorynametxt, txtfarmernametxt, txtfarmertypetxt, txtsurvynotxt, txtvillagetxt, txtplantationdatetxt, txthangamtxt, txtvarietytxt, txtareatxt;
        txtplotnotxt = activity.findViewById(R.id.txtplotnotxt);
        txtfactorynametxt = activity.findViewById(R.id.txtfactorynametxt);
        txtfarmernametxt = activity.findViewById(R.id.txtfarmernametxt);
        txtfarmertypetxt = activity.findViewById(R.id.txtfarmertypetxt);
        txtsurvynotxt = activity.findViewById(R.id.txtsurvynotxt);
        txtvillagetxt = activity.findViewById(R.id.txtvillagetxt);
        txtplantationdatetxt = activity.findViewById(R.id.txtplantationdatetxt);
        txthangamtxt = activity.findViewById(R.id.txthangamtxt);
        txtvarietytxt = activity.findViewById(R.id.txtvarietytxt);
        txtareatxt = activity.findViewById(R.id.txtareatxt);

        txtplotnotxt.setText(String.valueOf(plotDetailsResponse.getNplotNo()));
        txtfactorynametxt.setText(plotDetailsResponse.getVfactNameLocal());
        txtfarmernametxt.setText(plotDetailsResponse.getVentityNameLocal());
        txtfarmertypetxt.setText(plotDetailsResponse.getVfarmerTypeNameLocal());
        txtsurvynotxt.setText(plotDetailsResponse.getVsurvyNo());
        txtvillagetxt.setText(plotDetailsResponse.getVillageNameLocal());
        txtplantationdatetxt.setText(plotDetailsResponse.getDplantaitonDate());
        txthangamtxt.setText(plotDetailsResponse.getVhangamName());
        txtvarietytxt.setText(plotDetailsResponse.getVvarietyName());
        txtareatxt.setText(plotDetailsResponse.getNtentativeArea());

        createSaveJson(plotDetailsResponse, activity);
    }

    private void createSaveJson(HarvPlotDetailsResponse plotDetailsResponse, Activity activity) {
        JSONObject job = new JSONObject();
        try {
            //data String
            job.put("vyearId", plotDetailsResponse.getVyearId());
            job.put("ntentativeArea", plotDetailsResponse.getNtentativeArea());
            job.put("nexpectedYield", plotDetailsResponse.getNexpectedYield());
            job.put("nharvestedTonnage", plotDetailsResponse.getNharvestedTonnage());

            //data Integer
            job.put("nplotNo", plotDetailsResponse.getNplotNo());
            job.put("nfactId", plotDetailsResponse.getNfactId());
            job.put("nentityUniId", plotDetailsResponse.getNentityUniId());
            job.put("farmername", plotDetailsResponse.getVentityNameLocal());
            job.put("nfarmerTypeId", plotDetailsResponse.getNfarmerTypeId());
            job.put("nsectionId", plotDetailsResponse.getNsectionId());
            job.put("nvillageId", plotDetailsResponse.getNvillageId());
            job.put("villageName", plotDetailsResponse.getVillageNameLocal());
            job.put("ncropTypeId", plotDetailsResponse.getNcropTypeId());
            job.put("nhangamId", plotDetailsResponse.getNhangamId());
            job.put("nvarietyId", plotDetailsResponse.getNvarietyId());
            job.put("vvarietyName", plotDetailsResponse.getVvarietyName());
            job.put("ngpsDistance", plotDetailsResponse.getNgpsDistance());
            job.put("ndistance", plotDetailsResponse.getNdistance());
            job.put("extendedtonnage", plotDetailsResponse.getNextendedTonnage());
            job.put("nbullckcartLimit", plotDetailsResponse.getNbullckcartLimit());
            job.put("balanceTonnage", plotDetailsResponse.getNbalanceTonnage());
            job.put("allowedtonnage", plotDetailsResponse.getNallowedTon());

            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{activity.getResources().getString(R.string.prefharvplotdata)};
            String[] value = new String[]{job.toString()};
            cf.putSharedPrefValue(key, value, activity);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
