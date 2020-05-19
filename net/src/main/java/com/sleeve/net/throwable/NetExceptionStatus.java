package com.sleeve.net.throwable;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 说明
 * <p>
 * Create by lzx on 2019/8/19.
 */
public interface NetExceptionStatus {
    /**
     * 没有数据
     * @param msg 后台返回的提示
     */
    boolean onNoData(@NonNull String msg);

    /**
     * 加载失败，请求超时，json解析出错，未知错误
     */
    boolean onError(@NonNull String msg);

    /**
     * 友好提示，错误提示 Code=101
     */
    boolean onToast(@NonNull String msg);

    /**
     * code 是其他的情况
     */
    boolean onOtherCode(@NonNull String code, @NonNull String msg);

    @Nullable
    LoadStatus getLoader();

    @Nullable
    Context getContext();

    /**
     * 加载更多分页，page > 1 时转圈的处理
     */
    int getPage();
}
