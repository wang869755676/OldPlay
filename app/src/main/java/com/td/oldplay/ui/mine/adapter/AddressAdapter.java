package com.td.oldplay.ui.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.utils.AppUtils;
import com.td.oldplay.utils.ToastUtil;

import java.util.List;

/**
 * Created by my on 2017/7/10.
 */

public class AddressAdapter extends CommonAdapter<AddressBean> {
    private EditText itemName;
    private EditText itemPhone;
    private EditText itemAddress;

    private OnItemActionListener actionListener;
    private String address;
    private String name;
    private String phone;

    public void setActionListener(OnItemActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public AddressAdapter(Context context, int layoutId, List<AddressBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(final ViewHolder holder, final AddressBean addressBean, final int position) {
        holder.setText(R.id.item_addrs_name, "" + addressBean.consignee);
        holder.setText(R.id.item_addrs_phone, "" + addressBean.mobile);
        holder.setText(R.id.item_addrs_addr, "收货地址:" + addressBean.address);
        if (addressBean.isDefault == 1) {
            holder.setChecked(R.id.addr_default, true);
        } else {
            holder.setChecked(R.id.addr_default, false);
        }
        holder.setOnClickListener(R.id.addr_default, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.onAction("default", position, addressBean);
                }
            }
        });
        // ((EditText) holder.getView(R.id.item_addrs_phone)).addTextChangedListener(null);
        holder.setOnClickListener(R.id.addr_edit_done, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getView(R.id.item_addrs_phone).setEnabled(false);
                holder.getView(R.id.item_addrs_addr).setEnabled(false);
                holder.getView(R.id.item_addrs_name).setEnabled(false);


                address = ((EditText) holder.getView(R.id.item_addrs_addr)).getText().toString().replace("收货地址:", "");
                name = ((EditText) holder.getView(R.id.item_addrs_name)).getText().toString();
                phone = ((EditText) holder.getView(R.id.item_addrs_phone)).getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.show("请输入姓名");
                    return;
                }
                addressBean.consignee = name;
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.show("请输入手机号");
                    return;
                } else if (!AppUtils.checkPhone(phone)) {
                    ToastUtil.show("请输入正确的手机号");
                    return;
                }
                addressBean.mobile = phone;
                if (TextUtils.isEmpty(address)) {
                    ToastUtil.show("请输入地址");
                    return;
                }
                addressBean.address = address;

                holder.setVisible(R.id.addr_edit, true);
                holder.setVisible(R.id.addr_edit_done, false);
                if (actionListener != null) {
                    actionListener.onAction("update", position, addressBean);
                }
            }
        });
        holder.setOnClickListener(R.id.addr_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getView(R.id.item_addrs_phone).setEnabled(true);
                holder.getView(R.id.item_addrs_addr).setEnabled(true);
                holder.getView(R.id.item_addrs_name).setEnabled(true);
                holder.setVisible(R.id.addr_edit, false);
                holder.setVisible(R.id.addr_edit_done, true);
                (holder.getView(R.id.item_addrs_addr)).setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL
                                && event.getAction() == KeyEvent.ACTION_DOWN
                                && ((EditText) holder.getView(R.id.item_addrs_addr)).getText().length() == 5) {

                            return true;
                        }
                        return false;

                    }
                });
       /*         ((EditText) holder.getView(R.id.item_addrs_addr)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.e("===",s.length()+" ----------------");
                        if(TextUtils.isEmpty(s)){
                            if (s.length() <= 6) {
                                ((EditText) holder.getView(R.id.item_addrs_addr)).setText("收货地址:");
                            }
                        }
                    }
                });*/
            }
        });
                holder.setOnClickListener(R.id.addr_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (actionListener != null) {


                            actionListener.onAction("delete", position, addressBean);
                        }
                    }
                });

            }


            public interface OnItemActionListener {
                void onAction(String action, int postion, AddressBean item);

            }
        }
