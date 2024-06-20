package com.twd.chitboyapp.spsskl.adapter;

import android.app.Activity;
import android.app.Dialog;
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
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.FarmerList;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.chitboyapp.spsskl.vh.NPFarmerListHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NPFarmerListAdapter extends RecyclerView.Adapter<NPFarmerListHolder> implements RetrofitResponse {


    private final Activity activity;
    private final List<EntityMasterDetail> entityMasterDetails;
    private final FarmerList farmerList;

    public NPFarmerListAdapter(List<EntityMasterDetail> entityMasterDetails, Activity context, FarmerList farmerList) {
        this.entityMasterDetails = entityMasterDetails;
        this.activity = context;
        this.farmerList = farmerList;
    }

    @NonNull
    @Override
    public NPFarmerListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farmer_list_np, parent, false);
        return new NPFarmerListHolder(v);
    }

    @Override
    public void onBindViewHolder(NPFarmerListHolder holder, final int position) {
        EntityMasterDetail entityMasterDetail = entityMasterDetails.get(holder.getBindingAdapterPosition());
        switch (farmerList) {
            case BYCHITBOY:
                holder.btnreject.setVisibility(View.GONE);
                holder.btnverify.setVisibility(View.GONE);
                holder.llchitboy.setVisibility(View.GONE);
                holder.txtstatuslbl.setVisibility(View.VISIBLE);
                holder.txtfvrcollon4.setVisibility(View.VISIBLE);
                holder.txtstatustxt.setVisibility(View.VISIBLE);
                holder.txtstatustxt.setText(entityMasterDetail.getStatus());
                break;
            case BYOFFICER:
                holder.btnreject.setVisibility(View.VISIBLE);
                holder.btnverify.setVisibility(View.VISIBLE);
                holder.llchitboy.setVisibility(View.VISIBLE);
                holder.txtstatuslbl.setVisibility(View.GONE);
                holder.txtfvrcollon4.setVisibility(View.GONE);
                holder.txtstatustxt.setVisibility(View.GONE);

                holder.txtchitboynametxt.setText(entityMasterDetail.getNuserName());
                holder.txtchitboyid.setText(String.valueOf(entityMasterDetail.getNbankId()));
                holder.btnverify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        performAction("2", holder);
                    }
                });

                holder.btnreject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(activity);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                        dialog.setContentView(R.layout.popup_delete);

                        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);

                        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

                        AppCompatTextView txt_deletehead = dialog.findViewById(R.id.txt_deletehead);
                        AppCompatTextView message = dialog.findViewById(R.id.message);

                        txt_deletehead.setText(activity.getResources().getString(R.string.reject));
                        message.setText(activity.getResources().getString(R.string.errorrejectconfirm));

                        AppCompatButton btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
                        AppCompatButton btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);
                        btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                performAction("3", holder);
                                dialog.cancel();
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
                });
                break;
        }

        holder.txtfarmernametxt.setText(entityMasterDetail.getVentityNameLocal());
        holder.txtfarmercodetxt.setText(entityMasterDetail.getNentityUniId());
        holder.txtvillagetxt.setText(entityMasterDetail.getVillname());
        holder.txtvillageid.setText(String.valueOf(entityMasterDetail.getNvillageId()));
    }

    private void performAction(String status, NPFarmerListHolder holder) {
        JSONObject job = new JSONObject();
        String action = activity.getResources().getString(R.string.actionfarmerrequestverifyorreject);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{activity.getResources().getString(R.string.chitboyprefuniquestring), activity.getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        try {
            job.put("nentityUniId", holder.txtfarmercodetxt.getText().toString());
            job.put("nvillage_id", holder.txtvillageid.getText().toString());
            job.put("requestChitboyId", holder.txtchitboyid.getText().toString());
            job.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String servlet = activity.getResources().getString(R.string.servletregApproval);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<ActionResponse> call2 = apiInterface.farmerActions(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<ActionResponse> otphandler = new RetrofitHandler<>();
        otphandler.handleRetrofit(call2, NPFarmerListAdapter.this, RequestEnum.SAVEFRREQVORR, servlet, activity, versioncode, status, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return entityMasterDetails.size();
    }


    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        String status = String.valueOf(obj[0]);
        Integer position = (int) (obj[1]);
        if (requestCaller == RequestEnum.SAVEFRREQVORR) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : (status.equals("2") ? activity.getResources().getString(R.string.acceptsuccess) : activity.getResources().getString(R.string.rejectsuccess)), activity, R.drawable.ic_wrong);
                EntityMasterDetail entityMasterDetail = entityMasterDetails.get(position);
                entityMasterDetails.remove(entityMasterDetail);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, entityMasterDetails.size());
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : (status.equals("2") ? activity.getResources().getString(R.string.acceptfail) : activity.getResources().getString(R.string.rejectfail)), activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}