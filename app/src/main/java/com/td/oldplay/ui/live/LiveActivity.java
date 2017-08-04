package com.td.oldplay.ui.live;

import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.pili.droid.rtcstreaming.RTCAudioInfo;
import com.qiniu.pili.droid.rtcstreaming.RTCAudioLevelCallback;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceOptions;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceState;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceStateChangedListener;
import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;
import com.qiniu.pili.droid.rtcstreaming.RTCRemoteWindowEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCStartConferenceCallback;
import com.qiniu.pili.droid.rtcstreaming.RTCUserEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamStatusCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.WatermarkSetting;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;
import com.td.oldplay.R;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.MessageEvent;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.permission.MPermission;
import com.td.oldplay.permission.annotation.OnMPermissionDenied;
import com.td.oldplay.permission.annotation.OnMPermissionGranted;
import com.td.oldplay.permission.annotation.OnMPermissionNeverAskAgain;
import com.td.oldplay.permission.util.MPermissionUtil;
import com.td.oldplay.ui.live.adapter.AvatorAdapter;
import com.td.oldplay.ui.live.adapter.CommentAdapter;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.utils.LiveUtils;
import com.td.oldplay.utils.StreamUtils;
import com.td.oldplay.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * 直播或连麦的界面
 */
public class LiveActivity extends LiveBaseActivity implements View.OnClickListener, CustomDialog.DialogClick {

