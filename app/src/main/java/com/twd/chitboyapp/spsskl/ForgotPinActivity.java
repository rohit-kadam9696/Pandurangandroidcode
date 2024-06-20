package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

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
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.MainResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class ForgotPinActivity extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pin);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        AppCompatEditText edtotpf = findViewById(R.id.edtotpf);
        AppCompatEditText edtloginpinf = findViewById(R.id.edtloginpinf);
        AppCompatEditText edtloginconfirmpinf = findViewById(R.id.edtloginconfirmpinf);
        AppCompatButton btnsetpinf = findViewById(R.id.btnsetpinf);

        btnsetpinf.setOnClickListener(this);

        edtotpf.requestFocus();
        ConstantFunction cf = new ConstantFunction();
        Activity activity = ForgotPinActivity.this;


        String action = getResources().getString(R.string.actionresendotp);
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.chitboyprefmobile)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        JSONObject job = new JSONObject();
        try {
            job.put("mobileno", data[2]);
            String servlet = activity.getResources().getString(R.string.servletapp_login);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<MainResponse> call2 = apiInterface.resendOtp(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<MainResponse> otpcaller = new RetrofitHandler<>();
            otpcaller.handleRetrofit(call2, ForgotPinActivity.this, RequestEnum.RESENDOTP, servlet, activity, versioncode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        edtotpf.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    moveNewPin();
                    return true;
                }
                return false;
            }
        });

        edtloginpinf.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    moveConfirm();
                    return true;
                }
                return false;
            }
        });

        edtloginconfirmpinf.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setConfirm();
                    return true;
                }
                return false;
            }
        });

        edtloginpinf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                moveConfirm();
            }
        });

        edtloginconfirmpinf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setConfirm();
            }
        });
    }

    private void moveNewPin() {
        AppCompatEditText edtotpf = findViewById(R.id.edtotpf);
        String oldloginpin = edtotpf.getText().toString();
        if (oldloginpin.length() >= 5) {
            AppCompatEditText edtloginpinf = findViewById(R.id.edtloginpinf);
            edtloginpinf.requestFocus();
        }
    }


    private void setConfirm() {
        AppCompatEditText edtloginconfirmpinf = findViewById(R.id.edtloginconfirmpinf);
        String confirmloginpin = edtloginconfirmpinf.getText().toString();
        if (confirmloginpin.length() == 4) {
            performSetPin();
        }
    }

    private void moveConfirm() {
        AppCompatEditText edtloginpinf = findViewById(R.id.edtloginpinf);
        String loginpin = edtloginpinf.getText().toString();
        if (loginpin.length() == 4) {
            AppCompatEditText edtloginconfirmpinf = findViewById(R.id.edtloginconfirmpinf);
            edtloginconfirmpinf.requestFocus();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnsetpinf) {
            performSetPin();
        }
    }

    private void performSetPin() {
        AppCompatEditText edtotpf = findViewById(R.id.edtotpf);
        AppCompatEditText edtloginpinf = findViewById(R.id.edtloginpinf);
        AppCompatEditText edtloginconfirmpinf = findViewById(R.id.edtloginconfirmpinf);
        String loginpins = edtloginpinf.getText().toString();
        String confirmpins = edtloginconfirmpinf.getText().toString();
        String forgototptext = edtotpf.getText().toString();
        ConstantFunction cf = new ConstantFunction();

        if (loginpins.length() == 4 && confirmpins.length() == 4 && loginpins.equals(confirmpins)) {

            ServerValidation sv = new ServerValidation();
            final Activity activity = ForgotPinActivity.this;
            if (sv.checkNumber(forgototptext) && sv.checkgreaterlength(forgototptext, 4)) {
                JSONObject job = new JSONObject();
                try {
                    String action = getResources().getString(R.string.actionverifyotp);

                    String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.chitboyprefmobile)};
                    String[] value = new String[]{"", "0", ""};
                    String[] data = cf.getSharedPrefValue(activity, key, value);
                    String otp = edtotpf.getText().toString();
                    job.put("mobileno", data[2]);
                    job.put("otp", otp);

                    String servlet = activity.getResources().getString(R.string.servletapp_login);
                    APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                    String versioncode = cf.getVersionCode();
                    Call<MainResponse> call2 = apiInterface.resendOtp(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                    RetrofitHandler<MainResponse> otpcaller = new RetrofitHandler<>();
                    otpcaller.handleRetrofit(call2, ForgotPinActivity.this, RequestEnum.VERIFYOTP, servlet, activity, versioncode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                edtotpf.setError(getResources().getString(R.string.errorwrongotp));
            }
        } else {
            Constant.showToast(getResources().getString(R.string.errorpinnotmatch), ForgotPinActivity.this, R.drawable.ic_wrong);
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
        if (requestCaller == RequestEnum.VERIFYOTP) {
            ConstantFunction cf = new ConstantFunction();
            AppCompatEditText edtloginpinf = findViewById(R.id.edtloginpinf);
            String loginpins = edtloginpinf.getText().toString();
            String[] keypin = new String[]{getResources().getString(R.string.chitboyprefloginpin)};
            cf.putSharedPrefValue(keypin, new String[]{loginpins}, ForgotPinActivity.this);
            Intent redirectint = new Intent(ForgotPinActivity.this, HomeActivity.class);
            startActivity(redirectint);
        } else if (requestCaller == RequestEnum.RESENDOTP) {
            // do nothing
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {
        if (requestCaller == RequestEnum.VERIFYOTP) {
            // do nothing
        } else if (requestCaller == RequestEnum.RESENDOTP) {
            // do nothing
        }
    }
}
