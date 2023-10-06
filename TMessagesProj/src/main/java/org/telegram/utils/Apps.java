package org.telegram.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.android.exoplayer2.util.Log;

import org.telegram.messenger.ApplicationLoader;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

public class Apps {
    private static final String TAG = "Apps";

    public static String getVersionName() {
        String versionName = null;
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null) {
            versionName = getPackageInfo().versionName;
        }
        return versionName;
    }

    public static String getPackgeName() {
        return ApplicationLoader.applicationContext.getPackageName();
    }

    public static boolean compareVersion(String version){
        if (TextUtils.isEmpty(version)) {
           return false;
        }
        String clientVersion[] = getVersionName().split("\\.");
        String serverVersion[] = version.split("\\.");
        if(clientVersion.length != serverVersion.length){
            return false;
        }
        for(int i = 0;i<clientVersion.length;i++){
            if(Integer.parseInt(clientVersion[i])<Integer.parseInt(serverVersion[i])){
                return true;
            }

        }
        return false;
    }


    public static PackageInfo getPackageInfo() {
        PackageInfo info;
        try {
            info = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            info = null;
        } catch (Exception e) {
            e.printStackTrace();
            info = null;
        }
        return info;
    }


    /**
     * 判断App版本
     *
     * @param localVersion
     * @param newVersion
     * @return
     */
    public static boolean isUpgradeApp(String localVersion, String newVersion) {
        if (TextUtils.isEmpty(newVersion)) {
            return false;
        }
        String[] localVersionArray = localVersion.split("\\.", -1);
        String[] newVersionArray = newVersion.split("\\.", -1);
        if (localVersionArray.length < newVersionArray.length) {
            int cha = newVersionArray.length - localVersionArray.length;
            for (int i = 0; i < cha; i++) {
                localVersion = StringsUtil.buffer(localVersion, ".0");
            }
            localVersionArray = localVersion.split("\\.", -1);
        }
        try {
            for (int i = 0; i < newVersionArray.length; i++) {
                int temp = Integer.parseInt(newVersionArray[i]);
                int compar = Integer.parseInt(localVersionArray[i]);
                if (temp > compar) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 安装应用
     *
     * @param file
     * @param authority
     * @return
     */
    public static void installApp(File file, String authority, boolean isNewTask) {
        if (file == null) {
            Log.d(TAG, "installApp file == null");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = null;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file);
        } else {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                data = FileProvider.getUriForFile(ApplicationLoader.applicationContext, authority, file);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        if (data == null) {
            Toast.makeText(ApplicationLoader.applicationContext, "安装app失败", Toast.LENGTH_SHORT).show();
            return;
        }

        intent.setDataAndType(data, type);
        ApplicationLoader.applicationContext.startActivity(isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent);
    }


    /**
     * 检查写入权限
     *
     * @param activity
     */
    public static boolean checkWritePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23 && activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
            return false;
        }
        return true;
    }


    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getDeviceId(Context ctx) {
        String uuid = UUID.randomUUID().toString();
        String androidId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "getDeviceId androidId:" + androidId);
        if (!"9774d56d682e549c".equals(androidId) && !TextUtils.isEmpty(androidId)) {
            uuid = UUID.nameUUIDFromBytes(androidId.getBytes(Charset.defaultCharset())).toString();
            Log.d(TAG, "getDeviceId androidId uuid:" + uuid);
        } else if (ctx.checkCallingOrSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            String deviceId = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                deviceId = ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getImei();
                Log.d(TAG, "getDeviceId imei:" + deviceId);
            }

            if (deviceId != null) {
                uuid = UUID.nameUUIDFromBytes(deviceId.getBytes(Charset.defaultCharset())).toString();
                Log.d(TAG, "getDeviceId imei uuid:" + uuid);
            }
        }
        Log.d(TAG, "getDeviceId :" + androidId);
        return uuid.replace("-", "");
    }


    /**
     * 判断是否为主进程
     *
     * @param app
     * @return
     */
    public static boolean isMainProcess(Application app) {
        if(getProcessName(app.getApplicationContext()) == null) {
            return false;
        }

        boolean isMainProcess = app.getApplicationContext().getPackageName().equals(getProcessName(app.getApplicationContext()));
        return isMainProcess;
    }


    /**
     * 获取当前进程名字
     *
     * @param cxt
     * @return
     */
    public static String getProcessName(Context cxt) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null || runningApps.isEmpty()) {
            return null;
        }

        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }


}
