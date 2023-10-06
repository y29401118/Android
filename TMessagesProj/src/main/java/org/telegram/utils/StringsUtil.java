package org.telegram.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

/**
 * @author
 */
public class StringsUtil {

    public static int toInt(String content, int defaultValue) {
        try {
            return Integer.parseInt(content);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static float toFloat(String content, float defaultValue) {
        try {
            return Float.parseFloat(content);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static double toDouble(String content, float defaultValue) {
        try {
            return Double.parseDouble(content);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static long toLong(String content, long defaultValue) {
        try {
            return Long.parseLong(content);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static boolean toBoolean(String content, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(content);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * 字符串拼接,线程安全
     */
    public static String buffer(String... array) {
        StringBuffer s = new StringBuffer();
        for (String str : array) {
            s.append(str);
        }
        return s.toString();
    }

    /**
     * 字符串拼接,线程不安全,效率高
     */
    public static String builder(String... array) {
        StringBuilder s = new StringBuilder();
        for (String str : array) {
            s.append(str);
        }
        return s.toString();
    }

    /**
     * 字符串拼接,线程安全
     */
    public static String buffer(Object... array) {
        StringBuffer s = new StringBuffer();
        for (Object str : array) {
            s.append(str);
        }
        return s.toString();
    }

    /**
     * 字符串拼接,线程不安全,效率高
     */
    public static String builder(Object... array) {
        StringBuilder s = new StringBuilder();
        for (Object str : array) {
            s.append(str);
        }
        return s.toString();
    }


    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        boolean isEquals = false;
        if (a != null) {
            isEquals = a.equalsIgnoreCase(b);
        } else if (b == null) {
            isEquals = true;
        }
        return isEquals;
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) {
            return s;
        }
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }


    public static boolean isEmptyTrim(String str) {
        return str == null || str.trim().length() == 0;
    }


    public static String getDefValue(int value) {
        return String.valueOf(Math.max(value, 0));
    }

    public static String getDefValue(long value) {
        return String.valueOf(Math.max(value, 0L));
    }

    /**
     * 格式化地址
     *
     * @param url
     * @param params
     * @return
     */
    public static String formatStr(String url, Object... params) {
        String tempUrl = url;
        if (!TextUtils.isEmpty(url) && params != null && params.length > 0) {
            tempUrl = String.format(url, params);
        }
        return tempUrl;
    }

    /**
     * 使用指定符号替换指定范围内的字符：139****2145
     *
     * @param orign
     * @param sign
     * @param from
     * @param to
     * @return
     */
    public static String replaceRangeWidthSign(String orign, char sign, int from, int to) {
        if (isEmpty(orign) || isEmpty(String.valueOf(sign))) {
            return orign;
        } else {
            int length = orign.length();
            if (to <= length && from < to && to > 0) {
                int tempFrom = Math.max(from, 0);
                StringBuilder stringBuilder = new StringBuilder(orign);
                for (int i = from; i < to; i++) {
                    stringBuilder.setCharAt(i, sign);
                }
                return stringBuilder.toString();
            } else {
                return orign;
            }
        }
    }

    /**
     * 计算点赞、评论、数量
     *
     * @param hotCount
     * @return
     */
    public static String hotCount(int hotCount) {
        final int tenThousand = 10000;
        final int hundredMillion = tenThousand * tenThousand;
        String result;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        if (hotCount >= hundredMillion) {
            result = decimalFormat.format(hotCount / (double) hundredMillion) + "亿";
        } else if (hotCount >= tenThousand) {
            result = decimalFormat.format(hotCount / (double) tenThousand) + "万";
        } else {
            result = String.valueOf(hotCount <= 0 ? "0" : hotCount);
        }
        return result;
    }


    /**
     * 生成随机数字
     *
     * @param length 生成随机数的长度
     * @return
     */
    public static String randomNumStr(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val.append(random.nextInt(10));
        }
        return val.toString();
    }

    public static CharSequence getFixedChars(CharSequence oStr, int num) {
        StringBuilder stringBuilder = new StringBuilder(oStr);
        int maxLength = num * 2;
        int length = oStr.length();
        if (length > maxLength) {
            length = maxLength;
            stringBuilder.setLength(maxLength);
        }
        int byteLength = 0;
        try {
            byte[] bytes = stringBuilder.toString().getBytes("GBK");
            byteLength = bytes.length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (byteLength > maxLength) {
            int difLength = byteLength - maxLength + 1;
            stringBuilder.setLength(length - difLength / 2);
            if (stringBuilder.length() > num) {
                CharSequence temp = getFixedChars(stringBuilder.toString(), num);
                stringBuilder.setLength(0);
                stringBuilder.append(temp);
            }
        }
        return stringBuilder.toString().trim();
    }

    /**
     * 数组拼装
     *
     * @param strTwiceArray
     * @return
     */
    public static String[] contactArray(String[]... strTwiceArray) {
        String[] totalArray = null;
        if (strTwiceArray != null && strTwiceArray.length > 0) {
            int arrayLength = strTwiceArray.length;
            totalArray = strTwiceArray[0];
            for (int i = 1; i < arrayLength; i++) {
                int strLen1 = totalArray.length;
                String[] array2 = strTwiceArray[i];
                int strLen2 = array2.length;
                totalArray = Arrays.copyOf(totalArray, strLen1 + strLen2);
                System.arraycopy(array2, 0, totalArray, strLen1, strLen2);
            }
        }
        return totalArray;
    }

    public static boolean equals(String str, String str2) {
        boolean isEquals = false;
        if (str == null && str2 == null) {
            isEquals = true;
        } else if (str != null && str.equals(str2)) {
            isEquals = true;
        } else if (str2 != null && str2.equals(str)) {
            isEquals = true;
        }
        return isEquals;
    }

    public static String trim(String str) {
        if (str != null) {
            return str.trim();
        } else {
            return str;
        }
    }
}
