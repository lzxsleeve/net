package com.sleeve.net.throwable;

/**
 * 单独的弹出 Toast 提示 (Code=101)
 *
 * Create by lzx on 2019/8/19.
 */
public class NetToastException extends BaseException {

    public NetToastException(String message) {
        super(message);
    }

    @Override
    public void onNext(NetExceptionStatus netStatus) {
        if (!netStatus.onToast(getMessage())) {
            showToast(netStatus);
        }
    }

}
