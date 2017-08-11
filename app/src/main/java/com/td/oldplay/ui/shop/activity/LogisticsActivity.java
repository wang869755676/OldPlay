package com.td.oldplay.ui.shop.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.bean.LogisticsBean;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.shop.adapter.LogisticsAdapter;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class LogisticsActivity extends BaseFragmentActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    private LogisticsAdapter adapter;
    private List<LogisticsBean.DataBean> datas = new ArrayList<>();

    private String logisticnum;
    private String logistic;
    private  int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        logistic = getIntent().getStringExtra("logistic");
        logisticnum = getIntent().getStringExtra("logisticnum");
        status=getIntent().getIntExtra("status",0);
        title.setTitle("物流");
        title.setOnLeftListener(this);
        swipeLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));

       switch (status){
           case 3:
               datas.add(new LogisticsBean.DataBean("商品已出库"));
               break;
           case 2:
               datas.add(new LogisticsBean.DataBean("仓库配货中"));
               break;

           default:
               if(!TextUtils.isEmpty(logistic) && !TextUtils.isEmpty(logisticnum)){
                   getData();
               }
               datas.add(new LogisticsBean.DataBean("暂无物流信息"));
               break;
       }
        adapter = new LogisticsAdapter(mContext, R.layout.item_logidtics, datas);
        swipeTarget.setAdapter(adapter);

    }

    private void getData() {
        HttpManager.getInstance().getLogistics(logistic, logisticnum, new HttpSubscriber<LogisticsBean>(new OnResultCallBack<LogisticsBean>() {


            @Override
            public void onSuccess(LogisticsBean logisticsBean) {
                swipeLayout.setRefreshing(false);
                if (logisticsBean != null) {
                    if (logisticsBean.data != null && logisticsBean.data.size() > 0) {
                        datas.clear();
                        datas.addAll(logisticsBean.data);
                    } else {
                        datas.add(new LogisticsBean.DataBean("暂无物流信息"));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int code, String errorMsg) {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }
        }));
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
        }

    }
}
