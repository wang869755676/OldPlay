package com.td.oldplay.ui.shop.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.bean.GoodBean;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.shop.adapter.ShopCommentAdapter;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.tencent.mm.opensdk.utils.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class ScoreActivity extends BaseFragmentActivity implements View.OnClickListener {


    @BindView(R.id.score_send)
    Button scoreSend;
    @BindView(R.id.title)
    CustomTitlebarLayout title;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private String id;
    private List<GoodBean> datas;
    private ShopCommentAdapter adapter;
    private List<String> ids = new ArrayList<>();
    private List<String> contents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        datas = (List<GoodBean>) getIntent().getSerializableExtra("model");
        ButterKnife.bind(this);
        title.setTitle("商品评价");
        title.setOnLeftListener(this);
        scoreSend.setOnClickListener(this);
        if (datas == null) {
            return;
        }
        adapter = new ShopCommentAdapter(mContext, R.layout.item_comment_shop, datas);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            case R.id.score_send:
                ids.clear();
                contents.clear();
                for (int i = 0; i < datas.size(); i++) {
                    if (TextUtils.isEmpty(datas.get(i).comment)) {
                        ToastUtil.show("请输入" + datas.get(i).goodsName + "的评论内容");
                        break;
                    }
                    ids.add(datas.get(i).goodsId);
                    contents.add(datas.get(i).comment);
                    Log.e("===",i+"  "+datas.get(i).comment);

                }
                HttpManager.getInstance().commentShop(userId, ids, contents, new HttpSubscriber<String>(new OnResultCallBack<String>() {

                    @Override
                    public void onSuccess(String s) {
                        ToastUtil.show("评论成功");
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
