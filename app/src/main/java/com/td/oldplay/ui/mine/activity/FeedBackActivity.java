package com.td.oldplay.ui.mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedBackActivity extends BaseFragmentActivity implements View.OnClickListener{

    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.ed_fadeBack)
    EditText edFadeBack;
    @BindView(R.id.feedback_send)
    Button feedbackSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        title.setTitle("意见反馈");
        title.setOnLeftListener(this);
        feedbackSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_text:
                finish();
                break;
            case R.id.feedback_send:
                break;
        }
    }
}
