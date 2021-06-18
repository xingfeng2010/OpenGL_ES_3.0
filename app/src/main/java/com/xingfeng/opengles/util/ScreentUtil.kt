package com.xingfeng.opengles.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

object ScreentUtil {
    fun getScreenWidth(context: Activity):Int {
        var dms = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dms)
        return dms.widthPixels
    }

    fun getScreenHeight(context: Activity):Int {
        var dms = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dms)
        return dms.heightPixels
    }
}