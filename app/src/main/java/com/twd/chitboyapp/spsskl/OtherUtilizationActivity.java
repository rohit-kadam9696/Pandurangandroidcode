package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.filter.InputFilterMinMax;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.HangamMaster;
import com.twd.chitboyapp.spsskl.pojo.OtherUtilizationResponse;
import com.twd.chitboyapp.spsskl.pojo.VarietyMaster;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class OtherUtilizationActivity extends AppCompatActivity implements RetrofitResponse, SingleReturn {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_utilization);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Intent intent = getIntent();
        if (intent.hasExtra("otherUtilizationResponse")) {
            OtherUtilizationResponse otherUtilizationResponse = (OtherUtilizationResponse) intent.getSerializableExtra("otherUtilizationResponse");
            setData(otherUtilizationResponse);
        }

        AppCompatEditText edtcurrentareatxt = findViewById(R.id.edtcurrentareatxt);
        InputFilterMinMax inputFilterarea = new InputFilterMinMax(0, 25.00, 2);
        edtcurrentareatxt.setFilters(new InputFilter[]{inputFilterarea});

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SingleSpinnerSearch sspremark, sspfactory;
                AppCompatEditText edtcurrentareatxt;
                AppCompatTextView txtplotnoval, txtfarmercode, txtareatxt, txtexpectedyield;

                edtcurrentareatxt = findViewById(R.id.edtcurrentareatxt);
                txtplotnoval = findViewById(R.id.txtplotnoval);
                txtfarmercode = findViewById(R.id.txtfarmercode);
                txtareatxt = findViewById(R.id.txtareatxt);
                txtexpectedyield = findViewById(R.id.txtexpectedyield);

                sspremark = findViewById(R.id.sspremark);
                sspfactory = findViewById(R.id.sspfactory);


                Activity activity = OtherUtilizationActivity.this;
                ConstantFunction cf = new ConstantFunction();
                String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
                String[] value = new String[]{"", "0", ""};
                String[] data = cf.getSharedPrefValue(activity, key, value);
                int originalArea = (int) (Double.parseDouble(txtareatxt.getText().toString()) * 100);

                List<Long> remark = sspremark.getSelectedIds();

                boolean isValid = true;
                ServerValidation sv = new ServerValidation();

                boolean equal = false;
                String area = edtcurrentareatxt.getText().toString();
                String expectedyield = txtexpectedyield.getText().toString();
                if (!sv.checkFloat(area)) {
                    edtcurrentareatxt.setError(getResources().getString(R.string.errorareanotselect));
                    isValid = false;
                } else {
                    DecimalFormat df = new DecimalFormat("#0.00");
                    int darea = (int) (Double.parseDouble(area) * 100);
                    AppCompatTextView txtutiareatxt = findViewById(R.id.txtutiareatxt);
                    String utiarea = txtutiareatxt.getText().toString();
                    if (sv.checkFloat(utiarea)) {
                        originalArea = originalArea - (int) (Double.parseDouble(utiarea) * 100);
                    }
                    if (darea > originalArea) {
                        edtcurrentareatxt.setError(String.format(getResources().getString(R.string.errorplotarea), df.format(originalArea / 100.0)));
                        isValid = false;
                    } else {
                        equal = darea == originalArea;
                        if (darea % 5 != 0) {
                            edtcurrentareatxt.setError(getResources().getString(R.string.errormultipleof005));
                            isValid = false;
                        } else {
                            edtcurrentareatxt.setError(null);
                        }
                    }
                }

                Long factId = 0l;
                if (remark.size() == 0) {
                    Constant.showToast(getResources().getString(R.string.errorselectreason), activity, R.drawable.ic_wrong);
                    isValid = false;
                } else {
                    if (remark.get(0) == 1) {
                        List<Long> factory = sspfactory.getSelectedIds();
                        if (factory.size() == 0) {
                            Constant.showToast(getResources().getString(R.string.errorselectfact), activity, R.drawable.ic_wrong);
                            isValid = false;
                        } else {
                            factId = factory.get(0);
                        }
                    } else if (remark.get(0) == 5) {
                        factId = 28l;
                    }
                }

                if (isValid) {
                    try {
                        JSONObject job = new JSONObject();
                        job.put("vyearId", data[2]);
                        job.put("nentityUniId", txtfarmercode.getText().toString());
                        job.put("nplotNo", txtplotnoval.getText().toString());
                        job.put("nfactId", String.valueOf(factId));
                        job.put("nreasonId", String.valueOf(remark.get(0)));
                        job.put("nareaAllowed", area);
                        job.put("nexpectedYield", expectedyield);
                        job.put("equal", equal);

                        String action = activity.getResources().getString(R.string.actionsaveotheruti);
                        String servlet = activity.getResources().getString(R.string.servletharvdata);
                        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                        String versioncode = cf.getVersionCode();
                        RetrofitHandler<ActionResponse> extraplotrequesthandler = new RetrofitHandler<>();
                        Call<ActionResponse> call2 = apiInterface.actionHarvData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                        extraplotrequesthandler.handleRetrofit(call2, OtherUtilizationActivity.this, RequestEnum.SAVEOTHERUTI, servlet, activity, versioncode);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        final int[] prevlen = {0};
        edtcurrentareatxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String area = edtcurrentareatxt.getText().toString();
                if (area.length() == 1) {
                    if (area.charAt(0) == '.') {
                        edtcurrentareatxt.setText("0.");
                        edtcurrentareatxt.setSelection(edtcurrentareatxt.getText().toString().length());
                    } else if (prevlen[0] < 1) {
                        edtcurrentareatxt.setText(edtcurrentareatxt.getText().toString() + ".");
                        edtcurrentareatxt.setSelection(edtcurrentareatxt.getText().toString().length());
                    }
                }
                prevlen[0] = edtcurrentareatxt.length();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtcurrentareatxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String area = edtcurrentareatxt.getText().toString();
                    ServerValidation sv = new ServerValidation();
                    if (area.endsWith(".")) {
                        area = area.replace(".", "");
                    }
                    if (sv.checkFloat(area)) {
                        double darea = Double.parseDouble(area);
                        AppCompatTextView txtareatxt = findViewById(R.id.txtareatxt);
                        AppCompatTextView txtutiareatxt = findViewById(R.id.txtutiareatxt);
                        String utiarea = txtutiareatxt.getText().toString();
                        double originalArea = Double.parseDouble(txtareatxt.getText().toString());

                        if (sv.checkFloat(utiarea)) {
                            originalArea = originalArea - Double.parseDouble(utiarea);
                        }

                        if (darea > originalArea) {
                            edtcurrentareatxt.setError(String.format(getResources().getString(R.string.errorplotarea), String.valueOf(originalArea)));
                            edtcurrentareatxt.setText("");
                        } else {
                            DecimalFormat df = new DecimalFormat("#0.00");
                            edtcurrentareatxt.setText(MarathiHelper.convertMarathitoEnglish(df.format(darea)));
                        }
                    }
                }
            }
        });

    }

    private void setData(OtherUtilizationResponse otherUtilizationResponse) {
        Activity activity = this;
        AppCompatTextView txtplotnoval, txtfarmernametxt, txtvillagetxt, txtushangamtxt, txtvarietytxt, txtareatxt, txtfarmercode, txtexpectedyield, txtutiareatxt;
        SingleSpinnerSearch sspremark, sspfactory;

        txtplotnoval = findViewById(R.id.txtplotnoval);
        txtfarmernametxt = findViewById(R.id.txtfarmernametxt);
        txtfarmercode = findViewById(R.id.txtfarmercode);
        txtvillagetxt = findViewById(R.id.txtvillagetxt);
        txtushangamtxt = findViewById(R.id.txtushangamtxt);
        txtvarietytxt = findViewById(R.id.txtvarietytxt);
        txtareatxt = findViewById(R.id.txtareatxt);
        txtexpectedyield = findViewById(R.id.txtexpectedyield);
        txtutiareatxt = findViewById(R.id.txtutiareatxt);

        DBHelper dbHelper = new DBHelper(activity);
        HangamMaster hangamMaster = dbHelper.getHangamById(otherUtilizationResponse.getNhangamCode());
        VarietyMaster varietyMaster = dbHelper.getVarietyById(otherUtilizationResponse.getNvarietyCode());

        sspremark = findViewById(R.id.sspremark);
        sspfactory = findViewById(R.id.sspfactory);

        txtplotnoval.setText(otherUtilizationResponse.getNplotNo());
        txtfarmercode.setText(otherUtilizationResponse.getNentityUniId());
        txtfarmernametxt.setText(otherUtilizationResponse.getNentityUniId() + " " + otherUtilizationResponse.getVfarmerName());
        txtvillagetxt.setText(otherUtilizationResponse.getVvillageName());
        txtushangamtxt.setText(hangamMaster.getVhangamName());
        txtvarietytxt.setText(varietyMaster.getVvariety_name());
        txtareatxt.setText(otherUtilizationResponse.getNtentativeArea());
        txtexpectedyield.setText(otherUtilizationResponse.getNexpectedYield());
        txtutiareatxt.setText(otherUtilizationResponse.getNutilizationArea());

        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;

        ssh.initSingle(sspremark, activity, getResources().getString(R.string.remarksearch));
        ssh.initSingle(sspfactory, activity, getResources().getString(R.string.searchfactory));

        ssh.setSingleItems(sspremark, otherUtilizationResponse.getRemarkList(), DataSetter.REMARK);
        ssh.setSingleItems(sspfactory, otherUtilizationResponse.getFactoryList(), DataSetter.FACTORY);

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
        if (requestCaller == RequestEnum.SAVEOTHERUTI) {
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

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.REMARK) {
            AppCompatTextView txtfactorylbl = findViewById(R.id.txtfactorylbl);
            AppCompatTextView txtcollon3 = findViewById(R.id.txtcollon3);
            SingleSpinnerSearch sspfactory = findViewById(R.id.sspfactory);

            if (selectedItem.getId() == 1) {
                txtfactorylbl.setVisibility(View.VISIBLE);
                txtcollon3.setVisibility(View.VISIBLE);
                sspfactory.setVisibility(View.VISIBLE);
            } else {
                txtfactorylbl.setVisibility(View.GONE);
                txtcollon3.setVisibility(View.GONE);
                sspfactory.setVisibility(View.GONE);
            }
        }

    }
}