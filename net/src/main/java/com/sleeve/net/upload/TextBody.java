package com.sleeve.net.upload;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * 上传文本类型("text/plain")
 *
 * Create by lzx on 2019/8/20.
 */
public class TextBody extends RequestBody {

    public String mKey;
    public String mValue;

    public TextBody(String key, String value) {
        this.mKey = key;
        this.mValue = value;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("text/plain; charset=utf-8");
    }

    @Override
    public long contentLength() throws IOException {
        return mValue.getBytes().length;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.write(mValue.getBytes());
    }

    public static MultipartBody.Part getPart(String name, String value) {
        return MultipartBody.Part.createFormData(name, null, new TextBody(name, value));
    }
}
