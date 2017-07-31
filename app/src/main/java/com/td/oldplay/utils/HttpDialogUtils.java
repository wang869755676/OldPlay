package com.td.oldplay.utils;

import android.content.Context;
import android.view.View;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.lang.ref.WeakReference;

/**
 *   必须调用Context 进行初始化
 */

public class HttpDialogUtils {
    private static WeakReference<Context> mContext;//弱引用
    private static KProgressHUD hud;

    public  static void  init(Context context){
         mContext=new WeakReference<Context>(context);
    }
    public static void showLoading() {
        if (hud == null) {
            hud = KProgressHUD.create(mContext.get());
        }
        hud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).show();


    }

    public void showCustomLoading(View v) {
        if (hud == null) {
            hud = KProgressHUD.create(mContext.get());
        }

        hud.setCustomView(v).show();

    }

    public void hideLoading() {
        if (hud != null) {
            hud.dismiss();
        }
    }

}
