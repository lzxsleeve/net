package com.sleeve.net.download;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sleeve.net.NetCallback;
import com.sleeve.net.NetRequestFactory;
import com.sleeve.util.UtilConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 下载管理器
 * <p>
 * Create by lzx on 2019/7/24.
 */
public class DownloadManager {

    /**
     * 最大同时的下载量
     */
    public int mMaxDownload = 3;
    public List<DownloadBean> mDownloadList;

    private String mFileFolder;

    private DownloadManager() {
        if (mDownloadList == null) {
            mDownloadList = Collections.synchronizedList(new ArrayList<>());
        }
    }

    public static DownloadManager getInstance() {
        return DownloadManager.SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        private static final DownloadManager INSTANCE = new DownloadManager();
    }

    /**
     * 添加一个下载队列
     */
    public void addDownload(String url, DownloadListener downloadListener) {
        DownloadBean bean = getDownloadBean(url);
        if (bean == null) {
            // 当前没有添加过下载
            bean = addDownloadBean(url, downloadListener);
            if (bean.downloadStatus == DownloadListener.FINISH) {
                // 已经下载完成过了
                bean.mDownloadListener.onDownloadFinish(bean);
            } else {
                executeDownload(bean);
            }
        } else {
            // 已经有添加过了
            bean.mDownloadListener = downloadListener;
            executeHasAdd(bean);
        }
    }

    /**
     * 执行下载
     */
    private void executeDownload(DownloadBean bean) {
        if (getDownloadingNum() < mMaxDownload) {
            // 开始下载前请求网络有延迟，设置为等待状态
            bean.downloadStatus = DownloadListener.WAIT;
            bean.mDownloadListener.onDownloadWait(bean);
            // 未达到最大同时下载量
            requestDownload(bean);
        } else {
            // 已经达到最大同时下载量
            bean.downloadStatus = DownloadListener.WAIT;
            bean.mDownloadListener.onDownloadWait(bean);
        }
    }

