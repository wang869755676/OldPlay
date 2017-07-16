package com.td.oldplay.base;

import android.app.Activity;

import android.widget.ImageView;


import com.td.oldplay.utils.GlideUtils;

import me.zuichu.picker.loader.ImageLoader;


public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        GlideUtils.setPhotoImage(activity,path,imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
