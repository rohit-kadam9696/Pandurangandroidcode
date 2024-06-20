package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.BuildConfig;
import com.twd.chitboyapp.spsskl.IPSettingActivity;
import com.twd.chitboyapp.spsskl.PDFSettingActivity;
import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RefreshComplete;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.MasterDataResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class MenuHandler implements RetrofitResponse {
    int count;

    public boolean performRefreshOption(Menu menu, Activity activity) {
        MenuItem menuItem = menu.findItem(R.id.mnuinternet);
        if (menuItem != null) {
            count = 0;
            if (ConnectionUpdator.isConnected != null) {
                if (ConnectionUpdator.isConnected) {
                    menuItem.setIcon(activity.getResources().getDrawable(R.drawable.ic_internet));
                    menuItem.setTitle(activity.getResources().getString(R.string.internet));
                } else {
                    menuItem.setIcon(activity.getResources().getDrawable(R.drawable.ic_nointernet));
                    menuItem.setTitle(activity.getResources().getString(R.string.nointernet));
                }
            } else {
                menuItem.setIcon(activity.getResources().getDrawable(R.drawable.ic_ncinternet));
                menuItem.setTitle(activity.getResources().getString(R.string.ncinternet));
            }
        } else {
            count++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count <= 15)
                performRefreshOption(menu, activity);
        }
        return true;
    }

    public boolean openCommon(Activity activity, MenuItem item, RefreshComplete refreshComplete) {
        if (item.getItemId() == R.id.mnusettings) {
            Intent intent = new Intent(activity, IPSettingActivity.class);
            activity.startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.mnuaboutapp) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.setContentView(R.layout.popup_delete);

            int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);

            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

            AppCompatTextView txt_deletehead = dialog.findViewById(R.id.txt_deletehead);
            AppCompatTextView message = dialog.findViewById(R.id.message);

            txt_deletehead.setText(activity.getResources().getString(R.string.aboutapp));
            int verCode = BuildConfig.VERSION_CODE;
            String verName = BuildConfig.VERSION_NAME;
            int androidsdk = Build.VERSION.SDK_INT;
            String androidRelase = Build.VERSION.RELEASE;

            Field[] fields = Build.VERSION_CODES.class.getFields();
            StringBuilder appinfo = new StringBuilder();
            appinfo.append("App\n Version Code : ").append(verCode).append("\n Version Name : ").append(verName).append("\nAndroid\n Version Code : ").append(androidRelase);
            for (Field field : fields) {
                String fieldName = field.getName();
                int fieldValue = -1;

                try {
                    fieldValue = field.getInt(new Object());
                } catch (IllegalArgumentException | NullPointerException |
                         IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (fieldValue == Build.VERSION.SDK_INT) {
                    appinfo.append("\n Version Name : ").append(fieldName);
                }
            }
            appinfo.append("\n SDK Version : ").append(androidsdk);
            message.setText(appinfo.toString());

            AppCompatButton btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
            AppCompatButton btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);
            btncanceldelete.setVisibility(View.GONE);
            btnconfirmdelete.setText(activity.getResources().getString(R.string.ok));
            btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            dialog.show();
            return true;
        } else if (item.getItemId() == R.id.mnucache) {
            CacheHandler ch = new CacheHandler();
            ch.deleteCache(activity);
            return true;
        } else if (item.getItemId() == R.id.mnuinternet) {
            Constant.showToast(item.getTitle().toString(), activity, R.drawable.ic_info);
            return true;
        } else if (item.getItemId() == R.id.mnurefresh) {
            refreshOperation(activity, refreshComplete);
            return true;
        } else if (item.getItemId() == R.id.mnubackup) {
            AlertDialog dialog = ConstantFunction.createProgress(activity);
            ConstantFunction.showDialog(dialog);

            File sd = activity.getExternalFilesDir("");
            File data = Environment.getDataDirectory();
            FileChannel source;
            FileChannel destination;
            String currentDBPath = "/data/" + BuildConfig.APPLICATION_ID + "/databases/" + DBHelper.DATABASE_NAME;
            String backupDBPath = DBHelper.DATABASE_NAME;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Constant.showToast(activity.getResources().getString(R.string.dbexport), activity, R.drawable.ic_info);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ConstantFunction.dismissDialog(dialog);
            return true;
        } else if (item.getItemId() == R.id.mnurestore) {
            AlertDialog dialog = ConstantFunction.createProgress(activity);
            ConstantFunction.showDialog(dialog);

            File sd = activity.getExternalFilesDir("");
            File data = Environment.getDataDirectory();
            FileChannel source;
            FileChannel destination;
            String currentDBPath = "/data/" + BuildConfig.APPLICATION_ID + "/databases/" + DBHelper.DATABASE_NAME;
            String backupDBPath = DBHelper.DATABASE_NAME;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(backupDB).getChannel();
                destination = new FileOutputStream(currentDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Constant.showToast(activity.getResources().getString(R.string.dbimport), activity, R.drawable.ic_info);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ConstantFunction.dismissDialog(dialog);
            return true;
        } else if (item.getItemId() == R.id.mnupdfsetting) {
            Intent intent = new Intent(activity, PDFSettingActivity.class);
            activity.startActivity(intent);
        }
        return false;
    }

    RefreshComplete refreshComplete = null;

    public void refreshOperation(Activity activity, RefreshComplete refreshComplete) {
        this.refreshComplete = refreshComplete;
        DBHelper dbHelper = new DBHelper(activity);
        HashMap<String, Long> count = dbHelper.count(DBHelper.entityTable); // DBHelper.varietyTable will change after transaction table creation
        Long plantationCount = dbHelper.countPlantationNotSend();
        String action;
        if (count.get(DBHelper.entityTable) == 0) {
            action = activity.getResources().getString(R.string.actionall);
            performLoadData(action, activity, RequestEnum.REFRESH_ALL);
        } else if (plantationCount != 0) {
            action = activity.getResources().getString(R.string.actionmaster);
            Constant.showToast(activity.getResources().getString(R.string.errorpendingform), activity, R.drawable.ic_info);
            performLoadData(action, activity, RequestEnum.REFRESH_MASTER);
        } else {
            action = activity.getResources().getString(R.string.actionall);
            performLoadData(action, activity, RequestEnum.REFRESH_ALL);
            /*final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.setContentView(R.layout.popup_loaddata);
            dialog.setCancelable(false);

            int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);

            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

            AppCompatCheckBox chkmasterdata = dialog.findViewById(R.id.chkmasterdata);
            AppCompatCheckBox chkfarmerandvillage = dialog.findViewById(R.id.chkfarmerandvillage);
            AppCompatButton btncancel = dialog.findViewById(R.id.btncancel);
            AppCompatButton btnconfirm = dialog.findViewById(R.id.btnconfirm);

            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            btnconfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean masterdata = chkmasterdata.isChecked();
                    boolean farmerandvillage = chkfarmerandvillage.isChecked();
                    String action;
                    RequestEnum requestEnum;
                    if (masterdata && farmerandvillage) {
                        action = activity.getResources().getString(R.string.actionall);
                        requestEnum = RequestEnum.REFRESH_ALL;
                    } else if (masterdata) {
                        action = activity.getResources().getString(R.string.actionmaster);
                        requestEnum = RequestEnum.REFRESH_MASTER;
                    } else if (farmerandvillage) {
                        action = activity.getResources().getString(R.string.actionfarmerandvillege);
                        requestEnum = RequestEnum.REFRESH_FARMER;
                    } else {
                        Constant.showToast(activity.getResources().getString(R.string.errorselectloaddata), activity, R.drawable.ic_wrong);
                        return;
                    }
                    performLoadData(action, activity, requestEnum);
                    dialog.cancel();
                }
            });
            dialog.show();*/
        }
    }

    private void performLoadData(String action, Activity activity, RequestEnum refresh) {

        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{activity.getResources().getString(R.string.chitboyprefuniquestring), activity.getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        String servlet = activity.getResources().getString(R.string.servletloaddata);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<MasterDataResponse> call2 = apiInterface.loadData(action, "{}", cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<MasterDataResponse> otpcaller = new RetrofitHandler<>();
        otpcaller.handleRetrofit(call2, MenuHandler.this, refresh, servlet, activity, versioncode);

    }

    private void insertMaster(DBHelper dbHelper, MasterDataResponse masterDataResponse) {
        if (masterDataResponse.getMenuList() != null)
            dbHelper.insertMenu(masterDataResponse.getMenuList());
        if (masterDataResponse.getBankList() != null)
            dbHelper.insertBank(masterDataResponse.getBankList());
        if (masterDataResponse.getCropTypeList() != null)
            dbHelper.insertCropType(masterDataResponse.getCropTypeList());
        if (masterDataResponse.getFarmerCategoryList() != null)
            dbHelper.insertFarmerCategory(masterDataResponse.getFarmerCategoryList());
        if (masterDataResponse.getFarmerTypeList() != null)
            dbHelper.insertFarmerType(masterDataResponse.getFarmerTypeList());
        if (masterDataResponse.getHangamList() != null)
            dbHelper.insertHangam(masterDataResponse.getHangamList());
        if (masterDataResponse.getHarvestingTypeList() != null)
            dbHelper.insertHarvestingType(masterDataResponse.getHarvestingTypeList());
        if (masterDataResponse.getIrrigationTypeList() != null)
            dbHelper.insertIrrigationType(masterDataResponse.getIrrigationTypeList());
        if (masterDataResponse.getLagneTypekList() != null)
            dbHelper.insertLaneType(masterDataResponse.getLagneTypekList());
        if (masterDataResponse.getSectionList() != null)
            dbHelper.insertSection(masterDataResponse.getSectionList());
        if (masterDataResponse.getVarietyList() != null)
            dbHelper.insertVariety(masterDataResponse.getVarietyList());
        if (masterDataResponse.getVehicleTypeList() != null)
            dbHelper.insertVehicleType(masterDataResponse.getVehicleTypeList());
        if (masterDataResponse.getCropWaterList() != null)
            dbHelper.insertCropWater(masterDataResponse.getCropWaterList());
        if (masterDataResponse.getCaneConfirmList() != null)
            dbHelper.insertCaneConfirmation(masterDataResponse.getCaneConfirmList());
        if (masterDataResponse.getCaneTypeMasters() != null)
            dbHelper.insertCaneType(masterDataResponse.getCaneTypeMasters());
        if (masterDataResponse.getReasonMasters() != null)
            dbHelper.insertReason(masterDataResponse.getReasonMasters());
        if (masterDataResponse.getReasonAllMasters() != null)
            dbHelper.insertReasonAll(masterDataResponse.getReasonAllMasters());
        if (masterDataResponse.getFertSaleTypes() != null)
            dbHelper.insertFertSaleType(masterDataResponse.getFertSaleTypes());
        if (masterDataResponse.getCaneYards() != null)
            dbHelper.insertCaneYard(masterDataResponse.getCaneYards());
        if (masterDataResponse.getVehicleGroupMasters() != null)
            dbHelper.insertVehicleGroup(masterDataResponse.getVehicleGroupMasters());
        if (masterDataResponse.getPumps() != null)
            dbHelper.insertPump(masterDataResponse.getPumps());
        if (masterDataResponse.getPumpSaleTypes() != null)
            dbHelper.insertPumpSaleType(masterDataResponse.getPumpSaleTypes());
        if (masterDataResponse.getCustomerTypes() != null)
            dbHelper.insertCustomerType(masterDataResponse.getCustomerTypes());
    }

    private void insertFarmerAndVillage(DBHelper dbHelper, MasterDataResponse masterDataResponse) {
        if (masterDataResponse.getFarmerList() != null)
            dbHelper.insertEntity(masterDataResponse.getFarmerList());
        if (masterDataResponse.getVillageList() != null)
            dbHelper.insertVillage(masterDataResponse.getVillageList());
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {

        Observable.fromCallable(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                final AlertDialog[] loading = new AlertDialog[1];
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        loading[0] = ConstantFunction.createProgress(activity, activity.getResources().getString(R.string.informationsaving));
                        ConstantFunction.showDialog(loading[0]);
                    }
                });

                DBHelper dbHelper = new DBHelper(activity);
                if (requestCaller == RequestEnum.REFRESH_ALL) {
                    String[] tables = new String[]{DBHelper.menuTable, DBHelper.bankTable, DBHelper.cropTypeTable, DBHelper.farmerCategoryTable, DBHelper.farmerTypeTable, DBHelper.hangamTable, DBHelper.harvestingTypeTable, DBHelper.irrigationTypeTable, DBHelper.laneTypeTable, DBHelper.sectionTable, DBHelper.varietyTable, DBHelper.vehicleTypeTable, DBHelper.cropWaterTable, DBHelper.entityTable, DBHelper.villageTable, DBHelper.caneTypeTable, DBHelper.reasonMaster, DBHelper.fertSaleTypeTable, DBHelper.vehicleGroupTable, DBHelper.caneYardTable, DBHelper.reasonAllTable, DBHelper.pumpTable, DBHelper.pumpSaleTypeTable, DBHelper.customerTypeTable};
                    dbHelper.delete(tables);
                    MasterDataResponse masterDataResponse = (MasterDataResponse) response.body();
                    insertMaster(dbHelper, masterDataResponse);
                    insertFarmerAndVillage(dbHelper, masterDataResponse);
                } else if (requestCaller == RequestEnum.REFRESH_MASTER) {
                    String[] tables = new String[]{DBHelper.menuTable, DBHelper.bankTable, DBHelper.cropTypeTable, DBHelper.farmerCategoryTable, DBHelper.farmerTypeTable, DBHelper.hangamTable, DBHelper.harvestingTypeTable, DBHelper.irrigationTypeTable, DBHelper.laneTypeTable, DBHelper.sectionTable, DBHelper.varietyTable, DBHelper.vehicleTypeTable, DBHelper.cropWaterTable, DBHelper.caneTypeTable, DBHelper.reasonMaster, DBHelper.fertSaleTypeTable, DBHelper.vehicleGroupTable, DBHelper.caneYardTable, DBHelper.reasonAllTable, DBHelper.pumpTable, DBHelper.pumpSaleTypeTable, DBHelper.customerTypeTable};
                    dbHelper.delete(tables);
                    MasterDataResponse masterDataResponse = (MasterDataResponse) response.body();
                    insertMaster(dbHelper, masterDataResponse);
                } else if (requestCaller == RequestEnum.REFRESH_FARMER) {
                    MasterDataResponse masterDataResponse = (MasterDataResponse) response.body();
                    String[] tables = new String[]{DBHelper.entityTable, DBHelper.villageTable};
                    dbHelper.delete(tables);
                    insertFarmerAndVillage(dbHelper, masterDataResponse);
                }

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        ConstantFunction.dismissDialog(loading[0]);
                        if (refreshComplete != null) {
                            refreshComplete.onRefreshComplete();
                        }
                    }
                });


                return "1";

            }
        }).subscribeOn(Schedulers.computation()).subscribe();


    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}
