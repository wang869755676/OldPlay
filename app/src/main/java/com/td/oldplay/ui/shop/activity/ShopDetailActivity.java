package com.td.oldplay.ui.shop.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.base.adapter.BasePagerAdapter;
import com.td.oldplay.bean.CreateOrder;
import com.td.oldplay.bean.OrderBean;
import com.td.oldplay.bean.ShopDetail;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.activity.TeacherDetailActivity;
import com.td.oldplay.ui.shop.fragment.ShopCommentFragment;
import com.td.oldplay.ui.shop.fragment.ShopDetailFragment;
import com.td.oldplay.ui.window.SharePopupWindow;
import com.td.oldplay.ui.window.ShopCarDialog;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.td.oldplay.widget.countDown.CountdownView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopDetailActivity extends BaseFragmentActivity implements View.OnClickListener {


    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.shop_detail_banner)
    ImageView shopDetailBanner;
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

    @BindView(R.id.shop_de_tuangoudes)
    TextView shopDeTuangoudes;
    @BindView(R.id.shop_de_tuangouscore)
    TextView shopDeTuangouscore;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.rl_normal)
    RelativeLayout rlNormal;
    @BindView(R.id.shop_tuanghou_name)
    TextView shopTuanghouName;
    @BindView(R.id.shop_tuangou_price)
    TextView shopTuangouPrice;
    @BindView(R.id.shop_old_price)
    TextView shopOldPrice;
    @BindView(R.id.ishop_tuanggou_sell)
    TextView ishopTuanggouSell;
    @BindView(R.id.rl_tuangou)
    RelativeLayout rlTuangou;
    @BindView(R.id.shop_manjian_name)
    TextView shopManjianName;
    @BindView(R.id.shop_manjian_price)
    TextView shopManjianPrice;
    @BindView(R.id.ishop_manjian_sell)
    TextView ishopManjianSell;
    @BindView(R.id.shop_de_manjian)
    TextView shopDeManjian;
    @BindView(R.id.shop_de_manjianscore)
    TextView shopDeManjianscore;
    @BindView(R.id.rl_manjian)
    RelativeLayout rlManjian;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.cv_countdownView)
    CountdownView cvCountdownView;
    private List<Fragment> datas = new ArrayList<>();
    private ShopDetailFragment detailFragment;
    private String titles[] = {"商品详情", "商品评价"};

    private ShopCarDialog shopCarDialog;
    private ShopDetail bean;

    private int type; // 0加入购物车  1 立即购买

    private SharePopupWindow popupWindow;
    private String id;

    private HashMap<String, Object> params = new HashMap<>();

    private int carNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        id = getIntent().getStringExtra("id");
        params.put("userId", userId);
        params.put("goodsId", id);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        detailFragment = new ShopDetailFragment();
        datas.add(detailFragment);
        datas.add(new ShopCommentFragment());
        viewPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(), datas) {
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        shopOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        getData();

    }

    private void getData() {
        HttpManager.getInstance().getShopDetail(id, new HttpSubscriber<ShopDetail>(new OnResultCallBack<ShopDetail>() {

            @Override
            public void onSuccess(ShopDetail shopDetail) {
                bean = shopDetail;

                setData();
            }

            @Override
            public void onError(int code, String errorMsg) {

            }
        }));
    }

    private void setData() {
        if (bean != null) {
            if (bean.goods != null) {
                switch (bean.goods.isPreferential) {
                    case 0:
                        rlNormal.setVisibility(View.VISIBLE);
                        shopName.setText(bean.goods.goodsName + "");
                        shopPrice.setText(bean.goods.price + "");
                        itemShopSell.setText("月售 " + bean.goods.sellNum + "");
                        itemShopScore.setText("购买可得" + bean.goods.score + "积分");
                        break;
                    case 1:
                        rlManjian.setVisibility(View.VISIBLE);
                        shopManjianName.setText(bean.goods.goodsName + "");
                        shopManjianPrice.setText(bean.goods.price + "");
                        ishopManjianSell.setText("月售 " + bean.goods.sellNum + "");
                        shopDeManjianscore.setText("(购买可得" + bean.goods.score + "积分)");
                        if (bean.markDown != null) {
                            shopDeManjian.setText(bean.markDown.content);
                        }

                        break;
                    case 2:
                        shopDetailAddCart.setVisibility(View.GONE);
                        rlTuangou.setVisibility(View.VISIBLE);
                        shopTuanghouName.setText(bean.goods.goodsName + "");
                        // Log.e("===", TimeUtils.getTime(bean.groupBuy.startTime.time));
                        shopOldPrice.setText(bean.goods.price + "");
                        ishopTuanggouSell.setText("月售 " + bean.goods.sellNum + "");
                        shopDeTuangouscore.setText("(购买可得" + bean.goods.score + "积分)");
                        if (bean.groupBuy != null) {
                            shopTuangouPrice.setText("原价" + bean.groupBuy.price + "");
                            shopDeTuangoudes.setText(bean.groupBuy.content);
                            if (bean.groupBuy.endTime != null) {
                                cvCountdownView.start(bean.groupBuy.endTime.time - System.currentTimeMillis());
                            }
                        }

                        break;
                }
                GlideUtils.setImage(mContext, bean.goods.picUrl, shopDetailBanner);
                if (bean.goodsImageList != null) {
                    detailFragment.refreshData(bean.goodsImageList);
                }


            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_detail_add_cart:
                type = 0;
                if (shopCarDialog == null) {
                    shopCarDialog = new ShopCarDialog(mContext, this, bean, type);
                }
                shopCarDialog.show();
                break;
            case R.id.shop_detail_buy:
                type = 1;
                if (shopCarDialog == null) {
                    shopCarDialog = new ShopCarDialog(mContext, this, bean, type);
                }
                shopCarDialog.show();
                break;
            case R.id.shop_detail_teacher:
                startActivity(new Intent(mContext, TeacherDetailActivity.class));
                break;
            case R.id.shop_detail_cart:
                startActivity(new Intent(mContext, ShopCarActivity.class));
                break;
            case R.id.left_text:
                finish();
                break;
            case R.id.right_text:
                if (popupWindow == null) {
                    popupWindow =
                            new SharePopupWindow(mContext, MContants.SHARE_TITLE, MContants.SHARE_CONTENT, MContants.SHARE_URL, MContants.IMAG_URL);
                }
                popupWindow.showPopup(v);
                break;

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventMessage(EventMessage message) {
        if ("shop".equals(message.action)) {
            params.put("size", message.size);
            params.put("colorId", message.colorId);
            params.put("number", message.num);
            if (type == 0) {
                carNum = message.num;
                params.put("total", message.total);
                addCars();
            } else if (type == 1) {
                createOrders();
            }
        }


    }

    /**
     * 生成订单
     */
    private void createOrders() {
        showLoading("生成订单中");
        HttpManager.getInstance().createOrder(params, new HttpSubscriber<OrderBean>(new OnResultCallBack<OrderBean>() {

            @Override
            public void onSuccess(OrderBean orderBean) {
                hideLoading();
                if (orderBean != null) {
                    Intent intent = new Intent(mContext, OrderConfirmActivity.class);
                    intent.putExtra("model", orderBean);
                    startActivity(intent);
                }

            }

            @Override
            public void onError(int code, String errorMsg) {
                hideLoading();
                ToastUtil.show(errorMsg);
            }
        }));

    }

    /**
     * 添加到购物车中
     */
    private void addCars() {
        HttpManager.getInstance().addCar(params, new HttpSubscriber<String>(new OnResultCallBack<String>() {


            @Override
            public void onSuccess(String s) {
                shopDeCarNum.setVisibility(View.VISIBLE);
                shopDeCarNum.setText(carNum + "");
                ToastUtil.show("已加入购物车");
            }

            @Override
            public void onError(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
            }
        }));
    }
}
