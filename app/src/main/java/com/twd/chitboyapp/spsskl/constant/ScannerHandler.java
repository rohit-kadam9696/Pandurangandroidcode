package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;

import com.journeyapps.barcodescanner.ScanOptions;
import com.twd.chitboyapp.spsskl.R;

public class ScannerHandler {
    private Activity activity;
    private ActivityResultLauncher<ScanOptions> barcodeLauncher;

    public ScannerHandler(Activity activity, ActivityResultLauncher<ScanOptions> barcodeLauncher) {
        this.activity = activity;
        this.barcodeLauncher = barcodeLauncher;
    }

    public void openScanner() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
            options.setPrompt(activity.getResources().getString(R.string.scanqrcode));
            options.setCameraId(0); // Use a specific camera of the device

            options.setBeepEnabled(true);
            options.setBarcodeImageEnabled(true);
            barcodeLauncher.launch(options);
        } else {
            Constant.showToast(activity.getResources().getString(R.string.errornosupportfunctionality), activity, R.drawable.ic_wrong);
        }
    }
}
