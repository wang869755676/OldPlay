package com.td.oldplay.ui.displayphoto;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.td.oldplay.R;
import com.td.oldplay.base.BaseFragmentActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayPhotoActivity extends BaseFragmentActivity {
    public static final String INTENT_KEY_URLS = "urls";
    public static final String INTENT_KEY_POSITION = "position";

    @BindView(R.id.photo_pager)
    ViewPager mPhotoPager;
    @BindView(R.id.indicator)
    TextView mIndicator;

    private List<String> mUrls;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis_play);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            mUrls = getIntent().getStringArrayListExtra(INTENT_KEY_URLS);
            mCurrentPosition = getIntent().getIntExtra(INTENT_KEY_POSITION, 0);
        }

        init();
    }

    private void init() {
        PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), mUrls);
        mPhotoPager.setAdapter(photoPagerAdapter);

        mPhotoPager.setCurrentItem(mCurrentPosition);

        CharSequence text = getString(R.string.viewpager_indicator, mCurrentPosition + 1, mPhotoPager.getAdapter().getCount());
        mIndicator.setText(text);
        mPhotoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CharSequence text = getString(R.string.viewpager_indicator, position + 1, mPhotoPager.getAdapter().getCount());
                mIndicator.setText(text);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
