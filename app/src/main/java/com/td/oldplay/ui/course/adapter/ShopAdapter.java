package com.td.oldplay.ui.course.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.ShopBean;
import com.td.oldplay.utils.GlideUtils;

import java.util.List;

/**
 * Created by my on 2017/7/5.
 */

public class ShopAdapter extends CommonAdapter<ShopBean> {
    public ShopAdapter(Context context, int layoutId, List<ShopBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ShopBean item, int position) {
        if (item != null) {
            GlideUtils.setImage(mContext, item.picUrl, (ImageView) holder.getView(R.id.item_shop_iv));
            holder.setText(R.id.item_shop_sell, item.sellNum + "");
            holder.setText(R.id.item_shop_price, item.price + "");
            holder.setText(R.id.item_shop_name, item.goodsName + "");
            holder.setText(R.id.item_shop_score, "购买可得" + item.score + "积分");
        }

    }
}
