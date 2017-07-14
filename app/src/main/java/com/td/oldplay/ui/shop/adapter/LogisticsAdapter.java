package com.td.oldplay.ui.shop.adapter;

import android.content.Context;

import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.LogisticsBean;

import java.util.List;

/**
 * Created by my on 2017/7/14.
 */

public class LogisticsAdapter extends CommonAdapter<LogisticsBean> {
    public LogisticsAdapter(Context context, int layoutId, List<LogisticsBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, LogisticsBean logisticsBean, int position) {

    }
}
