package com.xingfeng.opengles.chapter22.chapter225;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;
import com.xingfeng.opengles.util.Constant;

public class Chapter225Activity extends GLRenderActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GameSurfaceView(this));

        Constant.OBJ_VER_PATH = "chapter202/chapter202.5/vertex.glsl";
        Constant.OBJ_FRAG_PATH = "chapter202/chapter202.5/frag.glsl";
    }
}
