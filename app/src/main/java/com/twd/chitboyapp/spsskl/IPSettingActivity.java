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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PrinterConstant;
import com.twd.chitboyapp.spsskl.printer.Bluetooth;
import com.twd.chitboyapp.spsskl.printer.DeviceListActivity;

public class IPSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipsetting);

        Activity activity = this;
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        AppCompatButton myprinter = findViewById(R.id.myprinter);
        myprinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bluetooth.connectPrinter(getApplicationContext(), activity);
            }
        });


        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.prefcurrentserver), getResources().getString(R.string.prefofficer)};
        String[] data = cf.getSharedPrefValue(activity, key, new String[]{"", "0"});
        if (!data[1].equals("113")) {
            RadioButton rbtestserver1 = findViewById(R.id.rbtestserver1);
            RadioButton rbtestserver2 = findViewById(R.id.rbtestserver2);
            RadioButton rblocalserver1 = findViewById(R.id.rblocalserver1);
            RadioButton rblocalserver2 = findViewById(R.id.rblocalserver2);
            RadioButton rblocaltestserver1 = findViewById(R.id.rblocaltestserver1);
            RadioButton rblocaltestserver2 = findViewById(R.id.rblocaltestserver2);
            rbtestserver1.setVisibility(View.GONE);
            rbtestserver2.setVisibility(View.GONE);
            rblocalserver1.setVisibility(View.GONE);
            rblocalserver2.setVisibility(View.GONE);
            rblocaltestserver1.setVisibility(View.GONE);
            rblocaltestserver2.setVisibility(View.GONE);
        }
        RadioButton rbserver;
        switch (data[0]) {
            case "1":
                rbserver = findViewById(R.id.rbserver1);
                break;
            case "2":
                rbserver = findViewById(R.id.rbserver2);
                break;
            case "3":
                rbserver = findViewById(R.id.rbtestserver1);
                break;
            case "4":
                rbserver = findViewById(R.id.rbtestserver2);
                break;
            case "5":
                rbserver = findViewById(R.id.rblocalserver1);
                break;
            case "6":
                rbserver = findViewById(R.id.rblocalserver2);
                break;
            case "7":
                rbserver = findViewById(R.id.rblocaltestserver1);
                break;
            case "8":
                rbserver = findViewById(R.id.rblocaltestserver2);
                break;
            default:
                rbserver = findViewById(R.id.rbserver1);
                break;
        }
        rbserver.setChecked(true);

        RadioGroup rgServerSetting = findViewById(R.id.rgserversetting);
        rgServerSetting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String[] key = new String[]{getResources().getString(R.string.prefcurrentserver)};
                if (checkedId == R.id.rbserver1) {
                    String[] value = new String[]{"1"};
                    cf.putSharedPrefValue(key, value, activity);
                } else if (checkedId == R.id.rbserver2) {
                    String[] value = new String[]{"2"};
                    cf.putSharedPrefValue(key, value, activity);
                } else if (checkedId == R.id.rbtestserver1) {
                    String[] value = new String[]{"3"};
                    cf.putSharedPrefValue(key, value, activity);
                } else if (checkedId == R.id.rbtestserver2) {
                    String[] value = new String[]{"4"};
                    cf.putSharedPrefValue(key, value, activity);
                } else if (checkedId == R.id.rblocalserver1) {
                    String[] value = new String[]{"5"};
                    cf.putSharedPrefValue(key, value, activity);
                } else if (checkedId == R.id.rblocalserver2) {
                    String[] value = new String[]{"6"};
                    cf.putSharedPrefValue(key, value, activity);
                } else if (checkedId == R.id.rblocaltestserver1) {
                    String[] value = new String[]{"7"};
                    cf.putSharedPrefValue(key, value, activity);
                } else if (checkedId == R.id.rblocaltestserver2) {
                    String[] value = new String[]{"8"};
                    cf.putSharedPrefValue(key, value, activity);
                }
            }
        });
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


}