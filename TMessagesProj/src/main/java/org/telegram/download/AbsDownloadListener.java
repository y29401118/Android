package org.telegram.download;

import com.arialyy.aria.core.task.DownloadTask;

/**
 * Desc:监听器，主要是集各监听方法最终于一个出口onNotify,简化实现类的方法
 *
 * @author ink
 * created at 2020/1/11 11:26
 */
public abstract class AbsDownloadListener implements OnDownloadListener {
    @Override
    public void onWait(DownloadTask task) {
        onNotify(task);
    }

    @Override
    public void onPre(DownloadTask task) {
        onNotify(task);
    }

    @Override
    public void onStart(DownloadTask task) {
        onNotify(task);
    }

    @Override
    public void onRunning(DownloadTask task) {
        onNotify(task);
    }

    @Override
    public void onResume(DownloadTask task) {
        onNotify(task);
    }

    @Override
    public void onStop(DownloadTask task) {
        onNotify(task);
    }

    @Override
    public void onCancel(DownloadTask task) {
        onNotify(task);
    }

    @Override
    public void onFail(DownloadTask task) {
        onNotify(task);
    }

    @Override
    public void onComplete(DownloadTask task) {
        onNotify(task);
    }

    public abstract void onNotify(DownloadTask task);
}
