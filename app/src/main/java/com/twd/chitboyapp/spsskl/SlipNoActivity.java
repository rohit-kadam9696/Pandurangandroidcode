package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PrinterConstant;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SlipConstants;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.HarvSlipDetailsResponse;
import com.twd.chitboyapp.spsskl.pojo.RemainingSlipBean;
import com.twd.chitboyapp.spsskl.pojo.RemainingSlipResponse;
import com.twd.chitboyapp.spsskl.pojo.SlipBeanR;
import com.twd.chitboyapp.spsskl.pojo.VehicleTypeMaster;
import com.twd.chitboyapp.spsskl.printer.Bluetooth;
import com.twd.chitboyapp.spsskl.printer.DeviceListActivity;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SlipNoActivity extends AppCompatActivity implements RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slip_no);

        Intent intent = getIntent();
        String mnu_id = intent.getStringExtra("mnu_id");

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Activity activity = SlipNoActivity.this;
        AppCompatEditText edtslipno = findViewById(R.id.edtslipno);

        AppCompatButton btnreset = findViewById(R.id.btnreset);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtslipno.setText("");
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnsubmit.setEnabled(false);
                if (edtslipno.hasFocus()) {
                    edtslipno.clearFocus();
                }

                //change all code
                String slipno = edtslipno.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (sv.checkNumber(slipno)) {
                    JSONObject job = new JSONObject();
                    String action = null;
                    ConstantFunction cf = new ConstantFunction();
                    String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
                    String[] value = new String[]{"", "0", ""};
                    String[] data = cf.getSharedPrefValue(activity, key, value);
                    try {
                        job.put("nslipNo", slipno);
                        job.put("yearCode", data[2]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RequestEnum requestEnum;
                    String servlet = activity.getResources().getString(R.string.servletharvdata);
                    APIInterface apiInterface;
                    String versioncode = cf.getVersionCode();

                    if ("8".equals(mnu_id)) {
                        action = getResources().getString(R.string.actionslipedit);
                        requestEnum = RequestEnum.SLIPDATA;
                        apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                        Call<HarvSlipDetailsResponse> call1 = apiInterface.editSlip(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                        RetrofitHandler<HarvSlipDetailsResponse> reqplot1 = new RetrofitHandler<>();
                        reqplot1.handleRetrofit(call1, SlipNoActivity.this, requestEnum, servlet, activity, versioncode);
                    } else if ("82".equals(mnu_id)) {
                        action = getResources().getString(R.string.actionslipregenerate);
                        requestEnum = RequestEnum.REGENRATE;
                        apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                        Call<RemainingSlipResponse> call1 = apiInterface.remainingSlip(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                        RetrofitHandler<RemainingSlipResponse> reqplot1 = new RetrofitHandler<>();
                        reqplot1.handleRetrofit(call1, SlipNoActivity.this, requestEnum, servlet, activity, versioncode);
                    } else {
                        Constant.showToast(String.format(getResources().getString(R.string.commingsoonorupdateapp), getResources().getString(R.string.sadar)), activity, R.drawable.ic_info);
                        return;
                    }

                } else {
                    Constant.showToast(getResources().getString(R.string.errorslipnoenter), activity, R.drawable.ic_info);
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
        if (requestCaller == RequestEnum.SLIPDATA) {
            HarvSlipDetailsResponse harvSlipDetailsResponse = (HarvSlipDetailsResponse) response.body();
            intent = new Intent(SlipNoActivity.this, HarvPlotDetailsActivity.class);
            intent.putExtra("plotdetails", harvSlipDetailsResponse);
            startActivity(intent);
        } else if (requestCaller == RequestEnum.REGENRATE) {
            RemainingSlipResponse remainingSlipResponse = (RemainingSlipResponse) response.body();
            assert remainingSlipResponse != null;
            if (remainingSlipResponse.getRemainingSlipBeans().size() > 0) {
                RemainingSlipBean remainingSlipBean = remainingSlipResponse.getRemainingSlipBeans().get(0);
                DBHelper dbHelper = new DBHelper(activity);
                VehicleTypeMaster vehicleType = dbHelper.getVehicleTypeById(remainingSlipBean.getNvehicalTypeId());
                SlipConstants slipConstants = new SlipConstants();
                JSONObject remark = dbHelper.getRemarkKeyPair();
                ConstantFunction cf = new ConstantFunction();
                JSONObject job = slipConstants.prepairJob(activity, remainingSlipBean, vehicleType.getVvehicleTypeNameLocal(), remark, null);
                List<SlipBeanR> slipBeanRList = new ArrayList<>();//(List<SlipBeanR>) intent.getSerializableExtra("slipBeanRList");
                SlipBeanR slipBeanR = new SlipBeanR();
                slipBeanR.setNslipNo(remainingSlipBean.getNslipNo());
                slipBeanR.setSlipDate(remainingSlipBean.getDslipDate());
                slipBeanR.setExtraQr(remainingSlipBean.getExtraQr());
                if (remainingSlipBean.getNvehicalTypeId().equals("3") || remainingSlipBean.getNvehicalTypeId().equals("4")) {
                    slipBeanR.setNbullokCode(remainingSlipBean.getNbullockCode());
                }
                slipBeanRList.add(slipBeanR);
                String date = remainingSlipBean.getDslipDate();//intent.getStringExtra("date");
                String[] key = new String[]{activity.getResources().getString(R.string.prefharvslipdata)};
                String[] value = new String[]{job.toString()};
                cf.putSharedPrefValue(key, value, activity);
                PrinterConstant print = new PrinterConstant();
                boolean isContinue = print.connectPrinter(activity);

                if (isContinue) {
                    slipConstants.printData(activity, print, slipBeanRList, date, false);
                } else {
                    new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            slipConstants.printData(activity, print, slipBeanRList, date, false);
                        }
                    }, 4000);

                }
            } else {
                Constant.showToast("No Record Found", activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Activity activity = this;
        if (requestCode == PrinterConstant.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            PrinterConstant printerConstant = new PrinterConstant();
            printerConstant.connectPrinter(activity);
            //Bluetooth.pairPrinter(getApplicationContext(), activity);
        } else if (requestCode == PrinterConstant.REQUEST_CONNECT_DEVICE && resultCode == RESULT_OK) {
            String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            Bluetooth.pairedPrinterAddress(getApplicationContext(), activity, address);

            if (PrinterConstant.printContent != null && !PrinterConstant.printContent.equals("")) {
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PrinterConstant printerConstant = new PrinterConstant();
                        printerConstant.print(activity);
                    }
                }, 4000);
            }
        }
    }
}