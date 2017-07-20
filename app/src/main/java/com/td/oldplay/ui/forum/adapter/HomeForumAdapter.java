package com.td.oldplay.ui.forum.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.ForumBean;
import com.td.oldplay.bean.ForumType;
import com.td.oldplay.utils.GlideUtils;

import java.util.List;

/**
 * Created by my on 2017/7/10.
 */

public class HomeForumAdapter extends CommonAdapter<ForumType> {
    public HomeForumAdapter(Context context, int layoutId, List<ForumType> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ForumType forumBean, int position) {
        if (forumBean != null) {
            holder.setText(R.id.item_forum_title, forumBean.name);
            GlideUtils.setImage(mContext, forumBean.picUrl, (ImageView) holder.getView(R.id.home_forum_iv));
        }

    }
}
