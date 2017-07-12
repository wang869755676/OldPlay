package com.td.oldplay.ui.course.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.ui.course.adapter.CommentAdapter;
import com.td.oldplay.ui.course.adapter.CourserAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;
    Unbinder unbinder;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    private List<CourseBean> datas;
    private LoadMoreWrapper adapter;

    int page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        page = 0;
    }


    @Override
    protected void init(View view) {
        swipeToLoadLayout.setOnRefreshListener(this);
        datas = new ArrayList<>();
        datas = new ArrayList<>();
        datas.add(new CourseBean());
        datas.add(new CourseBean());
        swipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new LoadMoreWrapper(new CourserAdapter(mActivity, R.layout.item_courese, datas));
        //adapter.setLoadMoreView(R.layout.default_loading);
        adapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(adapter);

    }

    @Override
    public void onLoadMoreRequested() {
        page++;

    }
}
