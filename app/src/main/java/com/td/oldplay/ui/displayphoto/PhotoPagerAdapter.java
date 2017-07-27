package com.td.oldplay.ui.displayphoto;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by snowbean on 16-6-9.
 */
public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mUrls;

    public PhotoPagerAdapter(FragmentManager fm, List<String> urls) {
        super(fm);
        mUrls = urls;
    }

    @Override
    public Fragment getItem(int position) {
        return PhotoDetailFragment.newInstance(mUrls.get(position));
    }

    @Override
    public int getCount() {
        return mUrls == null ? 0 : mUrls.size();
    }
}
