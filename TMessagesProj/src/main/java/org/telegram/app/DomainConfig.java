package org.telegram.app;


public class DomainConfig {


    public static String[] getDomain(int envType) {
        if (envType == ScenesConfig.EnvType.DEVELOP) {
            return getDevDomain();
        } else if (envType == ScenesConfig.EnvType.BETA) {
            return getBateDomain();
        } else if (envType == ScenesConfig.EnvType.SAFEST) {
            return getSafestDomain();
        } else {
            return getProductDomain();
        }
    }


    /**
     * 开发环境
     *
     * @return
     */
    private static String[] getDevDomain() {
        return new String[]{
                "103.87.241.235",
                "103.87.241.236",
                "103.87.241.237",
        };
    }

    /**
     * 测试环境
     *
     * @return
     */
    private static String[] getBateDomain() {
        return new String[]{
                "103.87.241.235",
                "103.87.241.236",
                "103.87.241.237",
        };

    }


    /**
     * 加密环境
     *
     * @return
     */
    private static String[] getSafestDomain() {
        return new String[]{
                "103.87.241.235",
                "103.87.241.236",
                "103.87.241.237",
        };

    }


    /**
     * 正式环境
     *
     * @return
     */
    private static String[] getProductDomain() {
        return new String[]{
                "103.87.241.235",
                "103.87.241.236",
                "103.87.241.237",
        };

    }


}
