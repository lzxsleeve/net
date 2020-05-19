package com.sleeve.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * OkClient
 * <p>
 * Create by lzx on 2019/8/19.
 */
public class NetOkHttpClient {

    public static final int TIMEOUT = 60;

    private NetOkHttpClient() {
    }

    public static OkHttpClient.Builder getInstance() {
        return SingleHolder.instance;
    }

    private static class SingleHolder {
        private static final OkHttpClient.Builder instance = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS);
    }
}
