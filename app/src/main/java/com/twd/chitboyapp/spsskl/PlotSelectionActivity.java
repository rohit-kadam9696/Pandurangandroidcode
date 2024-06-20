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
import androidx.appcompat.widget.AppCompatEditText;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneSamplePlantationData;
import com.twd.chitboyapp.spsskl.pojo.HarvPlotDetailsResponse;
import com.twd.chitboyapp.spsskl.pojo.OtherUtilizationResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class PlotSelectionActivity extends AppCompatActivity implements RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_selection);

        Intent intent = getIntent();
        String mnu_id = intent.getStringExtra("mnu_id");

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Activity activity = PlotSelectionActivity.this;
        AppCompatEditText edtplotno = findViewById(R.id.edtplotno);

        AppCompatButton btnreset = findViewById(R.id.btnreset);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtplotno.setText("");
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnsubmit.setEnabled(false);
                if (edtplotno.hasFocus()) {
                    edtplotno.clearFocus();
                }
                String plotno = edtplotno.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (sv.checkNumber(plotno)) {
                    Activity activity = PlotSelectionActivity.this;
                    JSONObject job = new JSONObject();
                    String action = null;
                    ConstantFunction cf = new ConstantFunction();
                    String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
                    String[] value = new String[]{"", "0", ""};
                    String[] data = cf.getSharedPrefValue(activity, key, value);
                    try {
                        job.put("nplotNo", plotno);
                        job.put("yearCode", data[2]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RequestEnum requestEnum;
                    String servlet = activity.getResources().getString(R.string.servletcanesample);
                    APIInterface apiInterface;
                    String versioncode = cf.getVersionCode();

                    switch (mnu_id) {
                        case "12":
                            action = getResources().getString(R.string.actioncanesampleinfodata);
                            requestEnum = RequestEnum.CANEDATA;
                            apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                            Call<CaneSamplePlantationData> call1 = apiInterface.caneInfoByPlotAndYearCode(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                            RetrofitHandler<CaneSamplePlantationData> reqplot1 = new RetrofitHandler<>();
                            reqplot1.handleRetrofit(call1, PlotSelectionActivity.this, requestEnum, servlet, activity, versioncode);
                            break;
                        case "13":
                            action = getResources().getString(R.string.actioncanesampleinfoList);
                            requestEnum = RequestEnum.CANEDATALIST;
                            apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                            Call<CaneSamplePlantationData> call2 = apiInterface.caneInfoByPlotAndYearCode(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                            RetrofitHandler<CaneSamplePlantationData> reqplot2 = new RetrofitHandler<>();
                            reqplot2.handleRetrofit(call2, PlotSelectionActivity.this, requestEnum, servlet, activity, versioncode);
                            break;
                        case "7":
                            action = getResources().getString(R.string.actionslipdataorextrarequest);
                            requestEnum = RequestEnum.SLIPDATAOREXTRAREQ;
                            servlet = getResources().getString(R.string.servletharvdata);
                            apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                            Call<HarvPlotDetailsResponse> call3 = apiInterface.plotDetails(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                            RetrofitHandler<HarvPlotDetailsResponse> reqplot3 = new RetrofitHandler<>();
                            reqplot3.handleRetrofit(call3, PlotSelectionActivity.this, requestEnum, servlet, activity, versioncode);
                            break;
                        case "10":
                            // us vilhevat cpde here
                            action = getResources().getString(R.string.actionotherutilizationdata);
                            requestEnum = RequestEnum.OTHUTILIZATION;
                            servlet = getResources().getString(R.string.servletharvdata);
                            apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                            Call<OtherUtilizationResponse> call4 = apiInterface.otherUtilization(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                            RetrofitHandler<OtherUtilizationResponse> otheruti = new RetrofitHandler<>();
                            otheruti.handleRetrofit(call4, PlotSelectionActivity.this, requestEnum, servlet, activity, versioncode);
                            break;
                        default:
                            Constant.showToast(String.format(getResources().getString(R.string.commingsoonorupdateapp), getResources().getString(R.string.sadar)), activity, R.drawable.ic_info);
                            return;
                    }

                } else {
                    Constant.showToast(getResources().getString(R.string.errorplotnoenter), activity, R.drawable.ic_info);
                }
                btnsubmit.setEnabled(true);
            }
        });
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
        Intent intent = null;
        switch (requestCaller) {
            case CANEDATA:
                CaneSamplePlantationData caneSamplePlantationData1 = (CaneSamplePlantationData) response.body();
                intent = new Intent(PlotSelectionActivity.this, CaneSampleActivity.class);
                intent.putExtra("canedataresponse", caneSamplePlantationData1);
                break;
            case CANEDATALIST:
                CaneSamplePlantationData caneSamplePlantationData2 = (CaneSamplePlantationData) response.body();
                intent = new Intent(PlotSelectionActivity.this, CaneSampleInfoActivity.class);
                intent.putExtra("canedataresponse", caneSamplePlantationData2);
                break;
            case SLIPDATAOREXTRAREQ:
                HarvPlotDetailsResponse harvPlotDetailsResponse = (HarvPlotDetailsResponse) response.body();
                intent = new Intent(PlotSelectionActivity.this, HarvPlotDetailsActivity.class);
                intent.putExtra("plotdetails", harvPlotDetailsResponse);
                break;
            case OTHUTILIZATION:
                OtherUtilizationResponse otherUtilizationResponse = (OtherUtilizationResponse) response.body();
                intent = new Intent(PlotSelectionActivity.this, OtherUtilizationActivity.class);
                intent.putExtra("otherUtilizationResponse", otherUtilizationResponse);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}