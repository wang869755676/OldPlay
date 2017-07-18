package com.td.oldplay.ui.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.adapter.CommentAdapter;
import com.td.oldplay.ui.course.adapter.TeacherAdapter;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeacherListActivity extends BaseFragmentActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;

    private LoadMoreWrapper adapter;
    private TeacherAdapter teacherAdapter;
    private int page = 1;
    private boolean isMore;
    private List<TeacherBean> datas = new ArrayList<>();
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        ButterKnife.bind(this);
        courseId=getIntent().getStringExtra("courseId");
        init();


    }

    private void init() {
        title.setTitle("讲师列表");
        title.setOnLeftListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        teacherAdapter = new TeacherAdapter(mContext, R.layout.item_teacher_list, datas, 0);
        adapter = new LoadMoreWrapper(teacherAdapter);
        swipeTarget.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);

        teacherAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mContext, TeacherDetailActivity.class);
                intent.putExtra("id",datas.get(position).userId);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        getData();
    }

    private void getData() {
        HttpManager.getInstance().getTeachersInCourse(page, courseId,new HttpSubscriber<List<TeacherBean>>(new OnResultCallBack<List<TeacherBean>>() {


            @Override
            public void onSuccess(List<TeacherBean> teacherBeen) {
                swipeToLoadLayout.setRefreshing(false);
                if (teacherBeen != null && teacherBeen.size() > 0) {
                    if (page == 1) {
                        datas.clear();
                        if (teacherBeen.size() >= MContants.PAGENUM) {
                            adapter.setLoadMoreView(R.layout.default_loading);
                        }

                    }
                    datas.addAll(teacherBeen);
                } else {
                    adapter.setLoadMoreView(0);
                    if (page > 1) {

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
        }));


    }

    @Override
    public void onClick(View v) {
        finish();
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
}
