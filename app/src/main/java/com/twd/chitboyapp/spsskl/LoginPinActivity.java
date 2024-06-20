package com.twd.chitboyapp.spsskl;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;

import java.util.HashMap;

public class LoginPinActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pin);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        AppCompatEditText edtloginpins = findViewById(R.id.edtloginpins);
        AppCompatEditText edtloginconfirmpins = findViewById(R.id.edtloginconfirmpins);
        AppCompatButton btnsetpin = findViewById(R.id.btnsetpin);

        btnsetpin.setOnClickListener(this);
        edtloginpins.requestFocus();

        DBHelper dbHelper = new DBHelper(LoginPinActivity.this);
        HashMap<String, Long> count = dbHelper.count(DBHelper.entityTable, DBHelper.menuTable);
        if (count.get(DBHelper.entityTable) == 0 && count.get(DBHelper.menuTable) == 0) {
            MenuHandler menuHandler = new MenuHandler();
            menuHandler.refreshOperation(LoginPinActivity.this, null);
        }

        edtloginpins.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    movenext();
                    return true;
                }
                return false;
            }
        });

        edtloginconfirmpins.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setConfirm();
                    return true;
                }
                return false;
            }
        });

        edtloginpins.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                movenext();
            }
        });

        edtloginconfirmpins.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setConfirm();
            }
        });
    }

    private void setConfirm() {
        AppCompatEditText edtloginconfirmpins = findViewById(R.id.edtloginconfirmpins);
        String confirmloginpin = edtloginconfirmpins.getText().toString();
        if (confirmloginpin.length() == 4) {
            performSetPin();
        }
    }

    private void movenext() {
        AppCompatEditText edtloginpins = findViewById(R.id.edtloginpins);
        String loginpin = edtloginpins.getText().toString();
        if (loginpin.length() == 4) {
            AppCompatEditText edtloginconfirmpins = findViewById(R.id.edtloginconfirmpins);
            edtloginconfirmpins.requestFocus();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnsetpin) {
            performSetPin();
        }
    }

    private void performSetPin() {
        AppCompatEditText edtloginpins = findViewById(R.id.edtloginpins);
        AppCompatEditText edtloginconfirmpins = findViewById(R.id.edtloginconfirmpins);
        String loginpins = edtloginpins.getText().toString();
        String confirmpins = edtloginconfirmpins.getText().toString();
        ConstantFunction cf = new ConstantFunction();
        if (loginpins.length() == 4 && confirmpins.length() == 4 && loginpins.equals(confirmpins)) {
            String[] key = new String[]{getResources().getString(R.string.chitboyprefloginpin)};
            cf.putSharedPrefValue(key, new String[]{loginpins}, this);
            Intent intent = new Intent(LoginPinActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Constant.showToast(getResources().getString(R.string.errorpinnotmatch), LoginPinActivity.this, R.drawable.ic_wrong);
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