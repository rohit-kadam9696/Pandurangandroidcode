package com.twd.chitboyapp.spsskl.pojo;

import java.util.List;

public class NumSlipListResponse extends MainResponse {
    private List<NumSlip> numSlips;

    public List<NumSlip> getNumSlips() {
        return numSlips;
    }

    public void setNumSlips(List<NumSlip> numSlips) {
        this.numSlips = numSlips;
    }
}
