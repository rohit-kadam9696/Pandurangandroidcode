package com.twd.chitboyapp.spsskl;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.twd.chitboyapp.spsskl.constant.CacheHandler;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.pojo.AppUpdate;
import com.twd.chitboyapp.spsskl.pojo.MainResponse;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    final int RequestPermissionCode = 1;
    boolean permissions_grant = false;

    @Override
    protected void onResume() {
        super.onResume();
        ConstantFunction.setEnglishApp(MainActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constant.onBackpress(MainActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CacheHandler ch = new CacheHandler();
        ch.deleteCache(MainActivity.this);

        ConstantFunction.setEnglishApp(MainActivity.this);
        DBHelper db = new DBHelper(MainActivity.this);
        ArrayList<String> deletePhoto = db.deletePlantation();

        ConstantFunction cf = new ConstantFunction();
        cf.deletePhotos(MainActivity.this, false, deletePhoto);

        AppCompatButton btnwelcomenext = findViewById(R.id.btnwelcomenext);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(MainActivity.this, true);
        btnwelcomenext.setVisibility(View.GONE);

        Objects.requireNonNull(getSupportActionBar()).hide();

        String[] data = cf.getSharedPrefValue(this, new String[]{getResources().getString(R.string.chitboyprefmobile), getResources().getString(R.string.chitboyprefloginpin), getResources().getString(R.string.chitboyprefcompulsoryupdate), getResources().getString(R.string.chitboyprefupdatehead), getResources().getString(R.string.chitboyprefupdatemsg), getResources().getString(R.string.chitboyprefupdateurl)}, new String[]{"", "", "", "", "", ""});

        final String mno = data[0];
        final String pin = data[1];
        RequestMultiplePermission();

        if (data[2].equals(cf.getVersionCode())) {
            RetrofitHandler<MainResponse> retrofitHandler = new RetrofitHandler<>();
            MainResponse mainResponse = new MainResponse();
            mainResponse.setUpdate(true);
            AppUpdate appUpdate = new AppUpdate();
            appUpdate.setForceUpdate(true);
            appUpdate.setHead(data[3]);
            appUpdate.setMessage(data[4]);
            appUpdate.setUpdateUrl(data[5]);
            mainResponse.setUpdateResponse(appUpdate);
            retrofitHandler.showUpdatePopup(MainActivity.this, mainResponse, cf, null, null, null, null, cf.getVersionCode());
        } else {
            int SPLASH_TIME_OUT = getResources().getInteger(R.integer.SPLASH_TIME_OUT);
            new Handler(Looper.myLooper()).postDelayed(() -> {
                btnwelcomenext.setVisibility(View.VISIBLE);
                if (permissions_grant) {
                    movenext(mno, pin);
                }
            }, SPLASH_TIME_OUT);
        }
        btnwelcomenext.setOnClickListener((v) -> movenext(mno, pin));

        TextView twdsite = findViewById(R.id.twdsite);
        twdsite.setOnClickListener((v) -> cf.openWebSite("http://www.3wdsoft.com", MainActivity.this));

    }

    private void movenext(String mno, String pin) {
        Intent i;
        if (mno.equals("")) {
            i = new Intent(MainActivity.this, LoginActivity.class);
        } else if (pin.equals("")) {
            i = new Intent(MainActivity.this, LoginPinActivity.class);
        } else {
            i = new Intent(MainActivity.this, AuthanticateActivity.class);
        }
        startActivity(i);
        finish();
    }

    private void RequestMultiplePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.USE_BIOMETRIC,
                            Manifest.permission.CAMERA,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH_SCAN
                    }, RequestPermissionCode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.USE_BIOMETRIC,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH_SCAN
                    }, RequestPermissionCode);
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.USE_BIOMETRIC,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN
                    }, RequestPermissionCode);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.USE_BIOMETRIC,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN
                    }, RequestPermissionCode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.USE_BIOMETRIC,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN
                    }, RequestPermissionCode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.USE_FINGERPRINT,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN
                    }, RequestPermissionCode);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN
                    }, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestPermissionCode) {
            permissions_grant = true;
            if (grantResults.length > 0) {
                int permissionlen = permissions.length;
                for (int i = 0; i < permissionlen; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        permissions_grant = false;
                        break;
                    }
                }

                if (!permissions_grant) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                    dialog.setContentView(R.layout.popup_delete);

                    int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);

                    dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

                    AppCompatTextView txt_deletehead = dialog.findViewById(R.id.txt_deletehead);
                    AppCompatTextView message = dialog.findViewById(R.id.message);

                    txt_deletehead.setText(getResources().getString(R.string.warninghead));
                    message.setText(getResources().getString(R.string.errorpermission));

                    AppCompatButton btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
                    AppCompatButton btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);

                    btncanceldelete.setText(getResources().getText(R.string.close));
                    btnconfirmdelete.setText(getResources().getText(R.string.setting));

                    btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            finishAffinity();
                            dialog.cancel();
                        }
                    });

                    btncanceldelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishAffinity();
                            dialog.cancel();
                        }
                    });

                    dialog.show();
                    /*ConstantFunction cfd = new ConstantFunction();
                    cfd.showCustomAlert(MainActivity.this, R.mipmap.ic_action_error, getResources().getString(R.string.errorpermission));
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);*/
                }
            }
        }
    }
}