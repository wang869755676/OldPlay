package com.td.oldplay.ui.window;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.ColorListBean;
import com.td.oldplay.bean.ShopBean;
import com.td.oldplay.bean.ShopDetail;
import com.td.oldplay.ui.shop.activity.OrderConfirmActivity;
import com.td.oldplay.utils.DeviceUtil;
import com.td.oldplay.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by Administrator on 2015/11/2.
 */
public class ShopCarDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private View.OnClickListener listener;
    private ShopDetail shopBean;

    private TextView modeTv;
    private TextView colorTv;
    private TextView moneyTv;
    private TextView okTv;
    private TextView decTv;
    private TextView addTv;
    private TextView numTv;

    private ListPopupWindow modelWindow;
    private Adapter modeAdapter;

    private ListPopupWindow colorWindow;
    private ColorAdapter colorAdpater;
    private int num = 1;
    private int type;
    private float totalMoney;
    private String ColorId = "0";
    private String size = "默认";
    private float money;
    EventMessage message;
    public ShopCarDialog(@NonNull Context context, View.OnClickListener listener, ShopDetail shopBean, int Type) {
        super(context, R.style.AlertDialogStyle); //dialog的样式
        this.context = context;
        this.listener = listener;
        this.shopBean = shopBean;
        this.type = Type;
        initalize();
    }

    //初始化View
    private void initalize() {
        setCanceledOnTouchOutside(true);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_shopcar, null);
        setContentView(view);
        initWindow();

        modeTv = (TextView) view.findViewById(R.id.dialog_type);
        colorTv = (TextView) view.findViewById(R.id.dialog_color);
        decTv = (TextView) view.findViewById(R.id.dialog_car_dec);
        addTv = (TextView) view.findViewById(R.id.dialog_car_add);
        numTv = (TextView) view.findViewById(R.id.dialog_car_num);
        moneyTv = (TextView) view.findViewById(R.id.dialog_money);
        okTv = (TextView) view.findViewById(R.id.dialog_ok);

        modeTv.setOnClickListener(this);
        moneyTv.setOnClickListener(this);
        colorTv.setOnClickListener(this);
        decTv.setOnClickListener(this);
        addTv.setOnClickListener(this);
        okTv.setOnClickListener(this);
        if(shopBean==null){
            return;
        }
        if(shopBean.goods!=null){
            moneyTv.setText("￥"+shopBean.goods.price+"");
            money=shopBean.goods.price;
        }
        if (shopBean.sizeList != null && shopBean.sizeList.size() > 0) {
            modeTv.setText(shopBean.sizeList.get(0));
            modeAdapter = new Adapter(context, R.layout.item_add_car, shopBean.sizeList);
            modeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    modelWindow.dismiss();
                    size = shopBean.sizeList.get(position);
                    modeTv.setText(size);

                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }else{
            modeTv.setText("默认");
        }

        if (shopBean.colorList != null && shopBean.colorList.size() > 0) {
            ColorId=shopBean.colorList.get(0).colorId;
            colorTv.setText(shopBean.colorList.get(0).name);
            colorAdpater = new ColorAdapter(context, R.layout.item_add_car, shopBean.colorList);
            colorAdpater.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    colorWindow.dismiss();
                    ColorId = shopBean.colorList.get(position).colorId;
                    colorTv.setText(shopBean.colorList.get(position).name);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }else{
            colorTv.setText("默认");
        }


    }

    /**
     * 添加黑色半透明背景
     */
    private void initWindow() {
        Window dialogWindow = getWindow();
        dialogWindow.setBackgroundDrawable(new ColorDrawable(0));//设置window背景
        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//设置输入法显示模式
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();//获取屏幕尺寸
        lp.width = (int) (d.widthPixels * 0.8); //宽度为屏幕80%
        lp.gravity = Gravity.CENTER;     //中央居中
        dialogWindow.setAttributes(lp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_car_add:
                if (shopBean.goods != null && shopBean.groupBuy != null) {
                    if (shopBean.goods.isPreferential == 2) {
                        if (num >= shopBean.groupBuy.buyNum) {
                            addTv.setEnabled(false);
                            ToastUtil.show("团购最大数量为" + shopBean.groupBuy.buyNum);
                            return;

                        }
                    }
                }
                numTv.setText((++num) + "");
                if(num>1){
                  decTv.setEnabled(true);
                }
                CalTotal();
                break;
            case R.id.dialog_car_dec:

                if (num == 1) {
                    decTv.setEnabled(false);
                    ToastUtil.show("不能再减了");
                } else {
                    num--;

                }
                numTv.setText((num) + "");
                if (shopBean.goods != null && shopBean.groupBuy != null) {
                    if (shopBean.goods.isPreferential == 2) {
                        if (num <shopBean.groupBuy.buyNum) {
                            addTv.setEnabled(true);

                        }
                    }
                }
                CalTotal();
                break;
            case R.id.dialog_type:
                if (modelWindow == null) {
                    modelWindow = new ListPopupWindow(context, modeAdapter, ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.dip2px(context, 100));
                }
                modelWindow.showPopupWindow(v);
                break;
            case R.id.dialog_color:
                if (colorWindow== null) {
                    colorWindow = new ListPopupWindow(context, colorAdpater, ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.dip2px(context, 100));
                }
                colorWindow.showPopupWindow(v);
                break;
            case R.id.dialog_ok:
                dismiss();
                message=new EventMessage("shop");
                message.colorId=ColorId;
                message.num=num;
                message.size=size;
                message.total=money;
                EventBus.getDefault().post(message);
                break;

        }

    }

    private void CalTotal() {
        if (shopBean.goods != null) {
            totalMoney = shopBean.goods.price * num;
         /*   if (shopBean.goods.isPreferential == 0) {


            } else if (shopBean.goods.isPreferential == 1) {
                if (shopBean.markDown != null) {
                    if (shopBean.markDown.conditions == 0) {

                    } else {

                    }
                }
            }*/
        }

        moneyTv.setText("￥" + totalMoney);
    }

    private static class Adapter extends CommonAdapter<String> {

        public Adapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, String s, int position) {
            holder.setText(R.id.item_tv, s);
        }
    }


    private static class ColorAdapter extends CommonAdapter<ColorListBean> {

        public ColorAdapter(Context context, int layoutId, List<ColorListBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, ColorListBean s, int position) {
            if (s != null) {
                holder.setText(R.id.item_tv, s.name);
            }

        }
    }
}
