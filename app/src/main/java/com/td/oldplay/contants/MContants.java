package com.td.oldplay.contants;

import android.os.Environment;

import com.td.oldplay.R;

import java.io.File;

public class MContants {

    public static final String SHAREPREFERENCE_NAME = "sp_应用名称";
    // 每一页的数量
    public static final int PAGENUM = 10;

    public static final int SUCCESS_CODE = 0;

    public static final String SHARE_URL = NetWorkApi.SERVER_IP + "common/share/share_index";

    public static final String SHARE_TITLE = "老年人";

    public static final String SHARE_CONTENT=  "老年人";

    public static final String IMAG_URL= R.mipmap.ic_launcher+"";
    /**
     * 友盟分享
     */
    public static final String UM_SHARE = "com.umeng.share";
    /**
     * 友盟登录
     */
    public static final String UM_LOGIN = "com.umeng.login";
    /**
     * 友盟分享 --官网
     */

    /**
     * QQ的App_id
     */
    public static final String QQ_App_Id = "1105535017";
    /**
     * QQ的App_key
     */
    public static final String QQ_App_Key = "WuDog0zUSLkWeKYW";
    /**
     * 微信分享APPid
     */
    public final static String WX_APP_ID = "wx4d9640a704df005c";
    public final static String WX_APP_KEY = "075cfc36ff594f61ed8ba3e786833e63";
    public final static String WX_APP_SIGNKEY = "2364c21b3cbe12cf9574227f9d0ed6f4";
    public final static String WX_PACKAGE_NAME = "com.tencent.mm";

    /**
     * 图片存放路径
     **/
    public static final String TAKE_PICTURE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "qlearn" + File.separator;
    /**
     * 剪裁图片存放路径
     **/
    public static final String TEMP_FILE_PATH = File.separator + "qlearn" + File.separator + "clip_temp.jpg";

    /**
     * 登录状态文件
     */
    public final static String UserLogin = "userLogin";
    /**
     * 用户名和密码文件
     */
    public final static String LoginPwd = "loginpwd";

    //public final static String USERINFO_CACHE_FILE = "UserInfos_cache_file";

    /**
     * Request Code
     **/
    public static final int LAUNCH_CAMERA_CODE = 0x102;
    public static final int LAUNCH_DCIM_CODE = 0x103;
    public static final int CROP_RESULT_CODE = 0x104;
    /**
     * Request Code
     **/
    public static final int SET_AUTHORIZATION_CODE = 0x105;
    public static final int SELECT_WECHAT_DCIM_CODE = 0x108;
    /**
     * 滚轮
     */
    public static int NumberPickerValue = 0;

    public static final String PICTURE_VIEW_BUTTON = "picture_add_button";

    public static final String REFRESH_FILETREE_ACTION = "refresh_file_tree";
}
