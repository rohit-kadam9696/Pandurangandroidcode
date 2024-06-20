package com.twd.chitboyapp.spsskl.pojo;

import java.util.List;

public class PlantationBean {
    private String vyearId;
    private String nentityUniId;
    private Integer nplotNo;
    private Integer nhangamId;
    private String vsurveNo;
    private String dentryDate;
    private String dplantationDate;
    private Integer nirrigationId;
    private Integer nvarietyId;
    private Double narea;
    private Double nexpectedYield; // rujuwat
    private Integer nvillageId;
    private Double ndistance;
    private String createDate;
    private Integer nsectionId;
    private String nfarmerTypeId;
    private Double ngpsArea;
    private Double ngpsDistance;
    private Integer nconfirmFlag;
    private String vphotoPath;
    private String vconfirmphotoPath; // rujuwat
    private Double ntentativeArea; // rujuwat
    private Integer ncropwaterCondition; // plantation 0 rujuwat if (area == tentive area then 1)
    private Integer njuneFlag; // rujuwat
    private Integer naugustFlag; // rujuwat
    private String vsoilTest;
    private String vgreenFert;
    private Integer nlaneTypeId;
    private String vbeneTreat;
    private String vbesalDose;
    private String vdripUsed;
    private Integer nharvestTypeId;
    private String nplotNoOffline;
    private Integer nregFlagOffline;
    private Integer nconfFlagOffline; // rujuwat
    private Integer nentryType; // 3wd field 1 for lagan 2 for rujuwat
    private Integer ngpsFlag; // 1 : Walking 2 : Finger
    private String vstandingLatitude;
    private String vstandinglongitude;
    private Double nstandingacc;
    private List<PlantLocation> plantLocations;

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

    public Integer getNplotNo() {
        return nplotNo;
    }

    public void setNplotNo(Integer nplotNo) {
        this.nplotNo = nplotNo;
    }

    public Integer getNhangamId() {
        return nhangamId;
    }

    public void setNhangamId(Integer nhangamId) {
        this.nhangamId = nhangamId;
    }

    public String getVsurveNo() {
        return vsurveNo;
    }

    public void setVsurveNo(String vsurveNo) {
        this.vsurveNo = vsurveNo;
    }

    public String getDentryDate() {
        return dentryDate;
    }

    public void setDentryDate(String dentryDate) {
        this.dentryDate = dentryDate;
    }

    public String getDplantationDate() {
        return dplantationDate;
    }

    public void setDplantationDate(String dplantationDate) {
        this.dplantationDate = dplantationDate;
    }

    public Integer getNirrigationId() {
        return nirrigationId;
    }

    public void setNirrigationId(Integer nirrigationId) {
        this.nirrigationId = nirrigationId;
    }

    public Integer getNvarietyId() {
        return nvarietyId;
    }

    public void setNvarietyId(Integer nvarietyId) {
        this.nvarietyId = nvarietyId;
    }

    public Double getNarea() {
        return narea;
    }

    public void setNarea(Double narea) {
        this.narea = narea;
    }

    public Double getNexpectedYield() {
        return nexpectedYield;
    }

    public void setNexpectedYield(Double nexpectedYield) {
        this.nexpectedYield = nexpectedYield;
    }

    public Integer getNvillageId() {
        return nvillageId;
    }

    public void setNvillageId(Integer nvillageId) {
        this.nvillageId = nvillageId;
    }

    public Double getNdistance() {
        return ndistance;
    }

