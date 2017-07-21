package com.td.oldplay.ui.forum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.base.EventMessage;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.ForumBean;
import com.td.oldplay.bean.ForumDetial;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.adapter.CommentAdapter;
import com.td.oldplay.ui.course.adapter.ShopAdapter;
import com.td.oldplay.ui.forum.adapter.PicAdapter;
import com.td.oldplay.ui.forum.adapter.VideAdapter;
import com.td.oldplay.ui.forum.adapter.VoiceAdapter;
import com.td.oldplay.utils.SoftInputUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.voicemanager.VoiceManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

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
    private LoadMoreWrapper adapter;

    private List<String> picStr = new ArrayList<>();
    private List<String> voiceStr = new ArrayList<>();
    private List<String> videoStr = new ArrayList<>();
    private PicAdapter picAdapter;
    private VoiceAdapter voiceAdapter;
    private VideAdapter videAdapter;

    private ForumDetial forumDetial;

    private HttpSubscriber<ForumDetial> subscriber;
    private String id;
    private int page = 1;
    private HttpSubscriber<List<CommentBean>> commentSubscriber;
    private HashMap<String, Object> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourm_detail);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        params.put("userId", userId);
        params.put("topicId", id);
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
        commentAdapter = new CommentAdapter(mContext, R.layout.item_forum_comment, datas);
        adapter = new LoadMoreWrapper(commentAdapter);
        adapter.setOnLoadMoreListener(this);
        swipeTarget.setAdapter(adapter);
        ryAu.setLayoutManager(new LinearLayoutManager(mContext));
        ryVideo.setLayoutManager(new LinearLayoutManager(mContext));
        ryPic.setLayoutManager(new LinearLayoutManager(mContext));
        picAdapter = new PicAdapter(mContext, R.layout.item_shop_pic, picStr);
        videAdapter = new VideAdapter(mContext, R.layout.item_videoview, videoStr);
        // picAdapter=new PicAdapter(mContext,R.layout.item_shop_pic,picStr);
        ryPic.setAdapter(picAdapter);
        ryVideo.setAdapter(videAdapter);
        subscriber = new HttpSubscriber<>(new OnResultCallBack<ForumDetial>() {

            @Override
            public void onSuccess(ForumDetial forumDetial) {
                if (forumDetial != null) {
                    FourmDetailActivity.this.forumDetial = forumDetial;
                    setData();
                }
            }

            @Override
            public void onError(int code, String errorMsg) {

            }
        });
        commentSubscriber = new HttpSubscriber<>(new OnResultCallBack<List<CommentBean>>() {


            @Override
            public void onSuccess(List<CommentBean> commentBean) {
                swipeLayout.setRefreshing(false);
                if (commentBean != null && commentBean.size() > 0) {
                    if (page == 1) {
                        datas.clear();
                        if (commentBean.size() >= MContants.PAGENUM) {
                            adapter.setLoadMoreView(R.layout.default_loading);
                        }

                    }
                    datas.addAll(commentBean);
                } else {
                    adapter.setLoadMoreView(0);
                    if (page > 1) {

                        ToastUtil.show("没有更多数据了");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String errorMsg) {
                swipeLayout.setRefreshing(false);
                ToastUtil.show(errorMsg);
            }
        });
        HttpManager.getInstance().getForumDetials(id, subscriber);
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

        HttpManager.getInstance().getForumComment(id, page, commentSubscriber);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
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
                Intent intent=new Intent(mContext,PublishForumActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("model",forumDetial);
                startActivity(intent);
                break;
            case R.id.comment_send: // 发表评论
                if (!TextUtils.isEmpty(commentEd.getText().toString())) {
                    SoftInputUtils.hideSoftInput(AContext,getWindow());
                    params.put("content", commentEd.getText().toString());
                    HttpManager.getInstance().sendComment(params, new HttpSubscriber<String>(new OnResultCallBack<String>() {

                        @Override
                        public void onSuccess(String s) {
                            commentEd.setText("");
                            forumDetailComment.setText((forumDetial.topic.replyCount+1) + "评论");
                            EventBus.getDefault().post(new EventMessage("forumCommnet"));
                            onRefresh();
                        }

                        @Override
                        public void onError(int code, String errorMsg) {

                        }
                    }));
                }
                break;
        }

    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoiceManager.getInstance(mContext).stopPlay();
        subscriber.unSubscribe();
        commentSubscriber.unSubscribe();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}
