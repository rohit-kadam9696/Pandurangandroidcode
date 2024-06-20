package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.PrinterConstant;
import com.twd.chitboyapp.spsskl.printer.Bluetooth;
import com.twd.chitboyapp.spsskl.printer.DeviceListActivity;

public class PrintTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_test);

        Activity activity = PrintTestActivity.this;
        AppCompatButton btntestprint = findViewById(R.id.btntestprint);
        AppCompatButton btnConnectPrinter = findViewById(R.id.btnConnectPrinter);
        AppCompatButton btnexit = findViewById(R.id.btnexit);
        PrinterConstant.printContent = "";
        setPrintEnableDisable(btntestprint);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        btntestprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrinterConstant printerConstant = new PrinterConstant();
                PrinterConstant.printContent = printerConstant.getPrintHeader("उस पावती", null);
                PrinterConstant.printContent += printerConstant.getPrintFooter(activity);
                printerConstant.print(activity);
            }
        });

        btnConnectPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bluetooth.connectPrinter(getApplicationContext(), activity);
            }
        });

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean setPrintEnableDisable(AppCompatButton btntestprint) {
        Activity activity = this;
        boolean isPrinter = Bluetooth.isPrinterConnected(activity, activity);
        //btntestprint.setEnabled(isPrinter);
        return isPrinter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppCompatButton btntestprint = findViewById(R.id.btntestprint);
        setPrintEnableDisable(btntestprint);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Activity activity = this;
        if (requestCode == PrinterConstant.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            Bluetooth.pairPrinter(getApplicationContext(), activity);
        } else if (requestCode == PrinterConstant.REQUEST_CONNECT_DEVICE && resultCode == RESULT_OK) {
            String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            Bluetooth.pairedPrinterAddress(getApplicationContext(), activity, address);

            AppCompatButton btntestprint = findViewById(R.id.btntestprint);
            final int[] wait = {0};
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (wait[0] < 500) {
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (setPrintEnableDisable(btntestprint)) {
                                    wait[0] = 1000;
                                }
                            }
                        });
                        wait[0]++;
                    }
                }
            }).start();

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