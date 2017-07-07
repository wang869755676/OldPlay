package com.td.oldplay.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class ImageLoaderCacheUtils {
	public final static String CLOSE_NOTICE_GROUP_FILE = "imageloader_cache_record_file";
	private Context context;

	public ImageLoaderCacheUtils(Context context) {
		this.context = context;
	}
	
	/**
	 * 
	 * @param groupId
	 */
	@SuppressLint("NewApi")
	public void saveImageUrlToSharedPreferences(String imageUrl) {
		if(hasOverCache(imageUrl)){
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(CLOSE_NOTICE_GROUP_FILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		Set<String> set = new HashSet<String>();
		set = sp.getStringSet("imageUrl_cache", null);
		if(set!=null){
			if(set.size()>100){
				set.clear();
				set.add(imageUrl);
			}else{
				set.add(imageUrl);
			}
		}else{
			set = new HashSet<String>();
			set.add(imageUrl);
		}
		editor.putStringSet("imageUrl_cache", set);
		editor.commit();
	}

	/**
	 * 从SharedPreferences中读取数据
	 * 
	 * @param keys
	 *            存储的键值数组
	 * 
	 */
	@SuppressLint("NewApi")
	public boolean hasOverCache(String imageUrl) {
		SharedPreferences sp = context.getSharedPreferences(CLOSE_NOTICE_GROUP_FILE, Context.MODE_PRIVATE);
		Set<String> set = new HashSet<String>();
		set = sp.getStringSet("imageUrl_cache", null);
		if(set!=null){
			if(set.contains(imageUrl)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	/**
	 * remove
	 * 
	 */
	@SuppressLint("NewApi")
	public void removeCacheToSharedPreferences(String imageUrl) {
		SharedPreferences sp = context.getSharedPreferences(CLOSE_NOTICE_GROUP_FILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		Set<String> set = new HashSet<String>();
		try {
			set = sp.getStringSet("imageUrl_cache", null);
			if(set!=null){
				set.remove(imageUrl);
			}
			editor.putStringSet("imageUrl_cache", set);
			editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 清除SharedPreferences的内容
	 */
	public void clearSharedPreferences() {
		SharedPreferences sp = context.getSharedPreferences(CLOSE_NOTICE_GROUP_FILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
}
