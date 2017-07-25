package com.td.oldplay.ui.shop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.ShopType;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.activity.TeacherListActivity;
import com.td.oldplay.ui.course.adapter.CourserListAdapter;
import com.td.oldplay.ui.shop.adapter.ShopTypeAdapter;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class ShopTypeListActivity extends BaseFragmentActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    private int page = 1;
    private List<ShopType> datas = new ArrayList<>();
    private ShopTypeAdapter shopTypeAdapter;
    private LoadMoreWrapper adapter;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        id = getIntent().getIntExtra("goodTypeId", 0);
        ButterKnife.bind(this);
        init();


    }

    private void init() {
        if (id == 1) {
            title.setTitle("绿色产品");
        } else if (id == 2) {
            title.setTitle("教学产品");
        }
        title.setOnLeftListener(this);
        swipeLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new GridLayoutManager(mContext, 2));
        shopTypeAdapter = new ShopTypeAdapter(mContext, R.layout.item_home_forum, datas);
        adapter = new LoadMoreWrapper(shopTypeAdapter);
        swipeTarget.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);
        shopTypeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext, ShopListActivity.class);
                intent.putExtra("title", datas.get(position).name);
                intent.putExtra("type", 1);
                intent.putExtra("goodTypeId", datas.get(position).goodsTypeId);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        getData();
    }

    private void getData() {
        HttpManager.getInstance().getShopTypesById(id, new HttpSubscriber<List<ShopType>>(new OnResultCallBack<List<ShopType>>() {


            @Override
            public void onSuccess(List<ShopType> shopTypes) {
                swipeLayout.setRefreshing(false);
                if (shopTypes != null && shopTypes.size() > 0) {
                    if (page == 1) {
                        datas.clear();
                       /* if (shopTypes.size() >= MContants.PAGENUM) {
                            adapter.setLoadMoreView(R.layout.default_loading);
                        }*/

                    }
                    datas.addAll(shopTypes);
                } else {
                    adapter.setLoadMoreView(0);
                    if (page > 1) {
                        ToastUtil.show("没有更多数据了");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String errorMsg) {
                swipeLayout.setRefreshing(false);
                ToastUtil.show(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));
    }

    @Override
    public void onRefresh() {
        page=1;
        getData();

    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onLoadMoreRequested() {

    }
}
