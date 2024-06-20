package com.twd.chitboyapp.spsskl.pojo;

import java.util.List;

public class WeightSlipResponse extends ActionResponse {

    private String date;
    private List<SlipBeanR> slipBeanrList;

    public List<SlipBeanR> getSlipBeanrList() {
        return slipBeanrList;
    }

    public void setSlipBeanrList(List<SlipBeanR> slipBeanrList) {
        this.slipBeanrList = slipBeanrList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
