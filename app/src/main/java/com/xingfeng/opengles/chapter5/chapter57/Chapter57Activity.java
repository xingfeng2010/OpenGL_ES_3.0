package com.xingfeng.opengles.chapter5.chapter57;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;

public class Chapter57Activity extends GLRenderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL57SurfaceView(this));
    }
}
