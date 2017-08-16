package com.td.oldplay;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.mob.MobSDK;

import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.network.HttpUtils;
import com.td.oldplay.ui.live.MessageEvent;
import com.td.oldplay.utils.PrefUtils;
import com.td.oldplay.utils.SharePreferenceUtil;
import com.td.oldplay.utils.ToastUtil;

import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.av.TIMAvManager;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by smile_x on 2016/10/25.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    public SharePreferenceUtil mPreferenceUtil;
    private  Context context;
    public static MyApplication getInstance() { // 通过一个方法给外面提供实例
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();


        IWXAPI api = WXAPIFactory.createWXAPI(this, MContants.WX_APP_ID);
        api.registerApp(MContants.WX_APP_ID);
        instance = this;
        // sp 工具类
        mPreferenceUtil = new SharePreferenceUtil(this, MContants.SHAREPREFERENCE_NAME);
        PrefUtils.init(this);
        // toast 工具类
        ToastUtil.init(this);
        /**
         *  初始化网络请求的工具类
         */
        HttpManager.init(this);


        // 分享
        MobSDK.init(this, "1f36d53439891", "1fd1f03e6b07f7e292b71483a86d12fc");

        // 极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        if(shouldInit()){
            initLive();
        }

    }

    private void initLive() {
        //初始化avsdk imsdk
        TIMManager.getInstance().disableBeaconReport();

        TIMManager.getInstance().setLogLevel(TIMLogLevel.INFO);

        // 初始化ILiveSDK
        ILiveSDK.getInstance().initSdk(this, MContants.SDK_APPID, MContants.ACCOUNT_TYPE);
        // 初始化直播模块
        ILVLiveConfig liveConfig = new ILVLiveConfig();
        liveConfig.setLiveMsgListener(com.td.oldplay.ui.live.MessageEvent.getInstance());
        ILVLiveManager.getInstance().init(liveConfig);

    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
