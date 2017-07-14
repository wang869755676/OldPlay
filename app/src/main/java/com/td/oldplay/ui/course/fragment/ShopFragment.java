package com.td.oldplay.ui.course.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.ShopBean;
import com.td.oldplay.ui.SearchActivity;
import com.td.oldplay.ui.course.adapter.CourserAdapter;
import com.td.oldplay.ui.course.adapter.ShopAdapter;
import com.td.oldplay.ui.shop.activity.ShopDetailActivity;

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
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.shop_seach)
    TextView shopSeach;

    @BindView(R.id.rb_price)
    CheckBox rbPrice;
    @BindView(R.id.rl_price)
    FrameLayout rlPrice;
    @BindView(R.id.rb_score)
    CheckBox rbScore;
    @BindView(R.id.rl_score)
    FrameLayout rlScore;
    @BindView(R.id.rb_sell)
    CheckBox rbSell;
    @BindView(R.id.rl_sell)
    FrameLayout rlSell;

    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;
    Unbinder unbinder;


    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;

    private List<ShopBean> datas;
    private LoadMoreWrapper adapter;
    private int page;
    private ShopAdapter shopAdapter;
    private boolean priceup;
    private boolean sellup;
    private boolean scoreup;

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
    protected void init(View view) {
        swipeToLoadLayout.setOnRefreshListener(this);
        rbPrice.setOnCheckedChangeListener(this);
        rbScore.setOnCheckedChangeListener(this);
        rbSell.setOnCheckedChangeListener(this);
        rbPrice.setOnClickListener(this);
        rbScore.setOnClickListener(this);
        rbSell.setOnClickListener(this);
        shopSeach.setOnClickListener(this);

        datas = new ArrayList<>();
        datas = new ArrayList<>();
        datas.add(new ShopBean());
        datas.add(new ShopBean());
        swipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        shopAdapter = new ShopAdapter(mActivity, R.layout.item_shop, datas);
        adapter = new LoadMoreWrapper(shopAdapter);
        adapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(adapter);
        shopAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(mActivity, ShopDetailActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    @Override
    public void onLoadMoreRequested() {
        page++;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rb_price:
            case R.id.rb_score:
            case R.id.rb_sell:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_price:
                priceup = true;
                break;
            case R.id.rb_score:
                break;
            case R.id.rb_sell:
                break;
            case R.id.shop_seach:
                Intent intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra("type", 1);
                // intent.putExtra()
                startActivity(intent);
        }
    }
}
