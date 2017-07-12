package com.td.oldplay.ui.mine.activity;

import android.app.Activity;
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
import com.td.oldplay.bean.GoodBean;
import com.td.oldplay.bean.OrderBean;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.activity.TeacherDetailActivity;
import com.td.oldplay.ui.course.adapter.TeacherAdapter;
import com.td.oldplay.ui.mine.adapter.OrderAdapter;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrdersActivity extends BaseFragmentActivity
        implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        LoadMoreWrapper.OnLoadMoreListener,
        OrderAdapter.OnItemActionListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;

    private LoadMoreWrapper adapter;
    private OrderAdapter orderAdapter;
    private int page = 1;
    private boolean isMore;
    private List<OrderBean> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);
        init();


    }

    private void init() {
        title.setTitle("我的订单");
        title.setOnLeftListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        OrderBean bean = new OrderBean();
        List<GoodBean> goodBeen = new ArrayList<>();
        goodBeen.add(new GoodBean());
        goodBeen.add(new GoodBean());
        goodBeen.add(new GoodBean());
        bean.goodBeanList = goodBeen;
        datas.add(bean);
        datas.add(new OrderBean());
        datas.add(new OrderBean());

        orderAdapter = new OrderAdapter(mContext, R.layout.item_mine_order, datas);
        orderAdapter.setActionListener(this);
        adapter = new LoadMoreWrapper(orderAdapter);
        swipeTarget.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);

        orderAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                // startActivity(new Intent(mContext, TeacherDetailActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        getData();
    }

    private void getData() {
        HttpManager.getInstance().getTeacherLists("eyJsYXRpdHVkZSI6IjMwLjMxMjUzNyIsImxvbmdpdHVkZSI6IjEyMC4xMjkwNTYiLCJ0b2tlbiI6Ik1UTXdOVEV5TmpFNU1UUVx1MDAzZCIsInBhZ2UiOjF9",
                new HttpSubscriber<List<TeacherBean>>(new OnResultCallBack<List<TeacherBean>>() {
                    @Override
                    public void onSuccess(List<TeacherBean> o) {
                        swipeToLoadLayout.setRefreshing(false);
                        /*if (o != null && o.size() > 0) {
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
                        orderAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();*/
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        Log.e("==", errorMsg);
                        swipeToLoadLayout.setRefreshing(false);
                    }
                }));

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

    @Override
    public void onAction(int action, int postion, OrderBean item) {
        switch (action) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}
