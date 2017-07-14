package com.td.oldplay.ui.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.td.oldplay.R;
import com.td.oldplay.utils.ShareSDKUtils;
import com.td.oldplay.utils.ToastUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by my on 2017/7/14.
 */

public class SharePopupWindow extends PopupWindow implements View.OnClickListener, PlatformActionListener {

    private String title;
    private String cocntent;
    private String url;
    private String imageUrl;

    private Animation animation_in;
    private Animation animation_out;

    private LinearLayout ll;

    public SharePopupWindow(Context context, String title, String cocntent, String url, String imageUrl) {
        super(context);
        this.title = title;
        this.cocntent = cocntent;
        this.url = url;
        this.imageUrl = imageUrl;
        View view = LayoutInflater.from(context).inflate(R.layout.pop_share, null);
        setContentView(view);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        setOutsideTouchable(true);
        setFocusable(true);
        view.findViewById(R.id.pop_share_root).setOnClickListener(this);
        view.findViewById(R.id.iv_wx).setOnClickListener(this);
        view.findViewById(R.id.iv_pyq).setOnClickListener(this);
        view.findViewById(R.id.btn_canel).setOnClickListener(this);
        ll = (LinearLayout) view.findViewById(R.id.share_ll);
        animation_in = AnimationUtils.loadAnimation(context, R.anim.menu_in);
        animation_out = AnimationUtils.loadAnimation(context, R.anim.menu_out);
        animation_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_canel:
            case R.id.pop_share_root:
                closeAnimation();
                break;
            case R.id.weixin:
                ShareSDKUtils.share(Wechat.NAME, title, cocntent, url, imageUrl, this);
                break;
            case R.id.iv_pyq:
                ShareSDKUtils.share(WechatMoments.NAME, title, cocntent, url, imageUrl, this);
                break;
        }
    }


    public void showPopup(View parent) {
        if (!this.isShowing()) {
            openAnimation();
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            this.dismiss();
        }
    }

    /**
     * 设置打开的东动画
     */
    private void openAnimation() {
       ll.startAnimation(animation_in);


    }

    /**
     * 设置关闭的动画
     */
    private void closeAnimation() {
        ll.clearAnimation();
        ll.startAnimation(animation_out);

    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtil.show("分享成功");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtil.show("分享失败");
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
