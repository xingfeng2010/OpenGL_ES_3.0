package com.xingfeng.opengles.chapter20.chapter203;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;
import com.xingfeng.opengles.util.Constant;

public class Chapter203Activity extends GLRenderActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL203SurfaceView(this));

        Constant.OBJ_VER_PATH = "chapter201/chapter201.3/vertex.glsl";
        Constant.OBJ_FRAG_PATH = "chapter201/chapter201.3/frag.glsl";
    }
}
