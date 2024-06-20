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
import com.twd.chitboyapp.spsskl.constant.HarvPlotDetailsHandler;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.pojo.HarvPlotDetailsResponse;
import com.twd.chitboyapp.spsskl.pojo.HarvSlipDetailsResponse;

import java.io.Serializable;

public class HarvPlotDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harv_plot_details);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Intent intent = getIntent();
        HarvPlotDetailsResponse plotDetailsResponse = null;
        if (intent.hasExtra("plotdetails")) {
            plotDetailsResponse = (HarvPlotDetailsResponse) intent.getSerializableExtra("plotdetails");
            HarvPlotDetailsHandler harvPlotDetailsHandler = new HarvPlotDetailsHandler();
            harvPlotDetailsHandler.setData(plotDetailsResponse, HarvPlotDetailsActivity.this);
        }

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        HarvPlotDetailsResponse finalPlotDetailsResponse = plotDetailsResponse;
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = null;
                Integer openintent = finalPlotDetailsResponse.getOpenIntent();
                if (openintent == null || openintent <= 1) {
                    intent1 = new Intent(HarvPlotDetailsActivity.this, HarvTransInfoActivity.class);
                    if (finalPlotDetailsResponse instanceof HarvSlipDetailsResponse) {
                        intent1.putExtra("harvdetails", (HarvSlipDetailsResponse) finalPlotDetailsResponse);
                    }
                } else if (openintent == 2) {
                    intent1 = new Intent(HarvPlotDetailsActivity.this, ExtraPlotRequestActivity.class);
                    intent1.putExtra("reasonlist", (Serializable) finalPlotDetailsResponse.getReasonList());
                } else if (openintent == 3) {
                    intent1 = new Intent(HarvPlotDetailsActivity.this, ExtraStatusRequestActivity.class);
                    intent1.putExtra("message", finalPlotDetailsResponse.getVstatusMessage());
                }
                if (intent1 != null) {
                    startActivity(intent1);
                }
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