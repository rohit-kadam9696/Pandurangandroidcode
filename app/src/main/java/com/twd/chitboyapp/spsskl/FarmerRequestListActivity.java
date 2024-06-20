package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.adapter.NPFarmerListAdapter;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.enums.FarmerList;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.FarmerListResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;

import retrofit2.Call;
import retrofit2.Response;

public class FarmerRequestListActivity extends AppCompatActivity implements RetrofitResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_request_list);

        Intent intent = getIntent();
        FarmerList farmerList = (FarmerList) intent.getSerializableExtra("caller");
        Activity activity = this;

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        String action = null;
        RequestEnum requestEnum = null;
        switch (farmerList) {
            case BYCHITBOY:
                action = getResources().getString(R.string.actionfarmerlistchitboy);
                requestEnum = RequestEnum.LISTCHITBOY;
                break;
            case BYOFFICER:
                action = getResources().getString(R.string.actionfarmerlistofficer);
                requestEnum = RequestEnum.LISTOFFICER;
                break;
            default:
                Constant.showToast(getResources().getString(R.string.errorwromgcaller), activity, R.drawable.ic_wrong);
                break;
        }

        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefuniquestring), getResources().getString(R.string.chitboyprefchit_boy_id)};
        String[] value = new String[]{"", "0"};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        String servlet = activity.getResources().getString(R.string.servletregApproval);
        APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
        String versioncode = cf.getVersionCode();
        Call<FarmerListResponse> call2 = apiInterface.farmerListActions(action, "{}", cf.getImei(activity), data[0], data[1], versioncode);
        RetrofitHandler<FarmerListResponse> otphandler = new RetrofitHandler<>();
        otphandler.handleRetrofit(call2, FarmerRequestListActivity.this, requestEnum, servlet, activity, versioncode, farmerList);
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        switch (requestCaller) {
            case LISTCHITBOY:
            case LISTOFFICER:
                FarmerListResponse farmerResponse = (FarmerListResponse) response.body();
                NPFarmerListAdapter npFarmerListAdapter = new NPFarmerListAdapter(farmerResponse.getEntityMasterDetailList(), activity, (FarmerList) obj[0]);
                RecyclerView mmlist = findViewById(R.id.mmlist);
                GridLayoutManager mLayoutManager1 = new GridLayoutManager(activity, 1);
                mmlist.setLayoutManager(mLayoutManager1);
                mmlist.setItemAnimator(new DefaultItemAnimator());
                mmlist.setAdapter(npFarmerListAdapter);
                break;
        }

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

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
}