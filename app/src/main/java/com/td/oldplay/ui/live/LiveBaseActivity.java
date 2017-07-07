package com.td.oldplay.ui.live;

import android.Manifest;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;
import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.bean.LiveBean;
import com.td.oldplay.bean.MessageEvent;
import com.td.oldplay.bean.PushMicNotificationType;
import com.td.oldplay.permission.MPermission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class LiveBaseActivity extends BaseFragmentActivity {

    protected static final int LIVE_PERMISSION_REQUEST_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /***********************
     * 录音摄像头权限申请
     *******************************/

    // 权限控制
    protected static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE};

    protected void requestLivePermission() {
        MPermission.with(this)
                .addRequestCode(LIVE_PERMISSION_REQUEST_CODE)
                .permissions(LIVE_PERMISSIONS)
                .request();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.action == PushMicNotificationType.JOIN_QUEUE.getValue()) {
            // 加入连麦队列
            joinQueue(event);
        } else if (event.action == PushMicNotificationType.EXIT_QUEUE.getValue()) {
            // 退出连麦队列
            exitQueue(event);
        } else if (event.action == PushMicNotificationType.CONNECTING_MIC.getValue()) {
            // 主播选中某人连麦
            onMicLinking(event);
        } else if (event.action == PushMicNotificationType.DISCONNECT_MIC.getValue()) {
            // 被主播断开连麦
            onMicCanceling(event);
        } else if (event.action == PushMicNotificationType.REJECT_CONNECTING.getValue()) {
            // 观众由于重新进入了房间而拒绝连麦
            rejectConnecting(event);
        } else if (event.action == PushMicNotificationType.MIS_CONNECTED.getValue()) {
            // 连麦者成功连接
            onMicConnectedMsg(event);
        } else if (event.action == PushMicNotificationType.REJECT_CONNECTING.getValue()) {
            // 连麦者断开连麦
            onMicDisConnectedMsg(event);
        }

    }

    // 收到连麦成功消息，由观众端实现
    protected abstract void onMicConnectedMsg(MessageEvent event);

    // 收到取消连麦消息,由观众的实现
    protected abstract void onMicDisConnectedMsg(MessageEvent event);

    /**
     * // 主播选中某人连麦，由观众实现
     *
     * @param event
     */
    protected abstract void onMicLinking(MessageEvent event);

    /***
     *   // 观众由于重新进入房间，而拒绝连麦，由主播实现
     * @param event
     */
    protected abstract void rejectConnecting(MessageEvent event);

    /**
     * 被主播断开连麦
     *
     * @param event
     */
    protected abstract void onMicCanceling(MessageEvent event);

    /**
     * // 退出连麦队列，由主播端实现
     *
     * @param event
     */
    protected abstract void exitQueue(MessageEvent event);

    /**
     * // 加入连麦队列，由主播端实现
     *
     * @param event
     */
    protected abstract void joinQueue(MessageEvent event);

    // 布局文件
    protected abstract int getLayout();


}
