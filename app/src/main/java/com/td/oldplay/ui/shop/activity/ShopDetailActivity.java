package com.td.oldplay.ui.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.BasePagerAdapter;
import com.td.oldplay.ui.course.activity.TeacherDetailActivity;
import com.td.oldplay.ui.course.fragment.CommentFragment;
import com.td.oldplay.ui.shop.fragment.ShopCommentFragment;
import com.td.oldplay.ui.shop.fragment.ShopDetailFragment;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.td.oldplay.widget.banner.MZBannerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopDetailActivity extends BaseFragmentActivity implements View.OnClickListener {


    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.shop_detail_banner)
    MZBannerView shopDetailBanner;
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.shop_price)
    TextView shopPrice;
    @BindView(R.id.item_shop_sell)
    TextView itemShopSell;
    @BindView(R.id.item_shop_score)
    TextView itemShopScore;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.shop_detail_teacher)
    TextView shopDetailTeacher;
    @BindView(R.id.shop_detail_cart)
    TextView shopDetailCart;
    @BindView(R.id.shop_de_car_num)
    TextView shopDeCarNum;
    @BindView(R.id.fl_car)
    FrameLayout flCar;
    @BindView(R.id.shop_detail_add_cart)
    TextView shopDetailAddCart;
    @BindView(R.id.shop_detail_buy)
    TextView shopDetailBuy;
    // 团购相关的
    @BindView(R.id.shop_old_price)
    TextView shopOldPrice;
    @BindView(R.id.shop_de_tuangoudes)
    TextView shopDeTuangoudes;
    @BindView(R.id.shop_de_tuangouscore)
    TextView shopDeTuangouscore;
    @BindView(R.id.ll)
    LinearLayout ll;
    private List<Fragment> datas = new ArrayList<>();

    private String titles[] = {"商品详情", "商品评价"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setTitle("商品详情");
        title.setOnLeftListener(this);
        title.setRightImageResource(R.mipmap.icon_shop_share);
        title.setOnRightListener(this);
        shopDetailTeacher.setOnClickListener(this);
        shopDetailCart.setOnClickListener(this);
        shopDetailAddCart.setOnClickListener(this);
        shopDetailBuy.setOnClickListener(this);
        tabLayout.setupWithViewPager(viewPager);
        datas.add(new ShopDetailFragment());
        datas.add(new ShopCommentFragment());
        viewPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(), datas) {
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_detail_add_cart:
                startActivity(new Intent(mContext,ShopCarActivity.class));
                break;
            case R.id.shop_detail_buy:
                break;
            case R.id.shop_detail_teacher:
                startActivity(new Intent(mContext, TeacherDetailActivity.class));
                break;
            case R.id.shop_detail_cart:
                break;
            case R.id.left_text:
                finish();
                break;
            case R.id.right_text:
                break;
        }

    }
}
