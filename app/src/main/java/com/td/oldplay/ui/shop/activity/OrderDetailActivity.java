package com.td.oldplay.ui.shop.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.bean.OrderDetail;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.shop.adapter.GoodAdapter;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class OrderDetailActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.order_detail_num)
    TextView orderDetailNum;
    @BindView(R.id.order_detail_time)
    TextView orderDetailTime;
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
    @BindView(R.id.order_detail_pay)
    CheckBox orderDetailPay;
    @BindView(R.id.order_detail_acore)
    CheckBox orderDetailAcore;
    @BindView(R.id.order_detail_total)
    TextView orderDetailTotal;
    @BindView(R.id.order_detial_confirm)
    TextView orderDetialConfirm;

    private String id;
    private GoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        title.setTitle("订单详情");
        title.setOnLeftListener(this);
        orderDetialConfirm.setOnClickListener(this);
        getData();
    }

    private void getData() {
        HttpManager.getInstance().orderDetails(id, new HttpSubscriber<OrderDetail>(new OnResultCallBack<OrderDetail>() {

            @Override
            public void onSuccess(OrderDetail orderDetail) {
                setData(orderDetail);
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

    private void setData(OrderDetail orderDetail) {
        if (orderDetail != null) {
            orderDetailNum.setText("订单号: " + orderDetail.orderNum);
            orderDetailTime.setText(orderDetail.formatTime);
            orderDetailTotal.setText("合计: " + orderDetail.amount_paid);
            orederConfirmTotal.setText("小计" + orderDetail.amount_payable);
            swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
            if (orderDetail.orderDetails != null) {
                swipeTarget.setAdapter(new GoodAdapter(mContext, R.layout.item_confirm_orderr, orderDetail.orderDetails));
            }
            if (orderDetail.isApplyScore == 1) {
                orderDetailAcore.setVisibility(View.VISIBLE);
                if (orderDetail.scoreOffset != null) {
                    orderDetailAcore.setText(orderDetail.scoreOffset.content);
                }
            } else {
                orderDetailAcore.setVisibility(View.GONE);
            }

            switch (orderDetail.payment_way) {
                case 0:
                    orderDetailPay.setText("账户余额:" + orderDetail.amount_paid + "元");
                    break;
                case 1:
                    orderDetailPay.setText("支付宝支付" + orderDetail.amount_paid + "元");
                    break;
                case 2:
                    orderDetailPay.setText("微信支付" + orderDetail.amount_paid + "元");
                    break;
            }
            if (orderDetail.address != null) {
                buyAddressName.setText("收货人: " + orderDetail.address.consignee);
                buyAddressTelphone.setText("收货人: " + orderDetail.address.mobile);
                buyAddressInfo.setText("收货人: " + orderDetail.address.address);
            }
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
