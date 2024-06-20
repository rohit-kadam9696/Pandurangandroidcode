package com.twd.chitboyapp.spsskl.pojo;

public class FarmerResponse extends MainResponse {
    private EntityMasterDetail entityMasterDetail;

    public EntityMasterDetail getEntityMasterDetail() {
        return entityMasterDetail;
    }

    public void setEntityMasterDetail(EntityMasterDetail entityMasterDetail) {
        this.entityMasterDetail = entityMasterDetail;
    }

    private String successMessage;

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
