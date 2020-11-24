package com.xingfeng.opengles.chapter5.chapter513;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.xingfeng.opengles.GLRenderActivity;
import com.xingfeng.opengles.R;
import com.xingfeng.opengles.util.Constant;
import com.xingfeng.opengles.util.MatrixState;


public class Chapter513Activity extends GLRenderActivity {

    private GL513SurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //初始化GLSurfaceView
        super.setGLSurfaceView(new GL513SurfaceView(this));

        //切换到主界面
        setContentView(R.layout.activity_chapter513);
        //初始化GLSurfaceView
        mGLSurfaceView = new GL513SurfaceView(this);
        mGLSurfaceView.requestFocus();//获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控
        //将自定义的GLSurfaceView添加到外层LinearLayout中
        LinearLayout ll = findViewById(R.id.main_liner);
        ll.addView(mGLSurfaceView);
        //控制是否打开背面剪裁的ToggleButton
        ToggleButton tb = this.findViewById(R.id.ToggleButton01);
        tb.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        //视角不合适导致变形的情况
                        //调用此方法计算产生透视投影矩阵
                        MatrixState.setProjectFrustum(-Constant.ratio * 0.7f, Constant.ratio * 0.7f, -0.7f, 0.7f, 1, 10);
                        //调用此方法产生摄像机观察矩阵
                        MatrixState.setCamera(0, 0.5f, 4, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
                    } else {
                        //视角合适不变形的情况
                        //调用此方法计算产生透视投影矩阵
                        MatrixState.setProjectFrustum(-Constant.ratio, Constant.ratio, -1, 1, 20, 100);
                        //调用此方法产生摄像机观察矩阵
                        MatrixState.setCamera(0, 8f, 45, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
                    }
                }
        );
    }

}
