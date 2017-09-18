package com.td.oldplay.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.bean.ShopDetail;

/**
 * 可以适应Scrollview中嵌套listview模式
 *
 * @author 佘岗
 *         Created by Administrator on 2015/9/26.
 */
public class CustomListView extends ListView {
    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
