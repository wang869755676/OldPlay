package com.td.oldplay.widget.footer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;


public class LoadMoreFooterView extends android.support.v7.widget.AppCompatTextView implements SwipeTrigger, SwipeLoadMoreTrigger {
    public LoadMoreFooterView(Context context) {
        super(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onLoadMore() {
        setText("Loading");
    }

    @Override
    public void onPrepare() {
        setText("");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled <= -getHeight()) {
                setText("Loading");
            } else {
                setText("Loading");
            }
        } else {
            setText("Loading");
        }
    }

    @Override
    public void onRelease() {
        setText("Loading");
    }

    @Override
    public void onComplete() {
        setText("Complete");
    }

    @Override
    public void onReset() {
        setText("");
    }
}
