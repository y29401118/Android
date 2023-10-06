package org.telegram.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.app.ScenesConfig;
import org.telegram.messenger.ApplicationLoader;

public class SharepeferenceUtil {

    public final static String IP_KEY = "ip_key";
    public static void saveIp(String ip) {
        SharedPreferences.Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", Context.MODE_PRIVATE).edit();
        editor.putString(IP_KEY,ip).commit();
    }

    public static String getIp() {
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", Context.MODE_PRIVATE);
        return sharedPreferences.getString(IP_KEY, ScenesConfig.getSocketHost());
    }

}
