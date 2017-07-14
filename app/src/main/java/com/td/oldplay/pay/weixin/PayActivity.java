package com.td.oldplay.pay.weixin;


import org.json.JSONException;
import org.json.JSONObject;

import com.td.oldplay.contants.MContants;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.os.Bundle;


public class PayActivity extends Activity {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, MContants.WX_APP_ID);

        JSONObject json = null;
        try {
            json = new JSONObject("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (null != json && !json.has("retcode")) {
            PayReq req = new PayReq();

			/*req.appId			= json.getString("appid");
			req.partnerId		= json.getString("partnerid");
			req.prepayId		= json.getString("prepayid");
			req.nonceStr		= json.getString("noncestr");
			req.timeStamp		= json.getString("timestamp");
			req.packageValue	= json.getString("package");
			req.sign			= json.getString("sign");
			req.extData			= "app data"; // optional
			Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
			// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
			api.sendReq(req);*/

        }
    }

}
