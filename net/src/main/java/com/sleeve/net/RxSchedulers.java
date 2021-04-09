package com.sleeve.net;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
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
