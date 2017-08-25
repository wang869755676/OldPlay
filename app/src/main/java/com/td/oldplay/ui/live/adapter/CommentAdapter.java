package com.td.oldplay.ui.live.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

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
    private SpannableString spannableString;
    private ForegroundColorSpan colorSpan;
    private ForegroundColorSpan rewordColorSpan;

    public CommentAdapter(Context context, int layoutId, List<CommentBean> datas) {
        super(context, layoutId, datas);
        colors = context.getResources().getIntArray(R.array.color);
    }

    @Override
    protected void convert(ViewHolder holder, CommentBean commentBean, int position) {
       /* holder.setText(R.id.item_live_cm_name, commentBean.user.nickName);
        holder.setTextColor(R.id.item_live_cm_name, colors[position % 4]);
        holder.setText(R.id.item_live_cm_content, commentBean.content);*/
        spannableString = new SpannableString(commentBean.user.nickName + "  " + commentBean.content);
        colorSpan = new ForegroundColorSpan(colors[position % 4]);
        spannableString.setSpan(colorSpan, 0, commentBean.user.nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        if (commentBean.type == 1) {
            rewordColorSpan=new ForegroundColorSpan(mContext.getResources().getColor(R.color.tv_red));
            spannableString.setSpan(rewordColorSpan, commentBean.user.nickName.length(), spannableString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.setVisible(R.id.item_live_cm_reword,true);
        }else{
            holder.setVisible(R.id.item_live_cm_reword,false);
        }
        holder.setText(R.id.item_live_cm_name, spannableString);
    }
}
