package com.sleeve.net;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

/**
 * 网络请求的类型、参数
 * <p>
 * Create by lzx on 2019/8/19.
 */
public interface NetApi {
    @GET("{arg}")
    Observable<Response<BaseBean>> get(@Path("arg") String arg, @QueryMap Map<String, String> argMap);

    @FormUrlEncoded
    @POST("{arg}")
    Observable<Response<BaseBean>> post(@Path("arg") String arg, @FieldMap Map<String, String> argMap);

    @GET()
    Observable<Response<BaseBean>> getUrl(@Url String url, @QueryMap Map<String, String> argMap);

    @FormUrlEncoded
    @POST()
    Observable<Response<BaseBean>> postUrl(@Url String url, @FieldMap Map<String, String> argMap);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Header("Range") String range, @Url String url);

    @Multipart
    @POST("{arg}")
    Observable<Response<BaseBean>> postUpload(@Path("arg") String arg, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST()
    Observable<Response<BaseBean>> postUploadUrl(@Url String url, @Part List<MultipartBody.Part> files);

    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> postUrlBody(@Url String url, @FieldMap Map<String, String> argMap);

    @GET()
    Observable<ResponseBody> getUrlBody(@Url String url, @QueryMap Map<String, String> argMap);
}
