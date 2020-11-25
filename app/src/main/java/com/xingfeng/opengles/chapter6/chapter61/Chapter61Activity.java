package com.xingfeng.opengles.chapter6.chapter61;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;

public class Chapter61Activity extends GLRenderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL61SurfaceView(this));
    }
}