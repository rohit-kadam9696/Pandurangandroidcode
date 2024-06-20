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
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.CaneSampleMaster;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.DigitProcessor;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneSamplePlantationData;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Response;

public class CaneSampleActivity extends AppCompatActivity implements RetrofitResponse, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cane_sample);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        AppCompatButton btnprev = findViewById(R.id.btnprev);


        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        btnsubmit.setOnClickListener(this);
        btnprev.setOnClickListener(this);

        Intent intent = getIntent();
        Activity activity = this;
        if (intent.hasExtra("canedataresponse")) {
            CaneSamplePlantationData caneSamplePlantationData = (CaneSamplePlantationData) intent.getSerializableExtra("canedataresponse");
            CaneSampleMaster caneSampleMaster = new CaneSampleMaster();
            caneSampleMaster.setMasterData(activity, caneSamplePlantationData);
        } else {
            finish();
        }

        DigitProcessor digitProcessor = new DigitProcessor();
        digitProcessor.processDigit(2, 2, "_", ".", R.id.edtpole, activity, R.id.edtrecovery);
        digitProcessor.processDigit(2, 2, "_", ".", R.id.edtbrix, activity, R.id.edtpole);
        digitProcessor.processDigit(2, 2, "_", ".", R.id.edtrecovery, activity, null);

        AppCompatEditText edtbrix, edtpole;
        AppCompatTextView edtpurity;
        edtbrix = findViewById(R.id.edtbrix);
        edtpole = findViewById(R.id.edtpole);
        edtpurity = findViewById(R.id.edtpurity);
        edtbrix.performClick();

        edtbrix.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                calculatePurity(edtbrix, edtpole, edtpurity);
            }
        });

        edtpole.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                calculatePurity(edtbrix, edtpole, edtpurity);
            }
        });
    }

    private void calculatePurity(AppCompatEditText edtbrix, AppCompatEditText edtpole, AppCompatTextView edtpurity) {
        String pole = edtpole.getText().toString();
        String brix = edtbrix.getText().toString();

        ServerValidation sv = new ServerValidation();
        if (sv.checkFloat(pole) && sv.checkFloat(brix)) {
            double purity = Double.parseDouble(pole) * 100 / Double.parseDouble(brix);
            DecimalFormat df = new DecimalFormat("#0.00");

            if (purity >= 100) {
                Constant.showToast(String.format(getResources().getString(R.string.errorwrongpurity), df.format(purity)), CaneSampleActivity.this, R.drawable.ic_wrong);
            } else {
                edtpurity.setText(df.format(purity));
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
        if (requestCaller == RequestEnum.SAVECANE) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.successfullysaved), activity, R.drawable.ic_accept);
                finish();
            }
        }

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnsubmit) {
            AppCompatEditText edtbrix, edtpole, edtrecovery;
            AppCompatTextView txtplotnotxt, edtpurity;
            edtbrix = findViewById(R.id.edtbrix);
            edtpole = findViewById(R.id.edtpole);
            edtpurity = findViewById(R.id.edtpurity);
            edtrecovery = findViewById(R.id.edtrecovery);
            txtplotnotxt = findViewById(R.id.txtplotnotxt);

            boolean isValid = true;
            Activity activity = this;

            String brix = edtbrix.getText().toString();
            String pole = edtpole.getText().toString();
            String purity = edtpurity.getText().toString();
            String recovery = edtrecovery.getText().toString();
            String plotno = txtplotnotxt.getText().toString();

            ServerValidation sv = new ServerValidation();
            if (!sv.checkFloat(brix)) {
                edtbrix.setError(getResources().getString(R.string.errorenterbrix));
                isValid = false;
            }
            if (!sv.checkFloat(pole)) {
                edtpole.setError(getResources().getString(R.string.errorenterpole));
                isValid = false;
            }
            if (!sv.checkFloat(purity)) {
                Constant.showToast(getResources().getString(R.string.errorenterpurity), activity, R.drawable.ic_wrong);
                isValid = false;
            } else {
                double purityd = Double.parseDouble(purity);
                if (purityd >= 100) {
                    Constant.showToast(String.format(getResources().getString(R.string.errorwrongpurity), purity), CaneSampleActivity.this, R.drawable.ic_wrong);
                    isValid = false;
                }
            }
            if (!sv.checkFloat(recovery)) {
                edtrecovery.setError(getResources().getString(R.string.errorenterrecovery));
                isValid = false;
            }

            if (isValid) {
                JSONObject job = new JSONObject();
                String action = getResources().getString(R.string.actionfarmerrequestsave);
                ConstantFunction cf = new ConstantFunction();
                String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
                String[] value = new String[]{"", "0", ""};
                String[] data = cf.getSharedPrefValue(activity, key, value);
                try {
                    job.put("nplotNo", plotno);
                    job.put("vyearId", data[2]);
                    job.put("nbrix", brix);
                    job.put("npole", pole);
                    job.put("npurity", purity);
                    job.put("nrecovery", recovery);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String servlet = getResources().getString(R.string.servletcanesample);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                Call<ActionResponse> call2 = apiInterface.saveCaneSample(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
                reqfarmer.handleRetrofit(call2, CaneSampleActivity.this, RequestEnum.SAVECANE, servlet, activity, versioncode);
            }
        }

    }
}