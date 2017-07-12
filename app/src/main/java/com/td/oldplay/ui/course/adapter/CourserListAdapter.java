package com.td.oldplay.ui.course.adapter;

import android.content.Context;

import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CourseBean;

import java.util.List;

/**
 * Created by my on 2017/7/5.
 */

public class CourserListAdapter extends CommonAdapter<CourseBean> {
    public CourserListAdapter(Context context, int layoutId, List<CourseBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CourseBean courseBean, int position) {

    }
}
