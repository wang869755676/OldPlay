package com.td.oldplay.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.mine.adapter.AddressAdapter;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

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


    private List<AddressBean> datas = new ArrayList<>();
    private AddressAdapter addressAdapter;
    private LoadMoreWrapper adapter;
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(EventMessage eventMessage) {
        if (eventMessage.action.equals("loadAddress")) {
            page = 1;
            getData();
        }
    }

    private void initView() {
        title.setTitle("地址变更");
        title.setOnLeftListener(this);
        title.setOnRightListener(this);
        title.setRightImageResource(R.mipmap.icon_adr_publish);
        swipeLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        addressAdapter = new AddressAdapter(mContext, R.layout.item_address, datas);
        addressAdapter.setActionListener(this);
        adapter = new LoadMoreWrapper(addressAdapter);
        adapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(adapter);
        addressAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                setResult(RESULT_OK, new Intent().putExtra("model", datas.get(position)));
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        getData();
    }

    private void getData() {
        showLoading();
        HttpManager.getInstance().getAddresse(page, userId, new HttpSubscriber<List<AddressBean>>(new OnResultCallBack<List<AddressBean>>() {
            @Override
            public void onSuccess(List<AddressBean> addressBeen) {
                hideLoading();
                swipeLayout.setRefreshing(false);

                if (addressBeen != null && addressBeen.size() > 0) {
                    if (page == 1) {
                        datas.clear();
                        if (addressBeen.size() >= MContants.PAGENUM) {
                            adapter.setLoadMoreView(R.layout.default_loading);
                        }
                    }
                    datas.addAll(addressBeen);
                } else {
                    adapter.setLoadMoreView(0);
                    if (page > 1) {
                        ToastUtil.show("没有更多数据了");
                    }

                }
                Collections.reverse(datas);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(int code, String errorMsg) {
                hideLoading();
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
        page = 1;
        getData();
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
        getData();
    }

    @Override
    public void onAction(String action, final int postion, AddressBean item) {
        if (action.equals("default")) {
            HttpManager.getInstance().setDefaultAddress(item.addressId, userId, new HttpSubscriber<String>(new OnResultCallBack<String>() {
                @Override
                public void onSuccess(String o) {
                    for (AddressBean bean :
                            datas) {
                        bean.isDefault = 0;
                    }
                    datas.get(postion).isDefault = 1;
                    adapter.notifyDataSetChanged();
                    ToastUtil.show("设置成功");
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
        } else if (action.equals("update")) {
            Intent intent = new Intent(mContext, AddAddressActivity.class);
            intent.putExtra("mode", item);
            startActivity(intent);
        } else if (action.equals("delete")) {
            HttpManager.getInstance().deleteAddress(item.addressId, new HttpSubscriber<String>(new OnResultCallBack<String>() {

                @Override
                public void onSuccess(String s) {
                    datas.remove(postion);
                    adapter.notifyDataSetChanged();
                    ToastUtil.show("删除成功");
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
    }


}
