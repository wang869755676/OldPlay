package com.td.oldplay.ui.course.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntruceFragment extends Fragment {


    @BindView(R.id.introduce_name)
    TextView introduceName;
    @BindView(R.id.introduce_des)
    TextView introduceDes;
    @BindView(R.id.intr_te_iv)
    CircleImageView intrTeIv;
    @BindView(R.id.intr_te_name)
    TextView intrTeName;
    @BindView(R.id.item_school)
    TextView itemSchool;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intruce, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
