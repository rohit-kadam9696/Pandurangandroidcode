package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;
import android.content.Intent;

import com.twd.chitboyapp.spsskl.WrongTimeActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeChecker {

    public boolean checkAutoDate(Activity activity, boolean openWarning) {
        int autoTime = android.provider.Settings.Global.getInt(activity.getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0);
        if (autoTime != 1) {
            if (openWarning) {
                Intent intent = new Intent(activity, WrongTimeActivity.class);
                intent.putExtra("type", "1");
                activity.startActivity(intent);
            }
            return false;
        }
        return true;
    }

    public boolean checkServerDate(Activity activity, boolean openWarning, String serverDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar calToday = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(serverDate));
            cal.set(Calendar.MINUTE, -15);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (cal.getTime().after(calToday.getTime())) {
            if (openWarning) {
                Intent intent = new Intent(activity, WrongTimeActivity.class);
                intent.putExtra("date", serverDate);
                intent.putExtra("type", "2");
                activity.startActivity(intent);
            }
            return false;
        }
        return true;
    }
}
