package com.td.oldplay.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 为保持登录token状态建立文件工具
 * 
 * @author John
 * 
 */
public class SharePreferenceUtil {
	private Context mcontext;
	private String fileName;

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	@SuppressLint("CommitPrefEdits")
	public SharePreferenceUtil(Context context, String file) {
		mcontext = context;
		fileName = file;
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	// 记录用户是否第一打开应用程序
	public boolean isFisrt(){
		return sp.getBoolean("isFirst",true);
	}
	public void setIsFirst(boolean isFirst){
		editor.putBoolean("isFirst", isFirst);
		editor.commit();
	}

	/**
	 * 
	 * @param token
	 */
	public void setToken(String token) {
		editor.putString("token", token);
		editor.commit();
	}

	public String getToken() {
		return sp.getString("token", "");
	}

	/**
	 * en-英语
	 * zh-中文
	 *
	 * 1-英文
	 * 2-中文
	 * @param language
     */
	public void setLanguage(int language) {
		editor.putInt("language", language);
		editor.commit();
	}

	public int getLanguage() {
		return sp.getInt("language", 2);
	}

	/**
	 *
	 * @param userId
	 */
	public void setUserId(int userId) {
		editor.putInt("userId", userId);
		editor.commit();
	}

	public int getUserId() {
		return sp.getInt("userId", 0);
	}

	/**
	 * 国际化姓名：en-名+姓
	 * zh-姓+名
	 * @param tukname
     */
	public void setTukname(String tukname) {
		editor.putString("tukname", tukname);
		editor.commit();
	}

	public String getTukname() {
		return sp.getString("tukname", "");
	}


	/**
	 * 1-游客模式；2-出租模式
	 * @param modle
     */
	public void setTukModle(int modle) {
		editor.putInt("tukmodle", modle);
		editor.commit();
	}

	public int getTukModle() {
		return sp.getInt("tukmodle", 1);
	}

	/**
	 * 
	 * @param nickname
	 */
	public void setNickname(String nickname) {
		editor.putString("nickname", nickname);
		editor.commit();
	}

	public String getNickname() {
		return sp.getString("nickname", "");
	}


	/**
	 * first login app
	 * @return
     */
	public int getFirstLogin() {
		return sp.getInt("FirstLogin", 0);
	}

	public void setFirstLogin(int first) {
		editor.putInt("FirstLogin", first);
		editor.commit();
	}

	//first_name  last_name registerDate lastvisitDate  mobile	language_name	sex		avatar_thumb200



	/********************************* Login Data **************************************/
	/**
	 * 
	 */
	public String getUserName() {
		return sp.getString("username", "");
	}

	public void setUserName(String username) {
		editor.putString("username", username);
		editor.commit();
	}

	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		editor.putString("password", password);
		editor.commit();
	}

	public String getPassword() {
		return sp.getString("password", "");
	}

	/********************************* shopping Car **************************************/
	/**
	 * 购物车数量
	 * 
	 * @param number
	 */
	public void setShoppingCarNum(int number) {
		editor.putInt("ShopCarNumber", number);
		editor.commit();
	}

	public int getShoppingCarNum() {
		return sp.getInt("ShopCarNumber", 0);
	}

	/**
	 * 清除SharedPreferences的内容
	 */
	public void clearSharedPreferences() {
		SharedPreferences sp = mcontext.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
}
