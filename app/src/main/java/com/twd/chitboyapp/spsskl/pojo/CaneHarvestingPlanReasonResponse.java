package com.twd.chitboyapp.spsskl.pojo;

import com.twd.multispinnerfilter.KeyPairBoolData;

import java.util.List;

public class CaneHarvestingPlanReasonResponse extends MainResponse {
    private List<KeyPairBoolData> reasonList;

    public List<KeyPairBoolData> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<KeyPairBoolData> reasonList) {
        this.reasonList = reasonList;
    }
}