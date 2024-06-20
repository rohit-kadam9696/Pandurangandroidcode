package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

public class DigitProcessor {
    public void processDigit(int digit, int floating, String digitchar, String centerchar, int edtId, Activity activity, Integer nextid) {
        AppCompatEditText appCompatEditText = activity.findViewById(edtId);
        AppCompatEditText nextswitch = null;
        if (nextid != null) {
            nextswitch = activity.findViewById(nextid);
        }
        appCompatEditText.setTag(true);
        appCompatEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    String text = appCompatEditText.getText().toString();
                    text = text.replaceAll(digitchar, "");
                    if (text.length() == digit + 1) {
                        appCompatEditText.setSelection(digit);
                    }
                }
                return false;
            }
        });

        appCompatEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = appCompatEditText.getText().toString();
                text = text.replaceAll(digitchar, "").replace(centerchar, "");
                int pos = text.length();
                if (pos >= digit) {
                    pos++;
                }
                appCompatEditText.setSelection(pos);
                appCompatEditText.setTag(true);
            }
        });

        AppCompatEditText finalNextswitch = nextswitch;
        appCompatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((boolean) appCompatEditText.getTag()) {
                    String text = charSequence.toString();
                    text = text.replaceAll(digitchar, "").replace(centerchar, "");
                    StringBuilder texttoset = new StringBuilder(text);
                    int pos = texttoset.length();
                    if (pos >= digit) {
                        texttoset = new StringBuilder(text.substring(0, digit) + centerchar);
                        if (pos > digit)
                            if (pos < digit + 1 + floating)
                                texttoset.append(text.substring(digit));
                            else {
                                texttoset.append(text.substring(digit, digit + floating));
                                pos = texttoset.length();
                            }
                        int addendChar = digit + floating - text.length();
                        for (int j = 0; j < addendChar; j++) {
                            texttoset.append(digitchar);
                        }
                        pos++;
                    } else {
                        int addendChar = digit - texttoset.length();
                        for (int j = 0; j < addendChar; j++) {
                            texttoset.append(digitchar);
                        }
                        texttoset.append(centerchar);
                        for (int j = 0; j < floating; j++) {
                            texttoset.append(digitchar);
                        }
                    }
                    appCompatEditText.setTag(false);
                    appCompatEditText.setText(texttoset.toString());
                    appCompatEditText.setSelection(Math.min(pos, digit + floating + 1));

                    if (pos >= (digit + floating + 1) && nextid != null) {
                        finalNextswitch.requestFocus();
                    }
                } else {
                    appCompatEditText.setTag(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
