package com.td.oldplay.ui.live;

import android.content.Context;
import android.text.TextUtils;

import com.td.oldplay.contants.MContants;

import com.td.oldplay.utils.ToastUtil;
import com.tencent.av.sdk.AVRoomMulti;

import com.tencent.av.sdk.AVView;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;

import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.ilivesdk.core.ILivePushOption;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.core.ILiveRoomOption;

import com.tencent.ilivesdk.data.ILivePushRes;
import com.tencent.ilivesdk.data.ILivePushUrl;
import com.tencent.livesdk.ILVChangeRoleRes;
import com.tencent.livesdk.ILVCustomCmd;

import com.tencent.livesdk.ILVLiveConstants;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.livesdk.ILVText;
import com.tencent.mm.opensdk.utils.Log;

import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * 直播控制类
 */
public class LiveHelper implements ILiveRoomOption.onRoomDisconnectListener, Observer {
    private final String TAG = "LiveHelper";
    private LiveBaseActivity mLiveView;
    public Context mContext;
    private boolean bCameraOn = false;
    private boolean bMicOn = false;
    private boolean flashLgihtStatus = false;
    private long streamChannelID;


    public LiveHelper(Context context, LiveBaseActivity liveview) {
        mContext = context;
        mLiveView = liveview;
        MessageEvent.getInstance().addObserver(this);
    }

    public void onDestory() {
        mLiveView = null;
        mContext = null;
        MessageEvent.getInstance().deleteObserver(this);
        ILVLiveManager.getInstance().quitRoom(null);
    }

    /**
     * 创建房间
     */
    public void createRoom(String hostId) {
        ILVLiveRoomOption hostOption = new ILVLiveRoomOption(hostId)
                .roomDisconnectListener(this)
                .videoMode(ILiveConstants.VIDEOMODE_BSUPPORT)
                .controlRole(MContants.SD_ROLE)
                .autoFocus(true)
                .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);
        int ret = ILVLiveManager.getInstance().createRoom(Integer.parseInt(hostId), hostOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                ILiveLog.d(TAG, "ILVB-SXB|startEnterRoom->create room sucess");
                bCameraOn = true;
                bMicOn = true;
                if (null != mLiveView)
                    mLiveView.enterRoomComplete(true);

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ILiveLog.d(TAG, "ILVB-SXB|createRoom->create room failed:" + module + "|" + errCode + "|" + errMsg);
                ToastUtil.show("创建房间失败,请冲洗");
                if (null != mLiveView) {
                    mLiveView.quiteRoomComplete(true, null);
                }
            }
        });
    }

    /**
     * 进入房间
     *
     * @param hostId
     */
    public void joinRoom(String hostId) {

        ILVLiveRoomOption memberOption = new ILVLiveRoomOption(hostId + "")
                .autoCamera(false)
                .roomDisconnectListener(this)
                .videoMode(ILiveConstants.VIDEOMODE_BSUPPORT)
                .controlRole(MContants.SD_ROLE)
                .authBits(AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO | AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO)
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO)
                .autoMic(false);
        int ret = ILVLiveManager.getInstance().joinRoom(Integer.parseInt(hostId), memberOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (null != mLiveView)
                    mLiveView.enterRoomComplete(true);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.show(errMsg);
                if (null != mLiveView) {
                    mLiveView.quiteRoomComplete(true, errCode);
                }
            }
        });
        // checkEnterReturn(ret);

    }


    public void startExitRoom() {
        ILiveSDK.getInstance().getAvVideoCtrl().setLocalVideoPreProcessCallback(null);
        quitLiveRoom();
    }

    private void quitLiveRoom() {
        ILVLiveManager.getInstance().quitRoom(new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                ILiveLog.d(TAG, "ILVB-SXB|quitRoom->success");

                //通知结束
                //NotifyServerLiveTask();
                if (null != mLiveView) {
                    mLiveView.quiteRoomComplete(true, null);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ILiveLog.d(TAG, "ILVB-SXB|quitRoom->failed:" + module + "|" + errCode + "|" + errMsg);
                if (null != mLiveView) {
                    mLiveView.quiteRoomComplete(true, null);
                }
            }
        });
    }

    public void startPush(ILivePushOption option) {
        ILiveRoomManager.getInstance().startPushStream(option, new ILiveCallBack<ILivePushRes>() {
            @Override
            public void onSuccess(ILivePushRes data) {
                List<ILivePushUrl> liveUrls = data.getUrls();
                streamChannelID = data.getChnlId();
                if (null != mLiveView)
                    mLiveView.pushStreamSucc(data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.show(errMsg);
            }
        });
    }

    public void stopPush() {
        ILiveRoomManager.getInstance().stopPushStream(streamChannelID, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.e(TAG, "stopPush->success");
                if (null != mLiveView)
                    mLiveView.stopStreamSucc();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.e(TAG, "stopPush->failed:" + module + "|" + errCode + "|" + errMsg);
            }
        });
    }

    public void upMemberVideo() {
        if (!ILiveRoomManager.getInstance().isEnterRoom()) {
            Log.e(TAG, "upMemberVideo->with not in room");
        }
        ILVLiveManager.getInstance().upToVideoMember(MContants.VIDEO_MEMBER_ROLE, true, true, new ILiveCallBack<ILVChangeRoleRes>() {
            @Override
            public void onSuccess(ILVChangeRoleRes data) {
                Log.d(TAG, "upToVideoMember->success");
                mLiveView.isLinking = true;

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.e(TAG, "upToVideoMember->failed:" + module + "|" + errCode + "|" + errMsg);
            }
        });
    }

    public void downMemberVideo() {
        if (!ILiveRoomManager.getInstance().isEnterRoom()) {
            Log.e(TAG, "downMemberVideo->with not in room");
        }
        ILVLiveManager.getInstance().downToNorMember(MContants.NORMAL_MEMBER_ROLE, new ILiveCallBack<ILVChangeRoleRes>() {
            @Override
            public void onSuccess(ILVChangeRoleRes data) {
                mLiveView.isLinking = false;
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.e(TAG, "downMemberVideo->failed:" + module + "|" + errCode + "|" + errMsg);
            }
        });
    }

