package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DatePickerExample;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.DynamicTableData;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.CrushingPlantHarvVillResponse;
import com.twd.chitboyapp.spsskl.pojo.CrushingReportReponse;
import com.twd.chitboyapp.spsskl.pojo.TableReportBean;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

public class CrushingReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, RetrofitResponse, SingleReturn {

    Datepicker datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crushing_report);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(CrushingReportActivity.this);
        connectionUpdator.startObserve(CrushingReportActivity.this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Activity activity = this;
        AppCompatEditText edtreportdate = findViewById(R.id.edtreportdate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -4);
        edtreportdate.setText(sdf.format(cal.getTime()));

        SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);

        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspsection, activity, getResources().getString(R.string.selectsection));

        ssh.setSingleItems(sspsection, new ArrayList<>(), DataSetter.SECTION);

        loadReport();

        edtreportdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(view, activity);

                datepicker = Datepicker.REPORTDATE;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance
                String date = edtreportdate.getText().toString();
                Calendar cal = Calendar.getInstance();
                datedata.putString(getResources().getString(R.string.datepara), date);// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.maxdate), cal.getTimeInMillis());// put string to pass with a key value
                dateFragment.setArguments(datedata);// Set bundle data to fragment
                dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        AppCompatEditText date;
        if (datepicker == Datepicker.REPORTDATE) {
            date = findViewById(R.id.edtreportdate);
        } else {
            return;
        }
        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
        loadReport();
    }

    private void loadReport() {
        AppCompatEditText edtreportdate = findViewById(R.id.edtreportdate);

        String date = edtreportdate.getText().toString();
        ServerValidation sv = new ServerValidation();
        if (sv.checkDateddMMyyyy(date, "/")) {
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
            String[] value = new String[]{"", "0"};
            ConstantFunction cf = new ConstantFunction();
            Activity activity = CrushingReportActivity.this;
            String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actioncrushingreport);

            try {
                job.put("date", date);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String servlet = activity.getResources().getString(R.string.servletmgmt);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            RetrofitHandler<CrushingReportReponse> handler = new RetrofitHandler<>();
            Call<CrushingReportReponse> call2 = apiInterface.crushingReport(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
            handler.handleRetrofit(call2, CrushingReportActivity.this, RequestEnum.CRUSHINGREPORT, servlet, activity, versioncode);

        }

    }

    private void loadReportVillage(Long sectionCode) {
        AppCompatEditText edtreportdate = findViewById(R.id.edtreportdate);

        String date = edtreportdate.getText().toString();
        ServerValidation sv = new ServerValidation();
        if (sv.checkDateddMMyyyy(date, "/")) {
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
            String[] value = new String[]{"", "0"};
            ConstantFunction cf = new ConstantFunction();
            Activity activity = CrushingReportActivity.this;
            String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actionplantharvvill);

            try {
                job.put("date", date);
                job.put("sectionCode", sectionCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String servlet = activity.getResources().getString(R.string.servletmgmt);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            RetrofitHandler<CrushingPlantHarvVillResponse> handler = new RetrofitHandler<>();
            Call<CrushingPlantHarvVillResponse> call2 = apiInterface.plantHarvVillReport(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
            handler.handleRetrofit(call2, CrushingReportActivity.this, RequestEnum.PLANTHARVVILLREPORT, servlet, activity, versioncode);

        }

    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        DynamicTableData dtd = new DynamicTableData();
        TableLayout tblvilltonnage = findViewById(R.id.tblvilltonnage);
        TableReportBean villTonnage;
        switch (requestCaller) {
            case CRUSHINGREPORT:
                CrushingReportReponse crushingReportReponse = (CrushingReportReponse) response.body();
                AppCompatTextView txthangamday = findViewById(R.id.txthangamday);
                TableLayout tbldailycrushing = findViewById(R.id.tbldailycrushing);
                TableLayout tbllabreport = findViewById(R.id.tbllabreport);
                TableLayout tblhangamtonnage = findViewById(R.id.tblhangamtonnage);
                TableLayout tblvarietytonnage = findViewById(R.id.tblvarietytonnage);
                TableLayout tblcropTypetonnage = findViewById(R.id.tblcropTypetonnage);
                TableLayout tblsectiontonnage = findViewById(R.id.tblsectiontonnage);
                TableLayout tblramaincane = findViewById(R.id.tblramaincane);

                SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);

                SingleSelectHandler ssh = new SingleSelectHandler();
                ssh.singleReturn = this;
                ssh.setSingleItems(sspsection, crushingReportReponse.getSectionList() != null ? crushingReportReponse.getSectionList() : new ArrayList<>(), DataSetter.SECTION);

                txthangamday.setText(crushingReportReponse.getNhangamDay());
                TableReportBean dailyCrusing = crushingReportReponse.getDailyCrushing() != null ? crushingReportReponse.getDailyCrushing() : new TableReportBean();
                TableReportBean labSummary = crushingReportReponse.getDailyLabSummary() != null ? crushingReportReponse.getDailyLabSummary() : new TableReportBean();
                TableReportBean hangamtonnage = crushingReportReponse.getHangamTonnage() != null ? crushingReportReponse.getHangamTonnage() : new TableReportBean();
                TableReportBean varietytonnage = crushingReportReponse.getVarietyTonnage() != null ? crushingReportReponse.getVarietyTonnage() : new TableReportBean();
                TableReportBean cropTypetonnage = crushingReportReponse.getCropTypeTonnage() != null ? crushingReportReponse.getCropTypeTonnage() : new TableReportBean();
                TableReportBean sectiontonnage = crushingReportReponse.getSectionTonnage() != null ? crushingReportReponse.getSectionTonnage() : new TableReportBean();
                TableReportBean remainCane = crushingReportReponse.getRemainCane() != null ? crushingReportReponse.getRemainCane() : new TableReportBean();
                villTonnage = crushingReportReponse.getVillageTonnage() != null ? crushingReportReponse.getVillageTonnage() : new TableReportBean();

                dtd.addTable(activity, dailyCrusing.getTableData(), tbldailycrushing, dailyCrusing.getRowColSpan(), dailyCrusing.getNoofHeads() != null ? dailyCrusing.getNoofHeads() : 1, dailyCrusing.isFooter(), dailyCrusing.getBoldIndicator(), true, dailyCrusing.getFloatings(), dailyCrusing.isMarathi(), dailyCrusing.getRowSpan(), dailyCrusing.getTextAlign(), dailyCrusing.getVisibility(), false, false, null, false);
                dtd.addTable(activity, labSummary.getTableData(), tbllabreport, labSummary.getRowColSpan(), labSummary.getNoofHeads() != null ? labSummary.getNoofHeads() : 1, labSummary.isFooter(), labSummary.getBoldIndicator(), true, labSummary.getFloatings(), labSummary.isMarathi(), labSummary.getRowSpan(), labSummary.getTextAlign(), labSummary.getVisibility(), false, false, null, false);
                dtd.addTable(activity, hangamtonnage.getTableData(), tblhangamtonnage, hangamtonnage.getRowColSpan(), hangamtonnage.getNoofHeads() != null ? hangamtonnage.getNoofHeads() : 1, hangamtonnage.isFooter(), hangamtonnage.getBoldIndicator(), true, hangamtonnage.getFloatings(), hangamtonnage.isMarathi(), hangamtonnage.getRowSpan(), hangamtonnage.getTextAlign(), hangamtonnage.getVisibility(), false, false, null, false);
                dtd.addTable(activity, varietytonnage.getTableData(), tblvarietytonnage, varietytonnage.getRowColSpan(), varietytonnage.getNoofHeads() != null ? varietytonnage.getNoofHeads() : 1, varietytonnage.isFooter(), varietytonnage.getBoldIndicator(), true, varietytonnage.getFloatings(), varietytonnage.isMarathi(), varietytonnage.getRowSpan(), varietytonnage.getTextAlign(), varietytonnage.getVisibility(), false, false, null, false);
                dtd.addTable(activity, cropTypetonnage.getTableData(), tblcropTypetonnage, cropTypetonnage.getRowColSpan(), cropTypetonnage.getNoofHeads() != null ? cropTypetonnage.getNoofHeads() : 1, cropTypetonnage.isFooter(), cropTypetonnage.getBoldIndicator(), true, cropTypetonnage.getFloatings(), cropTypetonnage.isMarathi(), cropTypetonnage.getRowSpan(), cropTypetonnage.getTextAlign(), cropTypetonnage.getVisibility(), false, false, null, false);
                dtd.addTable(activity, sectiontonnage.getTableData(), tblsectiontonnage, sectiontonnage.getRowColSpan(), sectiontonnage.getNoofHeads() != null ? sectiontonnage.getNoofHeads() : 1, sectiontonnage.isFooter(), sectiontonnage.getBoldIndicator(), true, sectiontonnage.getFloatings(), sectiontonnage.isMarathi(), sectiontonnage.getRowSpan(), sectiontonnage.getTextAlign(), sectiontonnage.getVisibility(), false, false, null, false);
                dtd.addTable(activity, remainCane.getTableData(), tblramaincane, remainCane.getRowColSpan(), remainCane.getNoofHeads() != null ? remainCane.getNoofHeads() : 1, remainCane.isFooter(), remainCane.getBoldIndicator(), true, remainCane.getFloatings(), remainCane.isMarathi(), remainCane.getRowSpan(), remainCane.getTextAlign(), remainCane.getVisibility(), false, false, null, false);
                dtd.addTable(activity, villTonnage.getTableData(), tblvilltonnage, villTonnage.getRowColSpan(), villTonnage.getNoofHeads() != null ? villTonnage.getNoofHeads() : 1, villTonnage.isFooter(), villTonnage.getBoldIndicator(), true, villTonnage.getFloatings(), villTonnage.isMarathi(), villTonnage.getRowSpan(), villTonnage.getTextAlign(), villTonnage.getVisibility(), false, false, null, false);

                break;
            case PLANTHARVVILLREPORT:
                CrushingPlantHarvVillResponse crushingPlantHarvVillResponse = (CrushingPlantHarvVillResponse) response.body();
                villTonnage = crushingPlantHarvVillResponse.getVillageTonnage() != null ? crushingPlantHarvVillResponse.getVillageTonnage() : new TableReportBean();
                dtd.addTable(activity, villTonnage.getTableData(), tblvilltonnage, villTonnage.getRowColSpan(), villTonnage.getNoofHeads() != null ? villTonnage.getNoofHeads() : 1, villTonnage.isFooter(), villTonnage.getBoldIndicator(), true, villTonnage.getFloatings(), villTonnage.isMarathi(), villTonnage.getRowSpan(), villTonnage.getTextAlign(), villTonnage.getVisibility(), false, false, null, false);
                break;
        }

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.SECTION) {
            loadReportVillage(selectedItem.getId());
        }

    }
}