package com.td.oldplay.ui.shop.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.score_ed)
    EditText scoreEd;
    @BindView(R.id.score_send)
    Button scoreSend;
    @BindView(R.id.title)
    CustomTitlebarLayout title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);
        title.setOnLeftListener(this);
        scoreSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.score_send:
                break;
        }

    }
}
