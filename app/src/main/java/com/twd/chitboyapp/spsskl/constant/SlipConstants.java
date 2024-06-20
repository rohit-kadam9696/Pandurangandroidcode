package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.pojo.RemainingSlipBean;
import com.twd.chitboyapp.spsskl.pojo.SlipBeanR;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SlipConstants {

    public JSONObject prepairJob(Activity activity, RemainingSlipBean remainingSlipBean, String vehicalType, JSONObject remark, String sessionUserName) {
        JSONObject job = new JSONObject();
        try {
            job.put("nplotNo", Integer.parseInt(remainingSlipBean.getNplotNo()));
            job.put("villageName", remainingSlipBean.getVvillageNameLocal());
            job.put("nentityUniId", remainingSlipBean.getNentityUnitId());
            job.put("farmername", remainingSlipBean.getVfarmerName());
            job.put("vvarietyName", remainingSlipBean.getVvarietyName());
            job.put("vehicleType", remainingSlipBean.getNvehicalTypeId());
            job.put("vehicalTypeName", vehicalType);
            if (remainingSlipBean.getNvehicalTypeId().equals("1") || remainingSlipBean.getNvehicalTypeId().equals("2")) {
                job.put("harvetortype", remainingSlipBean.getVharvestorType());
                job.put("nharvestorId", remainingSlipBean.getNharvestorId());
                job.put("harvetorname", remainingSlipBean.getVharvestorName());
                job.put("ntransportorId", remainingSlipBean.getNtransporterId());
                job.put("transportername", remainingSlipBean.getVtransporterName());
                job.put("vvehicleNo", remainingSlipBean.getVvehicleNo());
            } else {
                job.put("nbulluckcartMainId", remainingSlipBean.getNbullockMainCode());
                job.put("bulluckcartMainName", remainingSlipBean.getVbullockMainName());
                job.put("nbulluckcartMainId", remainingSlipBean.getNbullockMainCode());
                JSONArray jar = new JSONArray();
                JSONObject bulluck = new JSONObject();
                bulluck.put("name", remainingSlipBean.getVbullockName());
                JSONObject bulluckobject = new JSONObject();
                bulluckobject.put("code", remainingSlipBean.getNbullockCode());
                bulluckobject.put("name", remainingSlipBean.getNbullockCode() + " : " + remainingSlipBean.getVbullockName());
                bulluckobject.put("vehicleNo", remainingSlipBean.getVvehicleNo());
                bulluck.put("object", bulluckobject);
                jar.put(bulluck);

                job.put("bulluckdata", jar);
            }

            if (remainingSlipBean.getNvehicalTypeId().equals("2") || remainingSlipBean.getNvehicalTypeId().equals("4")) {
                job.put("tailorFrontName", remainingSlipBean.getVtailerFrontName());
                job.put("tailorBackName", remainingSlipBean.getVtailerBackName());
            } else if (remainingSlipBean.getNvehicalTypeId().equals("1"))
                job.put("wireropeName", remainingSlipBean.getVwireropeName());

            job.put("remarkName", remark.has(remainingSlipBean.getNremarkId()) ? remark.getString(remainingSlipBean.getNremarkId()) : remainingSlipBean.getNremarkId());
            if (remainingSlipBean.getChitboyName() != null)
                job.put("slipboyname", remainingSlipBean.getChitboyName());
            else job.put("slipboyname", sessionUserName);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast(activity.getResources().getString(R.string.errorcode) + e.getMessage(), activity, R.drawable.ic_wrong);
        }
        return job;
    }

    public void printData(Activity activity, PrinterConstant print, List<SlipBeanR> slipBeanRList, String date, boolean currentDate) {
        ArrayList<String> printview = print.generatePrintView(activity, slipBeanRList, date, activity.getResources().getString(R.string.slipreprint), true, currentDate);
        if (printview.size() > 0) {
            PrinterConstant.printContent = printview.get(0);
            print.print(activity);
        }
    }

}
