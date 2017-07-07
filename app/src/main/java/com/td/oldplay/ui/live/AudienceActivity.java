package com.td.oldplay.ui.live;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.utils.StreamUtils;
import com.td.oldplay.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 观看直播的界面
 */
public class AudienceActivity extends BaseFragmentActivity implements
        PLMediaPlayer.OnPreparedListener,
        PLMediaPlayer.OnInfoListener,
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        PLMediaPlayer.OnErrorListener {

    private static final int MESSAGE_ID_RECONNECTING = 1002;
    @BindView(R.id.au_videoview)
    PLVideoView auVideoview;
    @BindView(R.id.au_LoadingView)
    LinearLayout auLoadingView;
    @BindView(R.id.ControlButton)
    Button ControlButton;
    private boolean mIsActivityPaused;

    private String mVideoPath;
    private String roomName = "haha";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audience);
        ButterKnife.bind(this);

        auVideoview.setBufferingIndicator(auLoadingView);
        // 画面的尺寸
        auVideoview.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_ORIGIN);

        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);

        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0);

        // whether start play automatically after prepared, default value is 1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        auVideoview.setAVOptions(options);
        auVideoview.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);

        auVideoview.setOnPreparedListener(this);
        auVideoview.setOnInfoListener(this);
        auVideoview.setOnCompletionListener(this);
        auVideoview.setOnVideoSizeChangedListener(this);
        auVideoview.setOnErrorListener(this);


        mVideoPath = StreamUtils.requestPlayURL(roomName);
        Log.e("===",mVideoPath+"播放路径");
        // 设置播放的地址
        auVideoview.setVideoPath(mVideoPath);

    }

    // 当用户点击连麦以后，建议向 App Server 申请向主播连麦，主播同意后，方可进入房间，完成连麦
    public void onClickConference(View v) {
        Toast.makeText(this, "申请连麦... 主播已同意 !", Toast.LENGTH_SHORT).show();
        jumpToStreamingActivity(1, LiveActivity.class);


    }

    private void jumpToStreamingActivity(int role, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("role", role);
        intent.putExtra("roomName", "haha");
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        auVideoview.start();
        mIsActivityPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        auVideoview.pause();
        mIsActivityPaused = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        auVideoview.stopPlayback();
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer) {
        Log.e("===", "prepared");
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int i, int i1) {
        Log.e("===", "onInfo "+i);
        return false;
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        Log.e("===", "onCompletion ");
        finish();
    }

    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1, int i2, int i3) {

    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
        boolean isNeedReconnect = false;
        switch (errorCode) {
            case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                ToastUtil.show("Invalid URL !");
                break;
            case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                ToastUtil.show("404 resource not found !");
                break;
            case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                ToastUtil.show("Connection refused !");
                break;
            case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                ToastUtil.show("Connection timeout !");
                isNeedReconnect = true;
                break;
            case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                ToastUtil.show("Empty playlist !");
                break;
            case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                ToastUtil.show("Stream disconnected !");
                isNeedReconnect = true;
                break;
            case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                ToastUtil.show("Network IO Error !");
                isNeedReconnect = true;
                break;
            case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                ToastUtil.show("Unauthorized Error !");
                break;
            case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                ToastUtil.show("Prepare timeout !");
                isNeedReconnect = true;
                break;
            case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                ToastUtil.show("Read frame timeout !");
                isNeedReconnect = true;
                break;
            case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                break;
            default:
                ToastUtil.show("unknown error !");
                break;
        }
        // Todo pls handle the error status here, reconnect or call finish()
        if (isNeedReconnect) {
            sendReconnectMessage();
        } else {
            finish();
        }
        // Return true means the error has been handled
        // If return false, then `onCompletion` will be called
        return true;
    }

    private void sendReconnectMessage() {
        ToastUtil.show("正在重连...");
        auLoadingView.setVisibility(View.VISIBLE);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_ID_RECONNECTING), 500);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING || mIsActivityPaused) {
                return;
            }
            if (!StreamUtils.isNetworkAvailable(AContext)) {
                sendReconnectMessage();
                return;
            }

            auVideoview.setVideoPath(mVideoPath);
            auVideoview.start();
        }
    };
}
