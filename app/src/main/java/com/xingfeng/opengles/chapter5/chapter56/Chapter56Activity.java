package com.xingfeng.opengles.chapter5.chapter56;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;
import com.xingfeng.opengles.chapter5.chapter53.GL53SurfaceView;

public class Chapter56Activity extends GLRenderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL56SurfaceView(this));
    }
}
