package com.twd.chitboyapp.spsskl.pojo;

public class CaneHarvestingPlanStart extends MainResponse {

    private String dentryDate;
    private String dstartDate;
    private String dendDate;
    private Integer ndaysPermit;
    private String nlimitTonnage;
    private String nlimitTonnageExtra;

    public String getDentryDate() {
        return dentryDate;
    }

    public void setDentryDate(String dentryDate) {
        this.dentryDate = dentryDate;
    }

    public Integer getNdaysPermit() {
        return ndaysPermit;
    }

    public void setNdaysPermit(Integer ndaysPermit) {
        this.ndaysPermit = ndaysPermit;
    }

    public String getDstartDate() {
        return dstartDate;
    }

    public void setDstartDate(String dstartDate) {
        this.dstartDate = dstartDate;
    }

    public String getDendDate() {
        return dendDate;
    }

    public void setDendDate(String dendDate) {
        this.dendDate = dendDate;
    }

    public String getNlimitTonnage() {
        return nlimitTonnage;
    }

    public void setNlimitTonnage(String nlimitTonnage) {
        this.nlimitTonnage = nlimitTonnage;
    }

    public String getNlimitTonnageExtra() {
        return nlimitTonnageExtra;
    }

    public void setNlimitTonnageExtra(String nlimitTonnageExtra) {
        this.nlimitTonnageExtra = nlimitTonnageExtra;
    }
}