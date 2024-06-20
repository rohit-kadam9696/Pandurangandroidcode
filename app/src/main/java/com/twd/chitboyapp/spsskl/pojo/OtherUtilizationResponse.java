package com.twd.chitboyapp.spsskl.pojo;

import com.twd.multispinnerfilter.KeyPairBoolData;

import java.io.Serializable;
import java.util.List;

public class OtherUtilizationResponse extends MainResponse implements Serializable {
    private String nplotNo;
    private String nentityUniId;
    private String vfarmerName;
    private String vvillageName;
    private String nhangamCode;
    private String nvarietyCode;
    private String ntentativeArea;
    private String nutilizationArea;
    private String nexpectedYield;
    private List<KeyPairBoolData> remarkList;
    private List<KeyPairBoolData> factoryList;

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

    public String getVvillageName() {
        return vvillageName;
    }

    public void setVvillageName(String vvillageName) {
        this.vvillageName = vvillageName;
    }

    public String getNhangamCode() {
        return nhangamCode;
    }

    public void setNhangamCode(String nhangamCode) {
        this.nhangamCode = nhangamCode;
    }

    public String getNvarietyCode() {
        return nvarietyCode;
    }

    public void setNvarietyCode(String nvarietyCode) {
        this.nvarietyCode = nvarietyCode;
    }

    public String getNtentativeArea() {
        return ntentativeArea;
    }

    public void setNtentativeArea(String ntentativeArea) {
        this.ntentativeArea = ntentativeArea;
    }

    public List<KeyPairBoolData> getRemarkList() {
        return remarkList;
    }

    public void setRemarkList(List<KeyPairBoolData> remarkList) {
        this.remarkList = remarkList;
    }

    public List<KeyPairBoolData> getFactoryList() {
        return factoryList;
    }

    public void setFactoryList(List<KeyPairBoolData> factoryList) {
        this.factoryList = factoryList;
    }

    public String getNexpectedYield() {
        return nexpectedYield;
    }

    public void setNexpectedYield(String nexpectedYield) {
        this.nexpectedYield = nexpectedYield;
    }

    public String getNutilizationArea() {
        return nutilizationArea;
    }

    public void setNutilizationArea(String nutilizationArea) {
        this.nutilizationArea = nutilizationArea;
    }
}