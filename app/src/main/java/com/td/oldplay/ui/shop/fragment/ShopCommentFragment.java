package com.td.oldplay.ui.shop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.adapter.listview.CommonAdapter;
import com.td.oldplay.base.adapter.listview.ViewHolder;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.adapter.CommentAdapter;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustomListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopCommentFragment extends BaseFragment implements LoadMoreWrapper.OnLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.swipe_target)
    ListView swipeTarget;
    Unbinder unbinder;

    private CommentAdapter commentAdapter;
    private LoadMoreWrapper adapter;
    private List<CommentBean> datas;
    private int page = 1;
    private String goodId;
    private CommonAdapter lisCommonAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
        goodId = mActivity.getIntent().getStringExtra("id");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void init(View view) {
        datas = new ArrayList<>();
       // swipeToLoadLayout.setOnRefreshListener(this);
       // swipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        //commentAdapter = new CommentAdapter(mActivity, R.layout.item_comment_shopdetail, datas);
       // adapter = new LoadMoreWrapper(commentAdapter);
        lisCommonAdapter=new CommonAdapter<CommentBean>(mActivity, R.layout.item_comment_shopdetail, datas) {
            @Override
            protected void convert(ViewHolder viewHolder, CommentBean item, int position) {

            }
        };
        swipeTarget.setAdapter(lisCommonAdapter);
        getData();

    }

    private void getData() {
        HttpManager.getInstance().getShopComments(goodId, page, new HttpSubscriber<List<CommentBean>>(new OnResultCallBack<List<CommentBean>>() {

            @Override
            public void onSuccess(List<CommentBean> commentBean) {
               // swipeToLoadLayout.setRefreshing(false);
                if (commentBean != null && commentBean.size() > 0) {
                    if (page == 1) {
                        datas.clear();


                    }
                    datas.addAll(commentBean);
                } else {

                    if (page > 1) {

                        ToastUtil.show("没有更多数据了");
                    }
                }
                lisCommonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String errorMsg) {
              //  swipeToLoadLayout.setRefreshing(false);
                ToastUtil.show(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));


    }


    @Override
    public void onLoadMoreRequested() {
        page++;
        getData();

    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }
}
