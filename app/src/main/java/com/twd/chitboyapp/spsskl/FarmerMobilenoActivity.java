package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class FarmerMobilenoActivity extends AppCompatActivity implements RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_mobileno);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        String farmercode = getIntent().getStringExtra("farmercode");
        setFarmer(farmercode);

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        AppCompatButton btnupdate = findViewById(R.id.btnupdate);
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndUpdate();
            }
        });
    }

    private void validateAndUpdate() {

        AppCompatTextView txtfarmercodetxt, txtoldmobilenotxt;
        AppCompatEditText edtmobileno;
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtoldmobilenotxt = findViewById(R.id.txtoldmobilenotxt);
        edtmobileno = findViewById(R.id.edtmobileno);


        String nentityUniId = txtfarmercodetxt.getText().toString();
        String oldmobile = txtoldmobilenotxt.getText().toString();
        String vmobileNo = edtmobileno.getText().toString();

        Activity activity = FarmerMobilenoActivity.this;

        if (oldmobile.equals(vmobileNo)) {
            edtmobileno.setError(getResources().getString(R.string.errorsamemobileno));
            return;
        } else {
            edtmobileno.setError(null);
        }

        ServerValidation sv = new ServerValidation();
        if (sv.checkNull(nentityUniId)) {
            Constant.showToast(getResources().getString(R.string.errornofarmerfound), activity, R.drawable.ic_wrong);
            return;
        }

        if (!sv.checkMobile(vmobileNo)) {
            edtmobileno.setError(getResources().getString(R.string.errorwrongmobile));
            return;
        }
        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionfarmermobile);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        try {
            job.put("nentityUniId", nentityUniId);
            job.put("vmobileNo", vmobileNo);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }

        String servlet = getResources().getString(R.string.servletfarmer);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<ActionResponse> call2 = apiInterface.farmerActions(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, FarmerMobilenoActivity.this, RequestEnum.UPDATEFARMERMOB, servlet, activity, versioncode, nentityUniId, vmobileNo);
    }


    private void setFarmer(String farmerCodeF) {
        DBHelper dbHelper = new DBHelper(FarmerMobilenoActivity.this);
        EntityMasterDetail entityMasterDetail = dbHelper.getEntityById(farmerCodeF);

        AppCompatTextView txtfarmercodetxt, txtfarmernametxt, txtoldmobilenotxt;
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtfarmernametxt = findViewById(R.id.txtfarmernametxt);
        txtoldmobilenotxt = findViewById(R.id.txtoldmobilenotxt);

        txtfarmercodetxt.setText(farmerCodeF);
        txtfarmernametxt.setText(entityMasterDetail.getVentityNameLocal());
        txtoldmobilenotxt.setText(entityMasterDetail.getVmobileNo());
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
        if (requestCaller == RequestEnum.UPDATEFARMERMOB) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                DBHelper dbHelper = new DBHelper(activity);
                dbHelper.updateFarmerMobile((String) obj[0], (String) obj[1]);
                finish();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}