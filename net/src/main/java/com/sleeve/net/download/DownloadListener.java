package com.sleeve.net.download;

/**
 * 下载状态回调
 * <p>
 * Create by lzx on 2019/7/24.
 */
public interface DownloadListener {

    int DOWNLOADING = 1;
    int WAIT = 2;
    int PAUSE = 3;
    int CANCEL = 4;
    int FINISH = 5;

    /**
     * 下载中
     */
    void onDownloading(DownloadBean bean);

    /**
     * 等待中
     */
    void onDownloadWait(DownloadBean bean);

    /**
     * 完成
     */
    void onDownloadFinish(DownloadBean bean);

    /**
     * 暂停
     */
    void onDownloadPause(DownloadBean bean);

    /**
     * 下载中取消，删除文件
     */
    void onDownloadCancel(DownloadBean bean);

    /**
     * 错误
     */
    void onDownloadError(DownloadBean bean, String msg);
}
