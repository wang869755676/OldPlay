package com.td.oldplay.ui.course.fragment;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.ShopBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.adapter.ShopAdapter;
import com.td.oldplay.ui.shop.activity.ShopDetailActivity;
import com.td.oldplay.ui.window.SeachPopupWindow;
import com.td.oldplay.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener,
        View.OnClickListener{


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
    private int page = 1;
    private ShopAdapter shopAdapter;

    private int type;
    private int goodType; //1 绿色产品 2教学产品
    private callBack callBack;
    private String id;
    private int sortType = 0; //排序的类型,0:价格,1:积分,2:销量
    private int sort = 0; //排序方式,0:降序,1:升序
    private boolean priceDec = true;
    private int priceSort;
    private boolean scoreSell = true;
    private int scoreSort;
    private boolean sellDec = true;
    private int sellSort;
    private Drawable greyDrawable;
    private Drawable upDrawable;
    private Drawable downDrawable;

    private SeachPopupWindow popupWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        type = mActivity.getIntent().getIntExtra("type", 0);
        goodType = mActivity.getIntent().getIntExtra("goodTypeId", 0);
        id = mActivity.getIntent().getStringExtra("id");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }


    @Override
    protected void init(View view) {
        callBack = new callBack();
        swipeToLoadLayout.setOnRefreshListener(this);
        rbPrice.setOnClickListener(this);
        rbScore.setOnClickListener(this);
        rbSell.setOnClickListener(this);
        shopSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    if (popupWindow == null) {
                        popupWindow = new SeachPopupWindow(mActivity, 1, goodType);
                    }
                } else {
                    if (popupWindow == null) {
                        popupWindow = new SeachPopupWindow(mActivity, 1);
                    }
                }
                popupWindow.showPopup(v);
            }
        });

        datas = new ArrayList<>();

        swipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        shopAdapter = new ShopAdapter(mActivity, R.layout.item_shop, datas);
        adapter = new LoadMoreWrapper(shopAdapter);
        adapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(adapter);
        shopAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, ShopDetailActivity.class);
                intent.putExtra("id", datas.get(position).goodsId);
                startActivity(intent);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        greyDrawable = getResources().getDrawable(R.mipmap.icon_shop_gray);
        upDrawable = getResources().getDrawable(R.mipmap.icon_tri_up);
        downDrawable = getResources().getDrawable(R.mipmap.icon_tri_down);
        getData();

    }

    private void getData() {
        switch (type) {
            case 1:
                HttpManager.getInstance().getShopByType(page, goodType, sortType, sort, new HttpSubscriber<List<ShopBean>>(callBack));
                break;
            case 2:
                HttpManager.getInstance().getShopRecomments(page, sortType, sort, new HttpSubscriber<List<ShopBean>>(callBack));
                break;
            case 3:
                HttpManager.getInstance().getShopDiscounts(page, sortType, sort, new HttpSubscriber<List<ShopBean>>(callBack));
                break;
            default:
                // 老师中的商品
                HttpManager.getInstance().getShopInTeacher(id, page, sortType, sort, new HttpSubscriber<List<ShopBean>>(callBack));
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getData();
    }

    @Override
    public void onClick(View v) {
        clearDrawable();
        switch (v.getId()) {
            case R.id.rb_price:
                if (priceDec) {

                    priceDec = false;
                   priceSort = 1;
                    rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, upDrawable, null);
                } else {
                    priceDec = true;
                    priceSort = 0;
                    rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, downDrawable, null);
                }
                sort = priceSort;
                break;
            case R.id.rb_score:
                if (scoreSell) {
                    scoreSell = false;
                    scoreSort = 1;
                    rbScore.setCompoundDrawablesWithIntrinsicBounds(null, null, upDrawable, null);
                } else {
                    scoreSell = true;
                    scoreSort = 0;
                    rbScore.setCompoundDrawablesWithIntrinsicBounds(null, null, downDrawable, null);
                }
                sort = scoreSort;
                break;
            case R.id.rb_sell:

                if (sellDec) {
                    sellDec = false;
                    sellSort= 1;
                    rbSell.setCompoundDrawablesWithIntrinsicBounds(null, null, upDrawable, null);
                } else {
                    sellDec = true;
                    sellSort = 0;
                    rbSell.setCompoundDrawablesWithIntrinsicBounds(null, null, downDrawable, null);

                }
                sort=sellSort;
                break;
        }
        page = 1;
        getData();
    }

    private void clearDrawable() {
        rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, greyDrawable, null);
        rbSell.setCompoundDrawablesWithIntrinsicBounds(null, null, greyDrawable, null);
        rbScore.setCompoundDrawablesWithIntrinsicBounds(null, null, greyDrawable, null);

    }

    private class callBack implements OnResultCallBack<List<ShopBean>> {

        @Override
        public void onSuccess(List<ShopBean> shopBean) {
            swipeToLoadLayout.setRefreshing(false);
            if (shopBean != null && shopBean.size() > 0) {
                if (page == 1) {
                    datas.clear();
                    if (shopBean.size() >= MContants.PAGENUM) {
                        adapter.setLoadMoreView(R.layout.default_loading);
                    }

                }
                datas.addAll(shopBean);
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
            ToastUtil.show(errorMsg);
            swipeToLoadLayout.setRefreshing(false);
        }

        @Override
        public void onSubscribe(Disposable d) {
            addDisposable(d);
        }
    }
}
