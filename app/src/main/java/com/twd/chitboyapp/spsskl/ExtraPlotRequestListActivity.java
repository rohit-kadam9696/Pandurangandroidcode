package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.adapter.EPRListAdapter;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.ExcessTonPlotReqResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class ExtraPlotRequestListActivity extends AppCompatActivity implements RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_plot_request_list);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", ""};
        ConstantFunction cf = new ConstantFunction();
        Activity activity = ExtraPlotRequestListActivity.this;
        String[] sharedPrefval = cf.getSharedPrefValue(activity, key, value);

        JSONObject job = new JSONObject();
        String action = getResources().getString(R.string.actionextraplotrequest);

        try {
            job.put("yearCode", sharedPrefval[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String servlet = activity.getResources().getString(R.string.servletharvdata);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        RetrofitHandler<ExcessTonPlotReqResponse> handler = new RetrofitHandler<>();
        Call<ExcessTonPlotReqResponse> call2 = apiInterface.exessListHarvData(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), sharedPrefval[0], sharedPrefval[1], versioncode);
        handler.handleRetrofit(call2, ExtraPlotRequestListActivity.this, RequestEnum.LISTEXESSPLOT, servlet, activity, versioncode);

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
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.LISTEXESSPLOT) {
            ExcessTonPlotReqResponse excessTonPlotReqResponse = (ExcessTonPlotReqResponse) response.body();
            EPRListAdapter caneSampleListAdapter = new EPRListAdapter(excessTonPlotReqResponse.getList(), activity);
            RecyclerView mmlist = findViewById(R.id.mmlist);
            GridLayoutManager mLayoutManager1 = new GridLayoutManager(activity, 1);
            mmlist.setLayoutManager(mLayoutManager1);
            mmlist.setItemAnimator(new DefaultItemAnimator());
            mmlist.setAdapter(caneSampleListAdapter);

        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}