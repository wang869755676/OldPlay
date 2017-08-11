package com.td.oldplay.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.ui.MainActivity;
import com.td.oldplay.ui.RegisterScoreActivity;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.utils.BackgroundUtil;
import com.td.oldplay.utils.PrefUtils;
import com.tencent.mm.opensdk.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";
    private CustomDialog customDialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);


            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.e("===", "接收到推送下来的自定义消息:" + bundle.getString(JPushInterface.EXTRA_EXTRA));



            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {


            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                //Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
                Log.e("===", "打开推送的通知");
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                //	Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                //Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
                //Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            Log.e("===", e.getMessage());
        }

    }


    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Gson gson = new Gson();
        JpushInfo info = gson.fromJson(extras, JpushInfo.class);
        Intent i = null;
        if ("register".equals(info.action)) {  // 邀请注册
            PrefUtils.init(context);
            PrefUtils.putString(context, MContants.PRE_SCORE_KEY, info.content);
            if (BackgroundUtil.getRunningTask(context, context.getPackageName()) && MainActivity.isForeground) {
                i = new Intent(context, RegisterScoreActivity.class);
                i.putExtra("title", info.content);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } else {
                i = new Intent(context, MainActivity.class);
                i.putExtra("title", info.content);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        }

    }
}
