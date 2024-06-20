package com.twd.chitboyapp.spsskl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.view.TouchImageView;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);

        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);

        TouchImageView image = findViewById(R.id.image);
        Intent intent = getIntent();
        String internal = intent.getStringExtra("internal");
        String filePath = intent.getStringExtra("url");
        if (filePath != null) {
            if (internal != null && internal.equals("1")) {
                Picasso.get().load(new File(filePath)).into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        image.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        ConstantFunction cf = new ConstantFunction();
                        cf.showCustomAlert(ImageViewActivity.this, R.drawable.ic_wrong, getResources().getString(R.string.errorphotonotfound));
                    }
                });
            } else {
                Picasso.get().load(filePath)
                        .error(R.mipmap.ic_action_error)
                        .placeholder(R.mipmap.ic_launcher)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(image);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MenuHandler mh = new MenuHandler();
        return mh.openCommon(this, item, null);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler mh = new MenuHandler();
        return mh.performRefreshOption(menu, this);
    }

}