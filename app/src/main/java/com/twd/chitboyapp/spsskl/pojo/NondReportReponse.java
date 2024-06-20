package com.twd.chitboyapp.spsskl.pojo;

public class NondReportReponse extends NondReportHangamReponse {
    private TableReportBean nondSummary;
    private TableReportBean hangamSummary;
    private TableReportBean varietySummary;



    public TableReportBean getNondSummary() {
        return nondSummary;
    }

    public void setNondSummary(TableReportBean nondSummary) {
        this.nondSummary = nondSummary;
    }

    public TableReportBean getHangamSummary() {
        return hangamSummary;
    }

    public void setHangamSummary(TableReportBean hangamSummary) {
        this.hangamSummary = hangamSummary;
    }

    public TableReportBean getVarietySummary() {
        return varietySummary;
    }

    public void setVarietySummary(TableReportBean varietySummary) {
        this.varietySummary = varietySummary;
    }


}
