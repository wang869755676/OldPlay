package com.td.oldplay.ui.mine.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.bean.RechargeInfo;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class ConsumptionActivity extends BaseFragmentActivity {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.con_money)
    TextView conMoney;
    @BindView(R.id.con_type)
    TextView conType;
    @BindView(R.id.con_account)
    TextView conAccount;
    @BindView(R.id.con_zhifu_type)
    TextView conZhifuType;
    @BindView(R.id.con_zhifu_time)
    TextView conZhifuTime;
    @BindView(R.id.other_name)
    TextView otherName;
    @BindView(R.id.otener_content)
    TextView otenerContent;
    @BindView(R.id.ll_other)
    LinearLayout llOther;
    @BindView(R.id.ll_acount)
    LinearLayout llAcount;
    @BindView(R.id.con_acount_title)
    TextView conAcountTitle;
    @BindView(R.id.con_pay_title)
    TextView conPayTitle;

    private String id;
    private String typeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);
        id = getIntent().getStringExtra("id");
        ButterKnife.bind(this);
        title.setTitle("详情");
        title.setOnLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
    }

    private void getData() {
        HttpManager.getInstance().walletDetail(id, new HttpSubscriber<RechargeInfo>(new OnResultCallBack<RechargeInfo>() {

            @Override
            public void onSuccess(RechargeInfo rechargeInfo) {
                setData(rechargeInfo);
            }

            @Override
            public void onError(int code, String errorMsg) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));
    }

    private void setData(RechargeInfo rechargeInfo) {
        if (rechargeInfo.money.contains("+")) {
            conMoney.setTextColor(Color.parseColor("#168F1B"));
        } else {
            conMoney.setTextColor(getResources().getColor(R.color.tv_red));
        }
        conMoney.setText(rechargeInfo.money + "");

        conZhifuTime.setText(rechargeInfo.formatTime);
        if (TextUtils.isEmpty(rechargeInfo.detail)) {
            llOther.setVisibility(View.GONE);
        } else {
            llOther.setVisibility(View.VISIBLE);
            otenerContent.setText(rechargeInfo.detail);
        }

        switch (rechargeInfo.payType) {
            case 0:
                conZhifuType.setText("支付宝");
                break;
            case 1:
                conZhifuType.setText("微信");
                break;
            case 2:
                conZhifuType.setText("账户余额");
                llAcount.setVisibility(View.GONE);
                break;
        }

        switch (rechargeInfo.type) {
            case 0:
                llAcount.setVisibility(View.GONE);
                typeStr = "充值";
                break;
            case 1:
                typeStr = "提现";
                llAcount.setVisibility(View.VISIBLE);
                llOther.setVisibility(View.GONE);
                conPayTitle.setText("提现方式:");
                conZhifuType.setText("银行卡");
                conAcountTitle.setText("提现账号:");
                break;
            case 2:
                llAcount.setVisibility(View.GONE);
                typeStr = "消费";
                break;
            case 3:
                llAcount.setVisibility(View.GONE);
                typeStr = "直播";
                break;
        }
        conType.setText(rechargeInfo.typeDetail);
        conAccount.setText(rechargeInfo.account);


    }


}
