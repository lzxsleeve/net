package com.sleeve.net.throwable;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * 异常基类
 * 包括没有数据，没有网络等等
 *
 * Create by lzx on 2019/7/24.
 */
public abstract class BaseException extends Exception {

    @Nullable
    public LoadStatus mLoader;

    public BaseException(String message) {
        super(message);
    }

    public abstract void onNext(NetExceptionStatus netStatus);

    @Nullable
    public Context getContext(NetExceptionStatus netStatus) {
        mLoader = netStatus.getLoader();
        if (mLoader != null) {
            return mLoader.getContext();
        } else {
            return netStatus.getContext();
        }
    }

    public void showToast(NetExceptionStatus netStatus) {
        Context context = getContext(netStatus);
        String message = getMessage();
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
