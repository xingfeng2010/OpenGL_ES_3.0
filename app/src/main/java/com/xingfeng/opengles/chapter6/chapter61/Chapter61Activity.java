package com.xingfeng.opengles.chapter6.chapter61;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;
import com.xingfeng.opengles.util.Constant;

public class Chapter61Activity extends GLRenderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL61SurfaceView(this));
        Constant.OBJ_VER_PATH = "chapter6/chapter6.1/vertex.glsl";
        Constant.OBJ_FRAG_PATH = "chapter6/chapter6.1/frag.glsl";
    }
}