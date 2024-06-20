package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.twd.chitboyapp.spsskl.adapter.NumSlipAdapter;
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
import com.twd.chitboyapp.spsskl.constant.PhotoViewer;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.ScannerHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.BackPress;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.LocationUpdateListener;
import com.twd.chitboyapp.spsskl.interfaces.RefreshComplete;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneYard;
import com.twd.chitboyapp.spsskl.pojo.NumSlip;
import com.twd.chitboyapp.spsskl.pojo.NumSlipListResponse;
import com.twd.chitboyapp.spsskl.pojo.SavePrintResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

public class NumNumberActivity extends AppCompatActivity implements LocationUpdateListener, RetrofitResponse, View.OnClickListener, RefreshComplete {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_number);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        AppCompatButton btnscanslip, btnentercode, btntakephoto, btnviewphoto, btncancel, btnsubmit;

        btnscanslip = findViewById(R.id.btnscanslip);
        btnentercode = findViewById(R.id.btnentercode);
        btntakephoto = findViewById(R.id.btntakephoto);
        btnviewphoto = findViewById(R.id.btnviewphoto);
        btncancel = findViewById(R.id.btncancel);
        btnsubmit = findViewById(R.id.btnsubmit);

        btnscanslip.setOnClickListener(this);
        btnentercode.setOnClickListener(this);
        btntakephoto.setOnClickListener(this);
        btnviewphoto.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);

        Activity activity = this;

        if (Constant.locationUpdatetor != null) {
            Constant.locationUpdatetor.updateLocationUpdatetor(activity);
            Constant.locationUpdatetor.locationCaller = this;
        } else {
            startLocation(activity);
        }

        onRefreshComplete();
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
        return cf.openCommon(this, item, this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, this);
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.LIST) {
            NumSlipListResponse numSlipListResponse = (NumSlipListResponse) response.body();
            NumSlipAdapter npFarmerListAdapter = new NumSlipAdapter(numSlipListResponse.getNumSlips());
            RecyclerView rcvsliplist = findViewById(R.id.rcvsliplist);
            GridLayoutManager mLayoutManager1 = new GridLayoutManager(activity, 1);
            rcvsliplist.setLayoutManager(mLayoutManager1);
            rcvsliplist.setItemAnimator(new DefaultItemAnimator());
            rcvsliplist.setAdapter(npFarmerListAdapter);
            if (obj[0] != null) {
                Dialog dialog = (Dialog) obj[0];
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        } else if (requestCaller == RequestEnum.SAVE) {
            SavePrintResponse actionResponse = (SavePrintResponse) response.body();
            if (actionResponse.isActionComplete()) {
                AppCompatTextView txtphotopath = findViewById(R.id.txtphotopath);
                String photopath = txtphotopath.getText().toString();
                File root = getExternalFilesDir("");
                File myDir = new File(root + Constant.foldername);
                File imgFile = new File(myDir, photopath);
                if (imgFile.exists()) {
                    imgFile.delete();
                }
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                Intent intent = new Intent(NumNumberActivity.this, SugarReceiptReprintActivity.class);
                intent.putExtra("html", actionResponse.getHtmlContent());
                intent.putExtra("backpress", BackPress.NUMPRINT);
                startActivity(intent);
                clearAll();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }

        }
    }

    private void clearAll() {
        NumSlipAdapter npFarmerListAdapter = new NumSlipAdapter(new ArrayList<>());
        RecyclerView rcvsliplist = findViewById(R.id.rcvsliplist);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(NumNumberActivity.this, 1);
        rcvsliplist.setLayoutManager(mLayoutManager1);
        rcvsliplist.setItemAnimator(new DefaultItemAnimator());
        rcvsliplist.setAdapter(npFarmerListAdapter);

        AppCompatTextView txtphotopath = findViewById(R.id.txtphotopath);
        txtphotopath.setText("");

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnscanslip) {
            ScannerHandler scannerHandler = new ScannerHandler(NumNumberActivity.this, barcodeLauncher);
            scannerHandler.openScanner();
        } else if (id == R.id.btnentercode) {
            openDailog();
        } else if (id == R.id.btntakephoto) {
            ImageConstant imageConstant = new ImageConstant();
            imageConstant.openCameraIntent(NumNumberActivity.this, "num", true);
        } else if (id == R.id.btnviewphoto) {
            AppCompatTextView txtphotopath = findViewById(R.id.txtphotopath);
            String imagename = txtphotopath.getText().toString();
            ServerValidation sv = new ServerValidation();
            if (sv.checkNull(imagename)) {
                Constant.showToast(getResources().getString(R.string.errorphotonottaken), NumNumberActivity.this, R.drawable.ic_wrong);
            } else {
                PhotoViewer photoViewer = new PhotoViewer();
                photoViewer.openPhoto(imagename, NumNumberActivity.this, "1");
            }
        } else if (id == R.id.btncancel) {
            onBackPressed();
        } else if (id == R.id.btnsubmit) {
            validateAndSave();
        }
    }

    private void validateAndSave() {
        RecyclerView rcvsliplist = findViewById(R.id.rcvsliplist);
        NumSlipAdapter numSlipAdapter = (NumSlipAdapter) rcvsliplist.getAdapter();
        List<NumSlip> numSlipList = numSlipAdapter.getItems();
        if (numSlipList != null && !numSlipList.isEmpty()) {
            AppCompatTextView txtphotopath = findViewById(R.id.txtphotopath);
            String photopath = txtphotopath.getText().toString();
            ServerValidation sv = new ServerValidation();
            Activity activity = this;
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
            String action = getResources().getString(R.string.actionsavenumber);
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.chitboypreflatitude), getResources().getString(R.string.chitboypreflogitude), getResources().getString(R.string.chitboypreflastdatetime), getResources().getString(R.string.chitboyprefaccuracy), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", "", "", "", "", ""};
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

                Set<String> set = new HashSet<>();
                int size = numSlipList.size();
                for (int i = 0; i < size; i++) {
                    NumSlip numSlip = numSlipList.get(i);
                    if (i == 0) {
                        try {
                            job.put("nvillage_id", numSlip.getVillageid());
                            job.put("nentity_uni_id", numSlip.getTranscode());
                            job.put("nvehicle_type", numSlip.getVehicletype());
                            job.put("nvehicle_group_id", numSlip.getVehiclegroupid());
                            job.put("vvehicle_no", numSlip.getVehicleno());
                            job.put("vvillage_name", numSlip.getVillageName());
                            job.put("vtrans_name", numSlip.getTransporter());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    set.add(numSlip.getSlipno());
                }

                //long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                if (minutes < 240) {
                    try {
                        job.put("vlatitude", data[2]);
                        job.put("vlongitude", data[3]);
                        job.put("vaccuracy", data[5]);
                        job.put("vyear_id", data[6]);
                        job.put("nslip_no", String.join(",", set));
                        job.put("vphoto", photopath);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                        return;
                    } DataSender dataSender = new DataSender();
                    MultipartBody.Part fileToUpload = dataSender.getImagePart(activity, myDir, photopath);
                    PartOperation partOperation = new PartOperation();
                    String servlet = getResources().getString(R.string.servletnumbersyssave);
                    APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                    String versioncode = cf.getVersionCode();
                    Call<SavePrintResponse> call2 = apiInterface.savePrintNumber(fileToUpload, partOperation.createPartFromString(action), partOperation.createPartFromString(MarathiHelper.convertMarathitoEnglish(job.toString())), partOperation.createPartFromString(cf.getImei(activity)), partOperation.createPartFromString(data[0]), partOperation.createPartFromString(data[1]), partOperation.createPartFromString(cf.getVersionCode()));
                    RetrofitHandler<SavePrintResponse> reqfarmer = new RetrofitHandler<>();
                    reqfarmer.handleRetrofit(call2, NumNumberActivity.this, RequestEnum.SAVE, servlet, activity, versioncode);
                } else {
                    Constant.showToast(getResources().getString(R.string.errorfromlast4hrnolocation), activity, R.drawable.ic_wrong);
                }
            } else {
                Constant.showToast(getResources().getString(R.string.errorcurrentlocationnotin500), activity, R.drawable.ic_wrong);
            }

        } else {
            Constant.showToast(getResources().getString(R.string.errorslipnotfound), NumNumberActivity.this, R.drawable.ic_wrong);
            return;
        }
    }

    private void openDailog() {
        Activity activity = this;
        DBHelper dbHelper = new DBHelper(activity);

        ConstantFunction cf = new ConstantFunction();
        String key[] = new String[]{getResources().getString(R.string.prefyardid)};
        String value[] = new String[]{"0"};
        String data[] = cf.getSharedPrefValue(activity, key, value);

        CaneYard caneYard = dbHelper.getCaneYardById(data[0]);


        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.popup_num_code_entry);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);

        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        AppCompatEditText edtcode = dialog.findViewById(R.id.edtcode);
        RadioGroup rgvehicletype = dialog.findViewById(R.id.rgvehicletype);
        RadioButton rbbulluckcart = dialog.findViewById(R.id.rbbulluckcart);
        RadioButton rbtranporter = dialog.findViewById(R.id.rbtranporter);
        if(caneYard.getVbajat().equals("Y") && caneYard.getVtruckTracktor().equals("Y")) {
            rbbulluckcart.setClickable(true);
            rbtranporter.setClickable(true);
        } else if(caneYard.getVbajat().equals("Y")) {
            rbbulluckcart.setClickable(false);
            rbtranporter.setClickable(false);
            rbbulluckcart.setChecked(true);
        } else if(caneYard.getVtruckTracktor().equals("Y")) {
            rbbulluckcart.setClickable(false);
            rbtranporter.setClickable(false);
            rbtranporter.setChecked(true);
        }

        AppCompatButton btncancel = dialog.findViewById(R.id.btncancel);
        AppCompatButton btnconfirm = dialog.findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edtcode.getText().toString();
                int rb = rgvehicletype.getCheckedRadioButtonId();
                String vtype = "";
                if (rb == R.id.rbtranporter) {
                    vtype = "T";
                } else if (rb == R.id.rbbulluckcart) {
                    vtype = "B";
                } else {
                    Constant.showToast(getResources().getString(R.string.errorselectvehicletype), activity, R.drawable.ic_wrong);
                    return;
                }

                ServerValidation sv = new ServerValidation();
                if (!sv.checkNumber(code)) {
                    edtcode.setError(getResources().getString(R.string.errorentercode));
                    return;
                }
                loadList(vtype + code, "C", vtype, dialog);
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Constant.showToast(getResources().getString(R.string.cancelscan), NumNumberActivity.this, R.drawable.ic_wrong);
        } else {
            String qrText = result.getContents();
            loadList(qrText, "Q", null, null);
        }
    });

    private void loadList(String code, String type, String vtype, Dialog dialog) {
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(NumNumberActivity.this, key, value);

        Activity activity = NumNumberActivity.this;
        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionslipdatafornumbertaker);
        try {
            job.put("code", code);
            job.put("type", type);
            job.put("vtype", vtype);
            job.put("vyear_id", sharedPrefval[2]);
            String servlet = activity.getResources().getString(R.string.servletnumbersys);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            RetrofitHandler<NumSlipListResponse> otphandler = new RetrofitHandler<>();
            Call<NumSlipListResponse> call2 = apiInterface.numSlipData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
            otphandler.handleRetrofit(call2, NumNumberActivity.this, RequestEnum.LIST, servlet, activity, versioncode, dialog);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ImageConstant.REQUEST_CAPTURE_IMAGE) {
                ImageConstant img = new ImageConstant();
                String imagename = img.compressAndGetImage(requestCode, resultCode, NumNumberActivity.this, 200);
                AppCompatTextView txtphotopath = findViewById(R.id.txtphotopath);
                txtphotopath.setText(imagename);
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
        Constant.locationUpdatetor.updateUIData(NumNumberActivity.this, lats, longs, accuracy);
        if (accuracy < 500) {
            ConstantFunction cf = new ConstantFunction();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String[] key = new String[]{getResources().getString(R.string.chitboypreflatitude), getResources().getString(R.string.chitboypreflogitude), getResources().getString(R.string.chitboypreflastdatetime), getResources().getString(R.string.chitboyprefaccuracy)};
            String[] value = new String[]{"" + lats, "" + longs, "" + sdf.format(new Date()), "" + accuracy};
            cf.putSharedPrefValue(key, value, NumNumberActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(NumNumberActivity.this, HomeActivity.class);
        startActivity(intent1);
    }

    @Override
    public void onRefreshComplete() {

        Activity activity = this;

        ConstantFunction cf = new ConstantFunction();
        String key[] = new String[]{getResources().getString(R.string.prefyardid)};
        String value[] = new String[]{"0"};
        String data[] = cf.getSharedPrefValue(activity, key, value);

        DBHelper dbHelper = new DBHelper(activity);
        CaneYard caneYard = dbHelper.getCaneYardById(data[0]);
        AppCompatTextView txtyardname = findViewById(R.id.txtyardname);
        txtyardname.setText(String.format(getResources().getString(R.string.yarddetails), caneYard.getNyardId(), caneYard.getVyardName()));
    }
}