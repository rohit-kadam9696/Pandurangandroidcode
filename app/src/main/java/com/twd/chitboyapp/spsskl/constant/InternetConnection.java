package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.MainResponse;

import retrofit2.Call;

public class InternetConnection {


    //Check Network Status
    @Deprecated
    public static boolean isNetworkAvailable(final Activity context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean result = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!result)
            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    showAlertOnData(context);
                }
            });

        return result;
    }

    public static boolean isNetworkAvailableNormal(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Deprecated
    private static void showAlertOnData(final Activity context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_internet_on);

        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);


        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatButton btncancelinternet = dialog.findViewById(R.id.btncancelinternet);
        AppCompatButton btnretryinternet = dialog.findViewById(R.id.btnretryinternet);
        AppCompatButton btnsettinginternet = dialog.findViewById(R.id.btnsettinginternet);

        btncancelinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btnretryinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNetworkAvailable(context);
                dialog.cancel();
            }
        });


        btnsettinginternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public static <T extends MainResponse> boolean isNetworkAvailable(Activity activity, String servlet, Call<T> call2, RetrofitResponse<T> retrofitResponse, RequestEnum requestCaller, RetrofitHandler<T> tRetrofitHandler, String versioncode, Object... obj) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean result = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!result)
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    showAlertOnData(activity, servlet, call2, retrofitResponse, requestCaller, tRetrofitHandler, versioncode, obj);
                }
            });

        return result;
    }

    private static <T extends MainResponse> void showAlertOnData(Activity activity, String servlet, Call<T> call2, RetrofitResponse<T> retrofitResponse, RequestEnum requestCaller, RetrofitHandler<T> tRetrofitHandler, String versioncode, Object... obj) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_internet_on);

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatButton btncancelinternet = dialog.findViewById(R.id.btncancelinternet);
        AppCompatButton btnretryinternet = dialog.findViewById(R.id.btnretryinternet);
        AppCompatButton btnsettinginternet = dialog.findViewById(R.id.btnsettinginternet);

        btncancelinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btnretryinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tRetrofitHandler.handleRetrofit(call2, retrofitResponse, requestCaller, servlet, activity, versioncode, obj);
                dialog.cancel();
            }
        });


        btnsettinginternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                //dialog.cancel();
            }
        });

        dialog.show();
    }
}