    /**
     * 执行已经添加过的，再次添加会执行以下逻辑
     * 存在几种情况：下载中、等待中、暂停
     */
    private void executeHasAdd(DownloadBean bean) {
        switch (bean.downloadStatus) {
            case DownloadListener.DOWNLOADING:
                // 下载中-暂停
                bean.disposable();
                bean.downloadStatus = DownloadListener.PAUSE;
                bean.mDownloadListener.onDownloadPause(bean);
                // 去下载其他等待队列
                executeDownloadWait();
                break;
            case DownloadListener.WAIT:
                // 等待中-暂停
                bean.downloadStatus = DownloadListener.PAUSE;
                bean.mDownloadListener.onDownloadPause(bean);
                break;
            case DownloadListener.PAUSE:
                // 暂停-下载中或者等待中
                executeDownload(bean);
                break;
            case DownloadListener.FINISH:
                // 已完成的要移除队列，文件被删除后会重新下载
                try {
                    mDownloadList.remove(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addDownload(bean.url, bean.mDownloadListener);
                break;
            default:
                Log.e("download", "executeHasAdd: 已经添加过了但未实现的状态");
                break;
        }
    }

    /**
     * 请求下载
     */
    private void requestDownload(DownloadBean bean) {
        bean.downloadStatus = DownloadListener.DOWNLOADING;
        NetRequestFactory.createDownload(bean)
                .subscribe(bean.mDisposable = new NetCallback<DownloadBean>(UtilConfig.mContext) {

                    @Override
                    public void onSuccess(@NonNull DownloadBean bean1) {
                        if (bean.mDownloadListener != null) {
                            bean.mDownloadListener.onDownloading(bean1);
                        }
                    }

                    @Override
                    public boolean onError(@NonNull String msg) {
                        if (bean.bytesLoaded > 0 && bean.downloadFinish()) {
                            bean.downloadStatus = DownloadListener.FINISH;
                            bean.mDownloadListener.onDownloadFinish(bean);
                        } else {
                            bean.downloadStatus = DownloadListener.PAUSE;
                            bean.mDownloadListener.onDownloadError(bean, msg);
                        }

                        return super.onError(msg);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        bean.downloadStatus = DownloadListener.FINISH;
                        bean.mDownloadListener.onDownloadFinish(bean);

                        // 执行下一个等待队列
                        executeDownloadWait();
                    }
                });
    }

    /**
     * 已经有一个队列下载完毕，执行下一个等待中的队列
     */
    private void executeDownloadWait() {
        DownloadBean bean = getWaitDownloadBean();
        if (bean != null) {
            requestDownload(bean);
        }
    }

    /**
     * 单个暂停下载
     * 当前是下载中才能执行操作
     */
    public void pauseDownload(String url) {
        DownloadBean bean = getDownloadBean(url);
        if (bean != null && bean.downloadStatus == DownloadListener.DOWNLOADING) {
            bean.disposable();
            bean.downloadStatus = DownloadListener.PAUSE;
            bean.mDownloadListener.onDownloadPause(bean);
        }
    }

    /**
     * 全部暂停下载
     */
    public void pauseAllDownload() {
        for (DownloadBean bean : mDownloadList) {
            bean.disposable();
            bean.downloadStatus = DownloadListener.PAUSE;
            bean.mDownloadListener.onDownloadPause(bean);
        }
    }

    /**
     * 取消下载，需要删除对应文件
     * 当前是下载中才能执行操作
     */
    public void cancelDownload(String url) {
        DownloadBean bean = getDownloadBean(url);
        if (bean != null && bean.downloadStatus == DownloadListener.DOWNLOADING) {
            bean.disposable();
            bean.downloadStatus = DownloadListener.CANCEL;
            File file = new File(bean.fileFolder + bean.fileName);
            if (file.exists()) {
                file.delete();
            }
            bean.mDownloadListener.onDownloadCancel(bean);
        }
    }

    /**
     * 添加一个下载 bean
     */
    private DownloadBean addDownloadBean(String url, DownloadListener listener) {
        DownloadBean bean = new DownloadBean();
        bean.url = url;
        bean.mDownloadListener = listener;
        bean.fileFolder = getFileFolder();
        String lastName = getUrlLastName(url);
        bean.fileName = lastName + ".tmp";

        if (hasDownloadFinish(bean.fileFolder + lastName)) {
            // 判断是否已经下载完成
            bean.downloadStatus = DownloadListener.FINISH;
        } else {
            // 设置断点续传
            File file = new File(bean.fileFolder + bean.fileName);
            if (file.exists()) {
                bean.bytesLoaded = file.length();
            }
        }

        mDownloadList.add(bean);
        return bean;
    }

    /**
     * 获取下载队列
     */
    @Nullable
    public synchronized DownloadBean getDownloadBean(String url) {
        for (DownloadBean bean : mDownloadList) {
            if (TextUtils.equals(bean.url, url)) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 获取等待中的下载队列
     */
    @Nullable
    private synchronized DownloadBean getWaitDownloadBean() {
        for (DownloadBean bean : mDownloadList) {
            if (bean.downloadStatus == DownloadListener.WAIT) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 获取正在下载中的队列
     */
    @Nullable
    private synchronized int getDownloadingNum() {
        int downloadNum = 0;
        for (DownloadBean bean : mDownloadList) {
            if (bean.downloadStatus == DownloadListener.DOWNLOADING) {
                ++downloadNum;
            }
        }
        return downloadNum;
    }

    /**
     * 设置下载保存的文件夹
     */
    public void setDownloadFileFolder(String folder) {
        mFileFolder = folder;
    }

    public String getFileFolder() {
        if (TextUtils.isEmpty(mFileFolder)) {
            mFileFolder = UtilConfig.mContext.getExternalCacheDir() + File.separator;
        }
        return mFileFolder;
    }

    /**
     * 退出 Activity 或者 Fragment 调用
     */
    public void clearAll() {
        if (mDownloadList != null) {
            pauseAllDownload();
            mDownloadList.clear();
        }
    }

    /**
     * 判断文件是否存在
     */
    public static boolean hasDownloadFinish(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 获取 url "/"最后的名字
     */
    public static String getUrlLastName(String url) {
        String[] split = url.split("/");
        return split[split.length - 1];
    }
}
