package com.td.oldplay.ui.live;

import android.Manifest;

import android.os.Bundle;

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
     */
    public abstract void cancelInviteView(String identifier);


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
}
