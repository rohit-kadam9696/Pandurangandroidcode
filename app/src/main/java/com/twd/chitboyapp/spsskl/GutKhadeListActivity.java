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
import android.widget.TableRow;
import android.widget.TextView;

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
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.interfaces.TableOperations;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.GutKhadeResponse;
import com.twd.chitboyapp.spsskl.pojo.TableReportBean;
import com.twd.chitboyapp.spsskl.pojo.TableResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GutKhadeListActivity extends AppCompatActivity implements RetrofitResponse, DatePickerDialog.OnDateSetListener, SingleReturn, View.OnClickListener, TableOperations {

    Datepicker datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gut_khade_list);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        AppCompatEditText edtdate = findViewById(R.id.edtdate);

        SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        btnprev.setOnClickListener(this);

        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(this);

        Activity activity = GutKhadeListActivity.this;
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.initSingle(sspsection, activity, getResources().getString(R.string.selectsection));

        DBHelper dbHelper = new DBHelper(activity);
        List<KeyPairBoolData> sectionList = dbHelper.getSection(0);
        ssh.singleReturn = this;
        ssh.setSingleItems(sspsection, sectionList, DataSetter.SECTION);

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
                long mindate = calendar.getTimeInMillis();
                datedata.putString(getResources().getString(R.string.datepara), date);// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.mindate), mindate);// put string to pass with a key value
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
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.GUTKHADELIST) {
            TableLayout tblgutkhade = findViewById(R.id.tblgutkhade);
            TableResponse tableResponse = (TableResponse) response.body();
            TableReportBean gutKhadeList = tableResponse.getTableData();
            DynamicTableData dtd = new DynamicTableData();
            dtd.addTable(activity, gutKhadeList.getTableData(), tblgutkhade, gutKhadeList.getRowColSpan(), gutKhadeList.getNoofHeads() != null ? gutKhadeList.getNoofHeads() : 1, gutKhadeList.isFooter(), gutKhadeList.getBoldIndicator(), false, gutKhadeList.getFloatings(), gutKhadeList.isMarathi(), gutKhadeList.getRowSpan(), gutKhadeList.getTextAlign(), gutKhadeList.getVisibility(), true, true, GutKhadeListActivity.this, false);
        } else if (requestCaller == RequestEnum.EDIT) {
            GutKhadeResponse gutKhadeResponse = (GutKhadeResponse) response.body();
            Intent intent = new Intent(GutKhadeListActivity.this, GutKhadeActivity.class);
            intent.putExtra("data", gutKhadeResponse);
            startActivity(intent);
        } else if (requestCaller == RequestEnum.DELETE) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                if (!actionResponse.getSuccessMsg().isEmpty())
                    Constant.showToast(actionResponse.getSuccessMsg(), GutKhadeListActivity.this, R.drawable.ic_wrong);
                int position = (Integer) obj[0];
                TableLayout tableLayout = (TableLayout) obj[1];
                TableRow tableRow = (TableRow) tableLayout.getChildAt(position);
                TextView textView = (TextView) tableRow.getChildAt(tableRow.getChildCount() - 3);
                textView.setText(actionResponse.getNewStatus());
            } else {
                Constant.showToast(actionResponse.getFailMsg(), GutKhadeListActivity.this, R.drawable.ic_wrong);
            }
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
            Activity activity = GutKhadeListActivity.this;
            AppCompatEditText edtdate = findViewById(R.id.edtdate);
            SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
            String dateval = edtdate.getText().toString();
            String sectionId = null;
            List<KeyPairBoolData> selsection = sspsection.getSelectedItems();
            if (!selsection.isEmpty()) {
                sectionId = String.valueOf(selsection.get(0).getId());
            }
            ServerValidation sv = new ServerValidation();
            if (sv.checkNull(dateval)) {
                edtdate.setError(getResources().getString(R.string.errorselectdate));
                return;
            }
            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actiongutkhadelist);
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

            String servlet = getResources().getString(R.string.servletcaneharvestingplan);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<TableResponse> call2 = apiInterface.tableCaneHarvestingPlan(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<TableResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, GutKhadeListActivity.this, RequestEnum.GUTKHADELIST, servlet, activity, versioncode);
        } else if (view.getId() == R.id.btnprev) {
            Intent intent = new Intent(GutKhadeListActivity.this, GutKhadeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }

    @Override
    public void onEditClick(TableLayout tbl, String transId, int position) {
        Activity activity = GutKhadeListActivity.this;
        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actioneditgutkhade);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        try {
            job.put("yearId", data[2]);
            job.put("transId", transId);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }

        String servlet = getResources().getString(R.string.servletcaneharvestingplan);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<GutKhadeResponse> call2 = apiInterface.editGutKhade(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<GutKhadeResponse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, GutKhadeListActivity.this, RequestEnum.EDIT, servlet, activity, versioncode);
    }

    @Override
    public void onDeleteClick(TableLayout tbl, String transId, int position) {
        Activity activity = GutKhadeListActivity.this;
        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actiondeletegutkhade);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        try {
            job.put("yearId", data[2]);
            job.put("transId", transId);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }

        String servlet = getResources().getString(R.string.servletcaneharvestingplan);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<ActionResponse> call2 = apiInterface.actionCaneHarvestingPlan(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, GutKhadeListActivity.this, RequestEnum.DELETE, servlet, activity, versioncode, position, tbl);
    }
}