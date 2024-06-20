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
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.DataTwoListResonse;
import com.twd.chitboyapp.spsskl.pojo.UserRoleResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UserRoleChangeActivity extends AppCompatActivity implements RetrofitResponse, SingleReturn {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_role_change);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = UserRoleChangeActivity.this;
        AppCompatEditText edtempcoderole;
        AppCompatImageButton btnsearchemployee;
        SingleSpinnerSearch sspnewrole, sspuser;

        AppCompatButton btnsubmit;
        edtempcoderole = findViewById(R.id.edtempcoderole);
        btnsearchemployee = findViewById(R.id.btnsearchemployee);
        sspnewrole = findViewById(R.id.sspnewrole);
        sspuser = findViewById(R.id.sspuser);
        btnsubmit = findViewById(R.id.btnsubmit);

        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ArrayList<KeyPairBoolData> dummy = new ArrayList(1);
        ssh.initSingle(sspnewrole, activity, getResources().getString(R.string.selectnewrole));
        ssh.setSingleItems(sspnewrole, dummy, DataSetter.ROLE);
        ssh.initSingle(sspuser, activity, getResources().getString(R.string.selectuser));
        ssh.setSingleItems(sspuser, dummy, DataSetter.USER);

        btnsearchemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String empcode = edtempcoderole.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (sv.check(empcode, "-?\\d+")) {
                    loadRoleEmpInfo(empcode);
                } else {
                    Constant.showToast(getResources().getString(R.string.erroremployeeCodenotFound), UserRoleChangeActivity.this, R.drawable.ic_wrong);
                }
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationOrSave();
            }

        });

        loadRoleAndUser();
    }

    private void loadRoleAndUser() {
        String action = getResources().getString(R.string.actionroleusesr);
        Activity activity = UserRoleChangeActivity.this;
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        String servlet = getResources().getString(R.string.servletnumbersys);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<DataTwoListResonse> call2 = apiInterface.empDataTwoList(action, "{}", cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<DataTwoListResonse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, UserRoleChangeActivity.this, RequestEnum.LIST, servlet, activity, versioncode);
    }

    private void loadRoleEmpInfo(String empcode) {
        String action = getResources().getString(R.string.actionroleempinfo);
        Activity activity = UserRoleChangeActivity.this;
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
        Call<UserRoleResponse> call2 = apiInterface.empDataRole(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<UserRoleResponse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, UserRoleChangeActivity.this, RequestEnum.EMPDATA, servlet, activity, versioncode);
    }

    private void validationOrSave() {
        Activity activity = this;
        ServerValidation sv = new ServerValidation();

        AppCompatTextView txtemployeecode, txtcoderole;
        AppCompatEditText edtempcoderole;
        SingleSpinnerSearch sspnewrole;
        txtemployeecode = findViewById(R.id.txtemployeecode);
        txtcoderole = findViewById(R.id.txtrolecode);
        edtempcoderole = findViewById(R.id.edtempcoderole);
        sspnewrole = findViewById(R.id.sspnewrole);

        String employeecode = txtemployeecode.getText().toString();
        String coderole = txtcoderole.getText().toString();
        String empcoderole = edtempcoderole.getText().toString();
        List<KeyPairBoolData> selnewrole = sspnewrole.getSelectedItems();
        Long newroleId = null;

        if (sv.checkNull(empcoderole)) {
            Constant.showToast(getResources().getString(R.string.erroremployeecodenotfound), activity, R.drawable.ic_wrong);
            return;
        } else if (!empcoderole.equalsIgnoreCase(employeecode)) {
            Constant.showToast(getResources().getString(R.string.erroremployeecodenotmatch), activity, R.drawable.ic_wrong);
            return;
        }
        if (!selnewrole.isEmpty()) {
            KeyPairBoolData keyPairBoolData = selnewrole.get(0);
            newroleId = keyPairBoolData.getId();
        } else {
            Constant.showToast(getResources().getString(R.string.errornewrolenotfound), activity, R.drawable.ic_wrong);
            return;
        }
        if (coderole.equalsIgnoreCase(String.valueOf(newroleId))) {
            Constant.showToast(getResources().getString(R.string.errorcurrentroleandnewrolesame), activity, R.drawable.ic_wrong);
            return;
        }

        if (!coderole.equalsIgnoreCase("") && !coderole.equalsIgnoreCase(String.valueOf(newroleId))) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.setContentView(R.layout.popup_delete);

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            TextView message = dialog.findViewById(R.id.message);
            AppCompatTextView txtnametxt = findViewById(R.id.txtnametxt);
            String nametxt = txtnametxt.getText().toString();
            message.setText(String.format(getResources().getString(R.string.confirmsaverole), nametxt));

            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

            Button btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
            Button btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);

            btncanceldelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });


            Long finalNewroleId = newroleId;
            btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    saveRole(empcoderole, finalNewroleId);
                }
            });

            dialog.show();
        } else {
            saveRole(empcoderole, newroleId);
        }


    }

    private void saveRole(String empcoderole, Long finalNewroleId) {
        Activity activity = this;
        String action = getResources().getString(R.string.actionupdateroleemp);

        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        try {
            job.put("empcode", empcoderole);
            job.put("roleid", String.valueOf(finalNewroleId));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String servlet = getResources().getString(R.string.servletnumbersys);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<ActionResponse> call2 = apiInterface.saveData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<ActionResponse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, UserRoleChangeActivity.this, RequestEnum.SAVE, servlet, activity, versioncode);

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
        if (requestCaller == RequestEnum.EMPDATA) {
            UserRoleResponse userRoleResponse = (UserRoleResponse) response.body();
            AppCompatTextView txtnametxt = findViewById(R.id.txtnametxt);
            AppCompatTextView txtcurrentroletxt = findViewById(R.id.txtcurrentroletxt);
            AppCompatTextView txtrolecode = findViewById(R.id.txtrolecode);
            AppCompatTextView txtemployeecode = findViewById(R.id.txtemployeecode);

            txtnametxt.setText(userRoleResponse.getUserName());

            txtrolecode.setText(userRoleResponse.getCurrentRoleId());
            txtemployeecode.setText(userRoleResponse.getUserId());
            txtcurrentroletxt.setText(userRoleResponse.getCurrentRoleName());

            ConstantFunction cf = new ConstantFunction();
            View view = getCurrentFocus();
            if (view != null) cf.hideKeyboard(view, activity);


        } else if (requestCaller == RequestEnum.SAVE) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                clearAll();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
                return;
            }
        } else if (requestCaller == RequestEnum.LIST) {
            DataTwoListResonse twoListResonse = (DataTwoListResonse) response.body();
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.singleReturn = this;
            SingleSpinnerSearch sspnewrole = findViewById(R.id.sspnewrole);
            ssh.initSingle(sspnewrole, activity, getResources().getString(R.string.selectnewrole));
            ssh.setSingleItems(sspnewrole, twoListResonse.getDataList(), DataSetter.ROLE);

            SingleSpinnerSearch sspuser = findViewById(R.id.sspuser);
            ssh.initSingle(sspuser, activity, getResources().getString(R.string.selectuser));
            ssh.setSingleItems(sspuser, twoListResonse.getDataListTwo(), DataSetter.USER);

        }

    }


    private void clearAll() {
        AppCompatEditText edtempcoderole;
        AppCompatTextView txtnametxt, txtcurrentroletxt, txtemployeecode, txtcoderole;
        SingleSpinnerSearch sspnewrole;

        edtempcoderole = findViewById(R.id.edtempcoderole);
        txtnametxt = findViewById(R.id.txtnametxt);
        txtcurrentroletxt = findViewById(R.id.txtcurrentroletxt);
        txtemployeecode = findViewById(R.id.txtemployeecode);
        txtcoderole = findViewById(R.id.txtrolecode);
        sspnewrole = findViewById(R.id.sspnewrole);

        edtempcoderole.setText("");
        txtnametxt.setText("");
        txtcurrentroletxt.setText("");
        txtemployeecode.setText("");
        txtcoderole.setText("");
        sspnewrole.clearAll();

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.USER) {
            AppCompatEditText edtempcoderole = findViewById(R.id.edtempcoderole);
            edtempcoderole.setText(String.valueOf(selectedItem.getId()));
            AppCompatImageButton btnsearchemployee = findViewById(R.id.btnsearchemployee);
            btnsearchemployee.performClick();
        }
    }

}