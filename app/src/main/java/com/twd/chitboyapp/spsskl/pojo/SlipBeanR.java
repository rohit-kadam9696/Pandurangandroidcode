package com.twd.chitboyapp.spsskl.pojo;

import java.io.Serializable;

public class SlipBeanR implements Serializable {
    private String nbullokCode;
    private String nslipNo;
    private String slipDate;
    private String extraQr;

    public String getNbullokCode() {
        return nbullokCode;
    }

    public void setNbullokCode(String nbullokCode) {
        this.nbullokCode = nbullokCode;
    }

    public String getNslipNo() {
        return nslipNo;
    }

    public void setNslipNo(String nslipNo) {
        this.nslipNo = nslipNo;
    }

    public String getSlipDate() {
        return slipDate;
    }

    public void setSlipDate(String slipDate) {
        this.slipDate = slipDate;
    }

    public String getExtraQr() {
        return extraQr;
    }

    public void setExtraQr(String extraQr) {
        this.extraQr = extraQr;
    }
}
