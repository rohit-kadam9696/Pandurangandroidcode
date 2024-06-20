package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PrinterConstant;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;

import retrofit2.Call;
import retrofit2.Response;

public class FertDistPrintActivity extends AppCompatActivity implements RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fert_dist_print);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = this;

        Intent intent = getIntent();
        String htmlContent = null, head = null;
        if (intent.hasExtra("html")) {
            htmlContent = intent.getStringExtra("html");
        }
        if (intent.hasExtra("head")) {
            head = intent.getStringExtra("head");
        }


        PrinterConstant print = new PrinterConstant();
        String printview = print.generatePrintByHTML(this, htmlContent, null, head);

        WebView wvprintview = findViewById(R.id.wvprintview);
        wvprintview.getSettings().setLoadWithOverviewMode(true);
        wvprintview.getSettings().setUseWideViewPort(true);
        wvprintview.getSettings().setAllowFileAccess(true);
        wvprintview.getSettings().setJavaScriptEnabled(true);
        wvprintview.getSettings().setBuiltInZoomControls(true);
        wvprintview.loadDataWithBaseURL(null, printview, "text/html", "utf-8", null);

        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        AppCompatButton btnexit = findViewById(R.id.btnexit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean isContinue = print.connectPrinter(activity);
                    if (isContinue) {
                        PrinterConstant.printContent = printview;
                        print.print(activity);
                    } else {
                        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PrinterConstant.printContent = printview;
                                print.print(activity);
                            }
                        }, 4000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FertDistPrintActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}