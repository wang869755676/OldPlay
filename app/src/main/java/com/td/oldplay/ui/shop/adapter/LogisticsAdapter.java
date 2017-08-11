package com.td.oldplay.ui.shop.adapter;

import android.content.Context;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.LogisticsBean;

import java.util.List;

/**
 * Created by my on 2017/7/14.
 */

public class LogisticsAdapter extends CommonAdapter<LogisticsBean.DataBean> {
    public LogisticsAdapter(Context context, int layoutId, List<LogisticsBean.DataBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, LogisticsBean.DataBean logisticsBean, int position) {
        if(position!=0){
            holder.setImageResource(R.mipmap.icon_log,R.id.item_logi_ic);
        }
        holder.setText(R.id.item_log_time, logisticsBean.time);
        holder.setText(R.id.item_log_des, logisticsBean.context);
    }
}
