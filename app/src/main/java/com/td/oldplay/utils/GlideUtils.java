package com.td.oldplay.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.td.oldplay.R;


import java.io.File;

/**
 * Created by my on 2017/7/6.
 */

public class GlideUtils {
  /*  private static RequestOptions defaultOptions = new RequestOptions().placeholder(R.color.gray).dontAnimate();
    private static RequestOptions options = new RequestOptions().dontAnimate();*/

    public static void setImage(Context context, String url, ImageView imageView) {
       // defaultOptions.placeholder(R.color.gray).error(R.color.gray);
        Glide
                .with(context)
                .load(url)
                .placeholder(R.color.gray).error(R.color.gray).dontAnimate()
                .into(imageView);


    }

    public static void setAvatorImage(Context context, String url, ImageView imageView) {
        //defaultOptions.placeholder(R.mipmap.icon_avator_default).error(R.mipmap.icon_avator_default);
        Glide
                .with(context)
                .load(url)
                .placeholder(R.mipmap.icon_avator_default).error(R.mipmap.icon_avator_default)
                .into(imageView);


    }

    public static void setAvatorLoadImage(Context context, String path, ImageView imageView) {
       // defaultOptions.placeholder(R.mipmap.icon_avator_default).error(R.mipmap.icon_avator_default);
        Glide
                .with(context)
                .load(path)
                .placeholder(R.mipmap.icon_avator_default).error(R.mipmap.icon_avator_default)
                .into(imageView);


    }

    public static void setPhotoImage(Context context, String url, ImageView imageView) {

       /* defaultOptions.error(R.mipmap.default_image)//设置错误图片
                .placeholder(R.mipmap.default_image)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false);*/
        Glide.with(context)                             //配置上下文
                .load(Uri.fromFile(new File(url)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .error(R.mipmap.default_image)//设置错误图片
                .placeholder(R.mipmap.default_image)     //设置占位图片
                .into(imageView);


    }
    // 效果glid请求

    public static void destroyRequest(Context context) {
        Glide.with(context)
                .pauseRequests();

    }
}
