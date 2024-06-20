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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.LocationUpdatetor;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.ScannerHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.BackPress;
import com.twd.chitboyapp.spsskl.enums.FarmerList;
import com.twd.chitboyapp.spsskl.enums.PlantListCaller;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.LocationUpdateListener;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.CompletePlotResponse;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.pojo.FarmerResponse;
import com.twd.chitboyapp.spsskl.pojo.FarmerSugarResponse;
import com.twd.chitboyapp.spsskl.pojo.SugarSaleSavePrintResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Response;

public class OnlineFarmerActivity extends AppCompatActivity implements View.OnClickListener, RetrofitResponse, LocationUpdateListener {

    String mnu_id;
    String entryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_farmer);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Intent intent = getIntent();
        if (intent.hasExtra("mnu_id")) {
            mnu_id = intent.getStringExtra("mnu_id");
        } else {
            finish();
        }

        AppCompatTextView txtlatlongacc = findViewById(R.id.txtlatlongacc);
        txtlatlongacc.setVisibility(View.GONE);
        AppCompatButton btnreset = findViewById(R.id.btnreset);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        AppCompatButton btnnondpermissionlist = findViewById(R.id.btnnondpermissionlist);

        btnreset.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
        btnnondpermissionlist.setOnClickListener(this);

        if (!mnu_id.equals("3")) {
            btnnondpermissionlist.setVisibility(View.GONE);
        }
        Activity activity = this;
        if (mnu_id.equals("57")) {
            txtlatlongacc.setVisibility(View.VISIBLE);
            if (Constant.locationUpdatetor != null) {
                Constant.locationUpdatetor.updateLocationUpdatetor(activity);
                Constant.locationUpdatetor.locationCaller = this;
            } else {
                startLocation(activity);
            }
        }
        AppCompatImageView imgscanfarmer = findViewById(R.id.imgscanfarmer);

        imgscanfarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScannerHandler scannerHandler = new ScannerHandler(OnlineFarmerActivity.this, barcodeLauncher);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LocationUpdatetor.REQUEST_CHECK_SETTINGS) {
                Constant.locationUpdatetor.startLocationButtonClick();
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == LocationUpdatetor.REQUEST_CHECK_SETTINGS) {
                Constant.locationUpdatetor.stopLocationUpdates();
            }
        }
    }

    @Override
    public void onLocationUpdate(double lats, double longs, double accuracy) {
        Constant.locationUpdatetor.updateUIData(OnlineFarmerActivity.this, lats, longs, accuracy);
    }

    private void startLocation(Activity activity) {
        Constant.locationUpdatetor = new LocationUpdatetor(activity);
        Constant.locationUpdatetor.locationCaller = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constant.locationUpdatetor != null)
            Constant.locationUpdatetor.startLocationButtonClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // locationUpdatetor.stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //locationUpdatetor.stopLocationUpdates();
    }


    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Constant.showToast(getResources().getString(R.string.cancelscan), OnlineFarmerActivity.this, R.drawable.ic_wrong);
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
                Constant.showToast(getResources().getString(R.string.errorwrongfarmer), OnlineFarmerActivity.this, R.drawable.ic_wrong);
                return;
            }
            String entityUnitId = "F" + farmerCode;
            if (mnu_id.equals("3")) {
                DBHelper dbHelper = new DBHelper(OnlineFarmerActivity.this);
                EntityMasterDetail entityMasterDetail = dbHelper.getEntityById(entityUnitId);
                if (entityMasterDetail != null && entityMasterDetail.getNentityUniId() != null && entityMasterDetail.getNentityUniId().equals(entityUnitId)) {
                    Constant.showToast(getResources().getString(R.string.erroralreadyhaspermission), OnlineFarmerActivity.this, R.drawable.ic_wrong);
                    return;
                }
            }
            // check online
            JSONObject job = new JSONObject();
            try {
                if (mnu_id.equals("3")) {
                    String action = getResources().getString(R.string.actionfarmerrequest);
                    Activity activity = OnlineFarmerActivity.this;
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
                    otphandler.handleRetrofit(call2, OnlineFarmerActivity.this, RequestEnum.ONLINEFARMER, servlet, activity, versioncode);
                } else if (mnu_id.equals("24")) {
                    String action = getResources().getString(R.string.actionplotbyfarmer);
                    Activity activity = OnlineFarmerActivity.this;
                    ConstantFunction cf = new ConstantFunction();
                    String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
                    String[] value = new String[]{"", "0", ""};
                    String[] data = cf.getSharedPrefValue(activity, key, value);
                    job.put("nentityUniId", entityUnitId);
                    job.put("yearCode", data[2]);
                    String servlet = activity.getResources().getString(R.string.servletharvdata);
                    APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                    String versioncode = cf.getVersionCode();
                    Call<CompletePlotResponse> call2 = apiInterface.getHarvPlotDetails(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                    RetrofitHandler<CompletePlotResponse> otphandler = new RetrofitHandler<>();
                    otphandler.handleRetrofit(call2, OnlineFarmerActivity.this, RequestEnum.HARVPLOTDETAILS, servlet, activity, versioncode);
                } else if (mnu_id.equals("4")) {
                    Intent intent = new Intent(OnlineFarmerActivity.this, NondConfirmListActivity.class);
                    intent.putExtra("caller", PlantListCaller.VIEWNOND);
                    intent.putExtra("farmercode", farmerCode);
                    NondConfirmListActivity.count = 0;
                    startActivity(intent);
                } else if (mnu_id.equals("57")) {
                    String action = getResources().getString(R.string.actionsugarfarmerinfo);
                    Activity activity = OnlineFarmerActivity.this;
                    ConstantFunction cf = new ConstantFunction();
                    String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
                    String[] value = new String[]{"", "0", ""};
                    String[] data = cf.getSharedPrefValue(activity, key, value);
                    job.put("nentityUniId", entityUnitId);
                    job.put("yearCode", data[2]);
                    String servlet = activity.getResources().getString(R.string.servletsugarsale);
                    APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                    String versioncode = cf.getVersionCode();
                    Call<FarmerSugarResponse> call2 = apiInterface.getSugarDetails(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                    RetrofitHandler<FarmerSugarResponse> handleRetrofit = new RetrofitHandler<>();
                    handleRetrofit.handleRetrofit(call2, OnlineFarmerActivity.this, RequestEnum.SUGARFARMERDETAILS, servlet, activity, versioncode);
                } else if (mnu_id.equals("58")) {
                    String action = getResources().getString(R.string.actionsugarfarmerreprint);
                    Activity activity = OnlineFarmerActivity.this;
                    ConstantFunction cf = new ConstantFunction();
                    String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
                    String[] value = new String[]{"", "0", ""};
                    String[] data = cf.getSharedPrefValue(activity, key, value);
                    job.put("nentityUniId", entityUnitId);
                    job.put("yearCode", data[2]);
                    String servlet = activity.getResources().getString(R.string.servletsugarsale);
                    APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                    String versioncode = cf.getVersionCode();
                    Call<SugarSaleSavePrintResponse> call2 = apiInterface.savePrintSugarSale(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                    RetrofitHandler<SugarSaleSavePrintResponse> reprintinfo = new RetrofitHandler<>();
                    reprintinfo.handleRetrofit(call2, OnlineFarmerActivity.this, RequestEnum.SUGARREPRINT, servlet, activity, versioncode);
                } else {
                    Constant.showToast(String.format(getResources().getString(R.string.commingsoonorupdateapp), getResources().getString(R.string.sadar)), OnlineFarmerActivity.this, R.drawable.ic_info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.btnnondpermissionlist) {
            Intent intent = new Intent(OnlineFarmerActivity.this, FarmerRequestListActivity.class);
            intent.putExtra("caller", FarmerList.BYCHITBOY);
            startActivity(intent);
        }
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.ONLINEFARMER) {
            FarmerResponse farmerResponse = (FarmerResponse) response.body();
            Intent intent = new Intent(OnlineFarmerActivity.this, FarmerDetailsActivity.class);
            intent.putExtra("farmerResponse", (Serializable) farmerResponse.getEntityMasterDetail());
            startActivity(intent);
        } else if (requestCaller == RequestEnum.HARVPLOTDETAILS) {
            CompletePlotResponse completePlotResponse = (CompletePlotResponse) response.body();
            Intent intent = new Intent(OnlineFarmerActivity.this, CompletePlotActivity.class);
            intent.putExtra("completePlotResponse", (Serializable) completePlotResponse);
            intent.putExtra("isclose", "2");
            startActivity(intent);
        } else if (requestCaller == RequestEnum.SUGARFARMERDETAILS) {
            FarmerSugarResponse farmerSugarResponse = (FarmerSugarResponse) response.body();
            Intent intent = null;
            if (farmerSugarResponse.getBlocked().equals("N")) {
                intent = new Intent(OnlineFarmerActivity.this, SugarSaleActivity.class);
                intent.putExtra("employeeName", farmerSugarResponse.getVfullName());
                intent.putExtra("entryType", entryType);
            } else {
                intent = new Intent(OnlineFarmerActivity.this, SugarWithdrawActivity.class);
            }
            intent.putExtra("farmerSugarResponse", (Serializable) farmerSugarResponse);
            startActivity(intent);
        } else if (requestCaller == RequestEnum.SUGARREPRINT) {
            SugarSaleSavePrintResponse actionResponse = (SugarSaleSavePrintResponse) response.body();
            Intent intent = new Intent(OnlineFarmerActivity.this, SugarReceiptReprintActivity.class);
            intent.putExtra("head", getResources().getString(R.string.reprit));
            intent.putExtra("html", actionResponse.getHtmlContent());
            intent.putExtra("backpress", BackPress.REPRINT);
            startActivity(intent);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

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
    public void onBackPressed() {
        super.onBackPressed();
        if (Constant.locationUpdatetor != null)
            Constant.locationUpdatetor.stopLocationUpdates();
        Intent intent = new Intent(OnlineFarmerActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}