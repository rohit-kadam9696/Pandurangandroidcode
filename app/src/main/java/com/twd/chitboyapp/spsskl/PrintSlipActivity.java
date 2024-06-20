package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PrinterConstant;
import com.twd.chitboyapp.spsskl.pojo.SlipBeanR;
import com.twd.chitboyapp.spsskl.printer.Bluetooth;
import com.twd.chitboyapp.spsskl.printer.DeviceListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrintSlipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_slip);

        Activity activity = PrintSlipActivity.this;
        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Intent intent = getIntent();
        List<SlipBeanR> slipBeanRList = (List<SlipBeanR>) intent.getSerializableExtra("slipBeanRList");
        String date = intent.getStringExtra("date");
        boolean oldslip = intent.getBooleanExtra("oldslip", false);

        intent.removeExtra("slipBeanRList");
        intent.removeExtra("date");
        intent.removeExtra("oldslip");
        PrinterConstant print = new PrinterConstant();
        ArrayList<String> printview = print.generatePrintView(this, slipBeanRList, date, oldslip ? getResources().getString(R.string.slipdurusti) : null, false, true);

        WebView wvprintview = findViewById(R.id.wvprintview);
        wvprintview.getSettings().setLoadWithOverviewMode(true);
        wvprintview.getSettings().setUseWideViewPort(true);
        wvprintview.getSettings().setAllowFileAccess(true);
        wvprintview.getSettings().setJavaScriptEnabled(true);
        wvprintview.getSettings().setBuiltInZoomControls(true);
        wvprintview.loadDataWithBaseURL(null, printview.get(0), "text/html", "utf-8", null);

        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        AppCompatButton btnexit = findViewById(R.id.btnexit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean isContinue = print.connectPrinter(activity);
                    if (isContinue) {
                        ArrayList<String> printview = print.generatePrintView(PrintSlipActivity.this, slipBeanRList, date, oldslip ? getResources().getString(R.string.slipdurusti) : null, true, true);
                        performPrint(printview, 0, print, activity);
                    } else {

                        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<String> printview = print.generatePrintView(PrintSlipActivity.this, slipBeanRList, date, oldslip ? getResources().getString(R.string.slipdurusti) : null, true, true);
                                performPrint(printview, 0, print, activity);
                                // print.generatePrintView(PrintSlipActivity.this, slipBeanRList, date, true);
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
                Intent intent1 = new Intent(PrintSlipActivity.this, HomeActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void performPrint(ArrayList<String> printview, int pos, PrinterConstant print, Activity activity) {
        PrinterConstant.printContent = printview.get(pos);
        print.print(activity);

        if (pos + 1 < printview.size()) {
            Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.setContentView(R.layout.popup_delete);
            dialog.setCancelable(false);

            int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);

            dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

            AppCompatTextView txt_deletehead = dialog.findViewById(R.id.txt_deletehead);
            AppCompatTextView message = dialog.findViewById(R.id.message);

            txt_deletehead.setText(getResources().getString(R.string.information));
            message.setText(getResources().getString(R.string.nextgroupprint));

            AppCompatButton btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
            AppCompatButton btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);
            btnconfirmdelete.setEnabled(false);
            int wait = 20000;
            if (pos == 0) {
                wait = 25000;
            }
            new CountDownTimer(wait, getResources().getInteger(R.integer.countdowntimeinterval)) { // adjust the milli seconds here

                public void onTick(long millisUntilFinished) {
                    btnconfirmdelete.setText(getResources().getString(R.string.hoy) + " (" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + ")");
                }

                public void onFinish() {
                    btnconfirmdelete.setText(getResources().getString(R.string.hoy));
                    btnconfirmdelete.setEnabled(true);
                }
            }.start();
            btncanceldelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    performPrint(printview, pos + 1, print, activity);
                    dialog.cancel();
                }
            });
            dialog.show();
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
    public void onBackPressed() {
        super.onBackPressed();
        AppCompatButton btnexit = findViewById(R.id.btnexit);
        btnexit.performClick();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Activity activity = this;
        if (requestCode == PrinterConstant.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            PrinterConstant printerConstant = new PrinterConstant();
            printerConstant.connectPrinter(activity);
            //Bluetooth.pairPrinter(getApplicationContext(), activity);
        } else if (requestCode == PrinterConstant.REQUEST_CONNECT_DEVICE && resultCode == RESULT_OK) {
            String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            Bluetooth.pairedPrinterAddress(getApplicationContext(), activity, address);

            if (PrinterConstant.printContent != null && !PrinterConstant.printContent.equals("")) {
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PrinterConstant printerConstant = new PrinterConstant();
                        printerConstant.print(activity);
                    }
                }, 4000);
            }
        }
    }
}