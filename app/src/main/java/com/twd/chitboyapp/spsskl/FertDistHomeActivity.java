package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.ScannerHandler;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.pojo.FarmerResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

public class FertDistHomeActivity extends AppCompatActivity implements RetrofitResponse, View.OnClickListener {

    String entryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fert_dist_home);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        AppCompatTextView txtlocation = findViewById(R.id.txtlocation);
        String[] key = new String[]{getResources().getString(R.string.preflocationname)};
        String[] value = new String[]{""};
        ConstantFunction cf = new ConstantFunction();
        Activity activity = FertDistHomeActivity.this;
        String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

        txtlocation.setText(getResources().getString(R.string.location) + " : " + sharedPrefval[0]);


        AppCompatButton btnreset = findViewById(R.id.btnreset);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        AppCompatButton btnnext = findViewById(R.id.btnnext);

        btnreset.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
        btnnext.setOnClickListener(this);

        AppCompatImageView imgscanfarmer = findViewById(R.id.imgscanfarmer);

        imgscanfarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScannerHandler scannerHandler = new ScannerHandler(FertDistHomeActivity.this, barcodeLauncher);
                scannerHandler.openScanner();
            }
        });

        AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
        edtfarmercode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                entryType = "1";
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Constant.showToast(getResources().getString(R.string.cancelscan), FertDistHomeActivity.this, R.drawable.ic_wrong);
        } else {
            AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
            edtfarmercode.setText(result.getContents().replace("F", ""));
            entryType = "2";
        }
    });

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnreset) {
            AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
            edtfarmercode.setText("");
        } else if (id == R.id.btnsubmit) {
            ServerValidation sv = new ServerValidation();
            AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
            String farmerCode = edtfarmercode.getText().toString();
            if (!sv.checkNumber(farmerCode)) {
                Constant.showToast(getResources().getString(R.string.errorwrongfarmer), FertDistHomeActivity.this, R.drawable.ic_wrong);
                return;
            }
            String entityUnitId = "F" + farmerCode;
            // check online
            JSONObject job = new JSONObject();
            try {
                String action = getResources().getString(R.string.actionfarmerrequest);
                Activity activity = FertDistHomeActivity.this;
                ConstantFunction cf = new ConstantFunction();
                String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
                String[] value = new String[]{"", "0"};
                String[] data = cf.getSharedPrefValue(activity, key, value);
                job.put("nentityUniId", entityUnitId);
                String servlet = activity.getResources().getString(R.string.servletfarmer);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                Call<FarmerResponse> call2 = apiInterface.getOnlineFarmer(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                RetrofitHandler<FarmerResponse> otphandler = new RetrofitHandler<>();
                otphandler.handleRetrofit(call2, FertDistHomeActivity.this, RequestEnum.ONLINEFARMER, servlet, activity, versioncode, entityUnitId);
                View v = getCurrentFocus();
                if (v != null) cf.hideKeyboard(v, activity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.btnnext) {
            AppCompatTextView txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
            AppCompatTextView txtfarmernametxt = findViewById(R.id.txtfarmernametxt);
            AppCompatTextView txtyeartxt = findViewById(R.id.txtyeartxt);
            AppCompatTextView txtdatetxt = findViewById(R.id.txtdatetxt);
            String entityUnitId = txtfarmercodetxt.getText().toString();
            String farmerName = txtfarmernametxt.getText().toString();
            String yearCode = txtyeartxt.getText().toString();
            String date = txtdatetxt.getText().toString();
            Intent intent = new Intent(FertDistHomeActivity.this, FertPlantationDetailsActivity.class);
            intent.putExtra("entityUnitId", entityUnitId);
            intent.putExtra("farmerName", farmerName);
            intent.putExtra("yearCode", yearCode);
            intent.putExtra("date", date);
            startActivity(intent);
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
        if (requestCaller == RequestEnum.ONLINEFARMER) {
            ConstantFunction cf = new ConstantFunction();
            String yearCode = cf.getYearCode(new Date(), 4, 3);

            ConstraintLayout llfarmerinfo = findViewById(R.id.llfarmerinfo);
            AppCompatButton btnnext = findViewById(R.id.btnnext);
            AppCompatTextView txtdatetxt = findViewById(R.id.txtdatetxt);
            AppCompatTextView txtyeartxt = findViewById(R.id.txtyeartxt);
            AppCompatTextView txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
            AppCompatTextView txtfarmernametxt = findViewById(R.id.txtfarmernametxt);

            FarmerResponse farmerResponse = (FarmerResponse) response.body();
            EntityMasterDetail entityMasterDetail = farmerResponse.getEntityMasterDetail();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            llfarmerinfo.setVisibility(View.VISIBLE);
            btnnext.setVisibility(View.VISIBLE);
            txtdatetxt.setText(sdf.format(new Date()));
            txtyeartxt.setText(yearCode);
            txtfarmercodetxt.setText(entityMasterDetail.getNentityUniId());
            txtfarmernametxt.setText(entityMasterDetail.getVentityNameLocal());
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}