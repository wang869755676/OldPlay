package com.td.oldplay.ui.forum.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.ui.course.adapter.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FourmDetailActivity extends BaseFragmentActivity implements
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.forum_detail_sence)
    TextView forumDetailSence;
    @BindView(R.id.forum_detail_title)
    TextView forumDetailTitle;
    @BindView(R.id.forum_detail_edit)
    TextView forumDetailEdit;
    @BindView(R.id.forum_detail_comment)
    TextView forumDetailComment;
    @BindView(R.id.forum_detail_name)
    TextView forumDetailName;
    @BindView(R.id.tv_coment_total)
    TextView tvComentTotal;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.ed_comment)
    EditText edComment;
    @BindView(R.id.bt_publish)
    Button btPublish;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_image)
    ImageView rightImage;
    @BindView(R.id.right_image2)
    ImageView rightImage2;

    private List<CommentBean> datas=new ArrayList<>();
    private CommentAdapter commentAdapter;
    private LoadMoreWrapper Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourm_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleText.setText("详情");
        titleBack.setOnClickListener(this);
        rightImage.setOnClickListener(this);
        rightImage2.setOnClickListener(this);
        btPublish.setOnClickListener(this);
        forumDetailEdit.setOnClickListener(this);
        forumDetailEdit.setVisibility(View.GONE);
        swipeLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        commentAdapter=new CommentAdapter(mContext,R.layout.item_comment,datas);
        Adapter=new LoadMoreWrapper(commentAdapter);
        swipeTarget.setAdapter(Adapter);

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.right_image: // 分享
                break;
            case R.id.right_image2: // 喜欢
                break;
            case R.id.bt_publish: // 发表评论
                break;
            case R.id.forum_detail_edit: // 编辑信息
                break;
        }

    }

    @Override
    public void onLoadMoreRequested() {

    }
}
