package com.td.oldplay.ui.mine.activity;

import android.os.Bundle;
import android.view.View;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseFragmentActivity {

    @BindView(R.id.title)
    CustomTitlebarLayout title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        title.setTitle("关于我们");
        title.setOnLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
