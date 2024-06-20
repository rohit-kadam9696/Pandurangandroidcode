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
import androidx.appcompat.widget.AppCompatTextView;

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
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.chitboyapp.spsskl.pojo.NondReportHangamReponse;
import com.twd.chitboyapp.spsskl.pojo.NondReportReponse;
import com.twd.chitboyapp.spsskl.pojo.PDFTablePOJO;
import com.twd.chitboyapp.spsskl.pojo.TableReportBean;
import com.twd.chitboyapp.spsskl.pojo.VillageResonse;
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

public class NondReportActivity extends AppCompatActivity implements SingleReturn, RetrofitResponse {

    String[] sharedPrefval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nond_report);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = NondReportActivity.this;
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.prefofficer)};
        String[] value = new String[]{""};
        sharedPrefval = cf.getSharedPrefValue(activity, key, value);

        AppCompatTextView txtsectionlbl = findViewById(R.id.txtsectionlbl);
        SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
        SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);
        SingleSpinnerSearch ssphangam = findViewById(R.id.ssphangam);

        SingleSelectHandler ssh = new SingleSelectHandler();
        ssh.singleReturn = this;

        DBHelper dbHelper = new DBHelper(activity);
        List<KeyPairBoolData> hangamList = dbHelper.getHangam(1);

        ssh.initSingle(sspvillage, activity, getResources().getString(R.string.select));
        ssh.initSingle(ssphangam, activity, getResources().getString(R.string.select));
        ssh.initSingle(sspsection, activity, getResources().getString(R.string.select));

        ssh.setSingleItems(ssphangam, hangamList, DataSetter.HANGAM);
        if (sharedPrefval[0].equals("113")) {
            List<KeyPairBoolData> dummyData = new ArrayList<>(1);
            ssh.setSingleItems(sspvillage, dummyData, DataSetter.VILLAGE);

            List<KeyPairBoolData> sectionList = dbHelper.getSection(-1);
            ssh.setSingleItems(sspsection, sectionList, DataSetter.SECTION);

            txtsectionlbl.setVisibility(View.VISIBLE);
            sspsection.setVisibility(View.VISIBLE);
        } else {
            List<KeyPairBoolData> villageList = dbHelper.getVillage();
            ssh.setSingleItems(sspvillage, villageList, DataSetter.VILLAGE);

            txtsectionlbl.setVisibility(View.GONE);
            sspsection.setVisibility(View.GONE);
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
        if (item.getItemId() == R.id.mnupdf) {
            PDFOperations pdf = new PDFOperations();
            //String header = pdf.getHederFromView(RawanaReportActivity.this, null);

            String section = "";
            if (sharedPrefval[0].equals("113")) {
                SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
                List<KeyPairBoolData> selSect = sspsection.getSelectedItems();

                if (!selSect.isEmpty()) {
                    section = selSect.get(0).getName() + " / ";
                }
            }

            SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);
            String village;

            List<KeyPairBoolData> selVill = sspvillage.getSelectedItems();

            village = selVill.get(0).getName() + " - ";

            ConstantFunction cf = new ConstantFunction();
            String data[] = cf.getSharedPrefValue(NondReportActivity.this, new String[]{getResources().getString(R.string.prefprintersetting), getResources().getString(R.string.prefseason)}, new String[]{String.format(getResources().getString(R.string.defaultJson), getResources().getString(R.string.myfactory)), ""});

            PDFTablePOJO pdfTablePOJO = new PDFTablePOJO();
            AppCompatTextView lblplantsumm = findViewById(R.id.lblplantsumm);
            pdfTablePOJO.setHeadText(getResources().getString(R.string.season) + " : " + data[1] + "\n" + section + village + lblplantsumm.getText().toString());
            pdfTablePOJO.setTbl(findViewById(R.id.tblnondsummary));

            PDFTablePOJO pdfTablePOJO1 = new PDFTablePOJO();
            AppCompatTextView lblhagamsumm = findViewById(R.id.lblhagamsumm);
            pdfTablePOJO1.setHeadText(section + village + lblhagamsumm.getText().toString());
            pdfTablePOJO1.setTbl(findViewById(R.id.tblhangamsumm));

            PDFTablePOJO pdfTablePOJO2 = new PDFTablePOJO();
            AppCompatTextView lblvarietysumm = findViewById(R.id.lblvarietysumm);
            pdfTablePOJO2.setHeadText(section + village + lblvarietysumm.getText().toString());
            pdfTablePOJO2.setTbl(findViewById(R.id.tblvarietysumm));

            PDFTablePOJO pdfTablePOJO3 = new PDFTablePOJO();
            AppCompatTextView lblhangammjatsumm = findViewById(R.id.lblhangammjatsumm);
            pdfTablePOJO3.setHeadText(section + village + lblhangammjatsumm.getText().toString());
            pdfTablePOJO3.setTbl(findViewById(R.id.tblhangammjatsumm));
            PDFBody body = pdf.getBodyFromView(NondReportActivity.this, false, data[0], pdfTablePOJO, pdfTablePOJO1, pdfTablePOJO2, pdfTablePOJO3);

            PdfCreatorExampleActivity.pdfBody = body;
            Intent intent = new Intent(NondReportActivity.this, PdfCreatorExampleActivity.class);
            intent.putExtra("json", data[0]);
            /*intent.putExtra("body", body);*/
            startActivity(intent);

            return true;
        } else {
            MenuHandler cf = new MenuHandler();
            return cf.openCommon(this, item, null);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler cf = new MenuHandler();
        MenuItem mnupdf = menu.findItem(R.id.mnupdf);
        mnupdf.setVisible(true);
        return cf.performRefreshOption(menu, this);
    }

    private void loadReport() {
        Activity activity = NondReportActivity.this;
        SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);
        SingleSpinnerSearch ssphangam = findViewById(R.id.ssphangam);

        List<Long> selVillageId = sspvillage.getSelectedIds();
        List<KeyPairBoolData> selHangamId = ssphangam.getSelectedItems();
        String villageId, hangamId, hangamName;
        if (selVillageId.size() != 1) {
            Constant.showToast(getResources().getString(R.string.errorvillagenotselect), activity, R.drawable.ic_wrong);
            return;
        } else {
            villageId = selVillageId.get(0).toString();
        }

        if (selHangamId.size() != 1) {
            Constant.showToast(getResources().getString(R.string.errorhangamnotselect), activity, R.drawable.ic_wrong);
            return;
        } else {
            hangamId = String.valueOf(selHangamId.get(0).getId());
            hangamName = String.valueOf(selHangamId.get(0).getName());
        }

        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionnondreport);

        try {
            job.put("hangamId", hangamId);
            job.put("villageId", villageId);
            job.put("vyearCode", sharedPrefval[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String servlet = activity.getResources().getString(R.string.servletcanePlant);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        RetrofitHandler<NondReportReponse> handler = new RetrofitHandler<>();
        Call<NondReportReponse> call2 = apiInterface.canePlantationReport(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
        handler.handleRetrofit(call2, NondReportActivity.this, RequestEnum.NONDREPORT, servlet, activity, versioncode, hangamName);

    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {

        if (requestCaller == RequestEnum.NONDREPORT) {
            NondReportReponse nondReport = (NondReportReponse) response.body();
            TableLayout tblnondSummary = findViewById(R.id.tblnondsummary);
            TableLayout tblhangamsumm = findViewById(R.id.tblhangamsumm);
            TableLayout tblvarietysumm = findViewById(R.id.tblvarietysumm);
            TableLayout tblhangammjatsumm = findViewById(R.id.tblhangammjatsumm);


            AppCompatTextView lblhangammjatsumm = findViewById(R.id.lblhangammjatsumm);
            lblhangammjatsumm.setText(String.format(getResources().getString(R.string.hangammonthvarietysummary), (String) obj[0]));

            TableReportBean nondSummary = nondReport.getNondSummary() != null ? nondReport.getNondSummary() : new TableReportBean();
            TableReportBean hangamSummary = nondReport.getHangamSummary() != null ? nondReport.getHangamSummary() : new TableReportBean();
            TableReportBean varietySummary = nondReport.getVarietySummary() != null ? nondReport.getVarietySummary() : new TableReportBean();
            TableReportBean hangamMonthVariety = nondReport.getHangamMonthVarietyNond() != null ? nondReport.getHangamMonthVarietyNond() : new TableReportBean();

            tblnondSummary.setTag(nondSummary.getColWidth());
            tblhangamsumm.setTag(hangamSummary.getColWidth());
            tblvarietysumm.setTag(varietySummary.getColWidth());
            tblhangammjatsumm.setTag(hangamMonthVariety.getColWidth());

            DynamicTableData dtd = new DynamicTableData();
            dtd.addTable(activity, nondSummary.getTableData(), tblnondSummary, nondSummary.getRowColSpan(), nondSummary.getNoofHeads() != null ? nondSummary.getNoofHeads() : 1, nondSummary.isFooter(), nondSummary.getBoldIndicator(), false, nondSummary.getFloatings(), nondSummary.isMarathi(), nondSummary.getRowSpan(), nondSummary.getTextAlign(), nondSummary.getVisibility(), false, false, null, false);
            dtd.addTable(activity, hangamSummary.getTableData(), tblhangamsumm, hangamSummary.getRowColSpan(), hangamSummary.getNoofHeads() != null ? hangamSummary.getNoofHeads() : 1, hangamSummary.isFooter(), hangamSummary.getBoldIndicator(), false, hangamSummary.getFloatings(), hangamSummary.isMarathi(), hangamSummary.getRowSpan(), hangamSummary.getTextAlign(), hangamSummary.getVisibility(), false, false, null, false);
            dtd.addTable(activity, varietySummary.getTableData(), tblvarietysumm, varietySummary.getRowColSpan(), varietySummary.getNoofHeads() != null ? varietySummary.getNoofHeads() : 1, varietySummary.isFooter(), varietySummary.getBoldIndicator(), false, varietySummary.getFloatings(), varietySummary.isMarathi(), varietySummary.getRowSpan(), varietySummary.getTextAlign(), varietySummary.getVisibility(), false, false, null, false);
            dtd.addTable(activity, hangamMonthVariety.getTableData(), tblhangammjatsumm, hangamMonthVariety.getRowColSpan(), hangamMonthVariety.getNoofHeads() != null ? hangamMonthVariety.getNoofHeads() : 1, hangamMonthVariety.isFooter(), hangamMonthVariety.getBoldIndicator(), false, hangamMonthVariety.getFloatings(), hangamMonthVariety.isMarathi(), hangamMonthVariety.getRowSpan(), hangamMonthVariety.getTextAlign(), hangamMonthVariety.getVisibility(), false, false, null, false);

        } else if (requestCaller == RequestEnum.NONDREPORTHANGAM) {
            NondReportHangamReponse nondReport = (NondReportHangamReponse) response.body();
            TableLayout tblhangammjatsumm = findViewById(R.id.tblhangammjatsumm);
            DynamicTableData dtd = new DynamicTableData();
            TableReportBean hangamMonthVariety = nondReport.getHangamMonthVarietyNond() != null ? nondReport.getHangamMonthVarietyNond() : new TableReportBean();
            tblhangammjatsumm.setTag(hangamMonthVariety.getColWidth());
            dtd.addTable(activity, hangamMonthVariety.getTableData(), tblhangammjatsumm, hangamMonthVariety.getRowColSpan(), hangamMonthVariety.getNoofHeads() != null ? hangamMonthVariety.getNoofHeads() : 1, hangamMonthVariety.isFooter(), hangamMonthVariety.getBoldIndicator(), false, hangamMonthVariety.getFloatings(), hangamMonthVariety.isMarathi(), hangamMonthVariety.getRowSpan(), hangamMonthVariety.getTextAlign(), hangamMonthVariety.getVisibility(), false, false, null, false);

            AppCompatTextView lblhangammjatsumm = findViewById(R.id.lblhangammjatsumm);
            lblhangammjatsumm.setText(String.format(getResources().getString(R.string.hangammonthvarietysummary), (String) obj[0]));
        } else if (requestCaller == RequestEnum.LOADVILLAGE) {
            VillageResonse villageResonse = (VillageResonse) response.body();
            SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);
            SingleSelectHandler ssh = new SingleSelectHandler();
            ssh.singleReturn = this;
            ssh.setSingleItems(sspvillage, villageResonse.getVillList(), DataSetter.VILLAGE);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }

    @Override
    public void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter) {
        if (dataSetter == DataSetter.VILLAGE) {
            loadReport();
        } else if (dataSetter == DataSetter.HANGAM) {
            loadHangamReport();
        } else if (dataSetter == DataSetter.SECTION) {
            loadVillage();
        }

    }

    private void loadVillage() {

        Activity activity = NondReportActivity.this;
        SingleSpinnerSearch sspsection = findViewById(R.id.sspsection);
        List<Long> selSectionIds = sspsection.getSelectedIds();
        String sectionId;
        if (selSectionIds.size() != 1) {
            Constant.showToast(getResources().getString(R.string.errorsectionnotfound), activity, R.drawable.ic_wrong);
            return;
        } else {
            sectionId = selSectionIds.get(0).toString();
        }
        String action = getResources().getString(R.string.actionvillageBySetion);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();

        try {
            job.put("sectionId", sectionId);
        } catch (JSONException e) {
            e.printStackTrace();
            Constant.showToast("Local : " + e.getMessage(), activity, R.drawable.ic_wrong);
            return;
        }
        String servlet = getResources().getString(R.string.servletharvdata);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<VillageResonse> call2 = apiInterface.villList(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<VillageResonse> reqfarmer = new RetrofitHandler<>();
        reqfarmer.handleRetrofit(call2, NondReportActivity.this, RequestEnum.LOADVILLAGE, servlet, activity, versioncode);

    }

    private void loadHangamReport() {
        Activity activity = NondReportActivity.this;
        SingleSpinnerSearch sspvillage = findViewById(R.id.sspvillage);
        SingleSpinnerSearch ssphangam = findViewById(R.id.ssphangam);

        List<Long> selVillageId = sspvillage.getSelectedIds();
        List<KeyPairBoolData> selHangamId = ssphangam.getSelectedItems();
        String villageId, hangamId, hangamName;
        if (selVillageId.size() != 1) {
            Constant.showToast(getResources().getString(R.string.errorvillagenotselect), activity, R.drawable.ic_wrong);
            return;
        } else {
            villageId = selVillageId.get(0).toString();
        }

        if (selHangamId.size() != 1) {
            Constant.showToast(getResources().getString(R.string.errorhangamnotselect), activity, R.drawable.ic_wrong);
            return;
        } else {
            hangamId = String.valueOf(selHangamId.get(0).getId());
            hangamName = String.valueOf(selHangamId.get(0).getName());
        }

        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionnondhangamreport);

        try {
            job.put("hangamId", hangamId);
            job.put("villageId", villageId);
            job.put("vyearCode", sharedPrefval[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String servlet = activity.getResources().getString(R.string.servletcanePlant);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        RetrofitHandler<NondReportHangamReponse> handler = new RetrofitHandler<>();
        Call<NondReportHangamReponse> call2 = apiInterface.canePlantationReportHangam(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
        handler.handleRetrofit(call2, NondReportActivity.this, RequestEnum.NONDREPORTHANGAM, servlet, activity, versioncode, hangamName);


    }
}