package com.twd.chitboyapp.spsskl.pojo;

import java.util.ArrayList;

public class HarvestingPlanFarmerResponse extends MainResponse {
    private String farmerCode;
    private String farmerName;
    private ArrayList<Long> plotList;

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public ArrayList<Long> getPlotList() {
        return plotList;
    }

    public void setPlotList(ArrayList<Long> plotList) {
        this.plotList = plotList;
    }


}