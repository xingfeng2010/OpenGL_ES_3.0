package com.xingfeng.opengles.chapter5.chapter515;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;


public class Chapter515Activity extends GLRenderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL515SurfaceView(this));
    }

}
