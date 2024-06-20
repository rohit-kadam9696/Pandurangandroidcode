package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.filter.InputFilterMinMax;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneTransitResponse;
import com.twd.chitboyapp.spsskl.pojo.VillageResonse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CaneTransitActivity extends AppCompatActivity implements RetrofitResponse, SingleReturn, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cane_transit);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        AppCompatEditText edtlocaltolya = findViewById(R.id.edtlocaltolya);
        AppCompatEditText edtlocalbajat = findViewById(R.id.edtlocalbajat);
        AppCompatEditText edtlocalmachine = findViewById(R.id.edtlocalmachine);
        AppCompatEditText edtlocalbulluckcart = findViewById(R.id.edtlocalbulluckcart);

        AppCompatEditText edtoutsidetolya = findViewById(R.id.edtoutsidetolya);
        AppCompatEditText edtoutsidebajat = findViewById(R.id.edtoutsidebajat);
        AppCompatEditText edtoutsidemachine = findViewById(R.id.edtoutsidemachine);
        AppCompatEditText edtoutsidebulluckcart = findViewById(R.id.edtoutsidebulluckcart);

        AppCompatEditText edtselftolya = findViewById(R.id.edtselftolya);
        AppCompatEditText edtselfbajat = findViewById(R.id.edtselfbajat);


        AppCompatTextView txttotaltolya = findViewById(R.id.txttotaltolya);
        AppCompatTextView txttotalbajat = findViewById(R.id.txttotalbajat);
        AppCompatTextView txttotalmachine = findViewById(R.id.txttotalmachine);
        AppCompatTextView txttotalbulluckcart = findViewById(R.id.txttotalbulluckcart);

        AppCompatTextView txtclosetolya = findViewById(R.id.txtclosetolya);
        AppCompatTextView txtclosebajat = findViewById(R.id.txtclosebajat);

        AppCompatEditText edtbefore4pmtolya = findViewById(R.id.edtbefore4pmtolya);
        AppCompatEditText edtbefore4pmbajat = findViewById(R.id.edtbefore4pmbajat);
        AppCompatEditText edtbefore4pmmachine = findViewById(R.id.edtbefore4pmmachine);

        AppCompatEditText edtafter4pmtolya = findViewById(R.id.edtafter4pmtolya);
        AppCompatEditText edtafter4pmbajat = findViewById(R.id.edtafter4pmbajat);
        AppCompatEditText edtafter4pmmachine = findViewById(R.id.edtafter4pmmachine);

        AppCompatEditText edtextratriptoli = findViewById(R.id.edtextratriptoli);
        AppCompatEditText edtextratripbajat = findViewById(R.id.edtextratripbajat);

        AppCompatTextView txtdateval = findViewById(R.id.txtdateval);

        SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        btnprev.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);

        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.initSingle(sspvillage, CaneTransitActivity.this, getResources().getString(R.string.selectvillage));

        ConstantFunction cf = new ConstantFunction();
        cf.initDate(txtdateval);


        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        /*DBHelper dbHelper = new DBHelper(CaneTransitActivity.this);*/
        List<KeyPairBoolData> villList = new ArrayList<>();/*dbHelper.getVillage();*/
        ssh.singleReturn = this;
        ssh.setSingleItems(sspvillage, villList, DataSetter.VILLAGE);

        loadVillage();

        InputFilterMinMax inputMax10 = new InputFilterMinMax(0, 10);
        InputFilterMinMax inputMax15 = new InputFilterMinMax(0, 15);
        InputFilterMinMax inputMax20 = new InputFilterMinMax(0, 20);
        InputFilterMinMax inputMax25 = new InputFilterMinMax(0, 25);
        InputFilterMinMax inputMax30 = new InputFilterMinMax(0, 30);
        InputFilterMinMax inputMax40 = new InputFilterMinMax(0, 40);
        InputFilterMinMax inputMax100 = new InputFilterMinMax(0, 100);
        InputFilterMinMax inputMax125 = new InputFilterMinMax(0, 125);
        InputFilterMinMax inputMax170 = new InputFilterMinMax(0, 170);
        InputFilterMinMax inputMax500 = new InputFilterMinMax(0, 500);

        edtlocaltolya.setFilters(new InputFilter[]{inputMax15});
        edtlocalbajat.setFilters(new InputFilter[]{inputMax40});
        edtlocalmachine.setFilters(new InputFilter[]{inputMax15});
        edtlocalbulluckcart.setFilters(new InputFilter[]{inputMax500});

        edtoutsidetolya.setFilters(new InputFilter[]{inputMax10});
        edtoutsidebajat.setFilters(new InputFilter[]{inputMax30});
        edtoutsidemachine.setFilters(new InputFilter[]{inputMax10});
        edtoutsidebulluckcart.setFilters(new InputFilter[]{inputMax500});

        edtselftolya.setFilters(new InputFilter[]{inputMax100});
        edtselfbajat.setFilters(new InputFilter[]{inputMax100});

        edtbefore4pmtolya.setFilters(new InputFilter[]{inputMax125});
        edtbefore4pmbajat.setFilters(new InputFilter[]{inputMax170});
        edtbefore4pmmachine.setFilters(new InputFilter[]{inputMax25});

        edtafter4pmtolya.setFilters(new InputFilter[]{inputMax125});
        edtafter4pmbajat.setFilters(new InputFilter[]{inputMax170});
        edtafter4pmmachine.setFilters(new InputFilter[]{inputMax25});

        edtextratriptoli.setFilters(new InputFilter[]{inputMax20});
        edtextratripbajat.setFilters(new InputFilter[]{inputMax30});

        edtlocaltolya.addTextChangedListener(new TextChangeOperation(edtlocaltolya, edtoutsidetolya, edtselftolya, edtbefore4pmtolya, edtafter4pmtolya, txttotaltolya, txtclosetolya, 1));
        edtoutsidetolya.addTextChangedListener(new TextChangeOperation(edtlocaltolya, edtoutsidetolya, edtselftolya, edtbefore4pmtolya, edtafter4pmtolya, txttotaltolya, txtclosetolya, 2));
        edtselftolya.addTextChangedListener(new TextChangeOperation(edtlocaltolya, edtoutsidetolya, edtselftolya, edtbefore4pmtolya, edtafter4pmtolya, txttotaltolya, txtclosetolya, 3));
        edtbefore4pmtolya.addTextChangedListener(new TextChangeOperation(edtlocaltolya, edtoutsidetolya, edtselftolya, edtbefore4pmtolya, edtafter4pmtolya, txttotaltolya, txtclosetolya, 4));
        edtafter4pmtolya.addTextChangedListener(new TextChangeOperation(edtlocaltolya, edtoutsidetolya, edtselftolya, edtbefore4pmtolya, edtafter4pmtolya, txttotaltolya, txtclosetolya, 5));

        edtlocalbajat.addTextChangedListener(new TextChangeOperation(edtlocalbajat, edtoutsidebajat, edtselfbajat, edtbefore4pmbajat, edtafter4pmbajat, txttotalbajat, txtclosebajat, 1));
        edtoutsidebajat.addTextChangedListener(new TextChangeOperation(edtlocalbajat, edtoutsidebajat, edtselfbajat, edtbefore4pmbajat, edtafter4pmbajat, txttotalbajat, txtclosebajat, 2));
        edtselfbajat.addTextChangedListener(new TextChangeOperation(edtlocalbajat, edtoutsidebajat, edtselfbajat, edtbefore4pmbajat, edtafter4pmbajat, txttotalbajat, txtclosebajat, 3));
        edtbefore4pmbajat.addTextChangedListener(new TextChangeOperation(edtlocalbajat, edtoutsidebajat, edtselfbajat, edtbefore4pmbajat, edtafter4pmbajat, txttotalbajat, txtclosebajat, 4));
        edtafter4pmbajat.addTextChangedListener(new TextChangeOperation(edtlocalbajat, edtoutsidebajat, edtselfbajat, edtbefore4pmbajat, edtafter4pmbajat, txttotalbajat, txtclosebajat, 5));

        edtlocalmachine.addTextChangedListener(new TextChangeOperation(edtlocalmachine, edtoutsidemachine, null, edtbefore4pmmachine, edtafter4pmmachine, txttotalmachine, null, 1));
        edtoutsidemachine.addTextChangedListener(new TextChangeOperation(edtlocalmachine, edtoutsidemachine, null, edtbefore4pmmachine, edtafter4pmmachine, txttotalmachine, null, 2));
        edtbefore4pmmachine.addTextChangedListener(new TextChangeOperation(edtlocalmachine, edtoutsidemachine, null, edtbefore4pmmachine, edtafter4pmmachine, txttotalmachine, null, 4));
        edtafter4pmmachine.addTextChangedListener(new TextChangeOperation(edtlocalmachine, edtoutsidemachine, null, edtbefore4pmmachine, edtafter4pmmachine, txttotalmachine, null, 5));

        edtlocalbulluckcart.addTextChangedListener(new TextChangeOperation(edtlocalbulluckcart, edtoutsidebulluckcart, null, null, null, txttotalbulluckcart, null, 1));
        edtoutsidebulluckcart.addTextChangedListener(new TextChangeOperation(edtlocalbulluckcart, edtoutsidebulluckcart, null, null, null, txttotalbulluckcart, null, 2));


    }

    private void loadVillage() {

        Activity activity = CaneTransitActivity.this;
        String action = getResources().getString(R.string.actionloadMyVill);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();

        try {
            job.put("yearId", data[2]);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }
        String servlet = getResources().getString(R.string.servletharvdata);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<VillageResonse> call2 = apiInterface.villList(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<VillageResonse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, CaneTransitActivity.this, RequestEnum.LOADVILLAGE, servlet, activity, versioncode);

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
                clearAll(true, true);
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : activity.getResources().getString(R.string.savesucess), activity, R.drawable.ic_wrong);
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : activity.getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        } else if (requestCaller == RequestEnum.CANETRANSITDTLS) {
            setData((CaneTransitResponse) response.body());
        } else if (requestCaller == RequestEnum.LOADVILLAGE) {
            VillageResonse villageResonse = (VillageResonse) response.body();
            SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.singleReturn = this;
            ssh.setSingleItems(sspvillage, villageResonse.getVillList(), DataSetter.VILLAGE);
        }
    }

    private void setData(CaneTransitResponse caneTransitResponse) {
        if (!caneTransitResponse.getTransEdit().equals("0")) {
            clearAll(false, true);
            AppCompatEditText edtlocaltolya = findViewById(R.id.edtlocaltolya);
            AppCompatEditText edtlocalbajat = findViewById(R.id.edtlocalbajat);
            AppCompatEditText edtlocalmachine = findViewById(R.id.edtlocalmachine);
            AppCompatEditText edtlocalbulluckcart = findViewById(R.id.edtlocalbulluckcart);

            AppCompatEditText edtoutsidetolya = findViewById(R.id.edtoutsidetolya);
            AppCompatEditText edtoutsidebajat = findViewById(R.id.edtoutsidebajat);
            AppCompatEditText edtoutsidemachine = findViewById(R.id.edtoutsidemachine);
            AppCompatEditText edtoutsidebulluckcart = findViewById(R.id.edtoutsidebulluckcart);

            AppCompatEditText edtselftolya = findViewById(R.id.edtselftolya);
            AppCompatEditText edtselfbajat = findViewById(R.id.edtselfbajat);


            AppCompatTextView txttotaltolya = findViewById(R.id.txttotaltolya);
            AppCompatTextView txttotalbajat = findViewById(R.id.txttotalbajat);
            AppCompatTextView txttotalmachine = findViewById(R.id.txttotalmachine);
            AppCompatTextView txttotalbulluckcart = findViewById(R.id.txttotalbulluckcart);

            AppCompatTextView txtclosetolya = findViewById(R.id.txtclosetolya);
            AppCompatTextView txtclosebajat = findViewById(R.id.txtclosebajat);

            AppCompatEditText edtbefore4pmtolya = findViewById(R.id.edtbefore4pmtolya);
            AppCompatEditText edtbefore4pmbajat = findViewById(R.id.edtbefore4pmbajat);
            AppCompatEditText edtbefore4pmmachine = findViewById(R.id.edtbefore4pmmachine);

            AppCompatEditText edtafter4pmtolya = findViewById(R.id.edtafter4pmtolya);
            AppCompatEditText edtafter4pmbajat = findViewById(R.id.edtafter4pmbajat);
            AppCompatEditText edtafter4pmmachine = findViewById(R.id.edtafter4pmmachine);

            AppCompatEditText edtextratriptoli = findViewById(R.id.edtextratriptoli);
            AppCompatEditText edtextratripbajat = findViewById(R.id.edtextratripbajat);

            AppCompatEditText edtsparearea = findViewById(R.id.edtsparearea);
            AppCompatEditText edtsparetonnage = findViewById(R.id.edtsparetonnage);

            AppCompatTextView txttransedit = findViewById(R.id.txttransedit);

            txttransedit.setText(caneTransitResponse.getTransEdit());

            edtlocaltolya.setText(caneTransitResponse.getLocalTolya());
            edtlocalbajat.setText(caneTransitResponse.getLocalBajat());
            edtlocalmachine.setText(caneTransitResponse.getLocalMachine());
            edtlocalbulluckcart.setText(caneTransitResponse.getLocalBulluckCart());

            edtoutsidetolya.setText(caneTransitResponse.getOutsideTolya());
            edtoutsidebajat.setText(caneTransitResponse.getOutsideBajat());
            edtoutsidemachine.setText(caneTransitResponse.getOutsideMachine());
            edtoutsidebulluckcart.setText(caneTransitResponse.getOutsideBulluckCart());

            edtselftolya.setText(caneTransitResponse.getSelfTolya());
            edtselfbajat.setText(caneTransitResponse.getSelfBajat());

            txttotaltolya.setText(caneTransitResponse.getTotalTolya());
            txttotalbajat.setText(caneTransitResponse.getTotalBajat());
            txttotalmachine.setText(caneTransitResponse.getTotalMachine());
            txttotalbulluckcart.setText(caneTransitResponse.getTotalBulluckCart());

            txtclosetolya.setText(caneTransitResponse.getCloseTolya());
            txtclosebajat.setText(caneTransitResponse.getCloseBajat());

            edtbefore4pmtolya.setText(caneTransitResponse.getBefore4pmTolya());
            edtbefore4pmbajat.setText(caneTransitResponse.getBefore4pmBajat());
            edtbefore4pmmachine.setText(caneTransitResponse.getBefore4pmMachine());

            edtafter4pmtolya.setText(caneTransitResponse.getAfter4pmTolya());
            edtafter4pmbajat.setText(caneTransitResponse.getAfter4pmBajat());
            edtafter4pmmachine.setText(caneTransitResponse.getAfter4pmMachine());

            edtextratriptoli.setText(caneTransitResponse.getExtraTripToli());
            edtextratripbajat.setText(caneTransitResponse.getExtraTripBajat());

            edtsparearea.setText(caneTransitResponse.getSpareArea());
            edtsparetonnage.setText(caneTransitResponse.getSpareTonnage());

        } else {
            clearAll(false, false);
        }

    }

    private void clearAll(boolean clearVill, boolean setTag) {
        AppCompatEditText edtlocaltolya = findViewById(R.id.edtlocaltolya);
        AppCompatEditText edtlocalbajat = findViewById(R.id.edtlocalbajat);
        AppCompatEditText edtlocalmachine = findViewById(R.id.edtlocalmachine);
        AppCompatEditText edtlocalbulluckcart = findViewById(R.id.edtlocalbulluckcart);

        AppCompatEditText edtoutsidetolya = findViewById(R.id.edtoutsidetolya);
        AppCompatEditText edtoutsidebajat = findViewById(R.id.edtoutsidebajat);
        AppCompatEditText edtoutsidemachine = findViewById(R.id.edtoutsidemachine);
        AppCompatEditText edtoutsidebulluckcart = findViewById(R.id.edtoutsidebulluckcart);

        AppCompatEditText edtselftolya = findViewById(R.id.edtselftolya);
        AppCompatEditText edtselfbajat = findViewById(R.id.edtselfbajat);


        AppCompatTextView txttotaltolya = findViewById(R.id.txttotaltolya);
        AppCompatTextView txttotalbajat = findViewById(R.id.txttotalbajat);
        AppCompatTextView txttotalmachine = findViewById(R.id.txttotalmachine);

        AppCompatTextView txtclosetolya = findViewById(R.id.txtclosetolya);
        AppCompatTextView txtclosebajat = findViewById(R.id.txtclosebajat);

        AppCompatEditText edtbefore4pmtolya = findViewById(R.id.edtbefore4pmtolya);
        AppCompatEditText edtbefore4pmbajat = findViewById(R.id.edtbefore4pmbajat);
        AppCompatEditText edtbefore4pmmachine = findViewById(R.id.edtbefore4pmmachine);

        AppCompatEditText edtafter4pmtolya = findViewById(R.id.edtafter4pmtolya);
        AppCompatEditText edtafter4pmbajat = findViewById(R.id.edtafter4pmbajat);
        AppCompatEditText edtafter4pmmachine = findViewById(R.id.edtafter4pmmachine);

        AppCompatEditText edtextratriptoli = findViewById(R.id.edtextratriptoli);
        AppCompatEditText edtextratripbajat = findViewById(R.id.edtextratripbajat);

        AppCompatEditText edtsparearea = findViewById(R.id.edtsparearea);
        AppCompatEditText edtsparetonnage = findViewById(R.id.edtsparetonnage);

        if (clearVill) {
            SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);
            sspvillage.clearAll();
        }

        if (setTag) {
            edtlocaltolya.setTag("1");
            edtlocalbajat.setTag("1");
            edtlocalmachine.setTag("1");
            edtlocalbulluckcart.setTag("1");

            edtoutsidetolya.setTag("1");
            edtoutsidebajat.setTag("1");
            edtoutsidemachine.setTag("1");
            edtoutsidebulluckcart.setTag("1");

            edtselftolya.setTag("1");
            edtselfbajat.setTag("1");

            edtbefore4pmtolya.setTag("1");
            edtbefore4pmbajat.setTag("1");
            edtbefore4pmmachine.setTag("1");

            edtafter4pmtolya.setTag("1");
            edtafter4pmbajat.setTag("1");
            edtafter4pmmachine.setTag("1");

            edtextratriptoli.setTag("1");
            edtextratripbajat.setTag("1");

            edtsparearea.setTag("1");
            edtsparetonnage.setTag("1");
        }

        edtlocaltolya.setText("");
        edtlocalbajat.setText("");
        edtlocalmachine.setText("");
        edtlocalbulluckcart.setText("");

        edtoutsidetolya.setText("");
        edtoutsidebajat.setText("");
        edtoutsidemachine.setText("");
        edtoutsidebulluckcart.setText("");

        edtselftolya.setText("");
        edtselfbajat.setText("");

        txttotaltolya.setText("0");
        txttotalbajat.setText("0");
        txttotalmachine.setText("0");

        txtclosetolya.setText("0");
        txtclosebajat.setText("0");

        edtbefore4pmtolya.setText("");
        edtbefore4pmbajat.setText("");
        edtbefore4pmmachine.setText("");

        edtafter4pmtolya.setText("");
        edtafter4pmbajat.setText("");
        edtafter4pmmachine.setText("");

        edtextratriptoli.setText("");
        edtextratripbajat.setText("");

        edtsparearea.setText("");
        edtsparetonnage.setText("");

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.VILLAGE) {
            Activity activity = CaneTransitActivity.this;
            AppCompatTextView txtdateval = findViewById(R.id.txtdateval);
            AppCompatEditText edtsparearea = findViewById(R.id.edtsparearea);
            edtsparearea.requestFocus();
            String dateval = txtdateval.getText().toString();
            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actioncanetransitdtls);
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            try {
                job.put("dateVal", dateval);
                job.put("yearId", data[2]);
                job.put("villageId", String.valueOf(selectedItem.getId()));
            } catch (JSONException e) {
                e.printStackTrace();
                Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                return;
            }

            String servlet = getResources().getString(R.string.servletharvdata);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<CaneTransitResponse> call2 = apiInterface.actionCaneTransit(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<CaneTransitResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, CaneTransitActivity.this, RequestEnum.CANETRANSITDTLS, servlet, activity, versioncode);

        }
    }

    public class TextChangeOperation implements TextWatcher {
        AppCompatEditText edtlocal, edtoutside, edtself, edtbefore4pm, edtafter4pm;
        AppCompatTextView txttotal, txtclose;

        int caller;

        public TextChangeOperation(AppCompatEditText edtlocal, AppCompatEditText edtoutside, AppCompatEditText edtself, AppCompatEditText edtbefore4pm, AppCompatEditText edtafter4pm, AppCompatTextView txttotal, AppCompatTextView txtclose, int caller) {
            this.edtlocal = edtlocal;
            this.edtoutside = edtoutside;
            this.edtself = edtself;
            this.edtbefore4pm = edtbefore4pm;
            this.edtafter4pm = edtafter4pm;
            this.txttotal = txttotal;
            this.txtclose = txtclose;
            this.caller = caller;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String tag = null;
            switch (caller) {
                case 1:
                    if (edtlocal.getTag() != null) tag = (String) edtlocal.getTag();
                    edtlocal.setTag("2");
                    break;
                case 2:
                    if (edtoutside.getTag() != null) tag = (String) edtoutside.getTag();
                    edtoutside.setTag("2");
                    break;
                case 3:
                    if (edtself.getTag() != null) tag = (String) edtself.getTag();
                    edtself.setTag("2");
                    break;
                case 4:
                    if (edtbefore4pm.getTag() != null) tag = (String) edtbefore4pm.getTag();
                    edtbefore4pm.setTag("2");
                    break;
                case 5:
                    if (edtafter4pm.getTag() != null) tag = (String) edtafter4pm.getTag();
                    edtafter4pm.setTag("2");
                    break;
            }
            if (tag == null || !tag.equals("1")) {
                String local = edtlocal.getText().toString();
                String outside = edtoutside.getText().toString();
                String self = "0";
                if (edtself != null)
                    self = edtself.getText().toString();
                String before4pm = "0";
                if (edtbefore4pm != null)
                    before4pm = edtbefore4pm.getText().toString();
                String after4pm = "0";
                if (edtafter4pm != null)
                    after4pm = edtafter4pm.getText().toString();

                Integer localInt = 0, outsideInt = 0, selfInt = 0, before4pmInt = 0, after4pmInt = 0;

                ServerValidation sv = new ServerValidation();
                if (sv.checkNumber(local)) localInt = Integer.parseInt(local);
                if (sv.checkNumber(outside)) outsideInt = Integer.parseInt(outside);
                if (sv.checkNumber(self)) selfInt = Integer.parseInt(self);
                if (sv.checkNumber(before4pm)) before4pmInt = Integer.parseInt(before4pm);
                if (sv.checkNumber(after4pm)) after4pmInt = Integer.parseInt(after4pm);

                int total = localInt + outsideInt + selfInt;
                txttotal.setText(String.valueOf(total));

                if (txtclose != null) {
                    int close = total - before4pmInt - after4pmInt;
                    txtclose.setText(String.valueOf(close));
                    if (close < 0) {
                        Constant.showToast(getResources().getString(R.string.errorMinus) + " 1 ", CaneTransitActivity.this, R.drawable.ic_wrong);
                    }
                    txtclose.setText(String.valueOf(close));
                }
                txttotal.setText(String.valueOf(total));
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnsubmit) {

            AppCompatEditText edtlocaltolya = findViewById(R.id.edtlocaltolya);
            AppCompatEditText edtlocalbajat = findViewById(R.id.edtlocalbajat);
            AppCompatEditText edtlocalmachine = findViewById(R.id.edtlocalmachine);
            AppCompatEditText edtlocalbulluckcart = findViewById(R.id.edtlocalbulluckcart);

            AppCompatEditText edtoutsidetolya = findViewById(R.id.edtoutsidetolya);
            AppCompatEditText edtoutsidebajat = findViewById(R.id.edtoutsidebajat);
            AppCompatEditText edtoutsidemachine = findViewById(R.id.edtoutsidemachine);
            AppCompatEditText edtoutsidebulluckcart = findViewById(R.id.edtoutsidebulluckcart);

            AppCompatEditText edtselftolya = findViewById(R.id.edtselftolya);
            AppCompatEditText edtselfbajat = findViewById(R.id.edtselfbajat);

            AppCompatTextView txttotaltolya = findViewById(R.id.txttotaltolya);
            AppCompatTextView txttotalbajat = findViewById(R.id.txttotalbajat);
            AppCompatTextView txttotalmachine = findViewById(R.id.txttotalmachine);
            AppCompatTextView txttotalbulluckcart = findViewById(R.id.txttotalbulluckcart);

            AppCompatTextView txtclosetolya = findViewById(R.id.txtclosetolya);
            AppCompatTextView txtclosebajat = findViewById(R.id.txtclosebajat);

            AppCompatEditText edtbefore4pmtolya = findViewById(R.id.edtbefore4pmtolya);
            AppCompatEditText edtbefore4pmbajat = findViewById(R.id.edtbefore4pmbajat);
            AppCompatEditText edtbefore4pmmachine = findViewById(R.id.edtbefore4pmmachine);

            AppCompatEditText edtafter4pmtolya = findViewById(R.id.edtafter4pmtolya);
            AppCompatEditText edtafter4pmbajat = findViewById(R.id.edtafter4pmbajat);
            AppCompatEditText edtafter4pmmachine = findViewById(R.id.edtafter4pmmachine);

            AppCompatEditText edtextratriptoli = findViewById(R.id.edtextratriptoli);
            AppCompatEditText edtextratripbajat = findViewById(R.id.edtextratripbajat);

            AppCompatEditText edtsparearea = findViewById(R.id.edtsparearea);
            AppCompatEditText edtsparetonnage = findViewById(R.id.edtsparetonnage);

            AppCompatTextView txttransedit = findViewById(R.id.txttransedit);

            AppCompatTextView txtdateval = findViewById(R.id.txtdateval);
            SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);

            List<KeyPairBoolData> village = sspvillage.getSelectedItems();

            if (village.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorvillagenotselect), CaneTransitActivity.this, R.drawable.ic_wrong);
                return;
            }

            String localTolya = edtlocaltolya.getText().toString();
            String localBajat = edtlocalbajat.getText().toString();
            String localMachine = edtlocalmachine.getText().toString();
            String localBulluckCart = edtlocalbulluckcart.getText().toString();

            String outsideTolya = edtoutsidetolya.getText().toString();
            String outsideBajat = edtoutsidebajat.getText().toString();
            String outsideMachine = edtoutsidemachine.getText().toString();
            String outsideBulluckCart = edtoutsidebulluckcart.getText().toString();

            String selfTolya = edtselftolya.getText().toString();
            String selfBajat = edtselfbajat.getText().toString();

            String totalTolya = txttotaltolya.getText().toString();
            String totalBajat = txttotalbajat.getText().toString();
            String totalMachine = txttotalmachine.getText().toString();
            String totalBulluckCart = txttotalbulluckcart.getText().toString();

            String closeTolya = txtclosetolya.getText().toString();
            String closeBajat = txtclosebajat.getText().toString();

            String before4pmTolya = edtbefore4pmtolya.getText().toString();
            String before4pmBajat = edtbefore4pmbajat.getText().toString();
            String before4pmMachine = edtbefore4pmmachine.getText().toString();

            String after4pmTolya = edtafter4pmtolya.getText().toString();
            String after4pmBajat = edtafter4pmbajat.getText().toString();
            String after4pmMachine = edtafter4pmmachine.getText().toString();

            String extraTripToli = edtextratriptoli.getText().toString();
            String extraTripBajat = edtextratripbajat.getText().toString();

            String spareArea = edtsparearea.getText().toString();
            String spareTonnage = edtsparetonnage.getText().toString();

            ServerValidation sv = new ServerValidation();
            if (!sv.checkNumber(closeTolya)) {
                Constant.showToast(getResources().getString(R.string.errorMinus) + " 2 ", CaneTransitActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (!sv.checkNumber(closeBajat)) {
                Constant.showToast(getResources().getString(R.string.errorMinus) + " 3 ", CaneTransitActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (!sv.checkNumber(totalTolya)) {
                Constant.showToast(getResources().getString(R.string.errorMinusTotal) + " 4 ", CaneTransitActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (!sv.checkNumber(totalBajat)) {
                Constant.showToast(getResources().getString(R.string.errorMinusTotal) + " 5 ", CaneTransitActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (!sv.checkNumber(totalMachine)) {
                Constant.showToast(getResources().getString(R.string.errorMinusTotal) + " 6 ", CaneTransitActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (!sv.checkNumber(totalBulluckCart)) {
                Constant.showToast(getResources().getString(R.string.errorMinusTotal) + " 7 ", CaneTransitActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (!sv.checkNumber(spareTonnage)) {
                Constant.showToast(getResources().getString(R.string.errorMinusTonnage), CaneTransitActivity.this, R.drawable.ic_wrong);
                return;
            }

            int localtolyaInt = checkNumberAndSetError(localTolya, edtlocaltolya, sv);
            if (localtolyaInt < 0) return;
            int localbajatInt = checkNumberAndSetError(localBajat, edtlocalbajat, sv);
            if (localbajatInt < 0) return;
            int localmachineInt = checkNumberAndSetError(localMachine, edtlocalmachine, sv);
            if (localmachineInt < 0) return;
            int localbulluckcartInt = checkNumberAndSetError(localBulluckCart, edtlocalbulluckcart, sv);
            if (localbulluckcartInt < 0) return;

            int outsidetolyaInt = checkNumberAndSetError(outsideTolya, edtoutsidetolya, sv);
            if (outsidetolyaInt < 0) return;
            int outsidebajatInt = checkNumberAndSetError(outsideBajat, edtoutsidebajat, sv);
            if (outsidebajatInt < 0) return;
            int outsidemachineInt = checkNumberAndSetError(outsideMachine, edtoutsidemachine, sv);
            if (outsidemachineInt < 0) return;
            int outsidebulluckcartInt = checkNumberAndSetError(outsideBulluckCart, edtoutsidebulluckcart, sv);
            if (outsidebulluckcartInt < 0) return;

            int selftolyaInt = checkNumberAndSetError(selfTolya, edtselftolya, sv);
            if (selftolyaInt < 0) return;
            int selfbajatInt = checkNumberAndSetError(selfBajat, edtselfbajat, sv);
            if (selfbajatInt < 0) return;

            int before4pmtolyaInt = checkNumberAndSetError(before4pmTolya, edtbefore4pmtolya, sv);
            if (before4pmtolyaInt < 0) return;
            int before4pmbajatInt = checkNumberAndSetError(before4pmBajat, edtbefore4pmbajat, sv);
            if (before4pmbajatInt < 0) return;
            int before4pmmachineInt = checkNumberAndSetError(before4pmMachine, edtbefore4pmmachine, sv);
            if (before4pmmachineInt < 0) return;

            int after4pmtolyaInt = checkNumberAndSetError(after4pmTolya, edtafter4pmtolya, sv);
            if (after4pmtolyaInt < 0) return;
            int after4pmbajatInt = checkNumberAndSetError(after4pmBajat, edtafter4pmbajat, sv);
            if (after4pmbajatInt < 0) return;
            int after4pmmachineInt = checkNumberAndSetError(after4pmMachine, edtafter4pmmachine, sv);
            if (after4pmmachineInt < 0) return;

            int extratriptoliInt = checkNumberAndSetError(extraTripToli, edtextratriptoli, sv);
            if (extratriptoliInt < 0) return;
            int extratripbajatInt = checkNumberAndSetError(extraTripBajat, edtextratripbajat, sv);
            if (extratripbajatInt < 0) return;

            double spareAreaDouble = Double.parseDouble(spareArea);
            if (spareAreaDouble < 0) {
                edtsparetonnage.setError(getResources().getString(R.string.errorMinusTonnage));
                return;
            }
            double sparetonnagedouble = Double.parseDouble(spareTonnage);
            if (sparetonnagedouble < 0) {
                edtsparetonnage.setError(getResources().getString(R.string.errorMinusTonnage));
                return;
            }

            Activity activity = CaneTransitActivity.this;

            String dateval = txtdateval.getText().toString();
            String transedit = txttransedit.getText().toString();
            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actionsavecanetransit);
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            try {
                job.put("transEdit", transedit);
                job.put("dateVal", dateval);
                job.put("yearId", data[2]);
                job.put("villageId", String.valueOf(village.get(0).getId()));

                job.put("localTolya", localTolya);
                job.put("localBajat", localBajat);
                job.put("localMachine", localMachine);
                job.put("localBulluckCart", localBulluckCart);

                job.put("outsideTolya", outsideTolya);
                job.put("outsideBajat", outsideBajat);
                job.put("outsideMachine", outsideMachine);
                job.put("outsideBulluckCart", outsideBulluckCart);

                job.put("selfTolya", selfTolya);
                job.put("selfBajat", selfBajat);

                job.put("totalTolya", totalTolya);
                job.put("totalBajat", totalBajat);
                job.put("totalMachine", totalMachine);
                job.put("totalBulluckCart", totalBulluckCart);

                job.put("closeTolya", closeTolya);
                job.put("closeBajat", closeBajat);

                job.put("before4pmTolya", before4pmTolya);
                job.put("before4pmBajat", before4pmBajat);
                job.put("before4pmMachine", before4pmMachine);

                job.put("after4pmTolya", after4pmTolya);
                job.put("after4pmBajat", after4pmBajat);
                job.put("after4pmMachine", after4pmMachine);

                job.put("extraTripToli", extraTripToli);
                job.put("extraTripBajat", extraTripBajat);

                job.put("spareArea", spareArea);
                job.put("spareTonnage", spareTonnage);

            } catch (JSONException e) {
                e.printStackTrace();
                Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                return;
            }

            String servlet = getResources().getString(R.string.servletharvdata);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<ActionResponse> call2 = apiInterface.actionHarvData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, CaneTransitActivity.this, RequestEnum.SAVEORUPDATE, servlet, activity, versioncode);
        } else if (view.getId() == R.id.btnprev) {
            Intent intent = new Intent(CaneTransitActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private int checkNumberAndSetError(String value, AppCompatEditText cntrl, ServerValidation sv) {
        int intValue = 0;
        if (sv.checkNull(value)) {
            if (!sv.checkNumber(value)) {
                cntrl.setError(getResources().getString(R.string.errorPositiveNumber));
                return -1;
            }
            intValue = Integer.parseInt(value);
            if (intValue < 0) {
                cntrl.setError(getResources().getString(R.string.errorPositiveNumber));
            }
        }
        return intValue;
    }
}