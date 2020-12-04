package com.xingfeng.opengles.chapter6.chapter65;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.xingfeng.opengles.GLRenderActivity;
import com.xingfeng.opengles.R;

public class Chapter65Activity extends GLRenderActivity {

    private GL65SurfaceView mGLSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置为横屏
        // 初始化GLSurfaceView
        mGLSurfaceView = new GL65SurfaceView(this);
        // 切换到主界面
        setContentView(R.layout.activity_chapter62);
        LinearLayout ll = (LinearLayout) findViewById(R.id.main_liner);
        ll.addView(mGLSurfaceView);
        //普通拖拉条被拉动的处理代码
        SeekBar sb=(SeekBar)this.findViewById(R.id.SeekBar01);
        sb.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                        mGLSurfaceView.setLightOffset((seekBar.getMax()/2.0f-progress)/(seekBar.getMax()/2.0f)*-4);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar){}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar){}
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