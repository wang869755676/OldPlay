package com.td.oldplay.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.td.oldplay.MyApplication;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.utils.SharePreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;


public abstract class BaseFragmentActivity extends AppCompatActivity {

    public Context mContext;   //吐司的上下文
    protected Context AContext;
    public String userId;
    protected UserBean userBean;
    private KProgressHUD hud;
    protected SharePreferenceUtil spUilts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AContext = getApplicationContext();
        spUilts = MyApplication.getInstance().mPreferenceUtil;
        userId = spUilts.getUserId();
        userBean = spUilts.getUser();
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
        hud = KProgressHUD.create(AContext)
                .setCustomView(v).show();

    }

    public void hideLoading() {
        if (hud != null) {
            hud.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        GlideUtils.destroyRequest(AContext);
        clear();
        super.onDestroy();

    }

    private ListCompositeDisposable listCompositeDisposable = new ListCompositeDisposable();


    protected void addDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            listCompositeDisposable.add(disposable);
        }
    }

    protected void reDisposable(Disposable disposable) {
        if (disposable != null) {
            listCompositeDisposable.remove(disposable);
        }
    }

    protected void clear() {
        if (!listCompositeDisposable.isDisposed()) {
            listCompositeDisposable.clear();
        }
    }

/*    public void showProgress(String title) {
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel(title)
                .setMaxProgress(100)
                .setAutoDismiss(false)
                .show();
    }*/

    public void updateProgressTitle(String title) {
        if (hud != null) {
            hud.setProgress(0);
            hud.setLabel(title);
        } else {
            hud = KProgressHUD.create(mContext)
                    .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                    .setLabel(title)
                    .setMaxProgress(100)
                    .setAutoDismiss(false)
                    .show();
        }

    }

    public void updateProgress(int progress) {
        if (hud != null)
            hud.setProgress(progress);
    }
}
