package com.td.oldplay.ui.shop.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.GoodBean;

import java.util.List;

/**
 * Created by my on 2017/7/24.
 */

public class ShopCommentAdapter extends CommonAdapter<GoodBean> {

    public ShopCommentAdapter(Context context, int layoutId, List<GoodBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final GoodBean goodBean, int position) {
         holder.setText(R.id.item_shop_name,goodBean.goodsName);
        ((EditText) holder.getView(R.id.score_ed)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null){
                    goodBean.comment=s.toString();
                }

            }
        });
    }
}
