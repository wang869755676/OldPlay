package com.td.oldplay.ui.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.bean.GoodBean;
import com.td.oldplay.bean.OrderBean;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.mine.activity.MyAddressActivity;
import com.td.oldplay.ui.shop.adapter.GoodAdapter;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.td.oldplay.widget.password.PasswordInputView;

import java.util.ArrayList;
import java.util.List;

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
    private String password;
    private float totalScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        ButterKnife.bind(this);
        orderBean = (OrderBean) getIntent().getSerializableExtra("model");
        initView();
        initDialog();
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
            account.setText("账户余额:" + userBean.money + "元");
            ortderTotal.setText("￥ " + orderBean.amount_paid);
            orederConfirmTotal.setText("￥ " + orderBean.amount_payable);

            if (orderBean.orderDetails != null && orderBean.orderDetails.size() > 0) {
                datas.addAll(orderBean.orderDetails);
                goodAdapter.notifyDataSetChanged();
            }
            if (orderBean.address != null) {
                buyAddressName.setText("收货人: " + orderBean.address.consignee);
                buyAddressTelphone.setText("收货人: " + orderBean.address.mobile);
                buyAddressInfo.setText("收货人: " + orderBean.address.address);
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
                    } else {
                        ortderTotal.setText("￥ " + orderBean.amount_paid);
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
                startActivityForResult(new Intent(mContext, MyAddressActivity.class), 1002);
                break;
            case R.id.ortder_confirm:
                // 调用支付接口
                switch (payType) {
                    case 0:
                        if (userBean.money < orderBean.amount_paid) {
                            ToastUtil.show("账户余额不足");
                            return;
                        }
                        customDialog.show();
                        break;
                    case 1: // 微信支付
                        break;
                    case 2:  // 支付宝支付
                        break;

                }
                break;
        }

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
}
