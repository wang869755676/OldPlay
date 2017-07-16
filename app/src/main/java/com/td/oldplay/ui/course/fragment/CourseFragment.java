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
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.ui.course.adapter.CourserAdapter;
import com.td.oldplay.ui.window.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        LoadMoreWrapper.OnLoadMoreListener,
        CustomDialog.DialogClick {

    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;
    Unbinder unbinder;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    int page;
    private List<CourseBean> datas;
    private LoadMoreWrapper adapter;
    private CourserAdapter courserAdapter;
    private CustomDialog customDialog;

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
        datas = new ArrayList<>();
        datas = new ArrayList<>();
        datas.add(new CourseBean());
        datas.add(new CourseBean());

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

                customDialog.setContent("支付XX元购买课程");
                customDialog.show();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    @Override
    public void onLoadMoreRequested() {
        page++;

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onOk() {

    }
}
