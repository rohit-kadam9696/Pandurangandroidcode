package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.adapter.FarmerTonnageAdapter;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.FarmerTonnageResponse;
import com.twd.chitboyapp.spsskl.pojo.NameData;
import com.twd.chitboyapp.spsskl.pojo.NameListResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HarvFarmerTonnageActivity extends AppCompatActivity implements RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harv_farmer_tonnage);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        String[] key = new String[]{getResources().getString(R.string.prefseason)};
        String[] value = new String[]{""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(HarvFarmerTonnageActivity.this, key, value);
        AppCompatTextView txtseasonval = findViewById(R.id.txtseasonval);
        txtseasonval.setText(sharedPrefval[0]);

        AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
        AppCompatAutoCompleteTextView actfarmername = findViewById(R.id.actfarmername);
        AppCompatImageButton imgsearch = findViewById(R.id.imgsearch);
        AppCompatImageButton imgnamesearch = findViewById(R.id.imgnamesearch);
        AppCompatButton btntonnagedetails = findViewById(R.id.btntonnagedetails);

        ServerValidation sv = new ServerValidation();
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String farmercode = edtfarmercode.getText().toString();
                if (sv.checkNumber(farmercode)) {
                    loadReport(farmercode);
                } else {
                    edtfarmercode.setError(getResources().getString(R.string.errorfarmernotfound));
                }

            }
        });

        imgnamesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String farmername = actfarmername.getText().toString();
                if (sv.checklesslength(farmername, 4)) {
                    actfarmername.setError(getResources().getString(R.string.errorentermin3character));
                } else {
                    actfarmername.setError(null);
                    loadNames(farmername);
                }

            }
        });

        actfarmername.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = actfarmername.getText().toString();
                String[] code = name.split("  ");
                edtfarmercode.setText(code[0]);
                actfarmername.setText(code[1]);
                imgsearch.performClick();
            }
        });

        btntonnagedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String farmercode = edtfarmercode.getText().toString();
                if (sv.checkNumber(farmercode)) {
                    Intent intent = new Intent(HarvFarmerTonnageActivity.this, HarvFarmerTonnageDetailActivity.class);
                    intent.putExtra("farmercode", farmercode);
                    startActivity(intent);
                } else {
                    edtfarmercode.setError(getResources().getString(R.string.errorfarmernotfound));
                }
            }
        });
    }

    private void loadNames(String farmername) {

        Activity activity = HarvFarmerTonnageActivity.this;
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);
        String action = getResources().getString(R.string.actionfarmername);

        JSONObject job = new JSONObject();
        try {
            job.put("name", farmername);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String servlet = getResources().getString(R.string.servletharvdata);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        RetrofitHandler<NameListResponse> handler = new RetrofitHandler<>();
        Call<NameListResponse> call2 = apiInterface.nameSearch(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
        handler.handleRetrofit(call2, HarvFarmerTonnageActivity.this, RequestEnum.FARMERNAMELIST, servlet, activity, versioncode);


    }

    private void loadReport(String farmercode) {
        Activity activity = HarvFarmerTonnageActivity.this;
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionfarmertonnage);

        try {
            job.put("nfarmercode", farmercode);
            job.put("vyearCode", sharedPrefval[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String servlet = activity.getResources().getString(R.string.servletharvdata);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        RetrofitHandler<FarmerTonnageResponse> handler = new RetrofitHandler<>();
        Call<FarmerTonnageResponse> call2 = apiInterface.farmerTonnage(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
        handler.handleRetrofit(call2, HarvFarmerTonnageActivity.this, RequestEnum.FARMERTONNAGE, servlet, activity, versioncode);

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
        if (requestCaller == RequestEnum.FARMERTONNAGE) {
            FarmerTonnageResponse farmerTonnageResponse = (FarmerTonnageResponse) response.body();
            AppCompatAutoCompleteTextView actfarmername = findViewById(R.id.actfarmername);

            actfarmername.setText(farmerTonnageResponse.getFarmerName());
            FarmerTonnageAdapter farmerTonnageAdapter = new FarmerTonnageAdapter(farmerTonnageResponse.getFarmerTonnages(), activity);
            RecyclerView rcplotton = findViewById(R.id.rcplotton);
            GridLayoutManager mLayoutManager1 = new GridLayoutManager(activity, 1);
            rcplotton.setLayoutManager(mLayoutManager1);
            rcplotton.setItemAnimator(new DefaultItemAnimator());
            rcplotton.setAdapter(farmerTonnageAdapter);

            AppCompatButton btntonnagedetails = findViewById(R.id.btntonnagedetails);
            if (farmerTonnageResponse.getFarmerTonnages() != null && !farmerTonnageResponse.getFarmerTonnages().isEmpty())
                btntonnagedetails.setVisibility(View.VISIBLE);
            else btntonnagedetails.setVisibility(View.GONE);
        } else if (requestCaller == RequestEnum.FARMERNAMELIST) {
            NameListResponse nameResponse = (NameListResponse) response.body();
            AppCompatAutoCompleteTextView actfarmername = findViewById(R.id.actfarmername);
            ConstantFunction cf = new ConstantFunction();
            List<NameData> nameDataList = nameResponse.getNameDataList();
            int size = nameDataList.size();
            ArrayList<String> namearr = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                NameData nameData = nameDataList.get(i);
                namearr.add(nameData.getCode() + "  " + nameData.getName());
            }
            actfarmername.setAdapter(null);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.dropdown, namearr);
            actfarmername.setAdapter(adapter);
            actfarmername.showDropDown();
            View view = getCurrentFocus();
            if (view != null) cf.hideKeyboard(view, activity);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}