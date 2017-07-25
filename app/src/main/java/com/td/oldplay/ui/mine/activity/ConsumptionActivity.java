package com.td.oldplay.ui.mine.activity;

import android.os.Bundle;
import android.view.View;
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

    private String id;
    private String typeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);
        id=getIntent().getStringExtra("id");
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
        HttpManager.getInstance().walletDetail(id,new HttpSubscriber<RechargeInfo>(new OnResultCallBack<RechargeInfo>() {

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
        conMoney.setText(rechargeInfo.money+"");
        switch (rechargeInfo.type){
            case 0:
                typeStr="充值";
                break;
            case 1:
                typeStr="提现";
                break;
            case 2:
                typeStr="消费";
                break;
            case 3:
                typeStr="直播";
                break;
        }
        conType.setText(typeStr);
        conAccount.setText(rechargeInfo.account);
        conZhifuType.setText("");
        conZhifuTime.setText(rechargeInfo.formatTime);
    }


}
