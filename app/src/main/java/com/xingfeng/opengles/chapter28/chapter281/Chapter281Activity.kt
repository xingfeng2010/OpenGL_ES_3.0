package com.xingfeng.opengles.chapter28.chapter281;

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


open class Chapter281Activity : AppCompatActivity() {
    var mView: GL2JNIView? = null

    companion object {
        //屏幕对应的宽度和高度
        var WIDTH = 0f
        var HEIGHT = 0f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE) //去掉标题
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        ) //去掉标头
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE //强制横屏
        //获得系统的宽度以及高度
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        if (dm.widthPixels > dm.heightPixels) {
            WIDTH = dm.widthPixels.toFloat()
            HEIGHT = dm.heightPixels.toFloat()
        } else {
            WIDTH = dm.heightPixels.toFloat()
            HEIGHT = dm.widthPixels.toFloat()
        }
        mView = GL2JNIView(this)
        mView!!.requestFocus() //获取焦点
        mView!!.setFocusableInTouchMode(true) //设置为可触控
        setContentView(mView)
        //JNIPort.nativeSetAssetManager(this.assets) //初始化
    }

    override fun onPause() {
        super.onPause()
        mView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        mView!!.onResume()
    }
}



