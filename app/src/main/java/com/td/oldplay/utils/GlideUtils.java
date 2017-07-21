package com.td.oldplay.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.td.oldplay.R;
import com.td.oldplay.http.api.NetWorkAPI;

import java.io.File;

/**
 * Created by my on 2017/7/6.
 */

public class GlideUtils {
    private static RequestOptions defaultOptions = new RequestOptions().placeholder(R.color.gray).dontAnimate();
    private static RequestOptions options = new RequestOptions().dontAnimate();

    public static void setImage(Context context, String url, ImageView imageView) {
        defaultOptions.placeholder(R.color.gray);
        Glide
                .with(context)
                .load(url)
                .apply(defaultOptions)
                .into(imageView);


    }

    public static void setAvatorImage(Context context, String url, ImageView imageView) {
        defaultOptions.placeholder(R.mipmap.icon_avator_default);
        Glide
                .with(context)
                .load(url)
                .apply(defaultOptions)
                .into(imageView);


    }

    public static void setAvatorLoadImage(Context context, String path, ImageView imageView) {
        defaultOptions.placeholder(R.mipmap.icon_avator_default);
        Glide
                .with(context)
                .load(path)
                .apply(defaultOptions)
                .into(imageView);


    }

    public static void setPhotoImage(Context context, String url, ImageView imageView) {

        defaultOptions.error(R.mipmap.default_image)//设置错误图片
                .placeholder(R.mipmap.default_image)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false);
        Glide.with(context)                             //配置上下文
                .load(Uri.fromFile(new File(url)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .apply(defaultOptions)
                .into(imageView);


    }
    // 效果glid请求

    public static void destroyRequest(Context context) {
        Glide.with(context)
                .pauseRequests();

    }
}
