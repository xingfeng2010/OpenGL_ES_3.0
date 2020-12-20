package com.xingfeng.opengles.chapter10.chapter105;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;

public class Chapter105Activity extends GLRenderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL105SurfaceView(this));
    }
}
