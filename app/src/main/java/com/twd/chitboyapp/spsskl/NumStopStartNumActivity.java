package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

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
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.SingleNumDataResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NumStopStartNumActivity extends AppCompatActivity implements RetrofitResponse, SingleReturn, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_stop_start_num);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = this;
        DBHelper dbHelper = new DBHelper(NumStopStartNumActivity.this);
        List<KeyPairBoolData> reason = dbHelper.getReasonAll(6);

        SingleSpinnerSearch sspreason = findViewById(R.id.sspreason);
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspreason, activity, getResources().getString(R.string.selectreason));
        ssh.setSingleItems(sspreason, reason, DataSetter.REASON);

        AppCompatButton btnscanslip, btnentercode, btnprev, btnsubmit;

        btnscanslip = findViewById(R.id.btnscanslip);
        btnentercode = findViewById(R.id.btnentercode);
        btnprev = findViewById(R.id.btnprev);
        btnsubmit = findViewById(R.id.btnsubmit);

        btnscanslip.setOnClickListener(this);
        btnentercode.setOnClickListener(this);
        btnprev.setOnClickListener(this);
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

    private void clearAll() {
        AppCompatTextView txttransid = findViewById(R.id.txttransid);
        AppCompatTextView txtstatusid = findViewById(R.id.txtstatusid);
        AppCompatTextView txtstatusval = findViewById(R.id.txtstatusval);
        AppCompatTextView txtslipno = findViewById(R.id.txtslipno);
        AppCompatTextView txtvehiclegroupid = findViewById(R.id.txtvehiclegroupid);
        AppCompatTextView txtyardnumid = findViewById(R.id.txtyardnumid);
        AppCompatTextView txtnameval = findViewById(R.id.txtnameval);
        AppCompatTextView txtdatetimeval = findViewById(R.id.txtdatetimeval);
        AppCompatTextView txttokennoval = findViewById(R.id.txttokennoval);
        AppCompatTextView txtyardval = findViewById(R.id.txtyardval);
        SingleSpinnerSearch sspreason = findViewById(R.id.sspreason);

        txttransid.setText("");
        txtslipno.setText("");
        txtvehiclegroupid.setText("");
        txtyardnumid.setText("");
        txtnameval.setText("");
        txtdatetimeval.setText("");
        txttokennoval.setText("");
        txtyardval.setText("");
        txtstatusid.setText("");
        txtstatusval.setText("");
        sspreason.clearAll();
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.NUMBERDATA) {
            SingleNumDataResponse singleNumDataResponse = (SingleNumDataResponse) response.body();

            AppCompatTextView txttransid = findViewById(R.id.txttransid);
            AppCompatTextView txtslipno = findViewById(R.id.txtslipno);
            AppCompatTextView txtstatusid = findViewById(R.id.txtstatusid);
            AppCompatTextView txtstatusval = findViewById(R.id.txtstatusval);
            AppCompatTextView txtvehiclegroupid = findViewById(R.id.txtvehiclegroupid);
            AppCompatTextView txtyardnumid = findViewById(R.id.txtyardnumid);
            AppCompatTextView txtnameval = findViewById(R.id.txtnameval);
            AppCompatTextView txtdatetimeval = findViewById(R.id.txtdatetimeval);
            AppCompatTextView txttokennoval = findViewById(R.id.txttokennoval);
            AppCompatTextView txtyardval = findViewById(R.id.txtyardval);
            AppCompatTextView txtreasonlbl = findViewById(R.id.txtreasonlbl);
            AppCompatTextView txtcollon2 = findViewById(R.id.txtcollon2);
            SingleSpinnerSearch sspreason = findViewById(R.id.sspreason);

            txttransid.setText(singleNumDataResponse.getTansId());
            txtslipno.setText(singleNumDataResponse.getSlipNo());
            txtvehiclegroupid.setText(singleNumDataResponse.getNvehicleGroupId());
            txtyardnumid.setText(singleNumDataResponse.getNyardNumId());
            txtnameval.setText(singleNumDataResponse.getName());
            txtdatetimeval.setText(singleNumDataResponse.getTime());
            txttokennoval.setText(singleNumDataResponse.getNtokenNo());
            txtyardval.setText(singleNumDataResponse.getYardName());
            txtstatusid.setText(singleNumDataResponse.getStatusId());
            txtstatusval.setText(singleNumDataResponse.getStatusName());

            if (singleNumDataResponse.getStatusId().equals("1") || singleNumDataResponse.getStatusId().equals("2") || singleNumDataResponse.getStatusId().equals("4")) {
                txtreasonlbl.setVisibility(View.VISIBLE);
                txtcollon2.setVisibility(View.VISIBLE);
                sspreason.setVisibility(View.VISIBLE);
            } else {
                txtreasonlbl.setVisibility(View.GONE);
                txtcollon2.setVisibility(View.GONE);
                sspreason.setVisibility(View.GONE);
            }

            if (obj[0] != null) {
                Dialog dialog = (Dialog) obj[0];
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        } else if (requestCaller == RequestEnum.SAVE) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                clearAll();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
                return;
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnscanslip) {
            ScannerHandler scannerHandler = new ScannerHandler(NumStopStartNumActivity.this, barcodeLauncher);
            scannerHandler.openScanner();
        } else if (id == R.id.btnentercode) {
            openDailog();
        } else if (id == R.id.btnprev) {
            onBackPressed();
        } else if (id == R.id.btnsubmit) {
            validateAndSubmit();
        }
    }

    private void validateAndSubmit() {
        Activity activity = this;
        AppCompatTextView txttransid = findViewById(R.id.txttransid);
        AppCompatTextView txtslipno = findViewById(R.id.txtslipno);
        AppCompatTextView txtvehiclegroupid = findViewById(R.id.txtvehiclegroupid);
        AppCompatTextView txtyardnumid = findViewById(R.id.txtyardnumid);
        AppCompatTextView txtstatusid = findViewById(R.id.txtstatusid);
        SingleSpinnerSearch sspreason = findViewById(R.id.sspreason);

        String transId = txttransid.getText().toString();
        String slipNo = txtslipno.getText().toString();
        String vehicleGroupId = txtvehiclegroupid.getText().toString();
        String yardId = txtyardnumid.getText().toString();
        String statusId = txtstatusid.getText().toString();
        List<Long> reasonIds = sspreason.getSelectedIds();

        ServerValidation sv = new ServerValidation();
        if (!sv.checkNumber(statusId)) {
            Constant.showToast(getResources().getString(R.string.errormissingparam) + " - Status", NumStopStartNumActivity.this, R.drawable.ic_wrong);
            return;
        }

        boolean isReason = statusId.equals("1") || statusId.equals("2") || statusId.equals("4");
        if (isReason) {
            if (reasonIds.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectreason), NumStopStartNumActivity.this, R.drawable.ic_wrong);
                return;
            }
        }

        if (!sv.checkNumber(transId)) {
            Constant.showToast(getResources().getString(R.string.errormissingparam) + " - Trans Id", NumStopStartNumActivity.this, R.drawable.ic_wrong);
            return;
        }

        if (!sv.checkNumber(slipNo)) {
            Constant.showToast(getResources().getString(R.string.errormissingparam) + " - Slip No", NumStopStartNumActivity.this, R.drawable.ic_wrong);
            return;
        }

        if (!sv.checkNumber(vehicleGroupId)) {
            Constant.showToast(getResources().getString(R.string.errormissingparam) + " - Vehicle Group Id", NumStopStartNumActivity.this, R.drawable.ic_wrong);
            return;
        }

        if (!sv.checkNumber(yardId)) {
            Constant.showToast(getResources().getString(R.string.errormissingparam) + " - Yard Id", NumStopStartNumActivity.this, R.drawable.ic_wrong);
            return;
        }

        final Dialog dialog = new Dialog(NumStopStartNumActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_delete);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatTextView txt_deletehead = dialog.findViewById(R.id.txt_deletehead);
        AppCompatTextView message = dialog.findViewById(R.id.message);

        txt_deletehead.setText(getResources().getString(R.string.information));
        if (isReason) {
            message.setText(getResources().getText(R.string.stopnumber));
        } else {
            message.setText(getResources().getText(R.string.realeasenumber));
        }

        AppCompatButton btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
        AppCompatButton btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);
        btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String action = getResources().getString(R.string.actionnumindstop);

                ConstantFunction cf = new ConstantFunction();
                String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
                String[] value = new String[]{"", "0", "0-0"};
                String[] data = cf.getSharedPrefValue(activity, key, value);

                JSONObject job = new JSONObject();
                try {
                    job.put("transId", transId);
                    job.put("nyardId", yardId);
                    job.put("nyardId", yardId);
                    job.put("nslipNo", slipNo);
                    job.put("nvehicleGroupId", vehicleGroupId);
                    if (isReason) {
                        job.put("nreasonId", reasonIds.get(0));
                        job.put("statusId", "3");
                    } else {
                        job.put("statusId", "1");
                    }
                    job.put("yearId", data[2]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String servlet = getResources().getString(R.string.servletnumbersys);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                Call<ActionResponse> call2 = apiInterface.saveData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                RetrofitHandler<ActionResponse> handleRetrofit = new RetrofitHandler<>();
                handleRetrofit.handleRetrofit(call2, NumStopStartNumActivity.this, RequestEnum.SAVE, servlet, activity, versioncode);
                dialog.cancel();
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

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Constant.showToast(getResources().getString(R.string.cancelscan), NumStopStartNumActivity.this, R.drawable.ic_wrong);
        } else {
            String qrText = result.getContents();
            loadList(qrText, "Q", null, null);
        }
    });

    private void openDailog() {
        Activity activity = this;

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_num_code_entry);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatEditText edtcode = dialog.findViewById(R.id.edtcode);
        RadioGroup rgvehicletype = dialog.findViewById(R.id.rgvehicletype);

        AppCompatButton btncancel = dialog.findViewById(R.id.btncancel);
        AppCompatButton btnconfirm = dialog.findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edtcode.getText().toString();
                int rb = rgvehicletype.getCheckedRadioButtonId();
                String vtype = "";
                if (rb == R.id.rbtranporter) {
                    vtype = "T";
                } else if (rb == R.id.rbbulluckcart) {
                    vtype = "B";
                } else {
                    Constant.showToast(getResources().getString(R.string.errorselectvehicletype), activity, R.drawable.ic_wrong);
                    return;
                }

                ServerValidation sv = new ServerValidation();
                if (!sv.checkNumber(code)) {
                    edtcode.setError(getResources().getString(R.string.errorentercode));
                    return;
                }
                loadList(vtype + code, "C", vtype, dialog);
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private void loadList(String code, String type, String vtype, Dialog dialog) {
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(NumStopStartNumActivity.this, key, value);

        Activity activity = NumStopStartNumActivity.this;
        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionsinglenumdatablock);
        try {
            job.put("code", code);
            job.put("type", type);
            job.put("vtype", vtype);
            job.put("vyear_id", sharedPrefval[2]);
            String servlet = activity.getResources().getString(R.string.servletnumbersys);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            RetrofitHandler<SingleNumDataResponse> otphandler = new RetrofitHandler<>();
            Call<SingleNumDataResponse> call2 = apiInterface.numberData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
            otphandler.handleRetrofit(call2, NumStopStartNumActivity.this, RequestEnum.NUMBERDATA, servlet, activity, versioncode, dialog);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}