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
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ExtraPlotRequestActivity extends AppCompatActivity implements SingleReturn, View.OnClickListener, RetrofitResponse {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_plot_request);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Intent intent = getIntent();
        List<KeyPairBoolData> reasonlist = (List<KeyPairBoolData>) intent.getSerializableExtra("reasonlist");

        Activity activity = ExtraPlotRequestActivity.this;

        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.prefharvplotdata)};
        String[] value = new String[]{"{}"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        try {
            JSONObject job = new JSONObject(data[0]);
            AppCompatTextView txttentativetonnagetxt = findViewById(R.id.txttentativetonnagetxt);
            AppCompatTextView txtharvestedtonnageval = findViewById(R.id.txtharvestedtonnageval);
            AppCompatTextView txtextendedtonnagetxt = findViewById(R.id.txtextendedtonnagetxt);
            AppCompatTextView txtvillagetxt = findViewById(R.id.txtvillagetxt);
            AppCompatTextView txtfarmernametxt = findViewById(R.id.txtfarmernametxt);
            AppCompatTextView txtplotnotxt = findViewById(R.id.txtplotnotxt);
            AppCompatEditText edttonnage = findViewById(R.id.edttonnage);

            txttentativetonnagetxt.setText(job.has("nexpectedYield") ? job.getString("nexpectedYield") : "0");
            txtharvestedtonnageval.setText(job.has("nharvestedTonnage") ? job.getString("nharvestedTonnage") : "0");
            txtvillagetxt.setText(job.has("villageName") ? job.getString("villageName") : "0");
            txtfarmernametxt.setText(job.has("farmername") ? job.getString("farmername") : "0");
            txtplotnotxt.setText(job.has("nplotNo") ? job.getString("nplotNo") : "0");
            txtextendedtonnagetxt.setText(job.has("extendedtonnage") ? job.getString("extendedtonnage") : "0");
            edttonnage.setText(job.has("allowedtonnage") ? job.getString("allowedtonnage") : "0");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(this);

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        btnprev.setOnClickListener(this);

        SingleSpinnerSearch sspreason = findViewById(R.id.sspreason);

        SingleSelectHandler selectHandler = new SingleSelectHandler();
        selectHandler.singleReturn = this;
        selectHandler.initSingle(sspreason, activity, getResources().getString(R.string.selectreason));
        selectHandler.setSingleItems(sspreason, reasonlist, DataSetter.REASON);

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
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnsubmit) {
            Activity activity = this;
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefharvplotdata)};
            String[] value = new String[]{"", "0", "{}"};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            JSONObject json = null;
            try {
                json = new JSONObject(data[2]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AppCompatEditText edtarea = findViewById(R.id.edtarea);
            AppCompatEditText edttonnage = findViewById(R.id.edttonnage);
            SingleSpinnerSearch sspreason = findViewById(R.id.sspreason);

            List<Long> reason = sspreason.getSelectedIds();

            boolean isValid = true;
            ServerValidation sv = new ServerValidation();

            String tonnage = edttonnage.getText().toString();

            if (!sv.checkFloat(tonnage)) {
                edtarea.setError(getResources().getString(R.string.errorwrongtonnage));
                isValid = false;
            }

            if (reason.size() == 0) {
                Constant.showToast(getResources().getString(R.string.errorselectreason), activity, R.drawable.ic_wrong);
                isValid = false;
            }

            if (isValid) {
                try {
                    JSONObject job = new JSONObject();
                    job.put("vyearId", json.getString("vyearId"));
                    job.put("nentityUniId", json.getString("nentityUniId"));
                    job.put("nplotNo", json.getString("nplotNo"));
                    job.put("ntentativeArea", json.getString("ntentativeArea"));
                    job.put("nharvestedTonnage", json.getString("nharvestedTonnage"));
                    job.put("nexpectedYield", json.getString("nexpectedYield"));
                    job.put("extendedtonnage", json.getString("extendedtonnage"));
                    job.put("nreasonId", String.valueOf(reason.get(0)));
                    job.put("nrequestedTonnage", tonnage);

                    String action = activity.getResources().getString(R.string.actionsaveextraplot);
                    String servlet = activity.getResources().getString(R.string.servletharvdata);
                    APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                    String versioncode = cf.getVersionCode();
                    RetrofitHandler<ActionResponse> extraplotrequesthandler = new RetrofitHandler<>();
                    Call<ActionResponse> call2 = apiInterface.actionHarvData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                    extraplotrequesthandler.handleRetrofit(call2, ExtraPlotRequestActivity.this, RequestEnum.EXTRAPLOTSAVE, servlet, activity, versioncode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (view.getId() == R.id.btnprev) {
            Intent intent = new Intent(ExtraPlotRequestActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.EXTRAPLOTSAVE) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : activity.getResources().getString(R.string.savesucess), activity, R.drawable.ic_wrong);
                Intent intent = new Intent(activity, HomeActivity.class);
                startActivity(intent);
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : activity.getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}