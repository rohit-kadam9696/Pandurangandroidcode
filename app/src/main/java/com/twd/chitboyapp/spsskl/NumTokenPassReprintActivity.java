package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

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
import com.twd.chitboyapp.spsskl.pojo.SavePrintResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class NumTokenPassReprintActivity extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_token_pass_reprint);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        AppCompatButton btnback = findViewById(R.id.btnback);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);

        btnback.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);

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
        int id = view.getId();
        if (id == R.id.btnback) {
            onBackPressed();
        } else if (id == R.id.btnsubmit) {
            Activity activity = NumTokenPassReprintActivity.this;

            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actionprinttokenpass);
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            AppCompatEditText edtcode = findViewById(R.id.edtcode);
            RadioGroup rgprinttype = findViewById(R.id.rgprinttype);
            int rbprinttype = rgprinttype.getCheckedRadioButtonId();
            String printtype = "";
            if (rbprinttype == R.id.rbtoken) printtype = "T";
            else if (rbprinttype == R.id.rbpass) printtype = "P";
            else {
                Constant.showToast(getResources().getString(R.string.errorselectprinttype), activity, R.drawable.ic_wrong);
                return;
            }

            RadioGroup rgvehicletype = findViewById(R.id.rgvehicletype);
            String code = edtcode.getText().toString();
            int rb = rgvehicletype.getCheckedRadioButtonId();
            String vtype = "";
            if (rb == R.id.rbtranporter) vtype = "T";
            else if (rb == R.id.rbbulluckcart) vtype = "B";
            else {
                Constant.showToast(getResources().getString(R.string.errorselecttranstype), activity, R.drawable.ic_wrong);
                return;
            }

            ServerValidation sv = new ServerValidation();
            if (!sv.checkNumber(code)) {
                edtcode.setError(getResources().getString(R.string.errorentercode));
                return;
            }

            try {
                job.put("yearId", data[2]);
                job.put("printtype", printtype);
                job.put("vtype", vtype);
                job.put("code", vtype + code);
            } catch (JSONException e) {
                e.printStackTrace();
                Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                return;
            }

            String servlet = getResources().getString(R.string.servletnumbersys);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<SavePrintResponse> call2 = apiInterface.savePrintNumber(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<SavePrintResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, NumTokenPassReprintActivity.this, RequestEnum.PRINT, servlet, activity, versioncode);
        }
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.PRINT) {
            SavePrintResponse savePrintResponse = (SavePrintResponse) response.body();
            if (savePrintResponse.isActionComplete()) {
                Intent intent = new Intent(NumTokenPassReprintActivity.this, SugarReceiptReprintActivity.class);
                intent.putExtra("html", savePrintResponse.getHtmlContent());
                intent.putExtra("mainhead", savePrintResponse.getPrintHead());
                intent.putExtra("backpress", BackPress.NUMLISTPRINT);
                startActivity(intent);
            } else {
                Constant.showToast(savePrintResponse.getFailMsg() != null ? savePrintResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(NumTokenPassReprintActivity.this, HomeActivity.class);
        startActivity(intent1);
    }
}