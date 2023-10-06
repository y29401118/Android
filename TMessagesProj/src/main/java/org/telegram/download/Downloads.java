package org.telegram.download;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.HttpOption;
import com.arialyy.aria.core.common.RequestEnum;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.download.DownloadReceiver;
import com.arialyy.aria.core.download.target.HttpNormalTarget;
import com.arialyy.aria.core.processor.IKeyUrlConverter;
import com.google.android.exoplayer2.util.Log;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.utils.Commons;
import org.telegram.utils.Files;
import org.telegram.utils.SDCards;
import org.telegram.utils.StringsUtil;

import java.util.List;

public class Downloads {
    private static final String TAG = "Downloads";
    private static String fileSaveDir = null;
    private static HttpOption option = null;
    private static DownloadModule downloadModule = null;

    /**
     * 关于下载库的配置见:assets://aria_config.xml
     */
    public static void init(Context context) {
        synchronized (Downloads.class) {
            Aria.init(context);
        }
    }

    /**
     * 获取绑定类
     *
     * @return
     */
    private static DownloadModule getModule() {
        if (downloadModule == null) {
            downloadModule = new DownloadModule();
            register();
        }
        return downloadModule;
    }

    /**
     * 拿到下载服务对像
     *
     * @return
     */
    private static DownloadReceiver get() {
        return Aria.download(getModule());
    }

    /**
     * 绑定注册
     * <p>
     * 在Activity销毁时需要在modlue中调用unRegister销毁事件。 如果不进行销毁操作，将会导致内存泄露！！
     * 如：
     * 如果你在Module.java这个非Activity的类中进行register()；那么你应该在Module.java这个类中调用unregister()
     * <p>
     * 任意对像，但记得取消注册
     */
    public static void register() {
        Log.d(TAG, "register");
        getModule().register();
        get().register();
    }

    /**
     * 取消绑定
     * <p>
     * 注册时的对像
     */
    public static void unregister() {
        getModule().unregister();
        get().unRegister();
    }


    //mKeyUrlConverter
    static class KeyUrlConverter implements IKeyUrlConverter {
        @Override
        public String convert(String m3u8Url, String tsListUrl, String keyUrl) {
            Log.d(TAG, "KeyUrlConverter convert m3u8Url:" + m3u8Url);
            Log.d(TAG, "KeyUrlConverter convert keyUrl:" + keyUrl);
            return null;
        }
    }


    /**
     * 停止某个下载任务
     *
     * @param taskId
     * @return 是否停止了某个存在的任务
     */
    public static boolean stop(long taskId) {
        Log.d(TAG, "stop taskId:" + taskId);
        boolean stop = false;
        HttpNormalTarget target = get().load(taskId);
        if (target != null) {
            target.stop();
            stop = true;
        }
        return stop;
    }

    /**
     * 取消并删除记录与文件
     * <p>
     * ！！！！注意：
     * 这个是一定要有读写SD卡的权限的，不然删除不了
     *
     * @param taskId
     * @return
     */
    public static boolean cancel(long taskId) {
        Log.d(TAG, "cancel taskId:" + taskId);
        boolean cancel = false;
        HttpNormalTarget target = get().load(taskId);
        if (target != null) {
            target.cancel(true);
            cancel = true;
        }
        return cancel;
    }


    /**
     * 获取全部普通任务，不含群组任务
     *
     * @return
     */
    public static List<DownloadEntity> getAllTask() {
        return get().getTaskList();
    }

    /**
     * 分页拉取普通任务
     *
     * @param pageNum  页码
     * @param pageSize 每页多少条
     * @return
     */
    public static List<DownloadEntity> getTaskByPage(int pageNum, int pageSize) {
        return get().getTaskList(pageNum, pageSize);
    }

    /**
     * 添加监听
     *
     * @param listener
     */
    public static void addListener(OnDownloadListener listener, String tag) {
        getModule().addListener(listener, tag);
    }

    /**
     * 移除监听
     *
     * @param tag
     */
    public static void removeListener(String tag) {
        getModule().removeListener(tag);
    }


    /**
     * 创建下载地址
     *
     * @return
     */
    private static String getFileSaveDir() {
        if (fileSaveDir == null) {
            fileSaveDir = Files.getDownloadsDir();
        }
        return fileSaveDir;
    }

    /**
     * 配置下载参数
     *
     * @return
     */
    private static HttpOption getOption() {
        Log.d(TAG, "getOption");
        if (option == null) {
            option = new HttpOption()
                    .useServerFileName(false)
                    .setRequestType(RequestEnum.GET)
                    .addHeader("Accept-Encoding", "gzip, deflate");
        }
        return option;
    }

    /**
     * 任务是否存在
     *
     * @param url
     * @return
     */
    public static boolean isExists(String url) {
        Log.d(TAG, "isExists url:" + url);
        return Commons.isNotEmpty(url) && get().taskExists(url);
    }

    /**
     * 返回TaskId, -1 下载出错请重试
     *
     * @param url
     * @return
     */
    public static long startApk(String url, String tag) {
        String fileSavePath = StringsUtil.buffer(getFileSaveDir(), "/", Encrypts.encryptMD5ToString(url), ".apk");

        if (TextUtils.isEmpty(url) || sdcardIsFull()) {
            return -1;
        }
        Files.createDirs(getFileSaveDir());

        return get()
                .load(url)
                .option(getOption())
                //.ignoreFilePathOccupy()
                .setFilePath(fileSavePath)
                .setExtendField(tag)
                .resetState()
                .create();
    }


    public static boolean sdcardIsFull() {
        if (!SDCards.isSDCardMount() || lessThan20MB()) {
            Toast.makeText(ApplicationLoader.applicationContext, "容量不足，请清理缓存！", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }

    }

    public static boolean lessThan20MB() {
        if (SDCards.getAvailableExternalMemorySize(Environment.getExternalStorageDirectory().getAbsolutePath()) / 1024 / 1024 < 20) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 恢复下载
     *
     * @param taskId
     */
    public static boolean resume(long taskId, boolean justNow) {
        Log.d(TAG, "resume taskId:" + taskId);
        boolean isResume = false;
        HttpNormalTarget target = get().load(taskId);
        if (target != null && !target.isRunning()) {
            target.resume(justNow);
            isResume = true;
        }
        return isResume;
    }

    /**
     * 根据URL获取下载实体
     *
     * @param url
     * @return
     */
    public static DownloadEntity getDownloadEntity(String url) {
        Log.d(TAG, "getDownloadEntity url:" + url);
        return get().getFirstDownloadEntity(url);
    }

    /**
     * 根据URL获取下载实体
     *
     * @param taskId
     * @return
     */
    public static DownloadEntity getDownloadEntity(long taskId) {
        Log.d(TAG, "getDownloadEntity url:" + taskId);
        return get().getDownloadEntity(taskId);
    }

}
