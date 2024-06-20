package com.twd.chitboyapp.spsskl.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.twd.chitboyapp.spsskl.pojo.BankMaster;
import com.twd.chitboyapp.spsskl.pojo.CaneConfirmationRegistrationList;
import com.twd.chitboyapp.spsskl.pojo.CaneTypeMaster;
import com.twd.chitboyapp.spsskl.pojo.CaneYard;
import com.twd.chitboyapp.spsskl.pojo.CropTypeMaster;
import com.twd.chitboyapp.spsskl.pojo.CropWater;
import com.twd.chitboyapp.spsskl.pojo.CustomerType;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.pojo.FarmerCategoryMaster;
import com.twd.chitboyapp.spsskl.pojo.FarmerTypeMaster;
import com.twd.chitboyapp.spsskl.pojo.FertSaleType;
import com.twd.chitboyapp.spsskl.pojo.HangamMaster;
import com.twd.chitboyapp.spsskl.pojo.HarvestingType;
import com.twd.chitboyapp.spsskl.pojo.IrrigationTypeMaster;
import com.twd.chitboyapp.spsskl.pojo.LaneType;
import com.twd.chitboyapp.spsskl.pojo.MenuBean;
import com.twd.chitboyapp.spsskl.pojo.PlantLocation;
import com.twd.chitboyapp.spsskl.pojo.PlantationBean;
import com.twd.chitboyapp.spsskl.pojo.Pump;
import com.twd.chitboyapp.spsskl.pojo.PumpSaleType;
import com.twd.chitboyapp.spsskl.pojo.ReasonMaster;
import com.twd.chitboyapp.spsskl.pojo.SectionMaster;
import com.twd.chitboyapp.spsskl.pojo.VarietyMaster;
import com.twd.chitboyapp.spsskl.pojo.VehicleGroupMaster;
import com.twd.chitboyapp.spsskl.pojo.VehicleTypeMaster;
import com.twd.chitboyapp.spsskl.pojo.VillageMaster;
import com.twd.multispinnerfilter.KeyPairBoolData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressLint("Range")
public class DBHelper extends SQLiteOpenHelper {

    // context for all operations
    Context context;

    // database name
    public static final String DATABASE_NAME = "sugardata.db";

    // Master Table Start
    public static final String menuTable = "app_m_menu";
    private static final String menuFieldGroupId = "ngroupId";
    private static final String menuFieldGroupName = "vgroupName";
    private static final String menuFieldAndroidOrder = "nandroidOrder";

    public static final String bankTable = "bank";
    private static final String bankFieldBankId = "nbankId";
    private static final String bankFieldbankNameLocal = "vbankNameLocal";

    public static final String cropTypeTable = "crop_type";
    private static final String cropTypeFieldCropId = "ncropId";
    private static final String cropTypeFieldCropName = "vcropName";

    public static final String entityTable = "entity_details";
    private static final String entityFieldEntityUniId = "nentityUniId";
    private static final String entityFieldEntityNameLocal = "ventityNameLocal";
    private static final String entityFieldMobileNo = "vmobileNo";
    private static final String entityFieldBankId = "nbankId";
    private static final String entityFieldBankAcNo = "vbankAcNo";
    private static final String entityFieldVillageId = "nvillageId";
    private static final String entityFieldFarmerTypeId = "nfarmerTypeId";

    public static final String farmerCategoryTable = "farmer_category";
    private static final String farmerCategoryFieldFarmerCatId = "nfarmerCatId";
    private static final String farmerCategoryFieldFarmerCatNameLocal = "vfarmerCatNameLocal";

    public static final String farmerTypeTable = "farmer_type";
    private static final String farmerTypeFieldFarmerTypeId = "nfarmerTypeId";
    private static final String farmerTypeFieldFarmerTypeNameLocal = "vfarmerTypeNameLocal";
    private static final String farmerTypeFieldFarmerCatId = "nfarmerCatId";

    public static final String hangamTable = "hangam";
    private static final String hangamFieldHangamId = "nhangamId";
    private static final String hangamFieldHangamName = "vhangamName";
    private static final String hangamFieldHangamStartDate = "dhangamStartDate";
    private static final String hangamFieldHangamEndDate = "dhangamEndDate";

    public static final String harvestingTypeTable = "harvesting_type";
    private static final String harvestingTypeFieldHarvestTypeId = "nharvestTypeId";
    private static final String harvestingTypeFieldHarvestTypeNameLocal = "vharvestTypeNameLocal";

    public static final String irrigationTypeTable = "irrigation_type";
    private static final String irrigationTypeFieldIrrigationId = "nirrigationId";
    private static final String irrigationTypeFieldIrrigationName = "virrigationName";

    public static final String laneTypeTable = "lane_type";
    private static final String laneTypeFieldLaneTypeId = "nlaneTypeId";
    private static final String laneTypeFieldLaneTypeNameLocal = "vlaneTypeNameLocal";

    public static final String sectionTable = "Section";
    private static final String sectionFieldSectionId = "nsectionId";
    private static final String sectionFieldSectionNameLocal = "vsectionNameLocal";

    public static final String varietyTable = "variety";
    private static final String varietyFieldVarietyId = "navarietyId";
    private static final String varietyFieldVarietyName = "vvariety_name";
    private static final String varietyFieldHarvestingSeq = "nharvestingSeq";

    public static final String vehiclePlantTypeTable = "PlantVehicleType";
    public static final String vehicleTypeTable = "VehicleType";
    private static final String vehicleTypeFieldVehicleTypeId = "nvehicleTypeId";
    private static final String vehicleTypeFieldVehicleTypeNameLocal = "vvehicleTypeNameLocal";
    private static final String vehicleTypeFieldDropdownId = "ndropdownId";

    public static final String villageTable = "village";
    private static final String villageFieldVillageId = "nvillageId";
    private static final String villageFieldVillageNameLocal = "villageNameLocal";
    private static final String villageFieldSectionId = "nsectionId";
    private static final String villageFieldVillageDistance = "nvillageDistance";
    private static final String villageFieldUnderArea = "vunderArea";

    public static final String vehicleGroupTable = "vehicleGroup";
    private static final String vehicleGroupFieldGroupId = "nvehicle_group_id";
    private static final String vehicleGroupFieldGroupName = "vvehicle_group_name";

    public static final String caneYardTable = "caneYard";
    private static final String caneYardFieldYardId = "nyard_id";
    private static final String caneYardFieldYardNameMar = "vyard_name_mar";
    private static final String caneYardFieldTruckTracktor = "vtractor_truck";
    private static final String caneYardFieldBajat = "vbajat";

    public static final String pumpTable = "pump";
    private static final String pumpFieldPumpId = "pump_id";
    private static final String pumpFieldPumpNameMar = "vpump_name_mar";
    private static final String pumpFieldItemId = "item_id";
    private static final String pumpFieldItemName = "vitem_name_mar";


    public static final String customerTypeTable = "customer_type";
    private static final String customerTypeFieldCustomerTypeId = "customer_type_id";
    private static final String customerTypeFieldCustomerTypeNameMar = "vcustomer_type_name_mar";
    private static final String customerTypeFieldNandroidOrder = "nandroid_order";

    public static final String cropWaterTable = "cropWater";
    private static final String cropWaterFieldNCropWaterCondition = "ncropwaterCondition";
    private static final String cropWaterFieldVCropWaterCondition = "vcropwaterCondition";

    public static final String caneTypeTable = "caneType";
    private static final String caneTypeFieldNCaneTypeId = "caneTypeId";
    private static final String caneTypeFieldVCaneTypeName = "caneTypeName";

    public static final String reasonMaster = "reasonMaster";
    private static final String reasonFieldRemarkId = "nremarkId";
    private static final String reasonFieldRemarkNameLocal = "vremarkNameLocal";

    public static final String reasonAllTable = "reason";
    private static final String reasonAllFieldReasonId = "nreasonId";
    private static final String reasonAllFieldReasonNameLocal = "vreasonNameLocal";
    private static final String reasonAllFieldReasonGroupId = "vreasonGroupId";

    public static final String fertSaleTypeTable = "fertSaleType";
    private static final String saleTypeId = "saleTypeId";
    private static final String saleTypeName = "saleTypeName";
    private static final String saleTypeNameMarathi = "saleTypeNameMarathi";

    public static final String pumpSaleTypeTable = "pumpSaleType";
    private static final String pumpSaleTypeFiledTypeId = "saleTypeId";
    private static final String pumpSaleTypeFiledTypeName = "saleTypeName";

    // Master Table End

    // Transaction Table Start
    public static final String plantationTable = "cr_t_plantation";
    private static final String plantationFieldvyearId = "vyear_id";
    private static final String plantationFieldEntityUniId = "nentity_uni_id";
    private static final String plantationFieldPlotNo = "nplot_no";
    private static final String plantationFieldHangamId = "nhangam_id";
    private static final String plantationFieldSurveNo = "vsurve_no";
    private static final String plantationFieldEntryDate = "dentry_date";
    private static final String plantationFieldPlantationDate = "dplantation_date";
    private static final String plantationFieldIrrigationId = "nirrigation_id";
    private static final String plantationFieldVarietyId = "nvariety_id";
    private static final String plantationFieldArea = "narea";
    private static final String plantationFieldExpectedYield = "nexpected_yield";
    private static final String plantationFieldVillageId = "nvillage_id";
    private static final String plantationFieldDistance = "ndistance";
    private static final String plantationFieldCreateDate = "createDate";
    private static final String plantationFieldSectionId = "nsection_id";
    private static final String plantationFieldFarmerTypeId = "nfarmer_type_id";
    private static final String plantationFieldGpsArea = "ngps_area";
    private static final String plantationFieldGpsDistance = "ngps_distance";
    private static final String plantationFieldConfirmFlag = "nconfirm_flag";
    private static final String plantationFieldPhotoPath = "vphoto_path";
    private static final String plantationFieldConfirmphotoPath = "vconfirmphoto_path";
    private static final String plantationFieldTentativeArea = "ntentative_area";
    private static final String plantationFieldCropwaterCondition = "ncropwater_condition";
    private static final String plantationFieldJuneFlag = "njune_flag";
    private static final String plantationFieldAugustFlag = "naugust_flag";
    private static final String plantationFieldSoilTest = "vsoil_test"; // used as farmerVilleageName on rujuwat
    private static final String plantationFieldGreenFert = "vgreen_fert"; // used as sectionCode on rujuwat
    private static final String plantationFieldLaneTypeId = "nlane_type_id";
    private static final String plantationFieldBeneTreat = "vbene_treat";
    private static final String plantationFieldBesalDose = "vbesal_dose";
    private static final String plantationFieldDripUsed = "vdrip_used";
    private static final String plantationFieldHarvestTypeId = "nharvest_type_id";
    private static final String plantationFieldPlotNoOffline = "nplot_no_offline";
    private static final String plantationFieldRegFlagOffline = "nreg_flag_offline";
    private static final String plantationFieldConfFlagOffline = "nconf_flag_offline";
    private static final String plantationFieldEntryType = "entry_type";
    private static final String plantationFieldServerStatus = "serverstatus"; //1
    private static final String plantationFieldServerTime = "serverdatetime";
    private static final String plantationFieldGpsFlag = "ngpsFlag";
    private static final String plantationFieldStandingLatitude = "vstandingLatitude";
    private static final String plantationFieldStandinglongitude = "vstandinglongitude";
    private static final String plantationFieldStandingacc = "nstandingacc";

    public static final String latLongTable = "plant_latlong";
    private static final String latLongFieldPlotNoOffline = "nplot_no_offline";
    private static final String latLongFieldvyearId = "vyear_id";
    private static final String latLongFieldLatitude = "vlatitude";
    private static final String latLongFieldLongitude = "vlongitude";
    private static final String latLongFieldAccuracy = "naccuracy";
    // Transaction Table End

