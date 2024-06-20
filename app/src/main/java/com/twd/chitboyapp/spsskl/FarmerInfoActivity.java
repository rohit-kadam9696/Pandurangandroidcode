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
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.pojo.VillageMaster;

public class FarmerInfoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_info);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        String farmercode = getIntent().getStringExtra("farmercode");
        setFarmer("F" + farmercode);
        buttonOperation();
    }

    private void buttonOperation() {
        AppCompatButton btnbirthday, btnmobileno, btnadharcard, btnbankinfo, btnback;
        btnbirthday = findViewById(R.id.btnbirthday);
        btnmobileno = findViewById(R.id.btnmobileno);
        btnadharcard = findViewById(R.id.btnadharcard);
        btnbankinfo = findViewById(R.id.btnbankinfo);
        btnback = findViewById(R.id.btnback);

        btnbirthday.setOnClickListener(this);
        btnmobileno.setOnClickListener(this);
        btnadharcard.setOnClickListener(this);
        btnbankinfo.setOnClickListener(this);
        btnback.setOnClickListener(this);
    }

    private void setFarmer(String farmerCodeF) {
        DBHelper dbHelper = new DBHelper(FarmerInfoActivity.this);
        EntityMasterDetail entityMasterDetail = dbHelper.getEntityById(farmerCodeF);
        VillageMaster villageMaster = dbHelper.getVillageById(String.valueOf(entityMasterDetail.getNvillageId()));

        AppCompatTextView txtfarmercodetxt, txtfarmernametxt, txtfarmervilltxt;
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtfarmernametxt = findViewById(R.id.txtfarmernametxt);
        txtfarmervilltxt = findViewById(R.id.txtfarmervilltxt);

        txtfarmercodetxt.setText(farmerCodeF);
        txtfarmernametxt.setText(entityMasterDetail.getVentityNameLocal());
        if (villageMaster.getVillageNameLocal() != null) {
            txtfarmervilltxt.setText(villageMaster.getVillageNameLocal());
        } else {

        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = null;
        if (id == R.id.btnbirthday) {
            intent = new Intent(FarmerInfoActivity.this, FarmerBirthdayActivity.class);
        } else if (id == R.id.btnmobileno) {
            intent = new Intent(FarmerInfoActivity.this, FarmerMobilenoActivity.class);
        } else if (id == R.id.btnadharcard) {
            intent = new Intent(FarmerInfoActivity.this, FarmerAdharcardActivity.class);
        } else if (id == R.id.btnbankinfo) {
            intent = new Intent(FarmerInfoActivity.this, FarmerBankinfoActivity.class);
        } else if (id == R.id.btnback) {
            finish();
        }
        if (intent != null) {
            AppCompatTextView txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
            intent.putExtra("farmercode", txtfarmercodetxt.getText().toString());
            startActivity(intent);
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