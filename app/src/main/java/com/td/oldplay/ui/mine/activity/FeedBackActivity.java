package com.td.oldplay.ui.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class FeedBackActivity extends BaseFragmentActivity implements View.OnClickListener {

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
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.feedback_send:
                if (TextUtils.isEmpty(edFadeBack.getText().toString())) {
                    ToastUtil.show("请输入内容");
                    return;
                }
                HttpManager.getInstance().feedBack(userId, edFadeBack.getText().toString(), new HttpSubscriber<String>(new OnResultCallBack<String>() {


                    @Override
                    public void onSuccess(String s) {
                        ToastUtil.show("已收到您的宝贵意见");
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
                break;
        }
    }
}
