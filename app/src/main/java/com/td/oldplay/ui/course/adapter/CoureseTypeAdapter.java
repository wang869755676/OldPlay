package com.td.oldplay.ui.course.adapter;

import android.content.Context;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CourseTypeBean;

import java.util.List;

/**
 * Created by my on 2017/7/11.
 */

public class CoureseTypeAdapter extends CommonAdapter<CourseTypeBean> {
    private int[] colors = {R.drawable.cir_one,
            R.drawable.cir_two,
            R.drawable.cir_three,
            R.drawable.cir_four,
            R.drawable.cir_five,
            R.drawable.cir_six,
            R.drawable.cir_seven,
            R.drawable.cir_eigent};

    public CoureseTypeAdapter(Context context, int layoutId, List<CourseTypeBean> datas) {
        super(context, layoutId, datas);

    }

    @Override
    protected void convert(ViewHolder holder, CourseTypeBean courseTypeBean, int position) {
        if (courseTypeBean != null) {
            // GlideUtils.setImage(mContext,courseTypeBean.picUrl,holder.getView(R.id.item_cou));
            holder.setBackgroundRes(R.id.item_type_color, colors[position]);
            holder.setText(R.id.item_type_name, courseTypeBean.name);
        }

    }
}
