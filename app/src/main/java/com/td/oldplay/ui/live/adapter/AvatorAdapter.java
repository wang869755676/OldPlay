package com.td.oldplay.ui.live.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.utils.GlideUtils;

import java.util.List;

import cn.tee3.avd.User;

/**
 * Created by my on 2017/7/25.
 */

public class AvatorAdapter extends CommonAdapter<UserBean> {
    private int[] colors;

    public AvatorAdapter(Context context, int layoutId, List<UserBean> datas) {
        super(context, layoutId, datas);
        colors = context.getResources().getIntArray(R.array.color);
    }

    @Override
    protected void convert(ViewHolder holder, UserBean userBean, int position) {
        holder.setText(R.id.item_live_ava_name, userBean.nickName);
        holder.setTextColor(R.id.item_live_ava_name, colors[position % 4]);
        GlideUtils.setAvatorImage(mContext, userBean.avatar, (ImageView) holder.getView(R.id.item_live_ava));
    }
}
