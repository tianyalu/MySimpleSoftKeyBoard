package com.sty.learn.utils;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by shity on 2017/6/13/0013.
 */

public class Utils {
    /**
     * 得到设备屏幕的宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context){
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 把密度转换为像素
     * @param context
     * @param px
     * @return
     */
    public static int dip2px(Context context, float px){
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }

    /**
     * 判断当前屏幕是否为竖屏
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true，否则返回false
     */
    public static boolean isScreenOriatationPortrait(Context context){
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
