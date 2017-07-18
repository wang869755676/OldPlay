package com.td.oldplay.ui.course.adapter;

import android.content.Context;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CourseBean;

import java.util.List;

/**
 * Created by my on 2017/7/5.
 */

public class CourserAdapter extends CommonAdapter<CourseBean> {
    public CourserAdapter(Context context, int layoutId, List<CourseBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CourseBean courseBean, int position) {
           if(courseBean!=null){
               holder.setText(R.id.item_course_name,courseBean.name);
           }
    }
}
