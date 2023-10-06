package org.telegram.download;

import android.text.TextUtils;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.task.DownloadTask;
import com.google.android.exoplayer2.util.Log;

import org.telegram.utils.Commons;
import org.telegram.utils.StringsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc: 下载载体与状态监听类
 *
 * @author ink
 * created at 2020/1/11 11:26
 */
public class DownloadModule {
    private String TAG = "DownloadModule";
    private Map<String, OnDownloadListener> downloadListenerMap;
    private boolean isRegister = false;

    /**
     * 注册
     */
    void register() {
        if (!isRegister) {
            isRegister = true;
            downloadListenerMap = new HashMap<>(1);
        }
    }

    /**
     * 取消注册
     */
    void unregister() {
        if (Commons.isNotEmpty(downloadListenerMap)) {
            downloadListenerMap.clear();
        }
    }

    /**
     * 等待中
     *
     * @param task
     */
    @Download.onWait
    void onWait(DownloadTask task) {
        if (task != null) {
            Log.d(TAG, StringsUtil.buffer("onWait:", task.getKey(), "State:", task.getState()));
        }
        if (Commons.isNotEmpty(downloadListenerMap)) {
            for (Map.Entry<String, OnDownloadListener> entry : downloadListenerMap.entrySet()) {
                entry.getValue().onWait(task);
            }
        }
    }

    /**
     * 准备中
     *
     * @param task
     */
    @Download.onPre
    protected void onPre(DownloadTask task) {
        if (task != null) {
            Log.d(TAG, StringsUtil.buffer("onPre:", task.getKey(), "State:", task.getState()));
        }
        if (Commons.isNotEmpty(downloadListenerMap)) {
            for (Map.Entry<String, OnDownloadListener> entry : downloadListenerMap.entrySet()) {
                entry.getValue().onPre(task);
            }
        }
    }

    /**
     * 开始
     *
     * @param task
     */
    @Download.onTaskStart
    void onStart(DownloadTask task) {
        if (task != null) {
            Log.d(TAG, StringsUtil.buffer("onStart:", task.getKey(), "State:", task.getState()));
        }
        if (Commons.isNotEmpty(downloadListenerMap)) {
            for (Map.Entry<String, OnDownloadListener> entry : downloadListenerMap.entrySet()) {
                entry.getValue().onStart(task);
            }
        }
    }

    /**
     * 下载中
     *
     * @param task
     */
    @Download.onTaskRunning
    protected void onRunning(DownloadTask task) {
        if (task != null) {
            Log.d(TAG, StringsUtil.buffer("onRunning:", task.getKey(), "State:", task.getState()));
        }
        if (Commons.isNotEmpty(downloadListenerMap)) {
            for (Map.Entry<String, OnDownloadListener> entry : downloadListenerMap.entrySet()) {
                entry.getValue().onRunning(task);
            }
        }
    }

    /**
     * 恢复
     *
     * @param task
     */
    @Download.onTaskResume
    void onResume(DownloadTask task) {
        if (task != null) {
            Log.d(TAG, StringsUtil.buffer("onResume:", task.getKey(), "State:", task.getState()));
        }
        if (Commons.isNotEmpty(downloadListenerMap)) {
            for (Map.Entry<String, OnDownloadListener> entry : downloadListenerMap.entrySet()) {
                entry.getValue().onResume(task);
            }
        }
    }

    /**
     * 停止
     *
     * @param task
     */
    @Download.onTaskStop
    void onStop(DownloadTask task) {
        if (task != null) {
            Log.d(TAG, StringsUtil.buffer("onStop:", task.getKey(), "State:", task.getState()));
        }
        if (Commons.isNotEmpty(downloadListenerMap)) {
            for (Map.Entry<String, OnDownloadListener> entry : downloadListenerMap.entrySet()) {
                entry.getValue().onStop(task);
            }
        }
    }

    /**
     * 取消
     *
     * @param task
     */
    @Download.onTaskCancel
    void onCancel(DownloadTask task) {
        if (task != null) {
            Log.d(TAG, StringsUtil.buffer("onCancel:", task.getKey(), "State:", task.getState()));
        }
        if (Commons.isNotEmpty(downloadListenerMap)) {
            for (Map.Entry<String, OnDownloadListener> entry : downloadListenerMap.entrySet()) {
                entry.getValue().onCancel(task);
            }
        }
    }

    /**
     * 失败
     *
     * @param task
     */
    @Download.onTaskFail
    void onFail(DownloadTask task) {
        if (task != null) {
            Log.d(TAG, StringsUtil.buffer("onFail:", task.getKey(), "State:", task.getState()));
        }
        if (Commons.isNotEmpty(downloadListenerMap)) {
            for (Map.Entry<String, OnDownloadListener> entry : downloadListenerMap.entrySet()) {
                entry.getValue().onFail(task);
            }
        }
    }

    /**
     * 完成
     *
     * @param task
     */
    @Download.onTaskComplete
    void onComplete(DownloadTask task) {
        if (task != null) {
            Log.d(TAG, StringsUtil.buffer("onComplete:", task.getKey(), "State:", task.getState()));
        }
        if (Commons.isNotEmpty(downloadListenerMap)) {
            for (Map.Entry<String, OnDownloadListener> entry : downloadListenerMap.entrySet()) {
                Log.d(TAG, "onComplete key:" + entry.getKey());
                entry.getValue().onComplete(task);
            }
        }
    }

    /**
     * 添加监听
     *
     * @param listener
     */
    void addListener(OnDownloadListener listener, String tag) {
        if (downloadListenerMap != null && listener != null && !TextUtils.isEmpty(tag)) {
            downloadListenerMap.put(tag, listener);
        }
    }

    /**
     * 移除监听
     *
     * @param tag
     */
    void removeListener(String tag) {
        if (downloadListenerMap != null && !TextUtils.isEmpty(tag)) {
            downloadListenerMap.remove(tag);
            Log.i(TAG, " downloadListenerMap size: " + downloadListenerMap.size());
        }
    }

}
