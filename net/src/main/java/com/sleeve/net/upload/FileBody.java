package com.sleeve.net.upload;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * 上传文件类型(multipart/form-data)
 *
 * Create by lzx on 2019/8/20.
 */
public class FileBody extends RequestBody {
    private final File mFile;

    public FileBody(File file) {
        mFile = file;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("multipart/form-data");
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        try (Source source = Okio.source(mFile)) {
            sink.writeAll(source);
        }
    }

    public static MultipartBody.Part getPart(String name, File file) {
        return MultipartBody.Part.createFormData(name, file.getName(), new FileBody(file));
    }
}
