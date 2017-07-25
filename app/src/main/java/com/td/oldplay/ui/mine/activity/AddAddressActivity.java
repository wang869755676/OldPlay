package com.td.oldplay.ui.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.utils.AppUtils;
import com.td.oldplay.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class AddAddressActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.adr_name)
    EditText adrName;

    @BindView(R.id.adr_phone)
    EditText adrPhone;
    @BindView(R.id.adr_adr)
    EditText adrAdr;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.ok)
    TextView ok;
    private AddressBean bean;

    private String name;
    private String phone;
    private String address;
    private HashMap<String, Object> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        bean = (AddressBean) getIntent().getSerializableExtra("model");
        if (bean != null) {
            params.put("addressId", bean.addressId);
            initDatas();
        }
        params.put("userId", userId);
    }

    private void initDatas() {
        adrName.setText(bean.consignee);
        adrPhone.setText(bean.mobile);
        adrAdr.setText(bean.address);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.ok:
                v.setEnabled(false);
                if (checkInput()) {
                    if (bean == null) {
                        HttpManager.getInstance().addAddress(params, new HttpSubscriber<String>(new OnResultCallBack<String>() {

                            @Override
                            public void onSuccess(String s) {
                                v.setEnabled(true);
                                ToastUtil.show("添加成功");
                                EventBus.getDefault().post(new EventMessage("loadAddress"));
                                finish();
                            }

                            @Override
                            public void onError(int code, String errorMsg) {

                            }

                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }
                        }));
                    } else {
                        HttpManager.getInstance().updateAddress(params, new HttpSubscriber<String>(new OnResultCallBack<String>() {

                            @Override
                            public void onSuccess(String s) {
                                v.setEnabled(true);
                                ToastUtil.show("修改成功");
                                EventBus.getDefault().post(new EventMessage("loadAddress"));
                                finish();
                            }

                            @Override
                            public void onError(int code, String errorMsg) {

                            }

                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }
                        }));
                    }

                }
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    private boolean checkInput() {

        name = adrName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请输入姓名");
            return false;
        }
        params.put("consignee", name);


        phone = adrPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请输入手机号");
            return false;
        } else if (!AppUtils.checkPhone(phone)) {
            ToastUtil.show("请输入正确的手机号");
            return false;
        }
        params.put("mobile", phone);

        address = adrAdr.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请输入地址");
            return false;
        }
        params.put("address", address);

        return true;
    }
}
