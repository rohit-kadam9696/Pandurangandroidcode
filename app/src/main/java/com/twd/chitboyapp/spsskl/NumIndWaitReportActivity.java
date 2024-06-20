package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
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
import com.twd.chitboyapp.spsskl.pojo.NumIndResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NumIndWaitReportActivity extends AppCompatActivity implements View.OnClickListener, RetrofitResponse, SingleReturn {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_ind_wait_report);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        AppCompatButton btnscanslip, btnentercode, btncancelreceipt;
        AppCompatTextView txtreasonlbl = findViewById(R.id.txtreasonlbl);
        AppCompatTextView txtcollon2 = findViewById(R.id.txtcollon2);
        SingleSpinnerSearch sspreason = findViewById(R.id.sspreason);

        btnscanslip = findViewById(R.id.btnscanslip);
        btnentercode = findViewById(R.id.btnentercode);
        btncancelreceipt = findViewById(R.id.btncancelreceipt);
        String[] key = new String[]{getResources().getString(R.string.prefofficer)};
        String[] value = new String[]{"1"};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(NumIndWaitReportActivity.this, key, value);

        if (sharedPrefval[0].equals("113") || sharedPrefval[0].equals("115")) {
            btncancelreceipt.setVisibility(View.VISIBLE);
            txtreasonlbl.setVisibility(View.VISIBLE);
            txtcollon2.setVisibility(View.VISIBLE);
            sspreason.setVisibility(View.VISIBLE);
            DBHelper dbHelper = new DBHelper(NumIndWaitReportActivity.this);
            List<KeyPairBoolData> reason = dbHelper.getReasonAll(7);
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.singleReturn = this;
            ssh.initSingle(sspreason, NumIndWaitReportActivity.this, getResources().getString(R.string.selectreason));
            ssh.setSingleItems(sspreason, reason, DataSetter.REASON);
        } else {
            btncancelreceipt.setVisibility(View.GONE);
            txtreasonlbl.setVisibility(View.GONE);
            txtcollon2.setVisibility(View.GONE);
            sspreason.setVisibility(View.GONE);
        }

        btnscanslip.setOnClickListener(this);
        btnentercode.setOnClickListener(this);
        btncancelreceipt.setOnClickListener(this);

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
        if (requestCaller == RequestEnum.NUMBERDATA) {
            NumIndResponse numIndResponse = (NumIndResponse) response.body();

            AppCompatTextView txtnameval = findViewById(R.id.txtnameval);
            AppCompatTextView txtvehiclenoval = findViewById(R.id.txtvehiclenoval);
            AppCompatTextView txtdatetimeval = findViewById(R.id.txtdatetimeval);
            AppCompatTextView txtyardval = findViewById(R.id.txtyardval);
            AppCompatTextView txttokennoval = findViewById(R.id.txttokennoval);
            AppCompatTextView txttotalwaitingval = findViewById(R.id.txttotalwaitingval);
            AppCompatTextView txtphotopathval = findViewById(R.id.txtphotopathval);
            AppCompatImageView imgphoto = findViewById(R.id.imgphoto);
            AppCompatTextView txttotalwaitinglbl = findViewById(R.id.txttotalwaitinglbl);
            AppCompatTextView txtnclcollon8 = findViewById(R.id.txtnclcollon8);
            AppCompatTextView txttransid = findViewById(R.id.txttransid);
            AppCompatTextView txtslipno = findViewById(R.id.txtslipno);

            ServerValidation sv = new ServerValidation();
            if (!sv.checkNumber(numIndResponse.getTotalWaiting())) {
                txttotalwaitinglbl.setVisibility(View.GONE);
                txtnclcollon8.setVisibility(View.GONE);
            } else {
                txttotalwaitinglbl.setVisibility(View.VISIBLE);
                txtnclcollon8.setVisibility(View.VISIBLE);
            }

            txttransid.setText(numIndResponse.getTransId());
            txtslipno.setText(numIndResponse.getSlipNo());
            txtnameval.setText(numIndResponse.getTransName());
            txtvehiclenoval.setText(numIndResponse.getVvehicleNo());
            txtdatetimeval.setText(numIndResponse.getTokenDate());
            txtphotopathval.setText(numIndResponse.getPhotopath());
            DBHelper dbHelper = new DBHelper(activity);
            txtyardval.setText(dbHelper.getCaneYardById(numIndResponse.getYardId()).getVyardName());
            txttokennoval.setText(numIndResponse.getTokenNo());
            txttotalwaitingval.setText(numIndResponse.getTotalWaiting());

            Picasso.get().load(APIClient.getCurrentUrl(activity) + APIClient.imagePathNumber + numIndResponse.getPhotopath()).error(R.mipmap.ic_action_error)
                    .placeholder(R.mipmap.ic_launcher)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imgphoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            imgphoto.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            ConstantFunction cf = new ConstantFunction();
                            cf.showCustomAlert(activity, R.drawable.ic_wrong, activity.getResources().getString(R.string.errorphotonotfound));
                            imgphoto.setVisibility(View.GONE);
                        }
                    });

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
                if (obj[0] != null) {
                    Dialog dialog = (Dialog) obj[0];
                    if (dialog.isShowing()) {
                        dialog.cancel();
                    }
                }
                clearAll();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
                return;
            }
        }
    }

    private void clearAll() {
        AppCompatTextView txtnameval = findViewById(R.id.txtnameval);
        AppCompatTextView txtvehiclenoval = findViewById(R.id.txtvehiclenoval);
        AppCompatTextView txtdatetimeval = findViewById(R.id.txtdatetimeval);
        AppCompatTextView txtyardval = findViewById(R.id.txtyardval);
        AppCompatTextView txttokennoval = findViewById(R.id.txttokennoval);
        AppCompatTextView txttotalwaitingval = findViewById(R.id.txttotalwaitingval);
        AppCompatTextView txtphotopathval = findViewById(R.id.txtphotopathval);
        AppCompatImageView imgphoto = findViewById(R.id.imgphoto);
        AppCompatTextView txttransid = findViewById(R.id.txttransid);
        AppCompatTextView txtslipno = findViewById(R.id.txtslipno);

        txtnameval.setText("");
        txtvehiclenoval.setText("");
        txtdatetimeval.setText("");
        txtyardval.setText("");
        txttokennoval.setText("");
        txttotalwaitingval.setText("");
        txtphotopathval.setText("");
        txttransid.setText("");
        txtslipno.setText("");
        imgphoto.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnscanslip) {
            ScannerHandler scannerHandler = new ScannerHandler(NumIndWaitReportActivity.this, barcodeLauncher);
            scannerHandler.openScanner();
        } else if (id == R.id.btnentercode) {
            openDailog();
        } else if (id == R.id.btncancelreceipt) {
            confirmAndCancel();
        }
    }

    private void confirmAndCancel() {
        Activity activity = NumIndWaitReportActivity.this;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_delete);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        TextView message = dialog.findViewById(R.id.message);
        message.setText(getResources().getString(R.string.confirmcancelregistration));

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
        Button btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);

        btncanceldelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog.cancel();
                SingleSpinnerSearch sspreason = findViewById(R.id.sspreason);
                List<Long> reasonIds = sspreason.getSelectedIds();

                if (reasonIds.isEmpty()) {
                    Constant.showToast(getResources().getString(R.string.errorselectreason), NumIndWaitReportActivity.this, R.drawable.ic_wrong);
                    return;
                }

                AppCompatTextView txttransid = findViewById(R.id.txttransid);
                AppCompatTextView txtslipno = findViewById(R.id.txtslipno);

                String transId = txttransid.getText().toString();
                String slipNo = txtslipno.getText().toString();

                ServerValidation sv = new ServerValidation();
                if (!sv.checkNumber(transId)) {
                    Constant.showToast(getResources().getString(R.string.errormissingparam) + " - Trans Id", NumIndWaitReportActivity.this, R.drawable.ic_wrong);
                    return;
                }

                if (sv.checkNull(slipNo)) {
                    Constant.showToast(getResources().getString(R.string.errormissingparam) + " - Slip No", NumIndWaitReportActivity.this, R.drawable.ic_wrong);
                    return;
                }

                Activity activity = NumIndWaitReportActivity.this;
                String action = getResources().getString(R.string.actioncanelnumber);

                ConstantFunction cf = new ConstantFunction();
                String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
                String[] value = new String[]{"", "0", "0-0"};
                String[] data = cf.getSharedPrefValue(activity, key, value);

                JSONObject job = new JSONObject();
                try {
                    job.put("transId", transId);
                    job.put("nslipNo", slipNo);
                    job.put("nreasonId", reasonIds.get(0));
                    job.put("yearId", data[2]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String servlet = getResources().getString(R.string.servletnumbersys);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                Call<ActionResponse> call2 = apiInterface.saveData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                RetrofitHandler<ActionResponse> handleRetrofit = new RetrofitHandler<>();
                handleRetrofit.handleRetrofit(call2, NumIndWaitReportActivity.this, RequestEnum.SAVE, servlet, activity, versioncode, dialog);

            }
        });

        dialog.show();
    }


    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Constant.showToast(getResources().getString(R.string.cancelscan), NumIndWaitReportActivity.this, R.drawable.ic_wrong);
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
        String[] sharedPrefval = cf.getSharedPrefValue(NumIndWaitReportActivity.this, key, value);

        Activity activity = NumIndWaitReportActivity.this;
        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionnumberwating);
        try {
            job.put("code", code);
            job.put("type", type);
            job.put("vtype", vtype);
            job.put("nyearId", sharedPrefval[2]);
            String servlet = activity.getResources().getString(R.string.servletnumbersys);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            RetrofitHandler<NumIndResponse> otphandler = new RetrofitHandler<>();
            Call<NumIndResponse> call2 = apiInterface.numberIndReport(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
            otphandler.handleRetrofit(call2, NumIndWaitReportActivity.this, RequestEnum.NUMBERDATA, servlet, activity, versioncode, dialog);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }
}