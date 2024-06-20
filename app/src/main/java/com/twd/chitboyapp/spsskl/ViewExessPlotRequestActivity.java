package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

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
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.ExcessTonPlotReqDataResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class ViewExessPlotRequestActivity extends AppCompatActivity implements RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exess_plot_request);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Intent intent = getIntent();
        if (intent.hasExtra("data")) {
            ExcessTonPlotReqDataResponse data = (ExcessTonPlotReqDataResponse) intent.getSerializableExtra("data");
            setData(data);
        } else {
            Constant.showToast(getResources().getString(R.string.errornodata), ViewExessPlotRequestActivity.this, R.drawable.ic_wrong);
            finish();
        }

        AppCompatButton btnreject = findViewById(R.id.btnreject);
        AppCompatButton btnaccept = findViewById(R.id.btnaccept);

        btnreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ViewExessPlotRequestActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.popup_delete);

                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);

                dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

                AppCompatTextView txt_deletehead = dialog.findViewById(R.id.txt_deletehead);
                AppCompatTextView message = dialog.findViewById(R.id.message);

                txt_deletehead.setText(getResources().getString(R.string.reject));
                message.setText(getResources().getString(R.string.errorrejectconfirm));

                AppCompatButton btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
                AppCompatButton btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);
                btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject job = new JSONObject();
                        try {
                            job.put("nstatusId", "3");
                            performOperation(job, dialog);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                btncanceldelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });

        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatEditText edtallowedtonnage = findViewById(R.id.edtallowedtonnage);
                String allowedtonnage = edtallowedtonnage.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (!sv.checkFloat(allowedtonnage)) {
                    edtallowedtonnage.setError(getResources().getString(R.string.errorentertonnage));

                } else {
                    JSONObject job = new JSONObject();
                    try {
                        job.put("nstatusId", "2");
                        job.put("nallowedTonnage", allowedtonnage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    performOperation(job, null);
                }

            }
        });
    }

    private void performOperation(JSONObject job, Dialog dialog) {
        Activity activity = this;
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", "{}"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        AppCompatTextView txtplotnotxt = findViewById(R.id.txtplotnotxt);
        String nplotNo = txtplotnotxt.getText().toString();
        try {
            job.put("vyearCode", data[2]);
            job.put("nplotNo", nplotNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String action = activity.getResources().getString(R.string.actionaccptorrejectextraplot);
        String servlet = activity.getResources().getString(R.string.servletharvdata);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        RetrofitHandler<ActionResponse> extraplotrequesthandler = new RetrofitHandler<>();
        Call<ActionResponse> call2 = apiInterface.actionHarvData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        extraplotrequesthandler.handleRetrofit(call2, ViewExessPlotRequestActivity.this, RequestEnum.EXTRAPLOTACCEPTORREJECT, servlet, activity, versioncode, dialog);
    }

    private void setData(ExcessTonPlotReqDataResponse data) {
        AppCompatTextView txtplotnotxt, txtfarmernametxt, txtareatxt, txttentativetonnagetxt, txtextendedtonnagetxt, txtharvestedtonnageval, txttonnagetxt, txtreasontxt, txtdeptheadtxt;
        AppCompatEditText edtallowedtonnage;

        txtplotnotxt = findViewById(R.id.txtplotnotxt);
        txtfarmernametxt = findViewById(R.id.txtfarmernametxt);
        txtareatxt = findViewById(R.id.txtareatxt);
        txttentativetonnagetxt = findViewById(R.id.txttentativetonnagetxt);
        txtextendedtonnagetxt = findViewById(R.id.txtextendedtonnagetxt);
        txtharvestedtonnageval = findViewById(R.id.txtharvestedtonnageval);
        txttonnagetxt = findViewById(R.id.txttonnagetxt);
        txtreasontxt = findViewById(R.id.txtreasontxt);
        txtdeptheadtxt = findViewById(R.id.txtdeptheadtxt);
        edtallowedtonnage = findViewById(R.id.edtallowedtonnage);

        txtplotnotxt.setText(data.getNplotNo());
        txtfarmernametxt.setText(data.getNentityUniId() + " " + data.getVfarmerName());
        txtareatxt.setText(data.getNtentativeArea());
        txttentativetonnagetxt.setText(data.getNexpectedYield());
        txtextendedtonnagetxt.setText(data.getNlimitYield());
        txtharvestedtonnageval.setText(data.getNharvestedTonnage());
        txttonnagetxt.setText(data.getNremainingTonnage());
        txtreasontxt.setText(data.getVreasonName());
        txtdeptheadtxt.setText(data.getVchitboyname());
        edtallowedtonnage.setText(data.getNremainingTonnage());

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
        if (requestCaller == RequestEnum.EXTRAPLOTACCEPTORREJECT) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : activity.getResources().getString(R.string.savesucess), activity, R.drawable.ic_wrong);
                Intent intent = new Intent(activity, HomeActivity.class);
                startActivity(intent);
                if (obj[0] != null) {
                    ((Dialog) obj[0]).cancel();
                }
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : activity.getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}