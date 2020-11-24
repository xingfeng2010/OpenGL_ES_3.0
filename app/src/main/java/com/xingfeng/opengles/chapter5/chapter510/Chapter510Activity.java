package com.xingfeng.opengles.chapter5.chapter510;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;

public class Chapter510Activity extends GLRenderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL510SurfaceView(this));
    }
}
