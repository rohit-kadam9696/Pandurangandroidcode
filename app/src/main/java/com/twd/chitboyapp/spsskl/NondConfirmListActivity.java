package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.adapter.NCRegistrationListAdapter;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.PlantListCaller;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneConfirmationFarmerResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneConfirmationRegistrationList;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.pojo.SectionMaster;
import com.twd.chitboyapp.spsskl.pojo.VillageMaster;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NondConfirmListActivity extends AppCompatActivity implements RetrofitResponse {

    String rujtype;
    String farmercode;
    PlantListCaller plantListCaller;
    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nond_confirm_list);
        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Intent intent = getIntent();
        rujtype = null;
        if (intent.hasExtra("rujtype")) {
            rujtype = intent.getStringExtra("rujtype");
        }
        farmercode = intent.getStringExtra("farmercode");
        plantListCaller = (PlantListCaller) intent.getSerializableExtra("caller");
        loadData(false);
        count++;
    }

    private void loadData(boolean isBack) {
        String farmerCodeF = "F" + farmercode;
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(NondConfirmListActivity.this, key, value);


        Activity activity = NondConfirmListActivity.this;
        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionfarmerrujulistdata);
        try {
            //nentityUniId, yearCode     moved to else
            if (plantListCaller == PlantListCaller.NONDCONFIRM) {
                job.put("rujtype", rujtype);
                job.put("caller", "1");
                DBHelper dbHelper = new DBHelper(activity);
                List<CaneConfirmationRegistrationList> list = dbHelper.getCaneConfirmationList(sharedPrefval[2], farmerCodeF, rujtype);
                setAdapter(list, farmerCodeF, activity, (String) sharedPrefval[2], rujtype, plantListCaller, dbHelper, null, null, null);
            } else {
                job.put("nentityUniId", farmerCodeF);
                job.put("yearCode", sharedPrefval[2]);
                job.put("caller", "2");
                String servlet = activity.getResources().getString(R.string.servletloaddata);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                RetrofitHandler<CaneConfirmationFarmerResponse> otphandler = new RetrofitHandler<>();
                Call<CaneConfirmationFarmerResponse> call2 = apiInterface.loadNondListConfirmation(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
                otphandler.handleRetrofit(call2, NondConfirmListActivity.this, RequestEnum.NONDLISTFORCONFIRM, servlet, activity, versioncode, rujtype, sharedPrefval[2], farmerCodeF, plantListCaller, isBack);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (count > 1)
            loadData(true);
        count++;
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
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.NONDLISTFORCONFIRM) {
            DBHelper dbHelper = new DBHelper(activity);
            CaneConfirmationFarmerResponse caneConfirmationFarmerResponse = (CaneConfirmationFarmerResponse) response.body();
            String farmerCodeF = (String) obj[2];

            if ((boolean) obj[4] && caneConfirmationFarmerResponse.getList().size() == 0) {
                Intent intent = new Intent(NondConfirmListActivity.this, HomeActivity.class);
                startActivity(intent);
                return;
            }
            setAdapter(caneConfirmationFarmerResponse.getList(), farmerCodeF, activity, (String) obj[1], (String) obj[0], (PlantListCaller) obj[3], dbHelper, caneConfirmationFarmerResponse.getSectionCode(), caneConfirmationFarmerResponse.getFarmerVilleageName(), caneConfirmationFarmerResponse.getFarmerName());
        }
    }

    private void setAdapter(List<CaneConfirmationRegistrationList> list, String farmerCodeF, Activity activity, String yearCode, String rujutype, PlantListCaller plantListCaller, DBHelper dbHelper, String sectionCode, String farmerVilleageName, String farmerName) {


        AppCompatTextView txtseasonval, txtfarmercodetxt, txtfarmernametxt, txtvillagetxt, txtsectiontxt;
        txtvillagetxt = findViewById(R.id.txtvillagetxt);
        txtsectiontxt = findViewById(R.id.txtsectiontxt);
        txtseasonval = findViewById(R.id.txtseasonval);
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtfarmernametxt = findViewById(R.id.txtfarmernametxt);


        EntityMasterDetail entityMasterDetail = dbHelper.getEntityById(farmerCodeF);

        if (farmerVilleageName != null)
            txtvillagetxt.setText(farmerVilleageName);
        else {
            VillageMaster vm = dbHelper.getVillageById(String.valueOf(entityMasterDetail.getNvillageId()));
            txtvillagetxt.setText(vm.getVillageNameLocal() != null ? vm.getVillageNameLocal() : "-");
            if (sectionCode == null)
                sectionCode = String.valueOf(vm.getNsectionId());
        }

        if (sectionCode != null) {
            SectionMaster sectionMaster = dbHelper.getSectionById(sectionCode);
            txtsectiontxt.setText(sectionMaster.getVsectionNameLocal());
        } else
            txtsectiontxt.setText("-");

        txtseasonval.setText(yearCode);
        txtfarmercodetxt.setText(farmerCodeF);
        if (farmerName != null)
            txtfarmernametxt.setText(farmerName);
        else
            txtfarmernametxt.setText(entityMasterDetail.getVentityNameLocal());


        NCRegistrationListAdapter npFarmerListAdapter = new NCRegistrationListAdapter(list, activity, yearCode, rujutype, entityMasterDetail, plantListCaller);
        RecyclerView mmlist = findViewById(R.id.mmlist);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(activity, 1);
        mmlist.setLayoutManager(mLayoutManager1);
        mmlist.setItemAnimator(new DefaultItemAnimator());
        mmlist.setAdapter(npFarmerListAdapter);


    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}