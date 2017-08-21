package com.td.oldplay.contants;

import android.os.Environment;

import com.td.oldplay.R;

import java.io.File;

public class MContants {

    public static final String SHAREPREFERENCE_NAME = "oldplay";
    public static final String  PRE_SCORE_KEY = "score";
    // 每一页的数量
    public static final int PAGENUM = 10;

    public static final int SUCCESS_CODE = 0;

    public static final String SHARE_URL = NetWorkApi.SERVER_IP + "common/share/share_index";

    public static final String SHARE_TITLE = "老顽童";

    public static final String SHARE_CONTENT=  "老顽童";

    public static final String IMAG_URL= R.mipmap.ic_launcher+"";

    /**
     * 微信
     */
    public final static String WX_APP_ID = "wx4d9640a704df005c";
    public final static String WX_APP_KEY = "075cfc36ff594f61ed8ba3e786833e63";
    public final static String WX_APP_SIGNKEY = "2364c21b3cbe12cf9574227f9d0ed6f4";
    public final static String WX_PACKAGE_NAME = "com.tencent.mm";


    /**
     * 剪裁图片存放路径
     **/
    public static final String TEMP_FILE_PATH = File.separator + "qlearn" + File.separator + "clip_temp.jpg";


    /**
     * Request Code
     **/
    public static final int LAUNCH_CAMERA_CODE = 0x102;
    public static final int LAUNCH_DCIM_CODE = 0x103;


    /**
     *  直播相关的配置
     */
    public static final int SDK_APPID = 1400027849;
    public static final int ACCOUNT_TYPE = 11656;


    public static final String HD_ROLE = "HD";
    public static final String SD_ROLE = "SD";
    public static final String LD_ROLE = "LD";

    public static final int AVIMCMD_MULTI = 0x800;             // 多人互动消息类型

    public static final int AVIMCMD_MUlTI_HOST_INVITE = AVIMCMD_MULTI + 1;         // 邀请直播互动,
    public static final int AVIMCMD_MULTI_CANCEL_INTERACT = AVIMCMD_MUlTI_HOST_INVITE + 1;       // 断开互动，
    public static final int AVIMCMD_MUlTI_JOIN = AVIMCMD_MULTI_CANCEL_INTERACT + 1;       // 同意互动，
    public static final int AVIMCMD_MUlTI_REFUSE = AVIMCMD_MUlTI_JOIN + 1;      // 拒绝互动，
    public static final int AVIMCMD_MUlTI_LINKING = AVIMCMD_MUlTI_REFUSE+ 1;      // 主播正在连麦，
    public static final int AVIMCMD_MUlTI_NOSTARTLINK = AVIMCMD_MUlTI_LINKING+ 1;      // 主播为开启连麦，


    public static final int AVIMCMD_TEXT = -1;         // 普通的聊天消息

    public static final int AVIMCMD_NONE = AVIMCMD_TEXT + 1;               // 无事件

    // 以下事件为TCAdapter内部处理的通用事件
    public static final int AVIMCMD_ENTERLIVE = AVIMCMD_NONE + 1;          // 观众加入直播,
    public static final int AVIMCMD_EXITLIVE = AVIMCMD_ENTERLIVE + 1;         // 主播退出直播,




    public static final int AVIMCMD_PRAISE = AVIMCMD_EXITLIVE + 1;           // 点赞消息,
    public static final int AVIMCMD_HOST_LEAVE = AVIMCMD_PRAISE + 1;         // 主播离开,
    public static final int AVIMCMD_HOST_BACK = AVIMCMD_HOST_LEAVE + 1;      // 主播回来,

    public static final int AVIMCMD_LIVING = AVIMCMD_HOST_BACK  + 1;      // 主播创建房间

    public static final int AUDICE_EXITLIVE = AVIMCMD_LIVING + 1;         // 观众退出直播,

    public static final String HOST_ROLE = "LiveMaster";
    public static final String VIDEO_MEMBER_ROLE = "LiveGuest";
    public static final String NORMAL_MEMBER_ROLE = "Guest";
}
