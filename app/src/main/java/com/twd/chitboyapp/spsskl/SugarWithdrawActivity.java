package com.twd.chitboyapp.spsskl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.enums.MapEnum;
import com.twd.chitboyapp.spsskl.pojo.FarmerSugarResponse;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;

public class SugarWithdrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_withdraw);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        Activity activity = this;
        FarmerSugarResponse farmerSugarResponse = null;
        Intent intent = getIntent();
        if (intent.hasExtra("farmerSugarResponse")) {
            farmerSugarResponse = (FarmerSugarResponse) intent.getSerializableExtra("farmerSugarResponse");
        } else {
            Constant.showToast(getResources().getString(R.string.errorfarmerinfonotfound), activity, R.drawable.ic_wrong);
            finish();
        }

        AppCompatTextView txtempnameval = findViewById(R.id.txtempnameval);
        AppCompatTextView txtdatetimeval = findViewById(R.id.txtdatetimeval);
        AppCompatTextView txtlocationval = findViewById(R.id.txtlocationval);
        AppCompatTextView txtmaplocationval = findViewById(R.id.txtmaplocationval);

        txtempnameval.setText(farmerSugarResponse.getDistUserName());
        txtdatetimeval.setText(farmerSugarResponse.getPrevDate());
        txtlocationval.setText(farmerSugarResponse.getPrevLocation());
        txtmaplocationval.setText(farmerSugarResponse.getPrevLat() + " ," + farmerSugarResponse.getPrevLong());

        FarmerSugarResponse finalFarmerSugarResponse = farmerSugarResponse;
        txtmaplocationval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SugarWithdrawActivity.this, PlantAreaCalcuationActivity.class);
                intent.putExtra("map", MapEnum.VIEWLOCATION);
                intent.putExtra("visitLat", finalFarmerSugarResponse.getPrevLat() );
                intent.putExtra("visitlong", finalFarmerSugarResponse.getPrevLong());
                startActivity(intent);
            }
        });

        AppCompatImageView imgphoto = findViewById(R.id.imgphoto);

        Picasso.get().load(APIClient.getCurrentUrl(activity) + APIClient.imagePathSugar + farmerSugarResponse.getPrevPhoto()).error(R.mipmap.ic_action_error)
                .placeholder(R.mipmap.ic_launcher)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imgphoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        imgphoto.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        ConstantFunction cf = new ConstantFunction();
                        cf.showCustomAlert(activity, R.drawable.ic_wrong, activity.getResources().getString(R.string.errorphotonotfound));
                        imgphoto.setVisibility(View.GONE);
                    }
                });


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
        MenuItem mnupdf = menu.findItem(R.id.mnupdf);
        mnupdf.setVisible(true);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, this);
    }
}