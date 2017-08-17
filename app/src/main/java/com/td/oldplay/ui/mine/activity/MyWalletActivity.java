package com.td.oldplay.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.RechargeInfo;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.bean.WalletBean;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.mine.adapter.WalletAdapter;
import com.td.oldplay.ui.window.ListPopupWindow;
import com.td.oldplay.utils.DeviceUtil;
import com.td.oldplay.utils.TimeMangerUtil;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

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
    @BindView(R.id.wallet_chongzhi)
    TextView walletChongzhi;
    @BindView(R.id.wallet_tixian_action)
    TextView walletTixianAction;

    private WalletAdapter walletAdapter;
    private LoadMoreWrapper adapter;
    private int page = 1;
    private WalletBean bean;
    private List<RechargeInfo> datas = new ArrayList<>();

    private List<String> monthDatas;
    private int currentYear;
    private int currentMonth;
    private ListPopupWindow<String> popupWindow;
    private Adapter monthAdapter;
    private HashMap<String, Object> params = new HashMap<>();

    private Intent intent;
    private int RECHARGECODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        params.put("userId", userId);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    private void initView() {
        if (userBean.uType == 1) {
            rbLive.setVisibility(View.VISIBLE);
        } else {
            rbLive.setVisibility(View.GONE);
        }

        title.setTitle("我的钱包");
        title.setOnLeftListener(this);
        walletChongzhi.setOnClickListener(this);
        walletTixianAction.setOnClickListener(this);
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
                        params.put("type", 0);//类型(0:充值1:提现2:消费3:收入)
                        break;
                    case R.id.rb_live:
                        params.put("type", 3);
                        break;
                    case R.id.rb_tixian:
                        params.put("type", 1);
                        break;
                    case R.id.rb_xiaofei:
                        params.put("type", 2);
                        break;
                }
                getData();
            }
        });
        walletAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext, ConsumptionActivity.class);
                intent.putExtra("id", datas.get(position).id);
                startActivity(intent);
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
        params.put("time", monthDatas.get(0));
        monthAdapter = new Adapter(mContext, R.layout.item_month, monthDatas);
        monthAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                walletMonth.setText(monthDatas.get(position));
                popupWindow.dismiss();
                params.put("time", monthDatas.get(position));
                datas.clear();
                adapter.notifyDataSetChanged();
                getData();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        getData();
    }

    private void getData() {
        params.put("page", page);
        HttpManager.getInstance().getMyWallets(params, new HttpSubscriber<WalletBean>(new OnResultCallBack<WalletBean>() {

            @Override
            public void onSuccess(WalletBean walletBeen) {
                swipeLayout.setRefreshing(false);
                bean = walletBeen;
                setData();
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

    private void setData() {
        if (bean != null) {
            walletTixian.setText(bean.money + "");
            if (bean.rechargeSetsList != null) {
                datas.clear();
                datas.addAll(bean.rechargeSetsList);
                adapter.notifyDataSetChanged();
            }

        }
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
            case R.id.wallet_month:
                if (popupWindow == null) {
                    popupWindow = new ListPopupWindow<String>(mContext, monthAdapter, ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.dip2px(mContext, 100));
                }
                popupWindow.showPopupWindow(v);
                break;
            case R.id.wallet_tixian_action:
                if (bean != null && bean.money > 0) {
                    intent = new Intent(mContext, GetCashActivity.class);
                    intent.putExtra("money", bean.money);
                    startActivity(intent);
                } else {
                    ToastUtil.show("账户余额为0，无法提现");
                }

                break;
            case R.id.wallet_chongzhi:
                startActivity(new Intent(mContext, RechargeActivity.class));
                break;
        }

    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(EventMessage message) {
        Log.e("====",message.action);
        if ("changeAcount".equals(message.action)) {
            if (bean != null)
                bean.money += message.total;
            userBean.money += message.total;
            spUilts.setUser(userBean);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECHARGECODE && requestCode == RESULT_OK && data != null) {
            if (bean != null)
                bean.money += data.getFloatExtra("total", 0);
            userBean.money += data.getFloatExtra("total", 0);
            spUilts.setUser(userBean);
        }
    }

    public static class Adapter extends CommonAdapter<String> {

        public Adapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, String s, int position) {
            holder.setText(R.id.item_tv, s);
        }
    }
}
