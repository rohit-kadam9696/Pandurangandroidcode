package com.twd.chitboyapp.spsskl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        ConstantFunction.setEnglishApp(SuccessActivity.this);

        Intent prev = getIntent();
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        ConstantFunction cf = new ConstantFunction();

        String area = prev.getStringExtra("gpsarea");
        String distance = prev.getStringExtra("gpsdistance");
        String offlineplotno = prev.getStringExtra("offlineplotno");

        AppCompatTextView txttotalarea = findViewById(R.id.txttotalarea);
        AppCompatTextView txtkharkhana = findViewById(R.id.txtkarkhana);
        AppCompatTextView txtofflineplotno = findViewById(R.id.txtofflineplotno);


        txttotalarea.setText(getResources().getString(R.string.totalarea) + " " + MarathiHelper.convertMarathitoEnglish(area));
        txtkharkhana.setText(getResources().getString(R.string.karkhanadistance) + " " + MarathiHelper.convertMarathitoEnglish(distance));
        txtofflineplotno.setText(getResources().getString(R.string.plotnooffline) + " " + offlineplotno);


        AppCompatButton btnnextmap = findViewById(R.id.btnnextsuccess);
        AppCompatButton btnnextmain = findViewById(R.id.btnnextmain);

        btnnextmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessActivity.this, SelectFarmerActivity.class);
                intent.putExtra("mnu_id", "2");
                startActivity(intent);
            }
        });

        btnnextmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        ConstantFunction.playMedia(SuccessActivity.this, R.raw.save);
    }

    @Override
    public void onBackPressed() {
        Button btnnextmain = findViewById(R.id.btnnextmain);
        btnnextmain.performClick();
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