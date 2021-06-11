package com.xingfeng.opengles.chapter23.chapter233

import android.os.Bundle

import com.xingfeng.opengles.GLRenderActivity
import com.xingfeng.opengles.util.Constant

 open class Chapter233Activity: GLRenderActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         //初始化GLSurfaceView
         super.setGLSurfaceView(GL233SurfaceView(this))
         Constant.UNIT_SIZE=1f
     }
 }