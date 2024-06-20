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
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.BankBranchMaster;
import com.twd.chitboyapp.spsskl.pojo.BankBranchResponse;
import com.twd.chitboyapp.spsskl.pojo.BranchResponse;
import com.twd.chitboyapp.spsskl.pojo.EntityMasterDetail;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FarmerBankinfoActivity extends AppCompatActivity implements RetrofitResponse, SingleReturn {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_bankinfo);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        String farmercode = getIntent().getStringExtra("farmercode");
        setFarmer(farmercode);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


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

        AppCompatTextView txtoldbanknametxt = findViewById(R.id.txtoldbanknametxt);
        txtoldbanknametxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.showToast(txtoldbanknametxt.getText().toString(), FarmerBankinfoActivity.this, R.drawable.ic_info);
            }
        });
        photoConfig();
    }

    private void validateAndUpdate() {
        AppCompatTextView txtfarmercodetxt, txtphotopath;
        AppCompatEditText edtbankaccnotxt;
        SingleSpinnerSearch sspbranch = findViewById(R.id.sspbranch);
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        edtbankaccnotxt = findViewById(R.id.edtbankaccnotxt);
        txtphotopath = findViewById(R.id.txtphotopath);

        String nentityUniId = txtfarmercodetxt.getText().toString();
        String vbankAcNo = edtbankaccnotxt.getText().toString();
        String photopath = txtphotopath.getText().toString();
        List<Long> list = sspbranch.getSelectedIds();


        Activity activity = FarmerBankinfoActivity.this;

        ServerValidation sv = new ServerValidation();
        if (sv.checkNull(nentityUniId)) {
            Constant.showToast(getResources().getString(R.string.errornofarmerfound), activity, R.drawable.ic_wrong);
            return;
        }

        if (list.size() <= 0) {
            Constant.showToast(getResources().getString(R.string.errorbranchselect), activity, R.drawable.ic_wrong);
            return;
        }

        if (sv.checkNull(vbankAcNo)) {
            edtbankaccnotxt.setError(getResources().getString(R.string.errorwrongaccountnumber));
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
        String action = getResources().getString(R.string.actionfarmerupdatebank);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        String bankId = String.valueOf(list.get(0));
        try {
            job.put("nentityUniId", nentityUniId);
            job.put("vbankAcNo", vbankAcNo);
            job.put("nbankId", bankId);
            job.put("vpassbookPhoto", photopath);
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
        reqfarmer.handleRetrofit(call2, FarmerBankinfoActivity.this, RequestEnum.UPDATEFARMERBANK, servlet, activity, versioncode, photopath, myDir, nentityUniId, bankId, vbankAcNo);

    }


    private void photoConfig() {
        AppCompatImageView imgtakephoto = findViewById(R.id.imgtakephoto);
        imgtakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageConstant imageConstant = new ImageConstant();
                imageConstant.openCameraIntent(FarmerBankinfoActivity.this, "bank", true);
            }
        });
    }

    private void setFarmer(String farmerCodeF) {
        Activity activity = FarmerBankinfoActivity.this;
        DBHelper dbHelper = new DBHelper(FarmerBankinfoActivity.this);
        EntityMasterDetail entityMasterDetail = dbHelper.getEntityById(farmerCodeF);
        List<KeyPairBoolData> bankMaster = dbHelper.getBank();

        AppCompatTextView txtfarmercodetxt, txtfarmernametxt, txtoldbankaccnotxt;
        txtfarmercodetxt = findViewById(R.id.txtfarmercodetxt);
        txtfarmernametxt = findViewById(R.id.txtfarmernametxt);
        txtoldbankaccnotxt = findViewById(R.id.txtoldbankaccnotxt);

        txtfarmercodetxt.setText(farmerCodeF);
        txtfarmernametxt.setText(entityMasterDetail.getVentityNameLocal());
        txtoldbankaccnotxt.setText(entityMasterDetail.getVbankAcNo());

        SingleSpinnerSearch sspbank = findViewById(R.id.sspbank);
        SingleSpinnerSearch sspbranch = findViewById(R.id.sspbranch);

        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.initSingle(sspbank, activity, getResources().getString(R.string.select));
        ssh.initSingle(sspbranch, activity, getResources().getString(R.string.select));

        ssh.singleReturn = this;
        ssh.setSingleItems(sspbank, bankMaster, DataSetter.BANK);
        ssh.setSingleItems(sspbranch, new ArrayList<>(), DataSetter.BANKBRANCH);
        sspbranch.setEmptyTitle(getResources().getString(R.string.nobankselectedor));

        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionbranchdetails);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        try {
            job.put("branchCode", String.valueOf(entityMasterDetail.getNbankId()));
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }

        String servlet = getResources().getString(R.string.servletloaddata);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<BranchResponse> call2 = apiInterface.loadDataBranch(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<BranchResponse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, FarmerBankinfoActivity.this, RequestEnum.BRANCHDATA, servlet, activity, versioncode);

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
                String imagename = img.compressAndGetImage(requestCode, resultCode, FarmerBankinfoActivity.this, 200);
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

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        switch (requestCaller) {
            case BRANCHDATA:
                BranchResponse branchResponse = (BranchResponse) response.body();
                AppCompatTextView txtoldbanknametxt, txtoldbankifsctxt;
                txtoldbanknametxt = findViewById(R.id.txtoldbanknametxt);
                txtoldbankifsctxt = findViewById(R.id.txtoldbankifsctxt);
                txtoldbanknametxt.setText(branchResponse.getVbranchName());
                txtoldbankifsctxt.setText(branchResponse.getIfscCode());
                break;
            case BRANCHLIST:
                BankBranchResponse bankbranchList = (BankBranchResponse) response.body();
                SingleSpinnerSearch sspbranch = findViewById(R.id.sspbranch);
                List<KeyPairBoolData> branchListSsp = new ArrayList<>();
                List<BankBranchMaster> branchList = bankbranchList.getBankBranchList();
                for (BankBranchMaster bbm : branchList) {
                    KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                    keyPairBoolData.setId(bbm.getNbranchId());
                    keyPairBoolData.setName(bbm.getVbranchName());
                    keyPairBoolData.setObject(bbm);
                    branchListSsp.add(keyPairBoolData);
                }
                SingleSelectHandler ssh = new SingleSelectHandler();
                ssh.singleReturn = this;
                ssh.setSingleItems(sspbranch, branchListSsp, DataSetter.BANKBRANCH);
                break;
            case UPDATEFARMERBANK:
                ActionResponse actionResponse = (ActionResponse) response.body();
                if (actionResponse.isActionComplete()) {
                    Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                    DBHelper dbHelper = new DBHelper(activity);
                    dbHelper.updateFarmerBank((String) obj[2], (String) obj[3], (String) obj[4]);
                    File file = new File((File) obj[1], (String) obj[0]);
                    if (file.exists())
                        file.delete();
                    finish();
                } else {
                    Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
                }
                break;
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        Activity activity = FarmerBankinfoActivity.this;
        switch (dataSetter) {
            case BANK:
                Constant.showToast(selectedItem.getName(), activity, R.drawable.ic_info);
                JSONObject job = new JSONObject();
                String action = getResources().getString(R.string.actionbranchlist);
                ConstantFunction cf = new ConstantFunction();
                String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
                String[] value = new String[]{"", "0"};
                String[] data = cf.getSharedPrefValue(activity, key, value);
                try {
                    job.put("bankCode", String.valueOf(selectedItem.getId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                    return;
                }

                String servlet = getResources().getString(R.string.servletloaddata);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                Call<BankBranchResponse> call2 = apiInterface.loadDataBranchList(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
                RetrofitHandler<BankBranchResponse> reqfarmer = new RetrofitHandler<>();
                reqfarmer.handleRetrofit(call2, FarmerBankinfoActivity.this, RequestEnum.BRANCHLIST, servlet, activity, versioncode);
                break;
            case BANKBRANCH:
                Constant.showToast(selectedItem.getName(), activity, R.drawable.ic_info);
                BankBranchMaster bbm = (BankBranchMaster) selectedItem.getObject();
                AppCompatTextView txtbankifsctxt = findViewById(R.id.txtbankifsctxt);
                txtbankifsctxt.setText(bbm.getIfscCode());
                break;
        }
    }
}