package com.twd.chitboyapp.spsskl.constant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.twd.chitboyapp.spsskl.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        ConstantFunction.updateResources(getActivity(), "mr");
        String timePara = getArguments().getString(getResources().getString(R.string.timepara));
        if (timePara != null && !timePara.isEmpty() && !timePara.trim().equals("")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                cal.setTime(sdf.parse(MarathiHelper.convertMarathitoEnglish(timePara)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog ttd = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
        //Create and return a new instance of TimePickerDialog
        ttd.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.datepicker_positive_button_name), ttd);
        ttd.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.datepicker_negative_button_name), new DatePickerDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                }
            }
        });

        ttd.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                ttd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.black));
                ttd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.black));
            }
        });
        return ttd;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TimePickerDialog.OnTimeSetListener) {
            onTimeSetListener = (TimePickerDialog.OnTimeSetListener) context;
        }
    }
}