package com.td.oldplay.ui.shop.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.ShopCarBean;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopCarActivity extends BaseFragmentActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        LoadMoreWrapper.OnLoadMoreListener,
        View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;
    @BindView(R.id.car_cb_all)
    CheckBox carCbAll;
    @BindView(R.id.car_all)
    FrameLayout carAll;
    @BindView(R.id.car_jiesuan)
    TextView carJiesuan;

    private List<ShopCarBean> datas = new ArrayList<>();
    private CarAdapter carAdapter;
    private LoadMoreWrapper addapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        datas.add(new ShopCarBean());
        datas.add(new ShopCarBean());
        title.setTitle("购物车");
        title.setOnLeftListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        carAdapter = new CarAdapter(mContext, R.layout.item_car, datas);
        addapter = new LoadMoreWrapper(carAdapter);
        addapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(addapter);
        carJiesuan.setOnClickListener(this);
        carCbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                makeAllCheck(isChecked);

            }
        });
    }

    /**
     * 全选
     *
     * @param isChecked
     */
    private void makeAllCheck(boolean isChecked) {
        for (ShopCarBean carBean :
                datas) {
            carBean.isCheck = isChecked;
        }
        addapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.car_jiesuan:
                break;
        }

    }

    private static class CarAdapter extends CommonAdapter<ShopCarBean> {

        public CarAdapter(Context context, int layoutId, List<ShopCarBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(final ViewHolder holder, final ShopCarBean shopCarBean, int position) {
            ((CheckBox) holder.getView(R.id.item_car_checked)).setChecked(shopCarBean.isCheck);
            holder.setOnClickListener(R.id.item_car_add, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopCarBean.num++;
                    ((TextView) holder.getView(R.id.item_car_num)).setText(shopCarBean.num + "");
                }
            });
            holder.setOnClickListener(R.id.item_car_add, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shopCarBean.num == 0) {
                        ToastUtil.show("当前数量为0,不能再减了");
                    } else {
                        shopCarBean.num--;
                        ((TextView) holder.getView(R.id.item_car_num)).setText(shopCarBean.num + "");
                    }

                }
            });
            ((CheckBox) holder.getView(R.id.item_car_checked)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    shopCarBean.isCheck = isChecked;
                }
            });
        }

        private static View.OnClickListener ClickListenr = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_car_add:

                        break;
                }
            }
        };

    }
}
