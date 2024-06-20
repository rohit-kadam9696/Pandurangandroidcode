package com.twd.chitboyapp.spsskl.interfaces;

import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.AgriReportReponse;
import com.twd.chitboyapp.spsskl.pojo.BankBranchResponse;
import com.twd.chitboyapp.spsskl.pojo.BranchResponse;
import com.twd.chitboyapp.spsskl.pojo.BulluckCartResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneConfirmationFarmerResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneDailyInwardReportResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneHarvestingPlanReasonResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneHarvestingPlanStart;
import com.twd.chitboyapp.spsskl.pojo.CanePlantationResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneSamplePlantationData;
import com.twd.chitboyapp.spsskl.pojo.CaneTransitResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneYardBalanceResponse;
import com.twd.chitboyapp.spsskl.pojo.CloseTransferResponse;
import com.twd.chitboyapp.spsskl.pojo.CompletePlotResponse;
import com.twd.chitboyapp.spsskl.pojo.CrushingPlantHarvVillResponse;
import com.twd.chitboyapp.spsskl.pojo.CrushingReportReponse;
import com.twd.chitboyapp.spsskl.pojo.DataListResonse;
import com.twd.chitboyapp.spsskl.pojo.DataTwoListResonse;
import com.twd.chitboyapp.spsskl.pojo.EmpVillResponse;
import com.twd.chitboyapp.spsskl.pojo.ExcessTonPlotReqDataResponse;
import com.twd.chitboyapp.spsskl.pojo.ExcessTonPlotReqResponse;
import com.twd.chitboyapp.spsskl.pojo.FarmerListResponse;
import com.twd.chitboyapp.spsskl.pojo.FarmerResponse;
import com.twd.chitboyapp.spsskl.pojo.FarmerSugarResponse;
import com.twd.chitboyapp.spsskl.pojo.FarmerTonnageResponse;
import com.twd.chitboyapp.spsskl.pojo.FertProductResponse;
import com.twd.chitboyapp.spsskl.pojo.GutKhadeResponse;
import com.twd.chitboyapp.spsskl.pojo.HarvPlotDetailsResponse;
import com.twd.chitboyapp.spsskl.pojo.HarvReportReponse;
import com.twd.chitboyapp.spsskl.pojo.HarvSlipDetailsResponse;
import com.twd.chitboyapp.spsskl.pojo.HarvestingPlanFarmerResponse;
import com.twd.chitboyapp.spsskl.pojo.HarvestorResponse;
import com.twd.chitboyapp.spsskl.pojo.LoginResponse;
import com.twd.chitboyapp.spsskl.pojo.LotGenResponse;
import com.twd.chitboyapp.spsskl.pojo.MainResponse;
import com.twd.chitboyapp.spsskl.pojo.MasterDataResponse;
import com.twd.chitboyapp.spsskl.pojo.NameListResponse;
import com.twd.chitboyapp.spsskl.pojo.NondReportHangamReponse;
import com.twd.chitboyapp.spsskl.pojo.NondReportReponse;
import com.twd.chitboyapp.spsskl.pojo.NumIndResponse;
import com.twd.chitboyapp.spsskl.pojo.NumSlipListResponse;
import com.twd.chitboyapp.spsskl.pojo.OtherUtilizationResponse;
import com.twd.chitboyapp.spsskl.pojo.PumpCustDataReponse;
import com.twd.chitboyapp.spsskl.pojo.PumpRateCheckerResponse;
import com.twd.chitboyapp.spsskl.pojo.RemainingSlipResponse;
import com.twd.chitboyapp.spsskl.pojo.SavePrintResponse;
import com.twd.chitboyapp.spsskl.pojo.SingleNumDataResponse;
import com.twd.chitboyapp.spsskl.pojo.SugarInwardResponse;
import com.twd.chitboyapp.spsskl.pojo.SugarSaleSavePrintResponse;
import com.twd.chitboyapp.spsskl.pojo.TableResponse;
import com.twd.chitboyapp.spsskl.pojo.TransporterResponse;
import com.twd.chitboyapp.spsskl.pojo.UserPumpResponse;
import com.twd.chitboyapp.spsskl.pojo.UserRoleResponse;
import com.twd.chitboyapp.spsskl.pojo.UserYardResponse;
import com.twd.chitboyapp.spsskl.pojo.VillageResonse;
import com.twd.chitboyapp.spsskl.pojo.WeightSlipResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    @FormUrlEncoded
    @POST("app_login")
    Call<LoginResponse> doLogin(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("app_login")
    Call<MainResponse> resendOtp(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("loaddata")
    Call<MasterDataResponse> loadData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("loaddata")
    Call<BranchResponse> loadDataBranch(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);


    @FormUrlEncoded
    @POST("loaddata")
    Call<BankBranchResponse> loadDataBranchList(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("farmer")
    Call<FarmerResponse> getOnlineFarmer(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @Multipart
    @POST("savePlantation")
    Call<CanePlantationResponse> appSendLaganNond(@Part ArrayList<MultipartBody.Part> file, @Part("action") RequestBody action, @Part("json") RequestBody json, @Part("imei") RequestBody imei, @Part("randomstring") RequestBody randomstring, @Part("chit_boy_id") RequestBody chit_boy_id, @Part("versionId") RequestBody versionId);

    @FormUrlEncoded
    @POST("regApproval")
    Call<ActionResponse> farmerActions(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("caneharvestingplan")
    Call<CaneHarvestingPlanStart> caneHarvestPlanStart(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("caneharvestingplan")
    Call<CaneHarvestingPlanReasonResponse> reasonData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("caneharvestingplan")
    Call<HarvestingPlanFarmerResponse> farmerData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("caneharvestingplan")
    Call<ActionResponse> actionCaneHarvestingPlan(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("caneharvestingplan")
    Call<TableResponse> tableCaneHarvestingPlan(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<TableResponse> tableHarvData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<NameListResponse> nameSearch(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("caneharvestingplan")
    Call<GutKhadeResponse> editGutKhade(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @Multipart
    @POST("updateFarmerInfo")
    Call<ActionResponse> updateFarmerInfo(@Part MultipartBody.Part file, @Part("action") RequestBody action, @Part("json") RequestBody json, @Part("imei") RequestBody imei, @Part("randomstring") RequestBody randomstring, @Part("chit_boy_id") RequestBody chit_boy_id, @Part("versionId") RequestBody versionId);

    @FormUrlEncoded
    @POST("regApproval")
    Call<FarmerListResponse> farmerListActions(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("loaddata")
    Call<CaneConfirmationFarmerResponse> loadNondListConfirmation(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("caneSample")
    Call<CaneSamplePlantationData> caneInfoByPlotAndYearCode(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("caneSample")
    Call<ActionResponse> saveCaneSample(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<HarvPlotDetailsResponse> plotDetails(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);


    @FormUrlEncoded
    @POST("harvdata")
    Call<OtherUtilizationResponse> otherUtilization(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<CompletePlotResponse> getHarvPlotDetails(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<TransporterResponse> transDetails(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<ActionResponse> actionHarvData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<CaneTransitResponse> actionCaneTransit(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<VillageResonse> villList(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<CloseTransferResponse> transferPlot(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<ExcessTonPlotReqResponse> exessListHarvData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<CaneDailyInwardReportResponse> caneDailyInward(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<ExcessTonPlotReqDataResponse> exessDetailsHarvData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<HarvestorResponse> harvDetails(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<BulluckCartResponse> mukdetailsDetails(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<RemainingSlipResponse> remainingSlip(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<WeightSlipResponse> savews(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<HarvSlipDetailsResponse> editSlip(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("mgmt")
    Call<CrushingReportReponse> crushingReport(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("mgmt")
    Call<CrushingPlantHarvVillResponse> plantHarvVillReport(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("mgmt")
    Call<AgriReportReponse> agriReport(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("canePlant")
    Call<NondReportReponse> canePlantationReport(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("canePlant")
    Call<NondReportHangamReponse> canePlantationReportHangam(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("harvdata")
    Call<HarvReportReponse> harvReport(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);


    @FormUrlEncoded
    @POST("sugarsale")
    Call<FarmerSugarResponse> getSugarDetails(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("sugarsale")
    Call<SugarInwardResponse> getInwardDetails(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("sugarsale")
    Call<SugarSaleSavePrintResponse> savePrintSugarSale(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("sugarsale")
    Call<ActionResponse> actionSugarSale(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("sugarsale")
    Call<DataListResonse> listSugarSale(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("sugarsale")
    Call<TableResponse> tableSugarSale(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("canePlant")
    Call<EmpVillResponse> employeeData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);
    @FormUrlEncoded
    @POST("harvdata")
    Call<FarmerTonnageResponse> farmerTonnage(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @Multipart
    @POST("savesugarsale")
    Call<SugarSaleSavePrintResponse> savePrintSugarSale(@Part MultipartBody.Part file, @Part("action") RequestBody action, @Part("json") RequestBody json, @Part("imei") RequestBody imei, @Part("randomstring") RequestBody randomstring, @Part("chit_boy_id") RequestBody chit_boy_id, @Part("versionId") RequestBody versionId);

    @FormUrlEncoded
    @POST("fert")
    Call<FertProductResponse> fertProduct(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("fert")
    Call<SavePrintResponse> actionFert(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<UserYardResponse> empData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<UserRoleResponse> empDataRole(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<DataTwoListResonse> empDataTwoList(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<ActionResponse> saveData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<NumSlipListResponse> numSlipData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<SingleNumDataResponse> numberData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @Multipart
    @POST("numbersyssave")
    Call<SavePrintResponse> savePrintNumber(@Part MultipartBody.Part file, @Part("action") RequestBody action, @Part("json") RequestBody json, @Part("imei") RequestBody imei, @Part("randomstring") RequestBody randomstring, @Part("chit_boy_id") RequestBody chit_boy_id, @Part("versionId") RequestBody versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<TableResponse> tableNumberSys(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<CaneYardBalanceResponse> caneYardNumberSys(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<LotGenResponse> lotGenNumberSys(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<SavePrintResponse> savePrintNumber(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<DataListResonse> numDataList(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("numbersys")
    Call<NumIndResponse> numberIndReport(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("pump")
    Call<ActionResponse> pumpActionResponse(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("pump")
    Call<UserPumpResponse> userPumpData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @FormUrlEncoded
    @POST("pump")
    Call<PumpRateCheckerResponse> pumpRateCheckerResponse(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

    @Multipart
    @POST("pumpsave")
    Call<ActionResponse> savePumpTrans(@Part MultipartBody.Part file, @Part("action") RequestBody action, @Part("json") RequestBody json, @Part("imei") RequestBody imei, @Part("randomstring") RequestBody randomstring, @Part("chit_boy_id") RequestBody chit_boy_id, @Part("versionId") RequestBody versionId);

    @FormUrlEncoded
    @POST("pump")
    Call<PumpCustDataReponse> pumpCustData(@Field("action") String action, @Field("json") String json, @Field("imei") String imei, @Field("randomstring") String randomstring, @Field("chit_boy_id") String chit_boy_id, @Field("versionId") String versionId);

}
