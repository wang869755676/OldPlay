package com.td.oldplay.ui.course.adapter;

import android.content.Context;

import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CommentBean;

import java.util.List;

/**
 * Created by my on 2017/7/5.
 */

public class CommentAdapter extends CommonAdapter<CommentBean> {
    public CommentAdapter(Context context, int layoutId, List<CommentBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CommentBean commentBean, int position) {

    }
}
