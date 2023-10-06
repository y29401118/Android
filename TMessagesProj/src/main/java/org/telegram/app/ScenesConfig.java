package org.telegram.app;

import org.telegram.messenger.BuildConfig;

public class ScenesConfig {

    /**
     * 环境配置：影响到使用的key,服务器地址,某些功能项配置等
     */
    private static int envType = BuildConfig.EnvType;

    /**
     *  获取socket IP地址
     * @return
     */
    public static String getSocketHost() {
        return DomainConfig.getDomain(envType)[0];
    }

    /**
     * 获取网络请求地址
     * @return
     */
    public static String getHttpHost(){
        return DomainConfig.getDomain(envType)[1];
    }


    public static int getEnvType() {
        return envType;
    }


    public static void setEnvType(int envType) {
        ScenesConfig.envType = envType;
    }

    /**
     * 环境类型
     */
    public interface EnvType {
        int DEVELOP = 4;//开发环境
        int BETA = 3;//测试环境
        int SAFEST = 2;//加密环境
        int RELEASE = 1;//正式环境
    }

}
