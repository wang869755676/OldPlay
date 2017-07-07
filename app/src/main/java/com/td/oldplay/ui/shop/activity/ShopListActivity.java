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

public class ShopListActivity extends BaseFragmentActivity implements View.OnClickListener{

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.shop_list_container)
    FrameLayout shopListContainer;

    private int type; //0 绿色产品 1教学产品
    private ShopFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra("type",0);
        if(type==0){
            title.setTitle("绿色产品");
        }else{
            title.setTitle("教学产品");
        }
        title.setOnLeftListener(this);

        fragment=new ShopFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.shop_list_container,fragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_text:
                finish();
                break;
        }
    }
}
