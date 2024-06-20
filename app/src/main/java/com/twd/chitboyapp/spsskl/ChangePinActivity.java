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

public class ChangePinActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        AppCompatEditText edtoldloginpinc = findViewById(R.id.edtoldloginpinc);
        AppCompatEditText edtloginpinc = findViewById(R.id.edtloginpinc);
        AppCompatEditText edtloginconfirmpinc = findViewById(R.id.edtloginconfirmpinc);
        AppCompatButton btnsetpinc = findViewById(R.id.btnsetpinc);

        btnsetpinc.setOnClickListener(this);

        edtoldloginpinc.requestFocus();

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        edtoldloginpinc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    moveNewPin();
                    return true;
                }
                return false;
            }
        });

        edtloginpinc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    moveConfirm();
                    return true;
                }
                return false;
            }
        });

        edtloginconfirmpinc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setConfirm();
                    return true;
                }
                return false;
            }
        });

        edtoldloginpinc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                moveNewPin();
            }
        });

        edtloginpinc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                moveConfirm();
            }
        });

        edtloginconfirmpinc.addTextChangedListener(new TextWatcher() {
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

    private void moveNewPin() {
        AppCompatEditText edtoldloginpinc = findViewById(R.id.edtoldloginpinc);
        String oldloginpin = edtoldloginpinc.getText().toString();
        if (oldloginpin.length() == 4) {
            AppCompatEditText edtloginpinc = findViewById(R.id.edtloginpinc);
            edtloginpinc.requestFocus();
        }
    }


    private void setConfirm() {
        AppCompatEditText edtloginconfirmpinc = findViewById(R.id.edtloginconfirmpinc);
        String confirmloginpin = edtloginconfirmpinc.getText().toString();
        if (confirmloginpin.length() == 4) {
            performSetPin();
        }
    }

    private void moveConfirm() {
        AppCompatEditText edtloginpinc = findViewById(R.id.edtloginpinc);
        String loginpin = edtloginpinc.getText().toString();
        if (loginpin.length() == 4) {
            AppCompatEditText edtloginconfirmpinc = findViewById(R.id.edtloginconfirmpinc);
            edtloginconfirmpinc.requestFocus();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnsetpinc) {
            performSetPin();
        }

    }

    private void performSetPin() {
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefloginpin)};
        String[] data = cf.getSharedPrefValue(this, key, new String[]{""});
        String oldpin = data[0];
        AppCompatEditText edtoldloginpinc = findViewById(R.id.edtoldloginpinc);
        AppCompatEditText edtloginpinc = findViewById(R.id.edtloginpinc);
        AppCompatEditText edtloginconfirmpinc = findViewById(R.id.edtloginconfirmpinc);
        String oldloginpins = edtoldloginpinc.getText().toString();
        String loginpins = edtloginpinc.getText().toString();
        String confirmpins = edtloginconfirmpinc.getText().toString();
        if (!oldpin.equals(oldloginpins)) {
            Constant.showToast(getResources().getString(R.string.erroroldpinnotmatch), ChangePinActivity.this, R.drawable.ic_wrong);
        } else if (loginpins.length() == 4 && confirmpins.length() == 4 && loginpins.equals(confirmpins)) {
            cf.putSharedPrefValue(key, new String[]{loginpins}, this);
            startActivity(new Intent(ChangePinActivity.this, HomeActivity.class));
        } else {
            Constant.showToast(getResources().getString(R.string.errorpinnotmatch), ChangePinActivity.this, R.drawable.ic_wrong);
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