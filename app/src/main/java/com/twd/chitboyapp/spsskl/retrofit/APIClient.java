package com.twd.chitboyapp.spsskl.retrofit;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    //public static String BaseURL1 = "http://103.112.35.99:8080/SMSMPSSK_ChitBoy_WebServices/";
    //public static String BaseURL2 = "http://103.112.35.99:8081/SMSMPSSK_ChitBoy_WebServices/";
    public static String BaseURL1 = "http://spsskl1.3wdsoft.com:8080/PandurangSugarWebPanel/";
    public static String BaseURL2 = "http://spsskl2.3wdsoft.com:8080/PandurangSugarWebPanel/";

    public static String testBaseURL1 = "http://spsskl1.3wdsoft.com:8081/PandurangSugarWebPanel/";
    public static String testBaseURL2 = "http://spsskl2.3wdsoft.com:8081/PandurangSugarWebPanel/";

    public static String localBaseURL1 = "http://192.168.2.9:8080/PandurangSugarWebPanel/";
    public static String localBaseURL2 = "http://192.168.2.9:8080/PandurangSugarWebPanel/";

    public static String localtestBaseURL1 = "http://192.168.2.9:8081/PandurangSugarWebPanel/";
    public static String localtestBaseURL2 = "http://192.168.2.9:8081/PandurangSugarWebPanel/";

    public static String imagePath = "fetchimage?image=";
    public static String imagePathNumber = "fetchimage?type=number&image=";
    public static String imagePathSugar= "fetchimage?type=sugar&image=";
    public static Retrofit getClient(Activity activity, String servlet) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        HostSelectionInterceptor urlChanger = HostSelectionInterceptor.getInstance();
        String BaseURL = getCurrentUrl(activity);
        urlChanger.setHost(BaseURL + servlet);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(urlChanger).addInterceptor(interceptor).connectTimeout(30, TimeUnit.SECONDS).readTimeout(300, TimeUnit.SECONDS).writeTimeout(300, TimeUnit.SECONDS).callTimeout(300, TimeUnit.SECONDS).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit;
    }

    public static String getCurrentUrl(Activity activity) {
        String urltoreturn;
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{activity.getResources().getString(R.string.prefcurrentserver)};
        String[] data = cf.getSharedPrefValue(activity, key, new String[]{""});
        switch (data[0]) {
            case "1":
                urltoreturn = BaseURL1;
                break;
            case "2":
                urltoreturn = BaseURL2;
                break;
            case "3":
                urltoreturn = testBaseURL1;
                break;
            case "4":
                urltoreturn = testBaseURL2;
                break;
            case "5":
                urltoreturn = localBaseURL1;
                break;
            case "6":
                urltoreturn = localBaseURL2;
                break;
            case "7":
                urltoreturn = localtestBaseURL1;
                break;
            case "8":
                urltoreturn = localtestBaseURL2;
                break;
            default:
                urltoreturn = BaseURL1;
                break;
        }
        return urltoreturn;
    }

}
