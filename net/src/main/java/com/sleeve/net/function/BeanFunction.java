package com.sleeve.net.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

/**
 * 转换成 bean 或者 String
 *
 * Create by lzx on 2019/7/24.
 */
public class BeanFunction<T> extends BaseFunction<T> {

    private Class<T> mClass;

    public BeanFunction(Class<T> aClass) {
        mClass = aClass;
    }

    @SuppressWarnings("unchecked cast")
    @Override
    protected T convert(JSON result) throws Exception {
        if (String.class.equals(mClass)) {
            T t = (T) result.toJSONString();
            data(t);
            return t;
        } else {
            if (result instanceof JSONObject) {
                T t = JSON.parseObject(result.toJSONString(), mClass);
                data(t);
                return t;
            } else if (result instanceof JSONArray) {
                throw new JSONException("result必须是JSONObject");
            }
            throw new JSONException("result数据异常");
        }
    }

    protected void data(T t) {

    }
}