    private static final String TAG = "===";
    @BindView(R.id.live_sfv)
    GLSurfaceView liveSfv;
    @BindView(R.id.live_afl)
    AspectFrameLayout liveAfl;
    @BindView(R.id.live_gfv_winow)
    GLSurfaceView liveGfvWinow;
    @BindView(R.id.live_fl_window)
    FrameLayout liveFlWindow;
    @BindView(R.id.live_rv_chat)
    RecyclerView liveRvChat;
    @BindView(R.id.ControlButton)
    Button ControlButton;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.live_person_num)
    TextView livePersonNum;
    @BindView(R.id.live_lianmai)
    CheckBox liveLianmai;
    @BindView(R.id.live_change)
    LinearLayout liveChange;
    @BindView(R.id.live_close)
    ImageView liveClose;

    private RTCMediaStreamingManager mRTCStreamingManager;
    private RTCConferenceOptions options;
    private RTCVideoWindow windowA;
    private WatermarkSetting watermarksetting;
    private CameraStreamingSetting cameraStreamingSetting;
    private StreamingProfile mStreamingProfile;

    private int role;  // 0 是主播 1是副主播
    private int mCurrentCamFacingIndex; // 记录当前摄像头的方向
    private boolean isSwCodec; // 编码的方式 true 软 false 硬
    private boolean mIsActivityPaused;
    private boolean mIsConferenceStarted = false; // 是否连麦
    private boolean mIsPublishStreamStarted = false; // 是否是直播
    private boolean mIsInReadyState;// 是否已经准备好
    private String mRoomName = "haha";


    private boolean isPermissionGrant;
    private boolean isComment; //0 显示观看人的头像 1 显示评论记录
    private AvatorAdapter avatorAdapter;
    private List<UserBean> userDatas = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private List<CommentBean> commentDatas = new ArrayList<>();

    private CustomDialog customDialog;
    private View dialogView;
    private EditText dialogEd;
    private float money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        role = getIntent().getIntExtra("role", 0);
        mRoomName = getIntent().getStringExtra("roomName");
        mRoomName = "haha";
        requestLivePermission(); // 请求权限
        initView();
        initCamer();
        //initWater();
        initLister();
        initConference();
        initWindow();
        initStream();

    }

    @Override
    protected void onMicConnectedMsg(MessageEvent event) {

    }

    @Override
    protected void onMicDisConnectedMsg(MessageEvent event) {

    }

    @Override
    protected void onMicLinking(MessageEvent event) {

    }

    @Override
    protected void rejectConnecting(MessageEvent event) {

    }

    @Override
    protected void onMicCanceling(MessageEvent event) {

    }

    @Override
    protected void exitQueue(MessageEvent event) {

    }

    @Override
    protected void joinQueue(MessageEvent event) {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_live;
    }

    /**
     * 推流设置
     */
    private void initStream() {
        mStreamingProfile = new StreamingProfile();
        mStreamingProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_MEDIUM2)
                .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM1)
                .setEncoderRCMode(StreamingProfile.EncoderRCModes.BITRATE_PRIORITY)
                .setPreferredVideoEncodingSize(options.getVideoEncodingWidth(), options.getVideoEncodingHeight())
                .setFpsControllerEnable(true)
                .setSendingBufferProfile(new StreamingProfile.SendingBufferProfile(0.2f, 0.8f, 3.0f, 20 * 1000));
        mStreamingProfile.setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.LAND);
        // 动态切换横竖跑屏时 要调用 mMediaStreamingManager.notifyActivityOrientationChanged(); mMediaStreamingManager.notifyActivityOrientationChanged();
        if (role == 0) {
            mRTCStreamingManager.prepare(cameraStreamingSetting, null, watermarksetting, mStreamingProfile);
        } else {
            mRTCStreamingManager.prepare(cameraStreamingSetting, null);
        }

    }

    /**
     * 连麦小窗口
     */
    private void initWindow() {
        if (role == 0) {
            windowA = new RTCVideoWindow(liveFlWindow, liveGfvWinow);
            windowA.setAbsolutetMixOverlayRect(options.getVideoEncodingWidth() - 320, 100, 320, 240);
            mRTCStreamingManager.addRemoteWindow(windowA);

        }
    }

    /**
     * 连麦的配置属性
     */
    private void initConference() {

        options = new RTCConferenceOptions();
        if (role == 0) {
            // anchor should use a bigger size, must equals to `StreamProfile.setPreferredVideoEncodingSize` or `StreamProfile.setEncodingSizeLevel`
            // RATIO_16_9 & VIDEO_ENCODING_SIZE_HEIGHT_480 means the output size is 848 x 480
            options.setVideoEncodingSizeRatio(RTCConferenceOptions.VIDEO_ENCODING_SIZE_RATIO.RATIO_16_9);
            options.setVideoEncodingSizeLevel(RTCConferenceOptions.VIDEO_ENCODING_SIZE_HEIGHT_480);
            options.setVideoBitrateRange(300 * 1024, 800 * 1024);
            // 15 fps is enough
            options.setVideoEncodingFps(15);
        } else {
            // vice anchor can use a smaller size
            // RATIO_4_3 & VIDEO_ENCODING_SIZE_HEIGHT_240 means the output size is 320 x 240
            // 4:3 looks better in the mix frame
            options.setVideoEncodingSizeRatio(RTCConferenceOptions.VIDEO_ENCODING_SIZE_RATIO.RATIO_4_3);
            options.setVideoEncodingSizeLevel(RTCConferenceOptions.VIDEO_ENCODING_SIZE_HEIGHT_240);
            options.setVideoBitrateRange(300 * 1024, 800 * 1024);
            // 15 fps is enough
            options.setVideoEncodingFps(15);
        }
        options.setHWCodecEnabled(!isSwCodec);
        mRTCStreamingManager.setConferenceOptions(options);

    }

    /**
     * 事件监听
     */
    private void initLister() {
        AVCodecType codecType = isSwCodec ? AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC : AVCodecType.HW_VIDEO_YUV_AS_INPUT_WITH_HW_AUDIO_CODEC;
        mRTCStreamingManager = new RTCMediaStreamingManager(getApplicationContext(), liveAfl, liveSfv, codecType);
        mRTCStreamingManager.setConferenceStateListener(mRTCStreamingStateChangedListener);
        mRTCStreamingManager.setRemoteWindowEventListener(mRTCRemoteWindowEventListener);
        mRTCStreamingManager.setUserEventListener(mRTCUserEventListener);
        //debug 模式  mRTCStreamingManager.setDebugLoggingEnabled(isDebugModeEnabled);
        if (role == 0) {
            mRTCStreamingManager.setStreamStatusCallback(mStreamStatusCallback);
            mRTCStreamingManager.setStreamingStateListener(mStreamingStateChangedListener);
            mRTCStreamingManager.setStreamingSessionListener(mStreamingSessionListener);
        }
    }

    /**
     * 根据角色显示一些控件
     */
    private void initView() {
        liveChange.setOnClickListener(this);
        liveClose.setOnClickListener(this);
        liveLianmai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mRTCStreamingManager.kickoutUser(R.id.live_gfv_winow);
                    liveLianmai.setText("开始连麦");
                    liveFlWindow.setVisibility(View.GONE);
                    liveGfvWinow.setVisibility(View.GONE);
                }else{
                    if (customDialog != null) {
                        customDialog.show();
                    }
                }

            }
        });
        avatorAdapter = new AvatorAdapter(mContext, R.layout.item_avator, userDatas);
        commentAdapter = new CommentAdapter(mContext, R.layout.item_live_comment, commentDatas);
        liveRvChat.setLayoutManager(new GridLayoutManager(mContext, 4));
        liveRvChat.setAdapter(avatorAdapter);
        customDialog = new CustomDialog(mContext);
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_money, null);
        dialogEd = (EditText) dialogView.findViewById(R.id.dialog_money_ed);
        customDialog.setContanier(dialogView);
        customDialog.setViewLineVisible(View.GONE);
        customDialog.setTitle("设置连麦的金额");
        customDialog.setDialogClick(this);

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
        /**
         * Sconfig camera settings
         */
        cameraStreamingSetting = new CameraStreamingSetting();
        cameraStreamingSetting.setCameraFacingId(facingId)
                .setContinuousFocusModeEnabled(true)
                .setRecordingHint(false)
                .setResetTouchFocusDelayInMs(3000)
                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_VIDEO)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9);

        // 美颜处理
        cameraStreamingSetting.setBuiltInFaceBeautyEnabled(true); // Using sdk built in face beauty algorithm
        //FaceBeautySetting 中的参数依次为：beautyLevel，whiten，redden，即磨皮程度、美白程度以及红润程度，取值范围为[0.0f, 1.0f]
        cameraStreamingSetting.setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(0.8f, 0.8f, 0.6f)); // sdk built in face beauty settings
        cameraStreamingSetting.setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY); // set the beauty on/off

    }

    /**
     * 水印处理
     */
