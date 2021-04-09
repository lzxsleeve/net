package com.sleeve.net.function;

import com.alibaba.fastjson.JSON;
import com.sleeve.net.BaseBean;
import com.sleeve.net.NetConfig;
import com.sleeve.net.R;
import com.sleeve.net.throwable.NetErrorException;
import com.sleeve.net.throwable.NetToastException;
import com.sleeve.net.throwable.NoDataException;
import com.sleeve.net.throwable.OtherCodeException;
import com.sleeve.util.UtilConfig;

import io.reactivex.rxjava3.functions.Function;
import retrofit2.Response;

/**
 * 说明
 * <p>
 * Create by lzx on 2019/7/24.
 */
public abstract class BaseFunction<T> implements Function<Response<BaseBean>, T> {

    @Override
    public T apply(Response<BaseBean> response) throws Exception {
        if (response.code() == 200) {
            BaseBean bean = response.body();
            if (bean == null) {
                throw new NullPointerException("body 为空");
            }
            String msg = bean.getMsg();
            String code = bean.getCode();
            return codeAnalyze(bean, msg, code);
        } else {
            throw new NetErrorException(UtilConfig.mContext.getString(R.string.net_request_error) + response.message());
        }
    }

    /**
     * 分别对不同的 code 进行处理
     */
    private T codeAnalyze(BaseBean bean, String msg, String code) throws Exception {
        if (NetConfig.getInstance().getSuccessful().equals(code)) {
            // 100 成功返回
            return convert(bean.getResult());
        } else if (NetConfig.getInstance().getToast101().equals(code)) {
            // 101 只需要弹出Toast(包含错误，友好提示)
            throw new NetToastException(msg);
        } else if (NetConfig.getInstance().getNoData().equals(code)) {
            // 104 没有相关数据
            throw new NoDataException(msg);
        } else {
            // 除了code=100、101、104的其他情况，根据需求继承实现
            OtherCodeException otherCodeException = NetConfig.getInstance().mOtherCodeException;
            if (otherCodeException != null) {
                otherCodeException.mCode = code;
                otherCodeException.mMsg = msg;
                throw otherCodeException;
            } else {
                throw new NullPointerException("code=" + code + "先初始化 NetConfig.mOtherCodeException");
            }
        }
    }

    /**
     * 转换数据
     */
    protected abstract T convert(JSON result) throws Exception;
}
