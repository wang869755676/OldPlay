/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.td.oldplay.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * toast管理器, 确保只有一个Toast对象
 */
@SuppressLint("ShowToast")
public class ToastUtil {

    private static Toast it;
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    private ToastUtil() {
    }

    /**
     * 在程序初始化的时候调用, 只需调用一次
     */
    public static void init(Context _context) {
        mContext = _context;
        View v = Toast.makeText(mContext, "", Toast.LENGTH_SHORT).getView();
        init(mContext, v);
    }

    /**
     * 在程序初始化的时候调用, 只需调用一次
     */
    public static void init(Context _context, View view) {
        it = new Toast(_context);
        it.setView(view);
    }

    /**
     * 设置Toast背景
     */
    public static void setBackgroundView(View view) {
        it.setView(view);
    }

    public static void show(CharSequence text, int duration) {

        it.setText(text);
        it.setDuration(duration);
        it.show();
    }

    public static void show(int resid, int duration) {

        it.setText(resid);
        it.setDuration(duration);
        it.show();
    }

    public static void show(CharSequence text) {
        show(text, Toast.LENGTH_LONG);
    }

    public static void show(int resid) {
        show(resid, Toast.LENGTH_SHORT);
    }




}
