package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.adapter.FertRegistrationListAdapter;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.DigitProcessor;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.interfaces.UpdateRemain;
import com.twd.chitboyapp.spsskl.pojo.CaneConfirmationFarmerResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneConfirmationRegistrationList;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.pojo.FarmerResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FertPlantationDetailsActivity extends AppCompatActivity implements RetrofitResponse, View.OnClickListener, UpdateRemain, SingleReturn {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fert_plantation_details);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Intent intent = getIntent();
        String farmerName = intent.getStringExtra("farmerName");
        String entityUnitId = intent.getStringExtra("entityUnitId");
        String yearCode = intent.getStringExtra("yearCode");
        String date = intent.getStringExtra("date");

        AppCompatTextView txtfarmername = findViewById(R.id.txtfarmername);
        AppCompatTextView txtfarmercode = findViewById(R.id.txtfarmercode);
        AppCompatTextView txtfyearcode = findViewById(R.id.txtfyearcode);
        AppCompatTextView txtdate = findViewById(R.id.txtdate);
        txtfarmername.setText(entityUnitId + " " + farmerName);
        txtfarmercode.setText(entityUnitId);
        txtfyearcode.setText(yearCode);
        txtdate.setText(date);

        Activity activity = this;
        DBHelper dbHelper = new DBHelper(activity);
        List<KeyPairBoolData> saleTypes = dbHelper.getFertSaleType(0);
        SingleSpinnerSearch sspsaletype = findViewById(R.id.sspsaletype);
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspsaletype, activity, getResources().getString(R.string.selectsaletype));
        ssh.setSingleItems(sspsaletype, saleTypes, DataSetter.SALETYPE);

        loadData(entityUnitId);

        AppCompatImageButton imgguarantor1search = findViewById(R.id.imgguarantor1search);
        AppCompatImageButton imgguarantor2search = findViewById(R.id.imgguarantor2search);
        AppCompatButton btnnext = findViewById(R.id.btnnext);
        imgguarantor1search.setOnClickListener(this);
        imgguarantor2search.setOnClickListener(this);
        btnnext.setOnClickListener(this);

        DigitProcessor digitProcessor = new DigitProcessor();
        digitProcessor.processDigit(2, 2, "_", ".", R.id.edtfertarea, FertPlantationDetailsActivity.this, R.id.edtguarantor1);

        AppCompatEditText edtguarantor1  = findViewById(R.id.edtguarantor1);
        edtguarantor1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String gurantor1 = edtguarantor1.getText().toString();
                if(gurantor1.equals("")) {
                    AppCompatTextView txtguarantorcode;
                    AppCompatTextView txtguarantorname;

                    txtguarantorcode = findViewById(R.id.txtguarantor1code);
                    txtguarantorname = findViewById(R.id.txtguarantor1name);

                    txtguarantorcode.setText("");
                    txtguarantorname.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        AppCompatEditText edtguarantor2  = findViewById(R.id.edtguarantor2);
        edtguarantor1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String gurantor2 = edtguarantor2.getText().toString();
                if(gurantor2.equals("")) {
                    AppCompatTextView txtguarantorcode;
                    AppCompatTextView txtguarantorname;

                    txtguarantorcode = findViewById(R.id.txtguarantor2code);
                    txtguarantorname = findViewById(R.id.txtguarantor2name);

                    txtguarantorcode.setText("");
                    txtguarantorname.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtguarantor1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edtguarantor2.requestFocus();
                    return true;
                }
                // Return true if you have consumed the action, else false.
                return false;
            }
        });

        edtguarantor2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    sspsaletype.performClick();
                    return true;
                }
                // Return true if you have consumed the action, else false.
                return false;
            }
        });
    }

    private void loadData(String entityUnitId) {
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(FertPlantationDetailsActivity.this, key, value);

        Activity activity = FertPlantationDetailsActivity.this;
        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionfarmerrujulistdata);
        try {
            job.put("nentityUniId", entityUnitId);
            job.put("yearCode", sharedPrefval[2]);
            job.put("caller", "3");
            String servlet = activity.getResources().getString(R.string.servletloaddata);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            RetrofitHandler<CaneConfirmationFarmerResponse> otphandler = new RetrofitHandler<>();
            Call<CaneConfirmationFarmerResponse> call2 = apiInterface.loadNondListConfirmation(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
            otphandler.handleRetrofit(call2, FertPlantationDetailsActivity.this, RequestEnum.NONDLISTFORCONFIRM, servlet, activity, versioncode);


        } catch (JSONException e) {
            e.printStackTrace();
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
    public void onClick(View view) {
        long id = view.getId();
        if (id == R.id.imgguarantor1search || id == R.id.imgguarantor2search) {
            AppCompatEditText edtguarantor;
            AppCompatTextView txtguarantorcode;
            AppCompatTextView txtfarmercode = findViewById(R.id.txtfarmercode);
            if (id == R.id.imgguarantor1search) {
                edtguarantor = findViewById(R.id.edtguarantor1);
                txtguarantorcode = findViewById(R.id.txtguarantor2code);
            } else {
                edtguarantor = findViewById(R.id.edtguarantor2);
                txtguarantorcode = findViewById(R.id.txtguarantor1code);
            }

            String guarantor = edtguarantor.getText().toString();
            ServerValidation sv = new ServerValidation();

            if (!sv.checkNumber(guarantor)) {
                Constant.showToast(getResources().getString(R.string.errorwrongfarmer), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            String entityUnitId = "F" + guarantor;
            String opguarantor = txtguarantorcode.getText().toString();
            String fertfarmercode = txtfarmercode.getText().toString();
            if (entityUnitId.equals(opguarantor)) {
                edtguarantor.setError(getResources().getString(R.string.errorguarantor1andguarantor2shouldbediffernet));
                Constant.showToast(getResources().getString(R.string.errorguarantor1andguarantor2shouldbediffernet), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (entityUnitId.equals(fertfarmercode)) {
                edtguarantor.setError(getResources().getString(R.string.errorguarantorandfertfarmershouldbedifferent));
                Constant.showToast(getResources().getString(R.string.errorguarantorandfertfarmershouldbedifferent), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }
            // check online
            JSONObject job = new JSONObject();
            try {
                String action = getResources().getString(R.string.actionfarmerrequest);
                Activity activity = FertPlantationDetailsActivity.this;
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
                otphandler.handleRetrofit(call2, FertPlantationDetailsActivity.this, RequestEnum.ONLINEFARMER, servlet, activity, versioncode, id);
                View v = getCurrentFocus();
                if (v != null) cf.hideKeyboard(v, activity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.btnnext) {
            SingleSpinnerSearch sspsaletype = findViewById(R.id.sspsaletype);
            AppCompatTextView txttotalareatxt = findViewById(R.id.txttotalareatxt);
            AppCompatEditText edtfertarea = findViewById(R.id.edtfertarea);
            AppCompatEditText edtguarantor1 = findViewById(R.id.edtguarantor1);
            AppCompatTextView txtguarantor1code = findViewById(R.id.txtguarantor1code);
            AppCompatTextView txtfyearcode = findViewById(R.id.txtfyearcode);
            AppCompatEditText edtguarantor2 = findViewById(R.id.edtguarantor2);
            AppCompatTextView txtguarantor2code = findViewById(R.id.txtguarantor2code);
            AppCompatTextView txtfarmercode = findViewById(R.id.txtfarmercode);
            AppCompatTextView txtdate = findViewById(R.id.txtdate);

            AppCompatTextView txtfarmername = findViewById(R.id.txtfarmername);
            AppCompatTextView txtguarantor1name = findViewById(R.id.txtguarantor1name);
            AppCompatTextView txtguarantor2name = findViewById(R.id.txtguarantor2name);

            List<KeyPairBoolData> saleType = sspsaletype.getSelectedItems();
            List<String> plotNo = getPlotNos();
            String totalarea = txttotalareatxt.getText().toString();
            String fertarea = edtfertarea.getText().toString();
            String guarantor1 = edtguarantor1.getText().toString();
            String guarantor1code = txtguarantor1code.getText().toString();
            String yearCode = txtfyearcode.getText().toString();
            String guarantor2 = edtguarantor2.getText().toString();
            String guarantor2code = txtguarantor2code.getText().toString();
            String farmercode = txtfarmercode.getText().toString();
            String date = txtdate.getText().toString();
            String farmername = txtfarmername.getText().toString();
            String guarantor1name = txtguarantor1name.getText().toString();
            String guarantor2name = txtguarantor2name.getText().toString();

            ServerValidation sv = new ServerValidation();
            if (!sv.checkFloat(totalarea)) {
                Constant.showToast(getResources().getString(R.string.errorselectplot), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (Double.parseDouble(totalarea) <= 0) {
                Constant.showToast(getResources().getString(R.string.errorselectplot), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (!sv.checkFloat(fertarea)) {
                Constant.showToast(getResources().getString(R.string.enterarea), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (Double.parseDouble(fertarea) <= 0) {
                Constant.showToast(getResources().getString(R.string.enterarea), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (saleType.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectsaletype), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (Double.parseDouble(fertarea) > Double.parseDouble(totalarea)) {
                Constant.showToast(String.format(getResources().getString(R.string.errorplotarea), totalarea), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (!guarantor1.equals(guarantor1code.replace("F", ""))) {
                edtguarantor1.setError(getResources().getString(R.string.errorpleaseclick));
                Constant.showToast(getResources().getString(R.string.errorpleaseclick), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (!guarantor2.equals(guarantor2code.replace("F", ""))) {
                edtguarantor2.setError(getResources().getString(R.string.errorpleaseclick));
                Constant.showToast(getResources().getString(R.string.errorpleaseclick), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            if (plotNo == null || plotNo.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectplot), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            if(plotNo.size()>1) {
                Constant.showToast(getResources().getString(R.string.errormaxoneplot), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
                return;
            }

            //String selectedPlots = String.join(",", plotNo);
            String selectedPlots = plotNo.get(0);
            Intent intent = new Intent(FertPlantationDetailsActivity.this, FertMainDistActivity.class);
            intent.putExtra("selectedPlots", selectedPlots);
            intent.putExtra("fertarea", fertarea);
            intent.putExtra("farmercode", farmercode);
            intent.putExtra("guarantor1", guarantor1code);
            intent.putExtra("guarantor2", guarantor2code);
            intent.putExtra("yearCode", yearCode);
            intent.putExtra("date", date);
            intent.putExtra("saletypes", saleType.get(0).getObject().toString());
            intent.putExtra("farmername", farmername);
            intent.putExtra("guarantor1name", guarantor1name);
            intent.putExtra("guarantor2name", guarantor2name);
            startActivity(intent);
        }
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.NONDLISTFORCONFIRM) {
            CaneConfirmationFarmerResponse caneConfirmationFarmerResponse = (CaneConfirmationFarmerResponse) response.body();
            setAdapter(caneConfirmationFarmerResponse.getList(), activity);
        } else if (requestCaller == RequestEnum.ONLINEFARMER) {
            FarmerResponse farmerResponse = (FarmerResponse) response.body();
            EntityMasterDetail entityMasterDetail = farmerResponse.getEntityMasterDetail();
            long id = (long) obj[0];
            AppCompatTextView txtguarantorcode;
            AppCompatTextView txtguarantorname;
            if (id == R.id.imgguarantor1search) {
                txtguarantorcode = findViewById(R.id.txtguarantor1code);
                txtguarantorname = findViewById(R.id.txtguarantor1name);
            } else {
                txtguarantorcode = findViewById(R.id.txtguarantor2code);
                txtguarantorname = findViewById(R.id.txtguarantor2name);
            }
            txtguarantorcode.setText(entityMasterDetail.getNentityUniId());
            txtguarantorname.setText(entityMasterDetail.getNentityUniId() + ":" + entityMasterDetail.getVentityNameLocal());
        }
    }

    private void setAdapter(List<CaneConfirmationRegistrationList> list, Activity activity) {
        if (list != null && !list.isEmpty()) {
            FertRegistrationListAdapter npFarmerListAdapter = new FertRegistrationListAdapter(list, activity, this);
            RecyclerView rvplantations = findViewById(R.id.rvplantations);
            GridLayoutManager mLayoutManager1 = new GridLayoutManager(activity, 1);
            rvplantations.setLayoutManager(mLayoutManager1);
            rvplantations.setItemAnimator(new DefaultItemAnimator());
            rvplantations.setAdapter(npFarmerListAdapter);
        } else {
            Constant.showToast(getResources().getString(R.string.errornoplantationentryforfertsale), FertPlantationDetailsActivity.this, R.drawable.ic_wrong);
            finish();
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void updateRemain() {
        RecyclerView rvplantations = findViewById(R.id.rvplantations);
        FertRegistrationListAdapter npFarmerListAdapter = (FertRegistrationListAdapter) rvplantations.getAdapter();
        List<CaneConfirmationRegistrationList> caneConfirmationRegistrationLists = npFarmerListAdapter.caneConfirmationRegistrationLists;
        double narea = 0;
        if (caneConfirmationRegistrationLists != null) {
            ServerValidation sv = new ServerValidation();
            int size = caneConfirmationRegistrationLists.size();
            for (int i = 0; i < size; i++) {
                CaneConfirmationRegistrationList caneConfirmationRegistrationList = caneConfirmationRegistrationLists.get(i);
                if (caneConfirmationRegistrationList.isChecked()) {
                    if (sv.checkFloat(caneConfirmationRegistrationList.getNarea())) {
                        narea += Double.parseDouble(caneConfirmationRegistrationList.getNarea());
                    }
                }
            }
            DecimalFormat df = new DecimalFormat("#00.00");
            AppCompatTextView txttotalareatxt = findViewById(R.id.txttotalareatxt);
            AppCompatEditText edtfertarea = findViewById(R.id.edtfertarea);
            txttotalareatxt.setText(df.format(narea));
            edtfertarea.setText(df.format(narea));
        }
    }

    public List<String> getPlotNos() {
        RecyclerView rvplantations = findViewById(R.id.rvplantations);
        FertRegistrationListAdapter npFarmerListAdapter = (FertRegistrationListAdapter) rvplantations.getAdapter();
        List<CaneConfirmationRegistrationList> caneConfirmationRegistrationLists = npFarmerListAdapter.caneConfirmationRegistrationLists;
        List<String> plotNo = new ArrayList<>();
        if (caneConfirmationRegistrationLists != null) {
            int size = caneConfirmationRegistrationLists.size();
            for (int i = 0; i < size; i++) {
                CaneConfirmationRegistrationList caneConfirmationRegistrationList = caneConfirmationRegistrationLists.get(i);
                if (caneConfirmationRegistrationList.isChecked()) {
                    plotNo.add(caneConfirmationRegistrationList.getPlotNo());
                }
            }
        }
        return plotNo;
    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }
}