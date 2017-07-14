package com.td.oldplay.ui.mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);
        ButterKnife.bind(this);
        title.setTitle("详情");
        title.setOnLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
