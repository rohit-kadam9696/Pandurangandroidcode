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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.LocationUpdatetor;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PrinterConstant;
import com.twd.chitboyapp.spsskl.enums.BackPress;
import com.twd.chitboyapp.spsskl.interfaces.LocationUpdateListener;

public class SugarReceiptReprintActivity extends AppCompatActivity implements LocationUpdateListener {

    BackPress backpress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_receipt_reprint);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = this;

        Intent intent = getIntent();
        String htmlContent = null, head = null, mainHead = null;
        if (intent.hasExtra("html")) {
            htmlContent = intent.getStringExtra("html");
        }
        if (intent.hasExtra("head")) {
            head = intent.getStringExtra("head");
        }
        if (intent.hasExtra("mainhead")) {
            mainHead = intent.getStringExtra("mainhead");
        }
        if (intent.hasExtra("backpress")) {
            backpress = (BackPress) intent.getSerializableExtra("backpress");
        }
        AppCompatTextView txtlatlongacc = findViewById(R.id.txtlatlongacc);
        txtlatlongacc.setVisibility(View.GONE);
        if (backpress == BackPress.PRINT || backpress == BackPress.NUMPRINT) {
            if (backpress == BackPress.PRINT) {
                mainHead = getResources().getString(R.string.sugarhead);
            } else {
                mainHead = getResources().getString(R.string.numberhead);
            }
            txtlatlongacc.setVisibility(View.VISIBLE);
            if (Constant.locationUpdatetor != null) {
                Constant.locationUpdatetor.updateLocationUpdatetor(activity);
                Constant.locationUpdatetor.locationCaller = this;
            } else {
                startLocation(activity);
            }
        }

        PrinterConstant print = new PrinterConstant();
        String printview = print.generatePrintByHTML(this, htmlContent, mainHead, head);

        System.out.println("printview " + printview);

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
                Intent intent1 = null;
                if (backpress == BackPress.REPRINT)
                    intent1 = new Intent(SugarReceiptReprintActivity.this, HomeActivity.class);
                else if (backpress == BackPress.PRINT) {
                    intent1 = new Intent(SugarReceiptReprintActivity.this, OnlineFarmerActivity.class);
                    intent1.putExtra("mnu_id", "57");
                } else if (backpress == BackPress.NUMPRINT)
                    intent1 = new Intent(SugarReceiptReprintActivity.this, NumNumberActivity.class);
                else if (backpress == BackPress.NUMLISTPRINT)
                    //intent1 = new Intent(SugarReceiptReprintActivity.this, NumLotGenerationActivity.class);
                    onBackPressed();
                else return;
                if (intent1 != null)
                    startActivity(intent1);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.locationUpdatetor.REQUEST_CHECK_SETTINGS) {
                Constant.locationUpdatetor.startLocationButtonClick();
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == Constant.locationUpdatetor.REQUEST_CHECK_SETTINGS) {
                Constant.locationUpdatetor.stopLocationUpdates();
            }
        }
    }

    @Override
    public void onLocationUpdate(double lats, double longs, double accuracy) {
        Constant.locationUpdatetor.updateUIData(SugarReceiptReprintActivity.this, lats, longs, accuracy);
    }

    private void startLocation(Activity activity) {
        Constant.locationUpdatetor = new LocationUpdatetor(activity);
        Constant.locationUpdatetor.locationCaller = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constant.locationUpdatetor != null)
            Constant.locationUpdatetor.startLocationButtonClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Constant.locationUpdatetor.stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Constant.locationUpdatetor.stopLocationUpdates();
    }

}