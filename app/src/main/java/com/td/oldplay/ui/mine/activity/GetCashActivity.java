package com.td.oldplay.ui.mine.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.window.CustomDialog;
import com.td.oldplay.ui.window.PayAlertDialog;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.td.oldplay.widget.password.PasswordInputView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class GetCashActivity extends BaseFragmentActivity
        implements View.OnClickListener, TextWatcher {


    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.bank_num)
    EditText bankNum;
    @BindView(R.id.cash)
    EditText cashMoney;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.bank)
    EditText bank;


    private String cash;
    private String num;
    private String bankName;
    private String OpeningBank;

    private float totalMoney;

    private CustomDialog customDialog;
    private PasswordInputView passwordInputView;
    private View dialogView;
    private String password;

    private PayAlertDialog alertDialog;

    private PayAlertDialog paySuccessDilaog;
    private HashMap<String, Object> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cash);
        totalMoney = getIntent().getFloatExtra("money", 0);
        ButterKnife.bind(this);
        initDialog();
        title.setTitle("提现");
        title.setOnLeftListener(this);
        commit.setOnClickListener(this);
        cashMoney.setHint("可提现" + totalMoney);
        initListener();
        params.put("userId", userId);


    }

    private void initDialog() {
        customDialog = new CustomDialog(mContext);
        customDialog.setTitle("输入密码");
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_password, null);
        passwordInputView = (PasswordInputView) dialogView.findViewById(R.id.password);
        customDialog.setContanier(dialogView);
        customDialog.setDialogClick(new CustomDialog.DialogClick() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                password = passwordInputView.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.show("输入密码");
                    return;
                }
                params.put("payPassword", password);
                customDialog.dismiss();
                commitServer();
            }
        });

        paySuccessDilaog = new PayAlertDialog(mContext, false, false);
        paySuccessDilaog.setContent("提现申请已提交\n" +
                "请等待平台处理");

        alertDialog = new PayAlertDialog(mContext, false, true);
        alertDialog.setContent("提示");
        alertDialog.setScore("最低100元起提");


    }

    private void initListener() {
        cashMoney.addTextChangedListener(this);
        bankNum.addTextChangedListener(this);
        bank.addTextChangedListener(this);
        name.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                if (Float.parseFloat(cash) <= 100) {
                    alertDialog.show();
                    return;
                }

                customDialog.show();


                break;
            case R.id.left_text:
                finish();
                break;

        }

    }

    /**
     * 提交到服务器
     */
    private void commitServer() {

        HttpManager.getInstance().withDrawMoney(params, new HttpSubscriber<String>(new OnResultCallBack<String>() {


            @Override
            public void onSuccess(String s) {
                paySuccessDilaog.show();
            }

            @Override
            public void onError(int code, String errorMsg) {
                ToastUtil.show(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);

            }
        }));

    }

    private boolean checkInput() {
        cash = cashMoney.getText().toString().trim();
        num = bankNum.getText().toString().trim();
        bankName = name.getText().toString().trim();
        OpeningBank = bank.getText().toString().trim();

        if (TextUtils.isEmpty(cash)) {
            //  ToastUtil.show("请输入金额！");
            return false;
        } else if (TextUtils.isEmpty(num)) {
            // ToastUtil.show("请输入卡号！");
            return false;
        } else if (TextUtils.isEmpty(bankName)) {
            //ToastUtil.show("请输入姓名！");
            return false;
        } /*else if (TextUtils.isEmpty(OpeningBank)) {
            ToastUtil.show("开户银行！");
            return false;
        }*/
        params.put("money", cash);
        params.put("cardNo", num);
        params.put("name", bankName);
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (checkInput()) {
            commit.setEnabled(true);
        } else {
            commit.setEnabled(false);
        }
    }
}
