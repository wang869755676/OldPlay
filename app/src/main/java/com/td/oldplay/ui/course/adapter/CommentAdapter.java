package com.td.oldplay.ui.course.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.utils.GlideUtils;

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

        if (commentBean != null) {
            GlideUtils.setAvatorImage(mContext, commentBean.user.avatar, (ImageView) holder.getView(R.id.item_commen_iv));
            holder.setText(R.id.item_comment, commentBean.content);
            holder.setText(R.id.item_name, commentBean.user.nickName);
            holder.setText(R.id.item_time, commentBean.time);

        }
    }
}
