package com.td.oldplay.ui.course.activity;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceState;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceStateChangedListener;
import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;
import com.qiniu.pili.droid.rtcstreaming.RTCRemoteWindowEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCStartConferenceCallback;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;
import com.td.oldplay.R;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.base.adapter.BasePagerAdapter;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.MessageEvent;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.permission.MPermission;
import com.td.oldplay.permission.annotation.OnMPermissionDenied;
import com.td.oldplay.permission.annotation.OnMPermissionGranted;
import com.td.oldplay.permission.annotation.OnMPermissionNeverAskAgain;
import com.td.oldplay.permission.util.MPermissionUtil;
import com.td.oldplay.ui.course.fragment.CommentFragment;
import com.td.oldplay.ui.course.fragment.CourseFragment;
import com.td.oldplay.ui.course.fragment.IntruceFragment;
import com.td.oldplay.ui.course.fragment.ShopFragment;
import com.td.oldplay.ui.live.LiveBaseActivity;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.ui.window.PayTypePopupWindow;
import com.td.oldplay.ui.window.SharePopupWindow;
import com.td.oldplay.utils.LiveUtils;
import com.td.oldplay.utils.ScreenUtils;
import com.td.oldplay.utils.SoftInputUtils;
import com.td.oldplay.utils.StreamUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.reactivex.disposables.Disposable;

