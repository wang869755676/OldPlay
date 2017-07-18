package com.td.oldplay.ui.course.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.bean.TeacherDetail;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.activity.TeacherDetailActivity;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntruceFragment extends BaseFragment {


    @BindView(R.id.introduce_name)
    TextView introduceName;
    @BindView(R.id.introduce_des)
    TextView introduceDes;
    @BindView(R.id.intr_te_iv)
    CircleImageView intrTeIv;
    @BindView(R.id.intr_te_name)
    TextView intrTeName;
    @BindView(R.id.item_school)
    TextView itemSchool;
    Unbinder unbinder;
    private TeacherDetail bean;

    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intruce, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    private void getData() {
        HttpManager.getInstance().getCourseDetail(id,new HttpSubscriber<TeacherDetail>(new OnResultCallBack<TeacherDetail>() {

            @Override
            public void onSuccess(TeacherDetail teacherDetail) {
                bean=teacherDetail;
                if(bean!=null){
                    setDatas();
                }
            }

            @Override
            public void onError(int code, String errorMsg) {

            }
        }));
    }

    private void setDatas() {
        introduceName.setText(bean.name);
        introduceDes.setText(bean.profile);
        if(bean.user!=null){
           // intrTeIv
            GlideUtils.setAvatorImage(mActivity,bean.user.avatar,intrTeIv);
            intrTeName.setText(bean.user.nickName);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void init(View view) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(EventMessage message) {
        if ("changeCourse".equals(message.action)) {
            // 切换直播视频
            id = ((TeacherDetailActivity) mActivity).currentBean.coursesId;
            getData();
        }
    }


}
