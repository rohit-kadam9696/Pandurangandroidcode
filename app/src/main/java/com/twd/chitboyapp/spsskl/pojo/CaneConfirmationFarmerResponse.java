package com.twd.chitboyapp.spsskl.pojo;

import java.util.List;

public class CaneConfirmationFarmerResponse extends MainResponse {
    private String farmerName;
    private String farmerVilleageName;
    private String sectionCode;
    private List<CaneConfirmationRegistrationList> list;

    public String getFarmerVilleageName() {
        return farmerVilleageName;
    }

    public void setFarmerVilleageName(String farmerVilleageName) {
        this.farmerVilleageName = farmerVilleageName;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public List<CaneConfirmationRegistrationList> getList() {
        return list;
    }

    public void setList(List<CaneConfirmationRegistrationList> list) {
        this.list = list;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }
}