public class TeacherDetailActivity extends LiveBaseActivity implements
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        PLMediaPlayer.OnErrorListener,
        View.OnClickListener,
        CustomDialog.DialogClick,
        PayTypePopupWindow.payTypeAction {

    private static final String TAG = "TeacherDetailActivity";
    public CourseBean currentBean;
    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.au_videoview)
    PLVideoView auVideoview;
    @BindView(R.id.reword)
    ImageView reword;
    @BindView(R.id.join_con)
    CheckBox joinCon;
    @BindView(R.id.landan)
    ImageView landan;
    @BindView(R.id.pause)
    ImageView pause;
    @BindView(R.id.live_sfv)
    GLSurfaceView liveSfv;
    @BindView(R.id.live_afl)
    AspectFrameLayout liveAfl;
    @BindView(R.id.live_gfv_winow)
    GLSurfaceView liveGfvWinow;
    @BindView(R.id.live_fl_window)
    FrameLayout liveFlWindow;
    @BindView(R.id.live)
    FrameLayout live;
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
    @BindView(R.id.videoplayer)
    JCVideoPlayerStandard videoplayer;
    @BindView(R.id.dianbo)
    FrameLayout dianbo;
    @BindView(R.id.dianbo_back)
    ImageView dianboBack;
    private CustomDialog customDialog;
    private RTCMediaStreamingManager mRTCStreamingManager;
    // private RTCConferenceOptions options;
    private RTCVideoWindow windowA;
    //  private WatermarkSetting watermarksetting;
    private CameraStreamingSetting cameraStreamingSetting;
    private StreamingProfile mStreamingProfile;
    private int mCurrentCamFacingIndex; // 记录当前摄像头的方向
    private boolean isSwCodec; // 编码的方式 true 软 false 硬
    private boolean mIsActivityPaused;
    private boolean mIsConferenceStarted = false; // 是否连麦
    private String mRoomName = "9cd5821094c84894ae20d81f9068108a";
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
    private float Rewordmoney;

    private PayTypePopupWindow payTypePopupWindow;
    private boolean isPayFromRewoard;
    /***
     * 连麦的操作监听
     */
    private RTCConferenceStateChangedListener mRTCStreamingStateChangedListener = new RTCConferenceStateChangedListener() {
        @Override
        public void onConferenceStateChanged(RTCConferenceState state, int extra) {
            switch (state) {
                case READY:
                    break;
                case CONNECT_FAIL:
                    // 连麦失败
                    break;
                case VIDEO_PUBLISH_FAILED:
                case AUDIO_PUBLISH_FAILED:
                    break;
                case VIDEO_PUBLISH_SUCCESS:
                    // 连接成功掉
                    showOnMis();
                    break;
                case AUDIO_PUBLISH_SUCCESS:
                    break;
                case USER_JOINED_AGAIN:
                    break;
                case USER_KICKOUT_BY_HOST:
                    break;
                case OPEN_CAMERA_FAIL:
                    break;
                case AUDIO_RECORDING_FAIL:
                    break;
                default:
                    return;
            }
        }
    };
    private RTCRemoteWindowEventListener mRTCRemoteWindowEventListener = new RTCRemoteWindowEventListener() {
        @Override
        public void onRemoteWindowAttached(RTCVideoWindow window, String remoteUserId) {
            Log.d(TAG, "onRemoteWindowAttached: " + remoteUserId);
        }

        @Override
        public void onRemoteWindowDetached(RTCVideoWindow window, String remoteUserId) {
            Log.d(TAG, "onRemoteWindowDetached: " + remoteUserId);
        }

        @Override
        public void onFirstRemoteFrameArrived(String remoteUserId) {
            Log.d(TAG, "onFirstRemoteFrameArrived: " + remoteUserId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        teacherId = getIntent().getStringExtra("id");
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
        initDialog();
        initViewPager();
        initCamer();
        initLister();
        initWindow();
        initStream();
        initPlay();
        getData();
    }

    private void initDialog() {
        RewordDialog = new CustomDialog(mContext);
        RewordDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_money, null);
        RewordDialogEd = (EditText) RewordDialogView.findViewById(R.id.dialog_money_ed);
        RewordDialog.setContanier(RewordDialogView);
        RewordDialog.setViewLineVisible(View.GONE);
        RewordDialog.setTitle("输入打赏的金额");
        RewordDialog.setDialogClick(new CustomDialog.DialogClick() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                if (TextUtils.isEmpty(RewordDialogEd.getText().toString().trim())) {
                    ToastUtil.show("请输入打赏的金额");
                    return;
                } else {
                    // 请求打赏 ，然后支付
                    isPayFromRewoard = true;
                    if (payTypePopupWindow == null) {
                        payTypePopupWindow = new PayTypePopupWindow(mContext, TeacherDetailActivity.this);
                    }
                    payTypePopupWindow.showPopup(reword, false);
                }

            }
        });
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
            Log.e("===", "---------------");
            // 切换直播视频
            mRTCStreamingManager.mute(false);
            dianbo.setVisibility(View.VISIBLE);
            videoplayer.setUp(paths[position], JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
            videoplayer.startVideo();
            position++;
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

    private void initPlay() {
        // auVideoview.setBufferingIndicator(auLoadingView);
        // 画面的尺寸
        AVOptions options = new AVOptions();
       /* options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0);*/

        // whether start play automatically after prepared, default value is 1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        auVideoview.setAVOptions(options);
        auVideoview.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        auVideoview.setOnCompletionListener(this);
        auVideoview.setOnVideoSizeChangedListener(this);
        auVideoview.setOnErrorListener(this);

        // 设置播放的地址
        auVideoview.setVideoPath("rtmp://v1.live.126.net/live/f05d2012107143c4908c6069057f1dc5");
        live.setVisibility(View.GONE);
        mRTCStreamingManager.startCapture();

    }

    @Override
    protected void onMicConnectedMsg(MessageEvent event) {
        showOnMis();
        auVideoview.setVisibility(View.GONE);
        auVideoview.pause();
    }

    @Override
    protected void onMicDisConnectedMsg(MessageEvent event) {
        live.setVisibility(View.GONE);
        // 确保还处在频道中时，才要切换成拉流模式
        if (auVideoview != null) {
            return;
        }
        mRTCStreamingManager.destroy();
        RTCMediaStreamingManager.deinit();
        initPlay();

    }

    @Override
    protected void onMicLinking(MessageEvent event) {
        // 收到主播的确认连麦
        requestLivePermission();
    }

    @Override
    protected void rejectConnecting(MessageEvent event) {

    }

    @Override
    protected void onMicCanceling(MessageEvent event) {
        // 隐藏布局（无论是否处于频道中，都要清理界面上的布局，如连麦者姓名）
        live.setVisibility(View.GONE);
        // 确保还处在频道中时，才要切换成拉流模式
        if (auVideoview != null) {
            return;
        }
        mRTCStreamingManager.destroy();
        RTCMediaStreamingManager.deinit();
        initPlay();
    }

    @Override
    protected void exitQueue(MessageEvent event) {

    }

    @Override
    protected void joinQueue(MessageEvent event) {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_teacher_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join_con:
                //向主播发送连麦请求
                customDialog.setContent("支付多少钱连麦");
                customDialog.show();
                break;
            case R.id.landan:
                setRequestedOrientation(island ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mRTCStreamingManager.notifyActivityOrientationChanged();
                if (island) {
                    title.setVisibility(View.VISIBLE);
                    if (viewPager.getCurrentItem() == 3) {
                        llComment.setVisibility(View.VISIBLE);
                    } else {
                        llAction.setVisibility(View.VISIBLE);
                    }
                    params.height = ScreenUtils.dip2px(AContext, 200);
                } else {
                    title.setVisibility(View.GONE);
                    if (viewPager.getCurrentItem() == 3) {
                        llComment.setVisibility(View.GONE);
                    } else {
                        llAction.setVisibility(View.GONE);
                    }
                    params.height = LinearLayout.LayoutParams.MATCH_PARENT;
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
                RewordDialog.show();
                break;
            case R.id.pause:
                //mRTCStreamingManager.sto
                break;
            case R.id.left_text:
                finish();
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
                mRTCStreamingManager.mute(false);
                break;
        }
    }

    //====连麦操作===
    private boolean startConference() {
        if (mIsConferenceStarted) {
            return true;
        }
        showLoading("正在加入连麦");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                startConferenceInternal();
            }
        });
        return true;
    }

    private boolean startConferenceInternal() {
      /*  String roomToken = StreamUtils.requestRoomToken(StreamUtils.getTestUserId(this), mRoomName);
        if (roomToken == null) {
            hideLoading();
            ToastUtil.show("无法获取房间信息 !");
            return false;
        }
*/
        String roomToken = "69a10dee6a56429a985d6956a500d07e";
        mRTCStreamingManager.startConference(StreamUtils.getTestUserId(this), mRoomName, roomToken, new RTCStartConferenceCallback() {
            @Override
            public void onStartConferenceSuccess() {
                hideLoading();
                ToastUtil.show(getString(R.string.start_conference));
                // updateControlButtonText();
                mIsConferenceStarted = true;
                if (mIsActivityPaused) {
                    stopConference();
                }
                // 释放拉流
                auVideoview.stopPlayback();
                // 连麦者显示连麦画面
                showOnMis();
                joinCon.setVisibility(View.GONE);
            }

            @Override
            public void onStartConferenceFailed(int errorCode) {
                hideLoading();
                ToastUtil.show(getString(R.string.failed_to_start_conference));

            }
        });
        return true;
    }

    /**
     * 结束连麦
     *
     * @return
     */
    private boolean stopConference() {
        if (!mIsConferenceStarted) {
            return true;
        }
        mRTCStreamingManager.stopConference();
        mIsConferenceStarted = false;

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsActivityPaused = false;

        auVideoview.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        auVideoview.pause();
        mIsActivityPaused = true;
        mRTCStreamingManager.stopCapture();
        stopConference();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        auVideoview.stopPlayback();
        mRTCStreamingManager.destroy();
        RTCMediaStreamingManager.deinit();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        if (island) {
            setRequestedOrientation(island ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mRTCStreamingManager.notifyActivityOrientationChanged();
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
            super.onBackPressed();
        }


    }

    /**
     * 初始化相机相关的
     */
    private void initCamer() {
        // 显示模式
        liveAfl.setShowMode(AspectFrameLayout.SHOW_MODE.FULL);
        // 摄像头的方向
        CameraStreamingSetting.CAMERA_FACING_ID facingId = LiveUtils.chooseCameraFacingId();
        mCurrentCamFacingIndex = facingId.ordinal();
        // 美颜处理
        cameraStreamingSetting = new CameraStreamingSetting();
        cameraStreamingSetting.setCameraFacingId(facingId);
        cameraStreamingSetting.setBuiltInFaceBeautyEnabled(true); // Using sdk built in face beauty algorithm
        //FaceBeautySetting 中的参数依次为：beautyLevel，whiten，redden，即磨皮程度、美白程度以及红润程度，取值范围为[0.0f, 1.0f]
        cameraStreamingSetting.setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(0.8f, 0.8f, 0.6f)); // sdk built in face beauty settings
        cameraStreamingSetting.setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY); // set the beauty on/off

    }

    /**
     * 事件监听
     */
    private void initLister() {
        AVCodecType codecType = isSwCodec ? AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC : AVCodecType.HW_VIDEO_YUV_AS_INPUT_WITH_HW_AUDIO_CODEC;
        mRTCStreamingManager = new RTCMediaStreamingManager(getApplicationContext(), liveAfl, liveSfv, codecType);
        mRTCStreamingManager.setConferenceStateListener(mRTCStreamingStateChangedListener);
        mRTCStreamingManager.setRemoteWindowEventListener(mRTCRemoteWindowEventListener);
    }

    /**
     * 连麦小窗口
     */
    private void initWindow() {
        windowA = new RTCVideoWindow(liveFlWindow, liveGfvWinow);
        // windowA.setAbsolutetMixOverlayRect(options.getVideoEncodingWidth() - 320, 100, 320, 240);
        mRTCStreamingManager.addRemoteWindow(windowA);


    }

    /**
     * 推流设置
     */
    private void initStream() {
        mStreamingProfile = new StreamingProfile();
        mStreamingProfile.setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.PORT);
        // 动态切换横竖跑屏时 要调用 mMediaStreamingManager.notifyActivityOrientationChanged(); mMediaStreamingManager.notifyActivityOrientationChanged();
        mRTCStreamingManager.prepare(cameraStreamingSetting, null);

    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        // 显示直播完毕的画面
    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        return false;
    }

    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1, int i2, int i3) {

    }

    /**
     * 显示自己的画面
     */
    protected void showLiveView() {

        live.setVisibility(View.VISIBLE);
        liveFlWindow.setVisibility(View.GONE);
        liveAfl.setVisibility(View.VISIBLE);
    }

    /**
     * 连麦成功显示 连麦的小窗口
     */
    protected void showOnMis() {
        live.setVisibility(View.VISIBLE);
        liveFlWindow.setVisibility(View.VISIBLE);
        liveAfl.setVisibility(View.VISIBLE);

        auVideoview.setVisibility(View.GONE);
        auVideoview.pause();

    }

    protected void hideOnMis() {
        live.setVisibility(View.GONE);
        liveFlWindow.setVisibility(View.INVISIBLE);
        liveAfl.setVisibility(View.INVISIBLE);


        auVideoview.setVisibility(View.VISIBLE);
        auVideoview.start();


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
        mRTCStreamingManager.startCapture();
        startConference();
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
        if (joinCon.isChecked()) {
            isPayFromRewoard = false;
            if (payTypePopupWindow == null) {
                payTypePopupWindow = new PayTypePopupWindow(mContext, this);
            }

            payTypePopupWindow.showPopup(joinCon, false);
        } else {
            stopConference();
            hideOnMis();
        }

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
}
