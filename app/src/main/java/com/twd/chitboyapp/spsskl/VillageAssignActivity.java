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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.twd.chitboyapp.spsskl.constant.DynamicTableData;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.MultiSelectHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.MultiReturn;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.interfaces.TableOperations;
import com.twd.chitboyapp.spsskl.pojo.EmpVillResponse;
import com.twd.chitboyapp.spsskl.pojo.TableReportBean;
import com.twd.chitboyapp.spsskl.pojo.VillageResonse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.MultiSpinnerSearch;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class VillageAssignActivity extends AppCompatActivity implements RetrofitResponse, SingleReturn, MultiReturn, TableOperations, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_assign);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);
        AppCompatEditText edtemployeecode;
        AppCompatImageButton btnsearchemployee;
        SingleSpinnerSearch sspsection;
        AppCompatButton btnremove = findViewById(R.id.btnremove);
        AppCompatButton btnprev = findViewById(R.id.btnprev);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        btnremove.setOnClickListener(this);
        btnprev.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);

        edtemployeecode = findViewById(R.id.edtemployeecode);
        btnsearchemployee = findViewById(R.id.btnsearchemployee);
        sspsection = findViewById(R.id.sspsection);

        Activity activity = this;
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;

        DBHelper dbHelper = new DBHelper(activity);
        ssh.initSingle(sspsection, activity, getResources().getString(R.string.selectsection));


        ArrayList<KeyPairBoolData> dummyData = new ArrayList<>(1);
        List<KeyPairBoolData> sectionList = dbHelper.getSection(-1);
        ssh.setSingleItems(sspsection, sectionList, DataSetter.SECTION);


        MultiSpinnerSearch mspvillage = findViewById(R.id.mspvillage);
        MultiSelectHandler msh = new MultiSelectHandler();
        msh.multiReturn = this;
        msh.initMulti(mspvillage, true, 0, activity, getResources().getString(R.string.village), getResources().getString(R.string.selectvillage), null);
        msh.setMultiSpinner(mspvillage, dummyData, DataSetter.VILLAGE);


        btnsearchemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String employeecode = edtemployeecode.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (sv.check(employeecode, "-?\\d+")) {
                    loadEmpoyeeDetails(employeecode);
                } else {
                    Constant.showToast(getResources().getString(R.string.erroremployeeCodenotFound), VillageAssignActivity.this, R.drawable.ic_wrong);
                }
            }
        });

    }

    private void loadEmpoyeeDetails(String employeecode) {
        String action = getResources().getString(R.string.actionempdata);
        Activity activity = VillageAssignActivity.this;
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        try {
            job.put("employeecode", employeecode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String servlet = getResources().getString(R.string.servletcanePlant);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<EmpVillResponse> call2 = apiInterface.employeeData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<EmpVillResponse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, VillageAssignActivity.this, RequestEnum.EMPDATA, servlet, activity, versioncode);
    }


    private void loadVillage() {
        try {
            Activity activity = this;
            SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
            List<Long> selSectionIds = sspsection.getSelectedIds();
            String sectionId;
            if (selSectionIds.size() != 1) {
                Constant.showToast(getResources().getString(R.string.errorsectionnotfound), activity, R.drawable.ic_wrong);
                return;
            } else {
                sectionId = selSectionIds.get(0).toString();
            }
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
            String[] value = new String[]{"", "0"};
            String[] data = cf.getSharedPrefValue(activity, key, value);
            String servlet = getResources().getString(R.string.servletharvdata);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            RetrofitResponse retrofitResponse = (RetrofitResponse) VillageAssignActivity.this;
            String action = getResources().getString(R.string.actionvillageBySetion);
            JSONObject json = new JSONObject();
            try {
                json.put("sectionId", sectionId);
                json.put("attach", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<VillageResonse> call2 = apiInterface.villList(action, json.toString(), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<VillageResonse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, retrofitResponse, RequestEnum.LOADVILLAGE, servlet, activity, versioncode);

        } catch (Exception e) {
            e.printStackTrace();
        }

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
        if (requestCaller == RequestEnum.LOADVILLAGE) {
            VillageResonse villageResonse = (VillageResonse) response.body();
            MultiSpinnerSearch mspvillage = findViewById(R.id.mspvillage);
            MultiSelectHandler msh = new MultiSelectHandler();
            msh.multiReturn = this;
            msh.setMultiSpinner(mspvillage, villageResonse.getVillList(), DataSetter.VILLAGE);
        } else if (requestCaller == RequestEnum.EMPDATA || requestCaller == RequestEnum.EMPREMOVE || requestCaller == RequestEnum.EMPADDVILL) {
            EmpVillResponse empVillResponse = (EmpVillResponse) response.body();
            if (requestCaller == RequestEnum.EMPREMOVE || requestCaller == RequestEnum.EMPADDVILL) {
                if (empVillResponse.isActionComplete()) {
                    Constant.showToast(empVillResponse.getSuccessMsg() != null ? empVillResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                } else {
                    Constant.showToast(empVillResponse.getFailMsg() != null ? empVillResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
                    return;
                }
            }
            AppCompatTextView txtemployeecode = findViewById(R.id.txtemployeecode);
            AppCompatTextView txtempname = findViewById(R.id.txtempname);
            MultiSpinnerSearch mspvillage = findViewById(R.id.mspvillage);
            SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
            sspsection.clearAll();
            mspvillage.clearAll();
            MultiSelectHandler msp = new MultiSelectHandler();
            msp.multiReturn=this;
            msp.initMulti(mspvillage, true, 0, activity, getResources().getString(R.string.select), getResources().getString(R.string.selectvillage), null);
            txtemployeecode.setText(empVillResponse.getEmployeeCode());
            txtempname.setText(empVillResponse.getEmployeeName());

            TableLayout tblvillage = findViewById(R.id.tblvillage);
            TableReportBean tableReportBean = empVillResponse.getVillageList();
            tblvillage.setTag(tableReportBean.getColWidth());
            tblvillage.removeAllViews();
            DynamicTableData dtd = new DynamicTableData();
            dtd.setDeleteMsg(getResources().getString(R.string.confirmunlinkvillage));
            dtd.addTable(activity, tableReportBean.getTableData(), tblvillage, tableReportBean.getRowColSpan(), tableReportBean.getNoofHeads() != null ? tableReportBean.getNoofHeads() : 1, tableReportBean.isFooter(), tableReportBean.getBoldIndicator(), false, tableReportBean.getFloatings(), tableReportBean.isMarathi(), tableReportBean.getRowSpan(), tableReportBean.getTextAlign(), tableReportBean.getVisibility(), false, false, this, true);
            ConstantFunction cf = new ConstantFunction();
            View view = getCurrentFocus();
            if (view != null) cf.hideKeyboard(view, activity);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.SECTION) {
            loadVillage();
        }
    }

    @Override
    public void onItemsSelected(List<KeyPairBoolData> selectedItems, DataSetter dataSetter) {

    }

    @Override
    public void onOkClickLister(List<KeyPairBoolData> selectedItems, DataSetter dataSetter) {
        if (dataSetter == DataSetter.VILLAGE) {
            checkIsAllowed(selectedItems);
        }
    }

    private void checkIsAllowed(List<KeyPairBoolData> selectedItems) {
        AppCompatEditText edtemployeecode = findViewById(R.id.edtemployeecode);
        String employeecode = edtemployeecode.getText().toString();
        int size = selectedItems.size();
        StringBuilder error = new StringBuilder(getResources().getString(R.string.erroryoucannotselectbelowvillage));
        boolean isError = false;
        for (int i = 0; i < size; i++) {
            KeyPairBoolData data = selectedItems.get(i);
            String json = (String) data.getObject();
            try {
                JSONObject job = new JSONObject(json);
                if (job.has("u1") && job.has("u2")) {
                    error.append("\n" + data.getName());
                    isError = true;
                    continue;
                }
                if (job.has("u1") && job.getString("u1").equals(employeecode)) {
                    error.append("\n" + data.getName());
                    isError = true;
                    continue;
                }
                if (job.has("u2") && job.getString("u2").equals(employeecode)) {
                    error.append("\n" + data.getName());
                    isError = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isError) {
            Constant.showToast(error.toString(), VillageAssignActivity.this, R.drawable.ic_wrong);
            MultiSpinnerSearch mspVillage = findViewById(R.id.mspvillage);
            mspVillage.clearAll();
        }
    }

    @Override
    public void onEditClick(TableLayout tbl, String id, int position) {

    }

    @Override
    public void onDeleteClick(TableLayout tbl, String id, int position) {
        /*Activity activity = this;
        AppCompatTextView txtemployeecode = findViewById(R.id.txtemployeecode);
        AppCompatEditText edtemployeecode = findViewById(R.id.edtemployeecode);

        String hempcode = txtemployeecode.getText().toString();
        String empcode = edtemployeecode.getText().toString();
        if (!hempcode.equals(empcode)) {
            Constant.showToast(getResources().getString(R.string.errorclickonsearchbutton), activity, R.drawable.ic_wrong);
            return;
        }

        ServerValidation sv = new ServerValidation();
        if (!sv.check(empcode, "-?\\d+")) {
            Constant.showToast(getResources().getString(R.string.erroremployeeCodenotFound), activity, R.drawable.ic_wrong);
            return;
        }

        TableRow tableRow = (TableRow) tbl.getChildAt(position);
        TextView textbox3 = (TextView) tableRow.getChildAt(3);
        TextView textbox4 = (TextView) tableRow.getChildAt(4);
        String box3val = textbox3.getText().toString();
        String box4val = textbox4.getText().toString();
        ArrayList<String> selectedVill = new ArrayList<>(2);
        if (box3val.trim().equals(empcode)) selectedVill.add(id);
        else if (box4val.trim().equals(empcode)) {
            selectedVill.add("");
            selectedVill.add(id);
        } else selectedVill.add("");
            selectedVill.add("");


        removeVillage(selectedVill, empcode);*/

    }

    @Override
    public void onClick(View view) {
        Activity activity = this;
        long id = view.getId();
        if (id == R.id.btnremove) {
            AppCompatTextView txtemployeecode = findViewById(R.id.txtemployeecode);
            AppCompatEditText edtemployeecode = findViewById(R.id.edtemployeecode);

            String hempcode = txtemployeecode.getText().toString();
            String empcode = edtemployeecode.getText().toString();
            if (!hempcode.equals(empcode)) {
                Constant.showToast(getResources().getString(R.string.errorclickonsearchbutton), activity, R.drawable.ic_wrong);
                return;
            }

            ServerValidation sv = new ServerValidation();
            if (!sv.check(empcode, "-?\\d+")) {
                Constant.showToast(getResources().getString(R.string.erroremployeeCodenotFound), activity, R.drawable.ic_wrong);
                return;
            }

            TableLayout tblvillage = findViewById(R.id.tblvillage);
            ArrayList<String> selectedVill = getCheckedIdWithU1AndU2(tblvillage, empcode);

            activity = VillageAssignActivity.this;
            if (!selectedVill.get(0).isEmpty() || !selectedVill.get(1).isEmpty()) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.popup_delete);

                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                TextView message = dialog.findViewById(R.id.message);

                message.setText(getResources().getString(R.string.confirmunlinkvillage));

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
                        removeVillage(selectedVill, empcode);
                    }
                });

                dialog.show();
            } else {
                Constant.showToast(getResources().getString(R.string.errorselectvillagetoremove), activity, R.drawable.ic_wrong);
            }
        } else if (id == R.id.btnprev) {
            finish();
        } else if (id == R.id.btnsubmit) {
            AppCompatTextView txtemployeecode = findViewById(R.id.txtemployeecode);
            AppCompatEditText edtemployeecode = findViewById(R.id.edtemployeecode);

            String hempcode = txtemployeecode.getText().toString();
            String empcode = edtemployeecode.getText().toString();
            if (!hempcode.equals(empcode)) {
                Constant.showToast(getResources().getString(R.string.errorclickonsearchbutton), activity, R.drawable.ic_wrong);
                return;
            }

            ServerValidation sv = new ServerValidation();
            if (!sv.check(empcode, "-?\\d+")) {
                Constant.showToast(getResources().getString(R.string.erroremployeeCodenotFound), activity, R.drawable.ic_wrong);
                return;
            }

            MultiSpinnerSearch mspvillage = findViewById(R.id.mspvillage);
            StringBuilder selecedIdu1 = new StringBuilder();
            StringBuilder selecedIdu2 = new StringBuilder();
            List<KeyPairBoolData> keyPairBoolData = mspvillage.getSelectedItems();
            int size = keyPairBoolData.size();
            if (size == 0) {
                Constant.showToast(getResources().getString(R.string.errorvillagenotselect), activity, R.drawable.ic_wrong);
                return;
            }
            for (int i = 0; i < size; i++) {
                KeyPairBoolData data = keyPairBoolData.get(i);
                String jsonStr = (String) data.getObject();
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    if (json.has("u1")) {
                        if (json.getString("u1").equals("")) {
                            if (!selecedIdu1.toString().equals("")) {
                                selecedIdu1.append(",");
                            }
                            selecedIdu1.append(data.getId());
                            continue;
                        }
                    } else {
                        if (!selecedIdu1.toString().equals("")) {
                            selecedIdu1.append(",");
                        }
                        selecedIdu1.append(data.getId());
                        continue;
                    }

                    if (json.has("u2")) {
                        if (json.getString("u2").equals("")) {
                            if (!selecedIdu2.toString().equals("")) {
                                selecedIdu2.append(",");
                            }
                            selecedIdu2.append(data.getId());
                        }
                    } else {
                        if (!selecedIdu2.toString().equals("")) {
                            selecedIdu2.append(",");
                        }
                        selecedIdu2.append(data.getId());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String action = getResources().getString(R.string.actionaddempvillage);

            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
            String[] value = new String[]{"", "0"};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            JSONObject job = new JSONObject();
            try {
                job.put("empcode", empcode);
                job.put("ad1", selecedIdu1.toString());
                job.put("ad2", selecedIdu2.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String servlet = getResources().getString(R.string.servletcanePlant);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<EmpVillResponse> call2 = apiInterface.employeeData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<EmpVillResponse> handleRetrofit = new RetrofitHandler<>();
            handleRetrofit.handleRetrofit(call2, VillageAssignActivity.this, RequestEnum.EMPADDVILL, servlet, activity, versioncode);
        }

    }

    private void removeVillage(ArrayList<String> selectedVill, String empcode) {
        String action = getResources().getString(R.string.actionremoveempvillage);
        Activity activity = VillageAssignActivity.this;
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        try {
            job.put("empcode", empcode);
            job.put("rm1", selectedVill.get(0));
            job.put("rm2", selectedVill.get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String servlet = getResources().getString(R.string.servletcanePlant);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<EmpVillResponse> call2 = apiInterface.employeeData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<EmpVillResponse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, VillageAssignActivity.this, RequestEnum.EMPREMOVE, servlet, activity, versioncode);
    }

    public ArrayList<String> getCheckedIdWithU1AndU2(TableLayout tbl, String empCode) {
        ArrayList<String> myArr = new ArrayList<>(2);
        StringBuilder selecedIdu1 = new StringBuilder();
        StringBuilder selecedIdu2 = new StringBuilder();
        int size = tbl.getChildCount();
        for (int i = 1; i < size; i++) {
            TableRow tableRow = (TableRow) tbl.getChildAt(i);
            View checked = tableRow.getChildAt(0);
            if (checked instanceof CheckBox) {
                CheckBox checkBox = ((CheckBox) checked);
                if (checkBox.isChecked()) {
                    TextView textbox3 = (TextView) tableRow.getChildAt(3);
                    TextView textbox4 = (TextView) tableRow.getChildAt(4);
                    String box3val = textbox3.getText().toString();
                    String box4val = textbox4.getText().toString();
                    if (box3val.equals(empCode)) {
                        if (!selecedIdu1.toString().equals("")) {
                            selecedIdu1.append(",");
                        }
                        selecedIdu1.append(checkBox.getTag());
                    } else if (box4val.equals(empCode)) {
                        if (!selecedIdu2.toString().equals("")) {
                            selecedIdu2.append(",");
                        }
                        selecedIdu2.append(checkBox.getTag());
                    }
                }
            }
        }
        myArr.add(selecedIdu1.toString());
        myArr.add(selecedIdu2.toString());
        return myArr;

    }
}