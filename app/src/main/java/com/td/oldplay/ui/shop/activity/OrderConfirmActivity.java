package com.td.oldplay.ui.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.bean.GoodBean;
import com.td.oldplay.ui.shop.adapter.GoodAdapter;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        ButterKnife.bind(this);
        initView();
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
        datas.add(new GoodBean());
        datas.add(new GoodBean());
        datas.add(new GoodBean());
        datas.add(new GoodBean());
        goodAdapter = new GoodAdapter(mContext, R.layout.item_confirm_orderr, datas);
        swipeTarget.setAdapter(goodAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.buy_address_btn:
                startActivity(new Intent(mContext, ShopCarActivity.class));
                break;
            case R.id.ortder_confirm:
                // 调用支付接口
                break;
        }

    }
}
