package com.td.oldplay.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;


public class PrefUtils {
    private static final String PREF_NAME = "oumen";

    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    public static synchronized void init(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static void putBoolean(Context ctx, String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public static void putString(Context ctx, String key, String value) {
        editor.putString(key, value).commit();
    }

    public static String getString(Context ctx, String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public static void putInteger(Context ctx, String key, Integer value) {
        editor.putInt(key, value).commit();
    }

    public static Integer getInteger(Context ctx, String key, Integer defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public static boolean contains(Context ctx, String key) {
        return mSharedPreferences.contains(key);
    }

    /**
     * 保存集合
     */
    public static <T> void putList(Context ctx, String key, List<T> list) {
        String liststr = null;
        try {
            liststr = SceneList2String(list);
        } catch (Exception e) {
            //Log.d(TAG, "往SharedPreferences保存集合报错了", e);
            e.printStackTrace();
        }
        editor.putString(key, liststr).commit();
    }

    /**
     * 得到集合
     */
    public static <T> List<T> getList(Context ctx, String key, String defValue) {
        String liststr = mSharedPreferences.getString(key, defValue);
        List<T> string2SceneList = String2SceneList(liststr);
        return string2SceneList;
    }

    /**
     * List转换为String
     *
     * @param SceneList
     * @return
     * @throws IOException
     */
    private static String SceneList2String(List SceneList) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        String SceneListString = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(SceneList);
            // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
            SceneListString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            //Log.d(TAG, "List转换为String报异常了...", e);
        } finally {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                //Log.d(TAG, "objectInputStream流关闭报异常了...", e);
            }
        }
        return SceneListString;
    }

    /**
     * 将String转换为List
     *
     * @param <T>
     * @param SceneListString
     * @return
     * @throws StreamCorruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static <T> List<T> String2SceneList(String SceneListString) {
        if (SceneListString == null) {
            return null;
        }
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = null;
        List<T> SceneList = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            SceneList = (List<T>) objectInputStream.readObject();
        } catch (Exception e) {
            //Log.d(TAG, "String转换为List报异常了...", e);
            e.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
            } catch (IOException e) {
                //Log.d(TAG, "objectInputStream流关闭报异常了...", e);
                e.printStackTrace();
            }
        }
        return SceneList;
    }
}