    // create Table Sql for Master Table Start

    String createMenuTable = "create table " + menuTable + "(" + menuFieldGroupId + " text primary key," + menuFieldGroupName + " text," + menuFieldAndroidOrder + " text)";

    String createBankTable = "create table " + bankTable + "(" + bankFieldBankId + " text primary key," + bankFieldbankNameLocal + " text)";

    String createCropTypeTable = "create table " + cropTypeTable + "(" + cropTypeFieldCropId + " text primary key," + cropTypeFieldCropName + " text)";

    String createEntityTable = "create table " + entityTable + "(" + entityFieldEntityUniId + " text primary key," + entityFieldEntityNameLocal + " text," + entityFieldMobileNo + " text," + entityFieldBankId + " text," + entityFieldBankAcNo + " text," + entityFieldVillageId + " text," + entityFieldFarmerTypeId + " text)";

    String createFarmerCategoryTable = "create table " + farmerCategoryTable + "(" + farmerCategoryFieldFarmerCatId + " text primary key," + farmerCategoryFieldFarmerCatNameLocal + " text)";

    String createFarmerTypeTable = "create table " + farmerTypeTable + "(" + farmerTypeFieldFarmerTypeId + " text primary key," + farmerTypeFieldFarmerTypeNameLocal + " text," + farmerTypeFieldFarmerCatId + " text)";

    String createHangamTable = "create table " + hangamTable + "(" + hangamFieldHangamId + " text primary key," + hangamFieldHangamName + " text," + hangamFieldHangamStartDate + " text," + hangamFieldHangamEndDate + " text)";

    String createHarvestingTypeTable = "create table " + harvestingTypeTable + "(" + harvestingTypeFieldHarvestTypeId + " text primary key," + harvestingTypeFieldHarvestTypeNameLocal + " text)";

    String createIrrigationTypeTable = "create table " + irrigationTypeTable + "(" + irrigationTypeFieldIrrigationId + " text primary key," + irrigationTypeFieldIrrigationName + " text)";

    String createLaneTypeTable = "create table " + laneTypeTable + "(" + laneTypeFieldLaneTypeId + " text primary key," + laneTypeFieldLaneTypeNameLocal + " text)";

    String createSectionTable = "create table " + sectionTable + "(" + sectionFieldSectionId + " text primary key," + sectionFieldSectionNameLocal + " text)";

    String createVarietyTable = "create table " + varietyTable + "(" + varietyFieldVarietyId + " text primary key," + varietyFieldVarietyName + " text," + varietyFieldHarvestingSeq + " text)";

    String createVehicleTypeTable = "create table " + vehicleTypeTable + "(" + vehicleTypeFieldVehicleTypeId + " text primary key," + vehicleTypeFieldVehicleTypeNameLocal + " text," + vehicleTypeFieldDropdownId + " text)";

    String createPlantVehicleTypeTable = "create table " + vehiclePlantTypeTable + "(" + vehicleTypeFieldVehicleTypeId + " text primary key," + vehicleTypeFieldVehicleTypeNameLocal + " text," + vehicleTypeFieldDropdownId + " text)";

    String createVillageTable = "create table " + villageTable + "(" + villageFieldVillageId + " text primary key," + villageFieldVillageNameLocal + " text," + villageFieldSectionId + " text," + villageFieldVillageDistance + " text," + villageFieldUnderArea + " text)";

    String createCropWaterTable = "create table " + cropWaterTable + "(" + cropWaterFieldNCropWaterCondition + " text primary key," + cropWaterFieldVCropWaterCondition + " text)";


    String createCaneTypeTable = "create table " + caneTypeTable + "(" + caneTypeFieldNCaneTypeId + " text primary key," + caneTypeFieldVCaneTypeName + " text)";

    String createCaneReasonMaster = "create table " + reasonMaster + "(" + reasonFieldRemarkId + " text primary key," + reasonFieldRemarkNameLocal + " text)";
    String createReasonAllTable = "create table " + reasonAllTable + "(" + reasonAllFieldReasonId + " text primary key," + reasonAllFieldReasonNameLocal + " text," + reasonAllFieldReasonGroupId + " text)";
    String createFertSaleTypeTable = "create table " + fertSaleTypeTable + "(" + saleTypeId + " text primary key," + saleTypeName + " text," + saleTypeNameMarathi + " text)";
    String createPumpSaleTypeTable = "create table " + pumpSaleTypeTable + "(" + pumpSaleTypeFiledTypeId + " text primary key," + pumpSaleTypeFiledTypeName + " text)";

    // create Table Sql for Master Table end

    // Create Table Sql for Transaction Table Start
    String createPlantationTable = "create table " + plantationTable + "(" + plantationFieldvyearId + " text," + plantationFieldEntityUniId + " text," + plantationFieldPlotNo + " text," + plantationFieldHangamId + " text," + plantationFieldSurveNo + " text," + plantationFieldEntryDate + " text," + plantationFieldPlantationDate + " text," + plantationFieldIrrigationId + " text," + plantationFieldVarietyId + " text," + plantationFieldArea + " text," + plantationFieldExpectedYield + " text," + plantationFieldVillageId + " text," + plantationFieldDistance + " text," + plantationFieldCreateDate + " text," + plantationFieldSectionId + " text," + plantationFieldFarmerTypeId + " text," + plantationFieldGpsArea + " text," + plantationFieldGpsDistance + " text," + plantationFieldConfirmFlag + " text," + plantationFieldPhotoPath + " text," + plantationFieldConfirmphotoPath + " text," + plantationFieldTentativeArea + " text," + plantationFieldCropwaterCondition + " text," + plantationFieldJuneFlag + " text," + plantationFieldAugustFlag + " text," + plantationFieldSoilTest + " text," + plantationFieldGreenFert + " text," + plantationFieldLaneTypeId + " text," + plantationFieldBeneTreat + " text," + plantationFieldBesalDose + " text," + plantationFieldDripUsed + " text," + plantationFieldHarvestTypeId + " text," + plantationFieldPlotNoOffline + " text primary key," + plantationFieldRegFlagOffline + " text," + plantationFieldConfFlagOffline + " text," + plantationFieldEntryType + " text," + plantationFieldGpsFlag + " text," + plantationFieldStandingLatitude + " text," + plantationFieldStandinglongitude + " text," + plantationFieldStandingacc + " text," + plantationFieldServerStatus + " text," + plantationFieldServerTime + " text)";


    String createLatLongTable = "create table " + latLongTable + "(" + latLongFieldPlotNoOffline + " text," + latLongFieldvyearId + " text," + latLongFieldLatitude + " text," + latLongFieldLongitude + " text," + latLongFieldAccuracy + " text)";

    String createVehicleGroupTable = "create table " + vehicleGroupTable + "(" + vehicleGroupFieldGroupId + " text," + vehicleGroupFieldGroupName + " text)";

    String createCaneYardTable = "create table " + caneYardTable + "(" + caneYardFieldYardId + " text," + caneYardFieldYardNameMar + " text," + caneYardFieldTruckTracktor + " text," + caneYardFieldBajat + " text)";
    String createPumpTable = "create table " + pumpTable + "(" + pumpFieldPumpId + " text," + pumpFieldPumpNameMar + " text, " + pumpFieldItemId + " text," + pumpFieldItemName + " text)";
    String createCustomerTypeTable = "create table " + customerTypeTable + "(" + customerTypeFieldCustomerTypeId + " text," + customerTypeFieldCustomerTypeNameMar + " text," + customerTypeFieldNandroidOrder + " INTEGER)";
    // Create Table Sql for Transaction Table End


    // Alter Table Sql for Master Table Start


    // Alter Table Sql for Master Table end

    // Alter Table Sql for Transaction Table Start
    String beginTransaction = "BEGIN TRANSACTION;";
    String alterPlantationTable3a = "ALTER TABLE " + plantationTable + " ADD COLUMN " + plantationFieldServerStatus + " text;";
    String alterPlantationTable3b = "ALTER TABLE " + plantationTable + " ADD COLUMN " + plantationFieldServerTime + " text;";
    String commit = "COMMIT;";

    String alterPlantationTable4a = "ALTER TABLE " + plantationTable + " ADD COLUMN " + plantationFieldGpsFlag + " text;";
    String alterPlantationTable4b = "ALTER TABLE " + plantationTable + " ADD COLUMN " + plantationFieldStandingLatitude + " text;";
    String alterPlantationTable4c = "ALTER TABLE " + plantationTable + " ADD COLUMN " + plantationFieldStandinglongitude + " text;";
    String alterPlantationTable4d = "ALTER TABLE " + plantationTable + " ADD COLUMN " + plantationFieldStandingacc + " text;";

    String alterVarietyTable = "ALTER TABLE " + varietyTable + " ADD COLUMN " + varietyFieldHarvestingSeq + " text;";

