package com.twd.chitboyapp.spsskl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;

public class NondConfirmjaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nond_confirmja);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        String mnu_id = getIntent().getStringExtra("mnu_id");

        AppCompatButton btnjune, btnaug, btnreconfirm;
        btnjune = findViewById(R.id.btnjune);
        btnaug = findViewById(R.id.btnaug);
        btnreconfirm = findViewById(R.id.btnreconfirm);

        btnjune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSelectFarmer("1", mnu_id);
            }
        });

        btnaug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSelectFarmer("2", mnu_id);
            }
        });

        btnreconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSelectFarmer("3", mnu_id);
            }
        });
    }

    private void openSelectFarmer(String rujtype, String mnu_id) {
        Intent intent = new Intent(NondConfirmjaActivity.this, SelectFarmerActivity.class);
        intent.putExtra("rujtype", rujtype);
        intent.putExtra("mnu_id", mnu_id);
        startActivity(intent);
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