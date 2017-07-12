package com.td.oldplay.ui.mine;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.ui.mine.activity.FeedBackActivity;
import com.td.oldplay.ui.mine.activity.MyAddressActivity;
import com.td.oldplay.ui.mine.activity.MyConcernActivity;
import com.td.oldplay.ui.mine.activity.MyCoureseActivity;
import com.td.oldplay.ui.mine.activity.MyForumActivity;
import com.td.oldplay.ui.mine.activity.MyOrdersActivity;
import com.td.oldplay.ui.mine.activity.MyPasswordActivity;
import com.td.oldplay.widget.CircleImageView;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeMyFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.title)
    CustomTitlebarLayout title;

    @BindView(R.id.mine_user_head_image)
    CircleImageView mineUserHeadImage;
    @BindView(R.id.mine_nickname)
    TextView mineNickname;
    @BindView(R.id.go_personal)
    ImageView goPersonal;

    @BindView(R.id.mime_info)
    RelativeLayout mimeInfo;

    @BindView(R.id.mine_concern)
    RelativeLayout mineConcern;

    @BindView(R.id.mine_class)
    RelativeLayout mineClass;

    @BindView(R.id.mine_orders)
    RelativeLayout mineOrders;

    @BindView(R.id.mine_address)
    RelativeLayout mineAddress;

    @BindView(R.id.mine_money)
    RelativeLayout mineMoney;

    @BindView(R.id.mine_forum)
    RelativeLayout mineForum;

    @BindView(R.id.mine_update_password)
    RelativeLayout mineUpdatePassword;


    @BindView(R.id.mine_setting)
    RelativeLayout mineSetting;

    @BindView(R.id.swipe_to_load_layout_mine)
    SwipeRefreshLayout swipeToLoadLayoutMine;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view) {
        title.setTitle("我的");
        title.setLeftGone();
        mimeInfo.setOnClickListener(this);
        mineAddress.setOnClickListener(this);
        mineClass.setOnClickListener(this);
        mineConcern.setOnClickListener(this);
        mineMoney.setOnClickListener(this);
        mineSetting.setOnClickListener(this);
        mineUpdatePassword.setOnClickListener(this);
        mineOrders.setOnClickListener(this);
        mineForum.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.mine_concern:
                intent = new Intent(mActivity, MyConcernActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_class:
                intent = new Intent(mActivity, MyCoureseActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_orders:
                intent = new Intent(mActivity, MyOrdersActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_address:
                intent = new Intent(mActivity, MyAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_money:
                break;
            case R.id.mine_update_password:
                intent = new Intent(mActivity, MyPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_setting:
                intent = new Intent(mActivity, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.mime_info:
                break;
            case R.id.mine_forum:
                intent = new Intent(mActivity, MyForumActivity.class);
                startActivity(intent);
                break;

        }
    }
}
