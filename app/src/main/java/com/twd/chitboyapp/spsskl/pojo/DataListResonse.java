package com.twd.chitboyapp.spsskl.pojo;

import com.twd.multispinnerfilter.KeyPairBoolData;

import java.util.List;

public class DataListResonse extends MainResponse {
    private List<KeyPairBoolData> dataList;

    public List<KeyPairBoolData> getDataList() {
        return dataList;
    }

    public void setDataList(List<KeyPairBoolData> dataList) {
        this.dataList = dataList;
    }
}