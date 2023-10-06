package org.telegram.utils;

import android.text.TextUtils;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Commons {

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmptyCombine(CharSequence... strs) {
        boolean isAllEmpty = false;
        if (strs != null && strs.length > 0) {
             isAllEmpty = true;
            for (CharSequence str : strs) {
                if (isNotEmpty(str)) {
                    isAllEmpty = false;
                    break;
                }
            }
        }
        return isAllEmpty;
    }

    public static boolean isNotEmptyCombine(CharSequence... strs) {
        boolean isAllEmpty = false;
        if (strs != null && strs.length > 0) {
            isAllEmpty = true;
            for (CharSequence str : strs) {
                if (isEmpty(str)) {
                    isAllEmpty = false;
                    break;
                }
            }
        }
        return isAllEmpty;
    }

    public static boolean isEmptyTrim(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotEmpty(Collection collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isSizeMore(Collection collection, int size) {
        return isNotEmpty(collection) && collection.size() >= size;
    }

    public static boolean isSizeLess(Collection collection, int size) {
        return !isNotEmpty(collection) || collection.size() < size;
    }

    public static boolean isNotEmpty(Map map) {
        return map != null && !map.isEmpty();
    }

    public static boolean isNotEmpty(CharSequence str) {
        return str != null && str.length() > 0;
    }


    public static String getHttpHost(String httpUrl) {
        String host = null;
        if (!TextUtils.isEmpty(httpUrl)) {
            String flag = "://";
            int index = httpUrl.indexOf(flag);
            if (index >= 0) {
                index = index + flag.length();
            } else {
                index = 0;
            }

            int signIndex = httpUrl.indexOf(File.separatorChar, index);
            if (signIndex > 0) {
                host = httpUrl.substring(index, signIndex);
            } else {
                host = httpUrl.substring(index);
            }
        }
        return host;
    }

    public static String getHttpPath(String httpUrl) {
        String path = null;
        if (!TextUtils.isEmpty(httpUrl)) {
            String tempStr = getHttpHost(httpUrl);
            int length = 0;
            if (!TextUtils.isEmpty(tempStr)) {
                length = tempStr.length() + 1;
            }
            if (httpUrl.length() > length) {
                int index = httpUrl.indexOf("?");
                index = (index > length) ? index - 1 : httpUrl.length();

                path = httpUrl.substring(length, index);
            }
        }
        return path;
    }

    public static <T> List<T> getDefault(List<T> list) {
        return list != null ? list : Collections.emptyList();
    }

}
