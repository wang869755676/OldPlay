package com.td.oldplay.ui.forum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.ForumBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.forum.adapter.ForumListAdapter;
import com.td.oldplay.utils.ToastUtil;
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
    ImageView forumPublish;

    private List<ForumBean> datas = new ArrayList<>();
    private ForumListAdapter listAdapter;
    private LoadMoreWrapper adapter;
    private int page = 1;
    private String id;
    private String titleStr;
    private HttpSubscriber<List<ForumBean>> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_list);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        titleStr = getIntent().getStringExtra("title");
        initView();
    }

    private void initView() {
        title.setTitle(titleStr + "板块");
        title.setOnLeftListener(this);
        swipeLayout.setOnRefreshListener(this);
        forumPublish.setOnClickListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        listAdapter = new ForumListAdapter(mContext, R.layout.item_forum, datas);
        adapter = new LoadMoreWrapper(listAdapter);
        adapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(adapter);
        listAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(AContext, FourmDetailActivity.class);
                intent.putExtra("id",datas.get(position).topicId);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        subscriber=new HttpSubscriber<List<ForumBean>>(new OnResultCallBack<List<ForumBean>>() {

            @Override
            public void onSuccess(List<ForumBean> forumBeen) {
                swipeLayout.setRefreshing(false);
                if (forumBeen != null && forumBeen.size() > 0) {
                    if (page == 1) {
                        datas.clear();
                        if (forumBeen.size() >= MContants.PAGENUM) {
                            adapter.setLoadMoreView(R.layout.default_loading);
                        }

                    }
                    datas.addAll(forumBeen);
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
                ToastUtil.show(errorMsg);
                swipeLayout.setRefreshing(false);
            }});
        getData();
    }

    private void getData() {
        HttpManager.getInstance().getForumsNyId(id,page,subscriber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.forum_publish:
                startActivity(new Intent(mContext, PublishForumActivity.class));
                break;
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriber.unSubscribe();
    }
}
