package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.twd.chitboyapp.spsskl.pojo.LoginResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements RetrofitResponse {


    @Override
    protected void onResume() {
        super.onResume();
        ConstantFunction.setEnglishApp(LoginActivity.this);
    }

    @Override
    public void onBackPressed() {
        Constant.onBackpress(LoginActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        ConstantFunction.setEnglishApp(LoginActivity.this);

        AppCompatEditText editTextMobileno = findViewById(R.id.edittextmobileno);
        AppCompatButton buttonLogin = findViewById(R.id.btnlogin);

        editTextMobileno.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editTextMobileno.clearFocus();
                    ConstantFunction cf = new ConstantFunction();
                    cf.hideKeyboard(textView, LoginActivity.this);
                    buttonLogin.performClick();
                    return true;
                }
                return false;
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String mobileno = editTextMobileno.getText().toString();
                ServerValidation sv = new ServerValidation();
                ConstantFunction cf = new ConstantFunction();
                if (!sv.checkMobile(mobileno)) {
                    cf.showCustomAlert(getApplicationContext(), R.mipmap.ic_action_error, getResources().getString(R.string.errorwrongmobile));
                } else {
                    JSONObject job = new JSONObject();
                    try {
                        job.put("mobileno", MarathiHelper.convertMarathitoEnglish(mobileno));
                        Activity activity = LoginActivity.this;
                        String servlet = activity.getResources().getString(R.string.servletapp_login);
                        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                        String versioncode = cf.getVersionCode();
                        String action = getResources().getString(R.string.actionlogin);
                        Call<LoginResponse> call2 = apiInterface.doLogin(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), versioncode);
                        RetrofitHandler<LoginResponse> logincaller = new RetrofitHandler<>();
                        logincaller.handleRetrofit(call2, LoginActivity.this, RequestEnum.LONGIN, servlet, activity, versioncode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MenuHandler mh = new MenuHandler();
        return mh.openCommon(this, item, null);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler mh = new MenuHandler();
        return mh.performRefreshOption(menu, this);
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.LONGIN) {
            AppCompatEditText editTextMobileno = findViewById(R.id.edittextmobileno);
            String mobileno = editTextMobileno.getText().toString();
            LoginResponse loginResponse = (LoginResponse) response.body();
            Intent intent = new Intent(LoginActivity.this, OTP_Activity.class);
            intent.putExtra("mobileno", mobileno);
            intent.putExtra("chitboyid", loginResponse.getChitBoyId());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {
        if (requestCaller == RequestEnum.LONGIN) {
            //Constant.showToast("Login Called Failure", LoginActivity.this, R.drawable.ic_info);
        }
    }
}