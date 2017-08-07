package com.td.oldplay.ui.course.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.bean.ScoreOffset;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.pay.zhifubao.PayResult;
import com.td.oldplay.ui.course.activity.TeacherDetailActivity;
import com.td.oldplay.ui.course.adapter.CourserAdapter;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.ui.window.PayAlertDialog;
import com.td.oldplay.ui.window.PayTypeDialog;
import com.td.oldplay.ui.window.PayTypePopupWindow;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.password.PasswordInputView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        LoadMoreWrapper.OnLoadMoreListener,
        CustomDialog.DialogClick,
        PayTypeDialog.DialogClick {

    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;
    Unbinder unbinder;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    int page = 1;
    private List<CourseBean> datas;
    private LoadMoreWrapper adapter;
    private CourserAdapter courserAdapter;
    private CustomDialog customDialog;
    private String id;

    private CourseBean currentCourse;


    private PayTypeDialog payTypeDialog;
    private PayAlertDialog teacherDialog;

    private PayAlertDialog accountDialog;
    private PayAlertDialog paySuccessDialog;
    private CustomDialog passwordDialog;

    private PasswordInputView passwordInputView;
    private View dialogView;
    private String password;

    private ScoreOffset scoreOffset;

    private float coursePrice;

    private boolean isCoursePay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        id = mActivity.getIntent().getStringExtra("id");
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }


    @Override
    protected void init(View view) {
        datas = new ArrayList<>();

        swipeToLoadLayout.setOnRefreshListener(this);
        customDialog = new CustomDialog(mActivity);
        customDialog.setTitleVisible(View.GONE);
        customDialog.setDialogClick(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        courserAdapter = new CourserAdapter(mActivity, R.layout.item_courese, datas);
        adapter = new LoadMoreWrapper(courserAdapter);
        //adapter.setLoadMoreView(R.layout.default_loading);
        adapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(adapter);
        courserAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ((TeacherDetailActivity) mActivity).currentBean = datas.get(position);
                currentCourse = datas.get(position);
                if (datas.get(position).isBuy == 0) {
                    customDialog.setContent("支付" + datas.get(position).price + "元购买课程");
                    customDialog.show();
                } else if (datas.get(position).isBuy == 1) {
                    EventBus.getDefault().post(new EventMessage("changeCourseVideo"));  // 支付成功触发改事件
                } else if (datas.get(position).isBuy == 2) {
                    ToastUtil.show("等待老师开通中");
                }


            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        teacherDialog = new PayAlertDialog(mActivity, false, false);
        teacherDialog.setContent("申请提交成功\n" +
                "请尽快联系老师开通课程");
        paySuccessDialog = new PayAlertDialog(mActivity, false, false);
        paySuccessDialog.setContent("支付成功");
        paySuccessDialog.setDialogClick(new PayAlertDialog.DialogClick() {
            @Override
            public void onBack() {

            }

            @Override
            public void onnext() {
                EventBus.getDefault().post(new EventMessage("changeCourseVideo"));  // 支付成功触发改事件
            }
        });

        passwordDialog = new CustomDialog(mActivity);
        passwordDialog.setTitle("输入密码");
        passwordDialog.setCancelTv("返回");
        dialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_password, null);
        passwordInputView = (PasswordInputView) dialogView.findViewById(R.id.password);
        passwordDialog.setContanier(dialogView);
        passwordDialog.setDialogClick(new CustomDialog.DialogClick() {
            @Override
            public void onCancel() {
                if (accountDialog != null) {
                    accountDialog.show();
                }
            }

            @Override
            public void onOk() {
                password = passwordInputView.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.show("请输入密码");
                    return;
                }
                customDialog.dismiss();
                // 账户支付
                if (paySuccessDialog != null) {
                    paySuccessDialog.show();
                }

            }
        });
        accountDialog = new PayAlertDialog(mActivity, true, true);
        accountDialog.setDialogClick(new PayAlertDialog.DialogClick() {
            @Override
            public void onBack() {
                if (payTypeDialog != null) {
                    payTypeDialog.show();
                }
            }

            @Override
            public void onnext() {
                if (userBean.money < coursePrice) {
                    ToastUtil.show("账户余额不足，清选择其他支付方式");
                    if (payTypeDialog != null) {
                        payTypeDialog.show();
                    }
                    return;
                }
                if (passwordDialog != null) {
                    passwordDialog.show();
                }
            }
        });
        getData();

    }

    private void getData() {
        HttpManager.getInstance().getcoursesInTeacher(page, userId, id, new HttpSubscriber<List<CourseBean>>(new OnResultCallBack<List<CourseBean>>() {


            @Override
            public void onSuccess(List<CourseBean> courseBeen) {
                swipeToLoadLayout.setRefreshing(false);
                if (courseBeen != null && courseBeen.size() > 0) {
                    if (page == 1) {
                        datas.clear();
                        if (courseBeen.size() >= MContants.PAGENUM) {
                            adapter.setLoadMoreView(R.layout.default_loading);
                        }

                    }
                    datas.addAll(courseBeen);
                } else {
                    if (page > 1) {
                        adapter.setLoadMoreView(0);
                        ToastUtil.show("没有更多数据了");
                    }
                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onError(int code, String errorMsg) {
                swipeToLoadLayout.setRefreshing(false);
                ToastUtil.show(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getData();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onOk(int payType, String scoreId) {
        switch (payType) {
            case 0:
                if (scoreId != null && !scoreId.equals("")) {
                    getApplyScoreMoney();
                } else {
                    accountDialog.setContent("账户余额支付" + currentCourse.price + "元");
                    accountDialog.setScoreVisible(View.GONE);
                    accountDialog.show();
                }

                break;
            case 1:
                isCoursePay = true;
                ToastUtil.show("微信支付");


                EventBus.getDefault().post(new EventMessage("changeCourseVideo"));  // 支付成功触发改事件

                break;
            case 2:
                ToastUtil.show("支付宝支付");

                EventBus.getDefault().post(new EventMessage("changeCourseVideo"));  // 支付成功触发改事件
                break;
            case 3:
                ToastUtil.show("找老师开通");
                if (currentCourse != null) {
                    HttpManager.getInstance().openCourse(userId, currentCourse.coursesId, new HttpSubscriber<String>(new OnResultCallBack<String>() {

                        @Override
                        public void onSuccess(String s) {
                            currentCourse.isBuy = 2;
                            courserAdapter.notifyDataSetChanged();
                            if (teacherDialog != null) {
                                teacherDialog.show();
                            }

                            ToastUtil.show("等待老师开通");
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

                break;

        }
    }


    @Override
    public void onOk() {
        HttpManager.getInstance().getPayScoreRule(new HttpSubscriber<ScoreOffset>(new OnResultCallBack<ScoreOffset>() {


            @Override
            public void onSuccess(ScoreOffset scoreOffset) {
                if (payTypeDialog == null) {
                    payTypeDialog = new PayTypeDialog(mActivity, true);
                    payTypeDialog.setDialogClick(CourseFragment.this);
                    if (scoreOffset != null) {
                        payTypeDialog.setScoreOffset(scoreOffset);
                        payTypeDialog.setScoreVisisble(View.VISIBLE);
                    } else {
                        payTypeDialog.setScoreVisisble(View.GONE);
                    }

                    if (currentCourse != null) {
                        payTypeDialog.setTitle("支付" + currentCourse.price + "购买课程");
                    }

                }
                payTypeDialog.setAccount(userBean.money + "元");
                payTypeDialog.show();

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


    /**
     * 使用使用积分抵消之后所得到的价格
     */
    private void getApplyScoreMoney() {

        accountDialog.setContent("账户余额支付" + currentCourse.price + "元");
        accountDialog.setScoreVisible(View.GONE);
        accountDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(EventMessage message) {
        if (isCoursePay) {
            if ("WX".equals(message.action)) {
                switch (message.WxPayCode) {
                    case 0:
                        ToastUtil.show("支付成功！");
                        paySuccessDialog.show();
                        isCoursePay=false;
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

        }
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
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (paySuccessDialog != null) {
                            paySuccessDialog.show();
                        }
                    } else {
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


}