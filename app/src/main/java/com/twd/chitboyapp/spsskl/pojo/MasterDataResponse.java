package com.twd.chitboyapp.spsskl.pojo;

import java.util.ArrayList;
import java.util.List;

public class MasterDataResponse extends MainResponse {
    private ArrayList<BankMaster> bankList;
    private ArrayList<CropTypeMaster> cropTypeList;
    private ArrayList<EntityMasterDetail> farmerList;
    private ArrayList<FarmerCategoryMaster> farmerCategoryList;
    private ArrayList<FarmerTypeMaster> farmerTypeList;
    private ArrayList<HangamMaster> hangamList;
    private ArrayList<HarvestingType> harvestingTypeList;
    private ArrayList<IrrigationTypeMaster> irrigationTypeList;
    private ArrayList<LaneType> lagneTypekList;
    private ArrayList<MenuBean> menuList;
    private ArrayList<SectionMaster> sectionList;
    private ArrayList<VarietyMaster> varietyList;
    private ArrayList<VehicleTypeMaster> vehicleTypeList;
    private ArrayList<VehicleTypeMaster> plantationVehicleTypeList;
    private ArrayList<VillageMaster> villageList;
    private ArrayList<CropWater> cropWaterList;
    private List<CaneConfirmationRegistrationList> caneConfirmList;
    private List<CaneTypeMaster> caneTypeMasters;
    private List<ReasonMaster> reasonMasters;
    private List<ReasonMaster> reasonAllMasters;

    private List<FertSaleType> fertSaleTypes;
    private List<CaneYard> caneYards;
    private List<Pump> pumps;
    private List<PumpSaleType> pumpSaleTypes;
    private List<VehicleGroupMaster> vehicleGroupMasters;

    private List<CustomerType> customerTypes;

    public ArrayList<BankMaster> getBankList() {
        return bankList;
    }

    public void setBankList(ArrayList<BankMaster> bankList) {
        this.bankList = bankList;
    }

    public ArrayList<CropTypeMaster> getCropTypeList() {
        return cropTypeList;
    }

    public void setCropTypeList(ArrayList<CropTypeMaster> cropTypeList) {
        this.cropTypeList = cropTypeList;
    }

    public ArrayList<EntityMasterDetail> getFarmerList() {
        return farmerList;
    }

    public void setFarmerList(ArrayList<EntityMasterDetail> farmerList) {
        this.farmerList = farmerList;
    }

    public ArrayList<FarmerCategoryMaster> getFarmerCategoryList() {
        return farmerCategoryList;
    }

    public void setFarmerCategoryList(ArrayList<FarmerCategoryMaster> farmerCategoryList) {
        this.farmerCategoryList = farmerCategoryList;
    }

    public ArrayList<FarmerTypeMaster> getFarmerTypeList() {
        return farmerTypeList;
    }

    public void setFarmerTypeList(ArrayList<FarmerTypeMaster> farmerTypeList) {
        this.farmerTypeList = farmerTypeList;
    }

    public ArrayList<HangamMaster> getHangamList() {
        return hangamList;
    }

    public void setHangamList(ArrayList<HangamMaster> hangamList) {
        this.hangamList = hangamList;
    }

    public ArrayList<HarvestingType> getHarvestingTypeList() {
        return harvestingTypeList;
    }

    public void setHarvestingTypeList(ArrayList<HarvestingType> harvestingTypeList) {
        this.harvestingTypeList = harvestingTypeList;
    }

    public ArrayList<IrrigationTypeMaster> getIrrigationTypeList() {
        return irrigationTypeList;
    }

    public void setIrrigationTypeList(ArrayList<IrrigationTypeMaster> irrigationTypeList) {
        this.irrigationTypeList = irrigationTypeList;
    }

    public ArrayList<LaneType> getLagneTypekList() {
        return lagneTypekList;
    }

    public void setLagneTypekList(ArrayList<LaneType> lagneTypekList) {
        this.lagneTypekList = lagneTypekList;
    }

    public ArrayList<MenuBean> getMenuList() {
        return menuList;
    }

