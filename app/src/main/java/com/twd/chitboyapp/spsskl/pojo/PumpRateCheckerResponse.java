package com.twd.chitboyapp.spsskl.pojo;

public class PumpRateCheckerResponse extends ActionResponse {
    public String pumpRate;
    public String transId;

    public String getPumpRate() {
        return pumpRate;
    }

    public void setPumpRate(String pumpRate) {
        this.pumpRate = pumpRate;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
}
