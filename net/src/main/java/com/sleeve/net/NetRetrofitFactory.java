package com.sleeve.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Retrofit 的工厂类
 * <p>
 * Create by lzx on 2019/8/19.
 */
public class NetRetrofitFactory {

    private NetRetrofitFactory() {
    }

    static Retrofit createRetrofit() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        private static final Retrofit INSTANCE = new Retrofit.Builder()
                .baseUrl(NetConfig.getInstance().getHostName())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(NetOkHttpClient.getInstance().build())
                .build();
    }

    static Retrofit createDownloadRetrofit() {
        return SingleDownloadHolder.DOWNLOAD_INSTANCE;
    }

    private static class SingleDownloadHolder {
        private static final Retrofit DOWNLOAD_INSTANCE = new Retrofit.Builder()
                .baseUrl(NetConfig.getInstance().getHostName())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(NetOkHttpClient.TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(NetOkHttpClient.TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(NetOkHttpClient.TIMEOUT, TimeUnit.SECONDS)
                        .build())
                .build();
    }
}
