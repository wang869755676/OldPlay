package com.td.oldplay.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.utils.PrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterScoreActivity extends Activity {


    @BindView(R.id.dialog_content)
    TextView dialogContent;
    @BindView(R.id.dialog_ok)
    TextView dialogOk;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        dialogContent.setText(title);
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        PrefUtils.putString(this, MContants.PRE_SCORE_KEY,"");

    }


}
