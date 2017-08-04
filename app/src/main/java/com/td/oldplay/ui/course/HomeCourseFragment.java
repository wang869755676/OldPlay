package com.td.oldplay.ui.course;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.td.oldplay.bean.BannerInfo;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.bean.HomeCourseInfo;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.SearchActivity;
import com.td.oldplay.ui.course.activity.CourseListActivity;
import com.td.oldplay.ui.course.activity.TeacherDetailActivity;
import com.td.oldplay.ui.course.activity.TeacherListActivity;
import com.td.oldplay.ui.course.adapter.CoureseTypeAdapter;
import com.td.oldplay.ui.window.SeachPopupWindow;
import com.td.oldplay.utils.GlideUtils;
import com.td.oldplay.utils.ToastUtil;
import com.td.oldplay.widget.CustPagerTransformer;
import com.td.oldplay.widget.CustomTitlebarLayout;
import com.td.oldplay.widget.banner.MZBannerView;
import com.td.oldplay.widget.banner.holder.MZHolderCreator;
import com.td.oldplay.widget.banner.holder.MZViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeCourseFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


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
    @BindView(R.id.more_type_btn)
    TextView moreTypeBtn;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    private List<BannerInfo> banners = new ArrayList<>();
    private List<CourseTypeBean> typeBeens = new ArrayList<>();
    private List<CourseTypeBean> hotBeens = new ArrayList<>();
    private List<CourseTypeBean> recomendBeens = new ArrayList<>();

    private Adapter recommendAdapter;
    private Adapter hotAdapter;
    private CoureseTypeAdapter typeAdapter;

    private SeachPopupWindow popupWindow;

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
        title.setLeftGone();
        // title.setLeftImageResource(R.mipmap.icon_updown);
        title.setOnRightListener(this);
        title.setOnLeftListener(this);
        swipeLayout.setOnRefreshListener(this);
        homrCoureseBanner.setIndicatorVisible(false);
        homrCoureseBanner.getViewPager().setPageTransformer(true, new CustPagerTransformer(mActivity));
        homrCoureseBanner.getViewPager().setPageMargin(50);
        homrCoureseBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Intent intent = new Intent(mActivity, TeacherDetailActivity.class);
                intent.putExtra("id", banners.get(position).userId);
                startActivity(intent);
            }
        });
        homeCoureRecommend.setLayoutManager(new GridLayoutManager(mActivity, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        homeCoureHot.setLayoutManager(new GridLayoutManager(mActivity, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        homeCouresType.setLayoutManager(new GridLayoutManager(mActivity, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        hotAdapter = new Adapter(mActivity, R.layout.item_home_course, hotBeens);
        recommendAdapter = new Adapter(mActivity, R.layout.item_home_course, recomendBeens);
        typeAdapter = new CoureseTypeAdapter(mActivity, R.layout.item_course_type, typeBeens);
        homeCouresType.setAdapter(typeAdapter);
        homeCoureHot.setAdapter(hotAdapter);
        homeCoureRecommend.setAdapter(recommendAdapter);
        typeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, CourseListActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("title", typeBeens.get(position).name + "类课程");
                intent.putExtra("id", typeBeens.get(position).id);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        hotAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, TeacherListActivity.class);
                intent.putExtra("courseId", hotBeens.get(position).id);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recommendAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, TeacherListActivity.class);
                intent.putExtra("courseId", hotBeens.get(position).id);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        homeMoreHot.setOnClickListener(this);
        homeMoreRecoment.setOnClickListener(this);
        moreTypeBtn.setOnClickListener(this);

        getData();
    }

    private void getData() {
        showLoading();
        HttpManager.getInstance().getHomeCourse(new HttpSubscriber<HomeCourseInfo>(new OnResultCallBack<HomeCourseInfo>() {
            @Override
            public void onSuccess(HomeCourseInfo homeCourseInfo) {
                hideLoading();
                swipeLayout.setRefreshing(false);
                if (homeCourseInfo != null) {
                    if (homeCourseInfo.coursesBannerList != null && homeCourseInfo.coursesBannerList.size() > 0) {
                        banners = homeCourseInfo.coursesBannerList;
                        homrCoureseBanner.setPages(homeCourseInfo.coursesBannerList, new MZHolderCreator<BannerViewHolder>() {
                            @Override
                            public BannerViewHolder createViewHolder() {
                                return new BannerViewHolder();
                            }
                        });
                    }

                    if (homeCourseInfo.coursesTypeList != null) {
                        typeBeens.addAll(homeCourseInfo.coursesTypeList);
                        typeAdapter.notifyDataSetChanged();
                    }

                    if (homeCourseInfo.commendCourseList != null) {
                        recomendBeens.addAll(homeCourseInfo.commendCourseList);
                        recommendAdapter.notifyDataSetChanged();
                    }

                    if (homeCourseInfo.hotCoursesList != null) {
                        hotBeens.addAll(homeCourseInfo.hotCoursesList);
                        hotAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                hideLoading();
                swipeLayout.setRefreshing(false);
                ToastUtil.show(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }
        }));
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
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
            case R.id.home_more_recoment:
                intent = new Intent(mActivity, CourseListActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.more_type_btn:
                intent = new Intent(mActivity, CourseListActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("id", "");
                startActivity(intent);
                break;
            case R.id.left_text:
                break;
            case R.id.right_text:
                if (popupWindow == null) {
                    popupWindow = new SeachPopupWindow(mActivity, 1);
                }
                popupWindow.showPopup(v);
                /*intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra("type", 0);
                // intent.putExtra()
                startActivity(intent);*/
                break;

        }
    }

    @Override
    public void onRefresh() {
        hotBeens.clear();
        recomendBeens.clear();
        typeBeens.clear();
        getData();
    }

    public static class BannerViewHolder implements MZViewHolder<BannerInfo> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, BannerInfo data) {
            if (data != null) {
                GlideUtils.setImage(context, data.picUrl, mImageView);
            }

        }
    }

    private static class Adapter extends CommonAdapter<CourseTypeBean> {
        public Adapter(Context context, int layoutId, List<CourseTypeBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, CourseTypeBean homeCourseBean, int position) {
            if (homeCourseBean != null) {
                GlideUtils.setImage(mContext, homeCourseBean.picUrl, (ImageView) holder.getView(R.id.item_home_iv));
                holder.setText(R.id.item_courese_name, homeCourseBean.name);
            }

        }

    }

}
