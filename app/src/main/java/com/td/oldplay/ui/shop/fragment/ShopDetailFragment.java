package com.td.oldplay.ui.shop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopDetailFragment extends BaseFragment {


    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    Unbinder unbinder;
    private List<String > datas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view) {
        datas=new ArrayList<>();
        datas.add("http://pic.58pic.com/58pic/14/84/31/25C58PICBQi_1024.jpg");
        swipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        swipeTarget.setAdapter(new CommonAdapter<String>(mActivity,R.layout.item_shop_pic,datas) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                GlideUtils.setImage(mActivity,s, (ImageView) holder.getView(R.id.item_shop_de_pic));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
