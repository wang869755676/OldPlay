package com.td.oldplay.ui.course.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.utils.GlideUtils;

import java.util.List;

/**
 * Created by my on 2017/7/5.
 */

public class CourserListAdapter extends CommonAdapter<CourseTypeBean> {
    public CourserListAdapter(Context context, int layoutId, List<CourseTypeBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CourseTypeBean courseBean, int position) {
        if (courseBean != null) {
            GlideUtils.setImage(mContext, courseBean.picUrl, (ImageView) holder.getView(R.id.circularImage));
            holder.setText(R.id.item_name, courseBean.name);
            holder.setText(R.id.item_name, courseBean.name);
            holder.setText(R.id.item_introduce, "    "+courseBean.describes);
        }
    }
}
