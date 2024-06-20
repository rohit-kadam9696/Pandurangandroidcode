package com.twd.chitboyapp.spsskl.pojo;

import com.twd.multispinnerfilter.KeyPairBoolData;

import java.util.List;

public class VillageResonse extends MainResponse {
    private List<KeyPairBoolData> villList;

    public List<KeyPairBoolData> getVillList() {
        return villList;
    }

    public void setVillList(List<KeyPairBoolData> villList) {
        this.villList = villList;
    }
}