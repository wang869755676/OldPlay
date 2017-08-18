package com.td.oldplay.ui.mine.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.alipay.sdk.app.PayTask;
import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.pay.weixin.WechatInfo;
import com.td.oldplay.pay.weixin.WeixPayUtils;
import com.td.oldplay.pay.zhifubao.PayResult;
import com.td.oldplay.ui.shop.activity.OrdersConfirmActivity;
import com.td.oldplay.ui.window.AlertDialog;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.ui.window.PayAlertDialog;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.td.oldplay.widget.password.PasswordInputView;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class RechargeActivity extends BaseFragmentActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.check_weixin)
    CheckBox checkWeixin;
    @BindView(R.id.check_zhifubao)
    CheckBox checkZhifubao;
    @BindView(R.id.recharge_money)
    EditText rechargeMoney;
    @BindView(R.id.commit)
    Button commit;
    private CustomDialog customDialog;
    private PasswordInputView passwordInputView;
    private View dialogView;
    private String password = "";
    private int payType = -1;

    private boolean isRechargerPay;
    private PayAlertDialog paySuccessDialog;
    private float reMoney;

    private HashMap<String, Object> params;
    private AlertDialog alertDialog;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        api = WXAPIFactory.createWXAPI(this, MContants.WX_APP_ID);
        params = new HashMap<>();
        ButterKnife.bind(this);
        initDialog();
        title.setTitle("充值");
        title.setOnLeftListener(this);
        commit.setOnClickListener(this);
        checkWeixin.setOnCheckedChangeListener(this);
        checkZhifubao.setOnCheckedChangeListener(this);
    }

    private void initDialog() {
        EventBus.getDefault().post(new EventMessage("dfdsgfd"));
        customDialog = new CustomDialog(mContext);
        customDialog.setTitle("输入密码");
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_password, null);
        passwordInputView = (PasswordInputView) dialogView.findViewById(R.id.password);
        customDialog.setContanier(dialogView);
        customDialog.setDialogClick(new CustomDialog.DialogClick() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                password = passwordInputView.getText().toString();

            }
        });

        paySuccessDialog = new PayAlertDialog(mContext, false, false);
        paySuccessDialog.setContent("支付成功");
        paySuccessDialog.setDialogClick(new PayAlertDialog.DialogClick() {
            @Override
            public void onBack() {

            }

            @Override
            public void onnext() {
                EventBus.getDefault().post(new EventMessage("changeAcount").total = reMoney);  // 支付更新账户余额信息
                finish();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.commit:
                params.put("userId", userId);
                params.put("type", 4);

                if (payType == -1) {
                    ToastUtil.show("请选择支付方式");
                    return;
                }
                if (TextUtils.isEmpty(rechargeMoney.getText().toString().trim())) {

                    ToastUtil.show("请输入充值金额");
                    return;
                }
                params.put("price", rechargeMoney.getText().toString().trim());
                if (payType == 1) {
                    isRechargerPay = true;
                    payWechat();

                } else {
                    payZhifubao();
                }


                break;
        }

    }

    private void payZhifubao() {

        HttpManager.getInstance().payZhifubao(params, new HttpSubscriber<String>(new OnResultCallBack<String>() {

            @Override
            public void onSuccess(final String s) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(RechargeActivity.this);
                        Map<String, String> result = alipay.payV2(s, true);
                        Log.i("msp", result.toString());
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                }).start();

            }

            @Override
            public void onError(int code, String errorMsg) {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }
        }));
    }

    private void payWechat() {
        HttpManager.getInstance().payWechat(params, new HttpSubscriber<WechatInfo>(new OnResultCallBack<WechatInfo>() {

            @Override
            public void onSuccess(WechatInfo wechatInfo) {
                WeixPayUtils.pay(api, wechatInfo);

            }

            @Override
            public void onError(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.check_weixin:
                if (isChecked) {
                    checkZhifubao.setChecked(false);
                    payType = 1;
                } else {
                    payType = -1;
                }
                break;
            case R.id.check_zhifubao:
                if (isChecked) {
                    checkWeixin.setChecked(false);
                    payType = 2;
                } else {
                    payType = -1;
                }
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(EventMessage message) {

        if (isRechargerPay) {
            if ("WX".equals(message.action)) {
                switch (message.WxPayCode) {
                    case 0:
                        ToastUtil.show("支付成功！");
                        paySuccessDialog.show();
                        isRechargerPay = false;
                        break;
                    case -1:
                        ToastUtil.show("支付失败！");
                        break;
                    case -2:
                        ToastUtil.show("您取消了支付！");
                        break;

                    default:

                        ToastUtil.show("支付失败！");
                        break;

                }
            }

        }
    }


    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (paySuccessDialog != null) {
                            paySuccessDialog.show();
                        }
                    } else {
                        ToastUtil.show("支付失败");

                    }
                    break;
                }

                default:
                    break;
            }
        }

        ;
    };
}
