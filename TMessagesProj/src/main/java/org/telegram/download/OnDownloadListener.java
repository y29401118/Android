package org.telegram.download;

import com.arialyy.aria.core.task.DownloadTask;

/**
* Desc: 下载监听器
* @author ink
* created at 2020/1/11 11:12
*/
public interface OnDownloadListener {
    void onWait(DownloadTask task);

    void onPre(DownloadTask task);

    void onStart(DownloadTask task);

    void onRunning(DownloadTask task);

    void onResume(DownloadTask task);

    void onStop(DownloadTask task);

    void onCancel(DownloadTask task);

    void onFail(DownloadTask task);

    void onComplete(DownloadTask task);
}
