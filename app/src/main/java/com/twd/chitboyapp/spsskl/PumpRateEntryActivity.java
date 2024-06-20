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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DatePickerExample;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.constant.TimePickerFragment;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.filter.InputFilterMinMax;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.Pump;
import com.twd.chitboyapp.spsskl.pojo.PumpRateCheckerResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.MultiSpinnerSearch;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PumpRateEntryActivity extends AppCompatActivity implements RetrofitResponse, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    Datepicker datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pump_rate_entry);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        AppCompatEditText edtfromdate = findViewById(R.id.edtfromdate);
        AppCompatEditText edttodate = findViewById(R.id.edttodate);
        AppCompatEditText edtrate = findViewById(R.id.edtrate);

        InputFilterMinMax filter = new InputFilterMinMax(1, 200, 2);
        edtrate.setFilters(new InputFilterMinMax[]{filter});

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        btnprev.setOnClickListener(this);

        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(this);

        Activity activity = PumpRateEntryActivity.this;
        ConstantFunction cf = new ConstantFunction();
        DBHelper dbHelper = new DBHelper(activity);
        String[] key = new String[]{getResources().getString(R.string.prefpumpid)};
        String[] value = new String[]{""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        Pump pump = dbHelper.getPumpById(data[0]);

        AppCompatTextView txtproductname = findViewById(R.id.txtproductname);
        AppCompatTextView txtproductid = findViewById(R.id.txtproductid);

        txtproductname.setText(pump.getVitemName());
        txtproductid.setText(String.valueOf(pump.getNitemId()));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        cf.initDate(edtfromdate, edttodate);
        checkIfRateExist();
        edtfromdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(view, getApplicationContext());
                datepicker = Datepicker.FROM;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance
                datedata.putString(getResources().getString(R.string.datepara), edtfromdate.getText().toString());// put string to pass with a key value
                //datedata.putLong(getResources().getString(R.string.mindate), cal.getTimeInMillis());// put string to pass with a key value
                String dateto = edttodate.getText().toString();
                if (!dateto.trim().equals("")) {
                    Calendar calfrom = Calendar.getInstance();
                    try {
                        calfrom.setTime(sdf.parse(dateto));
                        datedata.putLong(getResources().getString(R.string.maxdate), calfrom.getTimeInMillis());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                dateFragment.setArguments(datedata);// Set bundle data to fragment
                dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));

            }
        });

        edttodate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(view, getApplicationContext());
                datepicker = Datepicker.TO;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance
                datedata.putString(getResources().getString(R.string.datepara), edttodate.getText().toString());// put string to pass with a key value
                String datefrom = edtfromdate.getText().toString();
                if (!datefrom.trim().equals("")) {
                    Calendar calfrom = Calendar.getInstance();
                    try {
                        calfrom.setTime(sdf.parse(datefrom));
                        datedata.putLong(getResources().getString(R.string.mindate), calfrom.getTimeInMillis());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Constant.showToast(getResources().getString(R.string.pleaseselectfromdate), PumpRateEntryActivity.this, R.drawable.ic_wrong);
                    return;
                }
                dateFragment.setArguments(datedata);// Set bundle data to fragment
                dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        AppCompatEditText date;
        if (datepicker == Datepicker.FROM) date = findViewById(R.id.edtfromdate);
        else if (datepicker == Datepicker.TO) date = findViewById(R.id.edttodate);
        else return;

        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
        checkIfRateExist();

    }

    private void checkIfRateExist() {
        AppCompatEditText edtfromdate = findViewById(R.id.edtfromdate);
        AppCompatEditText edttodate = findViewById(R.id.edttodate);
        AppCompatTextView txtproductid = findViewById(R.id.txtproductid);

        String productId = txtproductid.getText().toString();
        String fromdate = edtfromdate.getText().toString();
        String todate = edttodate.getText().toString();

        ServerValidation sv = new ServerValidation();
        if (sv.checkNull(fromdate)) {
            Constant.showToast(getResources().getString(R.string.errorselectdate), PumpRateEntryActivity.this, R.drawable.ic_wrong);
            return;
        }

        if (sv.checkNull(todate)) {
            Constant.showToast(getResources().getString(R.string.errorselectdate), PumpRateEntryActivity.this, R.drawable.ic_wrong);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date datet = sdf.parse(todate);
            Date datef = sdf.parse(fromdate);
            assert datef != null;
            if (datef.after(datet)) {
                edttodate.setError(getResources().getString(R.string.dateerrorafterdate1));
                Constant.showToast(getResources().getString(R.string.dateerrorafterdate1), PumpRateEntryActivity.this, R.drawable.ic_wrong);
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Constant.showToast(getResources().getString(R.string.dateerrorafterdate1), PumpRateEntryActivity.this, R.drawable.ic_wrong);
            return;
        }


        Activity activity = PumpRateEntryActivity.this;


        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actioncheckrateentry);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        try {
            job.put("fromDate", fromdate);
            job.put("toDate", todate);
            job.put("itemId", productId);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }

        String servlet = getResources().getString(R.string.servletpump);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<PumpRateCheckerResponse> call2 = apiInterface.pumpRateCheckerResponse(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<PumpRateCheckerResponse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, PumpRateEntryActivity.this, RequestEnum.ENTRYCHECK, servlet, activity, versioncode);
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
        if (requestCaller == RequestEnum.SAVEORUPDATE) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : activity.getResources().getString(R.string.savesucess), activity, R.drawable.ic_wrong);
                AppCompatButton btnprev = findViewById(R.id.btnprev);
                btnprev.performClick();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : activity.getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        } else if (requestCaller == RequestEnum.ENTRYCHECK) {
            PumpRateCheckerResponse pumpRateCheckerResponse = (PumpRateCheckerResponse) response.body();
            if (pumpRateCheckerResponse.isActionComplete()) {
                AppCompatTextView txttransid = findViewById(R.id.txttransid);
                AppCompatEditText edtrate = findViewById(R.id.edtrate);
                txttransid.setText(pumpRateCheckerResponse.getTransId());
                edtrate.setText(pumpRateCheckerResponse.getPumpRate());
            } else {
                AppCompatTextView txttransid = findViewById(R.id.txttransid);
                AppCompatEditText edtrate = findViewById(R.id.edtrate);
                txttransid.setText("");
                edtrate.setText("");
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnsubmit) {

            AppCompatTextView txttransid = findViewById(R.id.txttransid);
            AppCompatEditText edtfromdate = findViewById(R.id.edtfromdate);
            AppCompatEditText edttodate = findViewById(R.id.edttodate);
            AppCompatTextView txtproductid = findViewById(R.id.txtproductid);
            AppCompatEditText edtrate = findViewById(R.id.edtrate);

            String productId = txtproductid.getText().toString();
            String transId = txttransid.getText().toString();
            String fromdate = edtfromdate.getText().toString();
            String todate = edttodate.getText().toString();
            String rate = edtrate.getText().toString();

            ServerValidation sv = new ServerValidation();
            if (sv.checkNull(fromdate)) {
                Constant.showToast(getResources().getString(R.string.errorselectdate), PumpRateEntryActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (sv.checkNull(todate)) {
                Constant.showToast(getResources().getString(R.string.errorselectdate), PumpRateEntryActivity.this, R.drawable.ic_wrong);
                return;
            }


            if (!sv.checkFloat(rate)) {
                Constant.showToast(getResources().getString(R.string.errorenterrate), PumpRateEntryActivity.this, R.drawable.ic_wrong);
                return;
            } else {
                Double d = Double.parseDouble(rate);
                if (d <= 50) {
                    Constant.showToast(getResources().getString(R.string.errorenterrate), PumpRateEntryActivity.this, R.drawable.ic_wrong);
                    return;
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date datet = sdf.parse(todate);
                Date datef = sdf.parse(fromdate);
                assert datef != null;
                if (datef.after(datet)) {
                    edttodate.setError(getResources().getString(R.string.dateerrorafterdate1));
                    Constant.showToast(getResources().getString(R.string.dateerrorafterdate1), PumpRateEntryActivity.this, R.drawable.ic_wrong);
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Constant.showToast(getResources().getString(R.string.dateerrorafterdate1), PumpRateEntryActivity.this, R.drawable.ic_wrong);
                return;
            }

            Activity activity = PumpRateEntryActivity.this;


            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actionsaveorupdatepumprate);
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            try {
                if (!transId.isEmpty() && !transId.equals("0")) job.put("transId", transId);
                job.put("fromDate", fromdate);
                job.put("toDate", todate);
                job.put("itemId", productId);
                job.put("rate", rate);

            } catch (JSONException e) {
                e.printStackTrace();
                Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                return;
            }

            String servlet = getResources().getString(R.string.servletpump);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<ActionResponse> call2 = apiInterface.pumpActionResponse(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, PumpRateEntryActivity.this, RequestEnum.SAVEORUPDATE, servlet, activity, versioncode);
        } else if (view.getId() == R.id.btnlist) {
            Intent intent = new Intent(PumpRateEntryActivity.this, GutKhadeListActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btnprev) {
            Intent intent = new Intent(PumpRateEntryActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppCompatButton btnprev = findViewById(R.id.btnprev);
        btnprev.performClick();
    }


}