package com.twd.chitboyapp.spsskl.pojo;

import com.twd.multispinnerfilter.KeyPairBoolData;

import java.util.List;

public class BulluckCartResponse extends WireRopeResonse {
    private String mukadamCode;
    private String mukadamName;
    private String vehicalNo;
    private List<KeyPairBoolData> bulluckcartList;

    public String getMukadamCode() {
        return mukadamCode;
    }

    public void setMukadamCode(String mukadamCode) {
        this.mukadamCode = mukadamCode;
    }

    public String getMukadamName() {
        return mukadamName;
    }

    public void setMukadamName(String mukadamName) {
        this.mukadamName = mukadamName;
    }

    public List<KeyPairBoolData> getBulluckcartList() {
        return bulluckcartList;
    }

    public void setBulluckcartList(List<KeyPairBoolData> bulluckcartList) {
        this.bulluckcartList = bulluckcartList;
    }

    public String getVehicalNo() {
        return vehicalNo;
    }

    public void setVehicalNo(String vehicalNo) {
        this.vehicalNo = vehicalNo;
    }
}
