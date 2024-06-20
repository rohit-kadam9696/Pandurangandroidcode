package com.twd.chitboyapp.spsskl.constant;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.twd.chitboyapp.spsskl.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerExample extends DialogFragment {
    private OnDateSetListener onDateSetListener;

    @SuppressLint("SimpleDateFormat")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        long mindate = getArguments().getLong(getResources().getString(R.string.mindate), 0);
        long maxdate = getArguments().getLong(getResources().getString(R.string.maxdate), 0);
        String mydate = getArguments().getString(getResources().getString(R.string.datepara));
        final Calendar cal = Calendar.getInstance();
        ConstantFunction.updateResources(getActivity(), "mr");
        if (mydate != null && !mydate.isEmpty() && !mydate.trim().equals("")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                cal.setTime(sdf.parse(mydate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog dpd = new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);
        if (mindate != 0) {
            dpd.getDatePicker().setMinDate(mindate);
        }
        if (maxdate != 0) {
            dpd.getDatePicker().setMaxDate(maxdate);
        }
        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.datepicker_positive_button_name), dpd);
        dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.datepicker_negative_button_name), new DatePickerDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                }
            }
        });

        dpd.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.black));
                dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.black));
            }
        });
        return dpd;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity instanceof OnDateSetListener) {
            onDateSetListener = (OnDateSetListener) activity;
        }
    }


}
