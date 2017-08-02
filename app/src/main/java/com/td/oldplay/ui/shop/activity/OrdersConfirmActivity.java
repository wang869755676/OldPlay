package com.td.oldplay.ui.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.bean.OrderBean;
import com.td.oldplay.bean.ScoreOffset;
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
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_confirm);
        carIds = getIntent().getStringArrayListExtra("carIds");
        ButterKnife.bind(this);
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
            ortderTotal.setText("￥ " + totalMoney);
        }

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
                // 调用支付接口
                switch (payType) {
                    case 0:
                        if (userBean.money < totalMoney - scoreMoney) {
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
            case R.id.buy_address_btn:
                startActivityForResult(new Intent(mContext, MyAddressActivity.class), 1002);
                break;
        }

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
            holder.setText(R.id.oreder_confirm_score, "购买可多少几分:" + orderBean.amount_paid);
            recyclerView = holder.getView(R.id.swipe_target);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            if (orderBean.orderDetails != null) {
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
}
