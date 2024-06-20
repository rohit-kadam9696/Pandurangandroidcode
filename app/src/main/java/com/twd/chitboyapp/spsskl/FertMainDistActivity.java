package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.adapter.FertProductAdapter;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.BackPress;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.UpdateRemain;
import com.twd.chitboyapp.spsskl.pojo.FertProduct;
import com.twd.chitboyapp.spsskl.pojo.FertProductResponse;
import com.twd.chitboyapp.spsskl.pojo.SavePrintResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FertMainDistActivity extends AppCompatActivity implements RetrofitResponse, UpdateRemain, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fert_main_dist);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Intent intent = getIntent();
        String selectedPlots = intent.getStringExtra("selectedPlots");
        String fertarea = intent.getStringExtra("fertarea");
        String farmercode = intent.getStringExtra("farmercode");
        String guarantor1 = intent.getStringExtra("guarantor1");
        String guarantor2 = intent.getStringExtra("guarantor2");
        String saletypes = intent.getStringExtra("saletypes");
        String yearCode = intent.getStringExtra("yearCode");
        String date = intent.getStringExtra("date");
        String farmername = intent.getStringExtra("farmername");
        String guarantor1name = intent.getStringExtra("guarantor1name");
        String guarantor2name = intent.getStringExtra("guarantor2name");


        AppCompatTextView txtfarmercode = findViewById(R.id.txtfarmercode);
        AppCompatTextView txtgurantor1code = findViewById(R.id.txtgurantor1code);
        AppCompatTextView txtgurantor2code = findViewById(R.id.txtgurantor2code);
        AppCompatTextView txtplotno = findViewById(R.id.txtplotno);
        AppCompatTextView txtfertarea = findViewById(R.id.txtfertarea);
        AppCompatTextView txtsaletypes = findViewById(R.id.txtsaletypes);
        AppCompatTextView txtfyearcode = findViewById(R.id.txtfyearcode);
        AppCompatTextView txtfarmername = findViewById(R.id.txtfarmername);
        AppCompatTextView txtguarantor1name = findViewById(R.id.txtguarantor1name);
        AppCompatTextView txtguarantor2name = findViewById(R.id.txtguarantor2name);
        AppCompatTextView txtdate = findViewById(R.id.txtdate);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        AppCompatButton btnprev = findViewById(R.id.btnprev);

        btnsubmit.setOnClickListener(this);
        btnprev.setOnClickListener(this);

        txtfarmercode.setText(farmercode);
        txtgurantor1code.setText(guarantor1);
        txtgurantor2code.setText(guarantor2);
        txtplotno.setText(selectedPlots);
        txtfertarea.setText(fertarea);
        txtsaletypes.setText(saletypes);
        txtfyearcode.setText(yearCode);
        txtdate.setText(date);
        txtfarmername.setText(farmername);
        txtguarantor1name.setText(guarantor1name);
        txtguarantor2name.setText(guarantor2name);

        loadProduct();

        RecyclerView rcvproduct = findViewById(R.id.rcvproduct);
        rcvproduct.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return false;
            }
        });
    }

    private void loadProduct() {
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(FertMainDistActivity.this, key, value);

        Activity activity = FertMainDistActivity.this;
        JSONObject job = new JSONObject();
        AppCompatTextView txtsaletypes = findViewById(R.id.txtsaletypes);
        String saletypes = txtsaletypes.getText().toString();
        try {
            job.put("saletypes", saletypes);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String action = getResources().getString(R.string.actionloadproduct);
        String servlet = activity.getResources().getString(R.string.servletfert);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        RetrofitHandler<FertProductResponse> otphandler = new RetrofitHandler<>();
        Call<FertProductResponse> call2 = apiInterface.fertProduct(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
        otphandler.handleRetrofit(call2, FertMainDistActivity.this, RequestEnum.LIST, servlet, activity, versioncode);

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
        if (requestCaller == RequestEnum.LIST) {
            FertProductResponse fertProductResponse = (FertProductResponse) response.body();
            List<FertProduct> list = fertProductResponse.getFertProducts();
            if (list != null && !list.isEmpty()) {
                FertProductAdapter npFarmerListAdapter = new FertProductAdapter(list, activity, FertMainDistActivity.this);
                RecyclerView rcvproduct = findViewById(R.id.rcvproduct);
                GridLayoutManager mLayoutManager1 = new GridLayoutManager(activity, 1);
                rcvproduct.setLayoutManager(mLayoutManager1);
                rcvproduct.setItemAnimator(new DefaultItemAnimator());
                rcvproduct.setAdapter(npFarmerListAdapter);
            } else {
                Constant.showToast(getResources().getString(R.string.errornoproductfound), FertMainDistActivity.this, R.drawable.ic_wrong);
                finish();
            }
        } else if (requestCaller == RequestEnum.SAVE) {
            SavePrintResponse actionResponse = (SavePrintResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                Intent intent = new Intent(FertMainDistActivity.this, FertDistPrintActivity.class);
                intent.putExtra("html", actionResponse.getHtmlContent());
                intent.putExtra("backpress", BackPress.PRINT);
                startActivity(intent);
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void updateRemain() {
        RecyclerView rcvproduct = findViewById(R.id.rcvproduct);
        FertProductAdapter rcvproductAdapter = (FertProductAdapter) rcvproduct.getAdapter();
        List<FertProduct> fertProducts = rcvproductAdapter.fertProducts;
        double total = 0;
        if (fertProducts != null) {
            ServerValidation sv = new ServerValidation();
            int size = fertProducts.size();
            for (int i = 0; i < size; i++) {
                FertProduct fertProduct = fertProducts.get(i);
                if (fertProduct.getQuantity() != null && sv.checkFloat(fertProduct.getQuantity())) {
                    total += Double.parseDouble(fertProduct.getTotal()) + Double.parseDouble(fertProduct.getTotalTax());
                }
            }
            DecimalFormat df = new DecimalFormat("#0.00");
            AppCompatTextView txttotalmain = findViewById(R.id.txttotalmain);
            txttotalmain.setText(df.format(total));
        }
    }

    @Override
    public void onClick(View view) {
        long id = view.getId();
        if (id == R.id.btnsubmit) {
            Activity activity = this;
            AppCompatTextView txtfarmercode = findViewById(R.id.txtfarmercode);
            AppCompatTextView txtgurantor1code = findViewById(R.id.txtgurantor1code);
            AppCompatTextView txtgurantor2code = findViewById(R.id.txtgurantor2code);
            AppCompatTextView txtplotno = findViewById(R.id.txtplotno);
            AppCompatTextView txtfertarea = findViewById(R.id.txtfertarea);
            AppCompatTextView txtsaletypes = findViewById(R.id.txtsaletypes);
            AppCompatTextView txtfyearcode = findViewById(R.id.txtfyearcode);
            AppCompatTextView txtdate = findViewById(R.id.txtdate);
            AppCompatTextView txtfarmername = findViewById(R.id.txtfarmername);
            AppCompatTextView txtguarantor1name = findViewById(R.id.txtguarantor1name);
            AppCompatTextView txtguarantor2name = findViewById(R.id.txtguarantor2name);

            String fyearcode = txtfyearcode.getText().toString();
            String date = txtdate.getText().toString();
            String nentityUniId = txtfarmercode.getText().toString();
            String nfertGuarantor1 = txtgurantor1code.getText().toString();
            String nfertGuarantor2 = txtgurantor2code.getText().toString();
            String nfertiVatapArea = txtfertarea.getText().toString();
            String vsaleType = txtsaletypes.getText().toString();
            String nplotNo = txtplotno.getText().toString();
            String farmername = txtfarmername.getText().toString();
            String guarantor1name = txtguarantor1name.getText().toString();
            String guarantor2name = txtguarantor2name.getText().toString();

            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason), getResources().getString(R.string.preflocationid)};
            String[] value = new String[]{"", "0", "", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            JSONObject job = new JSONObject();
            try {
                job.put("vyear_id", fyearcode);
                job.put("dissue_date", date);
                job.put("nentity_uni_id", nentityUniId);
                job.put("nfert_guarantor_1", nfertGuarantor1);
                job.put("nfert_guarantor_2", nfertGuarantor2);
                job.put("n_ferti_vatap_area", nfertiVatapArea);
                job.put("farmername", farmername);
                job.put("guarantor1name", guarantor1name);
                job.put("guarantor2name", guarantor2name);
                job.put("vsale_type", vsaleType);
                job.put("vseason_year", data[2]);
                job.put("nplot_no", nplotNo);
                job.put("nlocation_id", data[3]);

                RecyclerView rcvproduct = findViewById(R.id.rcvproduct);
                FertProductAdapter npFarmerListAdapter = (FertProductAdapter) rcvproduct.getAdapter();
                List<FertProduct> fertProducts = npFarmerListAdapter.fertProducts;
                JSONArray jar = new JSONArray();
                if (fertProducts != null) {
                    ServerValidation sv = new ServerValidation();
                    int size = fertProducts.size();
                    if (size == 0) {
                        Constant.showToast(getResources().getString(R.string.errorselectonefertproduct), activity, R.drawable.ic_wrong);
                        return;
                    }
                    for (int i = 0; i < size; i++) {
                        FertProduct fertProduct = fertProducts.get(i);
                        if (fertProduct.getQuantity() != null && sv.checkNumber(fertProduct.getQuantity()) && Double.parseDouble(fertProduct.getQuantity()) > 0) {
                            DecimalFormat df = new DecimalFormat("#0.00");
                            JSONObject innerJob = new JSONObject();
                            innerJob.put("vitem_id", fertProduct.getItemId());
                            innerJob.put("vitem_name", fertProduct.getItemName());
                            innerJob.put("nindent_qty", fertProduct.getQuantity());
                            innerJob.put("nissued_qty", fertProduct.getQuantity());
                            innerJob.put("nsr_no", String.valueOf(i));
                            innerJob.put("nunit_id", fertProduct.getUnitId());
                            innerJob.put("namount", df.format(Double.parseDouble(fertProduct.getTotal()) + Double.parseDouble(fertProduct.getTotalTax())));
                            innerJob.put("nsale_rate", fertProduct.getItemRate());
                            innerJob.put("nsale_amount", fertProduct.getTotal());
                            //innerJob.put("ngroup_id", fertProduct.getG); take while save
                            String taxPer = df.format(Double.parseDouble(fertProduct.getTaxPer()) / 2);
                            String taxAmt = df.format(Double.parseDouble(fertProduct.getTotalTax()) / 2);
                            innerJob.put("ncgst_rate", taxPer);
                            innerJob.put("ncgst_amt", taxAmt);
                            innerJob.put("nsgst_rate", taxPer);
                            innerJob.put("nsgst_amt", taxAmt);

                            jar.put(innerJob);
                        }
                    }
                } else {
                    Constant.showToast(getResources().getString(R.string.errorsomethingwrong), activity, R.drawable.ic_wrong);
                    return;
                }

                job.put("details", jar);

                String action = getResources().getString(R.string.actionsavefert);
                String servlet = getResources().getString(R.string.servletfert);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                Call<SavePrintResponse> call2 = apiInterface.actionFert(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                RetrofitHandler<SavePrintResponse> reqfarmer = new RetrofitHandler<>();
                reqfarmer.handleRetrofit(call2, FertMainDistActivity.this, RequestEnum.SAVE, servlet, activity, versioncode);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        } else if (id == R.id.btnprev) {
            onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            View view = getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else {
            super.onBackPressed();
        }
       /* View view  = getCurrentFocus();
        if(view!=null ) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        } else {
            super.onBackPressed();
        }*/
    }
}