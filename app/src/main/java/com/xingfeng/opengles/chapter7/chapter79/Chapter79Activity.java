package com.xingfeng.opengles.chapter7.chapter79;

import android.os.Bundle;

import com.xingfeng.opengles.GLRenderActivity;

public class Chapter79Activity extends GLRenderActivity {
    static boolean threadFlag;//纹理矩形绕X轴旋转工作标志位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL79SurfaceView(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        threadFlag=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        threadFlag=false;
    }
}
