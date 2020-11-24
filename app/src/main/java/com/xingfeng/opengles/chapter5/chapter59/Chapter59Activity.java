package com.xingfeng.opengles.chapter5.chapter59;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;

public class Chapter59Activity extends GLRenderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL59SurfaceView(this));
    }
}
