package com.td.oldplay.ui.mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAddressActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.adr_name)
    TextView adrName;
    @BindView(R.id.search_edite)
    EditText searchEdite;
    @BindView(R.id.adr_phone)
    EditText adrPhone;
    @BindView(R.id.adr_adr)
    EditText adrAdr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                break;
            case R.id.cancel:
                break;
        }
    }
}
