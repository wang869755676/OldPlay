package com.td.oldplay.ui.shop.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.ui.course.fragment.ShopFragment;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopListActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.shop_list_container)
    FrameLayout shopListContainer;

    private int type;
    private int goodType; //1 绿色产品 2教学产品
    private ShopFragment fragment;
    private String titleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra("type", 0);
        goodType = getIntent().getIntExtra("goodTypeId", 0);
        titleStr=getIntent().getStringExtra("title");
        switch (type) {
            case 1:
                title.setTitle(titleStr);
                break;
            case 2:
                title.setTitle("推荐商品");
                break;
            case 3:
                title.setTitle("最新优惠");
                break;
        }

        title.setOnLeftListener(this);
        fragment = new ShopFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.shop_list_container, fragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
        }
    }
}
