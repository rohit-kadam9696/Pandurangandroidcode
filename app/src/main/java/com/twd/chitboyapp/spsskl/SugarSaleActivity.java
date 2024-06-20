package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DataSender;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.ImageConstant;
import com.twd.chitboyapp.spsskl.constant.LocationUpdatetor;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PartOperation;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.BackPress;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.LocationUpdateListener;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.FarmerSugarResponse;
import com.twd.chitboyapp.spsskl.pojo.SugarSaleSavePrintResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

public class SugarSaleActivity extends AppCompatActivity implements LocationUpdateListener, View.OnClickListener, RetrofitResponse {

    String entryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_sale);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = this;

        FarmerSugarResponse farmerSugarResponse = null;
        String employeeName = null;
        Intent intent = getIntent();
        if (intent.hasExtra("farmerSugarResponse")) {
            farmerSugarResponse = (FarmerSugarResponse) intent.getSerializableExtra("farmerSugarResponse");
        } else {
            Constant.showToast(getResources().getString(R.string.errorfarmerinfonotfound), activity, R.drawable.ic_wrong);
            finish();
        }
        if (intent.hasExtra("employeeName")) {
            employeeName = intent.getStringExtra("employeeName");
        } else {
            ConstantFunction cf = new ConstantFunction();
            String key[] = new String[]{getResources().getString(R.string.chitboyprefname)};
            String value[] = new String[]{""};
            String data[] = cf.getSharedPrefValue(activity, key, value);
            employeeName = data[0];
        }
        if (intent.hasExtra("entryType")) {
            entryType = intent.getStringExtra("entryType");
        } else {
            Constant.showToast(getResources().getString(R.string.errorentrytypenotfound), activity, R.drawable.ic_wrong);
            finish();
        }
        if (Constant.locationUpdatetor != null) {
            Constant.locationUpdatetor.updateLocationUpdatetor(activity);
            Constant.locationUpdatetor.locationCaller = this;
        } else {
            startLocation(activity);
        }
        farmerSugarResponse.setVfullName(employeeName);
        setData(farmerSugarResponse);

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);

        btnprev.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
        photoConfig(activity);

    }

    private void setData(FarmerSugarResponse farmerSugarResponse) {
        AppCompatTextView txtsugarseasonval = findViewById(R.id.txtsugarseasonval);
        AppCompatTextView txtsugarfortxt = findViewById(R.id.txtsugarfortxt);
        AppCompatTextView txteventid = findViewById(R.id.txteventid);
        AppCompatTextView txtemployeetxt = findViewById(R.id.txtemployeetxt);
        AppCompatTextView txtcodeval = findViewById(R.id.txtcodeval);
        AppCompatTextView txtnameval = findViewById(R.id.txtnameval);
        AppCompatTextView txtvillageval = findViewById(R.id.txtvillageval);
        AppCompatTextView txtsugarkgval = findViewById(R.id.txtsugarkgval);
        AppCompatTextView txtrateval = findViewById(R.id.txtrateval);
        AppCompatTextView txtamountval = findViewById(R.id.txtamountval);

        txtsugarseasonval.setText(farmerSugarResponse.getSugarYear());
        txtsugarfortxt.setText(farmerSugarResponse.getEventName());
        txteventid.setText(farmerSugarResponse.getEventId());
        txtemployeetxt.setText(farmerSugarResponse.getVfullName());
        txtcodeval.setText(farmerSugarResponse.getNentityUniId());
        txtnameval.setText(farmerSugarResponse.getVentityUniName());
        txtvillageval.setText(farmerSugarResponse.getVvillname());
        txtsugarkgval.setText(String.valueOf(farmerSugarResponse.getSugarInKg()));
        txtrateval.setText(String.valueOf(farmerSugarResponse.getRate()));
        txtamountval.setText(String.valueOf(farmerSugarResponse.getAmount()));
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ImageConstant.REQUEST_CAPTURE_IMAGE) {
                ImageConstant img = new ImageConstant();
                String imagename = img.compressAndGetImage(requestCode, resultCode, SugarSaleActivity.this, 200);
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
            } else if (requestCode == Constant.locationUpdatetor.REQUEST_CHECK_SETTINGS) {
                Constant.locationUpdatetor.startLocationButtonClick();
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == Constant.locationUpdatetor.REQUEST_CHECK_SETTINGS) {
                Constant.locationUpdatetor.stopLocationUpdates();
            }
        }
    }

    @Override
    public void onLocationUpdate(double lats, double longs, double accuracy) {
        Constant.locationUpdatetor.updateUIData(SugarSaleActivity.this, lats, longs, accuracy);
        if (accuracy < 200) {
            ConstantFunction cf = new ConstantFunction();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String[] key = new String[]{getResources().getString(R.string.chitboypreflatitude), getResources().getString(R.string.chitboypreflogitude), getResources().getString(R.string.chitboypreflastdatetime)};
            String[] value = new String[]{"" + lats, "" + longs, "" + sdf.format(new Date())};
            cf.putSharedPrefValue(key, value, SugarSaleActivity.this);
        }
    }

    private void startLocation(Activity activity) {
        Constant.locationUpdatetor = new LocationUpdatetor(activity);
        Constant.locationUpdatetor.locationCaller = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        Constant.locationUpdatetor.startLocationButtonClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Constant.locationUpdatetor.stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Constant.locationUpdatetor.stopLocationUpdates();
    }


    private void photoConfig(Activity activity) {
        AppCompatImageView imgtakephoto = findViewById(R.id.imgtakephoto);
        imgtakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatTextView txtsugarseasonval = findViewById(R.id.txtsugarseasonval);
                AppCompatTextView txteventid = findViewById(R.id.txteventid);
                AppCompatTextView txtcodeval = findViewById(R.id.txtcodeval);

                String sugarSeason = txtsugarseasonval.getText().toString();
                String eventId = txteventid.getText().toString();
                String nentityUniId = txtcodeval.getText().toString();
                String filename = sugarSeason.replace("-", "") + "_" + eventId + "_" + nentityUniId;
                ImageConstant imageConstant = new ImageConstant();
                imageConstant.openCameraIntent(activity, filename, false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnprev) {
            onBackPressed();
        } else if (view.getId() == R.id.btnsubmit) {
            // validate and save
            Activity activity = this;
            AppCompatTextView txtlat = findViewById(R.id.txtlat);
            AppCompatTextView txtlong = findViewById(R.id.txtlong);
            AppCompatTextView txtacc = findViewById(R.id.txtacc);

            AppCompatTextView txtsugarseasonval = findViewById(R.id.txtsugarseasonval);
            AppCompatTextView txteventid = findViewById(R.id.txteventid);
            AppCompatTextView txtcodeval = findViewById(R.id.txtcodeval);
            AppCompatTextView txtphotopath = findViewById(R.id.txtphotopath);

            String latitude = txtlat.getText().toString();
            String logitude = txtlong.getText().toString();
            String accuracy = txtacc.getText().toString();

            String sugarSeason = txtsugarseasonval.getText().toString();
            String eventId = txteventid.getText().toString();
            String nentityUniId = txtcodeval.getText().toString();
            String photopath = txtphotopath.getText().toString();


            ServerValidation sv = new ServerValidation();
            if (!sv.checkFloat(accuracy)) {
                Constant.showToast(getResources().getString(R.string.errorcurrentlocation), activity, R.drawable.ic_wrong);
                return;
            }

            /*double acc = Double.parseDouble(accuracy);
            if (acc > 500) {
                Constant.showToast(getResources().getString(R.string.errorcurrentlocationnotin500), activity, R.drawable.ic_wrong);
                return;
            }*/

            if (!sv.checkFloat(latitude)) {
                Constant.showToast(getResources().getString(R.string.errorcurrentlocation), activity, R.drawable.ic_wrong);
                return;
            }

            if (!sv.checkFloat(logitude)) {
                Constant.showToast(getResources().getString(R.string.errorcurrentlocation), activity, R.drawable.ic_wrong);
                return;
            }

            if (sv.checkNull(sugarSeason)) {
                Constant.showToast(getResources().getString(R.string.errorsugaryearnotgetting), activity, R.drawable.ic_wrong);
                return;
            }

            if (sv.checkNull(eventId)) {
                Constant.showToast(getResources().getString(R.string.errorsugarfor), activity, R.drawable.ic_wrong);
                return;
            }
            if (sv.checkNull(nentityUniId)) {
                Constant.showToast(getResources().getString(R.string.errorfarmerinfonotfound), activity, R.drawable.ic_wrong);
                return;
            }
            File myDir;
            File imgFile;
            if (sv.checkNull(photopath)) {
                Constant.showToast(getResources().getString(R.string.errorphotonottaken), activity, R.drawable.ic_wrong);
                return;
            } else {
                File root = getExternalFilesDir("");
                myDir = new File(root + Constant.foldername);
                imgFile = new File(myDir, photopath);
                if (!imgFile.exists()) {
                    Constant.showToast(getResources().getString(R.string.errorphotonotfound), activity, R.drawable.ic_wrong);
                    return;
                }
            }

            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actionupdatesugar);
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.chitboypreflatitude), getResources().getString(R.string.chitboypreflogitude), getResources().getString(R.string.chitboypreflastdatetime)};
            String[] value = new String[]{"", "0", "", "", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);
            if (!data[4].equals("")) {
                Calendar cal = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    cal2.setTime(sdf.parse(data[4]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long diff = cal.getTimeInMillis() - cal2.getTimeInMillis();//as given

                //long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                if (minutes < 180) {
                    try {
                        job.put("latitude", data[2]);
                        job.put("logitude", data[3]);
                        job.put("sugarSeason", sugarSeason);
                        job.put("eventId", eventId);
                        job.put("nentityUniId", nentityUniId);
                        job.put("entryType", entryType);
                        job.put("photopath", photopath);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                        return;
                    }
                    DataSender dataSender = new DataSender();
                    MultipartBody.Part fileToUpload = dataSender.getImagePart(activity, myDir, photopath);
                    PartOperation partOperation = new PartOperation();
                    String servlet = getResources().getString(R.string.servletsavesugarsale);
                    APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                    String versioncode = cf.getVersionCode();
                    Call<SugarSaleSavePrintResponse> call2 = apiInterface.savePrintSugarSale(fileToUpload, partOperation.createPartFromString(action), partOperation.createPartFromString(MarathiHelper.convertMarathitoEnglish(job.toString())), partOperation.createPartFromString(cf.getImei(activity)), partOperation.createPartFromString(data[0]), partOperation.createPartFromString(data[1]), partOperation.createPartFromString(cf.getVersionCode()));
                    RetrofitHandler<SugarSaleSavePrintResponse> reqfarmer = new RetrofitHandler<>();
                    reqfarmer.handleRetrofit(call2, SugarSaleActivity.this, RequestEnum.UPDATESUGAR, servlet, activity, versioncode);
                } else {
                    Constant.showToast(getResources().getString(R.string.errorfromlast3hrnolocation), activity, R.drawable.ic_wrong);
                }
            } else {
                Constant.showToast(getResources().getString(R.string.errorcurrentlocationnotin200), activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.UPDATESUGAR) {
            SugarSaleSavePrintResponse actionResponse = (SugarSaleSavePrintResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                AppCompatTextView txtphotopath = findViewById(R.id.txtphotopath);
                String photopath = txtphotopath.getText().toString();
                File root = getExternalFilesDir("");
                File myDir = new File(root + Constant.foldername);
                File imgFile = new File(myDir, photopath);
                if (imgFile.exists()) {
                    imgFile.delete();
                }
                Intent intent = new Intent(SugarSaleActivity.this, SugarReceiptReprintActivity.class);
                intent.putExtra("html", actionResponse.getHtmlContent());
                intent.putExtra("backpress", BackPress.PRINT);
                startActivity(intent);

            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }
    }

    private void getSugarFile() {

    }


    @Override

    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}