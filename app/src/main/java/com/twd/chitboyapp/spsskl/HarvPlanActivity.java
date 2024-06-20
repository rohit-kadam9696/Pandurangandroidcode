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
import com.twd.chitboyapp.spsskl.constant.MultiSelectHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.MultiReturn;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.HangamMaster;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.MultiSpinnerSearch;
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

public class HarvPlanActivity extends AppCompatActivity implements RetrofitResponse, MultiReturn, DatePickerDialog.OnDateSetListener {

    Long mindate = 0L, maxdate = 0L;
    Datepicker datepicker;
    List<KeyPairBoolData> hangamList;
    String[] year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harv_plan);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        MultiSpinnerSearch msphangam, mspvariety;
        AppCompatEditText edtplantdatrefrom, edtplantdatreto;
        AppCompatButton btnprev, btnsubmit;
        msphangam = findViewById(R.id.msphangam);
        mspvariety = findViewById(R.id.mspvariety);

        edtplantdatrefrom = findViewById(R.id.edtplantdatrefrom);
        edtplantdatreto = findViewById(R.id.edtplantdatreto);

        btnprev = findViewById(R.id.btnprev);
        btnsubmit = findViewById(R.id.btnsubmit);

        Activity activity = HarvPlanActivity.this;
        DBHelper dbHelper = new DBHelper(activity);
        hangamList = dbHelper.getHangam(-1);
        List<KeyPairBoolData> varityList = dbHelper.getVariety();

        MultiSelectHandler msh = new MultiSelectHandler();
        msh.multiReturn = this;
        msh.initMulti(msphangam, true, 0, activity, getResources().getString(R.string.searchhangam), getResources().getString(R.string.selecthangam), null);
        msh.initMulti(mspvariety, true, 0, activity, getResources().getString(R.string.searchvariety), getResources().getString(R.string.selectvariety), null);

        msh.setMultiSpinner(msphangam, new ArrayList<>(), DataSetter.HANGAM);
        msh.setMultiSpinner(mspvariety, varityList, DataSetter.VARIETY);

        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        year = data[2].split("-");

        int hangamSize = hangamList.size();
        int insidecount = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < hangamSize; i++) {
            KeyPairBoolData keyPairBoolData = hangamList.get(i);
            if (keyPairBoolData.getId() == 1) {
                HangamMaster hangamMaster = (HangamMaster) keyPairBoolData.getObject();
                String date = hangamMaster.getDhangamStartDate() + (Integer.parseInt(year[0]) - 1);
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(sdf.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mindate = cal.getTimeInMillis();
                insidecount++;
            } else if (keyPairBoolData.getId() == 4) {
                HangamMaster hangamMaster = (HangamMaster) keyPairBoolData.getObject();
                String date = hangamMaster.getDhangamEndDate() + year[0];
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(sdf.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                maxdate = cal.getTimeInMillis();
                insidecount++;
            }

            if (insidecount >= 2) {
                break;
            }
        }

        edtplantdatrefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(view, activity);

                datepicker = Datepicker.FROM;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance

                String date = edtplantdatrefrom.getText().toString();
                String dateto = edtplantdatreto.getText().toString();
                long maxdateS;
                if (!dateto.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(sdf.parse(dateto));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    maxdateS = calendar.getTimeInMillis();
                } else {
                    maxdateS = maxdate;
                }
                if (date.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(mindate);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    date = sdf.format(calendar.getTime());
                }
                datedata.putString(getResources().getString(R.string.datepara), date);// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.mindate), mindate);// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.maxdate), maxdateS);// put string to pass with a key value
                dateFragment.setArguments(datedata);// Set bundle data to fragment
                dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
            }
        });

        edtplantdatreto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(view, activity);

                datepicker = Datepicker.TO;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance

                String date = edtplantdatreto.getText().toString();
                String datefrom = edtplantdatrefrom.getText().toString();
                long mindateS;
                if (!datefrom.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(sdf.parse(datefrom));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    mindateS = calendar.getTimeInMillis();
                } else {
                    mindateS = mindate;
                }
                if (date.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(mindate);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    date = sdf.format(calendar.getTime());
                }
                datedata.putString(getResources().getString(R.string.datepara), date);// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.mindate), mindateS);// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.maxdate), maxdate);// put string to pass with a key value
                dateFragment.setArguments(datedata);// Set bundle data to fragment
                dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
            }
        });

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatEditText edtplantdatrefrom = findViewById(R.id.edtplantdatrefrom);
                AppCompatEditText edtplantdatreto = findViewById(R.id.edtplantdatreto);
                MultiSpinnerSearch msphangam = findViewById(R.id.msphangam);
                MultiSpinnerSearch mspvariety = findViewById(R.id.mspvariety);

                String plantdatrefrom = edtplantdatrefrom.getText().toString();
                String plantdatreto = edtplantdatreto.getText().toString();

                List<Long> hangamId = msphangam.getSelectedIds();
                List<Long> varietyId = mspvariety.getSelectedIds();
                boolean isValid = true;
                int hangamSize = hangamId.size();
                int varietySize = varietyId.size();

                ServerValidation sv = new ServerValidation();
                if (!sv.checkDateddMMyyyy(plantdatrefrom, "/")) {
                    edtplantdatrefrom.setError(getResources().getString(R.string.errorselectdate));
                    isValid = false;
                }

                if (!sv.checkDateddMMyyyy(plantdatreto, "/")) {
                    edtplantdatreto.setError(getResources().getString(R.string.errorselectdate));
                    isValid = false;
                }

                if (hangamSize == 0) {
                    Constant.showToast(getResources().getString(R.string.errorhangamnotselect), activity, R.drawable.ic_wrong);
                    isValid = false;
                }

                if (varietySize == 0) {
                    Constant.showToast(getResources().getString(R.string.errorvaritynotselect), activity, R.drawable.ic_wrong);
                    isValid = false;
                }

                JSONObject job = new JSONObject();

                if (isValid) {

                    StringBuilder hangamIds = new StringBuilder();
                    StringBuilder varietyIds = new StringBuilder();

                    for (int i = 0; i < hangamSize; i++) {
                        if (i != 0) {
                            hangamIds.append(",");
                        }
                        hangamIds.append(hangamId.get(i));
                    }

                    for (int i = 0; i < varietySize; i++) {
                        if (i != 0) {
                            varietyIds.append(",");
                        }
                        varietyIds.append(varietyId.get(i));
                    }
                    String[] data = cf.getSharedPrefValue(activity, key, value);
                    try {
                        job.put("yearCode", data[2]);
                        job.put("dplantFromDate", plantdatrefrom);
                        job.put("dplantToDate", plantdatreto);
                        job.put("nhangam", hangamIds.toString());
                        job.put("nvariety", varietyIds.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                        return;
                    }
                    String action = getResources().getString(R.string.actionsaveharvplan);
                    String servlet = getResources().getString(R.string.servletcaneharvestingplan);
                    APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                    String versioncode = cf.getVersionCode();
                    Call<ActionResponse> call2 = apiInterface.actionCaneHarvestingPlan(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                    RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
                    reqfarmer.handleRetrofit(call2, HarvPlanActivity.this, RequestEnum.HAVRPLANSAVE, servlet, activity, versioncode);
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
        if (requestCaller == RequestEnum.HAVRPLANSAVE) {
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
    public void onItemsSelected(List<KeyPairBoolData> selectedItems, DataSetter dataSetter) {

    }

    @Override
    public void onOkClickLister(List<KeyPairBoolData> selectedItems, DataSetter dataSetter) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        AppCompatEditText date;
        switch (datepicker) {
            case FROM:
                date = findViewById(R.id.edtplantdatrefrom);
                break;
            case TO:
                date = findViewById(R.id.edtplantdatreto);
                break;
            default:
                return;
        }
        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
        date.setError(null);
        restructureMutli();
    }

    private void restructureMutli() {
        int hangamSize = hangamList.size();
        AppCompatEditText edtplantdatefrom, edtplantdatreto;
        edtplantdatefrom = findViewById(R.id.edtplantdatrefrom);
        edtplantdatreto = findViewById(R.id.edtplantdatreto);

        String fromdate = edtplantdatefrom.getText().toString();
        String todate = edtplantdatreto.getText().toString();

        if (!fromdate.trim().equals("") && !todate.trim().equals("")) {
            ArrayList<KeyPairBoolData> temphangamlist = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calfrom = Calendar.getInstance();
            Calendar calto = Calendar.getInstance();
            try {
                calfrom.setTime(sdf.parse(fromdate));
                calto.setTime(sdf.parse(todate));
            } catch (ParseException e) {
                e.printStackTrace();
                Constant.showToast(e.getMessage(), HarvPlanActivity.this, R.drawable.ic_wrong);
                return;
            }

            for (int i = 0; i < hangamSize; i++) {
                KeyPairBoolData keyPairBoolData = hangamList.get(i);

                HangamMaster hangamMaster = (HangamMaster) keyPairBoolData.getObject();
                String datehs = hangamMaster.getDhangamStartDate() + (Integer.parseInt(year[0]) - 1);
                Calendar calhs = Calendar.getInstance();
                try {
                    calhs.setTime(sdf.parse(datehs));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String datehe = hangamMaster.getDhangamEndDate();
                String[] edms = datehe.split("/");

                if (Integer.parseInt(edms[1]) >= 6) datehe += (Integer.parseInt(year[0]) - 1);
                else datehe += year[0];
                Calendar calhe = Calendar.getInstance();
                try {
                    calhe.setTime(sdf.parse(datehe));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (calfrom.compareTo(calhs) >= 0 && calto.compareTo(calhe) <= 0) {
                    temphangamlist.add(keyPairBoolData);
                }

            }

            MultiSpinnerSearch msphangam = findViewById(R.id.msphangam);
            MultiSelectHandler msh = new MultiSelectHandler();
            msh.multiReturn = this;
            msh.initMulti(msphangam, true, 0, HarvPlanActivity.this, getResources().getString(R.string.searchhangam), getResources().getString(R.string.selecthangam), null);
            msh.setMultiSpinner(msphangam, temphangamlist, DataSetter.HANGAM);
        }
    }
}