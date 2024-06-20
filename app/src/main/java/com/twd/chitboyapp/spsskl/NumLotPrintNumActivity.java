package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.BackPress;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.CaneYard;
import com.twd.chitboyapp.spsskl.pojo.DataListResonse;
import com.twd.chitboyapp.spsskl.pojo.SavePrintResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NumLotPrintNumActivity extends AppCompatActivity implements View.OnClickListener, SingleReturn, RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_lot_print_num);
        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = this;
        SingleSpinnerSearch sspyard = findViewById(R.id.sspyard);
        SingleSpinnerSearch sspvehiclegroup = findViewById(R.id.sspvehiclegroup);
        SingleSpinnerSearch ssplotno = findViewById(R.id.ssplotno);

        DBHelper dbHelper = new DBHelper(NumLotPrintNumActivity.this);
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspyard, activity, getResources().getString(R.string.selectyard));
        ssh.setSingleItems(sspyard, dbHelper.getCaneYardList(), DataSetter.YARD);

        ssh.initSingle(sspvehiclegroup, activity, getResources().getString(R.string.selectvehiclegroup));
        ssh.initSingle(ssplotno, activity, getResources().getString(R.string.selectlot));
        ArrayList<KeyPairBoolData> dummyList = new ArrayList<>(1);
        ssh.setSingleItems(sspvehiclegroup, dummyList, DataSetter.VEHICALGROUP);
        ssh.setSingleItems(ssplotno, dummyList, DataSetter.LOTNO);

        AppCompatButton btnback = findViewById(R.id.btnback);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);

        btnback.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MenuHandler cf = new MenuHandler();
        return cf.openCommon(this, item, null);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnback) {
            onBackPressed();
        } else if (id == R.id.btnsubmit) {
            SingleSpinnerSearch sspyard = findViewById(R.id.sspyard);
            List<Long> selYard = sspyard.getSelectedIds();
            if (selYard.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectyard), NumLotPrintNumActivity.this, R.drawable.ic_wrong);
                return;
            }

            SingleSpinnerSearch sspvehiclegroup = findViewById(R.id.sspvehiclegroup);
            List<Long> selVehicleGroup = sspvehiclegroup.getSelectedIds();
            if (selVehicleGroup.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectvehiclegroup), NumLotPrintNumActivity.this, R.drawable.ic_wrong);
                return;
            }

            SingleSpinnerSearch ssplotno = findViewById(R.id.ssplotno);
            List<Long> selLotNo = ssplotno.getSelectedIds();
            if (selLotNo.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectlot), NumLotPrintNumActivity.this, R.drawable.ic_wrong);
                return;
            }

            long yardId = selYard.get(0);
            long vehicleGroupId = selVehicleGroup.get(0);
            long lotno = selLotNo.get(0);
            Activity activity = NumLotPrintNumActivity.this;

            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actionprintlot);
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            try {
                job.put("yearId", data[2]);
                job.put("yardId", yardId);
                job.put("vehicleGroupId", vehicleGroupId);
                job.put("lotno", lotno);
            } catch (JSONException e) {
                e.printStackTrace();
                Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                return;
            }

            String servlet = getResources().getString(R.string.servletnumbersys);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<SavePrintResponse> call2 = apiInterface.savePrintNumber(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<SavePrintResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, NumLotPrintNumActivity.this, RequestEnum.PRINT, servlet, activity, versioncode);
        }
    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.YARD) {
            CaneYard caneYard = (CaneYard) selectedItem.getObject();
            String groupId = "1,2";
            if (caneYard.getVtruckTracktor().equals("Y") && caneYard.getVbajat().equals("Y")) {
                groupId = "1,2";
            } else if (caneYard.getVtruckTracktor().equals("Y")) {
                groupId = "1";
            } else if (caneYard.getVbajat().equals("Y")) {
                groupId = "2";
            }
            DBHelper dbHelper = new DBHelper(NumLotPrintNumActivity.this);
            List<KeyPairBoolData> data = dbHelper.getVehicleGroupList(groupId);
            SingleSpinnerSearch sspvehiclegroup = findViewById(R.id.sspvehiclegroup);
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.singleReturn = this;
            ssh.setSingleItems(sspvehiclegroup, data, DataSetter.VEHICALGROUP);
        } else if (dataSetter == DataSetter.VEHICALGROUP) {

            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.singleReturn = this;
            SingleSpinnerSearch ssplotno = findViewById(R.id.ssplotno);

            ArrayList<KeyPairBoolData> dummyList = new ArrayList<>(1);
            ssh.setSingleItems(ssplotno, dummyList, DataSetter.LOTNO);


            SingleSpinnerSearch sspyard = findViewById(R.id.sspyard);
            List<Long> selYard = sspyard.getSelectedIds();
            if (selYard.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectyard), NumLotPrintNumActivity.this, R.drawable.ic_wrong);
                return;
            }

            SingleSpinnerSearch sspvehiclegroup = findViewById(R.id.sspvehiclegroup);
            List<Long> selVehicleGroup = sspvehiclegroup.getSelectedIds();
            if (selVehicleGroup.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectvehiclegroup), NumLotPrintNumActivity.this, R.drawable.ic_wrong);
                return;
            }

            long yardId = selYard.get(0);
            long vehicleGroupId = selVehicleGroup.get(0);
            Activity activity = NumLotPrintNumActivity.this;

            String action = getResources().getString(R.string.actionloadlot);
            JSONObject job = new JSONObject();
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            try {
                job.put("yearId", data[2]);
                job.put("yardId", yardId);
                job.put("vehicleGroupId", vehicleGroupId);
            } catch (JSONException e) {
                e.printStackTrace();
                Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                return;
            }

            String servlet = getResources().getString(R.string.servletnumbersys);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<DataListResonse> call2 = apiInterface.listSugarSale(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<DataListResonse> handleRetrofit = new RetrofitHandler<>();
            handleRetrofit.handleRetrofit(call2, NumLotPrintNumActivity.this, RequestEnum.LOADLOT, servlet, activity, versioncode);
        }
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.PRINT) {
            SavePrintResponse savePrintResponse = (SavePrintResponse) response.body();
            if (savePrintResponse.isActionComplete()) {
                Intent intent = new Intent(NumLotPrintNumActivity.this, SugarReceiptReprintActivity.class);
                intent.putExtra("html", savePrintResponse.getHtmlContent());
                intent.putExtra("mainhead", savePrintResponse.getPrintHead());
                intent.putExtra("backpress", BackPress.NUMLISTPRINT);
                startActivity(intent);
            } else {
                Constant.showToast(savePrintResponse.getFailMsg() != null ? savePrintResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        } else if (requestCaller == RequestEnum.LOADLOT) {
            SingleSpinnerSearch ssplotno = findViewById(R.id.ssplotno);
            DataListResonse dataListResonse = (DataListResonse) response.body();
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.setSingleItems(ssplotno, dataListResonse.getDataList(), DataSetter.LOTNO);
        }

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(NumLotPrintNumActivity.this, HomeActivity.class);
        startActivity(intent1);
    }
}