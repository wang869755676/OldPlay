package com.td.oldplay.ui.window;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.oldplay.R;


/**
 * Created by Administrator on 2015/11/2.
 */
public class CustomDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private TextView okTv;
    private TextView cancelTv;
    private TextView titletTv;
    private TextView contentTv;
    private LinearLayout titleLL;
    private View viewLine;
    private FrameLayout contaner;

    private DialogClick dialogClick;

    public CustomDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle); //dialog的样式
        this.context = context;

        initalize();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setDialogClick(DialogClick dialogClick) {
        this.dialogClick = dialogClick;
    }

    //初始化View
    private void initalize() {
        setCanceledOnTouchOutside(true);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_custom, null);
        setContentView(view);
        initWindow();
        contentTv = (TextView) view.findViewById(R.id.dialog_content);
        viewLine = view.findViewById(R.id.dialog_line);
        titleLL = (LinearLayout) view.findViewById(R.id.dialog_title_ll);
        titletTv = (TextView) view.findViewById(R.id.dialog_title);
        cancelTv = (TextView) view.findViewById(R.id.dialog_cancel);
        okTv = (TextView) view.findViewById(R.id.dialog_ok);
        contaner = (FrameLayout) view.findViewById(R.id.dialog_container);

        okTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
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
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT; //宽度为屏幕80%
        lp.gravity = Gravity.CENTER;     //中央居中
        dialogWindow.setAttributes(lp);
    }

    public void setTitle(String title) {
        titletTv.setText(title);
    }

    public void setTitleVisible(int visible) {
        titleLL.setVisibility(visible);
    }

    public void setViewLineVisible(int visible) {
        viewLine.setVisibility(visible);
    }

    public void setContent(String content) {
        contentTv.setText(content);
    }
    public void setContentGravity(int gravity) {
        contentTv.setGravity(gravity);
    }

    public void setContanier(View view) {
        contentTv.setVisibility(View.GONE);
        contaner.addView(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel:
                if (dialogClick != null) {
                    dismiss();
                    dialogClick.onCancel();
                }
                break;
            case R.id.dialog_ok:
                if (dialogClick != null) {
                    dismiss();
                    dialogClick.onOk();
                }
                break;
        }
    }

    public interface DialogClick {
        void onCancel();

        void onOk();
    }
}
