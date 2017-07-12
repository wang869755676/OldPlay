package com.td.oldplay.ui.course;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragment;
import com.td.oldplay.base.adapter.recyclerview.CommonAdapter;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.base.adapter.recyclerview.base.ViewHolder;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.ui.course.activity.CourseListActivity;
import com.td.oldplay.ui.course.activity.TeacherListActivity;
import com.td.oldplay.ui.course.adapter.CoureseTypeAdapter;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.widget.CustPagerTransformer;
import com.td.oldplay.widget.CustomTitlebarLayout;
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
public class HomeCourseFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.homr_courese_banner)
    MZBannerView homrCoureseBanner;
    @BindView(R.id.home_coures_type)
    RecyclerView homeCouresType;
    @BindView(R.id.more_recomment_btn)
    TextView moreRecommentBtn;
    @BindView(R.id.home_more_recoment)
    RelativeLayout homeMoreRecoment;
    @BindView(R.id.home_coure_recommend)
    RecyclerView homeCoureRecommend;
    @BindView(R.id.more_hot_btn)
    TextView moreHotBtn;
    @BindView(R.id.home_more_hot)
    RelativeLayout homeMoreHot;
    @BindView(R.id.home_coure_hot)
    RecyclerView homeCoureHot;
    Unbinder unbinder;
    @BindView(R.id.title)
    CustomTitlebarLayout title;

    private List<String> banners;
    private List<CourseBean> datas;
    private List<CourseTypeBean> typeBeens = new ArrayList<>();
    private Adapter recommendAdapter;
    private Adapter hotAdapter;
    private CoureseTypeAdapter typeAdapter;

    public HomeCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view) {
        title.setTitle("课程");
        title.setRightImageResource(R.mipmap.icon_searc_home);
        title.setLeftImageResource(R.mipmap.icon_updown);
        title.setOnRightListener(this);
        title.setOnLeftListener(this);
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
        homrCoureseBanner.getViewPager().setPageTransformer(true, new CustPagerTransformer(mActivity));
        homrCoureseBanner.getViewPager().setPageMargin(50);

        homeCoureRecommend.setLayoutManager(new GridLayoutManager(mActivity, 2));
        homeCoureHot.setLayoutManager(new GridLayoutManager(mActivity, 2));
        homeCouresType.setLayoutManager(new GridLayoutManager(mActivity, 4));

        datas = new ArrayList<>();
        datas.add(new CourseBean());
        datas.add(new CourseBean());

        typeBeens.add(new CourseTypeBean());
        typeBeens.add(new CourseTypeBean());

        hotAdapter = new Adapter(mActivity, R.layout.item_home_course, datas);
        recommendAdapter = new Adapter(mActivity, R.layout.item_home_course, datas);
        typeAdapter = new CoureseTypeAdapter(mActivity, R.layout.item_course_type, typeBeens);
        homeCouresType.setAdapter(typeAdapter);
        homeCoureHot.setAdapter(hotAdapter);
        homeCoureRecommend.setAdapter(recommendAdapter);
        typeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                  startActivity(new Intent(mActivity,CourseListActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        hotAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(mActivity,TeacherListActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recommendAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(mActivity,TeacherListActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        homeMoreHot.setOnClickListener(this);
        homeMoreRecoment.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        homrCoureseBanner.start();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.home_more_hot:
                intent = new Intent(mActivity, CourseListActivity.class);
                // intent.putExtra()
                startActivity(intent);
                break;
            case R.id.home_more_recoment:
                intent = new Intent(mActivity, CourseListActivity.class);
                // intent.putExtra()
                startActivity(intent);
                break;
            case R.id.left_text:
                break;
            case R.id.right_text:
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

    private static class Adapter extends CommonAdapter<CourseBean> {
        public Adapter(Context context, int layoutId, List<CourseBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, CourseBean homeCourseBean, int position) {
            GlideUtils.setImage(mContext, "http://pic.58pic.com/58pic/13/85/85/73T58PIC9aj_1024.jpg", (ImageView) holder.getView(R.id.item_home_iv));
        }
    }

}
