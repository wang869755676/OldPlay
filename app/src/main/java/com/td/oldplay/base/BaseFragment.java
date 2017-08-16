package com.td.oldplay.base;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.td.oldplay.MyApplication;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.utils.SharePreferenceUtil;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;

/**
 *
 */
public abstract class BaseFragment extends Fragment {
    protected BaseFragmentActivity mBaseActivity = null;
    protected Activity mActivity = null;
    protected String userId;
    protected UserBean userBean;
    private KProgressHUD hud;
    protected SharePreferenceUtil spUilts;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseFragmentActivity) {
            this.mBaseActivity = (BaseFragmentActivity) activity;
        }
        this.mActivity = activity;
        spUilts = MyApplication.getInstance().mPreferenceUtil;
        userId = spUilts.getUserId();
        userBean = spUilts.getUser();
        Log.e("===","onAcct");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    protected abstract void init(View view);

    /**
     * Activity跳转
     *
     * @param cls
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(mActivity, cls);
        startActivity(intent);
    }

    public void showLoading() {
        hud = KProgressHUD.create(mActivity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false).show();

    }

    public void showCustomLoading(View v) {
        hud = KProgressHUD.create(mActivity)
                .setCustomView(v).show();

    }

    public void hideLoading() {
        if (hud != null) {
            hud.dismiss();
        }
    }


    // 取消请求处理
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

    @Override
    public void onDestroy() {
        clear();
        super.onDestroy();
    }
}
