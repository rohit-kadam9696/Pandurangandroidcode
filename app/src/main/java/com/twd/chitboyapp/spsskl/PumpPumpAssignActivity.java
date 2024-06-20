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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RefreshComplete;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.UserPumpResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PumpPumpAssignActivity extends AppCompatActivity  implements RetrofitResponse, SingleReturn, RefreshComplete {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pump_pump_assign);
        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = PumpPumpAssignActivity.this;
        AppCompatEditText edtempcodepump;
        AppCompatImageButton btnsearchemployee;
        SingleSpinnerSearch sspnewpump;

        AppCompatButton btnsubmit;
        AppCompatImageButton btnremove;
        edtempcodepump = findViewById(R.id.edtempcodepump);
        btnsearchemployee = findViewById(R.id.btnsearchemployee);
        sspnewpump = findViewById(R.id.sspnewpump);
        btnsubmit = findViewById(R.id.btnsubmit);
        btnremove = findViewById(R.id.btnremove);

        DBHelper dbHelper = new DBHelper(PumpPumpAssignActivity.this);
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspnewpump, activity, getResources().getString(R.string.selectnewpump));
        ssh.setSingleItems(sspnewpump, dbHelper.getPumpList(), DataSetter.PUMP);

        btnsearchemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String empcode = edtempcodepump.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (sv.check(empcode, "-?\\d+")) {
                    loadpumpEmpInfo(empcode);
                } else {
                    Constant.showToast(getResources().getString(R.string.erroremployeeCodenotFound), PumpPumpAssignActivity.this, R.drawable.ic_wrong);
                }
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationOrSave();
            }

        });
        btnremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatTextView txtnametxt = findViewById(R.id.txtnametxt);
                AppCompatTextView txtcurrentpumptxt = findViewById(R.id.txtcurrentpumptxt);

                String currentpumptxt = txtcurrentpumptxt.getText().toString();
                String nametxt = txtnametxt.getText().toString();
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.popup_delete);

                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                TextView message = dialog.findViewById(R.id.message);

                message.setText(String.format(getResources().getString(R.string.confirmremovepump), nametxt, currentpumptxt));

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
                        dialog.cancel();
                        removePump();
                    }
                });

                dialog.show();

            }
        });


    }

    private void removePump() {
        Activity activity = this;
        ServerValidation sv = new ServerValidation();
        AppCompatTextView txtemployeecode, txtcodepump;
        AppCompatEditText edtempcodepump;

        txtemployeecode = findViewById(R.id.txtemployeecode);
        txtcodepump = findViewById(R.id.txtcodepump);
        edtempcodepump = findViewById(R.id.edtempcodepump);

        String empcode = txtemployeecode.getText().toString();
        String pumpid = txtcodepump.getText().toString();
        String employeecode = txtemployeecode.getText().toString();
        String empcodepump = edtempcodepump.getText().toString();
        String codepump = txtcodepump.getText().toString();

        if (sv.checkNull(empcodepump)) {
            Constant.showToast(getResources().getString(R.string.erroremployeecodenotfound), activity, R.drawable.ic_wrong);
            return;
        } else if (!empcodepump.equalsIgnoreCase(employeecode)) {
            Constant.showToast(getResources().getString(R.string.erroremployeecodenotmatch), activity, R.drawable.ic_wrong);
            return;
        }
        if (sv.checkNull(codepump)) {
            Constant.showToast(getResources().getString(R.string.errorpumpcodenotfound), activity, R.drawable.ic_wrong);
            return;
        }


        String action = getResources().getString(R.string.actionremoverpumpemp);

        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        try {
            job.put("empcode", empcode);
            job.put("pumpid", pumpid);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String servlet = getResources().getString(R.string.servletpump);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<ActionResponse> call2 = apiInterface.pumpActionResponse(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<ActionResponse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, PumpPumpAssignActivity.this, RequestEnum.REMOVEPUMP, servlet, activity, versioncode);

    }

    private void loadpumpEmpInfo(String empcode) {
        String action = getResources().getString(R.string.actionpumpempinfo);
        Activity activity = PumpPumpAssignActivity.this;
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        try {
            job.put("empcode", empcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String servlet = getResources().getString(R.string.servletpump);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<UserPumpResponse> call2 = apiInterface.userPumpData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<UserPumpResponse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, PumpPumpAssignActivity.this, RequestEnum.EMPDATA, servlet, activity, versioncode);
    }


    private void validationOrSave() {
        Activity activity = this;
        ServerValidation sv = new ServerValidation();

        AppCompatTextView txtemployeecode, txtcodepump;
        AppCompatEditText edtempcodepump;
        SingleSpinnerSearch sspnewpump;
        txtemployeecode = findViewById(R.id.txtemployeecode);
        txtcodepump = findViewById(R.id.txtcodepump);
        edtempcodepump = findViewById(R.id.edtempcodepump);
        sspnewpump = findViewById(R.id.sspnewpump);

        String employeecode = txtemployeecode.getText().toString();
        String codepump = txtcodepump.getText().toString();
        String empcodepump = edtempcodepump.getText().toString();
        List<KeyPairBoolData> selnewpump = sspnewpump.getSelectedItems();
        Long newpumpId = null;

        if (sv.checkNull(empcodepump)) {
            Constant.showToast(getResources().getString(R.string.erroremployeecodenotfound), activity, R.drawable.ic_wrong);
            return;
        } else if (!empcodepump.equalsIgnoreCase(employeecode)) {
            Constant.showToast(getResources().getString(R.string.erroremployeecodenotmatch), activity, R.drawable.ic_wrong);
            return;
        }
        if (!selnewpump.isEmpty()) {
            KeyPairBoolData keyPairBoolData = selnewpump.get(0);
            newpumpId = keyPairBoolData.getId();
        } else {
            Constant.showToast(getResources().getString(R.string.errorcurrentpumpnotfound), activity, R.drawable.ic_wrong);
            return;
        }
        if (codepump.equalsIgnoreCase(String.valueOf(newpumpId))) {
            Constant.showToast(getResources().getString(R.string.errorcurrentpumpnotfound), activity, R.drawable.ic_wrong);
            return;
        }

        if (!codepump.equalsIgnoreCase("") && !codepump.equalsIgnoreCase(String.valueOf(newpumpId))) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.setContentView(R.layout.popup_delete);

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            TextView message = dialog.findViewById(R.id.message);
            AppCompatTextView txtnametxt = findViewById(R.id.txtnametxt);
            String nametxt = txtnametxt.getText().toString();
            message.setText(String.format(getResources().getString(R.string.confirmsavepump), nametxt));

            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

            Button btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
            Button btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);

            btncanceldelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });


            Long finalNewpumpId = newpumpId;
            btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    savepump(empcodepump, finalNewpumpId);
                }
            });

            dialog.show();
        } else {
            savepump(empcodepump, newpumpId);
        }


    }

    private void savepump(String empcodepump, Long finalNewpumpId) {
        Activity activity = this;
        String action = getResources().getString(R.string.actionupdatepumpemp);

        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        try {
            job.put("empcode", empcodepump);
            job.put("pumpid", String.valueOf(finalNewpumpId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String servlet = getResources().getString(R.string.servletpump);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<ActionResponse> call2 = apiInterface.saveData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<ActionResponse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, PumpPumpAssignActivity.this, RequestEnum.SAVE, servlet, activity, versioncode);

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
        return cf.openCommon(this, item, this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, this);
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.EMPDATA) {
            UserPumpResponse UserPumpResponse = (UserPumpResponse) response.body();
            AppCompatTextView txtnametxt = findViewById(R.id.txtnametxt);
            AppCompatTextView txtcurrentpumptxt = findViewById(R.id.txtcurrentpumptxt);
            AppCompatTextView txtcodepump = findViewById(R.id.txtcodepump);
            AppCompatTextView txtemployeecode = findViewById(R.id.txtemployeecode);
            AppCompatImageButton btnremove = findViewById(R.id.btnremove);
            txtnametxt.setText(UserPumpResponse.getUserName());

            txtcodepump.setText(UserPumpResponse.getCurrentPumpId());
            txtemployeecode.setText(UserPumpResponse.getUserId());
            String pumpId = UserPumpResponse.getCurrentPumpId();

            if (pumpId == null || pumpId.equalsIgnoreCase(""))
                btnremove.setVisibility(View.GONE);
            else {
                btnremove.setVisibility(View.VISIBLE);
                DBHelper dbHelper = new DBHelper(PumpPumpAssignActivity.this);
                txtcurrentpumptxt.setText(dbHelper.getPumpById(pumpId).getVpumpName());
            }
            ConstantFunction cf = new ConstantFunction();
            View view = getCurrentFocus();
            if (view != null)
                cf.hideKeyboard(view, activity);


        } else if (requestCaller == RequestEnum.SAVE) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                clearAll();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
                return;
            }
        } else if (requestCaller == RequestEnum.REMOVEPUMP) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                ImageButton btnremove = findViewById(R.id.btnremove);
                btnremove.setVisibility(View.GONE);
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                AppCompatTextView txtcodepump = findViewById(R.id.txtcodepump);
                AppCompatTextView txtcurrentpumptxt = findViewById(R.id.txtcurrentpumptxt);
                txtcodepump.setText("");
                txtcurrentpumptxt.setText("");

            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
                return;
            }
        }
    }


    private void clearAll() {
        AppCompatEditText edtempcodepump;
        AppCompatTextView txtnametxt, txtcurrentpumptxt, txtemployeecode, txtcodepump;
        SingleSpinnerSearch sspnewpump;
        ImageButton btnremove = findViewById(R.id.btnremove);

        edtempcodepump = findViewById(R.id.edtempcodepump);
        txtnametxt = findViewById(R.id.txtnametxt);
        txtcurrentpumptxt = findViewById(R.id.txtcurrentpumptxt);
        txtemployeecode = findViewById(R.id.txtemployeecode);
        txtcodepump = findViewById(R.id.txtcodepump);
        sspnewpump = findViewById(R.id.sspnewpump);

        btnremove.setVisibility(View.GONE);
        edtempcodepump.setText("");
        txtnametxt.setText("");
        txtcurrentpumptxt.setText("");
        txtemployeecode.setText("");
        txtcodepump.setText("");
        sspnewpump.clearAll();

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }

    @Override
    public void onRefreshComplete() {
        Activity activity = PumpPumpAssignActivity.this;
        SingleSpinnerSearch sspnewpump = findViewById(R.id.sspnewpump);
        DBHelper dbHelper = new DBHelper(PumpPumpAssignActivity.this);
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspnewpump, activity, getResources().getString(R.string.selectnewpump));
        ssh.setSingleItems(sspnewpump, dbHelper.getPumpList(), DataSetter.PUMP);
    }
}