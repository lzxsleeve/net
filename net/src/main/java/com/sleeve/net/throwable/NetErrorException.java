package com.sleeve.net.throwable;

/**
 * 网络异常
 * <p>
 * Create by lzx on 2019/8/19.
 */
public class NetErrorException extends BaseException {
    public NetErrorException(String message) {
        super(message);
    }

    @Override
    public void onNext(NetExceptionStatus netStatus) {
        if (!netStatus.onError(getMessage())) {
            showToast(netStatus);
        }
    }
}
