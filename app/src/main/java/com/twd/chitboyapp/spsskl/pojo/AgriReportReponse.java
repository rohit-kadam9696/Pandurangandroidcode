package com.twd.chitboyapp.spsskl.pojo;

public class AgriReportReponse extends MainResponse {
    private TableReportBean nondSummary;
    private TableReportBean nondAndExpTonnage;
    private TableReportBean sectionHangamNond;
    private TableReportBean sectionVarietyNond;
    private TableReportBean hangamVarietyNond;

    public TableReportBean getNondSummary() {
        return nondSummary;
    }

    public void setNondSummary(TableReportBean nondSummary) {
        this.nondSummary = nondSummary;
    }

    public TableReportBean getSectionVarietyNond() {
        return sectionVarietyNond;
    }

    public void setSectionVarietyNond(TableReportBean sectionVarietyNond) {
        this.sectionVarietyNond = sectionVarietyNond;
    }

    public TableReportBean getHangamVarietyNond() {
        return hangamVarietyNond;
    }

    public void setHangamVarietyNond(TableReportBean hangamVarietyNond) {
        this.hangamVarietyNond = hangamVarietyNond;
    }

    public TableReportBean getNondAndExpTonnage() {
        return nondAndExpTonnage;
    }

    public void setNondAndExpTonnage(TableReportBean nondAndExpTonnage) {
        this.nondAndExpTonnage = nondAndExpTonnage;
    }

    public TableReportBean getSectionHangamNond() {
        return sectionHangamNond;
    }

    public void setSectionHangamNond(TableReportBean sectionHangamNond) {
        this.sectionHangamNond = sectionHangamNond;
    }

}
