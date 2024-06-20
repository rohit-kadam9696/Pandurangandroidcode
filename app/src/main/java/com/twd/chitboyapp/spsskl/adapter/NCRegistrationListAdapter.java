package com.twd.chitboyapp.spsskl.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.NondConfirmActivity;
import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.PlantListCaller;
import com.twd.chitboyapp.spsskl.pojo.CaneConfirmationRegistrationList;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.vh.NCRegistrationListHolder;

import java.util.HashMap;
import java.util.List;

public class NCRegistrationListAdapter extends RecyclerView.Adapter<NCRegistrationListHolder> {


    private final Activity activity;
    private final List<CaneConfirmationRegistrationList> caneConfirmationRegistrationLists;
    private final String yearCode;
    private final String rujutype;
    private final EntityMasterDetail entityMasterDetail;
    private final HashMap<String, String> village;
    private final HashMap<String, String> hangam;
    private final HashMap<String, String> varity;
    PlantListCaller plantListCaller;

    public NCRegistrationListAdapter(List<CaneConfirmationRegistrationList> caneConfirmationRegistrationLists, Activity context, String yearCode, String rujutype, EntityMasterDetail entityMasterDetail, PlantListCaller plantListCaller) {
        this.caneConfirmationRegistrationLists = caneConfirmationRegistrationLists;
        this.activity = context;
        this.yearCode = yearCode;
        this.rujutype = rujutype;
        this.entityMasterDetail = entityMasterDetail;
        this.plantListCaller = plantListCaller;
        DBHelper dbHelper = new DBHelper(activity);
        village = dbHelper.getVillageHashMap();
        hangam = dbHelper.getHangamHashMap();
        varity = dbHelper.getVarietyHashMap();
    }

    @NonNull
    @Override
    public NCRegistrationListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registration_list_nc, parent, false);
        return new NCRegistrationListHolder(v);
    }

    @Override
    public void onBindViewHolder(NCRegistrationListHolder holder, final int position) {
        CaneConfirmationRegistrationList caneConfirmationRegistrationList = caneConfirmationRegistrationLists.get(holder.getBindingAdapterPosition());


        holder.txtnondvillagetxt.setText(village.containsKey(caneConfirmationRegistrationList.getVilleageCode()) ? village.get(caneConfirmationRegistrationList.getVilleageCode()) : caneConfirmationRegistrationList.getVilleageCode());
        holder.txtplotnotxt.setText(caneConfirmationRegistrationList.getPlotNo());
        holder.txthangamtxt.setText(hangam.containsKey(caneConfirmationRegistrationList.getNhungamCode()) ? hangam.get(caneConfirmationRegistrationList.getNhungamCode()) : "");
        holder.txtareatxt.setText(String.valueOf(caneConfirmationRegistrationList.getNarea()));
        holder.txtvaritytxt.setText(varity.containsKey(caneConfirmationRegistrationList.getNcaneVarity()) ? varity.get(caneConfirmationRegistrationList.getNcaneVarity()) : "");
        holder.txtplantationdatetxt.setText(caneConfirmationRegistrationList.getDplantationDate());
        holder.txtexpectedyieldtxt.setText(caneConfirmationRegistrationList.getNexpectedYield());

        if (plantListCaller == PlantListCaller.NONDCONFIRM) {
            holder.ncregistrationcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, NondConfirmActivity.class);
                    intent.putExtra("confirmationData", caneConfirmationRegistrationList);
                    intent.putExtra("entityMasterDetail", entityMasterDetail);
                    intent.putExtra("yearCode", yearCode);
                    intent.putExtra("rujutype", rujutype);
                    activity.startActivity(intent);
                }
            });
            holder.txtexpectedyieldtxt.setVisibility(View.GONE);
            holder.txtexpectedyieldlbl.setVisibility(View.GONE);
            holder.txtnccollon6.setVisibility(View.GONE);

            holder.txtstatuslbl.setVisibility(View.GONE);
            holder.txtnccollon7.setVisibility(View.GONE);
            holder.txtstatustxt.setVisibility(View.GONE);
        } else {
            holder.txtstatustxt.setText(caneConfirmationRegistrationList.getVstatus());
            holder.txtexpectedyieldtxt.setVisibility(View.VISIBLE);
            holder.txtexpectedyieldlbl.setVisibility(View.VISIBLE);
            holder.txtnccollon6.setVisibility(View.VISIBLE);

            holder.txtstatuslbl.setVisibility(View.VISIBLE);
            holder.txtnccollon7.setVisibility(View.VISIBLE);
            holder.txtstatustxt.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return caneConfirmationRegistrationLists.size();
    }

}