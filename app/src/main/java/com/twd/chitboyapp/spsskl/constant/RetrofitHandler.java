package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.MainActivity;
import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.AppUpdate;
import com.twd.chitboyapp.spsskl.pojo.MainResponse;
import com.twd.chitboyapp.spsskl.pojo.ServerError;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.chitboyapp.spsskl.retrofit.HostSelectionInterceptor;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitHandler<T extends MainResponse> {

    Integer firstServer = null;
    static int maxServer = 8;
    static ArrayList<Integer> skiplist;

    static {
        skiplist = new ArrayList<>();
        skiplist.add(3);
        skiplist.add(4);
        skiplist.add(5);
        skiplist.add(6);
        skiplist.add(7);
        skiplist.add(8);
    }

    public void handleRetrofit(Call<T> call2, RetrofitResponse<T> retrofitResponse, RequestEnum requestCaller, String servletname, Activity activity, String versionCode, Object... obj) {
        if (InternetConnection.isNetworkAvailable(activity, servletname, call2, retrofitResponse, requestCaller, this, versionCode, obj)) {
            ConstantFunction cf = new ConstantFunction();
            final AlertDialog dialog = ConstantFunction.createProgress(activity);
            ConstantFunction.showDialog(dialog);
            String[] key = new String[]{activity.getResources().getString(R.string.prefcurrentserver)};
            String[] data = cf.getSharedPrefValue(activity, key, new String[]{"0"});
            if (firstServer == null)
                firstServer = Integer.parseInt(data[0]);
            call2.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    ConstantFunction.dismissDialog(dialog);
                    if (response.isSuccessful()) {
                        T sc = response.body();
                        if (sc != null) {
                            if (!sc.isUpdate()) {
                                proccedRequest(activity, sc, cf, requestCaller, retrofitResponse, call, response, obj);
                            } else {
                                showUpdatePopup(activity, sc, cf, requestCaller, retrofitResponse, call, response, versionCode, obj);
                            }
                        } else {
                            Constant.showToast(activity.getResources().getString(R.string.invalidresponse), activity, R.drawable.ic_wrong);
                        }
                    } else {
                        Constant.showToast(response.message(), activity, R.drawable.ic_wrong);
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    ConstantFunction.dismissDialog(dialog);
                    if (t instanceof SocketTimeoutException) {
                        int curserver = Integer.parseInt(data[0]);
                        do {
                            curserver = curserver + 1;
                            if (curserver > maxServer) {
                                curserver = 1;
                            }
                            if (firstServer == curserver) {
                                break;
                            }
                        } while (skiplist.contains(curserver));
                        if (firstServer != curserver) {
                            cf.putSharedPrefValue(key, new String[]{String.valueOf(curserver)}, activity);
                            Constant.showToast(activity.getResources().getString(R.string.checkingwithanother), activity, R.drawable.ic_wrong);
                            HostSelectionInterceptor urlChanger = HostSelectionInterceptor.getInstance();
                            urlChanger.setHost(APIClient.getCurrentUrl(activity) + servletname);
                            handleRetrofit(call2.clone(), retrofitResponse, requestCaller, servletname, activity, versionCode, obj);
                        } else {
                            cf.putSharedPrefValue(key, new String[]{String.valueOf(firstServer)}, activity);
                            Constant.showToast(activity.getResources().getString(R.string.errorallserverdown), activity, R.drawable.ic_wrong);
                        }
                    } else {
                        Constant.showToast(t.getMessage(), activity, R.drawable.ic_wrong);
                        retrofitResponse.onFailure(call, t, requestCaller, obj);
                    }
                }
            });
        }
    }

    public void showUpdatePopup(Activity activity, T sc, ConstantFunction cf, RequestEnum requestCaller, RetrofitResponse<T> retrofitResponse, Call<T> call, Response<T> response, String versioncode, Object... obj) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_update);
        dialog.setCancelable(false);

        AppUpdate appUpdate = sc.getUpdateResponse();
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatTextView txthead = dialog.findViewById(R.id.txthead);
        AppCompatTextView txtmessage = dialog.findViewById(R.id.txtmessage);
        AppCompatCheckBox chkdonotshow = dialog.findViewById(R.id.chkdonotshow);
        AppCompatImageView imgrefresh = dialog.findViewById(R.id.imgrefresh);

        txthead.setText(appUpdate.getHead());
        txtmessage.setText(appUpdate.getMessage());

        AppCompatButton btncancel = dialog.findViewById(R.id.btncancel);
        AppCompatButton btnconfirm = dialog.findViewById(R.id.btnconfirm);
        if (appUpdate.isForceUpdate()) {
            btncancel.setVisibility(View.GONE);
            chkdonotshow.setVisibility(View.GONE);
            cf.putSharedPrefValue(new String[]{activity.getResources().getString(R.string.chitboyprefcompulsoryupdate), activity.getResources().getString(R.string.chitboyprefupdatehead), activity.getResources().getString(R.string.chitboyprefupdatemsg), activity.getResources().getString(R.string.chitboyprefupdateurl)}, new String[]{versioncode, appUpdate.getHead(), appUpdate.getMessage(), appUpdate.getUpdateUrl()}, activity);
        } else {
            btncancel.setVisibility(View.VISIBLE);
            chkdonotshow.setVisibility(View.VISIBLE);
            String[] data = cf.getSharedPrefValue(activity, new String[]{activity.getResources().getString(R.string.chitboyprefignoreversion), activity.getResources().getString(R.string.chitboyprefcompulsoryupdate)}, new String[]{"", "0"});
            if (data[0].equals(versioncode) && !data[1].equalsIgnoreCase(versioncode)) {
                if (retrofitResponse != null) {
                    proccedRequest(activity, sc, cf, requestCaller, retrofitResponse, call, response, obj);
                    return;
                }
            }
        }

        imgrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cf.removeSharedPrefValue(activity, activity.getResources().getString(R.string.chitboyprefignoreversion), activity.getResources().getString(R.string.chitboyprefcompulsoryupdate), activity.getResources().getString(R.string.chitboyprefupdatehead), activity.getResources().getString(R.string.chitboyprefupdatemsg), activity.getResources().getString(R.string.chitboyprefupdateurl));
                dialog.cancel();
                if (activity instanceof MainActivity) {
                    activity.startActivity(new Intent(activity, MainActivity.class));
                }
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkdonotshow.isChecked()) {
                    cf.putSharedPrefValue(new String[]{activity.getResources().getString(R.string.chitboyprefignoreversion)}, new String[]{versioncode}, activity);
                }
                dialog.cancel();
                if (retrofitResponse != null)
                    proccedRequest(activity, sc, cf, requestCaller, retrofitResponse, call, response, obj);
            }
        });

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cf.openWebSite(appUpdate.getUpdateUrl(), activity);
            }
        });

        dialog.show();
    }

    private void proccedRequest(Activity activity, T sc, ConstantFunction cf, RequestEnum requestCaller, RetrofitResponse<T> retrofitResponse, Call<T> call, Response<T> response, Object... obj) {
        putSuccessResponseValue(activity, sc, cf, requestCaller);
        if (sc.isSuccess()) {
            DateTimeChecker dtd = new DateTimeChecker();
            if (sc.getCurrentDateTime() == null || dtd.checkServerDate(activity, true, sc.getCurrentDateTime()))
                retrofitResponse.onResponse(call, response, requestCaller, activity, obj);
        } else {
            showServerError(sc.getSe(), activity, sc.getUniquestring());
        }
    }

    private void putSuccessResponseValue(Activity activity, MainResponse sc, ConstantFunction cf, RequestEnum requestCaller) {
        String[] key;
        String[] value;
        if (requestCaller == RequestEnum.LONGIN) {
            key = new String[]{activity.getResources().getString(R.string.chitboyprefuniquestring)};
            value = new String[]{sc.getUniquestring()};
        } else {
            key = new String[]{
                    activity.getResources().getString(R.string.chitboyprefuniquestring),
                    activity.getResources().getString(R.string.chitboyprefgroupids),
                    activity.getResources().getString(R.string.prefyearcode),
                    activity.getResources().getString(R.string.prefofficer),
                    activity.getResources().getString(R.string.prefharvestingyearcode),
                    activity.getResources().getString(R.string.chitboyprefpermissiondt),
                    activity.getResources().getString(R.string.chitboyprefname),
                    activity.getResources().getString(R.string.chitboyprefdesignation),
                    activity.getResources().getString(R.string.chitboyprefslipboycode),
                    activity.getResources().getString(R.string.chitboyprefmobileno),
                    activity.getResources().getString(R.string.prefreportingyearcode),
                    activity.getResources().getString(R.string.preffertyearcode),
                    activity.getResources().getString(R.string.preffromtimerawana),
                    activity.getResources().getString(R.string.preftotimerawana),
                    activity.getResources().getString(R.string.preflocationid),
                    activity.getResources().getString(R.string.preflocationname),
                    activity.getResources().getString(R.string.prefsugtypeid),
                    activity.getResources().getString(R.string.prefyardid),
                    activity.getResources().getString(R.string.prefpumpid),
                    activity.getResources().getString(R.string.prefpumpunitid),
                    activity.getResources().getString(R.string.prefpumpunitname)};
            value = new String[]{
                    sc.getUniquestring(),
                    sc.getPggroups(),
                    sc.getYearCode(),
                    sc.getNuserRoleId(),
                    sc.getHarvestingYearCode(),
                    sc.getPerbygroup(),
                    sc.getVfullName(),
                    sc.getDesignation(),
                    sc.getSlipboycode(),
                    sc.getMobileno(),
                    sc.getVrrtgYear(),
                    sc.getVfertYear(),
                    sc.getFromTimeRawana(),
                    sc.getToTimeRawana(),
                    String.valueOf(sc.getNlocationId()),
                    sc.getLocationName(),
                    String.valueOf(sc.getNsugTypeId()),
                    String.valueOf(sc.getNyardId()),
                    String.valueOf(sc.getNpumpId()),
                    String.valueOf(sc.getNpumpUnitId()),
                    sc.getVpumpUnitName()};
        }
        cf.putSharedPrefValue(key, value, activity);
    }

    public void showServerError(ServerError se, Activity activity, String uniquestring) {
        ConstantFunction cf = new ConstantFunction();
        if (se == null) {
            Constant.showToast(activity.getResources().getString(R.string.errorserver), activity, R.drawable.ic_wrong);
            return;
        }
        String errorMessage = se.getMsg();
        if (errorMessage == null) {
            switch (se.getError()) {
                case 1:
                    errorMessage = activity.getResources().getString(R.string.errorrelogin);
                    break;
                case 2:
                    errorMessage = activity.getResources().getString(R.string.errorreloginimei);
                    break;
                case 3:
                    errorMessage = activity.getResources().getString(R.string.errorrelogindeactive);
                    break;
                case 4:
                    errorMessage = activity.getResources().getString(R.string.errorreloginnotregister);
                    break;
                case 5:
                    errorMessage = activity.getResources().getString(R.string.errorreloginnotallow);
                    break;
                default:
                    errorMessage = activity.getResources().getString(R.string.errorserver);
                    break;
            }
        }

        Constant.showToast(errorMessage, activity, R.drawable.ic_wrong);
        if (se.getError() <= 5) {
            deactivateUser(activity, cf);
            activity.startActivity(new Intent(activity, MainActivity.class));
            return;
        }
        String[] key = new String[]{activity.getResources().getString(R.string.chitboyprefuniquestring)};
        String[] value = new String[]{uniquestring};
        cf.putSharedPrefValue(key, value, activity);
    }

    public void deactivateUser(Context context, ConstantFunction cf) {
        cf.clearAllSharedPref(context);
    }
}
