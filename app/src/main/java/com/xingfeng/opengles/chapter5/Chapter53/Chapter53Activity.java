package com.xingfeng.opengles.chapter5.Chapter53;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;

public class Chapter53Activity extends GLRenderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL53SurfaceView(this));
    }
}
