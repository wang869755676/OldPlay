package com.td.oldplay.ui.course.adapter;

import android.content.Context;

import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.TeacherBean;

import java.util.List;

/**
 * Created by my on 2017/7/6.
 */

public class TeacherAdapter extends CommonAdapter<TeacherBean> {
    public TeacherAdapter(Context context, int layoutId, List<TeacherBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TeacherBean teacherBean, int position) {

    }
}
