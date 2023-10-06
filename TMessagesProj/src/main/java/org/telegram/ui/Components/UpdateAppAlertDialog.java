package org.telegram.ui.Components;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.inf.IEntity;
import com.arialyy.aria.core.task.DownloadTask;
import com.google.android.exoplayer2.util.Log;

import org.telegram.download.AbsDownloadListener;
import org.telegram.download.Downloads;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.net.UpdateEntity;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.utils.Apps;
import org.telegram.utils.Commons;
import org.telegram.utils.Files;
import org.telegram.utils.Jsons;

import java.io.File;

public class UpdateAppAlertDialog extends AlertDialog implements NotificationCenter.NotificationCenterDelegate {

    private TLRPC.TL_help_appUpdate appUpdate;
    private int accountNum;
    private String fileName;
    private RadialProgress radialProgress;
    private FrameLayout radialProgressView;
    private AnimatorSet progressAnimation;
    private Activity parentActivity;

    private String apkFilePath = null;
    private int state = 0;//0、初始，1、下载中，2、完成，3、失败
    private final int INIT = 0;
    private final int RUNNING = 1;
    private final int COMPLETE = 2;
    private final int FAILED = 3;

    public UpdateAppAlertDialog(final Activity activity, TLRPC.TL_help_appUpdate update, int account) {
        super(activity, 0);
        appUpdate = update;
        accountNum = account;
        if (update.document instanceof TLRPC.TL_document) {
            fileName = FileLoader.getAttachFileName(update.document);
        }
        parentActivity = activity;

        setTopImage(R.drawable.update, Theme.getColor(Theme.key_dialogTopBackground));
        setTopHeight(175);
        setMessage("开始更新");
//        if (appUpdate.document instanceof TLRPC.TL_document) {
//            setSecondTitle(AndroidUtilities.formatFileSize(appUpdate.document.size));
//        }
        setDismissDialogByButtons(false);
        setTitle(LocaleController.getString("UpdateTelegram", R.string.UpdateTelegram));
        setPositiveButton(LocaleController.getString("UpdateNow", R.string.UpdateNow), (dialog, which) -> {
            if (!BlockingUpdateView.checkApkInstallPermissions(getContext())) {
                return;
            }
            if (appUpdate.document instanceof TLRPC.TL_document) {
                if (!BlockingUpdateView.openApkInstall(parentActivity, appUpdate.document)) {
                    FileLoader.getInstance(accountNum).loadFile(appUpdate.document, "update", 1, 1);
                    showProgress(true);
                }
            } else if (appUpdate.url != null) {
                Browser.openUrl(getContext(), appUpdate.url);
                dialog.dismiss();
            }
        });
        setNeutralButton(LocaleController.getString("Later", R.string.Later), (dialog, which) -> {
            if (appUpdate.document instanceof TLRPC.TL_document) {
                FileLoader.getInstance(accountNum).cancelLoadFile(appUpdate.document);
            }
            dialog.dismiss();
        });

        radialProgressView = new FrameLayout(parentActivity) {
            @Override
            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                int width = right - left;
                int height = bottom - top;
                int w = AndroidUtilities.dp(24);
                int l = (width - w) / 2;
                int t = (height - w) / 2 + AndroidUtilities.dp(2);
                radialProgress.setProgressRect(l, t, l + w, t + w);
            }

            @Override
            protected void onDraw(Canvas canvas) {
                radialProgress.draw(canvas);
            }
        };
        radialProgressView.setWillNotDraw(false);
        radialProgressView.setAlpha(0.0f);
        radialProgressView.setScaleX(0.1f);
        radialProgressView.setScaleY(0.1f);
        radialProgressView.setVisibility(View.INVISIBLE);
        radialProgress = new RadialProgress(radialProgressView);
        radialProgress.setStrokeWidth(AndroidUtilities.dp(2));
        radialProgress.setBackground(null, true, false);
        radialProgress.setProgressColor(Theme.getColor(Theme.key_dialogButton));
    }


    public UpdateAppAlertDialog(final Activity activity, UpdateEntity entity, int account) {
        super(activity, 0);
//        appUpdate = update;
        accountNum = account;
//        if (update.document instanceof TLRPC.TL_document) {
//            fileName = FileLoader.getAttachFileName(update.document);
//        }
        parentActivity = activity;
        setTopImage(R.drawable.update, Theme.getColor(Theme.key_dialogTopBackground));
        setTopHeight(175);
        setMessage(entity.data.content);
//        if (appUpdate.document instanceof TLRPC.TL_document) {
//            setSecondTitle(AndroidUtilities.formatFileSize(appUpdate.document.size));
//        }
        setDismissDialogByButtons(false);
        setTitle("");
        setPositiveButton(LocaleController.getString("UpdateNow", R.string.UpdateNow), (dialog, which) -> {
            if (!Apps.checkWritePermission(activity)) {
                return;
            }
            if (!BlockingUpdateView.checkApkInstallPermissions(getContext())) {
                return;
            }
            if (getState() == COMPLETE && Files.isExists(apkFilePath)) {
                Apps.installApp(new File(apkFilePath), Apps.getPackgeName() + ".fileProvider", true);
            } else {
                Downloads.removeListener("UpdateAppAlertDialog");
                Downloads.addListener(onDownloadListener, "UpdateAppAlertDialog");
                DownloadEntity downloadEntity = Downloads.getDownloadEntity(entity.data.downloadUrl);
                if (downloadEntity == null) {
                    Log.i("UpdateAppAlertDialog", "开始下载任务： " + entity.data.downloadUrl);
                    Downloads.startApk(entity.data.downloadUrl, Jsons.toJson(entity));
                } else {
                    try {
                        if (downloadEntity.isComplete() && Files.isExists(downloadEntity.getFilePath())) {
                            dialog.dismiss();
                            apkFilePath = downloadEntity.getFilePath();
                            state = COMPLETE;
//                            AndroidUtilities.runOnUIThread(() -> NotificationCenter.getInstance(accountNum)
//                                    .postNotificationName(NotificationCenter.fileDidLoad, downloadEntity.getKey()));
                            didReceivedNotification(NotificationCenter.fileDidLoad, accountNum, "");
                        } else {
                            boolean resume = Downloads.resume(downloadEntity.getId(), true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                showProgress(true);
            }
        });
        setNeutralButton(LocaleController.getString("Later", R.string.Later), (dialog, which) -> {
//            if (appUpdate.document instanceof TLRPC.TL_document) {
//                FileLoader.getInstance(accountNum).cancelLoadFile(appUpdate.document);
//            }
            dialog.dismiss();
        });
        radialProgressView = new FrameLayout(parentActivity) {
            @Override
            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                int width = right - left;
                int height = bottom - top;
                int w = AndroidUtilities.dp(24);
                int l = (width - w) / 2;
                int t = (height - w) / 2 + AndroidUtilities.dp(2);
                radialProgress.setProgressRect(l, t, l + w, t + w);
            }

            @Override
            protected void onDraw(Canvas canvas) {
                radialProgress.draw(canvas);
            }
        };
        radialProgressView.setWillNotDraw(false);
        radialProgressView.setAlpha(0.0f);
        radialProgressView.setScaleX(0.1f);
        radialProgressView.setScaleY(0.1f);
        radialProgressView.setVisibility(View.INVISIBLE);
        radialProgress = new RadialProgress(radialProgressView);
        radialProgress.setStrokeWidth(AndroidUtilities.dp(2));
        radialProgress.setBackground(null, true, false);
        radialProgress.setProgressColor(Theme.getColor(Theme.key_dialogButton));
    }

    public int getState() {
        return state;
    }

    /**
     * 下载任务信息更新通知
     */
    AbsDownloadListener onDownloadListener = new AbsDownloadListener() {
        @Override
        public void onNotify(DownloadTask task) {

            if (task == null || task.getDownloadEntity() == null) {
                return;
            }
            String url = task.getDownloadEntity().getUrl();
            Log.i("UpdateAppAlertDialog", "onDownloadListener url: " + url);
            if (TextUtils.isEmpty(url)) {
                return;
            }
            if (url.endsWith(".apk")) {
                insertOrReplace(task);
            }
        }
    };

    /**
     * 更新或创建新任务信息
     *
     * @param downloadTask
     */
    private void insertOrReplace(DownloadTask downloadTask) {
        int tempStatus = downloadTask.getState();
        Log.i("UpdateAppAlertDialog", "insertOrReplace tempStatus: " + tempStatus);
        switch (tempStatus) {
            case IEntity.STATE_CANCEL:
                //删除任务
                state = FAILED;
//                AndroidUtilities.runOnUIThread(() -> NotificationCenter.getInstance(accountNum)
//                        .postNotificationName(NotificationCenter.fileDidFailToLoad, downloadTask.getKey()));
                didReceivedNotification(NotificationCenter.fileDidFailToLoad, accountNum, "");
                break;
            case IEntity.STATE_COMPLETE:
                //完成状态
                apkFilePath = downloadTask.getFilePath();
                state = COMPLETE;
//                AndroidUtilities.runOnUIThread(() -> NotificationCenter.getInstance(accountNum)
//                        .postNotificationName(NotificationCenter.fileDidLoad, downloadTask.getKey()));
                didReceivedNotification(NotificationCenter.fileDidLoad, accountNum, "");
                break;
            case IEntity.STATE_RUNNING:
                //正在执行
//                AndroidUtilities.runOnUIThread(() -> NotificationCenter.getInstance(accountNum)
//                        .postNotificationName(NotificationCenter.FileLoadProgressChanged, downloadTask.getKey(), downloadTask.getCurrentProgress(), downloadTask.getFileSize()));
                didReceivedNotification(NotificationCenter.FileLoadProgressChanged, accountNum, downloadTask.getKey(), downloadTask.getCurrentProgress(), downloadTask.getFileSize());
                break;
        }
    }


    @Override
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.fileDidLoad) {
//            String location = (String) args[0];
//            if (fileName != null && fileName.equals(location)) {
            showProgress(false);
//                BlockingUpdateView.openApkInstall(parentActivity, appUpdate.document);
            if (Commons.isNotEmpty(apkFilePath)) {
                Apps.installApp(new File(apkFilePath), Apps.getPackgeName() + ".fileProvider", true);
            }
//            }
        } else if (id == NotificationCenter.fileDidFailToLoad) {
//            String location = (String) args[0];
//            if (fileName != null && fileName.equals(location)) {
            showProgress(false);
//            }
        } else if (id == NotificationCenter.FileLoadProgressChanged) {
//            String location = (String) args[0];
//            if (fileName != null && fileName.equals(location)) {
            Long loadedSize = (Long) args[1];
            Long totalSize = (Long) args[2];
            float loadProgress = Math.min(1f, loadedSize / (float) totalSize);
            radialProgress.setProgress(loadProgress, true);
//            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationCenter.getInstance(accountNum).addObserver(this, NotificationCenter.fileDidLoad);
        NotificationCenter.getInstance(accountNum).addObserver(this, NotificationCenter.fileDidFailToLoad);
        NotificationCenter.getInstance(accountNum).addObserver(this, NotificationCenter.FileLoadProgressChanged);
        buttonsLayout.addView(radialProgressView, LayoutHelper.createFrame(36, 36));
    }

    @Override
    public void dismiss() {
        super.dismiss();
        NotificationCenter.getInstance(accountNum).removeObserver(this, NotificationCenter.fileDidLoad);
        NotificationCenter.getInstance(accountNum).removeObserver(this, NotificationCenter.fileDidFailToLoad);
        NotificationCenter.getInstance(accountNum).removeObserver(this, NotificationCenter.FileLoadProgressChanged);
    }

    private void showProgress(final boolean show) {
        if (progressAnimation != null) {
            progressAnimation.cancel();
        }
        progressAnimation = new AnimatorSet();
        final View textButton = buttonsLayout.findViewWithTag(BUTTON_POSITIVE);
        if (show) {
            radialProgressView.setVisibility(View.VISIBLE);
            textButton.setEnabled(false);
            progressAnimation.playTogether(
                    ObjectAnimator.ofFloat(textButton, "scaleX", 0.1f),
                    ObjectAnimator.ofFloat(textButton, "scaleY", 0.1f),
                    ObjectAnimator.ofFloat(textButton, "alpha", 0.0f),
                    ObjectAnimator.ofFloat(radialProgressView, "scaleX", 1.0f),
                    ObjectAnimator.ofFloat(radialProgressView, "scaleY", 1.0f),
                    ObjectAnimator.ofFloat(radialProgressView, "alpha", 1.0f));
        } else {
            textButton.setVisibility(View.VISIBLE);
            textButton.setEnabled(true);
            progressAnimation.playTogether(
                    ObjectAnimator.ofFloat(radialProgressView, "scaleX", 0.1f),
                    ObjectAnimator.ofFloat(radialProgressView, "scaleY", 0.1f),
                    ObjectAnimator.ofFloat(radialProgressView, "alpha", 0.0f),
                    ObjectAnimator.ofFloat(textButton, "scaleX", 1.0f),
                    ObjectAnimator.ofFloat(textButton, "scaleY", 1.0f),
                    ObjectAnimator.ofFloat(textButton, "alpha", 1.0f));

        }
        progressAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (progressAnimation != null && progressAnimation.equals(animation)) {
                    if (!show) {
                        radialProgressView.setVisibility(View.INVISIBLE);
                    } else {
                        textButton.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (progressAnimation != null && progressAnimation.equals(animation)) {
                    progressAnimation = null;
                }
            }
        });
        progressAnimation.setDuration(150);
        progressAnimation.start();
    }
}