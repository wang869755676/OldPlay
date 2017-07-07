package com.td.oldplay.ui.course.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.ShopBean;
import com.td.oldplay.ui.course.adapter.CourserAdapter;
import com.td.oldplay.ui.course.adapter.ShopAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener,
        View.OnClickListener {


    @BindView(R.id.shop_seach)
    EditText shopSeach;
    @BindView(R.id.shop_send_seach)
    Button shopSendSeach;
    @BindView(R.id.rb_price)
    RadioButton rbPrice;
    @BindView(R.id.rl_price)
    FrameLayout rlPrice;
    @BindView(R.id.rb_score)
    RadioButton rbScore;
    @BindView(R.id.rl_score)
    FrameLayout rlScore;
    @BindView(R.id.rb_sell)
    RadioButton rbSell;
    @BindView(R.id.rl_sell)
    FrameLayout rlSell;

    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;
    Unbinder unbinder;


    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;

    private List<ShopBean> datas;
    private LoadMoreWrapper adapter;
    private
    int page;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
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
    public void onClick(View v) {

    }

    @Override
    protected void init(View view) {
        swipeToLoadLayout.setOnRefreshListener(this);
        datas = new ArrayList<>();
        datas = new ArrayList<>();
        datas.add(new ShopBean());
        datas.add(new ShopBean());
        swipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new LoadMoreWrapper(new ShopAdapter(mActivity, R.layout.item_shop, datas));
        adapter.setLoadMoreView(R.layout.default_loading);
        adapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(adapter);


    }

    @Override
    public void onLoadMoreRequested() {
        page++;
    }
}
