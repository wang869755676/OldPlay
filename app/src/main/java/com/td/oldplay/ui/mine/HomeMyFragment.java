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
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.ui.mine.activity.AboutActivity;
import com.td.oldplay.ui.mine.activity.FeedBackActivity;
import com.td.oldplay.ui.mine.activity.MyAddressActivity;
import com.td.oldplay.ui.mine.activity.MyConcernActivity;
import com.td.oldplay.ui.mine.activity.MyCoureseActivity;
import com.td.oldplay.ui.mine.activity.MyForumActivity;
import com.td.oldplay.ui.mine.activity.MyOrdersActivity;
import com.td.oldplay.ui.mine.activity.MyPasswordActivity;
import com.td.oldplay.ui.mine.activity.MyWalletActivity;
import com.td.oldplay.ui.mine.activity.PersonDetailActivity;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.widget.CircleImageView;
import com.td.oldplay.widget.CustomTitlebarLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.mine_concern_image)
    ImageView mineConcernImage;
    @BindView(R.id.mine_concern_text)
    TextView mineConcernText;
    @BindView(R.id.go_concern)
    ImageView goConcern;
    @BindView(R.id.mine_notice_image)
    ImageView mineNoticeImage;
    @BindView(R.id.mine_notice_text)
    TextView mineNoticeText;
    @BindView(R.id.go_notice)
    ImageView goNotice;
    @BindView(R.id.mine_fans_image)
    ImageView mineFansImage;
    @BindView(R.id.mine_fans_text)
    TextView mineFansText;
    @BindView(R.id.go_fans)
    ImageView goFans;
    @BindView(R.id.mine_collection_image)
    ImageView mineCollectionImage;
    @BindView(R.id.mine_collection_text)
    TextView mineCollectionText;
    @BindView(R.id.go_collection)
    ImageView goCollection;
    @BindView(R.id.mine_already_image)
    ImageView mineAlreadyImage;
    @BindView(R.id.mine_already_text)
    TextView mineAlreadyText;
    @BindView(R.id.go_already)
    ImageView goAlready;
    @BindView(R.id.mine_concern_tag_image)
    ImageView mineConcernTagImage;
    @BindView(R.id.mine_concern_tag_text)
    TextView mineConcernTagText;
    @BindView(R.id.go_concern_tag)
    ImageView goConcernTag;
    @BindView(R.id.mine_already_photo_image)
    ImageView mineAlreadyPhotoImage;
    @BindView(R.id.mine_already_photo_text)
    TextView mineAlreadyPhotoText;
    @BindView(R.id.go_already_photo)
    ImageView goAlreadyPhoto;
    @BindView(R.id.mine_setting_image)
    ImageView mineSettingImage;
    @BindView(R.id.mine_setting_text)
    TextView mineSettingText;
    @BindView(R.id.mine_about_image)
    ImageView mineAboutImage;
    @BindView(R.id.mine_about_text)
    TextView mineAboutText;
    @BindView(R.id.go_setting)
    ImageView goSetting;
    @BindView(R.id.mine_about)
    RelativeLayout mineAbout;
    @BindView(R.id.swipe_target)
    NestedScrollView swipeTarget;
    @BindView(R.id.mine_scoree)
    TextView mineScoree;
    @BindView(R.id.go_about)
    ImageView goAbout;
    private UserBean user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setUser();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handEventMessage(EventMessage message) {
        if ("userUpdate".equals(message.action)) {
            // mineUserHeadImage
            setUser();


        }
    }

    private void setUser() {
        if (user != null) {
            GlideUtils.setAvatorImage(mActivity, user.avatar, mineUserHeadImage);
            mineNickname.setText(user.nickName);
            mineScoree.setText(user.score + "");
        }

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
        mineAbout.setOnClickListener(this);
        mineUserHeadImage.setOnClickListener(this);
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
                intent = new Intent(mActivity, MyWalletActivity.class);
                startActivity(intent);
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
            case R.id.mine_about:
                intent = new Intent(mActivity, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_user_head_image:
                intent = new Intent(mActivity, PersonDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
