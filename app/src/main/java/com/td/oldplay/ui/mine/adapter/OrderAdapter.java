package com.td.oldplay.ui.mine.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.td.oldplay.ui.shop.activity.OrderDetailActivity;
import com.td.oldplay.ui.shop.adapter.GoodAdapter;
import com.td.oldplay.utils.ToastUtil;

import java.util.List;

/**
 * Created by my on 2017/7/10.
 */

public class OrderAdapter extends CommonAdapter<OrderBean> {

    private RecyclerView recyclerView;
    private OnItemActionListener actionListener;
    private GoodAdapter goodAdapter;

    public void setActionListener(OnItemActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public OrderAdapter(Context context, int layoutId, List<OrderBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final OrderBean orderBean, final int position) {
        if (orderBean.status == 5) {
            holder.setVisible(R.id.item_oreder_confirm, false);
            holder.setVisible(R.id.item_order_comment, true);
        } else {
            holder.setVisible(R.id.item_oreder_confirm, true);
            holder.setVisible(R.id.item_order_comment, false);
        }
        holder.setText(R.id.order_time, orderBean.formatTime);
        holder.setText(R.id.order_num, orderBean.orderNum);
        if (orderBean.isApplyScore == 1) {
            holder.setVisible(R.id.item_order_socre, true);
            holder.setText(R.id.item_order_socre, "积分抵扣");
        } else {
            holder.setVisible(R.id.item_order_socre, false);
        }
        holder.setText(R.id.item_order_smoney, "合计 " + orderBean.amount_paid);
        recyclerView = holder.getView(R.id.item_goods);
        if (orderBean.orderDetails != null && orderBean.orderDetails.size() > 0) {
            if (orderBean.orderDetails.get(0).groupBuy != null) {
                if (orderBean.orderDetails.get(0).groupBuy.status == 1) {
                    holder.setText(R.id.item_order_groupstate, "团购中");
                    holder.setVisible(R.id.item_order_groupstate, true);
                    holder.setVisible(R.id.item_order_done_action, false);
                } else if (orderBean.orderDetails.get(0).groupBuy.status == 2) {
                    holder.setText(R.id.item_order_groupstate, "团购成功");
                    holder.setVisible(R.id.item_order_groupstate, true);
                    holder.setVisible(R.id.item_order_done_action, true);
                } else if (orderBean.orderDetails.get(0).groupBuy.status == 2) {
                    holder.setText(R.id.item_order_groupstate, "团购取消");
                    holder.setVisible(R.id.item_order_groupstate, true);
                    holder.setVisible(R.id.item_order_done_action, false);
                }

            } else {
                holder.setVisible(R.id.item_order_groupstate, false);
                holder.setVisible(R.id.item_order_done_action, true);
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            goodAdapter = new GoodAdapter(mContext, R.layout.item_mine_order_inner, orderBean.orderDetails,1);
            recyclerView.setAdapter(goodAdapter);
            goodAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("id", orderBean.orderId);
                    mContext.startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
          /*  recyclerView.setAdapter(new CommonAdapter<GoodBean>(mContext, R.layout.item_mine_order_inner, orderBean.goodBeanList) {
                @Override
                protected void convert(ViewHolder holder, GoodBean o, int position) {
                    holder.setText(R.id.item_order_goods_name,o.goodsName);
                    holder.setText(R.id.item_order_goods_num,"x "+o.number+"");
                    holder.setText(R.id.item_order_good_typr,"型号: "+o.size);
                    holder.setText(R.id.item_order_good_color,"颜色: "+o.color);
                    holder.setText(R.id.item_order_good_price,"￥ "+o.price);
                }


            });*/
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
                if (orderBean.status == 4) {
                    if (actionListener != null) {
                        actionListener.onAction(3, position, orderBean);
                    }
                } else {
                    ToastUtil.show("商家还未发货");
                }

            }
        });

    }

    public interface OnItemActionListener {
        abstract void onAction(int action, int postion, OrderBean item);

    }
}
