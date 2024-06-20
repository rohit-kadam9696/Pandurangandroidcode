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
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.pojo.FarmerTypeMaster;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FarmerDetailsActivity extends AppCompatActivity implements SingleReturn, RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_details);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        EntityMasterDetail entityMasterDetail = null;
        Intent intent = getIntent();
        if (intent.hasExtra("farmerResponse")) {
            entityMasterDetail = (EntityMasterDetail) intent.getSerializableExtra("farmerResponse");
        } else {
            Constant.showToast(getResources().getString(R.string.errorfarmernotfound), FarmerDetailsActivity.this, R.drawable.ic_wrong);
            finish();
        }
        setFarmerData(entityMasterDetail);

        AppCompatButton btnback = findViewById(R.id.btnback);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Activity activity = FarmerDetailsActivity.this;
                    String nvillage_id = null;

                    SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);
                    if (sspvillage.getSelectedItems().size() == 1) {
                        KeyPairBoolData keyPairBoolData = sspvillage.getSelectedItems().get(0);
                        nvillage_id = String.valueOf(keyPairBoolData.getId());
                    } else {
                        Constant.showToast(getResources().getString(R.string.errorvillagenotselect), activity, R.drawable.ic_wrong);
                        return;
                    }
                    AppCompatTextView txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
                    String entityUnitId = txtfarmercodetxt.getText().toString();

                    JSONObject job = new JSONObject();
                    String action = getResources().getString(R.string.actionfarmerrequestsave);
                    ConstantFunction cf = new ConstantFunction();
                    String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
                    String[] value = new String[]{"", "0"};
                    String[] data = cf.getSharedPrefValue(activity, key, value);
                    job.put("nentityUniId", entityUnitId);
                    job.put("nvillage_id", nvillage_id);
                    String servlet = activity.getResources().getString(R.string.servletregApproval);
                    APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                    String versioncode = cf.getVersionCode();
                    Call<ActionResponse> call2 = apiInterface.farmerActions(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                    RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
                    reqfarmer.handleRetrofit(call2, FarmerDetailsActivity.this, RequestEnum.SAVEFRREQ, servlet, activity, versioncode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setFarmerData(EntityMasterDetail entityMasterDetail) {
        Activity activity = this;

        AppCompatTextView txtfarmernametxt = findViewById(R.id.txtfarmernametxt);
        AppCompatTextView txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        AppCompatTextView txtfarmertypetxt = findViewById(R.id.txtfarmertypetxt);

        txtfarmernametxt.setText(entityMasterDetail.getVentityNameLocal());
        txtfarmercodetxt.setText(entityMasterDetail.getNentityUniId());

        DBHelper dbHelper = new DBHelper(FarmerDetailsActivity.this);
        FarmerTypeMaster farmerTypeMaster = dbHelper.getFarmerTypeById("" + entityMasterDetail.getNfarmerTypeId());

        txtfarmertypetxt.setText(farmerTypeMaster.getVfarmerTypeNameLocal());
        SingleSpinnerSearch sspvillage;
        sspvillage = findViewById(R.id.sspvillage);

        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.initSingle(sspvillage, activity, getResources().getString(R.string.selectvillage));


        List<KeyPairBoolData> villList = dbHelper.getVillage();
        ssh.singleReturn = this;
        ssh.setSingleItems(sspvillage, villList, DataSetter.VILLAGE);
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
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.VILLAGE) {

        }
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.SAVEFRREQ) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_wrong);
                finish();
                Intent intent = new Intent(FarmerDetailsActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}