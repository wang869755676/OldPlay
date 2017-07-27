package com.td.oldplay.ui.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.td.oldplay.widget.password.PasswordInputView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargeActivity extends BaseFragmentActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.check_weixin)
    CheckBox checkWeixin;
    @BindView(R.id.check_zhifubao)
    CheckBox checkZhifubao;
    @BindView(R.id.recharge_money)
    EditText rechargeMoney;
    @BindView(R.id.commit)
    Button commit;
    private CustomDialog customDialog;
    private PasswordInputView passwordInputView;
    private View dialogView;
    private String password;
    private int payType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initDialog();
        title.setTitle("充值");
        title.setOnLeftListener(this);
        commit.setOnClickListener(this);
        checkWeixin.setOnCheckedChangeListener(this);
        checkZhifubao.setOnCheckedChangeListener(this);
    }

    private void initDialog() {
        customDialog = new CustomDialog(mContext);
        customDialog.setTitle("输入密码");
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_password, null);
        passwordInputView = (PasswordInputView) dialogView.findViewById(R.id.password);
        customDialog.setContanier(dialogView);
        customDialog.setDialogClick(new CustomDialog.DialogClick() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                password = passwordInputView.getText().toString();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.commit:
                if (payType == -1) {
                    ToastUtil.show("请选择支付方式");
                    return;
                }

                if (TextUtils.isEmpty(rechargeMoney.getText().toString().trim())) {
                    ToastUtil.show("请输入充值金额");
                    return;
                }
                if (customDialog != null) {
                    customDialog.show();
                }
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.check_weixin:
                if (isChecked) {
                    checkZhifubao.setChecked(false);
                    payType = 1;
                } else {
                    payType = -1;
                }
                break;
            case R.id.check_zhifubao:
                if (isChecked) {
                    checkWeixin.setChecked(false);
                    payType = 2;
                } else {
                    payType = -1;
                }
                break;
        }
    }
}
