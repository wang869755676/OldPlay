package com.td.oldplay.ui.course.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.td.oldplay.R;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.base.adapter.BasePagerAdapter;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.pay.weixin.WechatInfo;
import com.td.oldplay.pay.weixin.WeixPayUtils;
import com.td.oldplay.pay.zhifubao.PayResult;
import com.td.oldplay.permission.MPermission;
import com.td.oldplay.permission.annotation.OnMPermissionDenied;
import com.td.oldplay.permission.annotation.OnMPermissionGranted;
import com.td.oldplay.permission.annotation.OnMPermissionNeverAskAgain;
import com.td.oldplay.permission.util.MPermissionUtil;
import com.td.oldplay.ui.course.fragment.CommentFragment;
import com.td.oldplay.ui.course.fragment.CourseFragment;
import com.td.oldplay.ui.course.fragment.IntruceFragment;
import com.td.oldplay.ui.course.fragment.ShopFragment;
import com.td.oldplay.ui.live.LinkeNumberInfo;
import com.td.oldplay.ui.live.LiveBaseActivity;
import com.td.oldplay.ui.live.LiveHelper;
import com.td.oldplay.ui.shop.activity.OrdersConfirmActivity;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.ui.window.PayAlertDialog;
import com.td.oldplay.ui.window.PayTypeDialog;
import com.td.oldplay.ui.window.PayTypePopupWindow;
import com.td.oldplay.ui.window.SharePopupWindow;
import com.td.oldplay.utils.ScreenUtils;
import com.td.oldplay.utils.SoftInputUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.td.oldplay.widget.password.PasswordInputView;
import com.tencent.av.opengl.ui.GLView;
import com.tencent.av.sdk.AVView;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.tencent.ilivesdk.view.BaseVideoView;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.reactivex.disposables.Disposable;

