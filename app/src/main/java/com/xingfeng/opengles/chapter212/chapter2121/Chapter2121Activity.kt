package com.xingfeng.opengles.chapter212.chapter2121;

import android.Manifest
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.core.app.ActivityCompat

import com.xingfeng.opengles.GLRenderActivity
import com.xingfeng.opengles.chapter211.chapter2111.GL2111SurfaceView
import com.xingfeng.opengles.util.Constant

open class Chapter2121Activity : GLRenderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_chapter221)
        //初始化GLSurfaceView

        Constant.OBJ_VER_PATH = "chapter1201/chapter1201.1/vertex.glsl"
        Constant.OBJ_FRAG_PATH = "chapter1201/chapter1201.1/frag.glsl"

        setGLSurfaceView(GL2121SurfaceView(this@Chapter2121Activity))

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            10)
    }

    override fun onKeyDown(keyCode: Int, e: KeyEvent): Boolean {
        if (keyCode == 4) {
            System.exit(0);
        }
        return false;
    }
}



