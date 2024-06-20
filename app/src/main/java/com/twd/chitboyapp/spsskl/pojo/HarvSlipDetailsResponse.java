package com.twd.chitboyapp.spsskl.pojo;

import com.twd.multispinnerfilter.KeyPairBoolData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class HarvSlipDetailsResponse extends HarvPlotDetailsResponse implements Serializable {

    private String nslipNo;
    private int ncropId;
    private int vehicleType;
    private String ndistancesave;
    private String nharvestorId;
    private String harvetorname;
    private String ngadiDokiId;
    private String harvetortype;
    private String ntransportorId;
    private String transportername;
    private String vvehicleNo;
    private String nbulluckcartMainId;
    private String bulluckcartMainName;
    private List<KeyPairBoolData> bulluckcartList;
    private List<KeyPairBoolData> wirerope;
    private List<KeyPairBoolData> wireropefront;
    private List<KeyPairBoolData> wireropeback;
    private HashMap<String, String> combos;
    private String nslipOfflineNo;
    private int nremarkId;
    private String slipDate;

    public String getNslipNo() {
        return nslipNo;
    }

    public void setNslipNo(String nslipNo) {
        this.nslipNo = nslipNo;
    }

    public int getNcropId() {
        return ncropId;
    }

    public void setNcropId(int ncropId) {
        this.ncropId = ncropId;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getNdistancesave() {
        return ndistancesave;
    }

    public void setNdistancesave(String ndistancesave) {
        this.ndistancesave = ndistancesave;
    }

    public String getNharvestorId() {
        return nharvestorId;
    }

    public void setNharvestorId(String nharvestorId) {
        this.nharvestorId = nharvestorId;
    }

    public String getHarvetorname() {
        return harvetorname;
    }

    public void setHarvetorname(String harvetorname) {
        this.harvetorname = harvetorname;
    }

    public String getNgadiDokiId() {
        return ngadiDokiId;
    }

    public void setNgadiDokiId(String ngadiDokiId) {
        this.ngadiDokiId = ngadiDokiId;
    }

    public String getHarvetortype() {
        return harvetortype;
    }

    public void setHarvetortype(String harvetortype) {
        this.harvetortype = harvetortype;
    }

    public String getNtransportorId() {
        return ntransportorId;
    }

    public void setNtransportorId(String ntransportorId) {
        this.ntransportorId = ntransportorId;
    }

    public String getTransportername() {
        return transportername;
    }

    public void setTransportername(String transportername) {
        this.transportername = transportername;
    }

    public String getVvehicleNo() {
        return vvehicleNo;
    }

    public void setVvehicleNo(String vvehicleNo) {
        this.vvehicleNo = vvehicleNo;
    }

    public String getNbulluckcartMainId() {
        return nbulluckcartMainId;
    }

    public void setNbulluckcartMainId(String nbulluckcartMainId) {
        this.nbulluckcartMainId = nbulluckcartMainId;
    }

    public String getBulluckcartMainName() {
        return bulluckcartMainName;
    }

    public void setBulluckcartMainName(String bulluckcartMainName) {
        this.bulluckcartMainName = bulluckcartMainName;
    }

    public List<KeyPairBoolData> getBulluckcartList() {
        return bulluckcartList;
    }

    public void setBulluckcartList(List<KeyPairBoolData> bulluckcartList) {
        this.bulluckcartList = bulluckcartList;
    }

    public List<KeyPairBoolData> getWirerope() {
        return wirerope;
    }

    public void setWirerope(List<KeyPairBoolData> wirerope) {
        this.wirerope = wirerope;
    }

    public List<KeyPairBoolData> getWireropefront() {
        return wireropefront;
    }

    public void setWireropefront(List<KeyPairBoolData> wireropefront) {
        this.wireropefront = wireropefront;
    }

    public List<KeyPairBoolData> getWireropeback() {
        return wireropeback;
    }

    public void setWireropeback(List<KeyPairBoolData> wireropeback) {
        this.wireropeback = wireropeback;
    }

    public HashMap<String, String> getCombos() {
        return combos;
    }

    public void setCombos(HashMap<String, String> combos) {
        this.combos = combos;
    }

    public String getNslipOfflineNo() {
        return nslipOfflineNo;
    }

    public void setNslipOfflineNo(String nslipOfflineNo) {
        this.nslipOfflineNo = nslipOfflineNo;
    }

    public int getNremarkId() {
        return nremarkId;
    }

    public void setNremarkId(int nremarkId) {
        this.nremarkId = nremarkId;
    }

    public String getSlipDate() {
        return slipDate;
    }

    public void setSlipDate(String slipDate) {
        this.slipDate = slipDate;
    }
}
