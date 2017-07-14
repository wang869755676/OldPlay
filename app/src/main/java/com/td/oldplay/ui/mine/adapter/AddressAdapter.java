package com.td.oldplay.ui.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.AddressBean;

import java.util.List;

/**
 * Created by my on 2017/7/10.
 */

public class AddressAdapter extends CommonAdapter<AddressBean> {
    private EditText itemName;
    private EditText itemPhone;
    private EditText itemAddress;

    private OnItemActionListener actionListener;

    public void setActionListener(OnItemActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public AddressAdapter(Context context, int layoutId, List<AddressBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final AddressBean addressBean, final int position) {

        holder.setOnClickListener(R.id.addr_default, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.onAction("default", position,addressBean);
                }
            }
        });
        holder.setOnClickListener(R.id.addr_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.onAction("update", position,addressBean);
                }

            }
        });
        holder.setOnClickListener(R.id.addr_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.onAction("default", position,addressBean);
                }
            }
        });

    }


    public static interface OnItemActionListener {
        abstract void onAction(String action, int postion, AddressBean item);

    }
}
