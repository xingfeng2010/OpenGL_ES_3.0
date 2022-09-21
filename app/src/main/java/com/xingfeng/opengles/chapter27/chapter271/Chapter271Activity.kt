package com.xingfeng.opengles.chapter27.chapter271;

import android.os.Bundle;
import android.view.KeyEvent;

import com.xingfeng.opengles.GLRenderActivity
import com.xingfeng.opengles.util.Constant

open class Chapter271Activity : GLRenderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_chapter221)
        //初始化GLSurfaceView

        Constant.OBJ_VER_PATH = "chapter701/chapter701.1/vertex.glsl"
        Constant.OBJ_FRAG_PATH = "chapter701/chapter701.1/frag.glsl"

        setGLSurfaceView(GL271SurfaceView(this@Chapter271Activity))
    }

    override fun onKeyDown(keyCode:Int, e:KeyEvent):Boolean {
        if(keyCode==4) {
            System.exit(0);
        }
        return false;
    }
}



