package com.td.oldplay.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.td.oldplay.MyApplication;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.utils.SharePreferenceUtil;


public abstract class BaseFragmentActivity extends FragmentActivity {

    public Context mContext;   //吐司的上下文
    protected Context AContext;
    protected String userId;
    private KProgressHUD hud;
    protected SharePreferenceUtil spUilts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AContext = getApplicationContext();
        userId= MyApplication.getInstance().mPreferenceUtil.getUserId();
        spUilts= MyApplication.getInstance().mPreferenceUtil;
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
    }

    public void showLoading() {
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

    }

    public void showLoading(String msg) {
        hud = KProgressHUD.create(mContext)
                .setLabel(msg)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

    }

    public void showCustomLoading(View v) {
        hud = KProgressHUD.create(mContext)
                .setCustomView(v).show();

    }

    public void hideLoading() {
        if (hud != null) {
            hud.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.destroyRequest(AContext);
    }
}
