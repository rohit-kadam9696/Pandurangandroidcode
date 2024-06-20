package com.twd.chitboyapp.spsskl.pojo;

public class HarvReportReponse extends MainResponse {
    private TableReportBean dailyCaneInward;
    private TableReportBean remainingCaneInfo;

    public TableReportBean getDailyCaneInward() {
        return dailyCaneInward;
    }

    public void setDailyCaneInward(TableReportBean dailyCaneInward) {
        this.dailyCaneInward = dailyCaneInward;
    }

    public TableReportBean getRemainingCaneInfo() {
        return remainingCaneInfo;
    }

    public void setRemainingCaneInfo(TableReportBean remainingCaneInfo) {
        this.remainingCaneInfo = remainingCaneInfo;
    }
}
