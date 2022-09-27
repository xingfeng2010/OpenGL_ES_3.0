package com.xingfeng.opengles.chapter211.chapter2112;

import android.os.Bundle;
import android.view.KeyEvent;

import com.xingfeng.opengles.GLRenderActivity
import com.xingfeng.opengles.util.Constant

open class Chapter2112Activity : GLRenderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_chapter221)
        //初始化GLSurfaceView

        Constant.OBJ_VER_PATH = "chapter1101/chapter1101.2/vertex.glsl"
        Constant.OBJ_FRAG_PATH = "chapter1101/chapter1101.2/frag.glsl"

        setGLSurfaceView(GL2112SurfaceView(this@Chapter2112Activity))
    }

    override fun onKeyDown(keyCode:Int, e:KeyEvent):Boolean {
        if(keyCode==4) {
            System.exit(0);
        }
        return false;
    }
}



