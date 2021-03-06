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

    public void toggleMic(boolean isMis) {
        ILiveRoomManager.getInstance().enableMic(isMis);
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
                .cameraId(ILiveConstants.FRONT_CAMERA)
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
                if (errCode == 6014) {
                    ToastUtil.show("该账号已经在其他设备上登录了，清重新登录");
                }else{
                    ToastUtil.show("创建房间失败,请重新进入");
                }
                if (null != mLiveView) {
                    mLiveView.quiteRoomComplete(true, null);
                }
            }
        });
        checkEnterReturn(ret);
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
                if (errCode == 6014) {
                    ToastUtil.show("该账号已经在其他设备上登录了，清重新登录");
                }
                ToastUtil.show(errMsg);
                if (null != mLiveView) {
                    mLiveView.quiteRoomComplete(true, errCode);
                }
            }
        });
         checkEnterReturn(ret);

    }
    private void checkEnterReturn(int iRet){
        if (ILiveConstants.NO_ERR != iRet){
            ILiveLog.d(TAG, "ILVB-Suixinbo|checkEnterReturn->enter room failed:" + iRet);
            if (ILiveConstants.ERR_ALREADY_IN_ROOM == iRet){     // 上次房间未退出处理做退出处理
                ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {

                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {

                    }
                });
            }
        }
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

    public void upMemberVideo(final String toUserId) {
        if (!ILiveRoomManager.getInstance().isEnterRoom()) {
            Log.e(TAG, "upMemberVideo->with not in room");
            sendC2CCmd(MContants.AVIMCMD_MUlTI_AUDICE_CANCEL, "", toUserId, null);
        }
        ILVLiveManager.getInstance().upToVideoMember(MContants.VIDEO_MEMBER_ROLE, true, true, new ILiveCallBack<ILVChangeRoleRes>() {
            @Override
            public void onSuccess(ILVChangeRoleRes data) {
                Log.d(TAG, "upToVideoMember->success");
                mLiveView.isLinking = true;
                sendC2CCmd(MContants.AVIMCMD_MUlTI_AYDIENCE_LINKED_SESS, "", toUserId, null);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.e(TAG, "upToVideoMember->failed:" + module + "|" + errCode + "|" + errMsg);
                sendC2CCmd(MContants.AVIMCMD_MUlTI_AUDICE_CANCEL, "", toUserId, null);
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
        if (info.msgType == MessageEvent.MSGTYPE_CMD) {
            ToastUtil.show("有Cmd消息了" + info.senderId);
        }
        switch (info.msgType) {
            case MessageEvent.MSGTYPE_CMD:
                processCmdMsg(info);
                break;


        }
    }

    /**
     * 发送信令
     */

    public int sendGroupCmd(int cmd, String param) {
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setCmd(cmd);
        customCmd.setParam(param);
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        return sendCmd(customCmd, null);
    }


    // 解析自定义信令
    private void processCmdMsg(MessageEvent.SxbMsgInfo info) {
        if (null == info.data || !(info.data instanceof ILVCustomCmd)) {

            return;
        }
        ILVCustomCmd cmd = (ILVCustomCmd) info.data;
        Log.e("===", cmd.getDestId() + "       " + mLiveView.userId);
        if (cmd.getType() == ILVText.ILVTextType.eGroupMsg
                && !CurLiveInfo.roomNum.equals(cmd.getDestId())) {
            // 过滤非当前群组中的信息
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
            case MContants.AVIMCMD_MUlTI_JOIN:  // 同意互动主播
                mLiveView.cancelInviteView(identifier, true);
                break;
            case MContants.AVIMCMD_MUlTI_REFUSE:
                mLiveView.cancelInviteView(identifier, false);
                ToastUtil.show("主播拒绝与您连麦");
                break;
            case MContants.AVIMCMD_EXITLIVE:
                //startExitRoom();
                mLiveView.forceQuitRoom();
                break;
            case MContants.AVIMCMD_ENTERLIVE:
                mLiveView.memberJoin(identifier, nickname);
                break;

            case MContants.AVIMCMD_LIVING: // 3`
                mLiveView.hostCreateRoom();
                break;

            case MContants.AVIMCMD_MULTI_CANCEL_INTERACT:// 下麦操作   观众端发起来的
                //如果是自己关闭Camera和Mic
                if (param.equals(mLiveView.userId)) {//是自己
                    //TODO 被动下麦 下麦 下麦
                    downMemberVideo();
                }
                //其他人关闭小窗口
                ILiveRoomManager.getInstance().getRoomView().closeUserView(param, AVView.VIDEO_SRC_TYPE_CAMERA, true);
                mLiveView.linkOther();
                break;
            case MContants.AVIMCMD_MUlTI_LINKING:// 主播正在连麦中
                ToastUtil.show("主播正在连麦中，请稍后");
                break;
            case MContants.AUDICE_EXITLIVE://  观众退出房间
                mLiveView.memberJoin(identifier, nickname);
                break;
            case MContants.AVIMCMD_MUlTI_NOSTARTLINK://  主播为开启来连麦
                mLiveView.linkedNoStart();
                ToastUtil.show("主播还未开启连麦权限");
                break;
            case MContants.AVIMCMD_MUlTI_AUDICE_CANCEL://   观众在支付的过程中 点击对话框的取消
                mLiveView.cancelInviteView(identifier, false);
                break;
            case MContants.AVIMCMD_MUlTI_AYDIENCE_LINKED_SESS://   在观众端连麦成功
                mLiveView.cancelInviteView(identifier, true);
                break;


        }
    }

}
