package com.td.oldplay.contants;

/**
 * @author John
 * @category 通过网络访问数据，后台接口统一定义在这里
 */
public class NetWorkApi {

    public final static String SERVER_IP = "http://47.90.16.112/evidencebased/qtuktuk/";




    /******************************* 一些常量信息 ******************************/
    /**
     * 请求成功
     */
    public final static String REQUEST_SUCCESS = "Success";
    /**
     * 网络断开
     */
    public final static String REQUEST_NETWORK_BREAKED = "您的网络已网络断开";
    /**
     * 请求失败
     */
    public final static String REQUEST_FAILED = "服务器连接失败,请稍后";
    /**
     * 数据解析失败
     */
    public final static String REQUEST_JSON_FAILED = "抱歉,数据解析失败,请稍后再试";
    /**
     * 请求返回值--成功
     */
    public final static int RETURN_SUCCESS = 1;
    /**
     * 请求返回值--失败
     */
    public static int RETURN_ERROR = 0;
    /**
     * 请求返回值--网络断开
     */
    public final static int RETURN_NETWORK_BREAKED = -1;
    /**
     * 请求返回值--json err
     */
    public final static int RETURN_JSON_FAILED = -2;


}
