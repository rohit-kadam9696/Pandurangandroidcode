package com.twd.chitboyapp.spsskl.pojo;

import java.util.List;

public class FarmerListResponse extends MainResponse {

    private List<EntityMasterDetail> entityMasterDetailList;

    public List<EntityMasterDetail> getEntityMasterDetailList() {
        return entityMasterDetailList;
    }

    public void setEntityMasterDetailList(List<EntityMasterDetail> entityMasterDetailList) {
        this.entityMasterDetailList = entityMasterDetailList;
    }
}
