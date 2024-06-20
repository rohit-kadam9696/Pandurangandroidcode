package com.twd.chitboyapp.spsskl.pojo;

import com.twd.multispinnerfilter.KeyPairBoolData;

import java.io.Serializable;
import java.util.List;

public class HarvPlotDetailsResponse extends MainResponse implements Serializable {

    // Common for all
    private Integer openIntent;  //1. plantation, 2 extra plot request 3 extra plot request message
    private Integer nplotNo;

    private String vyearId;
    private String vfactNameLocal;
    private String ventityNameLocal;
    private String vfarmerTypeNameLocal;
    private String vsectionNameLocal;
    private String villageNameLocal;
    private String vhangamName;
    private String vvarietyName;
    private String vsurvyNo;
    private String ntentativeArea;
    private String nexpectedYield;
    private String nextendedTonnage;
    private String nallowedTon;
    private String ngpsDistance;
    private String nbalanceTonnage;
    private String nbullckcartLimit;

    private String dplantaitonDate;
    // common ended

    // non common
    // for 1 start
    private Integer nfactId;
    private String nentityUniId;
    private Integer nfarmerTypeId;
    private Integer nsectionId;
    private Integer nvillageId;
    private Integer ncropTypeId;
    private Integer nhangamId;
    private Integer nvarietyId;
    private Integer ndistance;

    // for 1 end

    private String nharvestedTonnage;// 2

    private String vstatusMessage;// 3
    // non common ended

    private List<KeyPairBoolData> reasonList;

    public Integer getOpenIntent() {
        return openIntent;
    }

    public void setOpenIntent(Integer openIntent) {
        this.openIntent = openIntent;
    }

    public Integer getNplotNo() {
        return nplotNo;
    }

    public void setNplotNo(Integer nplotNo) {
        this.nplotNo = nplotNo;
    }

    public String getVyearId() {
        return vyearId;
    }

    public void setVyearId(String vyearId) {
        this.vyearId = vyearId;
    }

    public String getVfactNameLocal() {
        return vfactNameLocal;
    }

    public void setVfactNameLocal(String vfactNameLocal) {
        this.vfactNameLocal = vfactNameLocal;
    }

    public String getVentityNameLocal() {
        return ventityNameLocal;
    }

    public void setVentityNameLocal(String ventityNameLocal) {
        this.ventityNameLocal = ventityNameLocal;
    }

    public String getVfarmerTypeNameLocal() {
        return vfarmerTypeNameLocal;
    }

    public void setVfarmerTypeNameLocal(String vfarmerTypeNameLocal) {
        this.vfarmerTypeNameLocal = vfarmerTypeNameLocal;
    }

    public String getVsectionNameLocal() {
        return vsectionNameLocal;
    }

    public void setVsectionNameLocal(String vsectionNameLocal) {
        this.vsectionNameLocal = vsectionNameLocal;
    }

    public String getVillageNameLocal() {
        return villageNameLocal;
    }

    public void setVillageNameLocal(String villageNameLocal) {
        this.villageNameLocal = villageNameLocal;
    }

    public String getVhangamName() {
        return vhangamName;
    }

    public void setVhangamName(String vhangamName) {
        this.vhangamName = vhangamName;
    }

    public String getVvarietyName() {
        return vvarietyName;
    }

    public void setVvarietyName(String vvarietyName) {
        this.vvarietyName = vvarietyName;
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

    public Integer getNfactId() {
        return nfactId;
    }

    public void setNfactId(Integer nfactId) {
        this.nfactId = nfactId;
    }

    public String getNentityUniId() {
        return nentityUniId;
    }

    public void setNentityUniId(String nentityUniId) {
        this.nentityUniId = nentityUniId;
    }

    public Integer getNfarmerTypeId() {
        return nfarmerTypeId;
    }

    public void setNfarmerTypeId(Integer nfarmerTypeId) {
        this.nfarmerTypeId = nfarmerTypeId;
    }

    public Integer getNsectionId() {
        return nsectionId;
    }

    public void setNsectionId(Integer nsectionId) {
        this.nsectionId = nsectionId;
    }

    public Integer getNvillageId() {
        return nvillageId;
    }

    public void setNvillageId(Integer nvillageId) {
        this.nvillageId = nvillageId;
    }

    public Integer getNcropTypeId() {
        return ncropTypeId;
    }

    public void setNcropTypeId(Integer ncropTypeId) {
        this.ncropTypeId = ncropTypeId;
    }

    public Integer getNhangamId() {
        return nhangamId;
    }

    public void setNhangamId(Integer nhangamId) {
        this.nhangamId = nhangamId;
    }

    public Integer getNvarietyId() {
        return nvarietyId;
    }

    public void setNvarietyId(Integer nvarietyId) {
        this.nvarietyId = nvarietyId;
    }

    public String getNharvestedTonnage() {
        return nharvestedTonnage;
    }

    public void setNharvestedTonnage(String nharvestedTonnage) {
        this.nharvestedTonnage = nharvestedTonnage;
    }

    public String getVstatusMessage() {
        return vstatusMessage;
    }

    public void setVstatusMessage(String vstatusMessage) {
        this.vstatusMessage = vstatusMessage;
    }

    public String getVsurvyNo() {
        return vsurvyNo;
    }

    public void setVsurvyNo(String vsurvyNo) {
        this.vsurvyNo = vsurvyNo;
    }

    public String getDplantaitonDate() {
        return dplantaitonDate;
    }

    public void setDplantaitonDate(String dplantaitonDate) {
        this.dplantaitonDate = dplantaitonDate;
    }

    public String getNgpsDistance() {
        return ngpsDistance;
    }

    public void setNgpsDistance(String ngpsDistance) {
        this.ngpsDistance = ngpsDistance;
    }

    public Integer getNdistance() {
        return ndistance;
    }

    public void setNdistance(Integer ndistance) {
        this.ndistance = ndistance;
    }

    public List<KeyPairBoolData> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<KeyPairBoolData> reasonList) {
        this.reasonList = reasonList;
    }

    public String getNextendedTonnage() {
        return nextendedTonnage;
    }

    public void setNextendedTonnage(String nextendedTonnage) {
        this.nextendedTonnage = nextendedTonnage;
    }

    public String getNbalanceTonnage() {
        return nbalanceTonnage;
    }

    public void setNbalanceTonnage(String nbalanceTonnage) {
        this.nbalanceTonnage = nbalanceTonnage;
    }

    public String getNbullckcartLimit() {
        return nbullckcartLimit;
    }

    public void setNbullckcartLimit(String nbullckcartLimit) {
        this.nbullckcartLimit = nbullckcartLimit;
    }

    public String getNallowedTon() {
        return nallowedTon;
    }

    public void setNallowedTon(String nallowedTon) {
        this.nallowedTon = nallowedTon;
    }
}
