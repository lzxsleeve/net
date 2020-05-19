package com.sleeve.net.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.util.List;

/**
 * 转换成 List<T>
 * <p>
 * Create by lzx on 2019/7/24.
 */
public class ListFunction<T> extends BaseFunction<List<T>> {

    private final String mArrayKey;
    private final Class<T> mClass;

    /**
     * @param arrayKey json数组的key
     */
    public ListFunction(String arrayKey, Class<T> aClass) {
        mArrayKey = arrayKey;
        mClass = aClass;
    }

    @SuppressWarnings("unchecked cast")
    @Override
    protected List<T> convert(JSON result) throws Exception {
        if (result instanceof JSONObject) {
            String jsonArray = ((JSONObject) result).getString(mArrayKey);
            List<T> list = JSON.parseArray(jsonArray, mClass);
            data(list);
            return list;
        } else {
            throw new JSONException("result数据异常");
        }
    }

    protected void data(List<T> list) {

    }
}
