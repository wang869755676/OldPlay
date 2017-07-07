package com.td.oldplay.ui.shop;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CourseBean;

import com.td.oldplay.bean.ShopBean;
import com.td.oldplay.ui.course.adapter.ShopAdapter;
import com.td.oldplay.ui.shop.activity.ShopListActivity;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.widget.banner.MZBannerView;
import com.td.oldplay.widget.banner.holder.MZHolderCreator;
import com.td.oldplay.widget.banner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeShopFragment extends BaseFragment implements View.OnClickListener{


    @BindView(R.id.homr_courese_banner)
    MZBannerView homrCoureseBanner;
    @BindView(R.id.home_shop_green)
    TextView homeShopGreen;
    @BindView(R.id.home_shop_study)
    TextView homeShopStudy;
    @BindView(R.id.more_recomment_essence)
    Button moreRecommentEssence;
    @BindView(R.id.home_more_essence)
    RelativeLayout homeMoreEssence;
    @BindView(R.id.home_shop_essence)
    RecyclerView homeShopEssence;
    Unbinder unbinder;

    private List<String> banners;
    private List<ShopBean> datas;
    private ShopAdapter shopAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        homrCoureseBanner.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void init(View view) {
        homeShopGreen.setOnClickListener(this);
        homeShopStudy.setOnClickListener(this);
        banners = new ArrayList<>();
        banners.add("http://img2.imgtn.bdimg.com/it/u=49292017,22064401&fm=28&gp=0.jpg");
        banners.add("http://pic.58pic.com/58pic/13/85/85/73T58PIC9aj_1024.jpg");
        banners.add("http://img2.imgtn.bdimg.com/it/u=49292017,22064401&fm=28&gp=0.jpg");
        banners.add("http://pic.58pic.com/58pic/13/85/85/73T58PIC9aj_1024.jpg");

        homrCoureseBanner.setIndicatorVisible(false);
        homrCoureseBanner.setPages(banners, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });


        homeShopEssence.setLayoutManager(new GridLayoutManager(mActivity, 3));
        datas = new ArrayList<>();
        datas.add(new ShopBean());
        datas.add(new ShopBean());
        // shopAdapter=new ShopAdapter(mActivity,R.layout.item_home_course,datas);
        Adapter adapter = new Adapter(mActivity, R.layout.item_home_course, datas);
        homeShopEssence.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(mActivity, ShopListActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.home_shop_green:
                intent=new Intent(mActivity, ShopListActivity.class);
                startActivity(new Intent(mActivity, ShopListActivity.class));
                break;
            case R.id.home_shop_study:
                break;
        }
    }

    public static class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            GlideUtils.setImage(context, data, mImageView);
        }
    }


    private static class Adapter extends CommonAdapter<ShopBean> {
        public Adapter(Context context, int layoutId, List<ShopBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, ShopBean shopBean, int position) {
            GlideUtils.setImage(mContext, "http://pic.58pic.com/58pic/13/85/85/73T58PIC9aj_1024.jpg", (ImageView) holder.getView(R.id.item_home_iv));
        }
    }
}
