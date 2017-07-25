package com.td.oldplay.http.callback;

import io.reactivex.disposables.Disposable;

public interface OnResultCallBack<T> {

    void onSuccess(T t);

    void onError(int code, String errorMsg);

    void onSubscribe(Disposable d);
}
