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
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.adapter.RemainingSlipAdapter;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PrinterConstant;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.RemainingSlipResponse;
import com.twd.chitboyapp.spsskl.printer.Bluetooth;
import com.twd.chitboyapp.spsskl.printer.DeviceListActivity;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RemainingSlipActivity extends AppCompatActivity implements SingleReturn, RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remaining_slip);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = RemainingSlipActivity.this;
        SingleSpinnerSearch sspvehicaltype = findViewById(R.id.sspvehicaltype);

        DBHelper dbHelper = new DBHelper(activity);
        List<KeyPairBoolData> vehicaltypeList = dbHelper.getVehicleType(DBHelper.vehicleTypeTable, 0);

        SingleSelectHandler selectHandler = new SingleSelectHandler();
        selectHandler.singleReturn = this;
        selectHandler.initSingle(sspvehicaltype, activity, getResources().getString(R.string.vehicaltypesearch));
        selectHandler.setSingleItems(sspvehicaltype, vehicaltypeList, DataSetter.VEHICALTYPE);

        AppCompatImageButton btbsearch = findViewById(R.id.btnsearch);
        AppCompatEditText edtcode = findViewById(R.id.edtcode);


        btbsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<KeyPairBoolData> keyPairBoolData = sspvehicaltype.getSelectedItems();
                String code = edtcode.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (keyPairBoolData.size() == 0) {
                    Constant.showToast(getResources().getString(R.string.errorvehicaltypechoose), activity, R.drawable.ic_wrong);
                    return;
                }
                if (!sv.checkNumber(code)) {
                    edtcode.setError(getResources().getString(R.string.errorwrongcode));
                    return;
                }

                String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason), getResources().getString(R.string.chitboyprefname)};
                String[] value = new String[]{"", "0", "", ""};
                ConstantFunction cf = new ConstantFunction();
                String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

                JSONObject job = new JSONObject();
                try {
                    long vehicletypeId = keyPairBoolData.get(0).getId();
                    if (vehicletypeId < 3) job.put("transCode", code);
                    else job.put("bullockCode", code);
                    job.put("yearCode", sharedPrefval[2]);
                    job.put("vehicleType", String.valueOf(vehicletypeId));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String action = getResources().getString(R.string.actionremainingslip);
                String servlet = getResources().getString(R.string.servletharvdata);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                RetrofitHandler<RemainingSlipResponse> otphandler = new RetrofitHandler<>();
                Call<RemainingSlipResponse> call2 = apiInterface.remainingSlip(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
                otphandler.handleRetrofit(call2, RemainingSlipActivity.this, RequestEnum.REMAINSLIP, servlet, activity, versioncode, sharedPrefval[2], sharedPrefval[3], keyPairBoolData.get(0).getName());

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
        if (requestCaller == RequestEnum.REMAINSLIP) {
            RemainingSlipResponse remainingSlipResponse = (RemainingSlipResponse) response.body();
            RemainingSlipAdapter remainingSlipAdapter = new RemainingSlipAdapter(activity, remainingSlipResponse.getRemainingSlipBeans(), (String) obj[0], (String) obj[2], (String) obj[1]);
            RecyclerView mmlist = findViewById(R.id.mmlist);
            GridLayoutManager mLayoutManager1 = new GridLayoutManager(activity, 1);
            mmlist.setLayoutManager(mLayoutManager1);
            mmlist.setItemAnimator(new DefaultItemAnimator());
            mmlist.setAdapter(remainingSlipAdapter);
        }

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

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