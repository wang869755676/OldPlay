package com.td.oldplay.ui.mine.adapter;


import android.content.Context;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.RechargeInfo;
import com.td.oldplay.bean.WalletBean;
import com.td.oldplay.ui.course.adapter.CommentAdapter;

import java.util.List;


/**
 * Created by my on 2017/7/13.
 */

public class WalletAdapter extends CommonAdapter<RechargeInfo> {

    public WalletAdapter(Context context, int layoutId, List<RechargeInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, RechargeInfo walletBean, int position) {
        holder.setText(R.id.time,walletBean.formatTime);

        holder.setText(R.id.price,walletBean.money+"");
        switch (walletBean.type){
            case 0:
                holder.setText(R.id.money_type,"充值");
                break;
            case 1:
                holder.setText(R.id.money_type,"提现");
                break;
            case 2:
                holder.setText(R.id.money_type,"消费");
                break;
            case 3:
                holder.setText(R.id.money_type,"收入");
                break;
        }

    }
}
