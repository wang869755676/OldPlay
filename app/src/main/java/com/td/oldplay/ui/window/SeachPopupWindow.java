package com.td.oldplay.ui.window;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.adapter.recyclerview.MultiItemTypeAdapter;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.bean.SearchCourse;
import com.td.oldplay.bean.ShopBean;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.activity.TeacherListActivity;
import com.td.oldplay.ui.course.adapter.CourserListAdapter;
import com.td.oldplay.ui.course.adapter.ShopAdapter;
import com.td.oldplay.ui.shop.activity.ShopDetailActivity;
import com.td.oldplay.utils.ShareSDKUtils;
import com.td.oldplay.utils.ToastUtil;
import com.tencent.mm.opensdk.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.reactivex.disposables.Disposable;

/**
 * Created by my on 2017/7/14.
 */

public class SeachPopupWindow extends PopupWindow implements View.OnClickListener {

    private Context context;

    private EditText searchEdite;

    private TextView searchCancle;
    private RecyclerView seachRecycler;

    private LinearLayout root;
    private int type;  // 0 搜索课程  1 搜索商品
    private ShopAdapter shopAdapter;
    private List<ShopBean> shopBeens = new ArrayList<>();

    private CourserListAdapter courserAdapter;
    private List<TeacherBean> teacherBeens = new ArrayList<>();
    private List<CourseTypeBean> courseTypeBeens = new ArrayList<>();

    private int sortType = 0; //排序的类型,0:价格,1:积分,2:销量
    private int sort = 0; //排序方式,0:降序,1:升序
    private boolean priceDec = true;
    private boolean scoreSell = true;
    private boolean sellDec = true;
    private int goodTypeId;

    private HashMap<String, Object> params = new HashMap<>();
    private Intent intent;

    public SeachPopupWindow(Context context, int type) {
        super(context);
        this.type = type;
        this.context = context;
        init();
    }

    public SeachPopupWindow(Context context, int type, int goodTypeIds) {
        super(context);
        this.type = type;
        this.goodTypeId = goodTypeIds;
        this.context = context;
        params.put("goodTypeId", goodTypeId);
        params.put("type", sortType);
        params.put("sort", sort);
        init();


    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_search, null);
        setContentView(view);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        setOutsideTouchable(true);
        setFocusable(true);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        searchEdite = (EditText) view.findViewById(R.id.search_edite);
        seachRecycler = (RecyclerView) view.findViewById(R.id.seach_recycler);
        root = (LinearLayout) view.findViewById(R.id.pop_seach_root);
        root.setOnClickListener(this);

        view.findViewById(R.id.search_cancle).setOnClickListener(this);
        seachRecycler.setLayoutManager(new LinearLayoutManager(context));
        if (type == 0) {
            courserAdapter = new CourserListAdapter(context, R.layout.item_my_teacher_list, courseTypeBeens);
            seachRecycler.setAdapter(courserAdapter);
            courserAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    intent = new Intent(context, TeacherListActivity.class);
                    intent.putExtra("courseId", courseTypeBeens.get(position).id);
                    context.startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        } else if (type == 1) {
            shopAdapter = new ShopAdapter(context, R.layout.item_shop, shopBeens);
            seachRecycler.setAdapter(shopAdapter);
            shopAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    intent = new Intent(context, ShopDetailActivity.class);
                    intent.putExtra("id", shopBeens.get(position).goodsId);
                    context.startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }


        searchEdite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("===",s+"-------------------------");
                if (s == null || "".equals(s)) {
                    seachRecycler.setVisibility(View.GONE);
                } else {
                    search(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //搜索数据
    private void search(String s) {
        if (type == 0) {
            HttpManager.getInstance().searchCommentsTeachers(s, 1, new HttpSubscriber<List<CourseTypeBean>>(new OnResultCallBack<List<CourseTypeBean>>() {
                @Override
                public void onSuccess(List<CourseTypeBean> courseTypeBeen) {
                    if (courseTypeBeen != null) {
                        courseTypeBeens.clear();
                        courseTypeBeens.addAll(courseTypeBeen);
                        courserAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onError(int code, String errorMsg) {

                }

                @Override
                public void onSubscribe(Disposable d) {

                }
            }));
        } else if (type == 1) {
            params.put("name", s);
            HttpManager.getInstance().searchShop(params, new HttpSubscriber<List<ShopBean>>(new OnResultCallBack<List<ShopBean>>() {
                @Override
                public void onSuccess(List<ShopBean> shopBeen) {
                    if (shopBeen != null) {
                        shopBeens.clear();
                        shopBeens.addAll(shopBeen);
                        shopAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onError(int code, String errorMsg) {

                }

                @Override
                public void onSubscribe(Disposable d) {

                }
            }));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_seach_root:
                dismiss();
                break;
            case R.id.search_cancle:
                dismiss();
                break;

        }
    }


    public void showPopup(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.TOP, 0, 0);
        } else {
            this.dismiss();
        }
    }


}
