package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

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
import com.twd.chitboyapp.spsskl.constant.MultiSelectHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.constant.TimePickerFragment;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.MultiReturn;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.GutKhadeResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.MultiSpinnerSearch;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GutKhadeActivity extends AppCompatActivity implements RetrofitResponse, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, SingleReturn, MultiReturn, View.OnClickListener {

    Datepicker datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gut_khade);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        AppCompatEditText edtfromdate = findViewById(R.id.edtfromdate);
        AppCompatEditText edttodate = findViewById(R.id.edttodate);

        SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
        MultiSpinnerSearch mspvehicletype = findViewById(R.id.mspvehicletype);

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        btnprev.setOnClickListener(this);

        AppCompatButton btnlist = findViewById(R.id.btnlist);
        btnlist.setOnClickListener(this);

        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(this);

        Activity activity = GutKhadeActivity.this;
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.initSingle(sspsection, activity, getResources().getString(R.string.selectsection));

        MultiSelectHandler msh = new MultiSelectHandler();
        msh.multiReturn = this;

        DBHelper dbHelper = new DBHelper(activity);
        Intent intent = getIntent();
        List<KeyPairBoolData> sectionList = null;
        List<KeyPairBoolData> vehicleList = null;
        if (intent.hasExtra("data")) {
            GutKhadeResponse gutKhadeResponse = (GutKhadeResponse) intent.getSerializableExtra("data");
            setData(gutKhadeResponse);
            sectionList = dbHelper.getSection(Integer.parseInt(gutKhadeResponse.getSectionId()));
            vehicleList = dbHelper.getVehicleType(DBHelper.vehicleTypeTable, Integer.parseInt(gutKhadeResponse.getVehicleTypeId()));
            msh.initMulti(mspvehicletype, false, 1, activity, getResources().getString(R.string.searchsection), getResources().getString(R.string.select), null);
        } else {
            sectionList = dbHelper.getSection(0);
            vehicleList = dbHelper.getVehicleType(DBHelper.vehicleTypeTable, 0);
            msh.initMulti(mspvehicletype, true, 0, activity, getResources().getString(R.string.searchsection), getResources().getString(R.string.select), null);
        }

        ssh.singleReturn = this;
        ssh.setSingleItems(sspsection, sectionList, DataSetter.SECTION);

        msh.setMultiSpinner(mspvehicletype, vehicleList, DataSetter.VEHICALTYPE);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        edtfromdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(view, getApplicationContext());
                datepicker = Datepicker.FROM;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance
                datedata.putString(getResources().getString(R.string.datepara), edtfromdate.getText().toString());// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.mindate), cal.getTimeInMillis());// put string to pass with a key value
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
                    Constant.showToast(getResources().getString(R.string.pleaseselectfromdate), GutKhadeActivity.this, R.drawable.ic_wrong);
                    return;
                }
                dateFragment.setArguments(datedata);// Set bundle data to fragment
                dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
            }
        });
    }

    private void setData(GutKhadeResponse gutKhadeResponse) {
        AppCompatTextView txttransid = findViewById(R.id.txttransid);
        AppCompatEditText edtfromdate = findViewById(R.id.edtfromdate);
        AppCompatEditText edttodate = findViewById(R.id.edttodate);

        txttransid.setText(gutKhadeResponse.getTransId());
        edtfromdate.setText(gutKhadeResponse.getFromDate());
        edttodate.setText(gutKhadeResponse.getToDate());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        AppCompatEditText date;
        if (datepicker == Datepicker.FROM) date = findViewById(R.id.edtfromdate);
        else if (datepicker == Datepicker.TO) date = findViewById(R.id.edttodate);
        else return;

        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
        cf.updateDateTime(date, "00:00");

        TimePickerFragment timePickerDialog = new TimePickerFragment();
        Bundle timebundle = new Bundle();// create bundle instance
        timebundle.putString(getResources().getString(R.string.timepara), null);// put string to pass with a key value
        timePickerDialog.setArguments(timebundle);// Set bundle data to fragment
        timePickerDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        AppCompatEditText date;
        AppCompatEditText date2 = null;
        if (datepicker == Datepicker.FROM) date = findViewById(R.id.edtfromdate);
        else if (datepicker == Datepicker.TO) {
            date2 = findViewById(R.id.edtfromdate);
            date = findViewById(R.id.edttodate);
        } else return;


        date.setError(null);
        ConstantFunction cf = new ConstantFunction();
        DecimalFormat df = new DecimalFormat("00");
        cf.updateDateTime(date, df.format(hourOfDay) + ":" + df.format(minute));
        if (date2 != null) {
            String datechoose = date.getText().toString();
            String datevalidation = date2.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                Date datec = sdf.parse(datechoose);
                Date datev = sdf.parse(datevalidation);
                assert datev != null;
                if (datev.after(datec)) {
                    date.setText("");
                    date.setError(getResources().getString(R.string.dateerrorafterdate1));
                    Constant.showToast(getResources().getString(R.string.dateerrorafterdate1), GutKhadeActivity.this, R.drawable.ic_wrong);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            String datechoose = date.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                Date datec = sdf.parse(datechoose);
                Date datecuurent = new Date();
                if (datecuurent.after(datec)) {
                    date.setText("");
                    date.setError(getResources().getString(R.string.dateerrorafterdate));
                    Constant.showToast(getResources().getString(R.string.dateerrorafterdate), GutKhadeActivity.this, R.drawable.ic_wrong);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnsubmit) {

            AppCompatTextView txttransid = findViewById(R.id.txttransid);
            AppCompatEditText edtfromdate = findViewById(R.id.edtfromdate);
            AppCompatEditText edttodate = findViewById(R.id.edttodate);

            String transId = txttransid.getText().toString();
            String fromdate = edtfromdate.getText().toString();
            String todate = edttodate.getText().toString();

            ServerValidation sv = new ServerValidation();
            if (sv.checkNull(fromdate)) {
                Constant.showToast(getResources().getString(R.string.errorselectdate), GutKhadeActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (sv.checkNull(todate)) {
                Constant.showToast(getResources().getString(R.string.errorselectdate), GutKhadeActivity.this, R.drawable.ic_wrong);
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                Date datet = sdf.parse(todate);
                Date datef = sdf.parse(fromdate);
                assert datef != null;
                if (datef.after(datet)) {
                    edttodate.setError(getResources().getString(R.string.dateerrorafterdate1));
                    Constant.showToast(getResources().getString(R.string.dateerrorafterdate1), GutKhadeActivity.this, R.drawable.ic_wrong);
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Constant.showToast(getResources().getString(R.string.dateerrorafterdate1), GutKhadeActivity.this, R.drawable.ic_wrong);
                return;
            }

            Activity activity = GutKhadeActivity.this;

            MultiSpinnerSearch mspvehicletype = findViewById(R.id.mspvehicletype);
            SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);

            List<KeyPairBoolData> selVehicleType = mspvehicletype.getSelectedItems();
            List<KeyPairBoolData> selsection = sspsection.getSelectedItems();

            if (selVehicleType.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorinvalidvehicaltype), GutKhadeActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (selsection.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectsection), GutKhadeActivity.this, R.drawable.ic_wrong);
                return;
            }

            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actionsavegutkhade);
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);
            StringBuilder vehicleTypeId = new StringBuilder();
            int size = selVehicleType.size();
            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    vehicleTypeId.append(",");
                }
                vehicleTypeId.append(selVehicleType.get(i).getId());
            }
            try {
                if (!transId.isEmpty() && !transId.equals("0")) job.put("transId", transId);
                job.put("fromDate", fromdate);
                job.put("toDate", todate);
                job.put("yearId", data[2]);
                job.put("vehicleTypeId", vehicleTypeId.toString());
                job.put("sectionId", String.valueOf(selsection.get(0).getId()));

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
            reqfarmer.handleRetrofit(call2, GutKhadeActivity.this, RequestEnum.SAVEORUPDATE, servlet, activity, versioncode);
        } else if (view.getId() == R.id.btnlist) {
            Intent intent = new Intent(GutKhadeActivity.this, GutKhadeListActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btnprev) {
            Intent intent = new Intent(GutKhadeActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        AppCompatButton btnprev = findViewById(R.id.btnprev);
        btnprev.performClick();
    }

    @Override
    public void onItemsSelected(List<KeyPairBoolData> selectedItems, DataSetter dataSetter) {

    }

    @Override
    public void onOkClickLister(List<KeyPairBoolData> selectedItems, DataSetter dataSetter) {

    }
}