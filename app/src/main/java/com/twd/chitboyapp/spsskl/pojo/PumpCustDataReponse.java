package com.twd.chitboyapp.spsskl.pojo;

public class PumpCustDataReponse extends MainResponse {

    private String customerName;
    private int customerTypeId;
    private String customerCode;
    private String vvehicleNo;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(int customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getVvehicleNo() {
        return vvehicleNo;
    }

    public void setVvehicleNo(String vvehicleNo) {
        this.vvehicleNo = vvehicleNo;
    }
}
