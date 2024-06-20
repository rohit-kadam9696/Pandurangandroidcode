package com.twd.chitboyapp.spsskl.pojo;

import java.io.Serializable;
import java.util.List;

public class CompletePlotResponse extends MainResponse implements Serializable {
    private String vyearId;
    private String nentityUniId;
    private String vfarmerName;
    private String vvillageName;
    private String vsectName;

    private List<CompletePlotDetails> plotList;

    public String getVyearId() {
        return vyearId;
    }

    public void setVyearId(String vyearId) {
        this.vyearId = vyearId;
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

    public String getVvillageName() {
        return vvillageName;
    }

    public void setVvillageName(String vvillageName) {
        this.vvillageName = vvillageName;
    }

    public String getVsectName() {
        return vsectName;
    }

    public void setVsectName(String vsectName) {
        this.vsectName = vsectName;
    }

    public List<CompletePlotDetails> getPlotList() {
        return plotList;
    }

    public void setPlotList(List<CompletePlotDetails> plotList) {
        this.plotList = plotList;
    }
}
