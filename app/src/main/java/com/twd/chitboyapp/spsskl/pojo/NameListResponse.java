package com.twd.chitboyapp.spsskl.pojo;

import java.util.List;

public class NameListResponse extends MainResponse {

    private List<NameData> nameDataList;

    public List<NameData> getNameDataList() {
        return nameDataList;
    }

    public void setNameDataList(List<NameData> nameDataList) {
        this.nameDataList = nameDataList;
    }
}
