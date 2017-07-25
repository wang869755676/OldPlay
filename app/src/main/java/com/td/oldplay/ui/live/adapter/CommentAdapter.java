package com.td.oldplay.ui.live.adapter;

import android.content.Context;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CommentBean;

import java.util.List;

/**
 * Created by my on 2017/7/25.
 */

public class CommentAdapter extends CommonAdapter<CommentBean> {
    private int[] colors;

    public CommentAdapter(Context context, int layoutId, List<CommentBean> datas) {
        super(context, layoutId, datas);
        colors = context.getResources().getIntArray(R.array.color);
    }

    @Override
    protected void convert(ViewHolder holder, CommentBean commentBean, int position) {
        holder.setText(R.id.item_live_cm_name, commentBean.user.nickName);
        holder.setTextColor(R.id.item_live_cm_name, colors[position % 4]);
        holder.setText(R.id.item_live_cm_content, commentBean.content);
    }
}
