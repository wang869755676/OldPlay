package com.td.oldplay.ui.shop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.ui.course.adapter.CommentAdapter;
import com.tencent.mm.opensdk.utils.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopCommentFragment extends BaseFragment implements LoadMoreWrapper.OnLoadMoreListener {


    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    Unbinder unbinder;
    private CommentAdapter commentAdapter;
    private LoadMoreWrapper adapter;
    private List<CommentBean> datas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        datas.add(new CommentBean());
        Log.e("===", datas.size() + "---------------------");
        swipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        commentAdapter = new CommentAdapter(mActivity, R.layout.item_comment, datas);
        adapter = new LoadMoreWrapper(commentAdapter);
        swipeTarget.setAdapter(adapter);

    }

    @Override
    public void onLoadMoreRequested() {

    }
}
