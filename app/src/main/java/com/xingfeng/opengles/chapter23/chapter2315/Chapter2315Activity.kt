package com.xingfeng.opengles.chapter23.chapter2315

import android.os.Bundle
import android.util.Log
import com.xingfeng.opengles.GLRenderActivity
import com.xingfeng.opengles.R
import com.xingfeng.opengles.util.Constant

open class Chapter2315Activity : GLRenderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_chapter221)
        //初始化GLSurfaceView

        Constant.OBJ_VER_PATH = "chapter301/chapter301.15/vertex_brazier.glsl"
        Constant.OBJ_FRAG_PATH = "chapter301/chapter301.15/frag_brazier.glsl"

        setGLSurfaceView(GL2315SurfaceView(this@Chapter2315Activity))
    }
}