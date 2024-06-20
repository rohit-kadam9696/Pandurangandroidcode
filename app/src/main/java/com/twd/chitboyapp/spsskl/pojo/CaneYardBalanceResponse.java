package com.twd.chitboyapp.spsskl.pojo;

public class CaneYardBalanceResponse extends MainResponse {

    private TableReportBean inyardVehicle, outyardVehicle, emptyVehicle;
    private String dateTime;

    public TableReportBean getInyardVehicle() {
        return inyardVehicle;
    }

    public void setInyardVehicle(TableReportBean inyardVehicle) {
        this.inyardVehicle = inyardVehicle;
    }

    public TableReportBean getOutyardVehicle() {
        return outyardVehicle;
    }

    public void setOutyardVehicle(TableReportBean outyardVehicle) {
        this.outyardVehicle = outyardVehicle;
    }

    public TableReportBean getEmptyVehicle() {
        return emptyVehicle;
    }

    public void setEmptyVehicle(TableReportBean emptyVehicle) {
        this.emptyVehicle = emptyVehicle;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
