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
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DatePickerExample;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.DynamicTableData;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PDFOperations;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.PDFTablePOJO;
import com.twd.chitboyapp.spsskl.pojo.TableReportBean;
import com.twd.chitboyapp.spsskl.pojo.TableResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.pdfcreator.views.PDFBody;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

public class SugarSaleSummaryActivity extends AppCompatActivity implements View.OnClickListener, RetrofitResponse, DatePickerDialog.OnDateSetListener {

    Datepicker datepicker;
    String mnu_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_sale_summary);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        AppCompatEditText edtdate = findViewById(R.id.edtdate);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(this);

        Intent intent = getIntent();
        mnu_id = intent.getStringExtra("mnu_id");

        ConstantFunction cf = new ConstantFunction();
        cf.initDate(edtdate);
        Calendar cal = Calendar.getInstance();
        edtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cf.hideKeyboard(view, getApplicationContext());
                datepicker = Datepicker.REPORTDATE;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance
                datedata.putString(getResources().getString(R.string.datepara), edtdate.getText().toString());// put string to pass with a key value
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
        if (item.getItemId() == R.id.mnupdf) {
            PDFOperations pdf = new PDFOperations();
            //String header = pdf.getHederFromView(RawanaReportActivity.this, null);

            ConstantFunction cf = new ConstantFunction();
            String data[] = cf.getSharedPrefValue(SugarSaleSummaryActivity.this, new String[]{getResources().getString(R.string.prefprintersetting)}, new String[]{String.format(getResources().getString(R.string.defaultJson), getResources().getString(R.string.myfactory))});

            AppCompatEditText edtdate = findViewById(R.id.edtdate);
            PDFTablePOJO pdfTablePOJO = new PDFTablePOJO();
            pdfTablePOJO.setHeadText(getResources().getString(R.string.datedinak) + " - " + edtdate.getText().toString());
            pdfTablePOJO.setTbl(findViewById(R.id.tbldata));
            PDFBody body = pdf.getBodyFromView(SugarSaleSummaryActivity.this, false, data[0], pdfTablePOJO);

            PdfCreatorExampleActivity.pdfBody = body;
            Intent intent = new Intent(SugarSaleSummaryActivity.this, PdfCreatorExampleActivity.class);
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
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        AppCompatEditText date;
        if (datepicker == Datepicker.REPORTDATE) {
            date = findViewById(R.id.edtdate);
        } else {
            return;
        }
        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnsubmit) {
            ServerValidation sv = new ServerValidation();
            AppCompatEditText edtdate = findViewById(R.id.edtdate);
            String date = edtdate.getText().toString();
            if (!sv.checkDateddMMyyyy(date, "/")) {
                edtdate.setError(getResources().getString(R.string.errorselectdate));
                return;
            }

            String action = getResources().getString(R.string.actionsugarsummaryreport);
            Activity activity = SugarSaleSummaryActivity.this;
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            JSONObject job = new JSONObject();
            try {
                job.put("rdate", date);
                job.put("mnu_id", mnu_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String servlet = getResources().getString(R.string.servletsugarsale);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<TableResponse> call2 = apiInterface.tableSugarSale(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<TableResponse> handleRetrofit = new RetrofitHandler<>();
            handleRetrofit.handleRetrofit(call2, SugarSaleSummaryActivity.this, RequestEnum.SUGARSUMMARYREPORT, servlet, activity, versioncode);
        }
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        TableLayout tbldata = findViewById(R.id.tbldata);
        TableResponse tableResponse = (TableResponse) response.body();
        TableReportBean tableReportBean = tableResponse.getTableData();
        tbldata.setTag(tableReportBean.getColWidth());
        DynamicTableData dtd = new DynamicTableData();
        dtd.addTable(activity, tableReportBean.getTableData(), tbldata, tableReportBean.getRowColSpan(), tableReportBean.getNoofHeads() != null ? tableReportBean.getNoofHeads() : 1, tableReportBean.isFooter(), tableReportBean.getBoldIndicator(), false, tableReportBean.getFloatings(), tableReportBean.isMarathi(), tableReportBean.getRowSpan(), tableReportBean.getTextAlign(), tableReportBean.getVisibility(), false, false, null, false);
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}