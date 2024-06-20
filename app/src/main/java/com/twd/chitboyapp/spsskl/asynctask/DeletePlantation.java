package com.twd.chitboyapp.spsskl.asynctask;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.interfaces.UpdateRemain;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class DeletePlantation {

    public UpdateRemain delegate = null;
    Activity context;
    ArrayList<String> removeentry;

    public DeletePlantation(Activity context, ArrayList<String> removeentry) {
        this.context = context;
        this.removeentry = removeentry;
    }

    public void doInBackground() {
        Observable.fromCallable(new Callable<Object>() {

            @Override
            public Object call() {
                final AlertDialog[] loading = new AlertDialog[1];
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        loading[0] = ConstantFunction.createProgress(context, context.getResources().getString(R.string.pleasewait));
                        ConstantFunction.showDialog(loading[0]);
                    }
                });

                DBHelper mydb = new DBHelper(context);
                StringBuilder iddelete = new StringBuilder("'0'");
                //JSONArray jar = job.getJSONArray("list");
                int size = removeentry.size();
                for (int i = 0; i < size; i++) {
                    iddelete.append(",'").append(removeentry.get(i)).append("'");
                }
                if (size > 0) {
                    mydb.updatePlantationServer(iddelete.toString());
                }
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        ConstantFunction.dismissDialog(loading[0]);
                        if (delegate != null)
                            delegate.updateRemain();
                    }
                });

                return "1";

            }
        }).subscribeOn(Schedulers.computation()).subscribe();

    }

}
