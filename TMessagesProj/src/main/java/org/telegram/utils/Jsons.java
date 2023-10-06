package org.telegram.utils;

import com.alibaba.android.jsonlube.JsonLube;
import com.alibaba.android.jsonlube.JsonLubeSerializerException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class Jsons {
    private static final Gson mGson = new Gson();

    private Jsons() {
        throw new UnsupportedOperationException("Can't create a new Instance");
    }

    /**
     * 生成JSONObject,T对像为JsonLube注解的类
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> JSONObject toJsonObj(T object) {
        JSONObject tObj = null;
        try {
            tObj = JsonLube.toJson(object);
        } catch (JsonLubeSerializerException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tObj;
    }

    /**
     * 生成json 转 object
     *
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> cls) {
        T bean = null;
        try {
            bean = mGson.fromJson(json, cls);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * Type转换
     *
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Type tClass) {
        T bean = null;
        try {
            bean = mGson.fromJson(json, tClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 生成json
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String toJson(T object) {
        String json = null;
        try {
            json = mGson.toJson(object);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
