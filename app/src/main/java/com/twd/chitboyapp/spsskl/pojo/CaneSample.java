package com.twd.chitboyapp.spsskl.pojo;

import java.io.Serializable;

public class CaneSample implements Serializable {

    private String nbrix;
    private String npole;
    private String npurity;
    private String nrecovery;
    private String vyearId;
    private String sampleDate;

    public String getNbrix() {
        return nbrix;
    }

    public void setNbrix(String nbrix) {
        this.nbrix = nbrix;
    }

    public String getNpole() {
        return npole;
    }

    public void setNpole(String npole) {
        this.npole = npole;
    }

    public String getNpurity() {
        return npurity;
    }

    public void setNpurity(String npurity) {
        this.npurity = npurity;
    }

    public String getNrecovery() {
        return nrecovery;
    }

    public void setNrecovery(String nrecovery) {
        this.nrecovery = nrecovery;
    }

    public String getVyearId() {
        return vyearId;
    }

    public void setVyearId(String vyearId) {
        this.vyearId = vyearId;
    }

    public String getSampleDate() {
        return sampleDate;
    }

    public void setSampleDate(String sampleDate) {
        this.sampleDate = sampleDate;
    }


}
