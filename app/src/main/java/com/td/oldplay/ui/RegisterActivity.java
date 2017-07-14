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
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.utils.AppUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private CountDownTimer timer;
    private String phone;
    private String name;
    private String pws;
    private String rePws;
    private String zhipws;
    private String zhirePws;

    private String code;

    private HashMap<String, Object> paras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        title.setTitle("注册");
        title.setOnLeftListener(this);
        tvGetCode.setOnClickListener(this);
        registerSubmint.setOnClickListener(this);
        paras = new HashMap<>();
        timer = new CountDownTimer(1000, 60000) {
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
                break;
            case R.id.register_submint:
                if (checkInput()) {
                    registerServicer();
                }
                break;
            case R.id.tv_getCode:
                tvGetCode.setEnabled(false);
                if(!TextUtils.isEmpty(registerPhone.getText().toString()) && AppUtils.checkPhone(registerPhone.getText().toString())){
                    HttpManager.getInstance().getCode(phone, new HttpSubscriber<String>(new OnResultCallBack<String>() {
                        @Override
                        public void onSuccess(String s) {
                            timer.start();
                            ToastUtil.show("验证码已发送");
                        }

                        @Override
                        public void onError(int code, String errorMsg) {
                            ToastUtil.show(errorMsg);
                        }
                    }));
                }else{
                    ToastUtil.show("手机格式不正确");
                }



                break;
        }

    }

    private void registerServicer() {
        if (paras != null) {
            HttpManager.getInstance().registerUser(paras, new HttpSubscriber<String>(new OnResultCallBack<String>() {
                @Override
                public void onSuccess(String o) {
                    ToastUtil.show("注册成功");
                    finish();
                }

                @Override
                public void onError(int code, String errorMsg) {
                    ToastUtil.show(errorMsg);
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
        paras.put("passworad", pws);

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

        paras.put("vcode", code);
        return true;

    }


}
