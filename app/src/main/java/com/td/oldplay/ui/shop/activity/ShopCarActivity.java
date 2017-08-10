package com.td.oldplay.ui.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.MessageEvent;
import com.td.oldplay.bean.OrderBean;
import com.td.oldplay.bean.ShopCarBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

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
    private LoadMoreWrapper adapter;
    private int page = 1;
    private int checkNum;
    private List<String> carsId = new ArrayList<>();
    private List<ShopCarBean> updateCars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        title.setTitle("购物车");
        title.setOnLeftListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        carAdapter = new CarAdapter(mContext, R.layout.item_car, datas);
        adapter = new LoadMoreWrapper(carAdapter);
        adapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(adapter);
        carJiesuan.setOnClickListener(this);
        carCbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                makeAllCheck(isChecked);

            }
        });
        getData();
    }

    private void getData() {
        HttpManager.getInstance().getCars(userId, page, new HttpSubscriber<List<ShopCarBean>>(new OnResultCallBack<List<ShopCarBean>>() {

            @Override
            public void onSuccess(List<ShopCarBean> shopCarBeen) {
                swipeToLoadLayout.setRefreshing(false);
                if (shopCarBeen != null && shopCarBeen.size() > 0) {
                    if (page == 1) {
                        datas.clear();
                        if (datas.size() >= MContants.PAGENUM) {
                            adapter.setLoadMoreView(R.layout.default_loading);
                        }

                    }
                    datas.addAll(shopCarBeen);
                } else {
                    adapter.setLoadMoreView(0);
                    if (page > 1) {
                        ToastUtil.show("没有更多数据了");
                    } else {
                        datas.clear();
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String errorMsg) {
                swipeToLoadLayout.setRefreshing(false);
                ToastUtil.show(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));
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
        specialUpdate();
    }

    private void specialUpdate() {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                carAdapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
             //   finish();
                onBackPressed();
                break;
            case R.id.car_jiesuan:
                if (carsId.size() <= 0) {
                    ToastUtil.show("未选择任何商品");
                    return;
                }
                updateCar(1);
                //createOrderCar();
                break;
        }

    }

    /**
     * 结算商品
     */
    private void createOrderCar() {
        Intent intent = new Intent(mContext, OrdersConfirmActivity.class);
        intent.putStringArrayListExtra("carIds", (ArrayList<String>) carsId);
        startActivity(intent);
    }

    private class CarAdapter extends CommonAdapter<ShopCarBean> {

        public CarAdapter(Context context, int layoutId, List<ShopCarBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(final ViewHolder holder, final ShopCarBean shopCarBean, final int position) {
            holder.setText(R.id.item_car_num, shopCarBean.number + "");
            holder.setText(R.id.item_car_price, "￥" + (shopCarBean.goods.price * shopCarBean.number));
            if(shopCarBean.number>=2){
                holder.getView(R.id.item_car_dec).setEnabled(true);
            }else{
                holder.getView(R.id.item_car_dec).setEnabled(false);
            }
            if (shopCarBean.goods != null)
                holder.setText(R.id.item_car_name, shopCarBean.goods.goodsName);
            if (shopCarBean.color != null) {
                holder.setText(R.id.item_car_color, "颜色: " + shopCarBean.color.name);
            }
            holder.setText(R.id.item_car_type, "型号: " + shopCarBean.size);
            holder.setText(R.id.item_car_price, "￥" + shopCarBean.total);

            ((CheckBox) holder.getView(R.id.item_car_checked)).setChecked(shopCarBean.isCheck);
            holder.setOnClickListener(R.id.item_car_add, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopCarBean.number++;
                    ((TextView) holder.getView(R.id.item_car_num)).setText(shopCarBean.number + "");
                    holder.setText(R.id.item_car_price, "￥" + (shopCarBean.goods.price * shopCarBean.number));
                    if (shopCarBean.number >=2) {
                        holder.getView(R.id.item_car_dec).setEnabled(true);
                    }
                    if (updateCars == null) {
                        updateCars = new ArrayList<ShopCarBean>();
                    }
                    updateCars.remove(shopCarBean);
                    updateCars.add(shopCarBean);
                }
            });
            holder.setOnClickListener(R.id.item_car_dec, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shopCarBean.number == 1) {
                        ToastUtil.show("当前数量为1,不能再减了");
                    } else {
                        shopCarBean.number--;
                        ((TextView) holder.getView(R.id.item_car_num)).setText(shopCarBean.number + "");
                        holder.setText(R.id.item_car_price, "￥" + (shopCarBean.goods.price * shopCarBean.number));
                    }
                    if (shopCarBean.number <= 1) {
                        holder.getView(R.id.item_car_dec).setEnabled(false);
                    }
                    if (updateCars == null) {
                        updateCars = new ArrayList<ShopCarBean>();
                    }
                    updateCars.remove(shopCarBean);
                    updateCars.add(shopCarBean);

                }
            });
            ((CheckBox) holder.getView(R.id.item_car_checked)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    shopCarBean.isCheck = isChecked;
                    if (isChecked) {
                        checkNum++;
                        carsId.add(shopCarBean.cartId);
                    } else {
                        checkNum--;
                        carsId.remove(shopCarBean.cartId);
                    }
                    if (checkNum == 0) {
                        carCbAll.setChecked(false);
                    }
                    if (checkNum == datas.size()) {
                        carCbAll.setChecked(true);
                    }
                }
            });
            holder.setOnClickListener(R.id.item_car_delete, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCar(datas.get(position));
                }
            });
        }


    }

    /**
     * 删除商品
     */
    private void deleteCar(final ShopCarBean car) {

        HttpManager.getInstance().deleteCars(car.cartId, new HttpSubscriber<String>(new OnResultCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                ToastUtil.show("删除成功");
                onRefresh();
                if (datas != null && datas.size() > 0) {
                    EventBus.getDefault().post(new EventMessage("deleteCar").setNum(datas.size() - 1));
                } else {
                    EventBus.getDefault().post(new EventMessage("deleteCar").setNum(0));
                }
                if (updateCars != null && updateCars.size() > 0 && updateCars.contains(car)) {
                    updateCars.remove(car);
                }

            }

            @Override
            public void onError(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));
    }

    @Override
    public void onBackPressed() {
        updateCar(0);


    }

    private void updateCar(final int i) {
        if(updateCars!=null && updateCars.size()>0){
            showLoading("正在更新购物车,请稍等");
            HttpManager.getInstance().updateCars(new Gson().toJson(updateCars), new HttpSubscriber<String>(new OnResultCallBack<String>() {

                @Override
                public void onSuccess(String s) {
                    hideLoading();
                    if(i==0){
                        finish();
                    }else{
                        updateCars.clear();
                        createOrderCar();
                    }

                }

                @Override
                public void onError(int code, String errorMsg) {
                    hideLoading();

                    ToastUtil.show(errorMsg);
                    if(i==0){
                        finish();
                    }else{
                        createOrderCar();
                    }
                }

                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }
            }));
        }else{
            if(i==0){
                finish();
            }else{
                createOrderCar();
            }
        }

    }
}
