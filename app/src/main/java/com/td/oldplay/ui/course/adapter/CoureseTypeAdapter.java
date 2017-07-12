package com.td.oldplay.ui.course.adapter;

import android.content.Context;

import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CourseTypeBean;

import java.util.List;

/**
 * Created by my on 2017/7/11.
 */

public class CoureseTypeAdapter extends CommonAdapter<CourseTypeBean> {
    public CoureseTypeAdapter(Context context, int layoutId, List<CourseTypeBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CourseTypeBean courseTypeBean, int position) {

    }
}
