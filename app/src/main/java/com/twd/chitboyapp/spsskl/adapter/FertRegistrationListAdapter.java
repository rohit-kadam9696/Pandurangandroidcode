package com.twd.chitboyapp.spsskl.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.interfaces.UpdateRemain;
import com.twd.chitboyapp.spsskl.pojo.CaneConfirmationRegistrationList;
import com.twd.chitboyapp.spsskl.vh.FertRegistrationListHolder;

import java.util.HashMap;
import java.util.List;

public class FertRegistrationListAdapter extends RecyclerView.Adapter<FertRegistrationListHolder> {

    private final Activity activity;
    private static UpdateRemain updateArea;
    public final List<CaneConfirmationRegistrationList> caneConfirmationRegistrationLists;
    private final HashMap<String, String> village;
    private final HashMap<String, String> varity;


    public FertRegistrationListAdapter(List<CaneConfirmationRegistrationList> caneConfirmationRegistrationLists, Activity context, UpdateRemain updateArea) {
        this.caneConfirmationRegistrationLists = caneConfirmationRegistrationLists;
        this.activity = context;
        FertRegistrationListAdapter.updateArea = updateArea;
        DBHelper dbHelper = new DBHelper(activity);
        village = dbHelper.getVillageHashMap();
        varity = dbHelper.getVarietyHashMap();

    }

    @NonNull
    @Override
    public FertRegistrationListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registration_fert, parent, false);
        return new FertRegistrationListHolder(v);
    }

    @Override
    public void onBindViewHolder(FertRegistrationListHolder holder, final int position) {
        // holder.setIsRecyclable(false);
        CaneConfirmationRegistrationList caneConfirmationRegistrationList = caneConfirmationRegistrationLists.get(holder.getBindingAdapterPosition());

        holder.txtnondvillagetxt.setText(village.containsKey(caneConfirmationRegistrationList.getVilleageCode()) ? village.get(caneConfirmationRegistrationList.getVilleageCode()) : caneConfirmationRegistrationList.getVilleageCode());
        holder.txtplotnotxt.setText(caneConfirmationRegistrationList.getPlotNo());
        holder.txtareatxt.setText(String.valueOf(caneConfirmationRegistrationList.getNarea()));
        holder.txtvaritytxt.setText(varity.containsKey(caneConfirmationRegistrationList.getNcaneVarity()) ? varity.get(caneConfirmationRegistrationList.getNcaneVarity()) : "");
        holder.chkplotsel.setChecked(caneConfirmationRegistrationList.isChecked());

        holder.chkplotsel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                caneConfirmationRegistrationList.setChecked(checked);
                caneConfirmationRegistrationLists.set(holder.getBindingAdapterPosition(), caneConfirmationRegistrationList);
                try {
                    notifyItemChanged(holder.getBindingAdapterPosition());
                }catch (Exception e) {
                    e.printStackTrace();
                }
                if (updateArea != null) {
                    updateArea.updateRemain();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return caneConfirmationRegistrationLists.size();
    }

}