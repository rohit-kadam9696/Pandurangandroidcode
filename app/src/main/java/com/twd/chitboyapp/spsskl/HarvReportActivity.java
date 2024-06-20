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
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

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
import com.twd.chitboyapp.spsskl.pojo.HarvReportReponse;
import com.twd.chitboyapp.spsskl.pojo.PDFTablePOJO;
import com.twd.chitboyapp.spsskl.pojo.TableReportBean;
import com.twd.chitboyapp.spsskl.pojo.VillageResonse;
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

public class HarvReportActivity extends AppCompatActivity implements SingleReturn, RetrofitResponse, DatePickerDialog.OnDateSetListener {

    Datepicker datepicker;
    String[] sharedPrefval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harv_report);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = HarvReportActivity.this;

        AppCompatEditText edtdate = findViewById(R.id.edtdate);
        ConstantFunction cf = new ConstantFunction();
        cf.initDate(edtdate);

        String[] key = new String[]{getResources().getString(R.string.prefofficer)};
        String[] value = new String[]{""};
        sharedPrefval = cf.getSharedPrefValue(activity, key, value);

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

        AppCompatTextView txtsectionlbl = findViewById(R.id.txtsectionlbl);
        SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
        SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);

        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;

        DBHelper dbHelper = new DBHelper(activity);

        ssh.initSingle(sspvillage, activity, getResources().getString(R.string.select));
        ssh.initSingle(sspsection, activity, getResources().getString(R.string.select));

        if (sharedPrefval[0].equals("113")) {
            List<KeyPairBoolData> dummyData = new ArrayList<>(1);
            ssh.setSingleItems(sspvillage, dummyData, DataSetter.VILLAGE);

            List<KeyPairBoolData> sectionList = dbHelper.getSection(-1);
            ssh.setSingleItems(sspsection, sectionList, DataSetter.SECTION);

            txtsectionlbl.setVisibility(View.VISIBLE);
            sspsection.setVisibility(View.VISIBLE);
        } else {
            List<KeyPairBoolData> villageList = dbHelper.getVillage();
            ssh.setSingleItems(sspvillage, villageList, DataSetter.VILLAGE);

            txtsectionlbl.setVisibility(View.GONE);
            sspsection.setVisibility(View.GONE);
        }
    }

    private void loadReport() {
        Activity activity = HarvReportActivity.this;
        SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);

        List<Long> selVillageId = sspvillage.getSelectedIds();
        String villageId;
        if (selVillageId.size() != 1) {
            Constant.showToast(getResources().getString(R.string.errorvillagenotselect), activity, R.drawable.ic_wrong);
            return;
        } else {
            villageId = selVillageId.get(0).toString();
        }

        ServerValidation sv = new ServerValidation();
        AppCompatEditText edtdate = findViewById(R.id.edtdate);
        String date = edtdate.getText().toString();
        if (!sv.checkDateddMMyyyy(date, "/")) {
            edtdate.setError(getResources().getString(R.string.errorselectdate));
            return;
        }
        // TAKE DATE HERE

        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionharvreport);

        try {
            job.put("date", date);
            job.put("villageId", villageId);
            job.put("vyearCode", sharedPrefval[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String servlet = activity.getResources().getString(R.string.servletharvdata);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        RetrofitHandler<HarvReportReponse> handler = new RetrofitHandler<>();
        Call<HarvReportReponse> call2 = apiInterface.harvReport(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
        handler.handleRetrofit(call2, HarvReportActivity.this, RequestEnum.HARVREPORT, servlet, activity, versioncode);

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
            //String header = pdf.getHederFromView(RawanaReportActivity.this, null);

            AppCompatEditText edtdate = findViewById(R.id.edtdate);
            String date = edtdate.getText().toString();
            String section = "";
            if (sharedPrefval[0].equals("113")) {
                SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
                List<KeyPairBoolData> selSect = sspsection.getSelectedItems();

                if (!selSect.isEmpty()) {
                    section = selSect.get(0).getName() + " / ";
                }
            }

            SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);
            String village;

            List<KeyPairBoolData> selVill = sspvillage.getSelectedItems();

            village = selVill.get(0).getName() + " - ";
            ConstantFunction cf = new ConstantFunction();
            String data[] = cf.getSharedPrefValue(HarvReportActivity.this, new String[]{getResources().getString(R.string.prefprintersetting), getResources().getString(R.string.prefseason)}, new String[]{String.format(getResources().getString(R.string.defaultJson), getResources().getString(R.string.myfactory)), ""});

            PDFTablePOJO pdfTablePOJO = new PDFTablePOJO();
            AppCompatTextView lbldailycaneinward = findViewById(R.id.lbldailycaneinward);
            pdfTablePOJO.setHeadText(getResources().getString(R.string.date) + " : " + date + "\n" + getResources().getString(R.string.season) + " : " + data[1] + "\n" + section + village + lbldailycaneinward.getText().toString());
            pdfTablePOJO.setTbl(findViewById(R.id.tbldailycaneinward));

            PDFTablePOJO pdfTablePOJO1 = new PDFTablePOJO();
            AppCompatTextView lblremainingcaneinfo = findViewById(R.id.lblremainingcaneinfo);
            pdfTablePOJO1.setHeadText(section + village + lblremainingcaneinfo.getText().toString());
            pdfTablePOJO1.setTbl(findViewById(R.id.tblremainingcaneinfo));

            PDFBody body = pdf.getBodyFromView(HarvReportActivity.this, false, data[0], pdfTablePOJO, pdfTablePOJO1);

            PdfCreatorExampleActivity.pdfBody = body;
            Intent intent = new Intent(HarvReportActivity.this, PdfCreatorExampleActivity.class);
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
        MenuHandler cf = new MenuHandler();
        MenuItem mnupdf = menu.findItem(R.id.mnupdf);
        mnupdf.setVisible(true);
        return cf.performRefreshOption(menu, this);
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
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.HARVREPORT) {
            HarvReportReponse HarvReportReponse = (HarvReportReponse) response.body();
            TableLayout tbldailycaneinward = findViewById(R.id.tbldailycaneinward);
            TableLayout tblremainingcaneinfo = findViewById(R.id.tblremainingcaneinfo);

            TableReportBean dailyCaneInward = HarvReportReponse.getDailyCaneInward() != null ? HarvReportReponse.getDailyCaneInward() : new TableReportBean();
            TableReportBean remainCaneInfo = HarvReportReponse.getRemainingCaneInfo() != null ? HarvReportReponse.getRemainingCaneInfo() : new TableReportBean();

            tbldailycaneinward.setTag(dailyCaneInward.getColWidth());
            tblremainingcaneinfo.setTag(remainCaneInfo.getColWidth());

            DynamicTableData dtd = new DynamicTableData();
            dtd.addTable(activity, dailyCaneInward.getTableData(), tbldailycaneinward, dailyCaneInward.getRowColSpan(), dailyCaneInward.getNoofHeads() != null ? dailyCaneInward.getNoofHeads() : 1, dailyCaneInward.isFooter(), dailyCaneInward.getBoldIndicator(), false, dailyCaneInward.getFloatings(), dailyCaneInward.isMarathi(), dailyCaneInward.getRowSpan(), dailyCaneInward.getTextAlign(), dailyCaneInward.getVisibility(), false, false, null, false);
            dtd.addTable(activity, remainCaneInfo.getTableData(), tblremainingcaneinfo, remainCaneInfo.getRowColSpan(), remainCaneInfo.getNoofHeads() != null ? remainCaneInfo.getNoofHeads() : 1, remainCaneInfo.isFooter(), remainCaneInfo.getBoldIndicator(), false, remainCaneInfo.getFloatings(), remainCaneInfo.isMarathi(), remainCaneInfo.getRowSpan(), remainCaneInfo.getTextAlign(), remainCaneInfo.getVisibility(), false, false, null, false);
        } else if (requestCaller == RequestEnum.LOADVILLAGE) {
            VillageResonse villageResonse = (VillageResonse) response.body();
            SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.singleReturn = this;
            ssh.setSingleItems(sspvillage, villageResonse.getVillList(), DataSetter.VILLAGE);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.VILLAGE) {
            loadReport();
        } else if (dataSetter == DataSetter.SECTION) {
            loadVillage();
        }
    }

    private void loadVillage() {

        Activity activity = HarvReportActivity.this;
        SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
        List<Long> selSectionIds = sspsection.getSelectedIds();
        String sectionId;
        if (selSectionIds.size() != 1) {
            Constant.showToast(getResources().getString(R.string.errorsectionnotfound), activity, R.drawable.ic_wrong);
            return;
        } else {
            sectionId = selSectionIds.get(0).toString();
        }
        String action = getResources().getString(R.string.actionvillageBySetion);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();

        try {
            job.put("sectionId", sectionId);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }
        String servlet = getResources().getString(R.string.servletharvdata);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<VillageResonse> call2 = apiInterface.villList(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<VillageResonse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, HarvReportActivity.this, RequestEnum.LOADVILLAGE, servlet, activity, versioncode);

    }
}