package com.td.oldplay.ui.mine;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.LoginActivity;
import com.td.oldplay.ui.live.LiveHelper;
import com.td.oldplay.ui.live.LiveLoginHelper;
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
import com.td.oldplay.ui.window.SharePopupWindow;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.utils.ShareSDKUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CircleImageView;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.td.oldplay.widget.goodView.GoodView;
import com.tencent.ilivesdk.ILiveCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.wechat.friends.Wechat;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeMyFragment extends BaseFragment implements View.OnClickListener, PopupWindow.OnDismissListener {


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
    @BindView(R.id.mine_share_image)
    ImageView mineShareImage;
    @BindView(R.id.mine_share_text)
    TextView mineShareText;
    @BindView(R.id.mine_share)
    RelativeLayout mineShare;
    @BindView(R.id.mine_loginout)
    RelativeLayout mineLoginout;

    private SharePopupWindow popupWindow;

    private boolean isScoreAdd;
    private GoodView goodView;

    public int currentScore;
    private boolean isFirst = true;
    private boolean isHidden = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        goodView = new GoodView(mActivity);
        goodView.setOnDismissListener(this);
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
        } else if ("userAdd".equals(message.action)) {
            // 刷新ui
            isScoreAdd = true;
            refreshUser();
        } else if ("user".equals(message.action)) {

        }
    }

    private void showAddAnimation() {
        if (isScoreAdd && !isHidden) {
            goodView.setText("+" + (userBean.score - currentScore));
            goodView.setTextColor(getResources().getColor(R.color.tv_red));
            goodView.setTextSize(20);
            goodView.show(mineScoree);
        }
    }


    private void refreshUser() {
        showAddAnimation();
    }


    private void setUser() {
        if (userBean != null) {
            currentScore = userBean.score;
            GlideUtils.setAvatorImage(mActivity, userBean.avatar, mineUserHeadImage);
            mineNickname.setText(userBean.nickName);
            mineScoree.setText("积分: " + userBean.score);

        }

    }

    @Override
    protected void init(View view) {
        title.setTitle("我的");
        title.setLeftGone();
        title.setRightImageResource(R.mipmap.icon_shop_share);
        title.setOnRightListener(this);
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
        mineShare.setOnClickListener(this);
        mineLoginout.setOnClickListener(this);
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
                intent.putExtra("model", userBean);
                startActivity(intent);
                break;
            case R.id.right_text:
                if (popupWindow == null) {
                    popupWindow = new SharePopupWindow(mActivity, "", "", "", "");
                }
                popupWindow.showPopup(v);
                break;
            case R.id.mine_share:
                if (popupWindow == null) {
                    popupWindow = new SharePopupWindow(mActivity, "", "", "", "");
                }
                popupWindow.showPopup(v);
                break;
            case R.id.mine_loginout:
                HttpManager.getInstance().loginOut(new HttpSubscriber<String>(new OnResultCallBack<String>() {

                    @Override
                    public void onSuccess(String s) {
                        LiveLoginHelper.iLiveLogout(new ILiveCallBack() {
                            @Override
                            public void onSuccess(Object data) {
                                ToastUtil.show("退出成功");
                                JPushInterface.deleteAlias(mActivity, 2);
                                spUilts.clearSharedPreferences();
                                // JPushInterface.re
                                ShareSDKUtils.loginOut(Wechat.NAME);
                                startActivity(new Intent(mActivity, LoginActivity.class));
                                if (mActivity != null)
                                    mActivity.finish();
                            }

                            @Override
                            public void onError(String module, int errCode, String errMsg) {
                                ToastUtil.show(errMsg);
                            }
                        });

                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        ToastUtil.show(errorMsg);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);

                    }
                }));

                break;
        }
    }

    @Override
    public void onDismiss() {
        isScoreAdd = false;
        setUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        //showAddAnimation();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        showAddAnimation();
    }
}
