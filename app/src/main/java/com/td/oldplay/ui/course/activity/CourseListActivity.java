package com.td.oldplay.ui.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.CourseTypeBean;
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
    private List<CourseTypeBean> datas = new ArrayList<>();
    private int type; // 0  课程类别  1
    private callBack callBack;
    private String titles;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        titles=getIntent().getStringExtra("title");
        id=getIntent().getStringExtra("id");
        init();


    }

    private void init() {
        switch (type) {
            case 1:
                if (TextUtils.isEmpty(titles)) {
                    title.setTitle("课程分类");
                }else{
                    title.setTitle(titles+"类课程");
                }
                break;
            case 2:
                title.setTitle("推荐课程");
                break;
            case 3:
                title.setTitle("热门课程");
                break;
            case 4:

                break;
        }

        title.setOnLeftListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        coureseAdapter = new CourserListAdapter(mContext, R.layout.item_my_teacher_list, datas);
        adapter = new LoadMoreWrapper(coureseAdapter);
        swipeTarget.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);

        coureseAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mContext, TeacherListActivity.class);
                intent.putExtra("courseId",datas.get(position).id);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        callBack = new callBack();
        getData();
    }

    private void getData() {
        switch (type) {
            case 1:
                HttpManager.getInstance().getCourseTypes(id, page, new HttpSubscriber<List<CourseTypeBean>>(callBack));

                break;
            case 2:
                HttpManager.getInstance().getCourseRecomments(page, new HttpSubscriber<List<CourseTypeBean>>(callBack));
                break;
            case 3:
                HttpManager.getInstance().getCourseHots(page, new HttpSubscriber<List<CourseTypeBean>>(callBack));
                break;
        }

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

    private class callBack implements OnResultCallBack<List<CourseTypeBean>> {


        @Override
        public void onSuccess(List<CourseTypeBean> courseTypeBeen) {
            swipeToLoadLayout.setRefreshing(false);
            if (courseTypeBeen != null && courseTypeBeen.size() > 0) {
                if (page == 1) {
                    datas.clear();
                    if (datas.size() >= MContants.PAGENUM) {
                        adapter.setLoadMoreView(R.layout.default_loading);
                    }

                }
                datas.addAll(courseTypeBeen);
            } else {
                if (page > 1) {
                    adapter.setLoadMoreView(0);
                    ToastUtil.show("没有更多数据了");
                }
            }
            coureseAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onError(int code, String errorMsg) {
            ToastUtil.show(errorMsg);
            swipeToLoadLayout.setRefreshing(false);
        }
    }

}
