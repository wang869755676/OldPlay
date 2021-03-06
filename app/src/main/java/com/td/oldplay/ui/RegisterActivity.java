package com.td.oldplay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.live.LiveLoginHelper;
import com.td.oldplay.utils.AppUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.tencent.ilivesdk.ILiveCallBack;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.register_phone)
    EditText registerPhone;
    @BindView(R.id.register_name)
    EditText registerName;
    @BindView(R.id.register_passworad)
    EditText registerPassworad;
    @BindView(R.id.register_rePas)
    EditText registerRePas;
    @BindView(R.id.register_zhifuPas)
    EditText registerZhifuPas;
    @BindView(R.id.register_rezhifuPas)
    EditText registerRezhifuPas;
    @BindView(R.id.register_code)
    EditText registerCode;
    @BindView(R.id.tv_getCode)
    Button tvGetCode;
    @BindView(R.id.register_submint)
    Button registerSubmint;
    @BindView(R.id.register_invite_phone)
    EditText registerInvitePhone;

    private CountDownTimer timer;
    private String phone;
    private String name;
    private String pws;
    private String rePws;
    private String zhipws;
    private String zhirePws;
    private String invitePhone;

    private String code;

    private HashMap<String, Object> paras;

    private boolean isBound;
    private UserBean wechatUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        isBound = getIntent().getBooleanExtra("isBound", false);
        wechatUser = (UserBean) getIntent().getSerializableExtra("user");
        if (isBound) {
            title.setTitle("绑定用户信息");
            registerPassworad.setVisibility(View.GONE);
            registerRePas.setVisibility(View.GONE);
            registerInvitePhone.setVisibility(View.GONE);
            if (wechatUser != null) {
                registerName.setText(wechatUser.nickName);

            }

        } else {
            title.setTitle("注册");
        }

        title.setOnLeftListener(this);
        //title.setTitleBarBackgroud(R.color.transparent);
        tvGetCode.setOnClickListener(this);
        registerSubmint.setOnClickListener(this);
        paras = new HashMap<>();
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvGetCode.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                tvGetCode.setText("重新发送");
                tvGetCode.setEnabled(true);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.register_submint:
                if (checkInput()) {
                    if (isBound) {
                        paras.put("avatar", wechatUser.avatar);
                        paras.put("openId", wechatUser.userId);
                        bondServer();
                    } else {
                        registerServicer();
                    }

                }

                break;
            case R.id.tv_getCode:
                tvGetCode.setEnabled(false);
                if (!TextUtils.isEmpty(registerPhone.getText().toString()) && AppUtils.checkPhone(registerPhone.getText().toString())) {
                    HttpManager.getInstance().getCode(registerPhone.getText().toString(), new HttpSubscriber<String>(new OnResultCallBack<String>() {
                        @Override
                        public void onSuccess(String s) {

                            timer.start();
                            ToastUtil.show("验证码已发送");
                        }

                        @Override
                        public void onError(int code, String errorMsg) {
                            tvGetCode.setEnabled(true);
                            ToastUtil.show(errorMsg);
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }
                    }));
                } else {
                    ToastUtil.show("手机格式不正确");
                }


                break;
        }

    }

    private void bondServer() {
        HttpManager.getInstance().boundUser(paras, new HttpSubscriber<UserBean>(new OnResultCallBack<UserBean>() {

            @Override
            public void onSuccess(UserBean userBean) {
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

    private void loginLiveSsdk(final UserBean userBean) {

        LiveLoginHelper.iLiveLogin(userBean.userId, userBean.userSig, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                spUilts.setUser(userBean);
                spUilts.setUserId(userBean.userId);
                spUilts.setIsLogin(true);
                JPushInterface.setAlias(mContext, 1, userBean.userId);
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }

    private void registerServicer() {
        if (paras != null) {
            showLoading();
            HttpManager.getInstance().registerUser(paras, new HttpSubscriber<String>(new OnResultCallBack<String>() {
                @Override
                public void onSuccess(String o) {
                    hideLoading();
                    ToastUtil.show("注册成功");
                    finish();
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
    }

    /**
     * 检查输入的合法性
     *
     * @return
     */
    private boolean checkInput() {

        phone = registerPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请输入手机号");
            return false;
        } else if (!AppUtils.checkPhone(phone)) {
            ToastUtil.show("请输入正确的手机号");
            return false;
        }
        paras.put("phone", phone);
        name = registerName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请输入名称");
            return false;
        }
        paras.put("nickName", name);

        if (!isBound) {
            pws = registerPassworad.getText().toString();
            if (TextUtils.isEmpty(pws)) {
                ToastUtil.show("请输入密码");
                return false;
            }
            rePws = registerRePas.getText().toString();
            if (TextUtils.isEmpty(rePws)) {
                ToastUtil.show("请输入确认密码");
                return false;
            }
            if (!pws.equals(rePws)) {
                ToastUtil.show("两次的登录密码不一样");
                return false;
            }
            paras.put("password", pws);
        }


        zhipws = registerZhifuPas.getText().toString();
        if (TextUtils.isEmpty(zhipws)) {
            ToastUtil.show("请输入支付密码");
            return false;
        }
        zhirePws = registerRezhifuPas.getText().toString();
        if (TextUtils.isEmpty(zhirePws)) {
            ToastUtil.show("请输入确认支付密码密码");
            return false;
        }
        if (!zhipws.equals(zhirePws)) {
            ToastUtil.show("两次的支付密码不一样");
            return false;
        }
        paras.put("payPassword", zhipws);


        code = registerCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.show("请输入验证码");
            return false;
        }
        invitePhone = registerInvitePhone.getText().toString().trim();
        if (!TextUtils.isEmpty(invitePhone)) {
            paras.put("inviterPhone", invitePhone);
        }
        paras.put("vcode", code);
        return true;

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
