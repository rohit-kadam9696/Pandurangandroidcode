package com.twd.chitboyapp.spsskl.pojo;

import java.util.List;

public class RemainingSlipResponse extends MainResponse {
    private List<RemainingSlipBean> remainingSlipBeans;

    public List<RemainingSlipBean> getRemainingSlipBeans() {
        return remainingSlipBeans;
    }

    public void setRemainingSlipBeans(List<RemainingSlipBean> remainingSlipBeans) {
        this.remainingSlipBeans = remainingSlipBeans;
    }
}
