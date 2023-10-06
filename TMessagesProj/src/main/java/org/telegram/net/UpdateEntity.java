package org.telegram.net;

import android.text.TextUtils;

import org.telegram.utils.Apps;

public class UpdateEntity {
    private static final int FORCE_UPDATE = 1;
    public int code;
    public String msg;
    public LatestBean data;


    public static class LatestBean {
        public String content;// "改动的内容",
        public String currentVersion;//1.2.3",
        public String downloadUrl;
        public int forceUpgrade;
    }

    public boolean canUpgrade() {
        if (data == null || TextUtils.isEmpty(data.downloadUrl)) {
            return false;
        }
        return true;
    }

    public boolean isForce() {
        if (data != null && data.forceUpgrade == FORCE_UPDATE) {
            return true;
        }
        return false;
    }
}
