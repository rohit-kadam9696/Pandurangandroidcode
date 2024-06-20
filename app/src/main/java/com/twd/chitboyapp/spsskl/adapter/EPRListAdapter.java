package com.twd.chitboyapp.spsskl.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.ViewExessPlotRequestActivity;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ExcessTonPlotReq;
import com.twd.chitboyapp.spsskl.pojo.ExcessTonPlotReqDataResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.chitboyapp.spsskl.vh.EPRListHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class EPRListAdapter extends RecyclerView.Adapter<EPRListHolder> implements RetrofitResponse {


    private final List<ExcessTonPlotReq> excessTonPlotReqs;
    private final Activity activity;

    public EPRListAdapter(List<ExcessTonPlotReq> excessTonPlotReqs, Activity activity) {
        this.excessTonPlotReqs = excessTonPlotReqs;
        this.activity = activity;
    }

    @NonNull
    @Override
    public EPRListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_epr_list, parent, false);
        return new EPRListHolder(v);
    }

    @Override
    public void onBindViewHolder(EPRListHolder holder, final int position) {
        ExcessTonPlotReq excessTonPlotReq = excessTonPlotReqs.get(holder.getBindingAdapterPosition());

        holder.txtfarmernametxt.setText(excessTonPlotReq.getFarmerName());
        holder.txtplotnotxt.setText(excessTonPlotReq.getPlotNo());
        holder.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] key = new String[]{activity.getResources().getString(R.string.chitboyprefuniquestring), activity.getResources().getString(R.string.chitboyprefchit_boy_id), activity.getResources().getString(R.string.prefseason)};
                String[] value = new String[]{"", "0", ""};
                ConstantFunction cf = new ConstantFunction();
                String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

                JSONObject job = new JSONObject();
                String action = activity.getResources().getString(R.string.actiongetextraplotdetails);

                try {
                    job.put("yearCode", sharedPrefval[2]);
                    job.put("nplotNo", holder.txtplotnotxt.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String servlet = activity.getResources().getString(R.string.servletharvdata);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                RetrofitHandler<ExcessTonPlotReqDataResponse> handler = new RetrofitHandler<>();
                Call<ExcessTonPlotReqDataResponse> call2 = apiInterface.exessDetailsHarvData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
                handler.handleRetrofit(call2, EPRListAdapter.this, RequestEnum.EXESSPLOTDETAIL, servlet, activity, versioncode);


            }
        });


    }


    @Override
    public int getItemCount() {
        return excessTonPlotReqs.size();
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.EXESSPLOTDETAIL) {
            ExcessTonPlotReqDataResponse excessTonPlotReqDataResponse = (ExcessTonPlotReqDataResponse) response.body();
            Intent intent = new Intent(activity, ViewExessPlotRequestActivity.class);
            intent.putExtra("data", excessTonPlotReqDataResponse);
            activity.startActivity(intent);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}