package com.twd.chitboyapp.spsskl.pojo;

import java.io.Serializable;

public class FarmerSugarResponse extends MainResponse implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String sugarYear;
    private String eventName;
    private String eventId;
    private String nentityUniId;
    private String ventityUniName;
    private String vvillname;
    private double sugarInKg;
    private double rate;
    private double amount;
    private double prevLat;
    private double prevLong;
    private String distUserName;
    private String prevDate;
    private String prevPhoto;
    private String prevLocation;
    private String blocked;

    public String getSugarYear() {
        return sugarYear;
    }

    public void setSugarYear(String sugarYear) {
        this.sugarYear = sugarYear;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getNentityUniId() {
        return nentityUniId;
    }

    public void setNentityUniId(String nentityUniId) {
        this.nentityUniId = nentityUniId;
    }

    public String getVentityUniName() {
        return ventityUniName;
    }

    public void setVentityUniName(String ventityUniName) {
        this.ventityUniName = ventityUniName;
    }

    public String getVvillname() {
        return vvillname;
    }

    public void setVvillname(String vvillname) {
        this.vvillname = vvillname;
    }

    public double getSugarInKg() {
        return sugarInKg;
    }

    public void setSugarInKg(double sugarInKg) {
        this.sugarInKg = sugarInKg;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrevLat() {
        return prevLat;
    }

    public void setPrevLat(double prevLat) {
        this.prevLat = prevLat;
    }

    public double getPrevLong() {
        return prevLong;
    }

    public void setPrevLong(double prevLong) {
        this.prevLong = prevLong;
    }

    public String getDistUserName() {
        return distUserName;
    }

    public void setDistUserName(String distUserName) {
        this.distUserName = distUserName;
    }

    public String getPrevDate() {
        return prevDate;
    }

    public void setPrevDate(String prevDate) {
        this.prevDate = prevDate;
    }

    public String getPrevPhoto() {
        return prevPhoto;
    }

    public void setPrevPhoto(String prevPhoto) {
        this.prevPhoto = prevPhoto;
    }

    public String getPrevLocation() {
        return prevLocation;
    }

    public void setPrevLocation(String prevLocation) {
        this.prevLocation = prevLocation;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }
}
