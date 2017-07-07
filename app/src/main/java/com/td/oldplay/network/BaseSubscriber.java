package com.td.oldplay.network;

import android.content.Context;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

//  可以添加进度条之类的
public class BaseSubscriber<T> implements Subscriber<T> {
    private Context mContext;

    public BaseSubscriber() {
    }

    public BaseSubscriber(Context context) {
        mContext = context;
    }


    @Override
    public void onError(Throwable e) {
        ApiErrorHelper.handleCommonError(mContext, e);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(T t) {

    }
}