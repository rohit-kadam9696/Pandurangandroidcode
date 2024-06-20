package com.twd.chitboyapp.spsskl.constant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.twd.chitboyapp.spsskl.BuildConfig;
import com.twd.chitboyapp.spsskl.R;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConstantFunction {

    public static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        context.createConfigurationContext(configuration);
    }

    public String getImei(Context context) {
        String[] keyArr = new String[]{context.getResources().getString(R.string.chitboyprefdeviceuniqueid)};
        String[] deviceUniqueId = getSharedPrefValue(context, keyArr, new String[]{""});

        if (deviceUniqueId[0] == null || deviceUniqueId[0].equals("")) {
            String deviceUniqueIdr = getImeiOrUniqueDeiviceId(context);
            putSharedPrefValue(keyArr, new String[]{deviceUniqueIdr}, context);
            return deviceUniqueIdr;
        }
        return deviceUniqueId[0];
    }

    @SuppressLint("MissingPermission")
    public String getImeiOrUniqueDeiviceId(Context context) {
        String uniqueId;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE}, 12);
            }
            String imei = telephonyManager.getDeviceId();
            if (imei != null) uniqueId = imei;
            else uniqueId = getUniqueIdentifer(context);
        } catch (Exception e) {
            uniqueId = getUniqueIdentifer(context);
        }
        return MarathiHelper.convertMarathitoEnglish(uniqueId);
    }

    private static String getUniqueIdentifer(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void setEnglishApp(Context context) {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public void removeSharedPrefValue(Context context, String... removalKey) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.chitboypref), Context.MODE_PRIVATE).edit();
        for (String s : removalKey) {
            editor.remove(s);
        }
        editor.apply();
    }

    public String[] getSharedPrefValue(Context context, String[] keyArray, String[] deafaultArray) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.chitboypref), 0);
        int size = keyArray.length;
        String[] valueArray = new String[size];
        for (int k = 0; k < size; k++) {
            valueArray[k] = sharedPref.getString(keyArray[k], deafaultArray[k]);
        }
        return valueArray;
    }

    public void putSharedPrefValue(String[] keyArray, String[] valueArray, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.chitboypref), Context.MODE_PRIVATE).edit();
        int size = keyArray.length;
        for (int k = 0; k < size; k++) {
            editor.putString(keyArray[k], valueArray[k]);
        }
        editor.apply();
    }

    public void clearAllSharedPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.chitboypref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void deletePhotos(Activity activity, boolean message, ArrayList<String> deletePhoto) {
        AlertDialog dialog = ConstantFunction.createProgress(activity);
        ConstantFunction.showDialog(dialog);
        File root = activity.getExternalFilesDir("");
        File myDir = new File(root + Constant.foldername);

        for (String unimage : deletePhoto) {
            File file = new File(myDir, unimage);
            if (file.exists()) {
                file.delete();
            }
        }
        if (message)
            Constant.showToast(activity.getResources().getString(R.string.photodeleted), activity, R.drawable.ic_info);
        ConstantFunction.dismissDialog(dialog);
        /*AlertDialog dialog = ConstantFunction.createProgress(activity);
        ConstantFunction.showDialog(dialog);
        File root = activity.getExternalFilesDir("");
        File myDir = new File(root + Constant.foldername);
        File[] myfiles = myDir.listFiles();
        if (myfiles != null) {
            for (File f : myfiles) {
                if (f.isFile()) {
                    if (f.exists()) {
                        f.delete();
                    }
                }
            }
            if (message)
                Constant.showToast(activity.getResources().getString(R.string.photodeleted), activity, R.drawable.ic_info);
        }
        ConstantFunction.dismissDialog(dialog);*/
    }

    public static AlertDialog createProgress(Context context, String... msg) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false); // if you want user to wait for some process to finish,
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.progress_dialog, null);
            if (msg != null && msg.length > 0) {
                TextView progressmessage = view.findViewById(R.id.progressmessage);
                progressmessage.setText(msg[0]);
            }
            builder = builder.setView(view);
            return builder.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showDialog(AlertDialog dialog) {
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissDialog(AlertDialog dialog) {
        try {
            if (dialog.isShowing()) dialog.dismiss();
        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void showCustomAlert(Context context, int toastImage, String toastMessage) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View toastRoot = inflater.inflate(R.layout.toast, null);
        ImageView imgToast = toastRoot.findViewById(R.id.imgToast);
        TextView txtToast = toastRoot.findViewById(R.id.txtToast);
        txtToast.setText(toastMessage);
        imgToast.setImageResource(toastImage);
        Toast toast = new Toast(context);

        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }

    public void openWebSite(String url, Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public boolean hideKeyboard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return false;
    }

    public String getVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    public void openSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void setDateToView(AppCompatEditText date, int year, int monthOfYear, int dayOfMonth) {
        DecimalFormat df = new DecimalFormat("00");
        String datetoset = df.format(dayOfMonth) + "/" + df.format((monthOfYear + 1)) + "/" + year;
        datetoset = MarathiHelper.convertMarathitoEnglish(datetoset);
        date.setText(datetoset);
    }

    public void initDate(AppCompatTextView... date) {
        int size = date.length;
        if (size > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateToday = new Date();
            String datetoset = sdf.format(dateToday);
            datetoset = MarathiHelper.convertMarathitoEnglish(datetoset);
            for (AppCompatTextView appCompatTextView : date) {
                appCompatTextView.setText(datetoset);
            }
        }
    }

    public void initDate(AppCompatEditText... date) {
        int size = date.length;
        if (size > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateToday = new Date();
            String datetoset = sdf.format(dateToday);
            datetoset = MarathiHelper.convertMarathitoEnglish(datetoset);
            for (AppCompatEditText appCompatTextView : date) {
                appCompatTextView.setText(datetoset);
            }
        }
    }

    public static void playMedia(Context context, int sound) {

        MediaPlayer mediaPlayer = MediaPlayer.create(context, sound);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.start();
    }

    public void generateOfflinePlotNo(Activity activity) {
        AppCompatTextView txtnplotoffline = activity.findViewById(R.id.txtnplotoffline);
        long cu_id = System.currentTimeMillis();
        String[] data = getSharedPrefValue(activity, new String[]{activity.getResources().getString(R.string.chitboyprefchit_boy_id)}, new String[]{""});
        String chitboyid = data[0];
        String mbid = MarathiHelper.convertMarathitoEnglish("" + cu_id + "@" + chitboyid);
        txtnplotoffline.setText(mbid);
    }

    public void updateDateTime(AppCompatEditText date, String time) {
        String datet = date.getText().toString();
        String datetoset = datet.substring(0, 10) + " " + time;
        datetoset = MarathiHelper.convertMarathitoEnglish(datetoset);
        date.setText(datetoset);
    }

    public String getYearCode(Date date, int wsstartdate, int wsenddate) {
        String vyearcode = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        if (month >= wsstartdate) {
            vyearcode = year + "-" + (year + 1);
        } else {
            vyearcode = (year - 1) + "-" + year;
        }
        return vyearcode;
    }
}
