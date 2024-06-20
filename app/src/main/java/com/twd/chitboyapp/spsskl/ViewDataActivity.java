package com.twd.chitboyapp.spsskl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;

public class ViewDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        //DBHelper dbHelper = new DBHelper(ViewDataActivity.this);
        Intent indata = getIntent();
        //String type = indata.getStringExtra("type");
        //Toast.makeText(Form_Data.this, type, Toast.LENGTH_LONG).show();
        //int typeint = Integer.parseInt(type);
        //JSONArray jar = dbHelper.getAllPlantationDataList(typeint);
        /*DynamicTableData dtd = new DynamicTableData();
        TableLayout tbl = findViewById(R.id.tblformyadi);
        dtd.addTable(ViweDataActivity.this, jar, tbl, (typeint == 1), (typeint == 1), true, typeint);*/


        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

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