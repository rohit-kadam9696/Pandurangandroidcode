package com.twd.chitboyapp.spsskl.pojo;

import java.io.Serializable;

public class ExcessTonPlotReqDataResponse extends MainResponse implements Serializable {
    private String nplotNo;
    private String nentityUniId;
    private String vfarmerName;
    private String ntentativeArea;
    private String nexpectedYield;
    private String nlimitYield;
    private String nharvestedTonnage;
    private String nremainingArea;
    private String nremainingTonnage;
    private String vreasonName;
    private String vchitboyname;

    public String getNplotNo() {
        return nplotNo;
    }

    public void setNplotNo(String nplotNo) {
        this.nplotNo = nplotNo;
    }

    public String getNentityUniId() {
        return nentityUniId;
    }

    public void setNentityUniId(String nentityUniId) {
        this.nentityUniId = nentityUniId;
    }

    public String getVfarmerName() {
        return vfarmerName;
    }

    public void setVfarmerName(String vfarmerName) {
        this.vfarmerName = vfarmerName;
    }

    public String getNtentativeArea() {
        return ntentativeArea;
    }

    public void setNtentativeArea(String ntentativeArea) {
        this.ntentativeArea = ntentativeArea;
    }

    public String getNexpectedYield() {
        return nexpectedYield;
    }

    public void setNexpectedYield(String nexpectedYield) {
        this.nexpectedYield = nexpectedYield;
    }

    public String getNlimitYield() {
        return nlimitYield;
    }

    public void setNlimitYield(String nlimitYield) {
        this.nlimitYield = nlimitYield;
    }

    public String getNharvestedTonnage() {
        return nharvestedTonnage;
    }

    public void setNharvestedTonnage(String nharvestedTonnage) {
        this.nharvestedTonnage = nharvestedTonnage;
    }

    public String getNremainingArea() {
        return nremainingArea;
    }

    public void setNremainingArea(String nremainingArea) {
        this.nremainingArea = nremainingArea;
    }

    public String getNremainingTonnage() {
        return nremainingTonnage;
    }

    public void setNremainingTonnage(String nremainingTonnage) {
        this.nremainingTonnage = nremainingTonnage;
    }

    public String getVreasonName() {
        return vreasonName;
    }

    public void setVreasonName(String vreasonName) {
        this.vreasonName = vreasonName;
    }

    public String getVchitboyname() {
        return vchitboyname;
    }

    public void setVchitboyname(String vchitboyname) {
        this.vchitboyname = vchitboyname;
    }
}
