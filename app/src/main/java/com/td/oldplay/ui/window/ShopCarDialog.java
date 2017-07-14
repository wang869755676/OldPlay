package com.td.oldplay.ui.window;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.ShopBean;

import java.util.List;


/**
 * Created by Administrator on 2015/11/2.
 */
public class ShopCarDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private View.OnClickListener listener;
    private ShopBean shopBean;

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
    private Adapter colorAdpater;
    private int num = 0;

    public ShopCarDialog(@NonNull Context context, View.OnClickListener listener, ShopBean shopBean) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.shopBean = shopBean;
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

        moneyTv.setOnClickListener(this);
        colorTv.setOnClickListener(this);
        decTv.setOnClickListener(this);
        addTv.setOnClickListener(this);

        okTv.setOnClickListener(listener);
        //  modeAdapter=new Adapter(context,R.layout.item_month);
        //   colorAdpater=new Adapter(context,R.layout.item_month);


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
                numTv.setText((num++) + "");
                break;
            case R.id.dialog_car_dec:
                numTv.setText((num--) + "");
                break;
            case R.id.dialog_type:
              /*  if(modelWindow!=null){
                    modelWindow=new ListPopupWindow(context,new Adapter(context,R.layout.item_))
                }*/
                break;
            case R.id.dialog_color:
              /*  if(modelWindow!=null){
                    modelWindow=new ListPopupWindow(context,new Adapter(context,R.layout.item_))
                }*/
                break;
        }

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
}
