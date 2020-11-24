package com.xingfeng.opengles.chapter5.chapter516;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.xingfeng.opengles.R;

import androidx.appcompat.app.AppCompatActivity;


public class Chapter516Activity extends AppCompatActivity {

    private GL516SurfaceView mGLSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为横屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 初始化GLSurfaceView
        mGLSurfaceView = new GL516SurfaceView(this);
        // 切换到主界面
        setContentView(R.layout.activity_chapter516);
        //将自定义的SurfaceView添加到外层LinearLayout中
        LinearLayout ll=(LinearLayout)findViewById(R.id.main_liner);
        ll.addView(mGLSurfaceView);
        mGLSurfaceView.requestFocus();// 获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);// 设置为可触控
        RadioButton rb = (RadioButton) findViewById(R.id.RadioButton01);

        // RadioButton添加监听器
        rb.setOnCheckedChangeListener((buttonView, isChecked) -> mGLSurfaceView.setCwOrCcw(isChecked));
        rb = (RadioButton) findViewById(R.id.RadioButton03);
        // RadioButton添加监听器
        rb.setOnCheckedChangeListener((buttonView, isChecked) -> mGLSurfaceView.setCullFace(isChecked));
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
