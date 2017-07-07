/**
 * PushSharedPreferences
 * @author jwh
 * SharedPerferences工具类
 * 2015.3.3
 */
package com.td.oldplay.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PushSharedPreferences {

	private Context context;
	private String fileName;

	public PushSharedPreferences(Context context, String fileName) {
		this.context = context;
		this.fileName = fileName;
	}

	/**
	 * 存储数据到SharedPreferences
	 * 
	 * @param keys
	 *            存储的键值数组
	 * @param values
	 *            存储的具体值数组
	 */
	public void saveStringValuesToSharedPreferences(String[] keys,
			String[] values) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		int size = keys.length;
		for (int i = 0; i < size; i++) {
			editor.putString(keys[i], values[i]);
		}
		editor.commit();
	}

	/**
	 * 从SharedPreferences中读取数据
	 * 
	 * @param keys
	 *            存储的键值数组
	 * 
	 */
	public String[] getStringValuesByKeys(String[] keys) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		int size = keys.length;
		String[] values = new String[size];
		for (int i = 0; i < size; i++) {
			values[i] = sp.getString(keys[i], "");
		}
		return values;
	}

	/**
	 * 存储数据到SharedPreferences
	 * 
	 * @param key
	 *            存储的键值
	 * @param value
	 *            存储的具体值
	 */
	public void saveIntValuesToSharedPreferences(String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 从SharedPreferences中读取数据
	 * 
	 * @param key
	 *            存储的键值
	 * 
	 */
	public int getIntValuesByKeys(String key) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		int value = sp.getInt(key, 0);
		return value;
	}

	/**
	 * 清除SharedPreferences的内容
	 */
	public void clearSharedPreferences() {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();

	}

	public void resetSharedPreferences(String key) {
		SharedPreferences sp = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, 0);
		editor.commit();
	}
}
