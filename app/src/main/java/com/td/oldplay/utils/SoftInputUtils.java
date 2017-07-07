package com.td.oldplay.utils;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

/*
* 类描述：   
* 创建人：Administrator glf  
* 创建时间：2016/9/22 12:37   
*/
public class SoftInputUtils {
    public static void hideSoftInput(Context context, Window window){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if(imm.isActive()){
                imm.hideSoftInputFromWindow(window.getDecorView().getWindowToken(), 0);

            }

        }
    }

    public static void showSoftInput(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
