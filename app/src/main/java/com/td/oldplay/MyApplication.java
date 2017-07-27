package com.td.oldplay;

import android.app.Application;

import com.mob.MobSDK;
import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;
import com.qiniu.pili.droid.streaming.StreamingEnv;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.network.HttpUtils;
import com.td.oldplay.utils.SharePreferenceUtil;
import com.td.oldplay.utils.ToastUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by smile_x on 2016/10/25.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    public SharePreferenceUtil mPreferenceUtil;

    public static MyApplication getInstance() { // 通过一个方法给外面提供实例
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IWXAPI api = WXAPIFactory.createWXAPI(this, MContants.WX_APP_ID);
        api.registerApp(MContants.WX_APP_ID);
        instance = this;
        // sp 工具类
        mPreferenceUtil = new SharePreferenceUtil(this, MContants.SHAREPREFERENCE_NAME);
        // toast 工具类
        ToastUtil.init(this);
        /**
         *  初始化网络请求的工具类
         */
        HttpManager.init(this);
        // 互动直播
        // StreamingEnv.init(this);
        RTCMediaStreamingManager.init(this);
        // 分享
        MobSDK.init(this, "1f36d53439891", "1fd1f03e6b07f7e292b71483a86d12fc");

        // 极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }


}
