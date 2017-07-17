package com.td.oldplay.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.td.oldplay.bean.UserBean;

/**
 * 为保持登录token状态建立文件工具
 *
 * @author John
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

    public void setIsLogin(boolean f) {
        editor.putBoolean("login", f);
        editor.commit();
    }

    public boolean getIsLogin() {
       return sp.getBoolean("login", false);
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        editor.putString("userId", userId);
        editor.commit();
    }

    public String getUserId() {
        return sp.getString("userId", "-");
    }

    public void setUser(UserBean user) {
        Gson gson = new Gson();
        editor.putString("userInfo", gson.toJson(user));
        editor.commit();
    }

    public UserBean getUser() {
        Gson gson = new Gson();
        String json = sp.getString("userInfo", "");
        if (json != null && !json.equals("")) {
            return gson.fromJson(json, UserBean.class);
        }
        return null;

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
