package org.telegram.net;

import android.net.Uri;

import com.google.android.exoplayer2.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.telegram.entity.CrashResult;
import org.telegram.entity.IpModel;
import org.telegram.messenger.AndroidUtilities;

import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.AlertsCreator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {


    /**
     * 检查更新请求
     */
    public static void sendRequestCheckAppUpdate(NetCallBack<UpdateEntity> netCallBack) {
        //开启线程，发送请求
        new Thread(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                String uri = Uri.parse("https://lskjdwg.oss-cn-hongkong.aliyuncs.com/Android.json")
                        .buildUpon()
                        .build().toString();
                URL url = new URL(uri);
                connection = (HttpURLConnection) url.openConnection();
                //设置请求方法
                connection.setRequestMethod("GET");
                //设置连接超时时间（毫秒）
                connection.setConnectTimeout(8000);
                //设置读取超时时间（毫秒）
                connection.setReadTimeout(8000);
//                connection.addRequestProperty("clientType", "android");
//                connection.addRequestProperty("deviceId", Apps.getDeviceId(ApplicationLoader.applicationContext));
//                connection.addRequestProperty("version", Apps.getVersionName());
                //返回输入流
                InputStream in = connection.getInputStream();
                //读取输入流
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                Log.e("HttpRequest", "请求更新接口成功：" + result.toString());
                UpdateEntity entity = getUpdateDate(result.toString());
                if (netCallBack != null) {
                    netCallBack.onResult(true, entity);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                if (netCallBack != null) {
                    Log.e("HttpRequest", "请求更新接口失败");
                    netCallBack.onResult(false, null);
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
                if (netCallBack != null) {
                    Log.e("HttpRequest", "请求更新接口失败");
                    netCallBack.onResult(false, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (netCallBack != null) {
                    Log.e("HttpRequest", "请求更新接口失败");
                    netCallBack.onResult(false, null);
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {//关闭连接
                    connection.disconnect();
                }
            }
        }).start();
    }

    private static UpdateEntity getUpdateDate(String result) {
        UpdateEntity updateEntity = null;
        try {
            updateEntity = new Gson().fromJson(result, UpdateEntity.class);
        } catch (Exception e) {

        }
        return updateEntity;

    }

    /**
     * 获取服务端接口列表
     *
     * @param netCallBack
     */
    public static void getServerAddress(NetCallBack<List<IpModel>> netCallBack) {
        //开启线程，发送请求
        new Thread(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                String uri = Uri.parse("https://yxk22sj.oss-cn-hongkong.aliyuncs.com/yxl.json")
                        .buildUpon()
                        .build().toString();
                URL url = new URL(uri);
                connection = (HttpURLConnection) url.openConnection();
                //设置请求方法
                connection.setRequestMethod("GET");
                //设置连接超时时间（毫秒）
                connection.setConnectTimeout(8000);
                //设置读取超时时间（毫秒）
                connection.setReadTimeout(8000);
//                connection.addRequestProperty("clientType", "android");
//                connection.addRequestProperty("deviceId", Apps.getDeviceId(ApplicationLoader.applicationContext));
//                connection.addRequestProperty("version", Apps.getVersionName());
                //返回输入流
                InputStream in = connection.getInputStream();
                //读取输入流
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                Log.e("HttpRequest", "请求更新接口成功：" + result.toString());
                List<IpModel> retList = new ArrayList<>();
                try {
                    retList = new Gson().fromJson(result.toString(), new TypeToken<List<IpModel>>() {
                    }.getType());
                } catch (Exception e) {

                }
                if (netCallBack != null) {
                    netCallBack.onResult(true, retList);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                if (netCallBack != null) {
                    netCallBack.onResult(false, null);
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
                if (netCallBack != null) {
                    netCallBack.onResult(false, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (netCallBack != null) {
                    netCallBack.onResult(false, null);
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {//关闭连接
                    connection.disconnect();
                }
            }
        }).start();
    }

    public static void getUserData(String body, BaseFragment context) {
        Log.e("test","body:"+body);
        new Thread() {
            @Override
            public void run() {
                String result = "";


                String authHost = "http://103.85.254.79:6091/wgCallbackListener";

                try {
                    URL url = new URL(authHost);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // 设置请求方式
                    connection.setRequestMethod("POST");
                    // 设置是否向HttpURLConnection输出
                    connection.setDoOutput(true);
                    // 设置是否从httpUrlConnection读入
                    connection.setDoInput(true);
                    // 设置是否使用缓存
                    connection.setUseCaches(false);
                    //设置参数类型是json格式
                    connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    connection.connect();


                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    writer.write(body);
                    writer.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //定义 BufferedReader输入流来读取URL的响应
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while ((line = in.readLine()) != null) {
                            result += line;
                        }
                    }
                    CrashResult crashResult = new Gson().fromJson(result, CrashResult.class);
                    AndroidUtilities.runOnUIThread(() -> AlertsCreator.showSimpleToast(context, crashResult.data));
                    Log.e("test","result:"+result);

                } catch (Exception ex) {
                    Log.e("test","result:"+Log.getThrowableString(ex));
                }

            }
        }.start();

    }


}
