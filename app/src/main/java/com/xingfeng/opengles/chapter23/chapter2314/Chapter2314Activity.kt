package com.xingfeng.opengles.chapter23.chapter2314

import android.os.Bundle
import android.util.Log
import com.xingfeng.opengles.GLRenderActivity
import com.xingfeng.opengles.R
import com.xingfeng.opengles.util.Constant

open class Chapter2314Activity : GLRenderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_chapter221)
        //初始化GLSurfaceView

        Constant.OBJ_VER_PATH = "chapter301/chapter301.14/vertex_brazier.glsl"
        Constant.OBJ_FRAG_PATH = "chapter301/chapter301.14/frag_brazier.glsl"

        setGLSurfaceView(GL2314SurfaceView(this@Chapter2314Activity))

        //通过元素初始化一个不可变集合
        var list1 = listOf("aaa", "bbb","ccc","dddd")
        //初始化一个空的不可变集合
        var list2 = emptyList<String>()


        //创建一个带元素的可变集合
        var list5 = mutableListOf("22","33")

        list5.plus("hahha plus")
        list5.add("hahha add")


        for(i in 0 until list5.size) {
            Log.i("DEBUG_TEST", "list1 content ${list5.get(i)}")
        }
    }
}