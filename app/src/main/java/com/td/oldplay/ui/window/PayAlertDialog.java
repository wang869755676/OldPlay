package com.td.oldplay.ui.window;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.td.oldplay.R;


/**
 * Created by Administrator on 2015/11/2.
 */
public class PayAlertDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private TextView okTv;
    private TextView cancelTv;
    private TextView contentTv;
    private TextView scoreTv;


    private DialogClick dialogClick;
    private boolean isCancel;
    private boolean isScore;


    public PayAlertDialog(@NonNull Context context, boolean isCancel, boolean isScore) {
        super(context, R.style.AlertDialogStyle); //dialog的样式
        this.context = context;
        this.isCancel = isCancel;
        this.isScore = isCancel;
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
        View view = inflater.inflate(R.layout.dialog_payt_alert, null);
        setContentView(view);
        initWindow();

        contentTv = (TextView) view.findViewById(R.id.dialog_content);
        cancelTv = (TextView) view.findViewById(R.id.dialog_cancel);
        okTv = (TextView) view.findViewById(R.id.dialog_ok);
        scoreTv = (TextView) view.findViewById(R.id.dialog_score);

        if (isCancel) {
            cancelTv.setVisibility(View.VISIBLE);
        } else {
            cancelTv.setVisibility(View.GONE);
        }

        if (isScore) {
            scoreTv.setVisibility(View.VISIBLE);
        } else {
            scoreTv.setVisibility(View.GONE);
        }

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

    public void setContent(String content) {
        contentTv.setText(content);
    }

    public void setScore(String score) {
        scoreTv.setText(score);
    }
    public void setOkStr(String str) {
        okTv.setText(str);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel:
                dismiss();
                if (dialogClick != null) {

                    dialogClick.onBack();
                }
                break;
            case R.id.dialog_ok:
                dismiss();
                if (dialogClick != null) {
                    dialogClick.onnext();
                }
                break;
        }
    }

    public interface DialogClick {
        void onBack();

        void onnext();
    }
}
