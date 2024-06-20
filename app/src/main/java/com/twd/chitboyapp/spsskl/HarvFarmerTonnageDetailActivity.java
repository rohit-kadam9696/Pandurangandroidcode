package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.DynamicTableData;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.TableReportBean;
import com.twd.chitboyapp.spsskl.pojo.TableResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class HarvFarmerTonnageDetailActivity extends AppCompatActivity implements RetrofitResponse {

    String farmercode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harv_farmer_tonnage_detail);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Intent intent = getIntent();
        if (intent.hasExtra("farmercode")) {
            farmercode = intent.getStringExtra("farmercode");
            loadReport();
        } else {
            Constant.showToast(getResources().getString(R.string.errorfarmernotfound), HarvFarmerTonnageDetailActivity.this, R.drawable.ic_wrong);
            finish();
        }
    }

    private void loadReport() {

        Activity activity = HarvFarmerTonnageDetailActivity.this;

        String action = getResources().getString(R.string.actionfarmertonnagedetails);

        JSONObject job = new JSONObject();
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        try {
            job.put("farmercode", farmercode);
            job.put("yearId", data[2]);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }

        String servlet = getResources().getString(R.string.servletharvdata);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<TableResponse> call2 = apiInterface.tableHarvData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<TableResponse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, HarvFarmerTonnageDetailActivity.this, RequestEnum.FARMERTONNAGE, servlet, activity, versioncode);

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
        if (requestCaller == RequestEnum.FARMERTONNAGE) {
            TableLayout tbltondetails = findViewById(R.id.tbltondetails);
            TableResponse tableResponse = (TableResponse) response.body();
            TableReportBean tblBean = tableResponse.getTableData();
            DynamicTableData dtd = new DynamicTableData();
            tbltondetails.setTag(tblBean.getColWidth());
            dtd.addTable(activity, tblBean.getTableData(), tbltondetails, tblBean.getRowColSpan(), tblBean.getNoofHeads() != null ? tblBean.getNoofHeads() : 1, tblBean.isFooter(), tblBean.getBoldIndicator(), false, tblBean.getFloatings(), tblBean.isMarathi(), tblBean.getRowSpan(), tblBean.getTextAlign(), tblBean.getVisibility(), false, false, null, false);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}