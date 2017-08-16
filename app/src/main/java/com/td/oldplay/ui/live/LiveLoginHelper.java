package com.td.oldplay.ui.live;

import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;

/**
 * Created by my on 2017/8/15.
 */

public class LiveLoginHelper {

    /**
     *   登录到-------> 腾讯云的
     * @param id
     * @param sig
     */
    public static void iLiveLogin(String id, String sig,ILiveCallBack iLiveCallBack) {
        //登录
        ILiveLoginManager.getInstance().iLiveLogin(id, sig,iLiveCallBack);
    }

    /**
     * 退出imsdk <p> 退出成功会调用退出AVSDK
     */
    public static  void iLiveLogout(ILiveCallBack iLiveCallBack) {
        //TODO 新方式登出ILiveSDK
        ILiveLoginManager.getInstance().iLiveLogout(iLiveCallBack);
    }
}
