package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DataSender;
import com.twd.chitboyapp.spsskl.constant.DatePickerExample;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.ImageConstant;
import com.twd.chitboyapp.spsskl.constant.LocationUpdatetor;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PartOperation;
import com.twd.chitboyapp.spsskl.constant.PhotoViewer;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.ScannerHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.BackPress;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.Datepicker;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.filter.InputFilterMinMax;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.LocationUpdateListener;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.ActionResponse;
import com.twd.chitboyapp.spsskl.pojo.NumSlipListResponse;
import com.twd.chitboyapp.spsskl.pojo.Pump;
import com.twd.chitboyapp.spsskl.pojo.PumpCustDataReponse;
import com.twd.chitboyapp.spsskl.pojo.PumpRateCheckerResponse;
import com.twd.chitboyapp.spsskl.pojo.SavePrintResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

public class PumpSaleEntryActivity extends AppCompatActivity implements LocationUpdateListener, RetrofitResponse, DatePickerDialog.OnDateSetListener, SingleReturn, View.OnClickListener {

    Datepicker datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pump_sale_entry);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = this;
        if (Constant.locationUpdatetor != null) {
            Constant.locationUpdatetor.updateLocationUpdatetor(activity);
            Constant.locationUpdatetor.locationCaller = this;
        } else {
            startLocation(activity);
        }

        AppCompatButton btnscanslip, btntakephoto, btnviewphoto, btncancel, btnsubmit, btnsearchcode;

        btnscanslip = findViewById(R.id.btnscanslip);
        btntakephoto = findViewById(R.id.btntakephoto);
        btnviewphoto = findViewById(R.id.btnviewphoto);
        btncancel = findViewById(R.id.btncancel);
        btnsubmit = findViewById(R.id.btnsubmit);
        btnsearchcode = findViewById(R.id.btnsearchcode);

        btnscanslip.setOnClickListener(this);
        btntakephoto.setOnClickListener(this);
        btnviewphoto.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
        btnsearchcode.setOnClickListener(this);
        initData();
    }

    private void initData() {
        Activity activity = PumpSaleEntryActivity.this;
        ConstantFunction cf = new ConstantFunction();
        DBHelper dbHelper = new DBHelper(activity);
        String[] key = new String[]{getResources().getString(R.string.prefpumpid), getResources().getString(R.string.prefpumpunitid), getResources().getString(R.string.prefpumpunitname)};
        String[] value = new String[]{"", "", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        Calendar cal = Calendar.getInstance();
        AppCompatEditText edtbilldate = findViewById(R.id.edtbilldate);
        cf.initDate(edtbilldate);

        edtbilldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cf.hideKeyboard(view, getApplicationContext());
                datepicker = Datepicker.DATE;
                DatePickerExample dateFragment = new DatePickerExample();
                Bundle datedata = new Bundle();// create bundle instance
                datedata.putString(getResources().getString(R.string.datepara), edtbilldate.getText().toString());// put string to pass with a key value
                datedata.putLong(getResources().getString(R.string.maxdate), cal.getTimeInMillis());// put string to pass with a key value
                dateFragment.setArguments(datedata);// Set bundle data to fragment
                dateFragment.show(getSupportFragmentManager(), getResources().getString(R.string.datePicker));
            }
        });

        Pump pump = dbHelper.getPumpById(data[0]);
        List<KeyPairBoolData> customerTypes = dbHelper.getCustomerTypeList();
        List<KeyPairBoolData> salesTypes = dbHelper.getPumpSaleTypeList(1);
        List<KeyPairBoolData> shiftList = new ArrayList<>();
        cal.add(Calendar.HOUR_OF_DAY, -4);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        KeyPairBoolData boolData = new KeyPairBoolData();
        boolData.setId(1);
        boolData.setName(getResources().getString(R.string.shift4_12));
        boolData.setSelected(hours < 8);
        shiftList.add(boolData);

        boolData = new KeyPairBoolData();
        boolData.setId(2);
        boolData.setName(getResources().getString(R.string.shift12_8));
        boolData.setSelected(hours >= 8 && hours < 16);
        shiftList.add(boolData);

        boolData = new KeyPairBoolData();
        boolData.setId(3);
        boolData.setName(getResources().getString(R.string.shift8_4));
        boolData.setSelected(hours >= 16);
        shiftList.add(boolData);

        SingleSpinnerSearch sspshift = findViewById(R.id.sspshift);
        SingleSpinnerSearch sspsaletypes = findViewById(R.id.sspsaletypes);
        SingleSpinnerSearch sspcustomertype = findViewById(R.id.sspcustomertype);

        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspshift, activity, getResources().getString(R.string.selectshift));
        ssh.initSingle(sspsaletypes, activity, getResources().getString(R.string.salestype));
        ssh.initSingle(sspcustomertype, activity, getResources().getString(R.string.customertype));
        ssh.setSingleItems(sspshift, shiftList, DataSetter.SHIFT);
        ssh.setSingleItems(sspsaletypes, salesTypes, DataSetter.SALETYPE);
        ssh.setSingleItems(sspcustomertype, customerTypes, DataSetter.CUSTOMERTYPE);

        AppCompatEditText edtqty = findViewById(R.id.edtqty);

        InputFilterMinMax filter = new InputFilterMinMax(1, 500, 2);
        edtqty.setFilters(new InputFilterMinMax[]{filter});

        AppCompatTextView txtproductval = findViewById(R.id.txtproductval);
        AppCompatTextView txtproductid = findViewById(R.id.txtproductid);
        AppCompatTextView txtunitval = findViewById(R.id.txtunitval);
        AppCompatTextView txtunitid = findViewById(R.id.txtunitid);
        AppCompatTextView txtpumpname = findViewById(R.id.txtpumpname);
        AppCompatTextView txtpumpid = findViewById(R.id.txtpumpid);

        txtproductval.setText(pump.getVitemName());
        txtproductid.setText(String.valueOf(pump.getNitemId()));
        txtunitval.setText(data[2]);
        txtunitid.setText(data[1]);
        txtpumpname.setText(getResources().getString(R.string.pump) + " : " + pump.getVpumpName());
        txtpumpid.setText(data[0]);


        AppCompatTextView txtrateval = findViewById(R.id.txtrateval);
        AppCompatTextView txttotalval = findViewById(R.id.txttotalval);

        edtqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String srate = txtrateval.getText().toString();
                String sqty = edtqty.getText().toString();
                ServerValidation sv = new ServerValidation();
                if (sv.checkFloat(srate) && sv.checkFloat(sqty)) {
                    DecimalFormat df = new DecimalFormat("#0.00");
                    double rate = Double.parseDouble(srate);
                    double qty = Double.parseDouble(sqty);
                    double total = rate * qty;
                    txttotalval.setText(df.format(total));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loadRate();
    }

    private void loadRate() {
        AppCompatEditText edtbilldate = findViewById(R.id.edtbilldate);
        AppCompatTextView txtproductid = findViewById(R.id.txtproductid);

        String productId = txtproductid.getText().toString();
        String billdate = edtbilldate.getText().toString();


        ServerValidation sv = new ServerValidation();
        if (sv.checkNull(billdate)) {
            Constant.showToast(getResources().getString(R.string.errorselectdate), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
            return;
        }


        Activity activity = PumpSaleEntryActivity.this;


        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionproductrate);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        try {
            job.put("fromDate", billdate);
            job.put("toDate", billdate);
            job.put("itemId", productId);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }

        String servlet = getResources().getString(R.string.servletpump);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<PumpRateCheckerResponse> call2 = apiInterface.pumpRateCheckerResponse(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<PumpRateCheckerResponse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, PumpSaleEntryActivity.this, RequestEnum.ENTRYCHECK, servlet, activity, versioncode);
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
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        AppCompatEditText date;
        if (datepicker == Datepicker.DATE) {
            date = findViewById(R.id.edtbilldate);
        } else {
            return;
        }
        ConstantFunction cf = new ConstantFunction();
        cf.setDateToView(date, year, monthOfYear, dayOfMonth);
        loadRate();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnscanslip) {
            ScannerHandler scannerHandler = new ScannerHandler(PumpSaleEntryActivity.this, barcodeLauncher);
            scannerHandler.openScanner();
        } else if (id == R.id.btntakephoto) {
            ImageConstant imageConstant = new ImageConstant();
            imageConstant.openCameraIntent(PumpSaleEntryActivity.this, "pump", true);
        } else if (id == R.id.btnviewphoto) {
            AppCompatTextView txtphotopath = findViewById(R.id.txtphotopath);
            String imagename = txtphotopath.getText().toString();
            ServerValidation sv = new ServerValidation();
            if (sv.checkNull(imagename)) {
                Constant.showToast(getResources().getString(R.string.errorphotonottaken), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
            } else {
                PhotoViewer photoViewer = new PhotoViewer();
                photoViewer.openPhoto(imagename, PumpSaleEntryActivity.this, "1");
            }
        } else if (id == R.id.btncancel) {
            onBackPressed();
        } else if (id == R.id.btnsubmit) {
            validateAndSave();
        } else if(id == R.id.btnsearchcode) {
            AppCompatEditText edtcustomercode = findViewById(R.id.edtcustomercode);
            SingleSpinnerSearch sspcustomertype = findViewById(R.id.sspcustomertype);
            List<Long> customertypeList = sspcustomertype.getSelectedIds();
            if (!customertypeList.isEmpty()) {
                String customerCode = edtcustomercode.getText().toString();
                loadList(null, "C", customerCode, String.valueOf(customertypeList.get(0)));
            } else {
                Constant.showToast(getResources().getString(R.string.errorselectcustomertype), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
            }
        }
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Constant.showToast(getResources().getString(R.string.cancelscan), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
        } else {
            String qrText = result.getContents();
            loadList(qrText, "Q", null, null);
        }
    });

    private void loadList(String qrText, String type, String customerCode, String customerType) {
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(PumpSaleEntryActivity.this, key, value);

        Activity activity = PumpSaleEntryActivity.this;
        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionpumpcustdata);
        try {
            job.put("qrText", qrText);
            job.put("customerCode", customerCode);
            job.put("customerType", customerType);
            job.put("vyear_id", sharedPrefval[2]);
            String servlet = activity.getResources().getString(R.string.servletnumbersys);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            RetrofitHandler<PumpCustDataReponse> otphandler = new RetrofitHandler<>();
            Call<PumpCustDataReponse> call2 = apiInterface.pumpCustData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
            otphandler.handleRetrofit(call2, PumpSaleEntryActivity.this, RequestEnum.CUSTDATA, servlet, activity, versioncode, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void validateAndSave() {
        AppCompatTextView txtphotopath = findViewById(R.id.txtphotopath);
        AppCompatTextView txtpumpid = findViewById(R.id.txtpumpid);
        AppCompatEditText edtbilldate = findViewById(R.id.edtbilldate);
        SingleSpinnerSearch sspshift = findViewById(R.id.sspshift);
        SingleSpinnerSearch sspsaletypes = findViewById(R.id.sspsaletypes);
        SingleSpinnerSearch sspcustomertype = findViewById(R.id.sspcustomertype);
        AppCompatEditText edtcustomercode = findViewById(R.id.edtcustomercode);
        AppCompatTextView txtvehiclenoval = findViewById(R.id.txtvehiclenoval);
        AppCompatTextView txtproductid = findViewById(R.id.txtproductid);
        AppCompatTextView txtunitid = findViewById(R.id.txtunitid);
        AppCompatEditText edtqty = findViewById(R.id.edtqty);
        AppCompatEditText txtrateval = findViewById(R.id.txtrateval);
        AppCompatEditText txttotalval = findViewById(R.id.txttotalval);


        String photopath = txtphotopath.getText().toString();
        String pumpid = txtpumpid.getText().toString();
        String billdate = edtbilldate.getText().toString();
        List<Long> shiftList = sspshift.getSelectedIds();
        List<Long> saletypeList = sspsaletypes.getSelectedIds();
        List<Long> customertypeList = sspcustomertype.getSelectedIds();
        String shift;
        if (!shiftList.isEmpty()) {
            shift = shiftList.get(0).toString();
        } else {
            Constant.showToast(getResources().getString(R.string.errorselectshift), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
            return;
        }

        String saletype;
        if (!saletypeList.isEmpty()) {
            saletype = saletypeList.get(0).toString();
        } else {
            Constant.showToast(getResources().getString(R.string.errorselectsalestype), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
            return;
        }
        String customertype;
        if (!customertypeList.isEmpty()) {
            customertype = customertypeList.get(0).toString();
        } else {
            Constant.showToast(getResources().getString(R.string.errorselectcustomertype), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
            return;
        }

        String customercode = edtcustomercode.getText().toString();
        String vehiclenoval = txtvehiclenoval.getText().toString();
        String productid = txtproductid.getText().toString();
        String unitid = txtunitid.getText().toString();
        String qty = edtqty.getText().toString();
        String rate = txtrateval.getText().toString();
        String total = txttotalval.getText().toString();

        ServerValidation sv = new ServerValidation();
        if (!sv.checkNumber(pumpid)) {
            Constant.showToast(getResources().getString(R.string.errorpumpcodenotfound), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
            return;
        }

        if (!sv.checkDateddMMyyyy(billdate, "/")) {
            edtbilldate.setError(getResources().getString(R.string.errorselectdate));
            return;
        }

        if (!sv.checkNumber(customercode)) {
            edtcustomercode.setError(getResources().getString(R.string.errorcustomercodenotfound));
            return;
        }

        if (!sv.checkNumber(productid)) {
            Constant.showToast(getResources().getString(R.string.errorproductcodenotfound), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
            return;
        }

        if (!sv.checkNumber(unitid)) {
            Constant.showToast(getResources().getString(R.string.errorunitcodenotfound), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
            return;
        }

        if (!sv.checkNumber(qty)) {
            edtqty.setError(getResources().getString(R.string.errorenterqty));
            return;
        }

        if (!sv.checkNumber(rate)) {
            Constant.showToast(getResources().getString(R.string.errorratenotavaiablesalesnotallowed), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
            return;
        }

        if (!sv.checkNumber(total)) {
            Constant.showToast(getResources().getString(R.string.errorinvalidtotal), PumpSaleEntryActivity.this, R.drawable.ic_wrong);
            return;
        }


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
        String action = getResources().getString(R.string.actionsave);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.chitboypreflatitude), getResources().getString(R.string.chitboypreflogitude), getResources().getString(R.string.chitboypreflastdatetime), getResources().getString(R.string.chitboyprefaccuracy)};
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

            //long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            if (minutes < 240) {
                try {
                    job.put("vlatitude", data[2]);
                    job.put("vlongitude", data[3]);
                    job.put("vaccuracy", data[5]);
                    job.put("vphoto", photopath);
                    job.put("pumpid", pumpid);
                    job.put("billdate", billdate);
                    job.put("shift", shift);
                    job.put("saletype", saletype);
                    job.put("customertype", customertype);
                    job.put("customercode", customercode);
                    job.put("vehicleno", vehiclenoval);
                    job.put("itemId", productid);
                    job.put("unit", unitid);
                    job.put("qty", qty);
                    job.put("rate", rate);
                    job.put("total", total);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                    return;
                }
                DataSender dataSender = new DataSender();
                MultipartBody.Part fileToUpload = dataSender.getImagePart(activity, myDir, photopath);
                PartOperation partOperation = new PartOperation();
                String servlet = getResources().getString(R.string.servletpumpsave);
                APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
                String versioncode = cf.getVersionCode();
                Call<ActionResponse> call2 = apiInterface.savePumpTrans(fileToUpload, partOperation.createPartFromString(action), partOperation.createPartFromString(MarathiHelper.convertMarathitoEnglish(job.toString())), partOperation.createPartFromString(cf.getImei(activity)), partOperation.createPartFromString(data[0]), partOperation.createPartFromString(data[1]), partOperation.createPartFromString(cf.getVersionCode()));
                RetrofitHandler<ActionResponse> reqfarmer = new RetrofitHandler<>();
                reqfarmer.handleRetrofit(call2, PumpSaleEntryActivity.this, RequestEnum.SAVE, servlet, activity, versioncode);
            } else {
                Constant.showToast(getResources().getString(R.string.errorfromlast4hrnolocation), activity, R.drawable.ic_wrong);
            }
        } else {
            Constant.showToast(getResources().getString(R.string.errorcurrentlocationnotin500), activity, R.drawable.ic_wrong);
        }
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.ENTRYCHECK) {
            PumpRateCheckerResponse pumpRateCheckerResponse = (PumpRateCheckerResponse) response.body();
            if (pumpRateCheckerResponse.isActionComplete()) {
                AppCompatTextView txtrateval = findViewById(R.id.txtrateval);
                txtrateval.setText(pumpRateCheckerResponse.getPumpRate());
            } else {
                Constant.showToast(getResources().getString(R.string.errorratenotavaiablesalesnotallowed), activity, R.drawable.ic_wrong);
                AppCompatTextView txtrateval = findViewById(R.id.txtrateval);
                txtrateval.setText("");
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
                Intent intent = new Intent(PumpSaleEntryActivity.this, SugarReceiptReprintActivity.class);
                intent.putExtra("html", actionResponse.getHtmlContent());
                intent.putExtra("backpress", BackPress.NUMPRINT);
                startActivity(intent);
                clearAll();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }

        } else if (requestCaller == RequestEnum.CUSTDATA) {
            PumpCustDataReponse pumpCustDataReponse = (PumpCustDataReponse) response.body();
            AppCompatEditText edtcustomercode = findViewById(R.id.edtcustomercode);
            AppCompatTextView txtvehiclenoval = findViewById(R.id.txtvehiclenoval);
            AppCompatTextView txtcustomername = findViewById(R.id.txtcustomername);
            AppCompatTextView txtcustomercodeval = findViewById(R.id.txtcustomercodeval);
            SingleSpinnerSearch sspcustomertype = findViewById(R.id.sspcustomertype);

            String caller = (String) obj[0];
            if (caller.equals("Q")) {
                edtcustomercode.setText(pumpCustDataReponse.getCustomerCode());
            }
            txtvehiclenoval.setText(pumpCustDataReponse.getVvehicleNo());
            txtcustomername.setText(pumpCustDataReponse.getCustomerName());
            txtcustomercodeval.setText(pumpCustDataReponse.getCustomerCode());

            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.selectById(sspcustomertype, pumpCustDataReponse.getCustomerTypeId(), DataSetter.CUSTOMERTYPE);

        }
    }

    private void clearAll() {
        AppCompatTextView txtphotopath = findViewById(R.id.txtphotopath);
        AppCompatEditText edtcustomercode = findViewById(R.id.edtcustomercode);
        AppCompatTextView txtvehiclenoval = findViewById(R.id.txtvehiclenoval);
        AppCompatTextView txtcustomername = findViewById(R.id.txtcustomername);
        AppCompatEditText edtqty = findViewById(R.id.edtqty);
        AppCompatEditText txttotalval = findViewById(R.id.txttotalval);

        txtphotopath.setText("");
        edtcustomercode.setText("");
        txtvehiclenoval.setText("");
        txtcustomername.setText(getResources().getString(R.string.name));
        edtqty.setText("");
        txttotalval.setText("");
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ImageConstant.REQUEST_CAPTURE_IMAGE) {
                ImageConstant img = new ImageConstant();
                String imagename = img.compressAndGetImage(requestCode, resultCode, PumpSaleEntryActivity.this, 200);
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
        Constant.locationUpdatetor.updateUIData(PumpSaleEntryActivity.this, lats, longs, accuracy);
        if (accuracy < 500) {
            ConstantFunction cf = new ConstantFunction();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String[] key = new String[]{getResources().getString(R.string.chitboypreflatitude), getResources().getString(R.string.chitboypreflogitude), getResources().getString(R.string.chitboypreflastdatetime), getResources().getString(R.string.chitboyprefaccuracy)};
            String[] value = new String[]{"" + lats, "" + longs, "" + sdf.format(new Date()), "" + accuracy};
            cf.putSharedPrefValue(key, value, PumpSaleEntryActivity.this);
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

}