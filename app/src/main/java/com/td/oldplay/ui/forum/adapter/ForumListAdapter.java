package com.td.oldplay.ui.forum.adapter;

import android.content.Context;

import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.ForumBean;

import java.util.List;

/**
 * Created by my on 2017/7/10.
 */

public class ForumListAdapter extends CommonAdapter<ForumBean> {
    public ForumListAdapter(Context context, int layoutId, List<ForumBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ForumBean forumBean, int position) {

    }
}
