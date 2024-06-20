package com.twd.chitboyapp.spsskl.pojo;

import java.io.Serializable;
import java.util.List;

public class CaneSamplePlantationData extends MainResponse implements Serializable {

    private String nplotNo;
    private String vyearId;
    private String farmerName;
    private String villeageName;
    private String hungamName;
    private String varityName;
    private String narea;
    private List<CaneSample> caneSampleList;

    public String getNplotNo() {
        return nplotNo;
    }

    public void setNplotNo(String nplotNo) {
        this.nplotNo = nplotNo;
    }

    public String getVyearId() {
        return vyearId;
    }

    public void setVyearId(String vyearId) {
        this.vyearId = vyearId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getVilleageName() {
        return villeageName;
    }

    public void setVilleageName(String villeageName) {
        this.villeageName = villeageName;
    }

    public String getHungamName() {
        return hungamName;
    }

    public void setHungamName(String hungamName) {
        this.hungamName = hungamName;
    }

    public String getVarityName() {
        return varityName;
    }

    public void setVarityName(String varityName) {
        this.varityName = varityName;
    }

    public String getNarea() {
        return narea;
    }

    public void setNarea(String narea) {
        this.narea = narea;
    }

    public List<CaneSample> getCaneSampleList() {
        return caneSampleList;
    }

    public void setCaneSampleList(List<CaneSample> caneSampleList) {
        this.caneSampleList = caneSampleList;
    }
}
