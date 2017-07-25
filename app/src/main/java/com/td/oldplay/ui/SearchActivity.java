package com.td.oldplay.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.bean.SearchCourse;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.http.HttpManager;
import com.td.oldplay.http.callback.OnResultCallBack;
import com.td.oldplay.http.subscriber.HttpSubscriber;
import com.td.oldplay.ui.course.adapter.CourserAdapter;
import com.td.oldplay.ui.course.adapter.CourserListAdapter;
import com.td.oldplay.ui.course.adapter.ShopAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseFragmentActivity {

    @BindView(R.id.search_edite)
    EditText searchEdite;
    @BindView(R.id.search_cancle)
    TextView searchCancle;
    @BindView(R.id.seach_recycler)
    RecyclerView seachRecycler;

    private int type;  // 0 搜索课程  1 搜索商城

    private ShopAdapter shopAdapter;
    private CourserListAdapter courserAdapter;
    private SearchCourse searchCourse;
    private List<TeacherBean> teacherBeens = new ArrayList<>();
    private List<CourseTypeBean> courseTypeBeens = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        seachRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        courserAdapter = new CourserListAdapter(mContext, R.layout.item_my_teacher_list, courseTypeBeens);
        seachRecycler.setAdapter(courserAdapter);
        searchEdite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
          /*  HttpManager.getInstance().searchCommentsTeachers(s, new HttpSubscriber<List<CourseTypeBean>>(new OnResultCallBack<List<CourseTypeBean>>() {


                @Override
                public void onSuccess(List<CourseTypeBean> courseTypeBeen) {
                    if (courseTypeBeen != null) {
                        courseTypeBeens.clear();
                        courseTypeBeen.addAll(courseTypeBeen);
                        courserAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onError(int code, String errorMsg) {

                }
            }));*/
        } else if (type == 1) {

        }
    }
}
