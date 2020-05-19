package com.sleeve.net;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 说明
 * <p>
 * Create by lzx on 2019/8/19.
 */
public class RxSchedulers {
    static <T> ObservableTransformer<T, T> scheduler() {
        return upstream -> upstream
//                .delay(50, TimeUnit.MILLISECONDS) //延迟订阅，防止阻塞ui
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
