package com.td.oldplay.ui.forum.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.ForumBean;
import com.td.oldplay.bean.ForumDetial;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.adapter.CommentAdapter;
import com.td.oldplay.ui.course.adapter.ShopAdapter;
import com.td.oldplay.ui.forum.adapter.PicAdapter;
import com.td.oldplay.ui.forum.adapter.VideAdapter;
import com.td.oldplay.ui.forum.adapter.VoiceAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FourmDetailActivity extends BaseFragmentActivity implements
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {


    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_image)
    ImageView rightImage;
    @BindView(R.id.right_image2)
    ImageView rightImage2;
    @BindView(R.id.likeNum)
    TextView likeNum;

    @BindView(R.id.forum_detail_sence)
    TextView forumDetailSence;
    @BindView(R.id.forum_detail_title)
    TextView forumDetailTitle;
    @BindView(R.id.forum_detail_comment)
    TextView forumDetailComment;
    @BindView(R.id.forum_detail_edit)
    TextView forumDetailEdit;
    @BindView(R.id.ll_des)
    LinearLayout llDes;
    @BindView(R.id.forum_detail_name)
    TextView forumDetailName;
    @BindView(R.id.tv_coment_total)
    TextView tvComentTotal;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.comment_ed)
    EditText commentEd;
    @BindView(R.id.comment_send)
    TextView commentSend;
    @BindView(R.id.ll_comment)
    LinearLayout llComment;
    @BindView(R.id.ry_pic)
    RecyclerView ryPic;
    @BindView(R.id.ry_au)
    RecyclerView ryAu;
    @BindView(R.id.ry_video)
    RecyclerView ryVideo;
    private List<CommentBean> datas = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private LoadMoreWrapper Adapter;

    private List<String> picStr = new ArrayList<>();
    private List<String> voiceStr = new ArrayList<>();
    private List<String> videoStr = new ArrayList<>();
    private PicAdapter picAdapter;
    private VoiceAdapter voiceAdapter;
    private VideAdapter videAdapter;

    private ForumDetial forumDetial;

    private HttpSubscriber<ForumDetial> subscriber;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourm_detail);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        titleText.setText("详情");
        titleBack.setOnClickListener(this);
        rightImage.setOnClickListener(this);
        rightImage2.setOnClickListener(this);
        commentSend.setOnClickListener(this);
        forumDetailEdit.setOnClickListener(this);
        forumDetailEdit.setVisibility(View.GONE);
        swipeLayout.setOnRefreshListener(this);
        swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        commentAdapter = new CommentAdapter(mContext, R.layout.item_comment, datas);
        Adapter = new LoadMoreWrapper(commentAdapter);
        swipeTarget.setAdapter(Adapter);
        ryAu.setLayoutManager(new LinearLayoutManager(mContext));
        ryVideo.setLayoutManager(new LinearLayoutManager(mContext));
        ryPic.setLayoutManager(new LinearLayoutManager(mContext));
        picAdapter = new PicAdapter(mContext, R.layout.item_shop_pic, picStr);
        videAdapter = new VideAdapter(mContext, R.layout.item_videoview, videoStr);
        // picAdapter=new PicAdapter(mContext,R.layout.item_shop_pic,picStr);
        ryPic.setAdapter(picAdapter);
        ryVideo.setAdapter(Adapter);
        subscriber = new HttpSubscriber<>(new OnResultCallBack<ForumDetial>() {

            @Override
            public void onSuccess(ForumDetial forumDetial) {
                if (forumDetial != null) {
                    setData();
                }
            }

            @Override
            public void onError(int code, String errorMsg) {

            }
        });
        getData();
    }

    private void setData() {
        if (forumDetial.topic != null) {
            forumDetailTitle.setText(forumDetial.topic.title);
            forumDetailComment.setText(forumDetial.topic.replyCount + "评论");
            forumDetailName.setText(forumDetial.topic.userName);
            if (forumDetial.topic.userId.equals(userId)) {
                forumDetailEdit.setVisibility(View.VISIBLE);
            } else {
                forumDetailEdit.setVisibility(View.GONE);
            }
        }
        if (forumDetial.imageUrlList != null) {
            picStr.addAll(forumDetial.imageUrlList);
            picAdapter.notifyDataSetChanged();
        }
     /*   if (forumDetial.speechUrlList!=null) {
            voiceStr.addAll(forumDetial.speechUrlList);
            voiceAdapter.notifyDataSetChanged();
        }*/
        if (forumDetial.videoUrlList != null) {
            videoStr.addAll(forumDetial.videoUrlList);
            videAdapter.notifyDataSetChanged();
        }


    }

    private void getData() {
        HttpManager.getInstance().getForumDetials(id, subscriber);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.right_image: // 分享
                break;
            case R.id.right_image2: // 喜欢
                break;
            case R.id.forum_detail_edit: // 编辑信息
                break;
            case R.id.comment_send: // 发表评论
                break;
        }

    }

    @Override
    public void onLoadMoreRequested() {

    }

}
