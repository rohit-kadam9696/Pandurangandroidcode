package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.adapter.CaneSampleListAdapter;
import com.twd.chitboyapp.spsskl.constant.CaneSampleMaster;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.pojo.CaneSamplePlantationData;

public class CaneSampleInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cane_sample_info);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Intent intent = getIntent();
        Activity activity = this;
        if (intent.hasExtra("canedataresponse")) {
            CaneSamplePlantationData caneSamplePlantationData = (CaneSamplePlantationData) intent.getSerializableExtra("canedataresponse");
            CaneSampleMaster caneSampleMaster = new CaneSampleMaster();
            caneSampleMaster.setMasterData(activity, caneSamplePlantationData);

            CaneSampleListAdapter caneSampleListAdapter = new CaneSampleListAdapter(caneSamplePlantationData.getCaneSampleList());
            RecyclerView mmlist = findViewById(R.id.mmlist);
            GridLayoutManager mLayoutManager1 = new GridLayoutManager(activity, 1);
            mmlist.setLayoutManager(mLayoutManager1);
            mmlist.setItemAnimator(new DefaultItemAnimator());
            mmlist.setAdapter(caneSampleListAdapter);
        } else {
            finish();
        }

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
}