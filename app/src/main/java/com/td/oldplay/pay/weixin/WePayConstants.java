package com.td.oldplay.pay.weixin;


import com.td.oldplay.http.api.NetWorkAPI;

public class WePayConstants {
    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
//  public static final String APP_ID = "wxbd3bdf47c878ab37";
    public static final String APP_ID = "wxa6f2cfd1686465a2";
    //商户号
//  public static final String MCH_ID = "1319412001";
    public static final String MCH_ID = "1370254102";
    //TODO API密钥，在商户平台设置
//  public static final String API_KEY="Sx53Cbs0fW4bF7OpNrHcNvXpdsWLuE6n";
    public static final String API_KEY = "397828722C0D94F4EB3F914829DF95AD";
    public static final String wx_notify_url = NetWorkAPI.BASE_URL + "common/payment/weixin_mobVipNotify";
}
