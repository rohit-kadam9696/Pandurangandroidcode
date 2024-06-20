package com.twd.chitboyapp.spsskl.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.AgriReportActivity;
import com.twd.chitboyapp.spsskl.CaneInwardDailyActivity;
import com.twd.chitboyapp.spsskl.CaneTransitActivity;
import com.twd.chitboyapp.spsskl.CrushingReportActivity;
import com.twd.chitboyapp.spsskl.ExtraPlotRequestListActivity;
import com.twd.chitboyapp.spsskl.FarmerRequestListActivity;
import com.twd.chitboyapp.spsskl.FertDistHomeActivity;
import com.twd.chitboyapp.spsskl.GutKhadeActivity;
import com.twd.chitboyapp.spsskl.HarvFarmerTonnageActivity;
import com.twd.chitboyapp.spsskl.HarvPlanActivity;
import com.twd.chitboyapp.spsskl.HarvPlanIndviualActivity;
import com.twd.chitboyapp.spsskl.HarvProgramStartActivity;
import com.twd.chitboyapp.spsskl.HarvReportActivity;
import com.twd.chitboyapp.spsskl.NondConfirmjaActivity;
import com.twd.chitboyapp.spsskl.NondReportActivity;
import com.twd.chitboyapp.spsskl.NumIndExcludeActivity;
import com.twd.chitboyapp.spsskl.NumIndWaitReportActivity;
import com.twd.chitboyapp.spsskl.NumInwardSummaryActivity;
import com.twd.chitboyapp.spsskl.NumLotGenerationActivity;
import com.twd.chitboyapp.spsskl.NumLotPrintNumActivity;
import com.twd.chitboyapp.spsskl.NumNumberActivity;
import com.twd.chitboyapp.spsskl.NumStopStartNumActivity;
import com.twd.chitboyapp.spsskl.NumTokenPassReprintActivity;
import com.twd.chitboyapp.spsskl.NumVehicleRegistrationReportActivity;
import com.twd.chitboyapp.spsskl.NumYardAssignActivity;
import com.twd.chitboyapp.spsskl.NumYardBalanceActivity;
import com.twd.chitboyapp.spsskl.OnlineFarmerActivity;
import com.twd.chitboyapp.spsskl.PlotSelectionActivity;
import com.twd.chitboyapp.spsskl.PumpPumpAssignActivity;
import com.twd.chitboyapp.spsskl.PumpRateEntryActivity;
import com.twd.chitboyapp.spsskl.PumpSaleEntryActivity;
import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.RawanaReportActivity;
import com.twd.chitboyapp.spsskl.RemainingSlipActivity;
import com.twd.chitboyapp.spsskl.SelectFarmerActivity;
import com.twd.chitboyapp.spsskl.SlipNoActivity;
import com.twd.chitboyapp.spsskl.SugarDailyOutwardActivity;
import com.twd.chitboyapp.spsskl.SugarInwardActivity;
import com.twd.chitboyapp.spsskl.SugarSaleSummaryActivity;
import com.twd.chitboyapp.spsskl.UserRoleChangeActivity;
import com.twd.chitboyapp.spsskl.VillageAssignActivity;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DataSender;
import com.twd.chitboyapp.spsskl.constant.InternetConnection;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.FarmerList;
import com.twd.chitboyapp.spsskl.enums.RawanaEnum;
import com.twd.chitboyapp.spsskl.interfaces.UpdateRemain;
import com.twd.chitboyapp.spsskl.pojo.PlantationBean;
import com.twd.chitboyapp.spsskl.vh.SubTabHolder;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerListener;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SubTabAdapter extends RecyclerView.Adapter<SubTabHolder> {


    private final Activity activity;
    private final JSONArray data;
    private final String groupid;

    public SubTabAdapter(JSONArray data, Activity context, String groupid) {
        this.data = data;
        this.activity = context;
        this.groupid = groupid;
    }

    @NonNull
    @Override
    public SubTabHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subhead, parent, false);
        return new SubTabHolder(v);
    }

    @Override
    public void onBindViewHolder(final SubTabHolder holder, final int position) {
        try {
            JSONObject job = data.getJSONObject(holder.getBindingAdapterPosition());
            holder.txtmnuname.setText(job.getString("mnu_name"));
            holder.txtmnuname.setTag(job);

            int imageid = activity.getResources().getIdentifier("st" + (job.has("vimg_name") ? job.getString("vimg_name") : ""), "drawable", activity.getPackageName());
            if (imageid != 0) {
                Drawable dr = activity.getResources().getDrawable(imageid);
                if (dr != null) holder.imgmenuimg.setImageDrawable(dr);
            } else {
                holder.imgmenuimg.setImageDrawable(activity.getResources().getDrawable(R.drawable.st));
            }

            holder.submenucard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (groupid.equals("7")) {
                            ConstantFunction cf = new ConstantFunction();
                            String[] key = new String[]{activity.getResources().getString(R.string.preflocationid), activity.getResources().getString(R.string.prefsugtypeid)};
                            String[] value = new String[]{"", ""};
                            String data[] = cf.getSharedPrefValue(activity, key, value);

                            ServerValidation sv = new ServerValidation();
                            if (sv.checkNumber(data[0]) && sv.checkNumber(data[1])) {
                                Integer locationId = Integer.parseInt(data[0]);
                                Integer sugTypeId = Integer.parseInt(data[1]);
                                if (locationId <= 0 || sugTypeId <= 0) {
                                    Constant.showToast(activity.getResources().getString(R.string.errornorequireddataforsugarsale), activity, R.drawable.ic_wrong);
                                    return;
                                }
                            } else {
                                Constant.showToast(activity.getResources().getString(R.string.errornorequireddataforsugarsale), activity, R.drawable.ic_wrong);
                                return;
                            }
                        } else if (groupid.equals("8")) {
                            ConstantFunction cf = new ConstantFunction();
                            String[] key = new String[]{activity.getResources().getString(R.string.preflocationid)};
                            String[] value = new String[]{""};
                            String data[] = cf.getSharedPrefValue(activity, key, value);

                            ServerValidation sv = new ServerValidation();
                            if (sv.checkNumber(data[0])) {
                                Integer locationId = Integer.parseInt(data[0]);
                                if (locationId <= 0) {
                                    Constant.showToast(activity.getResources().getString(R.string.errornorequireddataforkhatvatap), activity, R.drawable.ic_wrong);
                                    return;
                                }
                            } else {
                                Constant.showToast(activity.getResources().getString(R.string.errornorequireddataforkhatvatap), activity, R.drawable.ic_wrong);
                                return;
                            }
                        }
                        String mnu_id = job.getString("mnu_id");
                        if ((groupid.equals("1") || groupid.equals("2") || groupid.equals("3") || groupid.equals("4") || groupid.equals("8") || mnu_id.equals("27")) && !mnu_id.equals("3") && !mnu_id.equals("1") && !mnu_id.equals("18") && !mnu_id.equals("66")) {
                            loadPopup(holder);
                        } else {
                            nextActivity(holder, null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void nextActivity(SubTabHolder holder, String yearcode) {
        try {
            JSONObject job = new JSONObject(holder.txtmnuname.getTag().toString());
            String mnu_id = job.getString("mnu_id");
            String mnu_name = job.getString("mnu_name");
            proceedAction(mnu_id, mnu_name, yearcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void proceedAction(String mnu_id, String mnu_name, String yearcode) {
        if (!mnu_id.equals("2") && !mnu_id.equals("5")) {
            if (!InternetConnection.isNetworkAvailable(activity)) {
                return;
            }
        }
        ConstantFunction cf = new ConstantFunction();
        cf.removeSharedPrefValue(activity, activity.getResources().getString(R.string.prefharvslipdata));
        Intent intent = null;
        String[] key = null;
        String[] value = null;
        ServerValidation sv;
        switch (mnu_id) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "24":// purn tutalela plot
            case "57":
            case "58":
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                String[] keyfetch = new String[]{activity.getResources().getString(R.string.prefofficer)};
                String[] valuefetch = new String[]{"0"};
                String[] data = cf.getSharedPrefValue(activity, keyfetch, valuefetch);
                if (mnu_id.equals("3") || mnu_id.equals("57") || mnu_id.equals("58") || (mnu_id.equals("4") && (data[0].equals("113") || data[0].equals("114") || data[0].equals("115"))) || (mnu_id.equals("24") && data[0].equals("113")))
                    intent = new Intent(activity, OnlineFarmerActivity.class);
                else intent = new Intent(activity, SelectFarmerActivity.class);
                intent.putExtra("mnu_id", mnu_id);
                break;
          /*  case "17":
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, ViewDataActivity.class);
                intent.putExtra("purpose", "1");
                break;*/
            case "18":
                sendData(1);
                break;
            /*case "19":
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, ViewDataActivity.class);
                intent.putExtra("purpose", "2");
                break;*/
            case "20":
                intent = new Intent(activity, FarmerRequestListActivity.class);
                intent.putExtra("caller", FarmerList.BYOFFICER);
                break;
            case "5":
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, NondConfirmjaActivity.class);
                intent.putExtra("mnu_id", mnu_id);
                break;
            case "25":
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, HarvProgramStartActivity.class);
                break;
            case "22":
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, HarvPlanIndviualActivity.class);
                break;
            case "21":
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, HarvPlanActivity.class);
                break;
            case "23":
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, ExtraPlotRequestListActivity.class);
                break;
            case "6":
                intent = new Intent(activity, NondReportActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "63":
                intent = new Intent(activity, HarvReportActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "12":
            case "13":
            case "7":// us pawati
            case "10":// us vilhewat other utilization activity created
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, PlotSelectionActivity.class);
                intent.putExtra("mnu_id", mnu_id);
                break;
            case "8": // us pawati durusti
            case "82":
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, SlipNoActivity.class);
                intent.putExtra("mnu_id", mnu_id);
                break;
            case "9": // us pawati shillak
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, RemainingSlipActivity.class);
                break;
            case "11": // us avak dainadin
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, CaneInwardDailyActivity.class);
                break;
            case "26":
                intent = new Intent(activity, CrushingReportActivity.class);
                break;
            case "27":
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, AgriReportActivity.class);
                break;
            case "51":
                intent = new Intent(activity, GutKhadeActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "52":
                key = new String[]{activity.getResources().getString(R.string.preffromtimerawana), activity.getResources().getString(R.string.preftotimerawana)};
                value = new String[]{"16:00", "17:30"};
                String vtime[] = cf.getSharedPrefValue(activity, key, value);

                Calendar todayCal = Calendar.getInstance();
                Calendar fromCal = Calendar.getInstance();
                Calendar toCal = Calendar.getInstance();

                String startTime[] = vtime[0].split(":");
                String endTime[] = vtime[1].split(":");
                fromCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTime[0]));
                fromCal.set(Calendar.MINUTE, Integer.parseInt(startTime[1]));

                toCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTime[0]));
                toCal.set(Calendar.MINUTE, Integer.parseInt(endTime[1]));
                if (fromCal.getTime().before(todayCal.getTime()) && toCal.getTime().after(todayCal.getTime())) {
                    intent = new Intent(activity, CaneTransitActivity.class);
                    key = new String[]{activity.getResources().getString(R.string.prefseason)};
                    value = new String[]{yearcode};
                    cf.putSharedPrefValue(key, value, activity);
                } else {
                    Constant.showToast(String.format(activity.getResources().getString(R.string.accessmenubetweentime), mnu_name, vtime[0], vtime[1]), activity, R.drawable.ic_wrong);
                }
                break;
            case "53":
                intent = new Intent(activity, RawanaReportActivity.class);
                intent.putExtra("rawana", RawanaEnum.VILLAGE);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "54":
                intent = new Intent(activity, RawanaReportActivity.class);
                intent.putExtra("rawana", RawanaEnum.SECTION);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "56":
                intent = new Intent(activity, SugarInwardActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "59":
                intent = new Intent(activity, SugarDailyOutwardActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "60":
            case "81":
                intent = new Intent(activity, SugarSaleSummaryActivity.class);
                intent.putExtra("mnu_id", mnu_id);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "62":
                intent = new Intent(activity, FertDistHomeActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "64":
                intent = new Intent(activity, HarvFarmerTonnageActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "66":
                intent = new Intent(activity, VillageAssignActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{yearcode};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "69":
                intent = new Intent(activity, NumYardAssignActivity.class);
                break;
            case "70":
                key = new String[]{activity.getResources().getString(R.string.prefyardid), activity.getResources().getString(R.string.prefharvestingyearcode)};
                value = new String[]{"", ""};
                data = cf.getSharedPrefValue(activity, key, value);

                sv = new ServerValidation();
                if (sv.checkNumber(data[0])) {
                    Integer locationId = Integer.parseInt(data[0]);
                    if (locationId <= 0) {
                        Constant.showToast(activity.getResources().getString(R.string.errornorequireddatafornumbertaker), activity, R.drawable.ic_wrong);
                        return;
                    }
                } else {
                    Constant.showToast(activity.getResources().getString(R.string.errornorequireddatafornumbertaker), activity, R.drawable.ic_wrong);
                    return;
                }
                intent = new Intent(activity, NumNumberActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{data[1]};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "71":
                intent = new Intent(activity, NumLotGenerationActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefharvestingyearcode)};
                value = new String[]{""};
                data = cf.getSharedPrefValue(activity, key, value);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{data[0]};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "73":
                intent = new Intent(activity, NumVehicleRegistrationReportActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefharvestingyearcode)};
                value = new String[]{""};
                data = cf.getSharedPrefValue(activity, key, value);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{data[0]};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "74":
                intent = new Intent(activity, NumIndExcludeActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefharvestingyearcode)};
                value = new String[]{""};
                data = cf.getSharedPrefValue(activity, key, value);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{data[0]};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "75":
                intent = new Intent(activity, NumLotPrintNumActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefharvestingyearcode)};
                value = new String[]{""};
                data = cf.getSharedPrefValue(activity, key, value);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{data[0]};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "76":
                intent = new Intent(activity, NumStopStartNumActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefharvestingyearcode)};
                value = new String[]{""};
                data = cf.getSharedPrefValue(activity, key, value);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{data[0]};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "77":
                intent = new Intent(activity, NumIndWaitReportActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefharvestingyearcode)};
                value = new String[]{""};
                data = cf.getSharedPrefValue(activity, key, value);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{data[0]};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "78":
                intent = new Intent(activity, NumTokenPassReprintActivity.class);
                key = new String[]{activity.getResources().getString(R.string.prefharvestingyearcode)};
                value = new String[]{""};
                data = cf.getSharedPrefValue(activity, key, value);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{data[0]};
                cf.putSharedPrefValue(key, value, activity);
                break;
            case "79":
                intent = new Intent(activity, UserRoleChangeActivity.class);
                break;
            case "72":
                intent = new Intent(activity, NumYardBalanceActivity.class);
                break;
            case "80":
                intent = new Intent(activity, NumInwardSummaryActivity.class);
                break;
            case "83":
                intent = new Intent(activity, PumpPumpAssignActivity.class);
                break;
            case "84":
                key = new String[]{activity.getResources().getString(R.string.prefpumpid)};
                value = new String[]{""};
                data = cf.getSharedPrefValue(activity, key, value);

                sv = new ServerValidation();
                if (sv.checkNumber(data[0])) {
                    Integer pumpId = Integer.parseInt(data[0]);
                    if (pumpId <= 0) {
                        Constant.showToast(activity.getResources().getString(R.string.errornorequireddataforpump), activity, R.drawable.ic_wrong);
                        return;
                    }
                } else {
                    Constant.showToast(activity.getResources().getString(R.string.errornorequireddataforpump), activity, R.drawable.ic_wrong);
                    return;
                }
                intent = new Intent(activity, PumpRateEntryActivity.class);
                break;
            case "85":
                key = new String[]{activity.getResources().getString(R.string.prefpumpid)};
                value = new String[]{""};
                data = cf.getSharedPrefValue(activity, key, value);

                sv = new ServerValidation();
                if (sv.checkNumber(data[0])) {
                    Integer pumpId = Integer.parseInt(data[0]);
                    if (pumpId <= 0) {
                        Constant.showToast(activity.getResources().getString(R.string.errornorequireddataforpump), activity, R.drawable.ic_wrong);
                        return;
                    }
                } else {
                    Constant.showToast(activity.getResources().getString(R.string.errornorequireddataforpump), activity, R.drawable.ic_wrong);
                    return;
                }
                key = new String[]{activity.getResources().getString(R.string.prefharvestingyearcode)};
                value = new String[]{""};
                data = cf.getSharedPrefValue(activity, key, value);
                key = new String[]{activity.getResources().getString(R.string.prefseason)};
                value = new String[]{data[0]};
                cf.putSharedPrefValue(key, value, activity);
                intent = new Intent(activity, PumpSaleEntryActivity.class);
                break;
            default:
                showInProgress(mnu_name);
                break;
        }
        if (intent != null) {
            activity.startActivity(intent);
        }
    }

    private void sendData(int callcount) {
        DBHelper mydb = new DBHelper(activity);
        ArrayList<PlantationBean> plantationBeans = mydb.getPlantationData();
        if (!plantationBeans.isEmpty()) {
            DataSender ds = new DataSender();
            ds.sendData(plantationBeans, activity, new UpdateRemain() {
                @Override
                public void updateRemain() {
                    sendData(callcount + 1);
                }
            });
        } else if (callcount == 1) {
            Constant.showToast(activity.getResources().getString(R.string.errornoformavailable), activity, R.drawable.ic_wrong);
        }
    }

    /*private void checkLocation(String mnu_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Dexter.withContext(activity)
                    .withPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            if (response.isPermanentlyDenied()) {
                                ConstantFunction cf = new ConstantFunction();
                                cf.openSettings(activity);
                            }
                        }

                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            openIntent(mnu_id);
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } else {
            openIntent(mnu_id);
        }
    }

    private void openIntent(String mnu_id) {
        Intent intent = null;
        if (mnu_id.equals("12")) {
            intent = new Intent(activity, AttendanceActivity.class);
        } else if (mnu_id.equals("40")) {
            intent = new Intent(activity, AttLocationActivity.class);
        }
        if (intent != null) {
            activity.startActivity(intent);
        }
    }*/

    private void showInProgress(String mnu_name) {
        Constant.showToast(String.format(activity.getResources().getString(R.string.commingsoonorupdateapp), mnu_name), activity, R.drawable.ic_info);
    }

    private void loadPopup(SubTabHolder holder) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_menu_param);

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);
        TextView paramhead = dialog.findViewById(R.id.paramhead);
        JSONObject job = (JSONObject) holder.txtmnuname.getTag();
        String mnu_name = null;
        try {
            mnu_name = job.getString("mnu_name");
        } catch (JSONException e) {
            e.printStackTrace();
            mnu_name = holder.txtmnuname.getText().toString();
        }
        paramhead.setText(mnu_name);

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btncancelparam = dialog.findViewById(R.id.btncancelparam);
        Button btnconfirmparam = dialog.findViewById(R.id.btnconfirmparam);

        SingleSpinnerSearch seasonsp = dialog.findViewById(R.id.seasonsp);

        ConstantFunction cf = new ConstantFunction();
        SingleSelectHandler ssh = new SingleSelectHandler();

        String mnu_id = "";
        try {
            mnu_id = job.getString("mnu_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (groupid.equals("1") && !mnu_id.equals("4") && !mnu_id.equals("5") && !mnu_id.equals("6")) {
            String[] key = new String[]{activity.getResources().getString(R.string.prefyearcode)};
            String[] value = new String[]{""};
            String[] result = cf.getSharedPrefValue(activity, key, value);
            setYear(result[0], seasonsp, 0);
            /*JSONObject job = new JSONObject(holder.txtmnuname.getTag().toString());*/
            /*mnu_id = job.getString("mnu_id");*/
            seasonsp.setVisibility(View.VISIBLE);

        } else if ((groupid.equals("2") || groupid.equals("3") || groupid.equals("4") || mnu_id.equals("5") || groupid.equals("9")) && !mnu_id.equals("63") && !mnu_id.equals("64")) {
            String[] key = new String[]{activity.getResources().getString(R.string.prefharvestingyearcode)};
            String[] value = new String[]{""};
            String[] result = cf.getSharedPrefValue(activity, key, value);
            setYear(result[0], seasonsp, 0);
            ssh.initSingle(seasonsp, activity, activity.getResources().getString(R.string.galithangamhint));
            seasonsp.setVisibility(View.VISIBLE);
        } else if (mnu_id.equals("6") || mnu_id.equals("4") || mnu_id.equals("27") || mnu_id.equals("63") || mnu_id.equals("64")) {
            String[] key = new String[]{activity.getResources().getString(R.string.prefreportingyearcode)};
            String[] value = new String[]{""};
            String[] result = cf.getSharedPrefValue(activity, key, value);
            setYear(result[0], seasonsp, (mnu_id.equals("63") || mnu_id.equals("64")) ? -1 : 0);
            ssh.initSingle(seasonsp, activity, activity.getResources().getString(R.string.galithangamhint));
            seasonsp.setVisibility(View.VISIBLE);
        } else if (groupid.equals("8")) {
            String[] key = new String[]{activity.getResources().getString(R.string.preffertyearcode)};
            String[] value = new String[]{""};
            String[] result = cf.getSharedPrefValue(activity, key, value);
            setYear(result[0], seasonsp, 0);
            ssh.initSingle(seasonsp, activity, activity.getResources().getString(R.string.fertyear));
            seasonsp.setVisibility(View.VISIBLE);
        } else {
            ssh.initSingle(seasonsp, activity, activity.getResources().getString(R.string.galithangamhint));
            seasonsp.setVisibility(View.GONE);
        }

        btncancelparam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btnconfirmparam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String season = null;
                List<KeyPairBoolData> seasonItem = seasonsp.getSelectedItems();
                if (seasonItem.size() == 1) {
                    season = String.valueOf(seasonItem.get(0).getName());
                } else {
                    cf.showCustomAlert(activity, R.drawable.ic_wrong, activity.getResources().getString(R.string.errorselectseason));
                    return;
                }
                nextActivity(holder, season);
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    private void setYear(String year, SingleSpinnerSearch seasonsp, int selPos) {
        String[] yeararr = year.split(",");
        int size = yeararr.length;
        if (selPos == -1) {
            selPos = size - 1;
        }
        List<KeyPairBoolData> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            KeyPairBoolData item = new KeyPairBoolData();
            item.setId(i + 1);
            item.setName(yeararr[i]);
            item.setSelected(i == selPos);
            data.add(item);
        }
        seasonsp.setItems(data, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {

            }

            @Override
            public void onClear() {

            }
        });
    }

}