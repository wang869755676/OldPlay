package com.td.oldplay.ui.forum.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.utils.GlideUtils;
import com.tencent.mm.opensdk.utils.Log;

import java.util.List;

/**
 * Created by my on 2017/7/20.
 */

public class PicAdapter extends CommonAdapter<String> {
    public PicAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String strings, int position) {
        Log.e("===",strings);
        GlideUtils.setImage(mContext, strings, (ImageView) holder.getView(R.id.item_shop_de_pic));
    }
}
