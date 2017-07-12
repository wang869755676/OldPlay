package com.td.oldplay.ui.mine.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.bean.GoodBean;
import com.td.oldplay.bean.OrderBean;

import java.util.List;

/**
 * Created by my on 2017/7/10.
 */

public class OrderAdapter extends CommonAdapter<OrderBean> {

    private RecyclerView recyclerView;
    private OnItemActionListener actionListener;

    public void setActionListener(OnItemActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public OrderAdapter(Context context, int layoutId, List<OrderBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final OrderBean orderBean, final int position) {
        recyclerView = holder.getView(R.id.item_goods);
        if (orderBean.goodBeanList != null && orderBean.goodBeanList.size() > 0) {
            Log.e("===", orderBean.goodBeanList.size() + "==========");
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(new CommonAdapter<GoodBean>(mContext, R.layout.item_mine_order_inner, orderBean.goodBeanList) {
                @Override
                protected void convert(ViewHolder holder, GoodBean o, int position) {
                    holder.setText(R.id.item_order_good_color,"颜色"+position);
                }


            });
        }
        holder.setOnClickListener(R.id.item_order_comment, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入评价界面
                if (actionListener != null) {
                    actionListener.onAction(2, position, orderBean);
                }
            }
        });
        holder.setOnClickListener(R.id.item_order_logistics, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看物流
                if (actionListener != null) {
                    actionListener.onAction(1, position, orderBean);
                }
            }
        });
        holder.setOnClickListener(R.id.item_oreder_confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确认收货
                if (actionListener != null) {
                    actionListener.onAction(3, position, orderBean);
                }
            }
        });

    }

    public interface OnItemActionListener {
        abstract void onAction(int action, int postion, OrderBean item);

    }
}
