package com.sleeve.net;

import com.sleeve.net.download.DownloadBean;
import com.sleeve.net.download.DownloadSource;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * 网络请求工厂类
 * <p>
 * Create by lzx on 2019/8/19.
 */
public class NetRequestFactory {

    /* http get */
    public static Observable<Response<BaseBean>> createGet(String arg, Map<String, String> argMap) {
        return NetRetrofitFactory.createRetrofit().create(NetApi.class).get(arg, argMap)
                .compose(RxSchedulers.scheduler())
                .retry(1);
    }

    /* http post */
    public static Observable<Response<BaseBean>> createPost(String arg, Map<String, String> argMap) {
        return NetRetrofitFactory.createRetrofit().create(NetApi.class).post(arg, argMap)
                .compose(RxSchedulers.scheduler())
                .retry(1);
    }

    /* url get */
    public static Observable<Response<BaseBean>> createUrlGet(String url, Map<String, String> argMap) {
        return NetRetrofitFactory.createRetrofit().create(NetApi.class).getUrl(url, argMap)
                .compose(RxSchedulers.scheduler())
                .retry(1);
    }

    /* url post */
    public static Observable<Response<BaseBean>> createUrlPost(String url, Map<String, String> argMap) {
        return NetRetrofitFactory.createRetrofit().create(NetApi.class).postUrl(url, argMap)
                .compose(RxSchedulers.scheduler())
                .retry(1);
    }

    /* url download */
    public static Observable<DownloadBean> createDownload(DownloadBean bean) {
        return NetRetrofitFactory.createDownloadRetrofit()
                .create(NetApi.class).downloadFile("bytes=" + bean.bytesLoaded + "-", bean.url)
                .subscribeOn(Schedulers.io())
                .flatMap((Function<ResponseBody, ObservableSource<DownloadBean>>) responseBody ->
                        new DownloadSource(responseBody, bean))
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /* post 上传多张图片 */
    public static Observable<Response<BaseBean>> createUpload(String arg, List<MultipartBody.Part> files) {
        Observable<Response<BaseBean>> observable = NetRetrofitFactory.createRetrofit()
                .create(NetApi.class).postUpload(arg, files);
        return observable.compose(RxSchedulers.scheduler()).retry(1);
    }

    /* post url 上传多张图片 */
    public static Observable<Response<BaseBean>> createUploadUrl(String url, List<MultipartBody.Part> files) {
        Observable<Response<BaseBean>> observable = NetRetrofitFactory.createRetrofit()
                .create(NetApi.class).postUploadUrl(url, files);
        return observable.compose(RxSchedulers.scheduler()).retry(1);
    }

    /* url post 直接获取ResponseBody*/
    public static Observable<ResponseBody> createBodyPost(String url, Map<String, String> argMap) {
        return NetRetrofitFactory.createRetrofit().create(NetApi.class).postUrlBody(url, argMap)
                .compose(RxSchedulers.scheduler())
                .retry(1);
    }

    /* url get 直接获取ResponseBody*/
    public static Observable<ResponseBody> createBodyGet(String url, Map<String, String> argMap) {
        return NetRetrofitFactory.createRetrofit().create(NetApi.class).getUrlBody(url, argMap)
                .compose(RxSchedulers.scheduler())
                .retry(1);
    }
}
