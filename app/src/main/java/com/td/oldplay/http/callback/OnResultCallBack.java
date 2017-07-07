package com.td.oldplay.http.callback;

public interface OnResultCallBack<T> {

    void onSuccess(T t);

    void onError(int code, String errorMsg);
}
