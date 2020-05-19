package com.sleeve.net.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * 下载 Source
 * <p>
 * Create by lzx on 2019/7/24.
 */
public class DownloadSource implements ObservableSource<DownloadBean> {

    private final DownloadBean mBean;
    private ResponseBody mResponseBody;
    private long mTimeMillis = System.currentTimeMillis();

    public DownloadSource(ResponseBody responseBody, DownloadBean bean) {
        mResponseBody = responseBody;
        mBean = bean;
    }

    @Override
    public void subscribe(Observer<? super DownloadBean> observer) {
        File file = new File(mBean.fileFolder + mBean.fileName);
        saveFile(observer, file, mResponseBody);
    }

    private void saveFile(Observer<? super DownloadBean> observer, File file, ResponseBody responseBody) {
        try (RandomAccessFile out = new RandomAccessFile(file, "rwd");
             InputStream in = responseBody.byteStream()) {

            byte[] buffer = new byte[4096];
            int read;

            mBean.total = responseBody.contentLength();
            mBean.total += mBean.bytesLoaded;
            if (file.exists()) {
                out.seek(mBean.bytesLoaded);
            }

            while ((read = in.read(buffer)) != -1) {
                // 下载状态控制
                if (downloadStatus(file, out)) {
                    return;
                }
                // 写入数据
                out.write(buffer, 0, read);
                mBean.bytesLoaded += read;

                long currentTimeMillis = System.currentTimeMillis();
                // 防止频繁刷新
                if (currentTimeMillis - mTimeMillis >= 50) {
                    mTimeMillis = currentTimeMillis;
                    observer.onNext(mBean);
                }
            }

            // 避免最后一次没刷新数据
            observer.onNext(mBean);

            out.close();
            // 重命名
            file.renameTo(new File(mBean.fileFolder +
                    mBean.fileName.substring(0, mBean.fileName.length() - 4)));

            observer.onComplete();
        } catch (IOException e) {
            if (mBean.bytesLoaded > 0 && mBean.downloadFinish()) {
                mBean.downloadStatus = DownloadListener.FINISH;
                mBean.mDownloadListener.onDownloadFinish(mBean);
            } else {
                mBean.downloadStatus = DownloadListener.PAUSE;
                mBean.mDownloadListener.onDownloadError(mBean, e.getMessage());
            }
        }
    }

    private boolean downloadStatus(File file, RandomAccessFile out) throws IOException {
        if (mBean.downloadStatus == DownloadListener.PAUSE) {
            // 暂停
            out.close();
            return true;
        }
        if (mBean.downloadStatus == DownloadListener.CANCEL) {
            // 取消
            out.close();
            if (file.exists()) {
                file.delete();
            }
            return true;
        }
        return false;
    }

}
