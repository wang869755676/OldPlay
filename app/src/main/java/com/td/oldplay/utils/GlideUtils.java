package com.td.oldplay.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.td.oldplay.R;

/**
 * Created by my on 2017/7/6.
 */

public class GlideUtils {
    private static RequestOptions defaultOptions = new RequestOptions().placeholder(R.color.gray).dontAnimate();

    public static void setImage(Context context, String url, ImageView imageView) {
        Glide
                .with(context)
                .load(url)
                .apply(defaultOptions)
                .into(imageView);


    }
}
