package com.sleeve.net.throwable;

/**
 * 没有数据 （104）
 *
 * Create by lzx on 2019/8/19.
 */
public class NoDataException extends BaseException {

    public NoDataException(String msg) {
        super(msg);
    }

    @Override
    public void onNext(NetExceptionStatus netStatus) {
        if (!netStatus.onNoData(getMessage())) {
            showToast(netStatus);
        }
    }

}
