package com.sleeve.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONException;
import com.sleeve.net.throwable.BaseException;
import com.sleeve.net.throwable.JsonParseException;
import com.sleeve.net.throwable.LoadStatus;
import com.sleeve.net.throwable.NetErrorException;
import com.sleeve.net.throwable.NetExceptionStatus;
import com.sleeve.util.UtilConfig;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * 网络请求回调
 * <p>
 * Create by lzx on 2019/8/19.
 */
public abstract class NetCallback<T> extends DisposableObserver<T> implements NetExceptionStatus {
    // 分页
    public int mPage = NetConfig.getInstance().mPage;
    private LoadStatus mLoader;
    private Context mContext;

    public NetCallback(LoadStatus loader) {
        mLoader = loader;
    }

    public NetCallback(Context context) {
        mContext = context;
    }

    /**
     * 处理分页
     */
    public NetCallback(LoadStatus loader, int page) {
        mLoader = loader;
        mPage = page;
    }

    /**
     * 检查网络是否可用
     */
    @SuppressLint("MissingPermission")
    private static boolean networkIsAvailable() {
        ConnectivityManager service = (ConnectivityManager) UtilConfig.mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (service != null) {
            NetworkInfo networkInfo = service.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        if (!networkIsAvailable()) {
            dispose();
            onError(new NetErrorException(UtilConfig.mContext.getString(R.string.net_no_net_connect)));
        }
    }

    @Override
    public final void onNext(T t) {
        if (getLoader() != null) {
            getLoader().loadRemoveAll();
        }
        onSuccess(t);
    }

    @CallSuper
    @Override
    public void onError(Throwable e) {
        dispose();
        if (getLoader() != null) {
            getLoader().loadRemoveAll();
        }
        BaseException netError;
        if (e instanceof BaseException) {
            netError = (BaseException) e;
        } else if (e instanceof TimeoutException || e instanceof SocketTimeoutException
                || e instanceof ConnectException || e instanceof SocketException) {
            netError = new NetErrorException(UtilConfig.mContext.getString(R.string.net_time_out));
        } else if (e instanceof HttpException) {
            netError = new NetErrorException(UtilConfig.mContext.getString(R.string.net_http_error) + ((HttpException) e).code());
        } else if (e instanceof JSONException) {
            netError = new JsonParseException(UtilConfig.mContext.getString(R.string.net_data_parse_error));
        } else if (e instanceof SSLException) {
            netError = new NetErrorException(UtilConfig.mContext.getString(R.string.net_time_out));
        } else {
            netError = new NetErrorException(UtilConfig.mContext.getString(R.string.net_unknown_error));
        }
        netError.onNext(this);
    }

    @CallSuper
    @Override
    public void onComplete() {
        if (!isDisposed()) {
            dispose();
        }
    }

    public abstract void onSuccess(@NonNull T t);

    @Override
    public boolean onNoData(@NonNull String msg) {
        // 默认 mPage = NetConfig.init().mPage
        // 分页的情况会改变 mPage 则不处理无数据界面显示
        if (mLoader != null && mPage == NetConfig.getInstance().mPage) {
            mLoader.loadNoData(0, msg);
        }
        // false会执行showToast,true则禁止
        return false;
    }

    @Override
    public boolean onError(@NonNull String msg) {
        if (mLoader != null && mPage == NetConfig.getInstance().mPage) {
            mLoader.loadError(msg);
        }
        // false会执行showToast,true则禁止
        return false;
    }

    @Override
    public boolean onToast(@NonNull String msg) {
        return false;
    }

    @Override
    public boolean onOtherCode(@NonNull String code, @NonNull String msg) {
        return false;
    }

    @Nullable
    @Override
    public LoadStatus getLoader() {
        return mLoader;
    }

    @Nullable
    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public int getPage() {
        return mPage;
    }
}
