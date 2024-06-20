package com.twd.chitboyapp.spsskl.pojo;

public class NumIndResponse extends MainResponse {
    private String transName, vvehicleNo, tokenDate, tokenNo, totalWaiting, photopath, yardId, slipNo, transId;

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public String getVvehicleNo() {
        return vvehicleNo;
    }

    public void setVvehicleNo(String vvehicleNo) {
        this.vvehicleNo = vvehicleNo;
    }

    public String getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(String tokenDate) {
        this.tokenDate = tokenDate;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getTotalWaiting() {
        return totalWaiting;
    }

    public void setTotalWaiting(String totalWaiting) {
        this.totalWaiting = totalWaiting;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public String getYardId() {
        return yardId;
    }

    public void setYardId(String yardId) {
        this.yardId = yardId;
    }

    public String getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(String slipNo) {
        this.slipNo = slipNo;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
}
