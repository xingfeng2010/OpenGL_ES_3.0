package com.xingfeng.opengles.chapter22.chapter226;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;
import com.xingfeng.opengles.chapter22.chapter225.GameSurfaceView;
import com.xingfeng.opengles.util.Constant;

public class Chapter226Activity extends GLRenderActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL226SurfaceView(this));

        Constant.OBJ_VER_PATH = "chapter202/chapter202.6/vertex.glsl";
        Constant.OBJ_FRAG_PATH = "chapter202/chapter202.6/frag.glsl";
    }
}
