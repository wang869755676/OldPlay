package com.td.oldplay.ui.forum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.ForumBean;
import com.td.oldplay.ui.forum.adapter.ForumListAdapter;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForumListActivity extends BaseFragmentActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.forum_publish)
    TextView forumPublish;

    private List<ForumBean> datas = new ArrayList<>();
    private ForumListAdapter listAdapter;
    private LoadMoreWrapper adapter;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setTitle("XX板块");
        title.setOnLeftListener(this);
        swipeLayout.setOnRefreshListener(this);
        forumPublish.setOnClickListener(this);
        datas.add(new ForumBean());
        datas.add(new ForumBean());
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        listAdapter = new ForumListAdapter(mContext, R.layout.item_forum, datas);
        adapter = new LoadMoreWrapper(listAdapter);
        adapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(adapter);
        listAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(AContext, FourmDetailActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.forum_publish:
                break;
        }

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
