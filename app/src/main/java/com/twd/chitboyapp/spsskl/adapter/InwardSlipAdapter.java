package com.twd.chitboyapp.spsskl.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.PrinterConstant;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneDailyInwardReport;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.chitboyapp.spsskl.vh.InwardSlipHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class InwardSlipAdapter extends RecyclerView.Adapter<InwardSlipHolder> implements RetrofitResponse {


    private final List<CaneDailyInwardReport> caneDailyInwardReports;
    private final Activity activity;
    PrinterConstant printerConstant;

    public InwardSlipAdapter(List<CaneDailyInwardReport> caneDailyInwardReports, Activity activity) {
        this.caneDailyInwardReports = caneDailyInwardReports;
        this.activity = activity;
        printerConstant = new PrinterConstant();
    }

    @NonNull
    @Override
    public InwardSlipHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inward_slip, parent, false);
        return new InwardSlipHolder(v);
    }

    @Override
    public void onBindViewHolder(InwardSlipHolder holder, final int position) {
        CaneDailyInwardReport caneDailyInwardReport = caneDailyInwardReports.get(holder.getBindingAdapterPosition());


        String html = printerConstant.getPageHeader() + caneDailyInwardReport.getHtml() + printerConstant.getPageFooter().replace("<br><br><br><span>.</span>", "");
        holder.txtslipno.setText(caneDailyInwardReport.getNslipNo());
        holder.vwslipdata.getSettings().setLoadWithOverviewMode(true);
        holder.vwslipdata.getSettings().setUseWideViewPort(true);
        holder.vwslipdata.getSettings().setAllowFileAccess(true);
        holder.vwslipdata.getSettings().setJavaScriptEnabled(true);
        holder.vwslipdata.getSettings().setBuiltInZoomControls(true);
        holder.vwslipdata.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

        holder.btnprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String html = printerConstant.getPageHeader() + printerConstant.getPrintHeader(activity.getResources().getString(R.string.dailyinward), null) + caneDailyInwardReport.getHtml() + printerConstant.getPrintFooter(activity) + printerConstant.getPageFooter();
                boolean isContinue = printerConstant.connectPrinter(activity);
                if (isContinue) {
                    PrinterConstant.printContent = html;
                    printerConstant.print(activity);
                } else {
                    new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PrinterConstant.printContent = html;
                            printerConstant.print(activity);
                        }
                    }, 4000);
                }
            }
        });
        holder.btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] key = new String[]{activity.getResources().getString(R.string.chitboyprefuniquestring), activity.getResources().getString(R.string.chitboyprefchit_boy_id), activity.getResources().getString(R.string.prefseason)};
                String[] value = new String[]{"", "0", ""};
                ConstantFunction cf = new ConstantFunction();
                String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

                JSONObject job = new JSONObject();
                String action = activity.getResources().getString(R.string.actionverifyslip);

                try {
                    job.put("yearCode", sharedPrefval[2]);
                    job.put("nslipNo", holder.txtslipno.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String servlet = activity.getResources().getString(R.string.servletharvdata);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                RetrofitHandler<ActionResponse> handler = new RetrofitHandler<>();
                Call<ActionResponse> call2 = apiInterface.actionHarvData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
                handler.handleRetrofit(call2, InwardSlipAdapter.this, RequestEnum.VERIFYSLIP, servlet, activity, versioncode, holder.getAdapterPosition());

            }
        });


    }


    @Override
    public int getItemCount() {
        return caneDailyInwardReports.size();
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.VERIFYSLIP) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : activity.getResources().getString(R.string.successfullysaved), activity, R.drawable.ic_accept);
                int position = (int) obj[0];
                caneDailyInwardReports.remove(position);
                notifyDataSetChanged();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : activity.getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    public List<CaneDailyInwardReport> getList() {
        return caneDailyInwardReports;
    }
}