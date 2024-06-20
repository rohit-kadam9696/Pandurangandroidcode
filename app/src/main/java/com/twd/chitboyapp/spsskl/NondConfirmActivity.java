package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.ImageConstant;
import com.twd.chitboyapp.spsskl.constant.LocationUpdatetor;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.WarningEnum;
import com.twd.chitboyapp.spsskl.filter.InputFilterMinMax;
import com.twd.chitboyapp.spsskl.interfaces.LocationUpdateListener;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.CaneConfirmationRegistrationList;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.pojo.HangamMaster;
import com.twd.chitboyapp.spsskl.pojo.PlantationBean;
import com.twd.chitboyapp.spsskl.pojo.VarietyMaster;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NondConfirmActivity extends AppCompatActivity implements SingleReturn, LocationUpdateListener/*, RetrofitResponse*/ {

    LocationUpdatetor locationUpdatetor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nond_confirm);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        Activity activity = this;
        ConstantFunction cf = new ConstantFunction();
        cf.generateOfflinePlotNo(activity);
        long ncropwatercondirion = setData(activity);
        initSingleSpinner(ncropwatercondirion);
        AppCompatImageView imgtakephoto = findViewById(R.id.imgtakephoto);
        imgtakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoConfig(activity);
            }
        });

        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        AppCompatButton btnprev = findViewById(R.id.btnprev);
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndSave();
            }
        });
        startLocation(activity);
    }

    private void validateAndSave() {
        AppCompatEditText edtexptonnagetxt, edtacutalareatxt, edtsurveyno;
        AppCompatTextView txtareatxt, txtphotopath, txtrujutype, txtplotnotxt, txtyearcodetxt, txtnplotoffline;
        AppCompatTextView txtlat, txtlong, txtacc;
        SingleSpinnerSearch sspwatercond;

        txtnplotoffline = findViewById(R.id.txtnplotoffline);
        txtareatxt = findViewById(R.id.txtareatxt);
        sspwatercond = findViewById(R.id.sspwatercond);
        edtexptonnagetxt = findViewById(R.id.edtexptonnagetxt);
        edtacutalareatxt = findViewById(R.id.edtacutalareatxt);
        edtsurveyno = findViewById(R.id.edtsurveyno);
        txtphotopath = findViewById(R.id.txtphotopath);
        txtrujutype = findViewById(R.id.txtrujutype);
        txtplotnotxt = findViewById(R.id.txtplotnotxt);
        txtyearcodetxt = findViewById(R.id.txtyearcodetxt);
        txtlat = findViewById(R.id.txtlat);
        txtlong = findViewById(R.id.txtlong);
        txtacc = findViewById(R.id.txtacc);

        List<KeyPairBoolData> watercond = sspwatercond.getSelectedItems();
        String expTonnage = edtexptonnagetxt.getText().toString();
        String calArea = edtacutalareatxt.getText().toString();
        String survyNo = edtsurveyno.getText().toString();
        String formArea = txtareatxt.getText().toString();
        String photopath = txtphotopath.getText().toString();
        String rujutype = txtrujutype.getText().toString();
        String plotnotxt = txtplotnotxt.getText().toString();
        String yearcode = txtyearcodetxt.getText().toString();
        String nplotoffline = txtnplotoffline.getText().toString();
        String lats = MarathiHelper.convertMarathitoEnglish(txtlat.getText().toString());
        String longs = MarathiHelper.convertMarathitoEnglish(txtlong.getText().toString());
        String acc = MarathiHelper.convertMarathitoEnglish(txtacc.getText().toString());

        Activity activity = NondConfirmActivity.this;

        boolean isValid = true;
        ServerValidation sv = new ServerValidation();
        if (!sv.checkFloat(acc)) {
            Constant.showToast(getResources().getString(R.string.errorcurrentlocation), activity, R.drawable.ic_wrong);
            isValid = false;
        } else {
            double dacc = Double.parseDouble(acc);
            if (dacc > 100) {
                Constant.showToast(getResources().getString(R.string.errorlocationshouldbe50mtr), activity, R.drawable.ic_wrong);
                isValid = false;
            }
        }

        if (sv.checkNull(survyNo)) {
            edtsurveyno.setError(getResources().getString(R.string.errorsurveynonotfound));
            isValid = false;
        }

        if (watercond.isEmpty()) {
            Constant.showToast(getResources().getString(R.string.errorselectwatercond), activity, R.drawable.ic_wrong);
            isValid = false;
        }

        if (!sv.checkFloat(calArea)) {
            edtacutalareatxt.setError(getResources().getString(R.string.errorareanotselect));
            isValid = false;
        } else {
            double uarea = Double.parseDouble(calArea);
            double farea = Double.parseDouble(formArea);
            if (uarea > farea || uarea < 0) {
                edtacutalareatxt.setError(String.format(getResources().getString(R.string.areabetween0toplantarea), formArea));
                isValid = false;
            } else {
                edtacutalareatxt.setError(null);
            }
        }

        if (isValid) {
            if (!sv.checkFloat(expTonnage)) {
                edtexptonnagetxt.setError(getResources().getString(R.string.errorenterexptonnage));
                isValid = false;
            } else {
                double uarea = Double.parseDouble(calArea);
                double dexpTonnage = Double.parseDouble(expTonnage);
                double maxTon = uarea * 250;
                if (dexpTonnage > maxTon) {
                    edtexptonnagetxt.setError(String.format(getResources().getString(R.string.exptonnagebetween0tocaltonnage), String.valueOf(maxTon)));
                    isValid = false;
                } else {
                    edtexptonnagetxt.setError(null);
                }
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

        if (isValid) {
            PlantationBean plantationBean = new PlantationBean();
            plantationBean.setNexpectedYield(Double.parseDouble(expTonnage));
            plantationBean.setNconfirmFlag(Integer.parseInt(rujutype));
            plantationBean.setVconfirmphotoPath(photopath);
            plantationBean.setNtentativeArea(Double.parseDouble(calArea));
            plantationBean.setNcropwaterCondition(Math.toIntExact(watercond.get(0).getId()));
            plantationBean.setNplotNoOffline(nplotoffline);
            plantationBean.setVstandingLatitude(lats);
            plantationBean.setVstandinglongitude(longs);
            plantationBean.setNgpsDistance(Double.parseDouble(calAirToAirDist(Double.parseDouble(lats), Double.parseDouble(longs))));
            plantationBean.setVsurveNo(survyNo);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            plantationBean.setDentryDate(MarathiHelper.convertMarathitoEnglish(sdf.format(new Date())));
            if (rujutype.equals("1")) {
                plantationBean.setNjuneFlag(1);
            } else {
                plantationBean.setNaugustFlag(1);
            }
            plantationBean.setNconfFlagOffline(1);
            plantationBean.setNplotNo(Integer.parseInt(plotnotxt));
            plantationBean.setVyearId(yearcode);

            DBHelper dbHelper = new DBHelper(activity);
            boolean rujuup = dbHelper.updateRujuwat(plantationBean);
            if (rujuup) {
                ConstantFunction cf = new ConstantFunction();
                cf.showCustomAlert(activity, R.mipmap.ic_action_accept, getResources().getString(R.string.successfullysaved));
                finish();
            }
            /*MultipartBody.Part fileToUpload = getImageInMultipart(plantationBean.getVconfirmphotoPath(), activity);
            ConstantFunction cf = new ConstantFunction();
            PartOperation partOperation = new PartOperation();

            String action = activity.getResources().getString(R.string.actionconfirmlagan);
            //String msg = activity.getResources().getString(R.string.senddatatoserver);

            String[] key = new String[]{activity.getResources().getString(R.string.chitboyprefuniquestring), activity.getResources().getString(R.string.chitboyprefchit_boy_id)};
            String[] value = new String[]{"", "0"};
            String[] data = cf.getSharedPrefValue(activity, key, value);
            String servlet = activity.getResources().getString(R.string.servletsavePlantation);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Gson gson = new Gson();

            Call<CanePlantationResponse> call2 = apiInterface.appSendLaganNond(fileToUpload, partOperation.createPartFromString(action), partOperation.createPartFromString(MarathiHelper.convertMarathitoEnglish(gson.toJson(plantationBean))), partOperation.createPartFromString(cf.getImei(activity)), partOperation.createPartFromString(data[0]), partOperation.createPartFromString(data[1]), partOperation.createPartFromString(cf.getVersionCode()));
            RetrofitHandler<CanePlantationResponse> datasendcaller = new RetrofitHandler<>();
            datasendcaller.handleRetrofit(call2, NondConfirmActivity.this, RequestEnum.SENDDATA, servlet, activity, versioncode);*/
        }
    }

    private String calAirToAirDist(double lat, double lng) {
        Location loc1 = new Location("");
        loc1.setLatitude(Constant.factLatitude);
        loc1.setLongitude(Constant.factLongitude);

        Location loc2 = new Location("");
        loc2.setLatitude(lat);
        loc2.setLongitude(lng);

        DecimalFormat df = new DecimalFormat("#0.000");
        float distanceInMeters = loc1.distanceTo(loc2) / 1000;
        return MarathiHelper.convertMarathitoEnglish(df.format(distanceInMeters));
    }

    private MultipartBody.Part getImageInMultipart(String vconfirmphotoPath, Activity activity) {
        MultipartBody.Part fileToUpload = null;
        File root = activity.getExternalFilesDir("");
        File myDir = new File(root + Constant.foldername);
        try {
            File imgFile = new File(myDir, vconfirmphotoPath);
            if (imgFile.exists()) {
                RequestBody requestBody = RequestBody.create(imgFile, MediaType.parse("*/*"));
                fileToUpload = MultipartBody.Part.createFormData("file", imgFile.getName(), requestBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileToUpload;
    }

    private long setData(Activity activity) {
        Intent intent = getIntent();
        CaneConfirmationRegistrationList confirmationData = (CaneConfirmationRegistrationList) intent.getSerializableExtra("confirmationData");
        EntityMasterDetail entityMasterDetail = (EntityMasterDetail) intent.getSerializableExtra("entityMasterDetail");
        String yearCode = intent.getStringExtra("yearCode");
        String rujutype = intent.getStringExtra("rujutype");

        DBHelper dbHelper = new DBHelper(activity);
        /*VillageMaster villageMaster = dbHelper.getVillageById(confirmationData.getVilleageCode());*/
        HangamMaster hangamMaster = dbHelper.getHangamById(confirmationData.getNhungamCode());
        VarietyMaster varietyMaster = dbHelper.getVarietyById(confirmationData.getNcaneVarity());

        AppCompatTextView txtyearcodetxt, txtrujutype, txtfarmertxt, txtfarmercodetxt, txtplotnotxt, txthangamtxt,
                txtareatxt, txtvaritytxt, txtplantationdatetxt;
        AppCompatEditText edtexptonnagetxt, edtacutalareatxt, edtsurveyno;
        SingleSpinnerSearch sspwatercond;

        sspwatercond = findViewById(R.id.sspwatercond);

        txtyearcodetxt = findViewById(R.id.txtyearcodetxt);
        txtyearcodetxt = findViewById(R.id.txtyearcodetxt);
        txtrujutype = findViewById(R.id.txtrujutype);
        txtfarmertxt = findViewById(R.id.txtfarmertxt);
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtplotnotxt = findViewById(R.id.txtplotnotxt);
        txthangamtxt = findViewById(R.id.txthangamtxt);
        txtareatxt = findViewById(R.id.txtareatxt);
        edtsurveyno = findViewById(R.id.edtsurveyno);
        txtvaritytxt = findViewById(R.id.txtvaritytxt);
        txtplantationdatetxt = findViewById(R.id.txtplantationdatetxt);

        edtexptonnagetxt = findViewById(R.id.edtexptonnagetxt);
        edtacutalareatxt = findViewById(R.id.edtacutalareatxt);

        txtyearcodetxt.setText(yearCode);
        txtrujutype.setText(rujutype);
        txtfarmertxt.setText(entityMasterDetail.getVentityNameLocal());
        txtfarmercodetxt.setText(entityMasterDetail.getNentityUniId());
        txtplotnotxt.setText(confirmationData.getPlotNo());
        txthangamtxt.setText(hangamMaster.getVhangamName());
        txtareatxt.setText(confirmationData.getNarea());
        edtsurveyno.setText(confirmationData.getServeNo());
        txtvaritytxt.setText(varietyMaster.getVvariety_name());
        txtplantationdatetxt.setText(confirmationData.getDplantationDate());
        edtexptonnagetxt.setText(confirmationData.getNexpectedYield());
        edtacutalareatxt.setText(confirmationData.getNtentativeArea());

        edtacutalareatxt.requestFocus();

        InputFilterMinMax inputFilterarea = new InputFilterMinMax(0, Double.parseDouble(confirmationData.getNarea()), 2);
        edtacutalareatxt.setFilters(new InputFilter[]{inputFilterarea});

        final int[] prevlen = {0};
        edtacutalareatxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String area = edtacutalareatxt.getText().toString();
                if (area.length() == 1) {
                    if (area.charAt(0) == '.') {
                        edtacutalareatxt.setText("0.");
                        edtacutalareatxt.setSelection(edtacutalareatxt.getText().toString().length());
                    } else if (prevlen[0] < 1) {
                        edtacutalareatxt.setText(edtacutalareatxt.getText().toString() + ".");
                        edtacutalareatxt.setSelection(edtacutalareatxt.getText().toString().length());
                    }
                }
                prevlen[0] = edtacutalareatxt.length();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtacutalareatxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String area = edtacutalareatxt.getText().toString();
                    ServerValidation sv = new ServerValidation();
                    if (area.endsWith(".")) {
                        area = area.replace(".", "");
                    }
                    if (sv.checkFloat(area)) {
                        double darea = Double.parseDouble(area);
                        if (darea > 25) {
                            edtacutalareatxt.setError(getResources().getString(R.string.errormaxarea));
                            edtacutalareatxt.setText("");
                        } else if (darea > 10) {
                            openWarning(WarningEnum.AREA, edtacutalareatxt);
                        } else {
                            DecimalFormat df = new DecimalFormat("#0.00");
                            edtacutalareatxt.setText(MarathiHelper.convertMarathitoEnglish(df.format(darea)));
                            InputFilterMinMax inputFilterarea = new InputFilterMinMax(0, darea * 250, 3);
                            edtexptonnagetxt.setFilters(new InputFilter[]{inputFilterarea});
                        }
                    }
                }
            }
        });

        edtacutalareatxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edtexptonnagetxt.requestFocus();
                }
                return false;
            }
        });

        edtexptonnagetxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    sspwatercond.performClick();
                }
                return false;
            }
        });
        return confirmationData.getNcropwaterCondition();
    }

    private void openWarning(WarningEnum warningEnum, AppCompatEditText edt) {

        final Dialog dialog = new Dialog(NondConfirmActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_delete);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatTextView txt_deletehead = dialog.findViewById(R.id.txt_deletehead);
        AppCompatTextView message = dialog.findViewById(R.id.message);

        txt_deletehead.setText(getResources().getString(R.string.warninghead));

        if (warningEnum == WarningEnum.AREA) {
            message.setText(getResources().getString(R.string.areabeyond10));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ImageConstant.REQUEST_CAPTURE_IMAGE) {
                ImageConstant img = new ImageConstant();
                String imagename = img.compressAndGetImage(requestCode, resultCode, NondConfirmActivity.this, 200);
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

    private void initSingleSpinner(long ncropwatercondirion) {
        Activity activity = NondConfirmActivity.this;

        SingleSpinnerSearch sspwatercond = findViewById(R.id.sspwatercond);

        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspwatercond, activity, getResources().getString(R.string.selectwatercondition));

        DBHelper dbHelper = new DBHelper(activity);
        List<KeyPairBoolData> waterCondList = dbHelper.getCropWater(ncropwatercondirion);

        ssh.setSingleItems(sspwatercond, waterCondList, DataSetter.WATERCOND);
    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.WATERCOND) {
            AppCompatImageView imgtakephoto = findViewById(R.id.imgtakephoto);
            imgtakephoto.performClick();
        }
    }

    /*@Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        ConstantFunction cf = new ConstantFunction();
        switch (requestCaller) {
            case SENDDATA:
                CanePlantationResponse canePlantationResponse = (CanePlantationResponse) response.body();
                cf.showCustomAlert(activity, R.mipmap.ic_action_accept, canePlantationResponse.getSuccessMessage());
                finish();
                break;
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }*/

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
    public void onLocationUpdate(double lats, double longs, double accuracy) {
        locationUpdatetor.updateUIData(NondConfirmActivity.this, lats, longs, accuracy);
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

}