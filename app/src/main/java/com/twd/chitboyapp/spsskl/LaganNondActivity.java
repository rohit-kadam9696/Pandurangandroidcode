package com.twd.chitboyapp.spsskl;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DatePickerExample;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.ImageConstant;
import com.twd.chitboyapp.spsskl.constant.InternetConnection;
import com.twd.chitboyapp.spsskl.constant.LocationUpdatetor;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.Confirmation;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.WarningEnum;
import com.twd.chitboyapp.spsskl.filter.InputFilterMinMax;
import com.twd.chitboyapp.spsskl.interfaces.LocationUpdateListener;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.pojo.FarmerTypeMaster;
import com.twd.chitboyapp.spsskl.pojo.HangamMaster;
import com.twd.chitboyapp.spsskl.pojo.PlantLocation;
import com.twd.chitboyapp.spsskl.pojo.PlantationBean;
import com.twd.chitboyapp.spsskl.pojo.SectionMaster;
import com.twd.chitboyapp.spsskl.pojo.VillageMaster;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LaganNondActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, OnMapReadyCallback, LocationUpdateListener, TextView.OnEditorActionListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, SingleReturn {

    int step = 1;
    Long mindate = 0L, maxdate = 0L;
    Datepicker datepicker;
    private GoogleMap mMap;
    LocationUpdatetor locationUpdatetor;
    private ArrayList<LatLng> arrayPoints = null;
    private ArrayList<Double> accuracy = null;
    Polygon polygon = null;
    private int locationType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lagan_nond);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        String farmercode = getIntent().getStringExtra("farmercode");

        // if edit do not excute
        ConstantFunction cf = new ConstantFunction();
        Activity activity = this;
        cf.generateOfflinePlotNo(activity);
        // object creation
        AppCompatButton btnprev, btnnext, btnregister, btntakepoint, btnundo, btnclear, btndone, btnskip;
        AppCompatEditText edtplantationdate, edtarea, edtdistance, edtsurveyno;

        // Object Linking to XML
        btnprev = findViewById(R.id.btnprev);
        btnnext = findViewById(R.id.btnnext);
        btnregister = findViewById(R.id.btnregister);
        btntakepoint = findViewById(R.id.btntakepoint);
        btnundo = findViewById(R.id.btnundo);
        btnclear = findViewById(R.id.btnclear);
        btndone = findViewById(R.id.btndone);
        btnskip = findViewById(R.id.btnskip);

        edtplantationdate = findViewById(R.id.edtplantaiondate);
        edtarea = findViewById(R.id.edtarea);
        edtdistance = findViewById(R.id.edtdistance);
        edtsurveyno = findViewById(R.id.edtsurveyno);
        moveStep();

        btnprev.setOnClickListener(this);
        btnnext.setOnClickListener(this);
        btnregister.setOnClickListener(this);
        btntakepoint.setOnClickListener(this);
        btnundo.setOnClickListener(this);
        btnclear.setOnClickListener(this);
        btndone.setOnClickListener(this);
        btnskip.setOnClickListener(this);

        setFarmerAndSeason("F" + farmercode);
        initSingleSpinner();

        edtsurveyno.setOnEditorActionListener(this);
        edtarea.setOnEditorActionListener(this);
        edtdistance.setOnEditorActionListener(this);

        photoConfig(activity);
        doMapInilization();
        startLocation(activity);

        InputFilterMinMax inputFilterarea = new InputFilterMinMax(0, 25.00, 2);
        edtarea.setFilters(new InputFilter[]{inputFilterarea});

        edtplantationdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantFunction cf = new ConstantFunction();
                cf.hideKeyboard(view, activity);
                if (mindate != 0 && maxdate != 0) {
                    datepicker = Datepicker.PLANTATIONDATE;
                    DatePickerExample dateFragment = new DatePickerExample();
                    Bundle datedata = new Bundle();// create bundle instance
                    String date = edtplantationdate.getText().toString();
                    if (date.equals("")) {
                        Calendar cal = Calendar.getInstance();
                        long todaydate = cal.getTimeInMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        if (mindate > todaydate || maxdate < todaydate) {
                            cal.setTimeInMillis(mindate);
                        }
                        date = MarathiHelper.convertMarathitoEnglish(sdf.format(cal.getTime()));
                    }
                    datedata.putString(getResources().getString(R.string.datepara), date);// put string to pass with a key value
                    datedata.putLong(getResources().getString(R.string.mindate), mindate);// put string to pass with a key value
                    datedata.putLong(getResources().getString(R.string.maxdate), maxdate);// put string to pass with a key value
                    dateFragment.setArguments(datedata);// Set bundle data to fragment
                    dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
                } else {
                    edtplantationdate.setError(getResources().getString(R.string.errorchoosehangamfirst));
                }
            }
        });

        final int[] prevlen = {0};
        edtarea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String area = edtarea.getText().toString();
                if (area.length() == 1) {
                    if (area.charAt(0) == '.') {
                        edtarea.setText("0.");
                        edtarea.setSelection(edtarea.getText().toString().length());
                    } else if (prevlen[0] < 1) {
                        edtarea.setText(edtarea.getText().toString() + ".");
                        edtarea.setSelection(edtarea.getText().toString().length());
                    }
                }
                prevlen[0] = edtarea.length();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtarea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String area = edtarea.getText().toString();
                    ServerValidation sv = new ServerValidation();
                    if (area.endsWith(".")) {
                        area = area.replace(".", "");
                    }
                    if (sv.checkFloat(area)) {
                        double darea = Double.parseDouble(area);
                        if (darea > 25) {
                            edtarea.setError(getResources().getString(R.string.errormaxarea));
                            edtarea.setText("");
                        } else if (darea > 10) {
                            openWarning(WarningEnum.AREA, edtarea);
                        } else {
                            DecimalFormat df = new DecimalFormat("#0.00");
                            edtarea.setText(MarathiHelper.convertMarathitoEnglish(df.format(darea)));
                        }
                    }
                }
            }
        });

        edtdistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String distance = edtdistance.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (sv.checkFloat(distance)) {
                    double ddistance = Double.parseDouble(distance);
                    if (ddistance > 100) {
                        edtdistance.setError(getResources().getString(R.string.errormaxdistaqnce));
                        edtdistance.setText("");
                    } else if (ddistance > 50) {
                        openWarning(WarningEnum.DISTANCE, edtdistance);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    private void photoConfig(Activity activity) {
        AppCompatImageView imgtakephoto = findViewById(R.id.imgtakephoto);
        imgtakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageConstant imageConstant = new ImageConstant();
                imageConstant.openCameraIntent(activity, "plot", true);
            }
        });
    }

    private void doMapInilization() {
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);
        arrayPoints = new ArrayList<>();
        accuracy = new ArrayList<>();
    }

    private void openWarning(WarningEnum warningEnum, AppCompatEditText edt) {

        final Dialog dialog = new Dialog(LaganNondActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_delete);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatTextView txt_deletehead = dialog.findViewById(R.id.txt_deletehead);
        AppCompatTextView message = dialog.findViewById(R.id.message);

        txt_deletehead.setText(getResources().getString(R.string.warninghead));

        switch (warningEnum) {
            case AREA:
                message.setText(getResources().getString(R.string.areabeyond10));
                break;
            case DISTANCE:
                message.setText(getResources().getString(R.string.distancebeyond50));
                break;
        }

        AppCompatButton btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
        AppCompatButton btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);
        btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btncanceldelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt.setText("");
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private void setFarmerAndSeason(String farmerCodeF) {
        String[] key = new String[]{getResources().getString(R.string.prefseason)};
        String[] value = new String[]{""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(LaganNondActivity.this, key, value);

        DBHelper dbHelper = new DBHelper(LaganNondActivity.this);
        EntityMasterDetail entityMasterDetail = dbHelper.getEntityById(farmerCodeF);
        FarmerTypeMaster farmerTypeMaster = dbHelper.getFarmerTypeById(String.valueOf(entityMasterDetail.getNfarmerTypeId()));

        AppCompatTextView txtseasonval, txtfarmercodetxt, txtfarmernametxt, txtfarmertypetxt, txtfarmertypetxtid;
        txtseasonval = findViewById(R.id.txtseasonval);
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtfarmernametxt = findViewById(R.id.txtfarmernametxt);
        txtfarmertypetxt = findViewById(R.id.txtfarmertypetxt);
        txtfarmertypetxtid = findViewById(R.id.txtfarmertypetxtid);

        txtseasonval.setText(sharedPrefval[0]);
        txtfarmercodetxt.setText(farmerCodeF);
        txtfarmernametxt.setText(entityMasterDetail.getVentityNameLocal());
        txtfarmertypetxt.setText(farmerTypeMaster.getVfarmerTypeNameLocal());
        txtfarmertypetxtid.setText(String.valueOf(farmerTypeMaster.getNfarmerTypeId()));

    }

    private void initSingleSpinner() {
        Activity activity = LaganNondActivity.this;

        SingleSpinnerSearch sspvillage, ssphangam, sspvarity, sspirrigation, ssplanetype, ssharvesttype;
        sspvillage = findViewById(R.id.sspvillage);
        ssphangam = findViewById(R.id.ssphangam);
        sspvarity = findViewById(R.id.sspvarity);
        sspirrigation = findViewById(R.id.sspirrigation);
        ssplanetype = findViewById(R.id.ssplanetype);
        ssharvesttype = findViewById(R.id.sspharvesttype);

        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspvillage, activity, getResources().getString(R.string.selectvillage));
        ssh.initSingle(ssphangam, activity, getResources().getString(R.string.selecthangam));
        ssh.initSingle(sspvarity, activity, getResources().getString(R.string.selectvariety));
        ssh.initSingle(sspirrigation, activity, getResources().getString(R.string.selectirrigation));
        ssh.initSingle(ssplanetype, activity, getResources().getString(R.string.selectlanetype));
        ssh.initSingle(ssharvesttype, activity, getResources().getString(R.string.hintharvesttype));

        DBHelper dbHelper = new DBHelper(activity);
        List<KeyPairBoolData> villList = dbHelper.getVillage();
        List<KeyPairBoolData> hangamList = dbHelper.getHangam(-1);
        List<KeyPairBoolData> varityList = dbHelper.getVariety();
        List<KeyPairBoolData> irrigationList = dbHelper.getIrrigationType();
        List<KeyPairBoolData> lanetypeList = dbHelper.getLaneType();
        List<KeyPairBoolData> harvesttypList = dbHelper.getHarvestingType();

        ssh.setSingleItems(sspvillage, villList, DataSetter.VILLAGE);
        ssh.setSingleItems(ssphangam, hangamList, DataSetter.HANGAM);
        ssh.setSingleItems(sspvarity, varityList, DataSetter.VARIETY);
        ssh.setSingleItems(sspirrigation, irrigationList, DataSetter.IRRIGATION);
        ssh.setSingleItems(ssplanetype, lanetypeList, DataSetter.LANETYPE);
        ssh.setSingleItems(ssharvesttype, harvesttypList, DataSetter.HARVESTTYPE);

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        switch (dataSetter) {
            case HANGAM:
                String[] key = new String[]{getResources().getString(R.string.prefseason)};
                String[] value = new String[]{""};
                ConstantFunction cf = new ConstantFunction();
                String[] sharedPrefval = cf.getSharedPrefValue(LaganNondActivity.this, key, value);
                String[] year = sharedPrefval[0].split("-");
                int year0 = Integer.parseInt(year[0]) - 1;
                int year1 = Integer.parseInt(year[1]) - 1;

                HangamMaster hangamMaster = (HangamMaster) selectedItem.getObject();
                String startDate = hangamMaster.getDhangamStartDate();
                String endDate = hangamMaster.getDhangamEndDate();

                String[] start = startDate.split("/");
                String[] end = endDate.split("/");

                String minmonth = start[1];
                String maxmonth = end[1];
                String maxday = end[0];
                String minday = start[0];
                int minyear;
                int maxyear;

                if (Integer.parseInt(end[1]) < Integer.parseInt(start[1])) {
                    minyear = year0;
                    maxyear = year1;
                } else {
                    minyear = year0;
                    maxyear = year0;
                }

                final Calendar minc = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy HH:mm:ss");
                try {
                    minc.setTime(sdf.parse(minday + "/" + minmonth + "/" + minyear + " 00:00:00"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mindate = minc.getTimeInMillis();
                final Calendar maxc = Calendar.getInstance();
                try {
                    maxc.setTime(sdf.parse(maxday + "/" + maxmonth + "/" + maxyear + " 23:59:59"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                maxdate = maxc.getTimeInMillis();
                AppCompatEditText edtplantaiondate = findViewById(R.id.edtplantaiondate);
                edtplantaiondate.performClick();
                break;
            case VILLAGE:
                AppCompatTextView txtsectionid, txtsectiontxt;
                AppCompatEditText edtsurveyno;
                txtsectionid = findViewById(R.id.txtsectionid);
                txtsectiontxt = findViewById(R.id.txtsectiontxt);
                edtsurveyno = findViewById(R.id.edtsurveyno);

                VillageMaster villageMaster = (VillageMaster) selectedItem.getObject();
                DBHelper dbHelper = new DBHelper(LaganNondActivity.this);
                SectionMaster sectionMaster = dbHelper.getSectionById(String.valueOf(villageMaster.getNsectionId()));
                txtsectionid.setText(String.valueOf(sectionMaster.getNsectionId()));
                txtsectiontxt.setText(sectionMaster.getVsectionNameLocal());
                edtsurveyno.requestFocus();
                break;
            case HARVESTTYPE:
                AppCompatEditText edtdistance = findViewById(R.id.edtdistance);
                if (selectedItem.getId() == 3 || selectedItem.getId() == 4) {
                    InputFilterMinMax inputFilterDistance = new InputFilterMinMax(1.00, 100.00, 2);
                    edtdistance.setFilters(new InputFilter[]{inputFilterDistance});
                    edtdistance.requestFocus();
                    edtdistance.setEnabled(true);
                } else {
                    edtdistance.setEnabled(false);
                    edtdistance.setText("");
                    AppCompatImageView imgtakephoto = findViewById(R.id.imgtakephoto);
                    imgtakephoto.performClick();
                }
                break;
            case VARIETY:
                SingleSpinnerSearch sspirrigation = findViewById(R.id.sspirrigation);
                sspirrigation.performClick();
                break;
            case IRRIGATION:
                AppCompatEditText edtarea = findViewById(R.id.edtarea);
                edtarea.requestFocus();
                break;
            case LANETYPE:
                AppCompatButton btnnext = findViewById(R.id.btnnext);
                btnnext.performClick();
                break;
            default:
                break;
        }
    }

    private void moveStep() {
        ConstraintLayout clstep1, clstep2, clstep3;
        clstep1 = findViewById(R.id.clstep1);
        clstep2 = findViewById(R.id.clstep2);
        clstep3 = findViewById(R.id.clstep3);

        AppCompatButton btnprev, btnnext, btnregister;
        btnprev = findViewById(R.id.btnprev);
        btnnext = findViewById(R.id.btnnext);
        btnregister = findViewById(R.id.btnregister);

        switch (step) {
            case 1:
                btnprev.setVisibility(View.INVISIBLE);
                btnnext.setVisibility(View.VISIBLE);
                btnregister.setVisibility(View.GONE);
                clstep1.setVisibility(View.VISIBLE);
                clstep2.setVisibility(View.GONE);
                clstep3.setVisibility(View.GONE);
                break;
            case 2:
                btnprev.setVisibility(View.VISIBLE);
                btnnext.setVisibility(View.VISIBLE);
                btnregister.setVisibility(View.GONE);
                clstep1.setVisibility(View.GONE);
                clstep2.setVisibility(View.VISIBLE);
                clstep3.setVisibility(View.GONE);
                break;
            case 3:
                btnprev.setVisibility(View.VISIBLE);
                btnnext.setVisibility(View.GONE);
                btnregister.setVisibility(View.VISIBLE);
                clstep1.setVisibility(View.GONE);
                clstep2.setVisibility(View.GONE);
                clstep3.setVisibility(View.VISIBLE);
                showMyLocation();
                break;
        }

    }

    private void showMyLocation() {
        AppCompatTextView txtlat = findViewById(R.id.txtlat);
        AppCompatTextView txtlong = findViewById(R.id.txtlong);
        AppCompatTextView txtacc = findViewById(R.id.txtacc);
        String lat = txtlat.getText().toString();
        String longs = txtlong.getText().toString();
        String acc = txtacc.getText().toString();
        if (!lat.equals("")) {
            double dlat = Double.parseDouble(lat);
            double dlong = Double.parseDouble(longs);
            double dacc = Double.parseDouble(acc);
            prevAcc = dacc;
            if (dacc <= 100) {
                LatLng latLng = new LatLng(dlat, dlong);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 50);
                if (mMap != null) {
                    mMap.animateCamera(cameraUpdate);
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        Activity activity = LaganNondActivity.this;
        int viewId = view.getId();
        if (viewId == R.id.btnprev) {
            if (step != 1) {
                step--;
                moveStep();
            }
        } else if (viewId == R.id.btnnext) {
            boolean valid;
            switch (step) {
                case 1:
                    valid = validateStep1();
                    break;
                case 2:
                    valid = validateStep2();
                    break;
                default:
                    valid = false;
                    break;
            }
            if (valid) {
                step++;
                moveStep();
            }
        } else if (viewId == R.id.btnregister) {
            boolean valid;
            valid = validateStep1();
            if (!valid) {
                Constant.showToast(getResources().getString(R.string.errorstep1), activity, R.drawable.ic_wrong);
                return;
            }
            valid = validateStep2();
            if (!valid) {
                Constant.showToast(getResources().getString(R.string.errorstep2), activity, R.drawable.ic_wrong);
                return;
            }
            valid = validateStep3();
            if (!valid) {
                return;
            }
            saveOrUpdate();
        } else if (viewId == R.id.btntakepoint) {
            if (locationType != 2) {
                takePointOnMap(activity);
            } else {
                Constant.showToast(getResources().getString(R.string.errorhybridarea), LaganNondActivity.this, R.drawable.ic_wrong);
            }
        } else if (viewId == R.id.btnundo) {
            askConfirm(Confirmation.UNDO, activity);
        } else if (viewId == R.id.btnclear) {
            askConfirm(Confirmation.CLEAR, activity);
        } else if (viewId == R.id.btndone) {
            if (arrayPoints.size() >= 3) {
                calculateArea(true);
            }
        } else if (viewId == R.id.btnskip) {
            skip();
        }
    }

    private void skip() {
        AppCompatTextView txtacc = findViewById(R.id.txtacc);
        AppCompatTextView txtlat = findViewById(R.id.txtlat);
        AppCompatTextView txtlong = findViewById(R.id.txtlong);
        AppCompatTextView txtgpsareatxt = findViewById(R.id.txtgpsareatxt);
        String acc = txtacc.getText().toString();
        String latitude = txtlat.getText().toString();
        String longitude = txtlong.getText().toString();
        ServerValidation sv = new ServerValidation();
        if (sv.checkFloat(acc)) {
            double dacc = Double.parseDouble(acc);

            LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            arrayPoints.add(point);
            accuracy.add(dacc);
            txtgpsareatxt.setText("0.40");

        } else {
            Constant.showToast("waiting for location", LaganNondActivity.this, R.drawable.ic_wrong);
        }
    }

    private void askConfirm(Confirmation confirmation, Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_delete);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        AppCompatTextView message = dialog.findViewById(R.id.message);
        AppCompatTextView txt_deletehead = dialog.findViewById(R.id.txt_deletehead);
        if (confirmation == Confirmation.CLEAR) {
            message.setText(getResources().getString(R.string.messageclearpoint));
            txt_deletehead.setText(getResources().getString(R.string.clear));
        } else if (confirmation == Confirmation.UNDO) {
            message.setText(getResources().getString(R.string.messageclearlast));
            txt_deletehead.setText(getResources().getString(R.string.undo));
        }

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatButton btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
        AppCompatButton btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);

        btncanceldelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmation == Confirmation.CLEAR) {
                    mapClear();
                } else if (confirmation == Confirmation.UNDO) {
                    removeLastPoint(activity);
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void mapClear() {
        mMap.clear();
        arrayPoints.clear();
        accuracy.clear();
        checkClick = false;
        locationType = 0;
        polygon = null;
    }

    private void removeLastPoint(Activity activity) {
        int size = arrayPoints.size();
        if (size == 0) {
            Constant.showToast(getResources().getString(R.string.errornopointtaken), activity, R.drawable.ic_wrong);
        } else if (size == 1) {
            mapClear();
        } else {
            arrayPoints.remove(size - 1);
            accuracy.remove(size - 1);
            polygon = null;
            polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
            polygon.setGeodesic(true);
            polygon.setPoints(arrayPoints);
            if (size > 3) {
                calculateArea(false);
            }
        }
    }

    private void takePointOnMap(Activity activity) {
        /*LatLng point = mMap.getCameraPosition().target;*/
        AppCompatTextView txtacc = findViewById(R.id.txtacc);
        AppCompatTextView txtlat = findViewById(R.id.txtlat);
        AppCompatTextView txtlong = findViewById(R.id.txtlong);
        String acc = txtacc.getText().toString();
        String latitude = txtlat.getText().toString();
        String longitude = txtlong.getText().toString();
        ServerValidation sv = new ServerValidation();
        if (sv.checkFloat(acc)) {
            double dacc = Double.parseDouble(acc);
            if (dacc <= 20) {
                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                mMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_zoom)).draggable(true).rotation(180).snippet(String.valueOf(arrayPoints.size())));
                arrayPoints.add(point);
                accuracy.add(dacc);
                if (polygon == null) {
                    polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
                    polygon.setGeodesic(true);
                }
                polygon.setPoints(arrayPoints);
                if (arrayPoints.size() >= 3) {
                    calculateArea(false);
                }
                locationType = 1;
            } else {
                Constant.showToast(getResources().getString(R.string.reqaccnotavailable), activity, R.drawable.ic_wrong);
            }
        }
    }

    private void calculateArea(boolean showMsg) {
        Activity activity = LaganNondActivity.this;
        ArrayList<LatLng> clone = (ArrayList<LatLng>) arrayPoints.clone();
        clone.add(arrayPoints.get(0));
        double area = SphericalUtil.computeArea(clone);
        double calarea = area / 10000;

        if (calarea < 0.01 || calarea > 25) {
            if (showMsg)
                Constant.showToast(getResources().getString(R.string.errorareabetween005to25), activity, R.drawable.ic_wrong);
        } else {
            DecimalFormat df = new DecimalFormat("#0.00");
            AppCompatTextView txtgpsareatxt = findViewById(R.id.txtgpsareatxt);
            txtgpsareatxt.setText(MarathiHelper.convertMarathitoEnglish(df.format(calarea)));
        }
    }

    private boolean validateStep1() {
        ServerValidation sv = new ServerValidation();
        Activity activity = LaganNondActivity.this;
        boolean isValid = true;


        AppCompatTextView txtfarmercodetxt, txtfarmertypetxtid, txtsectionid, txtphotopath;
        AppCompatEditText edtsurveyno, edtplantaiondate, edtarea, edtdistance;
        SingleSpinnerSearch sspvillage, ssphangam, sspvarity, sspirrigation, sspharvesttype;


        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtfarmertypetxtid = findViewById(R.id.txtfarmertypetxtid);
        txtsectionid = findViewById(R.id.txtsectionid);
        edtsurveyno = findViewById(R.id.edtsurveyno);
        sspvillage = findViewById(R.id.sspvillage);
        sspvarity = findViewById(R.id.sspvarity);
        ssphangam = findViewById(R.id.ssphangam);
        sspirrigation = findViewById(R.id.sspirrigation);
        sspharvesttype = findViewById(R.id.sspharvesttype);
        edtplantaiondate = findViewById(R.id.edtplantaiondate);
        edtarea = findViewById(R.id.edtarea);
        edtdistance = findViewById(R.id.edtdistance);
        txtphotopath = findViewById(R.id.txtphotopath);

        String farmercode = txtfarmercodetxt.getText().toString();
        String fcode = farmercode.replace("F", "");
        String farmertype = txtfarmertypetxtid.getText().toString();
        String section = txtsectionid.getText().toString();
        String surveyno = edtsurveyno.getText().toString();
        String plantationdate = edtplantaiondate.getText().toString();
        String area = edtarea.getText().toString();
        String distance = edtdistance.getText().toString();
        String photopath = txtphotopath.getText().toString();

        List<KeyPairBoolData> selvillage = sspvillage.getSelectedItems();
        List<KeyPairBoolData> selhangam = ssphangam.getSelectedItems();
        List<KeyPairBoolData> selvarity = sspvarity.getSelectedItems();
        List<KeyPairBoolData> selirrigation = sspirrigation.getSelectedItems();
        List<KeyPairBoolData> selharvesttype = sspharvesttype.getSelectedItems();


        if (!sv.checkNumber(fcode)) {
            Constant.showToast(getResources().getString(R.string.errorfarmernotfound), activity, R.drawable.ic_wrong);
            isValid = false;
        }

        if (!sv.checkNumber(farmertype)) {
            Constant.showToast(getResources().getString(R.string.errorfaramertypenotfound), activity, R.drawable.ic_wrong);
            isValid = false;
        }

        if (!sv.checkNumber(section)) {
            Constant.showToast(getResources().getString(R.string.errorsectionnotfound), activity, R.drawable.ic_wrong);
            isValid = false;
        }

        if (sv.checkNull(surveyno)) {
            edtsurveyno.setError(getResources().getString(R.string.errorsurveynonotfound));
            isValid = false;
        } else {
            edtsurveyno.setError(null);
        }

        if (selvillage.size() == 0) {
            Constant.showToast(getResources().getString(R.string.errorvillagenotselect), activity, R.drawable.ic_wrong);
            isValid = false;
        }

        if (selhangam.size() == 0) {
            Constant.showToast(getResources().getString(R.string.errorhangamnotselect), activity, R.drawable.ic_wrong);
            isValid = false;
        }

        if (sv.checkNull(plantationdate)) {
            edtplantaiondate.setError(getResources().getString(R.string.errorwrongdate));
            isValid = false;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(plantationdate));
            } catch (ParseException e) {
                e.printStackTrace();
                edtplantaiondate.setError(getResources().getString(R.string.errorwrongdate));
                isValid = false;
            }
            if (isValid) {
                Long selectedDate = cal.getTimeInMillis();
                if (mindate <= selectedDate && maxdate >= selectedDate) {
                    edtplantaiondate.setError(null);
                } else {
                    edtplantaiondate.setError(getResources().getString(R.string.errordatenotinrange));
                    Constant.showToast(getResources().getString(R.string.errordatenotinrange), activity, R.drawable.ic_wrong);
                    isValid = false;
                }
            }
        }

        if (selvarity.size() == 0) {
            Constant.showToast(getResources().getString(R.string.errorvaritynotselect), activity, R.drawable.ic_wrong);
            isValid = false;
        }
        if (selirrigation.size() == 0) {
            Constant.showToast(getResources().getString(R.string.errorirrigationnotselect), activity, R.drawable.ic_wrong);
            isValid = false;
        }
        if (selharvesttype.size() == 0) {
            Constant.showToast(getResources().getString(R.string.errorharvesttypenotselect), activity, R.drawable.ic_wrong);
            isValid = false;
        }
        if (!sv.checkFloat(area)) {
            edtarea.setError(getResources().getString(R.string.errorareanotselect));
            isValid = false;
        } else {
            double darea = Double.parseDouble(area);
            if (darea > 25) {
                edtarea.setError(getResources().getString(R.string.errormaxarea));
                isValid = false;
            } else {
                int iarea = Integer.parseInt(area.replace(".", ""));
                if (iarea % 5 != 0) {
                    edtarea.setError(getResources().getString(R.string.errormultipleof005));
                    isValid = false;
                } else {
                    edtarea.setError(null);
                }
            }
        }
        if (!sv.checkFloat(distance)) {
            if (!sv.checkNull(distance)) {
                edtdistance.setError(getResources().getString(R.string.errordistancenotselect));
                isValid = false;
            }
        } else {
            double ddistance = Double.parseDouble(distance);
            if (ddistance > 100) {
                edtdistance.setError(getResources().getString(R.string.errormaxdistaqnce));
                isValid = false;
            } else {
                edtdistance.setError(null);
            }
        }
        if (sv.checkNull(photopath)) {
            Constant.showToast(getResources().getString(R.string.errorphotonottaken), activity, R.drawable.ic_wrong);
            isValid = false;
        } else {
            File root = getExternalFilesDir("");
            File myDir = new File(root + Constant.foldername);
            File imgFile = new File(myDir, photopath);
            if (!imgFile.exists()) {
                Constant.showToast(getResources().getString(R.string.errorphotonotfound), activity, R.drawable.ic_wrong);
                isValid = false;
            }
        }
        return isValid;
    }

    private boolean validateStep2() {
        Activity activity = LaganNondActivity.this;
        boolean isValid = true;

        SingleSpinnerSearch ssplanetype;
        ssplanetype = findViewById(R.id.ssplanetype);
        List<KeyPairBoolData> sellanetype = ssplanetype.getSelectedItems();

        if (sellanetype.size() == 0) {
            Constant.showToast(getResources().getString(R.string.errorlanetypenotselect), activity, R.drawable.ic_wrong);
            isValid = false;
        }

        return isValid;
    }

    private boolean validateStep3() {
        boolean isValid = true;
        Activity activity = LaganNondActivity.this;
        ServerValidation sv = new ServerValidation();
        AppCompatTextView txtgpsareatxt = findViewById(R.id.txtgpsareatxt);
        String gpsarea = txtgpsareatxt.getText().toString();
        if (!sv.checkFloat(gpsarea)) {
            Constant.showToast(getResources().getString(R.string.errorareanotcount), activity, R.drawable.ic_wrong);
            isValid = false;
        } else {
            double darea = Double.parseDouble(gpsarea);
            if (darea > 25) {
                Constant.showToast(getResources().getString(R.string.errormaxarea), activity, R.drawable.ic_wrong);
                isValid = false;
            }
        }
        return isValid;
    }

    private void saveOrUpdate() {
        //Read all value and set in bean
        AppCompatTextView txtfarmercodetxt, txtfarmertypetxtid, txtsectionid, txtphotopath, txtseasonval, txtgpsareatxt, txtnplotoffline;
        AppCompatEditText edtsurveyno, edtplantaiondate, edtarea, edtdistance;
        SingleSpinnerSearch sspvillage, ssphangam, sspvarity, sspirrigation, sspharvesttype, ssplanetype;
        SwitchCompat swsoiltest, swgreenfert, swbenetreat, swbesaldose, swdripused;

        txtseasonval = findViewById(R.id.txtseasonval);
        txtnplotoffline = findViewById(R.id.txtnplotoffline);
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtfarmertypetxtid = findViewById(R.id.txtfarmertypetxtid);
        sspvillage = findViewById(R.id.sspvillage);
        txtsectionid = findViewById(R.id.txtsectionid);
        edtsurveyno = findViewById(R.id.edtsurveyno);
        ssphangam = findViewById(R.id.ssphangam);
        edtplantaiondate = findViewById(R.id.edtplantaiondate);
        sspvarity = findViewById(R.id.sspvarity);
        sspirrigation = findViewById(R.id.sspirrigation);
        edtarea = findViewById(R.id.edtarea);
        sspharvesttype = findViewById(R.id.sspharvesttype);
        edtdistance = findViewById(R.id.edtdistance);
        txtphotopath = findViewById(R.id.txtphotopath);
        swsoiltest = findViewById(R.id.swsoiltest);
        swgreenfert = findViewById(R.id.swgreenfert);
        swbenetreat = findViewById(R.id.swbenetreat);
        swbesaldose = findViewById(R.id.swbesaldose);
        swdripused = findViewById(R.id.swdripused);
        ssplanetype = findViewById(R.id.ssplanetype);
        txtgpsareatxt = findViewById(R.id.txtgpsareatxt);

        String seasonval = txtseasonval.getText().toString();
        String farmercode = txtfarmercodetxt.getText().toString();
        String farmertype = txtfarmertypetxtid.getText().toString();
        String section = txtsectionid.getText().toString();
        String surveyno = edtsurveyno.getText().toString();
        String plantationdate = edtplantaiondate.getText().toString();
        String area = edtarea.getText().toString();
        String distance = edtdistance.getText().toString();
        String photopath = txtphotopath.getText().toString();
        String gpsarea = txtgpsareatxt.getText().toString();
        String nplotoffline = txtnplotoffline.getText().toString();

        List<KeyPairBoolData> selvillage = sspvillage.getSelectedItems();
        List<KeyPairBoolData> selhangam = ssphangam.getSelectedItems();
        List<KeyPairBoolData> selvarity = sspvarity.getSelectedItems();
        List<KeyPairBoolData> selirrigation = sspirrigation.getSelectedItems();
        List<KeyPairBoolData> selharvesttype = sspharvesttype.getSelectedItems();
        List<KeyPairBoolData> ssllanetype = ssplanetype.getSelectedItems();

        Long villageId = null, hangamId = null, varityId = null, irrigationId = null, harvesttypId = null, lanetype = null;
        if (selvillage.size() > 0) {
            KeyPairBoolData keyPairBoolData = selvillage.get(0);
            villageId = keyPairBoolData.getId();
        }
        if (selhangam.size() > 0) {
            KeyPairBoolData keyPairBoolData = selhangam.get(0);
            hangamId = keyPairBoolData.getId();
        }
        if (selvarity.size() > 0) {
            KeyPairBoolData keyPairBoolData = selvarity.get(0);
            varityId = keyPairBoolData.getId();
        }
        if (selirrigation.size() > 0) {
            KeyPairBoolData keyPairBoolData = selirrigation.get(0);
            irrigationId = keyPairBoolData.getId();
        }
        if (selharvesttype.size() > 0) {
            KeyPairBoolData keyPairBoolData = selharvesttype.get(0);
            harvesttypId = keyPairBoolData.getId();
        }
        if (ssllanetype.size() > 0) {
            KeyPairBoolData keyPairBoolData = ssllanetype.get(0);
            lanetype = keyPairBoolData.getId();
        }
        PlantationBean pbean = new PlantationBean();
        pbean.setNgpsFlag(locationType);
        if (locationType == 2) {
            AppCompatTextView txtlat, txtlong, txtacc;

            txtlat = findViewById(R.id.txtlat);
            txtlong = findViewById(R.id.txtlong);
            txtacc = findViewById(R.id.txtacc);

            String slat = MarathiHelper.convertMarathitoEnglish(txtlat.getText().toString());
            String slong = MarathiHelper.convertMarathitoEnglish(txtlong.getText().toString());
            DecimalFormat df = new DecimalFormat("#0.00");
            String sacc = MarathiHelper.convertMarathitoEnglish(txtacc.getText().toString());

            if (!sacc.equals("")) {
                double dacc = Double.parseDouble(sacc);
                if (dacc > 20) {
                    Constant.showToast(getResources().getString(R.string.waittillstandinglocationupdate), LaganNondActivity.this, R.drawable.ic_wrong);
                    return;
                }
                pbean.setNstandingacc(dacc);
            } else {
                Constant.showToast(getResources().getString(R.string.wearegettingyourlocation), LaganNondActivity.this, R.drawable.ic_wrong);
                return;
            }

            pbean.setVstandingLatitude(slat);
            pbean.setVstandinglongitude(slong);

        }

        String soiltest = swsoiltest.isChecked() ? "Y" : "N";
        String greenfert = swgreenfert.isChecked() ? "Y" : "N";
        String benetreat = swbenetreat.isChecked() ? "Y" : "N";
        String besaldose = swbesaldose.isChecked() ? "Y" : "N";
        String dripused = swdripused.isChecked() ? "Y" : "N";

        Activity activity = LaganNondActivity.this;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        pbean.setVyearId(seasonval);
        pbean.setNentityUniId(farmercode);
        pbean.setNplotNo(0);
        pbean.setNhangamId(Math.toIntExact(hangamId));
        pbean.setVsurveNo(surveyno);
        pbean.setDplantationDate(plantationdate);
        pbean.setDentryDate(MarathiHelper.convertMarathitoEnglish(sdf.format(new Date())));
        pbean.setNirrigationId(Math.toIntExact(irrigationId));
        pbean.setNvarietyId(Math.toIntExact(varityId));
        pbean.setNarea(Double.parseDouble(area));
        pbean.setNvillageId(Math.toIntExact(villageId));
        String airDistance = calAirToAirDist();
        if (airDistance == null) {
            Constant.showToast(getResources().getString(R.string.errorcurrentlocation), activity, R.drawable.ic_wrong);
            return;
        }
        pbean.setNdistance(!distance.trim().equals("") ? Double.parseDouble(distance) : null);
        pbean.setNsectionId(Integer.parseInt(section));
        pbean.setNfarmerTypeId(farmertype);
        pbean.setNgpsArea(Double.parseDouble(gpsarea));
        pbean.setNgpsDistance(Double.parseDouble(airDistance));
        pbean.setNconfirmFlag(0);
        pbean.setVphotoPath(photopath);
        pbean.setNcropwaterCondition(0);
        pbean.setNjuneFlag(0);
        pbean.setNaugustFlag(0);
        pbean.setVsoilTest(soiltest);
        pbean.setVgreenFert(greenfert);
        pbean.setNlaneTypeId(Math.toIntExact(lanetype));
        pbean.setVbeneTreat(benetreat);
        pbean.setVbesalDose(besaldose);
        pbean.setVdripUsed(dripused);
        pbean.setNharvestTypeId(Math.toIntExact(harvesttypId));
        pbean.setNplotNoOffline(nplotoffline);
        pbean.setNregFlagOffline(InternetConnection.isNetworkAvailableNormal(LaganNondActivity.this) ? 1 : 2);
        pbean.setNconfFlagOffline(0);
        pbean.setNentryType(1);

        List<PlantLocation> plantLocations = new ArrayList<>();
        int size = arrayPoints.size();
        for (int i = 0; i < size; i++) {
            double acc = accuracy.get(i);
            LatLng latLng = arrayPoints.get(i);
            double latitude = latLng.latitude;
            double longitude = latLng.longitude;
            PlantLocation plantLocation = new PlantLocation();
            plantLocation.setVyearId(seasonval);
            plantLocation.setNplotNoOffline(nplotoffline);
            plantLocation.setNaccuracy(acc);
            plantLocation.setVlatitude(latitude);
            plantLocation.setVlongitude(longitude);
            plantLocations.add(plantLocation);
        }
        pbean.setPlantLocations(plantLocations);
        DBHelper dbHelper = new DBHelper(activity);
        boolean saved = dbHelper.insertAgriPlantation(pbean);
        if (saved) {
            Intent intent = new Intent(LaganNondActivity.this, SuccessActivity.class);
            intent.putExtra("gpsarea", String.valueOf(pbean.getNgpsArea()));
            intent.putExtra("gpsdistance", String.valueOf(pbean.getNgpsDistance()));
            intent.putExtra("offlineplotno", pbean.getNplotNoOffline());
            startActivity(intent);
        } else {
            Constant.showToast(getResources().getString(R.string.errornotsaved), activity, R.drawable.ic_wrong);
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        AppCompatEditText date;
        if (datepicker == Datepicker.PLANTATIONDATE) {
            date = findViewById(R.id.edtplantaiondate);
        } else {
            return;
        }
        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
        date.setError(null);
        SingleSpinnerSearch sspvarity = findViewById(R.id.sspvarity);
        sspvarity.performClick();
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.mMap.setMyLocationEnabled(true);
        this.mMap.setTrafficEnabled(true);
        this.mMap.setIndoorEnabled(true);
        this.mMap.setBuildingsEnabled(true);
        this.mMap.getUiSettings().setZoomControlsEnabled(true);
        this.mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                if (locationType == 2) {
                    checkClick = true;
                    int pos = Integer.parseInt(marker.getSnippet());

                    if (pos != -1) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        arrayPoints.set(pos, marker.getPosition());
                        if (polygon == null) {
                            polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
                            polygon.setGeodesic(true);
                        }
                        polygon.setPoints(arrayPoints);
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_double_zoom));
                        marker.setRotation(180);
                        if (arrayPoints.size() >= 3) {
                            calculateArea(false);
                        }
                    }
                } else {
                    Constant.showToast(getResources().getString(R.string.errorwakingpointmovenotallowed), LaganNondActivity.this, R.drawable.ic_wrong);
                }

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (locationType == 2) {
                    checkClick = false;
                    int pos = Integer.parseInt(marker.getSnippet());
                    if (pos != -1) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        arrayPoints.set(pos, marker.getPosition());
                        if (polygon == null) {
                            polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
                            polygon.setGeodesic(true);
                        }
                        polygon.setPoints(arrayPoints);
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_zoom));
                        marker.setRotation(180);
                        if (arrayPoints.size() >= 3) {
                            calculateArea(false);
                        }
                    }
                } else {
                    Constant.showToast(getResources().getString(R.string.errorwakingpointmovenotallowed), LaganNondActivity.this, R.drawable.ic_wrong);
                }
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                if (locationType == 2) {
                    int pos = Integer.parseInt(marker.getSnippet());

                    if (pos != -1) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        arrayPoints.set(pos, marker.getPosition());
                        if (polygon == null) {
                            polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
                            polygon.setGeodesic(true);
                        }
                        polygon.setPoints(arrayPoints);
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_double_zoom));
                        marker.setRotation(180);
                        if (arrayPoints.size() >= 3) {
                            calculateArea(false);
                        }
                    }
                } else {
                    Constant.showToast(getResources().getString(R.string.errorwakingpointmovenotallowed), LaganNondActivity.this, R.drawable.ic_wrong);
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ImageConstant.REQUEST_CAPTURE_IMAGE) {
                ImageConstant img = new ImageConstant();
                String imagename = img.compressAndGetImage(requestCode, resultCode, LaganNondActivity.this, 200);
                AppCompatTextView txtphotopath = findViewById(R.id.txtphotopath);
                AppCompatImageView imgtakenphoto = findViewById(R.id.imgtakenphoto);
                txtphotopath.setText(imagename);
                File root = getExternalFilesDir("");
                File myDir = new File(root + Constant.foldername);
                File imgFile = new File(myDir, imagename);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imgtakenphoto.setImageBitmap(myBitmap);
                }
            } else if (requestCode == LocationUpdatetor.REQUEST_CHECK_SETTINGS) {
                locationUpdatetor.startLocationButtonClick();
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == LocationUpdatetor.REQUEST_CHECK_SETTINGS) {
                locationUpdatetor.stopLocationUpdates();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (step == 1)
            super.onBackPressed();
        else {
            AppCompatButton btnprev = findViewById(R.id.btnprev);
            btnprev.performClick();
        }
    }

    double prevAcc = 9999;

    @Override
    public void onLocationUpdate(double lats, double longs, double accuracy) {
        locationUpdatetor.updateUIData(LaganNondActivity.this, lats, longs, accuracy);
        if (step == 3) {
            if (prevAcc > 100)
                showMyLocation();
        }
        AppCompatButton btntakepoint = findViewById(R.id.btntakepoint);
        btntakepoint.setEnabled(accuracy <= 20);
    }

    private void startLocation(Activity activity) {
        locationUpdatetor = new LocationUpdatetor(activity);
        locationUpdatetor.locationCaller = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        locationUpdatetor.startLocationButtonClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationUpdatetor.stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationUpdatetor.stopLocationUpdates();
    }


    private String calAirToAirDist() {
        Location loc1 = new Location("");
        loc1.setLatitude(Constant.factLatitude);
        loc1.setLongitude(Constant.factLongitude);

        if (arrayPoints.size() > 0) {
            LatLng latLng = arrayPoints.get(0);

            Location loc2 = new Location("");
            loc2.setLatitude(latLng.latitude);
            loc2.setLongitude(latLng.longitude);

            DecimalFormat df = new DecimalFormat("#0.000");
            float distanceInMeters = loc1.distanceTo(loc2) / 1000;
            return MarathiHelper.convertMarathitoEnglish(df.format(distanceInMeters));
        }
        return null;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        int id = textView.getId();
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            if (id == R.id.edtsurveyno) {
                SingleSpinnerSearch ssphangam = findViewById(R.id.ssphangam);
                ssphangam.performClick();
            } else if (id == R.id.edtarea) {
                SingleSpinnerSearch sspharvesttype = findViewById(R.id.sspharvesttype);
                sspharvesttype.performClick();
            }
            return true;
        } else if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (id == R.id.edtdistance) {
                AppCompatImageView imgtakephoto = findViewById(R.id.imgtakephoto);
                imgtakephoto.performClick();

            }
            return true;
        }
        return false;
    }

    private boolean checkClick = false;

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (locationType != 1) {
            if (!checkClick) {
                mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_zoom)).draggable(true).rotation(180).snippet(String.valueOf(arrayPoints.size())));
                arrayPoints.add(latLng);
                accuracy.add(0.0);
                if (polygon == null) {
                    polygon = mMap.addPolygon(new PolygonOptions().clickable(true).strokeColor(Color.BLUE).strokeWidth(5).geodesic(true).addAll(arrayPoints).fillColor(getResources().getColor(R.color.mapcolor)));
                    polygon.setGeodesic(true);
                }
                polygon.setPoints(arrayPoints);
                if (arrayPoints.size() >= 3) {
                    calculateArea(false);
                }
            }
            locationType = 2;
        } else {
            Constant.showToast(getResources().getString(R.string.errorhybridarea), LaganNondActivity.this, R.drawable.ic_wrong);
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (locationType == 2) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_double_zoom));
            marker.setRotation(180);
        } else {
            Constant.showToast(getResources().getString(R.string.errorwakingpointmovenotallowed), LaganNondActivity.this, R.drawable.ic_wrong);
        }
        return false;
    }

}