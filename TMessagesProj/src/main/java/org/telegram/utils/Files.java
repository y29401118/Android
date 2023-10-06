package org.telegram.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import org.telegram.messenger.ApplicationLoader;

import java.io.File;

public class Files {

    public static final String DOWNLOADS_FILE_NAME = "wegram";

    public static boolean isExists(String path) {
        boolean exists = false;
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            exists = file.exists();
        }
        return exists;
    }

    /**
     * 保存下载文件地址
     *
     * @return
     */
    public static String getDownloadsDir() {
        return getDownloads(ApplicationLoader.applicationContext, DOWNLOADS_FILE_NAME).getAbsolutePath();
    }

    public static File getDownloads(Context context, String diskCacheName) {
        File dirs = null;
        if (hasSDCard()) {
            dirs = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            if (dirs == null || !dirs.exists() || !dirs.canWrite()) {
                dirs = context.getExternalCacheDir();
            }
        }

        if (dirs == null || !dirs.exists() || !dirs.canWrite()) {
            dirs = context.getCacheDir();
        }

        if (diskCacheName != null) {
            dirs = new File(dirs, diskCacheName);
        }
        return dirs;
    }

    public static boolean hasSDCard() {
        String state = "";
        try {
            state = Environment.getExternalStorageState();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean createDirs(String dirPath) {
        boolean success = false;
        if (!TextUtils.isEmpty(dirPath)) {
            //父级文件是否已存在，不存在就创建，除非是空就不创建
            File file = new File(dirPath);
            if (!file.exists() || file.isFile()) {
                success = file.mkdirs();
            }
        }
        return success;
    }

}
