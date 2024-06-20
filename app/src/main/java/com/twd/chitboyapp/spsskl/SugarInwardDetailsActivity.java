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
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.SugarInwardResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class SugarInwardDetailsActivity extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {
    SugarInwardResponse sugarinward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_inward_details);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = this;
        Intent intent = getIntent();
        if (intent.hasExtra("sugarinward")) {
            sugarinward = (SugarInwardResponse) intent.getSerializableExtra("sugarinward");
        } else {
            Constant.showToast(getResources().getString(R.string.errorInwardInfoNotAvailable), activity, R.drawable.ic_wrong);
            finish();
        }

        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        AppCompatButton btnprev = findViewById(R.id.btnprev);

        btnsubmit.setOnClickListener(this);
        btnprev.setOnClickListener(this);

        setData(activity);

    }

    private void setData(Activity activity) {
        AppCompatTextView txtlocationval = findViewById(R.id.txtlocationval);
        AppCompatTextView txtsugarseasonval = findViewById(R.id.txtsugarseasonval);
        AppCompatTextView txtsugarforval = findViewById(R.id.txtsugarforval);
        AppCompatTextView txtemployeeval = findViewById(R.id.txtemployeeval);
        AppCompatTextView txtvehiclenoval = findViewById(R.id.txtvehiclenoval);
        AppCompatTextView txtsugarbagval = findViewById(R.id.txtsugarbagval);
        AppCompatTextView txtvajanprakarval = findViewById(R.id.txtvajanprakarval);
        AppCompatTextView txtsugarweightval = findViewById(R.id.txtsugarweightval);

        ConstantFunction cf = new ConstantFunction();
        String key[] = new String[]{getResources().getString(R.string.chitboyprefname)};
        String value[] = new String[]{""};
        String data[] = cf.getSharedPrefValue(activity, key, value);

        txtlocationval.setText(sugarinward.getLocationNameInward());
        txtsugarseasonval.setText(sugarinward.getVsugYearId());
        txtsugarforval.setText(sugarinward.getSugarFor());
        txtemployeeval.setText(data[0]);
        txtvehiclenoval.setText(sugarinward.getVvehicleNo());
        txtsugarbagval.setText(String.valueOf(sugarinward.getNqty()));
        txtvajanprakarval.setText(String.valueOf(sugarinward.getNwtBag()));
        txtsugarweightval.setText(String.valueOf(sugarinward.getNbags()));

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
        if (view.getId() == R.id.btnsubmit) {
            Activity activity = this;
            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actionsaveinward);
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
            String[] value = new String[]{"", "0"};
            String[] data = cf.getSharedPrefValue(activity, key, value);
            try {
                job.put("dawakDate", sugarinward.getDawakDate());
                job.put("ddoDate", sugarinward.getDdoDate());
                job.put("ndoNo", sugarinward.getNdoNo());
                job.put("nqty", sugarinward.getNqty());
                job.put("nbags", sugarinward.getNbags());
                job.put("vvehicleNo", sugarinward.getVvehicleNo());
                job.put("nwtBag", sugarinward.getNwtBag());
                job.put("nsugarFor", sugarinward.getNsugarFor());
                job.put("vsugYearId", sugarinward.getVsugYearId());
            } catch (JSONException e) {
                e.printStackTrace();
                Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                return;
            }

            String servlet = getResources().getString(R.string.servletsugarsale);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<ActionResponse> call2 = apiInterface.actionSugarSale(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, SugarInwardDetailsActivity.this, RequestEnum.SAVE, servlet, activity, versioncode);
        } else if (view.getId() == R.id.btnprev) {
            onBackPressed();
        }

    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.SAVE) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                Intent intent = new Intent(SugarInwardDetailsActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}