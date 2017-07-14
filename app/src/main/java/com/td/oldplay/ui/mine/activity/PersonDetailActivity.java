package com.td.oldplay.ui.mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.widget.CircleImageView;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonDetailActivity extends BaseFragmentActivity implements View.OnClickListener{

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.person_iv)
    CircleImageView personIv;
    @BindView(R.id.person_name)
    EditText personName;
    @BindView(R.id.person_save)
    Button personSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        ButterKnife.bind(this);
        title.setTitle("个人资料");
        title.setOnLeftListener(this);
        personSave.setOnClickListener(this);
        personIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.person_iv:
                break;
            case R.id.person_save:
                break;
            case R.id.left_text:
                finish();
                break;
        }

    }
}
