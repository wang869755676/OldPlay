package com.td.oldplay.ui.forum.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.ForumBean;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.utils.TimeMangerUtil;

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
        holder.setText(R.id.item_title, forumBean.title);
        holder.setText(R.id.item_content, "  "+forumBean.content);

        holder.setText(R.id.item_comment, forumBean.replyCount + "评论");
        if (!TextUtils.isEmpty(forumBean.formatTime)) {
            holder.setText(R.id.item_time, TimeMangerUtil.friendsCirclePastDate(forumBean.formatTime));
        }
        if (forumBean.user != null) {
            GlideUtils.setAvatorImage(mContext,forumBean.user.avatar, (ImageView) holder.getView(R.id.item_forum_iv));
            holder.setText(R.id.item_name, forumBean.user.nickName);
        }
        if (forumBean.label == 1) {
            holder.setVisible(R.id.item_sence, true);
        } else {
            holder.setVisible(R.id.item_sence, false);
        }


    }
}
