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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DatePickerExample;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Response;

public class FarmerBirthdayActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, RetrofitResponse {

    Datepicker datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_birthday);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        String farmercode = getIntent().getStringExtra("farmercode");
        setFarmer(farmercode);

        AppCompatEditText edtbirthdate = findViewById(R.id.edtbirthdate);
        edtbirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(view, FarmerBirthdayActivity.this);

                datepicker = Datepicker.BIRTHDAY;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance
                String date = edtbirthdate.getText().toString();
                Calendar calendar = Calendar.getInstance();
                long maxdate = calendar.getTimeInMillis();
                calendar.add(Calendar.YEAR, -100);
                long mindate = calendar.getTimeInMillis();
                datedata.putString(getResources().getString(R.string.datepara), date);// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.mindate), mindate);// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.maxdate), maxdate);// put string to pass with a key value
                dateFragment.setArguments(datedata);// Set bundle data to fragment
                dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
            }
        });

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        AppCompatButton btnupdate = findViewById(R.id.btnupdate);
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndUpdate();
            }
        });
    }

    private void validateAndUpdate() {

        AppCompatTextView txtfarmercodetxt, txtoldmobilenotxt;
        AppCompatEditText edtbirthdate, edtage;
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        edtbirthdate = findViewById(R.id.edtbirthdate);
        edtage = findViewById(R.id.edtage);

        String nentityUniId = txtfarmercodetxt.getText().toString();
        String dbirthDate = edtbirthdate.getText().toString();
        String age = edtage.getText().toString();

        Activity activity = FarmerBirthdayActivity.this;


        ServerValidation sv = new ServerValidation();
        if (sv.checkNull(nentityUniId)) {
            Constant.showToast(getResources().getString(R.string.errornofarmerfound), activity, R.drawable.ic_wrong);
            return;
        }

        if (sv.checkNull(dbirthDate)) {
            edtbirthdate.setError(getResources().getString(R.string.errorwrongdate));
            return;
        }

        if (!sv.checkNumber(age)) {
            edtbirthdate.setError(getResources().getString(R.string.erroragebetween1to100));
            return;
        }

        Integer iage = Integer.parseInt(age);
        if (iage < 1 || iage > 100) {
            edtbirthdate.setError(getResources().getString(R.string.erroragebetween1to100));
            return;
        }

        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionfarmerupdatedob);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        try {
            job.put("nentityUniId", nentityUniId);
            job.put("dbirthDate", dbirthDate);
            job.put("age", age);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }

        String servlet = getResources().getString(R.string.servletfarmer);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<ActionResponse> call2 = apiInterface.farmerActions(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, FarmerBirthdayActivity.this, RequestEnum.UPDATEFARMERAGE, servlet, activity, versioncode);
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.UPDATEFARMERAGE) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                finish();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }
    }


    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    private void setFarmer(String farmerCodeF) {
        DBHelper dbHelper = new DBHelper(FarmerBirthdayActivity.this);
        EntityMasterDetail entityMasterDetail = dbHelper.getEntityById(farmerCodeF);

        AppCompatTextView txtfarmercodetxt, txtfarmernametxt;
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtfarmernametxt = findViewById(R.id.txtfarmernametxt);

        txtfarmercodetxt.setText(farmerCodeF);
        txtfarmernametxt.setText(entityMasterDetail.getVentityNameLocal());
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
        if (datepicker == Datepicker.BIRTHDAY) {
            date = findViewById(R.id.edtbirthdate);
        } else {
            return;
        }
        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
        date.setError(null);
        calculateAge(year, monthOfYear, dayOfMonth);

    }

    private void calculateAge(int year, int monthOfYear, int dayOfMonth) {
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = Calendar.getInstance();
        int age = 0;
        int factor = 0;
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("M-d-yyyy").parse(monthOfYear - 1 + "-" + dayOfMonth + "-" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal1.setTime(date1);
        if (cal2.get(Calendar.DAY_OF_YEAR) < cal1.get(Calendar.DAY_OF_YEAR)) {
            factor = -1;
        }
        age = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR) + factor;
        AppCompatEditText edtage = findViewById(R.id.edtage);
        edtage.setText(String.valueOf(age));
    }
}