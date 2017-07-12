package com.td.oldplay.ui.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.adapter.CourserAdapter;
import com.td.oldplay.ui.course.adapter.CourserListAdapter;
import com.td.oldplay.ui.course.adapter.TeacherAdapter;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseListActivity extends BaseFragmentActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;

    private LoadMoreWrapper adapter;
    private CourserListAdapter coureseAdapter;
    private int page = 1;
    private List<CourseBean> datas = new ArrayList<>();
    private int type; // 0  课程类别  1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        ButterKnife.bind(this);
        init();


    }

    private void init() {
        title.setTitle("X类列表");
        title.setOnLeftListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        datas.add(new CourseBean());

        coureseAdapter = new CourserListAdapter(mContext, R.layout.item_my_teacher_list, datas);
        adapter = new LoadMoreWrapper(coureseAdapter);
        swipeTarget.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);

        coureseAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(mContext, TeacherListActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        getData();
    }

    private void getData() {
    /*    HttpManager.getInstance().getTeacherLists("eyJsYXRpdHVkZSI6IjMwLjMxMjUzNyIsImxvbmdpdHVkZSI6IjEyMC4xMjkwNTYiLCJ0b2tlbiI6Ik1UTXdOVEV5TmpFNU1UUVx1MDAzZCIsInBhZ2UiOjF9",
                new HttpSubscriber<List<TeacherBean>>(new OnResultCallBack<List<TeacherBean>>() {
                    @Override
                    public void onSuccess(List<TeacherBean> o) {
                        swipeToLoadLayout.setRefreshing(false);
                        if (o != null && o.size() > 0) {
                            if (page == 1) {
                                datas.clear();
                                if (datas.size() >= MContants.PAGENUM) {
                                    adapter.setLoadMoreView(R.layout.default_loading);
                                }

                            }
                            datas.addAll(o);
                        } else {
                            if (page > 1) {
                                adapter.setLoadMoreView(0);
                                ToastUtil.show("没有更多数据了");
                            }
                        }
                        Log.e("===", datas.size() + "-----------------");
                        coureseAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        Log.e("==", errorMsg);
                        swipeToLoadLayout.setRefreshing(false);
                    }
                }));
*/
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
