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
import com.twd.chitboyapp.spsskl.pojo.UserYardResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NumYardAssignActivity extends AppCompatActivity implements RetrofitResponse, SingleReturn, RefreshComplete {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_yardassign);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = NumYardAssignActivity.this;
        AppCompatEditText edtempcodeyard;
        AppCompatImageButton btnsearchemployee;
        SingleSpinnerSearch sspnewyard;

        AppCompatButton btnsubmit;
        AppCompatImageButton btnremove;
        edtempcodeyard = findViewById(R.id.edtempcodeyard);
        btnsearchemployee = findViewById(R.id.btnsearchemployee);
        sspnewyard = findViewById(R.id.sspnewyard);
        btnsubmit = findViewById(R.id.btnsubmit);
        btnremove = findViewById(R.id.btnremove);

        DBHelper dbHelper = new DBHelper(NumYardAssignActivity.this);
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspnewyard, activity, getResources().getString(R.string.selectnewyard));
        ssh.setSingleItems(sspnewyard, dbHelper.getCaneYardList(), DataSetter.YARD);

        btnsearchemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String empcode = edtempcodeyard.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (sv.check(empcode, "-?\\d+")) {
                    loadYardEmpInfo(empcode);
                } else {
                    Constant.showToast(getResources().getString(R.string.erroremployeeCodenotFound), NumYardAssignActivity.this, R.drawable.ic_wrong);
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
                AppCompatTextView txtcurrentyardtxt = findViewById(R.id.txtcurrentyardtxt);

                String currentyardtxt = txtcurrentyardtxt.getText().toString();
                String nametxt = txtnametxt.getText().toString();
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.popup_delete);

                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                TextView message = dialog.findViewById(R.id.message);

                message.setText(String.format(getResources().getString(R.string.confirmremoveyard), nametxt, currentyardtxt));

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
                        removeYard();
                    }
                });

                dialog.show();

            }
        });


    }

    private void removeYard() {
        Activity activity = this;
        ServerValidation sv = new ServerValidation();
        AppCompatTextView txtemployeecode, txtcodeyard;
        AppCompatEditText edtempcodeyard;

        txtemployeecode = findViewById(R.id.txtemployeecode);
        txtcodeyard = findViewById(R.id.txtcodeyard);
        edtempcodeyard = findViewById(R.id.edtempcodeyard);

        String empcode = txtemployeecode.getText().toString();
        String yardid = txtcodeyard.getText().toString();
        String employeecode = txtemployeecode.getText().toString();
        String empcodeyard = edtempcodeyard.getText().toString();
        String codeyard = txtcodeyard.getText().toString();

        if (sv.checkNull(empcodeyard)) {
            Constant.showToast(getResources().getString(R.string.erroremployeecodenotfound), activity, R.drawable.ic_wrong);
            return;
        } else if (!empcodeyard.equalsIgnoreCase(employeecode)) {
            Constant.showToast(getResources().getString(R.string.erroremployeecodenotmatch), activity, R.drawable.ic_wrong);
            return;
        }
        if (sv.checkNull(codeyard)) {
            Constant.showToast(getResources().getString(R.string.erroryardcodenotfound), activity, R.drawable.ic_wrong);
            return;
        }


        String action = getResources().getString(R.string.actionremoveryardemp);

        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        try {
            job.put("empcode", empcode);
            job.put("yardid", yardid);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String servlet = getResources().getString(R.string.servletnumbersys);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<ActionResponse> call2 = apiInterface.saveData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<ActionResponse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, NumYardAssignActivity.this, RequestEnum.REMOVEYARD, servlet, activity, versioncode);

    }

    private void loadYardEmpInfo(String empcode) {
        String action = getResources().getString(R.string.actionyardempinfo);
        Activity activity = NumYardAssignActivity.this;
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

        String servlet = getResources().getString(R.string.servletnumbersys);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<UserYardResponse> call2 = apiInterface.empData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<UserYardResponse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, NumYardAssignActivity.this, RequestEnum.EMPDATA, servlet, activity, versioncode);
    }


    private void validationOrSave() {
        Activity activity = this;
        ServerValidation sv = new ServerValidation();

        AppCompatTextView txtemployeecode, txtcodeyard;
        AppCompatEditText edtempcodeyard;
        SingleSpinnerSearch sspnewyard;
        txtemployeecode = findViewById(R.id.txtemployeecode);
        txtcodeyard = findViewById(R.id.txtcodeyard);
        edtempcodeyard = findViewById(R.id.edtempcodeyard);
        sspnewyard = findViewById(R.id.sspnewyard);

        String employeecode = txtemployeecode.getText().toString();
        String codeyard = txtcodeyard.getText().toString();
        String empcodeyard = edtempcodeyard.getText().toString();
        List<KeyPairBoolData> selnewyard = sspnewyard.getSelectedItems();
        Long newyardId = null;

        if (sv.checkNull(empcodeyard)) {
            Constant.showToast(getResources().getString(R.string.erroremployeecodenotfound), activity, R.drawable.ic_wrong);
            return;
        } else if (!empcodeyard.equalsIgnoreCase(employeecode)) {
            Constant.showToast(getResources().getString(R.string.erroremployeecodenotmatch), activity, R.drawable.ic_wrong);
            return;
        }
        if (!selnewyard.isEmpty()) {
            KeyPairBoolData keyPairBoolData = selnewyard.get(0);
            newyardId = keyPairBoolData.getId();
        } else {
            Constant.showToast(getResources().getString(R.string.errorcurrentyardnotfound), activity, R.drawable.ic_wrong);
            return;
        }
        if (codeyard.equalsIgnoreCase(String.valueOf(newyardId))) {
            Constant.showToast(getResources().getString(R.string.errorcurrentyardnotfound), activity, R.drawable.ic_wrong);
            return;
        }

        if (!codeyard.equalsIgnoreCase("") && !codeyard.equalsIgnoreCase(String.valueOf(newyardId))) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.setContentView(R.layout.popup_delete);

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            TextView message = dialog.findViewById(R.id.message);
            AppCompatTextView txtnametxt = findViewById(R.id.txtnametxt);
            String nametxt = txtnametxt.getText().toString();
            message.setText(String.format(getResources().getString(R.string.confirmsaveyard), nametxt));

            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

            Button btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
            Button btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);

            btncanceldelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });


            Long finalNewyardId = newyardId;
            btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    saveyard(empcodeyard, finalNewyardId);
                }
            });

            dialog.show();
        } else {
            saveyard(empcodeyard, newyardId);
        }


    }

    private void saveyard(String empcodeyard, Long finalNewyardId) {
        Activity activity = this;
        String action = getResources().getString(R.string.actionupdateyardemp);

        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        try {
            job.put("empcode", empcodeyard);
            job.put("yardid", String.valueOf(finalNewyardId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String servlet = getResources().getString(R.string.servletnumbersys);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<ActionResponse> call2 = apiInterface.saveData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<ActionResponse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, NumYardAssignActivity.this, RequestEnum.SAVE, servlet, activity, versioncode);

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
            UserYardResponse userYardResponse = (UserYardResponse) response.body();
            AppCompatTextView txtnametxt = findViewById(R.id.txtnametxt);
            AppCompatTextView txtcurrentyardtxt = findViewById(R.id.txtcurrentyardtxt);
            AppCompatTextView txtcodeyard = findViewById(R.id.txtcodeyard);
            AppCompatTextView txtemployeecode = findViewById(R.id.txtemployeecode);
            AppCompatImageButton btnremove = findViewById(R.id.btnremove);
            txtnametxt.setText(userYardResponse.getUserName());

            txtcodeyard.setText(userYardResponse.getCurrentYardId());
            txtemployeecode.setText(userYardResponse.getUserId());
            String yardId = userYardResponse.getCurrentYardId();

            if (yardId == null || yardId.equalsIgnoreCase(""))
                btnremove.setVisibility(View.GONE);
            else {
                btnremove.setVisibility(View.VISIBLE);
                DBHelper dbHelper = new DBHelper(NumYardAssignActivity.this);
                txtcurrentyardtxt.setText(dbHelper.getCaneYardById(yardId).getVyardName());
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
        } else if (requestCaller == RequestEnum.REMOVEYARD) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                ImageButton btnremove = findViewById(R.id.btnremove);
                btnremove.setVisibility(View.GONE);
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                AppCompatTextView txtcodeyard = findViewById(R.id.txtcodeyard);
                AppCompatTextView txtcurrentyardtxt = findViewById(R.id.txtcurrentyardtxt);
                txtcodeyard.setText("");
                txtcurrentyardtxt.setText("");

            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
                return;
            }
        }
    }


    private void clearAll() {
        AppCompatEditText edtempcodeyard;
        AppCompatTextView txtnametxt, txtcurrentyardtxt, txtemployeecode, txtcodeyard;
        SingleSpinnerSearch sspnewyard;
        ImageButton btnremove = findViewById(R.id.btnremove);

        edtempcodeyard = findViewById(R.id.edtempcodeyard);
        txtnametxt = findViewById(R.id.txtnametxt);
        txtcurrentyardtxt = findViewById(R.id.txtcurrentyardtxt);
        txtemployeecode = findViewById(R.id.txtemployeecode);
        txtcodeyard = findViewById(R.id.txtcodeyard);
        sspnewyard = findViewById(R.id.sspnewyard);

        btnremove.setVisibility(View.GONE);
        edtempcodeyard.setText("");
        txtnametxt.setText("");
        txtcurrentyardtxt.setText("");
        txtemployeecode.setText("");
        txtcodeyard.setText("");
        sspnewyard.clearAll();

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }

    @Override
    public void onRefreshComplete() {
        Activity activity = NumYardAssignActivity.this;
        SingleSpinnerSearch sspnewyard = findViewById(R.id.sspnewyard);
        DBHelper dbHelper = new DBHelper(NumYardAssignActivity.this);
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspnewyard, activity, getResources().getString(R.string.selectnewyard));
        ssh.setSingleItems(sspnewyard, dbHelper.getCaneYardList(), DataSetter.YARD);
    }
}