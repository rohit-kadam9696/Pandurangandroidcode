package com.twd.chitboyapp.spsskl.pojo;

public class BranchResponse extends MainResponse {
    private int nbranchId;
    private String vbranchName;
    private String ifscCode;

    public int getNbranchId() {
        return nbranchId;
    }

    public void setNbranchId(int nbranchId) {
        this.nbranchId = nbranchId;
    }

    public String getVbranchName() {
        return vbranchName;
    }

    public void setVbranchName(String vbranchName) {
        this.vbranchName = vbranchName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }
}