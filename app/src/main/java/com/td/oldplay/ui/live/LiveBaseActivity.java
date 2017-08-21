package com.td.oldplay.ui.live;

import android.Manifest;

import android.os.Bundle;
import android.view.WindowManager;

import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.permission.MPermission;
import com.tencent.ilivesdk.data.ILivePushRes;

import org.greenrobot.eventbus.EventBus;



public abstract class LiveBaseActivity extends BaseFragmentActivity {

    protected static final int LIVE_PERMISSION_REQUEST_CODE = 1000;
    public boolean isLinking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        setContentView(getLayout());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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



    // 布局文件
    protected abstract int getLayout();




    /**
     *  成功进入房间
     * @param b
     */
    public abstract void enterRoomComplete(boolean b);

    /**
     *  退出房间操作
     * @param b
     * @param o
     */
    public abstract void quiteRoomComplete(boolean b, Object o);

    /**
     *  主播端实现 ----有人请求连麦
     * @param identifier
     */
    public abstract void showInviteDialog(LinkeNumberInfo identifier);

    /**
     *  观众端实现连麦---主播同意连麦
     * @param identifier
     * @param b
     */
    public abstract void cancelInviteView(String identifier, boolean b);


    /**
     *  结束推流
     */
    public void stopStreamSucc() {
    }


    /**
     *  开启推流
     * @param data
     */
    public void pushStreamSucc(ILivePushRes data){}

    /**
     *  主播离开房间 通知观众
     */
    public void forceQuitRoom() {
    }


    /**
     *  观众加入直播房间
     * @param identifier
     * @param nickname
     */
    public void memberJoin(String identifier, String nickname) {
    }


    /**
     *  主播开始直播    观众端实现
     */
    public void hostCreateRoom() {
    }

    /**
     * 其中一个 连麦 关闭之后，去连麦另外的一个            主播端实现
     */
    public void linkOther() {
    }

    /**
     *  主播还没有开启连麦                观众端实现
     */
    public  void linkedNoStart(){

    }
}
