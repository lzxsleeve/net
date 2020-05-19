package com.sleeve.net.throwable;

import android.content.Context;

import androidx.annotation.DrawableRes;

/**
 * 说明
 * <p>
 * Create by lzx on 2019/8/19.
 */
public interface LoadStatus {

    void loading(boolean hasWhiteBG);

    void loading(boolean hasWhiteBG, boolean hasHeadBar);

    void loadRemoveAll();

    void loadNoData(@DrawableRes int resId, String msg);

    void loadError(String msg);

    Context getContext();
}
