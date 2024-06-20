package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

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
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.PlantListCaller;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RefreshComplete;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.CompletePlotResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerListener;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SelectFarmerActivity extends AppCompatActivity implements RefreshComplete, RetrofitResponse {

    HashMap<String, Integer> position = null;
    List<KeyPairBoolData> farmerList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_farmer);
        Intent intent = getIntent();
        String mnu_id = intent.getStringExtra("mnu_id");
        String rujtype;
        if (intent.hasExtra("rujtype")) {
            rujtype = intent.getStringExtra("rujtype");
        } else {
            rujtype = "";
        }

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
        AppCompatImageView imgscanfarmer = findViewById(R.id.imgscanfarmer);
        SingleSpinnerSearch spfarmername = findViewById(R.id.spfarmername);
        AppCompatButton btnreset = findViewById(R.id.btnreset);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);

        Activity activity = SelectFarmerActivity.this;
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.initSingle(spfarmername, activity, getResources().getString(R.string.selectfarmerhint));

        edtfarmercode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus)
                    selectFarmer();
            }
        });

        edtfarmercode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edtfarmercode.clearFocus();
                    ConstantFunction cf = new ConstantFunction();
                    cf.hideKeyboard(edtfarmercode, SelectFarmerActivity.this);
                    return true;
                }
                return false;
            }
        });

        initSingleData();

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtfarmercode.setText("");
                spfarmername.clearAll();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnsubmit.setEnabled(false);
                if (edtfarmercode.hasFocus()) {
                    edtfarmercode.clearFocus();
                }
                String farmercode = edtfarmercode.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (sv.checkNumber(farmercode)) {
                    Intent intent = null;
                    switch (mnu_id) {
                        case "1":
                            intent = new Intent(SelectFarmerActivity.this, FarmerInfoActivity.class);
                            break;
                        case "2":
                            intent = new Intent(SelectFarmerActivity.this, LaganNondActivity.class);
                            break;
                        case "5":
                            intent = new Intent(SelectFarmerActivity.this, NondConfirmListActivity.class);
                            intent.putExtra("rujtype", rujtype);
                            intent.putExtra("caller", PlantListCaller.NONDCONFIRM);
                            NondConfirmListActivity.count = 0;
                            break;
                        case "4":
                            intent = new Intent(SelectFarmerActivity.this, NondConfirmListActivity.class);
                            intent.putExtra("caller", PlantListCaller.VIEWNOND);
                            NondConfirmListActivity.count = 0;
                            break;
                        case "24":
                            JSONObject job = new JSONObject();
                            String action = getResources().getString(R.string.actionplotbyfarmer);
                            ConstantFunction cf = new ConstantFunction();
                            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
                            String[] value = new String[]{"", "0", ""};
                            String[] data = cf.getSharedPrefValue(activity, key, value);
                            try {
                                job.put("nentityUniId", "F" + farmercode);
                                job.put("yearCode", data[2]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String servlet = activity.getResources().getString(R.string.servletharvdata);
                            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                            String versioncode = cf.getVersionCode();
                            Call<CompletePlotResponse> call2 = apiInterface.getHarvPlotDetails(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                            RetrofitHandler<CompletePlotResponse> otphandler = new RetrofitHandler<>();
                            otphandler.handleRetrofit(call2, SelectFarmerActivity.this, RequestEnum.HARVPLOTDETAILS, servlet, activity, versioncode);

                            break;
                        default:
                            Constant.showToast(String.format(getResources().getString(R.string.commingsoonorupdateapp), getResources().getString(R.string.sadar)), activity, R.drawable.ic_info);
                            break;
                    }
                    if (intent != null) {
                        intent.putExtra("farmercode", farmercode);
                        startActivity(intent);
                    }
                } else {
                    Constant.showToast(getResources().getString(R.string.errorfarmernotselected), activity, R.drawable.ic_info);
                }
                btnsubmit.setEnabled(true);
            }
        });

        imgscanfarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScannerHandler scannerHandler = new ScannerHandler(SelectFarmerActivity.this, barcodeLauncher);
                scannerHandler.openScanner();
            }
        });
    }

    private void selectFarmer() {
        AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
        SingleSpinnerSearch spfarmername = findViewById(R.id.spfarmername);
        if (position != null) {
            String farmercode = edtfarmercode.getText().toString();
            if (position.containsKey("F" + farmercode)) {
                if (farmerList != null) {
                    Integer farmpos = position.get("F" + farmercode);
                    KeyPairBoolData keyPairBoolData = farmerList.get(farmpos);
                    keyPairBoolData.setSelected(true);
                    farmerList.set(farmpos, keyPairBoolData);
                    setDataFarmer(edtfarmercode, spfarmername);
                    edtfarmercode.setError(null);
                }
            } else {
                edtfarmercode.setText("");
                edtfarmercode.setError(getResources().getString(R.string.notpermission));
            }
        } else {
            edtfarmercode.setText("");
            edtfarmercode.setError(getResources().getString(R.string.datanotloaded));
        }
    }

    private void initSingleData() {
        AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
        String farmercode = edtfarmercode.getText().toString();
        SingleSpinnerSearch spfarmername = findViewById(R.id.spfarmername);
        Activity activity = SelectFarmerActivity.this;
        DBHelper dbHelper = new DBHelper(activity);
        HashMap<String, Object> farmerdata = dbHelper.getEntity("F" + farmercode);
        farmerList = (List<KeyPairBoolData>) farmerdata.get("data");
        position = (HashMap<String, Integer>) farmerdata.get("position");
        setDataFarmer(edtfarmercode, spfarmername);
    }


    private void setDataFarmer(AppCompatEditText edtfarmercode, SingleSpinnerSearch spfarmername) {
        if (farmerList == null) {
            farmerList = new ArrayList<>();
        }
        spfarmername.setItems(farmerList, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                edtfarmercode.setText(String.valueOf(selectedItem.getObject()).replace("F", ""));
            }

            @Override
            public void onClear() {

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
        return cf.openCommon(this, item, SelectFarmerActivity.this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, this);
    }

    @Override
    public void onRefreshComplete() {
        initSingleData();
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Constant.showToast(getResources().getString(R.string.cancelscan), SelectFarmerActivity.this, R.drawable.ic_wrong);
                } else {
                    AppCompatEditText edtfarmercode = findViewById(R.id.edtfarmercode);
                    edtfarmercode.setText(result.getContents().replace("F", ""));
                    selectFarmer();
                }
            });

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.HARVPLOTDETAILS) {
            CompletePlotResponse completePlotResponse = (CompletePlotResponse) response.body();
            Intent intent = new Intent(SelectFarmerActivity.this, CompletePlotActivity.class);
            intent.putExtra("completePlotResponse", (Serializable) completePlotResponse);
            intent.putExtra("isclose", "1");
            startActivity(intent);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}