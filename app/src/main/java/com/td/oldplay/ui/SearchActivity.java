package com.td.oldplay.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.ui.course.adapter.CourserAdapter;
import com.td.oldplay.ui.course.adapter.ShopAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseFragmentActivity {

    @BindView(R.id.search_edite)
    EditText searchEdite;
    @BindView(R.id.search_cancle)
    TextView searchCancle;
    @BindView(R.id.seach_recycler)
    RecyclerView seachRecycler;

    private int type ;  // 0 搜索课程  1 搜索商城

    private ShopAdapter shopAdapter;
    private CourserAdapter courserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        seachRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        searchEdite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 search(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || "".equals(s)) {
                    seachRecycler.setVisibility(View.GONE);
                }
            }
        });
    }

    //搜索数据
    private void search(CharSequence s) {
    }
}
