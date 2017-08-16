package com.td.oldplay.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.HomeCourseFragment;
import com.td.oldplay.ui.forum.HomeForumFragment;
import com.td.oldplay.ui.live.LiveActivity;
import com.td.oldplay.ui.mine.HomeMyFragment;
import com.td.oldplay.ui.shop.HomeShopFragment;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.utils.PrefUtils;
import com.td.oldplay.utils.ToastUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;



public class MainActivity extends BaseFragmentActivity {
    @BindView(R.id.main_contianer)
    FrameLayout mainContianer;
    @BindView(R.id.rb_tab1)
    RadioButton rbTab1;
    @BindView(R.id.rb_tab2)
    RadioButton rbTab2;
    @BindView(R.id.rb_tab3)
    RadioButton rbTab3;
    @BindView(R.id.rb_tab4)
    RadioButton rbTab4;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    @BindView(R.id.main_root)
    LinearLayout mainRoot;
    @BindView(R.id.live)
    TextView live;
    private long exitTime = 0;
    private Fragment tabCourse;
    private Fragment tabShop;
    private Fragment tabForum;
    private Fragment tabMe;

    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        String jpush = PrefUtils.getString(mContext, MContants.PRE_SCORE_KEY, "");
        if (!TextUtils.isEmpty(jpush)) {
            Intent i = new Intent(mContext, RegisterScoreActivity.class);
            i.putExtra("title", jpush);
            startActivity(i);
            PrefUtils.putString(mContext, MContants.PRE_SCORE_KEY, "");
        }
    }

    private void init() {
        JPushInterface.setAlias(mContext, 1, userId);
        if (userBean != null && userBean.uType == 1) {
            live.setVisibility(View.VISIBLE);
            live.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, LiveActivity.class));
                }
            });
        } else {
            live.setVisibility(View.GONE);
        }

        transaction = getSupportFragmentManager().beginTransaction();
        tabMe=new HomeMyFragment();
        transaction.add(R.id.main_contianer, tabMe);
        transaction.show(tabMe).commit();


        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                transaction = getSupportFragmentManager().beginTransaction();
                hideAll(transaction);
                switch (checkedId) {
                    case R.id.rb_tab1:
                        if (tabCourse == null) {
                            tabCourse = new HomeCourseFragment();
                            transaction.add(R.id.main_contianer, tabCourse);
                        }
                        transaction.show(tabCourse).commit();
                        break;
                    case R.id.rb_tab2:
                        if (tabShop == null) {
                            tabShop = new HomeShopFragment();
                            transaction.add(R.id.main_contianer, tabShop);
                        }
                        transaction.show(tabShop).commit();
                        break;
                    case R.id.rb_tab3:
                        if (tabForum == null) {
                            tabForum = new HomeForumFragment();
                            transaction.add(R.id.main_contianer, tabForum);
                        }
                        transaction.show(tabForum).commit();
                        break;
                    case R.id.rb_tab4:
                        if (tabMe == null) {
                            tabMe = new HomeMyFragment();
                            transaction.add(R.id.main_contianer, tabMe);
                        }
                        transaction.show(tabMe).commit();
                        break;


                }

            }
        });
        rbTab1.setChecked(true);


    }

    private void hideAll(FragmentTransaction transaction) {
        if (tabCourse != null) {
            transaction.hide(tabCourse);
        }
        if (tabShop != null) {
            transaction.hide(tabShop);
        }
        if (tabForum != null) {
            transaction.hide(tabForum);
        }
        if (tabMe != null) {
            transaction.hide(tabMe);
        }

    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 1500) {
            ToastUtil.show("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
        super.onBackPressed();
    }


    public static boolean isForeground = false;
    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }
}
