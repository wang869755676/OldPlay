package com.td.oldplay.pay.weixin;

import android.content.ContentValues;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.td.oldplay.pay.PayOrderId;
import com.td.oldplay.utils.MD5;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by my on 2017/7/27.
 */

public class WeixPayUtils {


    /**
     *  使用微信支付  直接传入json字段
     * @param api
     * @param json
     * @throws JSONException
     */
    public static void pay(IWXAPI api, JSONObject json) throws JSONException {
        PayReq req = new PayReq();

        req.appId = json.getString("appid");
        req.partnerId = json.getString("partnerid");
        req.prepayId = json.getString("prepayid");
        req.nonceStr = json.getString("noncestr");
        req.timeStamp = json.getString("timestamp");
        req.packageValue = json.getString("package");
        req.sign = json.getString("sign");
        req.extData = "app data"; // optional
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }
    /**
     *  使用微信支付  直接传入json字段
     * @param api
     * @param info
     * @throws JSONException
     */
    public static void pay(IWXAPI api, WechatInfo info) throws JSONException {
        PayReq req = new PayReq();

        req.appId = info.appid;
        req.partnerId = info.partnerid;
        req.prepayId = info.prepayid;
        req.nonceStr = info.noncestr;
        req.timeStamp = info.sign;
        req.packageValue = info.packageName;
        req.sign = info.sign;
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

}
