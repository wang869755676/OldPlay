package com.td.oldplay.ui.shop.adapter;

import android.content.Context;

import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.GoodBean;

import java.util.List;

/**
 * Created by my on 2017/7/13.
 */

public class GoodAdapter extends CommonAdapter<GoodBean> {
    public GoodAdapter(Context context, int layoutId, List<GoodBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, GoodBean goodBean, int position) {

    }
}
