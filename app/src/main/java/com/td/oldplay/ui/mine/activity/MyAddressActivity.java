package com.td.oldplay.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.ui.mine.adapter.AddressAdapter;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAddressActivity extends BaseFragmentActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        LoadMoreWrapper.OnLoadMoreListener,
        View.OnClickListener,
        AddressAdapter.OnItemActionListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.forum_publish)
    TextView forumPublish;

    private List<AddressBean> datas = new ArrayList<>();
    private AddressAdapter addressAdapter;
    private LoadMoreWrapper adapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setTitle("地址变更");
        title.setOnLeftListener(this);
        title.setOnRightListener(this);
        // title.setRightImageResource();
        swipeLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        datas.add(new AddressBean());
        addressAdapter = new AddressAdapter(mContext, R.layout.item_address, datas);
        adapter = new LoadMoreWrapper(addressAdapter);
        adapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(addressAdapter);
    }

    @Override
    public void onRefresh() {
        page = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.right_text: // 添加资质
                startActivity(new Intent(mContext, AddAddressActivity.class));
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
    }

    @Override
    public void onAction(String action, int postion, AddressBean item) {
        if (action.equals("default")) {

        } else if (action.equals("update")) {
            Intent intent = new Intent(mContext, AddAddressActivity.class);
            intent.putExtra("mode", item);
            startActivity(intent);
        } else if (action.equals("delete")) {


        }
    }
}
