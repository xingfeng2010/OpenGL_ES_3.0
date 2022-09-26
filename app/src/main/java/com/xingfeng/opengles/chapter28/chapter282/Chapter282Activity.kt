package com.xingfeng.opengles.chapter28.chapter282

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


open class Chapter282Activity : AppCompatActivity() {

    init {
        System.loadLibrary("anative");
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        val mGlSurfaceView = GLSurfaceView(this)
        setContentView(mGlSurfaceView)
        //设置版本
        mGlSurfaceView.setEGLContextClientVersion(2)
        val renderer: GLSurfaceView.Renderer = TriangleRenderJNI(this@Chapter282Activity)
        mGlSurfaceView.setRenderer(renderer)
        //只有在绘制数据改变时才会绘制View,可以防止GLSurfaceView帧重绘
        //该种模式下当需要重绘时需要我们手动调用glSurfaceView.requestRender();
        mGlSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }
}



