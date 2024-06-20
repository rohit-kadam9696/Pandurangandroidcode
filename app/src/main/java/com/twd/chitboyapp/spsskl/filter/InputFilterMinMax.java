package com.twd.chitboyapp.spsskl.filter;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputFilterMinMax implements InputFilter {

    Pattern mPattern;
    private final double min;
    private final double max;

    public InputFilterMinMax(double min, double max, int maxFloat) {
        this.min = min;
        this.max = max;
        mPattern = Pattern.compile("[0-9]+((\\.[0-9]{0," + (maxFloat) + "})?)||(\\.)?");
    }

    public InputFilterMinMax(double min, double max) {
        this.min = min;
        this.max = max;
        mPattern = Pattern.compile("\\d+");
    }


    public InputFilterMinMax(String min, String max, int maxFloat) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
        mPattern = Pattern.compile("[0-9]+((\\.[0-9]{0," + (maxFloat) + "})?)||(\\.)?");
    }


    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            double input = Double.parseDouble(dest.toString() + source.toString());
            Matcher matcher = mPattern.matcher(dest.toString() + source.toString());
            if (isInRange(min, max, input) && matcher.matches()) {
                return null;
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return "";
    }

    private boolean isInRange(double min, double max, double value) {
        return value >= min && value <= max;
    }

    /*@Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {   
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }     
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }*/
}