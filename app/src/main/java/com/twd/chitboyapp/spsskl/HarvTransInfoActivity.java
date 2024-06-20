package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.gson.Gson;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.MultiSelectHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.MultiReturn;
import com.twd.chitboyapp.spsskl.interfaces.RefreshComplete;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.BulluckCartResponse;
import com.twd.chitboyapp.spsskl.pojo.HarvSlipDetailsResponse;
import com.twd.chitboyapp.spsskl.pojo.HarvestorResponse;
import com.twd.chitboyapp.spsskl.pojo.SlipBeanR;
import com.twd.chitboyapp.spsskl.pojo.TransporterResponse;
import com.twd.chitboyapp.spsskl.pojo.WeightSlipResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.MultiSpinnerSearch;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HarvTransInfoActivity extends AppCompatActivity implements SingleReturn, MultiReturn, RetrofitResponse, View.OnClickListener, RefreshComplete {

    HarvSlipDetailsResponse harvSlipDetailsResponse;
    HashMap<String, String> combos = null;
    String tokenno = null;
    int ngps_distance;
    int distance;

    ArrayList<KeyPairBoolData> frontwirerope = null;
    ArrayList<KeyPairBoolData> backwirerope = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harv_trans_info);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        hideShow(0);
        Intent intent = getIntent();
        if (intent.hasExtra("harvdetails")) {
            harvSlipDetailsResponse = (HarvSlipDetailsResponse) intent.getSerializableExtra("harvdetails");
        }

        initAllData();

        performClicks();

    }

    private void generateToken() {
        ConstantFunction cf = new ConstantFunction();
        String[] keyin = new String[]{getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] valuein = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(this, keyin, valuein);
        tokenno = MarathiHelper.convertMarathitoEnglish(System.currentTimeMillis() + "@" + data[0]);
    }

    private void performClicks() {
        AppCompatImageButton btnsearchharvestor, btnsearchtransporter, btnsearchbulluckcardmukadam;
        AppCompatButton btnprev, btnsubmit;

        btnsearchharvestor = findViewById(R.id.btnsearchharvestor);
        btnsearchtransporter = findViewById(R.id.btnsearchtransporter);
        btnsearchbulluckcardmukadam = findViewById(R.id.btnsearchbulluckcardmukadam);
        btnprev = findViewById(R.id.btnprev);
        btnsubmit = findViewById(R.id.btnsubmit);

        btnsearchharvestor.setOnClickListener(this);
        btnsearchtransporter.setOnClickListener(this);
        btnsearchbulluckcardmukadam.setOnClickListener(this);
        btnprev.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
    }

    private void hideShow(int vehicalTypeId) {
        AppCompatTextView txtharvestorcodelbl, txtcollon4, txtharvetorlbl, txtcollon5, txtharvetorcodetxt, txtharvetortxt, txtharvetortypelbl, txtcollon6, txtharvetortypetxt, txttransportercodelbl, txtcollon7, txttransporterlbl, txtcollon8, txttransportercodetxt, txttransportertxt, txtvehicalnolbl, txtcollon9, txtvehicalnotxt, txtwireropelbl, txtcollon10, txtbulluckcardmukadamcodelbl, txtcollon11, txtbulluckcardmukadamlbl, txtcollon12, txtbulluckcardmukadamcodetxt, txtbulluckcardmukadamtxt, txtbulluckcartlbl, txtcollon13, txtfronttrailerlbl, txtcollon14, txtbacktrailerlbl, txtcollon15;
        AppCompatEditText edtharvestorcode, edttransportercode, edtbulluckcardmukadamcode;
        AppCompatImageButton btbsearchharvestor, btbsearchtransporter, btbsearchbulluckcardmukadam;
        SingleSpinnerSearch sspwirerope, sspfronttrailer, sspbacktrailer;
        MultiSpinnerSearch mspbulluckcart;

        txtharvestorcodelbl = findViewById(R.id.txtharvestorcodelbl);
        txtcollon4 = findViewById(R.id.txtcollon4);
        edtharvestorcode = findViewById(R.id.edtharvestorcode);
        btbsearchharvestor = findViewById(R.id.btnsearchharvestor);
        txtharvetorlbl = findViewById(R.id.txtharvetorlbl);
        txtcollon5 = findViewById(R.id.txtcollon5);
        txtharvetorcodetxt = findViewById(R.id.txtharvetorcodetxt);
        txtharvetortxt = findViewById(R.id.txtharvetortxt);
        txtharvetortypelbl = findViewById(R.id.txtharvetortypelbl);
        txtcollon6 = findViewById(R.id.txtcollon6);
        txtharvetortypetxt = findViewById(R.id.txtharvetortypetxt);
        txttransportercodelbl = findViewById(R.id.txttransportercodelbl);
        txtcollon7 = findViewById(R.id.txtcollon7);
        edttransportercode = findViewById(R.id.edttransportercode);
        btbsearchtransporter = findViewById(R.id.btnsearchtransporter);
        txttransporterlbl = findViewById(R.id.txttransporterlbl);
        txtcollon8 = findViewById(R.id.txtcollon8);
        txttransportercodetxt = findViewById(R.id.txttransportercodetxt);
        txttransportertxt = findViewById(R.id.txttransportertxt);
        txtvehicalnolbl = findViewById(R.id.txtvehicalnolbl);
        txtcollon9 = findViewById(R.id.txtcollon9);
        txtvehicalnotxt = findViewById(R.id.txtvehicalnotxt);
        txtwireropelbl = findViewById(R.id.txtwireropelbl);
        txtcollon10 = findViewById(R.id.txtcollon10);
        sspwirerope = findViewById(R.id.sspwirerope);
        txtbulluckcardmukadamcodelbl = findViewById(R.id.txtbulluckcardmukadamcodelbl);
        txtcollon11 = findViewById(R.id.txtcollon11);
        edtbulluckcardmukadamcode = findViewById(R.id.edtbulluckcardmukadamcode);
        btbsearchbulluckcardmukadam = findViewById(R.id.btnsearchbulluckcardmukadam);
        txtbulluckcardmukadamlbl = findViewById(R.id.txtbulluckcardmukadamlbl);
        txtcollon12 = findViewById(R.id.txtcollon12);
        txtbulluckcardmukadamtxt = findViewById(R.id.txtbulluckcardmukadamtxt);
        txtbulluckcardmukadamcodetxt = findViewById(R.id.txtbulluckcardmukadamcodetxt);
        txtbulluckcartlbl = findViewById(R.id.txtbulluckcartlbl);
        txtcollon13 = findViewById(R.id.txtcollon13);
        mspbulluckcart = findViewById(R.id.mspbulluckcart);
        txtfronttrailerlbl = findViewById(R.id.txtfronttrailerlbl);
        txtcollon14 = findViewById(R.id.txtcollon14);
        sspfronttrailer = findViewById(R.id.sspfronttrailer);
        txtbacktrailerlbl = findViewById(R.id.txtbacktrailerlbl);
        txtcollon15 = findViewById(R.id.txtcollon15);
        sspbacktrailer = findViewById(R.id.sspbacktrailer);

        switch (vehicalTypeId) {
            case 1:
            case 2:
                txtharvestorcodelbl.setVisibility(View.VISIBLE);
                txtcollon4.setVisibility(View.VISIBLE);
                edtharvestorcode.setVisibility(View.VISIBLE);
                btbsearchharvestor.setVisibility(View.VISIBLE);
                txtharvetorlbl.setVisibility(View.VISIBLE);
                txtcollon5.setVisibility(View.VISIBLE);
                txtharvetorcodetxt.setVisibility(View.VISIBLE);
                txtharvetortxt.setVisibility(View.VISIBLE);
                txtharvetortypelbl.setVisibility(View.VISIBLE);
                txtcollon6.setVisibility(View.VISIBLE);
                txtharvetortypetxt.setVisibility(View.VISIBLE);
                txttransportercodelbl.setVisibility(View.VISIBLE);
                txtcollon7.setVisibility(View.VISIBLE);
                edttransportercode.setVisibility(View.VISIBLE);
                btbsearchtransporter.setVisibility(View.VISIBLE);
                txttransporterlbl.setVisibility(View.VISIBLE);
                txtcollon8.setVisibility(View.VISIBLE);
                txttransportercodetxt.setVisibility(View.VISIBLE);
                txttransportertxt.setVisibility(View.VISIBLE);
                txtvehicalnolbl.setVisibility(View.VISIBLE);
                txtcollon9.setVisibility(View.VISIBLE);
                txtvehicalnotxt.setVisibility(View.VISIBLE);
                if (vehicalTypeId == 2) {
                    txtwireropelbl.setVisibility(View.GONE);
                    txtcollon10.setVisibility(View.GONE);
                    sspwirerope.setVisibility(View.GONE);

                    txtfronttrailerlbl.setVisibility(View.VISIBLE);
                    txtcollon14.setVisibility(View.VISIBLE);
                    sspfronttrailer.setVisibility(View.VISIBLE);
                    txtbacktrailerlbl.setVisibility(View.VISIBLE);
                    txtcollon15.setVisibility(View.VISIBLE);
                    sspbacktrailer.setVisibility(View.VISIBLE);
                } else {
                    txtwireropelbl.setVisibility(View.VISIBLE);
                    txtcollon10.setVisibility(View.VISIBLE);
                    sspwirerope.setVisibility(View.VISIBLE);

                    txtfronttrailerlbl.setVisibility(View.GONE);
                    txtcollon14.setVisibility(View.GONE);
                    sspfronttrailer.setVisibility(View.GONE);
                    txtbacktrailerlbl.setVisibility(View.GONE);
                    txtcollon15.setVisibility(View.GONE);
                    sspbacktrailer.setVisibility(View.GONE);
                }
                txtbulluckcardmukadamcodelbl.setVisibility(View.GONE);
                txtcollon11.setVisibility(View.GONE);
                edtbulluckcardmukadamcode.setVisibility(View.GONE);
                btbsearchbulluckcardmukadam.setVisibility(View.GONE);
                txtbulluckcardmukadamlbl.setVisibility(View.GONE);
                txtcollon12.setVisibility(View.GONE);
                txtbulluckcardmukadamcodetxt.setVisibility(View.GONE);
                txtbulluckcardmukadamtxt.setVisibility(View.GONE);
                txtbulluckcartlbl.setVisibility(View.GONE);
                txtcollon13.setVisibility(View.GONE);
                mspbulluckcart.setVisibility(View.GONE);
                break;
            case 3:
            case 4:
                txtharvestorcodelbl.setVisibility(View.GONE);
                txtcollon4.setVisibility(View.GONE);
                edtharvestorcode.setVisibility(View.GONE);
                btbsearchharvestor.setVisibility(View.GONE);
                txtharvetorlbl.setVisibility(View.GONE);
                txtcollon5.setVisibility(View.GONE);
                txtharvetorcodetxt.setVisibility(View.GONE);
                txtharvetortxt.setVisibility(View.GONE);
                txtharvetortypelbl.setVisibility(View.GONE);
                txtcollon6.setVisibility(View.GONE);
                txtharvetortypetxt.setVisibility(View.GONE);
                txttransportercodelbl.setVisibility(View.GONE);
                txtcollon7.setVisibility(View.GONE);
                edttransportercode.setVisibility(View.GONE);
                btbsearchtransporter.setVisibility(View.GONE);
                txttransporterlbl.setVisibility(View.GONE);
                txtcollon8.setVisibility(View.GONE);
                txttransportercodetxt.setVisibility(View.GONE);
                txttransportertxt.setVisibility(View.GONE);
                txtvehicalnolbl.setVisibility(View.GONE);
                txtcollon9.setVisibility(View.GONE);
                txtvehicalnotxt.setVisibility(View.GONE);
                txtwireropelbl.setVisibility(View.GONE);
                txtcollon10.setVisibility(View.GONE);
                sspwirerope.setVisibility(View.GONE);
                txtbulluckcardmukadamcodelbl.setVisibility(View.VISIBLE);
                txtcollon11.setVisibility(View.VISIBLE);
                edtbulluckcardmukadamcode.setVisibility(View.VISIBLE);
                btbsearchbulluckcardmukadam.setVisibility(View.VISIBLE);
                txtbulluckcardmukadamlbl.setVisibility(View.VISIBLE);
                txtcollon12.setVisibility(View.VISIBLE);
                txtbulluckcardmukadamcodetxt.setVisibility(View.VISIBLE);
                txtbulluckcardmukadamtxt.setVisibility(View.VISIBLE);
                txtbulluckcartlbl.setVisibility(View.VISIBLE);
                txtcollon13.setVisibility(View.VISIBLE);
                mspbulluckcart.setVisibility(View.VISIBLE);

                if (vehicalTypeId == 4) {
                    txtfronttrailerlbl.setVisibility(View.VISIBLE);
                    txtcollon14.setVisibility(View.VISIBLE);
                    sspfronttrailer.setVisibility(View.VISIBLE);
                    txtbacktrailerlbl.setVisibility(View.VISIBLE);
                    txtcollon15.setVisibility(View.VISIBLE);
                    sspbacktrailer.setVisibility(View.VISIBLE);
                } else {
                    txtfronttrailerlbl.setVisibility(View.GONE);
                    txtcollon14.setVisibility(View.GONE);
                    sspfronttrailer.setVisibility(View.GONE);
                    txtbacktrailerlbl.setVisibility(View.GONE);
                    txtcollon15.setVisibility(View.GONE);
                    sspbacktrailer.setVisibility(View.GONE);
                }
                break;
            default:
                txtharvestorcodelbl.setVisibility(View.GONE);
                txtcollon4.setVisibility(View.GONE);
                edtharvestorcode.setVisibility(View.GONE);
                btbsearchharvestor.setVisibility(View.GONE);
                txtharvetorlbl.setVisibility(View.GONE);
                txtcollon5.setVisibility(View.GONE);
                txtharvetorcodetxt.setVisibility(View.GONE);
                txtharvetortxt.setVisibility(View.GONE);
                txtharvetortypelbl.setVisibility(View.GONE);
                txtcollon6.setVisibility(View.GONE);
                txtharvetortypetxt.setVisibility(View.GONE);
                txttransportercodelbl.setVisibility(View.GONE);
                txtcollon7.setVisibility(View.GONE);
                edttransportercode.setVisibility(View.GONE);
                btbsearchtransporter.setVisibility(View.GONE);
                txttransporterlbl.setVisibility(View.GONE);
                txtcollon8.setVisibility(View.GONE);
                txttransportercodetxt.setVisibility(View.GONE);
                txttransportertxt.setVisibility(View.GONE);
                txtvehicalnolbl.setVisibility(View.GONE);
                txtcollon9.setVisibility(View.GONE);
                txtvehicalnotxt.setVisibility(View.GONE);
                txtwireropelbl.setVisibility(View.GONE);
                txtcollon10.setVisibility(View.GONE);
                sspwirerope.setVisibility(View.GONE);
                txtbulluckcardmukadamcodelbl.setVisibility(View.GONE);
                txtcollon11.setVisibility(View.GONE);
                edtbulluckcardmukadamcode.setVisibility(View.GONE);
                btbsearchbulluckcardmukadam.setVisibility(View.GONE);
                txtbulluckcardmukadamlbl.setVisibility(View.GONE);
                txtcollon12.setVisibility(View.GONE);
                txtbulluckcardmukadamcodetxt.setVisibility(View.GONE);
                txtbulluckcardmukadamtxt.setVisibility(View.GONE);
                txtbulluckcartlbl.setVisibility(View.GONE);
                txtcollon13.setVisibility(View.GONE);
                mspbulluckcart.setVisibility(View.GONE);
                txtfronttrailerlbl.setVisibility(View.GONE);
                txtcollon14.setVisibility(View.GONE);
                sspfronttrailer.setVisibility(View.GONE);
                txtbacktrailerlbl.setVisibility(View.GONE);
                txtcollon15.setVisibility(View.GONE);
                sspbacktrailer.setVisibility(View.GONE);
                break;
        }

    }

    private void initAllData() {
        Activity activity = HarvTransInfoActivity.this;
        SingleSpinnerSearch sspcanetype = findViewById(R.id.sspcanetype);
        SingleSpinnerSearch sspvehicaltype = findViewById(R.id.sspvehicaltype);
        SingleSpinnerSearch sspwirerope = findViewById(R.id.sspwirerope);
        SingleSpinnerSearch sspfronttrailer = findViewById(R.id.sspfronttrailer);
        SingleSpinnerSearch sspbacktrailer = findViewById(R.id.sspbacktrailer);
        SingleSpinnerSearch sspremark = findViewById(R.id.sspremark);

        MultiSpinnerSearch mspbulluckcart = findViewById(R.id.mspbulluckcart);

        AppCompatTextView txtslipboytxt = findViewById(R.id.txtslipboytxt);
        AppCompatTextView txtbalancetonnagetxt = activity.findViewById(R.id.txtbalancetonnagetxt);

        SingleSelectHandler selectHandler = new SingleSelectHandler();
        selectHandler.singleReturn = this;
        selectHandler.initSingle(sspcanetype, activity, getResources().getString(R.string.canetypesearch));
        selectHandler.initSingle(sspvehicaltype, activity, getResources().getString(R.string.vehicaltypesearch));
        selectHandler.initSingle(sspwirerope, activity, getResources().getString(R.string.wireropesearch));
        selectHandler.initSingle(sspfronttrailer, activity, getResources().getString(R.string.fronttrailersearch));
        selectHandler.initSingle(sspbacktrailer, activity, getResources().getString(R.string.backtrailersearch));
        selectHandler.initSingle(sspremark, activity, getResources().getString(R.string.remarksearch));

        MultiSelectHandler multiSelectHandler = new MultiSelectHandler();
        multiSelectHandler.multiReturn = this;


        int cropId = 0;
        int vehicleId = 0;
        int remarkId = 0;
        List<KeyPairBoolData> list = new ArrayList<>();
        if (harvSlipDetailsResponse == null) {
            selectHandler.setSingleItems(sspwirerope, list, DataSetter.WIREROPE);
            selectHandler.setSingleItems(sspfronttrailer, list, DataSetter.FRONTTRAILER);
            selectHandler.setSingleItems(sspbacktrailer, list, DataSetter.BACKTRAILER);

            multiSelectHandler.initMulti(mspbulluckcart, true, 0, activity, getResources().getString(R.string.bulluckcartsearch), getResources().getString(R.string.select), null);
            multiSelectHandler.setMultiSpinner(mspbulluckcart, list, DataSetter.BULLUCKCART);

            generateToken();
        } else {
            tokenno = harvSlipDetailsResponse.getNslipOfflineNo();
            combos = harvSlipDetailsResponse.getCombos();
            cropId = harvSlipDetailsResponse.getNcropId();
            vehicleId = harvSlipDetailsResponse.getVehicleType();
            remarkId = harvSlipDetailsResponse.getNremarkId();

            selectHandler.setSingleItems(sspwirerope, harvSlipDetailsResponse.getWirerope() != null ? harvSlipDetailsResponse.getWirerope() : list, DataSetter.WIREROPE);
            selectHandler.setSingleItems(sspfronttrailer, harvSlipDetailsResponse.getWireropefront() != null ? harvSlipDetailsResponse.getWireropefront() : list, DataSetter.FRONTTRAILER);
            selectHandler.setSingleItems(sspbacktrailer, harvSlipDetailsResponse.getWireropeback() != null ? harvSlipDetailsResponse.getWireropeback() : list, DataSetter.BACKTRAILER);

            if (harvSlipDetailsResponse.getNslipNo() == null || harvSlipDetailsResponse.getNslipNo().equals("") || harvSlipDetailsResponse.getNslipNo().equals("0")) {
                multiSelectHandler.initMulti(mspbulluckcart, true, 0, activity, getResources().getString(R.string.bulluckcartsearch), getResources().getString(R.string.select), null);
            } else {
                multiSelectHandler.initMulti(mspbulluckcart, false, 1, activity, getResources().getString(R.string.bulluckcartsearch), getResources().getString(R.string.select), null);
            }
            multiSelectHandler.setMultiSpinner(mspbulluckcart, harvSlipDetailsResponse.getBulluckcartList() != null ? harvSlipDetailsResponse.getBulluckcartList() : list, DataSetter.BULLUCKCART);

            AppCompatEditText edtharvestorcode, edttransportercode, edtbulluckcardmukadamcode;
            AppCompatTextView txtslipno, txtharvetorcodetxt, txtharvetortxt, txtharvetortypetxt, txtharvetortypeidtxt, txttransportercodetxt, txttransportertxt, txtbulluckcardmukadamcodetxt, txtbulluckcardmukadamtxt, txtvehicalnotxt;

            txtslipno = findViewById(R.id.txtslipno);
            edtharvestorcode = findViewById(R.id.edtharvestorcode);
            txtharvetorcodetxt = findViewById(R.id.txtharvetorcodetxt);
            txtharvetortxt = findViewById(R.id.txtharvetortxt);
            txtharvetortypetxt = findViewById(R.id.txtharvetortypetxt);
            txtharvetortypeidtxt = findViewById(R.id.txtharvetortypeidtxt);
            edttransportercode = findViewById(R.id.edttransportercode);
            txttransportercodetxt = findViewById(R.id.txttransportercodetxt);
            txttransportertxt = findViewById(R.id.txttransportertxt);
            txtvehicalnotxt = findViewById(R.id.txtvehicalnotxt);
            edtbulluckcardmukadamcode = findViewById(R.id.edtbulluckcardmukadamcode);
            txtbulluckcardmukadamcodetxt = findViewById(R.id.txtbulluckcardmukadamcodetxt);
            txtbulluckcardmukadamtxt = findViewById(R.id.txtbulluckcardmukadamtxt);

            txtslipno.setText(harvSlipDetailsResponse.getNslipNo());
            if (harvSlipDetailsResponse.getNharvestorId() != null) {
                edtharvestorcode.setText(harvSlipDetailsResponse.getNharvestorId().replaceAll("[^0-9]", ""));
                txtharvetorcodetxt.setText(harvSlipDetailsResponse.getNharvestorId());
                txtharvetortxt.setText(harvSlipDetailsResponse.getHarvetorname());
                txtharvetortypetxt.setText(harvSlipDetailsResponse.getHarvetortype());
                txtharvetortypeidtxt.setText(harvSlipDetailsResponse.getNgadiDokiId());
            }

            if (harvSlipDetailsResponse.getVvehicleNo() != null) {
                txtvehicalnotxt.setText(harvSlipDetailsResponse.getVvehicleNo());
            }

            if (harvSlipDetailsResponse.getNtransportorId() != null) {
                edttransportercode.setText(harvSlipDetailsResponse.getNtransportorId().replaceAll("[^0-9]", ""));
                txttransportercodetxt.setText(harvSlipDetailsResponse.getNtransportorId());
                txttransportertxt.setText(harvSlipDetailsResponse.getTransportername());
            }
            if (harvSlipDetailsResponse.getNbulluckcartMainId() != null) {
                edtbulluckcardmukadamcode.setText(harvSlipDetailsResponse.getNbulluckcartMainId().replaceAll("[^0-9]", ""));
                txtbulluckcardmukadamcodetxt.setText(harvSlipDetailsResponse.getNbulluckcartMainId());
                txtbulluckcardmukadamtxt.setText(harvSlipDetailsResponse.getBulluckcartMainName());
            }

        }

        DBHelper dbHelper = new DBHelper(activity);
        List<KeyPairBoolData> caneTypeList = dbHelper.getCaneType(cropId);
        List<KeyPairBoolData> vehicaltypeList = dbHelper.getVehicleType(DBHelper.vehicleTypeTable, vehicleId);
        List<KeyPairBoolData> reason = dbHelper.getRemark(remarkId);

        selectHandler.setSingleItems(sspcanetype, caneTypeList, DataSetter.CANETYPE);
        selectHandler.setSingleItems(sspvehicaltype, vehicaltypeList, DataSetter.VEHICALTYPE);
        selectHandler.setSingleItems(sspremark, reason, DataSetter.REMARK);

        ConstantFunction cf = new ConstantFunction();
        String[] data = cf.getSharedPrefValue(activity, new String[]{getResources().getString(R.string.chitboyprefname), getResources().getString(R.string.prefharvplotdata)}, new String[]{"", "{}"});
        txtslipboytxt.setText(data[0]);
        JSONObject harvJob = null;
        try {
            harvJob = new JSONObject(data[1]);
            txtbalancetonnagetxt.setText(harvJob.has("balanceTonnage") ? harvJob.getString("balanceTonnage") : "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (harvJob.has("ngpsDistance")) {
            try {
                ngps_distance = harvJob.getInt("ngpsDistance");
            } catch (JSONException e) {
                e.printStackTrace();
                ngps_distance = 0;
            }
        } else {
            ngps_distance = 0;
        }

        if (harvJob.has("ndistance")) {
            try {
                distance = harvJob.getInt("ndistance");
            } catch (JSONException e) {
                e.printStackTrace();
                distance = 0;
            }
        } else {
            distance = 0;
        }

        if (vehicleId != 0) {
            hideShow(vehicleId);
            loadDistance(vehicleId);
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
        return cf.openCommon(this, item, HarvTransInfoActivity.this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, this);
    }

    @Override
    public void onItemsSelected(List<KeyPairBoolData> selectedItems, DataSetter dataSetter) {
        if (dataSetter == DataSetter.BULLUCKCART) {
            checkIsAllow(selectedItems, false);
        }
    }

    private void checkIsAllow(List<KeyPairBoolData> selectedItems, boolean clearAll) {
        int size = selectedItems.size();
        boolean isZero = false;
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < size; i++) {
            KeyPairBoolData data = selectedItems.get(i);
            String json = (String) data.getObject();
            try {
                JSONObject job = new JSONObject(json);
                if (!job.has("allow") || !job.getBoolean("allow")) {
                    isZero = true;
                    message.append("\n");
                    if (job.has("msg"))
                        message.append(job.getString("name")).append(" ").append(job.getString("msg"));
                    else {
                        if (i == 0) {
                            message.append(getResources().getString(R.string.errornotallow));
                        }
                        message.append(data.getName());
                    }
                } else {
                    if (job.has("removewire")) removeWirerope(job.getJSONObject("removewire"));
                    else {
                        SingleSpinnerSearch sspvehicaltype = findViewById(R.id.sspvehicaltype);
                        List<Long> sspselid = sspvehicaltype.getSelectedIds();
                        if (sspselid.get(0) == 4) {
                            removeWirerope(new JSONObject());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (isZero) {
            Constant.showToast(message.toString(), HarvTransInfoActivity.this, R.drawable.ic_wrong);
            if (clearAll) {
                MultiSpinnerSearch mspbulluckcart = findViewById(R.id.mspbulluckcart);
                mspbulluckcart.clearAll();
            }
        }
    }

    private void removeWirerope(JSONObject removewire) {
        SingleSelectHandler selectHandler = new SingleSelectHandler();
        selectHandler.singleReturn = this;
        int frontsize = 0;
        int backsize = 0;

        boolean frontall = false;
        boolean backall = false;
        String keepid = "5";
        try {
            System.out.println(" before " + frontall + " removewire " + removewire);
            frontall = removewire.has("frontall") && removewire.getBoolean("frontall");
            System.out.println(" after " + frontall);
            System.out.println(removewire.has("frontall"));
            backall = removewire.has("backall") && removewire.getBoolean("backall");
            if (removewire.has("keepid")) keepid = removewire.getString("keepid");
            if (frontall && backall) {
                Constant.showToast(getResources().getString(R.string.errorpreviousslipremain), HarvTransInfoActivity.this, R.drawable.ic_wrong);
                MultiSpinnerSearch mspbulluckcart = findViewById(R.id.mspbulluckcart);
                mspbulluckcart.clearAll();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SingleSpinnerSearch sspfronttrailer = findViewById(R.id.sspfronttrailer);
        SingleSpinnerSearch sspbacktrailer = findViewById(R.id.sspbacktrailer);

        if (removewire.has("front")) {

            ArrayList<KeyPairBoolData> keyPairBoolDataList = (ArrayList<KeyPairBoolData>) frontwirerope.clone();
            frontsize = keyPairBoolDataList.size();
            try {
                String selectedWire = "," + removewire.getString("front") + ",";
                for (int i = frontsize - 1; i >= 0; i--) {
                    KeyPairBoolData keyPairBoolData = keyPairBoolDataList.get(i);
                    if (frontall) {
                        if (keyPairBoolData.getId() != Long.parseLong(keepid)) {
                            keyPairBoolDataList.remove(i);
                        }
                    } else if (selectedWire.contains("," + keyPairBoolData.getId() + ",")) {
                        keyPairBoolDataList.remove(i);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            frontsize = keyPairBoolDataList.size();
            selectHandler.setSingleItems(sspfronttrailer, keyPairBoolDataList, DataSetter.FRONTTRAILER);
        } else {
            selectHandler.setSingleItems(sspfronttrailer, frontwirerope, DataSetter.FRONTTRAILER);
        }

        if (removewire.has("back")) {
            ArrayList<KeyPairBoolData> keyPairBoolDataList = (ArrayList<KeyPairBoolData>) backwirerope.clone();
            backsize = keyPairBoolDataList.size();
            try {
                String selectedWire = "," + removewire.getString("back") + ",";
                for (int i = backsize - 1; i >= 0; i--) {
                    KeyPairBoolData keyPairBoolData = keyPairBoolDataList.get(i);
                    if (backall) {
                        if (keyPairBoolData.getId() != Long.parseLong(keepid)) {
                            keyPairBoolDataList.remove(i);
                        }
                    } else if (selectedWire.contains("," + keyPairBoolData.getId() + ",")) {
                        keyPairBoolDataList.remove(i);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            backsize = keyPairBoolDataList.size();
            selectHandler.setSingleItems(sspbacktrailer, keyPairBoolDataList, DataSetter.BACKTRAILER);
        } else {
            selectHandler.setSingleItems(sspbacktrailer, backwirerope, DataSetter.BACKTRAILER);
        }

        if (backsize == 1 && frontsize == 1) {
            Constant.showToast(getResources().getString(R.string.errorpreviousslipremain), HarvTransInfoActivity.this, R.drawable.ic_wrong);
            MultiSpinnerSearch mspbulluckcart = findViewById(R.id.mspbulluckcart);
            mspbulluckcart.clearAll();
            sspbacktrailer.clearAll();
            sspfronttrailer.clearAll();
        }
    }

    @Override
    public void onOkClickLister(List<KeyPairBoolData> selectedItems, DataSetter dataSetter) {
        if (dataSetter == DataSetter.BULLUCKCART) {
            checkIsAllow(selectedItems, true);
        }
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        String vehicalType;
        AppCompatTextView txtvehicalnotxt;
        switch (requestCaller) {
            case TRANSINFO:
                SingleSpinnerSearch sspfronttrailer, sspbacktrailer;
                SingleSelectHandler selectHandler;
                TransporterResponse transporterResponse = (TransporterResponse) response.body();
                AppCompatTextView txttransportercodetxt, txttransportertxt;
                SingleSpinnerSearch sspwirerope;

                txttransportercodetxt = findViewById(R.id.txttransportercodetxt);
                txttransportertxt = findViewById(R.id.txttransportertxt);
                txtvehicalnotxt = findViewById(R.id.txtvehicalnotxt);

                sspwirerope = findViewById(R.id.sspwirerope);
                sspfronttrailer = findViewById(R.id.sspfronttrailer);
                sspbacktrailer = findViewById(R.id.sspbacktrailer);

                txttransportercodetxt.setText(transporterResponse.getCode());
                txttransportertxt.setText(transporterResponse.getName());
                txtvehicalnotxt.setText(transporterResponse.getVehicalNo());

                vehicalType = (String) obj[0];
                selectHandler = new SingleSelectHandler();
                selectHandler.singleReturn = this;
                if (vehicalType.equals("1")) {
                    if (transporterResponse.getWirerope() != null)
                        selectHandler.setSingleItems(sspwirerope, transporterResponse.getWirerope(), DataSetter.WIREROPE);
                } else {
                    if (transporterResponse.getWireropefront() != null)
                        selectHandler.setSingleItems(sspfronttrailer, transporterResponse.getWireropefront(), DataSetter.FRONTTRAILER);
                    if (transporterResponse.getWireropeback() != null)
                        selectHandler.setSingleItems(sspbacktrailer, transporterResponse.getWireropeback(), DataSetter.BACKTRAILER);
                }
                combos = transporterResponse.getCombos();
                break;
            case HARVINFO:
                HarvestorResponse harvestorResponse = (HarvestorResponse) response.body();
                AppCompatTextView txtharvetorcodetxt, txtharvetortxt, txtharvetortypetxt, txtharvetortypeidtxt;

                txtharvetorcodetxt = findViewById(R.id.txtharvetorcodetxt);
                txtharvetortxt = findViewById(R.id.txtharvetortxt);
                txtharvetortypetxt = findViewById(R.id.txtharvetortypetxt);
                txtharvetortypeidtxt = findViewById(R.id.txtharvetortypeidtxt);

                txtharvetorcodetxt.setText(harvestorResponse.getCode());
                txtharvetortxt.setText(harvestorResponse.getName());
                txtharvetortypetxt.setText(harvestorResponse.getType());
                txtharvetortypeidtxt.setText(harvestorResponse.getTypeCode());

                break;
            case BULLUCKINFO:

                BulluckCartResponse bulluckCartResponse = (BulluckCartResponse) response.body();
                AppCompatTextView txtbulluckcardmukadamcodetxt, txtbulluckcardmukadamtxt;
                MultiSpinnerSearch mspbulluckcart;

                MultiSelectHandler multiSelect = new MultiSelectHandler();
                multiSelect.multiReturn = this;

                vehicalType = (String) obj[0];
                mspbulluckcart = findViewById(R.id.mspbulluckcart);
                if (vehicalType.equals("4"))
                    multiSelect.initMulti(mspbulluckcart, false, 1, activity, getResources().getString(R.string.bulluckcartsearch), getResources().getString(R.string.select), null);
                else {
                    String data = (String) obj[1];
                    try {
                        JSONObject job = new JSONObject(data);
                        if (job.has("nbullckcartLimit")) {
                            ServerValidation sv = new ServerValidation();
                            String nbullckcartLimit = job.getString("nbullckcartLimit");
                            if (sv.checkNumber(nbullckcartLimit)) {
                                multiSelect.initMulti(mspbulluckcart, false, Integer.parseInt(nbullckcartLimit), activity, getResources().getString(R.string.bulluckcartsearch), getResources().getString(R.string.select), String.format(activity.getResources().getString(R.string.maxgadimessage), "" + nbullckcartLimit));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                multiSelect.setMultiSpinner(mspbulluckcart, bulluckCartResponse.getBulluckcartList(), DataSetter.BULLUCKCART);

                txtvehicalnotxt = findViewById(R.id.txtvehicalnotxt);
                txtbulluckcardmukadamcodetxt = findViewById(R.id.txtbulluckcardmukadamcodetxt);
                txtbulluckcardmukadamtxt = findViewById(R.id.txtbulluckcardmukadamtxt);

                txtbulluckcardmukadamcodetxt.setText(bulluckCartResponse.getMukadamCode());
                txtbulluckcardmukadamtxt.setText(bulluckCartResponse.getMukadamName());
                if (vehicalType.equals("4")) {
                    frontwirerope = (ArrayList<KeyPairBoolData>) bulluckCartResponse.getWireropefront();
                    backwirerope = (ArrayList<KeyPairBoolData>) bulluckCartResponse.getWireropeback();
                    combos = bulluckCartResponse.getCombos();
                    txtvehicalnotxt.setText(bulluckCartResponse.getVehicalNo());
                }
                break;
            case SAVEWS:
                WeightSlipResponse weightSlipResponse = (WeightSlipResponse) response.body();
                String oldslipno = obj[1] != null ? (String) obj[1] : null;
                if (weightSlipResponse.isActionComplete()) {
                    Constant.showToast(weightSlipResponse.getSuccessMsg() != null ? weightSlipResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                    Intent intent = new Intent(HarvTransInfoActivity.this, PrintSlipActivity.class);
                    List<SlipBeanR> slipBeanRList = weightSlipResponse.getSlipBeanrList();
                    intent.putExtra("slipBeanRList", (Serializable) slipBeanRList);
                    intent.putExtra("date", weightSlipResponse.getDate());
                    intent.putExtra("oldslip", oldslipno != null && !oldslipno.trim().equals(""));
                    startActivity(intent);
                } else {
                    Constant.showToast(weightSlipResponse.getFailMsg() != null ? weightSlipResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        switch (dataSetter) {
            case CANETYPE:

                break;
            case VEHICALTYPE:
                clearAll(false);
                hideShow((int) selectedItem.getId());
                loadDistance((int) selectedItem.getId());
                break;
            case WIREROPE:

                break;
            case FRONTTRAILER:

                break;
            case BACKTRAILER:

                break;
            case REMARK:

                break;
            default:

                break;
        }
    }

    private void loadDistance(int vehicalTypeId) {
        /* ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.prefharvplotdata)};
        String[] value = new String[]{""};
        String[] data = cf.getSharedPrefValue(HarvTransInfoActivity.this, key, value);
        JSONObject job = null;*/
        AppCompatTextView txtdistancetxt = findViewById(R.id.txtdistancetxt);
        /* try {
            job = new JSONObject(data[0]);
        } catch (JSONException e) {
            e.printStackTrace();
            txtdistancetxt.setText("0");
            return;
        }*/
        AppCompatEditText edtdistanceuser = findViewById(R.id.edtdistanceuser);
        if (vehicalTypeId == 1 || vehicalTypeId == 2) {
            txtdistancetxt.setText(String.valueOf(ngps_distance));
            /* if (job.has("ngpsDistance")) {
                try {
                    txtdistancetxt.setText(String.valueOf(job.getInt("ngpsDistance")));
                } catch (JSONException e) {
                    e.printStackTrace();
                    txtdistancetxt.setText("0");
                }
            } else {
                txtdistancetxt.setText("0");
            }*/
            edtdistanceuser.setVisibility(View.GONE);
            txtdistancetxt.setVisibility(View.VISIBLE);
        } else if (vehicalTypeId == 3 || vehicalTypeId == 4) {
            txtdistancetxt.setText(String.valueOf(distance));
            /* if (job.has("ndistance")) {
                try {
                    txtdistancetxt.setText(String.valueOf(job.getInt("ndistance")));
                } catch (JSONException e) {
                    e.printStackTrace();
                    txtdistancetxt.setText("0");
                }
            } else {
                txtdistancetxt.setText("0");
            }*/
            //String distance = txtdistancetxt.getText().toString().trim();

            if (distance == 0) {
                edtdistanceuser.setVisibility(View.VISIBLE);
                txtdistancetxt.setVisibility(View.INVISIBLE);
            } else {
                edtdistanceuser.setVisibility(View.GONE);
                txtdistancetxt.setVisibility(View.VISIBLE);
            }
        }
    }

    private void clearAll(boolean clearAll) {
        AppCompatTextView txtdistancetxt, txtharvetorcodetxt, txtharvetortxt, txtharvetortypetxt, txtharvetortypeidtxt, txttransportercodetxt, txttransportertxt, txtvehicalnotxt, txtbulluckcardmukadamcodetxt, txtbulluckcardmukadamtxt, txtslipboytxt;
        AppCompatEditText edtharvestorcode, edttransportercode, edtbulluckcardmukadamcode;
        SingleSpinnerSearch sspcanetype, sspvehicaltype, sspwirerope, sspfronttrailer, sspbacktrailer, sspremark;
        MultiSpinnerSearch mspbulluckcart;

        sspcanetype = findViewById(R.id.sspcanetype);
        sspvehicaltype = findViewById(R.id.sspvehicaltype);
        txtdistancetxt = findViewById(R.id.txtdistancetxt);
        edtharvestorcode = findViewById(R.id.edtharvestorcode);
        txtharvetorcodetxt = findViewById(R.id.txtharvetorcodetxt);
        txtharvetortxt = findViewById(R.id.txtharvetortxt);
        txtharvetortypetxt = findViewById(R.id.txtharvetortypetxt);
        txtharvetortypeidtxt = findViewById(R.id.txtharvetortypeidtxt);
        edttransportercode = findViewById(R.id.edttransportercode);
        txttransportercodetxt = findViewById(R.id.txttransportercodetxt);
        txttransportertxt = findViewById(R.id.txttransportertxt);
        txtvehicalnotxt = findViewById(R.id.txtvehicalnotxt);
        sspwirerope = findViewById(R.id.sspwirerope);
        edtbulluckcardmukadamcode = findViewById(R.id.edtbulluckcardmukadamcode);
        txtbulluckcardmukadamcodetxt = findViewById(R.id.txtbulluckcardmukadamcodetxt);
        txtbulluckcardmukadamtxt = findViewById(R.id.txtbulluckcardmukadamtxt);
        mspbulluckcart = findViewById(R.id.mspbulluckcart);
        sspfronttrailer = findViewById(R.id.sspfronttrailer);
        sspbacktrailer = findViewById(R.id.sspbacktrailer);
        txtslipboytxt = findViewById(R.id.txtslipboytxt);
        sspremark = findViewById(R.id.sspremark);

        if (clearAll) {
            sspcanetype.clearAll();
            sspvehicaltype.clearAll();
            txtslipboytxt.setText("");
        }
        txtdistancetxt.setText("");
        txtharvetorcodetxt.setText("");
        txtharvetortxt.setText("");
        txtharvetortypetxt.setText("");
        txtharvetortypeidtxt.setText("");
        txttransportercodetxt.setText("");
        txttransportertxt.setText("");
        txtvehicalnotxt.setText("");
        txtbulluckcardmukadamcodetxt.setText("");
        txtbulluckcardmukadamtxt.setText("");
        edtharvestorcode.setText("");
        edttransportercode.setText("");
        edtbulluckcardmukadamcode.setText("");
        sspwirerope.clearAll();
        sspfronttrailer.clearAll();
        sspbacktrailer.clearAll();
        sspremark.clearAll();
        mspbulluckcart.clearAll();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btnprev) {
            finish();
            return;
        }

        Activity activity = HarvTransInfoActivity.this;
        SingleSpinnerSearch sspvehicaltype = findViewById(R.id.sspvehicaltype);
        List<KeyPairBoolData> list = sspvehicaltype.getSelectedItems();
        if (list.size() != 1) {
            Constant.showToast(getResources().getString(R.string.errorvehicaltypechoose), activity, R.drawable.ic_wrong);
            return;
        }

        KeyPairBoolData selectedVehical = list.get(0);
        String vehicleType = String.valueOf(selectedVehical.getId());

        RetrofitResponse retrofitResponse = HarvTransInfoActivity.this;
        JSONObject job = new JSONObject();
        String action;
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason), getResources().getString(R.string.prefharvplotdata)};
        String[] value = new String[]{"", "0", "", "{}"};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        String servlet = getResources().getString(R.string.servletharvdata);
        JSONObject faremrInfo = null;
        try {
            faremrInfo = new JSONObject(data[3]);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();

        try {
            job.put("nsectionId", faremrInfo.has("nsectionId") ? faremrInfo.getString("nsectionId") : "");
            job.put("yearCode", data[2]);
            job.put("vehicleType", vehicleType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerValidation sv = new ServerValidation();
        if (id == R.id.btnsearchharvestor) {
            AppCompatEditText edtharvestorcode = findViewById(R.id.edtharvestorcode);
            String harvCode = edtharvestorcode.getText().toString();

            if (!sv.checkNumber(harvCode)) {
                edtharvestorcode.setError(getResources().getString(R.string.errorenterharvcode));
                return;
            }

            try {
                job.put("harvCode", harvCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            action = getResources().getString(R.string.actionharvdetails);
            Call<HarvestorResponse> call2 = apiInterface.harvDetails(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<HarvestorResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, retrofitResponse, RequestEnum.HARVINFO, servlet, activity, versioncode, vehicleType);
        } else if (id == R.id.btnsearchtransporter) {
            AppCompatEditText edttransportercode = findViewById(R.id.edttransportercode);
            AppCompatTextView txtslipno = findViewById(R.id.txtslipno);
            String transCode = edttransportercode.getText().toString();

            if (!sv.checkNumber(transCode)) {
                edttransportercode.setError(getResources().getString(R.string.errorentertranscode));
                return;
            }

            try {
                job.put("nslipNo", txtslipno.getText().toString());
                job.put("transCode", transCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            action = getResources().getString(R.string.actiontransdetails);
            Call<TransporterResponse> call2 = apiInterface.transDetails(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<TransporterResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, retrofitResponse, RequestEnum.TRANSINFO, servlet, activity, versioncode, vehicleType);
        } else if (id == R.id.btnsearchbulluckcardmukadam) {
            AppCompatEditText edtbulluckcardmukadamcode = findViewById(R.id.edtbulluckcardmukadamcode);
            AppCompatTextView txtslipno = findViewById(R.id.txtslipno);
            String mukadamcode = edtbulluckcardmukadamcode.getText().toString();

            if (!sv.checkNumber(mukadamcode)) {
                edtbulluckcardmukadamcode.setError(getResources().getString(R.string.errorentermukadamcode));
                return;
            }

            try {
                job.put("nslipNo", txtslipno.getText().toString());
                job.put("mukadamCode", mukadamcode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            action = getResources().getString(R.string.actionmukadamdetails);
            Call<BulluckCartResponse> call2 = apiInterface.mukdetailsDetails(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<BulluckCartResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, retrofitResponse, RequestEnum.BULLUCKINFO, servlet, activity, versioncode, vehicleType, data[3]);
        } else if (id == R.id.btnsubmit) {
            AppCompatTextView txtdistancetxt, txtharvetorcodetxt, txtharvetortxt, txtharvetortypetxt, txtharvetortypeidtxt, txttransportercodetxt, txttransportertxt, txtvehicalnotxt, txtbulluckcardmukadamcodetxt, txtbulluckcardmukadamtxt, txtslipboytxt;
            AppCompatEditText edtharvestorcode, edttransportercode, edtbulluckcardmukadamcode;
            SingleSpinnerSearch sspcanetype, sspwirerope, sspfronttrailer, sspbacktrailer, sspremark;
            MultiSpinnerSearch mspbulluckcart;

            sspcanetype = findViewById(R.id.sspcanetype);
            txtdistancetxt = findViewById(R.id.txtdistancetxt);
            edtharvestorcode = findViewById(R.id.edtharvestorcode);
            txtharvetorcodetxt = findViewById(R.id.txtharvetorcodetxt);
            txtharvetortxt = findViewById(R.id.txtharvetortxt);
            txtharvetortypetxt = findViewById(R.id.txtharvetortypetxt);
            txtharvetortypeidtxt = findViewById(R.id.txtharvetortypeidtxt);
            edttransportercode = findViewById(R.id.edttransportercode);
            txttransportercodetxt = findViewById(R.id.txttransportercodetxt);
            txttransportertxt = findViewById(R.id.txttransportertxt);
            txtvehicalnotxt = findViewById(R.id.txtvehicalnotxt);
            sspwirerope = findViewById(R.id.sspwirerope);
            edtbulluckcardmukadamcode = findViewById(R.id.edtbulluckcardmukadamcode);
            txtbulluckcardmukadamcodetxt = findViewById(R.id.txtbulluckcardmukadamcodetxt);
            txtbulluckcardmukadamtxt = findViewById(R.id.txtbulluckcardmukadamtxt);
            mspbulluckcart = findViewById(R.id.mspbulluckcart);
            sspfronttrailer = findViewById(R.id.sspfronttrailer);
            sspbacktrailer = findViewById(R.id.sspbacktrailer);
            txtslipboytxt = findViewById(R.id.txtslipboytxt);
            sspremark = findViewById(R.id.sspremark);

            List<Long> caneType = sspcanetype.getSelectedIds();
            List<KeyPairBoolData> remark = sspremark.getSelectedItems();
            String ndistancesave = txtdistancetxt.getText().toString();
            String slipboyname = txtslipboytxt.getText().toString();

            boolean isValid = true;
            if (caneType.size() == 0) {
                Constant.showToast(getResources().getString(R.string.errorchoosecanetype), activity, R.drawable.ic_wrong);
                isValid = false;
            }

            if (remark.size() == 0) {
                Constant.showToast(getResources().getString(R.string.errorchooseremark), activity, R.drawable.ic_wrong);
                isValid = false;
            }

            if (!sv.checkNumber(ndistancesave)) {
                Constant.showToast(getResources().getString(R.string.errordistancenotfound), activity, R.drawable.ic_wrong);
                isValid = false;
            } else {
                if (ndistancesave.equals("0")) {
                    AppCompatEditText edtdistanceuser = findViewById(R.id.edtdistanceuser);
                    if (vehicleType.equals("3")) {
                        String distanceuser = edtdistanceuser.getText().toString();
                        if (!sv.checkNumber(distanceuser)) {
                            edtdistanceuser.setError(getResources().getString(R.string.errordistancenotfound));
                            isValid = false;
                        } else if (Integer.parseInt(distanceuser) <= 0 || Integer.parseInt(distanceuser) > ngps_distance + 3) {
                            edtdistanceuser.setError(String.format(getResources().getString(R.string.errordistancebetween1tox), MarathiHelper.convertEnglishtoMarathi(String.valueOf(ngps_distance + 3))));
                            isValid = false;
                        } else {
                            ndistancesave = distanceuser;
                            try {
                                job.put("userDistance", "1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (vehicleType.equals("4")) {
                        String distanceuser = edtdistanceuser.getText().toString();
                        if (!sv.checkNumber(distanceuser)) {
                            edtdistanceuser.setError(getResources().getString(R.string.errordistancenotfound));
                            isValid = false;
                        } else if (Integer.parseInt(distanceuser) <= 0 || Integer.parseInt(distanceuser) > ngps_distance + 2) {
                            edtdistanceuser.setError(String.format(getResources().getString(R.string.errordistancebetween1tox), MarathiHelper.convertEnglishtoMarathi(String.valueOf(ngps_distance + 2))));
                            isValid = false;
                        } else {
                            ndistancesave = distanceuser;
                            try {
                                job.put("userDistance", "1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }

            if (!isValid) {
                return;
            }
            try {
                KeyPairBoolData remarkDetails = remark.get(0);
                job.put("ncropId", caneType.get(0));
                job.put("slipboyname", slipboyname);
                job.put("ndistancesave", ndistancesave);
                job.put("nremarkId", remarkDetails.getId());
                job.put("remarkName", remarkDetails.getName());
                job.put("nslipboyId", data[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String vehicalno = txtvehicalnotxt.getText().toString();

            switch (vehicleType) {
                case "1":
                case "2":
                    String harvestorcodepref = txtharvetorcodetxt.getText().toString();
                    String harvestorcode = edtharvestorcode.getText().toString();
                    String harvetorname = txtharvetortxt.getText().toString();
                    String harvetortype = txtharvetortypetxt.getText().toString();
                    String harvetortypeid = txtharvetortypeidtxt.getText().toString();
                    String transportercodepref = txttransportercodetxt.getText().toString();
                    String transportercode = edttransportercode.getText().toString();
                    String transportername = txttransportertxt.getText().toString();

                    if (!sv.checkNumber(harvestorcode) || sv.checkNull(harvestorcodepref) || !harvestorcodepref.replaceAll("[^0-9]", "").equals("" + harvestorcode)) {
                        edtharvestorcode.setError(getResources().getString(R.string.errorwrongharvestorcode));
                        isValid = false;
                    }

                    if (sv.checkNull(harvetorname)) {
                        edtharvestorcode.setError(getResources().getString(R.string.errorwrongharvestorcode));
                        isValid = false;
                    }

                    if (sv.checkNull(harvetortype)) {
                        edtharvestorcode.setError(getResources().getString(R.string.errorwrongharvestortype));
                        isValid = false;
                    }

                    if (sv.checkNull(harvetortypeid)) {
                        edtharvestorcode.setError(getResources().getString(R.string.errorwrongharvestortype));
                        isValid = false;
                    }

                    if (sv.checkNull(transportercode) || sv.checkNull(transportercodepref) || !transportercodepref.replaceAll("[^0-9]", "").equals("" + transportercode)) {
                        edttransportercode.setError(getResources().getString(R.string.errorwrongedttransportercode));
                        isValid = false;
                    }

                    if (sv.checkNull(transportername)) {
                        edttransportercode.setError(getResources().getString(R.string.errorwrongedttransportercode));
                        isValid = false;
                    }

                    if (vehicleType.equals("1")) {
                        List<KeyPairBoolData> wireropeData = sspwirerope.getSelectedItems();
                        if (wireropeData.size() == 0) {
                            Constant.showToast(getResources().getString(R.string.errorselectwirerope), activity, R.drawable.ic_wrong);
                            isValid = false;
                        }
                        if (isValid) {
                            try {
                                KeyPairBoolData wireropeDataItem = wireropeData.get(0);
                                if (!combos.containsKey(String.valueOf(wireropeDataItem.getId()))) {
                                    Constant.showToast(getResources().getString(R.string.errorwireropecombonotallowed), activity, R.drawable.ic_wrong);
                                    isValid = false;
                                } else {
                                    job.put("nseqWeight", combos.get(String.valueOf(wireropeDataItem.getId())));
                                }
                                job.put("nwireropeNo", wireropeDataItem.getId());
                                job.put("wireropeName", wireropeDataItem.getName());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        List<KeyPairBoolData> fronttrailerData = sspfronttrailer.getSelectedItems();
                        List<KeyPairBoolData> backtrailerData = sspbacktrailer.getSelectedItems();
                        if (fronttrailerData.size() == 0) {
                            Constant.showToast(getResources().getString(R.string.errorselectfronttrailer), activity, R.drawable.ic_wrong);
                            isValid = false;
                        }
                        if (backtrailerData.size() == 0) {
                            Constant.showToast(getResources().getString(R.string.errorselectbacktrailer), activity, R.drawable.ic_wrong);
                            isValid = false;
                        }
                        if (isValid) {
                            try {
                                KeyPairBoolData fronttrailerDataItem = fronttrailerData.get(0);
                                KeyPairBoolData backtrailerDataItem = backtrailerData.get(0);
                                String comboKey = fronttrailerDataItem.getId() + "," + backtrailerDataItem.getId();
                                if (!combos.containsKey(comboKey)) {
                                    Constant.showToast(getResources().getString(R.string.errorwireropecombonotallowed), activity, R.drawable.ic_wrong);
                                    isValid = false;
                                } else {
                                    job.put("nseqWeight", combos.get(comboKey));
                                    job.put("ntailorFront", fronttrailerDataItem.getId());
                                    job.put("tailorFrontName", fronttrailerDataItem.getName());
                                    job.put("ntailorBack", backtrailerDataItem.getId());
                                    job.put("tailorBackName", backtrailerDataItem.getName());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    try {
                        job.put("nharvestorId", harvestorcodepref);
                        job.put("harvetorname", harvetorname);
                        job.put("ndistancesave", ndistancesave);
                        job.put("ngadiDokiId", harvetortypeid);
                        job.put("harvetortype", harvetortype);
                        job.put("ntransportorId", transportercodepref);
                        job.put("transportername", transportername);
                        job.put("vvehicleNo", vehicalno);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                case "4":
                    String bulluckcardmukadamcodepref = txtbulluckcardmukadamcodetxt.getText().toString();
                    String bulluckcardmukadamcode = edtbulluckcardmukadamcode.getText().toString();
                    String bulluckcardmukadamname = txtbulluckcardmukadamtxt.getText().toString();
                    List<KeyPairBoolData> bulluckcartData = mspbulluckcart.getSelectedItems();

                    if (!sv.checkNumber(bulluckcardmukadamcode) || sv.checkNull(bulluckcardmukadamcodepref) || !bulluckcardmukadamcodepref.replaceAll("[^0-9]", "").equals("" + bulluckcardmukadamcode)) {
                        edtbulluckcardmukadamcode.setError(getResources().getString(R.string.errorwrongmukadamcode));
                        isValid = false;
                    }

                    if (sv.checkNull(bulluckcardmukadamname)) {
                        edtbulluckcardmukadamcode.setError(getResources().getString(R.string.errorwrongmukadamcode));
                        isValid = false;
                    }

                    int size = bulluckcartData.size();
                    if (size == 0) {
                        Constant.showToast(getResources().getString(R.string.errorselectbulluckcart), activity, R.drawable.ic_wrong);
                        isValid = false;
                    }

                    if (vehicleType.equals("4")) {
                        List<KeyPairBoolData> fronttrailerData = sspfronttrailer.getSelectedItems();
                        List<KeyPairBoolData> backtrailerData = sspbacktrailer.getSelectedItems();
                        if (fronttrailerData.size() == 0) {
                            Constant.showToast(getResources().getString(R.string.errorselectfronttrailer), activity, R.drawable.ic_wrong);
                            isValid = false;
                        }
                        if (backtrailerData.size() == 0) {
                            Constant.showToast(getResources().getString(R.string.errorselectbacktrailer), activity, R.drawable.ic_wrong);
                            isValid = false;
                        }
                        if (isValid) {
                            try {
                                KeyPairBoolData fronttrailerDataItem = fronttrailerData.get(0);
                                KeyPairBoolData backtrailerDataItem = backtrailerData.get(0);
                                String comboKey = fronttrailerDataItem.getId() + "," + backtrailerDataItem.getId();
                                if (!combos.containsKey(comboKey)) {
                                    Constant.showToast(getResources().getString(R.string.errorwireropecombonotallowed), activity, R.drawable.ic_wrong);
                                    isValid = false;
                                } else {
                                    job.put("nseqWeight", combos.get(comboKey));
                                    job.put("ntailorFront", fronttrailerDataItem.getId());
                                    job.put("tailorFrontName", fronttrailerDataItem.getName());
                                    job.put("ntailorBack", backtrailerDataItem.getId());
                                    job.put("tailorBackName", backtrailerDataItem.getName());
                                    job.put("vvehicleNo", vehicalno);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    try {
                        job.put("nbulluckcartMainId", bulluckcardmukadamcodepref);
                        job.put("bulluckcartMainName", bulluckcardmukadamname);
                        Gson gson = new Gson();
                        String bulluckcartJson = gson.toJson(bulluckcartData);
                        job.put("bulluckdata", new JSONArray(bulluckcartJson));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    Constant.showToast(getResources().getString(R.string.errorinvalidvehicaltype), activity, R.drawable.ic_wrong);
                    isValid = false;
                    break;
            }

            if (isValid) {
                try {
                    job = addAlltoJSON(job, new JSONObject(data[3]));
                    AppCompatTextView txtslipno = findViewById(R.id.txtslipno);
                    job.put("slipDate", harvSlipDetailsResponse != null ? harvSlipDetailsResponse.getSlipDate() : "");
                    job.put("nslipNo", txtslipno.getText().toString());
                    job.put("tokenno", tokenno);
                    job.put("vehicalTypeName", selectedVehical.getName());
                    key = new String[]{getResources().getString(R.string.prefharvslipdata)};
                    value = new String[]{job.toString()};
                    cf.putSharedPrefValue(key, value, activity);
                    action = getResources().getString(R.string.actionsavews);
                    Call<WeightSlipResponse> call2 = apiInterface.savews(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                    RetrofitHandler<WeightSlipResponse> reqfarmer = new RetrofitHandler<>();
                    reqfarmer.handleRetrofit(call2, retrofitResponse, RequestEnum.SAVEWS, servlet, activity, versioncode, vehicleType, txtslipno.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private JSONObject addAlltoJSON(JSONObject job, JSONObject jsonObject) {
        Iterator<String> itr = jsonObject.keys();
        while (itr.hasNext()) {
            String key = itr.next();
            try {
                job.put(key, jsonObject.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return job;
    }

    @Override
    public void onRefreshComplete() {
        initAllData();
    }
}