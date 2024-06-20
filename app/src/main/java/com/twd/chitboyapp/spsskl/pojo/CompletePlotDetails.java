package com.twd.chitboyapp.spsskl.pojo;

import java.io.Serializable;

public class CompletePlotDetails implements Serializable {
    private String nvillCode;
    private String nplotNo;
    private String nhangamCode;
    private String ntentativeArea;
    private String nvarietyCode;
    private String nexpectedYield;
    private String nharvestedTonnage;
    private String dplantationDate;
    private String nstatus; //1 open 2 close 3 close and registered
    private String vstatusName;
    private String dmindate;

    public String getNvillCode() {
        return nvillCode;
    }

    public void setNvillCode(String nvillCode) {
        this.nvillCode = nvillCode;
    }

    public String getNplotNo() {
        return nplotNo;
    }

    public void setNplotNo(String nplotNo) {
        this.nplotNo = nplotNo;
    }

    public String getNhangamCode() {
        return nhangamCode;
    }

    public void setNhangamCode(String nhangamCode) {
        this.nhangamCode = nhangamCode;
    }

    public String getNtentativeArea() {
        return ntentativeArea;
    }

    public void setNtentativeArea(String ntentativeArea) {
        this.ntentativeArea = ntentativeArea;
    }

    public String getNvarietyCode() {
        return nvarietyCode;
    }

    public void setNvarietyCode(String nvarietyCode) {
        this.nvarietyCode = nvarietyCode;
    }

    public String getNexpectedYield() {
        return nexpectedYield;
    }

    public void setNexpectedYield(String nexpectedYield) {
        this.nexpectedYield = nexpectedYield;
    }

    public String getNharvestedTonnage() {
        return nharvestedTonnage;
    }

    public void setNharvestedTonnage(String nharvestedTonnage) {
        this.nharvestedTonnage = nharvestedTonnage;
    }

    public String getDplantationDate() {
        return dplantationDate;
    }

    public void setDplantationDate(String dplantationDate) {
        this.dplantationDate = dplantationDate;
    }

    public String getNstatus() {
        return nstatus;
    }

    public void setNstatus(String nstatus) {
        this.nstatus = nstatus;
    }

    public String getVstatusName() {
        return vstatusName;
    }

    public void setVstatusName(String vstatusName) {
        this.vstatusName = vstatusName;
    }

    public String getDmindate() {
        return dmindate;
    }

    public void setDmindate(String dmindate) {
        this.dmindate = dmindate;
    }
}
