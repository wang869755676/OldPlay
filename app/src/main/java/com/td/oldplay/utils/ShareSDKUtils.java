package com.td.oldplay.utils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;


/**
 * Created by my on 2017/7/6.
 */

public class ShareSDKUtils {

    /**
     *  根据指定的平台进行分享
     * @param platformType
     * @param title
     * @param text
     * @param url
     * @param imageUrl
     * @param listener
     */
    public static void share(String platformType, String title, String text, String url, String imageUrl, PlatformActionListener listener) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url);
        sp.setText(text);
        sp.setUrl(url);
        sp.setImageUrl(imageUrl);
        Platform platform = ShareSDK.getPlatform(platformType);
        platform.setPlatformActionListener(listener);
        platform.share(sp);
    }

    /**
     *  根据指定的平台登录
     * @param platformType
     * @param listener
     */
    public static void login(String platformType, PlatformActionListener listener) {
        Platform plaform= ShareSDK.getPlatform(platformType);
        plaform.removeAccount(true);
        plaform.setPlatformActionListener(listener);
        plaform.showUser(null);
    }

    /**
     *  要功能
     * @param platformType
     * @param listener
     */
    public static void loginAuthorize(String platformType, PlatformActionListener listener) {
        Platform plaform= ShareSDK.getPlatform(platformType);
        plaform.removeAccount(true);
        plaform.setPlatformActionListener(listener);
        plaform.authorize();
    }


    /**
     *  退出第三方平摊的登录
     * @param platformType
     */
    public static void loginOut(String platformType) {
        Platform plaform= ShareSDK.getPlatform(platformType);
        if(plaform.isAuthValid()){
            plaform.removeAccount(true);
        }

    }

}
