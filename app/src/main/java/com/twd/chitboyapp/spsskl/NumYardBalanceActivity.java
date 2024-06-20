package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.DynamicTableData;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.PDFOperations;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.CaneYardBalanceResponse;
import com.twd.chitboyapp.spsskl.pojo.PDFTablePOJO;
import com.twd.chitboyapp.spsskl.pojo.TableReportBean;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.pdfcreator.views.PDFBody;

import retrofit2.Call;
import retrofit2.Response;

public class NumYardBalanceActivity extends AppCompatActivity implements RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_yard_balance);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        loadReport();
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

            ConstantFunction cf = new ConstantFunction();
            String data[] = cf.getSharedPrefValue(NumYardBalanceActivity.this, new String[]{getResources().getString(R.string.prefprintersetting)}, new String[]{String.format(getResources().getString(R.string.defaultJson), getResources().getString(R.string.myfactory))});

            PDFTablePOJO[] pdfTablePOJOList = new PDFTablePOJO[3];
            PDFTablePOJO pdfTablePOJO = new PDFTablePOJO();

            pdfTablePOJO.setHeadView(findViewById(R.id.txtinyardvehiclelbl));
            pdfTablePOJO.setTbl(findViewById(R.id.tblinyardvehicle));
            pdfTablePOJOList[0] = pdfTablePOJO;

            pdfTablePOJO = new PDFTablePOJO();
            pdfTablePOJO.setHeadView(findViewById(R.id.txtemptyvehiclelbl));
            pdfTablePOJO.setTbl(findViewById(R.id.tblemptyvehicle));
            pdfTablePOJOList[1] = pdfTablePOJO;

            pdfTablePOJO = new PDFTablePOJO();
            pdfTablePOJO.setHeadView(findViewById(R.id.txtoutyardvehiclelbl));
            pdfTablePOJO.setTbl(findViewById(R.id.tbloutyardvehicle));
            pdfTablePOJOList[2] = pdfTablePOJO;


            PDFBody body = pdf.getBodyFromView(NumYardBalanceActivity.this, false, data[0], pdfTablePOJOList);

            PdfCreatorExampleActivity.pdfBody = body;
            Intent intent = new Intent(NumYardBalanceActivity.this, PdfCreatorExampleActivity.class);
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
        MenuItem mnupdf = menu.findItem(R.id.mnupdf);
        mnupdf.setVisible(true);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, this);
    }

    public void loadReport() {

        String action = getResources().getString(R.string.actionsummaryreport);
        Activity activity = NumYardBalanceActivity.this;
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        String servlet = getResources().getString(R.string.servletnumbersys);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<CaneYardBalanceResponse> call2 = apiInterface.caneYardNumberSys(action, "{}", cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<CaneYardBalanceResponse> handleRetrofit = new RetrofitHandler<>();
        handleRetrofit.handleRetrofit(call2, NumYardBalanceActivity.this, RequestEnum.SUMMARYREPORT, servlet, activity, versioncode);
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        CaneYardBalanceResponse tableResponse = (CaneYardBalanceResponse) response.body();

        AppCompatTextView txtinyardvehiclelbl = findViewById(R.id.txtinyardvehiclelbl);
        AppCompatTextView txtoutyardvehiclelbl = findViewById(R.id.txtoutyardvehiclelbl);
        AppCompatTextView txtemptyvehiclelbl = findViewById(R.id.txtemptyvehiclelbl);

        TableLayout tblinyardvehicle = findViewById(R.id.tblinyardvehicle);
        TableLayout tbloutyardvehicle = findViewById(R.id.tbloutyardvehicle);
        TableLayout tblemptyvehicle = findViewById(R.id.tblemptyvehicle);

        txtinyardvehiclelbl.setText(String.format(getResources().getString(R.string.inyardvehicle), tableResponse.getDateTime()));
        txtoutyardvehiclelbl.setText(String.format(getResources().getString(R.string.outyardvehicle), tableResponse.getDateTime()));
        txtemptyvehiclelbl.setText(String.format(getResources().getString(R.string.emptyvehicle), tableResponse.getDateTime()));

        TableReportBean inyardReportBean = tableResponse.getInyardVehicle();
        TableReportBean outYardReportBean = tableResponse.getOutyardVehicle();
        TableReportBean emptyVehicle = tableResponse.getEmptyVehicle();

        tblinyardvehicle.setTag(inyardReportBean.getColWidth());
        tbloutyardvehicle.setTag(outYardReportBean.getColWidth());
        tblemptyvehicle.setTag(emptyVehicle.getColWidth());

        DynamicTableData dtd = new DynamicTableData();
        dtd.addTable(activity, inyardReportBean.getTableData(), tblinyardvehicle, inyardReportBean.getRowColSpan(), inyardReportBean.getNoofHeads() != null ? inyardReportBean.getNoofHeads() : 1, inyardReportBean.isFooter(), inyardReportBean.getBoldIndicator(), false, inyardReportBean.getFloatings(), inyardReportBean.isMarathi(), inyardReportBean.getRowSpan(), inyardReportBean.getTextAlign(), inyardReportBean.getVisibility(), false, false, null, false);
        dtd.addTable(activity, outYardReportBean.getTableData(), tbloutyardvehicle, outYardReportBean.getRowColSpan(), outYardReportBean.getNoofHeads() != null ? outYardReportBean.getNoofHeads() : 1, outYardReportBean.isFooter(), outYardReportBean.getBoldIndicator(), false, outYardReportBean.getFloatings(), outYardReportBean.isMarathi(), outYardReportBean.getRowSpan(), outYardReportBean.getTextAlign(), outYardReportBean.getVisibility(), false, false, null, false);
        dtd.addTable(activity, emptyVehicle.getTableData(), tblemptyvehicle, emptyVehicle.getRowColSpan(), emptyVehicle.getNoofHeads() != null ? emptyVehicle.getNoofHeads() : 1, emptyVehicle.isFooter(), emptyVehicle.getBoldIndicator(), false, emptyVehicle.getFloatings(), emptyVehicle.isMarathi(), emptyVehicle.getRowSpan(), emptyVehicle.getTextAlign(), emptyVehicle.getVisibility(), false, false, null, false);
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}