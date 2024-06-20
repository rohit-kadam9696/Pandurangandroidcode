package com.twd.chitboyapp.spsskl.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.PrinterConstant;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SlipConstants;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.RemainingSlipBean;
import com.twd.chitboyapp.spsskl.pojo.SlipBeanR;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.chitboyapp.spsskl.vh.RemainingSlipHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RemainingSlipAdapter extends RecyclerView.Adapter<RemainingSlipHolder> implements RetrofitResponse {


    private Activity activity;
    private List<RemainingSlipBean> remainingSlipBeans;
    private String season;
    private String vehicalType;
    private String sessionUserName;
    private JSONObject remark;

    public RemainingSlipAdapter(Activity activity, List<RemainingSlipBean> remainingSlipBeans, String season, String vehicalType, String sessionUserName) {
        this.activity = activity;
        this.remainingSlipBeans = remainingSlipBeans;
        this.season = season;
        this.vehicalType = vehicalType;
        this.sessionUserName = sessionUserName;
        DBHelper dbHelper = new DBHelper(activity);
        remark = dbHelper.getRemarkKeyPair();
    }

    @NonNull
    @Override
    public RemainingSlipHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_remaining_slip, parent, false);
        return new RemainingSlipHolder(v);
    }

    @Override
    public void onBindViewHolder(RemainingSlipHolder holder, final int position) {
        RemainingSlipBean remainingSlipBean = remainingSlipBeans.get(holder.getBindingAdapterPosition());

        holder.txthangamtxt.setText(season);
        holder.txtslipdatetxt.setText(remainingSlipBean.getDslipDate());
        holder.txtslipnotxt.setText(remainingSlipBean.getNslipNo());
        holder.txtfarmercodetxt.setText(remainingSlipBean.getNentityUnitId());
        holder.txtfarmernametxt.setText(remainingSlipBean.getVfarmerName());
        holder.txtvvillagenametxt.setText(remainingSlipBean.getVvillageNameLocal());
        holder.txtplotnotxt.setText(remainingSlipBean.getNplotNo());
        holder.txtvehicaltypetxt.setText(vehicalType);
        int transcase = View.GONE;
        int bulluckcase = View.GONE;

        int wireropecase = View.GONE;
        int tailercase = View.GONE;

        if (remainingSlipBean.getNvehicalTypeId().equals("1") || remainingSlipBean.getNvehicalTypeId().equals("2")) {
            holder.txtharvestortxt.setText(remainingSlipBean.getNharvestorId() + " " + remainingSlipBean.getVharvestorName());
            holder.txttransportertxt.setText(remainingSlipBean.getNtransporterId() + " " + remainingSlipBean.getVtransporterName());
            if (remainingSlipBean.getNvehicalTypeId().equals("1")) wireropecase = View.VISIBLE;
            else tailercase = View.VISIBLE;
            transcase = View.VISIBLE;
        } else {
            holder.txtmukadamtxt.setText(remainingSlipBean.getNbullockMainCode() + " " + remainingSlipBean.getVbullockMainName());
            holder.txtgadiwantxt.setText(remainingSlipBean.getNbullockCode() + " " + remainingSlipBean.getVbullockName());
            if (remainingSlipBean.getNvehicalTypeId().equals("4")) tailercase = View.VISIBLE;
            bulluckcase = View.VISIBLE;
        }

        if (wireropecase == View.VISIBLE) {
            holder.txtwireropetxt.setText(remainingSlipBean.getVwireropeName());
        }

        if (tailercase == View.VISIBLE) {
            holder.txtfronttrailertxt.setText(remainingSlipBean.getVtailerFrontName());
            holder.txtbacktrailertxt.setText(remainingSlipBean.getVtailerBackName());
        }

        holder.txtharvestorter.setVisibility(transcase);
        holder.txtfvrcollon9.setVisibility(transcase);
        holder.txtharvestortxt.setVisibility(transcase);
        holder.txttransporter.setVisibility(transcase);
        holder.txtfvrcollon2.setVisibility(transcase);
        holder.txttransportertxt.setVisibility(transcase);

        holder.txtmukadam.setVisibility(bulluckcase);
        holder.txtfvrcollon10.setVisibility(bulluckcase);
        holder.txtmukadamtxt.setVisibility(bulluckcase);
        holder.txtgadiwan.setVisibility(bulluckcase);
        holder.txtfvrcollon11.setVisibility(bulluckcase);
        holder.txtgadiwantxt.setVisibility(bulluckcase);

        holder.txtwireropelbl.setVisibility(wireropecase);
        holder.txtfvrcollon5.setVisibility(wireropecase);
        holder.txtwireropetxt.setVisibility(wireropecase);
        holder.txtfronttrailerlbl.setVisibility(tailercase);
        holder.txtfvrcollon6.setVisibility(tailercase);
        holder.txtfronttrailertxt.setVisibility(tailercase);
        holder.txtbacktrailerlbl.setVisibility(tailercase);
        holder.txtfvrcollon7.setVisibility(tailercase);
        holder.txtbacktrailertxt.setVisibility(tailercase);

        holder.txtvehiclenotxt.setText(remainingSlipBean.getVvehicleNo());
        try {
            holder.txtremarktxt.setText(remark.has(remainingSlipBean.getNremarkId()) ? remark.getString(remainingSlipBean.getNremarkId()) : remainingSlipBean.getNremarkId());
        } catch (JSONException e) {
            e.printStackTrace();
            holder.txtremarktxt.setText(remainingSlipBean.getNremarkId());
        }
        if (remainingSlipBean.getChitboyName() != null)
            holder.txtdeptheadtxt.setText(remainingSlipBean.getChitboyName());
        else holder.txtdeptheadtxt.setText(sessionUserName);

        holder.btncancelslip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askConfirmDeativate(remainingSlipBean, holder.getAdapterPosition());
            }
        });

        holder.btnreprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantFunction cf = new ConstantFunction();
                SlipConstants slipConstants = new SlipConstants();
                JSONObject job = slipConstants.prepairJob(activity, remainingSlipBean, vehicalType, remark, sessionUserName);
                List<SlipBeanR> slipBeanRList = new ArrayList<>();//(List<SlipBeanR>) intent.getSerializableExtra("slipBeanRList");
                SlipBeanR slipBeanR = new SlipBeanR();
                slipBeanR.setNslipNo(remainingSlipBean.getNslipNo());
                slipBeanR.setSlipDate(remainingSlipBean.getDslipDate());
                slipBeanR.setExtraQr(remainingSlipBean.getExtraQr());
                if (remainingSlipBean.getNvehicalTypeId().equals("3") || remainingSlipBean.getNvehicalTypeId().equals("4")) {
                    slipBeanR.setNbullokCode(remainingSlipBean.getNbullockCode());
                }
                slipBeanRList.add(slipBeanR);
                String date = remainingSlipBean.getDslipDate();//intent.getStringExtra("date");
                String[] key = new String[]{activity.getResources().getString(R.string.prefharvslipdata)};
                String[] value = new String[]{job.toString()};
                cf.putSharedPrefValue(key, value, activity);
                PrinterConstant print = new PrinterConstant();
                boolean isContinue = print.connectPrinter(activity);
                if (isContinue) {
                   slipConstants.printData(activity, print, slipBeanRList, date, true);
                } else {
                    new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            slipConstants.printData(activity, print, slipBeanRList, date, true);
                        }
                    }, 4000);

                }
            }
        });
    }

    private void askConfirmDeativate(RemainingSlipBean remainingSlipBean, int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_delete);

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatTextView txt_deletehead = dialog.findViewById(R.id.txt_deletehead);
        AppCompatTextView message = dialog.findViewById(R.id.message);

        txt_deletehead.setText(activity.getResources().getString(R.string.slipcancel));
        message.setText(activity.getResources().getString(R.string.slipcancelmessage));

        AppCompatButton btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
        AppCompatButton btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);

        btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] key = new String[]{activity.getResources().getString(R.string.chitboyprefuniquestring), activity.getResources().getString(R.string.chitboyprefchit_boy_id), activity.getResources().getString(R.string.prefseason)};
                String[] value = new String[]{"", "0", ""};
                ConstantFunction cf = new ConstantFunction();
                String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

                JSONObject job = new JSONObject();
                try {
                    job.put("yearCode", sharedPrefval[2]);
                    job.put("nslip_no", remainingSlipBean.getNslipNo());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String action = activity.getResources().getString(R.string.actiondeactivate);
                String servlet = activity.getResources().getString(R.string.servletharvdata);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                RetrofitHandler<ActionResponse> otphandler = new RetrofitHandler<>();
                Call<ActionResponse> call2 = apiInterface.actionHarvData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
                otphandler.handleRetrofit(call2, RemainingSlipAdapter.this, RequestEnum.DEACTIVATESLIP, servlet, activity, versioncode, dialog, position);

            }
        });
        btncanceldelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return remainingSlipBeans.size();
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.DEACTIVATESLIP) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : activity.getResources().getString(R.string.savesucess), activity, R.drawable.ic_wrong);
                if (((Dialog) obj[0]).isShowing()) {
                    ((Dialog) obj[0]).cancel();
                }

                remainingSlipBeans.remove((int) obj[1]);
                notifyDataSetChanged();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : activity.getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}