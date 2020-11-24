package com.xingfeng.opengles.chapter5.chapter512;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;


public class Chapter512Activity extends GLRenderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL512SurfaceView(this));
    }

}
