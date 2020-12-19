package com.xingfeng.opengles.chapter10.chapter104;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.xingfeng.opengles.GLRenderActivity;
import com.xingfeng.opengles.R;
import com.xingfeng.opengles.chapter7.chapter71.GL71SurfaceView;

public class Chapter104Activity extends GLRenderActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL104SurfaceView(this));
    }
}
