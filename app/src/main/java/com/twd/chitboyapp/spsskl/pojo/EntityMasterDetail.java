package com.twd.chitboyapp.spsskl.pojo;

import java.io.Serializable;

public class EntityMasterDetail implements Serializable {

    private String nentityUniId;
    private String ventityNameLocal;
    private String vmobileNo;
    private int nbankId;
    private String vbankAcNo;
    private int nvillageId;
    private int nfarmerTypeId;
    private String nuserName; // case of plantation permission
    private String status; // case of plantation permission
    private String villname; // case of plantation permission

    /**
     * @return the nentityUniId
     */
    public String getNentityUniId() {
        return nentityUniId;
    }

    /**
     * @param nentityUniId the nentityUniId to set
     */
    public void setNentityUniId(String nentityUniId) {
        this.nentityUniId = nentityUniId;
    }

    /**
     * @return the ventityNameLocal
     */
    public String getVentityNameLocal() {
        return ventityNameLocal;
    }

    /**
     * @param ventityNameLocal the ventityNameLocal to set
     */
    public void setVentityNameLocal(String ventityNameLocal) {
        this.ventityNameLocal = ventityNameLocal;
    }

    /**
     * @return the vmobileNo
     */
    public String getVmobileNo() {
        return vmobileNo;
    }

    /**
     * @param vmobileNo the vmobileNo to set
     */
    public void setVmobileNo(String vmobileNo) {
        this.vmobileNo = vmobileNo;
    }

    /**
     * @return the nbankId
     */
    public int getNbankId() {
        return nbankId;
    }

    /**
     * @param nbankId the nbankId to set
     */
    public void setNbankId(int nbankId) {
        this.nbankId = nbankId;
    }

    /**
     * @return the vbankAcNo
     */
    public String getVbankAcNo() {
        return vbankAcNo;
    }

    /**
     * @param vbankAcNo the vbankAcNo to set
     */
    public void setVbankAcNo(String vbankAcNo) {
        this.vbankAcNo = vbankAcNo;
    }

    /**
     * @return the nvillageId
     */
    public int getNvillageId() {
        return nvillageId;
    }

    /**
     * @param nvillageId the nvillageId to set
     */
    public void setNvillageId(int nvillageId) {
        this.nvillageId = nvillageId;
    }

    /**
     * @return the nfarmerTypeId
     */
    public int getNfarmerTypeId() {
        return nfarmerTypeId;
    }

    /**
     * @param nfarmerTypeId the nfarmerTypeId to set
     */
    public void setNfarmerTypeId(int nfarmerTypeId) {
        this.nfarmerTypeId = nfarmerTypeId;
    }

    public String getNuserName() {
        return nuserName;
    }

    public void setNuserName(String nuserName) {
        this.nuserName = nuserName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVillname() {
        return villname;
    }

    public void setVillname(String villname) {
        this.villname = villname;
    }
}
