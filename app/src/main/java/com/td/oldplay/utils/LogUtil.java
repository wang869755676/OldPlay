package com.td.oldplay.utils;

import android.util.Log;

public class LogUtil {
	
	 /** 
     * isDebug :是用来控制，是否打印日志 
     */  
    private static final boolean isDeBug = true;
    /** 
     * verbose等级的日志输出 
     *  
     * @param tag 
     *            日志标识 
     * @param msg 
     *            要输出的内容 
     * @return void 返回类型 
     * @throws 
     */  
    public static void v(String tag, String msg) {
        // 是否开启日志输出  
        if (isDeBug) {  
            Log.v(tag, msg);
        }  
        // 是否将日志写入文件  
    }  
  
    /** 
     * debug等级的日志输出 
     *  
     * @param tag 
     *            标识 
     * @param msg 
     *            内容 
     * @return void 返回类型 
     * @throws 
     */  
    public static void d(String tag, String msg) {
        if (isDeBug) {  
            Log.d(tag, msg);
        }  
    }  
  
    /** 
     * info等级的日志输出 
     *  
     * @param  tag 标识 
     * @param  msg 内容 
     * @return void 返回类型 
     * @throws 
     */  
    public static void i(String tag, String msg) {
        if (isDeBug) {  
            Log.i(tag, msg);
        }  
    }  
  
    /** 
     * warn等级的日志输出 
     *  
     * @param tag 标识 
     * @param msg 内容 
     * @return void 返回类型 
     * @throws 
     */  
    public static void w(String tag, String msg) {
        if (isDeBug) {  
            Log.w(tag, msg);
        }  
    }  
  
    /** 
     * error等级的日志输出 
     *  
     * @param  tag 标识 
     * @param  msg 内容 
     * @return void 返回类型 
     */  
    public static void e(String msg) {
        if (isDeBug) {  
            Log.e("Okgo", msg);
        }  
    }  
  

}
