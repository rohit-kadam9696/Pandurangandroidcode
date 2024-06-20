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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DataSender;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.ImageConstant;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PartOperation;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import retrofit2.Call;
import retrofit2.Response;

public class FarmerAdharcardActivity extends AppCompatActivity implements RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_adharcard);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        String farmercode = getIntent().getStringExtra("farmercode");
        setFarmer(farmercode);

        AppCompatButton btnprev = findViewById(R.id.btnprev);
        AppCompatButton btnupdate = findViewById(R.id.btnupdate);
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndUpdate();
            }
        });
        photoConfig(FarmerAdharcardActivity.this);
    }

    private void photoConfig(Activity activity) {
        AppCompatImageView imgtakephoto = findViewById(R.id.imgtakephoto);
        imgtakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageConstant imageConstant = new ImageConstant();
                imageConstant.openCameraIntent(activity, "aadhaar", true);
            }
        });
    }

    private void validateAndUpdate() {

        AppCompatTextView txtfarmercodetxt, txtphotopath;
        AppCompatEditText edtadhar;
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        edtadhar = findViewById(R.id.edtadhar);
        txtphotopath = findViewById(R.id.txtphotopath);

        String nentityUniId = txtfarmercodetxt.getText().toString();
        String vaadhaarNo = edtadhar.getText().toString();
        String photopath = txtphotopath.getText().toString();

        Activity activity = FarmerAdharcardActivity.this;

        ServerValidation sv = new ServerValidation();
        if (sv.checkNull(nentityUniId)) {
            Constant.showToast(getResources().getString(R.string.errornofarmerfound), activity, R.drawable.ic_wrong);
            return;
        }

        if (!sv.check(vaadhaarNo, "^[2-9]{1}[0-9]{11}$")) {
            edtadhar.setError(getResources().getString(R.string.errorwrongadharno));
            return;
        }
        File myDir;
        if (sv.checkNull(photopath)) {
            Constant.showToast(getResources().getString(R.string.errorphotonottaken), activity, R.drawable.ic_wrong);
            return;
        } else {
            File root = getExternalFilesDir("");
            myDir = new File(root + Constant.foldername);
            File imgFile = new File(myDir, photopath);
            if (!imgFile.exists()) {
                Constant.showToast(getResources().getString(R.string.errorphotonotfound), activity, R.drawable.ic_wrong);
                return;
            }
        }
        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionfarmerupdateadhar);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        try {
            job.put("nentityUniId", nentityUniId);
            job.put("vaadhaarNo", vaadhaarNo);
            job.put("vaadhaarPhoto", photopath);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }

        DataSender dataSender = new DataSender();
        PartOperation partOperation = new PartOperation();
        String servlet = getResources().getString(R.string.servletupdateFarmerInfo);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<ActionResponse> call2 = apiInterface.updateFarmerInfo(dataSender.getImagePart(activity, null, photopath), partOperation.createPartFromString(action), partOperation.createPartFromString(MarathiHelper.convertMarathitoEnglish(job.toString())), partOperation.createPartFromString(cf.getImei(activity)), partOperation.createPartFromString(data[0]), partOperation.createPartFromString(data[1]), partOperation.createPartFromString(versioncode));
        RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, FarmerAdharcardActivity.this, RequestEnum.UPDATEFARMERADHAR, servlet, activity, versioncode, photopath, myDir);
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.UPDATEFARMERADHAR) {
            ActionResponse actionResponse = (ActionResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                File file = new File((File) obj[1], (String) obj[0]);
                if (file.exists())
                    file.delete();
                finish();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }


    private void setFarmer(String farmerCodeF) {
        DBHelper dbHelper = new DBHelper(FarmerAdharcardActivity.this);
        EntityMasterDetail entityMasterDetail = dbHelper.getEntityById(farmerCodeF);

        AppCompatTextView txtfarmercodetxt, txtfarmernametxt;
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtfarmernametxt = findViewById(R.id.txtfarmernametxt);

        txtfarmercodetxt.setText(farmerCodeF);
        txtfarmernametxt.setText(entityMasterDetail.getVentityNameLocal());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ImageConstant.REQUEST_CAPTURE_IMAGE) {
                ImageConstant img = new ImageConstant();
                String imagename = img.compressAndGetImage(requestCode, resultCode, FarmerAdharcardActivity.this, 200);
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
            }
        }
    }
}