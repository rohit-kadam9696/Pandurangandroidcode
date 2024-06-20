package com.twd.chitboyapp.spsskl.retrofit;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

public final class HostSelectionInterceptor implements Interceptor {
    private volatile String host;

    static HostSelectionInterceptor hostObj = null;

    public void setHost(String host) {
        this.host = host;
    }

    private HostSelectionInterceptor() {

    }

    public static HostSelectionInterceptor getInstance() {
        if (hostObj == null) {
            hostObj = new HostSelectionInterceptor();
        }
        return hostObj;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String host = this.host;
        if (host != null) {
            //HttpUrl newUrl = request.url().newBuilder()
            //    .host(host)
            //    .build();
            HttpUrl newUrl = HttpUrl.parse(host);
            request = request.newBuilder()
                    .url(newUrl)
                    .build();
        }
        return chain.proceed(request);
    }

}