package com.td.oldplay.pay.weixin;

import android.content.ContentValues;
import android.util.Log;
import android.util.Xml;

import com.td.oldplay.pay.PayOrderId;
import com.td.oldplay.utils.MD5;

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

    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    public static String genPackageSign(ContentValues params) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Object> entry :
                params.valueSet()) {
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            sb.append('&');
        }

        sb.append("key=");
        sb.append(WePayConstants.API_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", packageSign);

        return packageSign;
    }

    public static String toXml(ContentValues params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (Map.Entry<String, Object> entry :
                params.valueSet()) {
            sb.append("<" + entry.getKey() + ">");


            sb.append(entry.getValue());
            sb.append("</" + entry.getKey() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", sb.toString());
        return sb.toString();
    }

    // 生成订单参数
    private String genProductArgs(PayOrderId mPayOrderInfo) {
        StringBuffer xml = new StringBuffer();
        try {
            String nonceStr = genNonceStr(); // 随机的一个字符串
            int orderMoney = 0;
            try {
                orderMoney = Integer.valueOf(mPayOrderInfo.totalfee.trim()) * 100;
            } catch (Exception e) {
                Log.e("===", "钱数转换异常" + e.toString());
            }

            String money = String.valueOf(orderMoney);
            Log.e("===", money);

            xml.append("</xml>");
            ContentValues contentValues = new ContentValues();


            contentValues.put("appid", WePayConstants.APP_ID);
            contentValues.put("body", "会员充值");
            contentValues.put("mch_id", WePayConstants.MCH_ID);
            contentValues.put("nonce_str", nonceStr);
            contentValues.put("notify_url",WePayConstants.wx_notify_url);
            contentValues.put("out_trade_no", mPayOrderInfo.out_trade_no);
            contentValues.put("spbill_create_ip", "127.0.0.1");
            //TODO 设置钱数
            contentValues.put("total_fee", money);
//			new BasicNameValuePair("total_fee", "1"));
            contentValues.put("trade_type", "APP");

            String sign = genPackageSign(contentValues);
            contentValues.put("sign", sign);

            String xmlstring = toXml(contentValues);

            return new String(xmlstring.toString().getBytes(), "ISO8859-1");

        } catch (Exception e) {
            // Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }
    }



}
