package com.twd.chitboyapp.spsskl.pojo;

import com.twd.multispinnerfilter.KeyPairBoolData;

import java.util.List;

public class CrushingReportReponse extends CrushingPlantHarvVillResponse {
    private String nhangamDay;
    private TableReportBean dailyCrushing;
    private TableReportBean dailyLabSummary;
    private TableReportBean sectionTonnage;
    private TableReportBean hangamTonnage;
    private TableReportBean varietyTonnage;
    private TableReportBean cropTypeTonnage;
    private TableReportBean remainCane;
    private List<KeyPairBoolData> sectionList;

    public String getNhangamDay() {
        return nhangamDay;
    }

    public void setNhangamDay(String nhangamDay) {
        this.nhangamDay = nhangamDay;
    }

    public TableReportBean getDailyCrushing() {
        return dailyCrushing;
    }

    public void setDailyCrushing(TableReportBean dailyCrushing) {
        this.dailyCrushing = dailyCrushing;
    }

    public TableReportBean getDailyLabSummary() {
        return dailyLabSummary;
    }

    public void setDailyLabSummary(TableReportBean dailyLabSummary) {
        this.dailyLabSummary = dailyLabSummary;
    }

    public TableReportBean getSectionTonnage() {
        return sectionTonnage;
    }

    public void setSectionTonnage(TableReportBean sectionTonnage) {
        this.sectionTonnage = sectionTonnage;
    }

    public TableReportBean getHangamTonnage() {
        return hangamTonnage;
    }

    public void setHangamTonnage(TableReportBean hangamTonnage) {
        this.hangamTonnage = hangamTonnage;
    }

    public TableReportBean getVarietyTonnage() {
        return varietyTonnage;
    }

    public void setVarietyTonnage(TableReportBean varietyTonnage) {
        this.varietyTonnage = varietyTonnage;
    }

    public TableReportBean getCropTypeTonnage() {
        return cropTypeTonnage;
    }

    public void setCropTypeTonnage(TableReportBean cropTypeTonnage) {
        this.cropTypeTonnage = cropTypeTonnage;
    }

    public List<KeyPairBoolData> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<KeyPairBoolData> sectionList) {
        this.sectionList = sectionList;
    }

    public TableReportBean getRemainCane() {
        return remainCane;
    }

    public void setRemainCane(TableReportBean remainCane) {
        this.remainCane = remainCane;
    }
}
