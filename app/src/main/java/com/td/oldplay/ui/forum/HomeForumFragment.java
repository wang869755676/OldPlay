package com.td.oldplay.ui.forum;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.bean.ForumBean;
import com.td.oldplay.ui.forum.activity.ForumListActivity;
import com.td.oldplay.ui.forum.adapter.HomeForumAdapter;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeForumFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    Unbinder unbinder;

    private HomeForumAdapter homeForumAdapter;
    private List<ForumBean> datas = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_forum, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view) {
        title.setTitle("论坛");
        title.setLeftGone();
        datas.add(new ForumBean());
        datas.add(new ForumBean());
        homeForumAdapter = new HomeForumAdapter(mActivity, R.layout.item_home_forum, datas);
        swipeTarget.setLayoutManager(new GridLayoutManager(mActivity, 2));
        swipeTarget.setAdapter(homeForumAdapter);
        homeForumAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(mActivity, ForumListActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {

    }
}
