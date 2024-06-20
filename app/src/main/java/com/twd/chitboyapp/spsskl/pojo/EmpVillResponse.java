package com.twd.chitboyapp.spsskl.pojo;

public class EmpVillResponse extends ActionResponse {
    private TableReportBean villageList;
    private String employeeName;
    private String employeeCode;

    public TableReportBean getVillageList() {
        return villageList;
    }

    public void setVillageList(TableReportBean villageList) {
        this.villageList = villageList;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
}
