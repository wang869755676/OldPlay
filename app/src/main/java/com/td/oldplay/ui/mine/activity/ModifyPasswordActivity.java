package com.td.oldplay.ui.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModifyPasswordActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.text_user)
    TextView textUser;
    @BindView(R.id.old_pwd)
    EditText oldPwd;
    @BindView(R.id.text_user3)
    TextView textUser3;
    @BindView(R.id.update_pwd)
    EditText updatePwd;
    @BindView(R.id.text_user4)
    TextView textUser4;
    @BindView(R.id.re_update_pwd)
    EditText reUpdatePwd;
    @BindView(R.id.que_update_pwd)
    Button queUpdatePwd;

    private int type;
    private String oldPws;
    private String newPws;
    private String reNewPws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            title.setTitle("修改登录密码");
        } else if (type == 1) {
            title.setTitle("修改支付密码");
        }
        title.setOnLeftListener(this);
        queUpdatePwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.que_update_pwd:
                if(checkInput()){
                    updatePassword();
                }

                break;
        }
    }

    private boolean checkInput() {
        oldPws = oldPwd.getText().toString();
        newPws = updatePwd.getText().toString();
        reNewPws = reUpdatePwd.getText().toString();
        if (TextUtils.isEmpty(oldPws)) {
            oldPwd.setError("请输入旧密码");
            return false;
        } else if (TextUtils.isEmpty(newPws)) {
            updatePwd.setError("请输入新密码");
            return false;
        } else if (TextUtils.isEmpty(reNewPws)) {
            reUpdatePwd.setError("请输入确认密码");
            return false;
        } else if (!newPws.equals(reNewPws)) {
            reUpdatePwd.setError("两次密码不一致");
            return false;
        } else

            return true;
    }

    private void updatePassword() {
    }
}
