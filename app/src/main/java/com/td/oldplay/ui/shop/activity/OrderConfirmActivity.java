package com.td.oldplay.ui.shop.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.bean.GoodBean;
import com.td.oldplay.bean.OrderBean;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.pay.weixin.WechatInfo;
import com.td.oldplay.pay.weixin.WeixPayUtils;
import com.td.oldplay.pay.zhifubao.PayDemoActivity;
import com.td.oldplay.pay.zhifubao.PayResult;
import com.td.oldplay.ui.mine.activity.MyAddressActivity;
import com.td.oldplay.ui.shop.adapter.GoodAdapter;
import com.td.oldplay.ui.window.AlertDialog;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.utils.SoftInputUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.td.oldplay.widget.password.PasswordInputView;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class OrderConfirmActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.oreder_confirm_score)
    TextView orederConfirmScore;
    @BindView(R.id.oreder_confirm_total)
    TextView orederConfirmTotal;
    @BindView(R.id.buy_address_name)
    TextView buyAddressName;
    @BindView(R.id.buy_address_telphone)
    TextView buyAddressTelphone;
    @BindView(R.id.buy_address_info)
    TextView buyAddressInfo;
    @BindView(R.id.buy_address_btn)
    RelativeLayout buyAddressBtn;
    @BindView(R.id.account)
    RadioButton account;
    @BindView(R.id.weixin)
    RadioButton weixin;
    @BindView(R.id.zhifubao)
    RadioButton zhifubao;
    @BindView(R.id.order_confirm_rg)
    RadioGroup orderConfirmRg;
    @BindView(R.id.acore)
    CheckBox acore;
    @BindView(R.id.ortder_total)
    TextView ortderTotal;
    @BindView(R.id.ortder_confirm)
    TextView ortderConfirm;

    private List<GoodBean> datas;
    private GoodAdapter goodAdapter;
    private int payType; // 支付的类型
    private OrderBean orderBean;
    private int scoreCount;
    private float scoreMoney;
    private List<String> orderIds;

    private CustomDialog customDialog;
    private PasswordInputView passwordInputView;
    private View dialogView;
    private String password="";
    private float totalScore = 0;

    private HashMap<String, Object> params;
    private AlertDialog alertDialog;
    private IWXAPI api;

    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        alertDialog.show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        EventBus.getDefault().register(this);
        api = WXAPIFactory.createWXAPI(this, MContants.WX_APP_ID);
        ButterKnife.bind(this);
        orderBean = (OrderBean) getIntent().getSerializableExtra("model");
        params = new HashMap<>();
        initView();
        initDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initDialog() {
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
                // 使用账户支付
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.show("请输入密码");
                    return;
                }

                customDialog.dismiss();
                params.put("payPassword",password);
                SoftInputUtils.hideSoftInput(mContext,getWindow());
                payAccout();
            }
        });
        alertDialog = new AlertDialog(mContext);
        alertDialog.setAlertOk(new AlertDialog.onAlertOk() {
            @Override
            public void onClickOk() {
                alertDialog.dismiss();
                finish();
            }
        });
    }


    private void initView() {
        title.setTitle("订单确认");
        title.setOnLeftListener(this);
        ortderConfirm.setOnClickListener(this);
        buyAddressBtn.setOnClickListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        orderConfirmRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.account:
                        payType = 0;
                        break;
                    case R.id.weixin:
                        payType = 1;
                        break;
                    case R.id.zhifubao:
                        payType = 2;
                        break;

                }
            }
        });
        datas = new ArrayList<>();
        goodAdapter = new GoodAdapter(mContext, R.layout.item_confirm_orderr, datas);
        swipeTarget.setAdapter(goodAdapter);

        setData();
    }

    private void setData() {
        if (orderBean != null) {
            account.setText(String.format("账户余额: % .1f 元",userBean.money));
            ortderTotal.setText("￥ " + orderBean.amount_paid);
            orederConfirmTotal.setText("￥ " + orderBean.amount_payable);

            if (orderBean.orderDetails != null && orderBean.orderDetails.size() > 0) {
                datas.addAll(orderBean.orderDetails);
                goodAdapter.notifyDataSetChanged();
            }
            if (orderBean.address != null) {
                buyAddressName.setText("收货人: " + orderBean.address.consignee);
                buyAddressTelphone.setText("联系电话: " + orderBean.address.mobile);
                buyAddressInfo.setText("收货地址: " + orderBean.address.address);
            }

            if (orderBean.scoreOffset != null) {
                acore.setVisibility(View.VISIBLE);
                acore.setText(orderBean.scoreOffset.content);

            } else {
                acore.setVisibility(View.GONE);
            }
            orderIds = new ArrayList<>();
            orderIds.add(orderBean.orderId);
            acore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (scoreMoney != 0) {
                            ortderTotal.setText("￥ " + scoreMoney);
                        } else if (orderIds != null) {
                            HttpManager.getInstance().applyScore(userId, orderIds, scoreSubcriber);
                        }
                        params.put("score",0);
                    } else {
                        ortderTotal.setText("￥ " + orderBean.amount_paid);
                        params.put("score",1);
                    }
                }
            });

            for (GoodBean bean : orderBean.orderDetails) {
                totalScore += bean.score;
            }
            orederConfirmScore.setText("购买可得" + totalScore + "积分");
        } else {
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.buy_address_btn:
                startActivityForResult(new Intent(mContext, MyAddressActivity.class).putExtra("fromType",1), 1002);
                break;
            case R.id.ortder_confirm:
                if(orderBean!=null){
                    // 调用支付接口
                    params.put("userId",userId);
                    params.put("type",3);//购买类型,0:课程支付,1:打赏支付,2连麦支付,3:商品支付
                    if(acore.isChecked()){
                        params.put("price",scoreMoney);
                    }else{
                        params.put("price",orderBean.amount_payable);
                    }
                    params.put("payNum",orderBean.payNum);
                    if(orderBean!=null && orderBean.address==null){
                        ToastUtil.show("请选择收货地址");
                        return;
                    }
                    switch (payType) {
                        case 0:
                            if (userBean.money < orderBean.amount_paid) {
                                ToastUtil.show("账户余额不足");
                                return;
                            }
                            customDialog.show();
                            break;
                        case 1: // 微信支付
                            payWechat();
                            break;
                        case 2:  // 支付宝支付
                            payZhifubao();
                            break;

                    }
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
                        PayTask alipay = new PayTask(OrderConfirmActivity.this);
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

    private void payAccout() {
        HttpManager.getInstance().payAccount(params, new HttpSubscriber<UserBean>(new OnResultCallBack<UserBean>() {
            @Override
            public void onSuccess(UserBean userBean) {
                spUilts.setUser(userBean);
                alertDialog.show();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            if (orderBean != null) {
                orderBean.address = (AddressBean) data.getSerializableExtra("model");
                setAddress();
            }
        }
    }

    private void setAddress() {
        if (orderBean.address != null) {
            buyAddressName.setText("收货人: " + orderBean.address.consignee);
            buyAddressTelphone.setText("联系电话: " + orderBean.address.mobile);
            buyAddressInfo.setText("收货地址: " + orderBean.address.address);
        }

    }

    private HttpSubscriber<Float> scoreSubcriber = new HttpSubscriber<>(new OnResultCallBack<Float>() {


        @Override
        public void onSuccess(Float aFloat) {
            scoreMoney = aFloat;
            if (ortderTotal != null) {
                ortderTotal.setText("￥ " + aFloat);
            }

        }

        @Override
        public void onError(int code, String errorMsg) {
            ToastUtil.show(errorMsg);
        }

        @Override
        public void onSubscribe(Disposable d) {
            addDisposable(d);
        }
    });
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(EventMessage message) {
        if ("WX".equals(message.action)) {
            switch (message.WxPayCode) {
                case 0:
                    ToastUtil.show("支付成功！");
                    alertDialog.show();
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
