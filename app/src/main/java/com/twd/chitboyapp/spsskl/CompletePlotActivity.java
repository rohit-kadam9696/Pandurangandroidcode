package com.twd.chitboyapp.spsskl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.adapter.CompletePlotAdapter;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.pojo.CompletePlotResponse;

public class CompletePlotActivity extends AppCompatActivity {

    String isclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_plot);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(CompletePlotActivity.this);
        connectionUpdator.startObserve(CompletePlotActivity.this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Intent intent = getIntent();
        if (intent.hasExtra("completePlotResponse")) {
            CompletePlotResponse completePlotResponse = (CompletePlotResponse) intent.getSerializableExtra("completePlotResponse");
            isclose = intent.getStringExtra("isclose");
            setData(completePlotResponse);
        } else {
            Constant.showToast(getResources().getString(R.string.errornodata), CompletePlotActivity.this, R.drawable.ic_wrong);
            finish();
        }

    }

    private void setData(CompletePlotResponse completePlotResponse) {
        AppCompatTextView txtseasonval, txtfarmercodetxt, txtfarmernametxt, txtvillagetxt, txtsectiontxt;

        txtseasonval = findViewById(R.id.txtseasonval);
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtfarmernametxt = findViewById(R.id.txtfarmernametxt);
        txtvillagetxt = findViewById(R.id.txtvillagetxt);
        txtsectiontxt = findViewById(R.id.txtsectiontxt);

        txtseasonval.setText(completePlotResponse.getVyearId());
        txtfarmercodetxt.setText(completePlotResponse.getNentityUniId());
        txtfarmernametxt.setText(completePlotResponse.getVfarmerName());
        txtvillagetxt.setText(completePlotResponse.getVvillageName());
        txtsectiontxt.setText(completePlotResponse.getVsectName());

        if (completePlotResponse.getPlotList() != null) {
            CompletePlotAdapter completePlotAdapter = new CompletePlotAdapter(completePlotResponse.getPlotList(), CompletePlotActivity.this, isclose);
            RecyclerView mmlist = findViewById(R.id.mmlist);
            GridLayoutManager mLayoutManager1 = new GridLayoutManager(CompletePlotActivity.this, 1);
            mmlist.setLayoutManager(mLayoutManager1);
            mmlist.setItemAnimator(new DefaultItemAnimator());
            mmlist.setAdapter(completePlotAdapter);
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