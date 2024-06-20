package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.twd.chitboyapp.spsskl.model.NetworkStateManager;


public class ConnectionUpdator {
    private Activity activity = null;
    public static Boolean isConnected = null;

    public ConnectionUpdator(Activity activity) {
        this.activity = activity;
    }

    private final Observer<Boolean> activeNetworkStateObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isConnected) {
            ConnectionUpdator.isConnected = isConnected;
            activity.invalidateOptionsMenu();
        }
    };

    public void startObserve(LifecycleOwner lifecycleOwner) {
        NetworkStateManager.getInstance().getNetworkConnectivityStatus()
                .observe(lifecycleOwner, activeNetworkStateObserver);
    }
}
