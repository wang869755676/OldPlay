package com.td.oldplay.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.oldplay.R;


public class CustomTitlebarLayout extends LinearLayout {

    private View mTitleBarLayout;

    private TextView mLeftView;
    private TextView mRightView;
    private TextView mTitleView;
    private Context mContext;

    public CustomTitlebarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTitlebarLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mTitleBarLayout = LayoutInflater.from(mContext).inflate(R.layout.custom_title_bar_layout, this, false);
        mLeftView = (TextView) mTitleBarLayout.findViewById(R.id.left_text);
        mRightView = (TextView) mTitleBarLayout.findViewById(R.id.right_text);
        mTitleView = (TextView) mTitleBarLayout.findViewById(R.id.title_text);
        addView(mTitleBarLayout);
    }

    /**
     * 左边按钮点击监听
     *
     * @param onClickListener
     */
    public void setOnLeftListener(OnClickListener onClickListener) {
        if (onClickListener == null) {
            return;
        }

        mLeftView.setOnClickListener(onClickListener);
    }

    /**
     * 监听右边按钮点击
     *
     * @param onClickListener
     */
    public void setOnRightListener(OnClickListener onClickListener) {
        if (onClickListener == null) {
            return;
        }
        mRightView.setOnClickListener(onClickListener);
    }

    public void setTitle(int title) {
        mTitleView.setText(title);
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    public void setTitleImageResource(int resource) {
        mTitleView.setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
    }

    public void setTitleBarBackgroud(int resource) {
        mTitleBarLayout.setBackgroundColor(resource);
    }

    public void setLeftGone() {
        mLeftView.setVisibility(GONE);
    }


    public void setRightText(String text) {
        mRightView.setText(text);
    }

    public void setRightImageResource(int resource) {
        mRightView.setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
    }

    public void setRightBackGround(int resource) {
        mRightView.setBackgroundResource(resource);
    }

    public void setLeftImageResource(int resourceID) {
        mLeftView.setCompoundDrawablesWithIntrinsicBounds(resourceID, 0, 0, 0);
    }

    public void setRightGone() {
        mRightView.setVisibility(GONE);
    }

}

