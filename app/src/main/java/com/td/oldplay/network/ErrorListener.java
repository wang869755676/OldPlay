package com.td.oldplay.network;

/**
 * 错误监听接口，code为http响应返回状态码。
 * Created by snowbean on 16-6-13.
 */
public interface ErrorListener {
    void onRemoteErrorHappened(int code);
}
