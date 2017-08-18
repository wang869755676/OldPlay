package com.td.oldplay.ui.shop.activity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.bean.GoodBean;
import com.td.oldplay.bean.OrderBean;
import com.td.oldplay.bean.ScoreOffset;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.pay.weixin.WechatInfo;
import com.td.oldplay.pay.weixin.WeixPayUtils;
import com.td.oldplay.pay.zhifubao.PayResult;
import com.td.oldplay.ui.mine.activity.MyAddressActivity;
import com.td.oldplay.ui.shop.adapter.GoodAdapter;
import com.td.oldplay.ui.window.AlertDialog;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.td.oldplay.widget.password.PasswordInputView;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class OrdersConfirmActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;

    @BindView(R.id.ortder_total)
    TextView ortderTotal;
    @BindView(R.id.ortder_confirm)
    TextView ortderConfirm;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
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

    private List<String> carIds;
    private List<OrderBean> datas = new ArrayList<>();

    private orderConfirmAdapter adapter;
    private AddressBean addressBean;
    private ScoreOffset scoreOffset;

    private int payType; // 支付的类型
    private float totalMoney;
    private float scoreMoney;
    private List<String> orderIds;

    private CustomDialog customDialog;
    private PasswordInputView passwordInputView;
    private View dialogView;
    private String password="";

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
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_orders_confirm);
        api = WXAPIFactory.createWXAPI(this, MContants.WX_APP_ID);
        params=new HashMap<>();
        carIds = getIntent().getStringArrayListExtra("carIds");
        ButterKnife.bind(this);
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
        adapter = new orderConfirmAdapter(mContext, R.layout.item_order_confim, datas);
        swipeTarget.setAdapter(adapter);
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

        getData();
    }

    private void getData() {
        showLoading("生成订单中");
        HttpManager.getInstance().createOrderCars(carIds, userId, new HttpSubscriber<List<OrderBean>>(new OnResultCallBack<List<OrderBean>>() {

            @Override
            public void onSuccess(List<OrderBean> orderBean) {
                hideLoading();
                if (orderBean != null && orderBean.size() > 0) {
                    datas.addAll(orderBean);
                    adapter.notifyDataSetChanged();
                    addressBean = orderBean.get(0).address;
                    scoreOffset = orderBean.get(0).scoreOffset;
                    setData();
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

    private void setData() {
        account.setText("账户余额:" + userBean.money + "元");
        if (addressBean != null) {
            buyAddressName.setText("收货人: " + addressBean.consignee);
            buyAddressTelphone.setText("收货人: " + addressBean.mobile);
            buyAddressInfo.setText("收货人: " + addressBean.address);
        }

        if (scoreOffset != null) {
            acore.setVisibility(View.VISIBLE);
            acore.setText(scoreOffset.content);

        } else {
            acore.setVisibility(View.GONE);
        }
        orderIds = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            orderIds.add(datas.get(i).orderId);
            totalMoney += datas.get(i).amount_paid;

        }
        ortderTotal.setText("￥ " + totalMoney);
        acore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (scoreMoney != 0) {
                        ortderTotal.setText("￥ " + scoreMoney);
                    } else if (orderIds != null) {
                        HttpManager.getInstance().applyScore(userId, orderIds, scoreSubcriber);
                    }
                } else {
                    ortderTotal.setText("￥ " + totalMoney);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.ortder_confirm:
                if (datas != null && datas.size() > 0) {
                    // 调用支付接口
                    params.put("userId", userId);
                    params.put("type", 3);//购买类型,0:课程支付,1:打赏支付,2连麦支付,3:商品支付
                    if (acore.isChecked()) {
                        params.put("price", scoreMoney);
                    } else {
                        params.put("price", totalMoney);
                    }
                    params.put("payPassword", password);
                    params.put("payNum", datas.get(0).payNum);
                    switch (payType) {
                        case 0:
                            if (userBean.money < totalMoney - scoreMoney) {
                                ToastUtil.show("账户余额不足");
                                return;
                            }
                            customDialog.show();
                            break;
                        case 1: // 微信支付
                            isGoodCars=true;
                            payWechat();
                            break;
                        case 2:  // 支付宝支付
                            payZhifubao();
                            break;

                    }
                }

                break;
            case R.id.buy_address_btn:
                startActivityForResult(new Intent(mContext, MyAddressActivity.class), 1002);
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
                        PayTask alipay = new PayTask(OrdersConfirmActivity.this);
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
            addressBean = (AddressBean) data.getSerializableExtra("model");
            setAddress();
        }
    }

    private void setAddress() {
        if (addressBean != null) {
            buyAddressName.setText("收货人: " + addressBean.consignee);
            buyAddressTelphone.setText("联系电话: " + addressBean.mobile);
            buyAddressInfo.setText("收货地址: " + addressBean.address);
        }

    }

    private class orderConfirmAdapter extends CommonAdapter<OrderBean> {
        private RecyclerView recyclerView;
        private GoodAdapter goodAdapter;

        public orderConfirmAdapter(Context context, int layoutId, List<OrderBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, OrderBean orderBean, int position) {
            holder.setText(R.id.oreder_confirm_total, "小计:" + orderBean.amount_paid);

            recyclerView = holder.getView(R.id.swipe_target);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            if (orderBean.orderDetails != null) {

                float totalScore = 0;
                for (GoodBean bean : orderBean.orderDetails) {
                    totalScore += bean.score;
                }
                holder.setText(R.id.oreder_confirm_score, "购买可得" + totalScore + "积分");
                recyclerView.setAdapter(new GoodAdapter(mContext, R.layout.item_confirm_orderr, orderBean.orderDetails));
            }

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

    private boolean isGoodCars;
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
