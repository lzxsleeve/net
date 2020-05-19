package com.sleeve.net;

import android.text.TextUtils;

import com.sleeve.net.throwable.OtherCodeException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * 网络请求的一些配置
 * <p>
 * Create by lzx on 2019/8/19.
 */
public class NetConfig {

    // https或是http全部统一放在hostName里面设置
    public String mHostName = "http://api.com/";
    public String mArg = "android/";

    public String mSuccessful = "100";
    public String mToast101 = "101";
    public String mNoData = "104";

    // 除了已经定义的 code 之外的其他 code 异常，方便新增 code 扩展
    public OtherCodeException mOtherCodeException;

    // 列表分页开始的默认值
    public int mPage = 1;

    private NetConfig() {
    }

    public static NetConfig init() {
        return Holder.sInstance;
    }

    public static NetConfig getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private final static NetConfig sInstance = new NetConfig();
    }

    /**
     * 设置拦截器，比如添加统一头部、参数等
     */
    public NetConfig addInterceptor(Interceptor interceptor) {
        NetOkHttpClient.getInstance().addInterceptor(interceptor);
        return this;
    }

    /**
     * 获取 OKHttpClient 对象，可以设置超时时间等
     */
    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return NetOkHttpClient.getInstance();
    }

    public String getHostName() {
        checkNoNull(mHostName);
        return mHostName;
    }

    public String getSuccessful() {
        checkNoNull(mSuccessful);
        return mSuccessful;
    }

    public String getNoData() {
        checkNoNull(mNoData);
        return mNoData;
    }

    public String getToast101() {
        checkNoNull(mToast101);
        return mToast101;
    }

    public NetConfig setHostName(String hostName) {
        mHostName = hostName;
        return this;
    }

    public NetConfig setArg(String arg) {
        mArg = arg;
        return this;
    }

    public NetConfig setSuccessful(String successful) {
        mSuccessful = successful;
        return this;
    }

    public NetConfig setToast101(String toast101) {
        mToast101 = toast101;
        return this;
    }

    public NetConfig setNoData(String noData) {
        mNoData = noData;
        return this;
    }

    public NetConfig setOtherCodeException(OtherCodeException otherCodeException) {
        mOtherCodeException = otherCodeException;
        return this;
    }

    public NetConfig setPage(int page) {
        mPage = page;
        return this;
    }

    private void checkNoNull(String obj) {
        if (TextUtils.isEmpty(obj)) {
            throw new NullPointerException("请先配置 NetConfig 相关参数");
        }
    }
}
