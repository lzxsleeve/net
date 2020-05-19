package com.sleeve.net.throwable;

/**
 * 除了已经定义的 code 之外的其他 code 异常，方便新增 code 扩展
 *
 * Create by lzx on 2019/8/19.
 */
public abstract class OtherCodeException extends BaseException {

    public String mCode;
    public String mMsg;

    public OtherCodeException(String msg) {
        super(msg);
    }

    @Override
    public final void onNext(NetExceptionStatus netStatus) {
        if (!netStatus.onOtherCode(mCode, mMsg)) {
            otherCode(netStatus);
        }
    }

    protected abstract void otherCode(NetExceptionStatus netStatus);

}
