package com.xingfeng.opengles.chapter23.chapter231;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.xingfeng.opengles.GLRenderActivity;
import com.xingfeng.opengles.util.Constant;

 public class Chapter231Activity extends GLRenderActivity {

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL231SurfaceView(this));
        Constant.UNIT_SIZE=1f;
        Constant.OBJ_VER_PATH = "chapter301/chapter301.1/vertex.glsl";
        Constant.OBJ_FRAG_PATH = "chapter301/chapter301.1/frag.glsl";
    }
 }