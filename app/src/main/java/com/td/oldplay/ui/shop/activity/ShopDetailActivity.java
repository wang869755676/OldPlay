package com.td.oldplay.ui.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    @BindView(R.id.shop_detail_banner)
    MZBannerView shopDetailBanner;
    @BindView(R.id.rb_detail)
    RadioButton rbDetail;
    @BindView(R.id.rb_comment)
    RadioButton rbComment;
    @BindView(R.id.rg_segment)
    RadioGroup rgSegment;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.shop_detail_teacher)
    TextView shopDetailTeacher;
    @BindView(R.id.shop_detail_cart)
    ImageView shopDetailCart;
    @BindView(R.id.shop_detail_add_cart)
    TextView shopDetailAddCart;
    @BindView(R.id.shop_detail_buy)
    TextView shopDetailBuy;
    private List<Fragment> datas=new ArrayList<>();

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
        shopDetailTeacher.setOnClickListener(this);
        shopDetailCart.setOnClickListener(this);
        shopDetailAddCart.setOnClickListener(this);
        shopDetailBuy.setOnClickListener(this);
        datas.add(new ShopDetailFragment());
        datas.add(new CommentFragment());
        viewPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(), datas));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rbDetail.setChecked(true);
                        break;
                    case 1:
                        rbComment.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rgSegment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_detail:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_comment:
                        viewPager.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_detail_add_cart:
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
        }

    }
}
