package com.td.oldplay.pay.weixin;

import com.google.gson.annotations.SerializedName;

/**
 * Created by my on 2017/8/14.
 */

public class WechatInfo {

    public String appid;
    public String partnerid;
    public String prepayid;
    public String noncestr;
    public String timestamp;
    @SerializedName("package")
    public String packageName;
    public String sign;
}
