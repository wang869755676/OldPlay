package com.td.oldplay.widget.header;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by snowbean on 16-5-28.
 */
public class RefreshHeaderView extends android.support.v7.widget.AppCompatImageView implements SwipeRefreshTrigger,SwipeTrigger {

    private AnimationDrawable mPlayAnim;

    public RefreshHeaderView(Context context) {
        super(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPlayAnim = (AnimationDrawable) getDrawable();
    }

    @Override
    public void onRefresh() {
        mPlayAnim.start();
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {

            } else {

            }
        } else {

        }
    }

    @Override
    public void onRelease() {
        mPlayAnim.stop();
    }

    @Override
    public void onComplete() {
        mPlayAnim.stop();
    }

    @Override
    public void onReset() {
        mPlayAnim.stop();
    }
}
