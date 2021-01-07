package com.xingfeng.opengles.chapter22.chapter224;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;
import com.xingfeng.opengles.chapter20.chapter201.GL201SurfaceView;
import com.xingfeng.opengles.util.Constant;

public class Chapter224Activity extends GLRenderActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL201SurfaceView(this));

        Constant.OBJ_VER_PATH = "chapter202/chapter202.4/vertex.glsl";
        Constant.OBJ_FRAG_PATH = "chapter202/chapter202.4/frag.glsl";
    }
}
