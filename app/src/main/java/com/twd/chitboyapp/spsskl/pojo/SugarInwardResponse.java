package com.twd.chitboyapp.spsskl.pojo;

import java.io.Serializable;

public class SugarInwardResponse extends MainResponse implements Serializable {
    private String dawakDate;
    private double nqty;
    private double nbags;
    private String ndoNo;
    private String vvehicleNo;
    private String vfinYear;
    private double nwtBag;
    private int nsugarFor;
    private String vsugYearId;
    private String ddoDate;
    private String locationNameInward;
    private String sugarFor;

    public String getDawakDate() {
        return dawakDate;
    }

    public void setDawakDate(String dawakDate) {
        this.dawakDate = dawakDate;
    }

    public double getNqty() {
        return nqty;
    }

    public void setNqty(double nqty) {
        this.nqty = nqty;
    }

    public double getNbags() {
        return nbags;
    }

    public void setNbags(double nbags) {
        this.nbags = nbags;
    }

    public String getNdoNo() {
        return ndoNo;
    }

    public void setNdoNo(String ndoNo) {
        this.ndoNo = ndoNo;
    }

    public String getVvehicleNo() {
        return vvehicleNo;
    }

    public void setVvehicleNo(String vvehicleNo) {
        this.vvehicleNo = vvehicleNo;
    }

    public String getVfinYear() {
        return vfinYear;
    }

    public void setVfinYear(String vfinYear) {
        this.vfinYear = vfinYear;
    }

    public double getNwtBag() {
        return nwtBag;
    }

    public void setNwtBag(double nwtBag) {
        this.nwtBag = nwtBag;
    }

    public int getNsugarFor() {
        return nsugarFor;
    }

    public void setNsugarFor(int nsugarFor) {
        this.nsugarFor = nsugarFor;
    }

    public String getVsugYearId() {
        return vsugYearId;
    }

    public void setVsugYearId(String vsugYearId) {
        this.vsugYearId = vsugYearId;
    }

    public String getDdoDate() {
        return ddoDate;
    }

    public void setDdoDate(String ddoDate) {
        this.ddoDate = ddoDate;
    }

    public String getLocationNameInward() {
        return locationNameInward;
    }

    public void setLocationNameInward(String locationNameInward) {
        this.locationNameInward = locationNameInward;
    }

    public String getSugarFor() {
        return sugarFor;
    }

    public void setSugarFor(String sugarFor) {
        this.sugarFor = sugarFor;
    }

}
