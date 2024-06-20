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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DatePickerExample;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.DataListResonse;
import com.twd.chitboyapp.spsskl.pojo.SugarInwardResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SugarInwardActivity extends AppCompatActivity implements View.OnClickListener, RetrofitResponse, DatePickerDialog.OnDateSetListener {

    Datepicker datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_inward);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        AppCompatEditText edtinwarddate = findViewById(R.id.edtinwarddate);
        AppCompatEditText edtdodate = findViewById(R.id.edtdodate);

        SingleSpinnerSearch sspdono = findViewById(R.id.sspdono);
        Activity activity = SugarInwardActivity.this;
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.initSingle(sspdono, activity, getResources().getString(R.string.select));
        ssh.setSingleItems(sspdono, new ArrayList<>(), DataSetter.DO);


        AppCompatButton btnprev = findViewById(R.id.btnprev);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);

        btnprev.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        edtinwarddate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(view, getApplicationContext());
                datepicker = Datepicker.INWARD;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance
                datedata.putString(getResources().getString(R.string.datepara), edtinwarddate.getText().toString());// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.maxdate), cal.getTimeInMillis());// put string to pass with a key value
                dateFragment.setArguments(datedata);// Set bundle data to fragment
                dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
            }
        });

        edtdodate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String date = edtinwarddate.getText().toString();
                ServerValidation sv = new ServerValidation();
                ConstantFunction cf = new ConstantFunction();
                if (sv.checkDateddMMyyyy(date, "/")) {
                    cf.hideKeyboard(view, getApplicationContext());
                    datepicker = Datepicker.DODATE;
                    DatePickerExample dateFragment = new DatePickerExample();
                    Bundle datedata = new Bundle();// create bundle instance
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        calendar.setTime(sdf.parse(date));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    datedata.putString(getResources().getString(R.string.datepara), edtdodate.getText().toString());// put string to pass with a key value
                    datedata.putLong(getResources().getString(R.string.maxdate), calendar.getTimeInMillis());// put string to pass with a key value
                    dateFragment.setArguments(datedata);// Set bundle data to fragment
                    dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
                } else {
                    Constant.showToast(getResources().getString(R.string.pleaseselectinwarddatefirst), SugarInwardActivity.this, R.drawable.ic_wrong);
                }
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
    public void onClick(View v) {
        if (v.getId() == R.id.btnprev) {
            onBackPressed();
        } else if (v.getId() == R.id.btnsubmit) {
            String action = getResources().getString(R.string.actionsugarfarmerinward);
            Activity activity = SugarInwardActivity.this;
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            AppCompatEditText edtinwarddate = findViewById(R.id.edtinwarddate);
            AppCompatEditText edtdodate = findViewById(R.id.edtdodate);
            SingleSpinnerSearch sspdono = findViewById(R.id.sspdono);
            List<KeyPairBoolData> seldono = sspdono.getSelectedItems();


            String inwarddate = edtinwarddate.getText().toString();
            String dodate = edtdodate.getText().toString();

            ServerValidation sv = new ServerValidation();
            if (!sv.checkDateddMMyyyy(inwarddate, "/")) {
                edtinwarddate.setError(getResources().getString(R.string.errorselectinwarddate));
                return;
            }

            if (!sv.checkDateddMMyyyy(dodate, "/")) {
                edtdodate.setError(getResources().getString(R.string.errorselectdodate));
                return;
            }

            if (seldono.size() <= 0) {
                edtdodate.setError(getResources().getString(R.string.errorenterdono));
                return;
            }

            String dono = String.valueOf(seldono.get(0).getId());

            JSONObject job = new JSONObject();
            try {
                job.put("avakDate", inwarddate);
                job.put("dinvoice_date", dodate);
                job.put("ndo_id", dono);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String servlet = getResources().getString(R.string.servletsugarsale);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<SugarInwardResponse> call2 = apiInterface.getInwardDetails(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<SugarInwardResponse> handleRetrofit = new RetrofitHandler<>();
            handleRetrofit.handleRetrofit(call2, SugarInwardActivity.this, RequestEnum.SUGARFARMERDETAILS, servlet, activity, versioncode);
        }
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.SUGARFARMERDETAILS) {
            SugarInwardResponse sugarInwardResponse = (SugarInwardResponse) response.body();
            Intent intent = new Intent(SugarInwardActivity.this, SugarInwardDetailsActivity.class);
            intent.putExtra("sugarinward", sugarInwardResponse);
            startActivity(intent);
        } else if (requestCaller == RequestEnum.LOADDO) {
            SingleSpinnerSearch sspdono = findViewById(R.id.sspdono);
            DataListResonse dataListResonse = (DataListResonse) response.body();
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.setSingleItems(sspdono, dataListResonse.getDataList(), DataSetter.DO);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        boolean loadDo = false;
        AppCompatEditText date;
        if (datepicker == Datepicker.INWARD) {
            date = findViewById(R.id.edtinwarddate);
        } else if (datepicker == Datepicker.DODATE) {
            date = findViewById(R.id.edtdodate);
            loadDo = true;
        } else {
            return;
        }
        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
        if (loadDo)
            loadDo();
    }

    private void loadDo() {
        String action = getResources().getString(R.string.actionloaddo);
        AppCompatEditText edtdodate = findViewById(R.id.edtdodate);
        String dodate = edtdodate.getText().toString();
        ServerValidation sv = new ServerValidation();

        Activity activity = SugarInwardActivity.this;
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        if (!sv.checkDateddMMyyyy(dodate, "/")) {
            edtdodate.setError(getResources().getString(R.string.errorselectdodate));
            return;
        }

        JSONObject job = new JSONObject();
        try {
            job.put("dinvoice_date", dodate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String servlet = getResources().getString(R.string.servletsugarsale);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<DataListResonse> call2 = apiInterface.listSugarSale(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<DataListResonse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, SugarInwardActivity.this, RequestEnum.LOADDO, servlet, activity, versioncode);
    }
}