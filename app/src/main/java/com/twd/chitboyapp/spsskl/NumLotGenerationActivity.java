package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.DynamicTableData;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PDFOperations;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.constant.SingleSelectHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.BackPress;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.CaneYard;
import com.twd.chitboyapp.spsskl.pojo.LotGenResponse;
import com.twd.chitboyapp.spsskl.pojo.PDFTablePOJO;
import com.twd.chitboyapp.spsskl.pojo.TableReportBean;
import com.twd.chitboyapp.spsskl.pojo.TableResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerSearch;
import com.twd.pdfcreator.views.PDFBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NumLotGenerationActivity extends AppCompatActivity implements RetrofitResponse, SingleReturn, View.OnClickListener {

    String head = null;
    String printhead = null;
    String htmlContent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_lot_generation);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = this;
        SingleSpinnerSearch sspyard = findViewById(R.id.sspyard);
        SingleSpinnerSearch sspvehiclegroup = findViewById(R.id.sspvehiclegroup);

        DBHelper dbHelper = new DBHelper(NumLotGenerationActivity.this);
        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;
        ssh.initSingle(sspyard, activity, getResources().getString(R.string.selectyard));
        ssh.setSingleItems(sspyard, dbHelper.getCaneYardList(), DataSetter.YARD);

        ssh.initSingle(sspvehiclegroup, activity, getResources().getString(R.string.selectvehiclegroup));
        ArrayList<KeyPairBoolData> dummyList = new ArrayList<>(1);
        ssh.setSingleItems(sspvehiclegroup, dummyList, DataSetter.VEHICALGROUP);

        AppCompatButton btnback = findViewById(R.id.btnback);
        AppCompatButton btnsubmit = findViewById(R.id.btnsubmit);
        AppCompatButton btnexclude = findViewById(R.id.btnexclude);
        AppCompatButton btnprint = findViewById(R.id.btnprint);
        AppCompatButton btnclear = findViewById(R.id.btnclear);
        AppCompatButton btnexportpdf = findViewById(R.id.btnexportpdf);

        btnback.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
        btnexclude.setOnClickListener(this);
        btnprint.setOnClickListener(this);
        btnclear.setOnClickListener(this);
        btnexportpdf.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnupdf) {
            exportPdf();
            return true;
        } else {
            MenuHandler cf = new MenuHandler();
            return cf.openCommon(this, item, null);
        }
    }

    private void exportPdf() {
        if (head == null) {
            Constant.showToast(getResources().getString(R.string.errorgeneratelotfirst), NumLotGenerationActivity.this, R.drawable.ic_wrong);
            return;
        }
        PDFOperations pdf = new PDFOperations();
        //String header = pdf.getHederFromView(RawanaReportActivity.this, null);

        ConstantFunction cf = new ConstantFunction();
        String data[] = cf.getSharedPrefValue(NumLotGenerationActivity.this, new String[]{getResources().getString(R.string.prefprintersetting), getResources().getString(R.string.prefseason)}, new String[]{String.format(getResources().getString(R.string.defaultJson), getResources().getString(R.string.myfactory)), ""});

        PDFTablePOJO pdfTablePOJO = new PDFTablePOJO();
        pdfTablePOJO.setHeadText(head);
        pdfTablePOJO.setTbl(findViewById(R.id.tbldata));

        PDFBody body = pdf.getBodyFromView(NumLotGenerationActivity.this, false, data[0], pdfTablePOJO);

        PdfCreatorExampleActivity.pdfBody = body;
        Intent intent = new Intent(NumLotGenerationActivity.this, PdfCreatorExampleActivity.class);
        intent.putExtra("json", data[0]);
        /*intent.putExtra("body", body);*/
        startActivity(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler cf = new MenuHandler();
        MenuItem mnupdf = menu.findItem(R.id.mnupdf);
        mnupdf.setVisible(true);
        return cf.performRefreshOption(menu, this);
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.LIST) {
            TableResponse tableRes = (TableResponse) response.body();
            setData(tableRes, activity);
        } else if (requestCaller == RequestEnum.SAVE) {
            LotGenResponse lotGenResponse = (LotGenResponse) response.body();
            if (lotGenResponse.isActionComplete()) {
                setData(lotGenResponse, activity);
                head = String.format(lotGenResponse.getVhead(), (String) obj[0], (String) obj[1]);
                Constant.showToast(lotGenResponse.getSuccessMsg() != null ? lotGenResponse.getSuccessMsg() : getResources().getString(R.string.savesucess), activity, R.drawable.ic_info);
                printhead = lotGenResponse.getVprintHead();
                htmlContent = lotGenResponse.getHtmlContent();
                AppCompatButton btnexclude = findViewById(R.id.btnexclude);
                AppCompatButton btnprint = findViewById(R.id.btnprint);
                AppCompatButton btnexportpdf = findViewById(R.id.btnexportpdf);
                AppCompatTextView txthead = findViewById(R.id.txthead);

                ConstraintLayout clmain = findViewById(R.id.clmain);
                ConstraintSet set = new ConstraintSet();
                set.constrainPercentWidth(R.id.btnclear, 0.22f);
                set.applyTo(clmain);

                txthead.setText(head);
                txthead.setVisibility(View.VISIBLE);
                btnexclude.setVisibility(View.GONE);
                btnprint.setVisibility(View.VISIBLE);
                btnexportpdf.setVisibility(View.VISIBLE);
            } else {
                Constant.showToast(lotGenResponse.getFailMsg() != null ? lotGenResponse.getFailMsg() : getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }
    }

    private void setData(TableResponse tableRes, Activity activity) {
        TableLayout tbldata = findViewById(R.id.tbldata);
        TableReportBean tableData = tableRes.getTableData();
        DynamicTableData dtd = new DynamicTableData();
        dtd.addTable(activity, tableData.getTableData(), tbldata, tableData.getRowColSpan(), tableData.getNoofHeads() != null ? tableData.getNoofHeads() : 1, tableData.isFooter(), tableData.getBoldIndicator(), false, tableData.getFloatings(), tableData.isMarathi(), tableData.getRowSpan(), tableData.getTextAlign(), tableData.getVisibility(), false, false, null, false);
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.YARD) {
            CaneYard caneYard = (CaneYard) selectedItem.getObject();
            String groupId = "1,2";
            if (caneYard.getVtruckTracktor().equals("Y") && caneYard.getVbajat().equals("Y")) {
                groupId = "1,2";
            } else if (caneYard.getVtruckTracktor().equals("Y")) {
                groupId = "1";
            } else if (caneYard.getVbajat().equals("Y")) {
                groupId = "2";
            }
            DBHelper dbHelper = new DBHelper(NumLotGenerationActivity.this);
            List<KeyPairBoolData> data = dbHelper.getVehicleGroupList(groupId);
            SingleSpinnerSearch sspvehiclegroup = findViewById(R.id.sspvehiclegroup);
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.singleReturn = this;
            ssh.setSingleItems(sspvehiclegroup, data, DataSetter.VEHICALGROUP);

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnback) {
            onBackPressed();
        } else if (id == R.id.btnsubmit) {
            SingleSpinnerSearch sspyard = findViewById(R.id.sspyard);
            List<Long> selYard = sspyard.getSelectedIds();
            if (selYard.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectyard), NumLotGenerationActivity.this, R.drawable.ic_wrong);
                return;
            }

            SingleSpinnerSearch sspvehiclegroup = findViewById(R.id.sspvehiclegroup);
            List<Long> selVehicleGroup = sspvehiclegroup.getSelectedIds();
            if (selVehicleGroup.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectvehiclegroup), NumLotGenerationActivity.this, R.drawable.ic_wrong);
                return;
            }

            long yardId = selYard.get(0);
            long vehicleGroupId = selVehicleGroup.get(0);
            Activity activity = NumLotGenerationActivity.this;

            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actiongeneratelotlist);
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            try {
                job.put("yearId", data[2]);
                job.put("yardId", yardId);
                job.put("vehicleGroupId", vehicleGroupId);
            } catch (JSONException e) {
                e.printStackTrace();
                Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                return;
            }

            String servlet = getResources().getString(R.string.servletnumbersys);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<TableResponse> call2 = apiInterface.tableNumberSys(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<TableResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, NumLotGenerationActivity.this, RequestEnum.LIST, servlet, activity, versioncode);
        } else if (id == R.id.btnexclude) {
            if (head != null) {
                Constant.showToast(getResources().getString(R.string.errorlotalreadygenerate), NumLotGenerationActivity.this, R.drawable.ic_wrong);
                return;
            }

            SingleSpinnerSearch sspyard = findViewById(R.id.sspyard);
            List<KeyPairBoolData> selYard = sspyard.getSelectedItems();
            if (selYard.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectyard), NumLotGenerationActivity.this, R.drawable.ic_wrong);
                return;
            }

            SingleSpinnerSearch sspvehiclegroup = findViewById(R.id.sspvehiclegroup);
            List<KeyPairBoolData> selVehicleGroup = sspvehiclegroup.getSelectedItems();
            if (selVehicleGroup.isEmpty()) {
                Constant.showToast(getResources().getString(R.string.errorselectvehiclegroup), NumLotGenerationActivity.this, R.drawable.ic_wrong);
                return;
            }

            KeyPairBoolData yarddata = selYard.get(0);
            KeyPairBoolData vgdata = selVehicleGroup.get(0);
            long yardId = yarddata.getId();
            long vehicleGroupId = vgdata.getId();
            Activity activity = NumLotGenerationActivity.this;

            JSONObject job = new JSONObject();
            String action = getResources().getString(R.string.actiongeneratelot);
            ConstantFunction cf = new ConstantFunction();
            String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
            String[] value = new String[]{"", "0", ""};
            String[] data = cf.getSharedPrefValue(activity, key, value);

            try {
                job.put("yearId", data[2]);
                job.put("yardId", yardId);
                job.put("vehicleGroupId", vehicleGroupId);
            } catch (JSONException e) {
                e.printStackTrace();
                Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
                return;
            }

            String servlet = getResources().getString(R.string.servletnumbersys);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Call<LotGenResponse> call2 = apiInterface.lotGenNumberSys(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            RetrofitHandler<LotGenResponse> reqfarmer = new RetrofitHandler<>();
            reqfarmer.handleRetrofit(call2, NumLotGenerationActivity.this, RequestEnum.SAVE, servlet, activity, versioncode, yarddata.getName(), vgdata.getName());

        } else if (id == R.id.btnclear) {

            TableLayout tbldata = findViewById(R.id.tbldata);
            SingleSpinnerSearch sspyard = findViewById(R.id.sspyard);
            SingleSpinnerSearch sspvehiclegroup = findViewById(R.id.sspvehiclegroup);
            AppCompatButton btnexclude = findViewById(R.id.btnexclude);
            AppCompatButton btnprint = findViewById(R.id.btnprint);
            AppCompatButton btnexportpdf = findViewById(R.id.btnexportpdf);
            AppCompatTextView txthead = findViewById(R.id.txthead);

            head = null;
            htmlContent = null;
            printhead = null;
            ConstraintLayout clmain = findViewById(R.id.clmain);
            ConstraintSet set = new ConstraintSet();
            set.constrainPercentWidth(R.id.btnclear, 0.45f);
            set.applyTo(clmain);

            txthead.setText("");
            txthead.setVisibility(View.VISIBLE);
            btnexclude.setVisibility(View.VISIBLE);
            btnprint.setVisibility(View.GONE);
            btnexportpdf.setVisibility(View.GONE);
            tbldata.removeAllViews();
            sspyard.clearAll();
            sspvehiclegroup.clearAll();
        } else if (id == R.id.btnprint) {
            Intent intent = new Intent(NumLotGenerationActivity.this, SugarReceiptReprintActivity.class);
            intent.putExtra("html", htmlContent);
            intent.putExtra("mainhead", printhead);
            intent.putExtra("backpress", BackPress.NUMLISTPRINT);
            startActivity(intent);
        } else if (id == R.id.btnexportpdf) {
            exportPdf();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(NumLotGenerationActivity.this, HomeActivity.class);
        startActivity(intent1);
    }
}