public class TeacherDetailActivity extends LiveBaseActivity implements
        View.OnClickListener,
        CustomDialog.DialogClick,
        PayTypePopupWindow.payTypeAction {

    private static final String TAG = "TeacherDetailActivity";
    public CourseBean currentBean;
    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.av_root_view)
    AVRootView avRootView;
    @BindView(R.id.reword)
    ImageView reword;
    @BindView(R.id.join_con)
    CheckBox joinCon;
    @BindView(R.id.landan)
    ImageView landan;
    @BindView(R.id.pause)
    ImageView pause;
    @BindView(R.id.videoplayer)
    JCVideoPlayerStandard videoplayer;
    @BindView(R.id.dianbo_back)
    ImageView dianboBack;
    @BindView(R.id.dianbo)
    FrameLayout dianbo;
    @BindView(R.id.live_root)
    FrameLayout liveRoot;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.concern_action)
    TextView concernAction;
    @BindView(R.id.rl_concern_action)
    RelativeLayout rlConcernAction;
    @BindView(R.id.share_action)
    TextView shareAction;
    @BindView(R.id.rl_collection_action)
    RelativeLayout rlCollectionAction;
    @BindView(R.id.collection_action)
    TextView collectionAction;
    @BindView(R.id.ll_action)
    LinearLayout llAction;
    @BindView(R.id.comment_ed)
    EditText commentEd;
    @BindView(R.id.comment_send)
    TextView commentSend;
    @BindView(R.id.ll_comment)
    LinearLayout llComment;
    @BindView(R.id.invite_view1)
    TextView inviteView1;
    @BindView(R.id.no_live)
    TextView noLive;
    private CustomDialog customDialog;

    private boolean mIsActivityPaused;
    private boolean mIsConferenceStarted = false; // 是否连麦

    private boolean isPermissionGrant;
    private List<Fragment> fragments;
    private CommentFragment commentFragment;
    private LinearLayout.LayoutParams params;
    private boolean island;
    private String[] titles;
    private SharePopupWindow popupWindow;
    private String teacherId;
    private int type;

    private CustomDialog RewordDialog;
    private View RewordDialogView;
    private EditText RewordDialogEd;
    private String Rewordmoney = "";

    private PayTypePopupWindow payTypePopupWindow;
    private boolean isPayFromRewoard;

    private PayTypeDialog payTypeDialog;
    //private PayAlertDialog accountDialog;
    private PayAlertDialog paySuccessDialog;
    private CustomDialog passwordDialog;


    private PasswordInputView passwordInputView;
    private View dialogView;
    private String password = "";

    private CustomDialog AlerDialog;

    private boolean isTeacherDetialPay;

    private String courseId;


    private String backGroundId;
    private LiveHelper mLiveHelper;
    private boolean isCreate;
    private boolean isJoin;

    private HashMap<String, Object> param;
    private IWXAPI api;
    private boolean isLiveing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        api = WXAPIFactory.createWXAPI(this, MContants.WX_APP_ID);
        param = new HashMap<>();
        teacherId = getIntent().getStringExtra("id");
        courseId = getIntent().getStringExtra("courseId");
        customDialog = new CustomDialog(mContext);
        customDialog.setTitleVisible(View.GONE);
        customDialog.setDialogClick(this);
        landan.setOnClickListener(this);
        joinCon.setOnClickListener(this);
        concernAction.setOnClickListener(this);
        collectionAction.setOnClickListener(this);
        shareAction.setOnClickListener(this);
        reword.setOnClickListener(this);
        pause.setOnClickListener(this);
        commentSend.setOnClickListener(this);
        dianboBack.setOnClickListener(this);
        title.setTitle("讲师直播");
        title.setOnLeftListener(this);
        params = (LinearLayout.LayoutParams) liveRoot.getLayoutParams();
        mLiveHelper = new LiveHelper(this, this);
        initDialog();
        initViewPager();
        initView();
        requestLivePermission(); // 请求权限
        getData();
    }

    private void initView() {

        //TODO 设置渲染层
        ILVLiveManager.getInstance().setAvVideoView(avRootView);

        //avRootView.setBackground(R.mipmap.renderback);
        avRootView.setGravity(AVRootView.LAYOUT_GRAVITY_RIGHT);
        // avRootView.setAutoOrientation(false);
        avRootView.setSubMarginY(getResources().getDimensionPixelSize(R.dimen.small_area_margin_top));
        avRootView.setSubMarginX(getResources().getDimensionPixelSize(R.dimen.small_area_marginright));
        avRootView.setSubPadding(getResources().getDimensionPixelSize(R.dimen.small_area_marginbetween));
        avRootView.setSubWidth(getResources().getDimensionPixelSize(R.dimen.small_area_height));
        avRootView.setSubHeight(getResources().getDimensionPixelSize(R.dimen.small_area_height));
        avRootView.setSubCreatedListener(new AVRootView.onSubViewCreatedListener() {
            @Override
            public void onSubViewCreated() {
                for (int i = 1; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
                    final int index = i;
                    AVVideoView avVideoView = avRootView.getViewByIndex(index);
                    avVideoView.setRotate(false);
                    avVideoView.setGestureListener(new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            avRootView.swapVideoView(0, index);
                            backGroundId = avRootView.getViewByIndex(0).getIdentifier();
                            return super.onSingleTapConfirmed(e);
                        }
                    });
                }

                avRootView.getViewByIndex(0).setRotate(false);


            }
        });
    }

    private void initDialog() {
        payTypeDialog = new PayTypeDialog(mContext, false);
        payTypeDialog.setScoreVisisble(View.GONE);
        payTypeDialog.setDialogClick(new PayTypeDialog.DialogClick() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk(int payType, String scoreId) {
                param.put("userId", userId);
                param.put("teacherId", teacherId);
                if (isPayFromRewoard) {
                    param.put("price", Rewordmoney);
                } else {
                    param.put("price", joinMoney);
                }
                switch (payType) {
                    case 0:
                        if (passwordDialog != null) {
                            passwordDialog.show();
                        }
                        break;
                    case 1:
                        isTeacherDetialPay = true;
                        ToastUtil.show("微信支付");
                        payWechat();
                        break;
                    case 2:
                        ToastUtil.show("支付宝支付");
                        payZhifubao();
                        break;

                }

            }
        });


        RewordDialog = new CustomDialog(mContext);
        RewordDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_reword_money, null);
        RewordDialogEd = (EditText) RewordDialogView.findViewById(R.id.dialog_money_ed);
        RewordDialog.setContanier(RewordDialogView);
        RewordDialog.setViewLineVisible(View.GONE);
        RewordDialog.setTitleVisible(View.GONE);
        RewordDialog.setDialogClick(new CustomDialog.DialogClick() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                Rewordmoney = RewordDialogEd.getText().toString();
                if (TextUtils.isEmpty(Rewordmoney)) {
                    ToastUtil.show("请输入打赏的金额");
                    return;
                } else {
                    // 请求打赏 ，然后支付
                    RewordDialog.dismiss();
                    payTypeDialog.setTitle("打赏: " + Rewordmoney + "元");
                    payTypeDialog.show();
                }

            }
        });


        paySuccessDialog = new PayAlertDialog(mContext, false, false);
        paySuccessDialog.setContent("支付成功");
        paySuccessDialog.setDialogClick(new PayAlertDialog.DialogClick() {
            @Override
            public void onBack() {

            }

            @Override
            public void onnext() {
                if (!isPayFromRewoard) {
                    //reQuestLink();
                    mLiveHelper.upMemberVideo();
                }
            }
        });

        passwordDialog = new CustomDialog(mContext);
        passwordDialog.setTitle("输入密码");
        passwordDialog.setCancelTv("返回");
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_password, null);
        passwordInputView = (PasswordInputView) dialogView.findViewById(R.id.password);
        passwordDialog.setContanier(dialogView);
        passwordDialog.setDialogClick(new CustomDialog.DialogClick() {
            @Override
            public void onCancel() {
                if (payTypeDialog != null) {
                    payTypeDialog.show();
                }
            }

            @Override
            public void onOk() {
                password = passwordInputView.getText().toString();

                if (TextUtils.isEmpty(password)) {
                    ToastUtil.show("请输入密码");
                    return;
                }
                passwordDialog.dismiss();
           /*     if (isPayFromRewoard) {
                    ToastUtil.show("账户支付打赏");
                    param.put("type",1);

                } else {
                    ToastUtil.show("账户连麦");
                    param.put("type",1);
                    paySuccessDialog.setOkStr("开始连麦");
                }*/
                payAccout();


            }
        });


        AlerDialog = new CustomDialog(mContext);
        AlerDialog.setTitleVisible(View.GONE);
        AlerDialog.setTitleVisible(View.GONE);

    /*    accountDialog = new PayAlertDialog(mContext, true, true);
        accountDialog.setDialogClick(new PayAlertDialog.DialogClick() {
            @Override
            public void onBack() {
                if (payTypeDialog != null) {
                    payTypeDialog.show();
                }
            }

            @Override
            public void onnext() {
                if (passwordDialog != null) {
                    passwordDialog.show();
                }
            }
        });
*/
    }

    private void getData() {
        HttpManager.getInstance().isConcernTeacher(userId, teacherId, new HttpSubscriber<Integer>(new OnResultCallBack<Integer>() {

            @Override
            public void onSuccess(Integer integer) {
                if (integer == 1) {
                    concernAction.setText("已关注");
                    concernAction.setEnabled(false);
                } else {
                    concernAction.setText("关注");
                    concernAction.setEnabled(true);
                }
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
    }

    private int position;
    private String[] paths =

            {
                    "http://video.jiecao.fm/8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4",
                    "http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4",
                    "http://video.jiecao.fm/8/18/%E5%A4%A7%E5%AD%A6.mp4",};

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(EventMessage message) {
        if ("changeCourseVideo".equals(message.action)) {
            ILiveRoomManager.getInstance().onPause();
            dianbo.setVisibility(View.VISIBLE);
            noLive.setVisibility(View.GONE);
            videoplayer.setUp(paths[position], JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
            videoplayer.startVideo();
            position++;
        } else if ("WX".equals(message.action)) {
            if (isTeacherDetialPay) {
                switch (message.WxPayCode) {
                    case 0:
                        ToastUtil.show("支付成功！");
                        if (isPayFromRewoard) {
                            ToastUtil.show("微信支付打赏");

                        } else {
                            ToastUtil.show("微信宝连麦");
                            paySuccessDialog.setOkStr("开始连麦");
                        }
                        if (paySuccessDialog != null) {
                            paySuccessDialog.show();

                        }
                        isTeacherDetialPay = false;
                        break;
                    case -1:
                        payTypeDialog.dismiss();
                        ToastUtil.show("支付失败！");
                        break;
                    case -2:
                        ToastUtil.show("您取消了支付！");
                        break;

                    default:

                        ToastUtil.show("支付失败！");
                        break;

                }
            }

        } else if ("live".equals(message.action)) {
            hostCreateRoom();
        }
    }

    /**
     * 初始化Viewpager
     */
    private void initViewPager() {
        titles = getResources().getStringArray(R.array.title);
        tabLayout.setupWithViewPager(viewPager);
        fragments = new ArrayList<>();
        fragments.add(new CourseFragment());
        fragments.add(new IntruceFragment());
        fragments.add(new ShopFragment());
        commentFragment = new CommentFragment();
        fragments.add(commentFragment);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(), fragments) {
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    llComment.setVisibility(View.VISIBLE);
                    llAction.setVisibility(View.GONE);
                } else {
                    llComment.setVisibility(View.GONE);
                    llAction.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    protected int getLayout() {
        return R.layout.activity_teacher_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join_con:

                if (isLinking) {
                    AlerDialog.setContent("确定与主播断开连麦?");
                    AlerDialog.setDialogClick(new CustomDialog.DialogClick() {
                        @Override
                        public void onCancel() {
                            AlerDialog.dismiss();
                        }

                        @Override
                        public void onOk() {
                            AlerDialog.dismiss();
                            mLiveHelper.sendGroupCmd(MContants.AVIMCMD_MULTI_CANCEL_INTERACT, userId);
                            avRootView.closeUserView(userId, AVView.VIDEO_SRC_TYPE_CAMERA, true);
                        }
                    });
                    AlerDialog.show();
                } else {

                    reQuestLink();
                    //向主播发送连麦请求
                }


                break;
            case R.id.landan:
                setRequestedOrientation(island ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //mRTCStreamingManager.notifyActivityOrientationChanged();
                Log.e("===", avRootView.getViewByIndex(1).getPosHeight() + "   " + avRootView.getViewByIndex(1).getPosWidth() + avRootView.getViewByIndex(0).getPosWidth());
                if (island) {
                    title.setVisibility(View.VISIBLE);
                    if (viewPager.getCurrentItem() == 3) {
                        llComment.setVisibility(View.VISIBLE);
                    } else {
                        llAction.setVisibility(View.VISIBLE);
                    }
                    params.height = ScreenUtils.dip2px(AContext, 200);
                    avRootView.getViewByIndex(0).setPosWidth(ScreenUtils.getScreenH(this));
                    avRootView.getViewByIndex(0).setPosHeight(ScreenUtils.dip2px(AContext, 200));
                    avRootView.getViewByIndex(0).autoLayout();


                    avRootView.setGravity(AVRootView.LAYOUT_GRAVITY_BOTTOM);
                    // avRootView.getViewByIndex(1).setPosWidth(getResources().getDimensionPixelSize(R.dimen.small_area_width));
                    // avRootView.getViewByIndex(1).setPosHeight(getResources().getDimensionPixelSize(R.dimen.small_area_height));
                    avRootView.getViewByIndex(1).setPosLeft(ScreenUtils.getScreenH(this) - getResources().getDimensionPixelSize(R.dimen.small_area_height) - 20);
                    avRootView.getViewByIndex(1).autoLayout();


                } else {
                    title.setVisibility(View.GONE);
                    if (viewPager.getCurrentItem() == 3) {
                        llComment.setVisibility(View.GONE);
                    } else {
                        llAction.setVisibility(View.GONE);
                    }
                    params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    avRootView.getViewByIndex(0).setPosWidth(ScreenUtils.getScreenW(this));
                    avRootView.getViewByIndex(0).setPosHeight(ScreenUtils.getScreenH(this));
                    avRootView.getViewByIndex(0).autoLayout();


                    avRootView.setGravity(AVRootView.LAYOUT_GRAVITY_BOTTOM);
                    //avRootView.getViewByIndex(1).setPosWidth(getResources().getDimensionPixelSize(R.dimen.small_area_height));
                    //  avRootView.getViewByIndex(1).setPosHeight(getResources().getDimensionPixelSize(R.dimen.small_area_width));
                    avRootView.getViewByIndex(1).setPosLeft(ScreenUtils.getScreenW(this) - getResources().getDimensionPixelSize(R.dimen.small_area_height) - 20);
                    avRootView.getViewByIndex(1).autoLayout();
                }
                liveRoot.setLayoutParams(params);
                island = !island;
                break;
            case R.id.collection_action:
                break;
            case R.id.concern_action:
                type = 1;
                HttpManager.getInstance().concernTeacher(userId, teacherId, httpSubscriber);
                break;
            case R.id.share_action:
                if (popupWindow == null) {
                    popupWindow = new SharePopupWindow(mContext, "老年人", "老年人", "", "");
                }
                popupWindow.showPopup(v);
                break;
            case R.id.reword:
                isPayFromRewoard = true;
                RewordDialog.show();
                break;
            case R.id.pause:
                //mRTCStreamingManager.sto
                break;
            case R.id.left_text:
                onBackPressed();
                //  finish();
                break;
            case R.id.comment_send:
                type = 2;
                if (TextUtils.isEmpty(commentEd.getText().toString())) {
                    ToastUtil.show("请输入评论的内容");
                }
                HttpManager.getInstance().commentTeacher(userId, teacherId, commentEd.getText().toString(), httpSubscriber);
                break;
            case R.id.dianbo_back:
                JCVideoPlayer.releaseAllVideos();
                dianbo.setVisibility(View.GONE);
                ILiveRoomManager.getInstance().onResume();
                if (!isLiveing) {
                    noLive.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ILiveRoomManager.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        ILiveRoomManager.getInstance().onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mLiveHelper.onDestory();

    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        if (island) {
            setRequestedOrientation(island ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            island = false;
            title.setVisibility(View.VISIBLE);
            if (viewPager.getCurrentItem() == 3) {
                llComment.setVisibility(View.VISIBLE);
            } else {
                llAction.setVisibility(View.VISIBLE);
            }
            params.height = ScreenUtils.dip2px(AContext, 200);
            liveRoot.setLayoutParams(params);
        } else {
            AlerDialog.setContent("是否退出房间？");
            AlerDialog.setDialogClick(new CustomDialog.DialogClick() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onOk() {
                    AlerDialog.dismiss();
                    showLoading("正在退出中，请稍后.");
                    HttpManager.getInstance().quitRoom(userId, teacherId, new HttpSubscriber<String>(new OnResultCallBack<String>() {


                        @Override
                        public void onSuccess(String s) {
                            mLiveHelper.sendC2CCmd(MContants.ACCOUNT_TYPE, "", teacherId, null);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mLiveHelper.startExitRoom();
                                }
                            }).start();
                        }

                        @Override
                        public void onError(int code, String errorMsg) {
                            ToastUtil.show(errorMsg);
                            hideLoading();
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }
                    }));


                }
            });
            AlerDialog.show();

            // super.onBackPressed();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /***********************
     * 录音摄像头权限申请
     *******************************/

    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGranted() {
        isPermissionGrant = true;
        mLiveHelper.joinRoom("1899");
    }

    @OnMPermissionDenied(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, LIVE_PERMISSIONS);
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法连麦";
        ToastUtil.show(tip);
    }

    @OnMPermissionNeverAskAgain(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, LIVE_PERMISSIONS);
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, LIVE_PERMISSIONS);
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }

        ToastUtil.show(sb);
    }


    @Override
    public void onCancel() {

    }

    @Override
    public void onOk() {

        payTypeDialog.setTitle("支付" + joinMoney + "元与直播连麦");
        payTypeDialog.setScoreVisisble(View.GONE);
        payTypeDialog.show();
       /* if (joinCon.isChecked()) {
            startConference();  // 支付成功后开通连麦
        } else {

            stopConference();
            hideOnMis();
        }
*/
    }


    private HttpSubscriber<String> httpSubscriber = new HttpSubscriber<>(new OnResultCallBack<String>() {

        @Override
        public void onSuccess(String s) {
            if (type == 1) {
                ToastUtil.show("关注成功");
                concernAction.setText("已关注");
                concernAction.setEnabled(false);
            } else if (type == 2) {
                ToastUtil.show("评论成功");
                if (commentFragment != null)
                    commentFragment.onRefresh();
                commentEd.setText("");
                SoftInputUtils.hideSoftInput(AContext, getWindow());
            }
        }

        @Override
        public void onError(int code, String errorMsg) {
            ToastUtil.show(errorMsg);
        }

        @Override
        public void onSubscribe(Disposable d) {
            addDisposable(d);
        }
    });

    @Override
    public void onPayType(int viewId) {
        switch (viewId) {
            case R.id.iv_wx_pay:
                ToastUtil.show("微信支付");
                break;
            case R.id.iv_zhifubao_pay:
                ToastUtil.show("支付宝支付");
                break;
            case R.id.iv_teacher_pay:
                ToastUtil.show("找老师开通");
                break;
        }
    }


    /**
     * 获得连麦的金额数
     */
    private float joinMoney;

    private void getLianmaiMoney() {
        HttpManager.getInstance().getJoinMoney(userId, new HttpSubscriber<Float>(new OnResultCallBack<Float>() {

            @Override
            public void onSuccess(Float aFloat) {
                joinMoney = aFloat;
                if (aFloat == 0) {
                    ToastUtil.show("主播还没有开启连麦");
                } else {
                    customDialog.setContent("支付" + aFloat + "元连麦");
                    customDialog.show();
                }

            }

            @Override
            public void onError(int code, String errorMsg) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));
    }


    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if (isPayFromRewoard) {
                            ToastUtil.show("支付宝支付打赏");

                        } else {
                            ToastUtil.show("连麦");
                            paySuccessDialog.setOkStr("开始连麦");
                        }
                        if (paySuccessDialog != null) {
                            paySuccessDialog.show();
                        }
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show("支付失败");
                        payTypeDialog.dismiss();
                    }
                    break;
                }

                default:
                    break;
            }
        }

        ;
    };


    @Override
    public void enterRoomComplete(boolean b) {
        isCreate = true;
        isJoin = true;
        isJoin=true;
        noLive.setVisibility(View.GONE);
        // 重置美颜
        ILiveRoomManager.getInstance().enableBeauty(5);
        ILiveRoomManager.getInstance().enableWhite(5);
        avRootView.getViewByIndex(0).setVisibility(GLView.VISIBLE);

        // 通知服务器加入房间  然后发送消息 到主播 加入房间 主播端刷新列表

        //发消息通知上线
        mLiveHelper.sendC2CCmd(MContants.AVIMCMD_ENTERLIVE, "", teacherId, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }

    @Override
    public void quiteRoomComplete(boolean b, Object o) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isCreate) {
                    isCreate = true;
                    noLive.setVisibility(View.VISIBLE);
                } else {
                    hideLoading();
                    avRootView.clearUserView();
                    finish();
                }
            }
        });


    }

    @Override
    public void showInviteDialog(LinkeNumberInfo identifier) {

    }

    @Override
    public void cancelInviteView(String identifier) {
        // getLianmaiMoney();  // 支付连麦

        mLiveHelper.upMemberVideo();
        isPayFromRewoard = false;
        if ((inviteView1 != null)) {
            inviteView1.setVisibility(View.GONE);
            isLinking = true;
        }
    }

    private void reQuestLink() {
        mLiveHelper.sendC2CCmd(MContants.AVIMCMD_MUlTI_HOST_INVITE, "", teacherId, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                showLinkView();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.show(errMsg);
            }
        });
    }

    private void showLinkView() {
        inviteView1.setVisibility(View.VISIBLE);
        inviteView1.setText("等待中");
    }

    private void hideLinkView() {
        inviteView1.setVisibility(View.GONE);
    }

    public void forceQuitRoom() {
        isLiveing=false;
        ILiveRoomManager.getInstance().onPause();
        noLive.setVisibility(View.VISIBLE);
    }

    @Override
    public void hostCreateRoom() {
        ToastUtil.show("主播创建房间了");
        noLive.setVisibility(View.GONE);
        if (!isJoin) {
            mLiveHelper.joinRoom("1899");
        }

    }


    private void payZhifubao() {
        if (isPayFromRewoard) {
            param.put("type", 1);
            paySuccessDialog.setOkStr("确定");
        } else {
            param.put("type", 2);
            paySuccessDialog.setOkStr("开始连麦");
        }
        HttpManager.getInstance().payZhifubao(param, new HttpSubscriber<String>(new OnResultCallBack<String>() {

            @Override
            public void onSuccess(final String s) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(TeacherDetailActivity.this);
                        Map<String, String> result = alipay.payV2(s, true);
                        Log.i("msp", result.toString());
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                }).start();

            }

            @Override
            public void onError(int code, String errorMsg) {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }
        }));
    }

    private void payWechat() {
        if (isPayFromRewoard) {
            param.put("type", 1);
            paySuccessDialog.setOkStr("确定");
        } else {
            param.put("type", 2);
            paySuccessDialog.setOkStr("开始连麦");
        }
        HttpManager.getInstance().payWechat(param, new HttpSubscriber<WechatInfo>(new OnResultCallBack<WechatInfo>() {

            @Override
            public void onSuccess(WechatInfo wechatInfo) {
                WeixPayUtils.pay(api, wechatInfo);

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
    }

    private void payAccout() {
        if (isPayFromRewoard) {
            param.put("type", 1);
            paySuccessDialog.setOkStr("确定");
        } else {
            param.put("type", 2);
            paySuccessDialog.setOkStr("开始连麦");
        }
        param.put("payPassword", password);
        HttpManager.getInstance().payAccount(param, new HttpSubscriber<UserBean>(new OnResultCallBack<UserBean>() {
            @Override
            public void onSuccess(UserBean userBean) {
                spUilts.setUser(userBean);
                // 账户支付
                if (paySuccessDialog != null) {
                    paySuccessDialog.show();
                }

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
    }

    private boolean isGoodCars;


}