    public void setMenuList(ArrayList<MenuBean> menuList) {
        this.menuList = menuList;
    }

    public ArrayList<SectionMaster> getSectionList() {
        return sectionList;
    }

    public void setSectionList(ArrayList<SectionMaster> sectionList) {
        this.sectionList = sectionList;
    }

    public ArrayList<VarietyMaster> getVarietyList() {
        return varietyList;
    }

    public void setVarietyList(ArrayList<VarietyMaster> varietyList) {
        this.varietyList = varietyList;
    }

    public ArrayList<VehicleTypeMaster> getVehicleTypeList() {
        return vehicleTypeList;
    }

    public void setVehicleTypeList(ArrayList<VehicleTypeMaster> vehicleTypeList) {
        this.vehicleTypeList = vehicleTypeList;
    }

    public ArrayList<VillageMaster> getVillageList() {
        return villageList;
    }

    public void setVillageList(ArrayList<VillageMaster> villageList) {
        this.villageList = villageList;
    }

    /**
     * @return the plantationVehicleTypeList
     */
    public ArrayList<VehicleTypeMaster> getPlantationVehicleTypeList() {
        return plantationVehicleTypeList;
    }

    /**
     * @param plantationVehicleTypeList the plantationVehicleTypeList to set
     */
    public void setPlantationVehicleTypeList(ArrayList<VehicleTypeMaster> plantationVehicleTypeList) {
        this.plantationVehicleTypeList = plantationVehicleTypeList;
    }

    public ArrayList<CropWater> getCropWaterList() {
        return cropWaterList;
    }

    public void setCropWaterList(ArrayList<CropWater> cropWaterList) {
        this.cropWaterList = cropWaterList;
    }

    public List<CaneConfirmationRegistrationList> getCaneConfirmList() {
        return caneConfirmList;
    }

    public void setCaneConfirmList(List<CaneConfirmationRegistrationList> caneConfirmList) {
        this.caneConfirmList = caneConfirmList;
    }

    public List<CaneTypeMaster> getCaneTypeMasters() {
        return caneTypeMasters;
    }

    public void setCaneTypeMasters(List<CaneTypeMaster> caneTypeMasters) {
        this.caneTypeMasters = caneTypeMasters;
    }

    public List<ReasonMaster> getReasonMasters() {
        return reasonMasters;
    }

    public void setReasonMasters(List<ReasonMaster> reasonMasters) {
        this.reasonMasters = reasonMasters;
    }

    public List<ReasonMaster> getReasonAllMasters() {
        return reasonAllMasters;
    }

    public void setReasonAllMasters(List<ReasonMaster> reasonAllMasters) {
        this.reasonAllMasters = reasonAllMasters;
    }

    public List<FertSaleType> getFertSaleTypes() {
        return fertSaleTypes;
    }

    public void setFertSaleTypes(List<FertSaleType> fertSaleTypes) {
        this.fertSaleTypes = fertSaleTypes;
    }

    public List<CaneYard> getCaneYards() {
        return caneYards;
    }

    public void setCaneYards(List<CaneYard> caneYards) {
        this.caneYards = caneYards;
    }

    public List<VehicleGroupMaster> getVehicleGroupMasters() {
        return vehicleGroupMasters;
    }

    public void setVehicleGroupMasters(List<VehicleGroupMaster> vehicleGroupMasters) {
        this.vehicleGroupMasters = vehicleGroupMasters;
    }

    public List<Pump> getPumps() {
        return pumps;
    }

    public void setPumps(List<Pump> pumps) {
        this.pumps = pumps;
    }

    public List<PumpSaleType> getPumpSaleTypes() {
        return pumpSaleTypes;
    }

    public void setPumpSaleTypes(List<PumpSaleType> pumpSaleTypes) {
        this.pumpSaleTypes = pumpSaleTypes;
    }

    public List<CustomerType> getCustomerTypes() {
        return customerTypes;
    }

    public void setCustomerTypes(List<CustomerType> customerTypes) {
        this.customerTypes = customerTypes;
    }
}