    public void setNdistance(Double ndistance) {
        this.ndistance = ndistance;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getNsectionId() {
        return nsectionId;
    }

    public void setNsectionId(Integer nsectionId) {
        this.nsectionId = nsectionId;
    }

    public String getNfarmerTypeId() {
        return nfarmerTypeId;
    }

    public void setNfarmerTypeId(String nfarmerTypeId) {
        this.nfarmerTypeId = nfarmerTypeId;
    }

    public Double getNgpsArea() {
        return ngpsArea;
    }

    public void setNgpsArea(Double ngpsArea) {
        this.ngpsArea = ngpsArea;
    }

    public Double getNgpsDistance() {
        return ngpsDistance;
    }

    public void setNgpsDistance(Double ngpsDistance) {
        this.ngpsDistance = ngpsDistance;
    }

    public Integer getNconfirmFlag() {
        return nconfirmFlag;
    }

    public void setNconfirmFlag(Integer nconfirmFlag) {
        this.nconfirmFlag = nconfirmFlag;
    }

    public String getVphotoPath() {
        return vphotoPath;
    }

    public void setVphotoPath(String vphotoPath) {
        this.vphotoPath = vphotoPath;
    }

    public String getVconfirmphotoPath() {
        return vconfirmphotoPath;
    }

    public void setVconfirmphotoPath(String vconfirmphotoPath) {
        this.vconfirmphotoPath = vconfirmphotoPath;
    }

    public Double getNtentativeArea() {
        return ntentativeArea;
    }

    public void setNtentativeArea(Double ntentativeArea) {
        this.ntentativeArea = ntentativeArea;
    }

    public Integer getNcropwaterCondition() {
        return ncropwaterCondition;
    }

    public void setNcropwaterCondition(Integer ncropwaterCondition) {
        this.ncropwaterCondition = ncropwaterCondition;
    }

    public Integer getNjuneFlag() {
        return njuneFlag;
    }

    public void setNjuneFlag(Integer njuneFlag) {
        this.njuneFlag = njuneFlag;
    }

    public Integer getNaugustFlag() {
        return naugustFlag;
    }

    public void setNaugustFlag(Integer naugustFlag) {
        this.naugustFlag = naugustFlag;
    }

    public String getVsoilTest() {
        return vsoilTest;
    }

    public void setVsoilTest(String vsoilTest) {
        this.vsoilTest = vsoilTest;
    }

    public String getVgreenFert() {
        return vgreenFert;
    }

    public void setVgreenFert(String vgreenFert) {
        this.vgreenFert = vgreenFert;
    }

    public Integer getNlaneTypeId() {
        return nlaneTypeId;
    }

    public void setNlaneTypeId(Integer nlaneTypeId) {
        this.nlaneTypeId = nlaneTypeId;
    }

    public String getVbeneTreat() {
        return vbeneTreat;
    }

    public void setVbeneTreat(String vbeneTreat) {
        this.vbeneTreat = vbeneTreat;
    }

    public String getVbesalDose() {
        return vbesalDose;
    }

    public void setVbesalDose(String vbesalDose) {
        this.vbesalDose = vbesalDose;
    }

    public String getVdripUsed() {
        return vdripUsed;
    }

    public void setVdripUsed(String vdripUsed) {
        this.vdripUsed = vdripUsed;
    }

    public Integer getNharvestTypeId() {
        return nharvestTypeId;
    }

    public void setNharvestTypeId(Integer nharvestTypeId) {
        this.nharvestTypeId = nharvestTypeId;
    }

    public String getNplotNoOffline() {
        return nplotNoOffline;
    }

    public void setNplotNoOffline(String nplotNoOffline) {
        this.nplotNoOffline = nplotNoOffline;
    }

    public Integer getNregFlagOffline() {
        return nregFlagOffline;
    }

    public void setNregFlagOffline(Integer nregFlagOffline) {
        this.nregFlagOffline = nregFlagOffline;
    }

    public Integer getNconfFlagOffline() {
        return nconfFlagOffline;
    }

    public void setNconfFlagOffline(Integer nconfFlagOffline) {
        this.nconfFlagOffline = nconfFlagOffline;
    }

    public Integer getNentryType() {
        return nentryType;
    }

    public void setNentryType(Integer nentryType) {
        this.nentryType = nentryType;
    }

    public List<PlantLocation> getPlantLocations() {
        return plantLocations;
    }

    public void setPlantLocations(List<PlantLocation> plantLocations) {
        this.plantLocations = plantLocations;
    }

    public Integer getNgpsFlag() {
        return ngpsFlag;
    }

    public void setNgpsFlag(Integer ngpsFlag) {
        this.ngpsFlag = ngpsFlag;
    }

    public String getVstandingLatitude() {
        return vstandingLatitude;
    }

    public void setVstandingLatitude(String vstandingLatitude) {
        this.vstandingLatitude = vstandingLatitude;
    }

    public String getVstandinglongitude() {
        return vstandinglongitude;
    }

    public void setVstandinglongitude(String vstandinglongitude) {
        this.vstandinglongitude = vstandinglongitude;
    }

    public Double getNstandingacc() {
        return nstandingacc;
    }

    public void setNstandingacc(Double nstandingacc) {
        this.nstandingacc = nstandingacc;
    }
}
