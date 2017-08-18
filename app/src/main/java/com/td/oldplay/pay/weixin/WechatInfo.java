package com.td.oldplay.pay.weixin;

import com.google.gson.annotations.SerializedName;

/**
 * Created by my on 2017/8/14.
 */

public class WechatInfo {


    /**
     * msg : ok
     * appid : wx2421b1c4370ec43b
     * timestamp : 1503028513
     * noncestr : 1155122429
     * partnerid : XXXXXXXXX
     * prepayid : qwertyuio
     * package : Sign=WXPay
     * sign : C9CF4CB425000112492E23880BB68912
     */

    public String msg;
    public String appid;
    public String timestamp;
    public String noncestr;
    public String partnerid;
    public String prepayid;
    @SerializedName("package")
    public String packageX;
    public String sign;


}