    // Alter Table Sql for Transaction Table End
    String alterMenuTable = "ALTER TABLE " + menuTable + " ADD COLUMN " + menuFieldAndroidOrder + " text;";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 14);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Execute Master Table Query Start
        sqLiteDatabase.execSQL(createMenuTable);
        sqLiteDatabase.execSQL(createBankTable);
        sqLiteDatabase.execSQL(createCropTypeTable);
        sqLiteDatabase.execSQL(createEntityTable);
        sqLiteDatabase.execSQL(createFarmerCategoryTable);
        sqLiteDatabase.execSQL(createFarmerTypeTable);
        sqLiteDatabase.execSQL(createHangamTable);
        sqLiteDatabase.execSQL(createHarvestingTypeTable);
        sqLiteDatabase.execSQL(createIrrigationTypeTable);
        sqLiteDatabase.execSQL(createLaneTypeTable);
        sqLiteDatabase.execSQL(createSectionTable);
        sqLiteDatabase.execSQL(createVarietyTable);
        sqLiteDatabase.execSQL(createVehicleTypeTable);
        sqLiteDatabase.execSQL(createPlantVehicleTypeTable);
        sqLiteDatabase.execSQL(createVillageTable);
        sqLiteDatabase.execSQL(createCropWaterTable);
        sqLiteDatabase.execSQL(createCaneTypeTable);
        sqLiteDatabase.execSQL(createCaneReasonMaster);
        // Execute Master Table Query End

        // Execute Transaction Table Query Start
        sqLiteDatabase.execSQL(createPlantationTable);
        sqLiteDatabase.execSQL(createLatLongTable);
        sqLiteDatabase.execSQL(createFertSaleTypeTable);
        sqLiteDatabase.execSQL(createPumpSaleTypeTable);
        sqLiteDatabase.execSQL(createCaneYardTable);
        sqLiteDatabase.execSQL(createVehicleGroupTable);
        sqLiteDatabase.execSQL(createReasonAllTable);
        sqLiteDatabase.execSQL(createPumpTable);
        sqLiteDatabase.execSQL(createCustomerTypeTable);
        // Execute Transaction Table Query End
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                sqLiteDatabase.execSQL(createPlantVehicleTypeTable);
                sqLiteDatabase.execSQL(createPlantationTable);
            case 2:
                sqLiteDatabase.execSQL(createLatLongTable);
            case 3:
                sqLiteDatabase.execSQL(beginTransaction);
                sqLiteDatabase.execSQL(alterPlantationTable3a);
                sqLiteDatabase.execSQL(alterPlantationTable3b);
                sqLiteDatabase.execSQL(commit);
            case 4:
                sqLiteDatabase.execSQL(beginTransaction);
                sqLiteDatabase.execSQL(alterPlantationTable4a);
                sqLiteDatabase.execSQL(alterPlantationTable4b);
                sqLiteDatabase.execSQL(alterPlantationTable4c);
                sqLiteDatabase.execSQL(alterPlantationTable4d);
                sqLiteDatabase.execSQL(commit);
            case 5:
                sqLiteDatabase.execSQL(createCropWaterTable);
            case 6:
                sqLiteDatabase.execSQL(alterVarietyTable);
            case 7:
                sqLiteDatabase.execSQL(createCaneTypeTable);
            case 8:
                sqLiteDatabase.execSQL(createCaneReasonMaster);
            case 9:
                sqLiteDatabase.execSQL(alterMenuTable);
            case 10:
                sqLiteDatabase.execSQL(createFertSaleTypeTable);
            case 11:
                sqLiteDatabase.execSQL(createCaneYardTable);
                sqLiteDatabase.execSQL(createVehicleGroupTable);
            case 12:
                sqLiteDatabase.execSQL(createReasonAllTable);
            case 13:
                sqLiteDatabase.execSQL(createPumpTable);
                sqLiteDatabase.execSQL(createPumpSaleTypeTable);
                sqLiteDatabase.execSQL(createCustomerTypeTable);

        }
    }

    public void delete(String... tables) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (String table : tables) {
            db.delete(table, null, null);
        }
        closeDb(db);
    }

    public HashMap<String, Long> count(String... tables) {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, Long> total = new HashMap<>();
        for (String table : tables) {
            total.put(table, DatabaseUtils.queryNumEntries(db, table));
        }
        closeDb(db);
        return total;
    }

    public Long countPlantationNotSend() {
        SQLiteDatabase db = this.getReadableDatabase();
        Long total = DatabaseUtils.queryNumEntries(db, plantationTable, plantationFieldServerStatus + "=? and " + plantationFieldEntryType + "=1", new String[]{"1"});
        closeDb(db);
        return total;
    }

    // insert Master with List Start
    public boolean insertMenu(List<MenuBean> menuBeans) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (MenuBean menuBean : menuBeans) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(menuFieldGroupId, menuBean.getGroupId());
            contentValues.put(menuFieldGroupName, menuBean.getGroupName());
            contentValues.put(menuFieldAndroidOrder, menuBean.getNandroidOrder());
            db.insert(menuTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertBank(List<BankMaster> bankMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (BankMaster bankMaster : bankMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(bankFieldBankId, bankMaster.getNbankId());
            contentValues.put(bankFieldbankNameLocal, bankMaster.getVbankNameLocal());
            db.insert(bankTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertCropType(List<CropTypeMaster> cropTypeMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (CropTypeMaster cropTypeMaster : cropTypeMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(cropTypeFieldCropId, cropTypeMaster.getNcropId());
            contentValues.put(cropTypeFieldCropName, cropTypeMaster.getVcropName());
            db.insert(cropTypeTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertEntity(List<EntityMasterDetail> entityMasterDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (EntityMasterDetail entityMasterDetail : entityMasterDetails) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(entityFieldEntityUniId, entityMasterDetail.getNentityUniId());
            contentValues.put(entityFieldEntityNameLocal, entityMasterDetail.getVentityNameLocal());
            contentValues.put(entityFieldMobileNo, entityMasterDetail.getVmobileNo());
            contentValues.put(entityFieldBankId, entityMasterDetail.getNbankId());
            contentValues.put(entityFieldBankAcNo, entityMasterDetail.getVbankAcNo());
            contentValues.put(entityFieldVillageId, entityMasterDetail.getNvillageId());
            contentValues.put(entityFieldFarmerTypeId, entityMasterDetail.getNfarmerTypeId());
            db.insert(entityTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertFarmerCategory(List<FarmerCategoryMaster> farmerCategoryMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (FarmerCategoryMaster farmerCategoryMaster : farmerCategoryMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(farmerCategoryFieldFarmerCatId, farmerCategoryMaster.getNfarmerCatId());
            contentValues.put(farmerCategoryFieldFarmerCatNameLocal, farmerCategoryMaster.getVfarmerCatNameLocal());
            db.insert(farmerCategoryTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertFarmerType(List<FarmerTypeMaster> farmerTypeMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (FarmerTypeMaster farmerTypeMaster : farmerTypeMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(farmerTypeFieldFarmerTypeId, farmerTypeMaster.getNfarmerTypeId());
            contentValues.put(farmerTypeFieldFarmerTypeNameLocal, farmerTypeMaster.getVfarmerTypeNameLocal());
            contentValues.put(farmerTypeFieldFarmerCatId, farmerTypeMaster.getNfarmerCatId());
            db.insert(farmerTypeTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertHangam(List<HangamMaster> hangamMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (HangamMaster hangamMaster : hangamMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(hangamFieldHangamId, hangamMaster.getNhangamId());
            contentValues.put(hangamFieldHangamName, hangamMaster.getVhangamName());
            contentValues.put(hangamFieldHangamStartDate, hangamMaster.getDhangamStartDate());
            contentValues.put(hangamFieldHangamEndDate, hangamMaster.getDhangamEndDate());
            db.insert(hangamTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertHarvestingType(List<HarvestingType> harvestingTypes) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (HarvestingType harvestingType : harvestingTypes) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(harvestingTypeFieldHarvestTypeId, harvestingType.getNharvestTypeId());
            contentValues.put(harvestingTypeFieldHarvestTypeNameLocal, harvestingType.getVharvestTypeNameLocal());
            db.insert(harvestingTypeTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertIrrigationType(List<IrrigationTypeMaster> irrigationTypes) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (IrrigationTypeMaster irrigationType : irrigationTypes) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(irrigationTypeFieldIrrigationId, irrigationType.getNirrigationId());
            contentValues.put(irrigationTypeFieldIrrigationName, irrigationType.getVirrigationName());
            db.insert(irrigationTypeTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertLaneType(List<LaneType> laneTypes) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (LaneType laneType : laneTypes) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(laneTypeFieldLaneTypeId, laneType.getNlaneTypeId());
            contentValues.put(laneTypeFieldLaneTypeNameLocal, laneType.getVlaneTypeNameLocal());
            db.insert(laneTypeTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertSection(List<SectionMaster> sectionMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (SectionMaster sectionMaster : sectionMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(sectionFieldSectionId, sectionMaster.getNsectionId());
            contentValues.put(sectionFieldSectionNameLocal, sectionMaster.getVsectionNameLocal());
            db.insert(sectionTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertVariety(List<VarietyMaster> varietyMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (VarietyMaster varietyMaster : varietyMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(varietyFieldVarietyId, varietyMaster.getNavarietyId());
            contentValues.put(varietyFieldVarietyName, varietyMaster.getVvariety_name());
            contentValues.put(varietyFieldHarvestingSeq, varietyMaster.getNharvestingSeq());
            db.insert(varietyTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertVehicleGroup(List<VehicleGroupMaster> vehicleGroupMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (VehicleGroupMaster vehicleGroupMaster : vehicleGroupMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(vehicleGroupFieldGroupId, vehicleGroupMaster.getNvehicleGroupId());
            contentValues.put(vehicleGroupFieldGroupName, vehicleGroupMaster.getVvehicleGroupName());
            db.insert(vehicleGroupTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertCaneYard(List<CaneYard> caneYards) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (CaneYard caneYard : caneYards) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(caneYardFieldYardId, caneYard.getNyardId());
            contentValues.put(caneYardFieldYardNameMar, caneYard.getVyardName());
            contentValues.put(caneYardFieldTruckTracktor, caneYard.getVtruckTracktor());
            contentValues.put(caneYardFieldBajat, caneYard.getVbajat());
            db.insert(caneYardTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertPump(List<Pump> pumps) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Pump pump : pumps) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(pumpFieldPumpId, pump.getNpumpId());
            contentValues.put(pumpFieldPumpNameMar, pump.getVpumpName());
            contentValues.put(pumpFieldItemId, pump.getNitemId());
            contentValues.put(pumpFieldItemName, pump.getVitemName());
            db.insert(pumpTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertCustomerType(List<CustomerType> customerTypes) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (CustomerType customerType : customerTypes) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(customerTypeFieldCustomerTypeId, customerType.getNcustTypeId());
            contentValues.put(customerTypeFieldCustomerTypeNameMar, customerType.getVcustTypeName());
            contentValues.put(customerTypeFieldNandroidOrder, customerType.getNandroidOrderId());
            db.insert(customerTypeTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertVehicleType(List<VehicleTypeMaster> vehicleTypeMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (VehicleTypeMaster vehicleTypeMaster : vehicleTypeMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(vehicleTypeFieldVehicleTypeId, vehicleTypeMaster.getNvehicleTypeId());
            contentValues.put(vehicleTypeFieldVehicleTypeNameLocal, vehicleTypeMaster.getVvehicleTypeNameLocal());
            contentValues.put(vehicleTypeFieldDropdownId, vehicleTypeMaster.getNdropdownId());
            db.insert(vehicleTypeTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertVehiclePlantType(List<VehicleTypeMaster> vehicleTypeMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (VehicleTypeMaster vehicleTypeMaster : vehicleTypeMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(vehicleTypeFieldVehicleTypeId, vehicleTypeMaster.getNvehicleTypeId());
            contentValues.put(vehicleTypeFieldVehicleTypeNameLocal, vehicleTypeMaster.getVvehicleTypeNameLocal());
            contentValues.put(vehicleTypeFieldDropdownId, vehicleTypeMaster.getNdropdownId());
            db.insert(vehiclePlantTypeTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertVillage(List<VillageMaster> villageMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (VillageMaster villageMaster : villageMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(villageFieldVillageId, villageMaster.getNvillageId());
            contentValues.put(villageFieldVillageNameLocal, villageMaster.getVillageNameLocal());
            contentValues.put(villageFieldSectionId, villageMaster.getNsectionId());
            contentValues.put(villageFieldVillageDistance, villageMaster.getNvillageDistance());
            contentValues.put(villageFieldUnderArea, villageMaster.getVunderArea());
            db.insert(villageTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertCropWater(List<CropWater> cropWaters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (CropWater cropWater : cropWaters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(cropWaterFieldNCropWaterCondition, cropWater.getNcropwaterCondition());
            contentValues.put(cropWaterFieldVCropWaterCondition, cropWater.getVcropwaterCondition());
            db.insert(cropWaterTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertCaneType(List<CaneTypeMaster> caneTypeMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (CaneTypeMaster caneTypeMaster : caneTypeMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(caneTypeFieldNCaneTypeId, caneTypeMaster.getNcaneTypeId());
            contentValues.put(caneTypeFieldVCaneTypeName, caneTypeMaster.getVcaneTypeName());
            db.insert(caneTypeTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }


    public boolean insertReason(List<ReasonMaster> reasonMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ReasonMaster reason : reasonMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(reasonFieldRemarkId, reason.getNremarkId());
            contentValues.put(reasonFieldRemarkNameLocal, reason.getVremarkNameLocal());
            db.insert(reasonMaster, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertReasonAll(List<ReasonMaster> reasonMasters) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ReasonMaster reason : reasonMasters) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(reasonAllFieldReasonId, reason.getNremarkId());
            contentValues.put(reasonAllFieldReasonNameLocal, reason.getVremarkNameLocal());
            contentValues.put(reasonAllFieldReasonGroupId, reason.getNremarkGroupId());
            db.insert(reasonAllTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }


    public boolean insertFertSaleType(List<FertSaleType> fertSaleTypes) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (FertSaleType fertSaleType : fertSaleTypes) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(saleTypeId, fertSaleType.getNsaleTypeId());
            contentValues.put(saleTypeName, fertSaleType.getVsaleTypeName());
            contentValues.put(saleTypeNameMarathi, fertSaleType.getVsaleTypeNameMarathi());
            db.insert(fertSaleTypeTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertPumpSaleType(List<PumpSaleType> pumpSaleTypes) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (PumpSaleType pumpSaleType : pumpSaleTypes) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(pumpSaleTypeFiledTypeId, pumpSaleType.getNsaleTypeId());
            contentValues.put(pumpSaleTypeFiledTypeName, pumpSaleType.getVsaleTypeName());
            db.insert(pumpSaleTypeTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }

    public boolean insertCaneConfirmation(List<CaneConfirmationRegistrationList> caneConfirmationRegistrationLists) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isFound = false;
        for (CaneConfirmationRegistrationList caneConfirmationRegistrationList : caneConfirmationRegistrationLists) {
            isFound = false;
            Cursor rs = db.rawQuery("select " + plantationFieldEntryDate + " from " + plantationTable + " p where p." + plantationFieldvyearId + " = ? and " + plantationFieldPlotNo + " = ? ", new String[]{caneConfirmationRegistrationList.getYearCode(), caneConfirmationRegistrationList.getPlotNo()});
            if (rs.moveToFirst()) {
                if (rs.getString(rs.getColumnIndex(plantationFieldEntryDate)) != null) {
                    continue;
                } else {
                    isFound = true;
                }
            }
            closeCursor(rs);
            ContentValues contentValues = new ContentValues();
            contentValues.put(plantationFieldvyearId, caneConfirmationRegistrationList.getYearCode());
            contentValues.put(plantationFieldEntityUniId, caneConfirmationRegistrationList.getNentityUniId());
            contentValues.put(plantationFieldPlotNo, caneConfirmationRegistrationList.getPlotNo());
            contentValues.put(plantationFieldSurveNo, caneConfirmationRegistrationList.getServeNo());
            contentValues.put(plantationFieldPlantationDate, caneConfirmationRegistrationList.getDplantationDate());
            contentValues.put(plantationFieldHangamId, caneConfirmationRegistrationList.getNhungamCode());
            contentValues.put(plantationFieldVarietyId, caneConfirmationRegistrationList.getNcaneVarity());
            contentValues.put(plantationFieldArea, caneConfirmationRegistrationList.getNarea());
            contentValues.put(plantationFieldVillageId, caneConfirmationRegistrationList.getVilleageCode());
            contentValues.put(plantationFieldJuneFlag, caneConfirmationRegistrationList.getNjuneFlag());
            contentValues.put(plantationFieldAugustFlag, caneConfirmationRegistrationList.getNaugustFlag());
            contentValues.put(plantationFieldConfirmFlag, caneConfirmationRegistrationList.getNconfirmFlag());
            contentValues.put(plantationFieldEntryType, "2");
            contentValues.put(plantationFieldServerStatus, "1");
            contentValues.putNull(plantationFieldEntryDate);
            contentValues.put(plantationFieldExpectedYield, caneConfirmationRegistrationList.getNexpectedYield());
            contentValues.put(plantationFieldCropwaterCondition, caneConfirmationRegistrationList.getNcropwaterCondition());
            contentValues.put(plantationFieldTentativeArea, caneConfirmationRegistrationList.getNtentativeArea());
            contentValues.put(plantationFieldSoilTest, caneConfirmationRegistrationList.getFarmerVilleageName());  //used as farmerVilleageName on rujuwat
            contentValues.put(plantationFieldGreenFert, caneConfirmationRegistrationList.getSectionCode());  //used as sectionCode on rujuwat
            if (isFound)
                db.update(plantationTable, contentValues, plantationFieldvyearId + " = ? AND " + plantationFieldPlotNo + " = ? ", new String[]{caneConfirmationRegistrationList.getYearCode(), caneConfirmationRegistrationList.getPlotNo()});
            else
                db.insert(plantationTable, null, contentValues);
        }
        closeDb(db);
        return true;
    }
    // insert Master with List End


    // update master start

    public boolean updateFarmerMobile(String farmerCode, String mobileno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(entityFieldMobileNo, mobileno);
        db.update(entityTable, contentValues, entityFieldEntityUniId + "=?", new String[]{farmerCode});
        closeDb(db);
        return true;
    }

    public boolean updateFarmerBank(String farmerCode, String bankid, String accno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(entityFieldBankId, bankid);
        contentValues.put(entityFieldBankAcNo, accno);
        db.update(entityTable, contentValues, entityFieldEntityUniId + "=?", new String[]{farmerCode});
        closeDb(db);
        return true;
    }
    // update master end

    // Get Master Data with or without filter and in List start
    public List<MenuBean> getMenu(String menuIds) {
        List<MenuBean> menuBeanList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + menuFieldGroupId + "," + menuFieldGroupName + " from " + menuTable + " where " + menuFieldGroupId + " in (" + menuIds + ") order by CAST(" + menuFieldAndroidOrder + " AS INTEGER)", null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                MenuBean menuBean = new MenuBean();
                menuBean.setGroupId(rs.getString(rs.getColumnIndex(menuFieldGroupId)));
                menuBean.setGroupName(rs.getString(rs.getColumnIndex(menuFieldGroupName)));
                menuBeanList.add(menuBean);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return menuBeanList;
    }

    public List<KeyPairBoolData> getBank() {
        List<KeyPairBoolData> keyPairBoolDataArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + bankFieldBankId + "," + bankFieldbankNameLocal + " from " + bankTable + " order by " + bankFieldbankNameLocal, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                keyPairBoolData.setId(rs.getInt(rs.getColumnIndex(bankFieldBankId)));
                keyPairBoolData.setName(rs.getString(rs.getColumnIndex(bankFieldbankNameLocal)));
                keyPairBoolDataArrayList.add(keyPairBoolData);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return keyPairBoolDataArrayList;
    }


    public List<CropTypeMaster> getCropType() {
        List<CropTypeMaster> cropTypeMasterList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + cropTypeFieldCropId + "," + cropTypeFieldCropName + " from " + cropTypeTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                CropTypeMaster cropTypeMaster = new CropTypeMaster();
                cropTypeMaster.setNcropId(rs.getInt(rs.getColumnIndex(cropTypeFieldCropId)));
                cropTypeMaster.setVcropName(rs.getString(rs.getColumnIndex(cropTypeFieldCropName)));
                cropTypeMasterList.add(cropTypeMaster);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return cropTypeMasterList;
    }

    public List<FarmerCategoryMaster> getFarmerCategory() {
        List<FarmerCategoryMaster> farmerCategoryMasters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + farmerCategoryFieldFarmerCatId + "," + farmerCategoryFieldFarmerCatNameLocal + " from " + farmerCategoryTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                FarmerCategoryMaster farmerCategoryMaster = new FarmerCategoryMaster();
                farmerCategoryMaster.setNfarmerCatId(rs.getInt(rs.getColumnIndex(farmerCategoryFieldFarmerCatId)));
                farmerCategoryMaster.setVfarmerCatNameLocal(rs.getString(rs.getColumnIndex(farmerCategoryFieldFarmerCatNameLocal)));
                farmerCategoryMasters.add(farmerCategoryMaster);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return farmerCategoryMasters;
    }

    public List<FarmerTypeMaster> getFarmerType() {
        List<FarmerTypeMaster> farmerTypeMasters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + farmerTypeFieldFarmerTypeId + "," + farmerTypeFieldFarmerTypeNameLocal + "," + farmerTypeFieldFarmerCatId + " from " + farmerTypeTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                FarmerTypeMaster farmerTypeMaster = new FarmerTypeMaster();
                farmerTypeMaster.setNfarmerTypeId(rs.getInt(rs.getColumnIndex(farmerTypeFieldFarmerTypeId)));
                farmerTypeMaster.setVfarmerTypeNameLocal(rs.getString(rs.getColumnIndex(farmerTypeFieldFarmerTypeNameLocal)));
                farmerTypeMaster.setNfarmerCatId(rs.getInt(rs.getColumnIndex(farmerTypeFieldFarmerCatId)));
                farmerTypeMasters.add(farmerTypeMaster);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return farmerTypeMasters;
    }


    public List<KeyPairBoolData> getHangam(int selectedId) {
        List<KeyPairBoolData> hangamMasters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + hangamFieldHangamId + "," + hangamFieldHangamName + "," + hangamFieldHangamStartDate + "," + hangamFieldHangamEndDate + " from " + hangamTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                HangamMaster hangamMaster = new HangamMaster();
                keyPairBoolData.setId(rs.getInt(rs.getColumnIndex(hangamFieldHangamId)));
                keyPairBoolData.setSelected(rs.getInt(rs.getColumnIndex(hangamFieldHangamId)) == selectedId);
                keyPairBoolData.setName(rs.getString(rs.getColumnIndex(hangamFieldHangamName)));
                hangamMaster.setDhangamStartDate(rs.getString(rs.getColumnIndex(hangamFieldHangamStartDate)));
                hangamMaster.setDhangamEndDate(rs.getString(rs.getColumnIndex(hangamFieldHangamEndDate)));
                keyPairBoolData.setObject(hangamMaster);
                hangamMasters.add(keyPairBoolData);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return hangamMasters;
    }

    public HashMap<String, String> getHangamHashMap() {
        HashMap<String, String> hangamMasters = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + hangamFieldHangamId + "," + hangamFieldHangamName + " from " + hangamTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                hangamMasters.put(rs.getString(rs.getColumnIndex(hangamFieldHangamId)), rs.getString(rs.getColumnIndex(hangamFieldHangamName)));
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return hangamMasters;
    }

    public List<KeyPairBoolData> getHarvestingType() {
        List<KeyPairBoolData> harvestingTypes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + harvestingTypeFieldHarvestTypeId + "," + harvestingTypeFieldHarvestTypeNameLocal + " from " + harvestingTypeTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData harvestingType = new KeyPairBoolData();
                harvestingType.setId(rs.getInt(rs.getColumnIndex(harvestingTypeFieldHarvestTypeId)));
                harvestingType.setName(rs.getString(rs.getColumnIndex(harvestingTypeFieldHarvestTypeNameLocal)));
                harvestingTypes.add(harvestingType);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return harvestingTypes;
    }

    public List<KeyPairBoolData> getIrrigationType() {
        List<KeyPairBoolData> irrigationTypeMasters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + irrigationTypeFieldIrrigationId + "," + irrigationTypeFieldIrrigationName + " from " + irrigationTypeTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData irrigationTypeMaster = new KeyPairBoolData();
                irrigationTypeMaster.setId(rs.getInt(rs.getColumnIndex(irrigationTypeFieldIrrigationId)));
                irrigationTypeMaster.setName(rs.getString(rs.getColumnIndex(irrigationTypeFieldIrrigationName)));
                irrigationTypeMasters.add(irrigationTypeMaster);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return irrigationTypeMasters;
    }

    public List<KeyPairBoolData> getLaneType() {
        List<KeyPairBoolData> laneTypes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + laneTypeFieldLaneTypeId + "," + laneTypeFieldLaneTypeNameLocal + " from " + laneTypeTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData laneType = new KeyPairBoolData();
                laneType.setId(rs.getInt(rs.getColumnIndex(laneTypeFieldLaneTypeId)));
                laneType.setName(rs.getString(rs.getColumnIndex(laneTypeFieldLaneTypeNameLocal)));
                laneTypes.add(laneType);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return laneTypes;
    }

    public List<KeyPairBoolData> getVariety() {
        List<KeyPairBoolData> varietyMasters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + varietyFieldVarietyId + "," + varietyFieldVarietyName + " from " + varietyTable + " order by " + varietyFieldHarvestingSeq, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData varietyMaster = new KeyPairBoolData();
                varietyMaster.setId(rs.getInt(rs.getColumnIndex(varietyFieldVarietyId)));
                varietyMaster.setName(rs.getString(rs.getColumnIndex(varietyFieldVarietyName)));
                varietyMasters.add(varietyMaster);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return varietyMasters;
    }

    public HashMap<String, String> getVarietyHashMap() {
        HashMap<String, String> varietyMasters = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + varietyFieldVarietyId + "," + varietyFieldVarietyName + " from " + varietyTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                varietyMasters.put(rs.getString(rs.getColumnIndex(varietyFieldVarietyId)), rs.getString(rs.getColumnIndex(varietyFieldVarietyName)));
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return varietyMasters;
    }

    public List<VehicleTypeMaster> getVehicleType() {
        List<VehicleTypeMaster> vehicleTypeMasters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + vehicleTypeFieldVehicleTypeId + "," + vehicleTypeFieldVehicleTypeNameLocal + " from " + vehicleTypeTable + " Order by " + vehicleTypeFieldDropdownId, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                VehicleTypeMaster vehicleTypeMaster = new VehicleTypeMaster();
                vehicleTypeMaster.setNvehicleTypeId(rs.getInt(rs.getColumnIndex(vehicleTypeFieldVehicleTypeId)));
                vehicleTypeMaster.setVvehicleTypeNameLocal(rs.getString(rs.getColumnIndex(vehicleTypeFieldVehicleTypeNameLocal)));
                vehicleTypeMasters.add(vehicleTypeMaster);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return vehicleTypeMasters;
    }

    public List<KeyPairBoolData> getVehicleGroupList(String groupId) {
        List<KeyPairBoolData> vehicleGroupMasters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + vehicleGroupFieldGroupId + "," + vehicleGroupFieldGroupName + " from " + vehicleGroupTable + " Where " + vehicleGroupFieldGroupId + " in (" + groupId + ")" + " Order by " + vehicleGroupFieldGroupId, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData vehicleGroupMaster = new KeyPairBoolData();
                vehicleGroupMaster.setId(rs.getInt(rs.getColumnIndex(vehicleGroupFieldGroupId)));
                vehicleGroupMaster.setName(rs.getString(rs.getColumnIndex(vehicleGroupFieldGroupName)));
                vehicleGroupMasters.add(vehicleGroupMaster);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return vehicleGroupMasters;
    }

    public List<KeyPairBoolData> getCaneYardList() {
        List<KeyPairBoolData> caneYards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + caneYardFieldYardId + "," + caneYardFieldYardNameMar + "," + caneYardFieldTruckTracktor + "," + caneYardFieldBajat + " from " + caneYardTable + " Order by " + caneYardFieldYardId, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                CaneYard caneYard = new CaneYard();
                keyPairBoolData.setId(rs.getInt(rs.getColumnIndex(caneYardFieldYardId)));
                keyPairBoolData.setName(rs.getString(rs.getColumnIndex(caneYardFieldYardNameMar)));
                caneYard.setVtruckTracktor(rs.getString(rs.getColumnIndex(caneYardFieldTruckTracktor)));
                caneYard.setVbajat(rs.getString(rs.getColumnIndex(caneYardFieldBajat)));
                keyPairBoolData.setObject(caneYard);
                caneYards.add(keyPairBoolData);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return caneYards;
    }

    public List<KeyPairBoolData> getPumpList() {
        List<KeyPairBoolData> caneYards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + pumpFieldPumpId + "," + pumpFieldPumpNameMar + " from " + pumpTable + " Order by " + pumpFieldPumpId, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                keyPairBoolData.setId(rs.getInt(rs.getColumnIndex(pumpFieldPumpId)));
                keyPairBoolData.setName(rs.getString(rs.getColumnIndex(pumpFieldPumpNameMar)));
                caneYards.add(keyPairBoolData);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return caneYards;
    }

    public List<KeyPairBoolData> getCustomerTypeList() {
        List<KeyPairBoolData> caneYards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + customerTypeFieldCustomerTypeId + "," + customerTypeFieldCustomerTypeNameMar  + ", " + customerTypeFieldNandroidOrder + " from " + customerTypeTable + " Order by " + customerTypeFieldNandroidOrder, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                keyPairBoolData.setId(rs.getInt(rs.getColumnIndex(customerTypeFieldCustomerTypeId)));
                keyPairBoolData.setName(rs.getString(rs.getColumnIndex(customerTypeFieldCustomerTypeNameMar)));
                keyPairBoolData.setSelected(rs.getInt(rs.getColumnIndex(customerTypeFieldNandroidOrder)) == 1);
                caneYards.add(keyPairBoolData);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return caneYards;
    }
    public CaneYard getCaneYardById(String yardId) {
        CaneYard caneYard = new CaneYard();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + caneYardFieldYardId + "," + caneYardFieldYardNameMar + "," + caneYardFieldTruckTracktor + "," + caneYardFieldBajat + " from " + caneYardTable + " WHERE " + caneYardFieldYardId + " =? ", new String[]{yardId});
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                caneYard.setNyardId(rs.getInt(rs.getColumnIndex(caneYardFieldYardId)));
                caneYard.setVyardName(rs.getString(rs.getColumnIndex(caneYardFieldYardNameMar)));
                caneYard.setVtruckTracktor(rs.getString(rs.getColumnIndex(caneYardFieldTruckTracktor)));
                caneYard.setVbajat(rs.getString(rs.getColumnIndex(caneYardFieldBajat)));
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return caneYard;
    }

    public Pump getPumpById(String pumpId) {
        Pump pump = new Pump();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + pumpFieldPumpId + "," + pumpFieldPumpNameMar + "," + pumpFieldItemId + "," + pumpFieldItemName + " from " + pumpTable + " WHERE " + pumpFieldPumpId + " =? ", new String[]{pumpId});
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                pump.setNpumpId(rs.getInt(rs.getColumnIndex(pumpFieldPumpId)));
                pump.setVpumpName(rs.getString(rs.getColumnIndex(pumpFieldPumpNameMar)));
                pump.setNitemId(rs.getInt(rs.getColumnIndex(pumpFieldItemId)));
                pump.setVitemName(rs.getString(rs.getColumnIndex(pumpFieldItemName)));
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return pump;
    }

    public List<KeyPairBoolData> getVehicleType(String table, int vehicleTypeId) {
        List<KeyPairBoolData> vehicleTypeMasters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + vehicleTypeFieldVehicleTypeId + "," + vehicleTypeFieldVehicleTypeNameLocal + " from " + table + " Order by " + vehicleTypeFieldDropdownId, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData vehicleTypeMaster = new KeyPairBoolData();
                vehicleTypeMaster.setId(rs.getInt(rs.getColumnIndex(vehicleTypeFieldVehicleTypeId)));
                vehicleTypeMaster.setName(rs.getString(rs.getColumnIndex(vehicleTypeFieldVehicleTypeNameLocal)));
                vehicleTypeMaster.setSelected(rs.getInt(rs.getColumnIndex(vehicleTypeFieldVehicleTypeId)) == vehicleTypeId);
                vehicleTypeMasters.add(vehicleTypeMaster);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return vehicleTypeMasters;
    }

    public List<KeyPairBoolData> getVillage() {
        List<KeyPairBoolData> villageMasters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + villageFieldVillageId + "," + villageFieldVillageNameLocal + "," + villageFieldSectionId + "," + villageFieldVillageDistance + "," + villageFieldUnderArea + " from " + villageTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                VillageMaster villageMaster = new VillageMaster();
                keyPairBoolData.setId(rs.getInt(rs.getColumnIndex(villageFieldVillageId)));
                keyPairBoolData.setName(rs.getString(rs.getColumnIndex(villageFieldVillageNameLocal)));
                villageMaster.setNsectionId(rs.getInt(rs.getColumnIndex(villageFieldSectionId)));
                villageMaster.setNvillageDistance(rs.getString(rs.getColumnIndex(villageFieldVillageDistance)));
                villageMaster.setVunderArea(rs.getString(rs.getColumnIndex(villageFieldUnderArea)));
                keyPairBoolData.setObject(villageMaster);
                villageMasters.add(keyPairBoolData);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return villageMasters;
    }

    public List<KeyPairBoolData> getSection(int sectionId) {
        List<KeyPairBoolData> sectionMasters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + sectionFieldSectionId + "," + sectionFieldSectionNameLocal + " from " + sectionTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                keyPairBoolData.setId(rs.getInt(rs.getColumnIndex(sectionFieldSectionId)));
                keyPairBoolData.setName(rs.getString(rs.getColumnIndex(sectionFieldSectionNameLocal)));
                keyPairBoolData.setSelected(rs.getInt(rs.getColumnIndex(sectionFieldSectionId)) == sectionId);
                sectionMasters.add(keyPairBoolData);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return sectionMasters;
    }

    public HashMap<String, String> getVillageHashMap() {
        HashMap<String, String> villageMasters = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + villageFieldVillageId + "," + villageFieldVillageNameLocal + " from " + villageTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                villageMasters.put(rs.getString(rs.getColumnIndex(villageFieldVillageId)), rs.getString(rs.getColumnIndex(villageFieldVillageNameLocal)));
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return villageMasters;
    }

    public List<VillageMaster> getVillageBySection(String sectionId) {
        List<VillageMaster> villageMasters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + villageFieldVillageId + "," + villageFieldVillageNameLocal + "," + villageFieldSectionId + "," + villageFieldVillageDistance + "," + villageFieldUnderArea + " from " + villageTable + " Where " + villageFieldSectionId + " =?", new String[]{sectionId});
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                VillageMaster villageMaster = new VillageMaster();
                villageMaster.setNvillageId(rs.getInt(rs.getColumnIndex(villageFieldVillageId)));
                villageMaster.setVillageNameLocal(rs.getString(rs.getColumnIndex(villageFieldVillageNameLocal)));
                villageMaster.setNsectionId(rs.getInt(rs.getColumnIndex(villageFieldSectionId)));
                villageMaster.setNvillageDistance(rs.getString(rs.getColumnIndex(villageFieldVillageDistance)));
                villageMaster.setVunderArea(rs.getString(rs.getColumnIndex(villageFieldUnderArea)));
                villageMasters.add(villageMaster);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return villageMasters;
    }

    public HashMap<String, Object> getEntity(String farmercodeui) {
        HashMap<String, Object> returndata = new HashMap<>();
        HashMap<String, Integer> position = new HashMap<>();
        List<KeyPairBoolData> entityMasterDetails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int i = 0;
        Cursor rs = db.rawQuery("select " + entityFieldEntityUniId + "," + entityFieldEntityNameLocal + " from " + entityTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                String farmercode = rs.getString(rs.getColumnIndex(entityFieldEntityUniId));
                position.put(farmercode, i);
                KeyPairBoolData entityMasterDetail = new KeyPairBoolData();
                entityMasterDetail.setId(i++);
                entityMasterDetail.setObject(farmercode);
                entityMasterDetail.setName(rs.getString(rs.getColumnIndex(entityFieldEntityNameLocal)) + " " + farmercode);
                if (farmercodeui.equals(farmercode)) {
                    entityMasterDetail.setSelected(true);
                }
                entityMasterDetails.add(entityMasterDetail);
                rs.moveToNext();
            }
        }
        returndata.put("data", entityMasterDetails);
        returndata.put("position", position);
        closeCursor(rs);
        closeDb(db);
        return returndata;
    }

    public List<KeyPairBoolData> getCropWater(long ncropwatercondirion) {
        List<KeyPairBoolData> harvestingTypes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + cropWaterFieldNCropWaterCondition + "," + cropWaterFieldVCropWaterCondition + " from " + cropWaterTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData harvestingType = new KeyPairBoolData();
                harvestingType.setId(rs.getInt(rs.getColumnIndex(cropWaterFieldNCropWaterCondition)));
                harvestingType.setName(rs.getString(rs.getColumnIndex(cropWaterFieldVCropWaterCondition)));
                harvestingType.setSelected(harvestingType.getId() == ncropwatercondirion);
                harvestingTypes.add(harvestingType);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return harvestingTypes;
    }

    public List<KeyPairBoolData> getCaneType(int cropId) {
        List<KeyPairBoolData> caneTypeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + caneTypeFieldNCaneTypeId + "," + caneTypeFieldVCaneTypeName + " from " + caneTypeTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData caneType = new KeyPairBoolData();
                caneType.setId(rs.getInt(rs.getColumnIndex(caneTypeFieldNCaneTypeId)));
                caneType.setName(rs.getString(rs.getColumnIndex(caneTypeFieldVCaneTypeName)));
                caneType.setSelected(rs.getInt(rs.getColumnIndex(caneTypeFieldNCaneTypeId)) == cropId);
                caneTypeList.add(caneType);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return caneTypeList;
    }

    public List<KeyPairBoolData> getRemark(int remarkId) {
        List<KeyPairBoolData> remarkList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + reasonFieldRemarkId + "," + reasonFieldRemarkNameLocal + " from " + reasonMaster, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData remark = new KeyPairBoolData();
                remark.setId(rs.getInt(rs.getColumnIndex(reasonFieldRemarkId)));
                remark.setName(rs.getString(rs.getColumnIndex(reasonFieldRemarkNameLocal)));
                remark.setSelected(rs.getInt(rs.getColumnIndex(reasonFieldRemarkId)) == remarkId);
                remarkList.add(remark);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return remarkList;
    }

    public List<KeyPairBoolData> getReasonAll(int reasonGroupId) {
        List<KeyPairBoolData> resonAll = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + reasonAllFieldReasonId + "," + reasonAllFieldReasonNameLocal + " from " + reasonAllTable + " where " + reasonAllFieldReasonGroupId + "=?", new String[]{String.valueOf(reasonGroupId)});
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData reason = new KeyPairBoolData();
                reason.setId(rs.getInt(rs.getColumnIndex(reasonAllFieldReasonId)));
                reason.setName(rs.getString(rs.getColumnIndex(reasonAllFieldReasonNameLocal)));
                resonAll.add(reason);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return resonAll;
    }

    public List<KeyPairBoolData> getFertSaleType(int fertSaleTypeId) {
        List<KeyPairBoolData> FertSaleType = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + saleTypeId + "," + saleTypeName + "," + saleTypeNameMarathi + " from " + fertSaleTypeTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData fertSale = new KeyPairBoolData();
                fertSale.setId(rs.getInt(rs.getColumnIndex(saleTypeId)));
                fertSale.setName(rs.getString(rs.getColumnIndex(saleTypeNameMarathi)));
                fertSale.setObject(rs.getString(rs.getColumnIndex(saleTypeName)));
                fertSale.setSelected(rs.getInt(rs.getColumnIndex(saleTypeId)) == fertSaleTypeId);
                FertSaleType.add(fertSale);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return FertSaleType;
    }


    public List<KeyPairBoolData> getPumpSaleTypeList(int pumpSaleTypeId) {
        List<KeyPairBoolData> FertSaleType = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + pumpSaleTypeFiledTypeId + "," + pumpSaleTypeFiledTypeName + " from " + pumpSaleTypeTable, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                KeyPairBoolData fertSale = new KeyPairBoolData();
                fertSale.setId(rs.getInt(rs.getColumnIndex(pumpSaleTypeFiledTypeId)));
                fertSale.setName(rs.getString(rs.getColumnIndex(pumpSaleTypeFiledTypeName)));
                fertSale.setSelected(rs.getInt(rs.getColumnIndex(pumpSaleTypeFiledTypeId)) == pumpSaleTypeId);
                FertSaleType.add(fertSale);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return FertSaleType;
    }

    public JSONObject getRemarkKeyPair() {
        JSONObject remarkobj = new JSONObject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + reasonFieldRemarkId + "," + reasonFieldRemarkNameLocal + " from " + reasonMaster, null);
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                try {
                    remarkobj.put(rs.getString(rs.getColumnIndex(reasonFieldRemarkId)), rs.getString(rs.getColumnIndex(reasonFieldRemarkNameLocal)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return remarkobj;
    }

    // Get Master Data with or without filter and in List End

    // Get Master Data with or without filter and in id start
    public BankMaster getBankById(String bankId) {
        BankMaster bankMaster = new BankMaster();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + bankFieldBankId + "," + bankFieldbankNameLocal + " from " + bankTable + " where " + bankFieldBankId + "=?", new String[]{bankId});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                bankMaster.setNbankId(rs.getInt(rs.getColumnIndex(bankFieldBankId)));
                bankMaster.setVbankNameLocal(rs.getString(rs.getColumnIndex(bankFieldbankNameLocal)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return bankMaster;
    }

    public SectionMaster getSectionById(String sectionId) {
        SectionMaster sectionMaster = new SectionMaster();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + sectionFieldSectionId + "," + sectionFieldSectionNameLocal + " from " + sectionTable + " where " + sectionFieldSectionId + "=?", new String[]{sectionId});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                sectionMaster.setNsectionId(rs.getInt(rs.getColumnIndex(sectionFieldSectionId)));
                sectionMaster.setVsectionNameLocal(rs.getString(rs.getColumnIndex(sectionFieldSectionNameLocal)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return sectionMaster;
    }

    public CropTypeMaster getCropTypeById(String cropId) {
        CropTypeMaster cropTypeMaster = new CropTypeMaster();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + cropTypeFieldCropId + "," + cropTypeFieldCropName + " from " + cropTypeTable + " where " + cropTypeFieldCropId + "=?", new String[]{cropId});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                cropTypeMaster.setNcropId(rs.getInt(rs.getColumnIndex(cropTypeFieldCropId)));
                cropTypeMaster.setVcropName(rs.getString(rs.getColumnIndex(cropTypeFieldCropName)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return cropTypeMaster;
    }

    public FarmerCategoryMaster getFarmerCategoryById(String farmerCatId) {
        FarmerCategoryMaster farmerCategoryMaster = new FarmerCategoryMaster();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + farmerCategoryFieldFarmerCatId + "," + farmerCategoryFieldFarmerCatNameLocal + " from " + farmerCategoryTable + " where " + farmerCategoryFieldFarmerCatId + "=?", new String[]{farmerCatId});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                farmerCategoryMaster.setNfarmerCatId(rs.getInt(rs.getColumnIndex(farmerCategoryFieldFarmerCatId)));
                farmerCategoryMaster.setVfarmerCatNameLocal(rs.getString(rs.getColumnIndex(farmerCategoryFieldFarmerCatNameLocal)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return farmerCategoryMaster;
    }

    public FarmerTypeMaster getFarmerTypeById(String farmerTypeId) {
        FarmerTypeMaster farmerTypeMaster = new FarmerTypeMaster();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + farmerTypeFieldFarmerTypeId + "," + farmerTypeFieldFarmerTypeNameLocal + "," + farmerTypeFieldFarmerCatId + " from " + farmerTypeTable + " where " + farmerTypeFieldFarmerTypeId + "=?", new String[]{farmerTypeId});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                farmerTypeMaster.setNfarmerTypeId(rs.getInt(rs.getColumnIndex(farmerTypeFieldFarmerTypeId)));
                farmerTypeMaster.setVfarmerTypeNameLocal(rs.getString(rs.getColumnIndex(farmerTypeFieldFarmerTypeNameLocal)));
                farmerTypeMaster.setNfarmerCatId(rs.getInt(rs.getColumnIndex(farmerTypeFieldFarmerCatId)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return farmerTypeMaster;
    }


    public HangamMaster getHangamById(String hangamId) {
        HangamMaster hangamMaster = new HangamMaster();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + hangamFieldHangamId + "," + hangamFieldHangamName + "," + hangamFieldHangamStartDate + "," + hangamFieldHangamEndDate + " from " + hangamTable + " where " + hangamFieldHangamId + "=?", new String[]{hangamId});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                hangamMaster.setNhangamId(rs.getInt(rs.getColumnIndex(hangamFieldHangamId)));
                hangamMaster.setVhangamName(rs.getString(rs.getColumnIndex(hangamFieldHangamName)));
                hangamMaster.setDhangamStartDate(rs.getString(rs.getColumnIndex(hangamFieldHangamStartDate)));
                hangamMaster.setDhangamEndDate(rs.getString(rs.getColumnIndex(hangamFieldHangamEndDate)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return hangamMaster;
    }

    public HarvestingType getHarvestingTypeById(String harvestingTypeId) {
        HarvestingType harvestingType = new HarvestingType();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + harvestingTypeFieldHarvestTypeId + "," + harvestingTypeFieldHarvestTypeNameLocal + " from " + harvestingTypeTable + " where " + harvestingTypeFieldHarvestTypeId + "=?", new String[]{harvestingTypeId});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                harvestingType.setNharvestTypeId(rs.getInt(rs.getColumnIndex(harvestingTypeFieldHarvestTypeId)));
                harvestingType.setVharvestTypeNameLocal(rs.getString(rs.getColumnIndex(harvestingTypeFieldHarvestTypeNameLocal)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return harvestingType;
    }

    public IrrigationTypeMaster getIrrigationTypeById(String irrigationTypeId) {
        IrrigationTypeMaster irrigationTypeMaster = new IrrigationTypeMaster();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + irrigationTypeFieldIrrigationId + "," + irrigationTypeFieldIrrigationName + " from " + irrigationTypeTable + " where " + irrigationTypeFieldIrrigationId + "=?", new String[]{irrigationTypeId});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                irrigationTypeMaster.setNirrigationId(rs.getInt(rs.getColumnIndex(irrigationTypeFieldIrrigationId)));
                irrigationTypeMaster.setVirrigationName(rs.getString(rs.getColumnIndex(irrigationTypeFieldIrrigationName)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return irrigationTypeMaster;
    }

    public VarietyMaster getVarietyById(String varietyCode) {
        VarietyMaster varietyMaster = new VarietyMaster();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + varietyFieldVarietyId + "," + varietyFieldVarietyName + " from " + varietyTable + " where " + varietyFieldVarietyId + "=?", new String[]{varietyCode});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                varietyMaster.setNavarietyId(rs.getInt(rs.getColumnIndex(varietyFieldVarietyId)));
                varietyMaster.setVvariety_name(rs.getString(rs.getColumnIndex(varietyFieldVarietyName)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return varietyMaster;
    }

    public LaneType getLaneTypeById(String lateTypeId) {
        LaneType laneType = new LaneType();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + laneTypeFieldLaneTypeId + "," + laneTypeFieldLaneTypeNameLocal + " from " + laneTypeTable + " where " + laneTypeFieldLaneTypeId + "=?", new String[]{lateTypeId});
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                laneType.setNlaneTypeId(rs.getInt(rs.getColumnIndex(laneTypeFieldLaneTypeId)));
                laneType.setVlaneTypeNameLocal(rs.getString(rs.getColumnIndex(laneTypeFieldLaneTypeNameLocal)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return laneType;
    }

    public VehicleTypeMaster getVehicleTypeById(String vehicleTypeId) {
        VehicleTypeMaster vehicleTypeMaster = new VehicleTypeMaster();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + vehicleTypeFieldVehicleTypeId + "," + vehicleTypeFieldVehicleTypeNameLocal + " from " + vehicleTypeTable + " where " + vehicleTypeFieldVehicleTypeId + "=?", new String[]{vehicleTypeId});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                vehicleTypeMaster.setNvehicleTypeId(rs.getInt(rs.getColumnIndex(vehicleTypeFieldVehicleTypeId)));
                vehicleTypeMaster.setVvehicleTypeNameLocal(rs.getString(rs.getColumnIndex(vehicleTypeFieldVehicleTypeNameLocal)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return vehicleTypeMaster;
    }

    public EntityMasterDetail getEntityById(String entityUniId) {
        EntityMasterDetail entityMasterDetail = new EntityMasterDetail();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + entityFieldEntityUniId + "," + entityFieldEntityNameLocal + "," + entityFieldMobileNo + "," + entityFieldBankId + "," + entityFieldBankAcNo + "," + entityFieldVillageId + "," + entityFieldFarmerTypeId + " from " + entityTable + " where " + entityFieldEntityUniId + "=?", new String[]{entityUniId});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                entityMasterDetail.setNentityUniId(rs.getString(rs.getColumnIndex(entityFieldEntityUniId)));
                entityMasterDetail.setVentityNameLocal(rs.getString(rs.getColumnIndex(entityFieldEntityNameLocal)));
                entityMasterDetail.setVmobileNo(rs.getString(rs.getColumnIndex(entityFieldMobileNo)));
                entityMasterDetail.setNbankId(rs.getInt(rs.getColumnIndex(entityFieldBankId)));
                entityMasterDetail.setVbankAcNo(rs.getString(rs.getColumnIndex(entityFieldBankAcNo)));
                entityMasterDetail.setNvillageId(rs.getInt(rs.getColumnIndex(entityFieldVillageId)));
                entityMasterDetail.setNfarmerTypeId(rs.getInt(rs.getColumnIndex(entityFieldFarmerTypeId)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return entityMasterDetail;
    }

    public VillageMaster getVillageById(String villageId) {
        VillageMaster villageMaster = new VillageMaster();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select " + villageFieldVillageId + "," + villageFieldVillageNameLocal + "," + villageFieldSectionId + "," + villageFieldVillageDistance + "," + villageFieldUnderArea + " from " + villageTable + " where " + villageFieldVillageId + "=?", new String[]{villageId});
        if (rs.moveToFirst()) {
            if (!rs.isAfterLast()) {
                villageMaster.setNvillageId(rs.getInt(rs.getColumnIndex(villageFieldVillageId)));
                villageMaster.setVillageNameLocal(rs.getString(rs.getColumnIndex(villageFieldVillageNameLocal)));
                villageMaster.setNsectionId(rs.getInt(rs.getColumnIndex(villageFieldSectionId)));
                villageMaster.setNvillageDistance(rs.getString(rs.getColumnIndex(villageFieldVillageDistance)));
                villageMaster.setVunderArea(rs.getString(rs.getColumnIndex(villageFieldUnderArea)));
            }
        }
        closeCursor(rs);
        closeDb(db);
        return villageMaster;
    }
    // Get Master Data with or without filter and in Id End


    //Transaction Table insert Data Start
    public boolean insertAgriPlantation(PlantationBean plantationBean) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(plantationFieldvyearId, plantationBean.getVyearId());
        contentValues.put(plantationFieldEntityUniId, plantationBean.getNentityUniId());
        contentValues.put(plantationFieldPlotNo, plantationBean.getNplotNo());
        contentValues.put(plantationFieldHangamId, plantationBean.getNhangamId());
        contentValues.put(plantationFieldSurveNo, plantationBean.getVsurveNo());
        contentValues.put(plantationFieldEntryDate, plantationBean.getDentryDate());
        contentValues.put(plantationFieldPlantationDate, plantationBean.getDplantationDate());
        contentValues.put(plantationFieldIrrigationId, plantationBean.getNirrigationId());
        contentValues.put(plantationFieldVarietyId, plantationBean.getNvarietyId());
        contentValues.put(plantationFieldArea, plantationBean.getNarea());
        contentValues.put(plantationFieldExpectedYield, plantationBean.getNexpectedYield());
        contentValues.put(plantationFieldVillageId, plantationBean.getNvillageId());
        contentValues.put(plantationFieldDistance, plantationBean.getNdistance());
        contentValues.put(plantationFieldCreateDate, plantationBean.getCreateDate());
        contentValues.put(plantationFieldSectionId, plantationBean.getNsectionId());
        contentValues.put(plantationFieldFarmerTypeId, plantationBean.getNfarmerTypeId());
        contentValues.put(plantationFieldGpsArea, plantationBean.getNgpsArea());
        contentValues.put(plantationFieldGpsDistance, plantationBean.getNgpsDistance());
        contentValues.put(plantationFieldConfirmFlag, plantationBean.getNconfirmFlag());
        contentValues.put(plantationFieldPhotoPath, plantationBean.getVphotoPath());
        contentValues.put(plantationFieldConfirmphotoPath, plantationBean.getVconfirmphotoPath());
        contentValues.put(plantationFieldTentativeArea, plantationBean.getNtentativeArea());
        contentValues.put(plantationFieldCropwaterCondition, plantationBean.getNcropwaterCondition());
        contentValues.put(plantationFieldJuneFlag, plantationBean.getNjuneFlag());
        contentValues.put(plantationFieldAugustFlag, plantationBean.getNaugustFlag());
        contentValues.put(plantationFieldSoilTest, plantationBean.getVsoilTest());
        contentValues.put(plantationFieldGreenFert, plantationBean.getVgreenFert());
        contentValues.put(plantationFieldLaneTypeId, plantationBean.getNlaneTypeId());
        contentValues.put(plantationFieldBeneTreat, plantationBean.getVbeneTreat());
        contentValues.put(plantationFieldBesalDose, plantationBean.getVbesalDose());
        contentValues.put(plantationFieldDripUsed, plantationBean.getVdripUsed());
        contentValues.put(plantationFieldHarvestTypeId, plantationBean.getNharvestTypeId());
        contentValues.put(plantationFieldPlotNoOffline, plantationBean.getNplotNoOffline());
        contentValues.put(plantationFieldRegFlagOffline, plantationBean.getNregFlagOffline());
        contentValues.put(plantationFieldConfFlagOffline, plantationBean.getNconfFlagOffline());
        contentValues.put(plantationFieldEntryType, plantationBean.getNentryType());
        contentValues.put(plantationFieldServerStatus, "1");
        contentValues.put(plantationFieldGpsFlag, plantationBean.getNgpsFlag());
        contentValues.put(plantationFieldStandingLatitude, plantationBean.getVstandingLatitude());
        contentValues.put(plantationFieldStandinglongitude, plantationBean.getVstandinglongitude());
        contentValues.put(plantationFieldStandingacc, plantationBean.getNstandingacc());


        db.insert(plantationTable, null, contentValues);

        List<PlantLocation> plantLocations = plantationBean.getPlantLocations();
        int locationsize = plantLocations.size();

        for (int i = 0; i < locationsize; i++) {

            PlantLocation plant = plantLocations.get(i);
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put(latLongFieldPlotNoOffline, plant.getNplotNoOffline());
            contentValues1.put(latLongFieldvyearId, plant.getVyearId());
            contentValues1.put(latLongFieldLatitude, plant.getVlatitude());
            contentValues1.put(latLongFieldLongitude, plant.getVlongitude());
            contentValues1.put(latLongFieldAccuracy, plant.getNaccuracy());

            db.insert(latLongTable, null, contentValues1);
        }


        closeDb(db);
        return true;
    }

    //Transaction Table insert Data End


    // Transaction table fetch start

    public ArrayList<PlantationBean> getPlantationData() {
        ArrayList<PlantationBean> planattionList = new ArrayList<>();
        PlantationBean plantationBean = null;
        String curId = "0", prevId = "0";
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 1; i < 3; i++) {
            if (planattionList.size() == 10) {
                break;
            }
            String sql;
            if (i == 1) {
                sql = "select p.*, l." + latLongFieldLatitude + ", l." + latLongFieldLongitude + ", l." + latLongFieldAccuracy + " from " + plantationTable + " p, " + latLongTable + " l where p." + plantationFieldPlotNoOffline + " = l." + latLongFieldPlotNoOffline + "  and p." + plantationFieldEntryType + " = 1 and (p." + plantationFieldServerStatus + " = 1 or p." + plantationFieldServerStatus + " is null) order by p." + plantationFieldPlotNoOffline;
            } else {
                sql = "select p.* from " + plantationTable + " p where p." + plantationFieldEntryType + " = 2 and p." + plantationFieldConfirmphotoPath + " is not null and (p." + plantationFieldServerStatus + " = 1 or p." + plantationFieldServerStatus + " is null) order by p." + plantationFieldPlotNoOffline;
            }

            int count = 0;
            Cursor rs = db.rawQuery(sql, null);
            if (rs.moveToFirst()) {
                while (!rs.isAfterLast()) {
                    List<PlantLocation> plantLocations = null;
                    curId = rs.getString(rs.getColumnIndex(plantationFieldPlotNoOffline));
                    String yearId = rs.getString(rs.getColumnIndex(plantationFieldvyearId));
                    if (!curId.equals(prevId)) {
                        if (planattionList.size() == 9) {
                            break;
                        }
                        if (!prevId.equals("0")) planattionList.add(plantationBean);
                        plantationBean = new PlantationBean();
                        plantationBean.setVyearId(yearId);
                        plantationBean.setNentityUniId(rs.getString(rs.getColumnIndex(plantationFieldEntityUniId)));
                        plantationBean.setNplotNo(rs.getInt(rs.getColumnIndex(plantationFieldPlotNo)));
                        plantationBean.setNhangamId(rs.getInt(rs.getColumnIndex(plantationFieldHangamId)));
                        plantationBean.setVsurveNo(rs.getString(rs.getColumnIndex(plantationFieldSurveNo)));
                        plantationBean.setDentryDate(rs.getString(rs.getColumnIndex(plantationFieldEntryDate)));
                        plantationBean.setDplantationDate(rs.getString(rs.getColumnIndex(plantationFieldPlantationDate)));
                        plantationBean.setNirrigationId(rs.getInt(rs.getColumnIndex(plantationFieldIrrigationId)));
                        plantationBean.setNvarietyId(rs.getInt(rs.getColumnIndex(plantationFieldVarietyId)));
                        plantationBean.setNarea(rs.getDouble(rs.getColumnIndex(plantationFieldArea)));
                        plantationBean.setNexpectedYield(rs.getDouble(rs.getColumnIndex(plantationFieldExpectedYield)));
                        plantationBean.setNvillageId(rs.getInt(rs.getColumnIndex(plantationFieldVillageId)));
                        plantationBean.setNdistance(rs.getDouble(rs.getColumnIndex(plantationFieldDistance)));
                        plantationBean.setCreateDate(rs.getString(rs.getColumnIndex(plantationFieldCreateDate)));
                        plantationBean.setNsectionId(rs.getInt(rs.getColumnIndex(plantationFieldSectionId)));
                        plantationBean.setNfarmerTypeId(rs.getString(rs.getColumnIndex(plantationFieldFarmerTypeId)));
                        plantationBean.setNgpsArea(rs.getDouble(rs.getColumnIndex(plantationFieldGpsArea)));
                        plantationBean.setNgpsDistance(rs.getDouble(rs.getColumnIndex(plantationFieldGpsDistance)));
                        plantationBean.setNconfirmFlag(rs.getInt(rs.getColumnIndex(plantationFieldConfirmFlag)));
                        plantationBean.setVphotoPath(rs.getString(rs.getColumnIndex(plantationFieldPhotoPath)));
                        plantationBean.setVconfirmphotoPath(rs.getString(rs.getColumnIndex(plantationFieldConfirmphotoPath)));
                        plantationBean.setNtentativeArea(rs.getDouble(rs.getColumnIndex(plantationFieldTentativeArea)));
                        plantationBean.setNcropwaterCondition(rs.getInt(rs.getColumnIndex(plantationFieldCropwaterCondition)));
                        plantationBean.setNjuneFlag(rs.getInt(rs.getColumnIndex(plantationFieldJuneFlag)));
                        plantationBean.setNaugustFlag(rs.getInt(rs.getColumnIndex(plantationFieldAugustFlag)));
                        plantationBean.setVsoilTest(rs.getString(rs.getColumnIndex(plantationFieldSoilTest)));
                        plantationBean.setVgreenFert(rs.getString(rs.getColumnIndex(plantationFieldGreenFert)));
                        plantationBean.setNlaneTypeId(rs.getInt(rs.getColumnIndex(plantationFieldLaneTypeId)));
                        plantationBean.setVbeneTreat(rs.getString(rs.getColumnIndex(plantationFieldBeneTreat)));
                        plantationBean.setVbesalDose(rs.getString(rs.getColumnIndex(plantationFieldBesalDose)));
                        plantationBean.setVdripUsed(rs.getString(rs.getColumnIndex(plantationFieldDripUsed)));
                        plantationBean.setNharvestTypeId(rs.getInt(rs.getColumnIndex(plantationFieldHarvestTypeId)));
                        plantationBean.setNplotNoOffline(curId);
                        plantationBean.setNregFlagOffline(rs.getInt(rs.getColumnIndex(plantationFieldRegFlagOffline)));
                        plantationBean.setNconfFlagOffline(rs.getInt(rs.getColumnIndex(plantationFieldConfFlagOffline)));
                        plantationBean.setNentryType(rs.getInt(rs.getColumnIndex(plantationFieldEntryType))); //1 plantation //2 rujuwat
                        plantationBean.setNgpsFlag(rs.getInt(rs.getColumnIndex(plantationFieldGpsFlag)));
                        plantationBean.setVstandingLatitude(rs.getString(rs.getColumnIndex(plantationFieldStandingLatitude)));
                        plantationBean.setVstandinglongitude(rs.getString(rs.getColumnIndex(plantationFieldStandinglongitude)));
                        plantationBean.setNstandingacc(rs.getDouble(rs.getColumnIndex(plantationFieldStandingacc)));
                        if (i == 1) plantLocations = new ArrayList<>();
                    } else {
                        if (i == 1) plantLocations = plantationBean.getPlantLocations();
                    }

                    if (i == 1) {
                        PlantLocation plantLocation = new PlantLocation();

                        plantLocation.setNplotNoOffline(curId);
                        plantLocation.setVyearId(yearId);
                        plantLocation.setVlatitude(rs.getDouble(rs.getColumnIndex(latLongFieldLatitude)));
                        plantLocation.setVlongitude(rs.getDouble(rs.getColumnIndex(latLongFieldLongitude)));
                        plantLocation.setNaccuracy(rs.getDouble(rs.getColumnIndex(latLongFieldAccuracy)));

                        plantLocations.add(plantLocation);
                        plantationBean.setPlantLocations(plantLocations);
                    }
                    prevId = curId;
                    rs.moveToNext();
                }
                if (!prevId.equals("0")) planattionList.add(plantationBean);
            }
            closeCursor(rs);
        }
        closeDb(db);
        return planattionList;

    }

    // Transaction table fetch end

    // Transaction table Update start

    public boolean updatePlantationServer(String iddelete) {
        boolean isUpdated = false;
        if (!iddelete.trim().equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SQLiteDatabase db1 = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(plantationFieldServerStatus, "2");
            contentValues.put(plantationFieldServerTime, sdf.format(new Date()));
            contentValues.putNull(plantationFieldEntryDate);
            db1.update(plantationTable, contentValues, plantationFieldPlotNoOffline + " in (" + iddelete + ")", new String[]{});
            closeDb(db1);
        }
        return isUpdated;
    }
    // Transaction table Update end

    // close db
    public void closeDb(SQLiteDatabase db) {
        if (db.isOpen()) {
            db.close();
        }
    }

    // close cursor
    public void closeCursor(Cursor cursor) {
        if (!cursor.isClosed()) {
            cursor.close();
        }
    }

    public ArrayList<String> deletePlantation() {
        ArrayList<String> deletePhoto = new ArrayList<>();
        StringBuilder isDelete = new StringBuilder();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select ap." + plantationFieldPlotNoOffline + ", ap." + plantationFieldPhotoPath + ", ap." + plantationFieldConfirmphotoPath + ", ap." + plantationFieldServerTime + " from " + plantationTable + " ap  where " + plantationFieldServerStatus + " = ? ", new String[]{"" + "2"});
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        res.moveToFirst();
        boolean isFirst = true;
        //Toast.makeText(context, res.getCount() + " ", Toast.LENGTH_LONG ).show();
        while (!res.isAfterLast()) {
            try {
                cal1.setTime(sdf.parse(res.getString(res.getColumnIndex(plantationFieldServerTime))));
                if (cal.getTime().compareTo(cal1.getTime()) >= 0) {
                    if (!isFirst) {
                        isDelete.append(",");
                    }
                    isFirst = false;

                    isDelete.append("'").append(res.getString(res.getColumnIndex(plantationFieldPlotNoOffline))).append("'");
                    String confirmPhoto = res.getString(res.getColumnIndex(plantationFieldConfirmphotoPath));
                    if (confirmPhoto != null && !confirmPhoto.trim().equals("")) {
                        deletePhoto.add(confirmPhoto);
                    }
                    String photo = res.getString(res.getColumnIndex(plantationFieldPhotoPath));
                    if (photo != null && !photo.trim().equals("")) {
                        deletePhoto.add(photo);
                    }
                }
                res.moveToNext();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!res.isClosed()) {
            res.close();
        }
        closeDb(db);
        if (!isDelete.toString().trim().equals("")) {
            SQLiteDatabase db1 = this.getWritableDatabase();
            db1.delete(plantationTable, plantationFieldPlotNoOffline + " in (" + isDelete + ")", new String[]{});
            closeDb(db1);
        }
        return deletePhoto;
    }

    public List<CaneConfirmationRegistrationList> getCaneConfirmationList(String yearcode, String farmerCodeF, String rujtype) {
        List<CaneConfirmationRegistrationList> caneConfirmationRegistrationLists = new ArrayList<>();
        CaneConfirmationRegistrationList caneConfirmationRegistrationList = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String extraFilter = "";
        if (rujtype.equals("1")) {
            extraFilter = " and " + plantationFieldJuneFlag + "=0 and " + plantationFieldAugustFlag + "=0";
        } else if (rujtype.equals("2")) {
            extraFilter = " and " + plantationFieldAugustFlag + "=0";
        } else if (rujtype.equals("3")) {
            extraFilter = " and " + plantationFieldConfirmFlag + "<=2";
        }
        Cursor rs = db.rawQuery("select " + plantationFieldExpectedYield + " , " + plantationFieldCropwaterCondition + " , " + plantationFieldTentativeArea + " , " + plantationFieldvyearId + " , " + plantationFieldEntityUniId + " , " + plantationFieldHangamId + " , " + plantationFieldPlotNo + " , " + plantationFieldSurveNo + " , " + plantationFieldPlantationDate + " , " + plantationFieldVarietyId + " , " + plantationFieldArea + " , " + plantationFieldVillageId + " , " + plantationFieldJuneFlag + " , " + plantationFieldAugustFlag + " , " + plantationFieldEntryType + " , " + plantationFieldServerStatus + " , " + plantationFieldSoilTest + " , " + plantationFieldGreenFert + " from " + plantationTable + " p where p." + plantationFieldEntryType + " = 2 and " + plantationFieldEntityUniId + " = ? and " + plantationFieldvyearId + " = ? " + extraFilter + " order by p." + plantationFieldPlotNo, new String[]{farmerCodeF, yearcode});
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                caneConfirmationRegistrationList = new CaneConfirmationRegistrationList();
                caneConfirmationRegistrationList.setYearCode(yearcode);
                caneConfirmationRegistrationList.setNentityUniId(rs.getString(rs.getColumnIndex(plantationFieldEntityUniId)));
                caneConfirmationRegistrationList.setPlotNo(rs.getString(rs.getColumnIndex(plantationFieldPlotNo)));
                caneConfirmationRegistrationList.setServeNo(rs.getString(rs.getColumnIndex(plantationFieldSurveNo)));
                caneConfirmationRegistrationList.setDplantationDate(rs.getString(rs.getColumnIndex(plantationFieldPlantationDate)));
                caneConfirmationRegistrationList.setNcaneVarity(rs.getString(rs.getColumnIndex(plantationFieldVarietyId)));
                caneConfirmationRegistrationList.setNhungamCode(rs.getString(rs.getColumnIndex(plantationFieldHangamId)));
                caneConfirmationRegistrationList.setNarea(rs.getString(rs.getColumnIndex(plantationFieldArea)));
                caneConfirmationRegistrationList.setVilleageCode(rs.getString(rs.getColumnIndex(plantationFieldVillageId)));
                caneConfirmationRegistrationList.setNjuneFlag(rs.getString(rs.getColumnIndex(plantationFieldJuneFlag)));
                caneConfirmationRegistrationList.setNaugustFlag(rs.getString(rs.getColumnIndex(plantationFieldAugustFlag)));
                caneConfirmationRegistrationList.setFarmerVilleageName(rs.getString(rs.getColumnIndex(plantationFieldSoilTest)));
                caneConfirmationRegistrationList.setSectionCode(rs.getString(rs.getColumnIndex(plantationFieldGreenFert)));
                caneConfirmationRegistrationList.setNexpectedYield(rs.getString(rs.getColumnIndex(plantationFieldExpectedYield)));
                caneConfirmationRegistrationList.setNcropwaterCondition(rs.getLong(rs.getColumnIndex(plantationFieldCropwaterCondition)));
                caneConfirmationRegistrationList.setNtentativeArea(rs.getString(rs.getColumnIndex(plantationFieldTentativeArea)));
                caneConfirmationRegistrationLists.add(caneConfirmationRegistrationList);
                rs.moveToNext();
            }
        }
        closeCursor(rs);
        closeDb(db);
        return caneConfirmationRegistrationLists;

    }

    public boolean updateRujuwat(PlantationBean plantationBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(plantationFieldExpectedYield, plantationBean.getNexpectedYield());
        contentValues.put(plantationFieldConfirmFlag, plantationBean.getNconfirmFlag());
        contentValues.put(plantationFieldConfirmphotoPath, plantationBean.getVconfirmphotoPath());
        contentValues.put(plantationFieldTentativeArea, plantationBean.getNtentativeArea());
        contentValues.put(plantationFieldCropwaterCondition, plantationBean.getNcropwaterCondition());
        contentValues.put(plantationFieldPlotNoOffline, plantationBean.getNplotNoOffline());
        contentValues.put(plantationFieldStandingLatitude, plantationBean.getVstandingLatitude());
        contentValues.put(plantationFieldStandinglongitude, plantationBean.getVstandinglongitude());
        contentValues.put(plantationFieldGpsDistance, plantationBean.getNgpsDistance());
        contentValues.put(plantationFieldSurveNo, plantationBean.getVsurveNo());
        contentValues.put(plantationFieldEntryDate, plantationBean.getDentryDate());

        if (plantationBean.getNconfirmFlag() == 1)
            contentValues.put(plantationFieldJuneFlag, plantationBean.getNjuneFlag());
        else if (plantationBean.getNconfirmFlag() == 2)
            contentValues.put(plantationFieldAugustFlag, plantationBean.getNaugustFlag());
        contentValues.put(plantationFieldConfFlagOffline, plantationBean.getNconfFlagOffline());
        db.update(plantationTable, contentValues, plantationFieldvyearId + "=? and " + plantationFieldPlotNo + "=?", new String[]{plantationBean.getVyearId(), String.valueOf(plantationBean.getNplotNo())});
        closeDb(db);
        return true;
    }

}
