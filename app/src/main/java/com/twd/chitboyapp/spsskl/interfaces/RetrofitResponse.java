package com.twd.chitboyapp.spsskl.interfaces;

import android.app.Activity;

import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.pojo.MainResponse;

import retrofit2.Call;
import retrofit2.Response;

public interface RetrofitResponse<T extends MainResponse> {

    void onResponse(Call<T> call, Response<T> response, RequestEnum requestCaller, Activity activity, Object... obj);

    void onFailure(Call<T> call, Throwable t, RequestEnum requestCaller, Object... obj);
}
