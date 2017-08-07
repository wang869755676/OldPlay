package com.td.oldplay.wxapi;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.utils.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends BaseFragmentActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    private EventMessage message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, MContants.WX_APP_ID);

        api.handleIntent(getIntent(), this);
        message = new EventMessage("WX");
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            message.WxPayCode = resp.errCode;
            String msg = "";
            switch (code) {
                case 0:
                    msg = "支付成功！";

                    break;
                case -1:
                    message.WxPayCode = 0;
                    msg = "支付失败！";
                    break;
                case -2:
                    msg = "您取消了支付！";
                    break;

                default:
                    msg = "支付失败！";
                    break;

            }
            EventBus.getDefault().post(message);


        }
    }
}