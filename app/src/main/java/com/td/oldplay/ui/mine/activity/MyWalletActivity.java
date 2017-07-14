package com.td.oldplay.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.WalletBean;
import com.td.oldplay.ui.mine.adapter.WalletAdapter;
import com.td.oldplay.ui.window.ListPopupWindow;
import com.td.oldplay.utils.DeviceUtil;
import com.td.oldplay.utils.TimeMangerUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWalletActivity extends BaseFragmentActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        LoadMoreWrapper.OnLoadMoreListener,
        View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.wallet_tixian)
    TextView walletTixian;
    @BindView(R.id.rb_chongzhi)
    RadioButton rbChongzhi;
    @BindView(R.id.rb_tixian)
    RadioButton rbTixian;
    @BindView(R.id.rb_xiaofei)
    RadioButton rbXiaofei;
    @BindView(R.id.rb_live)
    RadioButton rbLive;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    @BindView(R.id.wallet_month)
    TextView walletMonth;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    private WalletAdapter walletAdapter;
    private LoadMoreWrapper adapter;
    private int page = 1;
    private List<WalletBean> datas = new ArrayList<>();

    private List<String> monthDatas;
    private int currentYear;
    private int currentMonth;
    private ListPopupWindow<String> popupWindow;
    private Adapter monthAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        datas.add(new WalletBean());
        datas.add(new WalletBean());
        title.setTitle("我的钱包");
        title.setOnLeftListener(this);
        swipeLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        walletAdapter = new WalletAdapter(mContext, R.layout.item_money_detail, datas);
        adapter = new LoadMoreWrapper(walletAdapter);
        swipeTarget.setAdapter(adapter);
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chongzhi:
                        break;
                    case R.id.rb_live:
                        break;
                    case R.id.rb_tixian:
                        break;
                    case R.id.rb_xiaofei:
                        break;
                }
            }
        });
        walletAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(mContext,ConsumptionActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        initMonth();


    }

    private void initMonth() {
        walletMonth.setOnClickListener(this);
        currentYear = TimeMangerUtil.getCurrentYear();
        currentMonth = TimeMangerUtil.getCurrentMonth();
        StringBuilder builder = new StringBuilder();
        monthDatas = new ArrayList<>();
        for (int i = currentMonth; i > 0; i--) {
            monthDatas.add(String.format("%s年%s月", currentYear, i));
        }
        walletMonth.setText(monthDatas.get(0));
        monthAdapter = new Adapter(mContext, R.layout.item_month, monthDatas);
        monthAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                walletMonth.setText(monthDatas.get(position));
                // 请求数据
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    @Override
    public void onRefresh() {
        page = 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.wallet_month:
                if (popupWindow == null) {
                    popupWindow = new ListPopupWindow<String>(mContext, monthAdapter, ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.dip2px(mContext, 100));
                }
                popupWindow.showPopupWindow(v);
                break;
        }

    }

    @Override
    public void onLoadMoreRequested() {
        page++;
    }

    private static class Adapter extends CommonAdapter<String> {

        public Adapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, String s, int position) {
            holder.setText(R.id.item_tv, s);
        }
    }
}
