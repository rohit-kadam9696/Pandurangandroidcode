package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;

public class WrongTimeActivity extends AppCompatActivity implements View.OnClickListener {
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_time);
        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);


        AppCompatButton btnclose = findViewById(R.id.btnclose);
        AppCompatButton btnretry = findViewById(R.id.btnretry);
        AppCompatButton btnsettings = findViewById(R.id.btnsettings);

        btnclose.setOnClickListener(this);
        btnretry.setOnClickListener(this);
        btnsettings.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.hasExtra("type")) {
            String type = intent.getStringExtra("type");
            if (type.equalsIgnoreCase("2")) {
                AppCompatTextView txtmsg = findViewById(R.id.txtmsg);
                txtmsg.setText(getResources().getString(R.string.serverdate));
            }
            if (intent.hasExtra("date")) {
                date = intent.getStringExtra("date");
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnclose) {
            finishAffinity();
        } else if (id == R.id.btnretry) {
            onBackPressed();
        } else if (id == R.id.btnsettings) {
            openSettings();
        }
    }

    public void openSettings() {
        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
        someActivityResultLauncher.launch(intent);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                onBackPressed();
            }
        }
    });

    @Override
    public void onBackPressed() {
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        if (date == null && dateTimeChecker.checkAutoDate(this, false)) super.onBackPressed();
        else if (date != null && dateTimeChecker.checkServerDate(this, false, date))
            super.onBackPressed();
    }


}