/*    *//**
     * 发送信令
     *//*

    public int sendGroupCmd(int cmd, String param) {
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setCmd(cmd);
        customCmd.setParam(param);
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        return sendCmd(customCmd);
    }*/
    /**
     * 发送消息
     *
     * @param cmd
     * @param param
     * @param destId
     * @return
     */
    public int sendC2CCmd(final int cmd, String param, String destId, ILiveCallBack callBack) {
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setDestId(destId);
        customCmd.setCmd(cmd);
        customCmd.setParam(param);
        customCmd.setType(ILVText.ILVTextType.eC2CMsg);
        return sendCmd(customCmd, callBack);
    }

    private int sendCmd(final ILVCustomCmd cmd, ILiveCallBack callBack) {
        return ILVLiveManager.getInstance().sendCustomCmd(cmd, callBack);
    }

    @Override
    public void onRoomDisconnect(int errCode, String errMsg) {

    }

    @Override
    public void update(Observable o, Object arg) {
        MessageEvent.SxbMsgInfo info = (MessageEvent.SxbMsgInfo) arg;
        switch (info.msgType) {
            case MessageEvent.MSGTYPE_CMD:
                processCmdMsg(info);
                break;


        }
    }

    // 解析自定义信令
    private void processCmdMsg(MessageEvent.SxbMsgInfo info) {
        if (null == info.data || !(info.data instanceof ILVCustomCmd)) {

            return;
        }
        ILVCustomCmd cmd = (ILVCustomCmd) info.data;
        if (cmd.getType() == ILVText.ILVTextType.eGroupMsg
                && !mLiveView.userId.equals(cmd.getDestId())) {
            // SxbLog.d(TAG, "processCmdMsg->ingore message from: "+cmd.getDestId()+"/"+CurLiveInfo.getChatRoomId());
            return;
        }

        String name = info.senderId;
        if (null != info.profile && !TextUtils.isEmpty(info.profile.getNickName())) {
            name = info.profile.getNickName();
        }
        handleCustomMsg(cmd.getCmd(), cmd.getParam(), info.senderId, name);
    }

    private void handleCustomMsg(int action, String param, String identifier, String nickname) {
        Log.d(TAG, "handleCustomMsg->action: " + action);
        if (null == mLiveView) {
            return;
        }
        switch (action) {
            case MContants.AVIMCMD_MUlTI_HOST_INVITE:
                mLiveView.showInviteDialog(new LinkeNumberInfo(identifier, nickname));
                break;
            case MContants.AVIMCMD_MUlTI_JOIN:
                mLiveView.cancelInviteView(identifier);
            case MContants.AVIMCMD_MUlTI_REFUSE:
                mLiveView.cancelInviteView(identifier);
                ToastUtil.show("主播拒绝与您连麦");
                break;
            case MContants.AVIMCMD_EXITLIVE:
                //startExitRoom();
                mLiveView.forceQuitRoom();
                break;
            case  MContants.AVIMCMD_ENTERLIVE:
                mLiveView.memberJoin(identifier, nickname);


        }
    }

}
