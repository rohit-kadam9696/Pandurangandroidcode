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

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DatePickerExample;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneHarvestingPlanStart;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

public class HarvProgramStartActivity extends AppCompatActivity implements RetrofitResponse, DatePickerDialog.OnDateSetListener {

    Datepicker datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harv_program_start);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Activity activity = HarvProgramStartActivity.this;
        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionharvplandata);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        try {
            job.put("yearCode", data[2]);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }

        String servlet = getResources().getString(R.string.servletcaneharvestingplan);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<CaneHarvestingPlanStart> call2 = apiInterface.caneHarvestPlanStart(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<CaneHarvestingPlanStart> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, HarvProgramStartActivity.this, RequestEnum.HAVRPLANSTARTDATA, servlet, activity, versioncode);

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatEditText edtentrydate = findViewById(R.id.edtentrydate);
                AppCompatEditText edtdaypermit = findViewById(R.id.edtdaypermit);
                AppCompatEditText edtlimittonnage = findViewById(R.id.edtlimittonnage);
                AppCompatEditText edtlimittonnageexta = findViewById(R.id.edtlimittonnageexta);

                String entryDate = edtentrydate.getText().toString();
                String permitdays = edtdaypermit.getText().toString();
                String limittonnage = edtlimittonnage.getText().toString();
                String limittonnageexta = edtlimittonnageexta.getText().toString();

                boolean isValid = true;
                ServerValidation sv = new ServerValidation();
                if (!sv.checkDateddMMyyyy(entryDate, "/")) {
                    edtentrydate.setError(getResources().getString(R.string.errorselectdate));
                    isValid = false;
                }

                if (!sv.checkNumber(permitdays)) {
                    edtdaypermit.setError(getResources().getString(R.string.errorpermitday));
                    isValid = false;
                } else {
                    int days = Integer.parseInt(permitdays);
                    if (days < 0 || days > 31) {
                        edtdaypermit.setError(getResources().getString(R.string.errorpermitdaybetween1to31));
                        isValid = false;
                    }
                }

                if (!sv.checkFloat(limittonnage)) {
                    edtlimittonnage.setError(getResources().getString(R.string.errorextraton));
                    isValid = false;
                }

                if (!sv.checkFloat(limittonnageexta)) {
                    edtlimittonnageexta.setError(getResources().getString(R.string.errorextratonreq));
                    isValid = false;
                }

                String[] data = cf.getSharedPrefValue(activity, key, value);
                if (isValid) {
                    try {
                        job.put("yearCode", data[2]);
                        job.put("dentryDate", entryDate);
                        job.put("ndaysPermit", permitdays);
                        job.put("nlimitTonnage", limittonnage);
                        job.put("nlimitTonnageExtra", limittonnageexta);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                        return;
                    }
                    String action = getResources().getString(R.string.actionsaveharvplanstart);

                    APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                    String versioncode = cf.getVersionCode();
                    Call<ActionResponse> call2 = apiInterface.actionCaneHarvestingPlan(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                    RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
                    reqfarmer.handleRetrofit(call2, HarvProgramStartActivity.this, RequestEnum.HAVRPLANSTARTSAVE, servlet, activity, versioncode);
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
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.HAVRPLANSTARTDATA) {
            CaneHarvestingPlanStart caneHarvestingPlanStart = (CaneHarvestingPlanStart) response.body();
            AppCompatEditText edtentrydate = findViewById(R.id.edtentrydate);
            AppCompatEditText edtdaypermit = findViewById(R.id.edtdaypermit);
            AppCompatEditText edtlimittonnage = findViewById(R.id.edtlimittonnage);
            AppCompatEditText edtlimittonnageexta = findViewById(R.id.edtlimittonnageexta);

            if (caneHarvestingPlanStart.getDentryDate() != null) {
                edtentrydate.setText(caneHarvestingPlanStart.getDentryDate());
                edtdaypermit.setText(String.valueOf(caneHarvestingPlanStart.getNdaysPermit()));
                edtlimittonnage.setText(String.valueOf(caneHarvestingPlanStart.getNlimitTonnage()));
                edtlimittonnageexta.setText(String.valueOf(caneHarvestingPlanStart.getNlimitTonnageExtra()));
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            edtentrydate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConstantFunction cf = new ConstantFunction();
                    cf.hideKeyboard(view, activity);

                    datepicker = Datepicker.PLANSTARTDAY;
                    DatePickerExample dateFragment = new DatePickerExample();
                    Bundle datedata = new Bundle();// create bundle instance

                    String date = edtentrydate.getText().toString();
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(sdf.parse(caneHarvestingPlanStart.getDendDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long maxdate = calendar.getTimeInMillis();
                    try {
                        calendar.setTime(sdf.parse(caneHarvestingPlanStart.getDstartDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long mindate = calendar.getTimeInMillis();
                    datedata.putString(getResources().getString(R.string.datepara), date);// put string to pass with a key value
                    datedata.putLong(getResources().getString(R.string.mindate), mindate);// put string to pass with a key value
                    datedata.putLong(getResources().getString(R.string.maxdate), maxdate);// put string to pass with a key value
                    dateFragment.setArguments(datedata);// Set bundle data to fragment
                    dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
                }
            });


        } else if (requestCaller == RequestEnum.HAVRPLANSTARTSAVE) {
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        AppCompatEditText date;
        if (datepicker == Datepicker.PLANSTARTDAY) {
            date = findViewById(R.id.edtentrydate);
        } else {
            return;
        }
        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
        date.setError(null);

    }
}