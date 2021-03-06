package com.td.oldplay.http.subscriber;


import android.util.Log;

import com.google.gson.stream.MalformedJsonException;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.exception.ApiException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;


public class HttpSubscriber<T> implements Observer<T> {
    private OnResultCallBack mOnResultListener;
    private Disposable mDisposable;

    public HttpSubscriber(OnResultCallBack listener) {
        this.mOnResultListener = listener;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        if (mOnResultListener != null) {
            mOnResultListener.onSubscribe(d);
        }
    }

    @Override
    public void onNext(T t) {
        if (mOnResultListener != null) {
            mOnResultListener.onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof CompositeException) {
            CompositeException compositeE = (CompositeException) e;
            for (Throwable throwable : compositeE.getExceptions()) {
                if (throwable instanceof SocketTimeoutException) {
                    mOnResultListener.onError(ApiException.Code_TimeOut, ApiException.SOCKET_TIMEOUT_EXCEPTION);
                } else if (throwable instanceof ConnectException) {
                    mOnResultListener.onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
                } else if (throwable instanceof UnknownHostException) {
                    mOnResultListener.onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
                } else if (throwable instanceof MalformedJsonException) {
                    mOnResultListener.onError(ApiException.Code_MalformedJson, ApiException.MALFORMED_JSON_EXCEPTION);
                }
            }
        } else if (e instanceof SocketTimeoutException) {
            Log.e("===",e.getMessage());
            mOnResultListener.onError(ApiException.Code_TimeOut, ApiException.SOCKET_TIMEOUT_EXCEPTION);
        } else if (e instanceof ApiException) {

            String msg = e.getMessage();
            int code;
            if (msg.contains("#")) {
                code = Integer.parseInt(msg.split("#")[0]);
                if (code == ApiException.Code_NULL_DATA) {
                    onNext(null);
                } else {
                    mOnResultListener.onError(code, msg.split("#")[1]);
                }

            } else {
                code = ApiException.Code_Default;
                mOnResultListener.onError(code, msg);
            }
        } else {
            mOnResultListener.onError(1001, "网络请求失败");
        }
    }

    @Override
    public void onComplete() {

    }

    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
