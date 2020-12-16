package com.xingfeng.opengles.chapter7.chapter74;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.xingfeng.opengles.R;

public class Chapter74Activity extends Activity {
    private GL74SurfaceView mGLSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //切换到主界面
        setContentView(R.layout.activity_chapter74);
        //初始化GLSurfaceView
        mGLSurfaceView = new GL74SurfaceView(this);
        mGLSurfaceView.requestFocus();//获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控
        //将自定义的GLSurfaceView添加到外层LinearLayout中
        LinearLayout ll=(LinearLayout)findViewById(R.id.main_liner);
        ll.addView(mGLSurfaceView);

        //为RadioButton添加监听器及SxT选择代码
        RadioButton rab=(RadioButton)findViewById(R.id.Radi01);
        rab.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    //GL_CLAMP_TO_EDGE模式下
                    if(isChecked)
                    {
                        mGLSurfaceView.currenttexId32 = mGLSurfaceView.texId[0];
                        mGLSurfaceView.currenttexId256 = mGLSurfaceView.texId[4];
                    }
                }
        );
        rab=(RadioButton)findViewById(R.id.Radi02);
        rab.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    //GL_REPEAT模式下
                    if(isChecked)
                    {
                        mGLSurfaceView.currenttexId32 = mGLSurfaceView.texId[1];
                        mGLSurfaceView.currenttexId256 = mGLSurfaceView.texId[5];
                    }
                }
        );

        rab=(RadioButton)findViewById(R.id.Radi03);
        rab.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    //GL_REPEAT模式下
                    if(isChecked)
                    {
                        mGLSurfaceView.currenttexId32 = mGLSurfaceView.texId[2];
                        mGLSurfaceView.currenttexId256 = mGLSurfaceView.texId[6];
                    }
                }
        );

        //为RadioButton添加监听器及SxT选择代码
        RadioButton rb=(RadioButton)findViewById(R.id.Radi04);
        rb.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if(isChecked)
                    {//设置为纹理坐标SxT=1x1
                        mGLSurfaceView.currenttexId32 = mGLSurfaceView.texId[3];
                        mGLSurfaceView.currenttexId256 = mGLSurfaceView.texId[7];
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
