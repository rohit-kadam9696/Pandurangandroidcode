package com.twd.chitboyapp.spsskl.constant;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.twd.chitboyapp.spsskl.BuildConfig;
import com.twd.chitboyapp.spsskl.R;

import java.io.File;
import java.util.List;

public class ImageConstant {
    public static int REQUEST_CAPTURE_IMAGE = 101;
    public static int REQUEST_CAPTURE_IMAGE_ONLINE = 103;
    //public static String pictureFilePath;
    //public static String pictureFileName;

    public void checkForPermission(Activity activity, int caller, String filePrefix, boolean timeStamp) {
        Dexter.withContext(activity)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            loadCamera(activity, caller, filePrefix, timeStamp);
                        } else {
                            ConstantFunction cf = new ConstantFunction();
                            cf.openSettings(activity);
                            Constant.showToast(activity.getResources().getString(R.string.errorreuiredPermission), activity, R.drawable.ic_wrong);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).check();
    }

    public void openCameraIntent(Activity activity, String filePrefix, boolean timeStamp) {
        openCameraIntentWithRequest(activity, REQUEST_CAPTURE_IMAGE, filePrefix, timeStamp);
    }

    public void openCameraIntentWithRequest(Activity activity, int Request_Code, String filePrefix, boolean timeStamp) {
        checkForPermission(activity, Request_Code, filePrefix, timeStamp);
    }

    private void loadCamera(Activity activity, int Request_Code, String filePrefix, boolean timeStamp) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //cameraIntent.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            //activity.startActivityForResult(cameraIntent, Request_Code);
            File pictureFile = getPictureFile(activity, filePrefix, timeStamp);
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(cameraIntent, Request_Code);
            }
        }
    }

    private File getPictureFile(Activity activity, String filePrefix, boolean timeStamp) {
        ConstantFunction cf = new ConstantFunction();
        long n = System.currentTimeMillis();
        String[] userId = cf.getSharedPrefValue(activity, new String[]{activity.getResources().getString(R.string.chitboyprefchit_boy_id)}, new String[]{""});
        String pictureFile = filePrefix + ((timeStamp) ? "_s_" + n + "u" + ((userId.length > 0) ? userId[0] : "") : "") + ".jpg";
        pictureFile = MarathiHelper.convertMarathitoEnglish(pictureFile);

        File root = activity.getExternalFilesDir("");
        File myDir = new File(root + Constant.foldername);
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        File image = new File(myDir, pictureFile);
        String pictureFilePath = image.getAbsolutePath();
        //String pictureFileName = image.getName();
        cf.putSharedPrefValue(new String[]{activity.getResources().getString(R.string.preflastpicpath), activity.getResources().getString(R.string.preflastpicname)}, new String[]{pictureFilePath, pictureFile}, activity);
        return image;
    }

    public String compressAndGetImage(int requestCode, int resultCode, Activity activity, int size) {
        if ((requestCode == REQUEST_CAPTURE_IMAGE || requestCode == REQUEST_CAPTURE_IMAGE_ONLINE) && resultCode == Activity.RESULT_OK) {
            ConstantFunction cf = new ConstantFunction();
            String pictureFilePath = null;
            String[] data = cf.getSharedPrefValue(activity, new String[]{activity.getResources().getString(R.string.preflastpicpath), activity.getResources().getString(R.string.preflastpicname)}, new String[]{"", ""});
            pictureFilePath = data[0];
            ImageCompressor.resizeAndCompressImageBeforeSend(activity, pictureFilePath, size, null);
            return data[1];
        }
        return "";
    }

}
