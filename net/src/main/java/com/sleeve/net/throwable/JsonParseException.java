package com.sleeve.net.throwable;

/**
 * Json解析异常
 * <p>
 * Create by lzx on 2019/8/19.
 */
public class JsonParseException extends BaseException {

    public JsonParseException(String message) {
        super(message);
    }

    @Override
    public void onNext(NetExceptionStatus netStatus) {
        if (!netStatus.onError(getMessage())) {
            showToast(netStatus);
        }
    }
}
