package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.GoogleMap;
import com.twd.chitboyapp.spsskl.R;

public class Screenshot implements GoogleMap.SnapshotReadyCallback {

    private Activity activity;
    private String fileName;
    private Intent nextIntent;

    private AlertDialog alertDialog;

    public Screenshot(Activity activity) {
        this.activity = activity;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Intent getNextIntent() {
        return nextIntent;
    }

    public void setNextIntent(Intent nextIntent) {
        this.nextIntent = nextIntent;
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public void setAlertDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    public String generateFileName(String filePrefix) {
        ConstantFunction cf = new ConstantFunction();
        long n = System.currentTimeMillis();
        String[] userId = cf.getSharedPrefValue(activity, new String[]{activity.getResources().getString(R.string.chitboyprefchit_boy_id)}, new String[]{""});
        String pictureFile = filePrefix + "_s_" + n + "u" + ((userId.length > 0) ? userId[0] : "") + ".jpg";
        pictureFile = MarathiHelper.convertMarathitoEnglish(pictureFile);
        return pictureFile;
    }

/*

    public String takeScreenshot(Activity activity, int viewId) {
        Date now = new Date();
        String fileName = android.text.format.DateFormat.format("yyyy-MM-dd_hh_mm_ss", now) + ".jpg";
        try {

            //FrameLayout memecontentView =(FrameLayout) findViewById(R.id.lltxtid);
            // View v1 = memecontentView;
            // create bitmap screen capture
            //View v1 = getWindow().getDecorView().getRootView();
            // View v1 = getWindow().getDecorView().getRootView();
            View v1 = activity.findViewById(viewId);
            */
/*v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());*//*

            Bitmap bitmap = Bitmap.createBitmap(
                    v1.getWidth(), v1.getHeight(), Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(bitmap);
            v1.draw(canvas);

            // save bitmap to file
            File imageFile = new File(activity.getExternalFilesDir(Constant.foldername), getFileName());
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
*/
/*

            // add the screenshot to the gallery
            MediaScannerConnection.scanFile(activity,new String[]{imageFile.getAbsolutePath()},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
*//*


            return fileName;
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        return null;
    }
*/

    @Override
    public void onSnapshotReady(@Nullable Bitmap bitmap) {
        String filePath = activity.getExternalFilesDir(Constant.foldername).getPath();
           /* File imageFile = new File(filePath, fileName);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();*/
        ImageCompressor.resizeAndCompressImageBeforeSend(activity, filePath + "/" + fileName, 200, bitmap);
        if (alertDialog != null)
            ConstantFunction.dismissDialog(alertDialog);
        activity.finish();

    }
}
