package com.td.oldplay.ui.course.adapter;

import android.content.Context;

import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.ShopBean;

import java.util.List;

/**
 * Created by my on 2017/7/5.
 */

public class ShopAdapter extends CommonAdapter<ShopBean> {
    public ShopAdapter(Context context, int layoutId, List<ShopBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ShopBean courseBean, int position) {

    }
}
