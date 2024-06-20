package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.adapter.InwardSlipAdapter;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DatePickerExample;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
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
import com.twd.chitboyapp.spsskl.pojo.CaneDailyInwardReport;
import com.twd.chitboyapp.spsskl.pojo.CaneDailyInwardReportResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CaneInwardDailyActivity extends AppCompatActivity implements SingleReturn, RetrofitResponse, DatePickerDialog.OnDateSetListener {

    Datepicker datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cane_inward_daily);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(CaneInwardDailyActivity.this);
        connectionUpdator.startObserve(CaneInwardDailyActivity.this);

        Activity activity = this;
        SingleSpinnerSearch sspvehicaltype = findViewById(R.id.sspvehicaltype);
        AppCompatEditText edtdate = findViewById(R.id.edtdate);
        DBHelper dbHelper = new DBHelper(activity);
        List<KeyPairBoolData> vehicaltypeList = dbHelper.getVehicleType(DBHelper.vehicleTypeTable, 0);

        SingleSelectHandler selectHandler = new SingleSelectHandler();
        selectHandler.singleReturn = this;
        selectHandler.initSingle(sspvehicaltype, activity, getResources().getString(R.string.vehicaltypesearch));
        selectHandler.setSingleItems(sspvehicaltype, vehicaltypeList, DataSetter.VEHICALTYPE);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        edtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(view, activity);

                datepicker = Datepicker.REPORTDATE;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance
                String date = edtdate.getText().toString();
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
            date = findViewById(R.id.edtdate);
        } else {
            return;
        }
        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
        loadReport();
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.LISTINWARDCANE) {
            CaneDailyInwardReportResponse caneDailyInwardReportResponse = (CaneDailyInwardReportResponse) response.body();
            setRecylerView(caneDailyInwardReportResponse.getList(), activity);
        }
    }

    private void setRecylerView(List<CaneDailyInwardReport> list, Activity activity) {

        InwardSlipAdapter caneSampleListAdapter = new InwardSlipAdapter(list, activity);
        RecyclerView mmlist = findViewById(R.id.mmlist);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(activity, 1);
        mmlist.setLayoutManager(mLayoutManager1);
        mmlist.setItemAnimator(new DefaultItemAnimator());
        mmlist.setAdapter(caneSampleListAdapter);
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.VEHICALTYPE) {
            loadReport();
        }

    }

    private void loadReport() {
        setRecylerView(new ArrayList<>(), CaneInwardDailyActivity.this);
        SingleSpinnerSearch sspvehicaltype = findViewById(R.id.sspvehicaltype);
        AppCompatEditText edtdate = findViewById(R.id.edtdate);

        String date = edtdate.getText().toString();
        ServerValidation sv = new ServerValidation();
        List<Long> vehicaltype = sspvehicaltype.getSelectedIds();
        int size = vehicaltype.size();
        if (sv.checkDateddMMyyyy(date, "/") && size > 0) {
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            ConstantFunction cf = new ConstantFunction();
            Activity activity = CaneInwardDailyActivity.this;
            String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actiondailyinwardreport);

            try {
                job.put("yearCode", sharedPrefval[2]);
                job.put("slipDate", date);
                job.put("vehicleType", String.valueOf(vehicaltype.get(0)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String servlet = activity.getResources().getString(R.string.servletharvdata);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            RetrofitHandler<CaneDailyInwardReportResponse> handler = new RetrofitHandler<>();
            Call<CaneDailyInwardReportResponse> call2 = apiInterface.caneDailyInward(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
            handler.handleRetrofit(call2, CaneInwardDailyActivity.this, RequestEnum.LISTINWARDCANE, servlet, activity, versioncode);

        }

    }
}