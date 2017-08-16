package com.td.oldplay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.td.oldplay.MyApplication;
import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.live.LiveLoginHelper;
import com.td.oldplay.ui.live.MessageEvent;
import com.td.oldplay.utils.AppUtils;
import com.td.oldplay.utils.ShareSDKUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.wechat.friends.Wechat;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseFragmentActivity implements View.OnClickListener {


    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_pws)
    EditText loginPws;

    @BindView(R.id.login_register)
    TextView loginRegister;
    @BindView(R.id.login_servier)
    TextView loginServier;
    @BindView(R.id.login_forget)
    TextView loginForget;
    @BindView(R.id.login_weixin)
    ImageView loginWeixin;
    @BindView(R.id.login_typ_zhubo)
    RadioButton loginTypZhubo;
    @BindView(R.id.login_typ_common)
    RadioButton loginTypCommon;
    @BindView(R.id.login_type)
    RadioGroup loginType;

    private String phone;
    private String pws;
    private int type = 1;

    private HashMap<String, Object> params = new HashMap<>();
    private UserBean weUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginForget.setOnClickListener(this);
        loginRegister.setOnClickListener(this);
        loginServier.setOnClickListener(this);
        loginWeixin.setOnClickListener(this);
        loginType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.login_typ_common:
                        type = 0;
                        break;
                    case R.id.login_typ_zhubo:
                        type = 1;
                        break;
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_forget:
                startActivity(new Intent(mContext, ForgetPwsActivity.class));
                break;
            case R.id.login_register:
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
            case R.id.login_servier:
                if (checkInput()) {
                    loginServier();
                }
                break;
            case R.id.login_weixin:
               /* Intent intent = new Intent(mContext, RegisterActivity.class);
                intent.putExtra("isBound", true);
                intent.putExtra("user", weUser);
                startActivity(intent);
                ShareSDKUtils.loginOut(Wechat.NAME);*/
                ShareSDKUtils.login(Wechat.NAME, new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        PlatformDb db = platform.getDb();
                        if (weUser != null)
                            weUser = new UserBean();

                        weUser.userId = db.getUserId();
                        weUser.avatar = db.getUserIcon();
                        weUser.avatar = db.getUserName();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isBound(weUser);

                            }
                        });
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        ToastUtil.show(throwable.getMessage() + "");
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {

                    }
                });
                break;
        }

    }

    /**
     * 微信登录是否已经绑定信息
     */
    private void isBound(UserBean user) {
        if (user != null) {
            HttpManager.getInstance().isBound(user.userId, new HttpSubscriber<UserBean>(new OnResultCallBack<UserBean>() {

                @Override
                public void onSuccess(UserBean userBean) {
                    if (userBean != null) {
                        loginLiveSsdk(userBean);
                    } else {
                        Intent intent = new Intent(mContext, RegisterActivity.class);
                        intent.putExtra("isBound", true);
                        intent.putExtra("user", weUser);
                        startActivity(intent);

                    }

                }

                @Override
                public void onError(int code, String errorMsg) {

                }

                @Override
                public void onSubscribe(Disposable d) {

                }
            }));
        }
    }

    private void loginLiveSsdk(final UserBean userBean) {

        LiveLoginHelper.iLiveLogin(userBean.userId,userBean.userSig, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                hideLoading();
                spUilts.setUser(userBean);
                spUilts.setUserId(userBean.userId);
                spUilts.setIsLogin(true);
                JPushInterface.setAlias(mContext, 1, userBean.userId);
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                hideLoading();
                Log.e("===",errMsg);
                ToastUtil.show(errCode + " " + errMsg);
            }
        });
    }

    private void loginServier() {
        showLoading();
        HttpManager.getInstance().loginUser(params, new HttpSubscriber<UserBean>(new OnResultCallBack<UserBean>() {
            @Override
            public void onSuccess(final UserBean userBean) {
                if (userBean != null) {

                   loginLiveSsdk(userBean);

                }

            }

            @Override
            public void onError(int code, String errorMsg) {
                hideLoading();
                ToastUtil.show(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));

    }

    private boolean checkInput() {

        phone = loginPhone.getText().toString();
       /* if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请输入手机号");
            return false;
        } else if (!AppUtils.checkPhone(phone)) {
            ToastUtil.show("请输入正确的手机号");
            return false;
        }*/
        params.put("phone", phone);


        pws = loginPws.getText().toString();
        if (TextUtils.isEmpty(pws)) {
            ToastUtil.show("请输入密码");
            return false;
        }

        params.put("password", pws);

        //  params.put("uType", type);
        return true;
    }
}
