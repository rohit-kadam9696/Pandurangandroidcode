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
import com.twd.chitboyapp.spsskl.enums.RawanaEnum;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
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

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RawanaReportActivity extends AppCompatActivity implements RetrofitResponse, DatePickerDialog.OnDateSetListener, SingleReturn, View.OnClickListener {

    Datepicker datepicker;
    RawanaEnum rawanaEnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rawana_report);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        AppCompatEditText edtdate = findViewById(R.id.edtdate);
        AppCompatTextView txtsectionlbl = findViewById(R.id.txtsectionlbl);

        Intent intent = getIntent();
        if (intent.hasExtra("rawana")) {
            rawanaEnum = (RawanaEnum) intent.getSerializableExtra("rawana");
        }
        ConstantFunction cf = new ConstantFunction();
        cf.initDate(edtdate);

        Activity activity = RawanaReportActivity.this;
        SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
        if (rawanaEnum == RawanaEnum.VILLAGE) {
            sspsection.setVisibility(View.VISIBLE);
            txtsectionlbl.setVisibility(View.VISIBLE);

            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.initSingle(sspsection, activity, getResources().getString(R.string.selectsection));

            DBHelper dbHelper = new DBHelper(activity);
            List<KeyPairBoolData> sectionList = dbHelper.getSection(0);
            ssh.singleReturn = this;
            ssh.setSingleItems(sspsection, sectionList, DataSetter.SECTION);
        } else {
            sspsection.setVisibility(View.GONE);
            txtsectionlbl.setVisibility(View.GONE);
        }

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
            String data[] = cf.getSharedPrefValue(RawanaReportActivity.this, new String[]{getResources().getString(R.string.prefprintersetting)}, new String[]{String.format(getResources().getString(R.string.defaultJson), getResources().getString(R.string.myfactory))});

            PDFTablePOJO pdfTablePOJO = new PDFTablePOJO();
            //pdfTablePOJO.setHeadText(getResources().getString(R.string.usrawana));
            pdfTablePOJO.setTbl(findViewById(R.id.tblrawan));
            PDFBody body = pdf.getBodyFromView(RawanaReportActivity.this, false, data[0], pdfTablePOJO);

            PdfCreatorExampleActivity.pdfBody = body;
            Intent intent = new Intent(RawanaReportActivity.this, PdfCreatorExampleActivity.class);
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
        if (requestCaller == RequestEnum.RAWANAREPORT) {
            TableLayout tblrawan = findViewById(R.id.tblrawan);
            TableResponse tableResponse = (TableResponse) response.body();
            TableReportBean tblBean = tableResponse.getTableData();
            DynamicTableData dtd = new DynamicTableData();
            tblrawan.setTag(tblBean.getColWidth());
            dtd.addTable(activity, tblBean.getTableData(), tblrawan, tblBean.getRowColSpan(), tblBean.getNoofHeads() != null ? tblBean.getNoofHeads() : 1, tblBean.isFooter(), tblBean.getBoldIndicator(), false, tblBean.getFloatings(), tblBean.isMarathi(), tblBean.getRowSpan(), tblBean.getTextAlign(), tblBean.getVisibility(), false, false, null, false);
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
            Activity activity = RawanaReportActivity.this;
            AppCompatEditText edtdate = findViewById(R.id.edtdate);
            SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
            String dateval = edtdate.getText().toString();
            String sectionId = null;
            String action = getResources().getString(R.string.actiongutkhadelist);
            if (rawanaEnum == RawanaEnum.VILLAGE) {
                action = getResources().getString(R.string.actionrawanavillreport);
                List<KeyPairBoolData> selsection = sspsection.getSelectedItems();
                if (!selsection.isEmpty()) {
                    sectionId = String.valueOf(selsection.get(0).getId());
                } else {
                    Constant.showToast(getResources().getString(R.string.selectsection), activity, R.drawable.ic_wrong);
                    return;
                }
            } else {
                action = getResources().getString(R.string.actionrawanasectreport);
            }
            ServerValidation sv = new ServerValidation();
            if (sv.checkNull(dateval)) {
                edtdate.setError(getResources().getString(R.string.errorselectdate));
                Constant.showToast(getResources().getString(R.string.errorselectdate), activity, R.drawable.ic_wrong);
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
                job.put("sectionId", sectionId);
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
            reqfarmer.handleRetrofit(call2, RawanaReportActivity.this, RequestEnum.RAWANAREPORT, servlet, activity, versioncode);
        } else if (view.getId() == R.id.btnprev) {
            Intent intent = new Intent(RawanaReportActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }
}