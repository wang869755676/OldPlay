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


public class AlertDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private TextView okTv;

    private TextView contentTv;

    public AlertDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle); //dialog的样式
        this.context = context;

        initalize();
    }


    //初始化View
    private void initalize() {
        setCanceledOnTouchOutside(true);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_alert, null);
        setContentView(view);
        initWindow();
        contentTv = (TextView) view.findViewById(R.id.dialog_content);
        okTv = (TextView) view.findViewById(R.id.dialog_ok);
        okTv.setOnClickListener(this);

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


    public void setContent(String content) {
        contentTv.setText(content);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_ok:
                if (alertOk != null) {
                    alertOk.onClickOk();
                }
                break;
        }
    }

    private onAlertOk alertOk;

    public void setAlertOk(onAlertOk alertOk) {
        this.alertOk = alertOk;
    }

    public interface onAlertOk {
        void onClickOk();
    }

}
