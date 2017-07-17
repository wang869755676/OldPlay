package com.td.oldplay.ui.course.adapter;

import android.content.Context;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.utils.GlideUtils;

import java.util.List;

/**
 * Created by my on 2017/7/11.
 */

public class CoureseTypeAdapter extends CommonAdapter<CourseTypeBean> {
    private int[] colors;

    public CoureseTypeAdapter(Context context, int layoutId, List<CourseTypeBean> datas) {
        super(context, layoutId, datas);
        colors=context.getResources().getIntArray(R.array.color);
    }

    @Override
    protected void convert(ViewHolder holder, CourseTypeBean courseTypeBean, int position) {
        if(courseTypeBean!=null){
           // GlideUtils.setImage(mContext,courseTypeBean.picUrl,holder.getView(R.id.item_cou));
            holder.setBackgroundColor(R.id.item_type_color,colors[position]);
            holder.setText(R.id.item_type_name,courseTypeBean.name);
        }

    }
}
