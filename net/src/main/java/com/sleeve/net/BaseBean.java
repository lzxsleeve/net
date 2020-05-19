package com.sleeve.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 基础 Bean
 * <p>
 * Create by lzx on 2019/8/19.
 */
public class BaseBean {

    @JSONField(name = "code")
    private String code;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "body")
    private JSON result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSON getResult() {
        return result;
    }

    public void setResult(JSON result) {
        this.result = result;
    }
}
