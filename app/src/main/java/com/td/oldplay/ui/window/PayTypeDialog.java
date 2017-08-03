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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.td.oldplay.R;


/**
 * Created by Administrator on 2015/11/2.
 */
public class PayTypeDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private TextView okTv;
    private TextView cancelTv;
    private TextView titletTv;
    private RadioGroup rgPay;
    private RadioButton rbTeacher;
    private RadioButton rbAccount;
    private CheckBox cbScore;
    private View viewLine;

    private int payType;
    private boolean isScore;
    private int scoreId;
    private DialogClick dialogClick;
    private boolean isTeacher;

    public PayTypeDialog(@NonNull Context context, boolean isTeacher) {
        super(context, R.style.AlertDialogStyle); //dialog的样式
        this.context = context;
        this.isTeacher = isTeacher;
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
        View view = inflater.inflate(R.layout.dialog_paytype, null);
        setContentView(view);
        initWindow();

        titletTv = (TextView) view.findViewById(R.id.dialog_title);
        cancelTv = (TextView) view.findViewById(R.id.dialog_cancel);
        okTv = (TextView) view.findViewById(R.id.dialog_ok);

        rgPay = (RadioGroup) view.findViewById(R.id.dialog_pay_rg);
        rbTeacher = (RadioButton) view.findViewById(R.id.teacher);
        rbAccount= (RadioButton) view.findViewById(R.id.account);
        if (isTeacher) {
            rbTeacher.setVisibility(View.VISIBLE);
        } else {
            rbTeacher.setVisibility(View.GONE);
        }

        cbScore = (CheckBox) view.findViewById(R.id.acore);
        viewLine = view.findViewById(R.id.viewLine);
        okTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
        rgPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                cbScore.setEnabled(false);
                cbScore.setChecked(false);
                isScore = false;
                scoreId = 0;
                switch (checkedId) {
                    case R.id.account:
                        payType = 0;
                        cbScore.setEnabled(true);
                        break;
                    case R.id.weixin:
                        payType = 1;
                        break;
                    case R.id.zhifubao:
                        payType = 2;
                        break;
                    case R.id.teacher:
                        payType = 3;
                        break;

                }
            }
        });
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
    public void setAccount(String accocunt) {
        rbAccount.append(accocunt);
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
                    dialogClick.onOk(payType, scoreId);
                }
                break;
        }
    }

    public interface DialogClick {
        void onCancel();

        void onOk(int payType, int scoreId);
    }
}
