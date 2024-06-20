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

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DatePickerExample;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.DynamicTableData;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.AgriReportReponse;
import com.twd.chitboyapp.spsskl.pojo.TableReportBean;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

public class AgriReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, RetrofitResponse {

    Datepicker datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agri_report);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = this;
        AppCompatEditText edtreportdate = findViewById(R.id.edtreportdate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -4);
        edtreportdate.setText(sdf.format(cal.getTime()));

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

    private void loadReport() {
        AppCompatEditText edtreportdate = findViewById(R.id.edtreportdate);

        String date = edtreportdate.getText().toString();
        ServerValidation sv = new ServerValidation();
        if (sv.checkDateddMMyyyy(date, "/")) {
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            ConstantFunction cf = new ConstantFunction();
            Activity activity = AgriReportActivity.this;
            String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actionagrireport);

            try {
                job.put("date", date);
                job.put("vyearCode", sharedPrefval[2]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String servlet = activity.getResources().getString(R.string.servletmgmt);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            RetrofitHandler<AgriReportReponse> handler = new RetrofitHandler<>();
            Call<AgriReportReponse> call2 = apiInterface.agriReport(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
            handler.handleRetrofit(call2, AgriReportActivity.this, RequestEnum.AGRIREPORT, servlet, activity, versioncode);

        }

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

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.AGRIREPORT) {
            AgriReportReponse agriReportReponse = (AgriReportReponse) response.body();
            TableLayout tblnondSummary = findViewById(R.id.tblnondsummary);
            TableLayout tblsectionnondton = findViewById(R.id.tblsectionnondton);
            TableLayout tblsectionhangamnond = findViewById(R.id.tblsectionhangamnond);
            TableLayout tblsectionVarietyNond = findViewById(R.id.tblsectionvarietynond);
            TableLayout tblhangamVarietyNond = findViewById(R.id.tblhangamvarietynond);

            TableReportBean nondSummary = agriReportReponse.getNondSummary() != null ? agriReportReponse.getNondSummary() : new TableReportBean();
            TableReportBean sectionNondTon = agriReportReponse.getNondAndExpTonnage() != null ? agriReportReponse.getNondAndExpTonnage() : new TableReportBean();
            TableReportBean sectionHangamNond = agriReportReponse.getSectionHangamNond() != null ? agriReportReponse.getSectionHangamNond() : new TableReportBean();
            TableReportBean sectionVarietyNond = agriReportReponse.getSectionVarietyNond() != null ? agriReportReponse.getSectionVarietyNond() : new TableReportBean();
            TableReportBean hangamVarietyNond = agriReportReponse.getHangamVarietyNond() != null ? agriReportReponse.getHangamVarietyNond() : new TableReportBean();

            DynamicTableData dtd = new DynamicTableData();
            dtd.addTable(activity, nondSummary.getTableData(), tblnondSummary, nondSummary.getRowColSpan(), nondSummary.getNoofHeads() != null ? nondSummary.getNoofHeads() : 1, nondSummary.isFooter(), nondSummary.getBoldIndicator(), true, nondSummary.getFloatings(), nondSummary.isMarathi(), nondSummary.getRowSpan(), nondSummary.getTextAlign(), nondSummary.getVisibility(), false, false, null, false);
            dtd.addTable(activity, sectionNondTon.getTableData(), tblsectionnondton, sectionNondTon.getRowColSpan(), sectionNondTon.getNoofHeads() != null ? sectionNondTon.getNoofHeads() : 1, sectionNondTon.isFooter(), sectionNondTon.getBoldIndicator(), true, sectionNondTon.getFloatings(), sectionNondTon.isMarathi(), sectionNondTon.getRowSpan(), sectionNondTon.getTextAlign(), sectionNondTon.getVisibility(), false, false, null, false);
            dtd.addTable(activity, sectionHangamNond.getTableData(), tblsectionhangamnond, sectionHangamNond.getRowColSpan(), sectionHangamNond.getNoofHeads() != null ? sectionHangamNond.getNoofHeads() : 1, sectionHangamNond.isFooter(), sectionHangamNond.getBoldIndicator(), true, sectionHangamNond.getFloatings(), sectionHangamNond.isMarathi(), sectionHangamNond.getRowSpan(), sectionHangamNond.getTextAlign(), sectionHangamNond.getVisibility(), false, false, null, false);
            dtd.addTable(activity, sectionVarietyNond.getTableData(), tblsectionVarietyNond, sectionVarietyNond.getRowColSpan(), sectionVarietyNond.getNoofHeads() != null ? sectionVarietyNond.getNoofHeads() : 1, sectionVarietyNond.isFooter(), sectionVarietyNond.getBoldIndicator(), true, sectionVarietyNond.getFloatings(), sectionVarietyNond.isMarathi(), sectionVarietyNond.getRowSpan(), sectionVarietyNond.getTextAlign(), sectionVarietyNond.getVisibility(), false, false, null, false);
            dtd.addTable(activity, hangamVarietyNond.getTableData(), tblhangamVarietyNond, hangamVarietyNond.getRowColSpan(), hangamVarietyNond.getNoofHeads() != null ? hangamVarietyNond.getNoofHeads() : 1, hangamVarietyNond.isFooter(), hangamVarietyNond.getBoldIndicator(), true, hangamVarietyNond.getFloatings(), hangamVarietyNond.isMarathi(), hangamVarietyNond.getRowSpan(), hangamVarietyNond.getTextAlign(), hangamVarietyNond.getVisibility(), false, false, null, false);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}