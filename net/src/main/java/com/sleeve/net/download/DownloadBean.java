package com.sleeve.net.download;

import com.sleeve.net.NetCallback;

import java.util.Locale;

/**
 * 下载Bean
 * <p>
 * Create by lzx on 2019/7/24.
 */
public class DownloadBean {

    public long total;
    public long bytesLoaded;
    public String url;
    public int downloadStatus;
    public DownloadListener mDownloadListener;

    public String fileName;
    public String fileFolder;
    public NetCallback<DownloadBean> mDisposable;

    /**
     * @return 获取总共大小 kb eg：21.28 kB
     */
    public String getTotalKB() {
        return String.format(Locale.CHINA, "%.2f", total / 1024D) + " kB";
    }

    /**
     * @return 获取总共大小 m eg：21.28 MB
     */
    public String getTotalMB() {
        return String.format(Locale.CHINA, "%.2f", total / 1024D / 1024D) + " MB";
    }

    /**
     * @return 获取下载了多少 kb eg：21.28 kB
     */
    public String getLoadedKB() {
        return String.format(Locale.CHINA, "%.2f", bytesLoaded / 1024D) + " kB";
    }

    /**
     * @return 获取下载了多少 m eg：21.28 MB
     */
    public String getLoadedMB() {
        return String.format(Locale.CHINA, "%.2f", bytesLoaded / 1024D / 1024D) + " MB";
    }

    /**
     * @return true 下载完成
     */
    public boolean downloadFinish() {
        return bytesLoaded == total;
    }

    /**
     * @return 下载的百分比 eg：24
     */
    public int getIntProgress() {
        return (int) (bytesLoaded * 100 / total);
    }

    /**
     * @return 下载的百分比 eg：24.0
     */
    public String getProgress() {
        return String.format(Locale.CHINA, "%.1f", bytesLoaded * 100d / total);
    }

    /**
     * 取消订阅
     */
    public void disposable() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public String toString() {
        return "DownloadBean{" +
                "total=" + total +
                ", bytesLoaded=" + bytesLoaded +
                ", url='" + url + '\'' +
                ", downloadStatus=" + downloadStatus +
                ", mDownloadListener=" + mDownloadListener +
                ", fileName='" + fileName + '\'' +
                ", fileFolder='" + fileFolder + '\'' +
                ", mDisposable=" + mDisposable +
                '}';
    }

}
