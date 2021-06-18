package com.xingfeng.opengles.chapter23.chapter2314

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem

import com.xingfeng.opengles.GLRenderActivity
import com.xingfeng.opengles.R
import com.xingfeng.opengles.util.Constant

open class Chapter2314Activity : GLRenderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化GLSurfaceView

        setGLSurfaceView(GL2314SurfaceView(this@Chapter2314Activity))
    }
}