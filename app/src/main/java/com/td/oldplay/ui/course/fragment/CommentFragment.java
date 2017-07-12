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
import com.td.oldplay.ui.course.adapter.CommentAdapter;

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
    private List<CommentBean> datas;
    private LoadMoreWrapper adapter;
    private CommentAdapter commentAdapter;

    int page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        datas = new ArrayList<>();
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        commentAdapter = new CommentAdapter(mActivity, R.layout.item_comment, datas);
        adapter = new LoadMoreWrapper(commentAdapter);
        // adapter.setLoadMoreView(R.layout.default_loading); //当有数据的时候 再设置布局
        swipeTarget.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);

    }

    @Override
    public void onRefresh() {
        page = 0;
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
    }
}
