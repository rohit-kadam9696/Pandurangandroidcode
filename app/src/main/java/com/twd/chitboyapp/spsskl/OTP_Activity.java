package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.twd.chitboyapp.spsskl.pojo.MainResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class OTP_Activity extends AppCompatActivity implements RetrofitResponse {

    private static OTP_Activity activity;
    AppCompatEditText verifyOTP;
    AppCompatButton btnverify;

    @Override
    protected void onResume() {
        super.onResume();
        ConstantFunction.setEnglishApp(OTP_Activity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constant.onBackpress(OTP_Activity.this);
    }

    public static OTP_Activity instance() {
        return activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        activity = this;
    }


    public void ReciveMessage(final String newSms) {
        String numberOnly = newSms.replaceAll("[^0-9]", "");
        verifyOTP.setText(numberOnly);
        if (numberOnly.length() > 0) {
            btnverify.performClick();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        ConstantFunction.setEnglishApp(OTP_Activity.this);

        try {

            AppCompatTextView verifyotptextview = findViewById(R.id.verifyotptextview);
            AppCompatTextView changeMobile = findViewById(R.id.changemobile);
            AppCompatTextView countdown = findViewById(R.id.countdown);
            AppCompatTextView resend = findViewById(R.id.resend);
            verifyOTP = findViewById(R.id.verifyotp);
            btnverify = findViewById(R.id.btnverify);

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String mobileno, chitboyid;
            if (extras != null) {
                mobileno = extras.getString("mobileno");
                chitboyid = extras.getString("chitboyid");
            } else {
                Constant.showToast(getResources().getString(R.string.errormissingparam), activity, R.drawable.ic_wrong);
                finish();
                return;
            }

            ConstantFunction cf = new ConstantFunction();
            cf.putSharedPrefValue(new String[]{getResources().getString(R.string.chitboyprefchit_boy_id)}, new String[]{chitboyid}, this);

            String otptext = String.format(getResources().getString(R.string.verifyotptext), mobileno);
            verifyotptextview.setText(otptext);
            startCounter(OTP_Activity.this, countdown, resend);

            changeMobile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    startActivity(new Intent(OTP_Activity.this, LoginActivity.class));
                }
            });

            verifyOTP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        verifyOTP.clearFocus();
                        ConstantFunction cf = new ConstantFunction();
                        cf.hideKeyboard(textView, OTP_Activity.this);
                        btnverify.performClick();
                        return true;
                    }
                    return false;
                }
            });

            resend.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    JSONObject job = new JSONObject();
                    try {
                        String action = getResources().getString(R.string.actionresendotp);
                        Activity activity = OTP_Activity.this;
                        ConstantFunction cf = new ConstantFunction();
                        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
                        String[] value = new String[]{"", "0"};
                        String[] data = cf.getSharedPrefValue(activity, key, value);
                        job.put("mobileno", mobileno);
                        String servlet = activity.getResources().getString(R.string.servletapp_login);
                        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                        String versioncode = cf.getVersionCode();
                        Call<MainResponse> call2 = apiInterface.resendOtp(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                        RetrofitHandler<MainResponse> otphandler = new RetrofitHandler<>();
                        otphandler.handleRetrofit(call2, OTP_Activity.this, RequestEnum.RESENDOTP, servlet, activity, versioncode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    countdown.setVisibility(View.VISIBLE);
                    resend.setVisibility(View.GONE);
                    startCounter(OTP_Activity.this, countdown, resend);
                }
            });

            btnverify.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    ConstantFunction cf = new ConstantFunction();
                    if (mobileno == null) {
                        cf.showCustomAlert(getApplicationContext(), R.mipmap.ic_action_error, getResources().getString(R.string.errorwrongmobileonotp));
                    } else {
                        JSONObject job = new JSONObject();
                        try {
                            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
                            String[] value = new String[]{"", "0"};
                            String[] data = cf.getSharedPrefValue(activity, key, value);
                            String otp = verifyOTP.getText().toString();
                            job.put("mobileno", mobileno);
                            job.put("otp", otp);

                            String action = getResources().getString(R.string.actionverifyotp);

                            String servlet = activity.getResources().getString(R.string.servletapp_login);
                            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                            String versioncode = cf.getVersionCode();
                            Call<MainResponse> call2 = apiInterface.resendOtp(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                            RetrofitHandler<MainResponse> otpcaller = new RetrofitHandler<>();
                            otpcaller.handleRetrofit(call2, OTP_Activity.this, RequestEnum.VERIFYOTP, servlet, activity, versioncode);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void startCounter(final Context context, final TextView countdown, final TextView resend) {
        new CountDownTimer(context.getResources().getInteger(R.integer.countdowntime), context.getResources().getInteger(R.integer.countdowntimeinterval)) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                countdown.setText(context.getResources().getString(R.string.resendcount) + " " + String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                countdown.setVisibility(View.INVISIBLE);
                resend.setVisibility(View.VISIBLE);
            }
        }.start();
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
            Intent redirectint = new Intent(OTP_Activity.activity, LoginPinActivity.class);
            startActivity(redirectint);
            finish();
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