/*    private void initWater() {
        watermarksetting = new WatermarkSetting(this);
        watermarksetting.setResourceId(R.mipmap.ic_launcher_round)
                .setSize(WatermarkSetting.WATERMARK_SIZE.MEDIUM)
                .setAlpha(100)
                .setCustomPosition(0.5f, 0.5f);
    }*/
    @Override
    protected void onResume() {
        super.onResume();
        mIsActivityPaused = false;
        /**
         * Step 10: You must start capture before conference or streaming
         * You will receive `Ready` state callback when capture started success
         */
        if (isPermissionGrant) {
            mRTCStreamingManager.startCapture();
        }

        ControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsPublishStreamStarted) {
                    startPublishStreaming();
                } else {
                    stopPublishStreaming();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsActivityPaused = true;

        mRTCStreamingManager.stopCapture();
        stopConference();
        stopPublishStreaming();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRTCStreamingManager.destroy();

        RTCMediaStreamingManager.deinit();
    }

    /**
     * 连麦操作
     *
     * @return
     */
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
        String roomToken = StreamUtils.requestRoomToken(StreamUtils.getTestUserId(this), mRoomName);
        if (roomToken == null) {
            hideLoading();
            ToastUtil.show("无法获取房间信息 !");
            return false;
        }
        mRTCStreamingManager.startConference(StreamUtils.getTestUserId(this), mRoomName, roomToken, new RTCStartConferenceCallback() {
            @Override
            public void onStartConferenceSuccess() {
                hideLoading();
                ToastUtil.show(getString(R.string.start_conference));
                updateControlButtonText();
                mIsConferenceStarted = true;
                // Will cost 2% more cpu usage if enabled
                //mRTCStreamingManager.setStreamStatsEnabled(mIsStatsEnabled);
                /**
                 * Because `startConference` is called in child thread
                 * So we should check if the activity paused.
                 */
                if (mIsActivityPaused) {
                    stopConference();
                }
            }

            @Override
            public void onStartConferenceFailed(int errorCode) {
                hideLoading();
                ToastUtil.show(getString(R.string.failed_to_start_conference));
                // setConferenceBoxChecked(false);
                //  dismissProgressDialog();
                //  showToast(getString(R.string.failed_to_start_conference) + errorCode, Toast.LENGTH_SHORT);
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
        // setConferenceBoxChecked(false);
        updateControlButtonText();
        return true;
    }

    /**
     * 直播操作
     *
     * @return
     */
    private boolean startPublishStreaming() {
        if (mIsPublishStreamStarted) {
            return true;
        }
        if (!mIsInReadyState) {
            ToastUtil.show("还没准备好");
            return false;
        }

        showLoading("正在准备推流... ");
  /*      AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

            }
        });*/
        startPublishStreamingInternal();
        return true;
    }

    private boolean startPublishStreamingInternal() {
        String publishAddr = "rtmp://pili-publish.wangliangliang.qiniuts.com/wangliangliang-piliwork/57c836b21013850579011639?key=0ee13645-890a-4c6f-8f83-6e0ee0ce8d86";
        if (publishAddr == null) {
            hideLoading();
            ToastUtil.show("无法获取房间信息/推流地址 !");
            return false;
        }

        try {
            if (StreamUtils.IS_USING_STREAMING_JSON) {
                mStreamingProfile.setStream(new StreamingProfile.Stream(new JSONObject(publishAddr)));
            } else {
                mStreamingProfile.setPublishUrl(publishAddr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            hideLoading();
            ToastUtil.show("无效的推流地址 !");
            return false;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            hideLoading();
            ToastUtil.show("无效的推流地址 !");
            return false;
        }

        mRTCStreamingManager.setStreamingProfile(mStreamingProfile);
        if (!mRTCStreamingManager.startStreaming()) {
            hideLoading();
            ToastUtil.show(getString(R.string.failed_to_start_streaming));
            return false;
        }
        hideLoading();
        ToastUtil.show(getString(R.string.start_streaming));
        mIsPublishStreamStarted = true;
        updateControlButtonText();
        /**
         * Because `startPublishStreaming` need a long time in some weak network
         * So we should check if the activity paused.
         */
        if (mIsActivityPaused) {
            stopPublishStreaming();
        }
        return true;
    }

    private void updateControlButtonText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (role == 0) {
                    if (mIsPublishStreamStarted) {
                        ControlButton.setText(getString(R.string.stop_streaming));
                    } else {
                        ControlButton.setText(getString(R.string.start_streaming));
                    }
                } else {
                    if (mIsConferenceStarted) {
                        ControlButton.setText(getString(R.string.stop_conference));
                    } else {
                        ControlButton.setText(getString(R.string.start_conference));
                    }
                }
            }
        });
    }

    /**
     * 停止直播
     *
     * @return
     */
    private boolean stopPublishStreaming() {
        if (!mIsPublishStreamStarted) {
            return true;
        }
        mRTCStreamingManager.stopStreaming();
        mIsPublishStreamStarted = false;
        updateControlButtonText();
        return false;
    }


    private RTCConferenceStateChangedListener mRTCStreamingStateChangedListener = new RTCConferenceStateChangedListener() {
        @Override
        public void onConferenceStateChanged(RTCConferenceState state, int extra) {
            switch (state) {
                case READY:

                    break;
                case CONNECT_FAIL:
                    ToastUtil.show("连接失败");
                    finish();
                    break;
                case VIDEO_PUBLISH_FAILED:
                case AUDIO_PUBLISH_FAILED:
                    ToastUtil.show("推流失败");
                    finish();
                    break;
                case VIDEO_PUBLISH_SUCCESS:
                    break;
                case AUDIO_PUBLISH_SUCCESS:
                    break;
                case USER_JOINED_AGAIN:
                    finish();
                    break;
                case USER_KICKOUT_BY_HOST:
                    finish();
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

    private RTCUserEventListener mRTCUserEventListener = new RTCUserEventListener() {
        @Override
        public void onUserJoinConference(String remoteUserId) {
            // 向连麦者发送 连接成功的消息
        }

        @Override
        public void onUserLeaveConference(String remoteUserId) {
            // 向连麦者发送 断开连接成功的消息
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

    private RTCAudioLevelCallback mRTCAudioLevelCallback = new RTCAudioLevelCallback() {
        @Override
        public void onAudioLevelChanged(RTCAudioInfo rtcAudioInfo) {
            Log.d(TAG, "onAudioLevelChanged: " + rtcAudioInfo.toString());
        }
    };

    private StreamStatusCallback mStreamStatusCallback = new StreamStatusCallback() {
        @Override
        public void notifyStreamStatusChanged(final StreamingProfile.StreamStatus streamStatus) {
          /*  runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String stat = "bitrate: " + streamStatus.totalAVBitrate / 1024 + " kbps"
                            + "\naudio: " + streamStatus.audioFps + " fps"
                            + "\nvideo: " + streamStatus.videoFps + " fps";
                    mStatTextView.setText(stat);
                }
            });*/
        }
    };

    private StreamingStateChangedListener mStreamingStateChangedListener = new StreamingStateChangedListener() {
        @Override
        public void onStateChanged(final StreamingState state, Object o) {
            switch (state) {
                case PREPARING:

                    break;
                case READY:
                    mIsInReadyState = true;
                    Log.d(TAG, "onStateChanged state:" + "ready");
                    break;
                case CONNECTING:
                    Log.d(TAG, "onStateChanged state:" + "connecting");
                    break;
                case STREAMING:
                    Log.d(TAG, "onStateChanged state:" + "streaming");
                    break;
                case SHUTDOWN:
                    mIsInReadyState = true;
                    Log.d(TAG, "onStateChanged state:" + "shutdown");
                    break;
                case UNKNOWN:
                    Log.d(TAG, "onStateChanged state:" + "unknown");
                    break;
                case SENDING_BUFFER_EMPTY:
                    Log.d(TAG, "onStateChanged state:" + "sending buffer empty");
                    break;
                case SENDING_BUFFER_FULL:
                    Log.d(TAG, "onStateChanged state:" + "sending buffer full");
                    break;
                case OPEN_CAMERA_FAIL:
                    Log.d(TAG, "onStateChanged state:" + "open camera failed");
                    break;
                case AUDIO_RECORDING_FAIL:
                    Log.d(TAG, "onStateChanged state:" + "audio recording failed");
                    break;
                case IOERROR:
                    /**
                     * Network-connection is unavailable when `startStreaming`.
                     * You can do reconnecting or just finish the streaming
                     */
                    Log.d(TAG, "onStateChanged state:" + "io error");

                    // sendReconnectMessage();
                    // stopPublishStreaming();
                    break;
                case DISCONNECTED:
                    /**
                     * Network-connection is broken after `startStreaming`.
                     * You can do reconnecting in `onRestartStreamingHandled`
                     */
                    Log.d(TAG, "onStateChanged state:" + "disconnected");

                    // we will process this state in `onRestartStreamingHandled`
                    break;
            }
        }
    };
    private StreamingSessionListener mStreamingSessionListener = new StreamingSessionListener() {
        @Override
        public boolean onRecordAudioFailedHandled(int code) {
            return false;
        }

        /**
         * When the network-connection is broken, StreamingState#DISCONNECTED will notified first,
         * and then invoked this method if the environment of restart streaming is ready.
         *
         * @return true means you handled the event; otherwise, given up and then StreamingState#SHUTDOWN
         * will be notified.
         */
        @Override
        public boolean onRestartStreamingHandled(int code) {
            Log.d(TAG, "onRestartStreamingHandled, reconnect ...");
            return mRTCStreamingManager.startStreaming();
        }

        @Override
        public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
            for (Camera.Size size : list) {
                if (size.height >= 480) {
                    return size;
                }
            }
            return null;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


/*    **********************
     * 录音摄像头权限申请
     *******************************/

    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGranted() {
        ToastUtil.show("授权成功");
        isPermissionGrant = true;
        mRTCStreamingManager.startCapture();

    }

    @OnMPermissionDenied(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, LIVE_PERMISSIONS);
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
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

        ToastUtil.show(sb.toString());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_close:
                finish();
                break;
            case R.id.live_change:
                if (isComment) {
                    liveRvChat.setLayoutManager(new LinearLayoutManager(mContext));
                    liveRvChat.setAdapter(commentAdapter);
                    getComments();
                } else {
                    liveRvChat.setLayoutManager(new GridLayoutManager(mContext, 3));
                    liveRvChat.setAdapter(avatorAdapter);
                    getUsers();
                }
                isComment = !isComment;
                break;
            case R.id.live_lianmai:
               /* Log.e("===",liveLianmai.isChecked()+"  ");
                if(liveLianmai.isChecked()){
                    mRTCStreamingManager.kickoutUser(R.id.live_gfv_winow);
                }else{
                    if (customDialog != null) {
                        customDialog.show();
                    }
                }*/

                break;
        }

    }

    private void getComments() {
        HttpManager.getInstance().getCommentsInTeacher(userId, new HttpSubscriber<List<CommentBean>>(new OnResultCallBack<List<CommentBean>>() {


            @Override
            public void onSuccess(List<CommentBean> commentBeen) {
                commentDatas.clear();
                commentDatas.addAll(commentBeen);
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String errorMsg) {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }
        }));
    }

    private void getUsers() {
        userDatas.add(new UserBean());
        userDatas.add(new UserBean());
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onOk() {
        if (TextUtils.isEmpty(dialogEd.getText().toString())) {
            ToastUtil.show("请输入连麦金额");
            return;
        }
        money = Float.parseFloat(dialogEd.getText().toString());
        if(money<=0){
            ToastUtil.show("金额数必须大于0");
            return;
        }
        setJoinMoney(dialogEd.getText().toString());

    }

    /**
     *  主播设置连麦的金额
     */
    private void setJoinMoney(String money) {
        HttpManager.getInstance().setJoinMoney(userId,money,new HttpSubscriber<String>(new OnResultCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                ToastUtil.show("已开启连麦");
                liveFlWindow.setVisibility(View.VISIBLE);
                liveGfvWinow.setVisibility(View.VISIBLE);
                liveLianmai.setText("关闭连麦");
            }

            @Override
            public void onError(int code, String errorMsg) {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }
        }));
    }
}
