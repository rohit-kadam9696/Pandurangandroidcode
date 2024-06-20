package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DatePickerExample;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.DynamicTableData;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PDFOperations;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.DataListResonse;
import com.twd.chitboyapp.spsskl.pojo.PDFTablePOJO;
import com.twd.chitboyapp.spsskl.pojo.TableReportBean;
import com.twd.chitboyapp.spsskl.pojo.TableResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.pdfcreator.views.PDFBody;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NumVehicleRegistrationReportActivity extends AppCompatActivity implements RetrofitResponse, DatePickerDialog.OnDateSetListener, SingleReturn, View.OnClickListener {

    Datepicker datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_vehicle_registration_report);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);
        AppCompatEditText edtdate = findViewById(R.id.edtdate);
        ConstantFunction cf = new ConstantFunction();
        cf.initDate(edtdate);

        Activity activity = NumVehicleRegistrationReportActivity.this;
        SingleSpinnerSearch sspshift = findViewById(R.id.sspshift);
        SingleSpinnerSearch sspyard = findViewById(R.id.sspyard);
        SingleSpinnerSearch sspvehiclestatus = findViewById(R.id.sspvehiclestatus);

        SingleSelectHandler ssh = new SingleSelectHandler();


        DBHelper dbHelper = new DBHelper(activity);
        List<KeyPairBoolData> shiftList = new ArrayList<>();
        KeyPairBoolData data = new KeyPairBoolData();
        data.setId(0);
        data.setName(getResources().getString(R.string.all));
        data.setSelected(true);
        shiftList.add(data);

        data = new KeyPairBoolData();
        data.setId(1);
        data.setName(getResources().getString(R.string.shift4_12));
        data.setSelected(false);
        shiftList.add(data);

        data = new KeyPairBoolData();
        data.setId(2);
        data.setName(getResources().getString(R.string.shift12_8));
        data.setSelected(false);
        shiftList.add(data);

        data = new KeyPairBoolData();
        data.setId(3);
        data.setName(getResources().getString(R.string.shift8_4));
        data.setSelected(false);
        shiftList.add(data);

        ssh.initSingle(sspshift, activity, getResources().getString(R.string.selectshift));
        ssh.setSingleItems(sspshift, shiftList, DataSetter.SHIFT);


        List<KeyPairBoolData> yardList = dbHelper.getCaneYardList();
        ssh.singleReturn = this;
        ssh.initSingle(sspyard, activity, getResources().getString(R.string.selectyard));
        ssh.setSingleItems(sspyard, yardList, DataSetter.YARD);

        ArrayList<KeyPairBoolData> dummyAll = new ArrayList<>(1);
        KeyPairBoolData dummy = new KeyPairBoolData();
        dummy.setId(-1);
        dummy.setName("All");
        dummyAll.add(dummy);
        ssh.initSingle(sspvehiclestatus, activity, getResources().getString(R.string.selectstatus));
        ssh.setSingleItems(sspvehiclestatus, dummyAll, DataSetter.STATUS);

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        btnprev.setOnClickListener(this);

        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(this);

        edtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(view, activity);

                datepicker = Datepicker.REPORTDATE;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance
                String date = edtdate.getText().toString();
                Calendar calendar = Calendar.getInstance();
                long maxdate = calendar.getTimeInMillis();
                datedata.putString(getResources().getString(R.string.datepara), date);// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.maxdate), maxdate);// put string to pass with a key value
                dateFragment.setArguments(datedata);// Set bundle data to fragment
                dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
            }
        });

        loadStatus();

    }

    private void loadStatus() {
        Activity activity = NumVehicleRegistrationReportActivity.this;
        String action = getResources().getString(R.string.actionloadvehiclestatus);
        JSONObject job = new JSONObject();
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        String servlet = getResources().getString(R.string.servletnumbersys);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<DataListResonse> call2 = apiInterface.listSugarSale(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<DataListResonse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, NumVehicleRegistrationReportActivity.this, RequestEnum.LOADSTATUS, servlet, activity, versioncode);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnupdf) {
            PDFOperations pdf = new PDFOperations();

            ConstantFunction cf = new ConstantFunction();
            String data[] = cf.getSharedPrefValue(NumVehicleRegistrationReportActivity.this, new String[]{getResources().getString(R.string.prefprintersetting)}, new String[]{String.format(getResources().getString(R.string.defaultJson), getResources().getString(R.string.myfactory))});

            PDFTablePOJO pdfTablePOJO = new PDFTablePOJO();
            pdfTablePOJO.setTbl(findViewById(R.id.tbldata));
            PDFBody body = pdf.getBodyFromView(NumVehicleRegistrationReportActivity.this, false, data[0], pdfTablePOJO);

            PdfCreatorExampleActivity.pdfBody = body;
            Intent intent = new Intent(NumVehicleRegistrationReportActivity.this, PdfCreatorExampleActivity.class);
            intent.putExtra("json", data[0]);
            /*intent.putExtra("body", body);*/
            startActivity(intent);
            return true;
        } else {
            MenuHandler cf = new MenuHandler();
            return cf.openCommon(this, item, null);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem mnupdf = menu.findItem(R.id.mnupdf);
        mnupdf.setVisible(true);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, this);
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.REPORT) {
            TableLayout tbldata = findViewById(R.id.tbldata);
            TableResponse tableResponse = (TableResponse) response.body();
            TableReportBean tblBean = tableResponse.getTableData();
            DynamicTableData dtd = new DynamicTableData();
            tbldata.setTag(tblBean.getColWidth());
            dtd.addTable(activity, tblBean.getTableData(), tbldata, tblBean.getRowColSpan(), tblBean.getNoofHeads() != null ? tblBean.getNoofHeads() : 1, tblBean.isFooter(), tblBean.getBoldIndicator(), false, tblBean.getFloatings(), tblBean.isMarathi(), tblBean.getRowSpan(), tblBean.getTextAlign(), tblBean.getVisibility(), false, false, null, false);
        } else if (requestCaller == RequestEnum.LOADSTATUS) {
            SingleSpinnerSearch sspvehiclestatus = findViewById(R.id.sspvehiclestatus);
            DataListResonse dataListResonse = (DataListResonse) response.body();
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.setSingleItems(sspvehiclestatus, dataListResonse.getDataList(), DataSetter.LOTNO);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        AppCompatEditText date;
        if (datepicker == Datepicker.REPORTDATE) date = findViewById(R.id.edtdate);
        else return;
        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnsubmit) {
            Activity activity = NumVehicleRegistrationReportActivity.this;
            AppCompatEditText edtdate = findViewById(R.id.edtdate);
            SingleSpinnerSearch sspyard = findViewById(R.id.sspyard);
            SingleSpinnerSearch sspshift = findViewById(R.id.sspshift);
            SingleSpinnerSearch sspvehiclestatus = findViewById(R.id.sspvehiclestatus);
            String dateval = edtdate.getText().toString();
            String yardId = null;
            String shiftId = null;
            String vehicleStatus = null;
            String action = getResources().getString(R.string.actionvehicleregister);

            ServerValidation sv = new ServerValidation();
            if (sv.checkNull(dateval)) {
                edtdate.setError(getResources().getString(R.string.errorselectdate));
                Constant.showToast(getResources().getString(R.string.errorselectdate), activity, R.drawable.ic_wrong);
                return;
            }

            List<KeyPairBoolData> selshift = sspshift.getSelectedItems();
            if (!selshift.isEmpty()) {
                shiftId = String.valueOf(selshift.get(0).getId());
            } else {
                Constant.showToast(getResources().getString(R.string.selectshift), activity, R.drawable.ic_wrong);
                return;
            }

            List<KeyPairBoolData> selyard = sspyard.getSelectedItems();
            if (!selyard.isEmpty()) {
                yardId = String.valueOf(selyard.get(0).getId());
            } else {
                Constant.showToast(getResources().getString(R.string.selectyard), activity, R.drawable.ic_wrong);
                return;
            }

            List<KeyPairBoolData> selvehiclestatus = sspvehiclestatus.getSelectedItems();
            if (!selvehiclestatus.isEmpty()) {
                vehicleStatus = String.valueOf(selvehiclestatus.get(0).getId());
            } else {
                Constant.showToast(getResources().getString(R.string.selectstatus), activity, R.drawable.ic_wrong);
                return;
            }

            JSONObject job = new JSONObject();
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            try {
                job.put("dateVal", dateval);
                job.put("yearId", data[2]);
                job.put("yardId", yardId);
                job.put("shiftId", shiftId);
                job.put("vehicleStatus", vehicleStatus);
            } catch (JSONException e) {
                e.printStackTrace();
                Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                return;
            }

            String servlet = getResources().getString(R.string.servletnumbersys);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<TableResponse> call2 = apiInterface.tableNumberSys(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<TableResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, NumVehicleRegistrationReportActivity.this, RequestEnum.REPORT, servlet, activity, versioncode);
        } else if (view.getId() == R.id.btnprev) {
            Intent intent = new Intent(NumVehicleRegistrationReportActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }
}