package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;


public class PDFSettingActivity extends AppCompatActivity implements SingleReturn {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfsetting);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        SingleSpinnerSearch sspalignhead = findViewById(R.id.sspalignhead);
        SingleSpinnerSearch sspaligneven = findViewById(R.id.sspaligneven);
        SingleSpinnerSearch sspalignodd = findViewById(R.id.sspalignodd);
        SingleSpinnerSearch sspalignfoot = findViewById(R.id.sspalignfoot);

        Activity activity = PDFSettingActivity.this;
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.initSingle(sspalignhead, activity, getResources().getString(R.string.select));
        ssh.initSingle(sspaligneven, activity, getResources().getString(R.string.select));
        ssh.initSingle(sspalignodd, activity, getResources().getString(R.string.select));
        ssh.initSingle(sspalignfoot, activity, getResources().getString(R.string.select));
        ssh.singleReturn = this;

        setData(ssh);
        otherOperations();

        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJSON();
            }
        });

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AppCompatButton btnrestoredefault = findViewById(R.id.btnrestoredefault);
        btnrestoredefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantFunction cf = new ConstantFunction();
                cf.putSharedPrefValue(new String[]{getResources().getString(R.string.prefprintersetting)}, new String[]{String.format(getResources().getString(R.string.defaultJson), getResources().getString(R.string.myfactory))}, activity);
                setData(ssh);
            }
        });
    }

    private void updateJSON() {

        RadioGroup rgpagesize = findViewById(R.id.rgpagesize);

        AppCompatEditText edtwidth = findViewById(R.id.edtwidth);
        AppCompatEditText edtheight = findViewById(R.id.edtheight);
        AppCompatTextView txtwxhlbl = findViewById(R.id.txtwxhlbl);
        AppCompatTextView txtxlbl = findViewById(R.id.txtxlbl);

        RadioGroup rgpageheader = findViewById(R.id.rgpageheader);

        AppCompatEditText edtpageheading = findViewById(R.id.edtpageheading);
        AppCompatEditText edtpagesubheading = findViewById(R.id.edtpagesubheading);

        AppCompatEditText edtheadfont = findViewById(R.id.edtheadfont);
        AppCompatEditText edtsubheadfont = findViewById(R.id.edtsubheadfont);

        AppCompatButton btnheadcolor = findViewById(R.id.btnheadcolor);
        AppCompatButton btnsubheadcolor = findViewById(R.id.btnsubheadcolor);

        RadioGroup rgpageheaderlogo = findViewById(R.id.rgpageheaderlogo);
        RadioGroup rgpageorientation = findViewById(R.id.rgpageorientation);

        AppCompatEditText edtvarticalmargin = findViewById(R.id.edtvarticalmargin);
        AppCompatEditText edthorzontalmargin = findViewById(R.id.edthorzontalmargin);

        AppCompatEditText edttableheadsize = findViewById(R.id.edttableheadsize);
        AppCompatEditText edttableevensize = findViewById(R.id.edttableevensize);
        AppCompatEditText edttableoddsize = findViewById(R.id.edttableoddsize);
        AppCompatEditText edttablefootsize = findViewById(R.id.edttablefootsize);

        AppCompatButton btntextheadcolor = findViewById(R.id.btntextheadcolor);
        AppCompatButton btntextevencolor = findViewById(R.id.btntextevencolor);
        AppCompatButton btntextoddcolor = findViewById(R.id.btntextoddcolor);
        AppCompatButton btntextfootcolor = findViewById(R.id.btntextfootcolor);

        AppCompatButton btnbgheadcolor = findViewById(R.id.btnbgheadcolor);
        AppCompatButton btnbgevencolor = findViewById(R.id.btnbgevencolor);
        AppCompatButton btnbgoddcolor = findViewById(R.id.btnbgoddcolor);
        AppCompatButton btnbgfootcolor = findViewById(R.id.btnbgfootcolor);

        SingleSpinnerSearch sspalignhead = findViewById(R.id.sspalignhead);
        SingleSpinnerSearch sspaligneven = findViewById(R.id.sspaligneven);
        SingleSpinnerSearch sspalignodd = findViewById(R.id.sspalignodd);
        SingleSpinnerSearch sspalignfoot = findViewById(R.id.sspalignfoot);

        ConstantFunction cf = new ConstantFunction();
        try {
            Activity activity = this;
            JSONObject job = new JSONObject();
            int checkedIdPageSize = rgpagesize.getCheckedRadioButtonId();
            if (checkedIdPageSize != -1) {
                RadioButton pgSizeSel = findViewById(checkedIdPageSize);
                job.put("pagesize", pgSizeSel.getText().toString());
            } else {
                Constant.showToast(getResources().getString(R.string.errorpagesizenotselected), activity, R.drawable.ic_wrong);
                return;
            }

            String width = edtwidth.getText().toString();
            String height = edtheight.getText().toString();

            ServerValidation sv = new ServerValidation();
            if (!sv.checkFloat(width)) {
                edtwidth.setVisibility(View.VISIBLE);
                edtheight.setVisibility(View.VISIBLE);

                txtwxhlbl.setVisibility(View.VISIBLE);
                txtwxhlbl.setVisibility(View.VISIBLE);
                edtwidth.setError(getResources().getString(R.string.errorwidthnotfound));
                return;
            } else {
                job.put("w", Double.parseDouble(width));
            }

            if (!sv.checkFloat(height)) {
                edtwidth.setVisibility(View.VISIBLE);
                edtheight.setVisibility(View.VISIBLE);

                txtwxhlbl.setVisibility(View.VISIBLE);
                txtxlbl.setVisibility(View.VISIBLE);
                edtheight.setError(getResources().getString(R.string.errorwidthnotfound));
                return;
            } else {
                job.put("h", Double.parseDouble(height));
            }

            int checkedIdHeader = rgpageheader.getCheckedRadioButtonId();
            if (checkedIdHeader != -1) {
                if (checkedIdHeader == R.id.rbhyes) {
                    job.put("phyn", "Y");
                    String headfont = edtheadfont.getText().toString();
                    String subheadfont = edtsubheadfont.getText().toString();

                    if (!sv.checkNumber(headfont)) {
                        edtheadfont.setError(getResources().getString(R.string.errorheadfont));
                        return;
                    }

                    if (!sv.checkNumber(subheadfont)) {
                        edtsubheadfont.setError(getResources().getString(R.string.errorsubheadfont));
                        return;
                    }

                    job.put("hf", Integer.parseInt(headfont));
                    job.put("shf", Integer.parseInt(subheadfont));

                } else job.put("phyn", "N");
            } else {
                Constant.showToast(getResources().getString(R.string.errorpagesizenotselected), activity, R.drawable.ic_wrong);
                return;
            }

            String htc = (String) btnheadcolor.getTag();
            job.put("htc", htc);
            String shtc = (String) btnsubheadcolor.getTag();
            job.put("shtc", shtc);

            String ph = edtpageheading.getText().toString();
            job.put("ph", ph);
            String psh = edtpagesubheading.getText().toString();
            job.put("psh", psh);

            int checkedIdhrl = rgpageheaderlogo.getCheckedRadioButtonId();
            if (checkedIdhrl != -1) {
                if (checkedIdhrl == R.id.rbhgyes) job.put("hrl", "Y");
                else job.put("hrl", "N");
            } else {
                Constant.showToast(getResources().getString(R.string.errorpagesizenotselected), activity, R.drawable.ic_wrong);
                return;
            }

            int checkedIdOr = rgpageorientation.getCheckedRadioButtonId();
            if (checkedIdOr != -1) {
                if (checkedIdOr == R.id.rboportrait) job.put("or", "P");
                else job.put("or", "L");
            } else {
                Constant.showToast(getResources().getString(R.string.errorpagesizenotselected), activity, R.drawable.ic_wrong);
                return;
            }

            String varticalmargin = edtvarticalmargin.getText().toString();
            String horzontalmargin = edthorzontalmargin.getText().toString();

            if (!sv.checkNumber(varticalmargin)) {
                edtvarticalmargin.setError(getResources().getString(R.string.errormargin));
                return;
            }

            if (!sv.checkNumber(horzontalmargin)) {
                edthorzontalmargin.setError(getResources().getString(R.string.errormargin));
                return;
            }

            job.put("vm", Integer.parseInt(varticalmargin));
            job.put("hm", Integer.parseInt(horzontalmargin));

            String tableheadsize = edttableheadsize.getText().toString();
            String tableevensize = edttableevensize.getText().toString();
            String tableoddsize = edttableoddsize.getText().toString();
            String tablefootsize = edttablefootsize.getText().toString();

            if (!sv.checkNumber(tableheadsize)) {
                edttableheadsize.setError(getResources().getString(R.string.errorfontsize));
                return;
            }

            if (!sv.checkNumber(tableevensize)) {
                edttableevensize.setError(getResources().getString(R.string.errorfontsize));
                return;
            }

            if (!sv.checkNumber(tableoddsize)) {
                edttableoddsize.setError(getResources().getString(R.string.errorfontsize));
                return;
            }

            if (!sv.checkNumber(tablefootsize)) {
                edttablefootsize.setError(getResources().getString(R.string.errorfontsize));
                return;
            }

            job.put("thfs", Integer.parseInt(tableheadsize));
            job.put("tefs", Integer.parseInt(tableevensize));
            job.put("tofs", Integer.parseInt(tableoddsize));
            job.put("tffs", Integer.parseInt(tablefootsize));

            String thtc = job.has("thtc") ? job.getString("thtc") : "#FFFFFF";
            btntextheadcolor.setBackgroundColor(Color.parseColor(thtc));
            btntextheadcolor.setTag(thtc);

            String tetc = (String) btntextevencolor.getTag();
            job.put("tetc", tetc);

            String totc = (String) btntextoddcolor.getTag();
            job.put("totc", totc);

            String tftc = (String) btntextfootcolor.getTag();
            job.put("tftc", tftc);

            String thbc = (String) btnbgheadcolor.getTag();
            job.put("thbc", thbc);

            String tebc = (String) btnbgevencolor.getTag();
            job.put("tebc", tebc);

            String tobc = (String) btnbgoddcolor.getTag();
            job.put("tobc", tobc);

            String tfbc = (String) btnbgfootcolor.getTag();
            job.put("tfbc", tfbc);


            List<KeyPairBoolData> headalign = sspalignhead.getSelectedItems();
            if (headalign.size() == 0) {
                Constant.showToast(getResources().getString(R.string.errorselectalignment), activity, R.drawable.ic_wrong);
                return;
            } else {
                job.put("tha", (int) headalign.get(0).getId());
            }


            List<KeyPairBoolData> evenalign = sspaligneven.getSelectedItems();
            if (evenalign.size() == 0) {
                Constant.showToast(getResources().getString(R.string.errorselectalignment), activity, R.drawable.ic_wrong);
                return;
            } else {
                job.put("tea", (int) evenalign.get(0).getId());
            }


            List<KeyPairBoolData> oddalign = sspalignodd.getSelectedItems();
            if (oddalign.size() == 0) {
                Constant.showToast(getResources().getString(R.string.errorselectalignment), activity, R.drawable.ic_wrong);
                return;
            } else {
                job.put("toa", (int) oddalign.get(0).getId());
            }

            List<KeyPairBoolData> footalign = sspalignfoot.getSelectedItems();
            if (footalign.size() == 0) {
                Constant.showToast(getResources().getString(R.string.errorselectalignment), activity, R.drawable.ic_wrong);
                return;
            } else {
                job.put("tfa", (int) footalign.get(0).getId());
            }

            cf.putSharedPrefValue(new String[]{getResources().getString(R.string.prefprintersetting)}, new String[]{job.toString()}, activity);
            Constant.showToast(getResources().getString(R.string.updatedSuccessfully), PDFSettingActivity.this, R.drawable.ic_info);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private void otherOperations() {
        RadioGroup rgpagesize = findViewById(R.id.rgpagesize);
        rgpagesize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pageSizeChange(checkedId);
            }
        });

        RadioGroup rgpageheader = findViewById(R.id.rgpageheader);
        rgpageheader.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pageHeaderChange(checkedId);
            }
        });

        ColorPicker colorPicker = new ColorPicker();
        AppCompatButton btnheadcolor = findViewById(R.id.btnheadcolor);
        AppCompatButton btnsubheadcolor = findViewById(R.id.btnsubheadcolor);

        AppCompatButton btntextheadcolor = findViewById(R.id.btntextheadcolor);
        AppCompatButton btntextevencolor = findViewById(R.id.btntextevencolor);
        AppCompatButton btntextoddcolor = findViewById(R.id.btntextoddcolor);
        AppCompatButton btntextfootcolor = findViewById(R.id.btntextfootcolor);

        AppCompatButton btnbgheadcolor = findViewById(R.id.btnbgheadcolor);
        AppCompatButton btnbgevencolor = findViewById(R.id.btnbgevencolor);
        AppCompatButton btnbgoddcolor = findViewById(R.id.btnbgoddcolor);
        AppCompatButton btnbgfootcolor = findViewById(R.id.btnbgfootcolor);

        btnheadcolor.setOnClickListener(colorPicker);
        btnsubheadcolor.setOnClickListener(colorPicker);

        btntextheadcolor.setOnClickListener(colorPicker);
        btntextevencolor.setOnClickListener(colorPicker);
        btntextoddcolor.setOnClickListener(colorPicker);
        btntextfootcolor.setOnClickListener(colorPicker);

        btnbgheadcolor.setOnClickListener(colorPicker);
        btnbgevencolor.setOnClickListener(colorPicker);
        btnbgoddcolor.setOnClickListener(colorPicker);
        btnbgfootcolor.setOnClickListener(colorPicker);
    }

    private void pageSizeChange(int checkedId) {
        {
            AppCompatEditText edtwidth = findViewById(R.id.edtwidth);
            AppCompatEditText edtheight = findViewById(R.id.edtheight);

            AppCompatTextView txtwxhlbl = findViewById(R.id.txtwxhlbl);
            AppCompatTextView txtxlbl = findViewById(R.id.txtxlbl);

            if (checkedId == R.id.rba4) {
                edtwidth.setText("8.3");
                edtheight.setText("11.7");

                edtwidth.setVisibility(View.GONE);
                edtheight.setVisibility(View.GONE);
                txtwxhlbl.setVisibility(View.GONE);
                txtxlbl.setVisibility(View.GONE);
            } else if (checkedId == R.id.rblegal) {
                edtwidth.setText("8.5");
                edtheight.setText("14");

                edtwidth.setVisibility(View.GONE);
                edtheight.setVisibility(View.GONE);
                txtwxhlbl.setVisibility(View.GONE);
                txtxlbl.setVisibility(View.GONE);
            } else if (checkedId == R.id.rbcustom) {
                edtwidth.setText("");
                edtheight.setText("");

                edtwidth.setVisibility(View.VISIBLE);
                edtheight.setVisibility(View.VISIBLE);
                txtwxhlbl.setVisibility(View.VISIBLE);
                txtxlbl.setVisibility(View.VISIBLE);
            }
        }
    }

    private void pageHeaderChange(int checkedId) {
        AppCompatTextView txtpageheaderinglbl = findViewById(R.id.txtpageheaderinglbl);
        AppCompatEditText edtpageheading = findViewById(R.id.edtpageheading);
        AppCompatTextView txtpagesubheaderinglbl = findViewById(R.id.txtpagesubheaderinglbl);
        AppCompatEditText edtpagesubheading = findViewById(R.id.edtpagesubheading);
        LinearLayoutCompat llcommonheadings = findViewById(R.id.llcommonheadings);
        AppCompatTextView txtpageheaderlogolbl = findViewById(R.id.txtpageheaderlogolbl);
        RadioGroup rgpageheaderlogo = findViewById(R.id.rgpageheaderlogo);

        if (checkedId == R.id.rbhyes) {
            txtpageheaderinglbl.setVisibility(View.VISIBLE);
            edtpageheading.setVisibility(View.VISIBLE);
            txtpagesubheaderinglbl.setVisibility(View.VISIBLE);
            edtpagesubheading.setVisibility(View.VISIBLE);
            llcommonheadings.setVisibility(View.VISIBLE);
            txtpageheaderlogolbl.setVisibility(View.VISIBLE);
            rgpageheaderlogo.setVisibility(View.VISIBLE);
        } else {
            txtpageheaderinglbl.setVisibility(View.GONE);
            edtpageheading.setVisibility(View.GONE);
            txtpagesubheaderinglbl.setVisibility(View.GONE);
            edtpagesubheading.setVisibility(View.GONE);
            llcommonheadings.setVisibility(View.GONE);
            txtpageheaderlogolbl.setVisibility(View.GONE);
            rgpageheaderlogo.setVisibility(View.GONE);
        }
    }

    public void openColorPickerDialogue(int btnId) {

        // the AmbilWarnaDialog callback needs 3 parameters
        // one is the context, second is default color,
        AppCompatButton button = findViewById(btnId);
        String buttonColor = (String) button.getTag();
        int defaultColor = Integer.valueOf(buttonColor.substring(1), 16);

        final AmbilWarnaDialog colorPickerDialogue = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // leave this function body as
                // blank, as the dialog
                // automatically closes when
                // clicked on cancel button
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // change the mDefaultColor to
                // change the GFG text color as
                // it is returned when the OK
                // button is clicked from the
                // color picker dialog
                String choosenColor = String.format("#%06X", (0xFFFFFF & color));

                // now change the picked color
                // preview box to mDefaultColor
                button.setBackgroundColor(Color.parseColor(choosenColor));
                button.setTag(choosenColor);
            }
        });
        colorPickerDialogue.show();
    }

    private void setData(SingleSelectHandler ssh) {

        RadioButton rba4 = findViewById(R.id.rba4);
        RadioButton rblegal = findViewById(R.id.rblegal);
        RadioButton rbcustom = findViewById(R.id.rbcustom);

        AppCompatEditText edtwidth = findViewById(R.id.edtwidth);
        AppCompatEditText edtheight = findViewById(R.id.edtheight);

        RadioButton rbhyes = findViewById(R.id.rbhyes);
        RadioButton rbhno = findViewById(R.id.rbhno);

        AppCompatEditText edtpageheading = findViewById(R.id.edtpageheading);
        AppCompatEditText edtpagesubheading = findViewById(R.id.edtpagesubheading);

        AppCompatEditText edtheadfont = findViewById(R.id.edtheadfont);
        AppCompatEditText edtsubheadfont = findViewById(R.id.edtsubheadfont);

        AppCompatButton btnheadcolor = findViewById(R.id.btnheadcolor);
        AppCompatButton btnsubheadcolor = findViewById(R.id.btnsubheadcolor);

        RadioButton rbhgyes = findViewById(R.id.rbhgyes);
        RadioButton rbhgno = findViewById(R.id.rbhgno);

        RadioButton rboportrait = findViewById(R.id.rboportrait);
        RadioButton rbolandscape = findViewById(R.id.rbolandscape);

        AppCompatEditText edtvarticalmargin = findViewById(R.id.edtvarticalmargin);
        AppCompatEditText edthorzontalmargin = findViewById(R.id.edthorzontalmargin);

        AppCompatEditText edttableheadsize = findViewById(R.id.edttableheadsize);
        AppCompatEditText edttableevensize = findViewById(R.id.edttableevensize);
        AppCompatEditText edttableoddsize = findViewById(R.id.edttableoddsize);
        AppCompatEditText edttablefootsize = findViewById(R.id.edttablefootsize);

        AppCompatButton btntextheadcolor = findViewById(R.id.btntextheadcolor);
        AppCompatButton btntextevencolor = findViewById(R.id.btntextevencolor);
        AppCompatButton btntextoddcolor = findViewById(R.id.btntextoddcolor);
        AppCompatButton btntextfootcolor = findViewById(R.id.btntextfootcolor);

        AppCompatButton btnbgheadcolor = findViewById(R.id.btnbgheadcolor);
        AppCompatButton btnbgevencolor = findViewById(R.id.btnbgevencolor);
        AppCompatButton btnbgoddcolor = findViewById(R.id.btnbgoddcolor);
        AppCompatButton btnbgfootcolor = findViewById(R.id.btnbgfootcolor);

        SingleSpinnerSearch sspalignhead = findViewById(R.id.sspalignhead);
        SingleSpinnerSearch sspaligneven = findViewById(R.id.sspaligneven);
        SingleSpinnerSearch sspalignodd = findViewById(R.id.sspalignodd);
        SingleSpinnerSearch sspalignfoot = findViewById(R.id.sspalignfoot);

        ConstantFunction cf = new ConstantFunction();
        String data[] = cf.getSharedPrefValue(PDFSettingActivity.this, new String[]{getResources().getString(R.string.prefprintersetting)}, new String[]{String.format(getResources().getString(R.string.defaultJson), getResources().getString(R.string.myfactory))});
        try {
            JSONObject job = new JSONObject(data[0]);
            int checkedIdPageSize;
            String pagesize = job.has("pagesize") ? job.getString("pagesize") : "A4";
            if (rba4.getText().toString().equalsIgnoreCase(pagesize)) {
                rba4.setChecked(true);
                checkedIdPageSize = R.id.rba4;
            } else if (rblegal.getText().toString().equalsIgnoreCase(pagesize)) {
                rblegal.setChecked(true);
                checkedIdPageSize = R.id.rblegal;
            } else if (rbcustom.getText().toString().equalsIgnoreCase(pagesize)) {
                rbcustom.setChecked(true);
                checkedIdPageSize = R.id.rbcustom;
            } else {
                rba4.setChecked(true);
                checkedIdPageSize = R.id.rba4;
            }
            pageSizeChange(checkedIdPageSize);

            String w = job.has("w") ? String.valueOf(job.getDouble("w")) : "8.3";
            edtwidth.setText(w);

            String h = job.has("h") ? String.valueOf(job.getDouble("h")) : "11.7";
            edtheight.setText(h);

            int checkedIdHeader;
            String phyn = job.has("phyn") ? job.getString("phyn") : "Y";
            if (phyn.equalsIgnoreCase("Y")) {
                rbhyes.setChecked(true);
                checkedIdHeader = R.id.rbhyes;
            } else {
                rbhno.setChecked(true);
                checkedIdHeader = R.id.rbhno;
            }
            pageHeaderChange(checkedIdHeader);

            String ph = job.has("ph") ? job.getString("ph") : getResources().getString(R.string.myfactory);
            String psh = job.has("psh") ? job.getString("psh") : "";

            edtpageheading.setText(ph);
            edtpagesubheading.setText(psh);

            String hf = job.has("hf") ? String.valueOf(job.getInt("hf")) : "16";
            String shf = job.has("shf") ? String.valueOf(job.getInt("shf")) : "14";
            edtheadfont.setText(hf);
            edtsubheadfont.setText(shf);

            String htc = job.has("htc") ? job.getString("htc") : "#000080";
            btnheadcolor.setBackgroundColor(Color.parseColor(htc));
            btnheadcolor.setTag(htc);

            String shtc = job.has("shtc") ? job.getString("shtc") : "#000080";
            btnsubheadcolor.setBackgroundColor(Color.parseColor(shtc));
            btnsubheadcolor.setTag(shtc);

            String hrl = job.has("hrl") ? job.getString("hrl") : "Y";
            if (hrl.equalsIgnoreCase("Y")) rbhgyes.setChecked(true);
            else rbhgno.setChecked(true);

            String or = job.has("or") ? job.getString("or") : "P";
            if (or.equalsIgnoreCase("P")) rboportrait.setChecked(true);
            else rbolandscape.setChecked(true);

            String vm = job.has("vm") ? String.valueOf(job.getInt("vm")) : "20";
            String hm = job.has("hm") ? String.valueOf(job.getInt("hm")) : "20";
            edtvarticalmargin.setText(vm);
            edthorzontalmargin.setText(hm);

            String thfs = job.has("thfs") ? String.valueOf(job.getInt("thfs")) : "12";
            String tefs = job.has("tefs") ? String.valueOf(job.getInt("tefs")) : "12";
            String tofs = job.has("tofs") ? String.valueOf(job.getInt("tofs")) : "12";
            String tffs = job.has("tffs") ? String.valueOf(job.getInt("tffs")) : "12";
            edttableheadsize.setText(thfs);
            edttableevensize.setText(tefs);
            edttableoddsize.setText(tofs);
            edttablefootsize.setText(tffs);

            String thtc = job.has("thtc") ? job.getString("thtc") : "#FFFFFF";
            btntextheadcolor.setBackgroundColor(Color.parseColor(thtc));
            btntextheadcolor.setTag(thtc);

            String tetc = job.has("tetc") ? job.getString("tetc") : "#000080";
            btntextevencolor.setBackgroundColor(Color.parseColor(tetc));
            btntextevencolor.setTag(tetc);

            String totc = job.has("totc") ? job.getString("totc") : "#000080";
            btntextoddcolor.setBackgroundColor(Color.parseColor(totc));
            btntextoddcolor.setTag(totc);

            String tftc = job.has("tftc") ? job.getString("tftc") : "#FFFFFF";
            btntextfootcolor.setBackgroundColor(Color.parseColor(tftc));
            btntextfootcolor.setTag(tftc);


            String thbc = job.has("thbc") ? job.getString("thbc") : "#004977";
            btnbgheadcolor.setBackgroundColor(Color.parseColor(thbc));
            btnbgheadcolor.setTag(thbc);

            String tebc = job.has("tebc") ? job.getString("tebc") : "#cee9f2";
            btnbgevencolor.setBackgroundColor(Color.parseColor(tebc));
            btnbgevencolor.setTag(tebc);

            String tobc = job.has("tobc") ? job.getString("tobc") : "#f6f8f9";
            btnbgoddcolor.setBackgroundColor(Color.parseColor(tobc));
            btnbgoddcolor.setTag(tobc);

            String tfbc = job.has("tfbc") ? job.getString("tfbc") : "#00619E";
            btnbgfootcolor.setBackgroundColor(Color.parseColor(tfbc));
            btnbgfootcolor.setTag(tfbc);

            List<KeyPairBoolData> alignlist = new ArrayList<>();
            KeyPairBoolData aligndata = new KeyPairBoolData();
            aligndata.setId(1);
            aligndata.setName("D - Deafult");
            alignlist.add(aligndata);

            aligndata = new KeyPairBoolData();
            aligndata.setId(2);
            aligndata.setName("L - Left");
            alignlist.add(aligndata);

            aligndata = new KeyPairBoolData();
            aligndata.setId(3);
            aligndata.setName("C - Center");
            alignlist.add(aligndata);

            aligndata = new KeyPairBoolData();
            aligndata.setId(4);
            aligndata.setName("R - Right");
            alignlist.add(aligndata);

            List<KeyPairBoolData> headalign = alignlist;
            int tha = job.has("tha") ? job.getInt("tha") : 1;
            aligndata = headalign.get(tha - 1);
            aligndata.setSelected(true);
            headalign.set(tha - 1, aligndata);

            ssh.setSingleItems(sspalignhead, headalign, DataSetter.HEAD);


            List<KeyPairBoolData> evenalign = alignlist;
            int tea = job.has("tea") ? job.getInt("tea") : 1;
            aligndata = evenalign.get(tea - 1);
            aligndata.setSelected(true);
            evenalign.set(tea - 1, aligndata);

            ssh.setSingleItems(sspaligneven, evenalign, DataSetter.EVEN);


            List<KeyPairBoolData> oddalign = alignlist;
            int toa = job.has("toa") ? job.getInt("toa") : 1;
            aligndata = oddalign.get(toa - 1);
            aligndata.setSelected(true);
            oddalign.set(toa - 1, aligndata);

            ssh.setSingleItems(sspalignodd, oddalign, DataSetter.ODD);

            List<KeyPairBoolData> footalign = alignlist;
            int tfa = job.has("tfa") ? job.getInt("tfa") : 1;
            aligndata = footalign.get(tfa - 1);
            aligndata.setSelected(true);
            footalign.set(tfa - 1, aligndata);

            ssh.setSingleItems(sspalignfoot, footalign, DataSetter.FOOT);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MenuHandler cf = new MenuHandler();
        return cf.openCommon(this, item, null);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, this);
    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }

    class ColorPicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            openColorPickerDialogue(v.getId());
        }
    }
}