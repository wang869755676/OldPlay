package com.td.oldplay.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPasswordActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.tv_modify_login)
    TextView tvModifyLogin;
    @BindView(R.id.tv_modify_zhifu)
    TextView tvModifyZhifu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_password);
        ButterKnife.bind(this);
        title.setTitle("密码修改");
        title.setOnLeftListener(this);
        tvModifyLogin.setOnClickListener(this);
        tvModifyZhifu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.left_text:
                finish();
                break;
            case R.id.tv_modify_login:
                intent=new Intent(mContext,ModifyPasswordActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
                break;
            case R.id.tv_modify_zhifu:
                intent=new Intent(mContext,ModifyPasswordActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                break;
        }

    }
}
