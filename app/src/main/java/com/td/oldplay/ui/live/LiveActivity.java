package com.td.oldplay.ui.live;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
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

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.contants.MContants;
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
import com.td.oldplay.ui.window.ListPopupWindow;
import com.td.oldplay.ui.window.UserAvatorWindow;
import com.td.oldplay.utils.ScreenUtils;
import com.td.oldplay.utils.ToastUtil;
import com.tencent.TIMMessage;
import com.tencent.av.opengl.ui.GLView;
import com.tencent.av.sdk.AVView;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILivePushOption;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.data.ILivePushRes;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVText;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * 直播或连麦的界面
 */
public class LiveActivity extends LiveBaseActivity implements View.OnClickListener, CustomDialog.DialogClick {

    private static final String TAG = "===";
    @BindView(R.id.av_root_view)
    AVRootView avRootView;
    @BindView(R.id.live_rv_chat)
    RecyclerView liveRvChat;
    @BindView(R.id.live_person_num)
    TextView livePersonNum;
    @BindView(R.id.live_change)
    LinearLayout liveChange;
    @BindView(R.id.live_lianmai)
    CheckBox liveLianmai;
    @BindView(R.id.live_close)
    ImageView liveClose;
    @BindView(R.id.ControlButton)
    Button ControlButton;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.invite_view1)
    TextView inviteView1;
    @BindView(R.id.host_switch_cam)
    TextView hostSwitchCam;
    @BindView(R.id.rotate_view)
    FrameLayout rotateView;


    private boolean mIsActivityPaused;
    private boolean mIsPublishStreamStarted = false; // 是否是直播


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

    private UserAvatorWindow avatorWindow;
    private CustomDialog alerDialog;
    private boolean isCanLinked;
    private boolean isWait;

    OrientationEventListener mOrientationListener;
    int mLastOrientation = -25;
    private int mRotationAngle = 0;
    // 敏感度
    private int iRoleDt = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mLiveHelper = new LiveHelper(this, this);

        requestLivePermission(); // 请求权限
        initDailog();
        initView();
        mLiveHelper.createRoom(userId);
        getData();
        CurLiveInfo.roomNum = userId;  // 直播的房间id；

        /*mOrientationListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int orientation) {

                if (mLastOrientation < 0) {
                    mLastOrientation = 0;
                }

                if (((orientation - mLastOrientation) < iRoleDt)
                        && ((orientation - mLastOrientation) > -iRoleDt)) {
                    return;
                }
                mLastOrientation = orientation;
                if (orientation > 314 || orientation < 45) {
                    mRotationAngle = 0;
                } else if (orientation > 44 && orientation < 135) {
                    mRotationAngle = 90;
                } else if (orientation > 134 && orientation < 225) {
                    mRotationAngle = 180;
                } else {
                    mRotationAngle = 270;
                }
                if (mRotationAngle == 180 && mRotationAngle == 270) {

                }
                rotateView.setRotation(-mRotationAngle );

               // rotateView.invalidate();
                // rotateView.setRotationX(rotateView.getWidth()/2);
                // rotateView.setRotationY(rotateView.getHeight()/2);
            }
        };

        if (mOrientationListener.canDetectOrientation()) {
            Log.v("===", "Can detect orientation");
            mOrientationListener.enable();
        } else {
            Log.v("===", "Cannot detect orientation");
            mOrientationListener.disable();
        }*/
    }

    //// TODO: 2017/8/21  获得连麦的金额 
    private void getData() {
        HttpManager.getInstance().getLinkMoney(userId, new HttpSubscriber<Float>(new OnResultCallBack<Float>() {

            @Override
            public void onSuccess(Float aFloat) {
                money = aFloat;
                dialogEd.setText(money + "");

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


    @Override
    protected int getLayout() {
        return R.layout.activity_live;
    }


    /**
     * 根据角色显示一些控件
     */
    private void initView() {
        liveChange.setOnClickListener(this);
        liveClose.setOnClickListener(this);
        hostSwitchCam.setOnClickListener(this);
        liveLianmai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
           /*     if (currentLinked != null) {
                    mLiveHelper.sendGroupCmd(MContants.AVIMCMD_MULTI_CANCEL_INTERACT, currentLinked.id);
                    avRootView.closeUserView(currentLinked.id, AVView.VIDEO_SRC_TYPE_CAMERA, true);
                    //mLiveHelper.downMemberVideo();
                }*/
                if (isChecked) {
                    if (isWait) {
                        ToastUtil.show("正在等待连麦中，无法关闭连麦");
                    } else {
                        isCanLinked = false;
                        isWait = false;
                        liveLianmai.setText("开始连麦");
                        inviteView1.setVisibility(View.GONE);
                        if (currentLinked != null) {
                            mLiveHelper.sendGroupCmd(MContants.AVIMCMD_MULTI_CANCEL_INTERACT, currentLinked.id);
                            avRootView.closeUserView(currentLinked.id, AVView.VIDEO_SRC_TYPE_CAMERA, true);
                            //mLiveHelper.downMemberVideo();
                        }
                    }

                } else {
                    if (customDialog != null) {
                        customDialog.show();
                    }
                }

            }
        });
        avatorAdapter = new AvatorAdapter(mContext, R.layout.item_avator, userDatas);
        commentAdapter = new CommentAdapter(mContext, R.layout.item_live_comment, commentDatas);
        liveRvChat.setLayoutManager(new GridLayoutManager(mContext, 2));
        liveRvChat.setAdapter(avatorAdapter);
        customDialog = new CustomDialog(mContext);
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_money, null);
        dialogEd = (EditText) dialogView.findViewById(R.id.dialog_money_ed);
        customDialog.setContanier(dialogView);
        customDialog.setViewLineVisible(View.GONE);
        customDialog.setTitle("设置连麦的金额");
        customDialog.setDialogClick(this);
        avatorWindow = new UserAvatorWindow(mContext);
        avatorAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (avatorWindow != null) {
                    avatorWindow.setUser(userDatas.get(position));
                    avatorWindow.showPopup(avRootView);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        //TODO 设置渲染层
        ILVLiveManager.getInstance().setAvVideoView(avRootView);

        //avRootView.setBackground(R.mipmap.renderback);
        // avRootView.setGravity(AVRootView.LAYOUT_GRAVITY_RIGHT);
        avRootView.setGravity(AVRootView.LAYOUT_GRAVITY_BOTTOM);
        //avRootView.setAutoOrientation(false);
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

                }
                avRootView.getViewByIndex(0).setRotate(false);


            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        ILiveRoomManager.getInstance().onResume();
        ControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGrant) {
                    pushStream();
                }

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        ILiveRoomManager.getInstance().onResume();


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
       // mOrientationListener.disable();
        mLiveHelper.onDestory();

    }


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
                onBackPressed();
                break;
            case R.id.live_change:
                if (!isComment) {
                    liveRvChat.setLayoutManager(new LinearLayoutManager(mContext));
                    liveRvChat.setAdapter(commentAdapter);
                    getComments();
                } else {
                    liveRvChat.setLayoutManager(new GridLayoutManager(mContext, 2));
                    liveRvChat.setAdapter(avatorAdapter);

                }
                isComment = !isComment;
                break;
         /*   case R.id.live_lianmai:
                if (currentLinked != null) {
                       *//* mLiveHelper.sendGroupCmd(MContants.AVIMCMD_MULTI_CANCEL_INTERACT, currentLinked.id);
                        avRootView.closeUserView(currentLinked.id, AVView.VIDEO_SRC_TYPE_CAMERA, true);*//*
                    mLiveHelper.downMemberVideo();
                }
                if (liveLianmai.isChecked()) {
                    if (currentLinked != null) {
                        mLiveHelper.sendGroupCmd(MContants.AVIMCMD_MULTI_CANCEL_INTERACT, currentLinked.id);
                        avRootView.closeUserView(currentLinked.id, AVView.VIDEO_SRC_TYPE_CAMERA, true);
                        mLiveHelper.downMemberVideo();
                    }
                    liveLianmai.setText("开始连麦");
                } else {
                    if (customDialog != null) {
                        customDialog.show();
                    }
                }
                break;*/
            case R.id.host_switch_cam:
                Log.e("===", ScreenUtils.getScreenW(this) + "    " + ScreenUtils.getScreenH(this));
               /* avRootView.getViewByIndex(0).setPosWidth(ScreenUtils.getScreenW(this));
                avRootView.getViewByIndex(0).setPosHeight(ScreenUtils.getScreenH(this));
                avRootView.getViewByIndex(0).autoLayout();*/
                ILiveRoomManager.getInstance().switchCamera(1 - ILiveRoomManager.getInstance().getCurCameraId());
                // avRootView.getViewByIndex(0).setMirror(true);

                break;
        }

    }

    /*
    结束直播
     */
    private void finishLive() {
        showLoading("正在退出中，请稍后.");
        HttpManager.getInstance().quitLiveRoom(userId, new HttpSubscriber<String>(new OnResultCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mLiveHelper.startExitRoom();
                    }
                }).start();
            }

            @Override
            public void onError(int code, String errorMsg) {
                hideLoading();
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));


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
        if (money <= 0) {
            ToastUtil.show("金额数必须大于0");
            return;
        }
        customDialog.dismiss();
        setJoinMoney(dialogEd.getText().toString());

    }

    /**
     * 主播设置连麦的金额
     */
    private void setJoinMoney(String money) {
        HttpManager.getInstance().setJoinMoney(userId, money, new HttpSubscriber<String>(new OnResultCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                isCanLinked = true;
                ToastUtil.show("已开启连麦");
                inviteView1.setVisibility(View.VISIBLE);
                inviteView1.setText("等待观众连麦中");
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


    private LinkedList<LinkeNumberInfo> interactionDataSource; // 互动连麦人的数量
    private LinkeNumberInfo currentLinked;  // 当前正在连麦的人
    private CustomDialog linkDialog;
    private LiveHelper mLiveHelper;

    private ListPopupWindow<LinkeNumberInfo> listPopupWindow;  // 用来显示其他连麦的人数
    private CommonAdapter<LinkeNumberInfo> adapter;

    private void initDailog() {
        linkDialog = new CustomDialog(mContext);
        linkDialog.setTitleVisible(View.GONE);
        linkDialog.setViewLineVisible(View.GONE);
        linkDialog.setCancelTv("拒绝");
        linkDialog.setOkTv("同意");
        linkDialog.setDialogClick(new CustomDialog.DialogClick() {
            @Override
            public void onCancel() {
                linkDialog.dismiss();
                // 对连麦者发送 消息 同意
                mLiveHelper.sendC2CCmd(MContants.AVIMCMD_MUlTI_REFUSE, "", currentLinked.id, null);
                isWait = false;
            }

            @Override
            public void onOk() {
                linkDialog.dismiss();
                inviteView1.setText("等待" + currentLinked.nickName + "连麦中");
                isWait = true;
                // hideLinkView();
                // 对连麦者发送 消息 同意
                mLiveHelper.sendC2CCmd(MContants.AVIMCMD_MUlTI_JOIN, "", currentLinked.id, new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {

                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {

                    }
                });

            }
        });


        alerDialog = new CustomDialog(mContext);
        alerDialog.setTitleVisible(View.GONE);
        alerDialog.setTitleVisible(View.GONE);

        adapter = new CommonAdapter<LinkeNumberInfo>(mContext, R.layout.item_month, interactionDataSource) {

            @Override
            protected void convert(ViewHolder holder, LinkeNumberInfo linkeNumberInfo, int position) {
                holder.setText(R.id.item_tv, linkeNumberInfo.nickName);
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                currentLinked = interactionDataSource.get(position);
                linkDialog.setContent(currentLinked.nickName + "请求与您连麦");
                linkDialog.show();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        listPopupWindow = new ListPopupWindow<>(mContext, adapter, getResources().getDimensionPixelOffset(R.dimen.link_pop_width), getResources().getDimensionPixelOffset(R.dimen.link_pop_height));

    }

    private void startPush() {
        ILivePushOption option = new ILivePushOption();
        option.channelName(userId + "房间");
        option.encode(ILivePushOption.Encode.RTMP);
        mLiveHelper.startPush(option);//开启推流

    }

    public void pushStream() {
        if (!mIsPublishStreamStarted) {
            startPush();
        } else {
            mLiveHelper.stopPush();
        }
    }

    @Override
    public void enterRoomComplete(boolean b) {
        // 重置美颜
        ILiveRoomManager.getInstance().enableBeauty(8);
        ILiveRoomManager.getInstance().enableWhite(8);
        avRootView.getViewByIndex(0).setVisibility(GLView.VISIBLE);
        getUserDatas();
        // 通知服务器  进入房间
        HttpManager.getInstance().creatLiveRoom(userId, userId, new HttpSubscriber<String>(new OnResultCallBack<String>() {

            @Override
            public void onSuccess(String s) {

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

    @Override
    public void quiteRoomComplete(boolean b, Object o) {
        //通知server 我下线了
        //avRootView.clearUserView();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoading();
                finish();
            }
        });

    }

    @Override
    public void showInviteDialog(LinkeNumberInfo identifier) {
        if (isCanLinked) {   // 开启连麦
            if (isLinking) {

                // 可以允许多个连麦
         /*   if (interactionDataSource == null)
                interactionDataSource = new LinkedList<>();
            interactionDataSource.add(identifier);*/

                mLiveHelper.sendC2CCmd(MContants.AVIMCMD_MUlTI_LINKING, "", currentLinked.id, null);
            } else {
                currentLinked = identifier;
                linkDialog.setContent(identifier.nickName + "请求与您连麦");
                linkDialog.show();

            }

        } else {    // 开启连麦
            mLiveHelper.sendC2CCmd(MContants.AVIMCMD_MUlTI_NOSTARTLINK, "", identifier.id, null);
        }

    }

    @Override
    public void cancelInviteView(String identifier, boolean b) {
        if (b) {   //连麦成功
            isWait = false;
            hideLinkView();
        } else {  // 在未连接成功之前 取消
            isWait = false;
            hideLinkView();
        }
    }

    @Override
    public void stopStreamSucc() {
        mIsPublishStreamStarted = true;
        ControlButton.setText("开始直播");
    }

    @Override
    public void pushStreamSucc(ILivePushRes data) {
        mIsPublishStreamStarted = true;
        ControlButton.setText("结束直播");
    }

    @Override
    public void memberJoin(String identifier, String nickname) {
        // super.memberJoin(identifier, nickname);
        //  ToastUtil.show("刷新用户的头像");
        getUserDatas();
    }

    @Override
    public void onBackPressed() {
        if (alerDialog != null) {
            alerDialog.setContent("是否结束直播？");
            alerDialog.setDialogClick(new CustomDialog.DialogClick() {
                @Override
                public void onCancel() {
                    alerDialog.dismiss();
                }

                @Override
                public void onOk() {

                    ILVCustomCmd cmd = new ILVCustomCmd();
                    cmd.setCmd(MContants.AVIMCMD_EXITLIVE);
                    cmd.setType(ILVText.ILVTextType.eGroupMsg);
                    ILVLiveManager.getInstance().sendCustomCmd(cmd, new ILiveCallBack<TIMMessage>() {
                        @Override
                        public void onSuccess(TIMMessage data) {
                            //如果是直播，发消息
                            if (null != mLiveHelper) {
                                finishLive();
                                if (mIsPublishStreamStarted) {
                                    mLiveHelper.stopPush();
                                }
                            }
                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {
                            ToastUtil.show(errMsg);
                        }

                    });
                    alerDialog.dismiss();


                }
            });
            alerDialog.show();
        }

    }

    @Override
    public void linkOther() {
        showLinkView();
        isWait = false;


    }

    private void
    getUserDatas() {
        HttpManager.getInstance().getAudiences(userId, new HttpSubscriber<List<UserBean>>(new OnResultCallBack<List<UserBean>>() {

            @Override
            public void onSuccess(List<UserBean> userBeen) {
                if (userBeen != null) {
                    userDatas.clear();
                    userDatas.addAll(userBeen);
                    livePersonNum.setText("" + userBeen.size());
                    avatorAdapter.notifyDataSetChanged();
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

    private void hideLinkView() {
        inviteView1.setVisibility(View.GONE);
        inviteView1.setText("等待粉丝连麦中");
    }

    private void showLinkView() {
        inviteView1.setVisibility(View.VISIBLE);

    }

}
