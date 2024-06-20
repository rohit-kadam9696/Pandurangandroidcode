package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
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
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneHarvestingPlanReasonResponse;
import com.twd.chitboyapp.spsskl.pojo.HarvestingPlanFarmerResponse;
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

public class HarvPlanIndviualActivity extends AppCompatActivity implements RetrofitResponse, SingleReturn {

    String serverFarmerCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harv_plan_indviual);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Activity activity = HarvPlanIndviualActivity.this;

        AppCompatEditText edtfarmercode;
        AppCompatButton btnprev, btnsubmit;
        AppCompatImageButton btbsearch;
        SingleSpinnerSearch sspreason, sspplotno;
        sspreason = findViewById(R.id.sspreason);
        sspplotno = findViewById(R.id.sspplotno);
        edtfarmercode = findViewById(R.id.edtfarmercode);
        btbsearch = findViewById(R.id.btnsearch);
        btnprev = findViewById(R.id.btnprev);
        btnsubmit = findViewById(R.id.btnsubmit);


        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspreason, activity, getResources().getString(R.string.selectreason));
        ssh.initSingle(sspplotno, activity, getResources().getString(R.string.selectplot));

        ssh.setSingleItems(sspreason, new ArrayList<>(), DataSetter.REASON);
        ssh.setSingleItems(sspplotno, new ArrayList<>(), DataSetter.PLOTNO);

        String action = getResources().getString(R.string.actionharvplanreason);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        String servlet = getResources().getString(R.string.servletcaneharvestingplan);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<CaneHarvestingPlanReasonResponse> call2 = apiInterface.reasonData(action, "{}", cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<CaneHarvestingPlanReasonResponse> reqharvestingplan = new RetrofitHandler<>();
        reqharvestingplan.handleRetrofit(call2, HarvPlanIndviualActivity.this, RequestEnum.HAVRPLANDATAREASON, servlet, activity, versioncode);

        edtfarmercode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String farmerCode = edtfarmercode.getText().toString();
                    if (!("F" + farmerCode).equals(serverFarmerCode)) {
                        loadPlot();
                    }
                }
                return true;
            }
        });

        edtfarmercode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String farmerCode = edtfarmercode.getText().toString();
                    if (!("F" + farmerCode).equals(serverFarmerCode)) {
                        loadPlot();
                    }
                }
            }
        });

        btbsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String farmerCode = edtfarmercode.getText().toString();
                if (!("F" + farmerCode).equals(serverFarmerCode)) {
                    loadPlot();
                }
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
                AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
                SingleSpinnerSearch sspplotno, sspreason;
                sspplotno = findViewById(R.id.sspplotno);
                sspreason = findViewById(R.id.sspreason);

                String farmerCode = edtfarmercode.getText().toString();
                List<Long> plotno = sspplotno.getSelectedIds();
                List<Long> reason = sspreason.getSelectedIds();

                boolean isValid = true;
                ServerValidation sv = new ServerValidation();
                if (!sv.checkNumber(farmerCode)) {
                    edtfarmercode.setError(getResources().getString(R.string.errorwrongfarmer));
                    isValid = false;
                }

                if (reason.size() != 1) {
                    Constant.showToast(getResources().getString(R.string.errorchoosereason), activity, R.drawable.ic_wrong);
                    isValid = false;
                }

                if (plotno.size() != 1) {
                    Constant.showToast(getResources().getString(R.string.errorchoosePlot), activity, R.drawable.ic_wrong);
                    isValid = false;
                }

                String[] data = cf.getSharedPrefValue(activity, key, value);
                JSONObject job = new JSONObject();
                if (isValid) {
                    try {
                        job.put("yearCode", data[2]);
                        job.put("farmerCode", "F" + farmerCode);
                        job.put("nplotNo", String.valueOf(plotno.get(0)));
                        job.put("nreasonId", String.valueOf(reason.get(0)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                        return;
                    }
                    String action = getResources().getString(R.string.actionsaveharvplanspl);

                    APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                    String versioncode = cf.getVersionCode();
                    Call<ActionResponse> call2 = apiInterface.actionCaneHarvestingPlan(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                    RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
                    reqfarmer.handleRetrofit(call2, HarvPlanIndviualActivity.this, RequestEnum.HAVRPLANSTARTSAVESPL, servlet, activity, versioncode);
                }
            }
        });
    }

    private void loadPlot() {
        Activity activity = HarvPlanIndviualActivity.this;
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        ServerValidation sv = new ServerValidation();
        AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
        String farmerCode = edtfarmercode.getText().toString();
        if (!sv.checkNumber(farmerCode)) {
            edtfarmercode.setError(getResources().getString(R.string.errorwrongfarmer));
            return;
        }
        String action = getResources().getString(R.string.actionharvplanfarmerdata);
        JSONObject job = new JSONObject();
        try {
            job.put("yearCode", data[2]);
            job.put("farmerCode", "F" + farmerCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String servlet = getResources().getString(R.string.servletcaneharvestingplan);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);

        String versioncode = cf.getVersionCode();
        Call<HarvestingPlanFarmerResponse> call2 = apiInterface.farmerData(action, job.toString(), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<HarvestingPlanFarmerResponse> reqharvestingplan = new RetrofitHandler<>();
        reqharvestingplan.handleRetrofit(call2, HarvPlanIndviualActivity.this, RequestEnum.HAVRPLANDATAFARMERDATA, servlet, activity, versioncode);
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
        if (requestCaller == RequestEnum.HAVRPLANDATAREASON) {
            CaneHarvestingPlanReasonResponse caneHarvestingPlanReasonResponse = (CaneHarvestingPlanReasonResponse) response.body();
            List<KeyPairBoolData> reasonList;
            if (caneHarvestingPlanReasonResponse != null) {
                reasonList = caneHarvestingPlanReasonResponse.getReasonList();
            } else {
                reasonList = new ArrayList<>();
            }

            SingleSpinnerSearch sspreason;
            sspreason = findViewById(R.id.sspreason);
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.singleReturn = this;
            ssh.setSingleItems(sspreason, reasonList, DataSetter.REASON);
        } else if (requestCaller == RequestEnum.HAVRPLANDATAFARMERDATA) {
            HarvestingPlanFarmerResponse farmerResponse = (HarvestingPlanFarmerResponse) response.body();
            serverFarmerCode = farmerResponse.getFarmerCode();
            String farmerName = farmerResponse.getFarmerName();
            List<Long> plotlist = farmerResponse.getPlotList();
            AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
            AppCompatTextView txtfarmernametxt = findViewById(R.id.txtfarmernametxt);
            txtfarmernametxt.setText(farmerName);
            ArrayList<KeyPairBoolData> keyPairBoolDataArrayList = new ArrayList<>();
            int plotSize = plotlist.size();
            for (int i = 0; i < plotSize; i++) {
                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                keyPairBoolData.setId(plotlist.get(i));
                keyPairBoolData.setName(String.valueOf(plotlist.get(i)));
                keyPairBoolDataArrayList.add(keyPairBoolData);
            }
            SingleSpinnerSearch sspplotno;
            sspplotno = findViewById(R.id.sspplotno);
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.singleReturn = this;
            ssh.setSingleItems(sspplotno, keyPairBoolDataArrayList, DataSetter.PLOTNO);
            if (edtfarmercode.hasFocus()) {
                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(edtfarmercode, activity);
                edtfarmercode.clearFocus();
            }
        } else if (requestCaller == RequestEnum.HAVRPLANSTARTSAVESPL) {
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
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }
}
