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
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.adapter.CommentAdapter;
import com.td.oldplay.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {


    Unbinder unbinder;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;
    private List<CommentBean> datas = new ArrayList<>();
    private LoadMoreWrapper adapter;
    private CommentAdapter commentAdapter;

    int page = 1;
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
        id = mActivity.getIntent().getStringExtra("id");
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    protected void init(View view) {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        commentAdapter = new CommentAdapter(mActivity, R.layout.item_comment, datas);
        adapter = new LoadMoreWrapper(commentAdapter);
        swipeTarget.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);
        getData();

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

    private void getData() {
        HttpManager.getInstance().getCommentsInTeacher(page, id, new HttpSubscriber<List<CommentBean>>(new OnResultCallBack<List<CommentBean>>() {

            @Override
            public void onSuccess(List<CommentBean> commentBeen) {
                swipeToLoadLayout.setRefreshing(false);
                if (commentBeen != null && commentBeen.size() > 0) {
                    if (page == 1) {
                        datas.clear();
                        if (commentBeen.size() >= MContants.PAGENUM) {
                            adapter.setLoadMoreView(R.layout.default_loading);
                        }

                    }
                    datas.addAll(commentBeen);
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
        }));
    }

}
