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
import com.td.oldplay.bean.ForumType;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.forum.activity.ForumListActivity;
import com.td.oldplay.ui.forum.adapter.HomeForumAdapter;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;


public class HomeForumFragment extends BaseFragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    Unbinder unbinder;

    private HomeForumAdapter homeForumAdapter;
    private List<ForumType> datas = new ArrayList<>();
    private HttpSubscriber<List<ForumType>> subscriber;


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
        swipeLayout.setOnRefreshListener(this);
        homeForumAdapter = new HomeForumAdapter(mActivity, R.layout.item_home_forum, datas);
        swipeTarget.setLayoutManager(new GridLayoutManager(mActivity, 2));
        swipeTarget.setAdapter(homeForumAdapter);
        homeForumAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity, ForumListActivity.class);
                intent.putExtra("id",datas.get(position).boardId);
                intent.putExtra("title",datas.get(position).name);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        subscriber = new HttpSubscriber<List<ForumType>>(new OnResultCallBack<List<ForumType>>() {

            @Override
            public void onSuccess(List<ForumType> forumTypes) {
                datas.clear();
                swipeLayout.setRefreshing(false);
                if (forumTypes != null && forumTypes.size() > 0) {
                    datas.addAll(forumTypes);
                }
                homeForumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        });
        getData();
    }

    private void getData() {
        HttpManager.getInstance().getHomeForums(subscriber);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (subscriber != null) {
            subscriber.unSubscribe();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {

        getData();
    }
}
