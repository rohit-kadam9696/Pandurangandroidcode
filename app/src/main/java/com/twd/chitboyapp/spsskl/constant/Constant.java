package com.twd.chitboyapp.spsskl.constant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.twd.chitboyapp.spsskl.R;

public class Constant {

    public static LocationUpdatetor locationUpdatetor;
    public static Boolean exit = false;
    public static String foldername = "/photos";

    public static Double factLatitude = 17.85160;
    public static Double factLongitude = 75.10289;

    @SuppressLint("NewApi")
    public static void onBackpress(Activity context) {
        if (exit) {
            context.finishAffinity(); // finish activity
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.backclick),
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }

    }

    public static void showToast(final String toastMessage, final Activity context, final int toastImage) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                ConstantFunction toastFunction = new ConstantFunction();
                toastFunction.showCustomAlert(context, toastImage, toastMessage);
            }
        });
    }

}
